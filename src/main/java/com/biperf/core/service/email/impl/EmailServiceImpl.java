/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/email/impl/EmailServiceImpl.java,v $
 */

package com.biperf.core.service.email.impl;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Set;

import javax.mail.internet.MimeMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import com.biperf.core.domain.enums.EnvironmentTypeEnum;
import com.biperf.core.domain.mailing.MailingAttachmentInfo;
import com.biperf.core.domain.message.Message;
import com.biperf.core.domain.system.PropertySetItem;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.exception.ServiceErrorExceptionNoRollback;
import com.biperf.core.service.email.EmailBody;
import com.biperf.core.service.email.EmailCredentials;
import com.biperf.core.service.email.EmailHeader;
import com.biperf.core.service.email.EmailService;
import com.biperf.core.service.email.TextEmailBody;
import com.biperf.core.service.message.MessageService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.utils.CheckEmailServer;

/**
 * EmailServiceImpl
 * 
 *
 */
public class EmailServiceImpl implements EmailService
{
  private static final Log log = LogFactory.getLog( EmailServiceImpl.class );
  private SystemVariableService systemVariableService = null;
  private MessageService messageService;

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.email.EmailService#sendMessage(com.biperf.core.service.email.EmailHeader,
   *      com.biperf.core.service.email.EmailBody)
   * @param header
   * @param textBody
   * @throws ServiceErrorException
   */
  public void sendMessage( EmailHeader header, EmailBody textBody ) throws ServiceErrorException
  {
    sendMessage( header, textBody, null ); // plain text
  }

  public void sendMessage( EmailHeader header, EmailBody textBody, EmailBody htmlBody ) throws ServiceErrorExceptionNoRollback
  {
    sendMessage( header, textBody, htmlBody, null ); // html text without attachment
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.email.EmailService#sendMessage(com.biperf.core.service.email.EmailHeader,
   *      com.biperf.core.service.email.EmailBody, com.biperf.core.service.email.EmailBody)
   * @param header
   * @param textBody
   * @param htmlBody
   * @param mailingAttachmentInfos
   * @throws ServiceErrorExceptionNoRollback We don't want rollback to occur on mail failure so that
   *           we can continue db operations even on failure.
   */
  public void sendMessage( EmailHeader header, EmailBody textBody, EmailBody htmlBody, Set mailingAttachmentInfos ) throws ServiceErrorExceptionNoRollback
  {

    if ( !header.validate() )
    {
      log.error( "Email Header Validation Failed for:" + header.toString() );
      throw new ServiceErrorExceptionNoRollback( "Email Header Validation Failed for: " + header.toString() );
    }

    JavaMailSenderImpl mailSender = null;
    try
    {
      String smtpHost = CheckEmailServer.connectedTo( systemVariableService.getPropertyByName( SystemVariableService.EMAIL_SERVERS ).getStringVal() );
      mailSender = new JavaMailSenderImpl();
      mailSender.setHost( smtpHost );
      mailSender.setPort( buildSMTPPort() );
    }
    catch( IOException e )
    {
      log.error( "Unable to connect to smtp host" );
    }

    MimeMessage msg = mailSender.createMimeMessage();

    MimeMessageHelper msgHelper;
    if ( htmlBody == null && mailingAttachmentInfos == null )
    {
      msgHelper = new MimeMessageHelper( msg, "UTF-8" );
    }
    else
    {
      try
      {
        // Create a new MimeMessageHelper for the given MimeMessage, in multipart mode (supporting
        // alternative texts, inline elements and attachments)
        // using a specific multipart mode MULTIPART_MODE_MIXED_RELATED.
        msgHelper = new MimeMessageHelper( msg, true, "UTF-8" );
      }
      catch( Exception e )
      {
        log.error( "Error occurred while attempting to send message to recipient.", e );
        throw new ServiceErrorExceptionNoRollback( "Error creating multi-part MimeMessageHelper for e-mail", e );
      }
    }

    try
    {
      // set the message header

      msgHelper.setFrom( header.getSender(), header.getPersonal() );

      msgHelper.setTo( header.getRecipients() );
      if ( header.getReplyTo() != null )
      {
        msgHelper.setReplyTo( header.getReplyTo() );
      }
      String[] ccArray = header.getCcRecipients();
      if ( ccArray != null )
      {
        msgHelper.setCc( ccArray );
      }
      String[] bccArray = header.getBccRecipients();
      if ( bccArray != null )
      {
        msgHelper.setBcc( bccArray );
      }
      msgHelper.setSubject( header.getSubject() );

      if ( htmlBody == null )
      {
        msgHelper.setText( textBody.getBodyText() );
      }
      else
      {
        msgHelper.setText( textBody.getBodyText(), htmlBody.getBodyText() );
      }

      // validate the attachment file is Okay before adding to the msg
      // if ( mailAttachmentInfoValid( mailingAttachmentInfos ))
      if ( mailingAttachmentInfos != null && !mailingAttachmentInfos.isEmpty() )
      {
        Iterator iter = mailingAttachmentInfos.iterator();
        while ( iter.hasNext() )
        {
          MailingAttachmentInfo attachment = (MailingAttachmentInfo)iter.next();

          FileSystemResource file = new FileSystemResource( new File( attachment.getFullFileName() ) );

          // Include in the mail as an attachment
          msgHelper.addAttachment( attachment.getAttachmentFileName(), file );

          log.warn( "attachment added to mime message for file: " + attachment.getFullFileName() );
        }
      }
      // AWS installs require authentication for SMTP servers
      if ( EnvironmentTypeEnum.isAws() )
      {
        EmailCredentials credentials = getSMTPEmailCredentials();
        mailSender.setUsername( credentials.getUsername() );
        mailSender.setPassword( credentials.getPassword() );
      }
      // dispatch the message using JavaMailSender
      mailSender.send( msg );
    }
    catch( Exception e )
    {
      // We don't want rollback to occur on mail failure so that we can continue db operations even
      // on failure.
      log.error( header.toString(), e );
      throw new ServiceErrorExceptionNoRollback( header.toString(), e );
    }
  }

  private int buildSMTPPort()
  {
    PropertySetItem prop = systemVariableService.getPropertyByName( SystemVariableService.EMAIL_SERVERS_PORT );
    int port = -1;
    if ( null == prop || prop.getIntVal() == 0 )
    {
      port = 25;
    }
    else
    {
      port = prop.getIntVal();
    }
    if ( log.isDebugEnabled() )
    {
      log.debug( "Email server port: " + port );
    }
    return port;
  }

  /**
   * {@inheritDoc}
   */
  public void sendStageFailNotificationMessage()
  {
    Message message = messageService.getMessageByCMAssetCode( MessageService.PROCESS_FAILED_MESSAGE_CM_ASSET_CODE );
    String senderEmail = systemVariableService.getPropertyByName( SystemVariableService.SYSTEM_SENDER_EMAIL_ADDRESS ).getStringVal();
    String receiverEmail = systemVariableService.getPropertyByName( SystemVariableService.SYSTEM_EMAIL_ADDRESS ).getStringVal();

    EmailHeader header = new EmailHeader();
    header.setSubject( message != null ? message.getI18nSubject() : "" );
    header.setRecipients( new String[] { receiverEmail } );
    header.setSender( senderEmail );
    TextEmailBody body = new TextEmailBody( message != null ? message.getI18nHtmlBody() : "" );
    try
    {
      sendMessage( header, body );
    }
    catch( Exception e )
    {
      log.error( "An exception occurred while sending Summary Email." );
    }
  }

  /**
   * @param mailingAttachmentInfos
   * @return true if the mailingAttachmentInfos contains valid mailingAttachmentInfo
   */
  private boolean mailAttachmentInfoValid( Set mailingAttachmentInfos )
  {
    boolean valid = false;
    if ( mailingAttachmentInfos != null && !mailingAttachmentInfos.isEmpty() )
    {
      Iterator iter = mailingAttachmentInfos.iterator();
      while ( iter.hasNext() )
      {
        MailingAttachmentInfo attachment = (MailingAttachmentInfo)iter.next();
        String fullFileName = attachment.getFullFileName();

        // does the file exists?
        if ( fullFileName != null && fullFileName.length() > 0 )
        {
          boolean exists = new File( fullFileName ).exists();
          if ( !exists )
          {
            return false;
          }
        }
      }
      valid = true; // the entire set has valid attachment
    }
    return valid;
  }

  @Override
  public EmailCredentials getSMTPEmailCredentials()
  {
    String username = getSMTPHostUsername();
    String password = getSMTPHostPassword();
    if ( log.isDebugEnabled() )
    {
      log.debug( "SMTP credentials " + username + " and " + password );
    }
    return new EmailCredentials( username, password );
  }

  private String getSMTPHostUsername()
  {
    return systemVariableService.getPropertyByName( SystemVariableService.SMTP_HOST_USERNAME ).getStringVal();
  }

  private String getSMTPHostPassword()
  {
    String encryptedPassword = systemVariableService.getPropertyByName( SystemVariableService.SMPT_HOST_PASSWORD ).getStringVal();
    String decryptedPassword = systemVariableService.getAESDecryptedValue( encryptedPassword );
    if ( null == decryptedPassword )
    {
      log.error( "Unable to decrypt SMTP Password System variable: " + encryptedPassword );
    }
    return decryptedPassword;
  }

  public void setSystemVariableService( SystemVariableService systemVariableService )
  {
    this.systemVariableService = systemVariableService;
  }

  public void setMessageService( MessageService messageService )
  {
    this.messageService = messageService;
  }

}

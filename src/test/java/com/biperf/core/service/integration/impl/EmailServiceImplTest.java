/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javatest/com/biperf/core/service/integration/impl/EmailServiceImplTest.java,v $
 */

package com.biperf.core.service.integration.impl;

import java.util.Properties;

import javax.mail.Session;
import javax.mail.internet.MimeMessage;

import org.jmock.Mock;
import org.jmock.MockObjectTestCase;
import org.springframework.mail.javamail.JavaMailSender;

import com.biperf.core.domain.system.PropertySetItem;
import com.biperf.core.exception.ServiceErrorExceptionNoRollback;
import com.biperf.core.service.email.EmailHeader;
import com.biperf.core.service.email.TextEmailBody;
import com.biperf.core.service.email.impl.EmailServiceImpl;
import com.biperf.core.service.system.SystemVariableService;

import junit.framework.TestCase;

/**
 * EmailServiceImplTest.
 * <p>
 * <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>crosenquest</td>
 * <td>Apr 22, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class EmailServiceImplTest extends MockObjectTestCase
{
  private String[] recipients = new String[2];
  private String[] ccRecipients = new String[2];
  private String[] bccRecipients = new String[2];

  private EmailServiceImpl emailService = new EmailServiceImpl();
  private Mock mockJavaMailSender = null;
  private Mock mockSystemVariableService = null;

  /**
   * Setup the data for tests
   * 
   * @see TestCase#setUp()
   */
  protected void setUp() throws Exception
  {
    recipients[0] = "recipient1@biworldwide.com";
    recipients[1] = "recipient2@biworldwide.com";

    ccRecipients[0] = "ccrecipient1@biworldwide.com";
    ccRecipients[1] = "ccrrecipient2@biworldwide.com";

    bccRecipients[0] = "bccrecipient1@biworldwide.com";
    bccRecipients[1] = "bccrrecipient2@biworldwide.com";

    mockJavaMailSender = new Mock( JavaMailSender.class );
    mockSystemVariableService = new Mock( SystemVariableService.class );
    emailService.setSystemVariableService( (SystemVariableService)mockSystemVariableService.proxy() );

  }

  /**
   * Test when all correct and necessary parameters are present for sending email
   */
  public void testSendHappyPath() throws Exception
  {
    mockJavaMailSender.reset();
    EmailHeader header = new EmailHeader();

    header.setSender( "test@biworldwide.com" );

    header.setRecipients( recipients );
    header.setCcRecipients( ccRecipients );
    header.setBccRecipients( bccRecipients );

    header.setSubject( "SvcTestSubject" );

    TextEmailBody textEmailBody = new TextEmailBody( "This is a test email for X." );

    PropertySetItem systemVariableDaysToExpire = new PropertySetItem();
    systemVariableDaysToExpire.setStringVal( "strongmail2.biperf.com,strongmail3.biperf.com" );
    systemVariableDaysToExpire.setKey( SystemVariableService.EMAIL_SERVERS );
    systemVariableDaysToExpire.setEntityName( SystemVariableService.EMAIL_SERVERS );

    mockSystemVariableService.expects( once() ).method( "getPropertyByName" ).with( same( SystemVariableService.EMAIL_SERVERS ) ).will( returnValue( systemVariableDaysToExpire ) );

    final PropertySetItem emailPort = new PropertySetItem();
    emailPort.setIntVal( 25 );
    emailPort.setKey( SystemVariableService.EMAIL_SERVERS_PORT );
    emailPort.setEntityName( SystemVariableService.EMAIL_SERVERS_PORT );
    mockSystemVariableService.expects( once() ).method( "getPropertyByName" ).with( same( SystemVariableService.EMAIL_SERVERS_PORT ) ).will( returnValue( emailPort ) );

    MimeMessage mimeMessage = new MimeMessage( Session.getInstance( new Properties() ) );

    emailService.sendMessage( header, textEmailBody );

    mockJavaMailSender.verify();
  }

  /**
   * Test when sender is not provided i.e., no from email address available
   * 
   * @throws Exception
   */
  public void testSendWithoutSender() throws Exception
  {
    mockJavaMailSender.reset();
    try
    {
      EmailHeader header = new EmailHeader();
      // the sender
      // header.setSender("test@biworldwide.com");
      header.setRecipients( recipients );
      header.setCcRecipients( ccRecipients );
      header.setBccRecipients( bccRecipients );
      header.setSubject( "SvcTestSubject" );
      TextEmailBody textEmailBody = new TextEmailBody( "This is a test email" );
      emailService.sendMessage( header, textEmailBody );
      fail( "The service sent message without sender" );
    }
    catch( ServiceErrorExceptionNoRollback e )
    {
      // everything OK
    }
  }

  /**
   * Test when sender email address is in incorrect format/syntax
   * 
   * @throws Exception
   */
  public void testSendWithIncorrectSenderEmail() throws Exception
  {
    mockJavaMailSender.reset();
    try
    {
      EmailHeader header = new EmailHeader();
      // the sender
      header.setSender( "test@biworldwide" ); // incorrect email syntax
      header.setRecipients( recipients );
      header.setCcRecipients( ccRecipients );
      header.setBccRecipients( bccRecipients );
      header.setSubject( "SvcTestSubject" );
      TextEmailBody textEmailBody = new TextEmailBody( "This is a test email" );
      emailService.sendMessage( header, textEmailBody );
      fail( "The service sent message with incorrect sender email addr syntax" );
    }
    catch( ServiceErrorExceptionNoRollback e )
    {
      // everything OK
    }
  }

  /**
   * Test when subject is missing
   * 
   * @throws Exception
   */
  public void testSendWithoutSubject() throws Exception
  {
    try
    {
      EmailHeader header = new EmailHeader();
      // the sender
      header.setSender( "test@biworldwide.com" );
      header.setRecipients( recipients );
      header.setCcRecipients( ccRecipients );
      header.setBccRecipients( bccRecipients );
      header.setSubject( null );
      TextEmailBody textEmailBody = new TextEmailBody( "This is a test email" );
      emailService.sendMessage( header, textEmailBody );
      fail( "The service sent message with incorrect sender email addr syntax" );
    }
    catch( ServiceErrorExceptionNoRollback e )
    {
      // everything OK
    }
  }
}

/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/message/MessageForm.java,v $
 */

package com.biperf.core.ui.message;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import com.biperf.core.domain.enums.MessageModuleType;
import com.biperf.core.domain.enums.MessageSMSGroupType;
import com.biperf.core.domain.enums.MessageStatusType;
import com.biperf.core.domain.enums.MessageType;
import com.biperf.core.domain.message.Message;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.ui.BaseForm;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.MessageUtils;
import com.objectpartners.cms.util.CmsResourceBundle;

/**
 * MessageForm.
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
 * <td>robinsra</td>
 * <td>Sep 2, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class MessageForm extends BaseForm
{
  private String name;
  private String moduleCode;
  private String messageType;
  private String statusCode;
  private String dateLastSent;
  private String cmAssetCode;
  private String subject;
  private String strongMailSubject;
  private String strongMailMsg;
  private String htmlMsg;
  private String plainTextMsg;
  private String textMsg;
  private String translatedLocale;
  private String translatedSubject;
  private String translatedStrongMailSubject;
  private String translatedHtmlMsg;
  private String translatedPlainTextMsg;
  private String translatedTextMsg;
  private String translatedStrongMailMsg;
  private Map translatedSubjects = new HashMap();
  private Map translatedStrongMailSubjects = new HashMap();
  private Map translatedStrongMailMsgs = new HashMap();
  private Map translatedHtmlMsgs = new HashMap();
  private Map translatedPlainTextMsgs = new HashMap();
  private Map translatedTextMsgs = new HashMap();
  private String insertField;

  private long messageId;
  private long version;
  private long dateCreated;
  private String createdBy;
  private String method;

  private String messageSMSGroup;

  /**
   * Validate the properties that have been set from this HTTP request, and return an
   * <code>ActionErrors</code> object that encapsulates any validation errors that have been
   * found. If no errors are found, return <code>null</code> or an <code>ActionErrors</code>
   * object with no recorded error messages. Overridden from
   * 
   * @see org.apache.struts.action.ActionForm#validate(org.apache.struts.action.ActionMapping,
   *      javax.servlet.http.HttpServletRequest)
   * @param anActionMapping
   * @param aRequest
   * @return ActionErrors
   */
  public ActionErrors validate( ActionMapping anActionMapping, HttpServletRequest aRequest )
  {

    ActionErrors actionErrors = super.validate( anActionMapping, aRequest );
    if ( actionErrors == null )
    {
      actionErrors = new ActionErrors();
    }

    htmlMsg = nullCheckAndTrim( htmlMsg );
    plainTextMsg = nullCheckAndTrim( plainTextMsg );
    textMsg = nullCheckAndTrim( textMsg );
    strongMailSubject = nullCheckAndTrim( strongMailSubject );
    strongMailMsg = nullCheckAndTrim( strongMailMsg );
    translatedHtmlMsg = nullCheckAndTrim( translatedHtmlMsg );
    translatedPlainTextMsg = nullCheckAndTrim( translatedPlainTextMsg );
    translatedTextMsg = nullCheckAndTrim( translatedTextMsg );
    translatedStrongMailMsg = nullCheckAndTrim( translatedStrongMailMsg );

    if ( htmlMsg.equals( "" ) && plainTextMsg.equals( "" ) && textMsg.equals( "" ) && strongMailMsg.equals( "" ) )
    {
      actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( ServiceErrorMessageKeys.MESSAGE_AT_LEAST_ONE_REQUIRED ) );
    }
    else
    {
      // Handle personalization url links so absolute url is stripped (was inserted by tinymce)
      htmlMsg = MessageUtils.removeUrlFromPersonalizedToken( htmlMsg );
      translatedHtmlMsg = MessageUtils.removeUrlFromPersonalizedToken( translatedHtmlMsg );
    }

    if ( Arrays.asList( Message.STRONGMAIL_MESSAGE_ARRAY ).contains( cmAssetCode ) )
    {
      if ( strongMailSubject.equals( "" ) )
      {
        actionErrors.add( "strongMailSubject", new ActionMessage( "system.errors.REQUIRED", CmsResourceBundle.getCmsBundle().getString( "admin.message.details", "STRONGMAIL_SUBJECT" ) ) );
      }
      if ( strongMailMsg.equals( "" ) )
      {
        actionErrors.add( "strongMailMsg", new ActionMessage( "system.errors.REQUIRED", CmsResourceBundle.getCmsBundle().getString( "admin.message.details", "STRONGMAIL_MSG" ) ) );
      }
    }

    return actionErrors;
  } // end validate

  private String nullCheckAndTrim( String checkString )
  {
    if ( checkString == null )
    {
      checkString = "";
    }
    else
    {
      checkString = checkString.trim();
    }
    return checkString;
  }

  /**
   * Load the Message to the form
   * 
   * @param message
   */
  public void load( Message message )
  {
    this.name = message.getName();
    this.moduleCode = message.getModuleCode().getCode();
    this.messageType = message.getMessageTypeCode().getCode();
    this.statusCode = message.getStatusCode().getCode();
    this.dateLastSent = DateUtils.toDisplayString( message.getDateLastSent() );
    this.cmAssetCode = message.getCmAssetCode();
    this.subject = message.getI18nSubject();
    this.htmlMsg = message.getI18nHtmlBody();
    this.plainTextMsg = message.getI18nPlainTextBody();
    this.textMsg = message.getI18nTextBody();
    this.strongMailSubject = message.getI18nStrongMailSubject();
    this.strongMailMsg = message.getI18nStrongMailBody();

    htmlMsg = nullCheckAndTrim( htmlMsg );
    plainTextMsg = nullCheckAndTrim( plainTextMsg );
    textMsg = nullCheckAndTrim( textMsg );

    this.messageId = message.getId().longValue();
    this.createdBy = message.getAuditCreateInfo().getCreatedBy().toString();
    this.dateCreated = message.getAuditCreateInfo().getDateCreated().getTime();
    this.version = message.getVersion().longValue();

    if ( message.getMessageSMSGroupType() != null && message.getMessageSMSGroupType().getCode() != null && !message.getMessageSMSGroupType().getCode().isEmpty() )
    {
      this.messageSMSGroup = message.getMessageSMSGroupType().getCode();
    }

  } // end load

  /**
   * Builds a full domain object from the form.
   * 
   * @return Message
   */
  public Message toFullDomainObject()
  {
    Message message = new Message();
    message.setName( this.name );
    message.setModuleCode( MessageModuleType.lookup( this.moduleCode ) );
    message.setMessageTypeCode( MessageType.lookup( this.messageType ) );
    message.setStatusCode( MessageStatusType.lookup( this.statusCode ) );
    message.setDateLastSent( DateUtils.toDate( this.dateLastSent ) );
    message.setCmAssetCode( this.getCmAssetCode() );
    message.setId( new Long( this.getMessageId() ) );
    message.setVersion( new Long( this.version ) );
    message.getAuditCreateInfo().setCreatedBy( Long.valueOf( this.createdBy ) );
    message.getAuditCreateInfo().setDateCreated( new Timestamp( this.dateCreated ) );
    if ( this.messageSMSGroup != null && !this.messageSMSGroup.equals( "" ) )
    {
      message.setMessageSMSGroupType( MessageSMSGroupType.lookup( this.messageSMSGroup ) );
    }
    return message;
  } // end toFullDomainObject

  /**
   * Builds a domain object from the form.
   * 
   * @return Message
   */
  public Message toInsertedDomainObject()
  {
    Message message = new Message();

    message.setName( this.name );
    message.setModuleCode( MessageModuleType.lookup( this.moduleCode ) );
    message.setMessageTypeCode( MessageType.lookup( this.messageType ) );
    message.setStatusCode( MessageStatusType.lookup( this.statusCode ) );
    message.setDateLastSent( DateUtils.toDate( this.dateLastSent ) );
    message.setCmAssetCode( this.getCmAssetCode() );
    if ( this.messageSMSGroup != null && !this.messageSMSGroup.equals( "" ) )
    {
      message.setMessageSMSGroupType( MessageSMSGroupType.lookup( this.messageSMSGroup ) );
    }

    return message;
  } // end toInsertedDomainObject

  /**
   * Get CM Asset Code.
   * 
   * @return String
   */
  public String getCmAssetCode()
  {
    return cmAssetCode;
  }

  /**
   * Set CM Asset Code.
   * 
   * @param cmAssetCode
   */
  public void setCmAssetCode( String cmAssetCode )
  {
    this.cmAssetCode = cmAssetCode;
  }

  /**
   * Get Created By User ID
   * 
   * @return String
   */
  public String getCreatedBy()
  {
    return createdBy;
  }

  /**
   * Set Message Created By User ID
   * 
   * @param createdBy
   */
  public void setCreatedBy( String createdBy )
  {
    this.createdBy = createdBy;
  }

  /**
   * Get Date Message Created
   * 
   * @return long
   */
  public long getDateCreated()
  {
    return dateCreated;
  }

  /**
   * Set Date Message Created
   * 
   * @param dateCreated
   */
  public void setDateCreated( long dateCreated )
  {
    this.dateCreated = dateCreated;
  }

  /**
   * Get Date Message Last Sent
   * 
   * @return String
   */
  public String getDateLastSent()
  {
    return dateLastSent;
  }

  /**
   * Set Date Message Last Sent
   * 
   * @param dateLastSent
   */
  public void setDateLastSent( String dateLastSent )
  {
    this.dateLastSent = dateLastSent;
  }

  /**
   * Get HTML Message
   * 
   * @return String
   */
  public String getHtmlMsg()
  {
    return htmlMsg;
  }

  /**
   * Set HTML Message
   * 
   * @param htmlMsg
   */
  public void setHtmlMsg( String htmlMsg )
  {
    this.htmlMsg = htmlMsg;
  }

  /**
   * Get Message ID (Database PK)
   * 
   * @return long
   */
  public long getMessageId()
  {
    return messageId;
  }

  /**
   * Set Message ID (Database PK)
   * 
   * @param messageId
   */
  public void setMessageId( long messageId )
  {
    this.messageId = messageId;
  }

  /**
   * Get Module Code. Code from pick list. E.G. prd" for "Product Claim"
   * 
   * @return String
   */
  public String getModuleCode()
  {
    return moduleCode;
  }

  /**
   * Set Module Code. Code from pick list. E.G. prd" for "Product Claim"
   * 
   * @param moduleCode
   */
  public void setModuleCode( String moduleCode )
  {
    this.moduleCode = moduleCode;
  }

  /**
   * Get Message Type Code. Code from pick list. E.G. "general" for "General"
   * 
   * @return String
   */
  public String getMessageTypeCode()
  {
    return messageType;
  }

  /**
   * Set Message Type Code. Code from pick list. E.G. "general" for "General"
   * 
   * @param moduleCode
   */
  public void setMessageTypeCode( String messageType )
  {
    this.messageType = messageType;
  }

  /**
   * Get Message Name
   * 
   * @return String
   */
  public String getName()
  {
    return name;
  }

  /**
   * Set Message Name
   * 
   * @param name
   */
  public void setName( String name )
  {
    this.name = name;
  }

  /**
   * Get Plain Text Message
   * 
   * @return String
   */
  public String getPlainTextMsg()
  {
    return plainTextMsg;
  }

  /**
   * Set Plain Text Message
   * 
   * @param plainTextMsg
   */
  public void setPlainTextMsg( String plainTextMsg )
  {
    this.plainTextMsg = plainTextMsg;
  }

  /**
   * Get Message Status. Code from pick list. E.G. "act" for active
   * 
   * @return String
   */
  public String getStatusCode()
  {
    return statusCode;
  }

  /**
   * Set Message Status. Code from pick list. E.G. "act" for active
   * 
   * @param statusCode
   */
  public void setStatusCode( String statusCode )
  {
    this.statusCode = statusCode;
  }

  /**
   * Get Message Subject line
   * 
   * @return String
   */
  public String getSubject()
  {
    return subject;
  }

  /**
   * Set Message Subject line
   * 
   * @param subject
   */
  public void setSubject( String subject )
  {
    this.subject = subject;
  }

  /**
   * Get Text Message
   * 
   * @return String
   */
  public String getTextMsg()
  {
    return textMsg;
  }

  /**
   * Set Text Messsage
   * 
   * @param textMsg
   */
  public void setTextMsg( String textMsg )
  {
    this.textMsg = textMsg;
  }

  /**
   * Get version number of the message. Used by Hibernate
   * 
   * @return long
   */
  public long getVersion()
  {
    return version;
  }

  /**
   * Set version number of the message. Used by Hibernate
   * 
   * @param version
   */
  public void setVersion( long version )
  {
    this.version = version;
  }

  /**
   * Get the Action Method for this form
   * 
   * @return String
   */
  public String getMethod()
  {
    return method;
  }

  /**
   * Set the Action Method for this form
   * 
   * @param method
   */
  public void setMethod( String method )
  {
    this.method = method;
  }

  /**
   * Get the insert field
   * 
   * @return String
   */
  public String getInsertField()
  {
    return insertField;
  }

  /**
   * Set the insert field
   * 
   * @param insertField
   */
  public void setInsertField( String insertField )
  {
    this.insertField = insertField;
  }

  public String getMessageSMSGroup()
  {
    return messageSMSGroup;
  }

  public void setMessageSMSGroup( String messageSMSGroup )
  {
    this.messageSMSGroup = messageSMSGroup;
  }

  public String getTranslatedLocale()
  {
    return translatedLocale;
  }

  public void setTranslatedLocale( String translatedLocale )
  {
    this.translatedLocale = translatedLocale;
  }

  public String getTranslatedSubject()
  {
    return translatedSubject;
  }

  public void setTranslatedSubject( String translatedSubject )
  {
    this.translatedSubject = translatedSubject;
  }

  public String getTranslatedHtmlMsg()
  {
    return translatedHtmlMsg;
  }

  public void setTranslatedHtmlMsg( String translatedHtmlMsg )
  {
    this.translatedHtmlMsg = translatedHtmlMsg;
  }

  public String getTranslatedPlainTextMsg()
  {
    return translatedPlainTextMsg;
  }

  public void setTranslatedPlainTextMsg( String translatedPlainTextMsg )
  {
    this.translatedPlainTextMsg = translatedPlainTextMsg;
  }

  public String getTranslatedTextMsg()
  {
    return translatedTextMsg;
  }

  public void setTranslatedTextMsg( String translatedTextMsg )
  {
    this.translatedTextMsg = translatedTextMsg;
  }

  public Map getTranslatedSubjects()
  {
    return translatedSubjects;
  }

  public void setTranslatedSubjects( Map translatedSubjects )
  {
    this.translatedSubjects = translatedSubjects;
  }

  public Map getTranslatedHtmlMsgs()
  {
    return translatedHtmlMsgs;
  }

  public void setTranslatedHtmlMsgs( Map translatedHtmlMsgs )
  {
    this.translatedHtmlMsgs = translatedHtmlMsgs;
  }

  public Map getTranslatedPlainTextMsgs()
  {
    return translatedPlainTextMsgs;
  }

  public void setTranslatedPlainTextMsgs( Map translatedPlainTextMsgs )
  {
    this.translatedPlainTextMsgs = translatedPlainTextMsgs;
  }

  public Map getTranslatedTextMsgs()
  {
    return translatedTextMsgs;
  }

  public void setTranslatedTextMsgs( Map translatedTextMsgs )
  {
    this.translatedTextMsgs = translatedTextMsgs;
  }

  public static String getJsString( String input )
  {
    String output = null;
    if ( input != null )
    {
      output = input.replace( "\'", "\\'" );
      output = output.replace( "\"", "\\\"" );
      output = output.replace( "\r", "\\r" );
      output = output.replace( "\n", "\\n" );
    }
    return output;
  }

  public String getStrongMailSubject()
  {
    return strongMailSubject;
  }

  public void setStrongMailSubject( String strongMailSubject )
  {
    this.strongMailSubject = strongMailSubject;
  }

  public String getStrongMailMsg()
  {
    return strongMailMsg;
  }

  public void setStrongMailMsg( String strongMailMsg )
  {
    this.strongMailMsg = strongMailMsg;
  }

  public String getTranslatedStrongMailSubject()
  {
    return translatedStrongMailSubject;
  }

  public void setTranslatedStrongMailSubject( String translatedStrongMailSubject )
  {
    this.translatedStrongMailSubject = translatedStrongMailSubject;
  }

  public String getTranslatedStrongMailMsg()
  {
    return translatedStrongMailMsg;
  }

  public void setTranslatedStrongMailMsg( String translatedStrongMailMsg )
  {
    this.translatedStrongMailMsg = translatedStrongMailMsg;
  }

  public Map getTranslatedStrongMailSubjects()
  {
    return translatedStrongMailSubjects;
  }

  public void setTranslatedStrongMailSubjects( Map translatedStrongMailSubjects )
  {
    this.translatedStrongMailSubjects = translatedStrongMailSubjects;
  }

  public Map getTranslatedStrongMailMsgs()
  {
    return translatedStrongMailMsgs;
  }

  public void setTranslatedStrongMailMsgs( Map translatedStrongMailMsgs )
  {
    this.translatedStrongMailMsgs = translatedStrongMailMsgs;
  }

} // end class MessageForm

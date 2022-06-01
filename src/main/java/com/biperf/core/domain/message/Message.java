
package com.biperf.core.domain.message;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.biperf.core.domain.BaseDomain;
import com.biperf.core.domain.enums.MessageModuleType;
import com.biperf.core.domain.enums.MessageSMSGroupType;
import com.biperf.core.domain.enums.MessageStatusType;
import com.biperf.core.domain.enums.MessageType;
import com.biperf.core.service.cms.CMAssetService;
import com.biperf.core.service.message.MessageService;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.MessageUtils;
import com.objectpartners.cms.domain.Content;
import com.objectpartners.cms.domain.Translations;
import com.objectpartners.cms.service.ContentReader;
import com.objectpartners.cms.util.CmsUtil;
import com.objectpartners.cms.util.ContentReaderManager;

/**
 * Message domain object which represents a message within the Beacon application.
 * 
 *
 */
public class Message extends BaseDomain
{
  /**
   * CM Section Code
   */
  public static final String CM_SECTION_CODE = "message_data";

  /**
   * CM Asset Type Name
   */
  public static final String CM_ASSET_TYPE_NAME = "_Message";

  /**
   * CM Asset Code Prefix
   */
  public static final String CM_ASSET_CODE_PREFIX = CM_SECTION_CODE + ".message";

  /**
   * CM Asset Name Suffix
   */
  public static final String CM_ASSET_NAME_SUFFIX = " Message";

  /**
   * CM Key for Subject
   */
  public static final String CM_KEY_SUBJECT = "SUBJECT";

  /**
   * CM Key for StrongMail Subject
   */
  public static final String CM_KEY_SM_SUBJECT = "STRONGMAIL_SUBJECT";

  /**
   * CM Asset Type Item Name for Subject
   */
  public static final String CM_ASSET_TYPE_ITEM_NAME_SUBJECT = "Subject";

  /**
   * CM Asset Type Item Name for StrongMail Subject
   */
  public static final String CM_ASSET_TYPE_ITEM_NAME_SM_SUBJECT = "StorngMail Subject";

  /**
   * CM Key for HTML Mesage
   */
  public static final String CM_KEY_HTML_MSG = "HTML_MSG";

  /**
   * CM Asset Type Item Name for HTML Message
   */
  public static final String CM_ASSET_TYPE_ITEM_NAME_HTML_MSG = "HTML Message";

  /**
   * CM Key for Plain Text Message
   */
  public static final String CM_KEY_PLAIN_TEXT_MSG = "PLAIN_TEXT_MSG";

  /**
   * CM Key for StrongMail Message
   */
  public static final String CM_KEY_SM_MSG = "STRONGMAIL_MSG";

  /**
   * CM Asset Type Item Name for Plain Text Message
   */
  public static final String CM_ASSET_TYPE_ITEM_NAME_PLAIN_TEXT_MSG = "Plain Text Message";

  /**
   * CM Key for Text Message
   */
  public static final String CM_KEY_TEXT_MSG = "TEXT_MSG";

  /**
   * CM Asset Type Itemm Name for Text Message
   */
  public static final String CM_ASSET_TYPE_ITEM_NAME_TEXT_MSG = "Text Message";

  /**
   * CM Asset Type Item Name for StrongMail Message
   */
  public static final String CM_ASSET_TYPE_ITEM_NAME_SM_MSG = "StrongMail Message";

  public static final String[] STRONGMAIL_MESSAGE_ARRAY = { MessageService.WELCOME_EMAIL_MESSAGE_CM_ASSET_CODE };

  // ---------------------------------------------------------------------------
  // Fields
  // ---------------------------------------------------------------------------
  private String name;
  private MessageModuleType moduleCode;
  private MessageStatusType statusCode;
  private MessageType messageTypeCode;
  private MessageSMSGroupType messageSMSGroupType;
  private Date dateLastSent;
  private String cmAssetCode;

  // ---------------------------------------------------------------------------
  // Equals, Hashcode, and To String Methods
  // ---------------------------------------------------------------------------

  /**
   * Checks equality of the object parameter to this. Overridden from
   * 
   * @see com.biperf.core.domain.BaseDomain#equals(java.lang.Object)
   * @param object
   * @return boolean
   */
  public boolean equals( Object object )
  {
    if ( this == object )
    {
      return true;
    }

    if ( ! ( object instanceof Message ) )
    {
      return false;
    }

    final Message message = (Message)object;

    if ( this.getName() != null ? !this.getName().equals( message.getName() ) : message.getName() != null )
    {
      return false;
    }
    return true;
  }

  /**
   * Define the hashCode from the id. Overridden from
   * 
   * @see com.biperf.core.domain.BaseDomain#hashCode()
   * @return int
   */
  public int hashCode()
  {
    return getName() != null ? getName().hashCode() : 0;
  }

  /**
   * Builds a String representation of this class. Overridden from
   * 
   * @see java.lang.Object#toString()
   * @return String
   */

  public String toString()
  {
    final StringBuffer buf = new StringBuffer();
    buf.append( "Message [" );
    buf.append( "{id=" ).append( super.getId() ).append( "}, " );
    buf.append( "{name=" ).append( this.getName() ).append( "}, " );
    buf.append( "{moduleType=" ).append( this.getModuleCode() ).append( "}, " );
    buf.append( "{status=" ).append( this.getStatusCode() ).append( "}" );
    buf.append( "{dateLastSent=" ).append( this.getDateLastSent() ).append( "}" );
    buf.append( "{cmAssetCode=" ).append( this.getCmAssetCode() ).append( "}, " );
    buf.append( "]" );
    return buf.toString();
  }

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
   * Get Date Message Last Sent
   * 
   * @return String
   */
  public Date getDateLastSent()
  {
    return dateLastSent;
  }

  /**
   * Set Date Message Last Sent
   * 
   * @param dateLastSent
   */
  public void setDateLastSent( Date dateLastSent )
  {
    this.dateLastSent = dateLastSent;
  }

  /**
   * Get Module Code. Code from pick list. E.G. prd" for "Product Claim"
   * 
   * @return String
   */
  public MessageModuleType getModuleCode()
  {
    return moduleCode;
  }

  /**
   * Set Module Code. Code from pick list. E.G. prd" for "Product Claim"
   * 
   * @param moduleCode
   */
  public void setModuleCode( MessageModuleType moduleCode )
  {
    this.moduleCode = moduleCode;
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
   * Get Message Status. Code from pick list. E.G. "act" for active
   * 
   * @return String
   */
  public MessageStatusType getStatusCode()
  {
    return statusCode;
  }

  /**
   * Set Message Status. Code from pick list. E.G. "act" for active
   * 
   * @param statusCode
   */
  public void setStatusCode( MessageStatusType statusCode )
  {
    this.statusCode = statusCode;
  }

  /**
   * Get Message Type. Code from pick list. E.G. "promotion_launch" for Promotion Launch Notification Type
   * 
   * @return MessageType
   */
  public MessageType getMessageTypeCode()
  {
    return messageTypeCode;
  }

  /**
   * Set Message Type. Code from pick list. E.G. "promotion_launch" for Promotion Launch Notification Type
   * 
   * @param messageTypeCode
   */
  public void setMessageTypeCode( MessageType messageTypeCode )
  {
    this.messageTypeCode = messageTypeCode;
  }

  public MessageSMSGroupType getMessageSMSGroupType()
  {
    return messageSMSGroupType;
  }

  public void setMessageSMSGroupType( MessageSMSGroupType messageSMSGroupType )
  {
    this.messageSMSGroupType = messageSMSGroupType;
  }

  /**
   * Get I18N Subject
   * 
   * @return subject
   */
  public String getI18nSubject()
  {
    return this.getMessageContent( getCmAssetCode(), CM_KEY_SUBJECT );
  }

  /**
   * Get I18N StrongMail Subject
   * 
   * @return StrongMail Subject
   */
  public String getI18nStrongMailSubject()
  {
    return this.getMessageContent( getCmAssetCode(), CM_KEY_SM_SUBJECT );
  }

  /**
   * Get I18N Html Body
   * 
   * @return html body
   */
  public String getI18nHtmlBody()
  {

    return this.getMessageContent( getCmAssetCode(), CM_KEY_HTML_MSG );
  }

  /**
   * Get I18N StrongMail Body
   * 
   * @return StrongMail body
   */
  public String getI18nStrongMailBody()
  {

    return this.getMessageContent( getCmAssetCode(), CM_KEY_SM_MSG );
  }

  /**
   * Get I18N Text Body
   * 
   * @return text body
   */
  public String getI18nTextBody()
  {

    return this.getMessageContent( getCmAssetCode(), CM_KEY_TEXT_MSG );
  }

  /**
   * Get I18N Plain Text Body
   * 
   * @return plain text body
   */
  public String getI18nPlainTextBody()
  {

    return this.getMessageContent( getCmAssetCode(), CM_KEY_PLAIN_TEXT_MSG );
  }

  /**
   * Get I18N Subject given locale
   * 
   * @param localeCode
   * @return subject
   */
  public String getI18nSubject( String localeCode )
  {

    return this.getMessageContent( localeCode, getCmAssetCode(), CM_KEY_SUBJECT );
  }

  /**
   * Get I18N Subject for StrongMail
   * 
   * @return subject
   */
  public String getI18nStrongMailSubject( String localeCode )
  {
    return this.getMessageContent( localeCode, getCmAssetCode(), CM_KEY_SM_SUBJECT );
  }

  /**
   * Get I18N Html Body given locale
   * 
   * @param localeCode
   * @return html body
   */
  public String getI18nHtmlBody( String localeCode )
  {

    return this.getMessageContent( localeCode, getCmAssetCode(), CM_KEY_HTML_MSG );
  }

  /**
   * Get I18N Text Body given locale
   * 
   * @param localeCode
   */
  public String getI18nTextBody( String localeCode )
  {

    return this.getMessageContent( localeCode, getCmAssetCode(), CM_KEY_TEXT_MSG );
  }

  /**
   * Get I18N StrongMail Body
   * 
   * @return StrongMail body
   */
  public String getI18nStrongMailBody( String localeCode )
  {

    return this.getMessageContent( localeCode, getCmAssetCode(), CM_KEY_SM_MSG );
  }

  /**
   * Get I18N Plain Text Body given locale
   * 
   * @return plain text body
   */
  public String getI18nPlainTextBody( String localeCode )
  {

    return this.getMessageContent( localeCode, getCmAssetCode(), CM_KEY_PLAIN_TEXT_MSG );
  }

  /**
   * Get message content from content manager contentreader using assetcode and key
   * 
   * @param cmAssetCode
   * @param key
   * @return String
   */
  private String getMessageContent( String cmAssetCode, String key )
  {
    String messageContent = ContentReaderManager.getText( getCmAssetCode(), key );
    return messageContent.equalsIgnoreCase( "???" + cmAssetCode + "." + key + "???" ) ? "" : messageContent.trim();
  }

  /**
   * Get message content from content manager contentreader using assetcode and key
   * 
   * @param localeCode
   * @param cmAssetCode
   * @param key
   * @return String
   */
  private String getMessageContent( String localeCode, String cmAssetCode, String key )
  {
    Locale locale = CmsUtil.getLocale( localeCode );
    String value = null;
    Map dataMap = Collections.EMPTY_MAP;
    ContentReader contentReader = ContentReaderManager.getContentReader();
    Content content = null;
    if ( contentReader != null )
    {
      content = CmsUtil.getContentFromReaderObject( contentReader.getContent( cmAssetCode, locale ) );
      if ( content != null )
      {
        dataMap = content.getContentDataMapList();
      }
    }
    if ( dataMap.containsKey( key ) )
    {
      Translations translations = (Translations)dataMap.get( key );
      if ( translations != null )
      {
        if ( !StringUtils.isEmpty( translations.getValue() ) )
        {
          value = translations.getValue();
        }
      }
    }
    return value;
  }

  public boolean isWizardSendable()
  {
    boolean allowPromotionName = getModuleCode() != null && ( getModuleCode().equals( MessageModuleType.lookup( MessageModuleType.PRODUCT_CLAIM ) )
        || getModuleCode().equals( MessageModuleType.lookup( MessageModuleType.QUIZ ) ) || getModuleCode().equals( MessageModuleType.lookup( MessageModuleType.RECOGNITION ) ) );
    boolean isSendable = MessageUtils.isMessageTextWizardSendable( getI18nSubject(), allowPromotionName );

    if ( isSendable && getI18nHtmlBody() != null )
    {
      isSendable = MessageUtils.isMessageTextWizardSendable( getI18nHtmlBody(), allowPromotionName );
    }
    if ( isSendable && getI18nPlainTextBody() != null )
    {
      isSendable = MessageUtils.isMessageTextWizardSendable( getI18nPlainTextBody(), allowPromotionName );
    }
    if ( isSendable && getI18nTextBody() != null )
    {
      isSendable = MessageUtils.isMessageTextWizardSendable( getI18nTextBody(), allowPromotionName );
    }
    return isSendable;

  }

  @SuppressWarnings( "unchecked" )
  public boolean isTranslated()
  {
    CMAssetService cmAssetService = (CMAssetService)BeanLocator.getBean( CMAssetService.BEAN_NAME );
    List<Content> localeItems = cmAssetService.getSupportedLocales( true );
    for ( Content content : localeItems )
    {
      if ( content != null )
      {
        Locale locale = CmsUtil.getLocale( (String)content.getContentDataMap().get( "CODE" ) );
        if ( !StringUtils.isEmpty( cmAssetService.getString( getCmAssetCode(), CM_KEY_SUBJECT, locale, true ) )
            || !StringUtils.isEmpty( cmAssetService.getString( getCmAssetCode(), CM_KEY_HTML_MSG, locale, true ) )
            || !StringUtils.isEmpty( cmAssetService.getString( getCmAssetCode(), CM_KEY_PLAIN_TEXT_MSG, locale, true ) )
            || !StringUtils.isEmpty( cmAssetService.getString( getCmAssetCode(), CM_KEY_TEXT_MSG, locale, true ) ) )
        {
          return true;
        }
      }
    }
    return false;
  }

}

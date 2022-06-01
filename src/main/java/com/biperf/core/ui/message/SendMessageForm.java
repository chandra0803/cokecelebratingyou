/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/message/SendMessageForm.java,v $
 */

package com.biperf.core.ui.message;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import com.biperf.core.domain.message.Message;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.ui.BaseForm;
import com.biperf.core.ui.promotion.PromotionAudienceFormBean;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.MessageUtils;
import com.biperf.util.StringUtils;
import com.objectpartners.cms.util.CmsResourceBundle;

/**
 * SendMessageForm
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
 * <td>zahler</td>
 * <td>Nov 21, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class SendMessageForm extends BaseForm
{
  private String method;
  private String messageId;
  private String messageName;
  private String moduleName;
  private String moduleCode;
  private String subject;
  private String sender;
  private String previewEmailAddress1;
  private String previewEmailAddress2;
  private String previewEmailAddress3;
  private String messageAudience;
  private String messageSubAudience;
  private String deliveryMethod;
  private String deliveryDate = DateUtils.displayDateFormatMask;
  private String deliveryTime;
  private String programActivity;
  private String programDateRangeStart = DateUtils.displayDateFormatMask;
  private String programDateRangeEnd = DateUtils.displayDateFormatMask;
  private String promotionActivity;
  private String promotionDateRangeStart = DateUtils.displayDateFormatMask;
  private String promotionDateRangeEnd = DateUtils.displayDateFormatMask;
  private String selectedPromotionId;
  private String selectedAudienceId;
  private String htmlMsg;
  private String plainTextMsg;
  private String textMsg;
  private List audienceList = new ArrayList();

  private boolean excludePreviousRecipients;
  private boolean programHaveOrHaveNot;
  private boolean promotionHaveOrHaveNot;

  public void load( Message message )
  {
    messageId = message.getId().toString();
    messageName = message.getName();
    if ( message.getModuleCode() != null )
    {
      moduleCode = message.getModuleCode().getCode();
      moduleName = message.getModuleCode().getName();
    }
    else
    {
      moduleCode = "";
      moduleName = "";
    }
    subject = message.getI18nSubject();
  }

  /**
   * Reset all properties to their default values.
   * 
   * @param mapping The mapping used to select this instance
   * @param request The servlet request we are processing
   */
  public void reset( ActionMapping mapping, HttpServletRequest request )
  {
    audienceList = getEmptyValueList( RequestUtils.getOptionalParamInt( request, "audienceListCount" ) );
  }

  /**
   * resets the notificationList with empty PromotionNotificationForm beans
   * 
   * @param valueListCount
   * @return List
   */
  private List getEmptyValueList( int valueListCount )
  {
    List valueList = new ArrayList();

    for ( int i = 0; i < valueListCount; i++ )
    {
      // create an empty PromotionAudienceFormBean
      PromotionAudienceFormBean promoAudienceBean = new PromotionAudienceFormBean();
      valueList.add( promoAudienceBean );
    }

    return valueList;
  }

  public String getDeliveryDate()
  {
    return deliveryDate;
  }

  public void setDeliveryDate( String deliveryDate )
  {
    this.deliveryDate = deliveryDate;
  }

  public String getDeliveryMethod()
  {
    return deliveryMethod;
  }

  public void setDeliveryMethod( String deliveryMethod )
  {
    this.deliveryMethod = deliveryMethod;
  }

  public String getDeliveryTime()
  {
    return deliveryTime;
  }

  public void setDeliveryTime( String deliveryTime )
  {
    this.deliveryTime = deliveryTime;
  }

  public boolean isExcludePreviousRecipients()
  {
    return excludePreviousRecipients;
  }

  public void setExcludePreviousRecipients( boolean excludePreviousRecipients )
  {
    this.excludePreviousRecipients = excludePreviousRecipients;
  }

  public String getMessageAudience()
  {
    return messageAudience;
  }

  public void setMessageAudience( String messageAudience )
  {
    this.messageAudience = messageAudience;
  }

  public String getMessageId()
  {
    return messageId;
  }

  public void setMessageId( String messageId )
  {
    this.messageId = messageId;
  }

  public String getMessageName()
  {
    return messageName;
  }

  public void setMessageName( String messageName )
  {
    this.messageName = messageName;
  }

  public String getMessageSubAudience()
  {
    return messageSubAudience;
  }

  public void setMessageSubAudience( String messageSubAudience )
  {
    this.messageSubAudience = messageSubAudience;
  }

  public String getMethod()
  {
    return method;
  }

  public void setMethod( String method )
  {
    this.method = method;
  }

  public String getModuleName()
  {
    return moduleName;
  }

  public void setModuleName( String moduleName )
  {
    this.moduleName = moduleName;
  }

  public String getPreviewEmailAddress1()
  {
    return previewEmailAddress1;
  }

  public void setPreviewEmailAddress1( String previewEmailAddress1 )
  {
    this.previewEmailAddress1 = previewEmailAddress1;
  }

  public String getPreviewEmailAddress2()
  {
    return previewEmailAddress2;
  }

  public void setPreviewEmailAddress2( String previewEmailAddress2 )
  {
    this.previewEmailAddress2 = previewEmailAddress2;
  }

  public String getPreviewEmailAddress3()
  {
    return previewEmailAddress3;
  }

  public void setPreviewEmailAddress3( String previewEmailAddress3 )
  {
    this.previewEmailAddress3 = previewEmailAddress3;
  }

  public String getSender()
  {
    return sender;
  }

  public void setSender( String sender )
  {
    this.sender = sender;
  }

  public String getSubject()
  {
    if ( subject == null )
    {
      subject = "";
    }
    return subject;
  }

  public void setSubject( String subject )
  {
    this.subject = subject;
  }

  public boolean isProgramHaveOrHaveNot()
  {
    return programHaveOrHaveNot;
  }

  public void setProgramHaveOrHaveNot( boolean programHaveOrHaveNot )
  {
    this.programHaveOrHaveNot = programHaveOrHaveNot;
  }

  public boolean isPromotionHaveOrHaveNot()
  {
    return promotionHaveOrHaveNot;
  }

  public void setPromotionHaveOrHaveNot( boolean promotionHaveOrHaveNot )
  {
    this.promotionHaveOrHaveNot = promotionHaveOrHaveNot;
  }

  public String getProgramActivity()
  {
    return programActivity;
  }

  public void setProgramActivity( String programActivity )
  {
    this.programActivity = programActivity;
  }

  public String getProgramDateRangeEnd()
  {
    return programDateRangeEnd;
  }

  public void setProgramDateRangeEnd( String programDateRangeEnd )
  {
    this.programDateRangeEnd = programDateRangeEnd;
  }

  public String getProgramDateRangeStart()
  {
    return programDateRangeStart;
  }

  public void setProgramDateRangeStart( String programDateRangeStart )
  {
    this.programDateRangeStart = programDateRangeStart;
  }

  public String getPromotionActivity()
  {
    return promotionActivity;
  }

  public void setPromotionActivity( String promotionActivity )
  {
    this.promotionActivity = promotionActivity;
  }

  public String getPromotionDateRangeEnd()
  {
    return promotionDateRangeEnd;
  }

  public void setPromotionDateRangeEnd( String promotionDateRangeEnd )
  {
    this.promotionDateRangeEnd = promotionDateRangeEnd;
  }

  public String getPromotionDateRangeStart()
  {
    return promotionDateRangeStart;
  }

  public void setPromotionDateRangeStart( String promotionDateRangeStart )
  {
    this.promotionDateRangeStart = promotionDateRangeStart;
  }

  public String getSelectedPromotionId()
  {
    return selectedPromotionId;
  }

  public void setSelectedPromotionId( String selectedPromotionId )
  {
    this.selectedPromotionId = selectedPromotionId;
  }

  public List getAudienceAsList()
  {
    return audienceList;
  }

  public void setAudienceAsList( List audienceList )
  {
    this.audienceList = audienceList;
  }

  public String getSelectedAudienceId()
  {
    return selectedAudienceId;
  }

  public void setSelectedAudienceId( String selectedAudienceId )
  {
    this.selectedAudienceId = selectedAudienceId;
  }

  /**
   * Accessor for the number of PromotionAudienceFormBean objects in the list.
   * 
   * @return int
   */
  public int getAudienceListCount()
  {
    if ( audienceList == null )
    {
      return 0;
    }

    return audienceList.size();
  }

  /**
   * Accessor for the value list
   * 
   * @param index
   * @return Single instance of PromotionAudienceFormBean from the value list
   */
  public PromotionAudienceFormBean getAudienceList( int index )
  {
    try
    {
      return (PromotionAudienceFormBean)audienceList.get( index );
    }
    catch( Exception anException )
    {
      return null;
    }
  }

  public String getModuleCode()
  {
    return moduleCode;
  }

  public void setModuleCode( String moduleCode )
  {
    this.moduleCode = moduleCode;
  }

  /**
   * Validate the properties that have been set from this HTTP request, and return an
   * <code>ActionErrors</code> object that encapsulates any validation errors that have been
   * found. If no errors are found, return <code>null</code> or an <code>ActionErrors</code>
   * object with no recorded error messages. Validation is being done inside the form because there
   * are dynamic fields that may or may not need validating.
   * 
   * @param actionMapping the ActionMapping used to select this instance
   * @param request the HTTP request that is being processed
   * @return org.apache.struts.action.ActionErrors - collection of errors found on validation of
   *         data in form
   */

  public ActionErrors validate( ActionMapping actionMapping, HttpServletRequest request )
  {

    ActionErrors actionErrors = super.validate( actionMapping, request );

    if ( actionErrors == null )
    {
      actionErrors = new ActionErrors();
    }

    if ( StringUtils.isEmpty( getMessageId() ) )
    { // means we are doing an AdHoc message
      boolean allowPromotionName = !StringUtils.isEmpty( getSelectedPromotionId() );

      if ( StringUtils.isEmpty( getHtmlMsg() ) )
      {
        actionErrors.add( "htmlMsg", new ActionMessage( "system.errors.REQUIRED", CmsResourceBundle.getCmsBundle().getString( "admin.send.message.HTML_MESSAGE" ) ) );
      }
      if ( StringUtils.isEmpty( getPlainTextMsg() ) )
      {
        actionErrors.add( "plainTextMsg", new ActionMessage( "system.errors.REQUIRED", CmsResourceBundle.getCmsBundle().getString( "admin.send.message.PLAIN_TEXT_MESSAGE" ) ) );
      }
      // Check if sms text message exceeds 100 characters
      if ( getTextMsg().length() > 160 )
      {
        actionErrors.add( "textMsg", new ActionMessage( ServiceErrorMessageKeys.MESSAGE_TEXT_TOO_LONG, CmsResourceBundle.getCmsBundle().getString( "admin.message.errors.TEXT_TOO_LONG" ) ) );
      }
      if ( !MessageUtils.isMessageTextWizardSendable( getSubject(), allowPromotionName ) )
      {
        actionErrors.add( "subject", new ActionMessage( "admin.send.message.MESSAGE_NOT_WIZARD_SENDABLE", CmsResourceBundle.getCmsBundle().getString( "admin.message.details.SUBJECT" ) ) );
      }
      if ( getHtmlMsg() != null && !MessageUtils.isMessageTextWizardSendable( getHtmlMsg(), allowPromotionName ) )
      {
        actionErrors.add( "htmlMsg", new ActionMessage( "admin.send.message.MESSAGE_NOT_WIZARD_SENDABLE", CmsResourceBundle.getCmsBundle().getString( "admin.send.message.HTML_MESSAGE" ) ) );
      }
      if ( getHtmlMsg() != null && !MessageUtils.isMessageTextWizardSendable( getPlainTextMsg(), allowPromotionName ) )
      {
        actionErrors.add( "plainTextMsg",
                          new ActionMessage( "admin.send.message.MESSAGE_NOT_WIZARD_SENDABLE", CmsResourceBundle.getCmsBundle().getString( "admin.send.message.PLAIN_TEXT_MESSAGE" ) ) );
      }
      if ( getHtmlMsg() != null && !MessageUtils.isMessageTextWizardSendable( getTextMsg(), allowPromotionName ) )
      {
        actionErrors.add( "textMsg", new ActionMessage( "admin.send.message.MESSAGE_NOT_WIZARD_SENDABLE", CmsResourceBundle.getCmsBundle().getString( "admin.send.message.TEXT_MESSAGE" ) ) );
      }
    }
    if ( getMessageAudience() != null && getMessageAudience().equals( "preview" ) )
    {
      if ( StringUtils.isEmpty( getPreviewEmailAddress1() ) && StringUtils.isEmpty( getPreviewEmailAddress1() ) && StringUtils.isEmpty( getPreviewEmailAddress1() ) )
      {
        actionErrors.add( "messageAudience", new ActionMessage( "system.errors.REQUIRED", CmsResourceBundle.getCmsBundle().getString( "admin.send.message.PREVIEW_EMAIL_ADDRESS_ALERT" ) ) );
      }
    }
    if ( getMessageAudience() != null && getMessageAudience().equals( "programParticipants" ) )
    {
      if ( getMessageSubAudience() == null || !getMessageSubAudience().equals( "allPaxInProgram" ) && !getMessageSubAudience().equals( "allPaxInProgramWho" ) )
      {
        actionErrors.add( "messageAudience", new ActionMessage( "system.errors.REQUIRED", CmsResourceBundle.getCmsBundle().getString( "admin.send.message.PROGRAM_SUB_AUDIENCE" ) ) );
      }
    }
    if ( getMessageAudience() != null && getMessageAudience().equals( "promotionAudience" ) )
    {
      if ( StringUtils.isEmpty( getSelectedPromotionId() ) )
      {
        actionErrors.add( "messageAudience", new ActionMessage( "system.errors.REQUIRED", CmsResourceBundle.getCmsBundle().getString( "admin.send.message.PROMOTION" ) ) );
      }
      if ( getMessageSubAudience() == null || !getMessageSubAudience().equals( "allPaxInPromotion" ) && !getMessageSubAudience().equals( "allPaxInPromotionWho" ) )
      {
        actionErrors.add( "messageAudience", new ActionMessage( "system.errors.REQUIRED", CmsResourceBundle.getCmsBundle().getString( "admin.send.message.PROMOTION_SUB_AUDIENCE" ) ) );
      }
      else if ( getMessageSubAudience().equals( "allPaxInPromotionWho" ) && isDisplayPromotionDateRange() )
      {
        if ( StringUtils.isEmpty( getPromotionDateRangeStart() ) )
        {
          actionErrors.add( "messageAudience", new ActionMessage( "system.errors.REQUIRED", CmsResourceBundle.getCmsBundle().getString( "admin.send.message.PROMOTION_DATE_RANGE_START" ) ) );
        }
        if ( StringUtils.isEmpty( getPromotionDateRangeEnd() ) )
        {
          actionErrors.add( "messageAudience", new ActionMessage( "system.errors.REQUIRED", CmsResourceBundle.getCmsBundle().getString( "admin.send.message.PROMOTION_DATE_RANGE_END" ) ) );
        }
      }
    }
    return actionErrors;
  }

  public String getHtmlMsg()
  {
    if ( htmlMsg == null )
    {
      htmlMsg = "";
    }
    return htmlMsg;
  }

  public void setHtmlMsg( String htmlMsg )
  {
    this.htmlMsg = htmlMsg;
  }

  public String getPlainTextMsg()
  {
    if ( plainTextMsg == null )
    {
      plainTextMsg = "";
    }
    return plainTextMsg;
  }

  public void setPlainTextMsg( String plainTextMsg )
  {
    this.plainTextMsg = plainTextMsg;
  }

  public String getTextMsg()
  {
    if ( textMsg == null )
    {
      textMsg = "";
    }
    return textMsg;
  }

  public void setTextMsg( String textMsg )
  {
    this.textMsg = textMsg;
  }

  private boolean isDisplayPromotionDateRange()
  {
    return ! ( getPromotionActivity().equals( SendMessageAction.USED_ALL_BUDGET ) && !isPromotionHaveOrHaveNot() );
  }

}

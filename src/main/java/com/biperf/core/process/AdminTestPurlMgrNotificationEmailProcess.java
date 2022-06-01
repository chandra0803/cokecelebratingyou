
package com.biperf.core.process;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.domain.enums.MailingType;
import com.biperf.core.domain.mailing.Mailing;
import com.biperf.core.domain.mailing.MailingRecipient;
import com.biperf.core.domain.message.Message;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.service.cms.CMAssetService;
import com.biperf.core.service.message.MessageService;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.GuidUtils;
import com.biperf.core.utils.LocaleUtils;
import com.biperf.core.utils.NewServiceAnniversaryUtil;
import com.biperf.core.utils.StringUtil;
import com.objectpartners.cms.util.CmsUtil;

public class AdminTestPurlMgrNotificationEmailProcess extends BaseProcessImpl
{

  private static final Log log = LogFactory.getLog( AdminTestPurlMgrNotificationEmailProcess.class );

  public static final String PROCESS_NAME = "Admin Test PURL Manager Notification Email Process";
  public static final String BEAN_NAME = "adminTestPurlMgrNotificationEmailProcess";

  private PromotionService promotionService;
  private CMAssetService cmAssetService;

  private String recipientUserName;
  private String managerUserName;
  private Long promotionId;
  private String recipientLocale;
  // private String awardDate;
  private String invitationEndDate;
  private String customFormField1;
  private String customFormField2;
  private String customFormField3;

  public AdminTestPurlMgrNotificationEmailProcess()
  {
    super();
  }

  public void onExecute()
  {

    // Disabling The Process As New Service Anniversary & Celebration Module Enabled From Nackle
    // Eco-System.
    if ( NewServiceAnniversaryUtil.isNewServiceAnniversaryEnabled() )
    {
      log.info( " The Process 'AdminTestPurlMgrNotificationEmailProcess' Has Been Restricted !!!!, Since The New Service Anniversary Has Been Enabled IN DM From Nackle Eco-System." );
      addComment( " The Process 'AdminTestPurlMgrNotificationEmailProcess' Has Been Restricted !!!!, Since The New Service Anniversary Has Been Enabled IN DM From Nackle Eco-System." );

    }
    else
    {
      log.debug( "Starting Admin Test PURL Manager Notification Email Process with Manager User Name: " + managerUserName );
      try
      {
        Participant manager = participantService.getParticipantByUserName( managerUserName );
        if ( manager != null )
        {
          Message celebrationManagerMessage = messageService.getMessageByCMAssetCode( MessageService.PURL_MANAGER_NOTIFICATION_MESSAGE_CM_ASSET_CODE );

          /* normally sent by mailingService */
          MailingRecipient mailingRecipient = buildMailingRecipientForPurlManagerInvitation( manager );
          Mailing mailing = new Mailing();
          mailing.setGuid( GuidUtils.generateGuid() );
          mailing.setMailingType( MailingType.lookup( MailingType.PROMOTION ) );
          mailing.setMessage( celebrationManagerMessage );
          mailing.setDeliveryDate( new Timestamp( new Date().getTime() ) );
          mailing.setSender( systemVariableService.getPropertyByName( SystemVariableService.SYSTEM_EMAIL_ADDRESS ).getStringVal() );
          mailing.addMailingRecipient( mailingRecipient );
          this.mailingService.submitMailing( mailing, null );

          addComment( "Email sent to user name " + managerUserName );
        }
        else
        {
          addComment( "Manager user name " + managerUserName + " not available in the system." );
        }
      }
      catch( Exception e )
      {
        logErrorMessage( e );
        addComment( "An exception occurred while processing with manager user name: " + managerUserName );
      }

    }

  }

  public MailingRecipient buildMailingRecipientForPurlManagerInvitation( Participant nodeOwner )
  {

    MailingRecipient mailingRecipient = new MailingRecipient();
    mailingRecipient.setGuid( GuidUtils.generateGuid() );
    mailingRecipient.setUser( nodeOwner );
    mailingRecipient.setLocale( recipientLocale );

    Map dataMap = new HashMap();
    dataMap.put( "managerFirstName", nodeOwner.getFirstName() );
    dataMap.put( "managerLastName", nodeOwner.getLastName() );

    Participant recipient = participantService.getParticipantByUserName( recipientUserName );
    dataMap.put( "firstName", recipient.getFirstName() );
    dataMap.put( "lastName", recipient.getLastName() );

    StringBuffer purlRecipientName = new StringBuffer();

    purlRecipientName.append( recipient.getFirstName() ).append( " " ).append( recipient.getLastName() ).append( " " );
    // not used in the email at this time
    // Date awardDateDate = DateUtils.toDate( this.awardDate );
    // dataMap.put( "awardDate", com.biperf.core.utils.DateUtils.toDisplayString(awardDateDate,
    // LocaleUtils.getLocale(mailingRecipient.getLocale()) ) );

    dataMap.put( "purlRecipientName", purlRecipientName.toString() );
    Promotion promotion = promotionService.getPromotionById( promotionId );
    if ( promotion.getPromoNameAssetCode() != null )
    {
      Locale locale = CmsUtil.getLocale( mailingRecipient.getLocale() );
      String promotionName = cmAssetService.getString( promotion.getPromoNameAssetCode(), Promotion.PROMOTION_NAME_KEY_PREFIX, locale, true );
      dataMap.put( "promotionName", StringEscapeUtils.unescapeHtml4( promotionName ) );
    }
    Date invitationEndDateDate = DateUtils.toDate( this.invitationEndDate );
    // Purl Invitation End Notification date : purl contribution end date
    dataMap.put( "purlInvitationEndDate", com.biperf.core.utils.DateUtils.toDisplayString( invitationEndDateDate, LocaleUtils.getLocale( mailingRecipient.getLocale() ) ) );

    dataMap.put( "purlMaintenanceLink", "#" );

    dataMap.put( "companyName", systemVariableService.getPropertyByName( SystemVariableService.CLIENT_NAME ).getStringVal() );
    int i = 1;
    if ( !StringUtil.isNullOrEmpty( customFormField1 ) )
    {
      dataMap.put( "formElement" + i, customFormField1 );
      i++;
    }
    if ( !StringUtil.isNullOrEmpty( customFormField2 ) )
    {
      dataMap.put( "formElement" + i, customFormField2 );
      i++;
    }
    if ( !StringUtil.isNullOrEmpty( customFormField3 ) )
    {
      dataMap.put( "formElement" + i, customFormField3 );
    }

    mailingRecipient.addMailingRecipientDataFromMap( dataMap );

    return mailingRecipient;
  }

  public void setPromotionService( PromotionService promotionService )
  {
    this.promotionService = promotionService;
  }

  public void setCmAssetService( CMAssetService cmAssetService )
  {
    this.cmAssetService = cmAssetService;
  }

  public String getRecipientUserName()
  {
    return recipientUserName;
  }

  public void setRecipientUserName( String recipientUserName )
  {
    this.recipientUserName = recipientUserName;
  }

  public String getManagerUserName()
  {
    return managerUserName;
  }

  public void setManagerUserName( String managerUserName )
  {
    this.managerUserName = managerUserName;
  }

  public Long getPromotionId()
  {
    return promotionId;
  }

  public void setPromotionId( Long promotionId )
  {
    this.promotionId = promotionId;
  }

  public String getRecipientLocale()
  {
    return recipientLocale;
  }

  public void setRecipientLocale( String recipientLocale )
  {
    this.recipientLocale = recipientLocale;
  }

  // public String getAwardDate()
  // {
  // return awardDate;
  // }
  //
  // public void setAwardDate( String awardDate )
  // {
  // this.awardDate = awardDate;
  // }

  public String getInvitationEndDate()
  {
    return invitationEndDate;
  }

  public void setInvitationEndDate( String invitationEndDate )
  {
    this.invitationEndDate = invitationEndDate;
  }

  public String getCustomFormField1()
  {
    return customFormField1;
  }

  public void setCustomFormField1( String customFormField1 )
  {
    this.customFormField1 = customFormField1;
  }

  public String getCustomFormField2()
  {
    return customFormField2;
  }

  public void setCustomFormField2( String customFormField2 )
  {
    this.customFormField2 = customFormField2;
  }

  public String getCustomFormField3()
  {
    return customFormField3;
  }

  public void setCustomFormField3( String customFormField3 )
  {
    this.customFormField3 = customFormField3;
  }

}

/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/process/Attic/AdminTestRecogPurlManagersNonResponseEmailProcess.java,v $
 */

package com.biperf.core.process;

import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.domain.enums.MailingType;
import com.biperf.core.domain.enums.PromotionAwardsType;
import com.biperf.core.domain.mailing.Mailing;
import com.biperf.core.domain.mailing.MailingRecipient;
import com.biperf.core.domain.mailing.MailingRecipientData;
import com.biperf.core.domain.message.Message;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.service.cms.CMAssetService;
import com.biperf.core.service.message.MessageService;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.utils.ClientStateUtils;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.GuidUtils;
import com.biperf.core.utils.NewServiceAnniversaryUtil;
import com.biperf.core.utils.StringUtil;
import com.objectpartners.cms.util.CmsUtil;

/**
 * AdminTestRecogPurlContributorsNonResponseEmailProcess is the process to send the non response email message to purl contributors.
 * This process created for "EmailTestChangeRequestMay042015.doc"
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
 * <td>Venkatesh Dudam</td>
 * <td>Jun 29, 2015</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 */
public class AdminTestRecogPurlManagersNonResponseEmailProcess extends BaseProcessImpl
{

  private static final Log log = LogFactory.getLog( AdminTestRecogPurlManagersNonResponseEmailProcess.class );

  public static final String PROCESS_NAME = "Admin Test Recog Purl Managers Non Response Email Process";
  public static final String BEAN_NAME = "adminTestRecogPurlManagersNonResponseEmailProcess";

  private PromotionService promotionService;
  private CMAssetService cmAssetService;

  private String purlRecipientUserName;
  private String purlManagerUserName;
  private Long promotionId;
  private String awardDate;
  private String customFormField1;
  private String customFormField2;
  private String customFormField3;
  private String recipientLocale;

  public AdminTestRecogPurlManagersNonResponseEmailProcess()
  {
    super();
  }

  public void onExecute()
  {
    // Disabling The Process As New Service Anniversary & Celebration Module Enabled From Nackle
    // Eco-System.
    if ( NewServiceAnniversaryUtil.isNewServiceAnniversaryEnabled() )
    {
      log.info( " The Process 'AdminTestRecogPurlManagersNonResponseEmailProcess' Has Been Restricted !!!!, Since The New Service Anniversary Has Been Enabled IN DM From Nackle Eco-System." );
      addComment( " The Process 'AdminTestRecogPurlManagersNonResponseEmailProcess' Has Been Restricted !!!!, Since The New Service Anniversary Has Been Enabled IN DM From Nackle Eco-System." );

    }
    else
    {
      log.debug( "Starting Admin Test Recog Purl Managers Non Response Email Process with Purl Recipient User Name: " + purlRecipientUserName );
      try
      {
        Participant purlRecipient = participantService.getParticipantByUserName( purlRecipientUserName );
        Participant manager = participantService.getParticipantByUserName( purlManagerUserName );
        if ( purlRecipient != null && manager != null )
        {

          Message message = messageService.getMessageByCMAssetCode( MessageService.PURL_MANAGERS_NONRESPONSE_NOTIFICATION_MESSAGE_CM_ASSET_CODE );
          Promotion promotion = promotionService.getPromotionById( promotionId );
          Date awardDate = DateUtils.toDate( this.awardDate );
          Map dataMap = populateDataMap( promotion );

          // Compose the mailing
          Mailing mailing = composeMail( message.getId(), MailingType.PROMOTION );

          MailingRecipient mailingRecipient = new MailingRecipient();
          mailingRecipient.setGuid( GuidUtils.generateGuid() );
          mailingRecipient.setLocale( recipientLocale );
          mailingRecipient.setUser( manager );

          MailingRecipientData managerFirstName = new MailingRecipientData();
          managerFirstName.setKey( "managerFirstName" );
          managerFirstName.setValue( manager.getFirstName() );
          managerFirstName.setMailingRecipient( mailingRecipient );

          MailingRecipientData recipientFirstName = new MailingRecipientData();
          recipientFirstName.setKey( "recipientFirstName" );
          recipientFirstName.setValue( purlRecipient.getFirstName() );
          recipientFirstName.setMailingRecipient( mailingRecipient );

          MailingRecipientData recipientLastName = new MailingRecipientData();
          recipientLastName.setKey( "recipientLastName" );
          recipientLastName.setValue( purlRecipient.getLastName() );
          recipientLastName.setMailingRecipient( mailingRecipient );

          MailingRecipientData purlRecipientName = new MailingRecipientData();
          purlRecipientName.setKey( "purlRecipientName" );
          purlRecipientName.setValue( purlRecipient.getNameFLNoComma() );
          purlRecipientName.setMailingRecipient( mailingRecipient );

          MailingRecipientData purlMaintenanceListLink = new MailingRecipientData();
          purlMaintenanceListLink.setKey( "maintenanceListLink" );
          Map clientStateParameterMap = new HashMap();
          purlMaintenanceListLink.setValue( ClientStateUtils.generateEncodedLink( systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal(),
                                                                                  "/purl/purlMaintenanceList.do",
                                                                                  clientStateParameterMap ) );

          // Purl Invitation End Notification date : 5 days prior to the Purl award date
          Date purlInvitationEndNotificationDate = new Date( awardDate.getTime() - 5 * org.apache.commons.lang3.time.DateUtils.MILLIS_PER_DAY );
          MailingRecipientData purlInvitationEndDate = new MailingRecipientData();
          purlInvitationEndDate.setKey( "purlInvitationEndDate" );
          purlInvitationEndDate.setValue( com.biperf.core.utils.DateUtils.toDisplayString( purlInvitationEndNotificationDate ) );
          purlInvitationEndDate.setMailingRecipient( mailingRecipient );

          MailingRecipientData promotionName = new MailingRecipientData();
          promotionName.setKey( "promotionName" );
          promotionName.setValue( getPromotionName( promotion.getPromoNameAssetCode(), mailingRecipient.getLocale() ) );
          promotionName.setMailingRecipient( mailingRecipient );

          int i = 1;
          if ( !StringUtil.isNullOrEmpty( customFormField1 ) )
          {

            MailingRecipientData formElementData = new MailingRecipientData();
            formElementData.setKey( "formElement" + i );
            formElementData.setValue( customFormField1 );
            formElementData.setMailingRecipient( mailingRecipient );
            mailingRecipient.addMailingRecipientData( formElementData );
            i++;
          }
          if ( !StringUtil.isNullOrEmpty( customFormField2 ) )
          {
            MailingRecipientData formElementData = new MailingRecipientData();
            formElementData.setKey( "formElement" + i );
            formElementData.setValue( customFormField2 );
            formElementData.setMailingRecipient( mailingRecipient );
            mailingRecipient.addMailingRecipientData( formElementData );
            i++;
          }
          if ( !StringUtil.isNullOrEmpty( customFormField3 ) )
          {
            MailingRecipientData formElementData = new MailingRecipientData();
            formElementData.setKey( "formElement" + i );
            formElementData.setValue( customFormField3 );
            formElementData.setMailingRecipient( mailingRecipient );
            mailingRecipient.addMailingRecipientData( formElementData );
          }

          mailingRecipient.addMailingRecipientData( managerFirstName );
          mailingRecipient.addMailingRecipientData( recipientFirstName );
          mailingRecipient.addMailingRecipientData( recipientLastName );
          mailingRecipient.addMailingRecipientData( purlRecipientName );
          mailingRecipient.addMailingRecipientData( purlMaintenanceListLink );
          mailingRecipient.addMailingRecipientData( purlInvitationEndDate );
          mailingRecipient.addMailingRecipientData( promotionName );

          mailing.addMailingRecipient( mailingRecipient );
          try
          {
            mailingService.submitMailing( mailing, dataMap );
          }
          catch( Exception e )
          {
            logErrorMessage( e );
            addComment( "An exception occurred while sending Purl Manager NonResponse Reminder Email for promotion: " + promotion.getName() + " with admin test recog purl mgr non response process" );
          }

        }
        else
        {
          if ( purlRecipient == null && manager == null )
          {
            addComment( "Purl Recipient User Name " + purlRecipientUserName + " & Purl Recipient Manager User Name " + purlManagerUserName
                + " are not available in the system to launch Admin Test Recog Purl Contributors Non Response Email Process." );
          }
          else if ( purlRecipient == null )
          {
            addComment( "Purl Recipient User Name " + purlRecipientUserName + " is not available in the system to launch Admin Test Recog Purl Contributors Non Response Email Process." );
          }
          else
          {
            addComment( "Manager User Name " + purlManagerUserName + " is not available in the system to launch Admin Test Recog Purl Contributors Non Response Email Process." );
          }
        }
      }
      catch( Exception e )
      {
        logErrorMessage( e );
        addComment( "An exception occurred while processing Admin Test Recog Purl Managers Non Response Email Process with Purl Recipient User Name: " + purlRecipientUserName );
      }

    }

  }

  public String getPurlRecipientUserName()
  {
    return purlRecipientUserName;
  }

  public void setPurlRecipientUserName( String purlRecipientUserName )
  {
    this.purlRecipientUserName = purlRecipientUserName;
  }

  public String getPurlManagerUserName()
  {
    return purlManagerUserName;
  }

  public void setPurlManagerUserName( String purlManagerUserName )
  {
    this.purlManagerUserName = purlManagerUserName;
  }

  public Long getPromotionId()
  {
    return promotionId;
  }

  public void setPromotionId( Long promotionId )
  {
    this.promotionId = promotionId;
  }

  public String getAwardDate()
  {
    return awardDate;
  }

  public void setAwardDate( String awardDate )
  {
    this.awardDate = awardDate;
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

  public String getRecipientLocale()
  {
    return recipientLocale;
  }

  public void setRecipientLocale( String recipientLocale )
  {
    this.recipientLocale = recipientLocale;
  }

  private String getPromotionName( String assetCode, String userLocale )
  {
    Locale locale = CmsUtil.getLocale( userLocale );
    return cmAssetService.getString( assetCode, Promotion.PROMOTION_NAME_KEY_PREFIX, locale, true );
  }

  private Map populateDataMap( Promotion promotion )
  {
    Map dataMap = new HashMap();
    if ( promotion.getAwardType().isPointsAwardType() )
    {
      dataMap.put( "points", PromotionAwardsType.POINTS );
      dataMap.put( "pointsType", "TRUE" );
    }
    else if ( promotion.getAwardType().isMerchandiseAwardType() )
    {
      dataMap.put( "merchandiseType", "TRUE" );
    }
    dataMap.put( "companyName", systemVariableService.getPropertyByName( SystemVariableService.CLIENT_NAME ).getStringVal() );
    return dataMap;
  }

  public void setPromotionService( PromotionService promotionService )
  {
    this.promotionService = promotionService;
  }

  public void setCmAssetService( CMAssetService cmAssetService )
  {
    this.cmAssetService = cmAssetService;
  }

}

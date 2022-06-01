/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/process/Attic/AdminTestRecogPurlContributorsNonResponseEmailProcess.java,v $
 */

package com.biperf.core.process;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.domain.enums.MailingType;
import com.biperf.core.domain.mailing.Mailing;
import com.biperf.core.domain.mailing.MailingRecipient;
import com.biperf.core.domain.mailing.MailingRecipientData;
import com.biperf.core.domain.message.Message;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.service.message.MessageService;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.utils.ClientStateUtils;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.GuidUtils;
import com.biperf.core.utils.LocaleUtils;
import com.biperf.core.utils.NewServiceAnniversaryUtil;
import com.biperf.core.utils.StringUtil;

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
 * <td>Jun 26, 2015</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 */
public class AdminTestRecogPurlContributorsNonResponseEmailProcess extends BaseProcessImpl
{

  private static final Log log = LogFactory.getLog( AdminTestRecogPurlContributorsNonResponseEmailProcess.class );

  public static final String PROCESS_NAME = "Admin Test Recog Purl Contributors Non Response Email Process";
  public static final String BEAN_NAME = "adminTestRecogPurlContributorsNonResponseEmailProcess";

  private PromotionService promotionService;

  private String purlRecipientUserName;
  private String purlContributorUserName;
  private String purlInvitedContributorUserName;
  private Long promotionId;
  private String awardDate;
  private String customFormField1;
  private String customFormField2;
  private String customFormField3;
  private String recipientLocale;

  public AdminTestRecogPurlContributorsNonResponseEmailProcess()
  {
    super();
  }

  public void onExecute()
  {
    // Disabling The Process As New Service Anniversary & Celebration Module Enabled From Nackle
    // Eco-System.
    if ( NewServiceAnniversaryUtil.isNewServiceAnniversaryEnabled() )
    {
      log.info( " The Process 'AdminTestRecogPurlContributorsNonResponseEmailProcess' Has Been Restricted !!!!, Since The New Service Anniversary Has Been Enabled IN DM From Nackle Eco-System." );
      addComment( " The Process 'AdminTestRecogPurlContributorsNonResponseEmailProcess' Has Been Restricted !!!!, Since The New Service Anniversary Has Been Enabled IN DM From Nackle Eco-System." );

    }
    else
    {
      log.debug( "Starting Admin Test Recog Purl Contributors Non Response Email Process with Purl Recipient User Name: " + purlRecipientUserName + "& Purl Contributor User Name: "
          + purlContributorUserName );
      try
      {
        Participant purlRecipient = participantService.getParticipantByUserName( purlRecipientUserName );
        Participant purlContributor = participantService.getParticipantByUserName( purlContributorUserName );
        Participant purlInvitedContributor = participantService.getParticipantByUserName( purlInvitedContributorUserName );
        if ( purlRecipient != null && purlContributor != null && purlInvitedContributor != null )
        {
          Message message = messageService.getMessageByCMAssetCode( MessageService.PURL_CONTRIBUTOR_NONRESPONSE_NOTIFICATION_MESSAGE_CM_ASSET_CODE );
          Promotion promotion = promotionService.getPromotionById( promotionId );

          // Add the mailing level personalization data to the objectMap
          Map dataMap = new HashMap();
          dataMap.put( "companyName", systemVariableService.getPropertyByName( SystemVariableService.CLIENT_NAME ).getStringVal() );

          Map clientStateParameterMap = new HashMap();
          clientStateParameterMap.put( "promotionId", promotion.getId() );
          clientStateParameterMap.put( "purlContributorId", purlContributor.getId() );

          // Compose the mailing
          Mailing mailing = composeMail( message.getId(), MailingType.PROMOTION );

          MailingRecipient mailingRecipient = new MailingRecipient();
          mailingRecipient.setGuid( GuidUtils.generateGuid() );
          mailingRecipient.setLocale( recipientLocale );
          mailingRecipient.setUser( purlContributor );

          MailingRecipientData invitedContributorFirstName = new MailingRecipientData();
          invitedContributorFirstName.setKey( "invitedContributorFirstName" );
          invitedContributorFirstName.setValue( purlInvitedContributor.getFirstName() );
          invitedContributorFirstName.setMailingRecipient( mailingRecipient );
          mailingRecipient.addMailingRecipientData( invitedContributorFirstName );

          MailingRecipientData invitedContributorLastName = new MailingRecipientData();
          invitedContributorLastName.setKey( "invitedContributorLastName" );
          invitedContributorLastName.setValue( purlInvitedContributor.getLastName() );
          invitedContributorLastName.setMailingRecipient( mailingRecipient );
          mailingRecipient.addMailingRecipientData( invitedContributorLastName );

          MailingRecipientData contributorFirstName = new MailingRecipientData();
          contributorFirstName.setKey( "contributorFirstName" );
          contributorFirstName.setValue( purlContributor.getFirstName() );
          contributorFirstName.setMailingRecipient( mailingRecipient );
          mailingRecipient.addMailingRecipientData( contributorFirstName );

          MailingRecipientData contributorLastName = new MailingRecipientData();
          contributorLastName.setKey( "contributorLastName" );
          contributorLastName.setValue( purlContributor.getLastName() );
          contributorLastName.setMailingRecipient( mailingRecipient );
          mailingRecipient.addMailingRecipientData( contributorLastName );

          StringBuffer purlRecipientFullName = new StringBuffer();
          purlRecipientFullName.append( purlRecipient.getFirstName() ).append( " " ).append( purlRecipient.getLastName() ).append( " " );

          MailingRecipientData purlRecipientName = new MailingRecipientData();
          purlRecipientName.setKey( "purlRecipientName" );
          purlRecipientName.setValue( purlRecipientFullName.toString() );
          purlRecipientName.setMailingRecipient( mailingRecipient );
          mailingRecipient.addMailingRecipientData( purlRecipientName );

          MailingRecipientData recipientFirstName = new MailingRecipientData();
          recipientFirstName.setKey( "recipientFirstName" );
          recipientFirstName.setValue( purlRecipient.getFirstName() );
          recipientFirstName.setMailingRecipient( mailingRecipient );
          mailingRecipient.addMailingRecipientData( recipientFirstName );

          Date awardDate = com.biperf.core.utils.DateUtils.toDate( this.awardDate );
          MailingRecipientData contributorCloseDate = new MailingRecipientData();
          contributorCloseDate.setKey( "contributorCloseDate" );
          contributorCloseDate.setValue( com.biperf.core.utils.DateUtils.toDisplayString( DateUtils.getDateAfterNumberOfDays( awardDate, -1 ),
                                                                                          LocaleUtils.getLocale( purlRecipient.getLanguageType() != null
                                                                                              ? purlRecipient.getLanguageType().getCode()
                                                                                              : systemVariableService.getDefaultLanguage().getStringVal() ) ) );
          contributorCloseDate.setMailingRecipient( mailingRecipient );
          mailingRecipient.addMailingRecipientData( contributorCloseDate );

          MailingRecipientData purlContributionLink = new MailingRecipientData();
          purlContributionLink.setKey( "purlContributionLink" );
          purlContributionLink.setValue( ClientStateUtils.generateEncodedLink( systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal(),
                                                                               "/purl/purlTNC.do?method=display",
                                                                               clientStateParameterMap ) );
          mailingRecipient.addMailingRecipientData( purlContributionLink );
          if ( promotion.getPromoNameAssetCode() != null )
          {
            MailingRecipientData promotionName = new MailingRecipientData();
            promotionName.setKey( "promotionName" );
            promotionName.setValue( promotion.getName() );
            mailingRecipient.addMailingRecipientData( promotionName );
          }

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

          mailing.addMailingRecipient( mailingRecipient );
          mailingService.submitMailing( mailing, dataMap );
        }
        else
        {
          if ( purlRecipient == null && purlContributor == null && purlInvitedContributor == null )
          {
            addComment( "Purl Recipient User Name " + purlRecipientUserName + " & Purl Contribotor User Name " + purlContributorUserName + " & Purl Invited Contributor User Name "
                + purlInvitedContributorUserName + " are not available in the system to launch Admin Test Recog Purl Contributors Non Response Email Process." );
          }
          else if ( purlRecipient == null )
          {
            addComment( "Purl Recipient User Name " + purlRecipientUserName + " is not available in the system to launch Admin Test Recog Purl Contributors Non Response Email Process." );
          }
          else if ( purlContributor == null )
          {
            addComment( "Purl Contribotor User Name " + purlContributorUserName + " is not available in the system to launch Admin Test Recog Purl Contributors Non Response Email Process." );
          }
          else
          {
            addComment( "Purl Invited Contribotor User Name " + purlInvitedContributorUserName
                + " is not available in the system to launch Admin Test Recog Purl Contributors Non Response Email Process." );
          }
        }
      }
      catch( Exception e )
      {
        logErrorMessage( e );
        addComment( "An exception occurred while processing Admin Test Recog Purl Contributors Non Response Email Process with Purl Recipient User Name: " + purlRecipientUserName
            + " & Purl Contributor User Name: " + purlContributorUserName );
      }

    }

  }

  public void setPromotionService( PromotionService promotionService )
  {
    this.promotionService = promotionService;
  }

  public String getPurlRecipientUserName()
  {
    return purlRecipientUserName;
  }

  public void setPurlRecipientUserName( String purlRecipientUserName )
  {
    this.purlRecipientUserName = purlRecipientUserName;
  }

  public String getPurlContributorUserName()
  {
    return purlContributorUserName;
  }

  public void setPurlContributorUserName( String purlContributorUserName )
  {
    this.purlContributorUserName = purlContributorUserName;
  }

  public void setPurlInvitedContributorUserName( String purlInvitedContributorUserName )
  {
    this.purlInvitedContributorUserName = purlInvitedContributorUserName;
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

}


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

public class AdminTestPurlContributorsInvitationEmailProcess extends BaseProcessImpl
{

  private static final Log log = LogFactory.getLog( AdminTestPurlContributorsInvitationEmailProcess.class );

  public static final String PROCESS_NAME = "Admin Test PURL Contributors Invitation Email Process";
  public static final String BEAN_NAME = "adminTestPurlContributorsInvitationEmailProcess";

  private PromotionService promotionService;
  private CMAssetService cmAssetService;

  private String recipientUserName;
  private String contributorUserName;
  private String invitedContributorUserName;
  private Long promotionId;
  private String recipientLocale;
  private String awardDate;
  private String closeDate;
  private String customFormField1;
  private String customFormField2;
  private String customFormField3;

  public AdminTestPurlContributorsInvitationEmailProcess()
  {
    super();
  }

  public void onExecute()
  {
    // Disabling The Process As New Service Anniversary & Celebration Module Enabled From Nackle
    // Eco-System.
    if ( NewServiceAnniversaryUtil.isNewServiceAnniversaryEnabled() )
    {
      log.info( " The Process 'AdminTestPurlContributorsInvitationEmailProcess' Has Been Restricted !!!!, Since The New Service Anniversary Has Been Enabled IN DM From Nackle Eco-System." );
      addComment( " The Process 'AdminTestPurlContributorsInvitationEmailProcess' Has Been Restricted !!!!, Since The New Service Anniversary Has Been Enabled IN DM From Nackle Eco-System." );

    }
    else
    {
      log.debug( "Starting Admin Test PURL Contributors Invitation Email Process with Contributor User Name: " + contributorUserName );
      try
      {
        Participant contributor = participantService.getParticipantByUserName( contributorUserName );
        if ( contributor != null )
        {
          Message celebrationManagerMessage = messageService.getMessageByCMAssetCode( MessageService.PURL_CONTRIBUTOR_NOTIFICATION_MESSAGE_CM_ASSET_CODE );

          /* normally sent by mailingService */
          MailingRecipient mailingRecipient = buildMailingRecipientForPurlContributorInvitation( contributor );
          Mailing mailing = new Mailing();
          mailing.setGuid( GuidUtils.generateGuid() );
          mailing.setMailingType( MailingType.lookup( MailingType.PROMOTION ) );
          mailing.setMessage( celebrationManagerMessage );
          mailing.setDeliveryDate( new Timestamp( new Date().getTime() ) );
          mailing.setSender( systemVariableService.getPropertyByName( SystemVariableService.SYSTEM_EMAIL_ADDRESS ).getStringVal() );
          mailing.addMailingRecipient( mailingRecipient );
          this.mailingService.submitMailing( mailing, null );

          addComment( "Email sent to user name " + contributorUserName );
        }
        else
        {
          addComment( "Contributor user name " + contributorUserName + " not available in the system." );
        }
      }
      catch( Exception e )
      {
        logErrorMessage( e );
        addComment( "An exception occurred while processing with contributor user name: " + contributorUserName );
      }

    }

  }

  public MailingRecipient buildMailingRecipientForPurlContributorInvitation( Participant contributor )
  {

    Map dataMap = new HashMap();

    MailingRecipient mailingRecipient = new MailingRecipient();
    mailingRecipient.setGuid( GuidUtils.generateGuid() );
    mailingRecipient.setUser( contributor );
    mailingRecipient.setLocale( recipientLocale );

    dataMap.put( "contributorFirstName", contributor.getFirstName() );
    dataMap.put( "contributorLastName", contributor.getLastName() );

    Participant invitedContributor = participantService.getParticipantByUserName( invitedContributorUserName );

    if ( null != invitedContributor )
    {
      dataMap.put( "invitedContributorFirstName", invitedContributor.getFirstName() );
      dataMap.put( "invitedContributorLastName", invitedContributor.getLastName() );
    }
    Promotion promotion = promotionService.getPromotionById( promotionId );
    if ( promotion.getPromoNameAssetCode() != null )
    {
      Locale locale = CmsUtil.getLocale( mailingRecipient.getLocale() );
      String promotionName = cmAssetService.getString( promotion.getPromoNameAssetCode(), Promotion.PROMOTION_NAME_KEY_PREFIX, locale, true );
      dataMap.put( "promotionName", StringEscapeUtils.unescapeHtml4( promotionName ) );
    }

    Participant purlRecipientPax = participantService.getParticipantByUserName( recipientUserName );
    StringBuffer purlRecipientFullName = new StringBuffer();
    purlRecipientFullName.append( purlRecipientPax.getFirstName() ).append( " " ).append( purlRecipientPax.getLastName() ).append( " " );

    dataMap.put( "purlRecipientName", purlRecipientFullName.toString() );

    dataMap.put( "recipientFirstName", purlRecipientPax.getFirstName() );
    dataMap.put( "recipientLastName", purlRecipientPax.getLastName() );

    Date awardDateDate = DateUtils.toDate( this.awardDate );
    Date closeDateDate = DateUtils.toDate( this.closeDate );
    dataMap.put( "invitationStartDate", com.biperf.core.utils.DateUtils.toDisplayString( awardDateDate, LocaleUtils.getLocale( mailingRecipient.getLocale() ) ) );
    dataMap.put( "contributorCloseDate", com.biperf.core.utils.DateUtils.toDisplayString( closeDateDate, LocaleUtils.getLocale( mailingRecipient.getLocale() ) ) );

    dataMap.put( "purlContributorLink", "#" );
    String encodedLink = null;

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

  public String getContributorUserName()
  {
    return contributorUserName;
  }

  public void setContributorUserName( String contributorUserName )
  {
    this.contributorUserName = contributorUserName;
  }

  public String getInvitedContributorUserName()
  {
    return invitedContributorUserName;
  }

  public void setInvitedContributorUserName( String invitedContributorUserName )
  {
    this.invitedContributorUserName = invitedContributorUserName;
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

  public String getAwardDate()
  {
    return awardDate;
  }

  public void setAwardDate( String awardDate )
  {
    this.awardDate = awardDate;
  }

  public String getCloseDate()
  {
    return closeDate;
  }

  public void setCloseDate( String closeDate )
  {
    this.closeDate = closeDate;
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

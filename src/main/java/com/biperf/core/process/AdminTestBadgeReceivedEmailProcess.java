
package com.biperf.core.process;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.domain.enums.MailingType;
import com.biperf.core.domain.gamification.Badge;
import com.biperf.core.domain.gamification.BadgeLibrary;
import com.biperf.core.domain.gamification.BadgeRule;
import com.biperf.core.domain.mailing.Mailing;
import com.biperf.core.domain.mailing.MailingRecipient;
import com.biperf.core.domain.message.Message;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.user.User;
import com.biperf.core.service.gamification.GamificationService;
import com.biperf.core.service.message.MessageService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.utils.GuidUtils;
import com.biperf.core.utils.LocaleUtils;

public class AdminTestBadgeReceivedEmailProcess extends BaseProcessImpl
{

  private static final Log log = LogFactory.getLog( AdminTestBadgeReceivedEmailProcess.class );

  public static final String PROCESS_NAME = "Admin Test Badge Received Email Process";
  public static final String BEAN_NAME = "adminTestBadgeReceivedEmailProcess";

  private GamificationService gamificationService;

  private String recipientUserName;
  private Long promotionId;
  private String recipientLocale;

  public AdminTestBadgeReceivedEmailProcess()
  {
    super();
  }

  public void onExecute()
  {
    log.debug( "Starting Admin Test Badge Received Email Process with User Name: " + recipientUserName );
    try
    {
      Participant recipient = participantService.getParticipantByUserName( recipientUserName );
      if ( recipient != null )
      {

        Badge badge = gamificationService.getBadgeById( promotionId );

        for ( BadgeRule badgeRule : ( (Badge)badge ).getBadgeRules() )
        {
          // Default recordExist to zero. If 1 is passed both sentences(pointsEarnedBefore and
          // awardAmountNotNull)
          // are displaying for progress badges.
          int recordExist = 0;
          sendSummaryMessage( recipient, badge.getPromotionName(), badgeRule, recordExist );
          break; // pick the first rule in the loop
        }

        addComment( "Email sent to user name " + recipientUserName );
      }
      else
      {
        addComment( "User name " + recipientUserName + " not available in the system to launch Admin Test Badge Received Email Process." );
      }
    }
    catch( Exception e )
    {
      logErrorMessage( e );
      addComment( "An exception occurred while processing Admin Test Badge Received Email Process with Username: " + recipientUserName );
    }
  }

  /* cloned from GamificationServiceImpl */
  public void sendSummaryMessage( Participant participant, String promotionName, BadgeRule rule, int recordExist )
  {
    User recipientUser = (User)participant;

    Mailing mailing = new Mailing();
    Message message = messageService.getMessageByCMAssetCode( MessageService.BADGE_RECEIVED_MESSAGE_CM_ASSET_CODE );
    String badgeImageUrl = "";
    boolean showPromotion = false;
    String languageCode = recipientLocale;

    List earnedNotEarnedImageList = gamificationService.getEarnedNotEarnedImageList( rule.getBadgeLibraryCMKey(), LocaleUtils.getLocale( languageCode ) );
    Iterator itr = earnedNotEarnedImageList.iterator();
    String siteUrlPrefix = getSystemVariableService().getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();
    while ( itr.hasNext() )
    {
      BadgeLibrary badgeLib = (BadgeLibrary)itr.next();
      badgeImageUrl = siteUrlPrefix + "/" + badgeLib.getEarnedImageLarge();
    }
    if ( StringUtils.isEmpty( promotionName ) )
    {
      showPromotion = false;
    }
    else
    {
      showPromotion = true;
    }

    // Add the summary info to the objectMap
    Map objectMap = new HashMap();
    objectMap.put( "firstName", participant.getFirstName() );
    objectMap.put( "lastName", participant.getLastName() );
    objectMap.put( "showPromotion", showPromotion );
    objectMap.put( "promotionName", promotionName );
    objectMap.put( "imageUrl", badgeImageUrl );
    objectMap.put( "badgeName", rule.getBadgeNameTextFromCM() );
    objectMap.put( "programUrl", siteUrlPrefix );

    if ( rule.isEligibleForSweepstake() )
    {
      objectMap.put( "showSweeps", "true" );
    }

    if ( recordExist > 0 )
    {
      objectMap.put( "pointsEarnedBefore", true );
      if ( rule.getBadgePoints() == null )
      {
        objectMap.put( "pointsEarnedBefore", false );
      }
      getAwardProperties( rule, objectMap );
    }
    else
    {
      objectMap.put( "pointsEarnedBefore", false );
      getAwardProperties( rule, objectMap );
    }
    mailing = composeMail( message.getCmAssetCode(), MailingType.PROMOTION );
    // Add the recipient
    MailingRecipient mr = addRecipient( recipientUser, recipientLocale );
    mailing.addMailingRecipient( mr );
    try
    {
      // Send the e-mail message with personalization
      mailingService.submitMailing( mailing, objectMap );
    }
    catch( Exception e )
    {
      log.error( "An exception occurred while sending badge received email" );
    }
  }

  protected MailingRecipient addRecipient( User recipient, String recipientLocale )
  {
    MailingRecipient mailingRecipient = new MailingRecipient();
    mailingRecipient.setGuid( GuidUtils.generateGuid() );
    String localeCode = recipientLocale;
    mailingRecipient.setLocale( localeCode );
    mailingRecipient.setUser( recipient );

    return mailingRecipient;
  }

  private void getAwardProperties( BadgeRule rule, Map objectMap )
  {
    if ( rule.getBadgePoints() != null )
    {
      objectMap.put( "awardAmount", rule.getBadgePoints().toString() );
      objectMap.put( "awardAmountNotNull", true );
    }
    else
    {
      objectMap.put( "awardAmountNotNull", false );
    }
    if ( rule.getBadgePromotion() != null && rule.getBadgePromotion().getAwardType() != null )
    {
      objectMap.put( "mediaType", rule.getBadgePromotion().getAwardType().getAbbr() );
    }
  }

  public void setGamificationService( GamificationService gamificationService )
  {
    this.gamificationService = gamificationService;
  }

  public String getRecipientUserName()
  {
    return recipientUserName;
  }

  public void setRecipientUserName( String recipientUserName )
  {
    this.recipientUserName = recipientUserName;
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
}

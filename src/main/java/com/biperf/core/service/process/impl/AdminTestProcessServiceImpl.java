
package com.biperf.core.service.process.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.domain.claim.ClaimRecipient;
import com.biperf.core.domain.enums.BadgeType;
import com.biperf.core.domain.enums.LanguageType;
import com.biperf.core.domain.enums.PickListItem;
import com.biperf.core.domain.enums.PromotionAwardsType;
import com.biperf.core.domain.enums.PurlPromotionMediaType;
import com.biperf.core.domain.gamification.Badge;
import com.biperf.core.domain.gamification.BadgeLibrary;
import com.biperf.core.domain.gamification.BadgeRule;
import com.biperf.core.domain.gamification.ParticipantBadge;
import com.biperf.core.domain.mailing.MailingRecipient;
import com.biperf.core.domain.multimedia.ECard;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.PromotionBehavior;
import com.biperf.core.domain.promotion.PromotionCert;
import com.biperf.core.domain.promotion.PromotionECard;
import com.biperf.core.domain.promotion.RecognitionPromotion;
import com.biperf.core.domain.user.User;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.cms.CMAssetService;
import com.biperf.core.service.gamification.GamificationService;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.service.process.AdminTestProcessService;
import com.biperf.core.service.process.ProcessInvocationService;
import com.biperf.core.service.promotion.PromotionAssociationRequest;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.utils.ClientStateUtils;
import com.biperf.core.utils.DateFormatterUtil;
import com.biperf.core.utils.GuidUtils;
import com.biperf.core.utils.HtmlUtils;
import com.biperf.core.utils.LocaleUtils;
import com.biperf.core.utils.NumberFormatUtil;
import com.biperf.core.utils.StringUtil;
import com.biperf.core.value.AdminTestProcessParmBean;
import com.objectpartners.cms.domain.Content;
import com.objectpartners.cms.service.ContentReader;
import com.objectpartners.cms.util.CmsUtil;
import com.objectpartners.cms.util.ContentReaderManager;

/**
 * 
 * AdminTestProcessServiceImpl.
 * 
 * @author bethke
 * @since Mar 4, 2016
 */

public class AdminTestProcessServiceImpl implements AdminTestProcessService
{
  private static final Log log = LogFactory.getLog( AdminTestProcessServiceImpl.class );

  ProcessInvocationService processInvocationService;
  PromotionService promotionService;
  ParticipantService participantService;
  CMAssetService cmAssetService;
  SystemVariableService systemVariableService;
  GamificationService gamificationService;
  UserService userService;

  /* cloned from MailingServiceImpl.buildRecognitionMailingRecipient */
  public MailingRecipient buildRecognitionMailingRecipient( Participant recipient, AdminTestProcessParmBean parmBean ) throws ServiceErrorException
  {
    final String referenceNumber = "123456789";
    final String fullGiftCode = "1234567890123456";

    MailingRecipient mailingRecipient = new MailingRecipient();
    mailingRecipient.setGuid( GuidUtils.generateGuid() );
    mailingRecipient.setUser( recipient );
    mailingRecipient.setLocale( parmBean.getRecipientLocale() );

    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
    associationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.ECARDS ) );
    associationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.CERTIFICATES ) );
    associationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.PROMOTION_BEHAVIORS ) );
    RecognitionPromotion recognitionPromotion = (RecognitionPromotion)promotionService.getPromotionByIdWithAssociations( parmBean.getPromotionId(), associationRequestCollection );
    boolean includeCelebrations = recognitionPromotion.isIncludeCelebrations();
    boolean includePurl = recognitionPromotion.isIncludePurl();
    Map dataMap = new HashMap();

    Locale locale = CmsUtil.getLocale( mailingRecipient.getLocale() );
    ContentReader contentReader = ContentReaderManager.getContentReader();
    Content content = (Content)contentReader.getContent( "recognition.detail", locale );
    ClaimRecipient claimRecipient = new ClaimRecipient();
    claimRecipient.setRecipient( recipient );
    if ( includeCelebrations )
    {
      boolean isClaimRecipientThePersonBeingRecognized = false;
      if ( parmBean.getIsManager() != null && parmBean.getIsManager().equalsIgnoreCase( "no" ) )
      {
        isClaimRecipientThePersonBeingRecognized = true;
      }
      String promoNameAssetCode = recognitionPromotion.getPromoNameAssetCode();
      String subjectLine = populateCelebrationNonPurlSubjectLine( promoNameAssetCode, locale, content, contentReader, claimRecipient, isClaimRecipientThePersonBeingRecognized );
      dataMap.put( "subject", subjectLine );
    }
    else
    {
      String subjectLine = StringEscapeUtils.unescapeHtml4( (String)content.getContentDataMap().get( "MAIL_SUBJECT" ) );
      if ( parmBean.getIsManager() != null && parmBean.getIsManager().equalsIgnoreCase( "yes" ) )
      {
        subjectLine = StringEscapeUtils.unescapeHtml4( (String)content.getContentDataMap().get( "MAIL_SUBJECT1" ) ) + " " + recipient.getFirstName() + " " + recipient.getLastName() + " "
            + StringEscapeUtils.unescapeHtml4( (String)content.getContentDataMap().get( "MAIL_SUBJECT2" ) );
      }
      dataMap.put( "subject", subjectLine );
    }

    if ( includePurl && recognitionPromotion.getPurlPromotionMediaType() != null && recognitionPromotion.getPurlPromotionMediaType().getCode().equals( PurlPromotionMediaType.VIDEO )
        && recognitionPromotion.getPurlMediaValue() != null )
    {
      String mediaName = recognitionPromotion.getPurlMediaValue().getCode();
      String purlVideoUrl = systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal() + "/videos/purl/" + mediaName + "/" + mediaName;
      dataMap.put( "purlVideoUrl", purlVideoUrl );
    }
    // populate Celebration Message data map
    if ( includeCelebrations )
    {
      populateCelebrationMessageMap( dataMap, recipient, recognitionPromotion, claimRecipient, null, includePurl, parmBean );
    }

    dataMap.put( "firstName", recipient.getFirstName() );
    dataMap.put( "lastName", recipient.getLastName() );

    if ( recognitionPromotion.getPromoNameAssetCode() != null )
    {
      String promotionName = cmAssetService.getString( recognitionPromotion.getPromoNameAssetCode(), Promotion.PROMOTION_NAME_KEY_PREFIX, locale, true );
      dataMap.put( "programName", StringEscapeUtils.unescapeHtml4( promotionName ) );
    }
    Participant senderPax = participantService.getParticipantByUserName( parmBean.getSenderUserName() );
    dataMap.put( "sender", senderPax.getFirstName() + " " + senderPax.getLastName() );
    dataMap.put( "companyName", systemVariableService.getPropertyByName( SystemVariableService.CLIENT_NAME ).getStringVal() );

    String behaviorFromPromo = "";
    if ( parmBean.getDisplayBehavior() != null && parmBean.getDisplayBehavior().equalsIgnoreCase( "yes" ) )
    {
      boolean behaviorFound = false;
      List contentList = (List)contentReader.getContent( "picklist.promo.recognition.behavior.items", locale );

      for ( PromotionBehavior behavior : recognitionPromotion.getPromotionBehaviors() )
      {
        for ( Iterator iter = contentList.iterator(); iter.hasNext(); )
        {
          content = (Content)iter.next();
          String code = (String)content.getContentDataMap().get( PickListItem.ITEM_CODE_KEY );
          if ( behavior.getPromotionBehaviorType().getCode().equalsIgnoreCase( code ) )
          {
            String category = (String)content.getContentDataMap().get( PickListItem.ITEM_NAME_KEY );
            dataMap.put( "category", StringEscapeUtils.unescapeHtml4( category ) );
            behaviorFromPromo = StringEscapeUtils.unescapeHtml4( category );
            break;
          }
        }
        behaviorFound = true;
        break; // select the first behavior in the loop
      }
      if ( !behaviorFound )
      {
        processInvocationService.addComment( parmBean.getProcessInvocationId(), "PromotionId " + parmBean.getPromotionId() + " does not have any assigned behaviors" );
      }
    }

    if ( parmBean.getSubmitterComments() != null && !parmBean.getSubmitterComments().trim().equals( "" ) )
    {
      dataMap.put( "showComment", "TRUE" );
      dataMap.put( "message", parmBean.getSubmitterComments() );
    }

    if ( parmBean.getAwardAmount() != null )
    {
      if ( parmBean.getAwardAmount() != null && parmBean.getAwardAmount().longValue() > 1 )
      {
        dataMap.put( "manyAwardAmount", "TRUE" );
      }
      String tempAwardQuantity = NumberFormatUtil.getUserLocaleBasedNumberFormat( parmBean.getAwardAmount(), LocaleUtils.getLocale( mailingRecipient.getLocale() ) );
      dataMap.put( "awardAmount", String.valueOf( tempAwardQuantity ) );

      List contentList = (List)contentReader.getContent( "picklist.promotion.awardstype.items", locale );

      for ( Iterator iter = contentList.iterator(); iter.hasNext(); )
      {
        content = (Content)iter.next();
        String code = (String)content.getContentDataMap().get( PickListItem.ITEM_CODE_KEY );
        String status = (String)content.getContentDataMap().get( PickListItem.ITEM_STATUS_KEY );
        if ( recognitionPromotion.getAwardType().getCode().equalsIgnoreCase( code ) && status.equals( "true" ) )
        {
          String mediaType = (String)content.getContentDataMap().get( PickListItem.ITEM_NAME_KEY );
          dataMap.put( "mediaType", StringEscapeUtils.unescapeHtml4( mediaType ) );
          break;
        }
      }
    }

    if ( parmBean.getIsManager() != null && parmBean.getIsManager().equalsIgnoreCase( "no" ) )
    {
      dataMap.put( "copyManager", "TRUE" );
    }

    if ( recognitionPromotion.isSweepstakesActive() )
    {
      dataMap.put( "showSweeps", "TRUE" );
    }

    if ( parmBean.getDisplayCertificate() != null && parmBean.getDisplayCertificate().equalsIgnoreCase( "yes" ) )
    {
      boolean certFound = false;
      for ( PromotionCert promotionCert : recognitionPromotion.getPromotionCertificates() )
      {
        String certificateId = promotionCert.getCertificateId();

        Long submitterId = senderPax.getId();
        dataMap.put( "showCertificate", "TRUE" );

        Map clientStateParameterMap = new HashMap();
        clientStateParameterMap.put( "certificateId", certificateId );
        clientStateParameterMap.put( "behavior", behaviorFromPromo );
        clientStateParameterMap.put( "promotionId", recognitionPromotion.getId() );
        clientStateParameterMap.put( "recipientId", recipient.getId() );
        clientStateParameterMap.put( "submitterId", submitterId );
        clientStateParameterMap.put( "processInvocationId", parmBean.getProcessInvocationId() );
        clientStateParameterMap.put( "certLocale", parmBean.getRecipientLocale() );

        String certificatelink = ClientStateUtils.generateEncodedLink( systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal(),
                                                                       "/claim/adminTestDisplayCertificate.do?method=adminTestCertificateRecognitionDetail",
                                                                       clientStateParameterMap );

        String certificateCompleteLink = certificatelink + "&cmsLocaleCode=" + mailingRecipient.getLocale();
        dataMap.put( "certificateLink", certificateCompleteLink );
        log.debug( "Certificate Complete Url: " + certificateCompleteLink );
        certFound = true;
        break; // select the first certificate in the loop
      }
      if ( !certFound )
      {
        processInvocationService.addComment( parmBean.getProcessInvocationId(), "PromotionId " + parmBean.getPromotionId() + " does not have any assigned certificates" );
      }
    }

    if ( parmBean.getDisplayCard() != null && parmBean.getDisplayCard().equalsIgnoreCase( "yes" ) )
    {
      boolean cardFound = false;
      for ( PromotionECard ecard : recognitionPromotion.getPromotionECard() )
      {
        dataMap.put( "showECard", "TRUE" );

        ECard eCard = (ECard)ecard.geteCard();
        String eCardImg = eCard.getLargeImageNameLocale();
        dataMap.put( "eCardImg", eCardImg );
        dataMap.put( "eCardTypeImage", "TRUE" );

        // just display the image in case the old html with eCardLink is being used
        dataMap.put( "eCardLink", eCardImg );
        cardFound = true;
        break; // select the first card in the loop
      }
      if ( !cardFound )
      {
        processInvocationService.addComment( parmBean.getProcessInvocationId(), "PromotionId " + parmBean.getPromotionId() + " does not have any assigned cards" );
      }
    }

    if ( parmBean.getIsManager() != null && parmBean.getIsManager().equalsIgnoreCase( "yes" ) )
    {
      dataMap.put( "isManager", "TRUE" );
      Promotion mgrAwardPromotion = promotionService.getPromotionById( recognitionPromotion.getMgrAwardPromotionId() );
      boolean showManagerLink = false;
      if ( mgrAwardPromotion != null )
      {
        showManagerLink = true;
      }

      if ( showManagerLink )
      {
        dataMap.put( "showManagerLink", "TRUE" );
        String managerLink = "#";
        dataMap.put( "managerLink", managerLink );
      }
    }
    else
    {
      dataMap.put( "isClaimRecipient", "TRUE" );
    }

    if ( !recognitionPromotion.getAwardType().getCode().equals( PromotionAwardsType.MERCHANDISE ) )
    {
      dataMap.put( "showAcctLink", "TRUE" );
      dataMap.put( "siteLink",
                   systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal() + "/participantProfilePage.do?fromEmail=true#tab/Statement" );
    }
    else
    {
      if ( recognitionPromotion.isAwardActive() && recognitionPromotion.getAwardStructure() != null && recognitionPromotion.getAwardStructure().equals( RecognitionPromotion.AWARD_STRUCTURE_LEVEL ) )
      {
        // override the claim number to be the gift code's reference number
        dataMap.put( "claimNumber", referenceNumber );
        dataMap.put( "showLevelLink", "TRUE" );
        dataMap.put( "levelLink", "#" );
        // bug#18756 need to add gift code and phone number in the email
        dataMap.put( "showGiftCode", "TRUE" );
        dataMap.put( "giftCode", fullGiftCode );
        dataMap.put( "phoneNumber", systemVariableService.getPropertyByName( SystemVariableService.MERCHLINQ_ORDER_PHONE ).getStringVal() );
      }
    }

    if ( parmBean.getBadgePromotionId() != null )
    {

      List<ParticipantBadge> paxBadges = new ArrayList();
      ParticipantBadge paxBadge = new ParticipantBadge();
      Badge badge = gamificationService.getBadgeById( parmBean.getBadgePromotionId() );
      paxBadge.setBadgePromotion( badge );
      recipient.setLanguageType( LanguageType.lookup( parmBean.getRecipientLocale() ) );
      paxBadge.setParticipant( recipient );

      for ( BadgeRule badgeRuleFromBadge : ( (Badge)badge ).getBadgeRules() )
      {
        paxBadge.setBadgeRule( badgeRuleFromBadge );
        if ( badgeRuleFromBadge.getMaximumQualifier() != null )
        {
          paxBadge.setSentCount( badgeRuleFromBadge.getMaximumQualifier() );
          paxBadge.setReceivedCount( badgeRuleFromBadge.getMaximumQualifier() );
        }
        break; // pick the first rule in the loop
      }

      boolean isEarned = true;
      if ( parmBean.getDisplayProgressBar() != null && parmBean.getDisplayProgressBar().equalsIgnoreCase( "yes" ) )
      {
        isEarned = false; // set to false so the progress bar displays
        if ( !badge.getBadgeType().getCode().equals( BadgeType.PROGRESS ) )
        {
          processInvocationService.addComment( parmBean.getProcessInvocationId(),
                                               "Progress bar will not display for Badge PromotionId " + parmBean.getBadgePromotionId() + ". Badge type is " + badge.getBadgeType().getCode() );
        }
      }

      paxBadge.setEarnedDate( new Date() );
      paxBadge.setIsEarned( isEarned );
      paxBadges.add( paxBadge );

      if ( paxBadges != null && paxBadges.size() > 0 )
      {
        dataMap.put( "showBadges", "TRUE" );
        Map paxBadgeMap = new HashMap();
        paxBadgeMap = buildBadgeHtmlString( paxBadges );
        String paxBadgeEarnedString = "";
        String paxBadgeProgressString = "";
        if ( paxBadgeMap.get( "paxBadgeEarnedString" ) != null )
        {
          paxBadgeEarnedString = paxBadgeMap.get( "paxBadgeEarnedString" ).toString();
        }
        if ( paxBadgeMap.get( "paxBadgeProgressString" ) != null )
        {
          paxBadgeProgressString = paxBadgeMap.get( "paxBadgeProgressString" ).toString();
        }
        if ( !StringUtils.isEmpty( paxBadgeEarnedString ) )
        {
          String paxBadgeEarnedNoHtml = "";
          paxBadgeEarnedNoHtml = HtmlUtils.removeFormatting( paxBadgeEarnedString );
          if ( !StringUtils.isEmpty( paxBadgeEarnedNoHtml.trim() ) )
          {
            dataMap.put( "showEarnedBadges", "TRUE" );
            dataMap.put( "paxBadgeEarnedString", paxBadgeEarnedString );
            dataMap.put( "paxBadgeEarnedStringPlain", HtmlUtils.removeFormatting( paxBadgeEarnedString ) );
          }
        }
        if ( !StringUtils.isEmpty( paxBadgeProgressString ) )
        {
          String paxBadgeProgressNoHtml = "";
          paxBadgeProgressNoHtml = HtmlUtils.removeFormatting( paxBadgeProgressString );
          if ( !StringUtils.isEmpty( paxBadgeProgressNoHtml.trim() ) )
          {
            dataMap.put( "showProgressBar", "TRUE" );
            dataMap.put( "paxBadgeProgressString", paxBadgeProgressString );
            dataMap.put( "paxBadgeProgressStringPlain", HtmlUtils.removeFormatting( paxBadgeProgressString ) );
          }
        }
        if ( paxBadgeMap.get( "badgePoints" ) != null && (Long)paxBadgeMap.get( "badgePoints" ) > 1 )
        {
          dataMap.put( "manyBadgePoints", "TRUE" );
        }

        if ( paxBadgeMap.get( "badgePoints" ) != null )
        {
          String tempBadgeAwardQuantity = NumberFormatUtil.getUserLocaleBasedNumberFormat( (Long)paxBadgeMap.get( "badgePoints" ), LocaleUtils.getLocale( mailingRecipient.getLocale() ) );
          dataMap.put( "badgePoints", String.valueOf( tempBadgeAwardQuantity ) );
          dataMap.put( "badgeMediaType", paxBadgeMap.get( "badgeMediaType" ) );
        }
        if ( paxBadgeMap.get( "siteUrlPrefix" ) != null )
        {
          dataMap.put( "programUrl", paxBadgeMap.get( "siteUrlPrefix" ) );
        }
      }
    }

    if ( recognitionPromotion.getAwardStructure() == null || !recognitionPromotion.getAwardStructure().equals( RecognitionPromotion.AWARD_STRUCTURE_LEVEL ) )
    {
      dataMap.put( "claimNumber", referenceNumber );
    }

    mailingRecipient.addMailingRecipientDataFromMap( dataMap );
    return mailingRecipient;
  }

  public Map buildBadgeHtmlString( List<ParticipantBadge> paxBadges )
  {
    StringBuilder paxBadgesEarnedStr = new StringBuilder( "" );
    StringBuilder paxBadgesProgressStr = new StringBuilder( "" );
    Iterator badgeItr = paxBadges.iterator();
    String badgeImageUrl = "";
    Map paxBadgeMap = new HashMap();
    paxBadgesEarnedStr.append( "<br />" );
    while ( badgeItr.hasNext() )
    {
      ParticipantBadge paxBadge = (ParticipantBadge)badgeItr.next();
      BadgeRule rule = paxBadge.getBadgeRule();
      String languageCode = "";
      if ( paxBadge.getParticipant().getLanguageType() != null )
      {
        languageCode = paxBadge.getParticipant().getLanguageType().getCode();
      }
      else
      {
        languageCode = systemVariableService.getPropertyByName( SystemVariableService.DEFAULT_LANGUAGE ).getStringVal();
      }

      List earnedNotEarnedImageList = gamificationService.getEarnedNotEarnedImageList( rule.getBadgeLibraryCMKey(), LocaleUtils.getLocale( languageCode ) );
      Iterator itr = earnedNotEarnedImageList.iterator();
      String siteUrlPrefix = systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();

      paxBadgeMap.put( "badgePoints", rule.getBadgePoints() );
      paxBadgeMap.put( "badgeMediaType", rule.getBadgePromotion().getAwardType().getAbbr() );
      paxBadgeMap.put( "pointsEarnedBefore", false ); // set to false for this adminTest Process

      paxBadgeMap.put( "siteUrlPrefix", siteUrlPrefix );
      while ( itr.hasNext() )
      {
        BadgeLibrary badgeLib = (BadgeLibrary)itr.next();
        badgeImageUrl = siteUrlPrefix + "/" + badgeLib.getEarnedImageLarge();
      }

      String localeBasedBadgeNameFromCM = cmAssetService.getString( rule.getBadgeName(), BadgeRule.BADGE_RULES_CMASSET_TYPE_KEY, LocaleUtils.getLocale( languageCode ), true );

      if ( paxBadge.getBadgePromotion().getBadgeType().getCode().equalsIgnoreCase( BadgeType.PROGRESS ) && !paxBadge.getIsEarned() )
      {
        Long progressNumerator = 0L;
        Long progressDenominator = 0L;
        String badgeCountType = paxBadge.getBadgePromotion().getBadgeCountType().getCode();

        if ( badgeCountType != null && badgeCountType.equalsIgnoreCase( "given" ) )
        {
          progressNumerator = paxBadge.getSentCount();
        }
        else if ( badgeCountType != null && badgeCountType.equalsIgnoreCase( "received" ) )
        {
          progressNumerator = paxBadge.getReceivedCount();
        }
        else if ( badgeCountType != null && badgeCountType.equalsIgnoreCase( "total" ) )
        {
          // for adminTest Process we just need to make the progressNumerator match the maximum
          // value for the badge
          progressNumerator = paxBadge.getSentCount(); // + paxBadge.getReceivedCount();
        }
        progressDenominator = paxBadge.getBadgeRule().getMaximumQualifier();
        int progressStatus = new Double( progressNumerator * 100 / progressDenominator ).intValue();
        int progressRemaining = 100 - progressStatus;
        paxBadgesProgressStr.append( "<br />" );
        paxBadgesProgressStr.append( "<img src='" + badgeImageUrl + "' align='left' />" );
        paxBadgesProgressStr.append( "<br />" + localeBasedBadgeNameFromCM );
        paxBadgesProgressStr.append( "<table class='progress' width='100%' style='border: 1px solid #666;'><tr>" );
        paxBadgesProgressStr.append( "<td class='bar'  style='width:" + progressStatus + "%;background: #ccc;' width='" + progressStatus + "%'>" );
        paxBadgesProgressStr.append( progressNumerator + "/" + progressDenominator );
        paxBadgesProgressStr.append( "</td>" );
        paxBadgesProgressStr.append( "<td style='width:" + progressRemaining + "%;' width='" + progressRemaining + "%'> </td></tr>" );
        paxBadgesProgressStr.append( "</table>" );
      }
      else
      {
        paxBadgesEarnedStr.append( "<br />" );
        paxBadgesEarnedStr.append( "<p>" );
        paxBadgesEarnedStr.append( "<img src='" + badgeImageUrl + "'/>" );
        paxBadgesEarnedStr.append( "</p><p>" );
        paxBadgesEarnedStr.append( localeBasedBadgeNameFromCM + "</p>" );
      }
    }
    paxBadgeMap.put( "paxBadgeEarnedString", paxBadgesEarnedStr );
    paxBadgeMap.put( "paxBadgeProgressString", paxBadgesProgressStr );

    return paxBadgeMap;
  }

  /* cloned from MailingServiceImpl.buildCelebrationManagerMailingRecipient */
  public MailingRecipient buildCelebrationManagerMailingRecipient( Participant sendToUser, AdminTestProcessParmBean parmBean ) throws ServiceErrorException
  {
    MailingRecipient mailingRecipient = new MailingRecipient();

    mailingRecipient.setGuid( GuidUtils.generateGuid() );
    mailingRecipient.setUser( sendToUser );
    mailingRecipient.setLocale( parmBean.getRecipientLocale() );

    Map dataMap = new HashMap();

    Participant recipient = participantService.getParticipantByUserName( parmBean.getRecipientUserName() );
    RecognitionPromotion promotion = (RecognitionPromotion)promotionService.getPromotionById( parmBean.getPromotionId() );

    Date msgCollectExpireDateDate = com.biperf.core.utils.DateUtils.toDate( parmBean.getMsgCollectExpireDate() );
    Locale locale = CmsUtil.getLocale( mailingRecipient.getLocale() );
    ContentReader contentReader = ContentReaderManager.getContentReader();
    Content content = (Content)contentReader.getContent( "recognition.detail", locale );
    String subjectLine = StringEscapeUtils.unescapeHtml4( (String)content.getContentDataMap().get( "CELEBRATION_MANAGER_MAIL_SUBJECT" ) ) + " " + recipient.getFirstName() + " "
        + recipient.getLastName();
    dataMap.put( "subject", subjectLine );

    if ( recipient != null )
    {
      dataMap.put( "recipientFirstName", recipient.getFirstName() );
      dataMap.put( "recipientLastName", recipient.getLastName() );
    }

    if ( promotion.isIncludePurl() )
    {
      Date awardDateDate = com.biperf.core.utils.DateUtils.toDate( parmBean.getAwardDate() );
      dataMap.put( "awardDate", com.biperf.core.utils.DateUtils.toDisplayString( awardDateDate, LocaleUtils.getLocale( parmBean.getRecipientLocale() ) ) );

      int i = 1;
      if ( !StringUtil.isNullOrEmpty( parmBean.getCustomFormField1() ) )
      {
        dataMap.put( "formElement" + i, parmBean.getCustomFormField1() );
        i++;
      }
      if ( !StringUtil.isNullOrEmpty( parmBean.getCustomFormField2() ) )
      {
        dataMap.put( "formElement" + i, parmBean.getCustomFormField2() );
        i++;
      }
      if ( !StringUtil.isNullOrEmpty( parmBean.getCustomFormField3() ) )
      {
        dataMap.put( "formElement" + i, parmBean.getCustomFormField3() );
      }

      if ( promotion.isServiceAnniversary() )
      {
        if ( promotion.getAnniversaryInYears() )
        {
          if ( StringUtils.isNotEmpty( parmBean.getAnniversaryNumberOfYears() ) )
          {
            dataMap.put( "anniversaryYear", parmBean.getAnniversaryNumberOfYears() );
          }
          else
          {
            processInvocationService.addComment( parmBean.getProcessInvocationId(), "anniversaryYear not populated because anniversary number of years was not entered." );
          }
        }
        else
        {
          if ( StringUtils.isNotEmpty( parmBean.getAnniversaryNumberOfDays() ) )
          {
            dataMap.put( "anniversaryDay", parmBean.getAnniversaryNumberOfDays() );
          }
          else
          {
            processInvocationService.addComment( parmBean.getProcessInvocationId(), "anniversaryDay not populated because anniversary number of days was not entered." );
          }
        }
      }
    }
    else
    {
      dataMap.put( "awardDate", com.biperf.core.utils.DateUtils.toDisplayString( getCelebrationDeliveryDate( msgCollectExpireDateDate ), LocaleUtils.getLocale( parmBean.getRecipientLocale() ) ) );
      if ( promotion.isServiceAnniversary() )
      {
        if ( promotion.getAnniversaryInYears() )
        {
          if ( StringUtils.isNotEmpty( parmBean.getAnniversaryNumberOfYears() ) )
          {
            dataMap.put( "anniversaryYear", parmBean.getAnniversaryNumberOfYears() );
          }
          else
          {
            processInvocationService.addComment( parmBean.getProcessInvocationId(), "anniversaryYear not populated because anniversary number of years was not entered." );
          }
        }
        else
        {
          if ( StringUtils.isNotEmpty( parmBean.getAnniversaryNumberOfDays() ) )
          {
            dataMap.put( "anniversaryDay", parmBean.getAnniversaryNumberOfDays() );
          }
          else
          {
            processInvocationService.addComment( parmBean.getProcessInvocationId(), "anniversaryDay not populated because anniversary number of days was not entered." );
          }
        }
      }
    }
    if ( sendToUser != null )
    {
      dataMap.put( "managerFirstName", sendToUser.getFirstName() );
      dataMap.put( "managerLastName", sendToUser.getLastName() );
    }

    if ( promotion != null )
    {
      if ( promotion.getPromoNameAssetCode() != null )
      {
        String promotionName = cmAssetService.getString( promotion.getPromoNameAssetCode(), Promotion.PROMOTION_NAME_KEY_PREFIX, locale, true );
        dataMap.put( "promotionName", StringEscapeUtils.unescapeHtml4( promotionName ) );
      }
    }
    dataMap.put( "programWebsiteManagerMessage", "#" );
    dataMap.put( "managerContributionEndDate", com.biperf.core.utils.DateUtils.toDisplayString( msgCollectExpireDateDate, LocaleUtils.getLocale( parmBean.getRecipientLocale() ) ) );
    dataMap.put( "companyName", systemVariableService.getPropertyByName( SystemVariableService.CLIENT_NAME ).getStringVal() );

    mailingRecipient.addMailingRecipientDataFromMap( dataMap );

    return mailingRecipient;
  }

  private Date getCelebrationDeliveryDate( Date msgCollectExpireDate )
  {
    Date deliveryDate = msgCollectExpireDate != null ? msgCollectExpireDate : new Date();
    Calendar cal = Calendar.getInstance();
    cal.setTime( deliveryDate );
    cal.add( Calendar.DATE, 1 );
    deliveryDate = cal.getTime();
    return deliveryDate;
  }

  private String populateCelebrationNonPurlSubjectLine( String promoNameAssetCode,
                                                        Locale locale,
                                                        Content content,
                                                        ContentReader contentReader,
                                                        ClaimRecipient claimRecipient,
                                                        boolean isClaimRecipientThePersonBeingRecognized )
  {
    String programName = "";
    if ( promoNameAssetCode != null )
    {
      String promotionName = cmAssetService.getString( promoNameAssetCode, Promotion.PROMOTION_NAME_KEY_PREFIX, locale, true );
      programName = StringEscapeUtils.unescapeHtml4( promotionName );
    }
    String subjectLine = StringEscapeUtils.unescapeHtml4( (String)content.getContentDataMap().get( "CELEBRATION_MAIL_SUBJECT1" ) ) + " " + programName + " "
        + StringEscapeUtils.unescapeHtml4( (String)content.getContentDataMap().get( "CELEBRATION_MAIL_SUBJECT2" ) );

    if ( !isClaimRecipientThePersonBeingRecognized )
    {
      subjectLine = StringEscapeUtils.unescapeHtml4( (String)content.getContentDataMap().get( "CELEBRATION_OTHER_MAIL_SUBJECT1" ) ) + " " + claimRecipient.getFirstName() + " "
          + claimRecipient.getLastName() + " " + StringEscapeUtils.unescapeHtml4( (String)content.getContentDataMap().get( "CELEBRATION_OTHER_MAIL_SUBJECT2" ) + " " + programName + " "
              + StringEscapeUtils.unescapeHtml4( (String)content.getContentDataMap().get( "CELEBRATION_OTHER_MAIL_SUBJECT3" ) ) );
    }
    return subjectLine;
  }

  private void populateCelebrationMessageMap( Map dataMap,
                                              User sendToUser,
                                              RecognitionPromotion recognitionPromotion,
                                              ClaimRecipient claimRecipient,
                                              Long claimId,
                                              boolean includePurl,
                                              AdminTestProcessParmBean parmBean )
  {
    String localeCode = systemVariableService.getPropertyByName( SystemVariableService.DEFAULT_LANGUAGE ).getStringVal();
    Participant recipient = claimRecipient.getRecipient();
    if ( recipient.getLanguageType() != null )
    {
      localeCode = recipient.getLanguageType().getCode();
    }
    if ( includePurl )
    {
      int numOfDays = systemVariableService.getPropertyByName( SystemVariableService.PURL_DAYS_TO_EXP ).getIntVal();
      Date awardDateDate = com.biperf.core.utils.DateUtils.toDate( parmBean.getAwardDate() );
      Date purlExpireDate = new Date( awardDateDate.getTime() + numOfDays * DateUtils.MILLIS_PER_DAY );
      dataMap.put( "purlExpireDate", com.biperf.core.utils.DateUtils.toDisplayString( purlExpireDate, LocaleUtils.getLocale( localeCode ) ) );

      int i = 1;
      if ( !StringUtil.isNullOrEmpty( parmBean.getCustomFormField1() ) )
      {
        dataMap.put( "formElement" + i, parmBean.getCustomFormField1() );
        i++;
      }
      if ( !StringUtil.isNullOrEmpty( parmBean.getCustomFormField2() ) )
      {
        dataMap.put( "formElement" + i, parmBean.getCustomFormField2() );
        i++;
      }
      if ( !StringUtil.isNullOrEmpty( parmBean.getCustomFormField3() ) )
      {
        dataMap.put( "formElement" + i, parmBean.getCustomFormField3() );
      }

      dataMap.put( "purlRecipientLink", "#" );

    }
    dataMap.put( "recipientFirstName", sendToUser.getFirstName() );

    boolean serviceAnniversaryEnabled = false;
    if ( recognitionPromotion.isIncludeCelebrations() && recognitionPromotion.isServiceAnniversary() )
    {
      serviceAnniversaryEnabled = true;
    }
    if ( serviceAnniversaryEnabled )
    {
      dataMap.put( "serviceAnniversary", "TRUE" );
    }
    if ( !serviceAnniversaryEnabled )
    {
      dataMap.put( "noServiceAnniversary", "TRUE" );
    }

    if ( serviceAnniversaryEnabled )
    {
      if ( recognitionPromotion.getAnniversaryInYears() )
      {
        if ( StringUtils.isNotEmpty( parmBean.getAnniversaryNumberOfYears() ) )
        {
          dataMap.put( "anniversaryYear", parmBean.getAnniversaryNumberOfYears() );
        }
        else
        {
          processInvocationService.addComment( parmBean.getProcessInvocationId(), "anniversaryYear not populated because anniversary number of years was not entered." );
        }
      }
      else
      {
        if ( StringUtils.isNotEmpty( parmBean.getAnniversaryNumberOfDays() ) )
        {
          dataMap.put( "anniversaryDay", parmBean.getAnniversaryNumberOfDays() );
        }
        else
        {
          processInvocationService.addComment( parmBean.getProcessInvocationId(), "anniversaryDay not populated because anniversary number of days was not entered." );
        }
      }
    }

    if ( StringUtils.isNotEmpty( parmBean.getCelebManagerMessage() ) )
    {
      if ( StringUtils.isNotEmpty( parmBean.getCelebManagerMessage() ) )
      {
        if ( StringUtils.isNotEmpty( parmBean.getManagerUserName() ) )
        {
          Participant manager = participantService.getParticipantByUserName( parmBean.getManagerUserName() );
          dataMap.put( "managerMessage", "TRUE" );
          dataMap.put( "managerFirstName", manager.getFirstName() );
          dataMap.put( "managerLastName", manager.getLastName() );
          dataMap.put( "managerMessageText", parmBean.getCelebManagerMessage() );
        }
        else
        {
          processInvocationService.addComment( parmBean.getProcessInvocationId(), "Manager comment not displayed because manager was not selected." );
        }
      }

      if ( StringUtils.isNotEmpty( parmBean.getCelebManagerAboveMessage() ) )
      {
        if ( StringUtils.isNotEmpty( parmBean.getManagerAboveUserName() ) )
        {
          Participant managerAbove = participantService.getParticipantByUserName( parmBean.getManagerAboveUserName() );
          dataMap.put( "managerAboveMessage", "TRUE" );
          dataMap.put( "managerAboveFirstName", managerAbove.getFirstName() );
          dataMap.put( "managerAboveLastName", managerAbove.getLastName() );
          dataMap.put( "managerAboveMessageText", parmBean.getCelebManagerAboveMessage() );
        }
        else
        {
          processInvocationService.addComment( parmBean.getProcessInvocationId(), "Manager Above comment not displayed because manager above was not selected." );
        }
      }
    }
    dataMap.put( "celebrationLink", "#" );

    if ( recognitionPromotion.getVideoPath() != null )
    {
      String mediaName = recognitionPromotion.getVideoPath().getCode();
      String celebrationCorporateVideoUrl = systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal() + "/videos/celebration/" + mediaName + "/"
          + mediaName;
      dataMap.put( "celebrationCorporateVideoUrl", celebrationCorporateVideoUrl );
    }
    if ( recognitionPromotion.isAwardActive() )
    {
      dataMap.put( "award", "TRUE" );
    }

    Long displayPeriod = recognitionPromotion.getCelebrationDisplayPeriod();
    if ( displayPeriod != null )
    {
      String expireDateStr = getCelebrationExpireDateForRecipient( recipient, displayPeriod, localeCode );
      dataMap.put( "expireDate", expireDateStr );
    }
  }

  private String getCelebrationExpireDateForRecipient( Participant recipient, Long displayPeriod, String localeCode )
  {
    String datePattern = DateFormatterUtil.getOracleDatePattern( localeCode );
    SimpleDateFormat sdf = new SimpleDateFormat( datePattern );
    Calendar c = Calendar.getInstance();
    c.setTime( new Date() );
    c.add( Calendar.DATE, displayPeriod.intValue() );
    String recipientTimeZoneID = userService.getUserTimeZoneByUserCountry( recipient.getId() );
    Date recipientExpirationDate = com.biperf.core.utils.DateUtils.applyTimeZone( c.getTime(), recipientTimeZoneID );
    String expireDateStr = com.biperf.core.utils.DateUtils.toDisplayString( recipientExpirationDate );
    return expireDateStr;
  }

  public void setProcessInvocationService( ProcessInvocationService processInvocationService )
  {
    this.processInvocationService = processInvocationService;
  }

  public void setPromotionService( PromotionService promotionService )
  {
    this.promotionService = promotionService;
  }

  public void setParticipantService( ParticipantService participantService )
  {
    this.participantService = participantService;
  }

  public void setCmAssetService( CMAssetService cmAssetService )
  {
    this.cmAssetService = cmAssetService;
  }

  public void setSystemVariableService( SystemVariableService systemVariableService )
  {
    this.systemVariableService = systemVariableService;
  }

  public void setGamificationService( GamificationService gamificationService )
  {
    this.gamificationService = gamificationService;
  }

  public void setUserService( UserService userService )
  {
    this.userService = userService;
  }

}

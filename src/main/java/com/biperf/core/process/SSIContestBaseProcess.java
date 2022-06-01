
package com.biperf.core.process;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.biperf.core.domain.enums.LanguageType;
import com.biperf.core.domain.enums.MailingType;
import com.biperf.core.domain.enums.PromotionEmailNotificationType;
import com.biperf.core.domain.enums.SSIActivityMeasureType;
import com.biperf.core.domain.gamification.BadgeLibrary;
import com.biperf.core.domain.mailing.Mailing;
import com.biperf.core.domain.mailing.MailingRecipient;
import com.biperf.core.domain.message.Message;
import com.biperf.core.domain.participant.AudienceParticipant;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.PromotionNotificationType;
import com.biperf.core.domain.ssi.SSIContest;
import com.biperf.core.domain.ssi.SSIContestAwardThemNow;
import com.biperf.core.domain.ssi.SSIContestManager;
import com.biperf.core.domain.ssi.SSIContestParticipant;
import com.biperf.core.domain.user.User;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.cms.CMAssetService;
import com.biperf.core.service.promotion.PromotionNotificationService;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.service.ssi.SSIContestParticipantService;
import com.biperf.core.service.ssi.SSIContestService;
import com.biperf.core.service.ssi.SSIPromotionService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.GuidUtils;
import com.biperf.core.utils.SSIContestUtil;
import com.biperf.core.utils.StringUtil;
import com.biperf.core.value.FormattedValueBean;
import com.biperf.core.value.ssi.SSIContestPaxProgressDetailValueBean;
import com.biperf.core.value.ssi.SSIContestProgressValueBean;
import com.biperf.core.value.ssi.SSIContestStackRankPaxValueBean;
import com.biperf.core.value.ssi.SSIContestStackRankTeamValueBean;
import com.biperf.core.value.ssi.SSIContestUniqueCheckValueBean;
import com.biperf.core.value.ssi.SSIPaxContestLevelValueBean;
import com.objectpartners.cms.util.CmsResourceBundle;
import com.objectpartners.cms.util.CmsUtil;

/**
 * 
 * SSIContestNotificationProcess.
 * 
 * @author kumars
 */
public abstract class SSIContestBaseProcess extends BaseProcessImpl
{
  public static final String BEAN_NAME = "ssiContestNotificationProcess";

  protected static final int STACK_RANK_CURRENT_PAGE = 1;
  protected static final int TOTAL_STACK_RANK_RECORDS_PER_PAGE = 3;

  protected static final String PARAGRAPH_START = "<p>";
  protected static final String PARAGRAPH_END = "</p>";
  protected static final String LINE_BREAK = "<br/>";
  protected static final String STRONG_START = "<strong>";
  protected static final String STRONG_END = "</strong>";
  protected static final String WHITESPACE = " ";
  protected static final String SSI_CONTEST_PAX_CM_ASSET_CODE = "ssi_contest.participant";

  // general keys
  protected static final String KEY_CONTEST_NAME = "contestName";
  protected static final String KEY_CONTEST_START_DATE = "contestStartDate";
  protected static final String KEY_CONTEST_END_DATE = "contestEndDate";
  protected static final String KEY_ACTIVITY_DESCRIPTION = "activityDescription";
  protected static final String KEY_MEASURE_TYPE = "measureType";
  protected static final String KEY_PAYOUT_TYPE = "payoutType";
  protected static final String KEY_CONTEST_CREATOR_NAME = "contestCreatorName";
  protected static final String KEY_IS_INCLUDE_PERSONAL_MESSAGE = "isIncludePersonalMessage";
  protected static final String KEY_PERSONAL_MESSAGE = "personalMessage";
  protected static final String KEY_CONTEST_END_DAYS_REMAING = "contestEndDaysRemaing";
  protected static final String KEY_INCLUDE_BADGE = "includeBadge";
  protected static final String KEY_BADGE_IMAGE = "badgeImage";
  protected static final String KEY_BADGE_NAME = "badgeName";

  private static final String KEY_CONTEST_LAST_UPDATED = "contestLastUpdated";
  private static final String KEY_CONTEST_PROGRESS_LAST_UPDATED = "contestProgressLastUpdated";
  private static final String KEY_IS_INCLUDE_BONUS = "isIncludeBonus";
  protected static final String KEY_IS_INCLUDE_STACK_RANK = "isIncludeStackRank";

  private static final String KEY_BONUS_PAYOUT = "bonusPayout";
  private static final String KEY_BONUS_EARNED = "bonusEarned";
  protected static final String KEY_OBJECTIVES_TOTAL = "objectivesTotal";
  private static final String KEY_FOR_EVERY = "forEvery";
  protected static final String KEY_FINAL_ACTIVITY_RESULTS = "finalActivityResults";
  //
  protected static final String KEY_PROGRAM_URL = "programUrl";
  private static final String KEY_PROMOTION_NAME = "promotionName";
  protected static final String KEY_FIRST_NAME = "firstName";

  // objectives related keys
  protected static final String KEY_IS_OBJECTIVES_SAME = "isObjectivesSame";
  protected static final String KEY_PAYOUT_QUANTITY = "payoutQuantity";
  protected static final String KEY_PAYOUT_AMOUNT = "payoutAmount";
  protected static final String KEY_AWARD_AMOUNT = "awardAmount";
  protected static final String KEY_PAYOUT_DESCRIPTION = "payoutDescription";
  private static final String KEY_IS_ACHIEVED_OBJECTIVE = "isAchievedObjective";
  protected static final String KEY_OBJECTIVE_AMOUNT = "objectiveAmount";
  protected static final String KEY_TOTAL_AMOUNT_TO_GO = "totalAmountToGo";
  protected static final String KEY_PROGRESS_TO_DATE = "progressToDate";
  protected static final String KEY_ACTIVITY_GOAL_AMOUNT = "activityGoalAmount";
  protected static final String KEY_PERCENTAGE_TO_GOAL = "percentageToGoal";
  protected static final String KEY_PERCENTAGE_TO_OBJECTIVE = "percentageToObjective";
  protected static final String KEY_TO_GO_GOAL = "toGoGoal";
  private static final String KEY_CONTEST_GOAL = "contestGoal";
  protected static final String KEY_INDIVIDUAL_STACK_RANK = "individualStackRank";
  protected static final String KEY_IS_OTHER = "isOther";

  // Step It Up
  protected static final String KEY_IS_LEVEL_ACHIEVED = "isLevelAchieved";
  protected static final String KEY_ACHIEVED_HIGHEST_LEVEL = "achievedHighestLevel";
  protected static final String KEY_HIGHEST_LEVEL = "highestLevelName";
  protected static final String KEY_AMOUNT_FOR_CURRENT_LEVEL = "amountForCurrentLevel";
  protected static final String KEY_LEVEL_ACHIEVED_NAME = "levelAchievedName";
  protected static final String KEY_AMOUNT_FOR_NEXT_LEVEL = "amountForNextLevel";
  protected static final String KEY_NONE_LEVEL_ACHIEVED = "noneAchieved";
  //
  protected static final String KEY_TEAM_MEM_ACHIEVED_CNT = "teamMembersAchievedCnt";
  protected static final String KEY_TOTAL_TEAM_MEM_CNT = "totalTeamMembersCnt";
  protected static final String KEY_FINAL_PAYOUT_AMOUNT = "finalPayoutAmount";

  // Award them now keys
  protected static final String KEY_MANAGER_FIRST_NAME = "managerFirstName";
  protected static final String KEY_AWARD_VALUE = "awardValue";
  protected static final String KEY_AWARD_NOTIFICATION_MESSAGE = "awardNotificationMessage";
  protected static final String KEY_IS_AWARD_NOTIFICATION_MESSAGE = "isAwardNotificationMessage";
  protected static final String KEY_CONTEST_DESCRIPTION = "contestDescription";
  protected static final String KEY_PROGRAM_NAME = "programName";
  protected static final String KEY_IS_POINTS = "isPoints";
  protected static final String KEY_DATE = "date";
  protected static final String KEY_PARTICIPANT_COUNT = "participantCount";
  protected static final String KEY_ACTIVITY_AMOUNT = "activityAmount";
  protected static final String KEY_IS_UNIQUE_DESCRIPTION = "isUniqueDescription";
  protected static final String KEY_AWARD_CURRENCY = "awardAmountAndCurrency";

  protected static final String COPY_NOTE_CREATOR = "copyNoteToCreator";

  protected CMAssetService cmAssetService;
  protected SSIContestService ssiContestService;
  protected SSIPromotionService ssiPromotionService;
  protected PromotionService promotionService;
  protected SSIContestParticipantService ssiContestParticipantService;
  protected PromotionNotificationService promotionNotificationService;

  private String isSSIAdmin = "false";

  @SuppressWarnings( "unchecked" )
  protected ArrayList<PromotionNotificationType> getPromotionNotificationsByPromotionId( SSIContest contest )
  {
    return (ArrayList<PromotionNotificationType>)promotionNotificationService.getPromotionTypeNotificationsByPromotionId( contest.getPromotion().getId() );
  }

  protected SSIContestUniqueCheckValueBean getContestUniqueCheckValueBean( SSIContest contest ) throws ServiceErrorException
  {
    return contest.getContestType().isObjectives() ? ssiContestParticipantService.performUniqueCheck( contest.getId() ) : null;
  }

  // Participant, Objectives Contest Type
  protected Map<String, Object> objectivesEndAndFinalResultsForParticipants( SSIContestParticipant contestParticipant, SSIContest contest ) throws ServiceErrorException
  {
    int precision = getPrecision( contest );
    Map<String, Object> dataMap = new HashMap<String, Object>();
    SSIContestPaxProgressDetailValueBean paxProgressValueBean = ssiContestParticipantService.getContestParticipantProgress( contest.getId(), contestParticipant.getParticipant().getId() );
    if ( contest.getSameObjectiveDescription() )
    {
      dataMap.put( KEY_ACTIVITY_DESCRIPTION, contest.getActivityDescription() );
    }
    else
    {
      dataMap.put( KEY_ACTIVITY_DESCRIPTION, contestParticipant.getActivityDescription() );
    }
    dataMap.put( KEY_OBJECTIVE_AMOUNT, SSIContestUtil.getFormattedValue( paxProgressValueBean.getObjectiveAmount(), precision ) + getActivitySuffix( contest ) );
    dataMap.put( KEY_PROGRESS_TO_DATE,
                 SSIContestUtil.getFormattedValue( paxProgressValueBean.getActivityAmount() != null ? paxProgressValueBean.getActivityAmount() : new Double( 0 ), precision )
                     + getActivitySuffix( contest ) );
    dataMap.put( KEY_FINAL_ACTIVITY_RESULTS,
                 SSIContestUtil.getFormattedValue( paxProgressValueBean.getActivityAmount() != null ? paxProgressValueBean.getActivityAmount() : new Double( 0 ), precision )
                     + getActivitySuffix( contest ) );
    dataMap.put( KEY_TOTAL_AMOUNT_TO_GO, SSIContestUtil.getFormattedValue( paxProgressValueBean.getToGoAmount(), precision ) + getActivitySuffix( contest ) );
    if ( contest.isIncludeStackRank() )
    {
      dataMap.put( KEY_IS_INCLUDE_STACK_RANK, String.valueOf( Boolean.TRUE ) );
    }
    dataMap.put( KEY_INDIVIDUAL_STACK_RANK, "#" + paxProgressValueBean.getStackRank() );
    if ( paxProgressValueBean.getTotalPax() != null )
    {
      dataMap.put( KEY_PARTICIPANT_COUNT, String.valueOf( paxProgressValueBean.getTotalPax() ) );
    }
    else
    {
      dataMap.put( KEY_PARTICIPANT_COUNT, paxProgressValueBean.getTotalPax() );
    }

    Long percentageToObjective = paxProgressValueBean.getPercentageAcheived() != null ? paxProgressValueBean.getPercentageAcheived().longValue() : null;
    dataMap.put( KEY_PERCENTAGE_TO_OBJECTIVE, percentageToObjective + "%" );
    dataMap.put( KEY_AWARD_AMOUNT, SSIContestUtil.getFormattedValue( paxProgressValueBean.getObjectivePayout(), SSIContestUtil.PAYOUT_DECIMAL_PRECISION ) + getPayoutSuffix( contest ) );

    if ( contest.isIncludeBonus() )
    {
      if ( paxProgressValueBean.getBonusEarned() != null )
      {
        dataMap.put( KEY_IS_INCLUDE_BONUS, String.valueOf( Boolean.TRUE ) );
      }
      dataMap.put( KEY_BONUS_EARNED, SSIContestUtil.getFormattedValue( paxProgressValueBean.getBonusEarned(), SSIContestUtil.PAYOUT_DECIMAL_PRECISION ) + getPayoutSuffix( contest ) );
      dataMap.put( KEY_FOR_EVERY, StringUtils.stripEnd( SSIContestUtil.getFormattedValue( paxProgressValueBean.getObjectiveBonusIncrement(), precision ) + getActivitySuffix( contest ), null ) );
      dataMap.put( KEY_BONUS_PAYOUT, SSIContestUtil.getFormattedValue( paxProgressValueBean.getObjectiveBonusPayout(), SSIContestUtil.PAYOUT_DECIMAL_PRECISION ) + getPayoutSuffix( contest ) );
    }
    if ( new Double( 0 ).equals( paxProgressValueBean.getToGoAmount() ) )
    {
      dataMap.put( KEY_IS_ACHIEVED_OBJECTIVE, String.valueOf( Boolean.TRUE ) );
      if ( contest.getPayoutType().isPoints() )
      {
        dataMap.put( KEY_IS_POINTS, String.valueOf( Boolean.TRUE ) );
        dataMap.put( KEY_PAYOUT_AMOUNT, SSIContestUtil.getFormattedValue( paxProgressValueBean.getObjectivePayout(), SSIContestUtil.PAYOUT_DECIMAL_PRECISION ) + getPayoutSuffix( contest ) );
      }
      if ( contest.getPayoutType().isOther() )
      {
        dataMap.put( KEY_IS_OTHER, String.valueOf( Boolean.TRUE ) );
        dataMap.put( KEY_PAYOUT_DESCRIPTION, paxProgressValueBean.getObjectivePayoutDescription() );
      }
    }
    return dataMap;
  }

  // Participant, Step It Up Contest Type
  protected Map<String, Object> setStepItUpContestDataParticipant( Participant participant, SSIContest contest ) throws ServiceErrorException
  {
    int precision = getPrecision( contest );
    SSIContestPaxProgressDetailValueBean paxProgressValueBean = ssiContestParticipantService.getContestParticipantProgress( contest.getId(), participant.getId() );
    Map<String, Object> dataMap = new HashMap<String, Object>();

    String nextHighestLevel;
    SSIPaxContestLevelValueBean levelAchieved = null;
    SSIPaxContestLevelValueBean currentLevel = null;
    SSIPaxContestLevelValueBean highestLevel = paxProgressValueBean.getLevels().get( paxProgressValueBean.getLevels().size() - 1 );

    // filtering the current level
    boolean currentLevelFound = false;
    for ( SSIPaxContestLevelValueBean vb : paxProgressValueBean.getLevels() )
    {
      if ( currentLevelFound )
      {
        vb.setCurrentLevel( false );
      }
      if ( vb.getCurrentLevel() != null && vb.getCurrentLevel() )
      {
        currentLevelFound = true;
      }
    }

    // finding current level
    for ( SSIPaxContestLevelValueBean level : paxProgressValueBean.getLevels() )
    {
      if ( level.getCurrentLevel() )
      {
        currentLevel = level;
        break;
      }
    }

    // finding highest level achieved
    if ( currentLevel != null )
    {
      // if achieved some level
      if ( Integer.parseInt( currentLevel.getName() ) != 1 )
      {
        if ( Integer.parseInt( currentLevel.getName() ) == Integer.parseInt( highestLevel.getName() ) && currentLevel.getCompleted() )
        {
          levelAchieved = currentLevel;// or highest level achieved
        }
        else
        {
          levelAchieved = paxProgressValueBean.getLevels().get( Integer.parseInt( currentLevel.getName() ) - 2 );
        }
      }
    }

    // completed highest level
    if ( currentLevel == null && highestLevel.getCompleted() )
    {
      dataMap.put( KEY_ACHIEVED_HIGHEST_LEVEL, String.valueOf( Boolean.TRUE ) );
      nextHighestLevel = MessageFormat.format( CmsResourceBundle.getCmsBundle().getString( "ssi_contest.approvals.detail.LEVEL" ), highestLevel.getName() );
      dataMap.put( KEY_HIGHEST_LEVEL, nextHighestLevel );
      dataMap.put( KEY_AMOUNT_FOR_CURRENT_LEVEL, SSIContestUtil.getFormattedValue( highestLevel.getPayout(), SSIContestUtil.PAYOUT_DECIMAL_PRECISION ) + getPayoutSuffix( contest ) );
      if ( contest.isIncludeBonus() )
      {
        dataMap.put( KEY_BONUS_EARNED, SSIContestUtil.getFormattedValue( highestLevel.getBonusEarned(), SSIContestUtil.PAYOUT_DECIMAL_PRECISION ) + getPayoutSuffix( contest ) );
      }
      if ( !StringUtil.isNullOrEmpty( highestLevel.getBadgeName() ) && !StringUtil.isNullOrEmpty( highestLevel.getBadgeUrl() ) )
      {
        String siteUrlPrefix = getSystemVariableService().getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();
        dataMap.put( KEY_INCLUDE_BADGE, String.valueOf( Boolean.TRUE ) );
        dataMap.put( KEY_BADGE_NAME, highestLevel.getBadgeName() );
        dataMap.put( KEY_BADGE_IMAGE, siteUrlPrefix + highestLevel.getBadgeUrl() );
      }
    }
    else if ( levelAchieved != null )
    {
      // if atleast one level achieved
      nextHighestLevel = MessageFormat.format( CmsResourceBundle.getCmsBundle().getString( "ssi_contest.approvals.detail.LEVEL" ), currentLevel.getName() );
      dataMap.put( KEY_IS_LEVEL_ACHIEVED, String.valueOf( Boolean.TRUE ) );
      dataMap.put( KEY_LEVEL_ACHIEVED_NAME, MessageFormat.format( CmsResourceBundle.getCmsBundle().getString( "ssi_contest.approvals.detail.LEVEL" ), levelAchieved.getName() ) );
      dataMap.put( KEY_HIGHEST_LEVEL, nextHighestLevel );
      dataMap.put( KEY_AMOUNT_FOR_CURRENT_LEVEL, SSIContestUtil.getFormattedValue( levelAchieved.getPayout(), SSIContestUtil.PAYOUT_DECIMAL_PRECISION ) + getPayoutSuffix( contest ) );
      if ( !StringUtil.isNullOrEmpty( levelAchieved.getBadgeName() ) && !StringUtil.isNullOrEmpty( levelAchieved.getBadgeUrl() ) )
      {
        String siteUrlPrefix = getSystemVariableService().getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();
        dataMap.put( KEY_INCLUDE_BADGE, String.valueOf( Boolean.TRUE ) );
        dataMap.put( KEY_BADGE_NAME, levelAchieved.getBadgeName() );
        dataMap.put( KEY_BADGE_IMAGE, siteUrlPrefix + levelAchieved.getBadgeUrl() );
      }
    }
    else
    {
      // not even one level achieved, progressing towards first level
      dataMap.put( KEY_NONE_LEVEL_ACHIEVED, String.valueOf( Boolean.TRUE ) );
      nextHighestLevel = MessageFormat.format( CmsResourceBundle.getCmsBundle().getString( "ssi_contest.approvals.detail.LEVEL" ), paxProgressValueBean.getLevels().get( 0 ).getName() );
      dataMap.put( KEY_HIGHEST_LEVEL, nextHighestLevel );
    }

    // amount and progress to date
    dataMap.put( KEY_AMOUNT_FOR_NEXT_LEVEL,
                 SSIContestUtil.getFormattedValue( currentLevel != null ? currentLevel.getRemaining() : paxProgressValueBean.getLevels().get( 0 ).getRemaining(), precision )
                     + getActivitySuffix( contest ) );

    Double currentAchieved = currentLevel != null ? currentLevel.getProgress() : paxProgressValueBean.getLevels().get( 0 ).getProgress();
    String currentAchievedPrecise = SSIContestUtil.getFormattedValue( currentAchieved != null ? currentAchieved : new Double( 0 ), precision );
    dataMap.put( KEY_FINAL_ACTIVITY_RESULTS, currentAchievedPrecise + getActivitySuffix( contest ) );

    Double progress = currentLevel != null ? currentLevel.getProgress() : highestLevel.getProgress();
    dataMap.put( KEY_PROGRESS_TO_DATE, SSIContestUtil.getFormattedValue( progress != null ? progress : new Double( 0 ), precision ) + getActivitySuffix( contest ) );

    // stack rank enabled add stack rank values
    if ( contest.isIncludeStackRank() )
    {
      dataMap.put( KEY_INDIVIDUAL_STACK_RANK, "#" + String.valueOf( paxProgressValueBean.getLevels().get( 0 ).getStackRank() ) );
      dataMap.put( KEY_TOTAL_TEAM_MEM_CNT, String.valueOf( paxProgressValueBean.getLevels().get( 0 ).getParticipantsCount() ) );
      dataMap.put( KEY_IS_INCLUDE_STACK_RANK, String.valueOf( Boolean.TRUE ) );
    }

    // bonus enabled add bonus values
    if ( contest.isIncludeBonus() )
    {
      dataMap.put( KEY_IS_INCLUDE_BONUS, String.valueOf( Boolean.TRUE ) );
      dataMap.put( KEY_FOR_EVERY, StringUtils.stripEnd( SSIContestUtil.getFormattedValue( contest.getStepItUpBonusIncrement(), precision ) + getActivitySuffix( contest ), null ) );
      dataMap.put( KEY_BONUS_PAYOUT, SSIContestUtil.getFormattedValue( contest.getStepItUpBonusPayout(), SSIContestUtil.PAYOUT_DECIMAL_PRECISION ) + getPayoutSuffix( contest ) );
    }
    return dataMap;
  }

  protected List<SSIContestProgressValueBean> getProgressData( Long contestId, Long userId ) throws ServiceErrorException
  {
    return ssiContestParticipantService.getContestProgress( contestId, userId );
  }

  protected int getPrecision( SSIContest contest )
  {
    return SSIContestUtil.getPrecision( contest.getActivityMeasureType().getCode() );
  }

  protected void submitMailing( SSIContest contest, Participant creator, Map<String, Object> dataMap, Message message, MailingType mailingType )
  {
    // Contest creator and recipient is the same participant
    // This applies for the contest creator messages
    submitMailing( contest, creator, dataMap, message, mailingType, creator );
  }

  protected void submitMailing( SSIContest contest, Participant creator, Map<String, Object> dataMap, Message message, MailingType mailingType, Participant recipient )
  {
    Mailing mailing = composeMail();
    mailing.setMessage( message );
    mailing.setMailingType( mailingType );
    MailingRecipient mailingRecipient = getMailingRecipient( recipient );
    Map<String, Object> objectMap = getContestGeneralInfo( contest, mailingRecipient.getLocale(), creator, recipient );
    if ( SSIContestUtil.canAddSSIAdminInEMailNotificxations( contest.getId() ) != null )
    {
      if ( message.getCmAssetCode().equals( PromotionEmailNotificationType.CONTEST_FINAL_RESULT_NOTIFY_CREATOR_OBJECTIVES )
          || message.getCmAssetCode().equals( PromotionEmailNotificationType.CONTEST_FINAL_RESULT_NOTIFY_CREATOR_DO_THIS_GET_THAT )
          || message.getCmAssetCode().equals( PromotionEmailNotificationType.CONTEST_FINAL_RESULT_NOTIFY_CREATOR_STACK_RANK )
          || message.getCmAssetCode().equals( PromotionEmailNotificationType.CONTEST_FINAL_RESULT_NOTIFY_CREATOR_STEP_IT_UP ) )
      {
        if ( isSSIAdmin.equals( "true" ) )
        {
          objectMap.put( "ssiCreatorEMailCopyNotes", String.valueOf( true ) );
        }
      }
    }
    mailingRecipient.addMailingRecipientDataFromMap( objectMap );
    mailingRecipient.addMailingRecipientDataFromMap( dataMap );
    mailing.addMailingRecipient( mailingRecipient );
    MailingRecipient mailingSSIAdminRecipient = null;
    if ( SSIContestUtil.canAddSSIAdminInEMailNotificxations( contest.getId() ) != null && creator.getId().equals( recipient.getId() ) )
    {
      mailingSSIAdminRecipient = getMailingRecipient( recipient );
      mailingSSIAdminRecipient.setUser( null );
      objectMap = getContestGeneralInfo( contest, systemVariableService.getDefaultLanguage().getStringVal(), creator, recipient );
      if ( message.getCmAssetCode().equals( PromotionEmailNotificationType.CONTEST_FINAL_RESULT_NOTIFY_CREATOR_OBJECTIVES )
          || message.getCmAssetCode().equals( PromotionEmailNotificationType.CONTEST_FINAL_RESULT_NOTIFY_CREATOR_DO_THIS_GET_THAT )
          || message.getCmAssetCode().equals( PromotionEmailNotificationType.CONTEST_FINAL_RESULT_NOTIFY_CREATOR_STACK_RANK )
          || message.getCmAssetCode().equals( PromotionEmailNotificationType.CONTEST_FINAL_RESULT_NOTIFY_CREATOR_STEP_IT_UP ) )
      {
        if ( isSSIAdmin.equals( "false" ) )
        {
          objectMap.put( "ssiAdminEMailCopyNotes", String.valueOf( true ) );
        }
      }
      mailingSSIAdminRecipient.addMailingRecipientDataFromMap( objectMap );
      mailingSSIAdminRecipient.addMailingRecipientDataFromMap( dataMap );
      mailingSSIAdminRecipient
          .setPreviewEmailAddress( getUserService().getPrimaryUserEmailAddress( SSIContestUtil.canAddSSIAdminInEMailNotificxations( contest.getId() ).getUserID() ).getEmailAddr() );
      mailing.addMailingRecipient( mailingSSIAdminRecipient );
    }
    mailingService.submitMailing( mailing, null );
  }

  protected Participant getContestCreator( SSIContest contest )
  {
    return participantService.getParticipantById( contest.getCreatorId() );
  }

  protected String getContestName( SSIContest contest, String mailingRecipientLocale )
  {
    return cmAssetService.getString( contest.getCmAssetCode(), SSIContest.CONTEST_CMASSET_NAME, getLocale( mailingRecipientLocale ), true );
  }

  protected Locale getLocale( String locale )
  {
    return CmsUtil.getLocale( locale );
  }

  protected Locale getLocale( Participant participant )
  {
    LanguageType languageType = participant.getLanguageType();
    String languageCode = languageType != null ? languageType.getCode() : systemVariableService.getDefaultLanguage().getStringVal();
    return CmsUtil.getLocale( languageCode );
  }

  protected String getActivityAmountSuffix( SSIContestAwardThemNow awardThemNowContest )
  {
    return SSIActivityMeasureType.CURRENCY_CODE.equals( awardThemNowContest.getContest().getActivityMeasureType().getCode() )
        ? " " + awardThemNowContest.getContest().getActivityMeasureCurrencyCode().toUpperCase()
        : "";
  }

  /**
   * @param paxs Set of Participant objects
   * @return Set containing MailingRecipient objects for mailing
   */
  protected List<Participant> getRecipients( Set paxs )
  {
    List<Participant> participants = new ArrayList<Participant>();
    for ( Object temp : paxs )
    {
      Participant participant = null;

      if ( temp instanceof Participant )
      {
        participant = (Participant)temp;
      }
      else if ( temp instanceof AudienceParticipant )
      {
        participant = ( (AudienceParticipant)temp ).getParticipant();
      }
      else if ( temp instanceof FormattedValueBean )
      {
        participant = participantService.getParticipantById( ( (FormattedValueBean)temp ).getId() );
      }
      else if ( temp instanceof SSIContestManager )
      {
        participant = ( (SSIContestManager)temp ).getManager();
      }
      else if ( temp instanceof SSIContestParticipant )
      {
        participant = ( (SSIContestParticipant)temp ).getParticipant();
      }
      participants.add( participant );
    }
    return participants;
  }

  protected MailingRecipient getMailingRecipient( User user )
  {
    LanguageType languageType = user.getLanguageType();
    MailingRecipient mailingRecipient = new MailingRecipient();
    mailingRecipient.setGuid( GuidUtils.generateGuid() );
    mailingRecipient.setLocale( languageType != null ? languageType.getCode() : systemVariableService.getDefaultLanguage().getStringVal() );
    mailingRecipient.setUser( user );
    return mailingRecipient;
  }

  protected Map<String, Object> setContestProgressStackRankData( Participant participant,
                                                                 int decimalPrecision,
                                                                 List<SSIContestStackRankPaxValueBean> ssiContestStackRankPaxValueBeanList,
                                                                 SSIContest contest )
  {
    Locale locale = CmsUtil.getLocale( participant.getLanguageType() != null ? participant.getLanguageType().getCode() : systemVariableService.getDefaultLanguage().getStringVal() );
    String hashTag = cmAssetService.getString( SSI_CONTEST_PAX_CM_ASSET_CODE, "HASHTAG", getLocale( participant ), true );
    String rankHeader = cmAssetService.getString( SSI_CONTEST_PAX_CM_ASSET_CODE, "RANK", locale, true );
    String amount = cmAssetService.getString( SSI_CONTEST_PAX_CM_ASSET_CODE, "ACTIVITY_AMOUNT", locale, true );
    String paxname = cmAssetService.getString( SSI_CONTEST_PAX_CM_ASSET_CODE, "PAX_NAME", locale, true );

    if ( StringUtils.isBlank( rankHeader ) )
    {
      rankHeader = "Rank";
    }
    if ( StringUtils.isBlank( amount ) )
    {
      rankHeader = " Activity Amount";
    }
    if ( StringUtils.isBlank( paxname ) )
    {
      rankHeader = "Participant Name";
    }

    StringBuffer stackRankToppersTable = new StringBuffer();
    StringBuffer stackRankToppersPlain = new StringBuffer();
    Map<String, Object> dataMap = new HashMap<String, Object>();
    if ( contest.getLastProgressUpdateDate() != null )
    {
      dataMap.put( KEY_IS_INCLUDE_STACK_RANK, String.valueOf( Boolean.TRUE ) );
    }
    // stackRankToppersTable.append( "<table style=\"width:100%\" border=\"1\">" ).append( "<tr>" );
    stackRankToppersTable.append( "<table style=\"width:100%\" border=\"1\">" ).append( "<thead><tr>" ).append( "<th>" ).append( rankHeader ).append( "</th><th>" ).append( paxname )
        .append( "</th><th>" ).append( amount ).append( "</th>" ).append( "</tr></thead><tbody>" );
    for ( SSIContestStackRankPaxValueBean contestStackRankPaxValueBean : ssiContestStackRankPaxValueBeanList )
    {
      Integer rank = contestStackRankPaxValueBean.getRank();
      String firstName = contestStackRankPaxValueBean.getFirstName();
      String lastName = contestStackRankPaxValueBean.getLastName();
      String activityAmount = SSIContestUtil.getFormattedValue( contestStackRankPaxValueBean.getScore(), decimalPrecision ) + getActivitySuffix( contest );

      // html
      stackRankToppersTable.append( "<tr align=\"center\" ><td width=\"(100/3)%\">" );
      stackRankToppersTable.append( "<b>&nbsp;" ).append( hashTag ).append( String.valueOf( rank ) ).append( "</b>" );
      stackRankToppersTable.append( "</td><td width=\"(100/3)%\">" );
      stackRankToppersTable.append( firstName ).append( " " ).append( lastName );
      stackRankToppersTable.append( "</td><td width=\"(100/3)%\">" );
      stackRankToppersTable.append( activityAmount );
      stackRankToppersTable.append( "</td><tr>" );

      // text
      stackRankToppersPlain.append( hashTag ).append( rank );
      stackRankToppersPlain.append( firstName ).append( " " ).append( lastName );
      stackRankToppersPlain.append( activityAmount );
    }
    stackRankToppersTable.append( "</tr>" ).append( "</table>" );
    dataMap.put( "stackRankToppersTable", stackRankToppersTable );
    dataMap.put( "stackRankToppersPlain", stackRankToppersPlain );
    return dataMap;
  }

  protected Map<String, Object> setContestFinalResultProgressStackRankData( Participant participant,
                                                                            int decimalPrecision,
                                                                            List<SSIContestStackRankTeamValueBean> ssiContestStackRankTeamValueBeanList,
                                                                            SSIContest contest )
  {
    Locale locale = CmsUtil.getLocale( participant.getLanguageType() != null ? participant.getLanguageType().getCode() : systemVariableService.getDefaultLanguage().getStringVal() );
    String hashTag = cmAssetService.getString( SSI_CONTEST_PAX_CM_ASSET_CODE, "HASHTAG", getLocale( participant ), true );
    String rankHeader = cmAssetService.getString( SSI_CONTEST_PAX_CM_ASSET_CODE, "RANK", locale, true );
    String amount = cmAssetService.getString( SSI_CONTEST_PAX_CM_ASSET_CODE, "ACTIVITY_AMOUNT", locale, true );
    String paxname = cmAssetService.getString( SSI_CONTEST_PAX_CM_ASSET_CODE, "PAX_NAME", locale, true );
    String payAmount = cmAssetService.getString( SSI_CONTEST_PAX_CM_ASSET_CODE, "PAY_AMOUNT", locale, true );

    if ( StringUtils.isBlank( rankHeader ) )
    {
      rankHeader = "Rank";
    }
    if ( StringUtils.isBlank( amount ) )
    {
      rankHeader = " Activity Amount";
    }
    if ( StringUtils.isBlank( paxname ) )
    {
      rankHeader = "Participant Name";
    }
    if ( StringUtils.isBlank( payAmount ) )
    {
      rankHeader = "Pay Amount";
    }

    StringBuffer stackRankToppersTable = new StringBuffer();
    StringBuffer stackRankToppersPlain = new StringBuffer();
    Map<String, Object> dataMap = new HashMap<String, Object>();
    if ( ssiContestStackRankTeamValueBeanList != null && ssiContestStackRankTeamValueBeanList.size() > 0 )
    {
      if ( contest.getLastProgressUpdateDate() != null )
      {
        dataMap.put( KEY_IS_INCLUDE_STACK_RANK, String.valueOf( Boolean.TRUE ) );
      }

      stackRankToppersTable.append( "<table style=\"width:100%\" border=\"1\">" ).append( "<thead><tr>" ).append( "<th>" ).append( rankHeader ).append( "</th><th>" ).append( paxname )
          .append( "</th><th>" ).append( amount ).append( "</th><th>" ).append( payAmount ).append( "</th>" ).append( "</tr></thead><tbody>" );

      // Taking a maximum of top 3 stack ranks
      if ( ssiContestStackRankTeamValueBeanList.size() > 3 )
      {
        ssiContestStackRankTeamValueBeanList = ssiContestStackRankTeamValueBeanList.subList( 0, 3 );
      }

      Collections.sort( ssiContestStackRankTeamValueBeanList, ASCE_COMPARATOR );

      for ( SSIContestStackRankTeamValueBean ssiContestStackRankTeamValueBean : ssiContestStackRankTeamValueBeanList )
      {
        // html

        stackRankToppersTable.append( "<tr align=\"center\" ><td width=\"25%\">" );
        stackRankToppersTable.append( "<b>&nbsp;" ).append( hashTag ).append( String.valueOf( ssiContestStackRankTeamValueBean.getRank() ) ).append( "</b>" );

        String firstName = ssiContestStackRankTeamValueBean.getFirstName();
        String lastName = ssiContestStackRankTeamValueBean.getLastName();
        stackRankToppersTable.append( "</td><td width=\"25%\">" );
        stackRankToppersTable.append( firstName ).append( " " ).append( lastName );

        String activityAmount = SSIContestUtil.getFormattedValue( ssiContestStackRankTeamValueBean.getScore(), decimalPrecision ) + getActivitySuffix( contest );
        stackRankToppersTable.append( "</td><td width=\"25%\">" );
        stackRankToppersTable.append( activityAmount );

        String payoutAmount = SSIContestUtil.getFormattedValue( ssiContestStackRankTeamValueBean.getPayout(), SSIContestUtil.PAYOUT_DECIMAL_PRECISION ) + getPayoutSuffix( contest );

        stackRankToppersTable.append( "</td><td width=\"25%\">" );
        stackRankToppersTable.append( payoutAmount );
        stackRankToppersTable.append( "</td><tr>" );

        // text
        stackRankToppersPlain.append( hashTag ).append( ssiContestStackRankTeamValueBean.getRank() );
        stackRankToppersPlain.append( firstName ).append( " " ).append( lastName );
        stackRankToppersPlain.append( activityAmount );
        stackRankToppersPlain.append( payoutAmount );

      }
      stackRankToppersTable.append( "</tbody>" ).append( "</table>" );
    }
    dataMap.put( "stackRankToppersTable", stackRankToppersTable );
    dataMap.put( "stackRankToppersPlain", stackRankToppersPlain );
    return dataMap;
  }

  private static Comparator<SSIContestStackRankTeamValueBean> ASCE_COMPARATOR = new Comparator<SSIContestStackRankTeamValueBean>()
  {
    public int compare( SSIContestStackRankTeamValueBean sr1, SSIContestStackRankTeamValueBean sr2 )
    {
      return sr1.getRank() - sr2.getRank();
    }
  };

  /**
   * Method to populate the objectMap with contest general data that applies to all contests
   * @param contest SSIContest
   * @param locale String
   * @param contestCreator Participant
   * @param recipient Participant
   * @return Map
   */
  private Map<String, Object> getContestGeneralInfo( SSIContest contest, String locale, Participant contestCreator, Participant recipient )
  {
    int precision = getPrecision( contest );
    Map<String, Object> objectMap = new HashMap<String, Object>();
    objectMap.put( KEY_FIRST_NAME, recipient.getFirstName() );
    objectMap.put( KEY_CONTEST_NAME, getContestName( contest, locale ) );
    objectMap.put( KEY_CONTEST_CREATOR_NAME, contestCreator.getFirstName() + " " + contestCreator.getLastName() );
    objectMap.put( KEY_CONTEST_START_DATE, DateUtils.toDisplayDateString( contest.getStartDate(), getLocale( locale ) ) );
    objectMap.put( KEY_CONTEST_END_DATE, DateUtils.toDisplayDateString( contest.getEndDate(), getLocale( locale ) ) );
    objectMap.put( KEY_CONTEST_GOAL, SSIContestUtil.getFormattedValue( contest.getContestGoal(), precision ) );
    if ( contest.getIncludePersonalMessage() != null && contest.getIncludePersonalMessage() )
    {
      objectMap.put( KEY_PERSONAL_MESSAGE, cmAssetService.getString( contest.getCmAssetCode(), SSIContest.CONTEST_CMASSET_MESSAGE, getLocale( locale ), true ) );
      objectMap.put( KEY_IS_INCLUDE_PERSONAL_MESSAGE, String.valueOf( Boolean.TRUE ) );
    }
    if ( contest.getLastProgressUpdateDate() != null && contest.isIncludeStackRank() )
    {
      objectMap.put( KEY_IS_INCLUDE_STACK_RANK, String.valueOf( Boolean.TRUE ) );
    }

    if ( contest.getBadgeRule() != null )
    {
      List<BadgeLibrary> badgeLibraryList = promotionService.buildBadgeLibraryList();
      for ( BadgeLibrary badgeLibrary : badgeLibraryList )
      {
        if ( contest.getBadgeRule().getBadgeLibraryCMKey().equals( badgeLibrary.getBadgeLibraryId() ) )
        {
          String siteUrlPrefix = getSystemVariableService().getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();
          objectMap.put( KEY_BADGE_IMAGE, "<img src=\"" + siteUrlPrefix + badgeLibrary.getEarnedImageSmall() + "\"/>" );
          objectMap.put( KEY_BADGE_NAME, contest.getBadgeRule().getBadgeNameTextFromCM() );
        }
      }
    }
    objectMap.put( KEY_PAYOUT_TYPE, contest.getPayoutType().getCode() );
    objectMap.put( KEY_MEASURE_TYPE, contest.getActivityMeasureType().getCode() );
    objectMap.put( KEY_CONTEST_LAST_UPDATED, DateUtils.toDisplayString( contest.getAuditUpdateInfo().getDateModified() ) );
    objectMap.put( KEY_CONTEST_PROGRESS_LAST_UPDATED,
                   DateUtils.toDisplayString( contest.getLastProgressUpdateDate() != null ? contest.getLastProgressUpdateDate() : new Date(), getLocale( locale ) ) );
    objectMap.put( KEY_CONTEST_END_DAYS_REMAING, String.valueOf( DateUtils.getDaysToGoFromToday( contest.getEndDate() ) ) );
    String programName = systemVariableService.getPropertyByName( SystemVariableService.CLIENT_NAME ).getStringVal();
    objectMap.put( KEY_PROMOTION_NAME, programName );
    objectMap.put( KEY_PROGRAM_NAME, programName );
    return objectMap;
  }

  public void setSsiPromotionService( SSIPromotionService ssiPromotionService )
  {
    this.ssiPromotionService = ssiPromotionService;
  }

  public void setPromotionService( PromotionService promotionService )
  {
    this.promotionService = promotionService;
  }

  public void setPromotionNotificationService( PromotionNotificationService promotionNotificationService )
  {
    this.promotionNotificationService = promotionNotificationService;
  }

  public void setCmAssetService( CMAssetService cmAssetService )
  {
    this.cmAssetService = cmAssetService;
  }

  public void setSsiContestService( SSIContestService ssiContestService )
  {
    this.ssiContestService = ssiContestService;
  }

  public void setSsiContestParticipantService( SSIContestParticipantService ssiContestParticipantService )
  {
    this.ssiContestParticipantService = ssiContestParticipantService;
  }

  protected String getActivitySuffix( SSIContest contest )
  {
    if ( contest.getActivityMeasureType().isCurrency() )
    {
      return WHITESPACE + contest.getActivityMeasureCurrencyCode().toUpperCase();
    }
    else
    {
      return WHITESPACE;
    }
  }

  protected String getPayoutSuffix( SSIContest contest )
  {
    if ( contest.getPayoutType().isPoints() )
    {
      return WHITESPACE + contest.getPayoutType().getName();
    }
    else if ( contest.getPayoutType().isOther() )
    {
      return WHITESPACE + contest.getPayoutOtherCurrencyCode().toUpperCase();
    }
    else
    {
      return WHITESPACE;
    }
  }

  public String getIsSSIAdmin()
  {
    return isSSIAdmin;
  }

  public void setIsSSIAdmin( String isSSIAdmin )
  {
    this.isSSIAdmin = isSSIAdmin;
  }

}


package com.biperf.core.service.gamification.impl;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.support.MutableSortDefinition;
import org.springframework.beans.support.PropertyComparator;

import com.biperf.core.dao.claim.ClaimDAO;
import com.biperf.core.dao.diyquiz.DIYQuizDAO;
import com.biperf.core.dao.gamification.GamificationDAO;
import com.biperf.core.dao.promotion.hibernate.PromotionQueryConstraint;
import com.biperf.core.domain.claim.Claim;
import com.biperf.core.domain.claim.ClaimRecipient;
import com.biperf.core.domain.claim.NominationClaim;
import com.biperf.core.domain.claim.NominationClaimBehaviors;
import com.biperf.core.domain.claim.ProductClaim;
import com.biperf.core.domain.claim.QuizClaim;
import com.biperf.core.domain.claim.RecognitionClaim;
import com.biperf.core.domain.diyquiz.DIYQuiz;
import com.biperf.core.domain.enums.BadgeLevelType;
import com.biperf.core.domain.enums.BadgeType;
import com.biperf.core.domain.enums.JournalStatusType;
import com.biperf.core.domain.enums.JournalTransactionType;
import com.biperf.core.domain.enums.MailingType;
import com.biperf.core.domain.enums.PromotionAwardsType;
import com.biperf.core.domain.enums.PromotionStatusType;
import com.biperf.core.domain.enums.PromotionType;
import com.biperf.core.domain.gamification.Badge;
import com.biperf.core.domain.gamification.BadgeBehaviorPromotion;
import com.biperf.core.domain.gamification.BadgeDetails;
import com.biperf.core.domain.gamification.BadgeInfo;
import com.biperf.core.domain.gamification.BadgeLibrary;
import com.biperf.core.domain.gamification.BadgePromotion;
import com.biperf.core.domain.gamification.BadgePromotionLevels;
import com.biperf.core.domain.gamification.BadgeRule;
import com.biperf.core.domain.gamification.BadgeTableBean;
import com.biperf.core.domain.gamification.BadgesComparator;
import com.biperf.core.domain.gamification.GamificationBadgeProfileView;
import com.biperf.core.domain.gamification.GamificationBadgeTileView;
import com.biperf.core.domain.gamification.ParticipantBadge;
import com.biperf.core.domain.gamification.ParticipantType;
import com.biperf.core.domain.goalquest.GoalBadgeRule;
import com.biperf.core.domain.hierarchy.NodeType;
import com.biperf.core.domain.journal.Journal;
import com.biperf.core.domain.mailing.Mailing;
import com.biperf.core.domain.mailing.MailingRecipient;
import com.biperf.core.domain.message.Message;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.AbstractGoalLevel;
import com.biperf.core.domain.promotion.ChallengePointPromotion;
import com.biperf.core.domain.promotion.GoalLevel;
import com.biperf.core.domain.promotion.GoalQuestPromotion;
import com.biperf.core.domain.promotion.NominationPromotion;
import com.biperf.core.domain.promotion.NominationPromotionLevel;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.QuizPromotion;
import com.biperf.core.domain.promotion.RecognitionPromotion;
import com.biperf.core.domain.promotion.ThrowdownPromotion;
import com.biperf.core.domain.ssi.SSIContest;
import com.biperf.core.domain.user.User;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.UpdateAssociationRequest;
import com.biperf.core.service.cms.CMAssetService;
import com.biperf.core.service.cms.CMDataElement;
import com.biperf.core.service.email.MailingService;
import com.biperf.core.service.gamification.GamificationService;
import com.biperf.core.service.journal.JournalService;
import com.biperf.core.service.message.MessageService;
import com.biperf.core.service.participant.AudienceService;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.promotion.PromotionAssociationRequest;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.service.util.ServiceError;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.service.util.StringUtil;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.GuidUtils;
import com.biperf.core.utils.LocaleUtils;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.PromotionMenuBean;
import com.biperf.util.StringUtils;
import com.objectpartners.cms.domain.Content;
import com.objectpartners.cms.domain.Translations;
import com.objectpartners.cms.domain.enums.DataTypeEnum;
import com.objectpartners.cms.service.ContentReader;
import com.objectpartners.cms.util.CmsResourceBundle;
import com.objectpartners.cms.util.ContentReaderManager;

public class GamificationServiceImpl implements GamificationService
{
  private GamificationDAO gamificationDAO;
  private ParticipantService participantService;
  private PromotionService promotionService;
  private SystemVariableService systemVariableService;
  private MailingService mailingService;
  private MessageService messageService;
  private CMAssetService cmAssetService;
  private AudienceService audienceService;
  private DIYQuizDAO diyQuizDAO;
  private ClaimDAO claimDAO;
  private JournalService journalService;

  private static final Log logger = LogFactory.getLog( GamificationServiceImpl.class );
  public static final String OPT_OUT_TEXT = "Opt Out Of Awards";

  /**
   * Set the GamificationDAO through IoC
   *
   * @param gamificationDAO
   */
  public void setGamificationDAO( GamificationDAO gamificationDAO )
  {
    this.gamificationDAO = gamificationDAO;
  }

  // Gamification Service Methods Impl Starts Here

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.gamification.GamificationService#getgetBadgeById(java.lang.Long)
   * 
   * @param id
   * @return Badge
   */

  public Badge getBadgeById( Long id )
  {
    Badge badge = this.gamificationDAO.getBadgeById( id );
    return badge;
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.gamification.GamificationService#getgetBadgeRuleById(java.lang.Long)
   * 
   * @param id
   * @return BadgeRule
   */

  public BadgeRule getBadgeRuleById( Long id )
  {
    BadgeRule badgeRule = this.gamificationDAO.getBadgeRuleById( id );
    return badgeRule;
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.gamification.GamificationService#getgetBadgeRuleByBadgeName(java.lang.String)
   * 
   * @param id
   * @return BadgeRule
   */
  public BadgeRule getBadgeRuleByBadgeName( String badgeName, Long badePromotionId )
  {
    BadgeRule badgeRule = this.gamificationDAO.getBadgeRuleByBadgeName( badgeName, badePromotionId );
    return badgeRule;
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.gamification.GamificationService#getgetBadgeByStatus(java.lang.String)
   * 
   * @param status
   * @return List<Badge>
   */
  public List<Badge> getBadgeByStatus( String status )
  {
    return this.gamificationDAO.getBadgeByStatus( status );
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.gamification.GamificationService#getgetBadgeByPromotion(java.lang.Long)
   * 
   * @param promotionId
   * @return List<Badge>
   */

  public List<Badge> getBadgeByPromotion( Long promotionId )
  {
    return this.gamificationDAO.getBadgeByPromotion( promotionId );
  }

  /**
   * Gets the list of Badge by status and type
   * 
   * @param status
   * @return List<Badge>
   */
  public List<Badge> getBadgeByTypeAndStatus( String type, String status )
  {
    return this.gamificationDAO.getBadgeByTypeAndStatus( type, status );
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.gamification.GamificationService#getgetBadgeByParticipantId(java.lang.Long)
   * 
   * @param userId
   * @return List<ParticipantBadge>
   */
  public List<ParticipantBadge> getBadgeByParticipantId( Long userId )
  {
    return this.gamificationDAO.getBadgeByParticipantId( userId );
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.gamification.GamificationService#getgetBadgeByParticipantProfileSorted(java.lang.Long,int)
   * 
   * @param userId
   * @param rowNumber
   * @return List<ParticipantBadge>
   */
  public List<ParticipantBadge> getBadgeByParticipantProfileSorted( List<PromotionMenuBean> eligiblePromotions, Long userId, int rowNumber )
  {
    List<ParticipantBadge> earnedBadgeProgressList = new ArrayList<ParticipantBadge>();
    List<ParticipantBadge> progressBadgeList = new ArrayList<ParticipantBadge>();
    List<ParticipantBadge> behaviorBadgeList = new ArrayList<ParticipantBadge>();
    List<ParticipantBadge> earnedfileLoadList = new ArrayList<ParticipantBadge>();
    List<ParticipantBadge> historyBadgeList = new ArrayList<ParticipantBadge>();
    List<ParticipantBadge> masterPartcipantList = new ArrayList<ParticipantBadge>();

    List<ParticipantBadge> behaviorNotEarnedBadgeList = new ArrayList<ParticipantBadge>();
    List<BadgeRule> behaviorNotEarnedBadgeRules = new ArrayList<BadgeRule>();

    List<ParticipantBadge> progressNotStartedBadgeList = new ArrayList<ParticipantBadge>();
    List<BadgeRule> progressNotStartedBadgeRules = new ArrayList<BadgeRule>();
    // Added all earned badges to list then ordered based on earned date.
    List<ParticipantBadge> earnedBadgeList = new ArrayList<ParticipantBadge>();
    List<ParticipantBadge> sortedEarnedBadgeList = new ArrayList<ParticipantBadge>();

    int masterIndex = 0;

    earnedBadgeProgressList = this.gamificationDAO.getBadgeByParticipantEarnedProgress( userId, rowNumber );
    behaviorBadgeList = this.gamificationDAO.getBadgeByParticipantBehavior( userId );
    earnedfileLoadList = this.gamificationDAO.getBadgeByParticipantEarnedFileLoad( userId, rowNumber );
    earnedBadgeList.addAll( earnedBadgeProgressList );
    earnedBadgeList.addAll( behaviorBadgeList );
    earnedBadgeList.addAll( earnedfileLoadList );
    sortedEarnedBadgeList = earnedBadgeList.stream().sorted( Comparator.comparing( ParticipantBadge::getEarnedDate ).reversed() ).collect( Collectors.toList() );
    progressBadgeList = this.gamificationDAO.getBadgeByParticipantNotEarnedProgress( userId, rowNumber );

    Long[] promotionsProgress = getPromotionArray( eligiblePromotions, "progress" );

    progressNotStartedBadgeRules = this.gamificationDAO.getBadgeByParticipantProgressNotStarted( promotionsProgress, userId );
    progressNotStartedBadgeList.addAll( buildParticipantBadgeFromBadgeRule( progressNotStartedBadgeRules, userId ) );

    Long[] promotions = getPromotionArray( eligiblePromotions, "behavior" );

    behaviorNotEarnedBadgeRules = this.gamificationDAO.getBadgeByParticipantBehaviorNotEarned( promotions, userId );
    behaviorNotEarnedBadgeList.addAll( buildParticipantBadgeFromBadgeRule( behaviorNotEarnedBadgeRules, userId ) );

    historyBadgeList = this.gamificationDAO.getBadgeParticipantHistory( userId );

    if ( sortedEarnedBadgeList != null && !sortedEarnedBadgeList.isEmpty() )
    {
      for ( int i = 0; i < sortedEarnedBadgeList.size(); i++ )
      {
        masterPartcipantList.add( i, sortedEarnedBadgeList.get( i ) );
        masterIndex++;
      }
    }

    if ( progressBadgeList != null && !progressBadgeList.isEmpty() )
    {
      for ( Iterator<ParticipantBadge> it = progressBadgeList.iterator(); it.hasNext(); )
      {
        ParticipantBadge participantBadge = it.next();
        if ( isEligibleProgressBadge( eligiblePromotions, participantBadge ) )
        {
          masterPartcipantList.add( masterIndex, participantBadge );
          masterIndex++;
        }
      }
    }
    if ( progressNotStartedBadgeList != null && !progressNotStartedBadgeList.isEmpty() )
    {
      for ( Iterator<ParticipantBadge> it = progressNotStartedBadgeList.iterator(); it.hasNext(); )
      {
        ParticipantBadge participantBadge = it.next();
        if ( isEligibleProgressBadge( eligiblePromotions, participantBadge ) )
        {
          masterPartcipantList.add( masterIndex, participantBadge );
          masterIndex++;
        }
      }
    }

    List<ParticipantBadge> earnedAndNotEarnedBehaviorBadgeList = new ArrayList<ParticipantBadge>();
    earnedAndNotEarnedBehaviorBadgeList.addAll( behaviorNotEarnedBadgeList );

    if ( earnedAndNotEarnedBehaviorBadgeList != null && !earnedAndNotEarnedBehaviorBadgeList.isEmpty() )
    {
      Collections.sort( earnedAndNotEarnedBehaviorBadgeList, new BadgesComparator() );
      for ( int i = 0; i < earnedAndNotEarnedBehaviorBadgeList.size(); i++ )
      {
        masterPartcipantList.add( masterIndex, earnedAndNotEarnedBehaviorBadgeList.get( i ) );
        masterIndex++;
      }
    }

    if ( historyBadgeList != null && !historyBadgeList.isEmpty() )
    {
      for ( int i = 0; i < historyBadgeList.size(); i++ )
      {
        masterPartcipantList.add( masterIndex, historyBadgeList.get( i ) );
        masterIndex++;
      }
    }

    return masterPartcipantList;
  }

  private boolean isEligibleProgressBadge( List<PromotionMenuBean> eligiblePromotions, ParticipantBadge participantBadge )
  {
    boolean isValidToAdd = false;
    Iterator promoItr = eligiblePromotions.iterator();
    while ( promoItr.hasNext() )
    {
      PromotionMenuBean promoBean = (PromotionMenuBean)promoItr.next();
      Badge badge = participantBadge.getBadgePromotion();
      Iterator<BadgePromotion> badgePromoItr = badge.getBadgePromotions().iterator();
      while ( badgePromoItr.hasNext() )
      {
        BadgePromotion badgePromotion = badgePromoItr.next();
        if ( badgePromotion.getEligiblePromotion().getId().equals( promoBean.getPromotion().getId() ) )
        {
          String badgeCountType = badge.getBadgeCountType().getCode();
          if ( badgeCountType != null && badgeCountType.equalsIgnoreCase( "given" ) )
          {
            if ( audienceService.isParticipantInPrimaryAudience( promoBean.getPromotion(), (Participant)participantBadge.getParticipant() ) && !participantBadge.getIsEarned() )
            {
              isValidToAdd = true;
              break;
            }
          }
          else if ( badgeCountType != null && badgeCountType.equalsIgnoreCase( "received" ) )
          {
            if ( audienceService.isParticipantInSecondaryAudience( promoBean.getPromotion(), (Participant)participantBadge.getParticipant() ) && !participantBadge.getIsEarned() )
            {
              isValidToAdd = true;
              break;
            }
          }
          else if ( badgeCountType != null && badgeCountType.equalsIgnoreCase( "total" ) )
          {
            if ( ( audienceService.isParticipantInPrimaryAudience( promoBean.getPromotion(), (Participant)participantBadge.getParticipant() )
                || audienceService.isParticipantInSecondaryAudience( promoBean.getPromotion(), (Participant)participantBadge.getParticipant() ) ) && !participantBadge.getIsEarned() )
            {
              isValidToAdd = true;
              break;
            }
          }
        }
      }
      if ( isValidToAdd )
      {
        break;
      }
    }
    return isValidToAdd;
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.gamification.GamificationService#getgetBadgeByParticipantSorted(java.lang.Long,int)
   * 
   * Customized for mobile app to only return progress type badges
   * 
   * @param userId
   * @param rowNumber
   * @return List<ParticipantBadge>
   */
  public List<ParticipantBadge> getBadgeByParticipantPromotionSorted( Long promotionId, Long userId, int rowNumber )
  {
    List<ParticipantBadge> earnedBadgeHighlightedList = new ArrayList<ParticipantBadge>();
    List<ParticipantBadge> progressBadgeList = new ArrayList<ParticipantBadge>();
    // List<ParticipantBadge> behaviorBadgeList = new ArrayList<ParticipantBadge>();
    // List<ParticipantBadge> earnedBadgeOldList = new ArrayList<ParticipantBadge>();
    List<ParticipantBadge> progressNotStartedList = new ArrayList<ParticipantBadge>();
    List<BadgeRule> progressNotStartedBadgeRules = new ArrayList<BadgeRule>();
    List<ParticipantBadge> masterPartcipantList = new ArrayList<ParticipantBadge>();
    List<ParticipantBadge> filteredPartcipantList = new ArrayList<ParticipantBadge>();

    int masterIndex = 0;
    Long[] promotions = new Long[1];
    promotions[0] = promotionId;

    earnedBadgeHighlightedList = this.gamificationDAO.getBadgeByParticipantPromotionEarnedHighLight( userId, promotionId );
    progressBadgeList = this.gamificationDAO.getBadgeByParticipantPromotionProgress( userId, promotionId );
    // behaviorBadgeList = this.gamificationDAO.getBadgeByParticipantPromotionBehavior( userId,
    // promotionId, rowNumber );
    // earnedBadgeOldList = this.gamificationDAO.getBadgeByParticipantPromotionEarnedOld( userId,
    // promotionId, rowNumber );
    // progressNotStartedList=this.gamificationDAO.getBadgeByParticipantPromotionProgressLessthanOne(
    // userId, promotionId, rowNumber );

    progressNotStartedBadgeRules = this.gamificationDAO.getBadgeByParticipantProgressNotStarted( promotions, userId );
    progressNotStartedList.addAll( buildParticipantBadgeFromBadgeRule( progressNotStartedBadgeRules, userId ) );

    if ( earnedBadgeHighlightedList != null && !earnedBadgeHighlightedList.isEmpty() )
    {
      for ( int i = 0; i < earnedBadgeHighlightedList.size(); i++ )
      {
        masterPartcipantList.add( i, earnedBadgeHighlightedList.get( i ) );
        masterIndex++;
      }
    }

    if ( progressBadgeList != null && !progressBadgeList.isEmpty() )
    {
      for ( int i = 0; i < progressBadgeList.size(); i++ )
      {
        masterPartcipantList.add( masterIndex, progressBadgeList.get( i ) );
        masterIndex++;
      }
    }

    // if ( behaviorBadgeList != null && !behaviorBadgeList.isEmpty() )
    // {
    // for ( int i = 0; i < behaviorBadgeList.size(); i++ )
    // {
    // masterPartcipantList.add( masterIndex, behaviorBadgeList.get( i ) );
    // masterIndex++;
    // }
    // }

    // if ( earnedBadgeOldList != null && !earnedBadgeOldList.isEmpty() )
    // {
    // for ( int i = 0; i < earnedBadgeOldList.size(); i++ )
    // {
    // masterPartcipantList.add( masterIndex, earnedBadgeOldList.get( i ) );
    // masterIndex++;
    // }
    // }

    if ( progressNotStartedList != null && !progressNotStartedList.isEmpty() )
    {
      for ( int i = 0; i < progressNotStartedList.size(); i++ )
      {
        masterPartcipantList.add( masterIndex, progressNotStartedList.get( i ) );
        masterIndex++;
      }
    }

    // if ( masterPartcipantList != null && masterPartcipantList.size() > 0 )
    // {
    // filteredPartcipantList = getBadgesForRecognitionConfirmation( masterPartcipantList, rowNumber
    // );
    // }
    // return filteredPartcipantList;
    return masterPartcipantList;
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.gamification.GamificationService#getgetBadgeByParticipantSorted(java.lang.Long,int)
   * 
   * @param userId
   * @param rowNumber
   * @return List<ParticipantBadge>
   */
  public List<ParticipantBadge> getBadgeByParticipantSorted( List<PromotionMenuBean> eligiblePromotions, Long userId )
  {
    List<ParticipantBadge> earnedBadgeHighlightedList = new ArrayList<ParticipantBadge>();
    List<ParticipantBadge> progressBadgeList = new ArrayList<ParticipantBadge>();
    List<ParticipantBadge> behaviorBadgeList = new ArrayList<ParticipantBadge>();
    List<ParticipantBadge> behaviorNotEarnedBadgeList = new ArrayList<ParticipantBadge>();
    List<ParticipantBadge> earnedBadgeOldList = new ArrayList<ParticipantBadge>();
    List<ParticipantBadge> progressNotStartedList = new ArrayList<ParticipantBadge>();
    List<BadgeRule> progressNotStartedBadgeRules = new ArrayList<BadgeRule>();
    List<ParticipantBadge> masterPartcipantList = new ArrayList<ParticipantBadge>();

    List<BadgeRule> behaviorNotEarnedBadgeRules = new ArrayList<BadgeRule>();
    int masterIndex = 0;

    earnedBadgeHighlightedList = this.gamificationDAO.getBadgeByParticipantEarnedHighLight( userId );
    progressBadgeList = this.gamificationDAO.getBadgeByParticipantProgress( userId );
    behaviorBadgeList = this.gamificationDAO.getBadgeByParticipantBehavior( userId );
    earnedBadgeOldList = this.gamificationDAO.getBadgeByParticipantEarnedOld( userId );
    // progressNotStartedList=this.gamificationDAO.getBadgeByParticipantProgressLessthanOne(
    // userId,rowNumber );

    Long[] promotions = getPromotionArray( eligiblePromotions, "behavior" );

    /*
     * for(int i=0;i<promotions.length;i++) {
     * behaviorNotEarnedBadgeRules=this.gamificationDAO.getBadgeByParticipantBehaviorNotEarned(
     * promotions[i], userId, rowNumber ); behaviorNotEarnedBadgeList.addAll(
     * buildParticipantBadgeFromBadgeRule(behaviorNotEarnedBadgeRules,userId) ); }
     */
    behaviorNotEarnedBadgeRules = this.gamificationDAO.getBadgeByParticipantBehaviorNotEarned( promotions, userId );
    behaviorNotEarnedBadgeList.addAll( buildParticipantBadgeFromBadgeRule( behaviorNotEarnedBadgeRules, userId ) );

    Long[] promotionsProgress = getPromotionArray( eligiblePromotions, "progress" );

    progressNotStartedBadgeRules = this.gamificationDAO.getBadgeByParticipantProgressNotStarted( promotionsProgress, userId );
    progressNotStartedList.addAll( buildParticipantBadgeFromBadgeRule( progressNotStartedBadgeRules, userId ) );

    if ( earnedBadgeHighlightedList != null && !earnedBadgeHighlightedList.isEmpty() )
    {
      for ( int i = 0; i < earnedBadgeHighlightedList.size(); i++ )
      {
        masterPartcipantList.add( i, earnedBadgeHighlightedList.get( i ) );
        masterIndex++;
      }
    }

    if ( progressBadgeList != null && !progressBadgeList.isEmpty() )
    {
      for ( Iterator<ParticipantBadge> it = progressBadgeList.iterator(); it.hasNext(); )
      {
        ParticipantBadge participantBadge = it.next();
        if ( isEligibleProgressBadge( eligiblePromotions, participantBadge ) )
        {
          masterPartcipantList.add( masterIndex, participantBadge );
          masterIndex++;
        }
      }
    }

    if ( behaviorBadgeList != null && !behaviorBadgeList.isEmpty() )
    {
      for ( int i = 0; i < behaviorBadgeList.size(); i++ )
      {
        masterPartcipantList.add( masterIndex, behaviorBadgeList.get( i ) );
        masterIndex++;
      }
    }

    if ( behaviorNotEarnedBadgeList != null && !behaviorNotEarnedBadgeList.isEmpty() )
    {
      for ( int i = 0; i < behaviorNotEarnedBadgeList.size(); i++ )
      {
        masterPartcipantList.add( masterIndex, behaviorNotEarnedBadgeList.get( i ) );
        masterIndex++;
      }
    }

    if ( earnedBadgeOldList != null && !earnedBadgeOldList.isEmpty() )
    {
      for ( int i = 0; i < earnedBadgeOldList.size(); i++ )
      {
        masterPartcipantList.add( masterIndex, earnedBadgeOldList.get( i ) );
        masterIndex++;
      }
    }

    if ( progressNotStartedList != null && !progressNotStartedList.isEmpty() )
    {
      for ( Iterator<ParticipantBadge> it = progressNotStartedList.iterator(); it.hasNext(); )
      {
        ParticipantBadge participantBadge = it.next();
        if ( isEligibleProgressBadge( eligiblePromotions, participantBadge ) )
        {
          masterPartcipantList.add( masterIndex, participantBadge );
          masterIndex++;
        }
      }
    }

    return masterPartcipantList;

  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.gamification.GamificationService#getgetPartcipantBadgeByPromotion(java.util.List)
   * 
   * @param partcipantBadgeList
   * @return HashMap<String, List<BadgeDetails>>
   */
  private HashMap<String, List<BadgeDetails>> getPartcipantBadgeByPromotion( List<ParticipantBadge> partcipantBadgeList, boolean isFromMiniProfile )
  {
    HashMap<String, List<BadgeDetails>> participantMap = new LinkedHashMap<String, List<BadgeDetails>>();
    List<BadgeDetails> badegDetailListFromMap = null;
    String promotionNames = "";
    Long progressNumerator = 0L;
    Long progressDenominator = 0L;
    Boolean progressVisible = false;
    Long lowestBadgeRuleId = 0L;
    Long idCount = 0L;
    String historyLabel = CmsResourceBundle.getCmsBundle().getString( "gamification.admin.labels.HISTORY_LABEL" );
    String otherBadgeLabel = CmsResourceBundle.getCmsBundle().getString( "gamification.admin.labels.OTHER_BADGE_LABEL" );

    SimpleDateFormat sdf = new SimpleDateFormat( "YYYY-MM-DD" );

    for ( ParticipantBadge participantBadge : partcipantBadgeList )
    {
      Iterator<BadgePromotion> iterator = participantBadge.getBadgePromotion().getBadgePromotions().iterator();
      Promotion promotion = iterator.hasNext() ? iterator.next().getEligiblePromotion() : null;

      Badge badge = participantBadge.getBadgePromotion();

      lowestBadgeRuleId = getLowestBadgeRule( badge );
      BadgeRule badgeRule = participantBadge.getBadgeRule();
      Date currentDate = new Date();
      progressVisible = false;

      if ( badge.getBadgeType().getCode().equalsIgnoreCase( "progress" ) )
      {
        String badgeCountType = badge.getBadgeCountType().getCode();
        if ( participantBadge.getSentCount() == null )
        {
          participantBadge.setSentCount( new Long( 0 ) );
        }
        if ( participantBadge.getReceivedCount() == null )
        {
          participantBadge.setReceivedCount( new Long( 0 ) );
        }
        if ( badgeCountType != null && badgeCountType.equalsIgnoreCase( "given" ) )
        {
          progressNumerator = participantBadge.getSentCount();
        }
        else if ( badgeCountType != null && badgeCountType.equalsIgnoreCase( "received" ) )
        {
          progressNumerator = participantBadge.getReceivedCount();
        }
        else if ( badgeCountType != null && badgeCountType.equalsIgnoreCase( "total" ) )
        {
          progressNumerator = participantBadge.getSentCount() + participantBadge.getReceivedCount();
        }
        progressDenominator = badgeRule.getMaximumQualifier();

        if ( progressNumerator.equals( 0L ) && badgeRule.getId().equals( lowestBadgeRuleId ) )
        {
          progressVisible = true;
        }
        else if ( progressNumerator > 0L && !participantBadge.getIsEarned() )
        {
          progressVisible = true;
        }
        else
        {
          progressVisible = false;
        }

      }
      else
      {
        progressNumerator = 0L;
        progressDenominator = 0L;
        progressVisible = false;

      }
      // code to identify the record is history or live
      if ( badge.getDisplayEndDate() == null )
      {
        try
        {

          badge.setDisplayEndDate( sdf.parse( "9999-12-31" ) );
        }
        catch( ParseException p )
        {
          logger.error( p );
        }
      }

      if ( currentDate.compareTo( badge.getDisplayEndDate() ) > 0 && !participantBadge.getIsEarned() )
      {
        continue;
      }
      // Added for bugId 53253
      // When Promotion End Date is null then display end date of badge is future date. Below code
      // is to set display end date when badge is expired manually to show them in History Section.
      if ( badge.getStatus().equals( "I" ) )
      {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime( currentDate );
        calendar.add( Calendar.DATE, -1 ); // number of days to subtract
        badge.setDisplayEndDate( calendar.getTime() );
      }
      if ( currentDate.compareTo( badge.getDisplayEndDate() ) > 0 && participantBadge.getIsEarned() )
      {
        promotionNames = historyLabel;
      }
      else
      {
        if ( badge.getBadgePromotions() != null && badge.getBadgePromotions().size() > 0 )
        {
          List<BadgePromotion> badgePromotions = new ArrayList<BadgePromotion>( badge.getBadgePromotions() );
          badgePromotions.sort( new Comparator<BadgePromotion>()
          {

            @Override
            public int compare( BadgePromotion badgePromotion1, BadgePromotion badgePromotion2 )
            {
              return badgePromotion1.getEligiblePromotion().getName().compareTo( badgePromotion2.getEligiblePromotion().getName() );
            }
          } );
          promotionNames = getPromotionNamesNoLine( badgePromotions, participantBadge );
        }
        else
        {
          promotionNames = otherBadgeLabel;
        }

      }
      // end code to identify the record is history or live

      BadgeDetails badgeDetail = new BadgeDetails();
      idCount = idCount + 1;
      badgeDetail.setUniqueId( idCount );
      badgeDetail.setBadgeId( participantBadge.getBadgePromotion().getId() );
      if ( promotion != null && promotion.isNominationPromotion() )
      {
        int earnedCount = gamificationDAO.getBadgesEarnedCount( participantBadge.getBadgeRule().getId(), participantBadge.getParticipant().getId() );
        badgeDetail.setEarnCount( earnedCount );
      }

      badgeDetail.setBadgeName( badgeRule.getBadgeNameTextFromCM() );
      badgeDetail.setBadgeType( badge.getBadgeType().getCode() );
      if ( participantBadge.getEarnedDate() != null )
      {
        badgeDetail.setDateEarned( DateUtils.toDisplayString( participantBadge.getEarnedDate() ) );
      }
      badgeDetail.setEarned( participantBadge.getIsEarned() );
      badgeDetail.setProgressDenominator( progressDenominator );
      badgeDetail.setProgressNumerator( progressNumerator );
      badgeDetail.setProgressVisible( progressVisible );
      List earnedNotEarnedImageList = getEarnedNotEarnedImageList( badgeRule.getBadgeLibraryCMKey() );
      Iterator itr = earnedNotEarnedImageList.iterator();
      while ( itr.hasNext() )
      {
        BadgeLibrary badgeLib = (BadgeLibrary)itr.next();
        badgeDetail.setImg( badgeLib.getEarnedImageSmall() );
        badgeDetail.setImgLarge( badgeLib.getEarnedImageMedium() );
      }
      if ( participantMap.get( promotionNames ) == null )
      {
        badegDetailListFromMap = new ArrayList();
        badegDetailListFromMap.add( badgeDetail );
        participantMap.put( promotionNames, badegDetailListFromMap );
      }
      else
      {

        badegDetailListFromMap = (List)participantMap.get( promotionNames );
        badegDetailListFromMap.add( badgeDetail );
        participantMap.put( promotionNames, badegDetailListFromMap );
      }
      SSIContest contest = participantBadge.getContest();
      if ( contest != null )
      {
        badgeDetail.setContestName( getContestName( contest, UserManager.getLocale() ) );
        badgeDetail.setBadgeDescription( null );
      }
      else
      {
        badgeDetail.setBadgeDescription( badgeRule.getBadgeDescriptionTextFromCM() );
      }

    }

    return participantMap;
  }

  public String getPromotionNamesNoLine( List<BadgePromotion> badgePromotions, ParticipantBadge participantBadge )
  {
    String promotioNames = "";
    Iterator<BadgePromotion> setItr = null;
    setItr = badgePromotions.iterator();
    int count = 0;

    while ( setItr.hasNext() )
    {
      BadgePromotion badgePromotion = (BadgePromotion)setItr.next();
      Promotion promotion = badgePromotion.getEligiblePromotion();
      // For DIY display quiz name rather than promotion name
      if ( promotion.isDIYQuizPromotion() )
      {
        if ( participantBadge.getBadgeRule() != null && participantBadge.getBadgeRule().getId() != null )
        {
          List<DIYQuiz> diyQuizzes = getDiyQuizDAO().getAllQuizzesByPromotionIdAndBadgeRuleId( promotion.getId(), participantBadge.getBadgeRule().getId() );
          if ( diyQuizzes != null )
          {
            for ( DIYQuiz diyQuiz : diyQuizzes )
            {
              if ( promotioNames.trim().length() > 0 )
              {
                promotioNames = promotioNames + ", " + diyQuiz.getName();
              }
              else
              {
                promotioNames = promotioNames + diyQuiz.getName();
              }
            }
          }
        }
      }
      else if ( count == 0 || count == badgePromotions.size() )
      {
        promotioNames = promotioNames + promotion.getName();
      }
      else
      {
        promotioNames = promotioNames + ", " + promotion.getName();
      }
      count++;
    }
    return promotioNames;
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.gamification.GamificationService#getgetBehaviorForSelectedPromotions(java.lang.String)
   * 
   * @param promotionIds
   * @return List
   */

  public List getBehaviorForSelectedPromotions( String promotionIds, List<String> existingPromosId, String promotionTypeCode )
  {
    List newBehaviorNames = new ArrayList();

    String[] promotionArray = StringUtil.parseCommaDelimitedList( promotionIds );
    String data = StringUtil.convertArrayToINQueryString( promotionArray );
    newBehaviorNames = this.gamificationDAO.getBehaviorForSelectedPromotions( data, promotionTypeCode );
    // if existing promo ids not null then get the existing promo behaviours and remove it from new
    // list.
    if ( existingPromosId != null )
    {
      String[] existingPromotionArray = new String[existingPromosId.size()];
      existingPromotionArray = existingPromosId.toArray( existingPromotionArray );
      String existingPromotionString = StringUtil.convertArrayToINQueryString( existingPromotionArray );
      List existingBehaviorNames = this.gamificationDAO.getBehaviorForSelectedPromotions( existingPromotionString, promotionTypeCode );
      for ( Iterator iterator = existingBehaviorNames.iterator(); iterator.hasNext(); )
      {
        BadgeBehaviorPromotion bbp = (BadgeBehaviorPromotion)iterator.next();
        for ( Iterator innerIterator = newBehaviorNames.iterator(); innerIterator.hasNext(); )
        {
          BadgeBehaviorPromotion innerBbp = (BadgeBehaviorPromotion)innerIterator.next();
          if ( innerBbp.getBehaviorCode().equalsIgnoreCase( bbp.getBehaviorCode() ) )
          {
            innerIterator.remove();
            break;
          }
        }
      }
    }
    return newBehaviorNames;
  }

  /*
   * public String[] getBehaviorForSelectedPromotions(String promotionIds) { List behaviorNames=new
   * ArrayList(); List<BadgeBehaviorPromotion> badgebehaviorList=new
   * ArrayList<BadgeBehaviorPromotion>(); String [] promotionArray=null; String[] behaviors=null;
   * String behaviorsCommaSeperated="";
   * promotionArray=StringUtil.parseCommaDelimitedList(promotionIds); String
   * data=StringUtil.convertArrayToINQueryString(promotionArray);
   * behaviorNames=this.gamificationDAO.getBehaviorForSelectedPromotions( data );
   * if(behaviorNames!=null && !behaviorNames.isEmpty()) { behaviors =(String[])
   * behaviorNames.toArray(new String[behaviorNames.size()]); } return behaviors; }
   */

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.gamification.GamificationService#getgetBehaviorForSelectedPromotions(java.lang.String,java.lang.String[])
   * 
   * @param promotionIds
   * @param behaviors
   * @return List<BadgeBehaviorPromotion>
   */
  public List<BadgeBehaviorPromotion> getBadgeBehaviorPromotions( String promotionIds, List behaviors )
  {
    String[] promotionArray = null;
    String[] promotionbehaviorsArray = null;
    List<BadgeBehaviorPromotion> badgebehaviorPromoList = new ArrayList<BadgeBehaviorPromotion>();
    String promotionNames = "";
    promotionArray = StringUtil.parseCommaDelimitedList( promotionIds );
    String data = StringUtil.convertArrayToINQueryString( promotionArray );
    List promotionNamesList = new ArrayList();
    Iterator itrBeh = behaviors.iterator();

    while ( itrBeh.hasNext() )
    {
      BadgeBehaviorPromotion behPromo = (BadgeBehaviorPromotion)itrBeh.next();
      promotionNamesList = this.gamificationDAO.getPromotionsNameForBehavior( behPromo.getBehaviorCode(), data );
      promotionbehaviorsArray = (String[])promotionNamesList.toArray( new String[promotionNamesList.size()] );
      promotionNames = StringUtil.convertStringArrayToCommaStringNewLine( promotionbehaviorsArray );
      BadgeBehaviorPromotion badgeBehaviorPromotions = new BadgeBehaviorPromotion();
      badgeBehaviorPromotions.setBehaviorCode( behPromo.getBehaviorCode() );
      badgeBehaviorPromotions.setBehaviorName( behPromo.getBehaviorName() );
      badgeBehaviorPromotions.setPromotionNames( promotionNames );
      // badgeBehaviorPromotions.setBadgeLibList( buildBadgeLibraryList() );
      badgebehaviorPromoList.add( badgeBehaviorPromotions );
    }

    return badgebehaviorPromoList;
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.gamification.GamificationService#getgetPromotionByBehavior(java.lang.String,java.lang.String)
   * 
   * @param promotionIds
   * @param behaviorName
   * @return String
   */
  public String getPromotionByBehavior( String promotionIds, String behaviorName )
  {
    String[] promotionbehaviorsArray = null;
    String[] promotionArray = null;
    String promotionNames = "";
    List promotionNamesList = new ArrayList();
    promotionArray = StringUtil.parseCommaDelimitedList( promotionIds );
    String data = StringUtil.convertArrayToINQueryString( promotionArray );
    promotionNamesList = this.gamificationDAO.getPromotionsNameForBehavior( behaviorName, data );
    promotionbehaviorsArray = (String[])promotionNamesList.toArray( new String[promotionNamesList.size()] );
    promotionNames = StringUtil.convertStringArrayToCommaStringNewLine( promotionbehaviorsArray );
    return promotionNames;
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.gamification.GamificationService#validatePromotionType(java.lang.String)
   * 
   * @param promotionIds
   * @return String
   */
  public String validatePromotionType( String promotionIds )
  {
    String[] promotionArray = null;
    String isRecogntionType = "N";
    promotionArray = StringUtil.parseCommaDelimitedList( promotionIds );
    Long promotionId;
    for ( int i = 0; i < promotionArray.length; i++ )
    {
      isRecogntionType = "N";
      promotionId = Long.parseLong( promotionArray[i] );
      Promotion promotion = getPromotionService().getPromotionById( promotionId );
      if ( promotion.getPromotionType().getCode().equals( PromotionType.RECOGNITION ) )
      {
        isRecogntionType = "Y";
      }
      else if ( promotion.getPromotionType().getCode().equals( PromotionType.GOALQUEST ) || promotion.getPromotionType().getCode().equals( PromotionType.CHALLENGE_POINT ) )
      {
        isRecogntionType = "GQ";
      }
      else if ( promotion.getPromotionType().getCode().equals( PromotionType.NOMINATION ) )
      {
        isRecogntionType = "NOM";
      }
      else
      {
        isRecogntionType = "N";
        break;
      }
    }
    return isRecogntionType;
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.gamification.GamificationService#validatePromotionTypeOthers(java.lang.String)
   * 
   * @param promotionIds
   * @return String
   */
  public String validatePromotionTypeOthers( String promotionIds )
  {
    String[] promotionArray = null;
    String isRecogntionType = "N";
    promotionArray = StringUtil.parseCommaDelimitedList( promotionIds );
    Long promotionId;
    for ( int i = 0; i < promotionArray.length; i++ )
    {
      isRecogntionType = "N";
      promotionId = Long.parseLong( promotionArray[i] );
      Promotion promotion = getPromotionService().getPromotionById( promotionId );
      if ( promotion != null )
      {
        if ( promotion.getPromotionType().getCode().equals( PromotionType.RECOGNITION ) )
        {
          isRecogntionType = "Y";
        }
        else if ( promotion.getPromotionType().getCode().equals( PromotionType.NOMINATION ) )
        {
          isRecogntionType = "NOM";
        }
        else if ( promotion.getPromotionType().getCode().equals( PromotionType.GOALQUEST ) || promotion.getPromotionType().getCode().equals( PromotionType.CHALLENGE_POINT ) )
        {
          isRecogntionType = "GQ";
        }
        else
        {
          isRecogntionType = "N";
          break;
        }
      }
    }
    return isRecogntionType;
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.gamification.GamificationService#validatePromotionEndDate(java.lang.String)
   * 
   * @param promotionIds
   * @return String
   */
  public String validatePromotionEndDate( String promotionIds )
  {
    String[] promotionArray = null;
    String isEndDateNull = "N";
    promotionArray = StringUtil.parseCommaDelimitedList( promotionIds );
    Long promotionId;
    for ( int i = 0; i < promotionArray.length; i++ )
    {
      isEndDateNull = "N";
      promotionId = Long.parseLong( promotionArray[i] );
      Promotion promotion = getPromotionService().getPromotionById( promotionId );
      if ( promotion.getSubmissionEndDate() == null )
      {
        isEndDateNull = "Y";
        break;
      }
      else
      {
        isEndDateNull = "N";
      }
    }
    return isEndDateNull;
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.gamification.GamificationService#getgetAwardType(java.lang.String)
   * 
   * @param promotionIds
   * @return String
   */

  public String getAwardType( String promotionIds )
  {
    String[] promotionArray = null;
    String awardType = "";
    promotionArray = StringUtil.parseCommaDelimitedList( promotionIds );
    Long promotionId;
    for ( int i = 0; i < promotionArray.length; i++ )
    {
      promotionId = Long.parseLong( promotionArray[i] );
      Promotion promotion = getPromotionService().getPromotionById( promotionId );
      if ( promotion instanceof GoalQuestPromotion )
      {
        awardType = getPromotionAwardType( (GoalQuestPromotion)promotion );
      }
      // Lock nominations as points - getAwardType() will be null - nomination awards are by level
      else if ( promotion.isNominationPromotion() )
      {
        awardType = PromotionAwardsType.POINTS;
      }
      else
      {
        awardType = promotion.getAwardType().getCode();
      }
    }
    return awardType;
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.gamification.GamificationService#getgetPromotionLevels(java.lang.String)
   * 
   * @param promotionIds
   * @return List<BadgePromotionLevels>
   */

  public List<BadgePromotionLevels> getPromotionLevels( String promotionIds )
  {
    long promotionId = Long.parseLong( promotionIds );
    String promotionType = "";
    Promotion promo = getPromotionService().getPromotionById( (Long)promotionId );
    if ( promo.getPromotionType().getCode().equalsIgnoreCase( PromotionType.GOALQUEST ) || promo.getPromotionType().getCode().equalsIgnoreCase( PromotionType.CHALLENGE_POINT ) )
    {
      promotionType = "goalQuest";
    }
    else
    {
      promotionType = "other";
    }
    String[] levelArray = null;
    List<BadgePromotionLevels> levelsList = this.gamificationDAO.getLevelsByPromotions( promotionId, promotionType );
    return levelsList;

  }

  /**
   * 
   * {@inheritDoc}
   */
  public String getPromotionTypeById( String promotionId )
  {
    long promoId = Long.parseLong( promotionId );
    String promotionType = "";
    Promotion promo = getPromotionService().getPromotionById( (Long)promoId );
    if ( promo.getPromotionType().getCode().equalsIgnoreCase( PromotionType.GOALQUEST ) || promo.getPromotionType().getCode().equalsIgnoreCase( PromotionType.CHALLENGE_POINT ) )
    {
      promotionType = "goalQuest";
    }
    else if ( promo.getPromotionType().getCode().equalsIgnoreCase( PromotionType.THROWDOWN ) )
    {
      promotionType = "throwDown";
    }
    else
    {
      promotionType = "other";
    }
    return promotionType;
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.gamification.GamificationService#getgetLiveCompletedPromotionList()
   * 
   * @return List
   */

  public List getLiveCompletedPromotionList()
  {
    List eligiblePromotionList = new ArrayList();
    PromotionQueryConstraint promoQueryConstraint = new PromotionQueryConstraint();
    promoQueryConstraint
        .setPromotionStatusTypesIncluded( new PromotionStatusType[] { PromotionStatusType.lookup( PromotionStatusType.LIVE ), PromotionStatusType.lookup( PromotionStatusType.COMPLETE ) } );
    promoQueryConstraint.setPromotionTypesExcluded( new PromotionType[] { PromotionType.lookup( PromotionType.DIY_QUIZ ),
                                                                          PromotionType.lookup( PromotionType.BADGE ),
                                                                          PromotionType.lookup( PromotionType.SELF_SERV_INCENTIVES ) } );
    AssociationRequestCollection promoAssociationRequestCollection = new AssociationRequestCollection();
    promoQueryConstraint.setOrderByPromotionNameCaseInsensitive( true );
    eligiblePromotionList = getPromotionService().getPromotionListWithAssociations( promoQueryConstraint, promoAssociationRequestCollection );
    return eligiblePromotionList;
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.gamification.GamificationService#getbuildBadgeLibraryList()
   * 
   * @return List
   */
  public List buildBadgeLibraryList()
  {
    List badgeLibListFinal = new ArrayList();
    String defaultBadgeLibLabel = CmsResourceBundle.getCmsBundle().getString( "gamification.admin.labels.CHOOSE_BADGE" );
    List badgeLibraryLisInitial = new ArrayList();
    BadgeLibrary badgeLib1 = new BadgeLibrary();
    badgeLib1.setBadgeLibraryId( "-1" );
    badgeLib1.setLibraryname( defaultBadgeLibLabel );
    badgeLibraryLisInitial.add( badgeLib1 );
    ContentReader contentReader = ContentReaderManager.getContentReader();
    List badgeLibList = new ArrayList();
    badgeLibList = (List)contentReader.getContent( "promotion.badge", UserManager.getLocale() );

    Iterator it = badgeLibList.iterator();
    while ( it.hasNext() )
    {
      Content content = (Content)it.next();
      Map m = content.getContentDataMapList();
      Translations nameObject = (Translations)m.get( "NAME" );
      Translations descObject = (Translations)m.get( "DESCRIPTION" );
      String badgeLibId = nameObject.getValue();
      String badgeLibDesc = descObject.getValue();
      BadgeLibrary badgeLib = new BadgeLibrary();
      badgeLib.setBadgeLibraryId( badgeLibId );
      badgeLib.setLibraryname( badgeLibDesc );
      badgeLibListFinal.add( badgeLib );
    }
    PropertyComparator.sort( badgeLibListFinal, new MutableSortDefinition( "libraryname", false, true ) );
    badgeLibraryLisInitial.addAll( badgeLibListFinal );
    return badgeLibraryLisInitial;
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.gamification.GamificationService#buildBadgeLibraryListProgress()
   * 
   * @return List
   */

  public List buildBadgeLibraryListProgress()
  {
    List badgeLibListFinal = new ArrayList();
    String defaultBadgeLibLabel = CmsResourceBundle.getCmsBundle().getString( "gamification.admin.labels.CHOOSE_BADGE" );
    List badgeLibraryLisInitial = new ArrayList();
    BadgeLibrary badgeLib1 = new BadgeLibrary();
    badgeLib1.setBadgeLibraryId( "-1" );
    badgeLib1.setLibraryname( defaultBadgeLibLabel );
    badgeLibraryLisInitial.add( badgeLib1 );
    ContentReader contentReader = ContentReaderManager.getContentReader();
    List badgeLibList = new ArrayList();
    badgeLibList = (List)contentReader.getContent( "promotion.badge", UserManager.getLocale() );

    Iterator it = badgeLibList.iterator();
    while ( it.hasNext() )
    {
      Content content = (Content)it.next();
      Map m = content.getContentDataMapList();
      Translations nameObject = (Translations)m.get( "NAME" );
      Translations descObject = (Translations)m.get( "DESCRIPTION" );
      String badgeLibId = nameObject.getValue();
      String badgeLibDesc = descObject.getValue();
      String isBadgeLibMultipleStr = "";
      Translations earnMultipleObject = (Translations)m.get( "EARN_MULTIPLE" );
      if ( earnMultipleObject != null )
      {
        isBadgeLibMultipleStr = earnMultipleObject.getValue();
      }
      if ( isBadgeLibMultipleStr.equalsIgnoreCase( "true" ) )
      {
        BadgeLibrary badgeLib = new BadgeLibrary();
        badgeLib.setBadgeLibraryId( badgeLibId );
        badgeLib.setLibraryname( badgeLibDesc );
        badgeLibListFinal.add( badgeLib );
      }
    }
    PropertyComparator.sort( badgeLibListFinal, new MutableSortDefinition( "libraryname", false, true ) );
    badgeLibraryLisInitial.addAll( badgeLibListFinal );
    return badgeLibraryLisInitial;
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.gamification.GamificationService#saveBadge(com.biperf.core.domain.gamification.Badge)
   * 
   * @param badgeToSave
   * @return Badge
   */
  public Badge saveBadge( Badge badgeToSave ) throws ServiceErrorException
  {
    Badge badgeToReturn = null;
    badgeToReturn = this.gamificationDAO.saveBadge( badgeToSave );
    return badgeToReturn;
  }

  public Badge saveBadge( Long badgeToSave, UpdateAssociationRequest updateAssociationRequest ) throws ServiceErrorException
  {
    if ( updateAssociationRequest != null )
    {
      List updateAssociations = new ArrayList();
      updateAssociations.add( updateAssociationRequest );
      return saveBadge( badgeToSave, updateAssociations );
    }

    return null;
  }

  public Badge saveBadge( Long badgeToSave, List updateAssociationRequests ) throws ServiceErrorException
  {
    if ( updateAssociationRequests != null && updateAssociationRequests.size() > 0 )
    {
      Badge badge = this.getBadgeById( badgeToSave );

      for ( int i = 0; i < updateAssociationRequests.size(); i++ )
      {
        UpdateAssociationRequest request = (UpdateAssociationRequest)updateAssociationRequests.get( i );
        request.execute( badge );
      }

      return saveBadge( badge );
    }

    return null;
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.gamification.GamificationService#saveBadge(com.biperf.core.domain.gamification.Badge)
   * 
   * @param participantBadge
   * @return ParticipantBadge
   */
  public ParticipantBadge saveParticipantBadge( ParticipantBadge participantBadge )
  {
    ParticipantBadge badgeToReturn = null;
    badgeToReturn = this.gamificationDAO.saveParticipantBadge( participantBadge );
    return badgeToReturn;
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.gamification.GamificationService#saveBadgeRules(List<com.biperf.core.domain.gamification.BadgeRule>)
   * @param badgeRuleList
   * @return BadgeRule
   */
  public BadgeRule saveBadgeRules( List<BadgeRule> badgeRuleList ) throws ServiceErrorException
  {
    BadgeRule badgeToReturn = null;
    for ( BadgeRule badgerule : badgeRuleList )
    {
      badgeToReturn = this.gamificationDAO.saveBadgeRule( badgerule );
    }

    return badgeToReturn;
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.gamification.GamificationService#saveBadgePromotion(com.biperf.core.domain.gamification.BadgePromotion)
   * @param badgePromo
   * @return BadgePromotion
   */

  public BadgePromotion saveBadgePromotion( BadgePromotion badgePromo ) throws ServiceErrorException
  {
    BadgePromotion badgeToReturn = null;

    badgeToReturn = this.gamificationDAO.saveBadgePromotion( badgePromo );

    return badgeToReturn;
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.gamification.GamificationService#isBadgeNameExists(java.lang.String)
   * @param badgeSetupName
   * @return String
   */
  public String isBadgeNameExists( String name, String badgeId ) throws ServiceErrorException
  {
    String isBadgeExists = "N";
    isBadgeExists = this.gamificationDAO.isBadgeNameExists( name, badgeId );
    return isBadgeExists;
  }

  public List<ParticipantBadge> getBadgesForRecognitionConfirmation( List<ParticipantBadge> partcipantBadgeList, int rowNumber )
  {
    List<ParticipantBadge> paxBadgeFiltered = new ArrayList<ParticipantBadge>();

    for ( ParticipantBadge participantBadge : partcipantBadgeList )
    {
      Badge badge = participantBadge.getBadgePromotion();
      BadgeRule badgeRule = participantBadge.getBadgeRule();
      Long displayEndDays = badge.getDisplayEndDays();
      Long tileHighLightDays = badge.getTileHighlightPeriod();
      boolean withinDisplayDateFlag = false;
      boolean withinPromotionEndDate = false;
      boolean progressBadgeAlreadyPresent = false;
      Date dateEarned = participantBadge.getEarnedDate();
      Date currentDate = new Date();
      SimpleDateFormat sdf = new SimpleDateFormat( "YYYY-MM-DD" );

      if ( badge.getDisplayEndDate() != null )
      {
        if ( badge.getDisplayEndDate().compareTo( currentDate ) > 0 )
        {
          withinPromotionEndDate = true;
        }
      }
      else
      {
        try
        {
          badge.setDisplayEndDate( sdf.parse( "9999-12-31" ) );
        }
        catch( ParseException p )
        {
          logger.error( p );
        }
        withinPromotionEndDate = true;
      }
      if ( dateEarned != null )
      {
        Calendar cal = Calendar.getInstance();
        cal.setTime( dateEarned );
        if ( tileHighLightDays != null )
        {
          cal.add( Calendar.DATE, tileHighLightDays.intValue() );
        }

        if ( cal.getTime().compareTo( currentDate ) > 0 )
        {
          withinDisplayDateFlag = true;
        }
      }

      Long progressNumerator = 0L;
      Long progressDenominator = 0L;
      Long badgeRuleId = 0L;

      if ( badge.getBadgeType().getCode().equalsIgnoreCase( "progress" ) )
      {
        Iterator badgeRulesIterator = badge.getBadgeRules().iterator();
        while ( badgeRulesIterator.hasNext() )
        {
          BadgeRule br = (BadgeRule)badgeRulesIterator.next();
          badgeRuleId = br.getId();
        }
        String badgeCountType = badge.getBadgeCountType().getCode();
        if ( participantBadge.getSentCount() == null )
        {
          participantBadge.setSentCount( new Long( 0 ) );
        }
        if ( participantBadge.getReceivedCount() == null )
        {
          participantBadge.setReceivedCount( new Long( 0 ) );
        }
        if ( badgeCountType != null && badgeCountType.equalsIgnoreCase( "given" ) )
        {
          progressNumerator = participantBadge.getSentCount();
        }
        else if ( badgeCountType != null && badgeCountType.equalsIgnoreCase( "received" ) )
        {
          progressNumerator = participantBadge.getReceivedCount();
        }
        else if ( badgeCountType != null && badgeCountType.equalsIgnoreCase( "total" ) )
        {
          progressNumerator = participantBadge.getSentCount() + participantBadge.getReceivedCount();
        }
        if ( !withinDisplayDateFlag && participantBadge.getIsEarned() && badge.getBadgeRules().size() > 1 )
        {
          if ( !participantBadge.getBadgeRule().getId().equals( badgeRuleId ) )
          {
            continue;
          }
        }
        progressDenominator = badgeRule.getMaximumQualifier();

        if ( !participantBadge.getIsEarned() )
        {
          progressBadgeAlreadyPresent = checkBadgeAlreadyPresentRecognition( badge.getId(), paxBadgeFiltered );
        }
      }
      else
      {
        progressBadgeAlreadyPresent = false;
      }

      if ( !withinPromotionEndDate )
      {
        continue;
      }

      if ( progressBadgeAlreadyPresent )
      {
        continue;
      }
      else
      {
        if ( rowNumber > 0 )
        {
          if ( paxBadgeFiltered.size() <= rowNumber )
          {
            paxBadgeFiltered.add( participantBadge );
          }
        }
        else
        {
          paxBadgeFiltered.add( participantBadge );
        }
      }

    }

    return paxBadgeFiltered;
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.gamification.GamificationService#getgetTileViewJson(java.util.List)
   * @param partcipantBadgeList
   * @return GamificationBadgeTileView
   */
  public GamificationBadgeTileView getTileViewJson( List<ParticipantBadge> partcipantBadgeList )
  {
    GamificationBadgeTileView gamificationTile = new GamificationBadgeTileView();
    String noBadgeEarnedLabel = CmsResourceBundle.getCmsBundle().getString( "gamification.admin.labels.NO_BADE_COMPLETED" );
    boolean isBadgeEarned = false;
    Long idCount = 0L;
    List<BadgeDetails> badgeDetailsList = new ArrayList<BadgeDetails>();
    if ( partcipantBadgeList == null || partcipantBadgeList.isEmpty() )
    {
      BadgeInfo badgeInfo = new BadgeInfo();
      gamificationTile.setBadgeInfo( badgeInfo );
    }
    else
    {
      for ( ParticipantBadge participantBadge : partcipantBadgeList )
      {
        Badge badge = participantBadge.getBadgePromotion();
        BadgeRule badgeRule = participantBadge.getBadgeRule();
        Long tileHighLightDays = badge.getTileHighlightPeriod();
        boolean withinDisplayDateFlag = false;
        boolean withinPromotionEndDate = false;
        boolean progressBadgeAlreadyPresent = false;
        Date dateEarned = participantBadge.getEarnedDate();
        Date currentDate = new Date();

        if ( badge.getDisplayEndDate() != null )
        {
          if ( badge.getDisplayEndDate().compareTo( currentDate ) > 0 )
          {
            withinPromotionEndDate = true;
          }
        }
        else
        {
          withinPromotionEndDate = true;
        }
        if ( dateEarned != null )
        {
          Calendar cal = Calendar.getInstance();
          cal.setTime( dateEarned );
          if ( tileHighLightDays != null )
          {
            cal.add( Calendar.DATE, tileHighLightDays.intValue() );
          }

          if ( cal.getTime().compareTo( currentDate ) > 0 )
          {
            withinDisplayDateFlag = true;
          }
        }

        Long progressNumerator = 0L;
        Long progressDenominator = 0L;
        Long badgeRuleId = 0L;

        if ( badge.getBadgeType().getCode().equalsIgnoreCase( "progress" ) )
        {
          Iterator badgeRulesIterator = badge.getBadgeRules().iterator();
          while ( badgeRulesIterator.hasNext() )
          {
            BadgeRule br = (BadgeRule)badgeRulesIterator.next();
            badgeRuleId = br.getId();
          }
          String badgeCountType = badge.getBadgeCountType().getCode();
          if ( participantBadge.getSentCount() == null )
          {
            participantBadge.setSentCount( new Long( 0 ) );
          }
          if ( participantBadge.getReceivedCount() == null )
          {
            participantBadge.setReceivedCount( new Long( 0 ) );
          }
          if ( badgeCountType != null && badgeCountType.equalsIgnoreCase( "given" ) )
          {
            progressNumerator = participantBadge.getSentCount();
          }
          else if ( badgeCountType != null && badgeCountType.equalsIgnoreCase( "received" ) )
          {
            progressNumerator = participantBadge.getReceivedCount();
          }
          else if ( badgeCountType != null && badgeCountType.equalsIgnoreCase( "total" ) )
          {
            progressNumerator = participantBadge.getSentCount() + participantBadge.getReceivedCount();
          }
          if ( !withinDisplayDateFlag && participantBadge.getIsEarned() && badge.getBadgeRules().size() > 1 )
          {
            if ( !participantBadge.getBadgeRule().getId().equals( badgeRuleId ) )
            {
              continue;
            }
          }
          progressDenominator = badgeRule.getMaximumQualifier();

          if ( !participantBadge.getIsEarned() )
          {
            progressBadgeAlreadyPresent = checkBadgeAlreadyPresent( badge.getId(), badgeDetailsList );
          }
        }
        else
        {
          progressBadgeAlreadyPresent = false;
        }

        if ( !withinPromotionEndDate )
        {
          continue;
        }

        idCount = idCount + 1;
        BadgeDetails badgeDetail = new BadgeDetails();
        badgeDetail.setUniqueId( idCount );
        badgeDetail.setBadgeId( participantBadge.getBadgePromotion().getId() );
        badgeDetail.setBadgeType( badge.getBadgeType().getCode() );

        if ( participantBadge.getEarnedDate() != null )
        {
          badgeDetail.setDateEarned( DateUtils.toDisplayString( participantBadge.getEarnedDate() ) );
        }

        badgeDetail.setEarned( participantBadge.getIsEarned() );
        badgeDetail.setProgressDenominator( progressDenominator );
        badgeDetail.setProgressNumerator( progressNumerator );
        String siteUrlPrefix = getSystemVariableService().getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();
        List earnedNotEarnedImageList = getEarnedNotEarnedImageList( badgeRule.getBadgeLibraryCMKey() );
        Iterator itr = earnedNotEarnedImageList.iterator();
        while ( itr.hasNext() )
        {
          BadgeLibrary badgeLib = (BadgeLibrary)itr.next();
          badgeDetail.setImg( siteUrlPrefix + "/" + badgeLib.getEarnedImageSmall() );
          badgeDetail.setImgLarge( siteUrlPrefix + "/" + badgeLib.getEarnedImageMedium() );
        }
        if ( badgeDetailsList.size() < 10 )
        {
          badgeDetail.setBadgeName( badgeRule.getBadgeNameTextFromCM() );
          SSIContest contest = participantBadge.getContest();
          if ( contest != null )
          {
            badgeDetail.setContestName( getContestName( contest, UserManager.getLocale() ) );
            badgeDetail.setBadgeDescription( null );
          }
          else
          {
            badgeDetail.setBadgeDescription( participantBadge.getBadgeRule().getBadgeDescriptionTextFromCM() );
          }
        }
        if ( badgeDetailsList.size() < 10 && allowAddToList( badgeDetailsList, badgeDetail ) )
        {
          badgeDetailsList.add( badgeDetail );
        }

        if ( participantBadge.getIsEarned() )
        {
          isBadgeEarned = true;
        }

      }
      BadgeInfo badgeInfo = new BadgeInfo();
      if ( !isBadgeEarned )
      {
        badgeInfo.setNoBadgesCompleteText( noBadgeEarnedLabel );
      }
      else
      {
        badgeInfo.setNoBadgesCompleteText( "" );
      }
      badgeInfo.setBadgeDetails( badgeDetailsList );
      gamificationTile.setBadgeInfo( badgeInfo );
    }

    return gamificationTile;
  }

  private boolean allowAddToList( List<BadgeDetails> badgeDetails, BadgeDetails badgeDetail )
  {
    boolean allow = true;
    if ( badgeDetail.getBadgeType().equals( "progress" ) )
    {
      for ( BadgeDetails bd : badgeDetails )
      {
        if ( "progress".equals( bd.getBadgeType() ) && !bd.getProgressNumerator().equals( bd.getProgressDenominator() ) )
        {
          allow = false;
        }
      }
    }
    return allow;
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.gamification.GamificationService#getgetProfileViewJson(java.util.List)
   * @param partcipantBadgeList
   * @return GamificationBadgeProfileView
   */
  public GamificationBadgeProfileView getProfileViewJson( List<ParticipantBadge> partcipantBadgeList, boolean isFromMiniProfile )
  {
    HashMap<String, List<BadgeDetails>> participantMap = new HashMap<String, List<BadgeDetails>>();
    GamificationBadgeProfileView gamificationTile = new GamificationBadgeProfileView();
    String historyLabel = CmsResourceBundle.getCmsBundle().getString( "gamification.admin.labels.HISTORY_LABEL" );
    String otherBadgeLabel = CmsResourceBundle.getCmsBundle().getString( "gamification.admin.labels.OTHER_BADGE_LABEL" );
    participantMap = getPartcipantBadgeByPromotion( partcipantBadgeList, isFromMiniProfile );
    List<BadgeInfo> badgeInfoList = new ArrayList<BadgeInfo>();
    List<BadgeInfo> badgeInfoOtherList = new ArrayList<BadgeInfo>();
    List<BadgeInfo> badgeInfoHistoryList = new ArrayList<BadgeInfo>();
    for ( Map.Entry<String, List<BadgeDetails>> entry : participantMap.entrySet() )
    {
      Long badgeId = entry.getValue().get( 0 ).getBadgeId();
      Badge badge = (Badge)getPromotionService().getPromotionById( badgeId );
      Long allBehaviorPoints = badge.getAllBehaviorPoints();

      if ( !entry.getKey().toString().equalsIgnoreCase( otherBadgeLabel ) && !entry.getKey().toString().equalsIgnoreCase( historyLabel ) )
      {
        entry.getValue().get( 0 ).getBadgeId();
        BadgeInfo badgeInfo = new BadgeInfo();
        badgeInfo.setHeaderTitle( entry.getKey().toString() );
        badgeInfo.setBadgeDetails( entry.getValue() );
        badgeInfo.setAllBehaviorPoints( allBehaviorPoints );
        badgeInfo.setShowProgress( true );
        badgeInfoList.add( badgeInfo );
      }
      else if ( entry.getKey().toString().equalsIgnoreCase( otherBadgeLabel ) )
      {
        BadgeInfo badgeInfo = new BadgeInfo();
        badgeInfo.setHeaderTitle( entry.getKey().toString() );
        badgeInfo.setBadgeDetails( entry.getValue() );
        badgeInfo.setAllBehaviorPoints( allBehaviorPoints );
        badgeInfo.setShowProgress( true );
        badgeInfoOtherList.add( badgeInfo );
      }
      else if ( entry.getKey().toString().equalsIgnoreCase( historyLabel ) )
      {
        BadgeInfo badgeInfo = new BadgeInfo();
        badgeInfo.setHeaderTitle( entry.getKey().toString() );
        badgeInfo.setBadgeDetails( entry.getValue() );
        badgeInfo.setAllBehaviorPoints( allBehaviorPoints );
        badgeInfo.setShowProgress( false );
        badgeInfoHistoryList.add( badgeInfo );
      }

    }
    badgeInfoList.addAll( badgeInfoOtherList );
    badgeInfoList.addAll( badgeInfoHistoryList );
    gamificationTile.setBadgeGroups( badgeInfoList );
    return gamificationTile;
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.gamification.GamificationService#buildFileLoadTableList(int)
   * 
   * @return List
   */
  public List buildFileLoadTableList( int count, List<BadgeRule> badgeRuleList )
  {
    List fileLoadTableList = new ArrayList();
    if ( badgeRuleList != null )
    {
      int i = 0;
      for ( Iterator iter = badgeRuleList.iterator(); iter.hasNext(); )
      {
        BadgeRule badgeRule = (BadgeRule)iter.next();
        BadgeTableBean fileLoadForm = new BadgeTableBean();
        fileLoadForm.setIndexNumber( i );
        fileLoadForm.setBadgeLibraryList( buildBadgeLibraryList() );
        fileLoadForm.setBadgeDescription( badgeRule.getBadgeDescription() );
        fileLoadForm.setBadgeName( badgeRule.getBadgeName() );
        fileLoadForm.setLevelName( badgeRule.getLevelName() );
        fileLoadForm.setPromotionName( badgeRule.getPromotionNames() );
        fileLoadForm.setRangeAmountMax( badgeRule.getMaximumQualifier() != null ? badgeRule.getMaximumQualifier().toString() : "" );
        fileLoadForm.setRangeAmountMin( badgeRule.getMinimumQualifier() != null ? badgeRule.getMinimumQualifier().toString() : "" );
        fileLoadTableList.add( fileLoadForm );
        i++;
      }
    }
    else
    {
      for ( int i = 0; i < count; i++ )
      {
        BadgeTableBean fileLoadForm = new BadgeTableBean();
        fileLoadForm.setIndexNumber( i );
        fileLoadForm.setBadgeLibraryList( buildBadgeLibraryList() );
        fileLoadTableList.add( fileLoadForm );
      }
    }

    return fileLoadTableList;
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.gamification.GamificationService#buildProgressTableList(int)
   * 
   * @return List
   */
  public List buildProgressTableList( int count, List<BadgeRule> badgeRuleList )
  {
    List progressTableList = new ArrayList();
    if ( badgeRuleList != null )
    {
      int i = 0;
      for ( Iterator iter = badgeRuleList.iterator(); iter.hasNext(); )
      {
        BadgeRule badgeRule = (BadgeRule)iter.next();
        BadgeTableBean fileLoadForm = new BadgeTableBean();
        fileLoadForm.setIndexNumber( i );
        fileLoadForm.setBadgeLibraryList( buildBadgeLibraryList() );
        fileLoadForm.setBadgeDescription( badgeRule.getBadgeDescription() );
        fileLoadForm.setBadgeName( badgeRule.getBadgeName() );
        fileLoadForm.setLevelName( badgeRule.getLevelName() );
        fileLoadForm.setPromotionName( badgeRule.getPromotionNames() );
        fileLoadForm.setRangeAmountMax( badgeRule.getMaximumQualifier() != null ? badgeRule.getMaximumQualifier().toString() : "" );
        fileLoadForm.setRangeAmountMin( badgeRule.getMinimumQualifier() != null ? badgeRule.getMinimumQualifier().toString() : "" );
        progressTableList.add( fileLoadForm );
        i++;
      }
    }
    else
    {
      for ( int i = 0; i < count; i++ )
      {
        BadgeTableBean progressForm = new BadgeTableBean();
        progressForm.setIndexNumber( i );
        progressForm.setBadgeLibraryList( buildBadgeLibraryListProgress() );
        progressTableList.add( progressForm );
      }
    }

    return progressTableList;
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.gamification.GamificationService#buildPointRangeTableList(int)
   * 
   * @return List
   */
  public List buildPointRangeTableList( int count, List<BadgeRule> badgeRuleList )
  {
    List pointRangeTableList = new ArrayList();
    if ( badgeRuleList != null )
    {
      int i = 0;
      for ( Iterator iter = badgeRuleList.iterator(); iter.hasNext(); )
      {
        BadgeRule badgeRule = (BadgeRule)iter.next();
        BadgeTableBean fileLoadForm = new BadgeTableBean();
        fileLoadForm.setIndexNumber( i );
        fileLoadForm.setBadgeLibraryList( buildBadgeLibraryList() );
        fileLoadForm.setBadgeDescription( badgeRule.getBadgeDescription() );
        fileLoadForm.setBadgeName( badgeRule.getBadgeName() );
        fileLoadForm.setLevelName( badgeRule.getLevelName() );
        fileLoadForm.setPromotionName( badgeRule.getPromotionNames() );
        fileLoadForm.setRangeAmountMax( badgeRule.getMaximumQualifier() != null ? badgeRule.getMaximumQualifier().toString() : "" );
        fileLoadForm.setRangeAmountMin( badgeRule.getMinimumQualifier() != null ? badgeRule.getMinimumQualifier().toString() : "" );
        pointRangeTableList.add( fileLoadForm );
        i++;
      }
    }
    else
    {
      for ( int i = 0; i < count; i++ )
      {
        BadgeTableBean pointRangeForm = new BadgeTableBean();
        pointRangeForm.setIndexNumber( i );
        pointRangeForm.setBadgeLibraryList( buildBadgeLibraryList() );
        pointRangeTableList.add( pointRangeForm );
      }
    }

    return pointRangeTableList;
  }

  /**
   * Overridden from
   * 
   * {@inheritDoc}
   */
  public List buildPromotionTableList( GoalBadgeRule badgeRule )
  {
    List promotionTableList = new ArrayList();

    BadgeTableBean pointRangeForm = new BadgeTableBean();
    pointRangeForm.setPromotionName( badgeRule.getPromotionName() );
    pointRangeForm.setBadgeLibraryList( buildBadgeLibraryList() );

    if ( badgeRule.getLevelNames() != null )
    {
      int count = badgeRule.getLevelNames().size();
      for ( int i = 0; i < count; i++ )
      {
        pointRangeForm.setIndexNumber( i );
        pointRangeForm.setLevelName( badgeRule.getLevelNames().get( i ) );
      }
    }
    promotionTableList.add( pointRangeForm );
    return promotionTableList;
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.gamification.GamificationService#getBadgeLibDescription(java.lang.String)
   * 
   * @return String
   */
  public String getBadgeLibDescription( String badgeLibCode )
  {
    String badgeDesc = "";
    ContentReader contentReader = ContentReaderManager.getContentReader();
    List badgeLibList = new ArrayList();
    badgeLibList = (List)contentReader.getContent( "promotion.badge", UserManager.getLocale() );
    Iterator it = badgeLibList.iterator();
    while ( it.hasNext() )
    {
      Content content = (Content)it.next();
      Map m = content.getContentDataMapList();
      Translations nameObject = (Translations)m.get( "NAME" );

      if ( nameObject.getValue().equalsIgnoreCase( badgeLibCode ) )
      {
        Translations descObject = (Translations)m.get( "DESCRIPTION" );
        badgeDesc = descObject.getValue();
        break;
      }

    }
    return badgeDesc;
  }

  /**
   * Method to retrieve the earned and not earned image urls for a badge library
   * @param badgeLibId 
   * @return List
   */
  public List getEarnedNotEarnedImageList( String badgeLibId )
  {
    return getEarnedNotEarnedImageList( badgeLibId, UserManager.getLocale() );
  }

  public List getEarnedNotEarnedImageList( String badgeLibId, Locale locale )
  {
    List badgeLibrarySelectedList = new ArrayList();
    ContentReader contentReader = ContentReaderManager.getContentReader();
    List badgeLibList = new ArrayList();
    badgeLibList = (List)contentReader.getContent( "promotion.badge", locale );
    Iterator it = badgeLibList.iterator();
    while ( it.hasNext() )
    {
      Content content = (Content)it.next();
      Map m = content.getContentDataMapList();
      Translations nameObject = (Translations)m.get( "NAME" );
      Translations descObject = (Translations)m.get( "DESCRIPTION" );
      if ( nameObject.getValue().equalsIgnoreCase( badgeLibId ) )
      {
        Translations earnedImageObjectLarge = (Translations)m.get( "EARNED_IMAGE_LARGE" );
        Translations earnedImageObjectMedium = (Translations)m.get( "EARNED_IMAGE_MEDIUM" );
        Translations earnedImageObjectSmall = (Translations)m.get( "EARNED_IMAGE_SMALL" );
        BadgeLibrary badgeLib = new BadgeLibrary();
        badgeLib.setBadgeLibraryId( badgeLibId );
        badgeLib.setEarnedImageLarge( earnedImageObjectLarge.getValue() );
        badgeLib.setEarnedImageMedium( earnedImageObjectMedium.getValue() );
        badgeLib.setEarnedImageSmall( earnedImageObjectSmall.getValue() );
        badgeLibrarySelectedList.add( badgeLib );
        break;
      }

    }
    return badgeLibrarySelectedList;
  }

  /**
   * Method to check same badge id already present in the list for a progress badge
   * @param badgeId 
   * @param badgeDetailsList
   * @return boolean
   */

  public boolean checkBadgeAlreadyPresent( Long badgeId, List badgeDetailsList )
  {
    boolean badgePresent = false;
    Iterator it = badgeDetailsList.iterator();
    while ( it.hasNext() )
    {
      BadgeDetails badgeDetail = (BadgeDetails)it.next();
      if ( badgeId.intValue() == badgeDetail.getBadgeId().intValue() )
      {
        badgePresent = true;
        break;
      }
    }
    return badgePresent;
  }

  /**
   * Method to check same badge id already present in the list for a progress badge
   * @param badgeId 
   * @param badgeDetailsList
   * @return boolean
   */

  public boolean checkBadgeAlreadyPresentRecognition( Long badgeId, List paxBadges )
  {
    boolean badgePresent = false;
    Iterator it = paxBadges.iterator();
    while ( it.hasNext() )
    {
      ParticipantBadge badgeDetail = (ParticipantBadge)it.next();
      if ( badgeId.intValue() == badgeDetail.getBadgePromotion().getId().intValue() )
      {
        badgePresent = true;
        break;
      }
    }
    return badgePresent;
  }

  /**
   * Method to check same badge id already present in the list for a progress badge
   * @param badgeId 
   * @param badgeDetailsList
   * @return boolean
   */

  public boolean checkBehaviorAlreadyPresent( String behaviorCode, List behaviorList )
  {
    boolean behaviorPresent = false;
    Iterator it = behaviorList.iterator();
    while ( it.hasNext() )
    {
      BadgeBehaviorPromotion badgeBehavior = (BadgeBehaviorPromotion)it.next();
      if ( behaviorCode.equalsIgnoreCase( badgeBehavior.getBehaviorCode() ) )
      {
        behaviorPresent = true;
        break;
      }
    }
    return behaviorPresent;
  }

  /**
   * Verifies the specified participant import file a page at a time.
   * 
   * @param importFileId
   * @param userId
   * @return Map result set
   */
  public Map verifyImportFile( Long importFileId, Long userId, Long badgeId, Date earnedDate )
  {
    return gamificationDAO.verifyImportFile( importFileId, "V", userId, badgeId, earnedDate );
  }

  /**
   * Import the specified participant import file a page at a time.
   * 
   * @param importFileId
   * @param userId
   * @return Map result set
   */
  public Map importImportFile( Long importFileId, Long userId, Long badgeId, Date earnedDate )
  {
    return gamificationDAO.verifyImportFile( importFileId, "L", userId, badgeId, earnedDate );
  }

  /**
   * Method to populate participant badge table for a claim for a promotion.
   * This method will be getting called from all the different places where partcipant has a claim for a promotion(eg: quiz completed, send recognition etc)
   * @param claim
   * @param badge
   * @throws ServiceErrorException 
   */
  @Override
  public List<ParticipantBadge> populateBadgePartcipant( Claim claim ) throws ServiceErrorException
  {
    List<ParticipantBadge> participantBadges = new ArrayList<>();
    List<Badge> badgeList = new ArrayList<Badge>();
    Promotion promotion = claim.getPromotion();
    if ( promotion.isDIYQuizPromotion() )
    {
      Badge badge = promotion.getBadge();
      if ( badge != null )
      {
        badgeList.add( badge );
      }
    }
    else
    {
      badgeList = getBadgeByBadgeIdAndPromotionId( promotion.getId() );
    }
    Iterator badgeItr = badgeList.iterator();

    while ( badgeItr.hasNext() )
    {
      Badge badge = (Badge)badgeItr.next();
      if ( badge.getBadgeType().getCode().equalsIgnoreCase( BadgeType.PROGRESS ) && claim.isRecognitionClaim() )
      {
        RecognitionClaim recClaim = (RecognitionClaim)claim;
        if ( recClaim.getSubmitter() != null )
        {
          participantBadges.add( populatePartcipantProgressBadge( claim, badge ) );
        }
      }
      else if ( badge.getBadgeType().getCode().equalsIgnoreCase( BadgeType.BEHAVIOR ) && claim.isRecognitionClaim() )
      {
        RecognitionClaim recClaim = (RecognitionClaim)claim;
        if ( recClaim.getBehavior() != null )
        {
          participantBadges = populatePartcipantBehaviorBadge( claim, badge );
        }

      }
      else if ( badge.getBadgeType().getCode().equalsIgnoreCase( BadgeType.BEHAVIOR ) && claim.isNominationClaim() )
      {
        NominationClaim nomClaim = (NominationClaim)claim;
        if ( !nomClaim.getNominationClaimBehaviors().isEmpty() )
        {
          participantBadges = populatePartcipantBehaviorBadge( claim, badge );
        }
      }
      else if ( badge.getBadgeType().getCode().equalsIgnoreCase( BadgeType.EARNED_OR_NOT_EARNED )
          && ( promotion.isDIYQuizPromotion() || promotion.isQuizPromotion() || promotion.isRecognitionPromotion() || promotion.isNominationPromotion() || promotion.isProductClaimPromotion() ) )
      {
        participantBadges.add( populatePartcipantEarnedBadge( claim, badge ) );
      }
    }

    return participantBadges;
  }

  public List<Badge> getBadgeByBadgeIdAndPromotionId( Long promotionId )
  {
    return gamificationDAO.getBadgeByBadgeIdAndPromotionId( promotionId );
  }

  /**
   * Method to populate participant badge table if the promotion in the claim has a progress badge
   * @param claim
   * @param badge
   * @throws ServiceErrorException 
   */
  public ParticipantBadge populatePartcipantProgressBadge( Claim claim, Badge badge ) throws ServiceErrorException
  {
    Set<BadgeRule> badgeRules = badge.getBadgeRules();
    List<BadgeRule> sortedList = getSortedBadgeRule( badgeRules );
    RecognitionClaim recClaim = (RecognitionClaim)claim;
    Participant sender = recClaim.getSubmitter();
    Set receipients = recClaim.getClaimRecipients();
    Iterator receipientItr = null;
    if ( receipients != null )
    {
      receipientItr = receipients.iterator();
    }
    String badgeCountType = badge.getBadgeCountType().getCode();
    ParticipantBadge returnBadge = null;
    // save participant badge for sender
    ParticipantBadge senderBadge = null;

    if ( badgeCountType.equalsIgnoreCase( "given" ) || badgeCountType.equalsIgnoreCase( "total" ) )
    {
      senderBadge = buildPartcipantBadgeProgress( sender, badgeCountType, sortedList, "sender", badge, claim.getPromotion() );
    }
    // save participant badge for receiver
    ParticipantBadge receiverBadge = null;
    if ( badgeCountType.equalsIgnoreCase( "received" ) || badgeCountType.equalsIgnoreCase( "total" ) )
    {
      if ( receipientItr != null )
      {
        while ( receipientItr.hasNext() )
        {
          ClaimRecipient receiver = (ClaimRecipient)receipientItr.next();
          receiverBadge = buildPartcipantBadgeProgress( receiver.getRecipient(), badgeCountType, sortedList, "receiver", badge, claim.getPromotion() );
        }
      }
    }
    if ( receiverBadge != null && receiverBadge.getId() != null )
    {
      returnBadge = receiverBadge;
    }
    else
    {
      returnBadge = senderBadge;
    }

    return returnBadge;

  }

  public List<BadgeRule> getSortedBadgeRule( Set<BadgeRule> sortedList )
  {
    List newList = new ArrayList( sortedList );
    if ( sortedList != null && !sortedList.isEmpty() )
    {

      Collections.sort( newList, new Comparator()
      {
        @Override
        public int compare( Object object, Object object1 )
        {
          BadgeRule badgeRule1 = (BadgeRule)object;
          BadgeRule badgeRule2 = (BadgeRule)object1;

          return badgeRule1.getMaximumQualifier().compareTo( badgeRule2.getMaximumQualifier() );
        }
      } );
    }

    return newList;
  }

  /**
   * Method to populate participant badge table if claim has behavior for the promotion
   * @param claim
   * @param badge
   */
  public List<ParticipantBadge> populatePartcipantBehaviorBadge( Claim claim, Badge badge )
  {
    // get behaviors for the badge
    Set<BadgeRule> badgeRules = badge.getBadgeRules();
    List<ParticipantBadge> participantBadges = new ArrayList<>();
    RecognitionClaim recClaim = null;
    NominationClaim nomClaim = null;
    Set receipients = null;
    if ( claim.isRecognitionClaim() )
    {
      recClaim = (RecognitionClaim)claim;
      receipients = recClaim.getClaimRecipients();
    }
    else if ( claim.isNominationClaim() )
    {
      nomClaim = (NominationClaim)claim;
      receipients = nomClaim.getClaimRecipients();
    }
    Iterator<BadgeRule> ruleItr = badgeRules.iterator();
    Iterator receipientItr = receipients.iterator();
    boolean insertRecord = false;
    boolean hasEarnedAllBehaviorPoints = false;
    boolean isEarnedBadgePoints = false;
    while ( receipientItr.hasNext() )
    {
      ClaimRecipient receiver = (ClaimRecipient)receipientItr.next();
      while ( ruleItr.hasNext() )
      {
        BadgeRule rule = (BadgeRule)ruleItr.next();
        String behaviorName = null;

        // if the receiver has opted out of awards then if the badge carries points then we might
        // need to nullify or set to zero
        Participant participant = receiver.getRecipient();
        if ( participant.getOptOutAwards() )
        {
          rule.setBadgePoints( null );
        }

        if ( claim.isRecognitionClaim() )
        {
          behaviorName = recClaim.getBehavior().getName();
          if ( rule.getBehaviorDisplayName().equalsIgnoreCase( behaviorName ) )
          {
            ParticipantBadge participantBadge = new ParticipantBadge();
            participantBadge = populateBehaviors( rule, badge, receiver, claim );
            participantBadges.add( participantBadge );
          }
        }
        else if ( claim.isNominationClaim() )
        {
          for ( NominationClaimBehaviors nominationClaimBehavior : nomClaim.getNominationClaimBehaviors() )
          {
            behaviorName = nominationClaimBehavior.getBehavior().getName();
            if ( rule.getBehaviorDisplayName().equalsIgnoreCase( behaviorName ) )
            {
              ParticipantBadge participantBadge = new ParticipantBadge();
              participantBadge = populateBehaviors( rule, badge, receiver, claim );
              participantBadges.add( participantBadge );
            }
          }
        }
      }
    }
    return participantBadges;
  }

  private ParticipantBadge populateBehaviors( BadgeRule rule, Badge badge, ClaimRecipient receiver, Claim claim )
  {
    boolean insertRecord = false;
    boolean hasEarnedAllBehaviorPoints = false;
    boolean isEarnedBadgePoints = false;
    ParticipantBadge returnPax = new ParticipantBadge();
    String promotionName = null;

    String badgeLibCm = rule.getBadgeLibraryCMKey();
    boolean isBadgeLibMultiple = isBadgeLibMultipleAllowed( badgeLibCm );
    if ( !isBadgeLibMultiple )
    {
      // if badge lib cannot earn multiple check already the partcipant has any record for the
      // same badge library
      returnPax = updatePartcipantBadge( badge, rule, receiver.getRecipient().getId(), badgeLibCm, claim.getPromotion().getName() );
      if ( returnPax == null )
      {
        insertRecord = true;
      }
      else
      {
        insertRecord = false;
      }
    }
    else
    {
      insertRecord = true;
    }

    int recordExist = canCreateJournal( receiver.getRecipient().getId(), rule.getId(), claim.getPromotion().getId(), badge.getBadgeType().getCode() );

    if ( recordExist == 0 && rule.getBadgePoints() != null && rule.getBadgePoints() > 0 )
    {
      createJournalEntry( (Promotion)badge, receiver.getRecipient(), rule.getBadgePoints() );
      isEarnedBadgePoints = true;
    }

    // Comments to be added
    Long totalBehaviorsEarned = new Long( gamificationDAO.getBehaviorEarnedCount( badge.getId(), receiver.getRecipient().getId() ) );

    if ( totalBehaviorsEarned.intValue() + 1 == badge.getBadgeRules().size() )
    {
      hasEarnedAllBehaviorPoints = true;
    }

    if ( insertRecord )
    {
      ParticipantBadge paxBadge = new ParticipantBadge();
      paxBadge.setBadgePromotion( badge );
      paxBadge.setBadgeRule( rule );
      paxBadge.setEarnedDate( new Date() );
      paxBadge.setIsEarned( true );
      paxBadge.setParticipant( receiver.getRecipient() );
      paxBadge.setStatus( Badge.BADGE_ACTIVE );
      paxBadge.setIsEarnedAllBehaviorPoints( hasEarnedAllBehaviorPoints );
      paxBadge.setIsBadgePointsEarned( isEarnedBadgePoints );
      paxBadge.setClaimId( claim.getId() ); // setting the claim id for the behavioral badges
      returnPax = this.saveParticipantBadge( paxBadge );
      if ( hasEarnedAllBehaviorPoints && badge.getAllBehaviorPoints() != null )
      {
        createJournalEntry( (Promotion)badge, receiver.getRecipient(), badge.getAllBehaviorPoints() );
      }
      if ( badge.getNotificationMessageId() != -1 )
      {
        String localeCode = systemVariableService.getPropertyByName( SystemVariableService.DEFAULT_LANGUAGE ).getStringVal();
        if ( null != claim.getPromotion().getPromoNameAssetCode() )
        {
          if ( null != receiver.getRecipient().getLanguageType() )
          {
            promotionName = getPromotionService().getPromotionNameByLocale( claim.getPromotion().getPromoNameAssetCode(), receiver.getRecipient().getLanguageType().getCode() );
          }
          else
          {
            promotionName = getPromotionService().getPromotionNameByLocale( claim.getPromotion().getPromoNameAssetCode(), localeCode );
          }
        }
        else
        {
          promotionName = claim.getPromotion().getName();
        }
        sendSummaryMessage( (Participant)returnPax.getParticipant(), promotionName, rule, recordExist );
      }
    }
    return returnPax;
  }

  public void createJournalEntry( Promotion promotion, Participant participant, Long points )
  {
    Journal journal = new Journal();
    journal.setGuid( GuidUtils.generateGuid() );
    journal.setPromotion( promotion );
    journal.setParticipant( participant );
    journal.setAccountNumber( participant.getAwardBanqNumber() );
    journal.setTransactionDate( new Date() );
    journal.setTransactionType( JournalTransactionType.lookup( JournalTransactionType.DEPOSIT ) );
    journal.setJournalType( Journal.BADGE_POINTS );
    journal.setJournalStatusType( JournalStatusType.lookup( JournalStatusType.APPROVE ) ); // bug
                                                                                           // 73458
    journal.setAwardPayoutType( PromotionAwardsType.lookup( PromotionAwardsType.POINTS ) );
    // Bug 53489 set promotion-name based on recipient Locale
    String promotionName = promotion.getPromotionName();
    if ( participant != null && participant.getLanguageType() != null )
    {
      promotionName = getPromotionService().getPromotionNameByLocale( promotion.getPromoNameAssetCode(), participant.getLanguageType().getCode() );
      promotionName = StringUtils.isEmpty( promotionName ) ? promotion.getPromotionName() : promotionName;
    }

    if ( participant.isOptOutAwards() )
    {
      journal.setTransactionAmount( new Long( 0 ) );
      journal.setTransactionDescription( Journal.BADGE_POINTS + " - " + promotionName + "-" + OPT_OUT_TEXT );
    }
    else
    {
      journal.setTransactionAmount( points );
      journal.setTransactionDescription( Journal.BADGE_POINTS + " - " + promotionName );
    }

    try
    {
      journalService.saveAndLaunchPointsDepositProcess( journal, false ); // bug 73458
    }
    catch( ServiceErrorException e )
    {
      logger.error( "Error while depositing badge points for promotion : " + promotion.getName() + "\n" + e.getStackTrace() );
    }
  }

  /**
   * Method to populate participant badge table if claim has level or points and the promotion has an earned/not earned badge type
   * @param claim
   * @param badge 
   */

  public ParticipantBadge populatePartcipantEarnedBadge( Claim claim, Badge badge )
  {
    ParticipantBadge returnPax = new ParticipantBadge();
    Set<BadgeRule> badgeRules = badge.getBadgeRules();
    Iterator<BadgeRule> ruleItr = badgeRules.iterator();
    Set receipients = null;
    NominationClaim nomClaim = null;
    if ( claim.isRecognitionClaim() || claim.isNominationClaim() )
    {
      // iterate receipients and insert records
      if ( claim.isRecognitionClaim() )
      {
        RecognitionClaim recClaim = (RecognitionClaim)claim;
        receipients = recClaim.getClaimRecipients();
        returnPax = populateEarnedBadgesRecognitionNomination( receipients, claim, badge );
      }
      else if ( claim.isNominationClaim() )
      {
        nomClaim = (NominationClaim)claim;
        if ( nomClaim.isTeam() )
        {
          receipients = nomClaim.getClaimRecipients();
          returnPax = populateEarnedBadgesNominationTeam( receipients, claim, badge );
        }
        else
        {
          receipients = nomClaim.getClaimRecipients();
          returnPax = populateEarnedBadgesRecognitionNomination( receipients, claim, badge );
        }
      }
    }
    else if ( claim.isProductClaim() )
    {
      ProductClaim productClaim = (ProductClaim)claim;
      Participant receipient = claim.getSubmitter();
      returnPax = populateEarnedBadgesNonRecognition( receipient, claim, badge );

      if ( productClaim.getClaimParticipants() != null )
      {
        receipients = productClaim.getClaimParticipants();
        returnPax = populateEarnedBadgesNominationTeam( receipients, claim, badge );
      }
    }
    else
    {
      Participant receipient = claim.getSubmitter();
      returnPax = populateEarnedBadgesNonRecognition( receipient, claim, badge );
    }

    return returnPax;
  }

  /**
   * Method to find a particular badge library can earn multiple or not
   * @param badgeLibCMCode
   * return boolean
   */
  public boolean isBadgeLibMultipleAllowed( String badgeLibCMCode )
  {
    boolean isBadgeLibMultiple = false;
    ContentReader contentReader = ContentReaderManager.getContentReader();
    List badgeLibList = new ArrayList();
    if ( contentReader.getContent( "promotion.badge", getUserLocale() ) instanceof java.util.List )
    {
      badgeLibList = (List)contentReader.getContent( "promotion.badge", getUserLocale() );
      Iterator it = badgeLibList.iterator();
      while ( it.hasNext() )
      {
        Content content = (Content)it.next();
        Map m = content.getContentDataMapList();
        Translations nameObject = (Translations)m.get( "NAME" );

        if ( nameObject.getValue().equalsIgnoreCase( badgeLibCMCode ) )
        {
          Translations earnMultipleObject = (Translations)m.get( "EARN_MULTIPLE" );
          String isBadgeLibMultipleStr = earnMultipleObject.getValue();
          if ( isBadgeLibMultipleStr.equalsIgnoreCase( "true" ) )
          {
            isBadgeLibMultiple = true;
          }
          break;
        }

      }
    }
    else
    {
      Content content = (Content)contentReader.getContent( "promotion.badge", getUserLocale() );
      if ( content != null )
      {
        Map m = content.getContentDataMapList();
        Translations nameObject = (Translations)m.get( "NAME" );
        if ( nameObject != null )
        {
          if ( nameObject.getValue().equalsIgnoreCase( badgeLibCMCode ) )
          {
            Translations earnMultipleObject = (Translations)m.get( "EARN_MULTIPLE" );
            String isBadgeLibMultipleStr = earnMultipleObject.getValue();
            if ( isBadgeLibMultipleStr.equalsIgnoreCase( "true" ) )
            {
              isBadgeLibMultiple = true;
            }
          }
        }
      }

    }
    return isBadgeLibMultiple;
  }

  /**
   * Method to find a participant has an existing badge record for a badge rule and update the record if badge library cannot be earned multiple times
   * @param badge
   * @param rule
   * @param participantId
   * @param badgeLibCm
   * return ParticipantBadge
   */
  public ParticipantBadge updatePartcipantBadge( Badge badge, BadgeRule rule, Long participantId, String badgeLibCm, String promotionName )
  {
    ParticipantBadge pBadge = this.gamificationDAO.getParticipantBadgeByBadgeLib( rule, participantId );
    if ( pBadge != null )
    {
      // update existing record
      pBadge.setBadgeRule( rule );
      pBadge.setBadgePromotion( badge );
      pBadge.setEarnedDate( new Date() );
      pBadge.setIsEarned( true );
      pBadge.setStatus( Badge.BADGE_ACTIVE );
      pBadge = this.saveParticipantBadge( pBadge );
      if ( badge.getNotificationMessageId() != -1 )
      {
        sendSummaryMessage( (Participant)pBadge.getParticipant(), promotionName, rule, 0 );
      }

    }

    return pBadge;
  }

  /**
   * Method to populate the participant badge for progress badge type. The same method will be used to populate both sender and receiver based on the paxType value
   * @param partcipant
   * @param badgeCountType
   * @param badgeRules
   * @param paxType
   * @param badge
   * return ParticipantBadge
   */
  public ParticipantBadge buildPartcipantBadgeProgress( Participant partcipant, String badgeCountType, List badgeRules, String paxType, Badge badge, Promotion promo ) throws ServiceErrorException
  {
    ParticipantBadge p = new ParticipantBadge();
    Iterator<BadgeRule> ruleItr = badgeRules.iterator();
    boolean insertNextRule = false;
    Long sentCount = 0L;
    Long receivedCount = 0L;
    Long totalCount = 0L;
    boolean isEarned = false;
    boolean alreadyEarned = false;
    boolean isBadgeLibMultiple = true;
    boolean insertRecord = false;
    boolean isEarnedBadgePoints = false;

    ParticipantBadge returnPax = new ParticipantBadge();

    while ( ruleItr.hasNext() )
    {
      BadgeRule rule = (BadgeRule)ruleItr.next();
      this.gamificationDAO.getSessionLockForParticipantBadge( rule, partcipant.getId() );

      ParticipantBadge participantProgress = this.gamificationDAO.getParticipantBadgeByBadgeRule( rule, partcipant.getId() );

      if ( participantProgress != null && paxType.equalsIgnoreCase( "sender" ) )
      {
        sentCount = participantProgress.getSentCount() + 1;
        receivedCount = participantProgress.getReceivedCount();
        totalCount = sentCount + receivedCount;
        alreadyEarned = participantProgress.getIsEarned();

      }
      else if ( participantProgress != null && paxType.equalsIgnoreCase( "receiver" ) )
      {
        sentCount = participantProgress.getSentCount();
        receivedCount = participantProgress.getReceivedCount() + 1;
        totalCount = sentCount + receivedCount;
        alreadyEarned = participantProgress.getIsEarned();

      }
      else if ( participantProgress == null && paxType.equalsIgnoreCase( "sender" ) )
      {
        sentCount = sentCount + 1;
        totalCount = sentCount + receivedCount;
      }
      else if ( participantProgress == null && paxType.equalsIgnoreCase( "receiver" ) )
      {
        receivedCount = receivedCount + 1;
        totalCount = sentCount + receivedCount;
      }
      if ( !alreadyEarned )
      {
        Long badgeLevel = rule.getMaximumQualifier();
        if ( badgeCountType != null && badgeCountType.equalsIgnoreCase( "given" ) )
        {
          if ( sentCount >= badgeLevel )
          {
            isEarned = true;
          }
          else
          {
            isEarned = false;
          }
        }
        else if ( badgeCountType != null && badgeCountType.equalsIgnoreCase( "received" ) )
        {
          if ( receivedCount >= badgeLevel )
          {
            isEarned = true;
          }
          else
          {
            isEarned = false;
          }

        }
        else if ( badgeCountType != null && badgeCountType.equalsIgnoreCase( "total" ) )
        {
          if ( totalCount >= badgeLevel )
          {
            isEarned = true;
          }
          else
          {
            isEarned = false;
          }

        }

        int recordExist = canCreateJournal( partcipant.getId(), rule.getId(), badge.getId(), badge.getBadgeType().getCode() );

        if ( isEarned && recordExist == 0 && rule.getBadgePoints() != null && rule.getBadgePoints() > 0 )
        {
          createJournalEntry( (Promotion)badge, (Participant)partcipant, rule.getBadgePoints() );
          isEarnedBadgePoints = true;
        }

        // if sender existing record and earned is true, update existing record and insert new
        // record with next badge rule id
        if ( participantProgress != null )
        {
          participantProgress.setIsEarned( isEarned );
          if ( isEarned )
          {
            participantProgress.setEarnedDate( new Date() );
            insertNextRule = true;
          }
          participantProgress.setSentCount( sentCount );
          participantProgress.setReceivedCount( receivedCount );
          participantProgress.setBadgeRule( rule );
          participantProgress.setIsBadgePointsEarned( isEarnedBadgePoints );
          p = this.gamificationDAO.saveParticipantBadge( participantProgress );
          if ( isEarned && badge.getNotificationMessageId() != -1 )
          {
            sendSummaryMessage( (Participant)p.getParticipant(), promo.getName(), rule, recordExist );
          }
          break;
        }

        // if sender not exist and earned is true, insert 2 new records one with current badge rule
        // id earned and next badge rule id not earned
        else if ( participantProgress == null )
        {
          ParticipantBadge partcipantBadge = new ParticipantBadge();
          partcipantBadge.setBadgePromotion( badge );
          partcipantBadge.setBadgeRule( rule );
          partcipantBadge.setIsEarned( isEarned );
          if ( isEarned )
          {
            partcipantBadge.setEarnedDate( new Date() );
            insertNextRule = true;
          }
          partcipantBadge.setParticipant( partcipant );
          partcipantBadge.setSentCount( sentCount );
          partcipantBadge.setReceivedCount( receivedCount );
          partcipantBadge.setIsBadgePointsEarned( isEarnedBadgePoints );
          partcipantBadge.setStatus( Badge.BADGE_ACTIVE );
          p = this.gamificationDAO.saveParticipantBadge( partcipantBadge );
          if ( isEarned && badge.getNotificationMessageId() != -1 )
          {
            sendSummaryMessage( (Participant)p.getParticipant(), promo.getName(), rule, recordExist );
          }
          break;
        }
      }
    } // end of while

    if ( insertNextRule )
    {
      // get the next rule and insert another PartcipantBadge row
      if ( ruleItr.hasNext() )
      {
        BadgeRule nextRule = (BadgeRule)ruleItr.next();
        ParticipantBadge partcipantBadge = new ParticipantBadge();
        partcipantBadge.setBadgePromotion( badge );
        partcipantBadge.setBadgeRule( nextRule );
        partcipantBadge.setIsEarned( false );
        partcipantBadge.setParticipant( partcipant );
        partcipantBadge.setSentCount( sentCount );
        partcipantBadge.setReceivedCount( receivedCount );
        partcipantBadge.setStatus( Badge.BADGE_ACTIVE );
        p = this.gamificationDAO.saveParticipantBadge( partcipantBadge );

      }
    }

    return p;
  }

  /**
   * Method to populate the participant badge for earned/not earned badge type for recognition and nomination promotions
   * @param receipients
   * @param claim
   * @param badge 
   */
  public ParticipantBadge populateEarnedBadgesNominationTeam( Set receipients, Claim claim, Badge badge )
  {
    ParticipantBadge returnPax = new ParticipantBadge();
    Set<BadgeRule> badgeRules = badge.getBadgeRules();
    Iterator<BadgeRule> ruleItr = badgeRules.iterator();
    boolean insertRecord = false;
    boolean validBadgeInsert = false;
    boolean isBadgeLibMultiple = false;
    BadgeRule rule = null;
    Long points = 0L;
    Iterator receipientItr = receipients.iterator();
    while ( receipientItr.hasNext() )
    {
      ClaimRecipient receiver = (ClaimRecipient)receipientItr.next();

      while ( ruleItr.hasNext() )
      {
        rule = (BadgeRule)ruleItr.next();
        String badgeLibCm = rule.getBadgeLibraryCMKey();
        isBadgeLibMultiple = isBadgeLibMultipleAllowed( badgeLibCm );

        String awardType = null;

        if ( claim.getPromotion().isNominationPromotion() )
        {
          NominationPromotion nominationPromotion = (NominationPromotion)claim.getPromotion();

          for ( NominationPromotionLevel nominationPromotionLevel : nominationPromotion.getNominationLevels() )
          {
            if ( nominationPromotionLevel.getLevelIndex().intValue() == claim.getApprovableItemApproversSize() )
            {
              awardType = nominationPromotionLevel.getAwardPayoutType().getCode();
            }
          }
        }
        else
        {
          awardType = claim.getPromotion().getAwardType().getCode();
        }

        if ( awardType != null && awardType.equalsIgnoreCase( PromotionAwardsType.POINTS ) )
        {
          if ( claim.getPromotion().isNominationPromotion() )
          {
            NominationPromotion nomPromotion = (NominationPromotion)claim.getPromotion();
            if ( nomPromotion.isAwardActive() )
            {
              points = receiver.getAwardQuantity();
            }
            if ( points != null && points >= rule.getMinimumQualifier() && points <= rule.getMaximumQualifier() )
            {
              validBadgeInsert = true;
            }
          }
          else if ( claim.getPromotion().isProductClaimPromotion() )
          {
            points = claimDAO.getEarningsForClaim( claim.getId(), receiver.getId() );
            if ( points != null && points >= rule.getMinimumQualifier() && points <= rule.getMaximumQualifier() )
            {
              validBadgeInsert = true;
            }
          }
        }

        if ( validBadgeInsert )
        {
          if ( !isBadgeLibMultiple )
          {
            if ( receiver.getRecipient() != null )
            {
              returnPax = updatePartcipantBadge( badge, rule, receiver.getRecipient().getId(), badgeLibCm, claim.getPromotion().getName() );
            }
            if ( returnPax == null )
            {
              insertRecord = true;
              break;
            }
            else
            {
              insertRecord = false;
            }

          }
          else
          {
            insertRecord = true;
            break;
          }
        }

      }

      // code to handle if the maximum point in the badge rule is less than the award amount for the
      // claim, thsi case we have to insert the maximum badge record

      if ( rule != null && !insertRecord && points != null && points > rule.getMaximumQualifier() )
      {
        if ( !isBadgeLibMultiple )
        {
          if ( receiver.getRecipient() != null )
          {
            returnPax = updatePartcipantBadge( badge, rule, receiver.getRecipient().getId(), rule.getBadgeLibraryCMKey(), claim.getPromotion().getName() );
          }
          if ( returnPax == null )
          {
            insertRecord = true;
          }
          else
          {
            insertRecord = false;
          }
        }
        else
        {
          insertRecord = true;
        }
      }
      if ( insertRecord && receiver.getRecipient() != null )
      {
        ParticipantBadge paxBadge = new ParticipantBadge();
        paxBadge.setBadgePromotion( badge );
        paxBadge.setBadgeRule( rule );
        paxBadge.setEarnedDate( new Date() );
        paxBadge.setIsEarned( true );
        paxBadge.setParticipant( receiver.getRecipient() );
        paxBadge.setStatus( Badge.BADGE_ACTIVE );
        returnPax = this.gamificationDAO.saveParticipantBadge( paxBadge );
        if ( badge.getNotificationMessageId() != -1 )
        {
          sendSummaryMessage( (Participant)returnPax.getParticipant(), claim.getPromotion().getName(), rule, 0 );
        }
      }
    }
    return returnPax;

  }

  /**
   * Method to populate the participant badge for earned/not earned badge type for recognition and nomination promotions
   * @param receipients
   * @param claim
   * @param badge
   */
  public ParticipantBadge populateEarnedBadgesRecognitionNomination( Set receipients, Claim claim, Badge badge )
  {
    ParticipantBadge returnPax = new ParticipantBadge();
    Set<BadgeRule> badgeRules = badge.getBadgeRules();
    Iterator<BadgeRule> ruleItr = badgeRules.iterator();
    boolean insertRecord = false;
    boolean validBadgeInsert = false;
    boolean isBadgeLibMultiple = false;
    BadgeRule rule = null;
    Long points = 0L;
    Iterator receipientItr = receipients.iterator();
    while ( receipientItr.hasNext() )
    {
      ClaimRecipient receiver = (ClaimRecipient)receipientItr.next();

      while ( ruleItr.hasNext() )
      {
        rule = (BadgeRule)ruleItr.next();
        String badgeLibCm = rule.getBadgeLibraryCMKey();
        isBadgeLibMultiple = isBadgeLibMultipleAllowed( badgeLibCm );
        String awardType = null;

        if ( claim.getPromotion().isNominationPromotion() )
        {
          NominationPromotion nominationPromotion = (NominationPromotion)claim.getPromotion();
          for ( NominationPromotionLevel nominationPromotionLevel : nominationPromotion.getNominationLevels() )
          {
            if ( nominationPromotionLevel.getLevelIndex().intValue() == claim.getApprovableItemApproversSize() )
            {
              awardType = nominationPromotionLevel.getAwardPayoutType().getCode();
            }
          }
        }
        else
        {
          awardType = claim.getPromotion().getAwardType().getCode();
        }

        if ( awardType != null && awardType.equalsIgnoreCase( PromotionAwardsType.POINTS ) )
        {
          // if(claim.getPromotio)

          if ( claim.getPromotion().isRecognitionPromotion() )
          {
            RecognitionPromotion recPromotion = (RecognitionPromotion)claim.getPromotion();
            if ( recPromotion.isAwardActive() )
            {
              points = receiver.getAwardQuantity();
            }
          }
          else if ( claim.getPromotion().isNominationPromotion() )
          {
            NominationPromotion nomPromotion = (NominationPromotion)claim.getPromotion();
            if ( nomPromotion.isAwardActive() )
            {
              points = receiver.getAwardQuantity();
            }
          }
          if ( points != null && points >= rule.getMinimumQualifier() && points <= rule.getMaximumQualifier() )
          {
            validBadgeInsert = true;
          }

        }
        else
        {
          if ( receiver.getPromoMerchProgramLevel() != null )
          {
            if ( rule.getLevelName().equalsIgnoreCase( receiver.getPromoMerchProgramLevel().getDisplayLevelName() )
                && rule.getCountryId().equals( receiver.getPromoMerchCountry().getCountry().getId() ) )
            {
              validBadgeInsert = true;
            }
          }
        }

        if ( validBadgeInsert )
        {
          if ( !isBadgeLibMultiple )
          {
            if ( receiver.getRecipient() != null )
            {
              returnPax = updatePartcipantBadge( badge, rule, receiver.getRecipient().getId(), badgeLibCm, claim.getPromotion().getName() );
            }
            if ( returnPax == null )
            {
              insertRecord = true;
              break;
            }
            else
            {
              insertRecord = false;
            }

          }
          else
          {
            insertRecord = true;
            break;
          }
        }

      }

      // code to handle if the maximum point in the badge rule is less than the award amount for the
      // claim, thsi case we have to insert the maximum badge record

      if ( rule != null && !insertRecord && points != null && points > rule.getMaximumQualifier() )
      {
        if ( !isBadgeLibMultiple )
        {
          if ( receiver.getRecipient() != null )
          {
            returnPax = updatePartcipantBadge( badge, rule, receiver.getRecipient().getId(), rule.getBadgeLibraryCMKey(), claim.getPromotion().getName() );
          }
          if ( returnPax == null )
          {
            insertRecord = true;
          }
          else
          {
            insertRecord = false;
          }
        }
        else
        {
          insertRecord = true;
        }
      }
      if ( insertRecord && receiver.getRecipient() != null )
      {
        ParticipantBadge paxBadge = new ParticipantBadge();
        paxBadge.setBadgePromotion( badge );
        paxBadge.setBadgeRule( rule );
        paxBadge.setEarnedDate( new Date() );
        paxBadge.setIsEarned( true );
        paxBadge.setParticipant( receiver.getRecipient() );
        paxBadge.setClaimId( claim.getId() );
        paxBadge.setStatus( Badge.BADGE_ACTIVE );
        returnPax = this.gamificationDAO.saveParticipantBadge( paxBadge );
        if ( badge.getNotificationMessageId() != -1 )
        {
          sendSummaryMessage( (Participant)returnPax.getParticipant(), claim.getPromotion().getName(), rule, 0 );
        }
      }
    }
    return returnPax;

  }

  /**
     * Method to populate the participant badge for earned/not earned badge type for promotions other than recognition and nomination promotions( for now it will be only quiz promotions)
     * @param receipient
     * @param claim
     * @param badge
     */
  public ParticipantBadge populateEarnedBadgesNonRecognition( Participant receipient, Claim claim, Badge badge )
  {
    ParticipantBadge returnPax = new ParticipantBadge();
    Set<BadgeRule> badgeRules = badge.getBadgeRules();
    Iterator<BadgeRule> ruleItr = badgeRules.iterator();
    boolean insertRecord = false;
    boolean validBadgeInsert = false;
    boolean isBadgeLibMultiple = false;
    BadgeRule rule = null;
    Long points = 0L;

    while ( ruleItr.hasNext() )
    {
      rule = (BadgeRule)ruleItr.next();
      String badgeLibCm = rule.getBadgeLibraryCMKey();
      isBadgeLibMultiple = isBadgeLibMultipleAllowed( badgeLibCm );

      if ( claim.getPromotion().getAwardType().getCode().equalsIgnoreCase( PromotionAwardsType.POINTS ) )
      {
        if ( claim.getPromotion().isDIYQuizPromotion() )
        {
          QuizClaim quizClaim = (QuizClaim)claim;
          DIYQuiz diyQuiz = (DIYQuiz)quizClaim.getQuiz();
          BadgeRule diyBadgeRule = diyQuiz.getBadgeRule();
          if ( rule != null && rule.getId() != null && diyBadgeRule != null && rule.getId().equals( diyBadgeRule.getId() ) )
          {
            validBadgeInsert = true;
          }
        }
        else
        {
          if ( claim.getPromotion().isQuizPromotion() )
          {
            QuizPromotion quizPromotion = (QuizPromotion)claim.getPromotion();
            if ( quizPromotion.isAwardActive() )
            {
              points = quizPromotion.getAwardAmount();
            }
          }
          else if ( claim.getPromotion().isProductClaimPromotion() )
          {
            points = claimDAO.getEarningsForClaim( claim.getId(), receipient.getId() );
            if ( points == null )
            {
              points = 0L;
            }
          }

          if ( points != null && points >= rule.getMinimumQualifier() && points <= rule.getMaximumQualifier() )
          {
            validBadgeInsert = true;
          }
        }
      }

      if ( validBadgeInsert )
      {
        if ( !isBadgeLibMultiple )
        {
          returnPax = updatePartcipantBadge( badge, rule, receipient.getId(), badgeLibCm, claim.getPromotion().getName() );
          if ( returnPax == null )
          {
            insertRecord = true;
            break;
          }
          else
          {
            insertRecord = false;
          }
        }
        else
        {
          insertRecord = true;
          break;
        }
      }
    }

    // code to handle if the maximum point in the badge rule is less than the award amount for the
    // claim, this case we have to insert the maximum badge record

    if ( rule != null && !insertRecord && points != null && points > rule.getMaximumQualifier() )
    {
      if ( !isBadgeLibMultiple )
      {
        returnPax = updatePartcipantBadge( badge, rule, receipient.getId(), rule.getBadgeLibraryCMKey(), claim.getPromotion().getName() );
        if ( returnPax == null )
        {
          insertRecord = true;
        }
        else
        {
          insertRecord = false;
        }
      }
      else
      {
        insertRecord = true;
      }
    }

    if ( insertRecord )
    {
      ParticipantBadge paxBadge = new ParticipantBadge();
      paxBadge.setBadgePromotion( badge );
      paxBadge.setBadgeRule( rule );
      paxBadge.setEarnedDate( new Date() );
      paxBadge.setIsEarned( true );
      paxBadge.setParticipant( receipient );
      paxBadge.setStatus( Badge.BADGE_ACTIVE );
      returnPax = this.gamificationDAO.saveParticipantBadge( paxBadge );
      if ( badge.getNotificationMessageId() != null && badge.getNotificationMessageId() != -1 )
      {
        sendSummaryMessage( (Participant)returnPax.getParticipant(), claim.getPromotion().getName(), rule, 0 );
      }
    }
    return returnPax;
  }

  public ParticipantBadge populateEarnedBadgesCPandGQ( String levelName, User reciever, GoalQuestPromotion promotion, Badge badge, boolean isParticipant )
  {
    ParticipantBadge returnPax = new ParticipantBadge();
    Set<BadgeRule> badgeRules = badge.getBadgeRules();
    Iterator<BadgeRule> ruleItr = badgeRules.iterator();
    boolean insertRecord = false;
    boolean validBadgeInsert = false;
    boolean isBadgeLibMultiple = false;
    BadgeRule rule = null;
    Long points = 0L;

    if ( promotion.isGoalQuestPromotion() && getPromotionAwardType( promotion ).equals( PromotionAwardsType.MERCHANDISE ) )
    {
      AssociationRequestCollection arCollection = new AssociationRequestCollection();
      arCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.GOAL_LEVELS ) );

      promotion = (GoalQuestPromotion)promotionService.getPromotionByIdWithAssociations( promotion.getId(), arCollection );
    }

    Iterator<AbstractGoalLevel> iter = promotion.getGoalLevels().iterator();
    while ( iter.hasNext() )
    {
      GoalLevel goalLevel = (GoalLevel)iter.next();
      if ( levelName != null && levelName.equalsIgnoreCase( goalLevel.getGoalLevelName() ) )
      {
        validBadgeInsert = true;

        while ( ruleItr.hasNext() )
        {
          rule = (BadgeRule)ruleItr.next();
          String badgeLibCm = rule.getBadgeLibraryCMKey();
          isBadgeLibMultiple = isBadgeLibMultipleAllowed( badgeLibCm );
          if ( goalLevel.getGoalLevelName().equalsIgnoreCase( rule.getLevelName() ) )
          {
            if ( isParticipant )
            {
              if ( !rule.getParticipantType().equals( ParticipantType.NONE ) )
              {
                continue;
              }
            }
            else
            {
              if ( !rule.getParticipantType().equals( ParticipantType.PARTNER ) )
              {
                continue;
              }
            }

            if ( !isBadgeLibMultiple )
            {
              returnPax = updatePartcipantBadge( badge, rule, reciever.getId(), badgeLibCm, promotion.getName() );
              if ( returnPax == null )
              {
                insertRecord = true;
                break;
              }
              else
              {
                insertRecord = false;
              }
            }
            else
            {
              insertRecord = true;
              break;
            }
          }
        }
        break;
      }
    }

    // code to handle if the maximum point in the badge rule is less than the award amount for the
    // claim, this case we have to insert the maximum badge record

    if ( rule != null && !insertRecord && points != null && points > rule.getMaximumQualifier() )
    {
      if ( !isBadgeLibMultiple )
      {
        returnPax = updatePartcipantBadge( badge, rule, reciever.getId(), rule.getBadgeLibraryCMKey(), promotion.getName() );
        if ( returnPax == null )
        {
          insertRecord = true;
        }
        else
        {
          insertRecord = false;
        }
      }
      else
      {
        insertRecord = true;
      }
    }

    if ( insertRecord )
    {
      ParticipantBadge paxBadge = new ParticipantBadge();
      paxBadge.setBadgePromotion( badge );
      paxBadge.setBadgeRule( rule );
      paxBadge.setEarnedDate( new Date() );
      paxBadge.setIsEarned( true );
      paxBadge.setParticipant( reciever );
      paxBadge.setStatus( Badge.BADGE_ACTIVE );
      returnPax = this.gamificationDAO.saveParticipantBadge( paxBadge );
      if ( badge.getNotificationMessageId() != -1 )
      {
        sendSummaryMessage( (Participant)returnPax.getParticipant(), promotion.getName(), rule, 0 );
      }
    }
    return returnPax;
  }

  public ParticipantBadge populateEarnedBadgesTD( Integer ranking, Participant reciever, ThrowdownPromotion promotion, Badge badge, BadgeLevelType type, String levelName )
  {
    ParticipantBadge returnPax = new ParticipantBadge();
    Set<BadgeRule> badgeRules = badge.getBadgeRules();
    Iterator<BadgeRule> ruleItr = badgeRules.iterator();
    boolean insertRecord = false;
    boolean validBadgeInsert = false;
    boolean isBadgeLibMultiple = false;
    BadgeRule rule = null;

    while ( ruleItr.hasNext() )
    {
      rule = (BadgeRule)ruleItr.next();
      String badgeLibCm = rule.getBadgeLibraryCMKey();
      isBadgeLibMultiple = isBadgeLibMultipleAllowed( badgeLibCm );

      if ( getPromotionAwardType( promotion ).equals( PromotionAwardsType.POINTS ) && rule.getLevelType().equals( type ) )
      {
        if ( !rule.getLevelType().getCode().equals( BadgeLevelType.UNDEFEATED ) && levelName.equalsIgnoreCase( rule.getLevelName() ) && ranking >= rule.getMinimumQualifier()
            && ranking <= rule.getMaximumQualifier() )
        {
          validBadgeInsert = true;
        }
        if ( rule.getLevelType().getCode().equals( BadgeLevelType.UNDEFEATED ) )
        {
          validBadgeInsert = true;
        }
      }

      if ( validBadgeInsert )
      {
        if ( !isBadgeLibMultiple )
        {
          returnPax = updatePartcipantBadge( badge, rule, reciever.getId(), badgeLibCm, promotion.getName() );
          if ( returnPax == null )
          {
            insertRecord = true;
            break;
          }
          else
          {
            insertRecord = false;
          }
        }
        else
        {
          insertRecord = true;
          break;
        }
      }
    }

    if ( insertRecord )
    {
      ParticipantBadge paxBadge = new ParticipantBadge();
      paxBadge.setBadgePromotion( badge );
      paxBadge.setBadgeRule( rule );
      paxBadge.setEarnedDate( new Date() );
      paxBadge.setIsEarned( true );
      paxBadge.setParticipant( reciever );
      paxBadge.setStatus( Badge.BADGE_ACTIVE );
      returnPax = this.gamificationDAO.saveParticipantBadge( paxBadge );
      if ( badge.getNotificationMessageId() != -1 )
      {
        sendSummaryMessage( reciever, promotion.getName(), rule, 0 );
      }
    }
    return returnPax;
  }

  public Long[] getPromotionArray( List eligiblePromotions, String badgeType )
  {
    Long[] promotionIds = null;
    List promotionList = new ArrayList();
    if ( eligiblePromotions != null )
    {
      Iterator eligiblePromoItr = eligiblePromotions.iterator();
      while ( eligiblePromoItr.hasNext() )
      {
        PromotionMenuBean promotion = (PromotionMenuBean)eligiblePromoItr.next();
        if ( promotion.getPromotion().isRecognitionPromotion() && promotion.isCanReceive() )
        {
          RecognitionPromotion recPromo = (RecognitionPromotion)promotion.getPromotion();
          if ( badgeType.equalsIgnoreCase( "behavior" ) )
          {
            if ( recPromo.isBehaviorActive() )
            {
              promotionList.add( promotion.getPromotion().getId() );
            }
          }
          else
          {
            promotionList.add( promotion.getPromotion().getId() );
          }
        }
        else if ( promotion.getPromotion().isNominationPromotion() && promotion.isCanReceive() )
        {
          NominationPromotion nomPromo = (NominationPromotion)promotion.getPromotion();
          if ( badgeType.equalsIgnoreCase( "behavior" ) )
          {
            if ( nomPromo.isBehaviorActive() )
            {
              promotionList.add( promotion.getPromotion().getId() );
            }
          }
          else
          {
            promotionList.add( promotion.getPromotion().getId() );
          }
        }
      }
    }
    promotionIds = (Long[])promotionList.toArray( new Long[promotionList.size()] );
    return promotionIds;

  }

  public Integer isUserHasActiveBadges( Long userId, List<PromotionMenuBean> eligiblePromotions )
  {
    String promotionIds = "";
    List<String> promotionIdList = new ArrayList<String>();
    int count = 0;
    // converting type to long since ids are coming as long by default though we are changing the
    // return type of dao method to List<String>
    List<Long> badgeEligiblePromoList = this.gamificationDAO.getBadgeEligiblePromotionIds();
    if ( eligiblePromotions != null && eligiblePromotions.size() > 0 && badgeEligiblePromoList.size() > 0 )
    {

      Iterator promoItr = eligiblePromotions.iterator();
      while ( promoItr.hasNext() )
      {
        PromotionMenuBean promoMenu = (PromotionMenuBean)promoItr.next();
        // using primitive type to compare the values with == symbol
        long id = promoMenu.getPromotion().getId();
        if ( badgeEligiblePromoList.stream().anyMatch( str -> str == id ) )

        {

          promotionIdList.add( new Long( id ).toString() );
        }
      }
      if ( promotionIdList.size() > 0 )
      {
        /* Written the package namw to avoid the conflicts */
        promotionIds = org.apache.commons.lang.StringUtils.join( promotionIdList, "," );
        return this.gamificationDAO.isUserHasActiveBadges( userId, promotionIds );
      }
      return 0;
    }
    return this.gamificationDAO.isUserHasActiveBadges( userId, promotionIds );
  }

  public List<ParticipantBadge> buildParticipantBadgeFromBadgeRule( List<BadgeRule> badgeRuleList, Long participantId )
  {
    List<ParticipantBadge> partcipantBadgeList = new ArrayList<ParticipantBadge>();
    Participant participant = getParticipantService().getParticipantById( participantId );
    if ( badgeRuleList != null )
    {
      Iterator badgeRuleItr = badgeRuleList.iterator();
      while ( badgeRuleItr.hasNext() )
      {
        BadgeRule rule = (BadgeRule)badgeRuleItr.next();
        ParticipantBadge participantBadge = new ParticipantBadge();
        participantBadge.setParticipant( participant );
        participantBadge.setBadgePromotion( rule.getBadgePromotion() );
        participantBadge.setBadgeRule( rule );
        participantBadge.setIsEarned( false );
        participantBadge.setStatus( "A" );
        participantBadge.setSentCount( 0L );
        participantBadge.setReceivedCount( 0L );
        partcipantBadgeList.add( participantBadge );
      }

    }
    return partcipantBadgeList;

  }

  /**
   * Method to get the pax badges for a promotion to show in send recognition confirmation tooltip
   * @param promotionId
   * @param userId
   * @return  List<ParticipantBadge>
   */
  public List<ParticipantBadge> getBadgesForRecognitionConfirmationScreen( Long promotionId, Long userId )
  {
    List<ParticipantBadge> paxBadgesRec = new ArrayList<ParticipantBadge>();
    paxBadgesRec = this.gamificationDAO.getBadgesForRecognitionConfirmationScreen( promotionId, userId );

    return paxBadgesRec;
  }

  public List<ParticipantBadge> getBadgesForRecognitionEmail( Long promotionId, Long userId, String behaviorName )
  {
    List<ParticipantBadge> paxBadgesProgress = new ArrayList<ParticipantBadge>();

    paxBadgesProgress = this.gamificationDAO.getBadgesForRecognitionEmailProgress( promotionId, userId );

    if ( !StringUtils.isEmpty( behaviorName ) )
    {
      List<ParticipantBadge> paxBadgesBehavior = new ArrayList<ParticipantBadge>();
      paxBadgesBehavior = this.gamificationDAO.getBadgesForRecognitionEmailBehavior( promotionId, userId, behaviorName );
      if ( paxBadgesBehavior != null && paxBadgesBehavior.size() > 0 )
      {
        paxBadgesProgress.addAll( paxBadgesBehavior );
      }
    }

    return paxBadgesProgress;
  }

  public List<ParticipantBadge> getBehaviorBadgesForRecognitionEmail( Long promotionId, Long userId, String behaviorName )
  {
    List<ParticipantBadge> paxBehaviorBadges = new ArrayList<ParticipantBadge>();

    if ( !StringUtils.isEmpty( behaviorName ) )
    {
      List<ParticipantBadge> paxBadgesBehavior = new ArrayList<ParticipantBadge>();
      paxBadgesBehavior = this.gamificationDAO.getBadgesForRecognitionEmailBehavior( promotionId, userId, behaviorName );
      if ( paxBadgesBehavior != null && paxBadgesBehavior.size() > 0 )
      {
        paxBehaviorBadges.addAll( paxBadgesBehavior );
      }
    }

    return paxBehaviorBadges;
  }

  public Long getLowestBadgeRule( Badge badge )
  {
    Long badgeRuleId = 0L;

    Set badgeRules = badge.getBadgeRules();

    if ( badgeRules != null && badgeRules.size() > 0 )
    {
      Iterator rulesItr = badgeRules.iterator();

      if ( rulesItr.hasNext() )
      {
        BadgeRule rule = (BadgeRule)rulesItr.next();
        badgeRuleId = rule.getId();
      }
    }
    return badgeRuleId;
  }

  public List<ParticipantBadge> getParticipansEarnedHighestLevel( Long badgeId )
  {
    List<ParticipantBadge> paxList = new ArrayList<ParticipantBadge>();
    paxList = this.gamificationDAO.getParticipansEarnedHighestLevel( badgeId );
    return paxList;
  }

  public List<ParticipantBadge> getBadgeByParticipantEarnedForPromotion( Long promotionId, Long userId )
  {
    List<ParticipantBadge> paxList = new ArrayList<ParticipantBadge>();
    paxList = this.gamificationDAO.getBadgeByParticipantEarnedForPromotion( promotionId, userId );
    return paxList;
  }

  public List<ParticipantBadge> getBadgeByParticipantEarnedForDIYQuiz( Long quizId, Long userId )
  {
    List<ParticipantBadge> paxList = new ArrayList<ParticipantBadge>();
    paxList = this.gamificationDAO.getBadgeByParticipantEarnedForDIYQuiz( quizId, userId );
    return paxList;
  }

  /**
   * Composes and sends a summary e-mail.
   */
  public void sendSummaryMessage( Participant participant, String promotionName, BadgeRule rule, int recordExist )
  {
    User recipientUser = (User)participant;
    Badge badge = gamificationDAO.getBadgeById( rule.getBadgePromotion().getId() );
    Long messageId = badge.getNotificationMessageId();

    if ( messageId > 0 )
    {
      List badgeMessageList = gamificationDAO.getBadgeNotificationList();
      for ( Iterator<Long> it = badgeMessageList.iterator(); it.hasNext(); )
      {
        Long notificationId = it.next();
        if ( messageId.equals( notificationId ) )
        {
          Mailing mailing = new Mailing();
          // Send Manager notification
          Message message = messageService.getMessageById( messageId );
          String badgeImageUrl = "";
          boolean showPromotion = false;

          String languageCode = getSystemVariableService().getPropertyByName( SystemVariableService.DEFAULT_LANGUAGE ).getStringVal();
          if ( participant.getLanguageType() != null )
          {
            languageCode = participant.getLanguageType().getCode();
          }

          List earnedNotEarnedImageList = getEarnedNotEarnedImageList( rule.getBadgeLibraryCMKey(), LocaleUtils.getLocale( languageCode ) );
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
          objectMap.put( "badgeName", rule.getBadgeNameTextFromCM( LocaleUtils.getLocale( languageCode ) ) );
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
          MailingRecipient mr = addRecipient( recipientUser );
          mailing.addMailingRecipient( mr );
          try
          {
            // Send the e-mail message with personalization
            mailingService.submitMailing( mailing, objectMap );
          }
          catch( Exception e )
          {
            logger.error( "An exception occurred while sending badge received email" );
          }
        }

      }

    }
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

  /**
   * Adds the message by name and mailing type to a mailing object
   * 
   * @param cmAssetCode
   * @param mailingType
   * @return a mailing object that is mostly assembled, except for the mailingRecipient(s)
   */
  protected Mailing composeMail( String cmAssetCode, String mailingType )
  {
    Mailing mailing = composeMail();

    mailing.setMailingType( MailingType.lookup( mailingType ) );

    Message message = messageService.getMessageByCMAssetCode( cmAssetCode );
    mailing.setMessage( message );

    return mailing;
  }

  /**
   * Creates a new mailing object and add Guid, Sender and Delivery Date to it
   * 
   * @return a partially assembled mailing object
   */
  private Mailing composeMail()
  {
    Mailing mailing = new Mailing();

    // Needs Guid due to lack of a business key
    mailing.setGuid( GuidUtils.generateGuid() );

    // Sender
    String sender = systemVariableService.getPropertyByName( SystemVariableService.SYSTEM_EMAIL_ADDRESS ).getStringVal();
    mailing.setSender( sender );

    // Delivery Date - Assumes Now (i.e. immediate delivery)
    Timestamp deliveryDate = new Timestamp( com.biperf.core.utils.DateUtils.getCurrentDate().getTime() );
    mailing.setDeliveryDate( deliveryDate );

    return mailing;

  }

  /**
   * Takes in a user and returns a mailing recipient object suitable for mailing service
   * 
   * @param recipient
   * @return a mailingRecipient object
   */
  protected MailingRecipient addRecipient( User recipient )
  {
    MailingRecipient mailingRecipient = new MailingRecipient();
    mailingRecipient.setGuid( GuidUtils.generateGuid() );
    String localeCode = getSystemVariableService().getPropertyByName( SystemVariableService.DEFAULT_LANGUAGE ).getStringVal();
    if ( recipient.getLanguageType() != null )
    {
      localeCode = recipient.getLanguageType().getCode();
    }
    mailingRecipient.setLocale( localeCode );
    mailingRecipient.setUser( recipient );

    return mailingRecipient;
  }

  /*
   * public Date getNoDisplayEndDate() { Calendar cal = Calendar.getInstance(); Date d=new
   * Date("999") cal.setTime( date ) // Set time fields to zero cal.set( Calendar.HOUR_OF_DAY, 0 );
   * cal.set( Calendar.MINUTE, 0 ); cal.set( Calendar.SECOND, 0 ); cal.set( Calendar.MILLISECOND, 0
   * ); return cal.getTime(); }
   */

  public String saveRulesCmText( String badgeRule, String badgeNameText ) throws ServiceErrorException
  {
    return saveRulesCmText( badgeRule, badgeNameText, UserManager.getLocale() );
  }

  @Override
  public String saveRulesCmText( String badgeRule, String badgeNameText, Locale locale ) throws ServiceErrorException
  {
    String badgeNameCmAsset = null;
    try
    {
      if ( badgeRule == null )
      {
        badgeNameCmAsset = cmAssetService.getUniqueAssetCode( BadgeRule.BADGE_RULES_CMASSET_PREFIX );
      }
      else
      {
        badgeNameCmAsset = badgeRule;
      }

      CMDataElement cmDataElement = new CMDataElement( BadgeRule.BADGE_RULES_CMASSET_NAME, BadgeRule.BADGE_RULES_CMASSET_TYPE_KEY, badgeNameText, false, DataTypeEnum.HTML );
      List elements = new ArrayList();
      elements.add( cmDataElement );

      cmAssetService.createOrUpdateAsset( BadgeRule.BADGE_RULES_SECTION_CODE, BadgeRule.BADGE_RULES_CMASSET_TYPE_NAME, BadgeRule.BADGE_RULES_CMASSET_NAME, badgeNameCmAsset, elements, locale, null );
    }
    catch( ServiceErrorException e )
    {
      List errors = new ArrayList();
      errors.add( new ServiceError( ServiceErrorMessageKeys.CM_SERVICE_SAVE_ERROR ) );
      throw e;
    }

    return badgeNameCmAsset;
  }

  public List<ParticipantBadge> getBadgeParticipantHistory( Long userId )
  {
    return this.gamificationDAO.getBadgeParticipantHistory( userId );
  }

  /**
   * Gets the Badge Rule by the Behavior.
   * 
   * @param behaviorName
   * @param promotionId
   * @return BadgeRule
   */
  public BadgeRule getBadgeRuleByBehaviorName( String behaviorName, Long promotionId )
  {
    BadgeRule badgeRule = this.gamificationDAO.getBadgeRuleByBehaviorName( behaviorName, promotionId );
    return badgeRule;
  }

  public ParticipantService getParticipantService()
  {
    return participantService;
  }

  public void setParticipantService( ParticipantService participantService )
  {
    this.participantService = participantService;
  }

  public void setPromotionService( PromotionService promotionService )
  {
    this.promotionService = promotionService;
  }

  private PromotionService getPromotionService()
  {
    return promotionService;
  }

  public void setSystemVariableService( SystemVariableService systemVariableService )
  {
    this.systemVariableService = systemVariableService;
  }

  public SystemVariableService getSystemVariableService()
  {
    return systemVariableService;
  }

  public DIYQuizDAO getDiyQuizDAO()
  {
    return diyQuizDAO;
  }

  public void setDiyQuizDAO( DIYQuizDAO diyQuizDAO )
  {
    this.diyQuizDAO = diyQuizDAO;
  }

  public MailingService getMailingService()
  {
    return mailingService;
  }

  public void setMailingService( MailingService mailingService )
  {
    this.mailingService = mailingService;
  }

  public MessageService getMessageService()
  {
    return messageService;
  }

  public void setMessageService( MessageService messageService )
  {
    this.messageService = messageService;
  }

  public void setCmAssetService( CMAssetService cmAssetService )
  {
    this.cmAssetService = cmAssetService;
  }

  public void setClaimDAO( ClaimDAO claimDAO )
  {
    this.claimDAO = claimDAO;
  }

  private String getPromotionAwardType( Promotion promotion )
  {
    String promoAwardType = null;

    if ( promotion.getPromotionType().getCode().equals( PromotionType.GOALQUEST ) || promotion.getPromotionType().getCode().equals( PromotionType.THROWDOWN ) )
    {
      promoAwardType = promotion.getAwardType().getCode();
    }
    else if ( promotion.getPromotionType().getCode().equals( PromotionType.CHALLENGE_POINT ) )
    {
      promoAwardType = ( (ChallengePointPromotion)promotion ).getChallengePointAwardType().getCode();
    }
    return promoAwardType;
  }

  public List<BadgeInfo> getTDRankingBadgesEarnable( Long promotionId, NodeType nodeType, Integer rank, BadgeLevelType levelType )
  {
    List<BadgeRule> rules = eligibleRuleForRankingBadges( promotionId, nodeType, rank, levelType );
    List<BadgeInfo> badges = new ArrayList<BadgeInfo>();
    if ( !rules.isEmpty() )
    {
      for ( BadgeRule rule : rules )
      {
        BadgeDetails badgeDetail = new BadgeDetails();
        badgeDetail.setBadgeName( rule.getBadgeNameTextFromCM() );
        badgeDetail.setBadgeDescription( rule.getBadgeDescriptionTextFromCM() );
        badgeDetail.setBadgeType( BadgeType.EARNED_OR_NOT_EARNED );
        List earnedNotEarnedImageList = getEarnedNotEarnedImageList( rule.getBadgeLibraryCMKey() );
        Iterator itr = earnedNotEarnedImageList.iterator();
        while ( itr.hasNext() )
        {
          BadgeLibrary badgeLib = (BadgeLibrary)itr.next();
          badgeDetail.setImg( badgeLib.getEarnedImageSmall() );
          badgeDetail.setImgLarge( badgeLib.getEarnedImageMedium() );
        }
        BadgeInfo badgeInfo = new BadgeInfo();
        badgeInfo.getBadgeDetails().add( badgeDetail );
        badges.add( badgeInfo );
      }
    }
    return badges;
  }

  private List<BadgeRule> eligibleRuleForRankingBadges( Long promotionId, NodeType nodeType, Integer rank, BadgeLevelType levelType )
  {
    List<BadgeRule> rules = new ArrayList<BadgeRule>();
    List<Badge> badges = getBadgeByPromotion( promotionId );
    for ( Badge badge : badges )
    {
      Set<BadgeRule> badgeRules = badge.getBadgeRules();
      Iterator<BadgeRule> ruleItr = badgeRules.iterator();
      while ( ruleItr.hasNext() )
      {
        BadgeRule rule = ruleItr.next();
        if ( rule.getLevelType().equals( levelType ) && rule.getLevelName().equals( nodeType.getNodeTypeName() ) )
        {
          if ( rank >= rule.getMinimumQualifier() && rank <= rule.getMaximumQualifier() )
          {
            rules.add( rule );
          }
        }
      }
    }
    return rules;
  }

  public List<ParticipantBadge> getBadgeByParticipantEarnedForThrowdown( Long promotionId, Long userId )
  {
    List<ParticipantBadge> paxList = new ArrayList<ParticipantBadge>();
    paxList = this.gamificationDAO.getBadgeByParticipantEarnedForThrowdown( promotionId, userId );
    return paxList;
  }

  /**
   * Gets the list of Badge by diy quiz
   * 
   * @param promotionId
   * @return List<Badge>
   */
  public List<Badge> getBadgeByQuiz( Long badgeRuleId )
  {
    return gamificationDAO.getBadgeByQuiz( badgeRuleId );
  }

  public List<BadgeDetails> toBadgeDetails( List<ParticipantBadge> badgeList )
  {
    List<BadgeDetails> badgeDetails = new ArrayList<BadgeDetails>();

    for ( ParticipantBadge paxBadge : badgeList )
    {
      BadgeDetails badgeDetail = new BadgeDetails();
      badgeDetail.setBadgeId( paxBadge.getId() );
      badgeDetail.setBadgeName( paxBadge.getBadgeRule().getBadgeName() );
      badgeDetail.setEarned( paxBadge.getIsEarned() );
      badgeDetail.setDateEarned( DateUtils.toDisplayString( paxBadge.getEarnedDate() ) );
      SSIContest contest = paxBadge.getContest();
      if ( contest != null )
      {
        badgeDetail.setContestName( getContestName( contest, UserManager.getLocale() ) );
        badgeDetail.setBadgeDescription( null );
      }
      else
      {
        badgeDetail.setBadgeDescription( paxBadge.getBadgeRule().getBadgeDescriptionTextFromCM() );
      }
      List earnedNotEarnedImageList = this.getEarnedNotEarnedImageList( paxBadge.getBadgeRule().getBadgeLibraryCMKey() );
      Iterator itr = earnedNotEarnedImageList.iterator();

      while ( itr.hasNext() )
      {
        BadgeLibrary badgeLib = (BadgeLibrary)itr.next();
        badgeDetail.setImg( badgeLib.getEarnedImageSmall() );
      }

      if ( paxBadge.getBadgePromotion().getBadgeType().getCode().equalsIgnoreCase( BadgeType.PROGRESS ) )
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
          progressNumerator = paxBadge.getSentCount() + paxBadge.getReceivedCount();
        }
        progressDenominator = paxBadge.getBadgeRule().getMaximumQualifier();
        badgeDetail.setProgressDenominator( progressDenominator );
        badgeDetail.setProgressNumerator( progressNumerator );
        badgeDetail.setBadgeType( paxBadge.getBadgePromotion().getBadgeType().getCode() );
      }
      badgeDetails.add( badgeDetail );
    }

    return badgeDetails;
  }

  public void setAudienceService( AudienceService audienceService )
  {
    this.audienceService = audienceService;
  }

  public void setJournalService( JournalService journalService )
  {
    this.journalService = journalService;
  }

  @Override
  public void deleteBadgePromotion( Set<BadgePromotion> badgePromotions ) throws ServiceErrorException
  {
    BadgePromotion badgePromo = new BadgePromotion();
    Iterator badgeItr = badgePromotions.iterator();
    while ( badgeItr.hasNext() )
    {
      badgePromo = (BadgePromotion)badgeItr.next();
      this.gamificationDAO.deleteBadgePromotion( badgePromo );
    }
  }

  public void deleteBadgeRule( Set<BadgeRule> badgeRuleList )
  {
    BadgeRule badgeRule = new BadgeRule();
    Iterator badgeRuleItr = badgeRuleList.iterator();
    while ( badgeRuleItr.hasNext() )
    {
      badgeRule = (BadgeRule)badgeRuleItr.next();
      this.gamificationDAO.deleteBadgeRule( badgeRule );
    }
  }

  public List<ParticipantBadge> getParticipantBadgeByPromotionId( Long promotionId )
  {
    return this.gamificationDAO.getParticipantBadgeByPromotionId( promotionId );
  }

  public List<Badge> getAllEligibleBadges()
  {
    return this.gamificationDAO.getAllEligibleBadges();
  }

  public int canCreateJournal( Long userId, Long ruleId, Long promotionId, String badgeType )
  {
    return this.gamificationDAO.canCreateJournal( userId, ruleId, promotionId, badgeType );
  }

  public String getBadgeImageUrlSuffix( BadgeRule badgeRule )
  {
    String badgeImage = "";
    if ( badgeRule != null )
    {
      List<BadgeLibrary> badgeLibraryList = promotionService.buildBadgeLibraryList();
      for ( BadgeLibrary badgeLibrary : badgeLibraryList )
      {
        if ( badgeRule.getBadgeLibraryCMKey().equals( badgeLibrary.getBadgeLibraryId() ) )
        {
          badgeImage = badgeLibrary.getEarnedImageSmall();
          break;
        }
      }
    }
    return badgeImage;
  }

  public String getContestName( SSIContest ssiContest, Locale locale )
  {
    return cmAssetService.getString( ssiContest.getCmAssetCode(), SSIContest.CONTEST_CMASSET_NAME, locale, true );
  }

  @Override
  public Badge getBadgeByIdWithAssociations( Long badgeId, AssociationRequestCollection associationRequestCollection )
  {
    return this.gamificationDAO.getBadgeByIdWithAssociations( badgeId, associationRequestCollection );
  }

  @Override
  public Set<BadgeRule> getSortedBadgeRulesById( Long badgeId )
  {
    return gamificationDAO.getSortedBadgeRulesById( badgeId );
  }

  @Override
  public List<ParticipantBadge> getBehaviorParticipantBadges( Long userId, Long promotionId, List<String> behaviorNames )
  {
    return gamificationDAO.getBehaviorParticipantBadges( userId, promotionId, behaviorNames );
  }

  protected Locale getUserLocale()
  {
    return UserManager.getLocale();
  }

}

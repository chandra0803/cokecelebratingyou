
package com.biperf.core.dao.gamification;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.biperf.core.dao.DAO;
import com.biperf.core.domain.gamification.Badge;
import com.biperf.core.domain.gamification.BadgePromotion;
import com.biperf.core.domain.gamification.BadgeRule;
import com.biperf.core.domain.gamification.ParticipantBadge;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.AssociationRequestCollection;

public interface GamificationDAO extends DAO
{

  // Gamification DAO Methods Starts Here

  /**
   * Gets the Badge by using id.
   * 
   * @param id
   * @return Badge
   */
  public Badge getBadgeById( Long id );

  /**
   * Gets the BadgeRule by using id.
   * 
   * @param id
   * @return BadgeRule
   */

  public BadgeRule getBadgeRuleById( Long id );

  /**
   * Gets the Badge Rule by the name.
   * 
   * @param id
   * @return BadgeRule
   */

  public BadgeRule getBadgeRuleByBadgeName( String badgeName, Long badePromotionId );

  /**
   * Gets the Badge by using status.
   * 
   * @param id
   * @return Badge
   */
  public List<Badge> getBadgeByStatus( String status );

  /**
   * Gets the list of Badge by status and type
   * 
   * @param status
   * @return List<Badge>
   */
  public List<Badge> getBadgeByTypeAndStatus( String type, String status );

  /**
   * Gets list of Badge by using userId
   * 
   * @param userId
   * @return List
   */
  public List<ParticipantBadge> getBadgeByParticipantId( Long userId );

  /**
   * Gets the behaviors for the promotions ids
   * 
   * @param promotionIds
   * @return List
   */
  public List getBehaviorForSelectedPromotions( String promotionIds, String promotionTypeCode );

  /**
   * Gets the Promotions Names for a behavior
   * 
   * @param promotionIds
   * @return List
   */

  public List getPromotionsNameForBehavior( String behavior, String promotionIds );

  /**
   * Gets the levels for the promotions ids
   * 
   * @param promotionId
   * @param promotionType
   * @return List
   */

  public List getLevelsByPromotions( long promotionId, String promotionType );

  /**
   *  Gets list of Badges by promotion
   *  @param promotionId
   *  @return List
   */
  public List<Badge> getBadgeByPromotion( Long promotionId );

  /**
   * Gets the All the history Badges for the particpant.
   * 
   * @param id
   * @return Badge
   */
  public List<ParticipantBadge> getBadgeParticipantHistory( Long userId );

  /**
   * Save or Update Badges
   * 
   * @param badge
   * @return Badge
   */
  public Badge saveBadge( Badge badge );

  /**
   * Save or Update BadgePromotion
   * 
   * @param badgePromo
   * @return BadgePromotion
   */

  public BadgePromotion saveBadgePromotion( BadgePromotion badgePromo );

  /**
   * Save or Update Badge Rule
   * 
   * @param badgeRule
   * @return BadgeRule
   */
  public BadgeRule saveBadgeRule( BadgeRule badgeRule );

  /**
   * Save or update the ParticipantBadge.
   * 
   * @param participantBadge
   * @return ParticipantBadge
   * @throws ServiceErrorException 
   */
  public ParticipantBadge saveParticipantBadge( ParticipantBadge participantBadge );

  /**
   * Check the badge setup name already exists.
   * 
   * @param new badge setup name
   * 
   */
  public String isBadgeNameExists( String badgeSetupName, String badgeId );

  // methods to get Gamification Tile sort order

  public List<ParticipantBadge> getBadgeByParticipantEarnedHighLight( Long userId );

  public List<ParticipantBadge> getBadgeByParticipantProgress( Long userId );

  public List<ParticipantBadge> getBadgeByParticipantBehavior( Long userId );

  public List<ParticipantBadge> getBadgeByParticipantEarnedOld( Long userId );

  // methods to get Gamification Profile Tile sort order

  public List<ParticipantBadge> getBadgeByParticipantEarnedProgress( Long userId, int rowNumber );

  public List<ParticipantBadge> getBadgeByParticipantNotEarnedProgress( Long userId, int rowNumber );

  public List<ParticipantBadge> getBadgeByParticipantEarnedFileLoad( Long userId, int rowNumber );

  // methods to get badges for a recognition

  public List<ParticipantBadge> getBadgeByParticipantPromotionEarnedHighLight( Long userId, Long promotionId );

  public List<ParticipantBadge> getBadgeByParticipantPromotionProgress( Long userId, Long promotionId );

  public Integer isUserHasActiveBadges( Long userId, String promotionIds );

  /**
   * Calls the stored procedure to verify badge file
   * 
   * @param importFileId
   * @param loadType
   * @param userId
   * @return Map result set
   */
  public Map verifyImportFile( Long importFileId, String loadType, Long userId, Long badgeId, Date earnedDate );

  public ParticipantBadge getParticipantBadgeByBadgeLib( BadgeRule rule, Long userId );

  public ParticipantBadge getParticipantBadgeByBadgeRule( BadgeRule badgeRule, Long userId );

  public List<BadgeRule> getBadgeByParticipantBehaviorNotEarned( Long[] promotionId, Long userId );

  public List<BadgeRule> getBadgeByParticipantProgressNotStarted( Long[] promotionId, Long userId );

  public List<ParticipantBadge> getBadgesForRecognitionConfirmationScreen( Long promotionId, Long userId );

  public List<ParticipantBadge> getBadgesForRecognitionEmailProgress( Long promotionId, Long userId );

  public List<ParticipantBadge> getBadgesForRecognitionEmailBehavior( Long promotionId, Long userId, String behaviorName );

  public List<ParticipantBadge> getParticipansEarnedHighestLevel( Long badgeId );

  public List<ParticipantBadge> getBadgeByParticipantEarnedForPromotion( Long promotionId, Long userId );

  /**
   * Gets the Badge Rule by the Behavior.
   * 
   * @param behaviorName
   * @param promotionId
   * @return BadgeRule
   */
  public BadgeRule getBadgeRuleByBehaviorName( String behaviorName, Long promotionId );

  public List<ParticipantBadge> getBadgeByParticipantEarnedForDIYQuiz( Long quizId, Long userId );

  public List<ParticipantBadge> getBadgeByParticipantEarnedForThrowdown( Long promotionId, Long userId );

  /**
   * Gets the list of Badge by diy quiz
   * 
   * @param promotionId
   * @return List<Badge>
   */
  public List<Badge> getBadgeByQuiz( Long badgeRuleId );

  public void getSessionLockForParticipantBadge( BadgeRule badgeRule, Long userId ) throws ServiceErrorException;

  public void deleteBadgePromotion( BadgePromotion badgePromotion );

  public void deleteBadgeRule( BadgeRule badgeRule );

  public List<Badge> getBadgeByBadgeIdAndPromotionId( Long promotionId );

  public List<ParticipantBadge> getParticipantBadgeByPromotionId( Long promotionId );

  public List<Badge> getAllEligibleBadges();

  public int canCreateJournal( Long userId, Long ruleId, Long promotionId, String badgeType );

  public Integer getBehaviorEarnedCount( Long promotionId, Long participantId );

  public List<Long> getBadgeNotificationList();

  public Badge getBadgeByIdWithAssociations( Long badgeId, AssociationRequestCollection associationRequestCollection );

  public Set<BadgeRule> getSortedBadgeRulesById( Long badgeId );

  public Integer getBadgesEarnedCount( Long badgeRuleId, Long participantId );

  /** Get ParticipantBadges given to the specified user for the given promotion ID and behaviors */
  public List<ParticipantBadge> getBehaviorParticipantBadges( Long userId, Long promotionId, List<String> behaviorNames );

  public List<Long> getBadgeEligiblePromotionIds();
}

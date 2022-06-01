
package com.biperf.core.service.gamification;

import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import com.biperf.core.domain.claim.Claim;
import com.biperf.core.domain.enums.BadgeLevelType;
import com.biperf.core.domain.gamification.Badge;
import com.biperf.core.domain.gamification.BadgeBehaviorPromotion;
import com.biperf.core.domain.gamification.BadgeDetails;
import com.biperf.core.domain.gamification.BadgeInfo;
import com.biperf.core.domain.gamification.BadgePromotion;
import com.biperf.core.domain.gamification.BadgePromotionLevels;
import com.biperf.core.domain.gamification.BadgeRule;
import com.biperf.core.domain.gamification.GamificationBadgeProfileView;
import com.biperf.core.domain.gamification.GamificationBadgeTileView;
import com.biperf.core.domain.gamification.ParticipantBadge;
import com.biperf.core.domain.goalquest.GoalBadgeRule;
import com.biperf.core.domain.hierarchy.NodeType;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.GoalQuestPromotion;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.ThrowdownPromotion;
import com.biperf.core.domain.user.User;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.SAO;
import com.biperf.core.service.UpdateAssociationRequest;
import com.biperf.core.value.PromotionMenuBean;

public interface GamificationService extends SAO
{

  /** Name of service bean in the beanFactory */
  public static final String BEAN_NAME = "gamificationService";

  // Gamification Service Methods Starts Here

  /**
  * Gets the Badges by the id.
  * 
  * @param id
  * @return Badge
  */
  public Badge getBadgeById( Long id );

  /**
   * Gets the Badge Rule by the id.
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
   * Gets the list of Badge by status
   * 
   * @param status
   * @return List<Badge>
   */
  public List<Badge> getBadgeByStatus( String status );

  /**
   * Gets the list of Badge by promotion
   * 
   * @param promotionId
   * @return List<Badge>
   */

  public List<Badge> getBadgeByPromotion( Long promotionId );

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
   * @return List<ParticipantBadge>
   */
  public List<ParticipantBadge> getBadgeByParticipantId( Long userId );

  /**
   * Gets list of Badge by using userId
   * 
   * @param userId
   * @return List<ParticipantBadge>
   */
  public List getEarnedNotEarnedImageList( String badgeLibId );

  public List getEarnedNotEarnedImageList( String badgeLibId, Locale locale );

  /**
   * Gets list of Badge by using userId
   * 
   * @param userId
   * @return List<ParticipantBadge>
   */
  public List<ParticipantBadge> getBadgeByParticipantSorted( List<PromotionMenuBean> eligiblePromotions, Long userId );

  /**
   * Gets list of Badge by using userId
   * 
   * @param userId
   * @return List<ParticipantBadge>
   */
  public List<ParticipantBadge> getBadgeByParticipantProfileSorted( List<PromotionMenuBean> eligiblePromotions, Long userId, int rowNumber );

  /**
   * Gets the behaviors for the promotions ids
   * 
   * @param promotionIds
   * @return String[]
   */
  public List getBehaviorForSelectedPromotions( String promotionIds, List<String> existingPromoId, String promotionTypeCode );

  /**
   * Validate the promotion type
   * 
   * @param promotionIds
   * @return String
   */
  public String validatePromotionType( String promotionIds );

  /**
   * Validate the promotion type
   * 
   * @param promotionIds
   * @return String
   */
  public String validatePromotionTypeOthers( String promotionIds );

  /**
   * Validate the promotion end date
   * 
   * @param promotionIds
   * @return String
   */
  public String validatePromotionEndDate( String promotionIds );

  /**
   * Get the award type
   * 
   * @param promotionIds
   * @return String
   */

  public String getAwardType( String promotionIds );

  /**
   * Get the levels for each promotion
   * 
   * @param promotionIds
   * @return List<BadgePromotionLevels>
   */
  public List<BadgePromotionLevels> getPromotionLevels( String promotionIds );

  /**
   * Get the promotion type (recognition, goalquest etc)
   * 
   * @param promotionId
   * @return
   */
  public String getPromotionTypeById( String promotionId );

  /**
   * Get the behaviors for each promotion
   * 
   * @param promotionIds
   * @return List<BadgeBehaviorPromotion>
   */
  public List<BadgeBehaviorPromotion> getBadgeBehaviorPromotions( String promotionIds, List behaviors );

  /**
   * Get the list of live and completed promotions
   * @return List
   */
  public List getLiveCompletedPromotionList();

  /**
   * Get the list of badge library from CM
   * @return List
   */
  public List buildBadgeLibraryList();

  /**
   * Get the list of badge library from CM for progress badge type
   * @return List
   */
  public List buildBadgeLibraryListProgress();

  /**
   * Get the list of rows for file load badge type
   * @param count
   * @return List
   */
  public List buildFileLoadTableList( int count, List<BadgeRule> badgeRuleList );

  /**
   * Get the list of rows for progress badge type
   * @param count
   * @return List
   */
  public List buildProgressTableList( int count, List<BadgeRule> badgeRuleList );

  /**
   * Get the list of rows for Point Range badge type
   * @param count
   * @return List
   */
  public List buildPointRangeTableList( int count, List<BadgeRule> badgeRuleList );

  /**
   * 
   * @param badgeRule
   * @return
   */
  public List buildPromotionTableList( GoalBadgeRule badgeRule );

  /**
   * Get the promotion names for each behavior
   * @param promotionIds
   * @param behaviorName
   * @return String
   */

  public String getPromotionByBehavior( String promotionIds, String behaviorName );

  /**
   * Save or Update the Badge 
   * 
   * @param badge
   * @return Badge
   */
  public Badge saveBadge( Badge badge ) throws ServiceErrorException;

  public Badge saveBadge( Long badgeToSave, UpdateAssociationRequest updateAssociationRequest ) throws ServiceErrorException;

  public Badge saveBadge( Long badgeToSave, List updateAssociationRequests ) throws ServiceErrorException;

  /**
   * Save or Update the Badge Rules
   * 
   * @param badgeruleList
   * @return BadgeRule
   */
  public BadgeRule saveBadgeRules( List<BadgeRule> badgeruleList ) throws ServiceErrorException;

  /**
   * Save the Badge Promotions
   * 
   * @param badgePromo
   * @return BadgePromotion
   */

  public BadgePromotion saveBadgePromotion( BadgePromotion badgePromo ) throws ServiceErrorException;

  /**
   * Save or update the ParticipantBadge.
   * 
   * @param participantBadge
   * @return ParticipantBadge
   */
  public ParticipantBadge saveParticipantBadge( ParticipantBadge participantBadge );

  /**
   * Check the badges Setup name already exists.
   * 
   * @param new badge setup name
   * 
   */
  public String isBadgeNameExists( String name, String badgeId ) throws ServiceErrorException;

  /**
   * Get the JSON object for showing in participant tile
   * 
   * @param partcipantBadgeList
   * @return GamificationBadgeTileView
   */

  public GamificationBadgeTileView getTileViewJson( List<ParticipantBadge> partcipantBadgeList );

  /**
   * Get the JSON object for showing in participant profile
   * 
   * @param partcipantBadgeList
   * @return GamificationBadgeProfileView
   */

  public GamificationBadgeProfileView getProfileViewJson( List<ParticipantBadge> partcipantBadgeList, boolean isFromMiniProfile );

  /**
   * Get the badges library description
   * 
   * @param badgeLibCode
   * @return badgeLibDescription(String)
   * 
   */

  public String getBadgeLibDescription( String badgeLibCode );

  public Integer isUserHasActiveBadges( Long userId, List<PromotionMenuBean> eligiblePromotions );

  /**
   * Verifies the specified badge import file
   * 
   * @param importFileId
   * @param userId
   * @return Map result set
   */
  public Map verifyImportFile( Long importFileId, Long userId, Long badgeId, Date earnedDate );

  /**
   * Import the specified badge import file
   * 
   * @param importFileId
   * @param userId
   * @return Map result set
   */
  public Map importImportFile( Long importFileId, Long userId, Long badgeId, Date earnedDate );

  public List<ParticipantBadge> populateBadgePartcipant( Claim claim ) throws ServiceErrorException;

  // Gamification Service Methods Ends Here

  /**
   * 
   * @param userId
   * @return List of ParticiantBadgee history
   */
  public List<ParticipantBadge> getBadgeParticipantHistory( Long userId );

  public List<ParticipantBadge> getBadgeByParticipantPromotionSorted( Long promotionId, Long userId, int rowNumber );

  public List<ParticipantBadge> getBadgesForRecognitionConfirmation( List<ParticipantBadge> partcipantBadgeList, int rowNumber );

  public List<ParticipantBadge> getBadgesForRecognitionConfirmationScreen( Long promotionId, Long userId );

  public List<ParticipantBadge> getBadgesForRecognitionEmail( Long promotionId, Long userId, String behaviorName );

  public void sendSummaryMessage( Participant participant, String promotionName, BadgeRule rule, int recordExist );

  public List<ParticipantBadge> getParticipansEarnedHighestLevel( Long badgeId );

  public String saveRulesCmText( String badgeName, String badgeName2 ) throws ServiceErrorException;

  public String saveRulesCmText( String badgeRule, String badgeNameText, Locale locale ) throws ServiceErrorException;

  public List<ParticipantBadge> getBadgeByParticipantEarnedForPromotion( Long promotionId, Long userId );

  public List<ParticipantBadge> getBadgeByParticipantEarnedForDIYQuiz( Long quizId, Long userId );

  public ParticipantBadge populateEarnedBadgesCPandGQ( String levelName, User reciever, GoalQuestPromotion promotion, Badge badge, boolean isParticipant );

  /**
   * Gets the Badge Rule by the Behavior.
   * 
   * @param behaviorName
   * @param promotionId
   * @return BadgeRule
   */
  public BadgeRule getBadgeRuleByBehaviorName( String behaviorName, Long promotionId );

  public ParticipantBadge populateEarnedBadgesTD( Integer ranking, Participant reciever, ThrowdownPromotion promotion, Badge badge, BadgeLevelType type, String levelName );

  public List<BadgeInfo> getTDRankingBadgesEarnable( Long promotionId, NodeType nodeType, Integer rank, BadgeLevelType levelType );

  public List<ParticipantBadge> getBadgeByParticipantEarnedForThrowdown( Long promotionId, Long userId );

  /**
   * Gets the list of Badge by diy quiz
   * 
   * @param promotionId
   * @return List<Badge>
   */
  public List<Badge> getBadgeByQuiz( Long badgeRuleId );

  public List<BadgeDetails> toBadgeDetails( List<ParticipantBadge> badgeList );

  public void deleteBadgePromotion( Set<BadgePromotion> badges ) throws ServiceErrorException;

  public void deleteBadgeRule( Set<BadgeRule> badgeRuleList );

  public List<Badge> getBadgeByBadgeIdAndPromotionId( Long promotionId );

  public List<ParticipantBadge> getParticipantBadgeByPromotionId( Long promotionId );

  public List<Badge> getAllEligibleBadges();

  public int canCreateJournal( Long userId, Long ruleId, Long promotionId, String badgeType );

  public void createJournalEntry( Promotion promotion, Participant participant, Long points );

  public String getBadgeImageUrlSuffix( BadgeRule badgeRule );

  public Badge getBadgeByIdWithAssociations( Long badgeId, AssociationRequestCollection badgeAssociationRequestCollection );

  public Set<BadgeRule> getSortedBadgeRulesById( Long badgeId );

  /** Get ParticipantBadges given to the specified user for the given promotion ID and behaviors */
  public List<ParticipantBadge> getBehaviorParticipantBadges( Long userId, Long promotionId, List<String> behaviorNames );

  public List<ParticipantBadge> getBehaviorBadgesForRecognitionEmail( Long promotionId, Long userId, String behaviorName );

}

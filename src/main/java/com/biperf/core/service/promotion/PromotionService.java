/**
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/promotion/PromotionService.java,v $
 */

package com.biperf.core.service.promotion;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import com.biperf.core.dao.promotion.hibernate.PromotionQueryConstraint;
import com.biperf.core.domain.budget.Budget;
import com.biperf.core.domain.budget.BudgetMaster;
import com.biperf.core.domain.budget.BudgetSegment;
import com.biperf.core.domain.budget.PromotionBudgetSweep;
import com.biperf.core.domain.claim.ClaimFormStep;
import com.biperf.core.domain.claim.ClaimFormStepElement;
import com.biperf.core.domain.enums.PickListItem;
import com.biperf.core.domain.enums.PromotionType;
import com.biperf.core.domain.gamification.BadgeLibrary;
import com.biperf.core.domain.hierarchy.Node;
import com.biperf.core.domain.merchandise.MerchOrder;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.participant.ParticipantPartner;
import com.biperf.core.domain.promotion.AbstractGoalLevel;
import com.biperf.core.domain.promotion.AbstractRecognitionPromotion;
import com.biperf.core.domain.promotion.Division;
import com.biperf.core.domain.promotion.EngagementPromotion;
import com.biperf.core.domain.promotion.GoalQuestPromotion;
import com.biperf.core.domain.promotion.NominationPromotion;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.PromotionCert;
import com.biperf.core.domain.promotion.PromotionClaimFormStepElementValidation;
import com.biperf.core.domain.promotion.QuizPromotion;
import com.biperf.core.domain.promotion.RecognitionPromotion;
import com.biperf.core.domain.promotion.ThrowdownPromotion;
import com.biperf.core.domain.quiz.QuizLearningDetails;
import com.biperf.core.domain.user.User;
import com.biperf.core.exception.BeaconRuntimeException;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.exception.ServiceErrorExceptionWithRollback;
import com.biperf.core.exception.UniqueConstraintViolationException;
import com.biperf.core.security.AuthenticatedUser;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.SAO;
import com.biperf.core.service.UpdateAssociationRequest;
import com.biperf.core.service.participant.ParticipantAssociationRequest;
import com.biperf.core.value.ActivityCenterValueBean;
import com.biperf.core.value.AlertsValueBean;
import com.biperf.core.value.FormattedValueBean;
import com.biperf.core.value.NameableBean;
import com.biperf.core.value.PromotionBean;
import com.biperf.core.value.PromotionMenuBean;
import com.biperf.core.value.PromotionsValueBean;
import com.biperf.core.value.QuizPageValueBean;
import com.biperf.core.value.RecognitionBean;
import com.biperf.core.value.SurveyPageValueBean;
import com.biperf.core.value.celebration.CelebrationImageFillerValue;
import com.biperf.core.value.nomination.NominationAdminApprovalsBean;
import com.biperf.core.value.nomination.NominationApproverValueBean;
import com.biperf.core.value.participant.PromoRecImageData;
import com.biperf.core.value.participant.PromoRecPictureData;
import com.biperf.core.value.promotion.CustomFormSelectListView;
import com.biperf.core.value.promotion.CustomFormStepElementsView;
import com.biperf.core.value.promotion.RecognitionAdvisorPromotionValueBean;

/**
 * PromotionService.
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
 * <td>asondgeroth</td>
 * <td>Jul 4, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 *
 *
 */
public interface PromotionService extends SAO
{
  public static final String BEAN_NAME = "promotionService";

  /**
   * Get the Promotion from the database by the id.
   *
   * @param id
   * @return Promotion
   */
  public Promotion getPromotionById( Long id );

  /**
   * Get the Promotion with associations from the database by the id.
   *
   * @param id
   * @param associationRequestCollection
   * @return Promotion
   */
  public Promotion getPromotionByIdWithAssociations( Long id, AssociationRequestCollection associationRequestCollection );

  public Promotion getPromotionByIdWithCalcAsso( Long id, AssociationRequestCollection associationRequestCollection );

  /**
   * Returns a list of promotions that meet the specified criteria. Any parameter can be left null
   * so that the query is not constrained by that parameter.
   *
   * @param queryConstraint
   * @return List the promotion list
   */
  public List getPromotionList( PromotionQueryConstraint queryConstraint );

  /**
   * Returns a list of promotions with any association request that meet the specified criteria. Any parameter can be left null
   * so that the query is not constrained by that parameter.
   *
   * @param queryConstraint
   * @param associationRequestCollection
   * @return List the promotion list
   */
  public List getPromotionListWithAssociations( PromotionQueryConstraint queryConstraint, AssociationRequestCollection associationRequestCollection );

  /**
   * Returns a count of promotions that meet the specified criteria. Any parameter can be left null
   * so that the query is not constrained by that parameter.
   *
   * @param queryConstraint
   * @return int the promotion list count
   */
  public int getPromotionListCount( PromotionQueryConstraint queryConstraint );

  public List getPromotionListWithAssociationsForHomePage( PromotionQueryConstraint queryConstraint, AssociationRequestCollection associationRequestCollection );

  /**
   * Builds a list of participants for the giver audience who are not budget owners.
   *
   * @param promotionId
   * @return List
   */
  public Set getParticipantsWithoutBudgetAllocationWithinPromotion( Long promotionId, Long budgetSegmentId );

  /**
   * Builds a list of participants for the giver audience who are not budget owners.
   *
   * @param promotionId
   * @return List
   */
  public Set getParticipantsWithoutPublicRecogBudgetAllocationWithinPromotion( Long promotionId, Long budgetSegmentId );

  public List<RecognitionBean> getRecognitionSubmissionList( Long userId, List<PromotionMenuBean> eligiblePromotions, boolean isUserAParticipant );

  /**
   * Builds a list of node for the giver audience who are not budget owners.
   *
   * @param promotionId
   * @return List
   */
  public Set getNodeWithoutPublicRecogBudgetAllocationWithinPromotion( Long promotionId, Long budgetSegmentId );

  /**
   * Builds a list of budgets for all participant nodes which don't have a budget amount allocated.
   *
   * @param promotionId
   * @return List
   */
  public Set getNodesWithoutBudgetAllocationWithinPromotion( Long promotionId, Long budgetSegmentId );

  /**
   * Saves the validation for a claimFormStepElement within a promotion.
   *
   * @param promotionClaimFormStepElementValidation
   * @return PromotionClaimFormStepElementValidation
   */
  public PromotionClaimFormStepElementValidation savePromotionClaimFormStepElementValidation( PromotionClaimFormStepElementValidation promotionClaimFormStepElementValidation );

  /**
   * Saves a list of newly created Promotion ClaimFormStepElement validations.
   *
   * @param promotionClaimFormStepElementValidationList
   * @return List
   */
  public List savePromotionClaimFormStepElementValidationList( List promotionClaimFormStepElementValidationList );

  /**
   * Update the list of promotion validations for the promotion.
   *
   * @param promotionId
   * @param promotionClaimFormStepElementValidationList
   * @return List
   */
  public List updatePromotionClaimFormElementValidations( Long promotionId, List promotionClaimFormStepElementValidationList );

  /**
   * Get the validation for the claimFormStepElement for the id.
   *
   * @param id
   * @return PromotionClaimFormStepElementValidation
   */
  public PromotionClaimFormStepElementValidation getPromotionClaimFormStepElementValidationById( Long id );

  /**
   * Get a list of all claimFormStepElement validations for a promotion and claimFormStep
   *
   * @param promotion
   * @param claimFormStep
   * @return List
   */
  public List getAllPromotionClaimFormStepElementValidations( Promotion promotion, ClaimFormStep claimFormStep );

  /**
   * Get a list of all claimFormStepElement validations for a promotionId.
   *
   * @param promotionId
   * @return List
   */
  public List getAllPromotionClaimFormStepElementValidations( Long promotionId );

  /**
   * Performs additional validation on the given promotion.
   *
   * @param promotionId
   * @param updateAssociationRequest
   * @throws ServiceErrorExceptionWithRollback
   */
  public void validatePromotion( Long promotionId, UpdateAssociationRequest updateAssociationRequest ) throws ServiceErrorExceptionWithRollback;

  /**
   * Saves the hierarchy to the database.
   *
   * @param promotion
   * @return Promotion
   * @throws UniqueConstraintViolationException
   */
  public Promotion savePromotion( Promotion promotion ) throws UniqueConstraintViolationException;

  /**
   * Saves the promotion with payouts to the database.
   *
   * @param promotionId
   * @param updateAssociationRequest
   * @return Promotion
   * @throws UniqueConstraintViolationException
   */
  public Promotion savePromotion( Long promotionId, UpdateAssociationRequest updateAssociationRequest ) throws UniqueConstraintViolationException;

  /**
   * Saves the promotion with payouts to the database.
   *
   * @param promotionId
   * @param updateAssociationRequests
   * @return Promotion
   * @throws UniqueConstraintViolationException
   */
  public Promotion savePromotion( Long promotionId, List updateAssociationRequests ) throws UniqueConstraintViolationException;

  /**
   * Deletes a list of promotions.
   *
   * @param promotionIdList - List of promotion.id
   * @throws ServiceErrorException
   */
  public void deletePromotions( List promotionIdList ) throws ServiceErrorException;

  /**
   * Deletes the Promotion from the database.
   *
   * @param promotionId
   * @throws ServiceErrorException
   */
  public void deletePromotion( Long promotionId ) throws ServiceErrorException;

  /**
   * Retrieves all the Promotion from the database.
   *
   * @return List a list of Promotions
   */
  public List getAll();

  /**
   * Retrieves all the live and expired promotions given type from the database
   *
   * @param userId
   * @return List list of Promotions
   */
  public List getAllLiveAndExpiredByUserId( Long userId );

  /**
   * Retrieves all the live and expired promotions given type from the database
   *
   * @param promotionType
   * @param userId
   * @return List list of Promotions
   */
  public List getAllLiveAndExpiredByTypeAndUserId( String promotionType, Long userId );

  /**
   * Retrieves all the live promotions of the given type from the database.
   *
   * @return List a list of Promotions
   */
  public List getAllLiveByType( String promotionType );

  /**
   * Retrieves all the live promotions given type from the database
   *
   * @param promotionType
   * @param userId
   * @return List list of Promotions
   */
  public List getAllLiveByTypeAndUserId( String promotionType, Long userId );

  /**
   * Retrieves all the live promotions which had Budget from the database.
   *
   * @return List a list of Promotions
   */
  public List getAllLiveWithBudget();

  /**
   * Retrieves all the non expired Promotions from the database.
   *
   * @return List a list of non expired Promotions
   */
  public List getAllNonExpired();

  /**
   * Retrieves all the non expired Promotions from the database with a specific type.
   *
   * @param promotionType
   * @return List a list of non expired Promotions
   */
  public List getAllNonExpiredByType( PromotionType promotionType );

  /**
   * Retrieves all the expired Promotions from the database.
   *
   * @return List a list of expired Promotions
   */
  public List getAllExpired();

  /**
   * Retrieves all the expired Promotions from the database with a specific type.
   *
   * @param promotionType
   * @return List a list of non expired Promotions
   */
  public List getAllExpiredByType( PromotionType promotionType );

  /**
   * Get all promotions with sweepstakes
   *
   * @return List
   */
  public List getAllWithSweepstakes();

  /**
   * Get all promotions with sweepstakes with associations
   *
   * @param associationRequestCollection
   * @return List
   */
  public List getAllWithSweepstakesWithAssociations( AssociationRequestCollection associationRequestCollection );

  /**
   * Gets promotions on which the participant can submit a claim.
   *
   * @param participant
   * @return List
   */
  public List getClaimablePromotionsForParticipant( Participant participant );

  /**
   * Build the list of nodes against which a participant can submit a claim.
   *
   * @param promotionId
   * @param participantId
   * @return returns list of claimable nodes
   */
  public List getClaimableNodes( Long promotionId, Long participantId );

  public List getClaimableNodesNonProxy( Long promotionId, Long participantId );

  public void expirePromotions();

  public void launchPromotions();

  /**
   * Saves the data into Content Manager based on the promotion argument's cmAsset and cmKey. If a
   * cmAsset or cmKey do not exist, then one will be created and set on the promotion instance.
   *
   * @param promotion
   * @param data
   * @return Promotion
   * @throws ServiceErrorException
   */
  public Promotion saveWebRulesCmText( Promotion promotion, String data ) throws ServiceErrorException;

  /**
   * Saves the data into Content Manager based on the locale and promotion argument's cmAsset and cmKey. If a
   * cmAsset or cmKey do not exist, then one will be created and set on the promotion instance.
   *
   * @param promotion
   * @param data
   * @return Promotion
   * @throws ServiceErrorException
   */
  public Promotion saveWebRulesCmText( Promotion promotion, String data, Locale locale ) throws ServiceErrorException;

  /**
   * Saves the data into Content Manager based on the promotion argument's promoNameAssetCode. If a
   * promoNameAssetCode does not exist, then one will be created and set on the promotion instance.
   *
   * @param promotion
   * @param data
   * @return Promotion
   * @throws ServiceErrorException
   */
  public Promotion savePromoNameCmText( Promotion promotion, String data ) throws ServiceErrorException, UniqueConstraintViolationException;

  /**
   * Saves the data into Content Manager based on the locale and promotion argument's promoNameAssetCode. If a
   * promoNameAssetCode does not exist, then one will be created and set on the promotion instance.
   *
   * @param promotion
   * @param data
   * @param locale
   * @param flag
   * @return Promotion
   * @throws ServiceErrorException
   */
  public Promotion savePromoNameCmText( Promotion promotion, String data, Locale locale, boolean flag ) throws ServiceErrorException, UniqueConstraintViolationException;

  public Promotion savePromoObjectivieCmText( GoalQuestPromotion promotion, String data ) throws ServiceErrorException;

  public Promotion savePromoObjectivieCmText( GoalQuestPromotion promotion, String data, Locale locale ) throws ServiceErrorException;

  public Promotion savePromotionOverviewCmText( Promotion promotion, String overviewDetailsText ) throws ServiceErrorException;

  public Promotion savePromotionOverviewCmText( Promotion promotion, String overviewDetailsText, Locale locale ) throws ServiceErrorException;

  public Promotion saveQuizDetailsCmText( Promotion promotion, String quizDetails ) throws ServiceErrorException;

  public Promotion saveQuizDetailsCmText( Promotion promotion, String quizDetails, Locale locale ) throws ServiceErrorException;

  public Promotion saveGoalNameAndDescriptionInCM( Promotion promotion, List<AbstractGoalLevel> goalLevelsFromAdmin ) throws ServiceErrorException;

  public Promotion saveGoalNameAndDescriptionInCM( Promotion promotion, List<AbstractGoalLevel> goalLevelsFromAdmin, Locale locale ) throws ServiceErrorException;

  public Promotion saveDivisionNamesInCM( ThrowdownPromotion promotion, Division division, Locale locale ) throws ServiceErrorException;

  public Promotion saveDivisionNamesInCM( ThrowdownPromotion promotion, Division division ) throws ServiceErrorException;

  public Promotion savePayoutStrutureBaseUnitInCM( Promotion promotion, String baseUnit ) throws ServiceErrorException;

  public Promotion savePayoutStrutureBaseUnitInCM( Promotion promotion, String baseUnit, Locale locale ) throws ServiceErrorException;

  public Promotion savePayoutStrutureBaseUnitInCM( ThrowdownPromotion promotion, String baseUnit ) throws ServiceErrorException;

  public Promotion savePayoutStrutureBaseUnitInCM( ThrowdownPromotion promotion, String baseUnit, Locale locale ) throws ServiceErrorException;

  /**
   * Copies a Promotion
   *
   * @param promotionIdToCopy
   * @param newPromotionName
   * @param newChildPromotionNameHolders
   * @return Promotion (The copied Promotion)
   * @throws UniqueConstraintViolationException
   * @throws ServiceErrorException
   */
  public Promotion copyPromotion( Long promotionIdToCopy, String newPromotionName, List newChildPromotionNameHolders ) throws UniqueConstraintViolationException, ServiceErrorException;

  public boolean isPromotionClaimableByParticipant( Long promotionId, Participant participant );

  public boolean isPromotionClaimableByParticipant( Promotion promotion, Participant participant );

  public boolean isPromotionClaimableByParticipant( Promotion promotion, Participant participant, Date onThisDate );

  public boolean isPromotionClaimableByParticipant( Long promotionId, Participant participant, boolean flag );

  public boolean isPromotionClaimableByParticipant( Promotion promotion, Participant participant, boolean flag );

  public boolean isPromotionClaimableByNode( Promotion promotion, Node node );

  /**
   * Retrieves all the child promotions for the specified promotion
   *
   * @param promotionId
   * @return List
   */
  public List getChildPromotions( Long promotionId );

  /**
   * Retrieves all the non expired child promotions for the specified promotion
   *
   * @param promotionId
   * @return List
   */
  public List getNonExpiredChildPromotions( Long promotionId );

  /**
   * Get all promotions that the approverUserId has approval rights on.
   *
   * @param approverUserId
   * @param promotionType
   * @return Set
   */
  public Set getAllForApprover( Long approverUserId, PromotionType promotionType );

  /**
   * Create a child promotion with necessary data related to the following promotion sections: -
   * Basics - Form Rules - Approvals - Notifications NOTE: This method does not persist the child
   * promotion.
   *
   * @see com.biperf.core.service.promotion.PromotionService#createChildPromotion(java.lang.Long)
   * @param parentPromotionId
   * @return Promotion (The child Promotion)
   * @throws ServiceErrorException
   * @throws BeaconRuntimeException
   */
  public Promotion createChildPromotion( Long parentPromotionId ) throws ServiceErrorException, BeaconRuntimeException;

  /**
   * @param participant
   * @param promotion
   * @param isSubmitter
   * @param submitterNode
   * @return
   */
  public boolean isParticipantMemberOfPromotionAudience( Participant participant, Promotion promotion, boolean isSubmitter, Node submitterNode );

  /**
   * Send an email to the client email address that contains a link back to the ECard page
   *
   * @param emailAddress
   * @param promotionId
   */
  public void sendCardSelectionEmail( String emailAddress, Long promotionId );

  /**
   * @param promotionId
   * @param participantId
   * @param nodeId
   * @return Budget
   */
  public Budget getAvailableBudget( Promotion promotion, Participant participant, Node node );

  // Fix for bug#56006,55519 start
  /**
   * @param promotionId
   * @param participantId
   * @param nodeId
   * @param budgetSegment
   * @return Budget
   */
  public Budget getAvailableBudget( Promotion promotion, Participant participant, Node node, BudgetSegment budgetSegment );

  // Fix for bug#56006,55519 end

  // TODO: find these references and see if we can deprecate this method to the one above.
  public Budget getAvailableBudget( Long promotionId, Long participantId, Long nodeId );

  // Fix for bug#56006,55519 start
  // TODO: as the above references were not found and modified, so for a bug fix had to create this
  // new one
  public Budget getAvailableBudget( Long promotionId, Long participantId, Long nodeId, BudgetSegment budgetSegment );

  // Fix for bug#56006,55519 end

  /**
   * Retrieves the promotion for the specified enrollProgramCode.
   * enrollProgramCode should be unique for each promotion.
   *
   * Overridden from @see com.biperf.core.dao.promotion.PromotionDAO#getPromotionByEnrollProgramCode(java.lang.String)
   * @param enrollProgramCode
   * @return List
   */
  public List getPromotionByEnrollProgramCode( String enrollProgramCode );

  /**
   * This method validates that the list of programs all contain the same number of Levels/Segments
   * within the OM system and returns the maximum number of levels for the collection of program ids.
   *
   * @param programIds the list of program IDs
   */
  public int getMaximumLevelForPrograms( List programIds ) throws ServiceErrorException;

  /**
   * Get the number of recognitions sent for a promotion, counting non-approved submissions
   *
   * @param promotionId
   * @return long
   */
  public long getRecognitionsSubmittedForPromotion( Long promotionId );

  /**
   * @param promotionId
   * @param participantId
   * @return Partners of an Partner By Promotion
   */
  public List<ParticipantPartner> getPartnersByPromotionAndParticipantWithAssociations( Long promotionId, Long participantId, ParticipantAssociationRequest paxAscReq );

  public List<ParticipantPartner> getParticipantsByPromotionAndPartnerWithAssociations( Long promotionId, Long partnerId, ParticipantAssociationRequest paxAscReq );

  public BigDecimal getPartnerAwardAmountByPromotionAndSequenceNo( Long promotionId, int seqNo );

  public boolean isParticipantInAudience( Participant participant, Promotion promotion );

  public boolean isParticipantInDIYPromotionAudience( Participant participant );

  /**
   * Get the list of live or complete recognition promotions which have budget transfer turned on.
   * List is filtered for the given user - only promotions they are eligible for are returned
   *
   * @param authenticatedUser - user to pull eligible promotions for
   * @return List<Promotion>
   */
  public HashSet<BudgetMaster> getEligibleBudgetTransfer( AuthenticatedUser authenticatedUser );

  /**
   * @return list
   */
  public List<Promotion> getAllPromotionsWithSweepstakes();

  public List getPaxPromoRules( AuthenticatedUser authenticatedUser ) throws ServiceErrorException;

  /**
   * Get All Pending Quiz Attempts for participant
   * @param allLivePromo
   * @param participant
   * @return
   */
  public List<QuizPageValueBean> getPendingQuizSubmissionList( Long userId );

  /*
   * Does the user have any pending quiz attempts?
   */
  public boolean isPendingQuizSubmissionsForUser( Long userId );

  /**
   * Get all Recognition and Nomination promotions that the participant can submit claims for
   * @param allLivePromo
   * @param participant
   * @return
   */
  public List<RecognitionBean> getRecognitionSubmissionList( Long userId, boolean isUserAParticipant );

  /**
   * Get all Nomination promotions (and only Nomination) that the participant can submit claims for
   * @param userId
   * @param isUserAParticipant
   * @return
   */
  public List<RecognitionBean> getNominationSubmissionList( Long userId, boolean isUserAParticipant );

  /**
   * Get all Easy Recognition promotions for the given submitter and given receiver.
   * Return the list of Easy Recognitions where the submitter is a valid giver
   * and the receiver is a valid receiver.
   * @param allLivePromo
   * @param participant
   * @return
   */
  public List<RecognitionBean> getEasyRecognitionSubmissionList( Long submitterId, boolean isSubmitterAParticipant, Long recipientId );

  /**
   * Determine if the given node ID is a claimable node for the given promotion
   * ID and participant ID.
   * @param nodeId
   * @param promotionId
   * @param participantId
   *
   * @return boolean indicating if the combination of node, promotion, and
   * participant is claimable.
   */
  public boolean isNodeClaimableForRecognitionPromotion( Long nodeId, Long promotionId, Long participantId );

  /**
   * Retrieves all the live and expired promotions given type from the database
   *
   * @param promotionType
   * @return List list of Promotions
   */
  public List getAllLiveAndExpiredByType( String promotionType );

  /**
   * Get list of pending alerts
   * @param eligiblePromos
   * @param participant
   * @return
   */
  public List<ActivityCenterValueBean> getPendingAlertsList( Long userId, List eligiblePromotions );

  public List<AlertsValueBean> getAlertsList( AuthenticatedUser user, Long userId, boolean isModalWindow, List eligiblePromotions, boolean isMessagesPage );

  public boolean isEasyPromotionWithBehaviors( AbstractRecognitionPromotion promotion );

  public Budget getPublicRecognitionBudget( Long promotionId, Long participantId, Long nodeId );

  public boolean displayPublicRecognitionTile();

  public List<AlertsValueBean> getPendingFileDownloadsForAlerts( Long userId );

  public List getMerchandisePromotionIds();

  public Promotion saveWebRulesManagerCmText( Promotion promotion, String data ) throws ServiceErrorException;

  public Promotion saveWebRulesManagerCmText( Promotion promotion, String data, Locale locale ) throws ServiceErrorException;

  public Promotion saveWebRulesPartnerCmText( Promotion promotion, String data ) throws ServiceErrorException;

  public Promotion saveWebRulesPartnerCmText( Promotion promotion, String data, Locale locale ) throws ServiceErrorException;

  public List<BadgeLibrary> buildBadgeLibraryList();

  public List<ParticipantPartner> getParticipantPartnersWhereSelectionEmailNotSentByPromotion( Long promotionId );

  public PromotionCert getPromoCertificateById( Long certificateId );

  public QuizPromotion getLiveDIYQuizPromotion();

  public QuizPromotion getLiveOrCompletedDIYQuizPromotion();

  public EngagementPromotion getLiveOrCompletedEngagementPromotion();

  public List<FormattedValueBean> getAwardGenEligiblePromotionList();

  public List<SurveyPageValueBean> getPendingSurveysList( Long userId );

  public RecognitionPromotion savePurlStandardMessageVideo( RecognitionPromotion promotion, String videoUrlMp4, String videoUrlWebm, String videoUrl3gp, String videoUrlOgg, String rightColumnHtml )
      throws ServiceErrorException, Exception;

  public RecognitionPromotion savePurlStandardMessageImage( RecognitionPromotion promotion, String leftColumnHtml, String rightColumnHtml, String mediaFilePath ) throws ServiceErrorException;

  public List<QuizLearningDetails> getPurlStandardMessagePictureObjects( String contentResourceCMCode );

  public List<FormattedValueBean> getEngagementEligiblePromotionList();

  public List<FormattedValueBean> getEngagementRecognitionPromotionsList();

  public List<Promotion> getAllBadges();

  public List<Long> getEligiblePromotionsFromPromoBadgeId( Long promoBadgeId );

  public Promotion savePromotionCelebrationCmText( Promotion promotion, String celebrationDetailsText ) throws ServiceErrorException;

  public Promotion savePromotionCelebrationCmText( Promotion promotion, String celebrationDetailsText, Locale locale ) throws ServiceErrorException;

  public boolean isPromotionNameUnique( String promotionName, Long currentPromotionId );

  public List<CelebrationImageFillerValue> getCelebrationImageFillersForPromotion( Long promotionId );

  public String getPromotionNameByLocale( String promoNameAssetCode, String userLocale );

  public Promotion updatePromotionBudgetSweepAndSavePromotion( Set<PromotionBudgetSweep> promotionBudgetSweeps, Long promotionId, List updateAssociations ) throws UniqueConstraintViolationException;

  public EngagementPromotion getLiveEngagementPromotion();

  public boolean displayPurlCelebrationTile();

  public boolean isRecogPromotionInRPM( Long promotionId );

  public Integer getBadgePromotionCountForPromoId( Long promoId );

  public Integer getPromotionSelfRecognition( Long promoId );

  public List<PromotionsValueBean> getAllSortedApproverPromotions( Long userId, String promotionType );

  public List<BadgeLibrary> buildBadgeLibraryList( Locale locale );

  public List<MerchOrder> getMerchOrdersToGenerateGiftCodeByPromotionIdAndUserId( Long promotionId, Long userId );

  public boolean getMerchOrderByPromotionIdAndUserId( Long promotionId, Long userId );

  public List getPromotionBillCodes( Long promotionId, boolean sweetStakesBillCode );

  CustomFormStepElementsView getStepElements( Long pomotionId );

  // List<CustomFormSelectList> getSelectionList( ClaimFormStepElement element, String
  // selectionPickListName );

  public List getApprovalOptionsByApproverId( Long approverId );

  void populateAddressGroup( CustomFormStepElementsView form, ClaimFormStepElement element );

  List<CustomFormSelectListView> getSelectList( List<PickListItem> stateList, String countryCode );

  /**
   * Get a list of the promotion IDs that have the given behavior selected
   */
  public List<Long> getPromotionIdsForBehavior( String behaviorType );

  public Promotion saveLevelLabelCmText( NominationPromotion promotion ) throws ServiceErrorException, UniqueConstraintViolationException;

  public Promotion savePayoutDescriptionCmText( NominationPromotion promotion ) throws ServiceErrorException, UniqueConstraintViolationException;

  List<NominationApproverValueBean> getCustomApproverList( int optionId );

  List<NominationApproverValueBean> getCustomApproverList( int levelId, Long promotionId );

  public List<NameableBean> getNominationPromotionListForApproverFileLoad();

  public Promotion saveTimePeriodNameCmText( NominationPromotion nominationPromotion ) throws ServiceErrorException, UniqueConstraintViolationException;

  public String getApproverTypeByLevel( Long approvalLevel, Long promotionId );

  public Budget getNominationPromotionAvailableBudgetByAwardType( Promotion promotion, Participant submitter, Node node, String awardType );

  public List<NominationAdminApprovalsBean> getNominationApprovalClaimPromotions();

  public Promotion saveBudgetSegmentNameCmText( Promotion promotion ) throws ServiceErrorException, UniqueConstraintViolationException;

  public Budget getRecognitionAvailableBudget( Long promotionId, Long participantId, Long nodeId, BudgetSegment budgetSegment );

  /**
   * Get the step name of the last step defined for a nomination promotion. 
   * May return null.
   */
  public String getLastWizardStepName( Long promotionId );

  public BigDecimal getTotalUnapprovedAwardQuantity( Long promotionId, Long userId, Long nodeId, Long budgetMasterId );

  public BigDecimal getTotalUnapprovedAwardQuantityPurl( Long promotionId, Long userId, Long nodeId, Long budgetMasterId );

  public long getClaimAwardQuantity( Long claimId );

  public long getTotalImportPaxAwardQuantity( Long importFileId );

  // Alerts Performance Tuning
  public List<AlertsValueBean> getPendingApprovalsForAlerts( List<PromotionBean> liveAndExpiredPromotions, List eligiblePromos, Participant participant, boolean isMessagesPage );

  public List<AlertsValueBean> getGQCPPromotionsForAlerts( List eligiblePromos, Participant pax );

  public List<AlertsValueBean> getGQCPPromotionsSurveyForAlerts( List eligiblePromos, Participant pax );

  public List<AlertsValueBean> getSurveyPromotionsForAlerts( List eligiblePromos, Participant pax );

  public List<AlertsValueBean> getDIYQuizPromotionsForAlerts( List eligiblePromos, Participant pax );

  public List getAwardGeneratorAlertForManager( List eligiblePromotions, Participant participant );

  public List getCelebrationAlertForManager( List eligiblePromotions, Participant participant );

  public List getApproverReminderAlerts( List eligiblePromos, Participant participant );

  public List getNominatorRequestMoreInfoAlerts( Participant participant );

  public List getNominationMyWinnersList( Participant participant );

  // Alerts Performance Tuning
  public List getAwardReminderListForAlerts( List eligiblePromos, Participant participant, Map<Long, Promotion> eligiblePromoMap );

  public List getBudgetEndAlertForAlerts( List eligiblePromos, Participant participant );

  public List<AlertsValueBean> getPendingPurlInvitationsForAlerts( Participant pax );

  public List<AlertsValueBean> getPendingPurlContributionsForAlerts( Participant pax, boolean isModalWindow );

  public List<AlertsValueBean> getPendingViewPurlsForAlerts( Participant pax );

  public List<AlertsValueBean> getPendingFileDownloadsForAlerts( User user );

  public List<AlertsValueBean> getInstantPollForAlerts( User user );

  public List<AlertsValueBean> getCelebrationAlerts( User user );

  public List<AlertsValueBean> getInProgressNominationClaimsCount( List eligiblePromos, Participant participant );

  boolean isEasyPromotion( AbstractRecognitionPromotion promotion );

  public boolean isSSILivePromotionAvailable();

  public boolean checkIfAnyPointsContestsByPaxId( Long userId );

  public Date getUAGoalQuestPromotionStartDate( Long userId );

  public List<String> getAllUniqueBillCodes( Long promotionId );

  public List<RecognitionAdvisorPromotionValueBean> getPromotionListForRA( Long userId, Long receiverId );

  public int deletePromoCard( Long cardId, Long promotionId );

  public List<PromoRecImageData> getNotMigratedPromRecogAvatarData();

  public void updatePromRecAvatar( Long promotionId, String defaultCelebrationAvatar, String defaultCcontributorAavatar );

  public List<String> getNonPurlAndCelebPromotionsName();

  public List getAllLiveEngagementByUserId( Long userId, List<PromotionMenuBean> eligiblePromotions );

  public void updatePromRecCeleAvatar( Long promotionId, String defaultCelebrationAvatar );

  public void updatePromRecContrAvatar( Long promotionId, String defaultContributorAavatar );

  public List<PromoRecPictureData> getNotMigratedPromRecogPictureData();

  public void updateContResPic( Long promotionId );
  
  // Client customizations for WIP #42701 starts
  public boolean isCashPromo( Long promotionId );
  // Client customizations for WIP #42701 ends
  
  public List<AlertsValueBean> getPendingPurlContributionsAlertsForNonDefaultInvitee( Participant pax, boolean isModalWindow );

  public List<AlertsValueBean> getPendingPurlContributionsAlertsForDefaultInvitee( Participant pax, boolean isModalWindow );
  
  // Client customization for WIP #39189 starts
  public Long getPromotionIdByClaimId( Long claimId );
  // Client customization for WIP #39189 ends
  
  // Client customizations for WIP #62128 starts
  public Long getCheersPromotionId();
  // Client customizations for WIP #62128 ends
  
  List getPromotionListWithAssociationsForRecognitions( PromotionQueryConstraint queryConstraint, AssociationRequestCollection associationRequestCollection ); //(home page tuning)

}
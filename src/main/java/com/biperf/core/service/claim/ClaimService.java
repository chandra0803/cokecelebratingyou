/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/claim/ClaimService.java,v $
 */

package com.biperf.core.service.claim;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.sanselan.ImageReadException;

import com.biperf.core.dao.claim.hibernate.ClaimQueryConstraint;
import com.biperf.core.dao.claim.hibernate.JournalClaimQueryConstraint;
import com.biperf.core.dao.claim.hibernate.QuizClaimQueryConstraint;
import com.biperf.core.domain.budget.Budget;
import com.biperf.core.domain.budget.BudgetSegment;
import com.biperf.core.domain.claim.AbstractRecognitionClaim;
import com.biperf.core.domain.claim.Approvable;
import com.biperf.core.domain.claim.Claim;
import com.biperf.core.domain.claim.ClaimElement;
import com.biperf.core.domain.claim.ClaimRecipient;
import com.biperf.core.domain.claim.RecognitionClaim;
import com.biperf.core.domain.enums.ApproverType;
import com.biperf.core.domain.enums.PromotionType;
import com.biperf.core.domain.gamification.BadgeDetails;
import com.biperf.core.domain.hierarchy.Node;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.AbstractRecognitionPromotion;
import com.biperf.core.domain.promotion.CelebrationManagerMessage;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.PromotionSweepstake;
import com.biperf.core.domain.promotion.RecognitionPromotion;
import com.biperf.core.domain.purl.PurlRecipient;
import com.biperf.core.domain.user.User;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.SAO;
import com.biperf.core.service.TranslatedContent;
import com.biperf.core.service.translation.UnexpectedTranslationException;
import com.biperf.core.service.translation.UnsupportedTranslationException;
import com.biperf.core.service.util.ServiceError;
import com.biperf.core.value.ClaimInfoBean;
import com.biperf.core.value.PersonalInfoFileUploadValue;
import com.biperf.core.value.ProductClaimPromotionsValueBean;
import com.biperf.core.value.ProductClaimStatusCountsBean;
import com.biperf.core.value.PromotionApprovableValue;
import com.biperf.core.value.client.TcccClaimFileValueBean;
import com.biperf.core.value.nomination.TranslateCommentViewBean;
import com.biperf.core.value.promotion.CustomFormStepElementsView;

/**
 * ClaimSubmissionService.
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
 * <td>Adam</td>
 * <td>Jun 28, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public interface ClaimService extends SAO
{
  /** BEAN_NAME for referencing in tests and spring config files. */
  public final String BEAN_NAME = "claimService";

  /**
   * Get the ClaimProducts for a given Claim.
   * 
   * @param claimId
   * @return Claim
   */
  public Claim getClaimById( Long claimId );

  /**
   * @param promotionId
   * @param userId
   * @return claimId
   */
  public Long getOpenClaimByPromotionIdAndUserId( Long promotionId, Long userId );

  /**
   * @param promotionId
   * @param userId
   * @param quizId
   * @return
   */
  public Long getOpenClaimByPromotionIdQuizIdAndUserId( Long promotionId, Long userId, Long quizId );

  /**
   * Get claim id for quiz passed by quiz id
   * @param promotionId
   * @param userId
   * @return
   */
  public Long getPassedQuizClaimByPromotionIdAndUserId( Long promotionId, Long userId );

  /**
   * Get claim id for quiz passed
   * @param promotionId
   * @param userId
   * @param quizId
   * @return
   */
  public Long getPassedQuizClaimByPromotionIdQuizIdAndUserId( Long promotionId, Long userId, Long quizId );

  /**
   * Get the ClaimProducts for a given Claim.
   * 
   * @param claimId
   * @param associationRequestCollection
   * @return Claim
   */
  public Claim getClaimByIdWithAssociations( Long claimId, AssociationRequestCollection associationRequestCollection );

  /**
   * Returns a list of claims that meet the specified criteria. Any parameter can be left null so
   * that the query is not constrained by that parameter.
   * 
   * @param claimQueryConstraint
   * @return List the claim list
   */
  public List getClaimList( ClaimQueryConstraint claimQueryConstraint );

  /**
   * Returns a count of claims that meet the specified criteria. Any parameter can be left null so
   * that the query is not constrained by that parameter.
   * 
   * @param claimQueryConstraint
   * @return int the claim list count
   */
  public int getClaimListCount( ClaimQueryConstraint claimQueryConstraint );

  /**
   * Returns a list of claims that meet the specified criteria. Any parameter can be left null so
   * that the query is not constrained by that parameter.
   * 
   * @param claimQueryConstraint
   * @param associationRequestCollection
   * @return List the claim list
   */
  public List getClaimListWithAssociations( ClaimQueryConstraint claimQueryConstraint, AssociationRequestCollection associationRequestCollection );

  /**
   * Returns the claims specified by the given query constraint.
   *
   * @param queryConstraint  the query constraint.
   * @param associationRequestCollection  initializes properties of the returned {@link Claim} objects.
   * @return the specified claims, as a <code>List</code> of {@link Claim} objects.
   */
  public List getClaimList( JournalClaimQueryConstraint queryConstraint, AssociationRequestCollection associationRequestCollection );

  /**
   * Deletes the specified claim.
   * 
   * @param claimId the ID of the claim to delete.
   */
  public void deleteClaim( Long claimId );

  /**
   * Deletes the specified claims.
   * 
   * @param claimIdList the IDs of the claims to delete, as a <code>List</code> of
   *          <code>Long</code> objects.
   */
  public void deleteClaims( List claimIdList );

  /**
   * Saves the specific claim and sends submitter and approver notifications (if applicable).
   * 
   * @param claim the claim to save.
   * @param claimFormStepId required if notifications are to be sent
   * @param approverUser
   * @param forceAutoApprove
   * @throws ServiceErrorException if claim is invalid.
   */
  public Claim saveClaim( Claim claim, Long claimFormStepId, User approverUser, boolean forceAutoApprove, boolean deductBudget ) throws ServiceErrorException;

  // Fix for bug#56006,55519 start
  /**
   * Saves the specific claim - when a budget sweep process is run.
   * 
   * @param claim the claim to save.
   * @param claimFormStepId required if notifications are to be sent
   * @param approverUser
   * @param forceAutoApprove
   * @param deductBudget
   * @param budgetSegment
   * @throws ServiceErrorException if claim is invalid.
   */
  public Claim saveClaim( Claim claim, Long claimFormStepId, User approverUser, boolean forceAutoApprove, boolean deductBudget, BudgetSegment budgetSegment ) throws ServiceErrorException;

  // Fix for bug#56006,55519 end

  /**
   * Saves the specific approvable and sends submitter and approver notifications (if applicable).
   * 
   * @param approvable the approvable to save.
   * @param claimFormStepId required if notifications are to be sent
   * @param approverUser
   * @param forceAutoApprove
   * @throws ServiceErrorException if claim is invalid.
   */
  public void saveApprovable( Approvable approvable, Long claimFormStepId, User approverUser, boolean forceAutoApprove ) throws ServiceErrorException;

  /**
   * Update claims and process any closed claims Overridden from
   * 
   * @param claims
   * @param claimFormStepId
   * @param approverUser
   * @param forceAutoApprove
   * @throws ServiceErrorException
   */
  public void saveClaims( List claims, Long claimFormStepId, User approverUser, boolean forceAutoApprove ) throws ServiceErrorException;

  /**
   * Validates the given claim.
   * 
   * @param claim the claim to be validated.
   * @return a list of validation errors, as a <code>List</code> of {@link ServiceErrorException}
   *         objects.
   */
  public List validateClaim( Claim claim );

  /**
   * Validates the claim elements of the given claim.
   * 
   * @param claim the claim to be validated.
   * @param paxCount
   * @return a list of validation errors, as a <code>List</code> of {@link ServiceErrorException}
   *         objects.
   */
  public List validateClaimElements( Claim claim, int paxCount );

  /**
   * Validates the given claim elements
   * 
   * @param claimElementList the list of claim elements to be validated.
   * @param node the node these claim elements are associated with
   * @param claimElementList the promotion these claim elements are associated with
   * @return a list of validation errors, as a <code>List</code> of {@link ServiceError}
   *         objects.
   */
  public List<ServiceError> validateClaimElements( List<ClaimElement> claimElementList, Node node, Promotion promotion, int paxCount );

  /**
   * This will load all claims which are tied to promotions with delayed claim approval configured,
   * and process them if they are ready.
   */
  public void processDelayedApprovalClaims();

  /**
   * Gets claims for approval by user
   * 
   * @param userId
   * @param includedPromotionIds
   * @param isOpen
   * @param startDate
   * @param endDate
   * @param promotionType
   * @param claimAssociationRequestCollection
   * @param promotionAssociationRequestCollection
   * @param expired
   * @return List of PromotionClaimsValue objects
   */
  public List<PromotionApprovableValue> getClaimsForApprovalByUser( Long userId,
                                                                    Long[] includedPromotionIds,
                                                                    Boolean isOpen,
                                                                    Date startDate,
                                                                    Date endDate,
                                                                    PromotionType promotionType,
                                                                    AssociationRequestCollection claimAssociationRequestCollection,
                                                                    AssociationRequestCollection promotionAssociationRequestCollection,
                                                                    Boolean expired,
                                                                    Boolean allApprovers );

  // Alerts Performance Tuning
  public List<PromotionApprovableValue> getClaimsForApprovalByUser( Long userId,
                                                                    Long[] includedPromotionIds,
                                                                    PromotionType promotionType,
                                                                    AssociationRequestCollection claimAssociationRequestCollection );

  /**
   * Get all claims that the specified approverUserId can approve or approved.
   * This includes closed claims approved by other users when this user is part of approver list
   * @param approverUserId
   * @param includedPromotionIds
   * @param isOpen - if set null, return both open and closed, and return claims already
   * approved by approverUserId
   * @param startDate
   * @param endDate
   * @param promotionType
   * @param claimAssociationRequestCollection
   * @param promotionAssociationRequestCollection
   * @param expired
   * @param sortedBy 
   * @param sortedOn 
   */
  public List<PromotionApprovableValue> getProductClaimsForApprovalByUser( Long approverUserId,
                                                                           Long[] includedPromotionIds,
                                                                           Boolean isOpen,
                                                                           Date startDate,
                                                                           Date endDate,
                                                                           PromotionType promotionType,
                                                                           AssociationRequestCollection claimAssociationRequestCollection,
                                                                           AssociationRequestCollection promotionAssociationRequestCollection,
                                                                           Boolean expired,
                                                                           String sortedOn,
                                                                           String sortedBy,
                                                                           int rowNumStart,
                                                                           int rowNumEnd );

  /**
   * Get pending claim approval count by user id
   * 
   * @param userId
   * @param promotionType
   * @return number of pending claim approvals
   */
  public int getClaimsForApprovalByUserCount( Long userId, PromotionType promotionType );

  public int getNominationClaimsForApprovalByUserCount( Long userId, PromotionType promotionType, Long promotionId );

  /**
   * Tells you how much a claim has earned (uses journal entries)
   * 
   * @param claimId
   * @param userId
   * @return Long
   */
  public Long getEarningsForClaim( Long claimId, Long userId );

  /**
   * Tells you how much a product claim has earned (uses journal entries)
   * 
   * @param claimId
   * @param userId
   * @return Long
   */
  public Long getEarningsForProductClaim( Long claimId, Long userId, Long productId );

  /**
   * Tells you how much a claim has earned (uses journal entries)
   * 
   * @param quizClaimQueryConstraint
   * @param claimAssociationRequestCollection
   * @return Long
   */
  public Map getParticipantQuizClaimHistoryByPromotionMap( QuizClaimQueryConstraint quizClaimQueryConstraint, AssociationRequestCollection claimAssociationRequestCollection );

  /**
   * Process / send closed claim notifications to submitter
   * 
   * @param claim
   */
  public void processClosedClaimNotifications( Claim claim );

  /**
   * Get each open claim of the specified approverType whose submitters node name is not found in
   * the claim promotion's approval hierarchy.
   * 
   * @param approverType
   * @return List
   */
  public List getOpenClaimsWithNoMatchingNodeInApproverHierarchy( ApproverType approverType );

  /**
   * Return false if the recipient has any journal transactions for the claim with status of approve
   * or post
   * 
   * @param recipientId
   * @param claimId
   * @return boolean
   */
  public boolean hasPendingJournalForClaim( Long recipientId, Long claimId );

  /**
   * Returns list of claims
   * 
   * @param promotionId
   * @param eligibilityClaimsType
   * @param promoSweepstake
   * @return List
   */
  public List getProductClaimClaimsList( Long promotionId, String eligibilityClaimsType, PromotionSweepstake promoSweepstake );

  /**
   * Update approvables and process any closed approvables
   * @param approvables
   * @param claimFormStepId
   * @param approverUser
   * @param forceAutoApprove
   * @throws ServiceErrorException
   */
  public void saveApprovables( List approvables, Long claimFormStepId, User approverUser, boolean forceAutoApprove ) throws ServiceErrorException;

  /**
   * Update Nomination approvables and process any closed approvables. Update Budget value with
   * totalAwardChange if budget is in use.
   * 
   * @param approvables
   * @param claimFormStepId
   * @param approverUser
   * @param forceAutoApprove
   * @param promotion
   * @throws ServiceErrorException
   */
  public void saveNominationApprovables( List<Approvable> approvables, Long claimFormStepId, User approverUser, boolean forceAutoApprove, Promotion promotion, String AwardType )
      throws ServiceErrorException;

  public boolean isApprovalDeferred( Approvable approvable );

  /** Save the claims status as Pending, if the budget is overdrawn by looking the remainingBudget's value as negative. 
   * @param claims
   * @param claimFormStepId
   * @param approverUser
   * @param forceAutoApprove
   * @param remainingBudget
   * @throws ServiceErrorException
   */
  public void saveClaims( List claims, Long claimFormStepId, User approverUser, boolean forceAutoApprove, int remainingBudget ) throws ServiceErrorException;

  /**
   * @param claimItems
   * @throws ServiceErrorException
   */
  public void saveClaimItems( List<ClaimRecipient> claimItems ) throws ServiceErrorException;

  /**
   * @param userId
   * @param tabType
   * @param claimId
   */
  public void excecuteOnReversal( String claimId, String promotionType );

  /**
   * Schedules Recognition to be processed on a future date
   * 
   * @param claim
   * @param deliveryDate
   * @throws ServiceErrorException
   */
  public void scheduleRecognition( RecognitionClaim claim, Date deliveryDate ) throws ServiceErrorException;

  /**
   * Schedules Recognitions to be processed on a future date
   * 
   * @param claims
   * @param deliveryDate
   * @throws ServiceErrorException
   */
  public void scheduleRecognitions( List<RecognitionClaim> claims, Date deliveryDate ) throws ServiceErrorException;

  /**
   * Saves the specific claim, sends submitter and approver notifications (if applicable) and updates budget_id on journal table.
   * 
   * @param claim the claim to save.
   * @param claimFormStepId required if notifications are to be sent
   * @param approverUser
   * @param forceAutoApprove
   * @throws ServiceErrorException if claim is invalid.
   */
  public void saveandUpdateRecognitionClaim( Claim claim, Long claimFormStepId, User approverUser, boolean forceAutoApprove, boolean deductBudget, Budget budget ) throws ServiceErrorException;

  public int getPublicRecognitionClaimsSentByUserId( Long promoId, Long paxId, Date startDate, Date endDate, String promotionType, String approvalStatus );

  public int getPublicRecognitionClaimsReceivedbyUserId( Long promoId, Long paxId, Date startDate, Date endDate, String promotionType, String approvalStatus );

  public RecognitionClaimSubmissionResponse submitRecognition( RecognitionClaimSubmission submission ) throws ServiceErrorException;

  public RecognitionClaimSubmissionResponse validate( RecognitionClaimSubmission submission ) throws ServiceErrorException;

  public AbstractRecognitionClaim getRecognitionDetail( Long claimId, boolean includeTeamClaims );

  /**
   * 
   * @return
   */
  public Long getNextTeamId();

  public List<BadgeDetails> getBadgeDetailsFor( Long claimId );

  public Map getParticipantDIYQuizClaimHistoryByQuizMap( QuizClaimQueryConstraint quizClaimQueryConstraint, AssociationRequestCollection claimAssociationRequestCollection );

  public List<ProductClaimPromotionsValueBean> getEligibleProductClaimPromotions( Long userId );

  public int getClaimsApprovedByUserCount( Long approverUserId, PromotionType promotionType );

  public int getUserInClaimsApprovalAudienceCount( Long id, PromotionType lookup );

  public int getUserNominationInClaimApprovalAudienceCount( Long id, PromotionType lookup );

  public TranslatedContent getTranslatedRecognitionClaimSubmitterCommentFor( Long claimId, Long userId ) throws UnsupportedTranslationException, UnexpectedTranslationException;

  public List<ProductClaimStatusCountsBean> getProductClaimStatusCount( Long id );

  public int getProductClaimPromotionTeamMaxCount( Long promotionId );

  public List<Long> getDelayedApprovalClaimIds();

  public List<Participant> getGiversForParticipant( Long participantId, int count );

  public List<Long> getCelebrationClaims( Long participantId );

  public Long getCelebrationClaim( Long participantId );

  public int getEligibleUsersCountForCelebrationModule( Long claimId, Long participantId );

  public boolean displayCelebration( Long claimId, Long userId );

  public CelebrationManagerMessage populateAndSendCelebrationManagerMessages( RecognitionPromotion promotion,
                                                                              RecognitionClaim claim,
                                                                              PurlRecipient purlRecipient,
                                                                              Participant recipient,
                                                                              Date sendDate );

  public List<PromotionApprovableValue> getNominationClaimsForApprovalByUser( Long approverUserId,
                                                                              String claimIds,
                                                                              Long[] includedPromotionIds,
                                                                              Boolean isOpen,
                                                                              Date startDate,
                                                                              Date endDate,
                                                                              PromotionType promotionType,
                                                                              AssociationRequestCollection claimAssociationRequestCollection,
                                                                              AssociationRequestCollection promotionAssociationRequestCollection,
                                                                              Boolean expired,
                                                                              String filterApprovalStatusCode,
                                                                              Long approvalRound );

  Set buildTeamMembers( RecognitionClaimSubmission submission );

  List<RecognitionClaimRecipient> removeDuplicateRecipients( List<RecognitionClaimRecipient> recipients );

  ClaimRecipient buildIndividualClaimRecipient( AbstractRecognitionPromotion promotion, RecognitionClaimRecipient recognitionClaimRecipient );

  void populateClaimElements( Claim claim, CustomFormStepElementsView stepElements );

  public CustomFormStepElementsView getClaimStepElementsWithValue( Long valueOf );

  List<ClaimElement> getClaimElementDomain( CustomFormStepElementsView stepElements );

  public Claim getClaimForPostProcess( Long claimId );

  public TranslateCommentViewBean getTranslatedComments( Long claimId, Long userId ) throws UnsupportedTranslationException, UnexpectedTranslationException;

  public Date getMostRecentWinDate( Long promotionId, Long participantId, Long approvalLevel );

  public List<Long> getClaimIdList( Long submitterId, String approvalStatusType );

  public Claim saveClaim( Claim claim );

  public void saveUserConnection( AbstractRecognitionClaim abstractClaim );

  public Long getMinQualifierId( Long claimId, Long productId );

  boolean pastApprovalExist( Long approverId );

  public List<ClaimInfoBean> getActivityTimePeriod( Long claimId );

  List<AbstractRecognitionClaim> getClaimsByTeamId( Long teamId );

  public String getPromoTimePeriodNameById( Long timePeriodId );

  // For Nomination promotion and for draft, validation is not required. Hence introducing the new
  // method
  public List<ServiceError> validateClaimElements( List<ClaimElement> claimElementList, Node node, Promotion promotion, int paxCount, boolean isDraft );

  public String getDBTimeZone();

  public boolean isCardMapped( Long cardId );
  // Client Customization for WIP #43735 starts
  public void processClaimAward( Long claimId, String awardType ) throws ServiceErrorException;
  // Client Customization for WIP #43735 ends

  // Client customization for WIP 58122
  public boolean processNominationClaimAward( Long claimId, String awardType ) throws ServiceErrorException;
  // Client customization for WIP 58122
  
  // Client Customization for WIP #39189 starts
  public PersonalInfoFileUploadValue uploadClaimFile( PersonalInfoFileUploadValue data ) throws ServiceErrorException, ImageReadException, IOException;

  public void deleteClaimFile( String filePath ) throws ServiceErrorException, ImageReadException, IOException;
  // Client Customization for WIP #39189 ends
  
  // Client customizations for WIP #62128 starts
  public boolean submitCheersRecognition( AbstractRecognitionClaim claims, boolean deductBudget );
  // Client customizations for WIP #62128 ends
  
  // Client customization for WIP #39189 starts
  public List<TcccClaimFileValueBean> getClaimFiles( Long claimId );
  // Client customization for WIP #39189 starts
  /* coke customization start */
  public List<Long> getNomTeamClaimsWithoutJournalForActivityHistoryReceivedTab( Date startDate, Date endDate, Long promotionId, Long recipientId );
  /* coke customization end */
  
}

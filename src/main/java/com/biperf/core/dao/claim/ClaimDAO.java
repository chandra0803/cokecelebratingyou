/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/dao/claim/ClaimDAO.java,v $
 */

package com.biperf.core.dao.claim;

import java.util.Date;
import java.util.List;

import com.biperf.core.dao.DAO;
import com.biperf.core.dao.claim.hibernate.ClaimQueryConstraint;
import com.biperf.core.dao.claim.hibernate.JournalClaimQueryConstraint;
import com.biperf.core.domain.claim.AbstractRecognitionClaim;
import com.biperf.core.domain.claim.Claim;
import com.biperf.core.domain.claim.ClaimElement;
import com.biperf.core.domain.claim.ClaimProductCharacteristic;
import com.biperf.core.domain.claim.ClaimRecipient;
import com.biperf.core.domain.enums.ApproverType;
import com.biperf.core.domain.hierarchy.Node;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.PublicRecognitionUserConnections;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.value.ClaimInfoBean;
import com.biperf.core.value.ProductClaimPromotionsValueBean;
import com.biperf.core.value.ProductClaimStatusCountsBean;
import com.biperf.core.value.PurlContributorValueBean;
import com.biperf.core.value.client.TcccClaimFileValueBean;

/**
 * ClaimDAO will handle processing a claimForm submission.
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
 * <td>crosenquest</td>
 * <td>Jun 28, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 */
public interface ClaimDAO extends DAO
{

  /**
   * Bean name
   */
  public static final String BEAN_NAME = "claimDAO";

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
   * @return List the claim list
   */
  public int getClaimListCount( ClaimQueryConstraint claimQueryConstraint );

  /**
   * Returns the claims specified by the given query constraint.
   *
   * @param queryConstraint  the query constraint.
   * @param associationRequestCollection  initializes properties of the returned {@link Claim} objects.
   * @return the specified claims, as a <code>List</code> of {@link Claim} objects.
   */
  public List getClaimList( JournalClaimQueryConstraint queryConstraint, AssociationRequestCollection associationRequestCollection );

  /**
   * Get claim by claimId
   * 
   * @param claimId
   * @return Claim
   */
  public Claim getClaimById( Long claimId );

  public Long getOpenClaimByPromotionIdAndUserId( Long promotionId, Long userId );

  public Long getOpenClaimByPromotionIdQuizIdAndUserId( Long promotionId, Long userId, Long quizId );

  public Long getPassedQuizClaimByPromotionIdAndUserId( Long promotionId, Long userId );

  public Long getPassedQuizClaimByPromotionIdQuizIdAndUserId( Long promotionId, Long userId, Long quizId );

  /**
   * Get claim by claimId
   * 
   * @param claimId
   * @param associationRequestCollection
   * @return Claim
   */
  public Claim getClaimByIdWithAssociations( Long claimId, AssociationRequestCollection associationRequestCollection );

  /**
   * Deletes the given claim.
   * 
   * @param claim the claim to delete.
   */
  public void deleteClaim( Claim claim );

  /**
   * Update an existing claim
   * 
   * @param claim
   * @return Claim
   */
  public Claim saveClaim( Claim claim );

  /**
   * @param claimElement
   * @param node
   * @param promotion
   * @return boolean
   */
  public boolean isClaimElementValueUniqueWithinNode( ClaimElement claimElement, Node node, Promotion promotion );

  /**
   * @param claimElement
   * @param node
   * @param promotion
   * @return boolean
   */
  public boolean isClaimElementValueUniqueWithinNodeType( ClaimElement claimElement, Node node, Promotion promotion );

  /**
   * @param claimElement
   * @param node
   * @param promotion
   * @return boolean
   */
  public boolean isClaimElementValueUniqueWithinHierarchy( ClaimElement claimElement, Node node, Promotion promotion );

  /**
   * @param claimProductCharacteristic
   * @param promotion
   * @return boolean
   */
  public boolean isClaimProductCharacteristicUnique( ClaimProductCharacteristic claimProductCharacteristic, Promotion promotion );

  /**
   * returns the total earnings for this claim (uses journal entries)
   * 
   * @param claimId
   * @param userId
   * @return Long
   */
  public Long getEarningsForClaim( Long claimId, Long userId );

  /**
   * returns the total earnings for this  product claim (uses journal entries)
   * 
   * @param claimId
   * @param userId
   * @return Long
   */
  public Long getEarningsForProductClaim( Long claimId, Long userId, Long productId );

  /**
   * Return number of claims that have been submitted for the given promotion
   * 
   * @param promotionId
   * @return number of submitted claims
   */
  public int getClaimSubmittedCount( Long promotionId );

  /**
   * Get a list of userIds from CLAIM where promoId matches value passed and submission date is
   * within a given range
   * 
   * @param promoId
   * @param startDate
   * @param endDate
   * @return List
   */
  public List getUserIdsByPromoIdWithinRange( Long promoId, Date startDate, Date endDate );

  public long getUserIdByPromoIdWithinRange( Long promoId, Long paxId, Date startDate, Date endDate );

  /**
   * Get a list of userIds from CLAIM where promoId matches value passed, isOpen matches value
   * passed and submission date is within a given range
   * 
   * @param promoId
   * @param startDate
   * @param endDate
   * @param isOpen
   * @return List
   */
  public List getUserIdsByPromoIdWithinRange( Long promoId, Date startDate, Date endDate, boolean isOpen );

  /**
   * Get a list of userIds from CLAIM where promoId matches value passed and submission date is
   * within a given range and PASS on QUIZ_CLAIM = true
   * 
   * @param promoId
   * @param startDate
   * @param endDate
   * @param pass
   * 
   * @return List
   */
  public List getUserIdsWhoPassedQuizWithinRange( Long promoId, Date startDate, Date endDate, boolean pass );

  /**
   * Get each open claim of the specified approverType whose submitters node name is not found in
   * the claim promotion's approval hierarchy.
   * 
   * @param approverType
   */
  public List getOpenClaimsWithNoMatchingNodeInApproverHierarchy( ApproverType approverType );

  /**
   * Return a count of journal transactions for the recipient and claim with a status of approve or
   * post
   * 
   * @param recipientId
   * @param claimId
   * @return int
   */
  public int getNonPostedJournalCount( Long recipientId, Long claimId );

  /** To Update the Approval Status of ClaimRecipient with native sql query Updation.
   * @param cr
   * @return ClaimRecipient
   */
  public ClaimRecipient saveClaimitem( ClaimRecipient cr );

  /**
   * @param claimRecipientId
   * @return
   */
  public ClaimRecipient getClaimRecipientById( Long claimRecipientId );

  /* Fix 26324 */
  /**
   * Get claimElement by claimElementId
   * 
   * @param claimElementId
   * @return ClaimElement
   */
  public ClaimElement getClaimElementById( Long claimElementId );

  public void excecuteOnReversal( String claimId, String promotionType );

  /**
   * 
   * @param promoId
   * @param paxId
   * @param startDate
   * @param endDate
   * @param promotionType
   * @param approvalStatus
   * @return
   */
  public int getPublicRecognitionClaimsSentByUserId( Long promoId, Long paxId, Date startDate, Date endDate, String promotionType, String approvalStatus );

  /**
   * 
   * @param promoId
   * @param paxId
   * @param startDate
   * @param endDate
   * @param promotionType
   * @param approvalStatus
   * @return
   */
  public int getPublicRecognitionClaimsReceivedbyUserId( Long promoId, Long paxId, Date startDate, Date endDate, String promotionType, String approvalStatus );

  /**
   * 
   * @return
   */
  public Long getNextTeamId();

  /**
   * Return a list of claims that have the same team id as the given claim, including the given claim 
   * 
   * @param claimId
   * @return List<AbstractRecognitionClaim>
   */
  public List<AbstractRecognitionClaim> getTeamClaimsByClaimId( Long claimId );

  /**
   * Return a list of claims that have the given teamId
   * 
   * @param teamId
   * @return List<AbstractRecognitionClaim>
   */
  public List<AbstractRecognitionClaim> getClaimsByTeamId( Long teamId );

  public List<ProductClaimPromotionsValueBean> getEligibleProductClaimPromotions( Long userId );

  public List<ProductClaimStatusCountsBean> getProductClaimStatusCount( Long id );

  public int getProductClaimPromotionTeamMaxCount( Long promotionId );

  public List<Long> getDelayedApprovalClaimIds();

  public List<ClaimRecipient> findMostRecentRecipientsFor( Long submitterId );

  public List<Participant> getGiversForParticipant( Long participantId, int count );

  public List<Long> getCelebrationClaims( Long participantId );

  public int getEligibleUsersCountForCelebrationModule( Long claimId, Long participantId );

  public List<Participant> getAllPaxWhoHaveGivenOrReceivedRecognition( Long userId );

  public Long getExistingTeamIdForClaim( Long promotionId, Long userId );

  public Date getMostRecentWinDate( Long promotionId, Long participantId, Long approvalLevel );

  public List<Participant> getAllPreSelectedContributors( Long recipientId );

  public List<PurlContributorValueBean> getAllExistingContributors( Long userId );

  public List<Long> getClaimIdList( Long submitterId, String approvalStatusType );

  public void saveUserConnection( PublicRecognitionUserConnections publicRecognitionUserConnections );

  public Long getMinQualifierId( Long claimId, Long productId );

  boolean pastApprovalExist( Long approverId );

  public String getClaimIdByApproverAndpromotion( Long approverId, Long promotionId );

  public List<ClaimInfoBean> getActivityTimePeriod( Long claimId );

  public String getPromoTimePeriodNameById( Long timePeriodId );

  public String getDBTimeZone();

  boolean isCardMapped( Long cardId );
  
  // Client customization for WIP #39189 starts
  public List<TcccClaimFileValueBean> getClaimFiles( Long claimId );
  // Client customization for WIP #39189 end
  public List<Long> getNomTeamClaimsWithoutJournalForActivityHistoryReceivedTab( Date startDate, Date endDate, Long promotionId, Long recipientId );
  
}

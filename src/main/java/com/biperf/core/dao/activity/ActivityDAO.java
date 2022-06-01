/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/dao/activity/ActivityDAO.java,v $
 */

package com.biperf.core.dao.activity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Set;

import com.biperf.core.dao.DAO;
import com.biperf.core.dao.activity.hibernate.ActivityQueryConstraint;
import com.biperf.core.domain.activity.Activity;
import com.biperf.core.domain.activity.ManagerOverrideActivity;
import com.biperf.core.domain.activity.NominationActivity;
import com.biperf.core.domain.activity.SalesActivity;
import com.biperf.core.domain.enums.PromotionPayoutType;
import com.biperf.core.service.AssociationRequestCollection;

/*
 * ActivityDAO <p> <b>Change History:</b><br> <table border="1"> <tr> <th>Author</th> <th>Date</th>
 * <th>Version</th> <th>Comments</th> </tr> <tr> <td>OPI Admin</td> <td>Jul 12, 2005</td>
 * <td>1.0</td> <td>created</td> </tr> </table> </p>
 * 
 */

public interface ActivityDAO extends DAO
{

  public static final String BEAN_NAME = "activityDAO";

  /**
   * Save the activity.
   * 
   * @param activity
   * @return SalesActivity
   */
  public Activity saveActivity( Activity activity );

  /**
   * Get activity by activityId.
   * 
   * @param id
   * @return SalesActivity
   */
  public Activity getActivityById( Long id );

  /**
   * Returns a list of activities that meet the specified criteria. Any parameter can be left null
   * so that the query is not constrained by that parameter.
   * 
   * @return List the activity list
   */
  public List getActivityList( ActivityQueryConstraint activityQueryConstraint );

  /**
   * Get all activities.
   * 
   * @return List
   */
  public List getAllActivities();

  /**
   * Get all activities by participant.
   * 
   * @param id
   * @return List
   */
  public List getActivitiesByParticipantId( Long id );

  /**
   * Get all posted Sales activities by participant, claim, and promotion.
   * 
   * @param participantId
   * @param claimId
   * @param promotionId
   * @return List
   */
  public Set getUnpostedSalesActivities( Long participantId, Long claimId, Long promotionId );

  /**
   * Get all posted Recognition activities by participant, claim, and promotion.
   * 
   * @param participantId
   * @param claimId
   * @param promotionId
   * @return List
   */
  public Set getUnpostedRecognitionActivities( Long participantId, Long claimId, Long promotionId );

  /**
   * Get all non-posted Manager Override activities by promotion and date range.
   * 
   * @param promotionId
   * @param startDate
   * @param endDate
   * @return List
   */
  public Set getUnpostedManagerOverrideActivities( Long promotionId, Date startDate, Date endDate );

  /**
   * Get all non-posted Manager Override activities by participant, promotion and date range.
   * 
   * @param promotionId
   * @param participantId
   * @param startDate
   * @param endDate
   * @param payoutType
   * @return List
   */
  public Set getUnpostedManagerOverrideActivitiesByParticipant( Long promotionId, Long participantId, Date startDate, Date endDate, PromotionPayoutType payoutType );

  /**
   * Get all posted carryover activities by participant, and promotion.
   * 
   * @param participantId
   * @param promotionId
   * @return List
   */
  public Set getUnpostedCarryoverActivities( Long participantId, Long promotionId );

  /**
   * Get all activites not listed as posted.
   * 
   * @return List
   */
  public List getAllSalesActivitiesNotPosted();

  /**
   * Get all activites not listed as posted.
   * 
   * @return List
   */
  public List getAllRecognitionActivitiesNotPosted();

  /**
   * Get all SalesActivities for a claim.
   * 
   * @param claimId
   * @param userId
   * @return List
   */
  public List getSalesActivitiesByClaimAndUserId( Long claimId, Long userId );

  /**
   * Get all RecognitionActivities for a claim.
   * 
   * @param claimId
   * @param userId
   * @return List
   */
  public List getRecognitionActivitiesByClaimAndUserId( Long claimId, Long userId );

  /**
   * Get all SalesActivities for a Merchandise Gift code claim.
   * 
   * @param claimId
   * @param userId
   * @return List
   */
  public List getMerchOrderActivitiesByClaimAndUserId( Long claimId, Long userId );

  /**
   * Get all SalesActivities for a Merchandise Gift code claim and process associations
   * 
   * @param claimId
   * @param userId
   * @return List
   */
  public List getMerchOrderActivitiesByClaimAndUserIdWithAssociations( Long claimId, Long userId, AssociationRequestCollection associationRequestCollection );

  /**
   * Get all SalesActivities for a Merchandise Gift code claim and process associations
   * 
   * @param claimId
   * @param userId
   * @return List
   */
  public List getMerchOrderActivitiesByClaimAndUserIdWithAssociations( Long claimId, Long userId, AssociationRequestCollection associationRequestCollection, String val );

  /**
   * Get all ManagerOverrideActivities for a claim.
   * 
   * @param claimId
   * @param userId
   * @return List
   */
  public List getManagerOverrideActivitiesByClaimAndUserId( Long claimId, Long userId );

  /**
   * Get all ManagerOverrideActivities for a claim.
   * 
   * @param claimId
   * @param userId
   * @return List
   */
  public List getQuizActivitiesByClaimAndUserId( Long claimId, Long userId );

  /**
   * Overridden from
   * 
   * @see com.biperf.core.dao.promotion.PromotionDAO#getPromotionByIdWithAssociations(java.lang.Long,
   *      com.biperf.core.service.AssociationRequestCollection)
   * @param id
   * @param associationRequestCollection
   * @return SalesActivity
   */
  public SalesActivity getSalesActivityByIdWithAssociations( Long id, AssociationRequestCollection associationRequestCollection );

  /**
   * @param id
   * @param associationRequestCollection
   * @return ManagerOverrideActivity
   */
  public ManagerOverrideActivity getManagerOverrideActivityByIdWithAssociations( Long id, AssociationRequestCollection associationRequestCollection );

  /**
   * Get all userIds (submitter or receiver) for a promotion during a given time period.
   * 
   * @param promoId
   * @param isSubmitter
   * @param startDate
   * @param endDate
   * @return List
   */
  public List getUserIdsByPromotionDuringTimePeriod( Long promoId, boolean isSubmitter, Date startDate, Date endDate );

  /**
   * Get all userIds who used budget(PAX) for a promotion during a given time period.
   * 
   * @param promoId
   * @param startDate
   * @param endDate
   * @param usedAllBudget
   * @return List
   */
  public List getUserIdsByPromoIdWhoUsedPaxBudgetDuringTimePeriod( Long promoId, Date startDate, Date endDate, boolean usedAllBudget );

  /**
   * Get all userIds who used budget(NODE) for a promotion during a given time period.
   * 
   * @param promoId
   * @param startDate
   * @param endDate
   * @param usedAllBudget
   * @return List
   */
  public List getUserIdsByPromoIdWhoUsedNodeBudgetDuringTimePeriod( Long promoId, Date startDate, Date endDate, boolean usedAllBudget );

  /**
   * Get all userIds who used budget(CENTRAL) for a promotion during a given time period.
   * 
   * @param promoId
   * @param startDate
   * @param endDate
   * @param usedAllBudget
   * @return List
   */
  public List getUserIdsByPromoIdWhoUsedCentralBudgetDuringTimePeriod( Long promoId, Date startDate, Date endDate, boolean usedAllBudget );

  /**
   * Get all userIds who has not used budget(CENTRAL) for a promotion
   * 
   * @param promoId
   * @param startDate
   * @param endDate
   * @param usedAllBudget
   * @return List
   */
  public List getUserIdsByPromoIdWhoNotUsedAllCentralBudget( Long promoId );

  /**
   * Get all userIds who did not use all budget(PAX) for a promotion.
   * 
   * @param promoId
   * @return List
   */
  public List getUserIdsByPromoIdWhoNotUsedAllPaxBudget( Long promoId );

  /**
   * Get all userIds who did not use all budget(NODE) for a promotion.
   * 
   * @param promoId
   * @return List
   */
  public List getUserIdsByPromoIdWhoNotUsedAllNodeBudget( Long promoId );

  /**
   * Get the activity details who has a carry over
   * 
   * @param promotionId
   * @param participantId
   * @param associationRequestCollection
   * @return List
   */
  public List getActivitiesByCarryOverBalance( Long promotionId, Long participantId, AssociationRequestCollection associationRequestCollection );

  /**
   * @param participantId
   * @param approvableId
   * @param promotionId
   * @param isClaimGroup
   */
  public Set getUnpostedNominationActivities( Long participantId, Long approvableId, Long promotionId, boolean isClaimGroup );

  /**
   * @param merchOrderId
   * @return Activity
   */
  public Activity getActivityForMerchOrderId( Long merchOrderId );

  /**
   * @param matchTeamOutcomeId
   * @return boolean
   */
  public boolean getActivityForMatchTeamOutcomeId( Long matchTeamOutcomeId );

  /**
   * @param stackStandingPaxId
   * @return boolean
   */
  public boolean getActivityForStackStandingPaxId( Long stackStandingPaxId );

  /**
   * @param promotionId
   * @param participantId
   * @param startDate
   * @param endDate
   * @return Long
   */
  public Long getMyRecognitionReceived( Long promotionId, Long participantId, String fromDate, String toDate );

  /**
   * @param promotionId
   * @param participantId
   * @param startDate
   * @param endDate
   * @return BigDecimal
   */
  public BigDecimal getRecognitionReceivedAveargeForMyTeam( Long promotionId, Long participantId, String fromDate, String toDate );

  /**
   * @param promotionId
   * @param participantId
   * @param startDate
   * @param endDate
   * @return BigDecimal
   */
  public BigDecimal getRecognitionReceivedAveargeForPromotions( Long promotionId, String fromDate, String toDate );

  /**
   * @param promotionId
   * @param participantId
   * @param startDate
   * @param endDate
   * @return Long
   */
  public Long getMyRecognitionSent( Long promotionId, Long participantId, String fromDate, String toDate );

  /**
   * @param promotionId
   * @param participantId
   * @param startDate
   * @param endDate
   * @return BigDecimal
   */
  public BigDecimal getRecognitionSentAveargeForMyTeam( Long promotionId, Long participantId, String fromDate, String toDate );

  /**
   * @param promotionId
   * @param participantId
   * @param startDate
   * @param endDate
   * @return BigDecimal
   */
  public BigDecimal getRecognitionSentAveargeForPromotions( Long promotionId, String fromDate, String toDate );

  /**
   * @param promotionId
   * @param participantId
   * @param startDate
   * @param endDate
   * @return Long
   */
  public Long getMySumittedNominations( Long promotionId, Long participantId, String fromDate, String toDate );

  /**
   * @param promotionId
   * @param participantId
   * @param startDate
   * @param endDate
   * @return BigDecimal
   */
  public BigDecimal getSumittedNominationsAveargeForMyTeam( Long promotionId, Long participantId, String fromDate, String toDate );

  /**
   * @param promotionId
   * @param participantId
   * @param startDate
   * @param endDate
   * @return BigDecimal
   */
  public BigDecimal getSumittedNominationsAveargeForPromotions( Long promotionId, String fromDate, String toDate );

  /**
   * @param promotionId
   * @param participantId
   * @param startDate
   * @param endDate
   * @return Long
   */
  public Long getMyNominationsReceived( Long promotionId, Long participantId, String fromDate, String toDate );

  /**
   * @param promotionId
   * @param participantId
   * @param startDate
   * @param endDate
   * @return BigDecimal
   */
  public BigDecimal getNominationsReceivedAveargeForMyTeam( Long promotionId, Long participantId, String fromDate, String toDate );

  /**
   * @param promotionId
   * @param participantId
   * @param startDate
   * @param endDate
   * @return BigDecimal
   */
  public BigDecimal getNominationsReceivedAveargeForCompany( Long promotionId, String fromDate, String toDate );

  /**
   * @param promotionId
   * @param participantId
   * @param claimStatus
   * @param startDate
   * @param endDate
   * @return Long
   */
  public Long getMyProductClaimsSubmitted( Long promotionId, Long participantId, String claimStatus, String fromDate, String toDate );

  /**
   * @param promotionId
   * @param participantId
   * @param claimStatus
   * @param startDate
   * @param endDate
   * @return BigDecimal
   */
  public BigDecimal getProductClaimsSubmittedAverageForMyTeam( Long promotionId, Long participantId, String claimStatus, String fromDate, String toDate );

  /**
   * @param promotionId
   * @param participantId
   * @param claimStatus
   * @param startDate
   * @param endDate
   * @return BigDecimal
   */
  public BigDecimal getProductClaimsSubmittedAverageForCompany( Long promotionId, String claimStatus, String fromDate, String toDate );

  public List<Activity> getGQAndCPActivities( Long userId, String promotionIds, String startDate, String endDate, String languageCode );

  public Long getPromotionbyMerchOrderId( Long merchOrderId );

  public String getPromotionNamebyMerchOrderId( Long merchOrderId );

  public Activity getNominationActivityId( Long participantId, Long promotionId, Long id, boolean isCumulative, Long approvalRound );

  public NominationActivity getNominationActivity( Long recipientId, Long promotionId, Long claimId, Long approvalRound );

  public List getSweepActivityForGivers( Long promotionId, Date fromDate, Date toDate );

  public List getSweepActivityForReceivers( Long promotionId, Date fromDate, Date toDate );

  public List getSweepActivityForGiversAndReceivers( Long promotionId, Date fromDate, Date toDate );
  
  // Client customization for WIP #43735 starts
  public Activity getRecipientClaimActivity( Long participantId, Long claimId );
  public Activity getRecipientClaimActivityForNomination( Long participantId, Long claimId );
  // Client customization for WIP #43735 ends
}
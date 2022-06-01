/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/activity/ActivityService.java,v $
 */

package com.biperf.core.service.activity;

import java.math.BigDecimal;
import java.util.List;

import com.biperf.core.dao.activity.hibernate.ActivityQueryConstraint;
import com.biperf.core.domain.activity.Activity;
import com.biperf.core.domain.activity.ManagerOverrideActivity;
import com.biperf.core.domain.activity.NominationActivity;
import com.biperf.core.domain.activity.SalesActivity;
import com.biperf.core.domain.promotion.PromotionSweepstake;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.SAO;

/*
 * ActivityService <p> <b>Change History:</b><br> <table border="1"> <tr> <th>Author</th> <th>Date</th>
 * <th>Version</th> <th>Comments</th> </tr> <tr> <td>OPI Admin</td> <td>Jul 14, 2005</td>
 * <td>1.0</td> <td>created</td> </tr> </table> </p>
 * 
 *
 */

public interface ActivityService extends SAO
{
  /**
   * BEAN_NAME for referencing in tests and spring config files.
   */
  public final String BEAN_NAME = "activityService";

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
   * Returns list of quiz activities
   * 
   * @param promotionId
   * @param pass
   * @param promoSweepstake
   * @return List
   */
  public List getQuizActivityList( Long promotionId, Boolean pass, PromotionSweepstake promoSweepstake );

  /**
   * Returns list of givers recognition activities
   * 
   * @param promotionId
   * @param promoSweepstake
   * @return List
   */
  public List getGiversOnlyRecognitionActivityList( Long promotionId, PromotionSweepstake promoSweepstake );

  /**
   * Returns list of receivers recognition activities
   * 
   * @param promotionId
   * @param promoSweepstake
   * @return List
   */
  public List getReceiversOnlyRecognitionActivityList( Long promotionId, PromotionSweepstake promoSweepstake );

  /**
   * Returns list of givers and receivers recognition activities
   * 
   * @param promotionId
   * @param promoSweepstake
   * @return List
   */
  public List getGiversAndReceiversRecognitionActivityList( Long promotionId, PromotionSweepstake promoSweepstake );

  /**
   * Returns list of recognition activities
   * 
   * @param promotionId
   * @param poolType
   * @param promoSweepstake
   * @return List
   */
  public List getRecognitionActivityList( Long promotionId, Boolean poolType, PromotionSweepstake promoSweepstake );

  /**
   * Get all activities by participant.
   * 
   * @param claimId
   * @param userId
   * @return List
   */
  public List getMerchOrderActivitiesByClaimIdAndUserId( Long claimId, Long userId );

  /**
   * Get all activities by participant.
   * 
   * @param claimId
   * @param userId
   * @return List
   */
  public List getMerchOrderActivitiesByClaimIdAndUserId( Long claimId, Long userId, String val );

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
   * Get all activites not listed as posted.
   * 
   * @return List
   */
  public List getAllActivitiesNotPosted();

  /**
   * @param activities
   * @return List
   */
  public List saveActivities( List activities );

  /**
   * Get all activities by participant.
   * 
   * @param claimId
   * @param userId
   * @return List
   */
  public List getSalesActivitiesByClaimIdAndUserId( Long claimId, Long userId );

  /**
   * Get all activities by participant.
   * 
   * @param claimId
   * @param userId
   * @return List
   */
  public List getRecognitionActivitiesByClaimIdAndUserId( Long claimId, Long userId );

  /**
   * Get all activities by participant.
   * 
   * @param claimId
   * @param userId
   * @return List
   */
  public List getManagerOverrideActivityByClaimIdAndUserId( Long claimId, Long userId );

  /**
   * Get all activities by participant.
   * 
   * @param claimId
   * @param userId
   * @return List
   */
  public List getQuizActivityByClaimIdAndUserId( Long claimId, Long userId );

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.activity.ActivityService#getSalesActivityByIdWithAssociations(java.lang.Long,
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
   * Get all activities based on the constraint.
   * 
   * @param activityQueryConstraint
   * @return List
   */
  public List getActivityList( ActivityQueryConstraint activityQueryConstraint );

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
   * @param merchOrderId
   * @return Activity
   */
  public Activity getActivityForMerchOrderId( Long merchOrderId );

  /**
   * @param promotionId
   * @param participantId
   * @param startDate
   * @param endDate
   * @return List
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

  public List getPCActivityList( Long promotionId, PromotionSweepstake promoSweepstake );

  public List<Activity> getGQAndCPActivities( Long userId, String promotionIds, String startDate, String endDate, String languageCode );

  public Long getPromotionbyMerchOrderId( Long MerchOrderId );

  public String getPromotionNamebyMerchOrderId( Long merchOrderId );

  public Activity getNominationActivityId( Long participantId, Long promotionId, Long id, boolean isCumulative, Long approvalRound );

  public NominationActivity getNominationActivity( Long recipientId, Long promotionId, Long claimId, Long approvalRound );
}

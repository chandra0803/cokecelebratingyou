/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/activity/impl/ActivityServiceImpl.java,v $
 */

package com.biperf.core.service.activity.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.dao.activity.ActivityDAO;
import com.biperf.core.dao.activity.hibernate.ActivityQueryConstraint;
import com.biperf.core.dao.activity.hibernate.QuizActivityQueryConstraint;
import com.biperf.core.dao.activity.hibernate.RecognitionActivityQueryConstraint;
import com.biperf.core.dao.activity.hibernate.SalesActivityQueryConstraint;
import com.biperf.core.domain.activity.Activity;
import com.biperf.core.domain.activity.ManagerOverrideActivity;
import com.biperf.core.domain.activity.NominationActivity;
import com.biperf.core.domain.activity.SalesActivity;
import com.biperf.core.domain.promotion.PromotionSweepstake;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.activity.ActivityService;
import com.biperf.core.service.activity.MerchOrderActivityAssociationRequest;

/*
 * ActivityServiceImpl <p> <b>Change History:</b><br> <table border="1"> <tr> <th>Author</th>
 * <th>Date</th> <th>Version</th> <th>Comments</th> </tr> <tr> <td>OPI Admin</td> <td>Jul
 * 14, 2005</td> <td>1.0</td> <td>created</td> </tr> </table> </p>
 * 
 *
 */
public class ActivityServiceImpl implements ActivityService
{
  private static final Log logger = LogFactory.getLog( ActivityServiceImpl.class );

  private ActivityDAO activityDAO;

  public void setActivityDAO( ActivityDAO activityDAO )
  {
    this.activityDAO = activityDAO;
  }

  /**
   * Save the activity.
   * 
   * @param activity
   * @return SalesActivity
   */
  public Activity saveActivity( Activity activity )
  {
    return this.activityDAO.saveActivity( activity );
  }

  /**
   * Get activity by activityId.
   * 
   * @param id
   * @return SalesActivity
   */
  public Activity getActivityById( Long id )
  {
    return this.activityDAO.getActivityById( id );
  }

  /**
   * Returns list of quiz activities
   * 
   * @param promotionId
   * @param pass
   * @param promoSweepstake
   * @return List
   */
  public List getQuizActivityList( Long promotionId, Boolean pass, PromotionSweepstake promoSweepstake )
  {
    QuizActivityQueryConstraint quizPromoQueryConstraint = new QuizActivityQueryConstraint();
    quizPromoQueryConstraint.setPass( Boolean.TRUE );
    quizPromoQueryConstraint.setPromotionId( promotionId );
    quizPromoQueryConstraint.setStartDate( promoSweepstake.getStartDate() );
    quizPromoQueryConstraint.setEndDate( promoSweepstake.getEndDate() );

    return this.activityDAO.getActivityList( quizPromoQueryConstraint );
  }

  public List getPCActivityList( Long promotionId, PromotionSweepstake promoSweepstake )
  {
    SalesActivityQueryConstraint activityQueryConstraint = new SalesActivityQueryConstraint();
    activityQueryConstraint.setPromotionId( promotionId );
    activityQueryConstraint.setStartDate( promoSweepstake.getStartDate() );
    activityQueryConstraint.setEndDate( promoSweepstake.getEndDate() );

    return this.activityDAO.getActivityList( activityQueryConstraint );
  }

  /**
   * Returns list of receivers recognition activities
   * 
   * @param promotionId
   * @param promoSweepstake
   * @return List
   */
  public List getGiversOnlyRecognitionActivityList( Long promotionId, PromotionSweepstake promoSweepstake )
  {
    return this.getRecognitionActivityListForGivers( promotionId, RecognitionActivityQueryConstraint.GIVERS_ONLY, promoSweepstake );
  }

  /**
   * Returns list of receivers recognition activities
   * 
   * @param promotionId
   * @param promoSweepstake
   * @return List
   */
  public List getReceiversOnlyRecognitionActivityList( Long promotionId, PromotionSweepstake promoSweepstake )
  {
    return this.getRecognitionActivityListForReceivers( promotionId, RecognitionActivityQueryConstraint.RECEIVERS_ONLY, promoSweepstake );
  }

  /**
   * Returns list of givers and receivers recognition activities
   * 
   * @param promotionId
   * @param promoSweepstake
   * @return List
   */
  public List getGiversAndReceiversRecognitionActivityList( Long promotionId, PromotionSweepstake promoSweepstake )
  {
    return this.getRecognitionActivityListForGiversAndReceivers( promotionId, RecognitionActivityQueryConstraint.GIVERS_AND_RECEIVERS, promoSweepstake );
  }

  /**
   * Returns list of recognition activities
   * 
   * @param promotionId
   * @param poolType
   * @param promoSweepstake
   * @return List
   */
  public List getRecognitionActivityList( Long promotionId, Boolean poolType, PromotionSweepstake promoSweepstake )
  {
    RecognitionActivityQueryConstraint recPromoQueryConstraint = new RecognitionActivityQueryConstraint();
    recPromoQueryConstraint.setGiverOrReceiver( poolType );
    recPromoQueryConstraint.setPromotionId( promotionId );
    recPromoQueryConstraint.setStartDate( promoSweepstake.getStartDate() );
    recPromoQueryConstraint.setEndDate( promoSweepstake.getEndDate() );

    return this.activityDAO.getActivityList( recPromoQueryConstraint );
  }

  public List getRecognitionActivityListForGivers( Long promotionId, Boolean poolType, PromotionSweepstake promoSweepstake )
  {

    List activityList = this.activityDAO.getSweepActivityForGivers( promotionId, promoSweepstake.getStartDate(), promoSweepstake.getEndDate() );

    logger.error( "*****Givers poolType=" + poolType + " activityList.size=" + activityList.size() );
    return activityList;
  }

  public List getRecognitionActivityListForReceivers( Long promotionId, Boolean poolType, PromotionSweepstake promoSweepstake )
  {

    List activityList = this.activityDAO.getSweepActivityForReceivers( promotionId, promoSweepstake.getStartDate(), promoSweepstake.getEndDate() );

    logger.error( "*****Receivers poolType=" + poolType + " activityList.size=" + activityList.size() );
    return activityList;
  }

  public List getRecognitionActivityListForGiversAndReceivers( Long promotionId, Boolean poolType, PromotionSweepstake promoSweepstake )
  {

    List activityList = this.activityDAO.getSweepActivityForGiversAndReceivers( promotionId, promoSweepstake.getStartDate(), promoSweepstake.getEndDate() );

    logger.error( "*****GiversAndReceiver poolType=" + poolType + " activityList.size=" + activityList.size() );
    return activityList;
  }

  /**
   * Get all activities within the contraints.
   * 
   * @return List
   */
  public List getActivityList( ActivityQueryConstraint activityQueryConstraint )
  {
    return this.activityDAO.getActivityList( activityQueryConstraint );
  }

  /**
   * Get all activities.
   * 
   * @return List
   */
  public List getAllActivities()
  {
    return this.activityDAO.getAllActivities();
  }

  /**
   * Get all activities by participant.
   * 
   * @param id
   * @return List
   */
  public List getActivitiesByParticipantId( Long id )
  {
    return this.activityDAO.getActivitiesByParticipantId( id );
  }

  /**
   * Get all activities by participant.
   * 
   * @param claimId
   * @param userId
   * @return List
   */
  public List getSalesActivitiesByClaimIdAndUserId( Long claimId, Long userId )
  {
    return this.activityDAO.getSalesActivitiesByClaimAndUserId( claimId, userId );
  }

  /**
   * Get all activities by participant.
   * 
   * @param claimId
   * @param userId
   * @return List
   */
  public List getRecognitionActivitiesByClaimIdAndUserId( Long claimId, Long userId )
  {
    return this.activityDAO.getRecognitionActivitiesByClaimAndUserId( claimId, userId );
  }

  /**
   * Get all activities by participant.
   * 
   * @param claimId
   * @param userId
   * @return List
   */
  public List getMerchOrderActivitiesByClaimIdAndUserId( Long claimId, Long userId )
  {
    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
    associationRequestCollection.add( new MerchOrderActivityAssociationRequest( MerchOrderActivityAssociationRequest.ALL_HYDRATION_LEVEL ) );
    return this.activityDAO.getMerchOrderActivitiesByClaimAndUserIdWithAssociations( claimId, userId, associationRequestCollection );
  }

  /**
   * Get all activities by participant.
   * 
   * @param claimId
   * @param userId
   * @return List
   */
  public List getMerchOrderActivitiesByClaimIdAndUserId( Long claimId, Long userId, String val )
  {
    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
    associationRequestCollection.add( new MerchOrderActivityAssociationRequest( MerchOrderActivityAssociationRequest.ALL_HYDRATION_LEVEL ) );
    return this.activityDAO.getMerchOrderActivitiesByClaimAndUserIdWithAssociations( claimId, userId, associationRequestCollection, null );
  }

  /**
   * Get all activities by participant.
   * 
   * @param claimId
   * @param userId
   * @return List
   */
  public List getManagerOverrideActivityByClaimIdAndUserId( Long claimId, Long userId )
  {
    return this.activityDAO.getManagerOverrideActivitiesByClaimAndUserId( claimId, userId );
  }

  /**
   * Get all activities by participant.
   * 
   * @param claimId
   * @param userId
   * @return List
   */
  public List getQuizActivityByClaimIdAndUserId( Long claimId, Long userId )
  {
    return this.activityDAO.getQuizActivitiesByClaimAndUserId( claimId, userId );
  }

  /**
   * Get all activites not listed as posted.
   * 
   * @return List
   */
  public List getAllActivitiesNotPosted()
  {
    return this.activityDAO.getAllSalesActivitiesNotPosted();
  }

  /**
   * Saves the given activities.
   * 
   * @param activities the activities to be saved, as a <code>List</code> of {@link Activity}
   *          objects.
   * @return the saved activities, as a <code>List</code> of {@link Activity} objects.
   */
  public List saveActivities( List activities )
  {
    List savedActivities = new ArrayList();

    Iterator iter = activities.iterator();
    while ( iter.hasNext() )
    {
      Activity savedActivity = activityDAO.saveActivity( (Activity)iter.next() );
      savedActivities.add( savedActivity );
    }

    return savedActivities;
  }

  /**
   * Overridden from
   * 
   * @param id
   * @param associationRequestCollection
   * @return SalesActivity
   */
  public SalesActivity getSalesActivityByIdWithAssociations( Long id, AssociationRequestCollection associationRequestCollection )
  {
    return this.activityDAO.getSalesActivityByIdWithAssociations( id, associationRequestCollection );
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.activity.ActivityService#getManagerOverrideActivityByIdWithAssociations(java.lang.Long,
   *      com.biperf.core.service.AssociationRequestCollection)
   * @param id
   * @param associationRequestCollection
   * @return ManagerOverrideActivity
   */
  public ManagerOverrideActivity getManagerOverrideActivityByIdWithAssociations( Long id, AssociationRequestCollection associationRequestCollection )
  {
    return activityDAO.getManagerOverrideActivityByIdWithAssociations( id, associationRequestCollection );
  }

  /**
   * Get the activity details who has a carry over
   * 
   * @param promotionId
   * @param participantId
   * @param associationRequestCollection
   * @return List
   */
  public List getActivitiesByCarryOverBalance( Long promotionId, Long participantId, AssociationRequestCollection associationRequestCollection )
  {
    return activityDAO.getActivitiesByCarryOverBalance( promotionId, participantId, associationRequestCollection );
  }

  /**
   * @param merchOrderId
   * @return Activity
   */
  public Activity getActivityForMerchOrderId( Long merchOrderId )
  {
    return activityDAO.getActivityForMerchOrderId( merchOrderId );
  }

  @Override
  public Long getMyRecognitionReceived( Long promotionId, Long participantId, String fromDate, String toDate )
  {
    return activityDAO.getMyRecognitionReceived( promotionId, participantId, fromDate, toDate );
  }

  @Override
  public BigDecimal getRecognitionReceivedAveargeForMyTeam( Long promotionId, Long participantId, String fromDate, String toDate )
  {

    return activityDAO.getRecognitionReceivedAveargeForMyTeam( promotionId, participantId, fromDate, toDate );
  }

  @Override
  public BigDecimal getRecognitionReceivedAveargeForPromotions( Long promotionId, String fromDate, String toDate )
  {
    return activityDAO.getRecognitionReceivedAveargeForPromotions( promotionId, fromDate, toDate );
  }

  @Override
  public Long getMyRecognitionSent( Long promotionId, Long participantId, String fromDate, String toDate )
  {
    return activityDAO.getMyRecognitionSent( promotionId, participantId, fromDate, toDate );
  }

  @Override
  public BigDecimal getRecognitionSentAveargeForMyTeam( Long promotionId, Long participantId, String fromDate, String toDate )
  {
    return activityDAO.getRecognitionSentAveargeForMyTeam( promotionId, participantId, fromDate, toDate );
  }

  @Override
  public BigDecimal getRecognitionSentAveargeForPromotions( Long promotionId, String fromDate, String toDate )
  {
    return activityDAO.getRecognitionSentAveargeForPromotions( promotionId, fromDate, toDate );
  }

  @Override
  public Long getMySumittedNominations( Long promotionId, Long participantId, String fromDate, String toDate )
  {
    return activityDAO.getMySumittedNominations( promotionId, participantId, fromDate, toDate );
  }

  @Override
  public BigDecimal getSumittedNominationsAveargeForMyTeam( Long promotionId, Long participantId, String fromDate, String toDate )
  {

    return activityDAO.getSumittedNominationsAveargeForMyTeam( promotionId, participantId, fromDate, toDate );
  }

  @Override
  public BigDecimal getSumittedNominationsAveargeForPromotions( Long promotionId, String fromDate, String toDate )
  {
    return activityDAO.getSumittedNominationsAveargeForPromotions( promotionId, fromDate, toDate );
  }

  @Override
  public Long getMyNominationsReceived( Long promotionId, Long participantId, String fromDate, String toDate )
  {
    return activityDAO.getMyNominationsReceived( promotionId, participantId, fromDate, toDate );
  }

  @Override
  public BigDecimal getNominationsReceivedAveargeForMyTeam( Long promotionId, Long participantId, String fromDate, String toDate )
  {
    return activityDAO.getNominationsReceivedAveargeForMyTeam( promotionId, participantId, fromDate, toDate );
  }

  @Override
  public BigDecimal getNominationsReceivedAveargeForCompany( Long promotionId, String fromDate, String toDate )
  {
    return activityDAO.getNominationsReceivedAveargeForCompany( promotionId, fromDate, toDate );
  }

  /**
     * @param promotionId
     * @param participantId
     * @param claimStatus
     * @param startDate
     * @param endDate
     * @return Long
     */
  public Long getMyProductClaimsSubmitted( Long promotionId, Long participantId, String claimStatus, String fromDate, String toDate )
  {
    return activityDAO.getMyProductClaimsSubmitted( promotionId, participantId, claimStatus, fromDate, toDate );
  }

  /**
   * @param promotionId
   * @param participantId
   * @param claimStatus
   * @param startDate
   * @param endDate
   * @return BigDecimal
   */
  public BigDecimal getProductClaimsSubmittedAverageForMyTeam( Long promotionId, Long participantId, String claimStatus, String fromDate, String toDate )
  {
    return activityDAO.getProductClaimsSubmittedAverageForMyTeam( promotionId, participantId, claimStatus, fromDate, toDate );
  }

  /**
   * @param promotionId
   * @param participantId
   * @param claimStatus
   * @param startDate
   * @param endDate
   * @return BigDecimal
   */
  public BigDecimal getProductClaimsSubmittedAverageForCompany( Long promotionId, String claimStatus, String fromDate, String toDate )
  {
    return activityDAO.getProductClaimsSubmittedAverageForCompany( promotionId, claimStatus, fromDate, toDate );
  }

  public List<Activity> getGQAndCPActivities( Long userId, String promotionIds, String startDate, String endDate, String languageCode )
  {
    return activityDAO.getGQAndCPActivities( userId, promotionIds, startDate, endDate, languageCode );
  }

  public Long getPromotionbyMerchOrderId( Long MerchOrderId )
  {
    return activityDAO.getPromotionbyMerchOrderId( MerchOrderId );
  }

  public String getPromotionNamebyMerchOrderId( Long merchOrderId )
  {
    return activityDAO.getPromotionNamebyMerchOrderId( merchOrderId );
  }

  @Override
  public Activity getNominationActivityId( Long participantId, Long promotionId, Long id, boolean isCumulative, Long approvalRound )
  {
    return activityDAO.getNominationActivityId( participantId, promotionId, id, isCumulative, approvalRound );
  }

  @Override
  public NominationActivity getNominationActivity( Long recipientId, Long promotionId, Long claimId, Long approvalRound )
  {
    return activityDAO.getNominationActivity( recipientId, promotionId, claimId, approvalRound );
  }
}

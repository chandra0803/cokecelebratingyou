/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/dao/activity/hibernate/ActivityDAOImpl.java,v $
 */

package com.biperf.core.dao.activity.hibernate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.hibernate.Query;
import org.hibernate.Session;

import com.biperf.core.dao.BaseDAO;
import com.biperf.core.dao.BaseResultTransformer;
import com.biperf.core.dao.activity.ActivityDAO;
import com.biperf.core.domain.activity.Activity;
import com.biperf.core.domain.activity.ManagerOverrideActivity;
import com.biperf.core.domain.activity.MerchOrderActivity;
import com.biperf.core.domain.activity.NominationActivity;
import com.biperf.core.domain.activity.SalesActivity;
import com.biperf.core.domain.enums.PromotionPayoutType;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.utils.HibernateSessionManager;
import com.biperf.core.utils.UserManager;
import com.biperf.core.utils.hibernate.HibernateUtil;
import com.biperf.core.value.SweepstakeWinnerPoolValue;

/*
 * ActivityDAOImpl <p> <b>Change History:</b><br> <table border="1"> <tr> <th>Author</th> <th>Date</th>
 * <th>Version</th> <th>Comments</th> </tr> <tr> <td>OPI Admin</td> <td>Jul 13, 2005</td>
 * <td>1.0</td> <td>created</td> </tr> </table> </p>
 * 
 */

public class ActivityDAOImpl extends BaseDAO implements ActivityDAO
{

  private static String sweepSqlTop = "select ac.activity_id, ac.user_id, ac.is_submitter " + "from activity ac, application_user au, participant p " + "where ac.promotion_id = :promotionId "
      + "and ac.user_id = au.user_id " + "and au.user_id = p.user_id " + "and TRUNC(ac.submission_date) between TRUNC(:fromDate) and TRUNC(:toDate) ";

  private static String sweepSqlMiddleGiver = "and ac.is_submitter = 1 ";

  private static String sweepSqlMiddleReceiver = "and ac.is_submitter = 0 ";

  /**
   * Save the activity.
   * 
   * @param activity
   * @return SalesActivity
   */
  public Activity saveActivity( Activity activity )
  {
    return (Activity)HibernateUtil.saveOrUpdateOrShallowMerge( activity );
  }

  /**
   * Get activity by activityId.
   * 
   * @param id
   * @return SalesActivity
   */
  public Activity getActivityById( Long id )
  {
    return (Activity)getSession().get( Activity.class, id );
  }

  /**
   * Get all activities.
   * 
   * @return List
   */
  public List getAllActivities()
  {
    return getSession().createCriteria( Activity.class ).list();
  }

  /**
   * Returns a list of activities that meet the specified criteria. Any parameter can be left null
   * so that the query is not constrained by that parameter.
   * 
   * @return List the activity list
   */
  public List getActivityList( ActivityQueryConstraint activityQueryConstraint )
  {
    return HibernateUtil.getObjectList( activityQueryConstraint );
  }

  /**
   * Get all activities by participant.
   * 
   * @param participantId
   * @return List
   */
  public List getActivitiesByParticipantId( Long participantId )
  {
    SalesActivityQueryConstraint activityQueryConstraint = new SalesActivityQueryConstraint();
    activityQueryConstraint.setParticipantId( participantId );

    return getActivityList( activityQueryConstraint );
  }

  /**
   * Get all activites not listed as posted.
   * 
   * @return List
   */
  public List getAllSalesActivitiesNotPosted()
  {
    SalesActivityQueryConstraint activityQueryConstraint = new SalesActivityQueryConstraint();
    activityQueryConstraint.setPosted( Boolean.FALSE );

    return getActivityList( activityQueryConstraint );
  }

  /**
   * Get all activites not listed as posted.
   * 
   * @return List
   */
  public List getAllRecognitionActivitiesNotPosted()
  {
    RecognitionActivityQueryConstraint activityQueryConstraint = new RecognitionActivityQueryConstraint();
    activityQueryConstraint.setPosted( Boolean.FALSE );

    return getActivityList( activityQueryConstraint );
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.dao.activity.ActivityDAO#getUnpostedSalesActivities(java.lang.Long,
   *      java.lang.Long, java.lang.Long)
   * @param participantId
   * @param claimId
   * @param promotionId
   * @return Set of activities
   */
  public Set getUnpostedSalesActivities( Long participantId, Long claimId, Long promotionId )
  {
    SalesActivityQueryConstraint activityQueryConstraint = new SalesActivityQueryConstraint();
    activityQueryConstraint.setClaimId( claimId );
    activityQueryConstraint.setParticipantId( participantId );
    activityQueryConstraint.setPromotionId( promotionId );
    activityQueryConstraint.setPosted( Boolean.FALSE );

    return new LinkedHashSet( getActivityList( activityQueryConstraint ) );

  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.dao.activity.ActivityDAO#getUnpostedRecognitionActivities(java.lang.Long,
   *      java.lang.Long, java.lang.Long)
   * @param participantId
   * @param claimId
   * @param promotionId
   * @return Set of RecognitionActivities
   */
  public Set getUnpostedRecognitionActivities( Long participantId, Long claimId, Long promotionId )
  {
    RecognitionActivityQueryConstraint activityQueryConstraint = new RecognitionActivityQueryConstraint();
    activityQueryConstraint.setPromotionId( promotionId );
    activityQueryConstraint.setParticipantId( participantId );
    activityQueryConstraint.setClaimId( claimId );
    activityQueryConstraint.setPosted( Boolean.FALSE );

    return new LinkedHashSet( getActivityList( activityQueryConstraint ) );
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.dao.activity.ActivityDAO#getUnpostedManagerOverrideActivities(java.lang.Long,
   *      java.util.Date, java.util.Date)
   * @param promotionId
   * @param startDate
   * @param endDate
   * @return Set
   */
  public Set getUnpostedManagerOverrideActivities( Long promotionId, Date startDate, Date endDate )
  {
    ManagerOverrideActivityQueryConstraint activityQueryConstraint = new ManagerOverrideActivityQueryConstraint();
    activityQueryConstraint.setPromotionId( promotionId );
    activityQueryConstraint.setPosted( Boolean.FALSE );
    activityQueryConstraint.setStartDate( startDate );
    activityQueryConstraint.setEndDate( endDate );

    return new LinkedHashSet( getActivityList( activityQueryConstraint ) );
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.dao.activity.ActivityDAO#getUnpostedManagerOverrideActivitiesByParticipant(java.lang.Long,
   *      java.lang.Long, java.util.Date, java.util.Date)
   * @param promotionId
   * @param participantId
   * @param startDate
   * @param endDate
   * @param payoutType
   * @return Set
   */
  public Set getUnpostedManagerOverrideActivitiesByParticipant( Long promotionId, Long participantId, Date startDate, Date endDate, PromotionPayoutType payoutType )
  {
    ManagerOverrideActivityQueryConstraint activityQueryConstraint = new ManagerOverrideActivityQueryConstraint();
    activityQueryConstraint.setPromotionId( promotionId );
    activityQueryConstraint.setParticipantId( participantId );
    activityQueryConstraint.setPosted( Boolean.FALSE );
    activityQueryConstraint.setStartDate( startDate );
    activityQueryConstraint.setEndDate( endDate );
    // this doesn't apply to StackRank or CrossSell payout types
    if ( payoutType == null || !payoutType.getCode().equals( PromotionPayoutType.CROSS_SELL ) && !payoutType.getCode().equals( PromotionPayoutType.STACK_RANK ) )
    {
      activityQueryConstraint.setStatusMinimumQualifierMet( Boolean.TRUE );
    }

    return new LinkedHashSet( getActivityList( activityQueryConstraint ) );
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.dao.activity.ActivityDAO#getUnpostedCarryoverActivities(java.lang.Long,
   *      java.lang.Long)
   * @param participantId
   * @param promotionId
   * @return List
   */
  public Set getUnpostedCarryoverActivities( Long participantId, Long promotionId )
  {
    SalesActivityQueryConstraint activityQueryConstraint = new SalesActivityQueryConstraint();
    activityQueryConstraint.setPromotionId( promotionId );
    activityQueryConstraint.setParticipantId( participantId );
    activityQueryConstraint.setPosted( Boolean.FALSE );
    activityQueryConstraint.setCarryover( Boolean.TRUE );

    return new LinkedHashSet( getActivityList( activityQueryConstraint ) );

  }

  /**
   * Get all SalesActivities for a claim.
   * 
   * @param claimId
   * @param userId
   * @return List
   */
  public List getRecognitionActivitiesByClaimAndUserId( Long claimId, Long userId )
  {
    RecognitionActivityQueryConstraint recognitionActivityQueryConstraint = new RecognitionActivityQueryConstraint();
    recognitionActivityQueryConstraint.setClaimId( claimId );
    recognitionActivityQueryConstraint.setParticipantId( userId );

    return getActivityList( recognitionActivityQueryConstraint );
  }

  /**
   * Get all SalesActivities for a Merchandise Gift code claim.
   * 
   * @param claimId
   * @param userId
   * @return List
   */
  public List getMerchOrderActivitiesByClaimAndUserId( Long claimId, Long userId )
  {
    return getMerchOrderActivitiesByClaimAndUserIdWithAssociations( claimId, userId, null );
  }

  /**
   * Get all SalesActivities for a Merchandise Gift code claim and process associations
   * 
   * @param claimId
   * @param userId
   * @return List
   */
  public List getMerchOrderActivitiesByClaimAndUserIdWithAssociations( Long claimId, Long userId, AssociationRequestCollection associationRequestCollection )
  {
    MerchOrderActivityQueryConstraint merchOrderActivityQueryConstraint = new MerchOrderActivityQueryConstraint();
    merchOrderActivityQueryConstraint.setGiverOrReceiver( MerchOrderActivityQueryConstraint.RECEIVERS_ONLY );
    merchOrderActivityQueryConstraint.setClaimId( claimId );
    merchOrderActivityQueryConstraint.setParticipantId( userId );
    List activityList = new ArrayList();
    activityList = getActivityList( merchOrderActivityQueryConstraint );

    for ( Iterator iter = activityList.iterator(); iter.hasNext(); )
    {
      MerchOrderActivity merchOrderActivity = (MerchOrderActivity)iter.next();
      if ( associationRequestCollection != null )
      {
        associationRequestCollection.process( merchOrderActivity );
      }
    }
    return activityList;
  }

  /**
   * Get all SalesActivities for a Merchandise Gift code claim and process associations
   * 
   * @param claimId
   * @param userId
   * @return List
   */
  public List getMerchOrderActivitiesByClaimAndUserIdWithAssociations( Long claimId, Long userId, AssociationRequestCollection associationRequestCollection, String val )
  {
    MerchOrderActivityQueryConstraint merchOrderActivityQueryConstraint = new MerchOrderActivityQueryConstraint();
    merchOrderActivityQueryConstraint.setGiverOrReceiver( MerchOrderActivityQueryConstraint.GIVERS_AND_RECEIVERS );
    merchOrderActivityQueryConstraint.setClaimId( claimId );
    merchOrderActivityQueryConstraint.setParticipantId( userId );
    List activityList = new ArrayList();
    activityList = getActivityList( merchOrderActivityQueryConstraint );

    for ( Iterator iter = activityList.iterator(); iter.hasNext(); )
    {
      MerchOrderActivity merchOrderActivity = (MerchOrderActivity)iter.next();
      if ( associationRequestCollection != null )
      {
        associationRequestCollection.process( merchOrderActivity );
      }
    }
    return activityList;
  }

  /**
   * Get all SalesActivities for a claim.
   * 
   * @param claimId
   * @param userId
   * @return List
   */
  public List getSalesActivitiesByClaimAndUserId( Long claimId, Long userId )
  {
    SalesActivityQueryConstraint activityQueryConstraint = new SalesActivityQueryConstraint();
    activityQueryConstraint.setClaimId( claimId );
    activityQueryConstraint.setParticipantId( userId );

    return getActivityList( activityQueryConstraint );
  }

  /**
   * Get all ManagerOverrideActivities for a claim.
   * 
   * @param claimId
   * @param userId
   * @return List
   */
  public List getManagerOverrideActivitiesByClaimAndUserId( Long claimId, Long userId )
  {
    ManagerOverrideActivityQueryConstraint activityQueryConstraint = new ManagerOverrideActivityQueryConstraint();
    activityQueryConstraint.setClaimId( claimId );
    activityQueryConstraint.setParticipantId( userId );

    return getActivityList( activityQueryConstraint );
  }

  /**
   * Get all QuizActivities for a claim.
   * 
   * @param claimId
   * @param userId
   * @return List
   */
  public List getQuizActivitiesByClaimAndUserId( Long claimId, Long userId )
  {
    QuizActivityQueryConstraint activityQueryConstraint = new QuizActivityQueryConstraint();
    activityQueryConstraint.setClaimId( claimId );
    activityQueryConstraint.setParticipantId( userId );

    return getActivityList( activityQueryConstraint );
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.dao.promotion.PromotionDAO#getPromotionByIdWithAssociations(java.lang.Long,
   *      com.biperf.core.service.AssociationRequestCollection)
   * @param id
   * @param associationRequestCollection
   * @return SalesActivity
   */
  public SalesActivity getSalesActivityByIdWithAssociations( Long id, AssociationRequestCollection associationRequestCollection )
  {
    Session session = HibernateSessionManager.getSession();
    SalesActivity salesActivity = (SalesActivity)session.get( SalesActivity.class, id );

    if ( associationRequestCollection != null )
    {
      associationRequestCollection.process( salesActivity );
    }

    return salesActivity;
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.dao.activity.ActivityDAO#getManagerOverrideActivityByIdWithAssociations(java.lang.Long,
   *      com.biperf.core.service.AssociationRequestCollection)
   * @param id
   * @param associationRequestCollection
   * @return ManagerOverrideActivity
   */
  public ManagerOverrideActivity getManagerOverrideActivityByIdWithAssociations( Long id, AssociationRequestCollection associationRequestCollection )
  {
    Session session = HibernateSessionManager.getSession();
    ManagerOverrideActivity managerOverrideActivity = (ManagerOverrideActivity)session.get( ManagerOverrideActivity.class, id );

    if ( associationRequestCollection != null )
    {
      associationRequestCollection.process( managerOverrideActivity );
    }

    return managerOverrideActivity;
  }

  /**
   * Get all userIds (submitter or receiver) for a promotion during a given time period.
   * 
   * @param promoId
   * @param isSubmitter
   * @param startDate
   * @param endDate
   * @return List
   */
  public List getUserIdsByPromotionDuringTimePeriod( Long promoId, boolean isSubmitter, Date startDate, Date endDate )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.activity.UserIdsByPromotionDuringPeriod" );

    query.setParameter( "promoId", promoId );
    query.setParameter( "isSubmitter", new Boolean( isSubmitter ) );
    query.setParameter( "startDate", startDate );
    query.setParameter( "endDate", endDate );

    return query.list();
  }

  /**
   * Get all userIds who used budget(PAX) for a promotion during a given time period.
   * 
   * @param promoId
   * @param startDate
   * @param endDate
   * @param usedAllBudget
   * @return List
   */
  public List getUserIdsByPromoIdWhoUsedPaxBudgetDuringTimePeriod( Long promoId, Date startDate, Date endDate, boolean usedAllBudget )
  {
    Query query = null;
    if ( usedAllBudget )
    {
      query = getSession().getNamedQuery( "com.biperf.core.domain.activity.UserIdsUsingAllPaxBudgetByPromotionDuringPeriod" );
    }
    else
    {
      query = getSession().getNamedQuery( "com.biperf.core.domain.activity.UserIdsUsingPaxBudgetByPromotionDuringPeriod" );
    }

    query.setParameter( "promoId", promoId );
    query.setParameter( "startDate", startDate );
    query.setParameter( "endDate", endDate );

    return query.list();
  }

  /**
   * Get all userIds who used budget(NODE) for a promotion during a given time period.
   * 
   * @param promoId
   * @param startDate
   * @param endDate
   * @param usedAllBudget
   * @return List
   */
  public List getUserIdsByPromoIdWhoUsedNodeBudgetDuringTimePeriod( Long promoId, Date startDate, Date endDate, boolean usedAllBudget )
  {
    Query query = null;
    if ( usedAllBudget )
    {
      query = getSession().getNamedQuery( "com.biperf.core.domain.activity.UserIdsUsingAllNodeBudgetByPromotionDuringPeriod" );
    }
    else
    {
      query = getSession().getNamedQuery( "com.biperf.core.domain.activity.UserIdsUsingNodeBudgetByPromotionDuringPeriod" );
    }

    query.setParameter( "promoId", promoId );
    query.setParameter( "startDate", startDate );
    query.setParameter( "endDate", endDate );

    return query.list();
  }

  /**
   * Get all userIds who used budget(CENTRAL) for a promotion during a given time period.
   * 
   * @param promoId
   * @param startDate
   * @param endDate
   * @param usedAllBudget
   * @return List
   */
  public List getUserIdsByPromoIdWhoUsedCentralBudgetDuringTimePeriod( Long promoId, Date startDate, Date endDate, boolean usedAllBudget )
  {
    Query query = null;
    if ( usedAllBudget )
    {
      query = getSession().getNamedQuery( "com.biperf.core.domain.activity.UserIdsUsingAllCentralBudgetByPromotionDuringPeriod" );
    }
    else
    {
      query = getSession().getNamedQuery( "com.biperf.core.domain.activity.UserIdsUsingCentralBudgetByPromotionDuringPeriod" );
    }

    query.setParameter( "promoId", promoId );
    query.setParameter( "startDate", startDate );
    query.setParameter( "endDate", endDate );

    return query.list();
  }

  /**
   * Get all userIds who has not used budget(CENTRAL) for a promotion
   * 
   * @param promoId
   * @param startDate
   * @param endDate
   * @param usedAllBudget
   * @return List
   */
  public List getUserIdsByPromoIdWhoNotUsedAllCentralBudget( Long promoId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.activity.UserIdsNotUsingAllCentralBudgetByPromotion" );
    query.setParameter( "promoId", promoId );
    return query.list();
  }

  /**
   * Get all userIds who did not use all budget(PAX) for a promotion.
   * 
   * @param promoId
   * @return List
   */
  public List getUserIdsByPromoIdWhoNotUsedAllPaxBudget( Long promoId )
  {
    Query query = null;
    query = getSession().getNamedQuery( "com.biperf.core.domain.activity.UserIdsNotUsingAllPaxBudgetByPromotion" );

    query.setParameter( "promoId", promoId );

    return query.list();
  }

  /**
   * Get all userIds who did not use all budget(NODE) for a promotion.
   * 
   * @param promoId
   * @return List
   */
  public List getUserIdsByPromoIdWhoNotUsedAllNodeBudget( Long promoId )
  {
    Query query = null;
    query = getSession().getNamedQuery( "com.biperf.core.domain.activity.UserIdsNotUsingAllNodeBudgetByPromotion" );

    query.setParameter( "promoId", promoId );

    return query.list();
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

    SalesActivityQueryConstraint activityQueryConstraint = new SalesActivityQueryConstraint();
    activityQueryConstraint.setPromotionId( promotionId );
    activityQueryConstraint.setParticipantId( participantId );
    activityQueryConstraint.setCarryover( Boolean.TRUE );
    activityQueryConstraint.setPosted( Boolean.FALSE );

    List activitiesList = getActivityList( activityQueryConstraint );

    SalesActivity salesActivity = null;

    for ( int i = 0; i < activitiesList.size(); i++ )
    {
      salesActivity = (SalesActivity)activitiesList.get( i );
      associationRequestCollection.process( salesActivity );
    }

    return activitiesList;
  }

  /**
   * Overridden from @see com.biperf.core.dao.activity.ActivityDAO#getUnpostedNominationActivities(java.lang.Long, java.lang.Long, java.lang.Long, boolean)
   * @param participantId
   * @param approvableId
   * @param promotionId
   * @param isClaimGroup
   */
  public Set getUnpostedNominationActivities( Long participantId, Long approvableId, Long promotionId, boolean isClaimGroup )
  {
    NominationActivityQueryConstraint activityQueryConstraint = new NominationActivityQueryConstraint();
    activityQueryConstraint.setPromotionId( promotionId );
    activityQueryConstraint.setParticipantId( participantId );
    if ( isClaimGroup )
    {
      activityQueryConstraint.setClaimGroupId( approvableId );
    }
    else
    {
      activityQueryConstraint.setClaimId( approvableId );
    }
    activityQueryConstraint.setPosted( Boolean.FALSE );

    return new LinkedHashSet( getActivityList( activityQueryConstraint ) );
  }

  public Activity getNominationActivityId( Long participantId, Long promotionId, Long id, boolean isCumulative, Long approvalRound )
  {
    NominationActivityQueryConstraint activityQueryConstraint = new NominationActivityQueryConstraint();
    activityQueryConstraint.setPromotionId( promotionId );
    activityQueryConstraint.setParticipantId( participantId );
    activityQueryConstraint.setApprovalRound( approvalRound );
    if ( isCumulative )
    {
      activityQueryConstraint.setClaimGroupId( id );
    }
    else
    {
      activityQueryConstraint.setClaimId( id );
    }
    Activity activity = null;
    if ( getActivityList( activityQueryConstraint ) != null && getActivityList( activityQueryConstraint ).size() > 0 )
    {
      activity = (Activity)getActivityList( activityQueryConstraint ).get( 0 );
      return activity;
    }
    else
    {
      return null;
    }
  }

  public Activity getActivityForMerchOrderId( Long merchOrderId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.activity.activityByMerchOrderId" );
    query.setParameter( "merchOrderId", merchOrderId );
    List results = query.list();
    if ( results != null && !results.isEmpty() )
    {
      return (Activity)results.get( 0 );
    }
    return null;
  }

  public boolean getActivityForMatchTeamOutcomeId( Long matchTeamOutcomeId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.activity.activityByMatchTeamOutcomeId" );
    query.setParameter( "matchTeamOutcomeId", matchTeamOutcomeId );
    return (Integer)query.uniqueResult() > 0;
  }

  public boolean getActivityForStackStandingPaxId( Long stackStandingPaxId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.activity.activityByStackStandingPaxId" );
    query.setParameter( "stackStandingPaxId", stackStandingPaxId );
    List results = query.list();
    return (Integer)query.uniqueResult() > 0;
  }

  public String getUserLocale()
  {
    Locale locale = UserManager.getLocale();

    String userLocale = locale.toString();

    return userLocale;
  }

  @Override
  public Long getMyRecognitionReceived( Long promotionId, Long participantId, String fromDate, String toDate )
  {
    Query query = null;
    query = getSession().getNamedQuery( "com.biperf.core.domain.activity.MyRecognitionReceived" );
    query.setParameter( "promotionId", promotionId );
    query.setParameter( "participantId", participantId );
    query.setParameter( "fromDate", fromDate );
    query.setParameter( "toDate", toDate );
    query.setParameter( "languageCode", getUserLocale() );
    List results = query.list();
    if ( null == results || results.isEmpty() || results.get( 0 ) == null )
    {
      return new Long( 0 );
    }
    else
    {
      return ( (Long)results.get( 0 ) ).longValue();
    }
  }

  @Override
  public BigDecimal getRecognitionReceivedAveargeForMyTeam( Long promotionId, Long participantId, String fromDate, String toDate )
  {
    Query query = null;
    query = getSession().getNamedQuery( "com.biperf.core.domain.activity.RecognitionReceivedAveargeForMyTeam" );
    query.setParameter( "promotionId", promotionId );
    query.setParameter( "participantId", participantId );
    query.setParameter( "fromDate", fromDate );
    query.setParameter( "toDate", toDate );
    query.setParameter( "languageCode", getUserLocale() );
    List results = query.list();
    if ( null == results || results.isEmpty() || results.get( 0 ) == null )
    {
      return new BigDecimal( 0 );
    }
    else
    {
      return ( (BigDecimal)results.get( 0 ) ).abs();
    }
  }

  @Override
  public BigDecimal getRecognitionReceivedAveargeForPromotions( Long promotionId, String fromDate, String toDate )
  {
    Query query = null;
    query = getSession().getNamedQuery( "com.biperf.core.domain.activity.RecognitionReceivedAveargeForPromotions" );
    query.setParameter( "promotionId", promotionId );
    query.setParameter( "fromDate", fromDate );
    query.setParameter( "toDate", toDate );
    query.setParameter( "languageCode", getUserLocale() );
    List results = query.list();
    if ( null == results || results.isEmpty() || results.get( 0 ) == null )
    {
      return new BigDecimal( 0 );
    }
    else
    {
      return ( (BigDecimal)results.get( 0 ) ).abs();
    }
  }

  @Override
  public Long getMyRecognitionSent( Long promotionId, Long participantId, String fromDate, String toDate )
  {
    Query query = null;
    query = getSession().getNamedQuery( "com.biperf.core.domain.activity.MyRecognitionSent" );
    query.setParameter( "promotionId", promotionId );
    query.setParameter( "participantId", participantId );
    query.setParameter( "fromDate", fromDate );
    query.setParameter( "toDate", toDate );
    query.setParameter( "languageCode", getUserLocale() );
    List results = query.list();
    if ( null == results || results.isEmpty() || results.get( 0 ) == null )
    {
      return new Long( 0 );
    }
    else
    {
      return ( (Long)results.get( 0 ) ).longValue();
    }
  }

  @Override
  public BigDecimal getRecognitionSentAveargeForMyTeam( Long promotionId, Long participantId, String fromDate, String toDate )
  {
    Query query = null;
    query = getSession().getNamedQuery( "com.biperf.core.domain.activity.RecognitionSentAveargeForMyTeam" );
    query.setParameter( "promotionId", promotionId );
    query.setParameter( "participantId", participantId );
    query.setParameter( "fromDate", fromDate );
    query.setParameter( "toDate", toDate );
    query.setParameter( "languageCode", getUserLocale() );
    List results = query.list();
    if ( null == results || results.isEmpty() || results.get( 0 ) == null )
    {
      return new BigDecimal( 0 );
    }
    else
    {
      return ( (BigDecimal)results.get( 0 ) ).abs();
    }
  }

  @Override
  public BigDecimal getRecognitionSentAveargeForPromotions( Long promotionId, String fromDate, String toDate )
  {
    Query query = null;
    query = getSession().getNamedQuery( "com.biperf.core.domain.activity.RecognitionSentAveargeForPromotions" );
    query.setParameter( "promotionId", promotionId );
    query.setParameter( "fromDate", fromDate );
    query.setParameter( "toDate", toDate );
    query.setParameter( "languageCode", getUserLocale() );
    List results = query.list();
    if ( null == results || results.isEmpty() || results.get( 0 ) == null )
    {
      return new BigDecimal( 0 );
    }
    else
    {
      return ( (BigDecimal)results.get( 0 ) ).abs();
    }
  }

  @Override
  public Long getMySumittedNominations( Long promotionId, Long participantId, String fromDate, String toDate )
  {
    Query query = null;
    query = getSession().getNamedQuery( "com.biperf.core.domain.activity.MySubmittedNominations" );
    query.setParameter( "promotionId", promotionId );
    query.setParameter( "participantId", participantId );
    query.setParameter( "fromDate", fromDate );
    query.setParameter( "toDate", toDate );
    query.setParameter( "languageCode", getUserLocale() );
    List results = query.list();
    if ( null == results || results.isEmpty() || results.get( 0 ) == null )
    {
      return new Long( 0 );
    }
    else
    {
      return ( (Long)results.get( 0 ) ).longValue();
    }
  }

  @Override
  public BigDecimal getSumittedNominationsAveargeForMyTeam( Long promotionId, Long participantId, String fromDate, String toDate )
  {
    Query query = null;
    query = getSession().getNamedQuery( "com.biperf.core.domain.activity.SubmittedNominationsAveargeForMyTeam" );
    query.setParameter( "promotionId", promotionId );
    query.setParameter( "participantId", participantId );
    query.setParameter( "fromDate", fromDate );
    query.setParameter( "toDate", toDate );
    query.setParameter( "languageCode", getUserLocale() );
    List results = query.list();
    if ( null == results || results.isEmpty() || results.get( 0 ) == null )
    {
      return new BigDecimal( 0 );
    }
    else
    {
      return ( (BigDecimal)results.get( 0 ) ).abs();
    }
  }

  @Override
  public BigDecimal getSumittedNominationsAveargeForPromotions( Long promotionId, String fromDate, String toDate )
  {
    Query query = null;
    query = getSession().getNamedQuery( "com.biperf.core.domain.activity.SubmittedNominationsAveargeForPromotions" );
    query.setParameter( "promotionId", promotionId );
    query.setParameter( "fromDate", fromDate );
    query.setParameter( "toDate", toDate );
    query.setParameter( "languageCode", getUserLocale() );
    List results = query.list();
    if ( null == results || results.isEmpty() || results.get( 0 ) == null )
    {
      return new BigDecimal( 0 );
    }
    else
    {
      return ( (BigDecimal)results.get( 0 ) ).abs();
    }
  }

  @Override
  public Long getMyNominationsReceived( Long promotionId, Long participantId, String fromDate, String toDate )
  {
    Query query = null;
    query = getSession().getNamedQuery( "com.biperf.core.domain.activity.MyNominationsReceived" );
    query.setParameter( "promotionId", promotionId );
    query.setParameter( "participantId", participantId );
    query.setParameter( "fromDate", fromDate );
    query.setParameter( "toDate", toDate );
    query.setParameter( "languageCode", getUserLocale() );
    List results = query.list();
    if ( null == results || results.isEmpty() || results.get( 0 ) == null )
    {
      return new Long( 0 );
    }
    else
    {
      return ( (Long)results.get( 0 ) ).longValue();
    }
  }

  @Override
  public BigDecimal getNominationsReceivedAveargeForMyTeam( Long promotionId, Long participantId, String fromDate, String toDate )
  {
    Query query = null;
    query = getSession().getNamedQuery( "com.biperf.core.domain.activity.NominationsReceivedAveargeForMyTeam" );
    query.setParameter( "promotionId", promotionId );
    query.setParameter( "participantId", participantId );
    query.setParameter( "fromDate", fromDate );
    query.setParameter( "toDate", toDate );
    query.setParameter( "languageCode", getUserLocale() );
    List results = query.list();
    if ( null == results || results.isEmpty() || results.get( 0 ) == null )
    {
      return new BigDecimal( 0 );
    }
    else
    {
      return ( (BigDecimal)results.get( 0 ) ).abs();
    }
  }

  @Override
  public BigDecimal getNominationsReceivedAveargeForCompany( Long promotionId, String fromDate, String toDate )
  {
    Query query = null;
    query = getSession().getNamedQuery( "com.biperf.core.domain.activity.NominationsReceivedAveargeForCompany" );
    query.setParameter( "promotionId", promotionId );
    query.setParameter( "fromDate", fromDate );
    query.setParameter( "toDate", toDate );
    query.setParameter( "languageCode", getUserLocale() );
    List results = query.list();
    if ( null == results || results.isEmpty() || results.get( 0 ) == null )
    {
      return new BigDecimal( 0 );
    }
    else
    {
      return ( (BigDecimal)results.get( 0 ) ).abs();
    }
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
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.activity.MyProductClaimsSubmitted" );
    query.setParameter( "promotionId", promotionId );
    query.setParameter( "participantId", participantId );
    query.setParameter( "claimStatus", claimStatus );
    query.setParameter( "fromDate", fromDate );
    query.setParameter( "toDate", toDate );
    query.setParameter( "languageCode", getUserLocale() );

    List results = query.list();
    if ( null == results || results.isEmpty() || results.get( 0 ) == null )
    {
      return new Long( 0 );
    }
    else
    {
      return ( (Long)results.get( 0 ) ).longValue();
    }
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
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.activity.ProductClaimsSubmittedAverageForMyTeam" );
    query.setParameter( "promotionId", promotionId );
    query.setParameter( "participantId", participantId );
    query.setParameter( "claimStatus", claimStatus );
    query.setParameter( "fromDate", fromDate );
    query.setParameter( "toDate", toDate );
    query.setParameter( "languageCode", getUserLocale() );

    List results = query.list();
    if ( null == results || results.isEmpty() || results.get( 0 ) == null )
    {
      return new BigDecimal( 0 );
    }
    else
    {
      return ( (BigDecimal)results.get( 0 ) ).abs();
    }
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
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.activity.ProductClaimsSubmittedAverageForCompany" );
    query.setParameter( "promotionId", promotionId );
    query.setParameter( "claimStatus", claimStatus );
    query.setParameter( "fromDate", fromDate );
    query.setParameter( "toDate", toDate );
    query.setParameter( "languageCode", getUserLocale() );

    List results = query.list();
    if ( null == results || results.isEmpty() || results.get( 0 ) == null )
    {
      return new BigDecimal( 0 );
    }
    else
    {
      return ( (BigDecimal)results.get( 0 ) ).abs();
    }
  }

  @SuppressWarnings( "unchecked" )
  public List<Activity> getGQAndCPActivities( Long userId, String promotionIds, String startDate, String endDate, String languageCode )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.activity.getGQAndCPActivities" );
    query.setParameter( "userId", userId );
    query.setParameter( "promotionIds", promotionIds );
    query.setParameter( "startDate", startDate );
    query.setParameter( "endDate", endDate );
    query.setParameter( "languageCode", languageCode );
    return query.list();
  }

  public Long getPromotionbyMerchOrderId( Long merchOrderId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.activity.getPromotionbyMerchOrderId" );
    query.setParameter( "merchOrderId", merchOrderId );

    return (Long)query.uniqueResult();
  }

  public String getPromotionNamebyMerchOrderId( Long merchOrderId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.activity.getPromotionNamebyMerchOrderId" );
    query.setParameter( "merchOrderId", merchOrderId );

    return (String)query.uniqueResult();
  }

  @Override
  public NominationActivity getNominationActivity( Long recipientId, Long promotionId, Long claimId, Long approvalRound )
  {
    NominationActivityQueryConstraint activityQueryConstraint = new NominationActivityQueryConstraint();
    activityQueryConstraint.setPromotionId( promotionId );
    activityQueryConstraint.setParticipantId( recipientId );
    activityQueryConstraint.setClaimId( claimId );
    activityQueryConstraint.setApprovalRound( approvalRound );
    NominationActivity activity = null;
    if ( !getActivityList( activityQueryConstraint ).isEmpty() )
    {
      activity = (NominationActivity)getActivityList( activityQueryConstraint ).get( 0 );
      return activity;
    }
    else
    {
      return null;
    }
  }

  public List getSweepActivityForGivers( Long promotionId, Date fromDate, Date toDate )
  {
    Query query = getSession().createSQLQuery( sweepSqlTop + sweepSqlMiddleGiver );
    query.setParameter( "promotionId", promotionId );
    query.setDate( "fromDate", fromDate );
    query.setDate( "toDate", toDate );
    query.setResultTransformer( new SweepValueBeanMapper() );
    return query.list();
  }

  public List getSweepActivityForReceivers( Long promotionId, Date fromDate, Date toDate )
  {
    Query query = getSession().createSQLQuery( sweepSqlTop + sweepSqlMiddleReceiver );
    query.setParameter( "promotionId", promotionId );
    query.setDate( "fromDate", fromDate );
    query.setDate( "toDate", toDate );
    query.setResultTransformer( new SweepValueBeanMapper() );
    return query.list();
  }

  public List getSweepActivityForGiversAndReceivers( Long promotionId, Date fromDate, Date toDate )
  {
    Query query = getSession().createSQLQuery( sweepSqlTop );
    query.setParameter( "promotionId", promotionId );
    query.setDate( "fromDate", fromDate );
    query.setDate( "toDate", toDate );
    query.setResultTransformer( new SweepValueBeanMapper() );
    return query.list();
  }

  @SuppressWarnings( "serial" )
  private class SweepValueBeanMapper extends BaseResultTransformer
  {
    @Override
    public Object transformTuple( Object[] tuple, String[] aliases )
    {
      SweepstakeWinnerPoolValue winnerPoolValue = new SweepstakeWinnerPoolValue();

      winnerPoolValue.setActivityId( extractLong( tuple[0] ) );
      winnerPoolValue.setUserId( extractLong( tuple[1] ) );
      winnerPoolValue.setSubmitter( extractBoolean( tuple[2] ) );

      return winnerPoolValue;
    }
  }
  // Client customization for WIP #43735 starts
  public Activity getRecipientClaimActivity( Long participantId, Long claimId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.activity.getRecipientClaimActivityForNomination" );
    query.setParameter( "participantId", participantId );
    query.setParameter( "claimId", claimId );
    return (Activity)query.uniqueResult();
  }
  public Activity getRecipientClaimActivityForNomination( Long participantId, Long claimId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.activity.getRecipientClaimActivityForNomination" );
    query.setParameter( "participantId", participantId );
    query.setParameter( "claimId", claimId );
    return (Activity)query.uniqueResult();
  }
  // Client customization for WIP #43735 ends
}

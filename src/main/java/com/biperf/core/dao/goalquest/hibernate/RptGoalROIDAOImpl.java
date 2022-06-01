
package com.biperf.core.dao.goalquest.hibernate;

import java.util.List;

import org.hibernate.Query;

import com.biperf.core.dao.BaseDAO;
import com.biperf.core.dao.goalquest.RptGoalROIDAO;
import com.biperf.core.domain.goalquest.RptGoalROI;
import com.biperf.core.utils.hibernate.HibernateUtil;

public class RptGoalROIDAOImpl extends BaseDAO implements RptGoalROIDAO
{

  /**
   * Selects records from the RPT_GOAL_ROI by promotion id.
   * 
   * @param promotionId
   * @return list of RptGoalROI objects
   */
  public List getRptGoalROIByPromotionId( Long promotionId )
  {

    List rptGoalROIList = getSession().getNamedQuery( "com.biperf.core.domain.goalquest.RptGoalROIByPromotionId" ).setLong( "promotionId", promotionId.longValue() ).list();

    return rptGoalROIList;
  }

  /**
   * Overridden from @see com.biperf.core.dao.goalquest.RptGoalROIDAO#getNbrOfAllActivePax()
   * @return Integer number of all active participants (used in primary audience)
   */
  public Integer getNbrOfAllActivePax()
  {
    Integer count = (Integer)getSession().getNamedQuery( "com.biperf.core.domain.goalquest.AllActivePaxCount" ).uniqueResult();
    return count;
  }

  /**
   * @param promotionId
   * @return List - get goal achieved counts for a certain promotion.
   */
  public List getAchievedCounts( Long promotionId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.goalquest.PaxsByGoalsAchieved" );
    query.setLong( "promotionId", promotionId.longValue() );
    return query.list();
  }

  /**
   * @param promotionId
   * @return List - get goal not achieved over baseline counts for a certain promotion.
   */
  public List getNotAchievedOverBaselineCounts( Long promotionId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.goalquest.PaxsByGoalsNotAchievedOverBaseline" );
    query.setLong( "promotionId", promotionId.longValue() );
    return query.list();
  }

  /**
   * @param promotionId
   * @return List - get subtotal sum of achieved counts and
   *                goal not achieved over baseline counts for a certain promotion.
   */
  public List getSubtotalCounts( Long promotionId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.goalquest.Subtotal" );
    query.setLong( "promotionId", promotionId.longValue() );
    return query.list();
  }

  /**
   * @param promotionId
   * @return List - get goal not achieved under baseline counts for a certain promotion.
   */
  public List getNotAchievedUnderBaselineCounts( Long promotionId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.goalquest.PaxsByGoalsNotAchievedUnderBaseline" );
    query.setLong( "promotionId", promotionId.longValue() );
    return query.list();
  }

  /**
   * @param promotionId
   * @return List - get did not select goal counts for a certain promotion.
   */
  public List getDidNotSelectGoalCounts( Long promotionId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.goalquest.PaxsDidNotSelectGoal" );
    query.setLong( "promotionId", promotionId.longValue() );
    return query.list();
  }

  /**
   * Saves the rptGoalROI to the database.
   * 
   * @param rptGoalROI
   * @return rptGoalROI
   */
  public RptGoalROI saveRptGoalROI( RptGoalROI rptGoalROI )
  {
    return (RptGoalROI)HibernateUtil.saveOrUpdateOrDeepMerge( rptGoalROI );
  }

}

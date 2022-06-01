
package com.biperf.core.dao.goalquest.hibernate;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.biperf.core.dao.BaseDAO;
import com.biperf.core.dao.goalquest.GoalLevelDAO;
import com.biperf.core.domain.promotion.AbstractGoalLevel;
import com.biperf.core.domain.promotion.GoalLevel;
import com.biperf.core.utils.HibernateSessionManager;

/**
 * GoalLevelDAOImpl.
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
 * <td>meadows</td>
 * <td>Dec 28, 2006</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 */
public class GoalLevelDAOImpl extends BaseDAO implements GoalLevelDAO
{

  /**
   * Get the GoalLevel from the database by the id.
   * 
   * @param id
   * @return abstractGoalLevel
   */
  public AbstractGoalLevel getGoalLevelById( Long id )
  {
    Session session = HibernateSessionManager.getSession();
    AbstractGoalLevel goalLevel = (AbstractGoalLevel)session.get( GoalLevel.class, id );
    return goalLevel;
  }

  /**
   * Get the GoalLevels from the database by the id.
   * 
   * @param id
   * @return list
   */
  public List getGoalLevelsByPromotionId( Long promotionId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.promotion.getGoalLevelsByPromotionId" );

    query.setParameter( "promotionId", promotionId );
    return query.list();
  }

  /**
   * Get the max GoalLevel sequence for all active promotions
   * 
   * @return int
   */
  public int getMaxSequence()
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.promotion.MaxSequence" );
    return runMaxSequenceQuery( query );
  }

  /**
   * Get the max GoalLevel sequence for all active promotions where goal selection start
   * date has passed
   * 
   * @return int
   */
  public int getMaxSequenceWhereGoalSelectionStarted()
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.promotion.MaxSequenceGoalSelectionStarted" );
    return runMaxSequenceQuery( query );
  }

  /**
   * Get the max GoalLevel sequence for all active promotions where issue awards
   * has been run
   * 
   * @return int
   */
  public int getMaxSequenceWhereIssueAwardsRun()
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.promotion.MaxSequenceIssueAwardsRun" );
    return runMaxSequenceQuery( query );
  }

  /**
   * Get the max GoalLevel sequence for all active promotions where issue awards
   * has been run
   * 
   * @return int
   */
  private int runMaxSequenceQuery( Query query )
  {
    Integer max = (Integer)query.uniqueResult();
    if ( max != null )
    {
      return max.intValue();
    }
    return 0;

  }

}

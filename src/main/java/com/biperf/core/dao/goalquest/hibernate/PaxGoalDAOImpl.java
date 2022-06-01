
package com.biperf.core.dao.goalquest.hibernate;

import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.biperf.core.dao.BaseDAO;
import com.biperf.core.dao.goalquest.PaxGoalDAO;
import com.biperf.core.domain.goalquest.PaxGoal;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.utils.HibernateSessionManager;
import com.biperf.core.utils.hibernate.HibernateUtil;

/**
 * PaxGoalDAOImpl.
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
 * <td>Tammy Cheng</td>
 * <td>Dec 28, 2006</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 */
public class PaxGoalDAOImpl extends BaseDAO implements PaxGoalDAO
{

  /**
   * Get the PaxGoal from the database by the id.
   * 
   * @param id
   * @return PaxGoal
   */
  public PaxGoal getPaxGoalById( Long id )
  {
    return getPaxGoalByIdWithAssociations( id, null );
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.dao.goalquest.PaxGoalDAO#getPaxGoalByIdWithAssociations(java.lang.Long,
   *      com.biperf.core.service.AssociationRequestCollection)
   * @param id
   * @param associationRequestCollection
   * @return PaxGoal
   */
  public PaxGoal getPaxGoalByIdWithAssociations( Long id, AssociationRequestCollection associationRequestCollection )
  {
    Session session = HibernateSessionManager.getSession();
    PaxGoal paxGoal = (PaxGoal)session.get( PaxGoal.class, id );

    if ( associationRequestCollection != null )
    {
      associationRequestCollection.process( paxGoal );
    }

    return paxGoal;
  }

  /**
   * Get the paxGoal of a participant by promotionId and userId.
   * 
   * @param promotionId
   * @param userId
   * @return paxGoal
   */
  public PaxGoal getPaxGoalByPromotionIdAndUserId( Long promotionId, Long userId )
  {
    PaxGoal paxGoal = null;
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.goalquest.PaxGoalByUserIdAndPromotionId" );
    query.setLong( "userId", userId.longValue() );
    query.setLong( "promotionId", promotionId.longValue() );
    List<PaxGoal> paxGoalList = query.list();

    if ( paxGoalList != null && paxGoalList.size() > 0 )
    {
      paxGoal = (PaxGoal)paxGoalList.get( 0 );
    }

    return paxGoal;
  }

  /**
   * Get a list of user ids that have selected a paxgoal in a promotion.
   * 
   * @param userId
   * @return paxGoal
   */
  public List<PaxGoal> getUserIdsWithPaxGoalByPromotionId( Long promotionId )
  {

    List<PaxGoal> userIdList = getSession().getNamedQuery( "com.biperf.core.domain.goalquest.UserIdsWithPaxGoalByPromotionId" ).setLong( "promotionId", promotionId.longValue() ).list();

    return userIdList;
  }

  /**
   * Get a list of paxGoals by promotion id.
   * 
   * @param promotionId
   * @param associationRequestCollection
   * @return list of PaxGoal objects
   */
  public List<PaxGoal> getPaxGoalByPromotionId( Long promotionId, AssociationRequestCollection associationRequestCollection )
  {
    List<PaxGoal> paxGoalList = null;

    paxGoalList = getSession().getNamedQuery( "com.biperf.core.domain.goalquest.PaxGoalByPromotionId" ).setLong( "promotionId", promotionId.longValue() ).list();

    Iterator iter = paxGoalList.iterator();
    if ( associationRequestCollection != null )
    {
      while ( iter.hasNext() )
      {
        associationRequestCollection.process( (PaxGoal)iter.next() );
      }
    }
    return paxGoalList;
  }

  /**
   * Get a list of paxGoals by promotion id.
   * 
   * @param promotionId
   * @return list of PaxGoal objects
   */
  public List<PaxGoal> getPaxGoalByPromotionId( Long promotionId )
  {
    return getPaxGoalByPromotionId( promotionId, null );

  }

  /**
   * Saves the paxGoal to the database.
   * 
   * @param paxGoal
   * @return PaxGoal
   */
  public PaxGoal savePaxGoal( PaxGoal paxGoal )
  {
    return (PaxGoal)HibernateUtil.saveOrUpdateOrShallowMerge( paxGoal );
  }

}

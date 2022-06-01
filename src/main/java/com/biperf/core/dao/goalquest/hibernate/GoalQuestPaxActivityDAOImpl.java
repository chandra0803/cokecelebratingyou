/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/dao/goalquest/hibernate/GoalQuestPaxActivityDAOImpl.java,v $
 */

package com.biperf.core.dao.goalquest.hibernate;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;

import com.biperf.core.dao.BaseDAO;
import com.biperf.core.dao.goalquest.GoalQuestPaxActivityDAO;
import com.biperf.core.domain.goalquest.GoalQuestParticipantActivity;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.utils.HibernateSessionManager;
import com.biperf.core.utils.hibernate.HibernateUtil;

/*
 * JournalDAOImpl <p> <b>Change History:</b><br> <table border="1"> <tr> <th>Author</th> <th>Date</th>
 * <th>Version</th> <th>Comments</th> </tr> <tr> <td>meadows</td> <td>Jan 01, 2007</td>
 * <td>1.0</td> <td>created</td> </tr> </table> </p>
 * 
 */

public class GoalQuestPaxActivityDAOImpl extends BaseDAO implements GoalQuestPaxActivityDAO
{

  /**
   * Overridden from @see com.biperf.core.dao.goalquest.GoalQuestPaxActivityDAO#deleteGoalQuestPaxActivity(com.biperf.core.domain.goalquest.GoalQuestParticipantActivity)
   * @param goalQuestParticipantActivity
   */
  public void deleteGoalQuestPaxActivity( GoalQuestParticipantActivity goalQuestParticipantActivity )
  {
    getSession().delete( goalQuestParticipantActivity );
  }

  /**
   * Overridden from @see com.biperf.core.dao.goalquest.GoalQuestPaxActivityDAO#getGoalQuestParticipantActivityById(java.lang.Long)
   * @param id
   * @return
   */
  public GoalQuestParticipantActivity getGoalQuestParticipantActivityById( Long id )
  {
    return (GoalQuestParticipantActivity)getSession().get( GoalQuestParticipantActivity.class, id );
  }

  /**
   * Overridden from @see com.biperf.core.dao.goalquest.GoalQuestPaxActivityDAO#saveGoalQuestPaxActivity(com.biperf.core.domain.goalquest.GoalQuestParticipantActivity)
   * @param goalQuestParticipantActivity
   * @return
   */
  public GoalQuestParticipantActivity saveGoalQuestPaxActivity( GoalQuestParticipantActivity goalQuestParticipantActivity )
  {
    return (GoalQuestParticipantActivity)HibernateUtil.saveOrUpdateOrShallowMerge( goalQuestParticipantActivity );
  }

  /**
   * Get a list of GoalQuestParticipantActivity by promotion id and user id.
   * 
   * @param promotionId
   * @param userId 
   * @param associationRequestCollection
   * @return list of GoalQuestParticipantActivity objects
   */
  @SuppressWarnings( "unchecked" )
  public List<GoalQuestParticipantActivity> getGoalQuestParticipantActivityByPromotionIdAndUserId( Long promotionId, Long userId, AssociationRequestCollection associationRequestCollection )
  {
    List<GoalQuestParticipantActivity> goalQuestPaxActivityList = new ArrayList<GoalQuestParticipantActivity>();
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.goalquest.GoalQuestPaxActivityByUserIdAndPromotionId" );
    query.setLong( "userId", userId.longValue() );
    query.setLong( "promotionId", promotionId.longValue() );
    goalQuestPaxActivityList = query.list();

    Iterator<GoalQuestParticipantActivity> iter = goalQuestPaxActivityList.iterator();
    while ( iter.hasNext() )
    {
      if ( associationRequestCollection != null )
      {
        associationRequestCollection.process( iter.next() );
      }
    }
    return goalQuestPaxActivityList;
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.dao.goalquest.GoalQuestPaxActivityDAO#isParticipantPayoutComplete(java.lang.Long,
   *      java.lang.Long)
   * @param userId
   * @param promotionId
   * @return boolean payout completed or not
   */
  public boolean isParticipantPayoutComplete( Long userId, Long promotionId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.goalquest.ActivityByPromotionAndUserIdWithPayout" );
    query.setParameter( "userId", userId );
    query.setParameter( "promotionId", promotionId );
    return null == query.uniqueResult() ? false : true;
  }

  @SuppressWarnings( "unchecked" )
  @Override
  public GoalQuestParticipantActivity getPaxActivityByPromotionIdAndUserIdAndSubDate( Long promotionId, Long userId, Date submissionDate )
  {
    Criteria criteria = HibernateSessionManager.getSession().createCriteria( GoalQuestParticipantActivity.class, "goalQuestParticipantActivity" );
    criteria.add( Restrictions.eq( "goalQuestParticipantActivity.participant.id", userId.longValue() ) );
    criteria.add( Restrictions.eq( "goalQuestParticipantActivity.goalQuestPromotion.id", promotionId.longValue() ) );
    criteria.add( Restrictions.eq( "goalQuestParticipantActivity.submissionDate", submissionDate ) );
    List<GoalQuestParticipantActivity> goalQuestPaxActivityList = criteria.list();
    if ( goalQuestPaxActivityList.size() > 0 )
    {
      return goalQuestPaxActivityList.get( 0 );
    }
    else
    {
      return null;
    }
  }

}

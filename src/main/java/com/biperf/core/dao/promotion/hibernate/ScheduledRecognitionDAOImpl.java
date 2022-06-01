
package com.biperf.core.dao.promotion.hibernate;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.biperf.core.dao.BaseDAO;
import com.biperf.core.dao.promotion.ScheduledRecognitionDAO;
import com.biperf.core.domain.promotion.ScheduledRecognition;
import com.biperf.core.utils.HibernateSessionManager;
import com.biperf.core.utils.hibernate.HibernateUtil;

public class ScheduledRecognitionDAOImpl extends BaseDAO implements ScheduledRecognitionDAO
{
  public ScheduledRecognition saveScheduledRecognition( ScheduledRecognition scheduledRecognition )
  {
    return (ScheduledRecognition)HibernateUtil.saveOrUpdateOrShallowMerge( scheduledRecognition );
  }

  public ScheduledRecognition getScheduledRecognitionById( Long id )
  {
    return (ScheduledRecognition)getSession().get( ScheduledRecognition.class, id );
  }

  public List<ScheduledRecognition> getScheduledRecognitionsByDeliveryDate( Date deliveryDate )
  {
    Session session = HibernateSessionManager.getSession();
    Criteria criteria = session.createCriteria( ScheduledRecognition.class );
    criteria.add( Restrictions.eq( "deliveryDate", deliveryDate ) );
    List<ScheduledRecognition> schedulesList = criteria.list();
    return schedulesList;
  }

  public ScheduledRecognition getScheduledRecognitionByTriggerName( String triggerName )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.promotion.ScheduledRecognition.GetScheduledRecognitionByTriggerName" );
    query.setParameter( "triggerName", triggerName );

    return (ScheduledRecognition)query.uniqueResult();
  }

  public List<Long> getScheduledRecognitionIdsForRetryProcess()
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.promotion.ScheduledRecognition.getScheduledRecognitionIdsForRetryProcess" );
    List<Long> results = query.list();
    return results;
  }
}

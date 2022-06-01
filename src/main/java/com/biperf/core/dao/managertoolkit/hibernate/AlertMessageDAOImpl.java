
package com.biperf.core.dao.managertoolkit.hibernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;

import com.biperf.core.dao.BaseDAO;
import com.biperf.core.dao.managertoolkit.AlertMessageDAO;
import com.biperf.core.domain.managertoolkit.AlertMessage;

public class AlertMessageDAOImpl extends BaseDAO implements AlertMessageDAO
{
  public AlertMessage getAlertMessageById( Long id )
  {
    return (AlertMessage)getSession().get( AlertMessage.class, id );
  }

  public List getAlertMessageByUserId( Long userId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.managertoolkit.GetAlertMessageByUserId" );
    query.setLong( "userId", userId.longValue() );
    return query.list();
  }

  public List<AlertMessage> getAlertMessageByContestId( Long contestId )
  {
    Criteria criteria = getSession().createCriteria( AlertMessage.class );
    criteria.add( Restrictions.eq( "contestId", contestId ) );
    return criteria.list();
  }

}

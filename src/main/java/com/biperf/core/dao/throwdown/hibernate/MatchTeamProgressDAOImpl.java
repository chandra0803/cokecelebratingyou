
package com.biperf.core.dao.throwdown.hibernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.biperf.core.dao.BaseDAO;
import com.biperf.core.dao.throwdown.MatchTeamProgressDAO;
import com.biperf.core.domain.promotion.MatchTeamProgress;
import com.biperf.core.utils.HibernateSessionManager;
import com.biperf.core.utils.hibernate.HibernateUtil;

public class MatchTeamProgressDAOImpl extends BaseDAO implements MatchTeamProgressDAO
{

  @Override
  public MatchTeamProgress save( MatchTeamProgress matchTeamProgress )
  {
    getSession().save( matchTeamProgress );
    return matchTeamProgress;
  }

  public MatchTeamProgress saveMatchTeamProgress( MatchTeamProgress matchTeamProgress )
  {
    return (MatchTeamProgress)HibernateUtil.saveOrUpdateOrShallowMerge( matchTeamProgress );
  }

  @SuppressWarnings( "unchecked" )
  @Override
  public List<MatchTeamProgress> getProgressListByOutcome( Long id )
  {
    Session session = HibernateSessionManager.getSession();
    Criteria criteria = session.createCriteria( MatchTeamProgress.class );
    criteria.createAlias( "teamOutcome", "teamOutcome" );
    criteria.add( Restrictions.eq( "teamOutcome.id", id ) );
    criteria.addOrder( Order.asc( "auditCreateInfo" ) );
    List<MatchTeamProgress> progressList = criteria.list();
    return progressList;

  }

}

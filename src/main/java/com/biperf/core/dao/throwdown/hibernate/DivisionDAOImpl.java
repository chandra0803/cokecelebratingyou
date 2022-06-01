
package com.biperf.core.dao.throwdown.hibernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.biperf.core.dao.BaseDAO;
import com.biperf.core.dao.throwdown.DivisionDAO;
import com.biperf.core.domain.promotion.Division;
import com.biperf.core.utils.HibernateSessionManager;

public class DivisionDAOImpl extends BaseDAO implements DivisionDAO
{

  @Override
  public Division getDivision( Long id )
  {
    Session session = HibernateSessionManager.getSession();
    return (Division)session.get( Division.class, id );
  }

  @Override
  public Division save( Division division )
  {
    getSession().saveOrUpdate( division );
    return division;
  }

  @Override
  public void delete( Long divisionId )
  {
    Division division = (Division)getSession().get( Division.class, divisionId );
    getSession().delete( division );
  }

  @SuppressWarnings( "unchecked" )
  @Override
  public List<Division> getDivisionsByPromotionId( Long promotionId )
  {
    Session session = HibernateSessionManager.getSession();
    Criteria criteria = session.createCriteria( Division.class );
    criteria.createAlias( "promotion", "promotion" );
    criteria.add( Restrictions.eq( "promotion.id", promotionId ) );
    return criteria.list();
  }

}

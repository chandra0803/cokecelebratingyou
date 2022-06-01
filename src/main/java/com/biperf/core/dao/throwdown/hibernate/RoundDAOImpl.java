/*
 * (c) 2012 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/dao/throwdown/hibernate/RoundDAOImpl.java,v $
 */

package com.biperf.core.dao.throwdown.hibernate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.ResultTransformer;

import com.biperf.core.dao.BaseDAO;
import com.biperf.core.dao.throwdown.RoundDAO;
import com.biperf.core.domain.promotion.Round;
import com.biperf.core.utils.HibernateSessionManager;
import com.biperf.core.utils.UserManager;

public class RoundDAOImpl extends BaseDAO implements RoundDAO
{
  @Override
  public Round save( Round round )
  {
    Session session = HibernateSessionManager.getSession();
    session.save( round );
    return round;
  }

  @Override
  public Round getRoundById( Long roundId )
  {
    return (Round)HibernateSessionManager.getSession().get( Round.class, roundId );
  }

  @SuppressWarnings( "unchecked" )
  @Override
  public List<Round> getRoundsForPromotionByRoundNumber( Long promotionId, int roundNumber )
  {
    Criteria criteria = HibernateSessionManager.getSession().createCriteria( Round.class );
    criteria.createAlias( "division", "division" );
    criteria.createAlias( "division.promotion", "promotion" );
    criteria.add( Restrictions.eq( "promotion.id", promotionId ) );
    criteria.add( Restrictions.eq( "roundNumber", roundNumber ) );
    return (List<Round>)criteria.list();
  }

  @Override
  public Round getRoundsForPromotionByDivisionAndRoundNumber( Long promotionId, Long divisionId, int roundNumber )
  {
    Criteria criteria = HibernateSessionManager.getSession().createCriteria( Round.class );
    criteria.createAlias( "division", "division" );
    criteria.add( Restrictions.eq( "division.id", divisionId ) );
    criteria.createAlias( "division.promotion", "promotion" );
    criteria.add( Restrictions.eq( "promotion.id", promotionId ) );
    criteria.add( Restrictions.eq( "roundNumber", roundNumber ) );
    return (Round)criteria.uniqueResult();
  }

  @SuppressWarnings( "unchecked" )
  @Override
  public List<Round> getRoundsByDivision( Long divisionId )
  {
    Criteria criteria = HibernateSessionManager.getSession().createCriteria( Round.class );
    criteria.createAlias( "division", "division" );
    criteria.add( Restrictions.eq( "division.id", divisionId ) );
    return (List<Round>)criteria.list();
  }

  public BigDecimal getCalculatedAverageForRound( Long roundId, Long teamId, RoundingMode mode )
  {
    Criteria criteria = HibernateSessionManager.getSession().createCriteria( Round.class );
    criteria.add( Restrictions.eq( "id", roundId ) );
    criteria.createAlias( "matches", "matches" );
    criteria.createAlias( "matches.teamOutcomes", "teamOutcomes" );
    criteria.createAlias( "teamOutcomes.team", "team" );
    criteria.add( Restrictions.ne( "team.id", teamId ) );
    criteria.add( Restrictions.eq( "team.shadowPlayer", false ) );
    criteria.add( Restrictions.eq( "team.active", true ) );
    ProjectionList proList = Projections.projectionList();
    proList.add( Projections.sum( "teamOutcomes.currentValue" ) );
    proList.add( Projections.count( "teamOutcomes.team" ) );
    criteria.setProjection( proList );
    criteria.setResultTransformer( new CalculatedAverageResultTransformer( mode ) );
    return (BigDecimal)criteria.uniqueResult();
  }

  @SuppressWarnings( "serial" )
  private class CalculatedAverageResultTransformer implements ResultTransformer
  {
    private RoundingMode mode = null;

    public CalculatedAverageResultTransformer( RoundingMode mode )
    {
      this.mode = mode;
    }

    @Override
    public Object transformTuple( Object[] tuple, String[] aliases )
    {
      BigDecimal sum = tuple[0] != null ? (BigDecimal)tuple[0] : new BigDecimal( 0 );
      BigDecimal count = new BigDecimal( (Long)tuple[1] );
      // if count happens to be 0, well, can't divide by zero
      if ( sum.intValue() == 0 || count.intValue() == 0 )
      {
        return new BigDecimal( 0 );
      }
      else
      {
        return sum.divide( count, 2, mode );
      }
    }

    @SuppressWarnings( { "unchecked", "rawtypes" } )
    @Override
    public List<CalculatedAverageResultTransformer> transformList( List collection )
    {
      return collection;
    }
  }

  @Override
  public Round getCurrentRound( Long promotionId, Long divisionId )
  {
    Session session = HibernateSessionManager.getSession();
    Query query = session.getNamedQuery( "com.biperf.core.domain.round.getCurrentRound" );
    query.setParameter( "divisionId", divisionId );
    query.setParameter( "promotionId", promotionId );
    query.setParameter( "currentDate", UserManager.getCurrentDateWithTimeZoneID() );
    return (Round)query.uniqueResult();
  }

  @Override
  public boolean isRoundPaidForDivisionPayouts( Long promotionId, int roundNumber )
  {
    Session session = HibernateSessionManager.getSession();
    Query query = session.getNamedQuery( "com.biperf.core.domain.round.isRoundPaidForDivisionPayouts" );
    query.setParameter( "roundNumber", roundNumber );
    query.setParameter( "promotionId", promotionId );
    return ( (Long)query.uniqueResult() ).longValue() == 0;
  }

  @Override
  public boolean isRoundCompleted( Long promotionId, int roundNumber )
  {
    Session session = HibernateSessionManager.getSession();
    Query query = session.getNamedQuery( "com.biperf.core.domain.round.isRoundCompleted" );
    query.setParameter( "roundNumber", roundNumber );
    query.setParameter( "promotionId", promotionId );
    query.setParameter( "currentDate", UserManager.getCurrentDateWithTimeZoneID() );
    return ( (Long)query.uniqueResult() ).longValue() != 0;
  }

  @Override
  public boolean isRoundStarted( Long promotionId, int roundNumber )
  {
    Session session = HibernateSessionManager.getSession();
    Query query = session.getNamedQuery( "com.biperf.core.domain.round.isRoundStarted" );
    query.setParameter( "roundNumber", roundNumber );
    query.setParameter( "promotionId", promotionId );
    query.setParameter( "currentDate", UserManager.getCurrentDateWithTimeZoneID() );
    return ( (Long)query.uniqueResult() ).longValue() != 0;
  }

}

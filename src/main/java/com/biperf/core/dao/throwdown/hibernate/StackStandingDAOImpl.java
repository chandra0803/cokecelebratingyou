
package com.biperf.core.dao.throwdown.hibernate;

import java.math.BigDecimal;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.biperf.core.dao.BaseDAO;
import com.biperf.core.dao.throwdown.StackStandingDAO;
import com.biperf.core.domain.promotion.StackStanding;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.utils.HibernateSessionManager;
import com.biperf.core.utils.hibernate.HibernateUtil;

public class StackStandingDAOImpl extends BaseDAO implements StackStandingDAO
{

  @Override
  public StackStanding get( Long stackStandingId )
  {
    Session session = HibernateSessionManager.getSession();
    return (StackStanding)session.get( StackStanding.class, stackStandingId );
  }

  @Override
  public StackStanding get( Long stackStandingId, AssociationRequestCollection associationRequest )
  {
    StackStanding stackStanding = (StackStanding)getSession().get( StackStanding.class, stackStandingId );

    if ( associationRequest != null )
    {
      associationRequest.process( stackStanding );
    }

    return stackStanding;
  }

  @Override
  public StackStanding save( StackStanding stackStanding )
  {
    return (StackStanding)HibernateUtil.saveOrUpdateOrShallowMerge( stackStanding );
  }

  @SuppressWarnings( "unchecked" )
  @Override
  public List<StackStanding> getAll()
  {
    Session session = HibernateSessionManager.getSession();
    Criteria criteria = session.createCriteria( StackStanding.class );
    return criteria.list();
  }

  @SuppressWarnings( "unchecked" )
  @Override
  public List<StackStanding> getRankings()
  {
    Session session = HibernateSessionManager.getSession();
    Criteria criteria = session.createCriteria( StackStanding.class );
    criteria.add( Restrictions.eq( "active", true ) );
    return criteria.list();
  }

  @SuppressWarnings( "unchecked" )
  @Override
  public List<StackStanding> getRankingsForPromotion( Long promotionId )
  {
    Session session = HibernateSessionManager.getSession();
    Criteria criteria = session.createCriteria( StackStanding.class );
    criteria.add( Restrictions.eq( "active", true ) );
    criteria.createAlias( "promotion", "promotion" );
    criteria.add( Restrictions.eq( "promotion.id", promotionId ) );
    return criteria.list();
  }

  @Override
  public StackStanding getRankingForPromotionAndRound( Long promotionId, int roundNumber )
  {
    Session session = HibernateSessionManager.getSession();
    Criteria criteria = session.createCriteria( StackStanding.class );
    criteria.add( Restrictions.eq( "active", true ) );
    criteria.createAlias( "promotion", "promotion" );
    criteria.add( Restrictions.eq( "promotion.id", promotionId ) );
    criteria.add( Restrictions.eq( "roundNumber", roundNumber ) );
    return (StackStanding)criteria.uniqueResult();
  }

  @SuppressWarnings( "unchecked" )
  @Override
  public List<StackStanding> getApprovedRankings()
  {
    Session session = HibernateSessionManager.getSession();
    Criteria criteria = session.createCriteria( StackStanding.class );
    criteria.add( Restrictions.eq( "active", true ) );
    criteria.add( Restrictions.eq( "payoutsIssued", true ) );
    return criteria.list();
  }

  @SuppressWarnings( "unchecked" )
  @Override
  public List<StackStanding> getApprovedRankingsForPromotion( Long promotionId )
  {
    Session session = HibernateSessionManager.getSession();
    Criteria criteria = session.createCriteria( StackStanding.class );
    criteria.add( Restrictions.eq( "active", true ) );
    criteria.add( Restrictions.eq( "payoutsIssued", true ) );
    criteria.createAlias( "promotion", "promotion" );
    criteria.add( Restrictions.eq( "promotion.id", promotionId ) );
    return criteria.list();
  }

  @Override
  public StackStanding getApprovedRankingForPromotionAndRound( Long promotionId, int roundNumber )
  {
    Session session = HibernateSessionManager.getSession();
    Criteria criteria = session.createCriteria( StackStanding.class );
    criteria.add( Restrictions.eq( "active", true ) );
    criteria.add( Restrictions.eq( "payoutsIssued", true ) );
    criteria.createAlias( "promotion", "promotion" );
    criteria.add( Restrictions.eq( "promotion.id", promotionId ) );
    criteria.add( Restrictions.eq( "roundNumber", roundNumber ) );
    return (StackStanding)criteria.uniqueResult();
  }

  @SuppressWarnings( "unchecked" )
  @Override
  public List<StackStanding> getUnapprovedRankings()
  {
    Session session = HibernateSessionManager.getSession();
    Criteria criteria = session.createCriteria( StackStanding.class );
    criteria.add( Restrictions.eq( "active", true ) );
    criteria.add( Restrictions.eq( "payoutsIssued", false ) );
    return criteria.list();
  }

  @SuppressWarnings( "unchecked" )
  @Override
  public List<StackStanding> getUnapprovedRankingsForPromotion( Long promotionId )
  {
    Session session = HibernateSessionManager.getSession();
    Criteria criteria = session.createCriteria( StackStanding.class );
    criteria.add( Restrictions.eq( "active", true ) );
    criteria.add( Restrictions.eq( "payoutsIssued", false ) );
    criteria.createAlias( "promotion", "promotion" );
    criteria.add( Restrictions.eq( "promotion.id", promotionId ) );
    return criteria.list();
  }

  @Override
  public StackStanding getUnapprovedRankingForPromotionAndRound( Long promotionId, int roundNumber )
  {
    Session session = HibernateSessionManager.getSession();
    Criteria criteria = session.createCriteria( StackStanding.class );
    criteria.add( Restrictions.eq( "active", true ) );
    criteria.add( Restrictions.eq( "payoutsIssued", false ) );
    criteria.createAlias( "promotion", "promotion" );
    criteria.add( Restrictions.eq( "promotion.id", promotionId ) );
    criteria.add( Restrictions.eq( "roundNumber", roundNumber ) );
    return (StackStanding)criteria.uniqueResult();
  }

  @Override
  public boolean isAnyPaxPaidOutForRanking( Long stackStandingId )
  {
    Query query = HibernateSessionManager.getSession().getNamedQuery( "com.biperf.core.domain.throwdown.StackStanding.isAnyPaxPaidOutForRanking" );
    query.setParameter( "rankingId", stackStandingId );
    return ( (Long)query.uniqueResult() ).longValue() > 0;
  }

  @Override
  public Integer getNodeRankForUser( Long promotionId, int roundNumber, Long userId, Long nodeId )
  {
    Query query = HibernateSessionManager.getSession().getNamedQuery( "com.biperf.core.domain.throwdown.StackStanding.getNodeRankForUser" );
    query.setParameter( "promotionId", promotionId );
    query.setParameter( "roundNumber", roundNumber );
    query.setParameter( "nodeId", nodeId );
    query.setParameter( "userId", userId );
    Object rank = query.uniqueResult();
    return rank != null ? ( (Integer)rank ).intValue() : null;
  }

  @Override
  public Integer getHierarchyRankForUser( Long promotionId, int roundNumber, Long userId )
  {
    Query query = HibernateSessionManager.getSession().getNamedQuery( "com.biperf.core.domain.throwdown.StackStanding.getHierarchyRankForUser" );
    query.setParameter( "promotionId", promotionId );
    query.setParameter( "roundNumber", roundNumber );
    query.setParameter( "userId", userId );
    Object rank = query.uniqueResult();
    return rank != null ? ( (Integer)rank ).intValue() : null;
  }

  public Integer getTotalUsersInHierarchyRanking( Long promotionId, int roundNumber )
  {
    Query query = HibernateSessionManager.getSession().getNamedQuery( "com.biperf.core.domain.throwdown.StackStanding.getTotalUsersInHierarchyRanking" );
    query.setParameter( "promotionId", promotionId );
    query.setParameter( "roundNumber", roundNumber );
    Object count = query.uniqueResult();
    return count != null ? ( (BigDecimal)count ).intValue() : null;
  }

}

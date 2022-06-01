/*
 * (c) 2012 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/dao/throwdown/hibernate/MatchDAOImpl.java,v $
 */

package com.biperf.core.dao.throwdown.hibernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.biperf.core.dao.BaseDAO;
import com.biperf.core.dao.throwdown.MatchDAO;
import com.biperf.core.domain.enums.MatchStatusType;
import com.biperf.core.domain.promotion.Match;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.utils.HibernateSessionManager;

public class MatchDAOImpl extends BaseDAO implements MatchDAO
{
  @Override
  public Match save( Match match )
  {
    getSession().save( match );
    return match;
  }

  @Override
  public Match getMatch( Long matchId )
  {
    return (Match)getSession().get( Match.class, matchId );
  }

  @Override
  public Match getMatch( Long matchId, AssociationRequestCollection associationRequestCollection )
  {
    Match match = (Match)getSession().get( Match.class, matchId );
    if ( associationRequestCollection != null )
    {
      associationRequestCollection.process( match );
    }
    return match;
  }

  @Override
  public Match getMatchDetails( Long matchId )
  {
    Criteria criteria = getSession().createCriteria( Match.class );
    criteria.add( Restrictions.eq( "id", matchId ) );
    criteria.setFetchMode( "teamOutcomes", FetchMode.JOIN );
    criteria.setFetchMode( "teamOutcomes.team", FetchMode.JOIN );
    criteria.setFetchMode( "teamOutcomes.team.participant", FetchMode.JOIN );

    return (Match)criteria.uniqueResult();
  }

  @SuppressWarnings( "unchecked" )
  @Override
  public List<Match> getUnplayedMatchesForPromotionAndRound( Long roundId )
  {
    Criteria criteria = getSession().createCriteria( Match.class );
    criteria.add( Restrictions.eq( "round.id", roundId ) );
    criteria.add( Restrictions.eq( "status", MatchStatusType.lookup( MatchStatusType.NOT_PLAYED ) ) );
    return (List<Match>)criteria.list();
  }

  @SuppressWarnings( "unchecked" )
  @Override
  public List<Match> getMatchesByPromotionAndTeam( Long promotionId, Long teamId )
  {
    Session session = HibernateSessionManager.getSession();
    Query query = session.getNamedQuery( "com.biperf.core.domain.match.getMatchesByPromotionAndTeam" );
    query.setParameter( "teamId", teamId );
    query.setParameter( "promotionId", promotionId );
    return query.list();
  }

  @SuppressWarnings( "unchecked" )
  @Override
  public List<Match> getMatchesByRound( Long roundId )
  {
    Session session = HibernateSessionManager.getSession();
    Query query = session.getNamedQuery( "com.biperf.core.domain.match.getMatchesByRound" );
    query.setParameter( "roundId", roundId );
    return query.list();
  }

  @SuppressWarnings( "unchecked" )
  @Override
  public List<Match> getMatchesByPromotionAndRoundNumber( Long promotionId, Integer roundNumber )
  {
    Session session = HibernateSessionManager.getSession();
    Query query = session.getNamedQuery( "com.biperf.core.domain.match.getMatchesByPromotionAndRoundNumber" );
    query.setParameter( "promotionId", promotionId );
    query.setParameter( "roundNumber", roundNumber );
    return query.list();
  }

  public List<Match> getMatchesByPromotionAndRoundNumber( Long promotionId, Integer roundNumber, AssociationRequestCollection associationRequestCollection )
  {
    Session session = HibernateSessionManager.getSession();
    Query query = session.getNamedQuery( "com.biperf.core.domain.match.getMatchesByPromotionAndRoundNumber" );
    query.setParameter( "promotionId", promotionId );
    query.setParameter( "roundNumber", roundNumber );

    List<Match> matches = query.list();

    if ( associationRequestCollection != null )
    {
      associationRequestCollection.process( matches );
    }

    return matches;
  }

  @Override
  public Match getMatchByPromotionAndRoundIdAndTeam( Long promotionId, Long roundId, Long teamId )
  {
    Session session = HibernateSessionManager.getSession();
    Query query = session.getNamedQuery( "com.biperf.core.domain.match.getMatchesByPromotionAndRoundIdAndTeam" );
    query.setParameter( "teamId", teamId );
    query.setParameter( "promotionId", promotionId );
    query.setParameter( "roundId", roundId );
    return (Match)query.uniqueResult();
  }

  @Override
  public Match getMatchByPromotionAndRoundNumberAndTeam( Long promotionId, Integer roundNumber, Long teamId )
  {
    Session session = HibernateSessionManager.getSession();
    Query query = session.getNamedQuery( "com.biperf.core.domain.match.getMatchesByPromotionAndRoundNumberAndTeam" );
    query.setParameter( "teamId", teamId );
    query.setParameter( "promotionId", promotionId );
    query.setParameter( "roundNumber", roundNumber );
    return (Match)query.uniqueResult();
  }

}

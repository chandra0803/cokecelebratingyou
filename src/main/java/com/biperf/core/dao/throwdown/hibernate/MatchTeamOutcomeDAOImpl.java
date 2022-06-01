
package com.biperf.core.dao.throwdown.hibernate;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.biperf.core.dao.BaseDAO;
import com.biperf.core.dao.throwdown.MatchTeamOutcomeDAO;
import com.biperf.core.domain.promotion.MatchTeamOutcome;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.utils.HibernateSessionManager;

public class MatchTeamOutcomeDAOImpl extends BaseDAO implements MatchTeamOutcomeDAO
{

  @Override
  public MatchTeamOutcome save( MatchTeamOutcome matchTeamOutcome )
  {
    Session session = HibernateSessionManager.getSession();
    session.saveOrUpdate( matchTeamOutcome );
    return matchTeamOutcome;
  }

  @Override
  public MatchTeamOutcome getMatchTeamOutcome( Long id )
  {
    Session session = HibernateSessionManager.getSession();
    return (MatchTeamOutcome)session.get( MatchTeamOutcome.class, id );
  }

  public MatchTeamOutcome getOutcomeForMatch( Long teamId, Long promotionId, Integer roundNumber, AssociationRequestCollection associationRequestCollection )
  {
    MatchTeamOutcome matchTeamOutcome = getOutcomeForMatch( teamId, promotionId, roundNumber );
    if ( associationRequestCollection != null )
    {
      associationRequestCollection.process( matchTeamOutcome );
    }
    return matchTeamOutcome;
  }

  public MatchTeamOutcome getOutcomeForMatch( Long teamId, Long promotionId, Integer roundNumber )
  {
    Session session = HibernateSessionManager.getSession();
    Criteria criteria = session.createCriteria( MatchTeamOutcome.class );
    criteria.createAlias( "team", "team" );
    criteria.add( Restrictions.eq( "team.id", teamId ) );
    criteria.createAlias( "match", "match" );
    criteria.createAlias( "match.round", "round" );
    criteria.add( Restrictions.eq( "round.roundNumber", roundNumber ) );
    criteria.createAlias( "promotion", "promotion" );
    criteria.add( Restrictions.eq( "promotion.id", promotionId ) );
    return (MatchTeamOutcome)criteria.uniqueResult();
  }

  public MatchTeamOutcome getOutcomeForTeamInSpecificRound( Long teamId, Long roundId )
  {
    Session session = HibernateSessionManager.getSession();
    Criteria criteria = session.createCriteria( MatchTeamOutcome.class );
    criteria.createAlias( "team", "team" );
    criteria.add( Restrictions.eq( "team.id", teamId ) );
    criteria.createAlias( "match", "match" );
    criteria.createAlias( "match.round", "round" );
    criteria.add( Restrictions.eq( "round.id", roundId ) );
    return (MatchTeamOutcome)criteria.uniqueResult();
  }

  public MatchTeamOutcome getOutcomeForTeamInSpecificRound( Long userId, int roundNumber, Long promotionId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.promotion.getTeamOutcomeByPromotionIdUserIdRoundNumber" );
    query.setParameter( "userId", userId );
    query.setParameter( "roundNumber", roundNumber );
    query.setParameter( "promotionId", promotionId );

    return (MatchTeamOutcome)query.uniqueResult();
  }

  public MatchTeamOutcome getOutcomeForTeamInSpecificRound( Long userId, int roundNumber, Long promotionId, AssociationRequestCollection associationRequestCollection )
  {
    MatchTeamOutcome matchTeamOutcome = getOutcomeForTeamInSpecificRound( userId, roundNumber, promotionId );
    if ( associationRequestCollection != null )
    {
      associationRequestCollection.process( matchTeamOutcome );
    }
    return matchTeamOutcome;
  }

}

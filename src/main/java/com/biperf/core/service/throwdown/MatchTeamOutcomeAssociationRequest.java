
package com.biperf.core.service.throwdown;

import com.biperf.core.domain.promotion.MatchTeamOutcome;
import com.biperf.core.service.BaseAssociationRequest;

public class MatchTeamOutcomeAssociationRequest extends BaseAssociationRequest
{
  private int hydrateLevel = 0;

  public static final int ALL = 1;
  public static final int PROMOTION = 2;
  public static final int MATCH = 3;
  public static final int TEAM = 4;
  public static final int MATCH_OUTCOMES = 5;
  public static final int PROGRESS = 6;

  public MatchTeamOutcomeAssociationRequest( int hydrateLevel )
  {
    this.hydrateLevel = hydrateLevel;
  }

  public void execute( Object domainObject )
  {
    MatchTeamOutcome outcome = (MatchTeamOutcome)domainObject;

    switch ( hydrateLevel )
    {
      case ALL:
        hydrateAll( outcome );
        break;
      case PROMOTION:
        hydratePromotion( outcome );
        break;
      case MATCH:
        hydrateMatch( outcome );
        break;
      case TEAM:
        hydrateTeam( outcome );
        break;
      case MATCH_OUTCOMES:
        hydrateMatchOutcomes( outcome );
        break;
      case PROGRESS:
        hydrateProgress( outcome );
        break;
      default:
        break;
    }
  }

  private void hydratePromotion( MatchTeamOutcome outcome )
  {
    initialize( outcome.getPromotion() );
  }

  private void hydrateMatch( MatchTeamOutcome outcome )
  {
    initialize( outcome.getMatch() );
  }

  private void hydrateTeam( MatchTeamOutcome outcome )
  {
    initialize( outcome.getTeam() );
  }

  private void hydrateMatchOutcomes( MatchTeamOutcome outcome )
  {
    initialize( outcome.getMatch().getTeamOutcomes() );
  }

  private void hydrateProgress( MatchTeamOutcome outcome )
  {
    for ( MatchTeamOutcome teamOutcome : outcome.getMatch().getTeamOutcomes() )
    {
      initialize( teamOutcome.getProgress() );
    }
  }

  private void hydrateAll( MatchTeamOutcome outcome )
  {
    hydratePromotion( outcome );
    hydrateMatch( outcome );
    hydrateTeam( outcome );
    hydrateMatchOutcomes( outcome );
    hydrateProgress( outcome );
  }
}

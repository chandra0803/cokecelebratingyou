
package com.biperf.core.service.throwdown;

import com.biperf.core.domain.promotion.Match;
import com.biperf.core.domain.promotion.MatchTeamOutcome;
import com.biperf.core.service.BaseAssociationRequest;

public class MatchAssociationRequest extends BaseAssociationRequest
{

  private int hydrateLevel = 0;

  public static final int ALL = 1;
  public static final int ROUND = 2;
  public static final int MATCH_TEAM_OUTCOME = 3;
  public static final int TEAM = 4;
  public static final int MATCH_TEAM_PROGRESS = 5;
  public static final int PARTICIPANT = 6;

  public MatchAssociationRequest( int hydrateLevel )
  {
    this.hydrateLevel = hydrateLevel;
  }

  public void execute( Object domainObject )
  {
    Match match = (Match)domainObject;

    switch ( hydrateLevel )
    {
      case ALL:
        hydrateAll( match );
        break;
      case ROUND:
        hydrateRound( match );
        break;
      case MATCH_TEAM_OUTCOME:
        hydrateMatchTeamOutcome( match );
        break;
      case MATCH_TEAM_PROGRESS:
        hydrateTeamProgress( match );
        break;
      case TEAM:
        hydrateTeam( match );
        break;
      default:
        break;
    }
  }

  private void hydrateRound( Match match )
  {
    initialize( match.getRound() );
  }

  private void hydrateMatchTeamOutcome( Match match )
  {
    initialize( match.getTeamOutcomes() );
  }

  private void hydrateTeam( Match match )
  {
    for ( MatchTeamOutcome outcome : match.getTeamOutcomes() )
    {
      initialize( outcome.getTeam() );
      initialize( outcome.getTeam().getParticipant() );
    }
  }

  private void hydrateTeamProgress( Match match )
  {
    for ( MatchTeamOutcome outcome : match.getTeamOutcomes() )
    {
      initialize( outcome.getProgress() );
    }
  }

  private void hydrateAll( Match match )
  {
    hydrateRound( match );
    hydrateMatchTeamOutcome( match );
    hydrateTeam( match );
    hydrateTeamProgress( match );
  }
}

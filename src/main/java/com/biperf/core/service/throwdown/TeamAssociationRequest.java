
package com.biperf.core.service.throwdown;

import com.biperf.core.domain.promotion.Team;
import com.biperf.core.service.BaseAssociationRequest;

public class TeamAssociationRequest extends BaseAssociationRequest
{
  private int hydrateLevel = 0;

  public static final int ALL = 1;
  public static final int PARTICIPANT = 2;
  public static final int MATCH_TEAM_OUTCOME = 3;

  public TeamAssociationRequest( int hydrateLevel )
  {
    this.hydrateLevel = hydrateLevel;
  }

  public void execute( Object domainObject )
  {
    Team team = (Team)domainObject;

    switch ( hydrateLevel )
    {
      case ALL:
        hydrateAll( team );
        break;
      case PARTICIPANT:
        hydrateParticipant( team );
        break;
      case MATCH_TEAM_OUTCOME:
        hydrateMatchTeamOutcome( team );
        break;
      default:
        break;
    }
  }

  private void hydrateParticipant( Team team )
  {
    initialize( team.getParticipant() );
  }

  private void hydrateMatchTeamOutcome( Team team )
  {
    initialize( team.getMatchTeamOutcomes() );
  }

  private void hydrateAll( Team team )
  {
    hydrateParticipant( team );
    hydrateMatchTeamOutcome( team );
  }
}

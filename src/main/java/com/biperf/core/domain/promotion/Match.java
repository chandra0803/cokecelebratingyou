
package com.biperf.core.domain.promotion;

import java.util.Set;

import com.biperf.core.domain.BaseDomain;
import com.biperf.core.domain.enums.MatchStatusType;
import com.biperf.core.domain.enums.MatchTeamOutcomeType;

@SuppressWarnings( "serial" )
public class Match extends BaseDomain implements Cloneable
{
  private Round round;
  private Set<MatchTeamOutcome> teamOutcomes;
  private MatchStatusType status;

  public Round getRound()
  {
    return round;
  }

  public void setRound( Round round )
  {
    this.round = round;
  }

  public Set<MatchTeamOutcome> getTeamOutcomes()
  {
    return teamOutcomes;
  }

  public void setTeamOutcomes( Set<MatchTeamOutcome> teamOutcomes )
  {
    this.teamOutcomes = teamOutcomes;
  }

  public MatchStatusType getStatus()
  {
    return status;
  }

  public void setStatus( MatchStatusType status )
  {
    this.status = status;
  }

  // helper methods
  public MatchTeamOutcome getWinner()
  {
    for ( MatchTeamOutcome team : teamOutcomes )
    {
      if ( team.getOutcome().getCode().equals( MatchTeamOutcomeType.WIN ) )
      {
        return team;
      }
    }
    return null;
  }

  public MatchTeamOutcome getLoser()
  {
    for ( MatchTeamOutcome team : teamOutcomes )
    {
      if ( team.getOutcome().getCode().equals( MatchTeamOutcomeType.LOSS ) )
      {
        return team;
      }
    }
    return null;
  }

  public MatchTeamOutcome getTeamOutcome( Long teamId )
  {
    for ( MatchTeamOutcome team : teamOutcomes )
    {
      if ( team.getTeam().getId().equals( teamId ) )
      {
        return team;
      }
    }
    return null;
  }

  public MatchTeamOutcome getOpponentTeamOutcome( Long teamId )
  {
    for ( MatchTeamOutcome team : teamOutcomes )
    {
      if ( !team.getTeam().getId().equals( teamId ) )
      {
        return team;
      }
    }
    return null;
  }

  @Override
  public boolean equals( Object object )
  {
    if ( this == object )
    {
      return true;
    }
    if ( ! ( object instanceof Match ) )
    {
      return false;
    }

    final Match match = (Match)object;

    if ( getRound() != null ? !getRound().equals( match.getRound() ) : match.getRound() != null )
    {
      return false;
    }

    Team team1 = null != getTeamOutcomes() && !getTeamOutcomes().isEmpty() ? getTeamOutcomes().iterator().next().getTeam() : null;
    Team team2 = null != match.getTeamOutcomes() && !match.getTeamOutcomes().isEmpty() ? match.getTeamOutcomes().iterator().next().getTeam() : null;
    // I think we may need a guid here....
    if ( team1 == null && team2 == null )
    {
      return true;
    }
    if ( null != team1 && null != team2 )
    {
      return team1.getId().equals( team2.getId() );
    }
    else
    {
      return false;
    }
  }

  @Override
  public int hashCode()
  {
    int result;
    result = getRound() != null ? getRound().hashCode() : 0;
    result = 31 * result + ( getTeamOutcomes() != null ? getTeamOutcomes().hashCode() : 0 );

    return result;
  }
}

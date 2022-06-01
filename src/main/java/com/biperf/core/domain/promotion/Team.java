
package com.biperf.core.domain.promotion;

import java.util.HashSet;
import java.util.Set;

import com.biperf.core.domain.BaseDomain;
import com.biperf.core.domain.participant.Participant;

@SuppressWarnings( "serial" )
public class Team extends BaseDomain implements Cloneable
{
  private boolean shadowPlayer = false;
  private Set<MatchTeamOutcome> matchTeamOutcomes = new HashSet<MatchTeamOutcome>();
  private Participant participant;
  private Division division;
  private Boolean active;

  public Participant getParticipant()
  {
    return participant;
  }

  public void setParticipant( Participant participant )
  {
    this.participant = participant;
  }

  public Set<MatchTeamOutcome> getMatchTeamOutcomes()
  {
    return matchTeamOutcomes;
  }

  public void setMatchTeamOutcomes( Set<MatchTeamOutcome> matchTeamOutcomes )
  {
    this.matchTeamOutcomes = matchTeamOutcomes;
  }

  public Division getDivision()
  {
    return division;
  }

  public void setDivision( Division division )
  {
    this.division = division;
  }

  public boolean isShadowPlayer()
  {
    return shadowPlayer;
  }

  public void setShadowPlayer( boolean shadowPlayer )
  {
    this.shadowPlayer = shadowPlayer;
  }

  @Override
  public boolean equals( Object object )
  {
    if ( this == object )
    {
      return true;
    }
    if ( ! ( object instanceof Team ) )
    {
      return false;
    }

    final Team team = (Team)object;

    if ( getParticipant() != null ? !getParticipant().equals( team.getParticipant() ) : team.getParticipant() != null )
    {
      return false;
    }

    if ( getId() != null ? !getId().equals( team.getId() ) : team.getId() != null )
    {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode()
  {
    int result;
    result = getParticipant() != null ? getParticipant().hashCode() : 0;
    return result;
  }

  @Override
  protected Object clone() throws CloneNotSupportedException
  {
    return super.clone();
  }

  @Override
  public String toString()
  {
    StringBuilder sb = new StringBuilder();
    sb.append( "id: " + getId() + "\n" );
    sb.append( "shadowPlayer: " + shadowPlayer + "\n" );
    return sb.toString();
  }

  public Boolean getActive()
  {
    return active;
  }

  public void setActive( Boolean active )
  {
    this.active = active;
  }
}

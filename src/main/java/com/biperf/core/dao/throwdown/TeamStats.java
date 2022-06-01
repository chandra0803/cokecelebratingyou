
package com.biperf.core.dao.throwdown;

import java.io.Serializable;

import com.biperf.core.domain.enums.MatchTeamOutcomeType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@SuppressWarnings( "serial" )
@JsonInclude( value = Include.NON_NULL )
public class TeamStats implements Serializable
{
  private int none = 0;
  private int wins = 0;
  private int losses = 0;
  private int ties = 0;
  private int byes = 0;
  private int forfeits = 0;
  private int disqualifications = 0;

  @JsonProperty( "none" )
  public int getNone()
  {
    return none;
  }

  @JsonProperty( "forfeits" )
  public int getForfeits()
  {
    return forfeits;
  }

  @JsonIgnore
  public void setForfeits( int forfeits )
  {
    this.forfeits = forfeits;
  }

  @JsonIgnore
  public void setNone( int none )
  {
    this.none = none;
  }

  @JsonProperty( "wins" )
  public int getWins()
  {
    return wins;
  }

  @JsonIgnore
  public void setWins( int wins )
  {
    this.wins = wins;
  }

  @JsonProperty( "losses" )
  public int getLosses()
  {
    return losses;
  }

  @JsonIgnore
  public void setLosses( int losses )
  {
    this.losses = losses;
  }

  @JsonProperty( "ties" )
  public int getTies()
  {
    return ties;
  }

  @JsonIgnore
  public void setTies( int ties )
  {
    this.ties = ties;
  }

  @JsonProperty( "byes" )
  public int getByes()
  {
    return byes;
  }

  @JsonIgnore
  public void setByes( int byes )
  {
    this.byes = byes;
  }

  @JsonProperty( "disqualifications" )
  public int getDisqualifications()
  {
    return disqualifications;
  }

  @JsonIgnore
  public void setDisqualifications( int disqualifications )
  {
    this.disqualifications = disqualifications;
  }

  @JsonIgnore
  public void setTypeProperty( String measure, int value )
  {
    if ( null == measure )
    {
      return;
    }

    if ( MatchTeamOutcomeType.WIN.equals( measure ) )
    {
      this.wins = value;
    }
    else if ( MatchTeamOutcomeType.LOSS.equals( measure ) )
    {
      this.losses = value;
    }
    else if ( MatchTeamOutcomeType.NONE.equals( measure ) )
    {
      this.none = value;
    }
    else if ( MatchTeamOutcomeType.TIE.equals( measure ) )
    {
      this.ties = value;
    }
    else if ( MatchTeamOutcomeType.BYE.equals( measure ) )
    {
      this.byes = value;
    }
    else if ( MatchTeamOutcomeType.FORFEIT.equals( measure ) )
    {
      this.forfeits = value;
    }
    else if ( MatchTeamOutcomeType.DISQUALIFIED.equals( measure ) )
    {
      this.disqualifications = value;
    }
  }

  @Override
  @JsonIgnore
  public String toString()
  {
    StringBuilder sb = new StringBuilder();
    sb.append( "{" );
    sb.append( "wins=" + wins + "\n" );
    sb.append( "losses=" + losses + "\n" );
    sb.append( "ties=" + ties + "\n" );
    sb.append( "byes=" + byes + "\n" );
    sb.append( "none/not played=" + none + "\n" );
    sb.append( "forfeits=" + forfeits + "\n" );
    sb.append( "disqualifications=" + disqualifications + "\n" );
    sb.append( "}" );
    return sb.toString();
  }
}

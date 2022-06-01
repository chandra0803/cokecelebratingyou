
package com.biperf.core.dao.throwdown;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@SuppressWarnings( "serial" )
public class Standing implements Serializable
{
  private Long teamId;
  private Long userId;
  private String firstName;
  private String lastName;
  private String avatarUrl;

  private TeamStats stats;

  @JsonProperty( "id" )
  public Long getUserId()
  {
    return userId;
  }

  @JsonIgnore
  public void setUserId( Long userId )
  {
    this.userId = userId;
  }

  public String getFirstName()
  {
    return firstName;
  }

  public void setFirstName( String firstName )
  {
    this.firstName = firstName;
  }

  public String getLastName()
  {
    return lastName;
  }

  public void setLastName( String lastName )
  {
    this.lastName = lastName;
  }

  public TeamStats getStats()
  {
    return stats;
  }

  public void setStats( TeamStats stats )
  {
    this.stats = stats;
  }

  public Long getTeamId()
  {
    return teamId;
  }

  public void setTeamId( Long teamId )
  {
    this.teamId = teamId;
  }

  @JsonProperty( "loss" )
  public int getLosses()
  {
    return this.stats.getLosses();
  }

  @JsonProperty( "win" )
  public int getWin()
  {
    return this.stats.getWins();
  }

  @JsonProperty( "tie" )
  public int getTie()
  {
    return this.stats.getTies();
  }

  @JsonProperty( "name" )
  public String getNameLFWithComma()
  {
    StringBuffer fullName = new StringBuffer();
    if ( this.lastName != null )
    {
      fullName.append( this.lastName ).append( ", " );
    }
    if ( this.firstName != null )
    {
      fullName.append( this.firstName );
    }

    return fullName.toString();
  }

  public String toString()
  {
    StringBuilder sb = new StringBuilder();
    sb.append( "teamId=" + teamId );
    sb.append( "\nuserId=" + userId );
    sb.append( "\nfirstName=" + firstName );
    sb.append( "\nlastName=" + lastName );
    sb.append( "\nStats=" + stats );
    return sb.toString();
  }

  public String getAvatarUrl()
  {
    return this.avatarUrl;
  }

  public void setAvatarUrl( String avatarUrl )
  {
    this.avatarUrl = avatarUrl;
  }
}

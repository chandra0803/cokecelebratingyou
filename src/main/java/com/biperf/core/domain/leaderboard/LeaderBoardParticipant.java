
package com.biperf.core.domain.leaderboard;

import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

import com.biperf.core.domain.BaseDomain;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.utils.NumberFormatUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class LeaderBoardParticipant extends BaseDomain
{
  // ---------------------------------------------------------------------------
  // Fields
  // ---------------------------------------------------------------------------

  private boolean active;

  private String score;

  private Date asOfDate;

  private Set leaderboardparticipant = new LinkedHashSet();

  private LeaderBoard leaderboard;

  private Participant user;

  private Boolean currentUser;

  public static final String ALL_LB_PAX = "all";
  public static final String ACTIVE_LB_PAX = "active";
  public static final String IN_ACTIVE_LB_PAX = "inactive";

  // persisted variable
  public long participantRank;

  // transient variable only for reference
  // TODO have to get rid of this once ranking implemented
  public int leaderBoardPaxRank;

  // ---------------------------------------------------------------------------
  // Constructors
  // ---------------------------------------------------------------------------

  /**
   * Constructs a {@link LeaderBoardParticipant} object.
   */
  public LeaderBoardParticipant()
  {
    // Default constructor
  }

  // ---------------------------------------------------------------------------
  // Getter and Setter Methods
  // ---------------------------------------------------------------------------

  /**
   * Get  isActive
   * 
   * @return isActive
   */

  @JsonIgnore
  public String getScore()
  {
    return score;
  }

  @JsonProperty( "score" )
  public String displayScore()
  {
    String formattedScore = "0";
    try
    {
      formattedScore = NumberFormatUtil.getLocaleBasedNumberFormat( Long.parseLong( score ) );
    }
    catch( NumberFormatException nfe )
    {
      nfe.printStackTrace();
    }
    return formattedScore;
  }

  public boolean isActive()
  {
    return active;
  }

  public void setActive( boolean active )
  {
    this.active = active;
  }

  public void setScore( String score )
  {
    this.score = score;
  }

  @JsonIgnore
  public Date getAsOfDate()
  {
    return asOfDate;
  }

  public void setAsOfDate( Date asOfDate )
  {
    this.asOfDate = asOfDate;
  }

  /**
   * @return value of Leaderboardparticipant 
   */
  @JsonIgnore
  public Set getLeaderboardparticipant()
  {
    return leaderboardparticipant;
  }

  /**
   * @param  value for Leaderboardparticipant 
   */
  public void setLeaderboardparticipant( Set leaderboardparticipant )
  {
    this.leaderboardparticipant = leaderboardparticipant;
  }

  /**
   * @return value of LeaderBoard
   */
  @JsonIgnore
  public LeaderBoard getLeaderboard()
  {
    return leaderboard;
  }

  /**
   * @param value for leaderBoard
   */
  public void setLeaderboard( LeaderBoard leaderboard )
  {
    this.leaderboard = leaderboard;
  }

  /**
   * @return value of LeaderBoard
   */

  // ---------------------------------------------------------------------------
  // Equals, Hashcode, and To String Methods
  // ---------------------------------------------------------------------------

  public Boolean getCurrentUser()
  {
    return currentUser;
  }

  public void setCurrentUser( Boolean currentUser )
  {
    this.currentUser = currentUser;
  }

  @JsonIgnore
  public int getLeaderBoardPaxRank()
  {
    return leaderBoardPaxRank;
  }

  public void setLeaderBoardPaxRank( int leaderBoardPaxRank )
  {
    this.leaderBoardPaxRank = leaderBoardPaxRank;
  }

  @JsonIgnore
  public Participant getUser()
  {
    return user;
  }

  public void setUser( Participant user )
  {
    this.user = user;
  }

  @JsonProperty( "firstName" )
  public String getUserFirstName()
  {
    return user.getFirstName();
  }

  @JsonProperty( "lastName" )
  public String getUserLastName()
  {
    return user.getLastName();
  }

  @JsonProperty( "participantId" )
  public Long getUserId()
  {
    return user.getId();
  }

  @JsonProperty( "avatarUrl" )
  public String getAvatarUrl()
  {
    return user.getAvatarSmall();
  }

  @JsonProperty( "rank" )
  public long getParticipantRank()
  {
    return participantRank;
  }

  public void setParticipantRank( long participantRank )
  {
    this.participantRank = participantRank;
  }

  /**
   * Define the hashCode from the id. Overridden from
   * 
   * @see com.biperf.core.domain.BaseDomain#hashCode()
   * @return int
   */

  @JsonIgnore
  public String toString()
  {
    final StringBuffer buf = new StringBuffer();

    buf.append( "{Id=" ).append( this.getId() ).append( "}" );
    buf.append( "{userId=" ).append( this.getUser().getId() ).append( "}" );
    buf.append( "]" );
    return buf.toString();
  }

  @Override
  @JsonIgnore
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result + ( active ? 1231 : 1237 );
    result = prime * result + ( asOfDate == null ? 0 : asOfDate.hashCode() );
    result = prime * result + ( currentUser == null ? 0 : currentUser.hashCode() );
    result = prime * result + ( leaderboard == null ? 0 : leaderboard.hashCode() );
    result = prime * result + ( leaderboardparticipant == null ? 0 : leaderboardparticipant.hashCode() );
    result = prime * result + ( score == null ? 0 : score.hashCode() );
    result = prime * result + ( user == null ? 0 : user.hashCode() );
    return result;
  }

  /**
   * Checks equality of the object parameter to this. Overridden from
   * 
   * @see com.biperf.core.domain.BaseDomain#equals(java.lang.Object)
   * @param object
   * @return boolean
   */
  @Override
  @JsonIgnore
  public boolean equals( Object obj )
  {
    if ( this == obj )
    {
      return true;
    }
    if ( obj == null )
    {
      return false;
    }
    if ( getClass() != obj.getClass() )
    {
      return false;
    }
    LeaderBoardParticipant other = (LeaderBoardParticipant)obj;
    if ( active != other.active )
    {
      return false;
    }
    if ( asOfDate == null )
    {
      if ( other.asOfDate != null )
      {
        return false;
      }
    }
    else if ( !asOfDate.equals( other.asOfDate ) )
    {
      return false;
    }
    if ( currentUser == null )
    {
      if ( other.currentUser != null )
      {
        return false;
      }
    }
    else if ( !currentUser.equals( other.currentUser ) )
    {
      return false;
    }
    if ( participantRank != other.participantRank )
    {
      return false;
    }
    if ( leaderboard == null )
    {
      if ( other.leaderboard != null )
      {
        return false;
      }
    }
    else if ( !leaderboard.equals( other.leaderboard ) )
    {
      return false;
    }
    if ( leaderboardparticipant == null )
    {
      if ( other.leaderboardparticipant != null )
      {
        return false;
      }
    }
    else if ( !leaderboardparticipant.equals( other.leaderboardparticipant ) )
    {
      return false;
    }
    if ( score == null )
    {
      if ( other.score != null )
      {
        return false;
      }
    }
    else if ( !score.equals( other.score ) )
    {
      return false;
    }
    if ( user == null )
    {
      if ( other.user != null )
      {
        return false;
      }
    }
    else if ( !user.equals( other.user ) )
    {
      return false;
    }
    return true;
  }

}

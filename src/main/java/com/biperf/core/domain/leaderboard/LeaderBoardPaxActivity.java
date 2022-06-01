
package com.biperf.core.domain.leaderboard;

import java.util.Date;

import com.biperf.core.domain.BaseDomain;
import com.biperf.core.domain.user.User;

public class LeaderBoardPaxActivity extends BaseDomain
{
  // ---------------------------------------------------------------------------
  // Fields
  // ---------------------------------------------------------------------------

  private String type;

  private Date asofDate;

  private Date submissionDate;

  private LeaderBoard leaderBoard;

  private User user;

  // ---------------------------------------------------------------------------
  // Constructors
  // ---------------------------------------------------------------------------

  /**
   * Constructs a {@link LeaderBoardPaxActivity} object.
   */
  public LeaderBoardPaxActivity()
  {
    // Default constructor
  }

  // ---------------------------------------------------------------------------
  // Getter and Setter Methods
  // ---------------------------------------------------------------------------

  /**
   * Get  Type 
   * 
   * @return Type
   */

  public String getType()
  {
    return type;
  }

  public void setType( String type )
  {
    this.type = type;
  }

  public Date getAsofDate()
  {
    return asofDate;
  }

  public void setAsofDate( Date asofDate )
  {
    this.asofDate = asofDate;
  }

  public Date getSubmissionDate()
  {
    return submissionDate;
  }

  public void setSubmissionDate( Date submissionDate )
  {
    this.submissionDate = submissionDate;
  }
  /**
   * @return value of LeaderboardId 
   */

  // ---------------------------------------------------------------------------
  // Equals, Hashcode, and To String Methods
  // ---------------------------------------------------------------------------

  /**
   * Checks equality of the object parameter to this. Overridden from
   * 
   * @see com.biperf.core.domain.BaseDomain#equals(java.lang.Object)
   * @param object
   * @return boolean
   */
  @Override
  public boolean equals( Object object )
  {
    if ( this == object )
    {
      return true;
    }
    if ( ! ( object instanceof LeaderBoardPaxActivity ) )
    {
      return false;
    }
    final LeaderBoardPaxActivity leaderbaordpaxactivity = (LeaderBoardPaxActivity)object;
    if ( getId() != null ? !getId().equals( leaderbaordpaxactivity.getId() ) : leaderbaordpaxactivity.getId() != null )
    {
      return false;
    }
    if ( user.getId() != null ? !user.getId().equals( leaderbaordpaxactivity.getUser().getId() ) : leaderbaordpaxactivity.getUser().getId() != null )
    {
      return false;
    }
    return true;
  }

  public LeaderBoard getLeaderBoard()
  {
    return leaderBoard;
  }

  public void setLeaderBoard( LeaderBoard leaderBoard )
  {
    this.leaderBoard = leaderBoard;
  }

  public User getUser()
  {
    return user;
  }

  public void setUser( User user )
  {
    this.user = user;
  }

  /**
   * Define the hashCode from the id. Overridden from
   * 
   * @see com.biperf.core.domain.BaseDomain#hashCode()
   * @return int
   */
  @Override
  public int hashCode()
  {
    int result = 0;

    result += this.getAsofDate() != null ? this.getAsofDate().hashCode() : 0;

    return result;
  }

  public String toString()
  {
    final StringBuffer buf = new StringBuffer();

    buf.append( "{Id=" ).append( this.getId() ).append( "}" );
    buf.append( "{UserId=" ).append( this.user.getId() ).append( "}" );
    buf.append( "]" );
    return buf.toString();
  }
}

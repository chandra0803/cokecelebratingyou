
package com.biperf.core.domain.fileload;

import java.util.Date;

public class LeaderBoardImportRecord extends ImportRecord
{
  // ---------------------------------------------------------------------------
  // Fields
  // ---------------------------------------------------------------------------
  /**
   * The ID of the user who owns this LeaderBoard. A LeaderBoard is owned by either a node or a user.
   */
  private String userId;
  /**
   * The user name of the user into whose account the leaderboard will be deposited.
   */

  private Long importFileId;

  private String userName;
  /**
   * LeaderBoard Score
   */
  private Long score;
  /**
   * Name of the LeaderBoard
   */
  private String leaderBoardName;
  /**
   * The leaderBoard will be displayed as per the date
   */
  private Date asOfDate;
  /**
   * The LeaderBoard Score can be added or replaced
   */
  private Long action;

  private Long Version;

  // ---------------------------------------------------------------------------
  // Getter and Setter Methods
  // ---------------------------------------------------------------------------

  /**
   * Returns the LeaderBoard UserId.
   * 
   * @return the LeaderBoard UserId.
   */
  public String getUserId()
  {
    return userId;
  }

  /**
   * Sets the LeaderBoard UserId.
   * 
   * @param LeaderBoard userId.
   */
  public void setUserId( String userId )
  {
    this.userId = userId;
  }

  /**
   * Returns the LeaderBoard UserName.
   * 
   * @return the LeaderBoard UserName.
   */
  public String getUserName()
  {
    return userName;
  }

  /**
   * Sets the LeaderBoard UserName.
   * 
   * @param LeaderBoard userName.
   */
  public void setUserName( String userName )
  {
    this.userName = userName;
  }

  public Long getScore()
  {
    return score;
  }

  public void setScore( Long score )
  {
    this.score = score;
  }

  /**
   * Returns the LeaderBoard Name.
   * 
   * @return the leaderBoardName.
   */
  public String getLeaderBoardName()
  {
    return leaderBoardName;
  }

  /**
   * Sets the LeaderBoard Name.
   * 
   * @param leaderBoardName.
   */
  public void setLeaderBoardName( String leaderBoardName )
  {
    this.leaderBoardName = leaderBoardName;
  }

  /**
   * Returns the LeaderBoard AsOfDate.
   * 
   * @return asOfDate.
   */
  public Date getAsOfDate()
  {
    return asOfDate;
  }

  /**
   * Sets the LeaderBoard AsOfDate.
   * 
   * @param LeaderBoard asOfDate.
   */
  public void setAsOfDate( Date asOfDate )
  {
    this.asOfDate = asOfDate;
  }

  /**
   * Returns the LeaderBoard Action.
   * 
   * @return the LeaderBoard action.
   */
  public Long getAction()
  {
    return action;
  }

  /**
   * Sets the LeaderBoard Action.
   * 
   * @param LeaderBoard action.
   */
  public void setAction( Long action )
  {
    this.action = action;
  }

  /**
  * Returns the LeaderBoard Version.
  * 
  * @return the LeaderBoard Version.
  */
  public Long getVersion()
  {
    return Version;
  }

  /**
   * Sets the LeaderBoard Version.
   * 
   * @param LeaderBoard version.
   */
  public void setVersion( Long version )
  {
    Version = version;
  }

  public Long getImportFileId()
  {
    return importFileId;
  }

  public void setImportFileId( Long importFileId )
  {
    this.importFileId = importFileId;
  }

  // ---------------------------------------------------------------------------
  // Equals, Hashcode, and To String Methods
  // ---------------------------------------------------------------------------

  /**
   * Returns true if the given object and this object are equal; returns false otherwise.
   * 
   * @see com.biperf.core.domain.BaseDomain#equals(java.lang.Object)
   * @param object the object to compare to this object.
   * @return true if the given object and this object are equal; false otherwise.
   */
  @Override
  public boolean equals( Object object )
  {
    if ( this == object )
    {
      return true;
    }

    if ( ! ( object instanceof LeaderBoardImportRecord ) )
    {
      return false;
    }

    LeaderBoardImportRecord leaderboardImportRecord = (LeaderBoardImportRecord)object;

    if ( this.getId() != null && this.getId().equals( leaderboardImportRecord.getId() ) )
    {
      return true;
    }
    return false;
  }

  /**
   * Returns the hashcode for this object.
   * 
   * @see com.biperf.core.domain.BaseDomain#hashCode()
   * @return the hashcode for this object.
   */
  @Override
  public int hashCode()
  {
    return this.getId() != null ? this.getId().hashCode() : 0;
  }

  /**
   * Returns a string representation of this object.
   * 
   * @see java.lang.Object#toString()
   * @return a string representation of this object.
   */
  public String toString()
  {
    final StringBuffer buf = new StringBuffer();
    buf.append( "LeaderBoardImportRecord [" );
    buf.append( "{id=" ).append( super.getId() ).append( "}, " );
    buf.append( "]" );
    return buf.toString();
  }

}

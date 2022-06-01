
package com.biperf.core.domain.fileload;

import java.util.Date;

public class SSIProgressImportRecord extends ImportRecord
{

  // ---------------------------------------------------------------------------
  // Fields
  // ---------------------------------------------------------------------------

  private String userName;
  private String firstName;
  private String lastName;
  private String nodeName;

  private Long progress;
  private Long ssiContestId;
  private Long userId;

  private String emailAddress;
  private String activityDescription;
  private Long ssiContestActivityId;
  private Date activityDate;

  // ---------------------------------------------------------------------------
  // Getter and Setter Methods
  // ---------------------------------------------------------------------------

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
  public boolean equals( Object object )
  {
    if ( this == object )
    {
      return true;
    }

    if ( ! ( object instanceof SSIProgressImportRecord ) )
    {
      return false;
    }

    SSIProgressImportRecord ssiProgressImportRecord = (SSIProgressImportRecord)object;

    if ( this.getId() != null && this.getId().equals( ssiProgressImportRecord.getId() ) )
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
    buf.append( "SSIProgressImportRecord [" );
    buf.append( "{id=" ).append( super.getId() ).append( "}, " );
    buf.append( "]" );
    return buf.toString();
  }

  public String getUserName()
  {
    return userName;
  }

  public void setUserName( String userName )
  {
    this.userName = userName;
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

  public String getNodeName()
  {
    return nodeName;
  }

  public void setNodeName( String nodeName )
  {
    this.nodeName = nodeName;
  }

  public Long getProgress()
  {
    return progress;
  }

  public void setProgress( Long progress )
  {
    this.progress = progress;
  }

  public Long getSsiContestId()
  {
    return ssiContestId;
  }

  public void setSsiContestId( Long ssiContestId )
  {
    this.ssiContestId = ssiContestId;
  }

  public Long getUserId()
  {
    return userId;
  }

  public void setUserId( Long userId )
  {
    this.userId = userId;
  }

  public String getEmailAddress()
  {
    return emailAddress;
  }

  public void setEmailAddress( String emailAddress )
  {
    this.emailAddress = emailAddress;
  }

  public String getActivityDescription()
  {
    return activityDescription;
  }

  public void setActivityDescription( String activityDescription )
  {
    this.activityDescription = activityDescription;
  }

  public Long getSsiContestActivityId()
  {
    return ssiContestActivityId;
  }

  public void setSsiContestActivityId( Long ssiContestActivityId )
  {
    this.ssiContestActivityId = ssiContestActivityId;
  }

  public Date getActivityDate()
  {
    return activityDate;
  }

  public void setActivityDate( Date activityDate )
  {
    this.activityDate = activityDate;
  }

}


package com.biperf.core.domain.fileload;

public class PaxGoalImportRecord extends ImportRecord
{
  // ---------------------------------------------------------------------------
  // Fields
  // ---------------------------------------------------------------------------

  /**
   * The ID of a participant whose base objective to be modified by the Pax Base File Load.
   */
  private Long userId;

  /**
   * The Login ID of the user (participant) whose base objective to be modified by the Pax Base File Load.
   */
  private String userName;

  /**
   * The first name of the user (participant) whose base objective to be modified by the Pax Base File Load.
   */
  private String firstName;

  /**
   * The last name of the user (participant) whose base objective to be modified by the Pax Base File Load.
   */
  private String lastName;

  /**
   * The email address of the user (participant) whose base objective to be modified by the Pax Base File Load.
   */
  private String emailAddr;

  /**
   * The name of the goal level the user (participant) will be assigned.
   */
  private String goalLevelName;

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

    if ( ! ( object instanceof PaxGoalImportRecord ) )
    {
      return false;
    }

    PaxGoalImportRecord importRecord = (PaxGoalImportRecord)object;

    if ( this.getId() != null && this.getId().equals( importRecord.getId() ) )
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
    buf.append( "PaxGoalImportRecord [" );
    buf.append( "{id=" ).append( super.getId() ).append( "}, " );
    buf.append( "]" );
    return buf.toString();
  }

  // ---------------------------------------------------------------------------
  // Getter and Setter Methods
  // ---------------------------------------------------------------------------

  public Long getUserId()
  {
    return userId;
  }

  public void setUserId( Long userId )
  {
    this.userId = userId;
  }

  public String getEmailAddr()
  {
    return emailAddr;
  }

  public void setEmailAddr( String emailAddr )
  {
    this.emailAddr = emailAddr;
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

  public String getUserName()
  {
    return userName;
  }

  public void setUserName( String userName )
  {
    this.userName = userName;
  }

  public String getGoalLevelName()
  {
    return goalLevelName;
  }

  public void setGoalLevelName( String goalLevelName )
  {
    this.goalLevelName = goalLevelName;
  }
}

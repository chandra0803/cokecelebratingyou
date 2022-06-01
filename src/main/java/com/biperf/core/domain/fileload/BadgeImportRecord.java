
package com.biperf.core.domain.fileload;

public class BadgeImportRecord extends ImportRecord
{
  // ---------------------------------------------------------------------------
  // Fields
  // ---------------------------------------------------------------------------

  private String badgeName;
  private String userName;
  private Long userId;

  public String getBadgeName()
  {
    return badgeName;
  }

  public void setBadgeName( String badgeName )
  {
    this.badgeName = badgeName;
  }

  public String getUserName()
  {
    return userName;
  }

  public void setUserName( String userName )
  {
    this.userName = userName;
  }

  // ---------------------------------------------------------------------------
  // Equals, Hashcode, and To String Methods
  // ---------------------------------------------------------------------------

  public Long getUserId()
  {
    return userId;
  }

  public void setUserId( Long userId )
  {
    this.userId = userId;
  }

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

    if ( ! ( object instanceof BadgeImportRecord ) )
    {
      return false;
    }

    BadgeImportRecord badgeImportRecord = (BadgeImportRecord)object;

    if ( this.getId() != null && this.getId().equals( badgeImportRecord.getId() ) )
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
    buf.append( "BadgeImportRecord [" );
    buf.append( "{id=" ).append( super.getId() ).append( "}, " );
    buf.append( "]" );
    return buf.toString();
  }

}

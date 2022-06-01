
package com.biperf.core.domain.fileload;

public class SSIDtgtContestImportRecord extends ImportRecord
{
  private static final long serialVersionUID = 1L;

  private Long userId;
  private String userName;
  private String firstName;
  private String lastName;
  private String role;

  public boolean equals( Object object )
  {
    if ( this == object )
    {
      return true;
    }

    if ( ! ( object instanceof SSIDtgtContestImportRecord ) )
    {
      return false;
    }

    SSIDtgtContestImportRecord ssiDtgtContestImportRecord = (SSIDtgtContestImportRecord)object;

    if ( this.getId() != null && this.getId().equals( ssiDtgtContestImportRecord.getId() ) )
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

  public Long getUserId()
  {
    return userId;
  }

  public void setUserId( Long userId )
  {
    this.userId = userId;
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

  public String getRole()
  {
    return role;
  }

  public void setRole( String role )
  {
    this.role = role;
  }

}


package com.biperf.core.domain.fileload;

import java.math.BigDecimal;

public class SSISiuContestImportRecord extends ImportRecord
{
  private static final long serialVersionUID = 1L;

  private Long userId;
  private String userName;
  private String firstName;
  private String lastName;
  private String role;
  private BigDecimal baseLine;

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

    SSISiuContestImportRecord ssiSiuContestImportRecord = (SSISiuContestImportRecord)object;

    if ( this.getId() != null && this.getId().equals( ssiSiuContestImportRecord.getId() ) )
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

  public BigDecimal getBaseLine()
  {
    return baseLine;
  }

  public void setBaseLine( BigDecimal baseLine )
  {
    this.baseLine = baseLine;
  }

}

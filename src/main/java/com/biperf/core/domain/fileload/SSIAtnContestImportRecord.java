
package com.biperf.core.domain.fileload;

import java.math.BigDecimal;

public class SSIAtnContestImportRecord extends ImportRecord
{
  private static final long serialVersionUID = 1L;

  private Long userId;
  private String userName;
  private String firstName;
  private String lastName;
  private String role;
  private String activityDescription;
  private BigDecimal activityAmount;
  private BigDecimal payoutAmount;
  private String payoutDescription;
  private BigDecimal awardIssueValue;

  public boolean equals( Object object )
  {
    if ( this == object )
    {
      return true;
    }

    if ( ! ( object instanceof SSIAtnContestImportRecord ) )
    {
      return false;
    }

    SSIAtnContestImportRecord ssiAtnContestImportRecord = (SSIAtnContestImportRecord)object;

    if ( this.getId() != null && this.getId().equals( ssiAtnContestImportRecord.getId() ) )
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

  public String getActivityDescription()
  {
    return activityDescription;
  }

  public void setActivityDescription( String activityDescription )
  {
    this.activityDescription = activityDescription;
  }

  public BigDecimal getActivityAmount()
  {
    return activityAmount;
  }

  public void setActivityAmount( BigDecimal activityAmount )
  {
    this.activityAmount = activityAmount;
  }

  public BigDecimal getPayoutAmount()
  {
    return payoutAmount;
  }

  public void setPayoutAmount( BigDecimal payoutAmount )
  {
    this.payoutAmount = payoutAmount;
  }

  public String getPayoutDescription()
  {
    return payoutDescription;
  }

  public void setPayoutDescription( String payoutDescription )
  {
    this.payoutDescription = payoutDescription;
  }

  public BigDecimal getAwardIssueValue()
  {
    return awardIssueValue;
  }

  public void setAwardIssueValue( BigDecimal awardIssueValue )
  {
    this.awardIssueValue = awardIssueValue;
  }

}

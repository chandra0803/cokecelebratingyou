
package com.biperf.core.domain.fileload;

import java.math.BigDecimal;

public class SSIObjContestImportRecord extends ImportRecord
{
  private static final long serialVersionUID = 1L;

  private Long userId;
  private String userName;
  private String firstName;
  private String lastName;
  private String role;
  private String objectiveDescription;
  private BigDecimal objectiveAmount;
  private BigDecimal objectivePayout;
  private String otherPayoutDescription;
  private BigDecimal otherValue;
  private BigDecimal bonusForEvery;
  private BigDecimal bonusPayout;
  private BigDecimal bonusPayoutCap;

  public boolean equals( Object object )
  {
    if ( this == object )
    {
      return true;
    }

    if ( ! ( object instanceof SSIObjContestImportRecord ) )
    {
      return false;
    }

    SSIObjContestImportRecord ssiObjContestImportRecord = (SSIObjContestImportRecord)object;

    if ( this.getId() != null && this.getId().equals( ssiObjContestImportRecord.getId() ) )
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

  public String getObjectiveDescription()
  {
    return objectiveDescription;
  }

  public void setObjectiveDescription( String objectiveDescription )
  {
    this.objectiveDescription = objectiveDescription;
  }

  public BigDecimal getObjectiveAmount()
  {
    return objectiveAmount;
  }

  public void setObjectiveAmount( BigDecimal objectiveAmount )
  {
    this.objectiveAmount = objectiveAmount;
  }

  public BigDecimal getObjectivePayout()
  {
    return objectivePayout;
  }

  public void setObjectivePayout( BigDecimal objectivePayout )
  {
    this.objectivePayout = objectivePayout;
  }

  public String getOtherPayoutDescription()
  {
    return otherPayoutDescription;
  }

  public void setOtherPayoutDescription( String otherPayoutDescription )
  {
    this.otherPayoutDescription = otherPayoutDescription;
  }

  public BigDecimal getOtherValue()
  {
    return otherValue;
  }

  public void setOtherValue( BigDecimal otherValue )
  {
    this.otherValue = otherValue;
  }

  public BigDecimal getBonusForEvery()
  {
    return bonusForEvery;
  }

  public void setBonusForEvery( BigDecimal bonusForEvery )
  {
    this.bonusForEvery = bonusForEvery;
  }

  public BigDecimal getBonusPayout()
  {
    return bonusPayout;
  }

  public void setBonusPayout( BigDecimal bonusPayout )
  {
    this.bonusPayout = bonusPayout;
  }

  public BigDecimal getBonusPayoutCap()
  {
    return bonusPayoutCap;
  }

  public void setBonusPayoutCap( BigDecimal bonusPayoutCap )
  {
    this.bonusPayoutCap = bonusPayoutCap;
  }
}

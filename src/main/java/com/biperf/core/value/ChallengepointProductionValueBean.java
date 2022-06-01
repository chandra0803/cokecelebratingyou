
package com.biperf.core.value;

import java.math.BigDecimal;

public class ChallengepointProductionValueBean implements Cloneable
{
  private Integer totNbrOfUsers;
  private BigDecimal totBaseQuantity;
  private BigDecimal totCurrentValue;

  public ChallengepointProductionValueBean()
  {
    super();
  }

  /**
   * @param totNbrOfUsers
   * @param totBaseQuantity
   * @param totCurrentValue
   */
  public ChallengepointProductionValueBean( Long totNbrOfUsers, BigDecimal totBaseQuantity, BigDecimal totCurrentValue )
  {
    super();
    this.totNbrOfUsers = new Integer( totNbrOfUsers.intValue() );
    this.totBaseQuantity = totBaseQuantity;
    this.totCurrentValue = totCurrentValue;
  }

  public BigDecimal getTotBaseQuantity()
  {
    return totBaseQuantity;
  }

  public void setTotBaseQuantity( BigDecimal totBaseQuantity )
  {
    this.totBaseQuantity = totBaseQuantity;
  }

  public BigDecimal getTotCurrentValue()
  {
    return totCurrentValue;
  }

  public void setTotCurrentValue( BigDecimal totCurrentValue )
  {
    this.totCurrentValue = totCurrentValue;
  }

  public Integer getTotNbrOfUsers()
  {
    return totNbrOfUsers;
  }

  public void setTotNbrOfUsers( Integer totNbrOfUsers )
  {
    this.totNbrOfUsers = totNbrOfUsers;
  }

}

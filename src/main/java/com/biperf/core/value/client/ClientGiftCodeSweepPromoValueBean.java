
package com.biperf.core.value.client;

import java.io.Serializable;

/**
 * 
 * ClientGiftCodeSweepPromoValueBean.
 * 
 * @author Dudam
 * @since Nov 1, 2016
 * @version 1.0
 */
public class ClientGiftCodeSweepPromoValueBean implements Serializable
{
  private String monthYear;
  private String monthYearDesc;

  public ClientGiftCodeSweepPromoValueBean()
  {
    // empty constructor
  }

  public String getMonthYear()
  {
    return monthYear;
  }

  public void setMonthYear( String monthYear )
  {
    this.monthYear = monthYear;
  }

  public String getMonthYearDesc()
  {
    return monthYearDesc;
  }

  public void setMonthYearDesc( String monthYearDesc )
  {
    this.monthYearDesc = monthYearDesc;
  }

}
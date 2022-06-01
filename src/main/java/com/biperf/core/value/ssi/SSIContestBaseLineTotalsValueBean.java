
package com.biperf.core.value.ssi;

/**
 * 
 * SSIContestBaseLineTotalsValueBean.
 * 
 * @author kandhi
 * @since Jan 26, 2015
 * @version 1.0
 */
public class SSIContestBaseLineTotalsValueBean
{
  private Double baselineTotal;
  private boolean enteredBaseLineForAllPax;

  public Double getBaselineTotal()
  {
    return baselineTotal;
  }

  public void setBaselineTotal( Double baselineTotal )
  {
    this.baselineTotal = baselineTotal;
  }

  public boolean isEnteredBaseLineForAllPax()
  {
    return enteredBaseLineForAllPax;
  }

  public void setEnteredBaseLineForAllPax( boolean enteredBaseLineForAllPax )
  {
    this.enteredBaseLineForAllPax = enteredBaseLineForAllPax;
  }

}


package com.biperf.core.value;

import java.math.BigDecimal;

public class CashCurrencyBean
{

  private BigDecimal bPomEnteredRate;
  private BigDecimal rateMult;
  private BigDecimal rateDiv;

  public BigDecimal getbPomEnteredRate()
  {
    return bPomEnteredRate;
  }

  public void setbPomEnteredRate( BigDecimal bPomEnteredRate )
  {
    this.bPomEnteredRate = bPomEnteredRate;
  }

  public BigDecimal getRateMult()
  {
    return rateMult;
  }

  public void setRateMult( BigDecimal rateMult )
  {
    this.rateMult = rateMult;
  }

  public BigDecimal getRateDiv()
  {
    return rateDiv;
  }

  public void setRateDiv( BigDecimal rateDiv )
  {
    this.rateDiv = rateDiv;
  }

}

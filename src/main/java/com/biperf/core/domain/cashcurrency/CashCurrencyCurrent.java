/*
 * (c) 2016 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.domain.cashcurrency;

import java.util.Date;

import com.biperf.core.domain.BaseDomain;

/**
 * 
 * @author poddutur
 * @since Mar 29, 2016
 */
public class CashCurrencyCurrent extends BaseDomain
{
  private static final long serialVersionUID = -5949084508051494811L;
  private String fromCurrency;
  private String toCurrency;
  private String rtType;
  private Date effectiveDate;
  private float bPomEnteredRate;
  private float rateMult;
  private float rateDiv;

  @Override
  public boolean equals( Object object )
  {
    if ( this == object )
    {
      return true;
    }

    if ( ! ( object instanceof CashCurrencyCurrent ) )
    {
      return false;
    }

    final CashCurrencyCurrent other = (CashCurrencyCurrent)object;

    if ( getId() != null )
    {
      if ( !getId().equals( other.getId() ) )
      {
        return false;
      }
    }

    return true;
  }

  @Override
  public int hashCode()
  {
    int result = 0;
    result += this.getId() != null ? this.getId().hashCode() : 0;
    return result;
  }

  public String getFromCurrency()
  {
    return fromCurrency;
  }

  public void setFromCurrency( String fromCurrency )
  {
    this.fromCurrency = fromCurrency;
  }

  public String getToCurrency()
  {
    return toCurrency;
  }

  public void setToCurrency( String toCurrency )
  {
    this.toCurrency = toCurrency;
  }

  public String getRtType()
  {
    return rtType;
  }

  public void setRtType( String rtType )
  {
    this.rtType = rtType;
  }

  public Date getEffectiveDate()
  {
    return effectiveDate;
  }

  public void setEffectiveDate( Date effectiveDate )
  {
    this.effectiveDate = effectiveDate;
  }

  public float getbPomEnteredRate()
  {
    return bPomEnteredRate;
  }

  public void setbPomEnteredRate( float bPomEnteredRate )
  {
    this.bPomEnteredRate = bPomEnteredRate;
  }

  public float getRateMult()
  {
    return rateMult;
  }

  public void setRateMult( float rateMult )
  {
    this.rateMult = rateMult;
  }

  public float getRateDiv()
  {
    return rateDiv;
  }

  public void setRateDiv( float rateDiv )
  {
    this.rateDiv = rateDiv;
  }
}

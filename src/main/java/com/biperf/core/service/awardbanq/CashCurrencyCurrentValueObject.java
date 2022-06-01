/*
 * (c) 2016 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.service.awardbanq;

import java.io.Serializable;
import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * 
 * @author poddutur
 * @since Mar 29, 2016
 */
@XmlRootElement
public class CashCurrencyCurrentValueObject implements Serializable
{
  private static final long serialVersionUID = 1L;
  private float bpomEnteredRated;
  private Date effDate;
  private String fromCurrency;
  private Date lastUpdDttm;
  private float rateDiv;
  private float rateMult;
  private String rtRateIndex;
  private String rtType;
  private int syncId;
  private int term;
  private String toCurrency;

  public float getBpomEnteredRated()
  {
    return bpomEnteredRated;
  }

  public void setBpomEnteredRated( float bpomEnteredRated )
  {
    this.bpomEnteredRated = bpomEnteredRated;
  }

  public Date getEffDate()
  {
    return effDate;
  }

  public void setEffDate( Date effDate )
  {
    this.effDate = effDate;
  }

  public String getFromCurrency()
  {
    return fromCurrency;
  }

  public void setFromCurrency( String fromCurrency )
  {
    this.fromCurrency = fromCurrency;
  }

  public Date getLastUpdDttm()
  {
    return lastUpdDttm;
  }

  public void setLastUpdDttm( Date lastUpdDttm )
  {
    this.lastUpdDttm = lastUpdDttm;
  }

  public float getRateDiv()
  {
    return rateDiv;
  }

  public void setRateDiv( float rateDiv )
  {
    this.rateDiv = rateDiv;
  }

  public float getRateMult()
  {
    return rateMult;
  }

  public void setRateMult( float rateMult )
  {
    this.rateMult = rateMult;
  }

  public String getRtRateIndex()
  {
    return rtRateIndex;
  }

  public void setRtRateIndex( String rtRateIndex )
  {
    this.rtRateIndex = rtRateIndex;
  }

  public String getRtType()
  {
    return rtType;
  }

  public void setRtType( String rtType )
  {
    this.rtType = rtType;
  }

  public int getSyncId()
  {
    return syncId;
  }

  public void setSyncId( int syncId )
  {
    this.syncId = syncId;
  }

  public int getTerm()
  {
    return term;
  }

  public void setTerm( int term )
  {
    this.term = term;
  }

  public String getToCurrency()
  {
    return toCurrency;
  }

  public void setToCurrency( String toCurrency )
  {
    this.toCurrency = toCurrency;
  }

}

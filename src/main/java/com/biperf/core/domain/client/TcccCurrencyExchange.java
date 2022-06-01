
package com.biperf.core.domain.client;

import java.util.Date;

import com.biperf.core.domain.BaseDomain;

/**
 * TcccCurrencyExchange.
 * 
 * @author dudam
 * @since Feb 21, 2018
 * @version 1.0
 * 
 * This domain class is created as part of WIP #42701
 */
public class TcccCurrencyExchange extends BaseDomain
{
  private static final long serialVersionUID = 1L;
  private String currency;
  private String currencyName;
  private Double exchangeRate;
  private boolean processing;
  private Date dateEnd;

  public String getCurrency()
  {
    return currency;
  }

  public void setCurrency( String currency )
  {
    this.currency = currency;
  }

  public String getCurrencyName()
  {
    return currencyName;
  }

  public void setCurrencyName( String currencyName )
  {
    this.currencyName = currencyName;
  }

  public Double getExchangeRate()
  {
    return exchangeRate;
  }

  public void setExchangeRate( Double exchangeRate )
  {
    this.exchangeRate = exchangeRate;
  }

  public boolean isProcessing()
  {
    return processing;
  }

  public void setProcessing( boolean processing )
  {
    this.processing = processing;
  }

  public Date getDateEnd()
  {
    return dateEnd;
  }

  public void setDateEnd( Date dateEnd )
  {
    this.dateEnd = dateEnd;
  }

  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result + ( ( currency == null ) ? 0 : currency.hashCode() );
    result = prime * result + ( ( currencyName == null ) ? 0 : currencyName.hashCode() );
    result = prime * result + ( ( dateEnd == null ) ? 0 : dateEnd.hashCode() );
    result = prime * result + ( ( exchangeRate == null ) ? 0 : exchangeRate.hashCode() );
    result = prime * result + ( processing ? 1231 : 1237 );
    return result;
  }

  @Override
  public boolean equals( Object obj )
  {
    if ( this == obj )
      return true;
    if ( obj == null )
      return false;
    if ( getClass() != obj.getClass() )
      return false;
    TcccCurrencyExchange other = (TcccCurrencyExchange)obj;
    if ( currency == null )
    {
      if ( other.currency != null )
        return false;
    }
    else if ( !currency.equals( other.currency ) )
      return false;
    if ( currencyName == null )
    {
      if ( other.currencyName != null )
        return false;
    }
    else if ( !currencyName.equals( other.currencyName ) )
      return false;
    if ( dateEnd == null )
    {
      if ( other.dateEnd != null )
        return false;
    }
    else if ( !dateEnd.equals( other.dateEnd ) )
      return false;
    if ( exchangeRate == null )
    {
      if ( other.exchangeRate != null )
        return false;
    }
    else if ( !exchangeRate.equals( other.exchangeRate ) )
      return false;
    if ( processing != other.processing )
      return false;
    return true;
  }

}

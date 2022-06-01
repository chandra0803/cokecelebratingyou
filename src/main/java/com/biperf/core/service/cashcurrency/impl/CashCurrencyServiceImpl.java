/*
 * (c) 2016 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.service.cashcurrency.impl;

import static org.apache.commons.lang3.StringUtils.isEmpty;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import com.biperf.core.dao.cashcurrency.CashCurrencyDAO;
import com.biperf.core.domain.cashcurrency.CashCurrencyCurrent;
import com.biperf.core.domain.cashcurrency.CashCurrencyHistory;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.cashcurrency.CashCurrencyService;
import com.biperf.core.value.CashCurrencyBean;

/**
 * 
 * @author poddutur
 * @since Mar 29, 2016
 */
public class CashCurrencyServiceImpl implements CashCurrencyService
{
  private CashCurrencyDAO cashCurrencyDAO;

  @Override
  public CashCurrencyCurrent saveCashCurrencyCurrent( CashCurrencyCurrent cashCurrencyCurrent ) throws ServiceErrorException
  {
    return this.cashCurrencyDAO.saveCashCurrencyCurrent( cashCurrencyCurrent );
  }

  @Override
  public CashCurrencyHistory saveCashCurrencyHistory( CashCurrencyHistory cashCurrencyHistory ) throws ServiceErrorException
  {
    return this.cashCurrencyDAO.saveCashCurrencyHistory( cashCurrencyHistory );
  }

  @Override
  public List<CashCurrencyCurrent> getAllOldCashCurrencyList()
  {
    return this.cashCurrencyDAO.getAllOldCashCurrencyList();
  }

  @Override
  public CashCurrencyHistory getCashCurrencyHistoryById( Long cashCurrencyHistoryid )
  {
    return this.cashCurrencyDAO.getCashCurrencyHistoryById( cashCurrencyHistoryid );
  }

  @Override
  public void deleteOldCashCurrency( CashCurrencyCurrent oldCashCurrencyCurrent ) throws ServiceErrorException
  {
    this.cashCurrencyDAO.deleteOldCashCurrency( oldCashCurrencyCurrent );
  }

  public void setCashCurrencyDAO( CashCurrencyDAO cashCurrencyDAO )
  {
    this.cashCurrencyDAO = cashCurrencyDAO;
  }

  @Override
  public BigDecimal convertCurrency( String fromCurrcy, String toCurrency, BigDecimal fromAmt, Date expectedDate )
  {
    if ( isEmpty( fromCurrcy ) || isEmpty( toCurrency ) || Objects.isNull( fromAmt ) )
    {
      return null;
    }

    CashCurrencyBean cashCurrency = null;

    if ( fromCurrcy.equals( toCurrency ) )
    {
      return fromAmt.setScale( 16, BigDecimal.ROUND_HALF_DOWN );
    }

    if ( "USD".equalsIgnoreCase( fromCurrcy ) ) // multiply
    {
      cashCurrency = cashCurrencyDAO.getCashCurrencyFromCurrentAndHistory( fromCurrcy, toCurrency, expectedDate == null ? new Date() : expectedDate );
      if( cashCurrency != null )
      {
        fromAmt = fromAmt.multiply( cashCurrency.getbPomEnteredRate() ).setScale( 16, BigDecimal.ROUND_HALF_DOWN );
      }
      return fromAmt;
    }
    else if ( "USD".equalsIgnoreCase( toCurrency ) ) // divide
    {
      String toCurrencyParam = fromCurrcy;
      cashCurrency = cashCurrencyDAO.getCashCurrencyFromCurrentAndHistory( "USD", toCurrencyParam, expectedDate == null ? new Date() : expectedDate );
      if( cashCurrency != null )
      {
      fromAmt = fromAmt.divide( cashCurrency.getbPomEnteredRate(), 16, RoundingMode.HALF_DOWN );
      }
      return fromAmt;
    }
    else // First to USD, then to toCurrency
    {
      CashCurrencyBean toUsdCurrency = cashCurrencyDAO.getCashCurrencyFromCurrentAndHistory( "USD", fromCurrcy, expectedDate == null ? new Date() : expectedDate );
      CashCurrencyBean fromUsdCurrency = cashCurrencyDAO.getCashCurrencyFromCurrentAndHistory( "USD", toCurrency, expectedDate == null ? new Date() : expectedDate );
      if( toUsdCurrency != null && fromUsdCurrency  != null )
      {
      fromAmt =  fromAmt.divide( toUsdCurrency.getbPomEnteredRate() ).multiply( fromUsdCurrency.getbPomEnteredRate() ).setScale( 16, BigDecimal.ROUND_HALF_DOWN );
      }
      return fromAmt;
    }
  }

}

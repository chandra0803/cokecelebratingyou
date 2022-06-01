/*
 * (c) 2016 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.service.cashcurrency;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.biperf.core.domain.cashcurrency.CashCurrencyCurrent;
import com.biperf.core.domain.cashcurrency.CashCurrencyHistory;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.SAO;

/**
 * 
 * @author poddutur
 * @since Mar 29, 2016
 */
public interface CashCurrencyService extends SAO
{
  /** Name of service bean in the beanFactory */
  public static final String BEAN_NAME = "cashCurrencyService";

  public CashCurrencyCurrent saveCashCurrencyCurrent( CashCurrencyCurrent cashCurrencyCurrent ) throws ServiceErrorException;

  public CashCurrencyHistory saveCashCurrencyHistory( CashCurrencyHistory cashCurrencyHistory ) throws ServiceErrorException;

  public List<CashCurrencyCurrent> getAllOldCashCurrencyList();

  public CashCurrencyHistory getCashCurrencyHistoryById( Long cashCurrencyHistoryid );

  public void deleteOldCashCurrency( CashCurrencyCurrent oldCashCurrencyCurrent ) throws ServiceErrorException;

  BigDecimal convertCurrency( String fromCurrcy, String toCurrency, BigDecimal fromAmt, Date expectedDate );

}

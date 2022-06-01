/*
 * (c) 2016 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.dao.cashcurrency;

import java.util.Date;
import java.util.List;

import com.biperf.core.dao.DAO;
import com.biperf.core.domain.cashcurrency.CashCurrencyCurrent;
import com.biperf.core.domain.cashcurrency.CashCurrencyHistory;
import com.biperf.core.value.CashCurrencyBean;

/**
 * 
 * @author poddutur
 * @since Mar 29, 2016
 */
public interface CashCurrencyDAO extends DAO
{
  /** ApplicationContext beanName */
  public static final String BEAN_NAME = "cashCurrencyDAO";

  public CashCurrencyCurrent saveCashCurrencyCurrent( CashCurrencyCurrent cashCurrencyCurrent );

  public CashCurrencyHistory saveCashCurrencyHistory( CashCurrencyHistory cashCurrencyHistory );

  public List<CashCurrencyCurrent> getAllOldCashCurrencyList();

  public void deleteOldCashCurrency( CashCurrencyCurrent oldCashCurrencyCurrent );

  public CashCurrencyHistory getCashCurrencyHistoryById( Long id );

  public List<CashCurrencyCurrent> getCurrentCashCurrencies();

  public CashCurrencyBean getCashCurrencyFromCurrentAndHistory( String fromCurrency, String toCurrency, Date effectiveDate );

}

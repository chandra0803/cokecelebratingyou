/*
 * (c) 2016 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.dao.cashcurrency.hibernate;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;

import com.biperf.core.dao.BaseDAO;
import com.biperf.core.dao.BaseResultTransformer;
import com.biperf.core.dao.cashcurrency.CashCurrencyDAO;
import com.biperf.core.domain.cashcurrency.CashCurrencyCurrent;
import com.biperf.core.domain.cashcurrency.CashCurrencyHistory;
import com.biperf.core.utils.hibernate.HibernateUtil;
import com.biperf.core.value.CashCurrencyBean;

/**
 * 
 * @author poddutur
 * @since Mar 29, 2016
 */
public class CashCurrencyDAOImpl extends BaseDAO implements CashCurrencyDAO
{

  @Override
  public CashCurrencyCurrent saveCashCurrencyCurrent( CashCurrencyCurrent cashCurrencyCurrent )
  {
    return (CashCurrencyCurrent)HibernateUtil.saveOrUpdateOrShallowMerge( cashCurrencyCurrent );
  }

  @Override
  public CashCurrencyHistory saveCashCurrencyHistory( CashCurrencyHistory cashCurrencyHistory )
  {
    return (CashCurrencyHistory)HibernateUtil.saveOrUpdateOrShallowMerge( cashCurrencyHistory );
  }

  @Override
  public List<CashCurrencyCurrent> getAllOldCashCurrencyList()
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.cashcurrency.getAllOldCashCurrencyList" );
    return query.list();
  }

  @Override
  public void deleteOldCashCurrency( CashCurrencyCurrent oldCashCurrencyCurrent )
  {
    getSession().delete( oldCashCurrencyCurrent );
  }

  @Override
  public CashCurrencyHistory getCashCurrencyHistoryById( Long id )
  {
    return (CashCurrencyHistory)getSession().get( CashCurrencyHistory.class, id );
  }

  @Override
  public List<CashCurrencyCurrent> getCurrentCashCurrencies()
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.cashcurrency.getCurrentCashCurrencyList" );
    return query.list();
  }

  @Override
  public CashCurrencyBean getCashCurrencyFromCurrentAndHistory( String fromCurrency, String toCurrency, Date effectiveDate )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.cashcurrency.getCashCurrencyFromCurrentAndHistory" );
    query.setParameter( "fromCurrency", fromCurrency );
    query.setParameter( "toCurrency", toCurrency );
    query.setDate( "effectiveDate", effectiveDate );
    query.setResultTransformer( new CashCurrencyFromCurrentAndHistoryMapper() );
    return (CashCurrencyBean)query.uniqueResult();
  }

  private class CashCurrencyFromCurrentAndHistoryMapper extends BaseResultTransformer
  {
    private static final long serialVersionUID = 1L;

    @Override
    public Object transformTuple( Object[] tuple, String[] aliases )
    {
      CashCurrencyBean bean = new CashCurrencyBean();

      bean.setbPomEnteredRate( extractBigDecimal( tuple[0] ) );
      bean.setRateMult( extractBigDecimal( tuple[1] ) );
      bean.setRateDiv( extractBigDecimal( tuple[1] ) );
      return bean;
    }
  }

}

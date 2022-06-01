/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/dao/currency/hibernate/Attic/CurrencyDAOImpl.java,v $
 */

package com.biperf.core.dao.currency.hibernate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.biperf.core.dao.BaseDAO;
import com.biperf.core.dao.currency.CurrencyDAO;
import com.biperf.core.domain.currency.Currency;
import com.biperf.core.utils.HibernateSessionManager;
import com.biperf.core.utils.hibernate.HibernateUtil;

/**
 * CurrencyDAOImpl.
 * <p>
 * <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>dudam</td>
 * <td>December 19, 2014</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 */
public class CurrencyDAOImpl extends BaseDAO implements CurrencyDAO
{

  private static Comparator<Currency> ASCE_COMPARATOR = new Comparator<Currency>()
  {
    public int compare( Currency c1, Currency c2 )
    {
      return c1.getCurrencyName().compareTo( c2.getCurrencyName() );
    }
  };

  @Override
  public Currency save( Currency currency )
  {
    return (Currency)HibernateUtil.saveOrUpdateOrShallowMerge( currency );
  }

  @Override
  public Currency getCurrencyById( Long id )
  {
    Session session = HibernateSessionManager.getSession();
    return (Currency)session.get( Currency.class, id );
  }

  @Override
  public Currency getCurrencyByCode( String code )
  {
    Criteria criteria = getSession().createCriteria( Currency.class );
    criteria.add( Restrictions.eq( "currencyCode", code.toUpperCase() ) );
    return (Currency)criteria.uniqueResult();
  }

  @Override
  public List<Currency> getAllActiveCurrency()
  {
    Criteria criteria = getSession().createCriteria( Currency.class, "currency" );
    criteria.add( Restrictions.eq( "currency.status", "active" ) );
    List<Currency> currencies = criteria.list();
    List<Currency> sortedCurrencies = sortCurrencies( currencies );
    return sortedCurrencies;
  }

  @Override
  public List<Currency> getAllCurrency()
  {
    Criteria criteria = getSession().createCriteria( Currency.class, "currency" );
    List<Currency> currencies = criteria.list();
    List<Currency> sortedCurrencies = sortCurrencies( currencies );
    return sortedCurrencies;
  }

  public boolean isCurrencyNameUnique( String currencyName, Long currentCurrencyId, String locale )
  {
    boolean isUnique = true;
    if ( currentCurrencyId == null )
    {
      currentCurrencyId = new Long( 0 );
    }
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.currency.CurrencyNameExistsCount" );
    query.setParameter( "currencyId", currentCurrencyId );
    query.setParameter( "currencyName", currencyName );
    query.setParameter( "locale", locale );
    Integer count = (Integer)query.uniqueResult();
    isUnique = count.intValue() == 0;
    return isUnique;
  }

  private List<Currency> sortCurrencies( List<Currency> currencies )
  {
    List<Currency> sortedCurrencies = new ArrayList<Currency>();
    for ( Currency currency : currencies )
    {
      Currency c = new Currency();
      c.setCmAssetName( currency.getCmAssetName() );
      c.setCurrencyCode( currency.getCurrencyCode() );
      c.setCurrencyName( currency.getDisplayCurrencyName() );
      c.setCurrencySymbol( currency.getCurrencySymbol() );
      c.setId( currency.getId() );
      c.setStatus( currency.getStatus() );
      sortedCurrencies.add( c );
    }
    Collections.sort( sortedCurrencies, ASCE_COMPARATOR );
    return sortedCurrencies;
  }

}

/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/dao/currency/Attic/CurrencyDAO.java,v $
 */

package com.biperf.core.dao.currency;

import java.util.List;

import com.biperf.core.dao.DAO;
import com.biperf.core.domain.currency.Currency;

/**
 * CurrencyDAO.
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
public interface CurrencyDAO extends DAO
{

  public Currency save( Currency currency );

  public Currency getCurrencyById( Long id );

  public Currency getCurrencyByCode( String code );

  public List getAllActiveCurrency();

  public List getAllCurrency();

  public boolean isCurrencyNameUnique( String currencyName, Long currentCurrencyId, String locale );

}

/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/currency/Attic/CurrencyService.java,v $
 */

package com.biperf.core.service.currency;

import java.util.List;

import com.biperf.core.domain.currency.Currency;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.SAO;

/**
 * CurrencyService.
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
 *
 */
public interface CurrencyService extends SAO
{
  /** Name of service bean in the beanFactory */
  public static final String BEAN_NAME = "currencyService";

  /**
   * @param currency
   * @return
   */
  public Currency save( Currency currency ) throws ServiceErrorException;

  /**
   * @param id
   * @return
   */
  public Currency getCurrencyById( Long id );

  /**
   * @param code
   * @return
   */
  public Currency getCurrencyByCode( String code );

  /**
   * @return
   */
  public List getAllActiveCurrency();

  /**
   * @return
   */
  public List getAllCurrency();

  /**
   * @param currencyName
   * @param currentCurrencyId
   * @param locale
   * @return
   */
  public boolean isCurrencyNameUnique( String currencyName, Long currentCurrencyId, String locale );

}

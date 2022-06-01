/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/country/CountryService.java,v $
 */

package com.biperf.core.service.country;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.biperf.awardslinqDataRetriever.hibernate.entity.Program;
import com.biperf.core.domain.country.Country;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.SAO;
import com.biperf.core.value.CountryValueBean;

/**
 * CountryService.
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
 * <td>sedey</td>
 * <td>Jun 2, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public interface CountryService extends SAO
{
  /** Name of service bean in the beanFactory */
  public static final String BEAN_NAME = "countryService";

  /**
   * Saves the Country to the database.
   * 
   * @param supplierId
   * @param country
   * @return Country
   * @throws ServiceErrorException
   */
  public Country saveCountry( Long supplierId, Country country ) throws ServiceErrorException;

  /**
   * Gets the Country by the id.
   * 
   * @param id
   * @return Country
   */
  public Country getCountryById( Long id );

  /**
   * Gets a Country object by country code.
   * 
   * @param countryCode a country code.
   * @return the specified Country object.
   */
  public Country getCountryByCode( String countryCode );

  /**
   * Gets the budget media value for the given countryCode
   * 
   * @param countryCode a country code.
   * @return BigDecimal
   */
  public BigDecimal getBudgetMediaValueByCountryCode( String countryCode );

  /**
   * Return a List of all Countries.
   * 
   * @return List
   */
  public List getAll();

  /**
   * Return a List of all Active Countries.
   * 
   * @return List
   */
  public List<Country> getAllActive();

  /**
   * Return a List of all Active Countries allow SMS BOX is checked
   * 
   * @return List
   */
  public List getActiveCountriesForSmsChecked();

  /**
   * Return a List of all Active Countries for All Active Pax
   * 
   * @return List
   */
  public List getActiveCountriesForAllActivePax();

  /**
   * Gets a list of active countries for a pax based audience
   * 
   * @param audienceId
   * @return
   */
  public List getActiveCountriesForPaxBasedAudience( Long audienceId );

  /**
   * Gets the Country's Program object.
   * 
   * @param countryCode a country code.
   * @return a Program object.
   */
  public Program getProgramByCountryCode( String countryCode );

  /**
   * Gets the Country's current time.
   * 
   * @param country.
   * @return a Date.
   */
  public Date getCurrentTimeForCountry( Country country );

  /**
   * Gets the Country's current time.
   * 
   * @param srcDate.
   * @param srcCountry.
   * @param destCountry.
   * @return a Date.
   */
  public Date convertTimeForCountry( Date srcDate, Country srcCountry, Country destCountry );

  /**
   * @param countryCode
   * @param supplierId
   * @return
   */
  public boolean checkUserSupplier( String countryCode, String supplierName );

  public int getCountryCountByStartsWith( String startWith );

  public List<String> getCountryNameList();

  public List<CountryValueBean> getAllActiveCountriesCodesAbbrevs();

  public List<Country> getActiveCountriesForSmsAvailable();
  
  public boolean checkBiiExperience( String countryCode, String programId );
}

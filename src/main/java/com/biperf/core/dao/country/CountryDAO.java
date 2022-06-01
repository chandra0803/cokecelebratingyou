/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/dao/country/CountryDAO.java,v $
 */

package com.biperf.core.dao.country;

import java.math.BigDecimal;
import java.util.List;

import com.biperf.core.dao.DAO;
import com.biperf.core.domain.country.Country;

/**
 * CountryDAO.
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
 */
public interface CountryDAO extends DAO
{
  public static final String BEAN_NAME = "countryDAO";

  /**
   * Get the Country by id.
   * 
   * @param id
   * @return Country
   */
  public Country getCountryById( Long id );

  /**
   * Get the Country by code.
   * 
   * @param countryCode
   * @return Country
   */
  public Country getCountryByCode( String countryCode );

  /**
   * Get the Country by awardbanqAbbrev.
   * 
   * @param awardbanqAbbrev
   * @return Country
   */
  public Country getCountryByAwardbanqAbbrev( String awardbanqAbbrev );

  /**
   * Get All Country records.
   * 
   * @return List
   */
  public List getAll();

  /**
   * Get All Active Country records.
   * 
   * @return List
   */
  public List<Country> getAllActive();

  /**
   * Get all active countries for all active participants
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
   * Save or update the Country.
   * 
   * @param country
   * @return Country
   */
  public Country saveCountry( Country country );

  /**
   * @return
   */
  public List getActiveCountriesForSmsChecked();

  public List<Country> getActiveCountriesForSmsAvailable();

  /**
   * @param countryCode
   * @param supplierId
   * @return
   */
  public boolean checkUserSupplier( String countryCode, String supplierName );

  public BigDecimal getBudgetMediaValueByUserId( Long userId );

  public boolean checkBiiExperience( String countryCode, String programId );
  
  public List getAllActiveCountriesCodesAbbrevs();

}

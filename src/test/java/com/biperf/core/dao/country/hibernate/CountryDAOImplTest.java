/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javatest/com/biperf/core/dao/country/hibernate/CountryDAOImplTest.java,v $
 */

package com.biperf.core.dao.country.hibernate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import com.biperf.core.dao.country.CountryDAO;
import com.biperf.core.dao.hibernate.BaseDAOTest;
import com.biperf.core.dao.integration.SupplierDAO;
import com.biperf.core.dao.integration.hibernate.SupplierDAOImplTest;
import com.biperf.core.domain.country.Country;
import com.biperf.core.domain.country.CountrySupplier;
import com.biperf.core.domain.enums.AddressMethodType;
import com.biperf.core.domain.enums.CountryStatusType;
import com.biperf.core.domain.enums.TimeZoneId;
import com.biperf.core.domain.supplier.Supplier;
import com.biperf.core.utils.ApplicationContextFactory;
import com.biperf.core.value.CountryValueBean;

/**
 * CountryDAOImplTest.
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
 * <td>June 2, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class CountryDAOImplTest extends BaseDAOTest
{
  // ---------------------------------------------------------------------------
  // Build Methods
  // ---------------------------------------------------------------------------

  /**
   * Builds a {@link Country} object.
   * 
   * @param uniqueString used to make the returned country unique.
   * @return a {@link Country} object.
   */
  public static Country buildCountry( String uniqueString )
  {
    Country country = new Country();

    country.setCountryCode( "ba" + uniqueString );
    country.setCountryName( "Batavia" + uniqueString );
    country.setCampaignNbr( "NUMBER" + uniqueString );
    country.setCampaignPassword( "PASSWORD" + uniqueString );
    country.setAwardbanqAbbrev( "bat" );
    country.setAddressMethod( AddressMethodType.lookup( AddressMethodType.NORTH_AMERICAN ) );
    country.setStatus( CountryStatusType.lookup( CountryStatusType.ACTIVE ) );
    country.setDateStatus( new Date() );
    country.setAllowSms( Boolean.FALSE );
    country.setRequirePostalCode( Boolean.FALSE );
    country.setTimeZoneId( TimeZoneId.lookup( TimeZone.getDefault().getID() ) );
    country.setDisplayTravelAward( false );
    country.setBudgetMediaValue( BigDecimal.valueOf( .35 ) );

    return country;
  }

  public static CountrySupplier buildCountrySupplier( Country expectedCountry, Supplier expectedSupplier )
  {
    CountrySupplier countrySupplier = new CountrySupplier();
    countrySupplier.setCountry( expectedCountry );
    countrySupplier.setSupplier( expectedSupplier );
    countrySupplier.setPrimary( Boolean.TRUE );

    return countrySupplier;
  }

  /**
   * Builds a {@link Country} object that represents France.
   * 
   * @return a {@link Country} object that represents France.
   */
  public static Country buildFrance()
  {
    Country country = new Country();

    country.setCountryCode( "fr" );
    country.setCountryName( "France" );
    country.setAwardbanqAbbrev( "fra" );
    country.setAddressMethod( AddressMethodType.lookup( AddressMethodType.NORTH_AMERICAN ) );
    country.setStatus( CountryStatusType.lookup( CountryStatusType.ACTIVE ) );
    country.setDateStatus( new Date() );
    country.setAllowSms( Boolean.FALSE );
    country.setRequirePostalCode( Boolean.FALSE );
    country.setDisplayTravelAward( Boolean.FALSE );
    country.setTimeZoneId( TimeZoneId.lookup( TimeZone.getDefault().getID() ) );

    return country;
  }

  /**
   * Builds a {@link Country} object that represents the United Status.
   * 
   * @return a {@link Country} object that represents the United Status.
   */
  public static Country buildUnitedStates()
  {
    Country country = new Country();

    country.setCountryCode( "us" );
    country.setCountryName( "United States of America" );
    country.setAwardbanqAbbrev( "usa" );
    country.setAddressMethod( AddressMethodType.lookup( AddressMethodType.NORTH_AMERICAN ) );
    country.setStatus( CountryStatusType.lookup( CountryStatusType.ACTIVE ) );
    country.setDateStatus( new Date() );
    country.setAllowSms( Boolean.FALSE );
    country.setRequirePostalCode( Boolean.FALSE );
    country.setDisplayTravelAward( Boolean.FALSE );
    country.setTimeZoneId( TimeZoneId.lookup( TimeZone.getDefault().getID() ) );

    return country;
  }

  // ---------------------------------------------------------------------------
  // Test Methods
  // ---------------------------------------------------------------------------

  /**
   * Tests saving or updating the Country. This needs to fetch the Country by Id so it is also
   * testing CountryDAO.getCountryById(Long id).
   */
  public void testSaveAndGetCountryById()
  {
    CountryDAO countryDAO = getCountryDAO();
    SupplierDAO supplierDAO = getSupplierDAO();

    // Create a Supplier object to add to the Country object.
    Supplier expectedSupplier = SupplierDAOImplTest.buildSupplier( "Test Supplier1" );
    supplierDAO.saveSupplier( expectedSupplier );

    // Create a Country object
    Country expectedCountry = buildCountry( getUniqueString() );
    expectedCountry.addCountrySuppliers( buildCountrySupplier( expectedCountry, expectedSupplier ) );

    // Save the country object
    countryDAO.saveCountry( expectedCountry );

    flushAndClearSession();

    // Try to fetch the record that was just saved using the getCountryById
    Country actualCountryById = countryDAO.getCountryById( expectedCountry.getId() );

    // Check that the country fetched matches the one that was created.
    assertEquals( "Country not equals", expectedCountry, actualCountryById );

    // Try to update the country Update the Country
    expectedCountry.setCountryName( "testUpdatedName" );

    // Save the updated country
    countryDAO.saveCountry( expectedCountry );

    flushAndClearSession();

    // Try to get the updated Country from the database using getCountryByCode
    Country actualCountryByCode = countryDAO.getCountryByCode( expectedCountry.getCountryCode() );

    // Try to get the updated Country from the database using getCountryByAwardbanqAbbrev
    Country actualCountryByAwardbanqAbbrev = countryDAO.getCountryByCode( expectedCountry.getCountryCode() );

    // Check that each of the country selects equals the updated country
    assertEquals( "Updated Country not equals by code", expectedCountry, actualCountryByCode );
    assertEquals( "Updated Country not equals by awardbanqAbbrev", expectedCountry, actualCountryByAwardbanqAbbrev );
  }

  public void testSaveAndGetCountryByCampaignNumber()
  {
    CountryDAO countryDAO = getCountryDAO();
    SupplierDAO supplierDAO = getSupplierDAO();

    // Create a Supplier object to add to the Country object.
    Supplier expectedSupplier = SupplierDAOImplTest.buildSupplier( "Test Supplier1" );
    supplierDAO.saveSupplier( expectedSupplier );

    // Create a Country object
    Country expectedCountry = buildCountry( getUniqueString() );
    expectedCountry.addCountrySuppliers( buildCountrySupplier( expectedCountry, expectedSupplier ) );

    // Save the country object
    countryDAO.saveCountry( expectedCountry );

    flushAndClearSession();

    // getCountryByCampaignNumber(campaignNumber) is not used because of its assumption that
    // country has one-one relationship with campaign number, which is NOT true
    // same campaign Number can belong to many Countries

    // Try to fetch the record that was just saved using the getCountryById
    // Country actualCountryByCampaignNumber = countryDAO.getCountryByCampaignNumber(
    // expectedCountry.getCampaignNbr() );

    // Check that the country fetched matches the one that was created.
    // assertEquals( "Country not equals", expectedCountry, actualCountryByCampaignNumber );

  }

  /**
   * Tests saving and getting all the country records saved.
   */
  public void testGetAll()
  {
    CountryDAO countryDAO = getCountryDAO();
    SupplierDAO supplierDAO = getSupplierDAO();

    int count = 0;

    // Get a count of the countries in the database
    count = countryDAO.getAll().size();

    List expectedCountries = new ArrayList();

    // Create a supplier object to use in creating countries
    Supplier expectedSupplier = SupplierDAOImplTest.buildSupplier( "Test Supplier1" );
    supplierDAO.saveSupplier( expectedSupplier );

    // Create a country record to add to the list
    Country expectedCountry1 = buildCountry( getUniqueString() );
    expectedCountry1.addCountrySuppliers( buildCountrySupplier( expectedCountry1, expectedSupplier ) );
    expectedCountries.add( expectedCountry1 );
    countryDAO.saveCountry( expectedCountry1 );

    // Create a country record to add to the list
    Country expectedCountry2 = buildCountry( getUniqueString() );
    expectedCountry2.addCountrySuppliers( buildCountrySupplier( expectedCountry2, expectedSupplier ) );
    expectedCountries.add( expectedCountry2 );
    countryDAO.saveCountry( expectedCountry2 );

    flushAndClearSession();

    List actualCountries = countryDAO.getAll();

    assertEquals( "List of countries aren't the same size.", expectedCountries.size() + count, actualCountries.size() );
  }

  public void testGetAllCountriesWithSMSCapabilities()
  {
    List<Country> countries = getCountryDAO().getActiveCountriesForSmsAvailable();
    assertNotNull( countries );
    assertTrue( !countries.isEmpty() );
  }

  /**
   * Tests saving and getting all the country active records saved.
   */
  public void testGetAllActive()
  {
    CountryDAO countryDAO = getCountryDAO();
    SupplierDAO supplierDAO = getSupplierDAO();

    int count = 0;

    // Get a count of the countries in the database
    count = countryDAO.getAllActive().size();

    List expectedCountries = new ArrayList();

    // Create a supplier object to use in creating countries
    Supplier expectedSupplier = SupplierDAOImplTest.buildSupplier( "Test Supplier1" );
    supplierDAO.saveSupplier( expectedSupplier );

    // Create an ACTIVE country record to add to the list
    Country expectedCountry1 = buildCountry( getUniqueString() );
    expectedCountry1.addCountrySuppliers( buildCountrySupplier( expectedCountry1, expectedSupplier ) );
    expectedCountries.add( expectedCountry1 );
    countryDAO.saveCountry( expectedCountry1 );

    // Create an INACTIVE country record to add to the list
    Country expectedCountry2 = buildCountry( getUniqueString() );
    expectedCountry1.setStatus( CountryStatusType.lookup( CountryStatusType.INACTIVE ) );
    expectedCountry2.addCountrySuppliers( buildCountrySupplier( expectedCountry2, expectedSupplier ) );
    countryDAO.saveCountry( expectedCountry2 );

    flushAndClearSession();

    List actualActiveCountries = countryDAO.getAllActive();

    assertEquals( "List of active countries aren't the same size.", expectedCountries.size() + count, actualActiveCountries.size() );
  }

  // ---------------------------------------------------------------------------
  // Private Methods
  // ---------------------------------------------------------------------------

  /**
   * Get the CountryDAO.
   * 
   * @return CountryDAO
   */
  private CountryDAO getCountryDAO()
  {
    return (CountryDAO)ApplicationContextFactory.getApplicationContext().getBean( "countryDAO" );
  }

  /**
   * Get the SupplierDAO.
   * 
   * @return SupplierDAO
   */
  private SupplierDAO getSupplierDAO()
  {
    return (SupplierDAO)ApplicationContextFactory.getApplicationContext().getBean( "supplierDAO" );
  }

  @SuppressWarnings( { "unchecked", "rawtypes" } )
  public void testGetAllActiveCountriesCodesAbbrevs()
  {
    CountryDAO countryDAO = getCountryDAO();
    SupplierDAO supplierDAO = getSupplierDAO();

    int count = 0;
    count = countryDAO.getAllActive().size();

    List expectedCountries = new ArrayList();

    Supplier expectedSupplier = SupplierDAOImplTest.buildSupplier( "Test Supplier1" );
    supplierDAO.saveSupplier( expectedSupplier );

    Country expectedCountry1 = buildCountry( getUniqueString() );
    expectedCountry1.addCountrySuppliers( buildCountrySupplier( expectedCountry1, expectedSupplier ) );
    expectedCountries.add( expectedCountry1 );
    countryDAO.saveCountry( expectedCountry1 );

    Country expectedCountry2 = buildCountry( getUniqueString() );
    expectedCountry1.setStatus( CountryStatusType.lookup( CountryStatusType.INACTIVE ) );
    expectedCountry2.addCountrySuppliers( buildCountrySupplier( expectedCountry2, expectedSupplier ) );
    countryDAO.saveCountry( expectedCountry2 );

    flushAndClearSession();
    List<CountryValueBean> actualActiveCountries = countryDAO.getAllActiveCountriesCodesAbbrevs();
    assertEquals( "List of active countries aren't the same size.", expectedCountries.size() + count, actualActiveCountries.size() );
  }

  public void testCheckUserSupplier()
  {
    CountryDAO countryDAO = getCountryDAO();
    // Testing to check if the supplier found for country
    assertTrue( "Supplier not found", countryDAO.checkUserSupplier( Country.UNITED_STATES, Supplier.BI_BANK ) );

    // Testing to check if supplier not found for a country
    assertFalse( "Supplier shouldn't be found", countryDAO.checkUserSupplier( Country.UNITED_STATES, "Test Supplier" ) );

    // Testing with invalid country
    assertFalse( "Country shouldn't be found", countryDAO.checkUserSupplier( "test", Supplier.BI_BANK ) );

    // Testing with invalid country and invalid supplier
    assertFalse( "Country and Supplier shouldn't be found", countryDAO.checkUserSupplier( "test", "Test Supplier" ) );
  }

}
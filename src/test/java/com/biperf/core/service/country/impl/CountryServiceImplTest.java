/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javatest/com/biperf/core/service/country/impl/CountryServiceImplTest.java,v $
 */

package com.biperf.core.service.country.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.jmock.Mock;
import org.ujac.util.DateUtils;

import com.biperf.core.dao.country.CountryDAO;
import com.biperf.core.dao.integration.SupplierDAO;
import com.biperf.core.domain.country.Country;
import com.biperf.core.domain.enums.CountryStatusType;
import com.biperf.core.domain.enums.SupplierStatusType;
import com.biperf.core.domain.enums.TimeZoneId;
import com.biperf.core.domain.supplier.Supplier;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.BaseServiceTest;
import com.biperf.core.service.awardbanq.AwardBanQService;
import com.biperf.core.service.awardbanq.AwardBanQServiceFactory;
import com.biperf.core.service.cms.CMAssetService;

/**
 * CountryServiceImplTest.
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
public class CountryServiceImplTest extends BaseServiceTest
{

  /**
   * Constructor to take the name of the test.
   * 
   * @param test
   */
  public CountryServiceImplTest( String test )
  {
    super( test );
  }

  /** countryServiceImplementation */
  private CountryServiceImpl countryService = new CountryServiceImpl();

  /** mocks */
  private Mock mockCountryDAO = null;
  private Mock mockSupplierDAO = null;
  private Mock mockCmAssetService = null;

  private Mock mockawardBanQServiceFactory = null;
  private Mock mockAwardBanQService = null;

  /**
   * Setup this test case. Overridden from
   * 
   * @see junit.framework.TestCase#setUp()
   * @throws Exception
   */
  protected void setUp() throws Exception
  {
    super.setUp();

    mockCountryDAO = new Mock( CountryDAO.class );
    mockSupplierDAO = new Mock( SupplierDAO.class );
    mockCmAssetService = new Mock( CMAssetService.class );

    mockawardBanQServiceFactory = new Mock( AwardBanQServiceFactory.class );

    countryService.setCountryDAO( (CountryDAO)mockCountryDAO.proxy() );
    countryService.setSupplierDAO( (SupplierDAO)mockSupplierDAO.proxy() );
    countryService.setCmAssetService( (CMAssetService)mockCmAssetService.proxy() );

    countryService.setAwardBanQServiceFactory( (AwardBanQServiceFactory)mockawardBanQServiceFactory.proxy() );

    mockAwardBanQService = new Mock( AwardBanQService.class );
  }

  /**
   * Test getting the Country by id.
   */
  public void testGetCountryById()
  {
    // Get the test Country.
    Country country = new Country();
    country.setId( new Long( 1 ) );
    country.setCountryName( "TestServiceCountry" );
    country.setCountryCode( "TestCode" );
    country.setDisplayTravelAward( Boolean.FALSE );
    country.setTimeZoneId( TimeZoneId.lookup( TimeZone.getDefault().getID() ) );

    // CountryDAO expected to call getCountryById once with the CountryId which will return the
    // Country expected
    mockCountryDAO.expects( once() ).method( "getCountryById" ).with( same( country.getId() ) ).will( returnValue( country ) );

    countryService.getCountryById( country.getId() );

    mockCountryDAO.verify();
  }

  /**
   * Test getting a Country object by country code.
   */
  public void testGetCountryByCode()
  {
    // Get the test Country.
    Country country = new Country();
    country.setId( new Long( 1 ) );
    country.setCountryName( "TestServiceCountry" );
    country.setCountryCode( "TestCode" );
    country.setDisplayTravelAward( Boolean.FALSE );
    country.setTimeZoneId( TimeZoneId.lookup( TimeZone.getDefault().getID() ) );

    mockCountryDAO.expects( once() ).method( "getCountryByCode" ).with( same( country.getCountryCode() ) ).will( returnValue( country ) );

    countryService.getCountryByCode( country.getCountryCode() );

    mockCountryDAO.verify();
  }

  /**
   * Test updating the Country not changing the status through the service.
   * 
   * @throws ServiceErrorException
   */
  public void testSaveCountryUpdateNoStatusChange() throws ServiceErrorException
  {
    // Create a Country object to simulate the object being passed in.
    Country country = new Country();
    country.setId( new Long( 1 ) );
    country.setCountryName( "TestServiceCountry" );
    country.setCountryCode( "TestCode" );
    country.setAwardbanqAbbrev( "code" );
    country.setDisplayTravelAward( Boolean.FALSE );
    country.setTimeZoneId( TimeZoneId.lookup( TimeZone.getDefault().getID() ) );
    country.setStatus( CountryStatusType.lookup( "active" ) );
    country.setDateStatus( DateUtils.today() );

    // Create a Country object to simulate what is stored in the database.
    Country dbCountry = new Country();
    dbCountry.setId( new Long( 1 ) );
    dbCountry.setCountryName( "TestServiceCountry" );
    dbCountry.setCountryCode( "TestCode" );
    dbCountry.setAwardbanqAbbrev( "code" );
    dbCountry.setDisplayTravelAward( Boolean.FALSE );
    dbCountry.setTimeZoneId( TimeZoneId.lookup( TimeZone.getDefault().getID() ) );
    dbCountry.setStatus( CountryStatusType.lookup( "active" ) );
    dbCountry.setDateStatus( DateUtils.today() );

    // Create a Supplier object to simulate a looked up supplier.
    Supplier supplier = new Supplier();
    supplier.setId( new Long( 1 ) );
    supplier.setSupplierName( "TestSupplierName" );
    supplier.setStatus( SupplierStatusType.lookup( SupplierStatusType.ACTIVE ) );

    // Mock Country object for getCountryByCode Call.
    mockCountryDAO.expects( once() ).method( "getCountryByCode" ).with( same( country.getCountryCode() ) ).will( returnValue( dbCountry ) );

    // Mock Country object for getCountryByAwardbanqAbbrev Call.
    mockCountryDAO.expects( once() ).method( "getCountryByAwardbanqAbbrev" ).with( same( country.getAwardbanqAbbrev() ) ).will( returnValue( dbCountry ) );

    // Mock Country object for getCountryById Call.
    mockCountryDAO.expects( once() ).method( "getCountryById" ).with( same( country.getId() ) ).will( returnValue( dbCountry ) );

    // Mock Supplier object for getSupplierById Call.
    /*
     * mockSupplierDAO.expects( once() ).method( "getSupplierById" ).with( same( supplier.getId() )
     * ) .will( returnValue( supplier ) );
     */

    // Mock Country object for saveCountry Call.
    mockCountryDAO.expects( once() ).method( "saveCountry" ).with( same( country ) ).will( returnValue( dbCountry ) );

    mockCmAssetService.expects( once() ).method( "createOrUpdateAsset" );

    mockawardBanQServiceFactory.expects( once() ).method( "getAwardBanQService" ).will( returnValue( mockAwardBanQService.proxy() ) );

    mockAwardBanQService.expects( once() ).method( "getNullableMediaValueForCountry" );

    // Call countryService.saveCountry
    countryService.saveCountry( supplier.getId(), country );

    // Verify the mockDAO returns the same thing the service is attempting to save.
    mockCountryDAO.verify();

    // Assert that the dateStatus is not updated ( it stays the same as the
    // dbCountryById object).
    assertEquals( "Updated record dateStatus does not match.", country.getDateStatus(), dbCountry.getDateStatus() );
  }

  /**
   * Test updating the Country changing the status through the service.
   * 
   * @throws ServiceErrorException
   */
  public void testSaveCountryUpdateStatusChange() throws ServiceErrorException
  {
    // Create a Country object to simulate the object being passed in.
    Country country = new Country();
    country.setId( new Long( 1 ) );
    country.setCountryName( "TestServiceCountry" );
    country.setCountryCode( "TestCode" );
    country.setAwardbanqAbbrev( "code" );
    country.setDisplayTravelAward( Boolean.FALSE );
    country.setStatus( CountryStatusType.lookup( "active" ) );
    country.setTimeZoneId( TimeZoneId.lookup( TimeZone.getDefault().getID() ) );

    // Create a Country object to simulate what is stored in the database.
    Country dbCountry = new Country();
    dbCountry.setId( new Long( 1 ) );
    dbCountry.setCountryName( "TestServiceCountry" );
    dbCountry.setCountryCode( "TestCode" );
    dbCountry.setAwardbanqAbbrev( "code" );
    dbCountry.setDisplayTravelAward( Boolean.FALSE );
    dbCountry.setStatus( CountryStatusType.lookup( "inactive" ) );
    dbCountry.setTimeZoneId( TimeZoneId.lookup( TimeZone.getDefault().getID() ) );

    // Create a Supplier object to simulate a looked up supplier.
    Supplier supplier = new Supplier();
    supplier.setId( new Long( 1 ) );
    supplier.setSupplierName( "TestSupplierName" );
    supplier.setStatus( SupplierStatusType.lookup( SupplierStatusType.ACTIVE ) );

    // Mock Country object for getCountryByCode Call.
    mockCountryDAO.expects( once() ).method( "getCountryByCode" ).with( same( country.getCountryCode() ) ).will( returnValue( dbCountry ) );

    // Mock Country object for getCountryByAwardbanqAbbrev Call.
    mockCountryDAO.expects( once() ).method( "getCountryByAwardbanqAbbrev" ).with( same( country.getAwardbanqAbbrev() ) ).will( returnValue( dbCountry ) );

    // Mock Country object for getCountryById Call.
    mockCountryDAO.expects( once() ).method( "getCountryById" ).with( same( country.getId() ) ).will( returnValue( dbCountry ) );

    // Mock Supplier object for getSupplierById Call.
    /*
     * mockSupplierDAO.expects( once() ).method( "getSupplierById" ).with( same( supplier.getId() )
     * ) .will( returnValue( supplier ) );
     */

    // Mock Country object for saveCountry Call.
    mockCountryDAO.expects( once() ).method( "saveCountry" ).with( same( country ) ).will( returnValue( dbCountry ) );

    mockawardBanQServiceFactory.expects( once() ).method( "getAwardBanQService" ).will( returnValue( mockAwardBanQService.proxy() ) );

    mockAwardBanQService.expects( once() ).method( "getNullableMediaValueForCountry" );

    mockCmAssetService.expects( once() ).method( "createOrUpdateAsset" );

    // Call countryService.saveCountry
    countryService.saveCountry( supplier.getId(), country );

    // Verify the mockDAO returns the same thing the service is attempting to save.
    mockCountryDAO.verify();

    // Assert that the dateStatus is not updated ( it stays the same as the
    // dbCountryById object).
    assertNotSame( "Updated record dateStatus not updated.", country.getDateStatus(), dbCountry.getDateStatus() );
  }

  /**
   * Test adding the Country through the service.
   * 
   * @throws ServiceErrorException
   */
  public void testSaveCountryInsert() throws ServiceErrorException
  {
    // Create a Country object to simulate the object being passed in.
    Country country = new Country();
    country.setCountryName( "TestServiceCountry" );
    country.setCountryCode( "TestCode" );
    country.setDisplayTravelAward( Boolean.FALSE );
    country.setTimeZoneId( TimeZoneId.lookup( TimeZone.getDefault().getID() ) );
    country.setDateStatus( new Date() );
    country.setBudgetMediaValue( new BigDecimal( 100 ) );

    // Create a Country object to simulate what will get stored in the database.
    Country savedCountry = new Country();
    savedCountry.setId( new Long( 1 ) );
    savedCountry.setCountryName( "TestServiceCountry" );
    savedCountry.setCountryCode( "TestCode" );
    savedCountry.setDisplayTravelAward( Boolean.FALSE );
    savedCountry.setTimeZoneId( TimeZoneId.lookup( TimeZone.getDefault().getID() ) );
    savedCountry.setDateStatus( new Date() );

    // Create a Supplier object to simulate a looked up supplier.
    Supplier supplier = new Supplier();
    supplier.setId( new Long( 1 ) );
    supplier.setSupplierName( "TestSupplierName" );
    supplier.setStatus( SupplierStatusType.lookup( SupplierStatusType.ACTIVE ) );

    // Mock Country object for getCountryByCode Call.
    mockCountryDAO.expects( once() ).method( "getCountryByCode" ).with( same( country.getCountryCode() ) ).will( returnValue( null ) );

    // Mock Country object for getCountryByAwardbanqAbbrev Call.
    mockCountryDAO.expects( once() ).method( "getCountryByAwardbanqAbbrev" ).with( same( country.getAwardbanqAbbrev() ) ).will( returnValue( null ) );

    // Mock object for getSupplierById Call.
    /*
     * mockSupplierDAO.expects( once() ).method( "getSupplierById" ).with( same( supplier.getId() )
     * ) .will( returnValue( supplier ) );
     */

    // Mock object for saveCountry Call.
    mockCountryDAO.expects( once() ).method( "saveCountry" ).with( same( country ) ).will( returnValue( savedCountry ) );

    mockCmAssetService.expects( once() ).method( "getUniqueAssetCode" ).will( returnValue( "unique.asset.name" + System.currentTimeMillis() ) );
    mockCmAssetService.expects( once() ).method( "createOrUpdateAsset" );

    mockawardBanQServiceFactory.expects( once() ).method( "getAwardBanQService" ).will( returnValue( mockAwardBanQService.proxy() ) );

    mockAwardBanQService.expects( once() ).method( "getNullableMediaValueForCountry" );

    // Test the CountryService.saveCountry
    countryService.saveCountry( supplier.getId(), country );

    // Verify the mockDAO returns the same thing the service is attempting to save.
    mockCountryDAO.verify();

    // Assert that the saved country has a dateStatus populated.
    assertTrue( "Saved Country dateStatus not populated.", country.getDateStatus() != null );
  }

  /**
   * Test adding duplicate countryCode through the service.
   */
  public void testSaveCountryInsertDuplicateCodeViolation()
  {
    // Create a Country object to simulate the object being passed in.
    Country country = new Country();
    country.setCountryName( "TestServiceCountry" );
    country.setCountryCode( "TestCode" );
    country.setAwardbanqAbbrev( "code" );
    country.setDisplayTravelAward( Boolean.FALSE );
    country.setTimeZoneId( TimeZoneId.lookup( TimeZone.getDefault().getID() ) );

    // Create a Country object to simulate what will get stored in the database.
    Country dbCountry = new Country();
    dbCountry.setId( new Long( 1 ) );
    dbCountry.setCountryName( "TestServiceCountry" );
    dbCountry.setCountryCode( "TestCode" );
    dbCountry.setAwardbanqAbbrev( "code" );
    dbCountry.setDisplayTravelAward( Boolean.FALSE );
    dbCountry.setTimeZoneId( TimeZoneId.lookup( TimeZone.getDefault().getID() ) );

    // Mock Country object for getCountryByCode Call.
    mockCountryDAO.expects( once() ).method( "getCountryByCode" ).with( same( country.getCountryCode() ) ).will( returnValue( dbCountry ) );

    try
    {
      // Call the CountryService.saveCountry. Since we are expecting to fail before
      // making it to the supplier lookup just pass in a hard coded value of 1
      // for the supplierId.
      countryService.saveCountry( new Long( 1 ), country );
    }
    catch( ServiceErrorException e )
    {
      return;
    }

    fail( "Should have thrown ServiceErrorException" );
  }

  /**
   * Test updating duplicate countryCode through the service.
   */
  public void testSaveCountryUpdateDuplicateCodeViolation()
  {
    // Create a Country object to simulate the object being passed in.
    Country country = new Country();
    country.setId( new Long( 1 ) );
    country.setCountryName( "TestServiceCountry" );
    country.setCountryCode( "TestCode" );
    country.setAwardbanqAbbrev( "code" );
    country.setDisplayTravelAward( Boolean.FALSE );
    country.setTimeZoneId( TimeZoneId.lookup( TimeZone.getDefault().getID() ) );

    // Create a Country object to simulate what will get stored in the database.
    Country dbCountry = new Country();
    dbCountry.setId( new Long( 2 ) );
    dbCountry.setCountryName( "TestServiceCountry" );
    dbCountry.setCountryCode( "TestCode" );
    dbCountry.setAwardbanqAbbrev( "code" );
    dbCountry.setDisplayTravelAward( Boolean.FALSE );
    dbCountry.setTimeZoneId( TimeZoneId.lookup( TimeZone.getDefault().getID() ) );

    // Mock Country object for getCountryByCode Call.
    mockCountryDAO.expects( once() ).method( "getCountryByCode" ).with( same( country.getCountryCode() ) ).will( returnValue( dbCountry ) );

    try
    {
      // Call the CountryService.saveCountry. Since we are expecting to fail before
      // making it to the supplier lookup just pass in a hard coded value of 1
      // for the supplierId.
      countryService.saveCountry( new Long( 1 ), country );
    }
    catch( ServiceErrorException e )
    {
      return;
    }

    fail( "Should have thrown ServiceErrorException" );
  }

  /**
   * Test adding duplicate countryName through the service.
   */
  public void testSaveCountryInsertDuplicateNameViolation()
  {
    // todo jjd reimplement test once cm2 is fully integrated.
    // // Create a Country object to simulate the object being passed in.
    // Country country = new Country();
    // country.setCountryName( "TestServiceCountry" );
    // country.setCountryCode( "TestCode1" );
    // country.setAwardbanqAbbrev( "code1" );
    //
    // // Create a Country object to simulate what will get stored in the database.
    // Country dbCountry = new Country();
    // dbCountry.setId( new Long( 1 ) );
    // dbCountry.setCountryName( "TestServiceCountry" );
    // dbCountry.setCountryCode( "TestCode2" );
    // dbCountry.setAwardbanqAbbrev( "code2" );
    //
    // // Mock Country object for getCountryByCode Call.
    // mockCountryDAO.expects( once() ).method( "getCountryByCode" ).with( same(
    // country.getCountryCode() ))
    // .will( returnValue( null ) );
    //
    // try
    // {
    // // Call the CountryService.saveCountry. Since we are expecting to fail before
    // // making it to the supplier lookup just pass in a hard coded value of 1
    // // for the supplierId.
    // countryService.saveCountry( new Long(1), country );
    // }
    // catch( ServiceErrorException e )
    // {
    // return;
    // }
    //
    // fail( "Should have thrown ServiceErrorException" );
  }

  /**
   * Test updating duplicate countryName through the service.
   */
  public void testSaveCountryUpdateDuplicateNameViolation()
  {
    // todo jjd reimplement test once cm2 is fully integrated.
    // Create a Country object to simulate the object being passed in.
    // Country country = new Country();
    // country.setId( new Long( 1 ) );
    // country.setCountryName( "TestServiceCountry" );
    // country.setCountryCode( "TestCode" );
    // country.setAwardbanqAbbrev( "code" );
    //
    //
    // // Create a Country object to simulate what will get stored in the database.
    // Country dbCountry = new Country();
    // dbCountry.setId( new Long( 2 ) );
    // dbCountry.setCountryName( "TestServiceCountry" );
    // dbCountry.setCountryCode( "TestCode" );
    // dbCountry.setAwardbanqAbbrev( "code" );
    //
    // // Mock Country object for getCountryByCode Call.
    // mockCountryDAO.expects( once() ).method( "getCountryByCode" ).with( same(
    // country.getCountryCode() ))
    // .will( returnValue( null ) );
    //
    // try
    // {
    // // Call the CountryService.saveCountry. Since we are expecting to fail before
    // // making it to the supplier lookup just pass in a hard coded value of 1
    // // for the supplierId.
    // countryService.saveCountry( new Long(1), country );
    // }
    // catch( ServiceErrorException e )
    // {
    // return;
    // }
    //
    // fail( "Should have thrown ServiceErrorException" );
  }

  /**
   * Test adding duplicate awardbanqAbbrev through the service.
   */
  public void testSaveCountryInsertDuplicateAwardbanqAbbrevViolation()
  {
    // Create a Country object to simulate the object being passed in.
    Country country = new Country();
    country.setCountryName( "TestServiceCountry1" );
    country.setCountryCode( "TestCode1" );
    country.setAwardbanqAbbrev( "code" );
    country.setDisplayTravelAward( Boolean.FALSE );
    country.setTimeZoneId( TimeZoneId.lookup( TimeZone.getDefault().getID() ) );

    // Create a Country object to simulate what will get stored in the database.
    Country dbCountry = new Country();
    dbCountry.setId( new Long( 1 ) );
    dbCountry.setCountryName( "TestServiceCountry2" );
    dbCountry.setCountryCode( "TestCode2" );
    dbCountry.setAwardbanqAbbrev( "code" );
    dbCountry.setDisplayTravelAward( Boolean.FALSE );
    dbCountry.setTimeZoneId( TimeZoneId.lookup( TimeZone.getDefault().getID() ) );

    // Mock Country object for getCountryByCode Call.
    mockCountryDAO.expects( once() ).method( "getCountryByCode" ).with( same( country.getCountryCode() ) ).will( returnValue( null ) );

    // Mock Country object for getCountryByAwardbanqAbbrev Call.
    mockCountryDAO.expects( once() ).method( "getCountryByAwardbanqAbbrev" ).with( same( country.getAwardbanqAbbrev() ) ).will( returnValue( dbCountry ) );

    try
    {
      // Call the CountryService.saveCountry. Since we are expecting to fail before
      // making it to the supplier lookup just pass in a hard coded value of 1
      // for the supplierId.
      countryService.saveCountry( new Long( 1 ), country );
    }
    catch( ServiceErrorException e )
    {
      return;
    }

    fail( "Should have thrown ServiceErrorException" );
  }

  /**
   * Test updating duplicate awardbanqAbbrev through the service.
   */
  public void testSaveCountryUpdateDuplicateAwardbanqAbbrevViolation()
  {
    // Create a Country object to simulate the object being passed in.
    Country country = new Country();
    country.setId( new Long( 1 ) );
    country.setCountryName( "TestServiceCountry1" );
    country.setCountryCode( "TestCode1" );
    country.setAwardbanqAbbrev( "code" );
    country.setDisplayTravelAward( Boolean.FALSE );
    country.setTimeZoneId( TimeZoneId.lookup( TimeZone.getDefault().getID() ) );
    // Create a Country object to simulate what will get stored in the database.
    Country dbCountry = new Country();
    dbCountry.setId( new Long( 2 ) );
    dbCountry.setCountryName( "TestServiceCountry2" );
    dbCountry.setCountryCode( "TestCode2" );
    dbCountry.setAwardbanqAbbrev( "code" );
    dbCountry.setDisplayTravelAward( Boolean.FALSE );
    dbCountry.setTimeZoneId( TimeZoneId.lookup( TimeZone.getDefault().getID() ) );

    // Mock Country object for getCountryByCode Call.
    mockCountryDAO.expects( once() ).method( "getCountryByCode" ).with( same( country.getCountryCode() ) ).will( returnValue( null ) );

    // Mock Country object for getCountryByAwardbanqAbbrev Call.
    mockCountryDAO.expects( once() ).method( "getCountryByAwardbanqAbbrev" ).with( same( country.getAwardbanqAbbrev() ) ).will( returnValue( dbCountry ) );

    try
    {
      // Call the CountryService.saveCountry. Since we are expecting to fail before
      // making it to the supplier lookup just pass in a hard coded value of 1
      // for the supplierId.
      countryService.saveCountry( new Long( 1 ), country );
    }
    catch( ServiceErrorException e )
    {
      return;
    }

    fail( "Should have thrown ServiceErrorException" );
  }

  /**
   * Test get all Countries returns at least 1 Country
   */
  public void testGetAllCountries()
  {
    List countries = new ArrayList();
    Object[] countryObject = new Object[] { new Long( 1 ),
                                            "testCountryCode",
                                            "testCmAssetCode",
                                            "testNameCmKey",
                                            "testCampaignNbr",
                                            "testAwardbanqAbbrev",
                                            "north_american",
                                            "active",
                                            "testSupplierName" };
    countries.add( countryObject );

    // Mock object for the getAll call.
    mockCountryDAO.expects( once() ).method( "getAll" ).will( returnValue( countries ) );

    List returnedCountries = countryService.getAll();
    assertTrue( returnedCountries.size() > 0 );
  }

  /**
   * Test getting current time for country
   */
  public void testGetCurrentTimeForCountry()
  {

    Country destcountry = new Country();
    destcountry.setId( new Long( 2 ) );
    destcountry.setCountryName( "United States" );
    destcountry.setDisplayTravelAward( Boolean.FALSE );
    destcountry.setTimeZoneId( TimeZoneId.lookup( TimeZone.getDefault().getID() ) );

    Date curentdate = new Date();
    Date destdate = countryService.getCurrentTimeForCountry( destcountry );
    assertEquals( curentdate, destdate );
  }

  /**
   * Test getting current time for country
   */
  public void testConvertTimeForCountry()
  {
    Country srccountry = new Country();
    srccountry.setId( new Long( 1 ) );
    srccountry.setCountryName( "United States" );
    srccountry.setTimeZoneId( TimeZoneId.lookup( "America/Chicago" ) );
    srccountry.setDisplayTravelAward( Boolean.FALSE );

    Country destcountry = new Country();
    destcountry.setId( new Long( 2 ) );
    destcountry.setCountryName( "India" );
    destcountry.setDisplayTravelAward( Boolean.FALSE );
    destcountry.setTimeZoneId( TimeZoneId.lookup( "Asia/Calcutta" ) );
    System.out.println( TimeZoneId.lookup( TimeZoneId.ASIA_CALCUTTA ) );
    System.out.println( TimeZoneId.lookup( TimeZoneId.ASIA_CALCUTTA ).getCode() );

    Calendar calendar = Calendar.getInstance();
    calendar.set( 2011, 10, 16, 6, 30, 0 );
    Date srcdate = calendar.getTime();
    Date destdate = countryService.convertTimeForCountry( srcdate, srccountry, destcountry );
    long timeDiff = destdate.getTime() - srcdate.getTime();
    long elevenandhalfhrdiff = 41400000; // (1000 * 60 * 60 * 11.5) = 11.5 hours
    assertEquals( elevenandhalfhrdiff, timeDiff );

  }

  public void testGetAllActiveCountriesCodesAbbrevs()
  {
    List countries = new ArrayList();
    Object[] countryObject = new Object[] { new Long( 1 ),
                                            "testCountryCode",
                                            "testCmAssetCode",
                                            "testNameCmKey",
                                            "testCampaignNbr",
                                            "testAwardbanqAbbrev",
                                            "north_american",
                                            "active",
                                            "testSupplierName" };
    countries.add( countryObject );

    // Mock object for the getAll call.
    mockCountryDAO.expects( once() ).method( "getAllActiveCountriesCodesAbbrevs" ).will( returnValue( countries ) );

    List returnedCountries = countryService.getAllActiveCountriesCodesAbbrevs();
    assertTrue( returnedCountries.size() > 0 );
  }

}
/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javatest/com/biperf/core/domain/type/AddressTest.java,v $
 */

package com.biperf.core.domain.type;

import com.biperf.core.dao.country.CountryDAO;
import com.biperf.core.dao.hibernate.BaseDAOTest;
import com.biperf.core.domain.Address;
import com.biperf.core.domain.country.Country;
import com.biperf.core.domain.enums.StateType;
import com.biperf.core.utils.ApplicationContextFactory;

/**
 * AddressTest.
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
 * <td>robinsra</td>
 * <td>May 10, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class AddressTest extends BaseDAOTest
{
  // ---------------------------------------------------------------------------
  // Build Methods
  // ---------------------------------------------------------------------------

  /**
   * Builds a test North American address;
   * 
   * @return Address
   */
  private Address buildTestNAAddress()
  {
    Address address = new Address();
    address.setAddr1( "123 Main Street" );
    address.setAddr2( "Floor 33" );
    address.setAddr3( "Office 9" );
    address.setCity( "Anytown" );
    address.setStateType( StateType.lookup( "mn" ) );
    address.setCountry( getCountryDao().getCountryByCode( Country.UNITED_STATES ) );
    address.setPostalCode( "52901" );

    assertNotNull( "Country should not be null", address.getCountry() );
    assertNotNull( "StateType 'mn' should not be null", address.getStateType() );

    return address;
  } // end build TestNAAddress

  /**
   * Builds a test International address;
   * 
   * @return Address
   */
  private Address buildTestInternationalAddress()
  {
    Address address = new Address();

    address.setCountry( getCountryDao().getCountryByCode( Country.FRANCE ) );
    address.setAddr1( "123 Main Street" );
    address.setAddr2( "Floor 33" );
    address.setAddr3( "Office 9" );
    assertNotNull( "Country should not be null", address.getCountry() );

    return address;
  } // end build TestNAAddress

  // ---------------------------------------------------------------------------
  // Test Methods
  // ---------------------------------------------------------------------------

  /**
   * Test that the address is north american
   */
  public void testNAAddress()
  {
    Address northAmericanAddress = new Address();
    northAmericanAddress = buildTestNAAddress();
    boolean isNA = northAmericanAddress.isNorthAmerican();
    boolean isInternational = northAmericanAddress.isInternational();

    assertEquals( "isNA should be true", true, isNA );
    assertEquals( "isInternational should be false", false, isInternational );
  }

  /**
   * Test that the address is international
   */
  public void testInternationalAddress()
  {
    Address internationalAddress = new Address();
    internationalAddress = buildTestInternationalAddress();
    boolean isInternational = internationalAddress.isInternational();
    boolean isNA = internationalAddress.isNorthAmerican();

    assertEquals( "isInternational should be true", isInternational, true );
    assertEquals( "isNA should be false", isNA, false );
  }

  // ---------------------------------------------------------------------------
  // Private Methods
  // ---------------------------------------------------------------------------

  /**
   * Returns a reference to the country DAO.
   * 
   * @return a reference to the country DAO.
   */
  private static CountryDAO getCountryDao()
  {
    return (CountryDAO)ApplicationContextFactory.getApplicationContext().getBean( CountryDAO.BEAN_NAME );
  }
}

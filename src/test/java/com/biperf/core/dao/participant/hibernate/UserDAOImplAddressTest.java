/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javatest/com/biperf/core/dao/participant/hibernate/UserDAOImplAddressTest.java,v $
 */

package com.biperf.core.dao.participant.hibernate;

import java.util.Date;
import java.util.Iterator;

import com.biperf.core.dao.country.CountryDAO;
import com.biperf.core.dao.hibernate.BaseDAOTest;
import com.biperf.core.dao.participant.UserDAO;
import com.biperf.core.domain.Address;
import com.biperf.core.domain.country.Country;
import com.biperf.core.domain.enums.AddressType;
import com.biperf.core.domain.enums.StateType;
import com.biperf.core.domain.enums.UserType;
import com.biperf.core.domain.user.User;
import com.biperf.core.domain.user.UserAddress;
import com.biperf.core.utils.ApplicationContextFactory;
import com.biperf.core.utils.HibernateSessionManager;

/**
 * Exercises DAO implementations through JUnit.
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
 * <td>Apr 25, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class UserDAOImplAddressTest extends BaseDAOTest
{
  /**
   * Returns the country DAO.
   * 
   * @return the country DAO.
   */
  private CountryDAO getCountryDAO()
  {
    return (CountryDAO)ApplicationContextFactory.getApplicationContext().getBean( CountryDAO.BEAN_NAME );
  }

  /**
   * Returns user dao
   * 
   * @return UserDAO
   */
  protected UserDAO getUserDAO()
  {
    return (UserDAO)ApplicationContextFactory.getApplicationContext().getBean( "userDAO" );
  }

  /**
   * Builds a test address;
   * 
   * @return Address
   */
  private Address buildTestAddress()
  {
    Address address = new Address();

    address.setAddr1( "123 Main Street" );
    address.setAddr2( "Floor 33" );
    address.setAddr3( "Office 9" );
    address.setCity( "Anytown" );
    address.setStateType( StateType.lookup( "mn" ) );
    address.setPostalCode( "52901" );
    address.setCountry( getCountryDAO().getCountryByCode( Country.UNITED_STATES ) );

    assertNotNull( "Country should not be null", address.getCountry() );
    assertNotNull( "StateType 'mn' should not be null", address.getStateType() );

    return address;
  }

  /**
   * Test getting UserEmailAddress by EmailAddressIdd
   */
  public void testAddUserAddress()
  {
    // Create new user
    User expectedUser = new User();
    expectedUser.setUserType( UserType.lookup( UserType.BI ) );
    expectedUser.setFirstName( "TestFIRSTNAME" );
    expectedUser.setLastName( "TestLASTNAME" );
    expectedUser.setMasterUserId( new Long( 1 ) );
    expectedUser.setPassword( "testPASSWORD" );
    expectedUser.setActive( Boolean.TRUE );
    expectedUser.setWelcomeEmailSent( Boolean.FALSE );
    expectedUser.setUserName( "testUSERNAME" );
    expectedUser.setLoginFailuresCount( new Integer( 0 ) );
    expectedUser.setLastResetDate( new Date() );

    // Save the user
    User actualUser = getUserDAO().saveUser( expectedUser );

    // Create User Address
    UserAddress expectedUserAddress = new UserAddress();
    expectedUserAddress.setAddress( buildTestAddress() );
    expectedUserAddress.setAddressType( AddressType.lookup( "hom" ) );
    expectedUserAddress.setIsPrimary( Boolean.TRUE );
    assertNotNull( "AddressType hom should not be null", expectedUserAddress.getAddressType() );

    // Add Address
    actualUser.addUserAddress( expectedUserAddress );

    // Save the user with the address added
    User finalUser = getUserDAO().saveUser( actualUser );

    HibernateSessionManager.getSession().flush();

    assertTrue( finalUser.getUserAddresses().size() == 1 );
    assertTrue( finalUser.getUserAddresses().contains( expectedUserAddress ) );

    // check the address
    UserAddress foundUserAddress = null;
    Iterator iter = finalUser.getUserAddresses().iterator();

    while ( iter.hasNext() )
    {
      UserAddress userAddress = (UserAddress)iter.next();
      if ( userAddress.getAddressType().getCode().equals( "hom" ) )
      {
        foundUserAddress = userAddress;
      }
    }
    assertNotNull( "foundUserAddress should not be null", foundUserAddress );
    assertEquals( "Addresses should match", foundUserAddress, expectedUserAddress );
  }

} // end UserDAOImplAddressTest

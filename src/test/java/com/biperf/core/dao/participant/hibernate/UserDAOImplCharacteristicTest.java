/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javatest/com/biperf/core/dao/participant/hibernate/UserDAOImplCharacteristicTest.java,v $
 */

package com.biperf.core.dao.participant.hibernate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.biperf.core.dao.hibernate.BaseDAOTest;
import com.biperf.core.dao.participant.UserDAO;
import com.biperf.core.dao.system.CharacteristicDAO;
import com.biperf.core.domain.characteristic.UserCharacteristicType;
import com.biperf.core.domain.enums.CharacteristicDataType;
import com.biperf.core.domain.enums.CharacteristicVisibility;
import com.biperf.core.domain.enums.UserType;
import com.biperf.core.domain.user.User;
import com.biperf.core.domain.user.UserCharacteristic;
import com.biperf.core.utils.ApplicationContextFactory;

/**
 * UserDAOImplCharacteristicTest.
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
 * <td>May 4, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class UserDAOImplCharacteristicTest extends BaseDAOTest
{
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
   * Returns characteristic dao
   * 
   * @return CharacteristicDAO
   */
  protected CharacteristicDAO getCharacteristicDAO()
  {
    return (CharacteristicDAO)ApplicationContextFactory.getApplicationContext().getBean( "userCharacteristicDAO" );
  }

  /**
   * Test Adding a UserCharacteristic
   */
  public void testAddUserCharacteristic()
  {
    // Create new user
    User user = new User();
    user.setUserType( UserType.lookup( UserType.BI ) );
    user.setFirstName( "TestFIRSTNAME" );
    user.setLastName( "TestLASTNAME" );
    user.setPassword( "testPASSWORD" );
    user.setUserName( "testUSERNAME" );
    user.setActive( Boolean.TRUE );
    user.setWelcomeEmailSent( Boolean.FALSE );
    user.setLastResetDate( new Date() );

    // Save the user
    User actualUser = getUserDAO().saveUser( user );

    // Create a Characteristic
    CharacteristicDAO characteristicDAO = getCharacteristicDAO();

    String uniqueName = String.valueOf( Math.random() % 29930291 );
    UserCharacteristicType characteristic = new UserCharacteristicType();
    characteristic.setDescription( "description" );
    characteristic.setCharacteristicName( "New Char" );
    characteristic.setCharacteristicDataType( CharacteristicDataType.lookup( CharacteristicDataType.TEXT ) );
    characteristic.setMinValue( new BigDecimal( "1.5" ) );
    characteristic.setMaxValue( new BigDecimal( "10.4" ) );
    characteristic.setMaxSize( new Long( 5 ) );
    characteristic.setPlName( "new picklist" );
    characteristic.setNameCmKey( "asset_key" );
    characteristic.setCmAssetCode( "test.asset.name" + uniqueName );
    characteristic.setIsRequired( Boolean.valueOf( true ) );
    characteristic.setActive( true );
    characteristic.setVisibility( CharacteristicVisibility.lookup( CharacteristicVisibility.VISIBLE ) );

    characteristicDAO.saveCharacteristic( characteristic );

    // Create User Characteristic
    UserCharacteristic userCharacteristic = new UserCharacteristic();
    userCharacteristic.setCharacteristicValue( "VALUE 1" );
    userCharacteristic.setUserCharacteristicType( characteristic );
    userCharacteristic.setUser( user );

    // Add Characteristic
    actualUser.getUserCharacteristics().add( userCharacteristic );
    // Save the user with the characteristic added
    User finalUser = getUserDAO().saveUser( actualUser );

    flushAndClearSession();

    assertTrue( finalUser.getUserCharacteristics().size() == 1 );

    assertTrue( finalUser.getUserCharacteristics().contains( userCharacteristic ) );

  }

  /**
   * Test updating a UserCharacteristic
   */
  public void testUpdateUserCharacteristic()
  {
    // Create new user
    User user = new User();
    user.setUserType( UserType.lookup( UserType.BI ) );
    user.setFirstName( "TestFIRSTNAME" );
    user.setLastName( "TestLASTNAME" );
    user.setPassword( "testPASSWORD" );
    user.setUserName( "testUSERNAME" );
    user.setActive( Boolean.TRUE );
    user.setWelcomeEmailSent( Boolean.FALSE );
    user.setLastResetDate( new Date() );

    // Create a Characteristic
    CharacteristicDAO characteristicDAO = getCharacteristicDAO();

    UserCharacteristicType characteristic = new UserCharacteristicType();
    characteristic.setDescription( "description" );
    characteristic.setCharacteristicName( "New Char" );
    characteristic.setCharacteristicDataType( CharacteristicDataType.lookup( CharacteristicDataType.TEXT ) );
    characteristic.setMinValue( new BigDecimal( "1.5" ) );
    characteristic.setMaxValue( new BigDecimal( "10.4" ) );
    characteristic.setMaxSize( new Long( 5 ) );
    characteristic.setPlName( "new picklist" );
    String uniqueName = String.valueOf( Math.random() % 29930293 );
    characteristic.setNameCmKey( "asset_key" );
    characteristic.setCmAssetCode( "test.asset.name" + uniqueName );
    characteristic.setIsRequired( Boolean.valueOf( true ) );
    characteristic.setActive( true );
    characteristic.setVisibility( CharacteristicVisibility.lookup( CharacteristicVisibility.VISIBLE ) );

    characteristicDAO.saveCharacteristic( characteristic );

    // Create User Characteristic
    UserCharacteristic userCharacteristic = new UserCharacteristic();
    userCharacteristic.setCharacteristicValue( "VALUE 1" );
    userCharacteristic.setUserCharacteristicType( characteristic );
    userCharacteristic.setUser( user );

    user.getUserCharacteristics().add( userCharacteristic );

    // Save the user with the userCharacteristic added
    User insertedUser = getUserDAO().saveUser( user );

    String updatedCharacteristicValue = "VALUE 5";
    // Create updated User Characteristic
    UserCharacteristic userCharacteristicUpdated = new UserCharacteristic();
    userCharacteristicUpdated.setId( userCharacteristic.getId() );
    userCharacteristicUpdated.setAuditCreateInfo( userCharacteristic.getAuditCreateInfo() );
    userCharacteristicUpdated.setAuditUpdateInfo( userCharacteristic.getAuditUpdateInfo() );
    userCharacteristicUpdated.setVersion( userCharacteristic.getVersion() );
    userCharacteristicUpdated.setCharacteristicValue( updatedCharacteristicValue );
    userCharacteristicUpdated.setUser( insertedUser );
    userCharacteristicUpdated.setUserCharacteristicType( characteristic );

    getUserDAO().updateUserCharacteristic( userCharacteristicUpdated );

    User finalUser = getUserDAO().getUserById( insertedUser.getId() );

    Set finalUserCharacteristics = finalUser.getUserCharacteristics();

    String characteristicValueFound = "";

    // Check to see if the updated value matches what was set.
    Iterator iter = finalUserCharacteristics.iterator();
    while ( iter.hasNext() )
    {
      UserCharacteristic savedUserCharacteristic = (UserCharacteristic)iter.next();
      if ( savedUserCharacteristic.getId().equals( userCharacteristic.getId() ) )
      {
        characteristicValueFound = savedUserCharacteristic.getCharacteristicValue();
      }
    }

    assertEquals( characteristicValueFound, updatedCharacteristicValue );

  }

  /**
   * Test updating a list of UserCharacteristics
   */
  public void testUpdateUserCharacteristics()
  {
    // Create new user
    User user = new User();
    user.setUserType( UserType.lookup( UserType.BI ) );
    user.setFirstName( "TestFIRSTNAME" );
    user.setLastName( "TestLASTNAME" );
    user.setPassword( "testPASSWORD" );
    user.setUserName( "testUSERNAME" );
    user.setActive( Boolean.TRUE );
    user.setWelcomeEmailSent( Boolean.FALSE );
    user.setLastResetDate( new Date() );

    // Create a Characteristic
    CharacteristicDAO characteristicDAO = getCharacteristicDAO();

    String uniqueName = String.valueOf( Math.random() % 29930291 );

    UserCharacteristicType characteristic = new UserCharacteristicType();
    characteristic.setDescription( "description" );
    characteristic.setCharacteristicName( "New Char" );
    characteristic.setCharacteristicDataType( CharacteristicDataType.lookup( CharacteristicDataType.TEXT ) );
    characteristic.setMinValue( new BigDecimal( "1.5" ) );
    characteristic.setMaxValue( new BigDecimal( "10.4" ) );
    characteristic.setMaxSize( new Long( 5 ) );
    characteristic.setPlName( "new picklist" );
    characteristic.setNameCmKey( "asset_key" );
    characteristic.setCmAssetCode( "test.asset.name1" + uniqueName );
    characteristic.setIsRequired( Boolean.valueOf( true ) );
    characteristic.setActive( true );
    characteristic.setVisibility( CharacteristicVisibility.lookup( CharacteristicVisibility.VISIBLE ) );

    // Create another Characteristic
    UserCharacteristicType characteristic2 = new UserCharacteristicType();
    characteristic2.setDescription( "description" );
    characteristic2.setCharacteristicName( "New Char2" );
    characteristic2.setCharacteristicDataType( CharacteristicDataType.lookup( CharacteristicDataType.TEXT ) );
    characteristic2.setMinValue( new BigDecimal( "1.5" ) );
    characteristic2.setMaxValue( new BigDecimal( "10.4" ) );
    characteristic2.setMaxSize( new Long( 5 ) );
    characteristic2.setPlName( "new picklist" );
    characteristic2.setNameCmKey( "asset_key" );
    characteristic2.setCmAssetCode( "test.asset.name2" + uniqueName );
    characteristic2.setIsRequired( Boolean.valueOf( true ) );
    characteristic2.setActive( true );
    characteristic2.setVisibility( CharacteristicVisibility.lookup( CharacteristicVisibility.VISIBLE ) );

    // Save the two new characteristics
    characteristicDAO.saveCharacteristic( characteristic );
    characteristicDAO.saveCharacteristic( characteristic2 );

    // Create User Characteristic for the first saved characteristic
    UserCharacteristic userCharacteristic = new UserCharacteristic();
    userCharacteristic.setCharacteristicValue( "VALUE 1" );
    userCharacteristic.setUserCharacteristicType( characteristic );
    userCharacteristic.setUser( user );

    // Create another User Characteristic for the second saved characteristic
    UserCharacteristic userCharacteristic2 = new UserCharacteristic();
    userCharacteristic2.setCharacteristicValue( "VALUE 2" );
    userCharacteristic2.setUserCharacteristicType( characteristic2 );
    userCharacteristic2.setUser( user );

    // Add the two new user characteristics to the user
    user.getUserCharacteristics().add( userCharacteristic );
    user.getUserCharacteristics().add( userCharacteristic2 );

    // Save the user with the userCharacteristics added
    User insertedUser = getUserDAO().saveUser( user );
    flushAndClearSession();

    String updatedCharacteristicValue1 = "VALUE 5";
    String updatedCharacteristicValue2 = "VALUE 6";

    // Create updated User Characteristic
    UserCharacteristic userCharacteristicUpdated = new UserCharacteristic();
    userCharacteristicUpdated.setId( userCharacteristic.getId() );
    userCharacteristicUpdated.setAuditCreateInfo( userCharacteristic.getAuditCreateInfo() );
    userCharacteristicUpdated.setAuditUpdateInfo( userCharacteristic.getAuditUpdateInfo() );
    userCharacteristicUpdated.setVersion( userCharacteristic.getVersion() );
    userCharacteristicUpdated.setCharacteristicValue( updatedCharacteristicValue1 );
    userCharacteristicUpdated.setUser( insertedUser );
    userCharacteristicUpdated.setUserCharacteristicType( characteristic );

    // Create a second updated User Characteristic
    UserCharacteristic userCharacteristicUpdated2 = new UserCharacteristic();
    userCharacteristicUpdated2.setId( userCharacteristic2.getId() );
    userCharacteristicUpdated2.setAuditCreateInfo( userCharacteristic2.getAuditCreateInfo() );
    userCharacteristicUpdated2.setAuditUpdateInfo( userCharacteristic2.getAuditUpdateInfo() );
    userCharacteristicUpdated2.setVersion( userCharacteristic2.getVersion() );
    userCharacteristicUpdated2.setCharacteristicValue( updatedCharacteristicValue2 );
    userCharacteristicUpdated2.setUser( insertedUser );
    userCharacteristicUpdated2.setUserCharacteristicType( characteristic2 );

    // Create a list to hold the updated user characteristics
    List updatedUserCharacteristics = new ArrayList();

    // Add updated user characteristics to the list
    updatedUserCharacteristics.add( userCharacteristicUpdated );
    updatedUserCharacteristics.add( userCharacteristicUpdated2 );

    // Update the user passing in the list of updated user characteristics
    getUserDAO().updateUserCharacteristics( updatedUserCharacteristics );

    // Get the user by user id
    User finalUser = getUserDAO().getUserById( insertedUser.getId() );

    // Get the set of user characteristics for the user
    Set finalUserCharacteristics = finalUser.getUserCharacteristics();

    String characteristicValueFound1 = "";
    String characteristicValueFound2 = "";

    // Check to see if the updated value matches what was set.
    Iterator iter = finalUserCharacteristics.iterator();
    while ( iter.hasNext() )
    {
      UserCharacteristic savedUserCharacteristic = (UserCharacteristic)iter.next();
      if ( savedUserCharacteristic.getId().equals( userCharacteristic.getId() ) )
      {
        characteristicValueFound1 = savedUserCharacteristic.getCharacteristicValue();
      }
      if ( savedUserCharacteristic.getId().equals( userCharacteristic2.getId() ) )
      {
        characteristicValueFound2 = savedUserCharacteristic.getCharacteristicValue();
      }
    }
    assertEquals( characteristicValueFound1, updatedCharacteristicValue1 );
    assertEquals( characteristicValueFound2, updatedCharacteristicValue2 );
    System.out.println( "*************done****************" );
  }
}

/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javatest/com/biperf/core/strategy/impl/UserLockoutStrategyImplTest.java,v $
 */

package com.biperf.core.strategy.impl;

import org.jmock.Mock;
import org.jmock.MockObjectTestCase;
import org.joda.time.Instant;

import com.biperf.core.domain.system.PropertySetItem;
import com.biperf.core.domain.user.User;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.ui.user.LockoutInfo;

/**
 * UserLockoutStrategyImplTest.
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
 * <td>sharma</td>
 * <td>Apr 11, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class UserLockoutStrategyImplTest extends MockObjectTestCase
{
  private UserLockoutStrategyImpl userLockoutStrategyImpl = new UserLockoutStrategyImpl();

  private Mock systemVariableServiceMock;

  /**
   * Setup this test case. Overridden from
   * 
   * @see junit.framework.TestCase#setUp()
   * @throws Exception
   */
  @Override
  protected void setUp() throws Exception
  {
    systemVariableServiceMock = new Mock( SystemVariableService.class );
    userLockoutStrategyImpl.setSystemVariableService( (SystemVariableService)systemVariableServiceMock.proxy() );
  }

  /**
   * Test if lockout stratgey increments the failure count after a failed authentication.
   */
  public void testHandleAuthenticationFALSE()
  {
    User user = buildUser();
    // user.setStatusType( mockTrueStatusType ); // not sure about its business use
    user.setLoginFailuresCount( new Integer( 2 ) );

    userLockoutStrategyImpl.handleAuthentication( user, false );
    assertEquals( "FailedAuthentication did not change count", new Integer( 3 ), user.getLoginFailuresCount() );
  }

  /**
   * Test if lockout stratgey resets the failure count to 0 after a successfull authentication.
   */
  public void testHandleAuthenticationTRUE()
  {
    User user = buildUser();
    // user.setStatusType( mockTrueStatusType ); // not sure about its business use
    user.setLoginFailuresCount( new Integer( 0 ) );

    userLockoutStrategyImpl.handleAuthentication( user, true );
    assertEquals( "SuccessfullAuthentication did not reset count", new Integer( 0 ), user.getLoginFailuresCount() );
  }

  /**
   * Test if user is locked out based on the user's login failure count compared against a system
   * variable defined for max number of allowed failed counts.
   */
  public void testIsUserLockedoutFALSE()
  {
    User user = buildUser();
    // user.setStatusType( mockTrueStatusType ); // not sure about its business use
    user.setLoginFailuresCount( new Integer( 3 ) );

    PropertySetItem propertySetItem = buildPropertySetItem();

    systemVariableServiceMock.expects( once() ).method( "getPropertyByName" ).with( same( UserLockoutStrategyImpl.LOCKOUT_FAILURE_COUNT ) ).will( returnValue( propertySetItem ) );

    boolean lockedOut = userLockoutStrategyImpl.isUserLockedout( user );
    assertFalse( "User locked when should not be locked", lockedOut );
    systemVariableServiceMock.verify();
  }

  /**
   * Test if user is NOT locked out based on the user's login failure count compared against a
   * system variable defined for max number of allowed failed counts.
   */
  public void testIsUserLockedoutTRUE()
  {
    User user = buildUser();
    // user.setStatusType( mockTrueStatusType ); // not sure about its business use
    user.setLoginFailuresCount( new Integer( 4 ) );
    user.setLockTimeoutExpireDate( Instant.now().plus( 1000000000000L ).toDate() );
    PropertySetItem propertySetItem = buildPropertySetItem();

    systemVariableServiceMock.expects( once() ).method( "getPropertyByName" ).with( same( UserLockoutStrategyImpl.LOCKOUT_FAILURE_COUNT ) ).will( returnValue( propertySetItem ) );

    boolean lockedOut = userLockoutStrategyImpl.isUserLockedout( user );
    assertTrue( "User not locked when should be locked", lockedOut );

    systemVariableServiceMock.verify();
  }

  public void testIsUserLockedoutHardLockTRUE()
  {
    User user = buildUser();
    user.setLoginFailuresCount( new Integer( 4 ) );
    user.setAccountLocked( true );

    boolean lockedOut = userLockoutStrategyImpl.isUserLockedout( user );
    assertTrue( "User not locked when should be locked", lockedOut );

    systemVariableServiceMock.verify();
  }

  public void testIsUserLockedoutSoftLockTRUE()
  {
    User user = buildUser();
    user.setLoginFailuresCount( new Integer( 4 ) );
    user.setLockTimeoutExpireDate( Instant.now().plus( 1000000000000L ).toDate() );
    PropertySetItem propertySetItem = buildPropertySetItem();
    systemVariableServiceMock.expects( once() ).method( "getPropertyByName" ).with( same( UserLockoutStrategyImpl.LOCKOUT_FAILURE_COUNT ) ).will( returnValue( propertySetItem ) );
    boolean lockedOut = userLockoutStrategyImpl.isUserLockedout( user );
    assertTrue( "User not locked when should be locked", lockedOut );

    systemVariableServiceMock.verify();
  }

  public void testIsUserLockedoutSoftLockFalse()
  {
    User user = buildUser();
    user.setLoginFailuresCount( new Integer( 4 ) );
    user.setLockTimeoutExpireDate( Instant.now().minus( 1000000000000L ).toDate() );
    PropertySetItem propertySetItem = buildPropertySetItem();
    systemVariableServiceMock.expects( once() ).method( "getPropertyByName" ).with( same( UserLockoutStrategyImpl.LOCKOUT_FAILURE_COUNT ) ).will( returnValue( propertySetItem ) );
    boolean lockedOut = userLockoutStrategyImpl.isUserLockedout( user );
    assertTrue( "User is locked when should be not locked", !lockedOut );
    systemVariableServiceMock.verify();
  }

  private PropertySetItem buildPropertySetItem()
  {
    PropertySetItem propertySetItem = new PropertySetItem();
    propertySetItem.setIntVal( new Integer( 4 ) );
    propertySetItem.setEntityName( "test" );
    propertySetItem.setKey( UserLockoutStrategyImpl.LOCKOUT_FAILURE_COUNT );
    return propertySetItem;
  }

  private User buildUser()
  {
    User user = new User();
    user.setId( new Long( 1 ) );
    user.setUserName( "StrategyTestUser" );
    user.setFirstName( "StrategyTestFirstName" );
    user.setLastName( "StrategyTestLastName" );
    user.setMasterUserId( new Long( 1 ) ); // not clear on its business use
    user.setPassword( "StrategyTestPassword" );
    user.setActive( Boolean.TRUE );
    return user;
  }

  public void testIsUserLockedoutInfo()
  {
    User user = buildUser();
    user.setLoginFailuresCount( new Integer( 8 ) );

    PropertySetItem propertySetItem = buildPropertySetItem();

    systemVariableServiceMock.expects( atLeastOnce() ).method( "getPropertyByName" ).with( same( UserLockoutStrategyImpl.LOCKOUT_FAILURE_COUNT ) ).will( returnValue( propertySetItem ) );

    LockoutInfo lockoutInfo = userLockoutStrategyImpl.getUserLockOutInfo( user );
    assertEquals( lockoutInfo.isSoftLocked(), true );

    user.setAccountLocked( true );
    lockoutInfo = userLockoutStrategyImpl.getUserLockOutInfo( user );
    assertEquals( lockoutInfo.isHardLocked(), true );

    systemVariableServiceMock.verify();
  }

}

/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javatest/com/biperf/core/service/BaseServiceTest.java,v $
 */

package com.biperf.core.service;

import static org.easymock.EasyMock.createNiceMock;

import java.util.Collection;
import java.util.TimeZone;

import org.jmock.MockObjectTestCase;
import org.junit.Test;
import org.springframework.context.ApplicationContext;

import com.biperf.core.domain.enums.MockPickListFactory;
import com.biperf.core.domain.enums.PickListFactoryImpl;
import com.biperf.core.domain.enums.PickListItem;
import com.biperf.core.security.AuthenticatedUser;
import com.biperf.core.utils.ApplicationContextFactory;
import com.biperf.core.utils.BeaconAsserts;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.MockContentReader;
import com.biperf.core.utils.UserManager;
import com.objectpartners.cms.util.ContentReaderManager;

/**
 * This is the base Service test class. Sets up the PickListFactory.
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
 * <td>sathish</td>
 * <td>July 5, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class BaseServiceTest extends MockObjectTestCase
{

  public ApplicationContext applicationContext = createNiceMock( ApplicationContext.class );

  /**
   * Default Constructor
   */
  public BaseServiceTest()
  {
    super();

  }

  /**
   * Constructor to take the name of the test.
   * 
   * @param test
   */
  public BaseServiceTest( String test )
  {
    super( test );
  }

  /**
   * Sets up the fixture, for example, open a network connection. This method is called before a
   * test is executed.
   * 
   * @throws Exception
   */
  protected void setUp() throws Exception
  {
    super.setUp();

    // setup of the mock pick list factory
    PickListItem.setPickListFactory( new MockPickListFactory() );
    // check if the ContentReader is already set - true if we are in container.
    if ( ContentReaderManager.getContentReader() == null )
    {
      ContentReaderManager.setContentReader( new MockContentReader() );
    }

    if ( UserManager.getUser() == null )
    {
      UserManager.setUser( getDefaultAuthenticatedUser() );
    }

    ApplicationContextFactory.setApplicationContext( applicationContext );

  }

  private AuthenticatedUser getDefaultAuthenticatedUser()
  {
    AuthenticatedUser authUser = new AuthenticatedUser();
    authUser.setUserId( 100L );
    authUser.setTimeZoneId( TimeZone.getDefault().getID() );
    UserManager.setUser( authUser );
    ClientStatePasswordManager.setPassword( "password" );
    return authUser;
  }

  /**
   * Tears down the fixture, for example, close a network connection. This method is called after a
   * test is executed.
   * 
   * @throws Exception
   */
  protected void tearDown() throws Exception
  {
    super.tearDown();

    PickListItem.setPickListFactory( new PickListFactoryImpl() );
  }
  
  @Test
  public void testFoo()
  {
    // Avoiding warning when there are no tests in a class
    assertTrue( true );
  }

  /**
   * Returns a unique string.
   * 
   * @return a unique string.
   */
  protected static String getUniqueString()
  {
    return String.valueOf( System.currentTimeMillis() % 5503032 );
  }

  /**
   * Assert succeeds if the expectedObject is found as a property propertyName of one of the members
   * of actualCollection. <br/>For example, if you want to assert that a particular needle (needleA)
   * exists in a collection of Haystack objects (hayStacks), and haystack.getNeedle() exists, then
   * you would call assertContains(needleA, hayStacks, "needle"). This works for asserting deeper
   * objects as well ("needle.color").
   */
  public static void assertContains( Object expectedObject, Collection actualCollection, String propertyName )
  {
    BeaconAsserts.assertContains( expectedObject, actualCollection, propertyName );
  }
}

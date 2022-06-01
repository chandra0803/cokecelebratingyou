/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javatest/com/biperf/core/dao/hibernate/BaseDAOTest.java,v $
 */

package com.biperf.core.dao.hibernate;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.naming.NamingException;
import javax.naming.spi.NamingManager;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.Test;
import org.springframework.mock.jndi.SimpleNamingContextBuilder;
import org.springframework.transaction.TransactionStatus;

import com.biperf.core.dao.DAO;
import com.biperf.core.domain.BaseDomain;
import com.biperf.core.domain.enums.MockPickListFactory;
import com.biperf.core.domain.enums.PickListFactoryImpl;
import com.biperf.core.domain.enums.PickListItem;
import com.biperf.core.security.AuthenticatedUser;
import com.biperf.core.utils.ApplicationContextFactory;
import com.biperf.core.utils.HibernateSessionManager;
import com.biperf.core.utils.MockContentReader;
import com.biperf.core.utils.ResourceManager;
import com.biperf.core.utils.UserManager;
import com.biperf.core.utils.hibernate.SessionWrapper;
import com.objectpartners.cms.util.ContentReaderManager;

import junit.framework.TestCase;

/**
 * This is the base DAO test class. Sets up Application Context and datasource for Content Manager.
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
 * <td>dunne</td>
 * <td>Apr 7, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class BaseDAOTest extends TestCase
{
  private static final Log log = LogFactory.getLog( BaseDAOTest.class );

  private static boolean inContainer = false;
  private static int uniqueStringCounter = 1000;

  /**
   * Set up Naming Context plus application context
   */
  static
  {
    try
    {
      ApplicationContextFactory.getApplicationContext();
      // we are in container - do not set up mock application context.
      inContainer = true;
    }
    catch( IllegalStateException e )
    {
      // we are running the test suite out of container...build a mock ApplicationContext
      try
      {
        NamingManager.setInitialContextFactoryBuilder( new SimpleNamingContextBuilder() );
      }
      catch( NamingException e1 )
      {
        e1.printStackTrace();
      }

      ApplicationContextFactory.init( "datasourceContext-test.xml,dataAccessContext-hibernate.xml,dataAccessContext-hibernate-properties-test.xml" );

    }
  }

  /** tx */
  protected Transaction tx;
  protected TransactionStatus xStatus;

  /**
   * Flush and Clear the hibernate session. Flushing and clearing the session is important when
   * testing the results of an add or update by refetching the saved object. Clearing the session
   * (which evicts all object from the session cache) is done to insure that the refetched object
   * comes from the database rather than from the session cache. Flushing the session is done before
   * the clear to insure that the update/insert SQL operations(s) actually are run (since clear
   * cancels all pending SQL operations). <br/> Recommended Usage:</br>
   * 
   * <pre>
   * actualWidget.setSize( 40 );
   * widgetDAO.save( actualWidget );
   * flushAndClearSession();
   * expectedWidget = widgetDAO.loadByName( actualWidget.getName() );
   * assertEquals( expectedWidget.getSize(), actualWidget.getSize() );
   * </pre>
   * 
   * In this example, if flushAndClearSession() wasn't called, widgetDAO.loadByName() would in many
   * circumstances find the object in the session cache and would never actually refetch the object.
   * So it would return a reference to the identical object, rather than the desired behaviour of
   * refetching from the database. If the "size" property hasn't been added the associated Hibernate
   * mapping, the test would falsely succeed. <br/> Note: Neither flush() nor clear() will not cause
   * a commit to occur and all operations will occur in the same transaction.
   */
  public static void flushAndClearSession()
  {
    Session session = HibernateSessionManager.getSession();
    session.flush();
    session.clear();
  }

  /**
   * Builds a unique String for testing.
   * 
   * @return String
   */
  public static String buildUniqueString()
  {
    return "TEST" + ( System.currentTimeMillis() % 3432423 ) + "." + uniqueStringCounter++;
  }

  /**
   * setup
   * 
   * @throws Exception
   */
  protected void setUp() throws Exception
  {
    // setup of the mock pick list factory
    PickListItem.setPickListFactory( new MockPickListFactory() );

    ApplicationContextFactory.getApplicationContext();

    if ( !inContainer )
    {
      ContentReaderManager.setContentReader( new MockContentReader() );
    }

    AuthenticatedUser user = new AuthenticatedUser();
    user.setUserId( new Long( 1 ) );
    user.setUsername( "unittest" );

    UserManager.setUser( user );

    if ( !ResourceManager.hasResource( "HibernateSession" ) )
    {
      Session session = ( (SessionFactory)ApplicationContextFactory.getApplicationContext().getBean( "sessionFactory" ) ).openSession();
      ResourceManager.bindResource( "HibernateSession", new SessionWrapper( session ) );
    }

    tx = HibernateSessionManager.getSession().beginTransaction();
  }

  /**
   * tear down
   * 
   * @throws Exception
   */
  protected void tearDown() throws Exception
  {
    PickListItem.setPickListFactory( new PickListFactoryImpl() );
    try
    {
      Session session = HibernateSessionManager.getSession();
      session.flush();
      tx.rollback();
      // tx.commit();
    }
    catch( HibernateException e )
    {
      e.printStackTrace();
      throw e;
    }
    finally
    {
      HibernateSessionManager.closeSessionIfNecessary();

      if ( !inContainer )
      {
        ContentReaderManager.removeContentReader();
      }
      UserManager.removeUser();
    }
  }
  
  @Test
  public void testFoo()
  {
    // Avoiding warning when there are no tests in a class
    assertTrue( true );
  }

  /**
   * @see BaseDAOTest#assertDomainObjectEquals(String, BaseDomain, BaseDomain)
   * @param expectedObject
   * @param actualObject
   */
  protected void assertDomainObjectEquals( BaseDomain expectedObject, BaseDomain actualObject )
  {
    assertDomainObjectEquals( "", expectedObject, actualObject );
  }

  /**
   * Compare "top-level" properties of Domain Objects for equality. Equality is only checked on
   * primatives, primative wrappers, and PickListItem objects. expectedObject and actualObject must
   * be of the same type and they can not be the identical Java object.
   * 
   * @param message
   * @param expectedObject
   * @param actualObject
   */
  public static void assertDomainObjectEquals( String message, BaseDomain expectedObject, BaseDomain actualObject )
  {
    assertDomainObjectEquals( message, expectedObject, actualObject, false, null );
  }

  /**
   * Compare "top-level" properties of Domain Objects for equality. Equality is only checked on
   * primatives, primative wrappers, and PickListItem objects. expectedObject and actualObject must
   * be of the same type and they can not be the identical Java object.
   * 
   * @param message
   * @param expectedObject
   * @param actualObject
   * @param deepCompare
   * @param ignoreProperties
   */
  public static void assertDomainObjectEquals( String message, BaseDomain expectedObject, BaseDomain actualObject, boolean deepCompare, Set ignoreProperties )
  {
    // Formatting
    if ( message != null && !message.equals( "" ) )
    {
      message += ": ";
    }

    // First, check for null issues.
    if ( expectedObject == null && actualObject == null )
    {
      // Both being null is valid.
      return;
    }
    if ( expectedObject == null )
    {
      fail( message + "Domain objects are not Equal: Expected is null but Actual is not null" );
    }
    if ( actualObject == null )
    {
      fail( message + "Domain objects are not Equal: Actual is null but Expected is not null" );
    }

    // class type must be the same
    if ( !expectedObject.getClass().equals( actualObject.getClass() ) )
    {
      fail( message + "Domain objects are not Equal: There are of different Java type. Expected Type:" + expectedObject.getClass().getName() + "  Actual Type:" + actualObject.getClass().getName() );
    }

    assertNotSame( message + "Expected and Actual are same objects.  Note: If you really want to test for sameness, use assertSame() instead: ", expectedObject, actualObject );

    // Check Individual Properties
    try
    {
      Map expectedProps = PropertyUtils.describe( expectedObject );
      Map actualProps = PropertyUtils.describe( actualObject );

      Set expectedKeySet = expectedProps.keySet();
      for ( Iterator iter = expectedKeySet.iterator(); iter.hasNext(); )
      {
        String propName = (String)iter.next();
        if ( isComparibleDomainObjectProperty( propName, ignoreProperties ) )
        {

          Object expectedPropValue = expectedProps.get( propName );
          Object actualPropValue = actualProps.get( propName );

          // Check for null issues
          if ( expectedPropValue == null && actualPropValue == null )
          {
            // Both being null is valid.
            continue;
          }
          if ( expectedPropValue == null )
          {
            fail( message + "Domain objects are not Equal: Expected is null but Actual is not null : property=" + propName );
          }
          if ( actualPropValue == null )
          {
            fail( message + "Domain objects are not Equal: Actual is null but Expected is not null : property=" + propName );
          }

          // Compare enum values too if possible.
          // Do shallow compare, only basic objects, not Associations.
          if ( expectedPropValue instanceof String || expectedPropValue instanceof BigDecimal || expectedPropValue instanceof Integer || expectedPropValue instanceof Long
              || expectedPropValue instanceof Float || expectedPropValue instanceof Double || expectedPropValue instanceof Boolean || expectedPropValue instanceof PickListItem )
          {
            assertEquals( message + "Bean properties were not equal for property name:" + propName + ". ", expectedPropValue, actualPropValue );
          }

          // For objects with java.util.Date properties, hibernate is actually returning a
          // java.sql.Timestamp (a subclass of Date).
          // The javadoc clearly states that a Timestamp.equals(...) will return false if compared
          // to a Date.
          // Thus, in the case of Dates we will need to remove the nanoseconds when checking for
          // equality.
          if ( expectedPropValue instanceof Date )
          {

            // This truncates the nanoseconds of each time value
            long expectedLong = ( (Date)expectedPropValue ).getTime() / 1000;
            long actualLong = ( (Date)actualPropValue ).getTime() / 1000;

            assertEquals( message + "Bean properties were not equal for property name:" + propName + ". ", expectedLong, actualLong );
          }

          if ( expectedPropValue instanceof BaseDomain )
          {

            BaseDomain expectedPropValueDomain = (BaseDomain)expectedPropValue;
            BaseDomain actualPropValueDomain = (BaseDomain)actualPropValue;
            if ( actualPropValueDomain == null )
            {
              fail( message + "Associated Domain objects are not Equal: Actual is null but Expected is not null" );
            }

            // Compare ids if expected base domain object is not null and contains an id.
            // This assumes that the "actual" has been retrieved from the db.
            // This test is useful for many-to-one hibernate mapping checking
            if ( expectedPropValueDomain.getId() != null )
            {
              assertEquals( "Associated Domain Object's ids were not equal for property name:" + propName + ". ", expectedPropValueDomain.getId(), actualPropValueDomain.getId() );
            }
          }

          if ( deepCompare && expectedPropValue instanceof Collection )
          {
            assertEquals( message + "Bean properties were not equal for property name:" + propName + ". ", expectedPropValue, actualPropValue );
          }
        }
      }
    }
    catch( IllegalAccessException e )
    {
      // Wrapping with RuntimeException to simplify caller, and will produce the desired result: a
      // Junit Error.
      throw new RuntimeException( "Exception comparing domain objects", e );
    }
    catch( InvocationTargetException ite )
    {
      // Wrapping with RuntimeException to simplify caller, and will produce the desired result: a
      // Junit Error.
      throw new RuntimeException( "Exception comparing domain objects", ite );
    }
    catch( NoSuchMethodException nsme )
    {
      // Wrapping with RuntimeException to simplify caller, and will produce the desired result: a
      // Junit Error.
      throw new RuntimeException( "Exception comparing domain objects", nsme );
    }

  }

  /**
   * Returns false if the object property should not be compared (for props such as id and version).
   * 
   * @param propName
   * @return boolean
   */
  public static boolean isComparibleDomainObjectProperty( String propName, Set ignoreProperties )
  {
    if ( ignoreProperties != null )
    {
      if ( ignoreProperties.contains( propName ) )
      {
        return false;
      }
    }
    return !propName.equals( "id" ) && !propName.equals( "version" ) && !propName.equals( "class" );
  }

  protected boolean isInContainer()
  {
    return inContainer;
  }

  /**
   * Runs the bare test sequence. This overwrites <code>runBare()</code> in <code>TestCase</code>
   * such that exceptions in <code>tearDown()</code> are only shown if the test itself didn't
   * throw.
   * 
   * @throws Throwable
   */
  public void runBare() throws Throwable
  {
    setUp();
    try
    {
      runTest();
    }
    catch( Throwable testException )
    {
      try
      {
        tearDown();
      }
      catch( Exception teardownException )
      {
        log.error( "Exception in teardown after exception in test", teardownException );
      }

      // Rethrow original exception that caused the test to fail.
      throw testException;
    }
    tearDown();
  }

  /**
   * Get a DAO from the beanFactory.
   * 
   * @param beanName
   * @return DAO
   */
  public static DAO getDAO( String beanName )
  {
    return (DAO)ApplicationContextFactory.getApplicationContext().getBean( beanName );
  }

  /**
   * Returns a unique string.
   * 
   * @return a unique string.
   */
  protected String getUniqueString()
  {
    return "" + uniqueStringCounter++;
  }

}

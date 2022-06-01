
package com.biperf.core.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.fail;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.PropertyUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.TransactionStatus;

import com.biperf.core.dao.participant.UserDAO;
import com.biperf.core.domain.BaseDomain;
import com.biperf.core.domain.enums.MockPickListFactory;
import com.biperf.core.domain.enums.PickListFactoryImpl;
import com.biperf.core.domain.enums.PickListItem;
import com.biperf.core.security.AuthenticatedUser;
import com.biperf.core.utils.BeaconAsserts;
import com.biperf.core.utils.HibernateSessionManager;
import com.biperf.core.utils.MockContentReader;
import com.biperf.core.utils.ResourceManager;
import com.biperf.core.utils.UserManager;
import com.biperf.core.utils.hibernate.SessionWrapper;
import com.objectpartners.cms.util.ContentReaderManager;

@RunWith( SpringJUnit4ClassRunner.class )
@ContextConfiguration( locations = { "classpath:datasourceContext-test.xml", "classpath:dataAccessContext-hibernate.xml", "classpath:dataAccessContext-hibernate-properties-test.xml" } )
public class BaseDAOSpringRunnerTest
{

  @Autowired
  public ApplicationContext context;

  @Autowired
  com.biperf.cache.oscache.ManageableCacheAdministrator cacheadmin;

  @Autowired
  UserDAO dao;

  protected Transaction tx;
  protected TransactionStatus xStatus;

  public static void flushAndClearSession()
  {
    Session session = HibernateSessionManager.getSession();
    session.flush();
    session.clear();
  }

  @BeforeClass
  public static void beforeClass()
  {
    PickListItem.setPickListFactory( new MockPickListFactory() );
    ContentReaderManager.setContentReader( new MockContentReader() );
    UserManager.setUser( getAuthenticatedUser() );
  }

  @AfterClass
  public static void afterClass() throws Exception
  {
    PickListItem.setPickListFactory( new PickListFactoryImpl() );
    ContentReaderManager.removeContentReader();
    UserManager.removeUser();
  }

  @Before
  public void before()
  {
    SessionFactory sessionFactory = (SessionFactory)context.getBean( "sessionFactory" );
    Session session = sessionFactory.openSession();
    SessionWrapper sw = new SessionWrapper( session );
    ResourceManager.bindResource( "HibernateSession", sw );
    tx = HibernateSessionManager.getSession().beginTransaction();

  }

  @After
  public void after()
  {
    HibernateSessionManager.getSession().flush();
    tx.rollback();
    HibernateSessionManager.closeSessionIfNecessary();
  }

  public static void assertDomainObjectEquals( BaseDomain expectedObject, BaseDomain actualObject )
  {
    assertDomainObjectEquals( "", expectedObject, actualObject );
  }

  public static void assertDomainObjectEquals( String message, BaseDomain expectedObject, BaseDomain actualObject )
  {
    assertDomainObjectEquals( message, expectedObject, actualObject, false, null );
  }

  @SuppressWarnings( { "rawtypes", "unused" } )
  public static void assertDomainObjectEquals( String message, BaseDomain expectedObject, BaseDomain actualObject, boolean deepCompare, Set ignoreProperties )
  {

    if ( message != null && !message.equals( "" ) )
    {
      message += ": ";
    }

    if ( expectedObject == null && actualObject == null )
    {

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

    if ( !expectedObject.getClass().equals( actualObject.getClass() ) )
    {
      fail( message + "Domain objects are not Equal: There are of different Java type. Expected Type:" + expectedObject.getClass().getName() + "  Actual Type:" + actualObject.getClass().getName() );
    }

    assertNotSame( message + "Expected and Actual are same objects.  Note: If you really want to test for sameness, use assertSame() instead: ", expectedObject, actualObject );

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

          if ( expectedPropValue == null && actualPropValue == null )
          {

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

          if ( expectedPropValue instanceof String || expectedPropValue instanceof BigDecimal || expectedPropValue instanceof Integer || expectedPropValue instanceof Long
              || expectedPropValue instanceof Float || expectedPropValue instanceof Double || expectedPropValue instanceof Boolean || expectedPropValue instanceof PickListItem )
          {
            assertEquals( message + "Bean properties were not equal for property name:" + propName + ". ", expectedPropValue, actualPropValue );
          }

          if ( expectedPropValue instanceof Date )
          {

            long expectedLong = ( (Date)expectedPropValue ).getTime() / 1000;
            long actualLong = ( (Date)actualPropValue ).getTime() / 1000;

            assertEquals( message + "Bean properties were not equal for property name:" + propName + ". ", expectedLong, actualLong );
          }

          if ( expectedPropValue instanceof BaseDomain )
          {

            BaseDomain expectedPropValueDomain = (BaseDomain)expectedPropValue;
            BaseDomain actualPropValueDomain = (BaseDomain)actualPropValue;

            if ( expectedPropValueDomain == null && actualPropValueDomain == null )
            {

              continue;
            }
            if ( expectedPropValueDomain == null )
            {
              fail( message + "Associated Domain objects are not Equal: Expected is null but Actual is not null" );
            }
            if ( actualPropValueDomain == null )
            {
              fail( message + "Associated Domain objects are not Equal: Actual is null but Expected is not null" );
            }

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
      throw new RuntimeException( "Exception comparing domain objects", e );
    }
    catch( InvocationTargetException ite )
    {
      throw new RuntimeException( "Exception comparing domain objects", ite );
    }
    catch( NoSuchMethodException nsme )
    {
      throw new RuntimeException( "Exception comparing domain objects", nsme );
    }
  }

  @SuppressWarnings( "rawtypes" )
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

  @SuppressWarnings( "rawtypes" )
  public static void assertContains( Object expectedObject, Collection actualCollection, String propertyName )
  {
    BeaconAsserts.assertContains( expectedObject, actualCollection, propertyName );
  }

  public static AuthenticatedUser getAuthenticatedUser()
  {
    AuthenticatedUser user = new AuthenticatedUser();
    user.setUserId( new Long( 1 ) );
    user.setUsername( "unittest" );
    return user;
  }

  @Test
  public void varifyDAOContextFullyLoaded()
  {
    assertNotNull( context );
    assertNotNull( cacheadmin );
  }

}

/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javatest/com/biperf/core/domain/enums/BaseEnumTest.java,v $
 *
 */

package com.biperf.core.domain.enums;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.AbstractPlatformTransactionManager;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.biperf.core.dao.DAO;
import com.biperf.core.dao.system.SystemVariableDAO;
import com.biperf.core.domain.system.PropertySetItem;
import com.biperf.core.security.AuthenticatedUser;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.utils.ApplicationContextFactory;
import com.biperf.core.utils.HibernateSessionManager;
import com.biperf.core.utils.MockContentReaderFactory;
import com.biperf.core.utils.UserManager;
import com.objectpartners.cms.domain.enums.RoleEnum;
import com.objectpartners.cms.service.ContentReader;
import com.objectpartners.cms.util.CmsConfiguration;
import com.objectpartners.cms.util.ContentReaderFactory;
import com.objectpartners.cms.util.ContentReaderManager;

import junit.framework.TestCase;

/**
 * BaseEnumTest <p/> <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>dunne</td>
 * <td>Oct 3, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 * @author dunne
 *
 */
public class BaseEnumTest extends TestCase
{
  private static final Log log = LogFactory.getLog( BaseEnumTest.class );

  private static boolean inContainer = false;
  private static int uniqueStringCounter = 1000;
  protected static ApplicationContext cmApplicationContext = null;

  public void testA()
  {
    assertTrue( true );
  }

  /** tx */
  protected Transaction tx;
  protected TransactionStatus xStatus;

  // protected Transaction tx2;

  /**
   * setup
   * 
   * @throws Exception
   */
  protected void setUp() throws Exception
  {
    ApplicationContextFactory.init( "datasourceContext-test.xml,dataAccessContext-hibernate.xml,dataAccessContext-hibernate-properties-test.xml" );

    if ( cmApplicationContext == null )
    {
      try
      {
        // only load the cmApplicationContext if we are out of container.
        cmApplicationContext = new ClassPathXmlApplicationContext( new String[] { "datasourceContext-test.xml", "cmsApplicationContext-test.xml", "cmsApplicationContext-security.xml" } );
      }
      catch( Throwable t )
      {
        t.printStackTrace();
      }
    }

    /*
     * //Setting here didn't work, but these variables all need to be set for this to work.
     * System.setProperty( "-Dcom.sun.aas.instanceName", "localhost"); System.setProperty(
     * "-Dbi.appGrpNum", "localhost" ); System.setProperty( "-DDB_DIALECT",
     * "com.objectpartners.cms.util.hibernate.CustomOracle10gDialect" );
     */
    PickListItem.setPickListFactory( new MockPickListFactory() );
    ApplicationContext appCtx = ApplicationContextFactory.getApplicationContext();
    ContentReaderFactory readerFactory = null;
    if ( !inContainer )
    {

      List<GrantedAuthority> grantedAuthorityList = new ArrayList<GrantedAuthority>();
      SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority( "ROLE_" + RoleEnum.CONTENT_ADMINISTRATOR.getName() );
      grantedAuthorityList.add( simpleGrantedAuthority );

      // only setup a ContentReader if we are out of container.

      TestingAuthenticationToken token = new TestingAuthenticationToken( RoleEnum.CONTENT_ADMINISTRATOR.getName(), "credentials", grantedAuthorityList );

      /*
       * avoid redeployments issues by clearing the context instead of creating a new one
       */
      SecurityContextHolder.clearContext();
      SecurityContextHolder.getContext().setAuthentication( token );

      readerFactory = new MockContentReaderFactory();
      readerFactory.setCmsConfiguration( (CmsConfiguration)appCtx.getBean( "cmsConfiguration" ) );
      ContentReader reader = readerFactory.createContentReader( null );
      reader.setApplicationContext( cmApplicationContext );
      ContentReaderManager.setContentReader( reader );
    }

    AbstractPlatformTransactionManager cmsXaction = (AbstractPlatformTransactionManager)ContentReaderManager.getContentReader().getApplicationContext().getBean( "transactionManager" );
    xStatus = cmsXaction.getTransaction( new DefaultTransactionDefinition() );
    xStatus.setRollbackOnly();

    HibernateSessionManager.openSession();

    if ( !inContainer )
    {
      tx = HibernateSessionManager.getSession().beginTransaction();
    }

    AuthenticatedUser user = new AuthenticatedUser();
    user.setUserId( new Long( 1 ) );
    user.setUsername( "unittest" );

    UserManager.setUser( user );

    // Sys variable lookup needed for module-aware picklists, mock out service to always return
    // null.
    PickListFactory pickListFactory = PickListItem.getPickListFactory();
    ( (MockPickListFactory)pickListFactory ).setSystemVariableService( new SystemVariableService()
    {

      public void deleteProperty( PropertySetItem prop )
      {
        // nothing to do

      }

      public void saveProperty( PropertySetItem prop )
      {
        // nothing to do

      }

      public PropertySetItem getPropertyByNameAndEnvironment( String propertyName )
      {
        return null;
      }

      public PropertySetItem getPropertyByName( String propertyName )
      {
        return null;
      }

      public List getAllProperties()
      {
        return null;
      }

      public void setSystemVariableDAO( SystemVariableDAO systemVariableDAO )
      {
        // nothing to do
      }

      public void savePropertyValue( PropertySetItem prop )
      {
        // nothing to do

      }

      public void setContextName( String contextName )
      {
        // nothing to do

      }

      public String getContextName()
      {
        return null;
      }

      public boolean isGDEV()
      {
        return false;
      }

      @Override
      public String getPrefix()
      {
        return null;
      }

      @Override
      public String getAESDecryptedValue( String encryptedValue )
      {
        return null;
      }

      @Override
      public PropertySetItem getDefaultLanguage()
      {
        // TODO Auto-generated method stub
        return null;
      }

      @Override
      public String getAESDecryptedValue( String encryptedValue, String aesKey, String iv )
      {
        // TODO Auto-generated method stub
        return null;
      }
    } );

  }

  /**
   * tear down
   * 
   * @throws Exception
   */
  protected void tearDown() throws Exception
  {
    try
    {
      Session session = HibernateSessionManager.getSession();
      session.flush();
      if ( !inContainer )
      {
        tx.rollback();

      }
      AbstractPlatformTransactionManager xActionManager = (AbstractPlatformTransactionManager)ContentReaderManager.getContentReader().getApplicationContext().getBean( "transactionManager" );
      xActionManager.rollback( xStatus );

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
      PickListFactory pickListFactory = PickListItem.getPickListFactory();
      ( (MockPickListFactory)pickListFactory ).setSystemVariableService( null );
    }
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
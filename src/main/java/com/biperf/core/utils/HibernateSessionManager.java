/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/utils/HibernateSessionManager.java,v $
 */

package com.biperf.core.utils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.JDBCException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.engine.PersistenceContext;
import org.hibernate.persister.entity.EntityPersister;

import com.biperf.core.utils.hibernate.SessionWrapper;

/**
 * HibernateSessionManager.
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
 * <td>jdunne</td>
 * <td>Feb 23, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class HibernateSessionManager
{

  private static final Log logger = LogFactory.getLog( HibernateSessionManager.class );

  private static final String sessionKey = "HibernateSession";

  /**
   * Check if there is a resource for the given key bound to the current thread.
   * 
   * @return if there is a value bound to the current thread
   */
  public static boolean isSessionDefined()
  {
    return ResourceManager.hasResource( sessionKey );
  }

  /**
   * Retrieve the Hibernate Session that is bound to the current thread.
   * 
   * @return a Hibernate Session bound to the current thread, or null if none
   */
  public static Session getSession()
  {
    return (Session)ResourceManager.getResource( sessionKey );
  }

  /**
   * Bind the given resource for the given key to the current thread.
   * 
   * @return Session
   * @throws IllegalStateException if there is already a value bound to the thread
   */
  public static Session openSession() throws IllegalStateException
  {

    if ( ResourceManager.hasResource( sessionKey ) )
    {
      throw new IllegalStateException( "Session already opened and bound to thread [" + Thread.currentThread().getName() + "]" );
    }

    SessionFactory sf = getSessionFactory();
    // FIX for grabbing 2 connections
    // The call below is causing 2 connections to be opened, one here and one in the Spring/hibernate transaction mgr
    //Session session = sf.openSession();
    
    Session session = sf.getCurrentSession();
    
    // wrap the Hibernate session to our own class;
    // to force a flush before a query due to
    // internal change in Hibernate 3.2.3
    SessionWrapper sw = new SessionWrapper( session );
    ResourceManager.bindResource( sessionKey, sw );

    if ( logger.isDebugEnabled() )
    {
      logger.debug( "Hibernate Session opened." );
    }
    return sw;
  }

  /**
   * Close the given Session.
   */
  public static void closeSessionIfNecessary()
  {
    Session session = getSession();
    if ( session == null )
    {
      return;
    }
    // FIX for grabbing 2 connections
    // Don't close the session here, allow the Spring/Hibernate transaction mgr handle that
    //doClose( session );
    ResourceManager.unbindResource( sessionKey );
  }

  private static SessionFactory getSessionFactory()
  {
    return (SessionFactory)ApplicationContextFactory.getApplicationContext().getBean( "sessionFactory" );
  }

  /**
   * Perform the actual closing of the Hibernate Session.
   * 
   * @param session Session to close
   */
  private static void doClose( Session session )
  {
    if ( logger.isDebugEnabled() )
    {
      logger.debug( "Closing Hibernate session" );
    }
    try
    {
      session.close();
    }
    catch( JDBCException ex )
    {
      // SQLException underneath
      logger.error( "Could not close Hibernate session", ex.getSQLException() );
    }
    catch( HibernateException ex )
    {
      logger.error( "Could not close Hibernate session" );
    }
  }

  /**
   * This method is moved from HibernateUtil to keep all dependencies of SessionWrapper
   * class to one class.
   * @return
   */
  public static PersistenceContext getPersistenceContext()
  {
    return ( (SessionWrapper)getSession() ).getPersistenceContext();
  }

  /**
   * This method is moved from HibernateUtil to keep all dependencies of SessionWrapper
   * class to one class.
   * @return
   */
  public static EntityPersister getEntityPersister( Object domainObject )
  {
    return ( (SessionWrapper)getSession() ).getEntityPersister( domainObject );
  }

}

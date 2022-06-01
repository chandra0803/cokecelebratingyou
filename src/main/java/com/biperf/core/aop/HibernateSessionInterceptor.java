/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/aop/HibernateSessionInterceptor.java,v $
 */

package com.biperf.core.aop;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.exception.GenericJDBCException;

import com.biperf.core.exception.BeaconConnectionException;
import com.biperf.core.utils.HibernateSessionManager;

/**
 * HibernateSessionInterceptor is an Interceptor to manage the hibernate session - gets invoked
 * around the service layer.
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
 * <td>zahler</td>
 * <td>Mar 9, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 */
public class HibernateSessionInterceptor implements MethodInterceptor
{

  private static Log log = LogFactory.getLog( HibernateSessionInterceptor.class );

  /**
   * Overridden from
   * 
   * @see org.aopalliance.intercept.MethodInterceptor#invoke(org.aopalliance.intercept.MethodInvocation)
   * @param methodInvocation
   * @return Object
   * @throws Throwable
   */
  public Object invoke( MethodInvocation methodInvocation ) throws Throwable
  {
    if ( log.isDebugEnabled() )
    {
      log.debug( ">>> invoke" );
    }
    Object retVal = null;
    boolean isNew = true;
    RuntimeException caughtException = null;

    if ( HibernateSessionManager.isSessionDefined() )
    {
      isNew = false;
    }

    if ( log.isDebugEnabled() )
    {
      log.debug( "  invoke - isSessionNew:" + isNew );
    }

    Session session = null;
    if ( isNew )
    {
      session = HibernateSessionManager.openSession();
    }

    try
    {
      retVal = methodInvocation.proceed();
    }
    catch( RuntimeException re )
    {
      if ( re instanceof GenericJDBCException )
      {
        GenericJDBCException gjdbce = (GenericJDBCException)re;
        if ( gjdbce.getMessage() != null && gjdbce.getMessage().toUpperCase().indexOf( "CANNOT OPEN CONNECTION" ) >= 0 )
        {
          // save for use in the finally block
          caughtException = new BeaconConnectionException( re );
          throw caughtException;
        }
      }
      // save for use in the finally block
      caughtException = re;
      throw caughtException;
    }
    finally
    {
      if ( isNew )
      {
        try
        {
          if ( caughtException == null )
          {
            session.flush();
            // do a second flush here to save the history tables from our postupdate event
            // listeners.
            session.flush();
            if ( log.isDebugEnabled() )
            {
              log.debug( "  invoke - Session Flushed." );
            }
          }
          else
          {
            throw caughtException;
          }
        }
        catch( HibernateException ex )
        {
          throw ex;
        }
        finally
        {
          HibernateSessionManager.closeSessionIfNecessary();
          if ( log.isDebugEnabled() )
          {
            log.debug( "<<< invoke_finally" );
          }
        }
      } // isNew
    } // finally

    if ( log.isDebugEnabled() )
    {
      log.debug( "<<< invoke" );
    }

    return retVal;

  }

}

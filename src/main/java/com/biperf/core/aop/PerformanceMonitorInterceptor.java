/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/aop/PerformanceMonitorInterceptor.java,v $
 */

package com.biperf.core.aop;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.utils.UserManager;
import com.jamonapi.Monitor;
import com.jamonapi.MonitorFactory;

/**
 * PerformanceMonitorInterceptor is an Interceptor to handle performance monitoring on beforeAdvice
 * and afterReturningAdvice upon method invocation.
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
 * <td>Mar 8, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 */
public class PerformanceMonitorInterceptor implements MethodInterceptor
{
  private static final Log log = LogFactory.getLog( PerformanceMonitorInterceptor.class );
  private static int errorLimit = 15000;

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
    // Monitor Key
    String monitorKey = methodInvocation.getMethod().getDeclaringClass().getName() + "." + methodInvocation.getMethod().getName();

    // start the performance monitor
    Monitor perfMon = MonitorFactory.start( monitorKey );
    long start = errorLimit > 0 ? System.currentTimeMillis() : 0;
    try
    {
      // Do the invocation
      return methodInvocation.proceed();
    }
    finally
    {
      // stop the performance monitor
      perfMon.stop();
      long duration = errorLimit > 0 ? System.currentTimeMillis() - start : 0;
      if ( log.isDebugEnabled() )
      {
        log.debug( "*PERF* " + monitorKey + " took " + duration + "ms to execute" );
      }
      if ( duration >= errorLimit && errorLimit != 0 )
      {
        String userName = "anonymous";
        if ( UserManager.isUserLoggedIn() )
        {
          userName = UserManager.getUserName();
        }
        log.error( "*PERF* " + monitorKey + " took " + duration + "ms to execute for user: " + userName );
      }
    }
  }

  public static void setErrorLimit( int limit )
  {
    if ( limit >= 0 )
    {
      errorLimit = limit;
    }
  }
}

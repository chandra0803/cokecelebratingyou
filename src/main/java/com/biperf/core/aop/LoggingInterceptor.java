/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/aop/LoggingInterceptor.java,v $
 */

package com.biperf.core.aop;

import java.lang.reflect.Method;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import com.biperf.core.utils.LoggingManager;

/**
 * LoggingInterceptor is an Interceptor to handle logging events upon beforeAdvice and
 * afterReturningAdvice on method invocation.
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
public class LoggingInterceptor implements MethodInterceptor
{
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
    Method method = methodInvocation.getMethod();
    LoggingManager.enter( method );

    Object object = methodInvocation.proceed();

    LoggingManager.exit( method );

    return object;
  }

}

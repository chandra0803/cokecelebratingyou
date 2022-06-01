/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/utils/LoggingManager.java,v $
 */

package com.biperf.core.utils;

import java.lang.reflect.Method;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Manager class which will handle logging all enter and exit for a method. This will normally be
 * called from the LoggingBeforeAdvice aspect but can be called separately.
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
 * <td>crosenquest</td>
 * <td>Mar 7, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class LoggingManager
{

  /**
   * Logs an entry for the given method on enter of that method.
   * 
   * @param method
   */
  public static void enter( Method method )
  {
    Log logger = LogFactory.getLog( method.getDeclaringClass().getName() );
    if ( logger.isDebugEnabled() )
    {
      logger.debug( ">>> " + method.getDeclaringClass().getName() + " " + method.getName() );
    }
  }

  /**
   * Logs an entry for a given method on exit of that method.
   * 
   * @param method
   */
  public static void exit( Method method )
  {
    Log logger = LogFactory.getLog( method.getDeclaringClass().getName() );
    if ( logger.isDebugEnabled() )
    {
      logger.debug( "<<< " + method.getDeclaringClass().getName() + " " + method.getName() );
    }
  }
}

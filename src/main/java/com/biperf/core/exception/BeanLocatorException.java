/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/exception/BeanLocatorException.java,v $
 */

package com.biperf.core.exception;

/**
 * BeanLocatorException is thrown when bean isn't found in context.
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
 * <td>kumars</td>
 * <td>Mar 31, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class BeanLocatorException extends BeaconRuntimeException
{

  public BeanLocatorException()
  {
    super();
  }

  public BeanLocatorException( String message )
  {
    super( message );
  }

  public BeanLocatorException( Exception exception )
  {
    super( exception );
  }

  public BeanLocatorException( String message, Exception exception )
  {
    super( message, exception );
  }
}

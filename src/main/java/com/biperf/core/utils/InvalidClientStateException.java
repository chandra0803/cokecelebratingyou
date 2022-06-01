/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/utils/InvalidClientStateException.java,v $
 */

package com.biperf.core.utils;

/**
 * InvalidClientStateException
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
 * <td>Thomas Eaton</td>
 * <td>Oct 7, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * </p>
 * 
 *
 */

public class InvalidClientStateException extends Exception
{
  /**
   * Constructs an <code>InvalidClientStateException</code> with no detail message.
   */
  public InvalidClientStateException()
  {
    super();
  }

  /**
   * Constructs an <code>InvalidClientStateException</code> with the specified detail message.
   * 
   * @param message the detail message.
   */
  public InvalidClientStateException( String message )
  {
    super( message );
  }
}

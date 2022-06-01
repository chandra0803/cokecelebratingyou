/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/utils/crypto/InvalidMacException.java,v $
 */

package com.biperf.core.utils.crypto;

/**
 * InvalidMacException
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
 * <td>Oct 10, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * </p>
 * 
 *
 */

public class InvalidMacException extends Exception
{
  /**
   * Constructs an <code>InvalidMacException</code> with no detail message.
   */
  public InvalidMacException()
  {
    super();
  }

  /**
   * Constructs an <code>InvalidMacException</code> with the specified detail message.
   * 
   * @param message the detail message.
   */
  public InvalidMacException( String message )
  {
    super( message );
  }
}

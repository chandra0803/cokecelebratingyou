/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/exception/UniqueConstraintViolationException.java,v $
 */

package com.biperf.core.exception;

/**
 * UniqueConstraintViolationException.
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
 * <td>Apr 20, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class UniqueConstraintViolationException extends Exception
{
  public UniqueConstraintViolationException()
  {
    super( "UniqueConstraintViolationException" );
  }
}

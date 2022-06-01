/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/security/exception/AccountLockoutException.java,v $
 */

package com.biperf.core.security.exception;

import javax.security.auth.login.LoginException;

/**
 * AccountLockoutException.
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
 * <td>May 19, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class AccountLockoutException extends LoginException
{
  public AccountLockoutException()
  {
    super( "AccountLockoutException" );
  }

  public AccountLockoutException( String type )
  {
    super( type );
  }
}

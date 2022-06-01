/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/security/exception/AccountLockoutException.java,v $
 */

package com.biperf.core.security.exception;

@SuppressWarnings( "serial" )
public class AccountHardLockoutException extends AccountLockoutException
{
  public AccountHardLockoutException()
  {
    super( "AccountHardLockoutException" );
  }
}

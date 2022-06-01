
package com.biperf.core.service.security.impl;

import org.springframework.security.core.AuthenticationException;

/**
 *
 * @author Jason Sedey
 */
public class NonPaxMobileAppLoginException extends AuthenticationException
{
  // ~ Constructors
  // ===================================================================================================

  /**
       * Constructs a <code>NonPaxMobileAppLoginException</code> with the specified
       * message.
       *
       * @param msg the detail message
       */
  public NonPaxMobileAppLoginException( String msg )
  {
    super( msg );
  }

  /**
       * Constructs a <code>NonPaxMobileAppLoginException</code> with the specified
       * message and root cause.
       *
       * @param msg the detail message
       * @param t root cause
       */
  public NonPaxMobileAppLoginException( String msg, Throwable t )
  {
    super( msg, t );
  }
}

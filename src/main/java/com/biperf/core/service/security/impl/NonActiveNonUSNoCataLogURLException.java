
package com.biperf.core.service.security.impl;

import org.springframework.security.core.AuthenticationException;

/**
 *
 * @author Mattam Krishna
 */
public class NonActiveNonUSNoCataLogURLException extends AuthenticationException
{
  // ~ Constructors
  // ===================================================================================================

  /**
       * Constructs a <code>NonActiveNonUSNoCataLogURLException</code> with the specified
       * message.
       *
       * @param msg the detail message
       */
  public NonActiveNonUSNoCataLogURLException( String msg )
  {
    super( msg );
  }

  /**
       * Constructs a <code>NonActiveNonUSNoCataLogURLException</code> with the specified
       * message and root cause.
       *
       * @param msg the detail message
       * @param t root cause
       */
  public NonActiveNonUSNoCataLogURLException( String msg, Throwable t )
  {
    super( msg, t );
  }
}

package com.biperf.core.service.security.impl;

import org.springframework.security.core.AuthenticationException;

public class NoPointsException extends AuthenticationException
{

  // ~ Constructors
  // ===================================================================================================

  /**
       * Constructs a <code>NotElgibleShopException</code> with the specified
       * message.
       *
       * @param msg the detail message
       */
  public NoPointsException( String msg )
  {
    super( msg );
  }

  /**
       * Constructs a <code>NotElgibleShopException</code> with the specified
       * message and root cause.
       *
       * @param msg the detail message
       * @param t root cause
       */
  public NoPointsException( String msg, Throwable t )
  {
    super( msg, t );
  }

}

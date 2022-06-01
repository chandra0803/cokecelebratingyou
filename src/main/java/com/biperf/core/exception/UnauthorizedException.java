
package com.biperf.core.exception;

public class UnauthorizedException extends RuntimeException
{

  private static final long serialVersionUID = 1L;

  public UnauthorizedException( String msg )
  {
    super( msg );
  }

  public UnauthorizedException( String msg, Throwable t )
  {
    super( msg, t );
  }

}

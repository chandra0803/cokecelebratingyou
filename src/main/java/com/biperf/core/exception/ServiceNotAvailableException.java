
package com.biperf.core.exception;

public class ServiceNotAvailableException extends RuntimeException
{

  private static final long serialVersionUID = 1L;

  public ServiceNotAvailableException( String msg )
  {
    super( msg );
  }

  public ServiceNotAvailableException( String msg, Throwable t )
  {
    super( msg, t );
  }

}

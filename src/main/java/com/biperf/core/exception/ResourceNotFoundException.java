
package com.biperf.core.exception;

public class ResourceNotFoundException extends RuntimeException
{

  private static final long serialVersionUID = 1L;

  public ResourceNotFoundException( String msg )
  {
    super( msg );
  }

  public ResourceNotFoundException( String msg, Throwable t )
  {
    super( msg, t );
  }

}

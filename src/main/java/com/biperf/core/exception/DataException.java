
package com.biperf.core.exception;

/**
 * This is the generaric wrapper for any data issues that occur when interacting
 * with the remote web service.  This exception means that "I was able to connect/talk
 * to the web service, but the data I passed in was either missing or invalid in some way".
 * 
 */
public class DataException extends Exception
{
  public DataException( String message )
  {
    super( message );
  }

  public DataException( String message, Throwable e )
  {
    super( message, e );
  }
}
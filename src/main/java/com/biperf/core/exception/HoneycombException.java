
package com.biperf.core.exception;

/**
 * Exception thrown by an error response from honeycomb services
 */
@SuppressWarnings( "serial" )
public class HoneycombException extends Exception
{

  private int responseCode;
  private String responseMessage;
  private String developerMessage;
  private Integer errorCode;

  public int getResponseCode()
  {
    return responseCode;
  }

  public void setResponseCode( int responseCode )
  {
    this.responseCode = responseCode;
  }

  public String getResponseMessage()
  {
    return responseMessage;
  }

  public void setResponseMessage( String responseMessage )
  {
    this.responseMessage = responseMessage;
  }

  public String getDeveloperMessage()
  {
    return developerMessage;
  }

  public void setDeveloperMessage( String developerMessage )
  {
    this.developerMessage = developerMessage;
  }

  public Integer getErrorCode()
  {
    return errorCode;
  }

  public void setErrorCode( Integer errorCode )
  {
    this.errorCode = errorCode;
  }

}

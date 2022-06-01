
package com.biperf.core.value.underArmour.v1;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties( ignoreUnknown = true )
public class BaseRestResponseObject extends BaseRestObject
{

  public static final int SUCCESS = 0;
  public static final int UNEXPECTED_FAILURE_ERROR_CODE = -1;
  public static final int SECURITY_FAILURE_ERROR_CODE = -2;
  public static final int VALIDATION_FAILURE_ERROR_CODE = -3;
  public static final int SERVICE_FAILURE_ERROR_CODE = -4;

  public static final String SUCCESS_MESSAGE = "SUCCESS";

  private int returnCode = 0;
  private String returnMessage;
  private String url;
  private String developerMessage;

  public BaseRestResponseObject()
  {

  }

  public BaseRestResponseObject( int returnCode, String returnMessage )
  {
    super();
    this.returnCode = returnCode;
    this.returnMessage = returnMessage;
  }

  public int getReturnCode()
  {
    return returnCode;
  }

  public void setReturnCode( int returnCode )
  {
    this.returnCode = returnCode;
  }

  public String getReturnMessage()
  {
    return returnMessage;
  }

  public void setReturnMessage( String returnMessage )
  {
    this.returnMessage = returnMessage;
  }

  public String getUrl()
  {
    return url;
  }

  public void setUrl( String url )
  {
    this.url = url;
  }

  public String getDeveloperMessage()
  {
    return developerMessage;
  }

  public void setDeveloperMessage( String developerMessage )
  {
    this.developerMessage = developerMessage;
  }

  public void buildDeveloperMessage( Throwable t )
  {
    StringBuilder sb = new StringBuilder();
    StackTraceElement[] stackElements = t.getStackTrace();
    for ( StackTraceElement element : stackElements )
    {
      sb.append( element.toString() + System.lineSeparator() );
    }
    this.developerMessage = sb.toString();
  }

}
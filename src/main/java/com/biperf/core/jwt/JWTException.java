
package com.biperf.core.jwt;

public class JWTException extends Exception
{

  String errorCode = null;

  public JWTException( String msg )
  {
    super( msg );
  }

  public JWTException( String errorCode, String msg )
  {
    super( msg );
    this.errorCode = errorCode;
  }

  public String getErrorCode()
  {
    return errorCode;
  }

}


package com.biperf.core.value.mtc.v1.upload;

public class ResponseError
{

  private String errorMessage;

  public ResponseError( String errorMessage )
  {
    this.errorMessage = errorMessage;
  }

  public String getErrorMessage()
  {
    return errorMessage;
  }

  public void setErrorMessage( String errorMessage )
  {
    this.errorMessage = errorMessage;
  }

}

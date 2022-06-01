
package com.biperf.core.exception;

import java.io.Serializable;
import java.util.List;

import com.biperf.core.utils.Environment;

public class ExceptionView implements Serializable
{

  private static final long serialVersionUID = 1L;

  private int responseCode;
  private String responseMessage;
  private String developerMessage;
  private List<FieldValidation> fieldErrors;
  private Integer errorCode;

  public ExceptionView()
  {
  }

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

  public List<FieldValidation> getFieldErrors()
  {
    return fieldErrors;
  }

  public void setFieldErrors( List<FieldValidation> fieldErrors )
  {
    this.fieldErrors = fieldErrors;
  }

  @Override
  public String toString()
  {
    return responseCode + " " + responseMessage + " " + developerMessage + " " + fieldErrors;
  }

  public Integer getErrorCode()
  {
    return errorCode;
  }

  public void setErrorCode( Integer errorCode )
  {
    this.errorCode = errorCode;
  }

  /**
   * @param responseCode
   * @param responseMessage
   * @param developerMessage
   * @param errorCode
   */
  public ExceptionView( int responseCode, String responseMessage, String developerMessage, Integer errorCode )
  {
    this.responseCode = responseCode;
    this.responseMessage = responseMessage;
    this.errorCode = errorCode;
    if ( !Environment.ENV_PROD.equals( Environment.getEnvironment() ) )
    {
      this.developerMessage = developerMessage;
    }
  }

  /**
   * @param responseCode
   * @param responseMessage
   * @param developerMessage
   */
  public ExceptionView( int responseCode, String responseMessage, String developerMessage )
  {
    this.responseCode = responseCode;
    this.responseMessage = responseMessage;
    if ( !Environment.ENV_PROD.equals( Environment.getEnvironment() ) )
    {
      this.developerMessage = developerMessage;
    }
  }

}

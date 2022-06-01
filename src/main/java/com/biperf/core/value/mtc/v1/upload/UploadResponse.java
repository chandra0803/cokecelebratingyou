
package com.biperf.core.value.mtc.v1.upload;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties( ignoreUnknown = true )
public class UploadResponse
{

  private String code;
  private String message;
  private List<ResponseError> errors;
  private String requestId;

  public String getCode()
  {
    return code;
  }

  public void setCode( String code )
  {
    this.code = code;
  }

  public String getMessage()
  {
    return message;
  }

  public void setMessage( String message )
  {
    this.message = message;
  }

  public List<ResponseError> getErrors()
  {
    return errors;
  }

  public void setErrors( List<ResponseError> errors )
  {
    this.errors = errors;
  }

  public String getRequestId()
  {
    return requestId;
  }

  public void setRequestId( String requestId )
  {
    this.requestId = requestId;
  }

  @Override
  public String toString()
  {
    return "UploadResponse [code=" + code + ", message=" + message + ", errors=" + errors + ", requestId=" + requestId + "]";
  }

}

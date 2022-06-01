
package com.biperf.core.value.client;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * TcccClaimFileUploadResponse.
 * 
 * This class is created as part of Client Customization for WIP #39189
 * 
 * @author dudam
 * @since Nov 16, 2017
 * @version 1.0
 */
public class TcccClaimFileUploadResponse
{
  @JsonProperty( "isSuccess" )
  private boolean isSuccess;
  private String message;
  private List<String> files;

  public boolean getIsSuccess()
  {
    return isSuccess;
  }

  public void setSuccess( boolean isSuccess )
  {
    this.isSuccess = isSuccess;
  }

  public String getMessage()
  {
    return message;
  }

  public void setMessage( String message )
  {
    this.message = message;
  }

  public List<String> getFiles()
  {
    return files;
  }

  public void setFiles( List<String> files )
  {
    this.files = files;
  }

}

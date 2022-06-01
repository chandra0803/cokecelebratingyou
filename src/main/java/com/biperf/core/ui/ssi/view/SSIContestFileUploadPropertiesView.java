
package com.biperf.core.ui.ssi.view;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 
 * SSIContestFileUploadPropertiesView.
 * 
 * @author chowdhur
 * @since Nov 12, 2014
 */
public class SSIContestFileUploadPropertiesView
{
  private boolean isSuccess;
  private String originalFilename;
  private String fileUrl;
  private String errorText;

  @JsonProperty( "isSuccess" )
  public boolean getIsSuccess()
  {
    return isSuccess;
  }

  public void setIsSuccess( boolean isSuccess )
  {
    this.isSuccess = isSuccess;
  }

  public String getOriginalFilename()
  {
    return originalFilename;
  }

  public void setOriginalFilename( String originalFilename )
  {
    this.originalFilename = originalFilename;
  }

  public String getFileUrl()
  {
    return fileUrl;
  }

  public void setFileUrl( String fileUrl )
  {
    this.fileUrl = fileUrl;
  }

  public String getErrorText()
  {
    return errorText;
  }

  public void setErrorText( String errorText )
  {
    this.errorText = errorText;
  }
}

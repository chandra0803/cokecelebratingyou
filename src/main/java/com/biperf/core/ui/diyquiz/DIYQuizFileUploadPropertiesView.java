
package com.biperf.core.ui.diyquiz;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 
 * DIYQuizFileUploadPropertiesView.
 * 
 * @author kandhi
 * @since Jul 18, 2013
 * @version 1.0
 */
public class DIYQuizFileUploadPropertiesView
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

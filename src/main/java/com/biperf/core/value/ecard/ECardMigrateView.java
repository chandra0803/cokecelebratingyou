
package com.biperf.core.value.ecard;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties( ignoreUnknown = true )
public class ECardMigrateView implements Serializable
{
  private static final long serialVersionUID = 1L;

  private String fileName;
  private String path;
  private String uploadedName;
  private String locale;

  public String getFileName()
  {
    return fileName;
  }

  public void setFileName( String fileName )
  {
    this.fileName = fileName;
  }

  public String getPath()
  {
    return path;
  }

  public void setPath( String path )
  {
    this.path = path;
  }

  public String getUploadedName()
  {
    return uploadedName;
  }

  public void setUploadedName( String uploadedName )
  {
    this.uploadedName = uploadedName;
  }

  public String getLocale()
  {
    return locale;
  }

  public void setLocale( String locale )
  {
    this.locale = locale;
  }

}

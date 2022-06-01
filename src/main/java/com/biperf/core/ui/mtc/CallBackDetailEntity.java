
package com.biperf.core.ui.mtc;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties( ignoreUnknown = true )
public class CallBackDetailEntity
{
  private String descriptor;
  private String url;
  private String expirationDate;

  public String getUrl()
  {
    return url;
  }

  public void setUrl( String url )
  {
    this.url = url;
  }

  @Override
  public String toString()
  {
    return "CallBackDetailEntity [descriptor=" + descriptor + ", url=" + url + ", expirationDate=" + expirationDate + "]";
  }

  public String getDescriptor()
  {
    return descriptor;
  }

  public void setDescriptor( String descriptor )
  {
    this.descriptor = descriptor;
  }

  public String getExpirationDate()
  {
    return expirationDate;
  }

  public void setExpirationDate( String expirationDate )
  {
    this.expirationDate = expirationDate;
  }

}

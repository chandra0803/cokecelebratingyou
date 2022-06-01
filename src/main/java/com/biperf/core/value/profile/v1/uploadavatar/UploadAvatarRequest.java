
package com.biperf.core.value.profile.v1.uploadavatar;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties( ignoreUnknown = true )
public class UploadAvatarRequest implements Serializable
{

  private static final long serialVersionUID = 1L;
  private String imageData;

  public String getImageData()
  {
    return imageData;
  }

  public void setImageData( String imageData )
  {
    this.imageData = imageData;
  }

  @Override
  public String toString()
  {
    return "UploadAvatarRequest [imageData=" + imageData + "]";
  }

}

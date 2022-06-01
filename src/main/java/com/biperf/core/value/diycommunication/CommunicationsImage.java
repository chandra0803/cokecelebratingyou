
package com.biperf.core.value.diycommunication;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CommunicationsImage
{
  private String id;
  private String imageSize;
  private String imageSizeMobile;
  private String imageSizeMax;
  @JsonProperty( "isSelected" )
  private boolean isSelected = false;

  public String getId()
  {
    return id;
  }

  public void setId( String id )
  {
    this.id = id;
  }

  public void setSelected( boolean isSelected )
  {
    this.isSelected = isSelected;
  }

  public boolean isSelected()
  {
    return isSelected;
  }

  public String getImageSize()
  {
    return imageSize;
  }

  public void setImageSize( String imageSize )
  {
    this.imageSize = imageSize;
  }

  public String getImageSizeMobile()
  {
    return imageSizeMobile;
  }

  public void setImageSizeMobile( String imageSizeMobile )
  {
    this.imageSizeMobile = imageSizeMobile;
  }

  public String getImageSizeMax()
  {
    return imageSizeMax;
  }

  public void setImageSizeMax( String imageSizeMax )
  {
    this.imageSizeMax = imageSizeMax;
  }
}

/*
 * (c) 2019 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.service.imageservice.v1;

import java.io.Serializable;

/**
 * TODO Javadoc for ImageUploadRequest.
 * 
 * @author sivanand
 * @since Jan 31, 2019
 * @version 1.0
 */
public class ImageUploadRequest implements Serializable
{
  private static final long serialVersionUID = 1L;
  private String image;
  private String name;
  private boolean isPublic = true;
  private Crop crop;

  public String getImage()
  {
    return image;
  }

  public void setImage( String image )
  {
    this.image = image;
  }

  public String getName()
  {
    return name;
  }

  public void setName( String name )
  {
    this.name = name;
  }

  public boolean isPublic()
  {
    return isPublic;
  }

  public void setPublic( boolean isPublic )
  {
    this.isPublic = isPublic;
  }

  public Crop getCrop()
  {
    return crop;
  }

  public void setCrop( Crop crop )
  {
    this.crop = crop;
  }
}

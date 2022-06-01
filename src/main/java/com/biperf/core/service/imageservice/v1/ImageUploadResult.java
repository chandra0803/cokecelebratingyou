/*
 * (c) 2019 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.service.imageservice.v1;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 
 * @author sivanand
 * @since Jan 31, 2019
 * @version 1.0
 */

@JsonIgnoreProperties( ignoreUnknown = true )
public class ImageUploadResult
{
  private ImageUrlResult imageUrlResult;
  private String name;

  public ImageUrlResult getImageUrlResult()
  {
    return imageUrlResult;
  }

  public void setImageUrlResult( ImageUrlResult imageUrlResult )
  {
    this.imageUrlResult = imageUrlResult;
  }

  public String getName()
  {
    return name;
  }

  public void setName( String name )
  {
    this.name = name;
  }

}

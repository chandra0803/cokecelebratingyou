/*
 * (c) 2019 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.value;

import java.io.Serializable;

/**
 * 
 * @author sivanand
 * @since Jan 31, 2019
 * @version 1.0
 */

public class PaxContributorImageInfo implements Serializable
{
  private Long purlContributorCommentId;
  private String imageUrl;
  private String imageUrlThumb;

  public Long getPurlContributorCommentId()
  {
    return purlContributorCommentId;
  }

  public void setPurlContributorCommentId( Long purlContributorCommentId )
  {
    this.purlContributorCommentId = purlContributorCommentId;
  }

  public String getImageUrl()
  {
    return imageUrl;
  }

  public void setImageUrl( String imageUrl )
  {
    this.imageUrl = imageUrl;
  }

  public String getImageUrlThumb()
  {
    return imageUrlThumb;
  }

  public void setImageUrlThumb( String imageUrlThumb )
  {
    this.imageUrlThumb = imageUrlThumb;
  }

}

/*
 * (c) 2019 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.value.participant;

/**
 * 
 * @author sivanand
 * @since Jan 31, 2019
 * @version 1.0
 */
public class MigrateContributorImageInfo
{

  private String purlContributorCommentId;
  private String imgPathBase64Value;
  private String imageUrlThumbBase64Value;
  private String contentType;

  public String getImgPathBase64Value()
  {
    return imgPathBase64Value;
  }

  public void setImgPathBase64Value( String imgPathBase64Value )
  {
    this.imgPathBase64Value = imgPathBase64Value;
  }

  public String getImageUrlThumbBase64Value()
  {
    return imageUrlThumbBase64Value;
  }

  public void setImageUrlThumbBase64Value( String imageUrlThumbBase64Value )
  {
    this.imageUrlThumbBase64Value = imageUrlThumbBase64Value;
  }

  public String getPurlContributorCommentId()
  {
    return purlContributorCommentId;
  }

  public void setPurlContributorCommentId( String purlContributorCommentId )
  {
    this.purlContributorCommentId = purlContributorCommentId;
  }

  public String getContentType()
  {
    return contentType;
  }

  public void setContentType( String contentType )
  {
    this.contentType = contentType;
  }

}

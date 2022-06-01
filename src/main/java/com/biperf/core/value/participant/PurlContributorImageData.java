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
public class PurlContributorImageData
{
  private Long purlContributorCommentId;
  private String imageUrl;
  private String imageUrlThumb;

  private String onPremHostBaseUrl;
  private String rosterId;
  private String meshBaseUrl;
  private String companyIdentifier;
  private String meshClientId;
  private String meshSecretKey;

  private boolean aws;
  private String awsHostBaseUrl;
  private String awsS3BucketName;

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

  public String getOnPremHostBaseUrl()
  {
    return onPremHostBaseUrl;
  }

  public void setOnPremHostBaseUrl( String onPremHostBaseUrl )
  {
    this.onPremHostBaseUrl = onPremHostBaseUrl;
  }

  public String getRosterId()
  {
    return rosterId;
  }

  public void setRosterId( String rosterId )
  {
    this.rosterId = rosterId;
  }

  public String getMeshBaseUrl()
  {
    return meshBaseUrl;
  }

  public void setMeshBaseUrl( String meshBaseUrl )
  {
    this.meshBaseUrl = meshBaseUrl;
  }

  public String getCompanyIdentifier()
  {
    return companyIdentifier;
  }

  public void setCompanyIdentifier( String companyIdentifier )
  {
    this.companyIdentifier = companyIdentifier;
  }

  public String getMeshClientId()
  {
    return meshClientId;
  }

  public void setMeshClientId( String meshClientId )
  {
    this.meshClientId = meshClientId;
  }

  public String getMeshSecretKey()
  {
    return meshSecretKey;
  }

  public void setMeshSecretKey( String meshSecretKey )
  {
    this.meshSecretKey = meshSecretKey;
  }

  public boolean isAws()
  {
    return aws;
  }

  public void setAws( boolean aws )
  {
    this.aws = aws;
  }

  public String getAwsHostBaseUrl()
  {
    return awsHostBaseUrl;
  }

  public void setAwsHostBaseUrl( String awsHostBaseUrl )
  {
    this.awsHostBaseUrl = awsHostBaseUrl;
  }

  public String getAwsS3BucketName()
  {
    return awsS3BucketName;
  }

  public void setAwsS3BucketName( String awsS3BucketName )
  {
    this.awsS3BucketName = awsS3BucketName;
  }

}

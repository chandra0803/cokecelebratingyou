/*
 * (c) 2019 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.value.participant;

import java.util.UUID;

public class PaxAvatarData
{
  private Long userId;
  private String avatarOriginal;
  private String avatarSmall;
  private String onPremHostBaseUrl;
  private UUID rosterUserId;
  private String meshBaseUrl;
  private String companyIdentifier;
  private String meshClientId;
  private String meshSecretKey;
  private boolean aws;
  private String awsHostBaseUrl;
  private String awsS3BucketName;

  public Long getUserId()
  {
    return userId;
  }

  public void setUserId( Long userId )
  {
    this.userId = userId;
  }

  public String getAvatarOriginal()
  {
    return avatarOriginal;
  }

  public void setAvatarOriginal( String avatarOriginal )
  {
    this.avatarOriginal = avatarOriginal;
  }

  public String getAvatarSmall()
  {
    return avatarSmall;
  }

  public void setAvatarSmall( String avatarSmall )
  {
    this.avatarSmall = avatarSmall;
  }

  public String getOnPremHostBaseUrl()
  {
    return onPremHostBaseUrl;
  }

  public void setOnPremHostBaseUrl( String onPremHostBaseUrl )
  {
    this.onPremHostBaseUrl = onPremHostBaseUrl;
  }

  public UUID getRosterUserId()
  {
    return rosterUserId;
  }

  public void setRosterUserId( UUID rosterUserId )
  {
    this.rosterUserId = rosterUserId;
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

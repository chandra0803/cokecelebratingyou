
package com.biperf.core.value.ecard;

public class OwnCardImageData
{

  private Long id;
  private String ownCardName;
  private String onPremHostBaseUrl;
  private String meshBaseUrl;
  private String companyIdentifier;
  private String meshClientId;
  private String meshSecretKey;
  private boolean aws;
  private String awsHostBaseUrl;
  private String awsS3BucketName;

  public OwnCardImageData()
  {
  }

  public OwnCardImageData( Long id, String ownCardName )
  {
    this.id = id;
    this.ownCardName = ownCardName;
  }

  public Long getId()
  {
    return id;
  }

  public void setId( Long id )
  {
    this.id = id;
  }

  public String getOwnCardName()
  {
    return ownCardName;
  }

  public void setOwnCardName( String ownCardName )
  {
    this.ownCardName = ownCardName;
  }

  public String getOnPremHostBaseUrl()
  {
    return onPremHostBaseUrl;
  }

  public void setOnPremHostBaseUrl( String onPremHostBaseUrl )
  {
    this.onPremHostBaseUrl = onPremHostBaseUrl;
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

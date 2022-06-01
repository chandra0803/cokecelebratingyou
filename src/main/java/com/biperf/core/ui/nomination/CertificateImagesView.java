
package com.biperf.core.ui.nomination;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CertificateImagesView
{
  @JsonProperty( "eCards" )
  private List<CertificateImage> certificates = new ArrayList<>();

  public List<CertificateImage> getCertificates()
  {
    return certificates;
  }

  public void setCertificates( List<CertificateImage> certificates )
  {
    this.certificates = certificates;
  }

  public static class CertificateImage
  {
    @JsonProperty( "id" )
    private Long certificateId;

    @JsonProperty( "largeImage" )
    private String largeImage;

    public Long getCertificateId()
    {
      return certificateId;
    }

    public void setCertificateId( Long certificateId )
    {
      this.certificateId = certificateId;
    }

    public String getLargeImage()
    {
      return largeImage;
    }

    public void setLargeImage( String largeImage )
    {
      this.largeImage = largeImage;
    }
  }

}

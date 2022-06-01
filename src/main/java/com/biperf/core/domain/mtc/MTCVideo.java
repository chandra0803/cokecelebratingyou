
package com.biperf.core.domain.mtc;

import java.util.Date;

import com.biperf.core.domain.BaseDomain;

public class MTCVideo extends BaseDomain implements Cloneable
{
  @Override
  public String toString()
  {
    return "MTCVideo [requestId=" + requestId + ", mp4Url=" + mp4Url + ", webmUrl=" + webmUrl + ", thumbNailImageUrl=" + thumbNailImageUrl + ", originalFormat=" + originalFormat + ", expiryDate="
        + expiryDate + "]";
  }

  private String requestId;
  private String mp4Url;
  private String webmUrl;
  private String thumbNailImageUrl;
  private String originalFormat;
  private Date expiryDate;

  public String getRequestId()
  {
    return requestId;
  }

  public void setRequestId( String requestId )
  {
    this.requestId = requestId;
  }

  public String getMp4Url()
  {
    return mp4Url;
  }

  public void setMp4Url( String mp4Url )
  {
    this.mp4Url = mp4Url;
  }

  public String getWebmUrl()
  {
    return webmUrl;
  }

  public void setWebmUrl( String webmUrl )
  {
    this.webmUrl = webmUrl;
  }

  public String getThumbNailImageUrl()
  {
    return thumbNailImageUrl;
  }

  public void setThumbNailImageUrl( String thumbNailImageUrl )
  {
    this.thumbNailImageUrl = thumbNailImageUrl;
  }

  @Override
  public boolean equals( Object object )
  {
    if ( this == object )
    {
      return true;
    }
    if ( object == null )
    {
      return false;
    }

    MTCVideo mtcVideo = (MTCVideo)object;
    if ( requestId == null || mtcVideo.getRequestId() == null )
    {
      return false;
    }

    if ( requestId.equals( mtcVideo.getRequestId() ) )
    {
      return true;
    }

    return false;
  }

  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result + ( requestId == null ? 0 : requestId.hashCode() );
    return result;
  }

  public String getOriginalFormat()
  {
    return originalFormat;
  }

  public void setOriginalFormat( String originalFormat )
  {
    this.originalFormat = originalFormat;
  }

  public Date getExpiryDate()
  {
    return expiryDate;
  }

  public void setExpiryDate( Date expiryDate )
  {
    this.expiryDate = expiryDate;
  }

}

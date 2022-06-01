
package com.biperf.core.domain.multimedia;

import com.biperf.core.domain.BaseDomain;

/**
 * Used to keep information on video uploads.
 * Not used as the permanent location of the video. Instead, see Claim's video_url fields
 * 
 * @author corneliu
 * @since Sep 7, 2016
 */
public class VideoUploadDetail extends BaseDomain
{
  private static final long serialVersionUID = 8654734120349743042L;

  private String videoUrl;
  private String thumbnailUrl;

  @Override
  public String toString()
  {
    StringBuilder sb = new StringBuilder();
    sb.append( "VideoUploadDetail [" );
    sb.append( "{id=" );
    sb.append( super.getId() );
    sb.append( "}, videoUrl=" );
    sb.append( getVideoUrl() );
    sb.append( "}, thumnailUrl=" );
    sb.append( getThumbnailUrl() );
    sb.append( "}]" );
    return sb.toString();
  }

  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result + ( thumbnailUrl == null ? 0 : thumbnailUrl.hashCode() );
    result = prime * result + ( videoUrl == null ? 0 : videoUrl.hashCode() );
    return result;
  }

  @Override
  public boolean equals( Object obj )
  {
    if ( this == obj )
    {
      return true;
    }
    if ( obj == null )
    {
      return false;
    }
    if ( getClass() != obj.getClass() )
    {
      return false;
    }
    VideoUploadDetail other = (VideoUploadDetail)obj;
    if ( thumbnailUrl == null )
    {
      if ( other.thumbnailUrl != null )
      {
        return false;
      }
    }
    else if ( !thumbnailUrl.equals( other.thumbnailUrl ) )
    {
      return false;
    }
    if ( videoUrl == null )
    {
      if ( other.videoUrl != null )
      {
        return false;
      }
    }
    else if ( !videoUrl.equals( other.videoUrl ) )
    {
      return false;
    }
    return true;
  }

  public String getVideoUrl()
  {
    return videoUrl;
  }

  public void setVideoUrl( String videoUrl )
  {
    this.videoUrl = videoUrl;
  }

  public String getThumbnailUrl()
  {
    return thumbnailUrl;
  }

  public void setThumbnailUrl( String thumbnailUrl )
  {
    this.thumbnailUrl = thumbnailUrl;
  }

}

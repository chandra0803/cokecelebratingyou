
package com.biperf.core.domain.purl;

import com.biperf.core.domain.BaseDomain;
import com.biperf.core.domain.enums.LanguageType;
import com.biperf.core.domain.enums.PurlContributorCommentStatus;
import com.biperf.core.domain.enums.PurlContributorMediaStatus;
import com.biperf.core.domain.enums.PurlContributorVideoType;
import com.biperf.core.domain.enums.PurlMediaState;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.utils.ImageUtils;

public class PurlContributorComment extends BaseDomain
{
  private PurlContributor purlContributor;
  private String comments;
  private LanguageType commentsLanguageType;
  private PurlContributorCommentStatus status;
  private PurlMediaState mediaState;
  private String imageUrl;
  private String imageUrlThumb;
  private String videoUrl;
  private String videoUrlThumb;
  private String videoUrlExtension; // This is so it can be stored w/o extension for main website
                                    // and given extension for mobile...
  private PurlContributorMediaStatus imageStatus;
  private PurlContributorMediaStatus videoStatus;
  private PurlContributorVideoType videoType;

  public String getComments()
  {
    return comments;
  }

  @Override
  public String toString()
  {
    return "PurlContributorComment [purlContributor=" + purlContributor + ", comments=" + comments + ", commentsLanguageType=" + commentsLanguageType + ", status=" + status + ", mediaState="
        + mediaState + ", imageUrl=" + imageUrl + ", imageUrlThumb=" + imageUrlThumb + ", videoUrl=" + videoUrl + ", videoUrlThumb=" + videoUrlThumb + ", videoUrlExtension=" + videoUrlExtension
        + ", imageStatus=" + imageStatus + ", videoStatus=" + videoStatus + ", videoType=" + videoType + "]";
  }

  public void setComments( String comments )
  {
    this.comments = comments;
  }

  public LanguageType getCommentsLanguageType()
  {
    return commentsLanguageType;
  }

  public void setCommentsLanguageType( LanguageType commentsLanguageType )
  {
    this.commentsLanguageType = commentsLanguageType;
  }

  public PurlContributor getPurlContributor()
  {
    return purlContributor;
  }

  public void setPurlContributor( PurlContributor purlContributor )
  {
    this.purlContributor = purlContributor;
  }

  public PurlContributorCommentStatus getStatus()
  {
    return status;
  }

  public void setStatus( PurlContributorCommentStatus status )
  {
    this.status = status;
  }

  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result + ( comments == null ? 0 : comments.hashCode() );
    result = prime * result + ( status == null ? 0 : status.hashCode() );
    result = prime * result + ( purlContributor == null ? 0 : purlContributor.hashCode() );
    result = prime * result + ( mediaState == null ? 0 : mediaState.hashCode() );
    result = prime * result + ( imageUrl == null ? 0 : imageUrl.hashCode() );
    result = prime * result + ( imageUrlThumb == null ? 0 : imageUrlThumb.hashCode() );
    result = prime * result + ( videoUrl == null ? 0 : videoUrl.hashCode() );
    result = prime * result + ( imageStatus == null ? 0 : imageStatus.hashCode() );
    result = prime * result + ( videoStatus == null ? 0 : videoStatus.hashCode() );
    result = prime * result + ( videoType == null ? 0 : videoType.hashCode() );
    return result;
  }

  @Override
  public boolean equals( Object obj )
  {
    if ( this == obj )
    {
      return true;
    }
    if ( ! ( obj instanceof PurlContributorComment ) )
    {
      return false;
    }
    if ( getClass() != obj.getClass() )
    {
      return false;
    }
    PurlContributorComment other = (PurlContributorComment)obj;
    if ( comments == null )
    {
      if ( other.comments != null )
      {
        return false;
      }
    }
    else if ( !comments.equals( other.comments ) )
    {
      return false;
    }
    if ( status == null )
    {
      if ( other.status != null )
      {
        return false;
      }
    }
    else if ( !status.equals( other.status ) )
    {
      return false;
    }
    if ( purlContributor == null )
    {
      if ( other.purlContributor != null )
      {
        return false;
      }
    }
    else if ( !purlContributor.equals( other.purlContributor ) )
    {
      return false;
    }
    if ( mediaState == null )
    {
      if ( other.mediaState != null )
      {
        return false;
      }
    }
    else if ( !mediaState.equals( other.mediaState ) )
    {
      return false;
    }
    if ( imageUrl == null )
    {
      if ( other.imageUrl != null )
      {
        return false;
      }
    }
    else if ( !imageUrl.equals( other.imageUrl ) )
    {
      return false;
    }
    if ( imageUrlThumb == null )
    {
      if ( other.imageUrlThumb != null )
      {
        return false;
      }
    }
    else if ( !imageUrlThumb.equals( other.imageUrlThumb ) )
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
    if ( imageStatus == null )
    {
      if ( other.imageStatus != null )
      {
        return false;
      }
    }
    else if ( !imageStatus.equals( other.imageStatus ) )
    {
      return false;
    }
    if ( videoStatus == null )
    {
      if ( other.videoStatus != null )
      {
        return false;
      }
    }
    else if ( !videoStatus.equals( other.videoStatus ) )
    {
      return false;
    }
    if ( videoType == null )
    {
      if ( other.videoType != null )
      {
        return false;
      }
    }
    else if ( !videoType.equals( other.videoType ) )
    {
      return false;
    }
    return true;
  }

  public void setMediaState( PurlMediaState mediaState )
  {
    this.mediaState = mediaState;
  }

  public PurlMediaState getMediaState()
  {
    return mediaState;
  }

  public void setImageUrl( String imageUrl )
  {
    this.imageUrl = imageUrl;
  }

  public String getImageUrl()
  {
    return imageUrl;
  }

  public void setImageUrlThumb( String imageUrlThumb )
  {
    this.imageUrlThumb = imageUrlThumb;
  }

  public String getImageUrlThumb()
  {
    return imageUrlThumb;
  }

  public void setVideoUrl( String videoUrl )
  {
    this.videoUrl = videoUrl;
  }

  public String getVideoUrl()
  {
    return videoUrl;
  }

  public String getVideoUrlThumb()
  {
    return videoUrlThumb;
  }

  public void setVideoUrlThumb( String videoUrlThumb )
  {
    this.videoUrlThumb = videoUrlThumb;
  }

  public String getVideoUrlExtension()
  {
    return videoUrlExtension;
  }

  public void setVideoUrlExtension( String videoUrlExtension )
  {
    this.videoUrlExtension = videoUrlExtension;
  }

  public void setImageStatus( PurlContributorMediaStatus imageStatus )
  {
    this.imageStatus = imageStatus;
  }

  public PurlContributorMediaStatus getImageStatus()
  {
    return imageStatus;
  }

  public void setVideoStatus( PurlContributorMediaStatus videoStatus )
  {
    this.videoStatus = videoStatus;
  }

  public PurlContributorMediaStatus getVideoStatus()
  {
    return videoStatus;
  }

  public void setVideoType( PurlContributorVideoType videoType )
  {
    this.videoType = videoType;
  }

  public PurlContributorVideoType getVideoType()
  {
    return videoType;
  }

  public String getDisplayImageUrlThumb()
  {
    return ImageUtils.getFullImageUrlPath( this.imageUrl );
  }

  public String getRequestId( String videoUrl )
  {

    return videoUrl.substring( videoUrl.lastIndexOf( ":" ) + 1 );
  }

  public String getActualCardUrl( String path )
  {
    return path.substring( 0, path.lastIndexOf( ActionConstants.REQUEST_ID ) );
  }
}

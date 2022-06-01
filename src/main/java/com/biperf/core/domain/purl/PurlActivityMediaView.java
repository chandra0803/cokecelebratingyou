
package com.biperf.core.domain.purl;

import java.util.ArrayList;
import java.util.List;

import com.biperf.core.utils.ImageUtils;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * 
 * @author drahn
 * @since Jan 12, 2013
 * @version 1.0
 */
@JsonInclude( value = Include.NON_NULL )
public class PurlActivityMediaView
{
  private List<PurlActivityVideo> video = new ArrayList<PurlActivityVideo>();
  private List<PurlActivityPhoto> photo = new ArrayList<PurlActivityPhoto>();

  public PurlActivityMediaView()
  {
  }

  public PurlActivityMediaView( String imageUrl )
  {
    photo.add( new PurlActivityPhoto( imageUrl ) );
  }

  public PurlActivityMediaView( String fileType, String videoUrl )
  {
    video.add( new PurlActivityVideo( fileType, videoUrl ) );
  }

  public List<PurlActivityVideo> getVideo()
  {
    return video;
  }

  public void setVideo( List<PurlActivityVideo> video )
  {
    this.video = video;
  }

  public List<PurlActivityPhoto> getPhoto()
  {
    return photo;
  }

  public void setPhoto( List<PurlActivityPhoto> photo )
  {
    this.photo = photo;
  }

  public static class PurlActivityVideo
  {
    private String fileType;
    private String src;

    public PurlActivityVideo( String fileType, String src )
    {
      this.fileType = fileType;
      this.src = src;
    }

    public String getFileType()
    {
      return fileType;
    }

    public void setFileType( String fileType )
    {
      this.fileType = fileType;
    }

    public String getSrc()
    {
      return src;
    }

    public void setSrc( String src )
    {
      this.src = src;
    }
  }

  public static class PurlActivityPhoto
  {
    private String src;

    public PurlActivityPhoto( String src )
    {
      String imageUrl = "";
      if ( src != null )
      {
        if ( src.indexOf( "cm3dam" ) > 0 )
        {
          imageUrl = src;
        }
        else
        {
          imageUrl = ImageUtils.getFullImageUrlPath( src );
        }
      }
      else
      {
        imageUrl = ImageUtils.getFullImageUrlPath( src );
      }

      this.src = imageUrl;
    }

    public String getSrc()
    {
      return src;
    }

    public void setSrc( String src )
    {
      this.src = src;
    }
  }
}

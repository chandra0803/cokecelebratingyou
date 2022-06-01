
package com.biperf.core.ui.purl;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class ContributionState implements Serializable
{
  private Long contributorId;
  private int photoUploadLimit;
  private Avatar profileImage;
  private Map<String, Photo> photos;

  public ContributionState( Long contributorId, int photoUploadLimit )
  {
    this.contributorId = contributorId;
    this.photoUploadLimit = photoUploadLimit;
  }

  public Long getContributorId()
  {
    return contributorId;
  }

  public void addAvatar( String url )
  {
    profileImage = new Avatar();
    profileImage.setUrl( url );
  }

  public String getAvatarUrl()
  {
    if ( null != profileImage )
    {
      return profileImage.getUrl();
    }
    return null;
  }

  public boolean isAvatarUploaded()
  {
    if ( null != profileImage && null != profileImage.getUrl() )
    {
      return profileImage.isStatus();
    }
    return false;
  }

  public void markAvatarUploaded()
  {
    if ( null != profileImage && null != profileImage.getUrl() )
    {
      profileImage.setStatus( true );
    }
  }

  public void removeAvatar()
  {
    profileImage = null;
  }

  public boolean isPhotoUploadMaxLimitReached()
  {
    if ( null == photos )
    {
      return false;
    }
    return photos.size() >= photoUploadLimit;
  }

  public void addPhoto( String id, String detailUrl, String thumbUrl )
  {
    if ( null == photos )
    {
      photos = new HashMap<String, Photo>();
    }

    // Check the limit of photos that can be uploaded
    if ( isPhotoUploadMaxLimitReached() )
    {
      throw new IllegalStateException( "Upload limit reached" );
    }

    Photo photo = new Photo();
    photo.setId( id );
    photo.setDetailUrl( detailUrl );
    photo.setThumbUrl( thumbUrl );

    photos.put( id, photo );
  }

  /**
   * I have no idea if this will work. I'm just going to treat it like a photo, I guess.
   */
  public void addVideo( String id, String videoUrl, String thumbUrl )
  {
    if ( null == photos )
    {
      photos = new HashMap<String, Photo>();
    }

    // Check the limit of photos that can be uploaded
    if ( isPhotoUploadMaxLimitReached() )
    {
      throw new IllegalStateException( "Upload limit reached" );
    }

    Photo photo = new Photo();
    photo.setId( id );
    photo.setDetailUrl( videoUrl );
    photo.setThumbUrl( thumbUrl );

    photos.put( id, photo );
  }

  public String getDetailPhotoUrl( String id )
  {
    if ( null != photos )
    {
      Photo photo = photos.get( id );
      if ( null != photo )
      {
        return photo.getDetailUrl();
      }
    }
    return null;
  }

  public String getThumbPhotoUrl( String id )
  {
    if ( null != photos )
    {
      Photo photo = photos.get( id );
      if ( null != photo )
      {
        return photo.getThumbUrl();
      }
    }
    return null;
  }

  public void removePhoto( String id )
  {
    if ( null == photos )
    {
      return;
    }
    photos.remove( id );
  }

  class Avatar implements Serializable
  {
    private String url;
    private boolean status;

    public String getUrl()
    {
      return url;
    }

    public void setUrl( String url )
    {
      this.url = url;
    }

    public boolean isStatus()
    {
      return status;
    }

    public void setStatus( boolean status )
    {
      this.status = status;
    }

  }

  class Photo implements Serializable
  {
    private String id;
    private String detailUrl;
    private String thumbUrl;

    public String getId()
    {
      return id;
    }

    public void setId( String id )
    {
      this.id = id;
    }

    public String getDetailUrl()
    {
      return detailUrl;
    }

    public void setDetailUrl( String detailUrl )
    {
      this.detailUrl = detailUrl;
    }

    public String getThumbUrl()
    {
      return thumbUrl;
    }

    public void setThumbUrl( String thumbUrl )
    {
      this.thumbUrl = thumbUrl;
    }
  }

}

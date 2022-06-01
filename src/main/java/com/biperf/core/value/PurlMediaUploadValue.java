
package com.biperf.core.value;

import java.io.Serializable;

public class PurlMediaUploadValue implements Serializable
{
  public static final String STATUS_SUCCESS = "success";
  public static final String STATUS_FAIL = "fail";

  private String id;
  private String media;
  private String full;
  private String thumb;
  private String caption;
  private String status;

  public String getId()
  {
    return id;
  }

  public void setId( String id )
  {
    this.id = id;
  }

  public String getMedia()
  {
    return media;
  }

  public void setMedia( String media )
  {
    this.media = media;
  }

  public String getFull()
  {
    return full;
  }

  public void setFull( String full )
  {
    this.full = full;
  }

  public String getThumb()
  {
    return thumb;
  }

  public void setThumb( String thumb )
  {
    this.thumb = thumb;
  }

  public String getCaption()
  {
    return caption;
  }

  public void setCaption( String caption )
  {
    this.caption = caption;
  }

  public String getStatus()
  {
    return status;
  }

  public void setStatus( String status )
  {
    this.status = status;
  }

}

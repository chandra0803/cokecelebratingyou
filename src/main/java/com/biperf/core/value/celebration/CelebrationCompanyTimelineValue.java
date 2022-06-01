
package com.biperf.core.value.celebration;

import java.io.Serializable;

public class CelebrationCompanyTimelineValue implements Serializable
{
  private String url;
  private String timelinePhoto;

  public String getUrl()
  {
    return url;
  }

  public void setUrl( String url )
  {
    this.url = url;
  }

  public String getTimelinePhoto()
  {
    return timelinePhoto;
  }

  public void setTimelinePhoto( String timelinePhoto )
  {
    this.timelinePhoto = timelinePhoto;
  }
}


package com.biperf.core.ui.engagement;

/**
 * 
 * EngagementSiteVisitsLoginView.
 * 
 * @author kandhi
 * @since May 20, 2014
 * @version 1.0
 */
public class EngagementSiteVisitsLoginView
{
  private String date;
  private String time;
  private String timeZoneId;
  private String localeTime;

  public EngagementSiteVisitsLoginView( String date, String time, String timeZoneId, String localeTime )
  {
    super();
    this.date = date;
    this.time = time;
    this.timeZoneId = timeZoneId;
    this.localeTime = localeTime;
  }

  public String getDate()
  {
    return date;
  }

  public void setDate( String date )
  {
    this.date = date;
  }

  public String getTime()
  {
    return time;
  }

  public void setTime( String time )
  {
    this.time = time;
  }

  // Fix for 59693
  public String getTimeZoneId()
  {
    return timeZoneId;
  }

  public void setTimeZoneId( String timeZoneId )
  {
    this.timeZoneId = timeZoneId;
  }

  public String getLocaleTime()
  {
    return localeTime;
  }

  public void setLocaleTime( String localeTime )
  {
    this.localeTime = localeTime;
  }

}

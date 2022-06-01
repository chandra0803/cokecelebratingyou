
package com.biperf.core.value;

/**
 * 
 * EngagementSiteVisitsLoginValueBean.
 * 
 * @author kandhi
 * @since Jul 24, 2014
 * @version 1.0
 */
public class EngagementSiteVisitsLoginValueBean
{
  private String date;
  private String time;
  private String timeZoneId;
  private String localeTime;

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

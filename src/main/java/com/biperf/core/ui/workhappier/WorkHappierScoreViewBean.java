/*
 * (c) 2015 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.ui.workhappier;

/**
 * 
 * @author poddutur
 * @since Nov 25, 2015
 */
public class WorkHappierScoreViewBean
{
  private String imgUrl;
  private String Date;
  private String mood;

  public String getImgUrl()
  {
    return imgUrl;
  }

  public void setImgUrl( String imgUrl )
  {
    this.imgUrl = imgUrl;
  }

  public String getDate()
  {
    return Date;
  }

  public void setDate( String date )
  {
    Date = date;
  }

  public String getMood()
  {
    return mood;
  }

  public void setMood( String mood )
  {
    this.mood = mood;
  }

}

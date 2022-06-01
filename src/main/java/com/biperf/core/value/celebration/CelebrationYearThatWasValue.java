
package com.biperf.core.value.celebration;

import java.io.Serializable;

public class CelebrationYearThatWasValue implements Serializable
{
  private String img;
  private String countryCode;
  private int year;
  private String content;

  public String getImg()
  {
    return img;
  }

  public void setImg( String img )
  {
    this.img = img;
  }

  public String getCountryCode()
  {
    return countryCode;
  }

  public void setCountryCode( String countryCode )
  {
    this.countryCode = countryCode;
  }

  public int getYear()
  {
    return year;
  }

  public void setYear( int year )
  {
    this.year = year;
  }

  public String getContent()
  {
    return content;
  }

  public void setContent( String content )
  {
    this.content = content;
  }

}

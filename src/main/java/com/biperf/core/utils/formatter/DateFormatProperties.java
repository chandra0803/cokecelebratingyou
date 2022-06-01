
package com.biperf.core.utils.formatter;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.Locale;
import java.util.TimeZone;

@SuppressWarnings( "serial" )
public class DateFormatProperties implements Serializable
{
  private static final String DEFAULT = "default";
  private static final String SHORT = "short";
  private static final String MEDIUM = "medium";
  private static final String LONG = "long";
  private static final String FULL = "full";

  private Integer dateStyle;
  private Integer timeStyle;
  private Locale locale;
  private TimeZone timeZone;
  private String pattern;

  public String getPattern()
  {
    return pattern;
  }

  public void setPattern( String pattern )
  {
    if ( !isEmpty( pattern ) )
    {
      this.pattern = pattern;
    }
  }

  public Integer getDateStyle()
  {
    return null == dateStyle && null == timeStyle ? new Integer( DateFormat.DEFAULT ) : dateStyle;
  }

  public void setDateStyle( String dateStyle )
  {
    if ( !isEmpty( dateStyle ) )
    {
      this.dateStyle = new Integer( getStyle( dateStyle ) );
    }
  }

  public Integer getTimeStyle()
  {
    return null == dateStyle && null == timeStyle ? new Integer( DateFormat.DEFAULT ) : timeStyle;
  }

  public void setTimeStyle( String timeStyle )
  {
    if ( !isEmpty( timeStyle ) )
    {
      this.timeStyle = new Integer( getStyle( timeStyle ) );
    }
  }

  public Locale getLocale()
  {
    return locale;
  }

  public void setLocale( Locale locale )
  {
    this.locale = locale;
  }

  public TimeZone getTimeZone()
  {
    return timeZone;
  }

  public void setTimeZone( String timeZone )
  {
    if ( !isEmpty( timeZone ) )
    {
      this.timeZone = TimeZone.getTimeZone( timeZone );
    }
  }

  private int getStyle( String style )
  {
    int ret = DateFormat.DEFAULT;

    if ( style != null )
    {
      if ( DEFAULT.equalsIgnoreCase( style ) )
      {
        ret = DateFormat.DEFAULT;
      }
      else if ( SHORT.equalsIgnoreCase( style ) )
      {
        ret = DateFormat.SHORT;
      }
      else if ( MEDIUM.equalsIgnoreCase( style ) )
      {
        ret = DateFormat.MEDIUM;
      }
      else if ( LONG.equalsIgnoreCase( style ) )
      {
        ret = DateFormat.LONG;
      }
      else if ( FULL.equalsIgnoreCase( style ) )
      {
        ret = DateFormat.FULL;
      }
    }

    return ret;
  }

  private static boolean isEmpty( String str )
  {
    return str == null || str.length() == 0;
  }

}


package com.biperf.core.utils.formatter;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@SuppressWarnings( "serial" )
public class DateFormatter implements Serializable
{
  private DateFormatProperties props;
  private DateFormat formatter;

  public DateFormatter( DateFormatProperties props )
  {
    this.props = props;
    formatter = createFormatter();
  }

  protected DateFormat getFormatter()
  {
    return formatter;
  }

  public String format( Date value )
  {
    return formatter.format( value );
  }

  protected DateFormat createFormatter()
  {
    DateFormat formatter = new SimpleDateFormat( "dd/MM/yyyy" );
    if ( null != props.getPattern() )
    {
      formatter = new SimpleDateFormat( props.getPattern(), props.getLocale() );
    }
    else
    {
      if ( null == props.getDateStyle() && null != props.getTimeStyle() )
      {
        if ( null == props.getLocale() )
        {
          formatter = DateFormat.getTimeInstance( props.getTimeStyle() );
        }
        else
        {
          formatter = DateFormat.getTimeInstance( props.getTimeStyle(), props.getLocale() );
        }
      }
      else if ( null != props.getDateStyle() && null == props.getTimeStyle() )
      {
        if ( null == props.getLocale() )
        {
          formatter = DateFormat.getDateInstance( props.getDateStyle() );
        }
        else
        {
          formatter = DateFormat.getDateInstance( props.getDateStyle(), props.getLocale() );
        }
      }
      else if ( null != props.getDateStyle() && null != props.getTimeStyle() )
      {
        if ( null == props.getLocale() )
        {
          formatter = DateFormat.getDateTimeInstance( props.getDateStyle(), props.getTimeStyle() );
        }
        else
        {
          formatter = DateFormat.getDateTimeInstance( props.getDateStyle(), props.getTimeStyle(), props.getLocale() );
        }
      }
    }

    if ( null != props.getTimeZone() )
    {
      formatter.setTimeZone( props.getTimeZone() );
    }
    return formatter;
  }
}

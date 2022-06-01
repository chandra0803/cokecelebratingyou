
package com.biperf.core.utils.formatter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

@SuppressWarnings( "serial" )
public class CustomDateFormatter extends DateFormatter
{
  public CustomDateFormatter( DateFormatProperties props )
  {
    super( props );
  }

  public String getDatePattern()
  {
    String pattern = getLocalizedPattern();
    pattern = pattern.replaceAll( "aaaa", "yyyy" );
    pattern = pattern.replaceAll( "uuuu", "yyyy" );
    pattern = pattern.replaceAll( "bbbb", "yyyy" );
    pattern = pattern.replaceAll( "nn", "MM" );
    pattern = pattern.replaceAll( "jj", "dd" );
    pattern = pattern.replaceAll( "tt", "dd" );
    pattern = pattern.replaceAll( "LL", "dd" );
    return pattern;
  }

  protected String getLocalizedPattern()
  {
    SimpleDateFormat formatter = (SimpleDateFormat)super.getFormatter();
    return formatter.toLocalizedPattern();
  }

  protected DateFormat createFormatter()
  {
    DateFormat formatter = super.createFormatter();
    if ( formatter instanceof SimpleDateFormat )
    {
      SimpleDateFormat sdf = (SimpleDateFormat)formatter;
      sdf.applyLocalizedPattern( customPattern( sdf.toLocalizedPattern() ) );
      sdf.applyPattern( customPattern( sdf.toPattern() ) );
    }
    return formatter;
  }

  protected String customPattern( String pattern )
  {
    pattern = yearCustomPattern( pattern );
    pattern = monthCustomPattern( pattern );
    pattern = dateCustomPattern( pattern );
    return pattern;
  }

  protected String applyCustomPattern( String pattern, String[] params )
  {
    if ( params.length == 4 )
    {
      if ( pattern.indexOf( params[3] ) < 0 && pattern.indexOf( params[2] ) < 0 )
      {
        if ( pattern.indexOf( params[1] ) < 0 )
        {
          pattern = pattern.replaceAll( params[0], params[3] );
        }
        else
        {
          pattern = pattern.replaceAll( params[1], params[3] );
        }
      }
    }
    else if ( params.length == 2 )
    {
      if ( pattern.indexOf( params[1] ) < 0 )
      {
        pattern = pattern.replaceAll( params[0], params[1] );
      }
    }
    return pattern;
  }

  protected String yearCustomPattern( String pattern )
  {
    pattern = applyCustomPattern( pattern, new String[] { "y", "yy", "yyy", "yyyy" } );
    pattern = applyCustomPattern( pattern, new String[] { "a", "aa", "aaa", "aaaa" } );
    pattern = applyCustomPattern( pattern, new String[] { "u", "uu", "uuu", "uuuu" } );
    pattern = applyCustomPattern( pattern, new String[] { "b", "bb", "bbb", "bbbb" } );
    return pattern;
  }

  protected String monthCustomPattern( String pattern )
  {
    pattern = applyCustomPattern( pattern, new String[] { "M", "MM" } );
    pattern = applyCustomPattern( pattern, new String[] { "n", "nn" } );
    return pattern;
  }

  protected String dateCustomPattern( String pattern )
  {
    pattern = applyCustomPattern( pattern, new String[] { "d", "dd" } );
    pattern = applyCustomPattern( pattern, new String[] { "j", "jj" } );
    pattern = applyCustomPattern( pattern, new String[] { "t", "tt" } );
    pattern = applyCustomPattern( pattern, new String[] { "L", "LL" } );
    return pattern;
  }

}

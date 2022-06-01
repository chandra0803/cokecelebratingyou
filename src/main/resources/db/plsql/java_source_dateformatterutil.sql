DECLARE 
v_command VARCHAR2(32767);

BEGIN

v_command := '
CREATE OR REPLACE AND RESOLVE JAVA SOURCE NAMED "DateFormatterUtil" AS 

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateFormatterUtil implements Serializable
{
  static final String dateFormat = "dd/MM/yyyy";
  static final String timeFormat = "HH:mm";
  static final String timeWithSecFormat = "HH:mm:ss";
  static final String dateTimeFormat = dateFormat + " " + timeFormat;
  static final String dateTimeWithSecFormat = dateFormat + " " + timeWithSecFormat;
  
  static final String dateTinyMceFormat = "%d/%m/%Y";
  static final String timeTinyMceFormat = "%H:%M";
  static final String dateTimeTinyMceFormat = dateTinyMceFormat + " " + timeTinyMceFormat;
  
  static final String dateOracleFormat = "DD/MM/YYYY";
  static final String timeOracleFormat = "HH24:MI";
  static final String timeWithSecOracleFormat = "HH24:MI:SS";
  static final String dateTimeOracleFormat = dateOracleFormat + " " + timeOracleFormat;
  static final String dateTimeWithSecOracleFormat = dateOracleFormat + " " + timeWithSecOracleFormat;
  
  public static String getDatePattern( Locale locale )
  {
    // Get ISO standard pattern with formatted (DD, MM, YYYY)
    DateFormatProperties props = new DateFormatProperties();
    props.setDateStyle( "short" );
    props.setLocale( locale );

    CustomDateFormatter customFormatter = new CustomDateFormatter( props );
    return customFormatter.getDatePattern();
  }

  public static String getDateTimePattern( Locale locale )
  {
    String pattern = getDatePattern( locale );
    pattern = pattern + " " + timeFormat;
    return pattern;
  }

  public static String getDateTimeSecPattern( Locale locale )
  {
    String pattern = getDatePattern( locale );
    pattern = pattern + " " + timeWithSecFormat;
    return pattern;
  }

  public static String getDateTimeSecTZPattern( Locale locale )
  {
    String pattern = getDateTimeSecPattern( locale );
    pattern = pattern + " z";
    return pattern;
  }

  public static String getTinyMceDatePattern( Locale locale )
  {
    String pattern = getDatePattern( locale );
    pattern = pattern.replaceAll( "yyyy", "%Y" );
    pattern = pattern.replaceAll( "MM", "%m" );
    pattern = pattern.replaceAll( "dd", "%d" );
    return pattern;
  }

  public static String getTinyMceDateTimePattern( Locale locale )
  {
    String pattern = getTinyMceDatePattern( locale );
    pattern = pattern + " " + timeTinyMceFormat;
    return pattern;
  }

  public static String getOracleDatePattern( String languageCode )
  {
    String pattern = getDatePattern( getLocale( languageCode ) );
    pattern = pattern.replaceAll( "yyyy", "YYYY" );
    //pattern = pattern.replaceAll( "MM", "MM" );
    pattern = pattern.replaceAll( "dd", "DD" );
    return pattern;
  }

  public static String getOracleDateTimePattern( String languageCode )
  {
    String pattern = getOracleDatePattern( languageCode );
    pattern = pattern + " " + timeOracleFormat;
    return pattern;
  }

  public static String getOracleDateTimeSecPattern( String languageCode )
  {
    String pattern = getOracleDatePattern( languageCode );
    pattern = pattern + " " + timeWithSecOracleFormat;
    return pattern;
  }

  private static Locale getLocale( String languageCode )
  {
    int index = languageCode.indexOf( "_" );
    if ( index == -1 )
    {
      return new Locale( languageCode );
    }
    else
    {
      String language = languageCode.substring( 0, index );
      String country = languageCode.substring( index + 1 );
      return new Locale(language, country );
    }
  }

}

class CustomDateFormatter extends DateFormatter
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

class DateFormatter implements Serializable
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

class DateFormatProperties implements Serializable
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
    return ( null == dateStyle && null == timeStyle ) ? new Integer( DateFormat.DEFAULT ) : dateStyle;
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
    return ( null == dateStyle && null == timeStyle ) ? new Integer( DateFormat.DEFAULT ) : timeStyle;
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
  
  private static boolean isEmpty(String str) 
  {
    return str == null || str.length() == 0;
  }
}';
EXECUTE IMMEDIATE v_command;

END;
/


package com.biperf.core.utils;

import java.io.Serializable;
import java.util.Locale;

import com.biperf.core.utils.formatter.CustomDateFormatter;
import com.biperf.core.utils.formatter.DateFormatProperties;

@SuppressWarnings( "serial" )
public class DateFormatterUtil implements Serializable
{
  static final String dateFormat = "dd/MM/yyyy";
  static final String timeFormat = "HH:mm";
  static final String timeFormat12Hour = "hh:mm";
  static final String timeFormat12HourWithAmAndPm = "hh:mm aa";
  static final String timeWithSecFormat = "HH:mm:ss";
  static final String timeWithSecFormatWO = "HHmmss";
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

  static final String dateTimeWithSecUniversalFormat = "yyyy-MM-dd'T'HH:mm:ss";

  public static String getDatePattern( Locale locale )
  {
    // Get ISO standard pattern with formatted (DD, MM, YYYY)
    if ( locale != null && locale.toString().equalsIgnoreCase( "fr_CA" ) )
    {
      return dateFormat;
    }
    else
    {
      if ( locale != null && locale.toString().equalsIgnoreCase( "zh_HK" ) )
      {
        locale = new Locale( "zh", "CN" );
      }
      DateFormatProperties props = new DateFormatProperties();
      props.setDateStyle( "short" );
      props.setLocale( locale );

      CustomDateFormatter customFormatter = new CustomDateFormatter( props );
      return customFormatter.getDatePattern();
    }
  }

  public static String getDateTimePattern( Locale locale )
  {
    String pattern = getDatePattern( locale );
    pattern = pattern + " " + timeFormat;
    return pattern;
  }

  public static String getDateTime12HourPattern( Locale locale )
  {
    String pattern = getDatePattern( locale );
    pattern = pattern + " " + timeFormat12Hour;
    return pattern;
  }

  public static String getDateTime12HourPatternWithAmPm( Locale locale )
  {
    String pattern = getDatePattern( locale );
    pattern = pattern + " " + timeFormat12HourWithAmAndPm;
    return pattern;
  }

  public static String getDateTimeSecPattern( Locale locale )
  {
    String pattern = getDatePattern( locale );
    pattern = pattern + ", " + timeWithSecFormat;
    return pattern;
  }

  public static String getDateTimeSecUPattern( Locale locale )
  {
    String pattern = getDatePattern( locale );
    pattern = pattern + "_" + timeWithSecFormatWO;
    return pattern;
  }

  public static String getDateTimeSecTZPattern( Locale locale )
  {
    String pattern = getDateTimeSecPattern( locale );
    pattern = pattern + " z";
    return pattern;
  }

  public static String getDateTimeMerTZPattern( Locale locale )
  {
    String pattern = getDateTime12HourPattern( locale );
    pattern = pattern + " a z";
    return pattern;
  }

  public static String getDateTimeMerPattern( Locale locale )
  {
    String pattern = getDateTime12HourPattern( locale );
    pattern = pattern + " a";
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
    // pattern = pattern.replaceAll( "MM", "MM" );
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
    int index = languageCode.indexOf( '_' );
    if ( index == -1 )
    {
      return new Locale( languageCode );
    }
    else
    {
      String language = languageCode.substring( 0, index );
      String country = languageCode.substring( index + 1 );
      return new Locale( language, country );
    }
  }

  public static String getdateTimeWithSecUniversalFormat()
  {
    return dateTimeWithSecUniversalFormat;
  }

}

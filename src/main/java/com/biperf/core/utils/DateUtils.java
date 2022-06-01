/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/utils/DateUtils.java,v $
 */

package com.biperf.core.utils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.objectpartners.cms.util.CmsResourceBundle;

public class DateUtils
{
  // if this is changed, there is a function in commonFunctions.js that also needs to be changed.
  // this is ONLY used for prompting the user, not date formatting via Java so its format is not
  // important
  public static final String displayDateFormatMask = null; // was "MM/DD/YYYY";

  public static final Log log = LogFactory.getLog( DateUtils.class );

  public static Date getCurrentDate()
  {
    Calendar calendar = GregorianCalendar.getInstance();
    return calendar.getTime();
  }

  public static Date getCurrentDateTrimmed()
  {
    Date currentDate = new Date();
    Calendar cal = Calendar.getInstance();
    cal.setTime( currentDate );
    // Set time fields to zero
    cal.set( Calendar.HOUR_OF_DAY, 0 );
    cal.set( Calendar.MINUTE, 0 );
    cal.set( Calendar.SECOND, 0 );
    cal.set( Calendar.MILLISECOND, 0 );

    // Put it back in the Date object
    currentDate = cal.getTime();
    return currentDate;
  }

  public static Date getNextDay( Date inputDate )
  {
    Date currentDate = null;

    if ( inputDate == null )
    {
      currentDate = new Date();
    }
    else
    {
      currentDate = inputDate;
    }
    Date nextDay = new Date();
    Calendar cal = Calendar.getInstance();
    cal.setTime( currentDate );
    cal.add( Calendar.DATE, 1 );
    // Put it back in the Date object
    nextDay = cal.getTime();
    return nextDay;
  }

  public static Date getPastDueDate( Date inputDate, int pastDue )
  {
    Date currentDate = null;

    if ( inputDate == null )
    {
      currentDate = new Date();
    }
    else
    {
      currentDate = inputDate;
    }
    Date pastDueDate = new Date();
    Calendar cal = Calendar.getInstance();
    cal.setTime( currentDate );
    cal.add( Calendar.DATE, -pastDue );
    // Put it back in the Date object
    pastDueDate = cal.getTime();
    return pastDueDate;
  }

  /**
   * Returns the first of the current year (Jan 1, current year)
   * 
   * @return Date
   */
  public static Date getFirstDayOfThisYear()
  {
    Calendar calendar = GregorianCalendar.getInstance();
    calendar.set( Calendar.MONTH, 0 );
    calendar.set( Calendar.DAY_OF_MONTH, 1 );

    return calendar.getTime();
  }

  /**
   * Returns the first of the prior month
   * 
   * @param date
   * @return Date
   */
  public static Date getFirstDayOfPreviousMonth( Date date )
  {
    Calendar calendar = GregorianCalendar.getInstance();

    calendar.setTime( date );

    calendar.add( Calendar.MONTH, -1 );
    calendar.set( Calendar.DAY_OF_MONTH, calendar.getActualMinimum( Calendar.DAY_OF_MONTH ) );

    return toStartDate( calendar.getTime() );
  }

  /**
   * Returns the first of the prior month
   * 
   * @return Date
   */
  public static Date getFirstDayOfPreviousMonth()
  {
    return getFirstDayOfPreviousMonth( new Date() );
  }

  /**
   * Returns the first of the month
   * 
   * @param date
   * @return Date
   */
  public static Date getFirstDayOfMonth( Date date )
  {
    Calendar calendar = GregorianCalendar.getInstance();
    calendar.setTime( date );
    calendar.set( Calendar.DAY_OF_MONTH, calendar.getActualMinimum( Calendar.DAY_OF_MONTH ) );
    return toStartDate( calendar.getTime() );
  }

  /**
   * Returns the first of the current month
   * 
   * @return Date
   */
  public static Date getFirstDayOfCurrentMonth()
  {
    return getFirstDayOfMonth( new Date() );
  }

  /**
   * Returns the last of the month
   * 
   * @param date
   * @return Date
   */
  public static Date getLastDayOfMonth( Date date )
  {
    Calendar calendar = GregorianCalendar.getInstance();
    calendar.setTime( date );
    calendar.set( Calendar.DAY_OF_MONTH, calendar.getActualMaximum( Calendar.DAY_OF_MONTH ) );
    return toStartDate( calendar.getTime() );
  }

  /**
   * Returns the last of the current month
   * 
   * @return Date
   */
  public static Date getLastDayOfCurrentMonth()
  {
    return getLastDayOfMonth( new Date() );
  }

  /**
   * Gives the month for the specific date
   * @return int month
   */
  public static int getMonthFromDate( Date inputDate )
  {
    Calendar cal = Calendar.getInstance();
    cal.setTime( inputDate );
    return cal.get( Calendar.MONTH );
  }

  /**
   * Gives the year for the specific date
   * @return int current year
   */
  public static int getYearFromDate( Date inputDate )
  {
    Calendar cal = Calendar.getInstance();
    cal.setTime( inputDate );
    return cal.get( Calendar.YEAR );
  }

  /**
   * Calculates the start date of the next rolling period
   * @param timeframeMonthId
   * @param timeframeYear
   * @param timeframeType
   * @param timeframeNavigate
   * @return
   */
  public static Date getRollingPeriodStartDate( int timeframeMonthId, int timeframeYear, String timeframeType, String timeframeNavigate )
  {
    Date rollingPeriodStartDate = null;

    if ( StringUtils.isEmpty( timeframeType ) )
    {
      rollingPeriodStartDate = getFirstDayOfCurrentMonth();
    }
    else
    {
      Calendar calendar = Calendar.getInstance();
      calendar.set( Calendar.MONTH, timeframeMonthId );
      calendar.set( Calendar.YEAR, timeframeYear );
      int firstDayOfMonth = calendar.getActualMinimum( Calendar.DATE );
      calendar.set( Calendar.DAY_OF_MONTH, firstDayOfMonth );

      int rollingPeriodAmount = 0;

      if ( "month".equals( timeframeType ) )
      {
        if ( "next".equals( timeframeNavigate ) )
        {
          rollingPeriodAmount = 1;
        }
        else if ( "prev".equals( timeframeNavigate ) )
        {
          rollingPeriodAmount = -1;
        }
      }
      else if ( "quarter".equals( timeframeType ) )
      {
        if ( "next".equals( timeframeNavigate ) )
        {
          rollingPeriodAmount = 1;
        }
        else if ( "prev".equals( timeframeNavigate ) )
        {
          rollingPeriodAmount = -5;
        }
        else
        {
          rollingPeriodAmount = -2;
        }
      }
      else if ( "year".equals( timeframeType ) )
      {
        if ( "next".equals( timeframeNavigate ) )
        {
          rollingPeriodAmount = 1;
        }
        else if ( "prev".equals( timeframeNavigate ) )
        {
          rollingPeriodAmount = -23;
        }
        else
        {
          rollingPeriodAmount = -11;
        }
      }
      calendar.add( Calendar.MONTH, rollingPeriodAmount );

      rollingPeriodStartDate = calendar.getTime();
      rollingPeriodStartDate = toStartDate( rollingPeriodStartDate );
    }
    return rollingPeriodStartDate;
  }

  /**
   * Calculates the end date of the next rolling period
   * @param timeframeMonthId
   * @param timeframeType
   * @param timeframeType
   * @param timeframeNavigate
   * @return
   */
  public static Date getRollingPeriodEndDate( int timeframeMonthId, int timeframeYear, String timeframeType, String timeframeNavigate )
  {
    Date rollingPeriodEndDate = null;

    if ( StringUtils.isEmpty( timeframeType ) )
    {
      rollingPeriodEndDate = getLastDayOfCurrentMonth();
    }
    else
    {
      Calendar calendar = Calendar.getInstance();
      calendar.set( Calendar.MONTH, timeframeMonthId );
      calendar.set( Calendar.YEAR, timeframeYear );
      int lastDayOfMonth = calendar.getActualMaximum( Calendar.DATE );
      calendar.set( Calendar.DAY_OF_MONTH, lastDayOfMonth );

      int rollingPeriodAmount = 0;
      if ( "next".equals( timeframeNavigate ) )
      {
        rollingPeriodAmount = 1;
      }
      else if ( "prev".equals( timeframeNavigate ) )
      {
        rollingPeriodAmount = -1;
      }

      if ( "month".equals( timeframeType ) )
      {
        calendar.add( Calendar.MONTH, rollingPeriodAmount );
      }
      else if ( "quarter".equals( timeframeType ) )
      {
        calendar.add( Calendar.MONTH, rollingPeriodAmount * 3 );
      }
      else if ( "year".equals( timeframeType ) )
      {
        calendar.add( Calendar.YEAR, rollingPeriodAmount );
      }
      rollingPeriodEndDate = calendar.getTime();
      rollingPeriodEndDate = toStartDate( rollingPeriodEndDate );
    }
    return rollingPeriodEndDate;
  }

  /**
   * Returns the full name of the previous month
   * 
   * @param date
   * @return String
   */
  public static String getPreviousMonthAsString( Date date )
  {
    Calendar calendar = GregorianCalendar.getInstance();
    SimpleDateFormat sdf = new SimpleDateFormat( "MMMMM" );

    calendar.setTime( date );

    calendar.add( Calendar.MONTH, -1 );

    return sdf.format( calendar.getTime() );
  }

  public static Date getOneMonthBeforeAsString( Date date )
  {
    Calendar calendar = GregorianCalendar.getInstance();

    calendar.setTime( new Date() );

    calendar.add( Calendar.DAY_OF_MONTH, -30 );

    return toEndDate( calendar.getTime() );
  }

  /**
   * Returns the year as string of the previous month
   * 
   * @param date
   * @return String
   */
  public static String getYearOfPreviousMonthAsString( Date date )
  {
    Calendar calendar = GregorianCalendar.getInstance();
    SimpleDateFormat sdf = new SimpleDateFormat( "yyyy" );

    calendar.setTime( date );

    calendar.add( Calendar.MONTH, -1 );

    return sdf.format( calendar.getTime() );
  }

  /**
   * Returns the last day of the prior month
   * 
   * @param date
   * @return Date
   */
  public static Date getLastDayOfPreviousMonth( Date date )
  {
    Calendar calendar = GregorianCalendar.getInstance();

    calendar.setTime( date );

    calendar.add( Calendar.MONTH, -1 );
    calendar.set( Calendar.DAY_OF_MONTH, calendar.getActualMaximum( Calendar.DAY_OF_MONTH ) );

    return toEndDate( calendar.getTime() );
  }

  /**
   * Returns the last day of the prior month
   * 
   * @return Date
   */
  public static Date getLastDayOfPreviousMonth()
  {
    return getLastDayOfPreviousMonth( new Date() );
  }

  public static long getCurrentDateAsLong()
  {
    Calendar calendar = GregorianCalendar.getInstance();
    return calendar.getTime().getTime();
  }

  public static long getDateAsLong( Date date )
  {
    Calendar calendar = GregorianCalendar.getInstance();
    calendar.setTime( date );
    return calendar.getTime().getTime();
  }

  public static Date toDate( String dateString )
  {
    return toDate( dateString, UserManager.getLocale() );
  }

  public static Date toDateFromyyyyMMdd( String dateString )
  {
    return toDateWithPattern( dateString, "yyyy-MM-dd" );
  }

  public static Date toDate( String dateString, Locale locale )
  {
    String pattern = DateFormatterUtil.getDatePattern( locale );
    return toDateWithPattern( dateString, pattern );
  }

  public static Date toDate( String dateString, String timeZoneID )
  {
    if ( StringUtils.isEmpty( dateString ) )
    {
      return null;
    }
    SimpleDateFormat sdf = new SimpleDateFormat( UserManager.getUserDatePattern() );
    Date date = null;
    try
    {
      sdf.setTimeZone( TimeZone.getTimeZone( timeZoneID ) );
      date = sdf.parse( dateString.trim() );
    }
    catch( ParseException e )
    {
      log.warn( "String to java.util.Date parsing exception, string to convert: '" + dateString + "', with pattern: '" + UserManager.getUserDatePattern() + "'" );
    }
    return date;
  }

  public static Date toDateWithPattern( String dateString, String formatString )
  {
    if ( StringUtils.isEmpty( dateString ) )
    {
      return null;
    }
    SimpleDateFormat sdf = new SimpleDateFormat( formatString );
    Date date = null;
    try
    {
      date = sdf.parse( dateString.trim() );
    }
    catch( ParseException e )
    {
      log.warn( "String to java.util.Date parsing exception, string to convert: '" + dateString + "', with pattern: '" + formatString + "'" );
    }
    return date;
  }

  /**
   * Returns true if the given date is valid
   * 
   * @param dateString
   * @param formatString
   * @return
   */
  public static boolean isValidDate( String dateString, String formatString )
  {
    if ( StringUtils.isEmpty( dateString ) )
    {
      return false;
    }
    SimpleDateFormat sdf = new SimpleDateFormat( formatString );

    try
    {
      sdf.parse( dateString.trim() );
    }
    catch( ParseException e )
    {
      return false;
    }
    return true;
  }

  public static SimpleDateFormat displayStringDateFormat( Locale locale )
  {
    return new SimpleDateFormat( DateFormatterUtil.getDatePattern( locale ) );
  }

  public static String toConvertDateFormatString( DateFormat originalFormat, DateFormat targetFormat, String dateString )
  {
    String formattedStartDate = "";
    try
    {
      Date dateStart = originalFormat.parse( dateString );
      formattedStartDate = targetFormat.format( dateStart );
    }
    catch( Exception e )
    {
      log.error( "String to java.util.Date parsing exception" );
    }
    return formattedStartDate;
  }

  public static String toDisplayString( Date date )
  {
    return toDisplayString( date, UserManager.getLocale() );
  }

  public static String toDisplayString( Date date, Locale locale )
  {
    if ( date == null )
    {
      return "";
    }

    DateFormat df = displayStringDateFormat( locale );
    return df.format( date );
  }

  public static String toDisplayTimeString( Date date )
  {
    if ( date == null )
    {
      return "";
    }

    SimpleDateFormat sdf = new SimpleDateFormat( DateFormatterUtil.getDateTimeSecPattern( UserManager.getLocale() ) );
    return sdf.format( date );
  }

  public static String toDisplayTimeWithTimeZoneString( Date date )
  {
    if ( date == null )
    {
      return "";
    }

    SimpleDateFormat sdf = new SimpleDateFormat( DateFormatterUtil.getDateTimeSecTZPattern( UserManager.getLocale() ) );
    return sdf.format( date );
  }

  public static String toDisplayTimeWithMeridiemTimeZoneString( Date date )
  {
    if ( date == null )
    {
      return "";
    }

    SimpleDateFormat sdf = new SimpleDateFormat( DateFormatterUtil.getDateTimeMerTZPattern( UserManager.getLocale() ) );
    return sdf.format( date );
  }

  public static String toDisplayTimeWithMeridiemString( Date date )
  {
    if ( date == null )
    {
      return "";
    }

    SimpleDateFormat sdf = new SimpleDateFormat( DateFormatterUtil.getDateTimeMerPattern( UserManager.getLocale() ) );
    return sdf.format( date );
  }

  /**
   * Converts a <code>String</code> object that represents a date and time to a <code>Date</code>
   * object that represents the same date and time.
   * 
   * @param dateString a <code>String</code> object that represents a date and time.
   * @return a <code>Date</code> object that represents the same date and time as the parameter
   *         <code>dateString</code>.
   * @throws ParseException if <code>dateString</code> does not represent a valid date.
   */
  public static Date toDateChecked( String dateString ) throws ParseException
  {
    DateFormat dateFormat = displayStringDateFormat( UserManager.getLocale() );
    return dateFormat.parse( dateString );
  }

  /**
   * Returns a <code>Date</code> object that represents the earliest time on the specified date.
   * 
   * @param dateString
   * @return a <code>Date</code> object that represents the earliest time on the specified date.
   * @throws ParseException if <code>dateString</code> does not represent a valid date.
   */
  public static Date toStartDate( String dateString ) throws ParseException
  {
    return toStartDate( toDateChecked( dateString ) );
  }

  /**
   * Returns a <code>Date</code> object that represents the earliest time on the specified date.
   * 
   * @param date
   * @return a <code>Date</code> object that represents the earliest time on the specified date.
   */
  public static Date toStartDate( Date date )
  {
    if ( date == null )
    {
      return null;
    }
    Calendar calendar = Calendar.getInstance();
    calendar.setTime( date );

    calendar.set( Calendar.HOUR_OF_DAY, calendar.getActualMinimum( Calendar.HOUR_OF_DAY ) );
    calendar.set( Calendar.MINUTE, calendar.getActualMinimum( Calendar.MINUTE ) );
    calendar.set( Calendar.SECOND, calendar.getActualMinimum( Calendar.SECOND ) );
    calendar.set( Calendar.MILLISECOND, calendar.getActualMinimum( Calendar.MILLISECOND ) );

    return calendar.getTime();
  }

  /**
   * Returns a <code>Date</code> object that represents the latest time on the specified date.
   * 
   * @param dateString
   * @return a <code>Date</code> object that represents the latest time on the specified date.
   * @throws ParseException if <code>dateString</code> does not represent a valid date.
   */
  public static Date toEndDate( String dateString ) throws ParseException
  {
    return toEndDate( toDateChecked( dateString ) );
  }

  /**
   * Returns a <code>Date</code> object that represents the latest time on the specified date.
   * 
   * @param date
   * @return a <code>Date</code> object that represents the latest time on the specified date.
   */
  public static Date toEndDate( Date date )
  {
    if ( date == null )
    {
      return null;
    }
    Calendar calendar = Calendar.getInstance();
    calendar.setTime( date );

    calendar.set( Calendar.HOUR_OF_DAY, calendar.getActualMaximum( Calendar.HOUR_OF_DAY ) );
    calendar.set( Calendar.MINUTE, calendar.getActualMaximum( Calendar.MINUTE ) );
    calendar.set( Calendar.SECOND, calendar.getActualMaximum( Calendar.SECOND ) );
    calendar.set( Calendar.MILLISECOND, calendar.getActualMaximum( Calendar.MILLISECOND ) );

    return calendar.getTime();
  }

  /**
   * Checks if today's date is between the given start and end date. Note: it does not look at time -
   * it clears that portion out. If no date is given for either start or end date, it will use
   * today's date.
   * 
   * @param startDate
   * @param endDate
   * @return boolean true if today's date is between the given start and end date
   */
  public static boolean isTodaysDateBetween( Date startDate, Date endDate )
  {
    return isDateBetween( new Date(), startDate, endDate );
  }

  /**
   * Checks if reference date is between the given start and end date. Note: it does not look at time -
   * it clears that portion out. If no date is given for either start or end date, it will use
   * reference date.
   * 
   * @param startDate
   * @param endDate
   * @return boolean true if reference date is between the given start and end date
   */
  public static boolean isDateBetween( Date referenceDate, Date startDate, Date endDate )
  {
    // FALSE, if Reference date is NULL
    if ( null == referenceDate )
    {
      return false;
    }

    // TRUE
    // 1. If Start date is NULL OR Reference date is SAMEDAY or AFTER Start date and
    // 2. If End date is NULL OR Reference date is SAMEDAY or BEFORE End date
    // FALSE otherwise
    return ( null == startDate || org.apache.commons.lang3.time.DateUtils.isSameDay( referenceDate, startDate ) || referenceDate.after( startDate ) )
        && ( null == endDate || org.apache.commons.lang3.time.DateUtils.isSameDay( referenceDate, endDate ) || referenceDate.before( endDate ) );
  }

  public static boolean isDateBetween( Date referenceDate, Date startDate, Date endDate, String timeZoneID )
  {
    // Apply TimeZone if it is valid
    if ( null != TimeZone.getTimeZone( timeZoneID ) )
    {
      referenceDate = DateUtils.applyTimeZone( referenceDate, timeZoneID );
    }
    return isDateBetween( referenceDate, startDate, endDate );
  }

  // Apply TimeZone to GIVEN Date
  public static Date applyTimeZone( Date date, String timeZoneID )
  {
    Calendar calTZ = new GregorianCalendar( TimeZone.getTimeZone( timeZoneID ) );
    // null check
    if ( date != null )
    {
      calTZ.setTime( date );
    }
    else
    {
      calTZ.setTime( new Date() );
    }
    Calendar cal = Calendar.getInstance();
    cal.set( Calendar.YEAR, calTZ.get( Calendar.YEAR ) );
    cal.set( Calendar.MONTH, calTZ.get( Calendar.MONTH ) );
    cal.set( Calendar.DAY_OF_MONTH, calTZ.get( Calendar.DAY_OF_MONTH ) );
    cal.set( Calendar.HOUR_OF_DAY, calTZ.get( Calendar.HOUR_OF_DAY ) );
    cal.set( Calendar.MINUTE, calTZ.get( Calendar.MINUTE ) );
    cal.set( Calendar.SECOND, calTZ.get( Calendar.SECOND ) );
    cal.set( Calendar.MILLISECOND, calTZ.get( Calendar.MILLISECOND ) );

    return cal.getTime();
  }

  public static String convertTimeTo24HourFormatAndTimezone( String time, String timeZone )
  {
    if ( time != null && timeZone != null )
    {
      Calendar localTime = Calendar.getInstance();
      String hr = time.substring( 0, 2 );
      String min = time.substring( 3, 5 );
      String amPm = time.substring( 6, 8 );
      int hour = Integer.valueOf( hr ).intValue();
      int minute = Integer.valueOf( min ).intValue();
      localTime.set( Calendar.HOUR, hour );
      localTime.set( Calendar.MINUTE, minute );
      if ( "AM".equals( amPm ) )
      {
        localTime.set( Calendar.AM_PM, Calendar.AM );
      }
      if ( "PM".equals( amPm ) )
      {
        localTime.set( Calendar.AM_PM, Calendar.PM );
      }

      Calendar customizedTimeforTimeZone = new GregorianCalendar( TimeZone.getTimeZone( timeZone ) );
      customizedTimeforTimeZone.setTimeInMillis( localTime.getTimeInMillis() );

      SimpleDateFormat format = new SimpleDateFormat( "HH:mm a" );
      return format.format( customizedTimeforTimeZone.getTime() );
    }
    return "";
  }

  // Changes related to bug fix #75157 & JIRA #3787 starts New method for converting time based on
  // timezone
  /*
   * Examples Inputs --> Outputs ("Jan-03-2018","02:17 AM", "PST" ) --> 00:17 AM PST
   * ("Jan-03-2018","02:17 AM", "MST" ) --> 01:17 AM MST ("Jan-03-2018","02:17 AM", "CST" ) -->
   * 02:17 AM CST ("Jan-03-2018","02:17 AM", "EST" ) --> 03:17 AM EST ("Jan-03-2018","02:17 AM",
   * "IST" ) --> 13:47 PM IST
   */
  public static String convertTimeTo24HourFormatAndTimezone( String date, String time, String convertToTimeZone ) throws ParseException
  {
    if ( time != null && convertToTimeZone != null )
    {
      // Input convertToTimeZone for this method is the user timezone. So if the input
      // convertToTimeZone is in CST
      // then don't alter any time as the both server and user are in CST.
      if ( convertToTimeZone.equalsIgnoreCase( "CST" ) )
      {
        return time + " " + convertToTimeZone;
      }
      else
      {
        // Else alter the server time to display in user timezone time format.
        Calendar currentCstTime = Calendar.getInstance();
        SimpleDateFormat dateTimeFormat = displayStringDateFormat( UserManager.getLocale() );
        Date formattedDate = dateTimeFormat.parse( date + " " + time );
        currentCstTime.setTime( formattedDate );

        Calendar currentUserTime = new GregorianCalendar( TimeZone.getTimeZone( convertToTimeZone ) );
        currentUserTime.setTimeInMillis( currentCstTime.getTimeInMillis() );

        SimpleDateFormat timeFormat = new SimpleDateFormat( "hh:mm a" );
        timeFormat.setTimeZone( TimeZone.getTimeZone( convertToTimeZone ) );
        return timeFormat.format( formattedDate ) + " " + convertToTimeZone;
      }
    }
    return "";
  }

  public static Date startDateOfWeek( Date date, String timeZoneID )
  {
    Calendar calTZ = new GregorianCalendar( TimeZone.getTimeZone( timeZoneID ) );

    if ( date != null )
    {
      calTZ.setTime( date );
    }
    else
    {
      calTZ.setTime( new Date() );
    }

    Calendar cal = Calendar.getInstance();
    cal.setTime( calTZ.getTime() );
    cal.set( Calendar.DAY_OF_WEEK, calTZ.getActualMinimum( Calendar.DAY_OF_WEEK ) );
    cal.set( Calendar.HOUR_OF_DAY, 0 );
    cal.set( Calendar.MINUTE, 0 );
    cal.set( Calendar.SECOND, 0 );
    cal.set( Calendar.MILLISECOND, 0 );
    return cal.getTime();
  }

  public static Date endDateOfWeek( Date date, String timeZoneID )
  {
    Calendar calTZ = new GregorianCalendar( TimeZone.getTimeZone( timeZoneID ) );

    if ( date != null )
    {
      calTZ.setTime( date );
    }
    else
    {
      calTZ.setTime( new Date() );
    }

    Calendar cal = Calendar.getInstance();
    cal.setTime( calTZ.getTime() );
    cal.set( Calendar.DAY_OF_WEEK, calTZ.getActualMaximum( Calendar.DAY_OF_WEEK ) );
    cal.set( Calendar.HOUR_OF_DAY, 23 );
    cal.set( Calendar.MINUTE, 59 );
    cal.set( Calendar.SECOND, 59 );
    cal.set( Calendar.MILLISECOND, 0 );
    return cal.getTime();
  }

  public static Date applyTimeZoneWithFirstTimeOfDay( Date date, String timeZoneID )
  {
    Calendar calTZ = new GregorianCalendar( TimeZone.getTimeZone( timeZoneID ) );
    // null check
    if ( date != null )
    {
      calTZ.setTime( date );
    }
    else
    {
      calTZ.setTime( new Date() );
    }
    Calendar cal = Calendar.getInstance();
    cal.set( Calendar.YEAR, calTZ.get( Calendar.YEAR ) );
    cal.set( Calendar.MONTH, calTZ.get( Calendar.MONTH ) );
    cal.set( Calendar.DAY_OF_MONTH, calTZ.get( Calendar.DAY_OF_MONTH ) );
    cal.set( Calendar.HOUR_OF_DAY, 0 );
    cal.set( Calendar.MINUTE, 0 );
    cal.set( Calendar.SECOND, 0 );
    cal.set( Calendar.MILLISECOND, 0 );

    return cal.getTime();
  }

  /**
   * Converts a <code>String</code> object that represents a date and time to a <code>Date</code>
   * object that represents the same date and time.
   * 
   * @param dateString a <code>String</code> object that represents a date and time.
   * @return a <code>Date</code> object that represents the same date and time as the parameter
   *         <code>dateString</code>.
   * @throws ParseException if <code>dateString</code> does not represent a valid date.
   */
  public static Date toDateWithTime( String dateString ) throws ParseException
  {
    DateFormat dateFormat = new SimpleDateFormat( DateFormatterUtil.getDateTimePattern( UserManager.getLocale() ) );
    return dateFormat.parse( dateString );
  }

  /**
   * @param date1
   * @param date2
   * @return the number of days between 2 dates. e.g. 12/30/01 and 2/1/02 returns 33
   */
  public static int getElapsedDays( Date date1, Date date2 )
  {
    GregorianCalendar g1 = new GregorianCalendar();
    g1.setTime( date1 );

    GregorianCalendar g2 = new GregorianCalendar();
    g2.setTime( date2 );

    int elapsed = 0;
    GregorianCalendar gc1, gc2;

    if ( g2.after( g1 ) )
    {
      gc2 = (GregorianCalendar)g2.clone();
      gc1 = (GregorianCalendar)g1.clone();
    }
    else
    {
      gc2 = (GregorianCalendar)g1.clone();
      gc1 = (GregorianCalendar)g2.clone();
    }

    gc1.clear( Calendar.MILLISECOND );
    gc1.clear( Calendar.SECOND );
    gc1.clear( Calendar.MINUTE );
    gc1.clear( Calendar.HOUR_OF_DAY );

    gc2.clear( Calendar.MILLISECOND );
    gc2.clear( Calendar.SECOND );
    gc2.clear( Calendar.MINUTE );
    gc2.clear( Calendar.HOUR_OF_DAY );

    while ( gc1.before( gc2 ) )
    {
      gc1.add( Calendar.DATE, 1 );
      elapsed++;
    }
    return elapsed;
  }

  /**
  
   * Rounds a <code>Date</code> object to the nearest midnight.
  
   * @param date
  
   * @return a <code>Date</code> object representing the nearest midnight.
  
   */

  public static Date round( Date date )

  {
    if ( date == null )
    {
      return null;
    }

    Calendar calendar = Calendar.getInstance();
    calendar.setTime( date );
    boolean roundUp = calendar.get( Calendar.HOUR_OF_DAY ) >= 12;
    calendar.set( Calendar.HOUR_OF_DAY, calendar.getActualMinimum( Calendar.HOUR_OF_DAY ) );
    calendar.set( Calendar.MINUTE, calendar.getActualMinimum( Calendar.MINUTE ) );
    calendar.set( Calendar.SECOND, calendar.getActualMinimum( Calendar.SECOND ) );
    calendar.set( Calendar.MILLISECOND, calendar.getActualMinimum( Calendar.MILLISECOND ) );

    if ( roundUp )
    {
      calendar.add( Calendar.DAY_OF_MONTH, 1 );
    }

    return calendar.getTime();

  }

  /**
   * Convert inputDate into social friendly time laps String
   * Today               Just now (for 0-9 seconds)
   *                     About 50 seconds ago
   *                     About 2 minutes ago
   *                     About 23 hours ago
   * Yesterday           About 1 day ago 
   * Between 2-6 days    About 2 days ago
   * Between 7-13 days   About 1 week ago
   * Between 14-20 days  About 2 weeks ago
   * Between 30-60 days  About 1 Month ago
   * Between 60-90 days  About 2 Months ago
   * 1 - 2 years         About 1 year ago 
   * Between 2-3 years   About 2 years ago
   * 
   * 
   * @param inputDate
   * @return String
   */
  public static String toRelativeTimeLapsed( Date inputDate )
  {
    final String NOW = CmsResourceBundle.getCmsBundle().getString( "relative.time.elapsed.NOW" );// "Just
                                                                                                 // now";

    TimeUnit[] timeUnits = new TimeUnit[] { new TimeUnit( "SECOND", 60L, 1L ),
                                            new TimeUnit( "MINUTE", 3600L, 60L ), // 60min*60sec
                                            new TimeUnit( "HOUR", 86400L, 3600L ), // 24hours*60min*60sec
                                            new TimeUnit( "DAY", 604800L, 86400L ), // 7days*24hours*60min*60sec
                                            new TimeUnit( "WEEK", 2592000L, 604800L ), // 30days*24hours*60min*60sec
                                            new TimeUnit( "MONTH", 31536000L, 2592000L ), // 365days*24hours*60min*60sec
                                            new TimeUnit( "YEAR", 0, 31536000L ) };

    long currentTime = Calendar.getInstance().getTimeInMillis();
    long diffInSeconds = currentTime / 1000 - inputDate.getTime() / 1000;

    if ( diffInSeconds < 10 )
    {
      return NOW;
    }

    int i = 0;
    TimeUnit unit = null;
    long unitDifference = -1;
    String cmKey = null;
    String cmDesc = null;
    while ( i < timeUnits.length && unitDifference != 0 )
    {
      unit = timeUnits[i++];
      if ( diffInSeconds < unit.limit || unit.limit == 0 )
      {
        unitDifference = (long)Math.floor( diffInSeconds / unit.inSeconds );
        if ( unitDifference == 1 )
        {
          cmKey = "relative.time.elapsed.ABOUT_" + unit.name + "_AGO";
          cmDesc = CmsResourceBundle.getCmsBundle().getString( cmKey );
        }
        else if ( unitDifference > 1 )
        {
          cmKey = "relative.time.elapsed.ABOUT_" + unit.name + "S_AGO";
          cmDesc = CmsResourceBundle.getCmsBundle().getString( cmKey );
          MessageFormat formatter = new MessageFormat( cmDesc );
          cmDesc = formatter.format( new Long[] { unitDifference } );
        }
      }
    }
    return cmDesc;
  }

  static class TimeUnit
  {
    public String name;
    public long limit;
    public long inSeconds;

    public TimeUnit( String name, long limit, long inSeconds )
    {
      this.name = name;
      this.limit = limit;
      this.inSeconds = inSeconds;
    }
  }

  /**
   * <table border="1">
   * <caption>Method functionality</caption>
   * <tr><td>Input java.sql.Timestamp:</td><td>2012-12-15 12:02:29.329</td></tr>
   * <tr><td>Output java.util.Date:</td><td>Sat Dec 15 12:02:29 CST 2012</td></tr>
   * </table>
   * @param  timestamp
   * @return java.util.Date
   */
  public static Date getDateFromTimeStamp( Timestamp timestamp )
  {
    if ( timestamp != null )
    {
      return new java.util.Date( timestamp.getTime() );
    }
    else
    {
      return new Date();
    }
  }

  /**
   * <table border="1">
   * <caption>Method functionality</caption>
   * <tr><td>Input java.sql.Timestamp:</td><td>2012-12-15 12:02:29.329</td></tr>
   * <tr><td>Output String:</td><td>12/15/2012</td></tr>
   * </table>
   * @param  timestamp
   * @return String
   */
  public static String getStringFromTimeStamp( Timestamp timestamp )
  {
    Date date = new Date();
    if ( timestamp != null )
    {
      date = getDateFromTimeStamp( timestamp );
    }
    return toDisplayString( date );
  }

  /**
   * @param timestamp
   * @return String representing a date for sorting purposes <year><month><dayofmonth>
   */
  public static String getSortDateString( Timestamp timestamp )
  {
    Date date = new Date();
    if ( timestamp != null )
    {
      date = getDateFromTimeStamp( timestamp );
    }
    return getSortDateString( date );
  }

  public static String getSortDateString( Date date )
  {
    return String.format( "%1$tY%1$tm%1$td", date );
  }

  /**
   * <table border="1">
   * <caption>Method functionality</caption>
   * <tr><td>Input Date and Locale</td><td>java Date and Locale.US</td></tr>
   * <tr><td>Output String:</td><td>01/09/2012 01:51 PM</td></tr>
   * </table>
   * @param  date
   * @param  locale
   * @return String
   */
  public static String getDateTimeStringIn12HourPattern( Date date, Locale locale )
  {
    if ( date == null )
    {
      return "";
    }
    SimpleDateFormat sdf = new SimpleDateFormat( DateFormatterUtil.getDateTime12HourPatternWithAmPm( locale ) );
    return sdf.format( date );
  }

  public static String toDisplayTimeString( Date date, Locale locale )
  {
    if ( date == null )
    {
      return "";
    }

    SimpleDateFormat sdf = new SimpleDateFormat( DateFormatterUtil.getDateTimeSecPattern( locale ) );
    return sdf.format( date );
  }

  public static String toDisplayTimeUString( Date date, Locale locale )
  {
    if ( date == null )
    {
      return "";
    }

    SimpleDateFormat sdf = new SimpleDateFormat( DateFormatterUtil.getDateTimeSecUPattern( locale ) );
    return sdf.format( date );
  }

  public static String toDisplayDateString( Date date, Locale locale )
  {
    if ( date == null )
    {
      return "";
    }

    SimpleDateFormat sdf = new SimpleDateFormat( DateFormatterUtil.getDatePattern( locale ) );
    return sdf.format( date );
  }

  public static String toDisplayTimeStringWithoutSeconds( Date date, Locale locale )
  {
    if ( date == null )
    {
      return "";
    }

    SimpleDateFormat sdf = new SimpleDateFormat( DateFormatterUtil.getDateTimePattern( locale ) );
    return sdf.format( date );
  }

  public static Date getDateAfterNumberOfDays( Date date, int days )
  {
    Date newDate = new Date( date.getTime() );
    Calendar c = Calendar.getInstance();
    c.setTime( newDate );
    c.add( Calendar.DATE, days );
    newDate.setTime( c.getTime().getTime() );
    return newDate;
  }

  public static int getNumberOfDaysLeft( Date date1, Date date2 )
  {
    Calendar cal1 = Calendar.getInstance();
    Calendar cal2 = Calendar.getInstance();
    cal1.setTime( date1 );
    cal2.setTime( date2 );

    int numberOfDays = 0;
    while ( cal1.before( cal2 ) )
    {
      numberOfDays++;
      cal1.add( Calendar.DATE, 1 );
    }
    return numberOfDays;
  }

  public static String toDisplayUniversalDateString( Date date )
  {
    if ( date == null )
    {
      return "";
    }

    SimpleDateFormat sdf = new SimpleDateFormat( DateFormatterUtil.getdateTimeWithSecUniversalFormat() );
    return sdf.format( date );
  }

  public static String toDateFormat( String dateString )
  {
    if ( StringUtils.isEmpty( dateString ) )
    {
      return null;
    }

    // String pattern = DateFormatterUtil.getDatePattern( "" );
    SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd" );
    Date date = null;
    String returnString = "";
    try
    {
      date = sdf.parse( dateString.trim() );
      returnString = toDisplayDateString( date, UserManager.getLocale() );
    }
    catch( ParseException e )
    {
      log.warn( "String to java.util.Date parsing exception, string to convert: '" + dateString + "', with pattern: yyyy-MM-dd '" + "'" );
    }
    return returnString;
  }

  // Sting time in format ( HH:MM AMorPM )
  public static String convertTimeToTimezone( String time, String timeZone )
  {
    if ( time != null && timeZone != null )
    {
      Calendar localTime = Calendar.getInstance();
      String hr = time.substring( 0, 2 );
      String min = time.substring( 3, 5 );
      int hour = Integer.valueOf( hr ).intValue();
      int minute = Integer.valueOf( min ).intValue();

      localTime.set( Calendar.HOUR, hour );
      localTime.set( Calendar.MINUTE, minute );

      int customizedHour = localTime.get( Calendar.HOUR );
      int customizedMinute = localTime.get( Calendar.MINUTE );

      Calendar customizedTimeforTimeZone = new GregorianCalendar( TimeZone.getTimeZone( timeZone ) );
      customizedTimeforTimeZone.setTimeInMillis( localTime.getTimeInMillis() );
      customizedHour = customizedTimeforTimeZone.get( Calendar.HOUR );
      customizedMinute = customizedTimeforTimeZone.get( Calendar.MINUTE );
      int customTimeZoneMeridien = customizedTimeforTimeZone.get( Calendar.AM_PM );
      String convertedTime = String.format( "%02d:%02d", customizedHour, customizedMinute );
      String am_pm = null;
      if ( customTimeZoneMeridien == 0 )
      {
        am_pm = "AM";
      }
      else
      {
        am_pm = "PM";
      }

      StringBuffer convertTimeBuf = new StringBuffer();
      convertTimeBuf.append( convertedTime + " " + am_pm );
      return convertTimeBuf.toString();
    }
    return "";
  }

  public static int getDaysToGoFromToday( Date dateToEnd )
  {
    return getDaysToGoFromToday( dateToEnd, true );
  }

  public static int getDaysToGoFromToday( Date dateToEnd, boolean includeToday )
  {
    Calendar cal1 = Calendar.getInstance();
    Calendar cal2 = Calendar.getInstance();
    cal1.setTime( getCurrentDate() );
    cal2.setTime( dateToEnd );

    if ( includeToday )
    {
      cal2.add( Calendar.DATE, 1 );
    }

    int numberOfDays = 0;
    while ( cal1.before( cal2 ) )
    {
      numberOfDays++;
      cal1.add( Calendar.DATE, 1 );
    }
    return numberOfDays;
  }

  public static Date getZeroTimeDate( Date date )
  {
    Date res = date;
    Calendar calendar = Calendar.getInstance();

    calendar.setTime( date );
    calendar.set( Calendar.HOUR_OF_DAY, 0 );
    calendar.set( Calendar.MINUTE, 0 );
    calendar.set( Calendar.SECOND, 0 );
    calendar.set( Calendar.MILLISECOND, 0 );

    res = calendar.getTime();

    return res;
  }

  public static String getPreviousDayStringyyyy_MM_dd()
  {
    return LocalDate.now().minusDays( 1 ).format( DateTimeFormatter.ofPattern( "yyyy-MM-dd" ) );
  }

  public static String getDateStringyyyy_MM_dd( Date date )
  {
    return date != null ? new java.sql.Date( date.getTime() ).toLocalDate().format( DateTimeFormatter.ofPattern( "yyyy-MM-dd" ) ) : null;
  }

  public static Date getDateFromStringyyyy_MM_dd( String date )
  {
    return java.sql.Date.valueOf( LocalDate.parse( date, DateTimeFormatter.ofPattern( "yyyy-MM-dd" ) ) );
  }

  /**
   * Validate given date in ISO-8601 UTC format
   * 
   * @param date
   * @return True if the given date ISO-8601 UTC format other wise false 
   */
  public static boolean isValidUTCDate( String date )
  {
    try
    {
      Date.from( Instant.parse( date ) );
    }
    catch( Exception exception )
    {
      return false;
    }
    return true;
  }

  /**
   * Convert a given date in ISO-8601 UTC format to Java Util Date  
   * 
   * @param utcDate Valid ISO-8601 UTC format Date String
   * @return The given date in Java util Date
   */
  public static Date convert( String utcDate )
  {
    try
    {
      Date dt = Date.from( Instant.parse( utcDate ) );
      return dt;
    }
    catch( Exception exception )
    {
      return null;
    }

  }
  public static int getNumberOfYears( Date hireDate )
  {
    Calendar firstCalendar = GregorianCalendar.getInstance();
    firstCalendar.setTime( hireDate );
    Calendar secondCalendar = GregorianCalendar.getInstance();
    secondCalendar.setTime( new Date() );
    return secondCalendar.get( Calendar.YEAR ) - firstCalendar.get( Calendar.YEAR );
  }

  public static Date addMonths( int months )
  {
    Date current = new Date();
    Calendar cal = Calendar.getInstance();
    cal.setTime( current );
    cal.set( Calendar.MONTH, ( cal.get( Calendar.MONTH ) + months ) );
    return cal.getTime();
  }
  
 public static int monthsBetweenNowAndHireDate( Date hireDate )
 {
   LocalDate localHireDate = hireDate.toInstant().atZone( ZoneId.systemDefault() ).toLocalDate();
   LocalDate localTodayDate = LocalDate.now();
   Period period = Period.between( localHireDate, localTodayDate );
   return period.getMonths();
 } 
 
}

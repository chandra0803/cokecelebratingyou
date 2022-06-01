/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javatest/com/biperf/core/utils/DateUtilsTest.java,v $
 */

package com.biperf.core.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import com.biperf.core.domain.enums.TimeZoneId;

import junit.framework.TestCase;

/**
 * DateUtilsTest.
 * <p>
 * <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>tennant</td>
 * <td>Apr 6, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class DateUtilsTest extends TestCase
{

  public void testNullDate()
  {
    Locale locale = Locale.US;
    assertEquals( "", DateUtils.toDisplayString( null, locale ) );
  }

  public void testToDate()
  {
    String input = "01/05/2005";
    Locale locale = Locale.US;
    Date date = DateUtils.toDate( input, locale );
    Calendar cal = Calendar.getInstance();
    cal.setTime( date );
    assertEquals( 2005, cal.get( Calendar.YEAR ) );
    assertEquals( Calendar.JANUARY, cal.get( Calendar.MONTH ) );
    assertEquals( 5, cal.get( Calendar.DAY_OF_MONTH ) );
  }

  public void testToDisplayString()
  {
    Date date = new Date( 1104904800000L ); // This value was retrieved by observation from the
    // testToDate method
    Locale locale = Locale.US;
    String result = DateUtils.toDisplayString( date, locale );
    assertEquals( "01/05/2005", result );
  }

  /**
   * Test the getFirstDayOfThisYear method
   */
  public void testGetFirstDayOfThisYear()
  {
    Calendar cal = Calendar.getInstance();
    int year = cal.get( Calendar.YEAR );

    String expectedDate = "01/01/" + year;

    Date firstDayOfThisYear = DateUtils.getFirstDayOfThisYear();

    Locale locale = Locale.US;

    assertEquals( expectedDate, DateUtils.toDisplayString( firstDayOfThisYear, locale ) );
  }

  /**
   * Test the isTodaysDateBetween method Does today fall within 1/1 and 12/31 of current year?
   */
  public void testIsTodaysDateBetweenTestInRange()
  {
    Calendar cal = new GregorianCalendar();
    int year = cal.get( Calendar.YEAR ); // always use current year
    Calendar date1 = new GregorianCalendar( year, Calendar.JANUARY, 1 );
    Date start = date1.getTime();
    Calendar date2 = new GregorianCalendar( year, Calendar.DECEMBER, 31 );
    Date end = date2.getTime();
    // Test1: Assert true when the current date falls within 1/1 and 12/31 of the current year
    assertEquals( true, DateUtils.isTodaysDateBetween( start, end ) );
  }

  public void testIsTodaysDateBetweenYesterdayAndTomorrow()
  {
    Date start = new Date( new Date().getTime() - org.apache.commons.lang3.time.DateUtils.MILLIS_PER_DAY );
    Date end = new Date( new Date().getTime() + org.apache.commons.lang3.time.DateUtils.MILLIS_PER_DAY );
    assertTrue( DateUtils.isTodaysDateBetween( start, end ) );
  }

  public void testIsTodaysDateBetweenYesterdayAndYesterday()
  {
    Date start = new Date( new Date().getTime() - org.apache.commons.lang3.time.DateUtils.MILLIS_PER_DAY );
    Date end = new Date( new Date().getTime() - org.apache.commons.lang3.time.DateUtils.MILLIS_PER_DAY );
    assertFalse( DateUtils.isTodaysDateBetween( start, end ) );
  }

  public void testIsTodaysDateBetweenTomorrowAndTomorrow()
  {
    Date start = new Date( new Date().getTime() + org.apache.commons.lang3.time.DateUtils.MILLIS_PER_DAY );
    Date end = new Date( new Date().getTime() + org.apache.commons.lang3.time.DateUtils.MILLIS_PER_DAY );
    assertFalse( DateUtils.isTodaysDateBetween( start, end ) );
  }

  public void testIsTodaysDateBetweenOneMilliAgoAndOneMilliFromNow()
  {
    Date start = new Date( new Date().getTime() - 1 );
    Date end = new Date( new Date().getTime() + 1 );
    assertTrue( DateUtils.isTodaysDateBetween( start, end ) );
  }

  /** Test would break at exactly midnight, but slim chance of that */
  public void testIsTodaysDateBetweenOneMilliAgoAndOneMilliAgo()
  {
    Date start = new Date( new Date().getTime() - 1 );
    Date end = new Date( new Date().getTime() - 1 );
    assertTrue( DateUtils.isTodaysDateBetween( start, end ) );
  }

  /** Test would break at one milli b4 midnight, but slim chance of that */
  public void testIsTodaysDateBetweenOneMilliFromNowAndOneMilliFromNow()
  {
    Date start = new Date( new Date().getTime() - 1 );
    Date end = new Date( new Date().getTime() + 1 );
    assertTrue( DateUtils.isTodaysDateBetween( start, end ) );
  }

  /**
   * Test the isTodaysDateBetween method Is null being handled?
   */
  public void testIsTodaysDateBetweenTestForNull()
  {
    Calendar cal = new GregorianCalendar();
    int year = cal.get( Calendar.YEAR ); // always use current year
    Calendar date1 = new GregorianCalendar( year, Calendar.JANUARY, 1 );
    Date start = date1.getTime();
    Calendar date2 = new GregorianCalendar( year, Calendar.DECEMBER, 31 );
    Date end = date2.getTime();
    // Test2A: Assert true when start date is null (method will default to today's date)
    assertEquals( true, DateUtils.isTodaysDateBetween( null, end ) );
    // Test2B: Assert true when end date is null (method will default to today's date)
    assertEquals( true, DateUtils.isTodaysDateBetween( start, null ) );
  }

  /**
   * Test the isTodaysDateBetween method Is the time portion of the dates being ignored?
   */
  public void testIsTodaysDateBetweenTestTimeIgnored()
  {
    Date todayAtAnEarlierTime = null;
    try
    {
      SimpleDateFormat sdf = new SimpleDateFormat( "MM/dd/yyyy:HH:mm:SS" );
      Locale locale = Locale.US;
      String todayAsDate = DateUtils.toDisplayString( DateUtils.getCurrentDate(), locale );
      todayAtAnEarlierTime = sdf.parse( todayAsDate + ":05:09:45" );
      System.out.println( "start:" + sdf.format( todayAtAnEarlierTime ) );
      Date now = DateUtils.getCurrentDate();
      System.out.println( "end:" + sdf.format( now ) );
      // Test3: Assert true when the time portion of the current date is ignored
      assertEquals( true, DateUtils.isTodaysDateBetween( now, todayAtAnEarlierTime ) );
    }
    catch( ParseException e )
    {
      System.out.println( "String to java.util.Date parsing exception, string to convert:" + todayAtAnEarlierTime + " " + e );
    }
  }

  public void testGetFirstAndLastDayOfLastMonth()
  {
    // Test going to previous year.
    Locale locale = Locale.US;
    Date start = DateUtils.getFirstDayOfPreviousMonth( DateUtils.toDate( "01/01/2005", locale ) );
    Date end = DateUtils.getLastDayOfPreviousMonth( DateUtils.toDate( "01/01/2005", locale ) );
    assertEquals( "Start Date not what expected", DateUtils.toDisplayString( start, locale ), "12/01/2004" );
    assertEquals( "End Date not what expected", DateUtils.toDisplayString( end, locale ), "12/31/2004" );

    // Test month with less than 31 day.
    Date start1 = DateUtils.getFirstDayOfPreviousMonth( DateUtils.toDate( "12/01/2005", locale ) );
    Date end1 = DateUtils.getLastDayOfPreviousMonth( DateUtils.toDate( "12/01/2005", locale ) );
    assertEquals( "Start Date not what expected", DateUtils.toDisplayString( start1, locale ), "11/01/2005" );
    assertEquals( "End Date not what expected", DateUtils.toDisplayString( end1, locale ), "11/30/2005" );

    // Test a leep year.
    Date start2 = DateUtils.getFirstDayOfPreviousMonth( DateUtils.toDate( "03/01/2004", locale ) );
    Date end2 = DateUtils.getLastDayOfPreviousMonth( DateUtils.toDate( "03/01/2004", locale ) );
    assertEquals( "Start Date not what expected", DateUtils.toDisplayString( start2, locale ), "02/01/2004" );
    assertEquals( "End Date not what expected", DateUtils.toDisplayString( end2, locale ), "02/29/2004" );
  }

  public void testGetFirstDayOfMonth()
  {
    Locale locale = Locale.US;
    Date start = DateUtils.getFirstDayOfMonth( DateUtils.toDate( "05/29/2014", locale ) );
    assertEquals( "Start Date not what expected", DateUtils.toDisplayString( start, locale ), "05/01/2014" );
  }

  public void testGetLastDayOfMonth()
  {
    Locale locale = Locale.US;
    Date start = DateUtils.getLastDayOfMonth( DateUtils.toDate( "05/29/2014", locale ) );
    assertEquals( "Start Date not what expected", DateUtils.toDisplayString( start, locale ), "05/31/2014" );
  }

  public void testGetRollingPeriodStartDate()
  {
    Locale locale = Locale.US;
    Date start = DateUtils.getRollingPeriodStartDate( 2, 2010, "month", "next" );
    assertEquals( "Start Date not what expected", DateUtils.toDisplayString( start, locale ), "04/01/2010" );

    start = DateUtils.getRollingPeriodStartDate( 2, 2010, "month", "prev" );
    assertEquals( "Start Date not what expected", DateUtils.toDisplayString( start, locale ), "02/01/2010" );

    start = DateUtils.getRollingPeriodStartDate( 2, 2010, "quarter", "next" );
    assertEquals( "Start Date not what expected", DateUtils.toDisplayString( start, locale ), "04/01/2010" );

    start = DateUtils.getRollingPeriodStartDate( 2, 2010, "quarter", "prev" );
    assertEquals( "Start Date not what expected", DateUtils.toDisplayString( start, locale ), "10/01/2009" );

    start = DateUtils.getRollingPeriodStartDate( 2, 2010, "year", "next" );
    assertEquals( "Start Date not what expected", DateUtils.toDisplayString( start, locale ), "04/01/2010" );

    start = DateUtils.getRollingPeriodStartDate( 2, 2010, "year", "prev" );
    assertEquals( "Start Date not what expected", DateUtils.toDisplayString( start, locale ), "04/01/2008" );

    start = DateUtils.getRollingPeriodStartDate( 2, 2010, "month", null );
    assertEquals( "Start Date not what expected", DateUtils.toDisplayString( start, locale ), "03/01/2010" );

    start = DateUtils.getRollingPeriodStartDate( 2, 2010, "quarter", null );
    assertEquals( "Start Date not what expected", DateUtils.toDisplayString( start, locale ), "01/01/2010" );

    start = DateUtils.getRollingPeriodStartDate( 2, 2010, "year", null );
    assertEquals( "Start Date not what expected", DateUtils.toDisplayString( start, locale ), "04/01/2009" );
  }

  public void testGetRollingPeriodEndDate()
  {
    Locale locale = Locale.US;
    Date end = DateUtils.getRollingPeriodEndDate( 2, 2010, "month", "next" );
    assertEquals( "End Date not what expected", DateUtils.toDisplayString( end, locale ), "04/30/2010" );

    end = DateUtils.getRollingPeriodEndDate( 2, 2010, "month", "prev" );
    assertEquals( "End Date not what expected", DateUtils.toDisplayString( end, locale ), "02/28/2010" );

    end = DateUtils.getRollingPeriodEndDate( 2, 2010, "quarter", "next" );
    assertEquals( "End Date not what expected", DateUtils.toDisplayString( end, locale ), "06/30/2010" );

    end = DateUtils.getRollingPeriodEndDate( 2, 2010, "quarter", "prev" );
    assertEquals( "End Date not what expected", DateUtils.toDisplayString( end, locale ), "12/31/2009" );

    end = DateUtils.getRollingPeriodEndDate( 2, 2010, "year", "next" );
    assertEquals( "End Date not what expected", DateUtils.toDisplayString( end, locale ), "03/31/2011" );

    end = DateUtils.getRollingPeriodEndDate( 2, 2010, "year", "prev" );
    assertEquals( "End Date not what expected", DateUtils.toDisplayString( end, locale ), "03/31/2009" );

    end = DateUtils.getRollingPeriodEndDate( 2, 2010, "month", null );
    assertEquals( "End Date not what expected", DateUtils.toDisplayString( end, locale ), "03/31/2010" );

    end = DateUtils.getRollingPeriodEndDate( 2, 2010, "quarter", null );
    assertEquals( "End Date not what expected", DateUtils.toDisplayString( end, locale ), "03/31/2010" );

    end = DateUtils.getRollingPeriodEndDate( 2, 2010, "year", null );
    assertEquals( "End Date not what expected", DateUtils.toDisplayString( end, locale ), "03/31/2010" );

  }

  @SuppressWarnings( "deprecation" )
  public void testbeginigDateOfWeek() throws ParseException
  {
    final String currentDate = "01/05/2017";
    final DateFormat dateFormatter = new SimpleDateFormat( "MM/dd/yyyy" );
    final Date date = dateFormatter.parse( currentDate );

    // the first day of the week containing 01/05/2017 was the Sunday the 1st
    assertTrue( DateUtils.startDateOfWeek( date, TimeZoneId.CST ).getDate() == 1 );
  }

  @SuppressWarnings( "deprecation" )
  public void testendDateOfWeek() throws ParseException
  {
    final String currentDate = "01/05/2017";
    final DateFormat dateFormatter = new SimpleDateFormat( "MM/dd/yyyy" );
    final Date date = dateFormatter.parse( currentDate );

    // the last day of the week containing 01/05/2017 was Saturday the 7th
    assertTrue( DateUtils.endDateOfWeek( date, TimeZoneId.CST ).getDate() == 7 );
  }

  public void testGetPreviousDateyyyy_MM_dd()
  {
    assertTrue( DateUtils.getPreviousDayStringyyyy_MM_dd().equals( DateUtils.getDateStringyyyy_MM_dd( new Date( new Date().getTime() - ( 1000 * 60 * 60 * 24 ) ) ) ) );
  }

  public void testGetDateFromStringyyyy_MM_dd()
  {
    assertTrue( DateUtils.getDateFromStringyyyy_MM_dd( "2000-01-01" ) != null );
  }
}

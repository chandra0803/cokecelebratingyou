/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javatest/com/biperf/core/utils/DateFormatterUtilTest.java,v $
 */

package com.biperf.core.utils;

import java.util.Locale;

import junit.framework.TestCase;

public class DateFormatterUtilTest extends TestCase
{
  private static final Locale US_LOCALE = Locale.US;
  private static final Locale GB_LOCALE = new Locale( "en", "gb" );

  public void testGetDatePatternUs()
  {
    String datePattern = DateFormatterUtil.getDatePattern( US_LOCALE );
    assertEquals( "MM/dd/yyyy", datePattern );
  }

  public void testGetDatePatternGb()
  {
    String datePattern = DateFormatterUtil.getDatePattern( GB_LOCALE );
    assertEquals( "dd/MM/yyyy", datePattern );
  }

  public void testGetDateTimeSecTZPatternUs()
  {
    String datePattern = DateFormatterUtil.getDateTimeSecTZPattern( US_LOCALE );
    assertEquals( "MM/dd/yyyy, HH:mm:ss z", datePattern );
  }

  public void testGetDateTimeSecTZPatternGb()
  {
    String datePattern = DateFormatterUtil.getDateTimeSecTZPattern( GB_LOCALE );
    assertEquals( "dd/MM/yyyy, HH:mm:ss z", datePattern );
  }

  public void testGetDateTimeMerTZPatternUs()
  {
    String datePattern = DateFormatterUtil.getDateTimeMerTZPattern( US_LOCALE );
    assertEquals( "MM/dd/yyyy hh:mm a z", datePattern );
  }

  public void testGetDateTimeMerTZPatternGb()
  {
    String datePattern = DateFormatterUtil.getDateTimeMerTZPattern( GB_LOCALE );
    assertEquals( "dd/MM/yyyy hh:mm a z", datePattern );
  }

}

/*
 * (c) 2006 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javatest/com/biperf/core/dao/budget/limits/DateCharacteristicConstraintLimitsTest.java,v $
 */

package com.biperf.core.dao.budget.limits;

import java.util.Date;
import java.util.Locale;

import com.biperf.core.domain.user.UserCharacteristic;
import com.biperf.core.utils.DateUtils;

import junit.framework.TestCase;

/**
 * DateCharacteristicConstraintLimitsTest.
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
 * <td>Todd</td>
 * <td>Aug 14, 2006</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class DateCharacteristicConstraintLimitsTest extends TestCase
{

  /**
   * Test the inConstraints method of the DateCharacteristicConstraintLimits
   */
  public void testDateCharacteristicConstraintLimits()
  {
    DateCharacteristicConstraintLimits dateCharacteristicConstrantLimits = new DateCharacteristicConstraintLimits();
    Date date1 = DateUtils.toDate( "08/15/2006", Locale.US );
    Date date2 = DateUtils.toDate( "08/17/2006", Locale.US );
    dateCharacteristicConstrantLimits.setMinDateValue( date1 );
    dateCharacteristicConstrantLimits.setMaxDateValue( date2 );
    UserCharacteristic userCharacteristic = new UserCharacteristic();
    userCharacteristic.setCharacteristicValue( "08/16/2006" );
    assertTrue( dateCharacteristicConstrantLimits.inConstraints( userCharacteristic ) );
    userCharacteristic.setCharacteristicValue( "08/14/2006" );
    assertFalse( dateCharacteristicConstrantLimits.inConstraints( userCharacteristic ) );
  }

  /**
   * Test the inConstraints method of the DateCharacteristicConstraintLimits with a null limit
   */
  public void testDateCharacteristicConstraintLimitsWithNull()
  {
    DateCharacteristicConstraintLimits dateCharacteristicConstrantLimits = new DateCharacteristicConstraintLimits();
    Date date1 = DateUtils.toDate( "08/15/2006", Locale.US );
    Date date2 = DateUtils.toDate( "08/17/2006", Locale.US );
    dateCharacteristicConstrantLimits.setMinDateValue( null );
    dateCharacteristicConstrantLimits.setMaxDateValue( date2 );
    UserCharacteristic userCharacteristic = new UserCharacteristic();
    userCharacteristic.setCharacteristicValue( "08/16/2006" );
    assertTrue( dateCharacteristicConstrantLimits.inConstraints( userCharacteristic ) );
    userCharacteristic.setCharacteristicValue( "08/18/2006" );
    assertFalse( dateCharacteristicConstrantLimits.inConstraints( userCharacteristic ) );
    dateCharacteristicConstrantLimits.setMinDateValue( date1 );
    dateCharacteristicConstrantLimits.setMaxDateValue( null );
    userCharacteristic.setCharacteristicValue( "08/16/2006" );
    assertTrue( dateCharacteristicConstrantLimits.inConstraints( userCharacteristic ) );
    userCharacteristic.setCharacteristicValue( "08/14/2006" );
    assertFalse( dateCharacteristicConstrantLimits.inConstraints( userCharacteristic ) );
  }

}

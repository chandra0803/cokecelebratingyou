/*
 * (c) 2006 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javatest/com/biperf/core/dao/budget/limits/IntegerCharacteristicConstraintLimitsTest.java,v $
 */

package com.biperf.core.dao.budget.limits;

import com.biperf.core.domain.user.UserCharacteristic;

import junit.framework.TestCase;

/**
 * IntegerCharacteristicConstraintLimitsTest.
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
public class IntegerCharacteristicConstraintLimitsTest extends TestCase
{

  /**
   * Test the inConstraints method of the IntegerCharacteristicConstraintLimits
   */
  public void testIntegerCharacteristicConstraintLimits()
  {
    IntegerCharacteristicConstraintLimits integerCharacteristicConstrantLimits = new IntegerCharacteristicConstraintLimits();
    integerCharacteristicConstrantLimits.setMinIntegerValue( new Integer( 5 ) );
    integerCharacteristicConstrantLimits.setMaxIntegerValue( new Integer( 7 ) );
    UserCharacteristic userCharacteristic = new UserCharacteristic();
    userCharacteristic.setCharacteristicValue( "6" );
    assertTrue( integerCharacteristicConstrantLimits.inConstraints( userCharacteristic ) );
    userCharacteristic.setCharacteristicValue( "8" );
    assertFalse( integerCharacteristicConstrantLimits.inConstraints( userCharacteristic ) );
  }

  /**
   * Test the inConstraints method of the IntegerCharacteristicConstraintLimits with a null limit
   */
  public void testIntegerCharacteristicConstraintLimitsWithNull()
  {
    IntegerCharacteristicConstraintLimits integerCharacteristicConstrantLimits = new IntegerCharacteristicConstraintLimits();
    integerCharacteristicConstrantLimits.setMinIntegerValue( null );
    integerCharacteristicConstrantLimits.setMaxIntegerValue( new Integer( "6" ) );
    UserCharacteristic userCharacteristic = new UserCharacteristic();
    userCharacteristic.setCharacteristicValue( "5" );
    assertTrue( integerCharacteristicConstrantLimits.inConstraints( userCharacteristic ) );
    userCharacteristic.setCharacteristicValue( "7" );
    assertFalse( integerCharacteristicConstrantLimits.inConstraints( userCharacteristic ) );
    integerCharacteristicConstrantLimits.setMinIntegerValue( new Integer( "5" ) );
    integerCharacteristicConstrantLimits.setMaxIntegerValue( null );
    userCharacteristic.setCharacteristicValue( "6" );
    assertTrue( integerCharacteristicConstrantLimits.inConstraints( userCharacteristic ) );
    userCharacteristic.setCharacteristicValue( "4" );
    assertFalse( integerCharacteristicConstrantLimits.inConstraints( userCharacteristic ) );
  }

}

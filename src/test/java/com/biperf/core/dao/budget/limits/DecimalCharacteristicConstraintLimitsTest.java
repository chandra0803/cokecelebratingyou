/*
 * (c) 2006 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javatest/com/biperf/core/dao/budget/limits/DecimalCharacteristicConstraintLimitsTest.java,v $
 */

package com.biperf.core.dao.budget.limits;

import java.math.BigDecimal;

import com.biperf.core.domain.user.UserCharacteristic;

import junit.framework.TestCase;

/**
 * DecimalCharacteristicConstraintLimitsTest.
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
public class DecimalCharacteristicConstraintLimitsTest extends TestCase
{

  /**
   * Test the inConstraints method of the DecimalCharacteristicConstraintLimits
   */
  public void testDecimalCharacteristicConstraintLimits()
  {
    DecimalCharacteristicConstraintLimits decimalCharacteristicConstrantLimits = new DecimalCharacteristicConstraintLimits();
    decimalCharacteristicConstrantLimits.setMinDecimalValue( new BigDecimal( "23.0" ) );
    decimalCharacteristicConstrantLimits.setMaxDecimalValue( new BigDecimal( "23.5" ) );
    UserCharacteristic userCharacteristic = new UserCharacteristic();
    userCharacteristic.setCharacteristicValue( "23.1" );
    assertTrue( decimalCharacteristicConstrantLimits.inConstraints( userCharacteristic ) );
    userCharacteristic.setCharacteristicValue( "22.9" );
    assertFalse( decimalCharacteristicConstrantLimits.inConstraints( userCharacteristic ) );
  }

  /**
   * Test the inConstraints method of the DecimalCharacteristicConstraintLimits with a null limit
   */
  public void testDecimalCharacteristicConstraintLimitsWithNull()
  {
    DecimalCharacteristicConstraintLimits decimalCharacteristicConstrantLimits = new DecimalCharacteristicConstraintLimits();
    decimalCharacteristicConstrantLimits.setMinDecimalValue( null );
    decimalCharacteristicConstrantLimits.setMaxDecimalValue( new BigDecimal( "23.5" ) );
    UserCharacteristic userCharacteristic = new UserCharacteristic();
    userCharacteristic.setCharacteristicValue( "23.1" );
    assertTrue( decimalCharacteristicConstrantLimits.inConstraints( userCharacteristic ) );
    userCharacteristic.setCharacteristicValue( "23.6" );
    assertFalse( decimalCharacteristicConstrantLimits.inConstraints( userCharacteristic ) );
    decimalCharacteristicConstrantLimits.setMinDecimalValue( new BigDecimal( "23.0" ) );
    decimalCharacteristicConstrantLimits.setMaxDecimalValue( null );
    userCharacteristic.setCharacteristicValue( "23.1" );
    assertTrue( decimalCharacteristicConstrantLimits.inConstraints( userCharacteristic ) );
    userCharacteristic.setCharacteristicValue( "22.9" );
    assertFalse( decimalCharacteristicConstrantLimits.inConstraints( userCharacteristic ) );
  }

}

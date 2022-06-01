/*
 * (c) 2006 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javatest/com/biperf/core/dao/budget/limits/BooleanCharacteristicConstraintLimitsTest.java,v $
 */

package com.biperf.core.dao.budget.limits;

import com.biperf.core.domain.user.UserCharacteristic;

import junit.framework.TestCase;

/**
 * BooleanCharacteristicConstraintLimitsTest.
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
public class BooleanCharacteristicConstraintLimitsTest extends TestCase
{

  /**
   * Test the inConstraints method of the BooleanCharacteristicConstraintLimits
   */
  public void testBooleanCharacteristicConstraintLimits()
  {
    BooleanCharacteristicConstraintLimits booleanCharacteristicConstrantLimits = new BooleanCharacteristicConstraintLimits();
    booleanCharacteristicConstrantLimits.setBooleanValue( Boolean.TRUE );
    UserCharacteristic userCharacteristic = new UserCharacteristic();
    userCharacteristic.setCharacteristicValue( "true" );
    assertTrue( booleanCharacteristicConstrantLimits.inConstraints( userCharacteristic ) );
    userCharacteristic.setCharacteristicValue( "false" );
    assertFalse( booleanCharacteristicConstrantLimits.inConstraints( userCharacteristic ) );

  }

}

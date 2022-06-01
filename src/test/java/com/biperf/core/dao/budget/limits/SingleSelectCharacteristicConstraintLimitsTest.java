/*
 * (c) 2006 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javatest/com/biperf/core/dao/budget/limits/SingleSelectCharacteristicConstraintLimitsTest.java,v $
 */

package com.biperf.core.dao.budget.limits;

import com.biperf.core.domain.user.UserCharacteristic;

import junit.framework.TestCase;

/**
 * SingleSelectCharacteristicConstraintLimitsTest.
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
public class SingleSelectCharacteristicConstraintLimitsTest extends TestCase
{

  /**
   * Test the inConstraints method of the SingleSelectCharacteristicConstraintLimits
   */
  public void testSingleSelectCharacteristicConstraintLimits()
  {
    SingleSelectCharacteristicConstraintLimits singleSelectCharacteristicConstrantLimits = new SingleSelectCharacteristicConstraintLimits();
    singleSelectCharacteristicConstrantLimits.setValuesAllowed( new String[] { "aaa", "bbb" } );
    UserCharacteristic userCharacteristic = new UserCharacteristic();
    userCharacteristic.setCharacteristicValue( "bbb" );
    assertTrue( singleSelectCharacteristicConstrantLimits.inConstraints( userCharacteristic ) );
    userCharacteristic.setCharacteristicValue( "ccc" );
    assertFalse( singleSelectCharacteristicConstrantLimits.inConstraints( userCharacteristic ) );
  }

}

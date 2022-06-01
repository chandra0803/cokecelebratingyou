/*
 * (c) 2006 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javatest/com/biperf/core/dao/budget/limits/MultiSelectCharacteristicConstraintLimitsTest.java,v $
 */

package com.biperf.core.dao.budget.limits;

import com.biperf.core.domain.user.UserCharacteristic;

import junit.framework.TestCase;

/**
 * MultiSelectCharacteristicConstraintLimitsTest.
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
public class MultiSelectCharacteristicConstraintLimitsTest extends TestCase
{

  /**
   * Test the inConstraints method of the MultiSelectCharacteristicConstraintLimits using
   * a notAllOf constraint
   */
  public void testMultiSelectCharacteristicConstraintLimitsNotAllOf()
  {
    MultiSelectCharacteristicConstraintLimits multiSelectCharacteristicConstrantLimits = new MultiSelectCharacteristicConstraintLimits();
    multiSelectCharacteristicConstrantLimits.setValuesAllowed( new String[] { "aaa", "bbb" } );
    multiSelectCharacteristicConstrantLimits.setAllof( false );
    UserCharacteristic userCharacteristic = new UserCharacteristic();
    userCharacteristic.setCharacteristicValue( "aaa,bbb" );
    assertTrue( multiSelectCharacteristicConstrantLimits.inConstraints( userCharacteristic ) );
    userCharacteristic.setCharacteristicValue( "aaa" );
    assertTrue( multiSelectCharacteristicConstrantLimits.inConstraints( userCharacteristic ) );
    userCharacteristic.setCharacteristicValue( "ccc" );
    assertFalse( multiSelectCharacteristicConstrantLimits.inConstraints( userCharacteristic ) );
  }

  /**
   * Test the inConstraints method of the MultiSelectCharacteristicConstraintLimits using
   * a allOf constraint
   */
  public void testMultiSelectCharacteristicConstraintLimitsAllOf()
  {
    MultiSelectCharacteristicConstraintLimits multiSelectCharacteristicConstrantLimits = new MultiSelectCharacteristicConstraintLimits();
    multiSelectCharacteristicConstrantLimits.setValuesAllowed( new String[] { "aaa", "bbb" } );
    multiSelectCharacteristicConstrantLimits.setAllof( true );
    UserCharacteristic userCharacteristic = new UserCharacteristic();
    userCharacteristic.setCharacteristicValue( "aaa,bbb" );
    assertTrue( multiSelectCharacteristicConstrantLimits.inConstraints( userCharacteristic ) );
    userCharacteristic.setCharacteristicValue( "aaa" );
    assertFalse( multiSelectCharacteristicConstrantLimits.inConstraints( userCharacteristic ) );
    userCharacteristic.setCharacteristicValue( "ccc" );
    assertFalse( multiSelectCharacteristicConstrantLimits.inConstraints( userCharacteristic ) );
  }

}

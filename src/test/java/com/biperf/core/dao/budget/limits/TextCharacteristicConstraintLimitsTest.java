/*
 * (c) 2006 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javatest/com/biperf/core/dao/budget/limits/TextCharacteristicConstraintLimitsTest.java,v $
 */

package com.biperf.core.dao.budget.limits;

import com.biperf.core.domain.user.UserCharacteristic;

import junit.framework.TestCase;

/**
 * TextCharacteristicConstraintLimitsTest.
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
public class TextCharacteristicConstraintLimitsTest extends TestCase
{

  /**
   * Test the inConstraints method of the TextCharacteristicConstraintLimits
   */
  public void testTextCharacteristicConstraintLimitsContains()
  {
    TextCharacteristicConstraintLimits textCharacteristicConstrantLimits = new TextCharacteristicConstraintLimits();
    textCharacteristicConstrantLimits.setTextValue( "ABC" );
    textCharacteristicConstrantLimits.setType( TextCharacteristicConstraintLimits.TYPE_CONTAINS );
    UserCharacteristic userCharacteristic = new UserCharacteristic();
    userCharacteristic.setCharacteristicValue( "xABCD" );
    assertTrue( textCharacteristicConstrantLimits.inConstraints( userCharacteristic ) );
    userCharacteristic.setCharacteristicValue( "AAA" );
    assertFalse( textCharacteristicConstrantLimits.inConstraints( userCharacteristic ) );
  }

  /**
   * Test the inConstraints method of the TextCharacteristicConstraintLimits
   */
  public void testTextCharacteristicConstraintLimitsDoesNotContain()
  {
    TextCharacteristicConstraintLimits textCharacteristicConstrantLimits = new TextCharacteristicConstraintLimits();
    textCharacteristicConstrantLimits.setTextValue( "ABC" );
    textCharacteristicConstrantLimits.setType( TextCharacteristicConstraintLimits.TYPE_DOES_NOT_CONTAIN );
    UserCharacteristic userCharacteristic = new UserCharacteristic();
    userCharacteristic.setCharacteristicValue( "xABCD" );
    assertFalse( textCharacteristicConstrantLimits.inConstraints( userCharacteristic ) );
    userCharacteristic.setCharacteristicValue( "AAA" );
    assertTrue( textCharacteristicConstrantLimits.inConstraints( userCharacteristic ) );
  }

  /**
   * Test the inConstraints method of the TextCharacteristicConstraintLimits
   */
  public void testTextCharacteristicConstraintLimitsStartsWith()
  {
    TextCharacteristicConstraintLimits textCharacteristicConstrantLimits = new TextCharacteristicConstraintLimits();
    textCharacteristicConstrantLimits.setTextValue( "ABC" );
    textCharacteristicConstrantLimits.setType( TextCharacteristicConstraintLimits.TYPE_STARTS_WITH );
    UserCharacteristic userCharacteristic = new UserCharacteristic();
    userCharacteristic.setCharacteristicValue( "ABCD" );
    assertTrue( textCharacteristicConstrantLimits.inConstraints( userCharacteristic ) );
    userCharacteristic.setCharacteristicValue( "xABCD" );
    assertFalse( textCharacteristicConstrantLimits.inConstraints( userCharacteristic ) );
  }

  /**
   * Test the inConstraints method of the TextCharacteristicConstraintLimits
   */
  public void testTextCharacteristicConstraintLimitsDoesNotStartWith()
  {
    TextCharacteristicConstraintLimits textCharacteristicConstrantLimits = new TextCharacteristicConstraintLimits();
    textCharacteristicConstrantLimits.setTextValue( "ABC" );
    textCharacteristicConstrantLimits.setType( TextCharacteristicConstraintLimits.TYPE_DOES_NOT_START_WITH );
    UserCharacteristic userCharacteristic = new UserCharacteristic();
    userCharacteristic.setCharacteristicValue( "ABCD" );
    assertFalse( textCharacteristicConstrantLimits.inConstraints( userCharacteristic ) );
    userCharacteristic.setCharacteristicValue( "xABCD" );
    assertTrue( textCharacteristicConstrantLimits.inConstraints( userCharacteristic ) );
  }

  /**
   * Test the inConstraints method of the TextCharacteristicConstraintLimits
   */
  public void testTextCharacteristicConstraintLimitsEndsWith()
  {
    TextCharacteristicConstraintLimits textCharacteristicConstrantLimits = new TextCharacteristicConstraintLimits();
    textCharacteristicConstrantLimits.setTextValue( "ABC" );
    textCharacteristicConstrantLimits.setType( TextCharacteristicConstraintLimits.TYPE_ENDS_WITH );
    UserCharacteristic userCharacteristic = new UserCharacteristic();
    userCharacteristic.setCharacteristicValue( "xABc" );
    assertTrue( textCharacteristicConstrantLimits.inConstraints( userCharacteristic ) );
    userCharacteristic.setCharacteristicValue( "xABCD" );
    assertFalse( textCharacteristicConstrantLimits.inConstraints( userCharacteristic ) );
  }

  /**
   * Test the inConstraints method of the TextCharacteristicConstraintLimits
   */
  public void testTextCharacteristicConstraintLimitsDoesNotEndWith()
  {
    TextCharacteristicConstraintLimits textCharacteristicConstrantLimits = new TextCharacteristicConstraintLimits();
    textCharacteristicConstrantLimits.setTextValue( "ABC" );
    textCharacteristicConstrantLimits.setType( TextCharacteristicConstraintLimits.TYPE_DOES_NOT_END_WITH );
    UserCharacteristic userCharacteristic = new UserCharacteristic();
    userCharacteristic.setCharacteristicValue( "xABc" );
    assertFalse( textCharacteristicConstrantLimits.inConstraints( userCharacteristic ) );
    userCharacteristic.setCharacteristicValue( "xABCD" );
    assertTrue( textCharacteristicConstrantLimits.inConstraints( userCharacteristic ) );
  }

}

/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javatest/com/biperf/core/utils/BudgetUtilsTest.java,v $
 */

package com.biperf.core.utils;

import java.math.BigDecimal;

import junit.framework.TestCase;

public class BudgetUtilsTest extends TestCase
{

  public void testApplyMediaConversion()
  {
    BigDecimal convertedValue = BudgetUtils.applyMediaConversion( BigDecimal.valueOf( 500 ), BigDecimal.valueOf( .28 ), BigDecimal.valueOf( .42 ) );
    assertTrue( 333.33333333 <= convertedValue.doubleValue() );
    assertTrue( 333.33333334 >= convertedValue.doubleValue() );

    convertedValue = BudgetUtils.applyMediaConversion( BigDecimal.valueOf( 333.33333333 ), BigDecimal.valueOf( .42 ), BigDecimal.valueOf( .28 ) );
    assertTrue( 499.99999999 <= convertedValue.doubleValue() );
    assertTrue( 500.00000000 >= convertedValue.doubleValue() );

    convertedValue = BudgetUtils.applyMediaConversion( BigDecimal.valueOf( 33333333.33333333 ), BigDecimal.valueOf( .42 ), BigDecimal.valueOf( .28 ) );
    assertTrue( 49999999.99999999 <= convertedValue.doubleValue() );
    assertTrue( 50000000.00000000 >= convertedValue.doubleValue() );

    convertedValue = BudgetUtils.applyMediaConversion( BigDecimal.valueOf( 1000 ), BigDecimal.valueOf( .28 ), BigDecimal.valueOf( .2 ) );
    assertEquals( 1400d, convertedValue.doubleValue() );

    convertedValue = BudgetUtils.applyMediaConversion( BigDecimal.valueOf( 850 ), BigDecimal.valueOf( .28 ), BigDecimal.valueOf( .42 ) );
    assertTrue( 566.66666666 <= convertedValue.doubleValue() );
    assertTrue( 566.66666667 >= convertedValue.doubleValue() );

    assertEquals( BigDecimal.valueOf( 850 ), BudgetUtils.applyMediaConversion( BigDecimal.valueOf( 850 ), null, BigDecimal.valueOf( .42 ) ) );
    assertEquals( BigDecimal.valueOf( 850 ), BudgetUtils.applyMediaConversion( BigDecimal.valueOf( 850 ), BigDecimal.valueOf( .42 ), null ) );
  }

  public void testApplyMediaConversionRatio()
  {
    BigDecimal convertedValue = BudgetUtils.applyMediaConversion( BigDecimal.valueOf( 500 ), BigDecimal.valueOf( .66666666667 ) );
    assertTrue( 333.33333333 <= convertedValue.doubleValue() );
    assertTrue( 333.33333334 >= convertedValue.doubleValue() );

    convertedValue = BudgetUtils.applyMediaConversion( BigDecimal.valueOf( 333.33333333 ), BigDecimal.valueOf( 1.5 ) );
    assertTrue( 499.99999999 <= convertedValue.doubleValue() );
    assertTrue( 500.00000000 >= convertedValue.doubleValue() );

    convertedValue = BudgetUtils.applyMediaConversion( BigDecimal.valueOf( 33333333.33333333 ), BigDecimal.valueOf( 1.5 ) );
    assertTrue( 49999999.99999999 <= convertedValue.doubleValue() );
    assertTrue( 50000000.00000000 >= convertedValue.doubleValue() );

    convertedValue = BudgetUtils.applyMediaConversion( BigDecimal.valueOf( 1000 ), BigDecimal.valueOf( 1.4 ) );
    assertEquals( 1400d, convertedValue.doubleValue() );

    convertedValue = BudgetUtils.applyMediaConversion( BigDecimal.valueOf( 850 ), BigDecimal.valueOf( .66666666667 ) );
    assertTrue( 566.66666666 <= convertedValue.doubleValue() );
    assertTrue( 566.66666667 >= convertedValue.doubleValue() );

    assertEquals( BigDecimal.valueOf( 850 ), BudgetUtils.applyMediaConversion( BigDecimal.valueOf( 850 ), null ) );
    assertEquals( BigDecimal.valueOf( 850 ), BudgetUtils.applyMediaConversion( BigDecimal.valueOf( 850 ), BigDecimal.ONE ) );
  }

  public void testCalculateConversionRatio()
  {
    assertEquals( BigDecimal.valueOf( 1 ), BudgetUtils.calculateConversionRatio( BigDecimal.valueOf( .28 ), BigDecimal.valueOf( .28 ) ) );

    assertTrue( .66666667 >= BudgetUtils.calculateConversionRatio( BigDecimal.valueOf( .28 ), BigDecimal.valueOf( .42 ) ).doubleValue() );
    assertTrue( .66666666 <= BudgetUtils.calculateConversionRatio( BigDecimal.valueOf( .28 ), BigDecimal.valueOf( .42 ) ).doubleValue() );

    assertEquals( BigDecimal.valueOf( 1.4 ), BudgetUtils.calculateConversionRatio( BigDecimal.valueOf( .28 ), BigDecimal.valueOf( .2 ) ) );

    assertTrue( 2.54545455 >= BudgetUtils.calculateConversionRatio( BigDecimal.valueOf( .28 ), BigDecimal.valueOf( .11 ) ).doubleValue() );
    assertTrue( 2.54545454 <= BudgetUtils.calculateConversionRatio( BigDecimal.valueOf( .28 ), BigDecimal.valueOf( .11 ) ).doubleValue() );

    assertTrue( 1.86666667 >= BudgetUtils.calculateConversionRatio( BigDecimal.valueOf( .28 ), BigDecimal.valueOf( .15 ) ).doubleValue() );
    assertTrue( 1.86666666 <= BudgetUtils.calculateConversionRatio( BigDecimal.valueOf( .28 ), BigDecimal.valueOf( .15 ) ).doubleValue() );

    assertTrue( 1.16666667 >= BudgetUtils.calculateConversionRatio( BigDecimal.valueOf( .28 ), BigDecimal.valueOf( .24 ) ).doubleValue() );
    assertTrue( 1.16666666 <= BudgetUtils.calculateConversionRatio( BigDecimal.valueOf( .28 ), BigDecimal.valueOf( .24 ) ).doubleValue() );

    assertEquals( BigDecimal.valueOf( 1 ), BudgetUtils.calculateConversionRatio( null, BigDecimal.valueOf( .24 ) ) );
    assertEquals( BigDecimal.valueOf( 1 ), BudgetUtils.calculateConversionRatio( BigDecimal.valueOf( .24 ), null ) );
    assertEquals( BigDecimal.valueOf( 1 ), BudgetUtils.calculateConversionRatio( null, null ) );
  }

  public void testGetBudgetDisplayValueRoundDown()
  {
    assertEquals( 0, BudgetUtils.getBudgetDisplayValue( BigDecimal.valueOf( 0 ) ) );
    assertEquals( 1, BudgetUtils.getBudgetDisplayValue( BigDecimal.valueOf( 1 ) ) );
    assertEquals( 1, BudgetUtils.getBudgetDisplayValue( BigDecimal.valueOf( 1.1 ) ) );
    assertEquals( 1, BudgetUtils.getBudgetDisplayValue( BigDecimal.valueOf( 1.12 ) ) );
    assertEquals( 1, BudgetUtils.getBudgetDisplayValue( BigDecimal.valueOf( 1.45 ) ) );
    assertEquals( 1, BudgetUtils.getBudgetDisplayValue( BigDecimal.valueOf( 1.499999 ) ) );

    assertEquals( 0, BudgetUtils.getBudgetDisplayValue( BigDecimal.valueOf( .49999999999999 ) ) );
    assertEquals( 0, BudgetUtils.getBudgetDisplayValue( BigDecimal.valueOf( .000001 ) ) );

    assertEquals( 15, BudgetUtils.getBudgetDisplayValue( BigDecimal.valueOf( 15.49999 ) ) );
    assertEquals( 15, BudgetUtils.getBudgetDisplayValue( BigDecimal.valueOf( 15 ) ) );
  }

  public void testGetBudgetDisplayValueRoundUp()
  {
    assertEquals( 2, BudgetUtils.getBudgetDisplayValue( BigDecimal.valueOf( 1.5 ) ) );
    assertEquals( 2, BudgetUtils.getBudgetDisplayValue( BigDecimal.valueOf( 1.6 ) ) );
    assertEquals( 2, BudgetUtils.getBudgetDisplayValue( BigDecimal.valueOf( 1.500000001 ) ) );
    assertEquals( 2, BudgetUtils.getBudgetDisplayValue( BigDecimal.valueOf( 1.55 ) ) );

    assertEquals( 1, BudgetUtils.getBudgetDisplayValue( BigDecimal.valueOf( .5 ) ) );
    assertEquals( 1, BudgetUtils.getBudgetDisplayValue( BigDecimal.valueOf( .99999 ) ) );

    assertEquals( 16, BudgetUtils.getBudgetDisplayValue( BigDecimal.valueOf( 15.5 ) ) );
    assertEquals( 1566, BudgetUtils.getBudgetDisplayValue( BigDecimal.valueOf( 1565.5 ) ) );
  }

  public void testGetBudgetDisplayValueNegativeRoundDown()
  {
    assertEquals( -2, BudgetUtils.getBudgetDisplayValue( BigDecimal.valueOf( -1.5 ) ) );
    assertEquals( -2, BudgetUtils.getBudgetDisplayValue( BigDecimal.valueOf( -1.6 ) ) );
    assertEquals( -2, BudgetUtils.getBudgetDisplayValue( BigDecimal.valueOf( -1.500000001 ) ) );
    assertEquals( -2, BudgetUtils.getBudgetDisplayValue( BigDecimal.valueOf( -1.55 ) ) );

    assertEquals( -1, BudgetUtils.getBudgetDisplayValue( BigDecimal.valueOf( -.5 ) ) );
    assertEquals( -1, BudgetUtils.getBudgetDisplayValue( BigDecimal.valueOf( -.99999 ) ) );

    assertEquals( -16, BudgetUtils.getBudgetDisplayValue( BigDecimal.valueOf( -15.5 ) ) );
    assertEquals( -1566, BudgetUtils.getBudgetDisplayValue( BigDecimal.valueOf( -1565.5 ) ) );
  }

  public void testGetBudgetDisplayValueNegativeRoundUp()
  {
    assertEquals( 0, BudgetUtils.getBudgetDisplayValue( BigDecimal.valueOf( -0 ) ) );
    assertEquals( -1, BudgetUtils.getBudgetDisplayValue( BigDecimal.valueOf( -1 ) ) );
    assertEquals( -1, BudgetUtils.getBudgetDisplayValue( BigDecimal.valueOf( -1.1 ) ) );
    assertEquals( -1, BudgetUtils.getBudgetDisplayValue( BigDecimal.valueOf( -1.12 ) ) );
    assertEquals( -1, BudgetUtils.getBudgetDisplayValue( BigDecimal.valueOf( -1.45 ) ) );
    assertEquals( -1, BudgetUtils.getBudgetDisplayValue( BigDecimal.valueOf( -1.499999 ) ) );

    assertEquals( 0, BudgetUtils.getBudgetDisplayValue( BigDecimal.valueOf( -.49999999999999 ) ) );
    assertEquals( 0, BudgetUtils.getBudgetDisplayValue( BigDecimal.valueOf( -.000001 ) ) );

    assertEquals( -15, BudgetUtils.getBudgetDisplayValue( BigDecimal.valueOf( -15.49999 ) ) );
    assertEquals( -15, BudgetUtils.getBudgetDisplayValue( BigDecimal.valueOf( -15 ) ) );
  }

}

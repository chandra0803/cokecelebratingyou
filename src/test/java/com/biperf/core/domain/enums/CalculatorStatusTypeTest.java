/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javatest/com/biperf/core/domain/enums/CalculatorStatusTypeTest.java,v $
 */

package com.biperf.core.domain.enums;

import java.util.Collections;
import java.util.List;

/**
 * CalculatorStatusTypeTest.
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
 * <td>sedey</td>
 * <td>May 20, 2006</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class CalculatorStatusTypeTest extends BaseEnumTest
{

  /**
   * Test the ability to get the List
   */
  public void testGetPickList()
  {
    List calculatorStatusTypeList = CalculatorStatusType.getList();
    assertNotNull( calculatorStatusTypeList );
    assertNotSame( calculatorStatusTypeList, Collections.EMPTY_LIST );
    assertTrue( calculatorStatusTypeList.size() > 0 );
  }

  /**
   * Test the ability to lookup a single list item
   */
  public void testLookupPickListItem()
  {
    List calculatorStatusTypeList = CalculatorStatusType.getList();
    CalculatorStatusType calculatorStatusType = (CalculatorStatusType)calculatorStatusTypeList.get( 0 );
    CalculatorStatusType calculatorStatusType2 = CalculatorStatusType.lookup( calculatorStatusType.getCode() );
    assertEquals( calculatorStatusType, calculatorStatusType2 );
  }

  /**
   * Test to make sure the item name is not null - verify description asset is named right.
   */
  public void testPickListItem()
  {
    List calculatorStatusTypeList = CalculatorStatusType.getList();
    for ( int i = 0; i < calculatorStatusTypeList.size(); i++ )
    {
      CalculatorStatusType calculatorStatusType = (CalculatorStatusType)calculatorStatusTypeList.get( i );
      assertNotNull( calculatorStatusType );
      assertNotNull( calculatorStatusType.getName() );
    }
  }

} // end ClaimFormStatusTypeTest

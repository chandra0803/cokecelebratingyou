/*
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javatest/com/biperf/core/domain/enums/BudgetTypeTest.java,v $
 * (c) 2005 BI, Inc.  All rights reserved.
 */

package com.biperf.core.domain.enums;

import java.util.Collections;
import java.util.List;

/**
 * BudgetTypeTest.
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
 * <td>sharma</td>
 * <td>Jun 1, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class BudgetTypeTest extends BaseEnumTest
{
  /**
   * Test the ability to get the List
   */
  public void testGetPickList()
  {
    List budgetTypeList = BudgetType.getList();
    assertNotNull( budgetTypeList );
    assertNotSame( budgetTypeList, Collections.EMPTY_LIST );
    assertTrue( budgetTypeList.size() > 0 );
  }

  /**
   * Test the ability to lookup a single list item
   */
  public void testLookupPickListItem()
  {
    List budgetTypeList = BudgetType.getList();
    BudgetType budgetType = (BudgetType)budgetTypeList.get( 0 );
    BudgetType budgetType2 = BudgetType.lookup( budgetType.getCode() );
    assertEquals( budgetType, budgetType2 );
  }

  /**
   * Test to make sure the item name is not null - verify description asset is named right.
   */
  public void testPickListItem()
  {
    List budgetTypeList = BudgetType.getList();
    BudgetType budgetType = (BudgetType)budgetTypeList.get( 0 );
    assertNotNull( budgetType );
    assertNotNull( budgetType.getName() );
  }

  /**
   * Test to make sure the items are not null - verify description asset is named right.
   */
  public void testPickListItems()
  {
    List budgetTypeList = BudgetType.getList();
    for ( int i = 0; i < budgetTypeList.size(); i++ )
    {
      BudgetType budgetType = (BudgetType)budgetTypeList.get( i );
      assertNotNull( budgetType.getName() );
    }
  }
}

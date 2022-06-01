/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javatest/com/biperf/core/domain/enums/BudgetStatusTypeTest.java,v $
 */

package com.biperf.core.domain.enums;

import java.util.Collections;
import java.util.List;

/**
 * BudgetStatusTypeTest.
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
 * <td>May 24, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class BudgetStatusTypeTest extends BaseEnumTest
{

  /**
   * Test the ability to get the List
   */
  public void testGetPickList()
  {
    List budgetStatusTypeList = BudgetStatusType.getList();
    assertNotNull( budgetStatusTypeList );
    assertNotSame( budgetStatusTypeList, Collections.EMPTY_LIST );
    assertTrue( budgetStatusTypeList.size() > 0 );
  }

  /**
   * Test the ability to lookup a single list item
   */
  public void testLookupPickListItem()
  {
    List budgetStatusTypeList = BudgetStatusType.getList();
    BudgetStatusType budgetStatusType = (BudgetStatusType)budgetStatusTypeList.get( 0 );
    BudgetStatusType budgetStatusType2 = BudgetStatusType.lookup( budgetStatusType.getCode() );
    assertEquals( budgetStatusType, budgetStatusType2 );
  }

  /**
   * Test to make sure the item name is not null - verify description asset is named right.
   */
  public void testPickListItem()
  {
    List budgetStatusTypeList = BudgetStatusType.getList();
    BudgetStatusType budgetStatusType = (BudgetStatusType)budgetStatusTypeList.get( 0 );
    assertNotNull( budgetStatusType );
    assertNotNull( budgetStatusType.getName() );
  }

  /**
   * Test to make sure the items are not null - verify description asset is named right.
   */
  public void testPickListItems()
  {
    List budgetStatusTypeList = BudgetStatusType.getList();
    for ( int i = 0; i < budgetStatusTypeList.size(); i++ )
    {
      BudgetStatusType budgetStatusType = (BudgetStatusType)budgetStatusTypeList.get( i );
      assertNotNull( budgetStatusType.getName() );
    }
  }

}

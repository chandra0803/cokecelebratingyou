/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javatest/com/biperf/core/domain/enums/BudgetOverrideableTypeTest.java,v $
 */

package com.biperf.core.domain.enums;

import java.util.Collections;
import java.util.List;

/**
 * BudgetOverrideableTypeTest.
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
 * <td>May 23, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class BudgetOverrideableTypeTest extends BaseEnumTest
{

  /**
   * Test the ability to get the List
   */
  public void testGetPickList()
  {
    List budgetOverrideableTypeList = BudgetOverrideableType.getList();
    assertNotNull( budgetOverrideableTypeList );
    assertNotSame( budgetOverrideableTypeList, Collections.EMPTY_LIST );
    assertTrue( budgetOverrideableTypeList.size() > 0 );
  }

  /**
   * Test the ability to lookup a single list item
   */
  public void testLookupPickListItem()
  {
    List budgetOverrideableTypeList = BudgetOverrideableType.getList();
    BudgetOverrideableType budgetOverrideableType = (BudgetOverrideableType)budgetOverrideableTypeList.get( 0 );
    BudgetOverrideableType budgetOverrideableType2 = BudgetOverrideableType.lookup( budgetOverrideableType.getCode() );
    assertEquals( budgetOverrideableType, budgetOverrideableType2 );
  }

  /**
   * Test to make sure the item name is not null - verify description asset is named right.
   */
  public void testPickListItem()
  {
    List budgetOverrideableTypeList = BudgetOverrideableType.getList();
    BudgetOverrideableType budgetOverrideableType = (BudgetOverrideableType)budgetOverrideableTypeList.get( 0 );
    assertNotNull( budgetOverrideableType );
    assertNotNull( budgetOverrideableType.getName() );
  }

  /**
   * Test to make sure the items are not null - verify name asset is named right.
   */
  public void testPickListItems()
  {
    List budgetOverrideableTypeList = BudgetOverrideableType.getList();
    for ( int i = 0; i < budgetOverrideableTypeList.size(); i++ )
    {
      BudgetOverrideableType budgetOverrideableType = (BudgetOverrideableType)budgetOverrideableTypeList.get( i );
      assertNotNull( budgetOverrideableType.getName() );
    }
  }

}


package com.biperf.core.domain.enums;

import java.util.Collections;
import java.util.List;

/**
 * BudgetmasterStatusTypeTest.
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
 * <td>June 2, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class BudgetmasterStatusTypeTest extends BaseEnumTest
{

  /**
   * Test the ability to get the List
   */
  public void testGetPickList()
  {
    List budgetmasterStatusList = BudgetmasterStatusType.getList();
    assertNotNull( budgetmasterStatusList );
    assertNotSame( budgetmasterStatusList, Collections.EMPTY_LIST );
    assertTrue( budgetmasterStatusList.size() > 0 );
  }

  /**
   * Test the ability to lookup a single list item
   */
  public void testLookupPickListItem()
  {
    List budgetmasterStatusList = BudgetmasterStatusType.getList();
    BudgetmasterStatusType budgetmasterStatus = (BudgetmasterStatusType)budgetmasterStatusList.get( 0 );
    BudgetmasterStatusType budgetmasterStatus2 = BudgetmasterStatusType.lookup( budgetmasterStatus.getCode() );
    assertEquals( budgetmasterStatus, budgetmasterStatus2 );
  }

  /**
   * Test to make sure the item name is not null - verify description asset is named right.
   */
  public void testPickListItem()
  {
    List budgetmasterStatusList = BudgetmasterStatusType.getList();
    for ( int i = 0; i < budgetmasterStatusList.size(); i++ )
    {
      BudgetmasterStatusType budgetmasterStatus = (BudgetmasterStatusType)budgetmasterStatusList.get( i );
      assertNotNull( budgetmasterStatus );
      assertNotNull( budgetmasterStatus.getName() );
    }
  }

}
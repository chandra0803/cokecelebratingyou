/**
 * 
 */

package com.biperf.core.domain.enums;

import java.util.Collections;
import java.util.List;

/**
 * GoalQuestPaxActivityTypeTest.
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
 * <td>meadows</td>
 * <td>Jan 2, 2007</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class GoalQuestPaxActivityTypeTest extends BaseEnumTest
{

  /**
   * Test the ability to get the List
   */
  public void testGetPickList()
  {
    List goalQuestPaxActivityTypeList = GoalQuestPaxActivityType.getList();
    assertNotNull( goalQuestPaxActivityTypeList );
    assertNotSame( goalQuestPaxActivityTypeList, Collections.EMPTY_LIST );
    assertTrue( goalQuestPaxActivityTypeList.size() > 0 );
  }

  /**
   * Test the ability to lookup a single list item
   */
  public void testLookupPickListItem()
  {
    List goalQuestPaxActivityTypeList = GoalQuestPaxActivityType.getList();
    GoalQuestPaxActivityType goalQuestPaxActivityType1 = (GoalQuestPaxActivityType)goalQuestPaxActivityTypeList.get( 0 );
    GoalQuestPaxActivityType goalQuestPaxActivityType2 = GoalQuestPaxActivityType.lookup( goalQuestPaxActivityType1.getCode() );
    assertEquals( goalQuestPaxActivityType1, goalQuestPaxActivityType2 );
  }

  /**
   * Test to make sure the item name is not null - verify description asset is named right.
   */
  public void testPickListItem()
  {
    List goalQuestPaxActivityTypeList = GoalQuestPaxActivityType.getList();
    for ( int i = 0; i < goalQuestPaxActivityTypeList.size(); i++ )
    {
      GoalQuestPaxActivityType goalQuestPaxActivityType = (GoalQuestPaxActivityType)goalQuestPaxActivityTypeList.get( i );
      assertNotNull( goalQuestPaxActivityType );
      assertNotNull( goalQuestPaxActivityType.getName() );
    }
  }

}

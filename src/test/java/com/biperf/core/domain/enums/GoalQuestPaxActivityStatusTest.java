/**
 * 
 */

package com.biperf.core.domain.enums;

import java.util.Collections;
import java.util.List;

/**
 * GoalQuestPaxActivityStatusTest.
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
public class GoalQuestPaxActivityStatusTest extends BaseEnumTest
{

  /**
   * Test the ability to get the List
   */
  public void testGetPickList()
  {
    List goalQuestPaxActivityStatusList = GoalQuestPaxActivityStatus.getList();
    assertNotNull( goalQuestPaxActivityStatusList );
    assertNotSame( goalQuestPaxActivityStatusList, Collections.EMPTY_LIST );
    assertTrue( goalQuestPaxActivityStatusList.size() > 0 );
  }

  /**
   * Test the ability to lookup a single list item
   */
  public void testLookupPickListItem()
  {
    List goalQuestPaxActivityStatusList = GoalQuestPaxActivityStatus.getList();
    GoalQuestPaxActivityStatus goalQuestPaxActivityStatus1 = (GoalQuestPaxActivityStatus)goalQuestPaxActivityStatusList.get( 0 );
    GoalQuestPaxActivityStatus goalQuestPaxActivityStatus2 = GoalQuestPaxActivityStatus.lookup( goalQuestPaxActivityStatus1.getCode() );
    assertEquals( goalQuestPaxActivityStatus1, goalQuestPaxActivityStatus2 );
  }

  /**
   * Test to make sure the item name is not null - verify description asset is named right.
   */
  public void testPickListItem()
  {
    List goalQuestPaxActivityStatusList = GoalQuestPaxActivityStatus.getList();
    for ( int i = 0; i < goalQuestPaxActivityStatusList.size(); i++ )
    {
      GoalQuestPaxActivityStatus goalQuestPaxActivityStatus = (GoalQuestPaxActivityStatus)goalQuestPaxActivityStatusList.get( i );
      assertNotNull( goalQuestPaxActivityStatus );
      assertNotNull( goalQuestPaxActivityStatus.getName() );
    }
  }

}

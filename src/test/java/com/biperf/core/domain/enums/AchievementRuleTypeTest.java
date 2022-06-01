/**
 * 
 */

package com.biperf.core.domain.enums;

import java.util.Collections;
import java.util.List;

/**
 * AchievementRuleTypeTest.
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
 * <td>Dec 6, 2006</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class AchievementRuleTypeTest extends BaseEnumTest
{

  /**
   * Test the ability to get the List
   */
  public void testGetPickList()
  {
    List AchievementRuleTypeList = AchievementRuleType.getList();
    assertNotNull( AchievementRuleTypeList );
    assertNotSame( AchievementRuleTypeList, Collections.EMPTY_LIST );
    assertTrue( AchievementRuleTypeList.size() > 0 );
  }

  /**
   * Test the ability to lookup a single list item
   */
  public void testLookupPickListItem()
  {
    List AchievementRuleTypeList = AchievementRuleType.getList();
    AchievementRuleType AchievementRuleType1 = (AchievementRuleType)AchievementRuleTypeList.get( 0 );
    AchievementRuleType AchievementRuleType2 = AchievementRuleType.lookup( AchievementRuleType1.getCode() );
    assertEquals( AchievementRuleType1, AchievementRuleType2 );
  }

  /**
   * Test to make sure the item name is not null - verify description asset is named right.
   */
  public void testPickListItem()
  {
    List AchievementRuleTypeList = AchievementRuleType.getList();
    for ( int i = 0; i < AchievementRuleTypeList.size(); i++ )
    {
      AchievementRuleType AchievementRuleType = (AchievementRuleType)AchievementRuleTypeList.get( i );
      assertNotNull( AchievementRuleType );
      assertNotNull( AchievementRuleType.getName() );
    }
  }

}

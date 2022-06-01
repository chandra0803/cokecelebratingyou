/**
 * 
 */

package com.biperf.core.domain.enums;

import java.util.Collections;
import java.util.List;

/**
 * AchievementPrecisionTest.
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
public class AchievementPrecisionTest extends BaseEnumTest
{

  /**
   * Test the ability to get the List
   */
  public void testGetPickList()
  {
    List achievementPrecisionList = AchievementPrecision.getList();
    assertNotNull( achievementPrecisionList );
    assertNotSame( achievementPrecisionList, Collections.EMPTY_LIST );
    assertTrue( achievementPrecisionList.size() > 0 );
  }

  /**
   * Test the ability to lookup a single list item
   */
  public void testLookupPickListItem()
  {
    List achievementPrecisionList = AchievementPrecision.getList();
    AchievementPrecision achievementPrecision1 = (AchievementPrecision)achievementPrecisionList.get( 0 );
    AchievementPrecision achievementPrecision2 = AchievementPrecision.lookup( achievementPrecision1.getCode() );
    assertEquals( achievementPrecision1, achievementPrecision2 );
  }

  /**
   * Test to make sure the item name is not null - verify description asset is named right.
   */
  public void testPickListItem()
  {
    List achievementPrecisionList = AchievementPrecision.getList();
    for ( int i = 0; i < achievementPrecisionList.size(); i++ )
    {
      AchievementPrecision achievementPrecision = (AchievementPrecision)achievementPrecisionList.get( i );
      assertNotNull( achievementPrecision );
      assertNotNull( achievementPrecision.getName() );
    }
  }

}

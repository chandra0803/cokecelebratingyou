/**
 * 
 */

package com.biperf.core.domain.enums;

import java.util.Collections;
import java.util.List;

/**
 * PromotionAwardsTypeTest.
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
 * <td>asondgeroth</td>
 * <td>Oct 7, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class PromotionAwardsTypeTest extends BaseEnumTest
{

  /**
   * Test the ability to get the List
   */
  public void testGetPickList()
  {
    List promotionAudienceList = PromotionAwardsType.getList();
    assertNotNull( promotionAudienceList );
    assertNotSame( promotionAudienceList, Collections.EMPTY_LIST );
    assertTrue( promotionAudienceList.size() > 0 );
  }

  /**
   * Test the ability to lookup a single list item
   */
  public void testLookupPickListItem()
  {
    List promotionAwardsList = PromotionAwardsType.getList();
    PromotionAwardsType promotionAwardsType = (PromotionAwardsType)promotionAwardsList.get( 0 );
    PromotionAwardsType promotionAwardsType2 = PromotionAwardsType.lookup( promotionAwardsType.getCode() );
    assertEquals( promotionAwardsType, promotionAwardsType2 );
  }

  /**
   * Test to make sure the item name is not null - verify description asset is named right.
   */
  public void testPickListItem()
  {
    List promotionAwardsList = PromotionAwardsType.getList();
    for ( int i = 0; i < promotionAwardsList.size(); i++ )
    {
      PromotionAwardsType PromotionAwardsType = (PromotionAwardsType)promotionAwardsList.get( i );
      assertNotNull( PromotionAwardsType );
      assertNotNull( PromotionAwardsType.getName() );
    }
  }

}

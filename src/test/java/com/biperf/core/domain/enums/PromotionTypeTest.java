/**
 * 
 */

package com.biperf.core.domain.enums;

import java.util.Collections;
import java.util.List;

/**
 * PromotionTypeTest.
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
 * <td>Jul 5, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class PromotionTypeTest extends BaseEnumTest
{

  /**
   * Test the ability to get the List
   */
  public void testGetPickList()
  {
    List promotionTypeList = PromotionType.getList();
    assertNotNull( promotionTypeList );
    assertNotSame( promotionTypeList, Collections.EMPTY_LIST );
    assertTrue( promotionTypeList.size() > 0 );
  }

  /**
   * Test the ability to lookup a single list item
   */
  public void testLookupPickListItem()
  {
    List promotionTypeList = PromotionType.getList();
    PromotionType promotionType = (PromotionType)promotionTypeList.get( 0 );
    PromotionType promotionType2 = PromotionType.lookup( promotionType.getCode() );
    assertEquals( promotionType, promotionType2 );
  }

  /**
   * Test to make sure the item name is not null - verify description asset is named right.
   */
  public void testPickListItem()
  {
    List promotionTypeList = PromotionType.getList();
    for ( int i = 0; i < promotionTypeList.size(); i++ )
    {
      PromotionType promotionType = (PromotionType)promotionTypeList.get( i );
      assertNotNull( promotionType );
      assertNotNull( promotionType.getName() );
    }
  }

}

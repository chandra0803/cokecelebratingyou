/**
 * 
 */

package com.biperf.core.domain.enums;

import java.util.Collections;
import java.util.List;

/**
 * PromotionStatusTypeTest.
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
 * <td>Jul 1, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class PromotionStatusTypeTest extends BaseEnumTest
{

  /**
   * Test the ability to get the List
   */
  public void testGetPickList()
  {
    List promotionStatusTypeList = PromotionStatusType.getList();
    assertNotNull( promotionStatusTypeList );
    assertNotSame( promotionStatusTypeList, Collections.EMPTY_LIST );
    assertTrue( promotionStatusTypeList.size() > 0 );
  }

  /**
   * Test the ability to lookup a single list item
   */
  public void testLookupPickListItem()
  {
    List promotionStatusTypeList = PromotionStatusType.getList();
    PromotionStatusType promotionStatusType = (PromotionStatusType)promotionStatusTypeList.get( 0 );
    PromotionStatusType promotionStatusType2 = PromotionStatusType.lookup( promotionStatusType.getCode() );
    assertEquals( promotionStatusType, promotionStatusType2 );
  }

  /**
   * Test to make sure the item name is not null - verify description asset is named right.
   */
  public void testPickListItem()
  {
    List promotionStatusTypeList = PromotionStatusType.getList();
    for ( int i = 0; i < promotionStatusTypeList.size(); i++ )
    {
      PromotionStatusType promotionStatusType = (PromotionStatusType)promotionStatusTypeList.get( i );
      assertNotNull( promotionStatusType );
      assertNotNull( promotionStatusType.getName() );
    }
  }

}

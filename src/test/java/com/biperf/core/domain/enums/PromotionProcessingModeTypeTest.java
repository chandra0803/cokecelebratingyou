/**
 * 
 */

package com.biperf.core.domain.enums;

import java.util.Collections;
import java.util.List;

/**
 * PromotionProcessingModeTypeTest.
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
 * <td>Jul 4, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class PromotionProcessingModeTypeTest extends BaseEnumTest
{

  /**
   * Test the ability to get the List
   */
  public void testGetPickList()
  {
    List promotionProcessingModeTypeList = PromotionProcessingModeType.getList();
    assertNotNull( promotionProcessingModeTypeList );
    assertNotSame( promotionProcessingModeTypeList, Collections.EMPTY_LIST );
    assertTrue( promotionProcessingModeTypeList.size() > 0 );
  }

  /**
   * Test the ability to lookup a single list item
   */
  public void testLookupPickListItem()
  {
    List promotionProcessingModeTypeList = PromotionProcessingModeType.getList();
    PromotionProcessingModeType promotionProcessingModeType = (PromotionProcessingModeType)promotionProcessingModeTypeList.get( 0 );
    PromotionProcessingModeType promotionProcessingModeType2 = PromotionProcessingModeType.lookup( promotionProcessingModeType.getCode() );
    assertEquals( promotionProcessingModeType, promotionProcessingModeType2 );
  }

  /**
   * Test to make sure the item name is not null - verify description asset is named right.
   */
  public void testPickListItem()
  {
    List promotionProcessingModeTypeList = PromotionProcessingModeType.getList();
    for ( int i = 0; i < promotionProcessingModeTypeList.size(); i++ )
    {
      PromotionProcessingModeType promotionProcessingModeType = (PromotionProcessingModeType)promotionProcessingModeTypeList.get( i );
      assertNotNull( promotionProcessingModeType );
      assertNotNull( promotionProcessingModeType.getName() );
    }
  }

}

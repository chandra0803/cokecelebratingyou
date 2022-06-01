/**
 * 
 */

package com.biperf.core.domain.enums;

import java.util.Collections;
import java.util.List;

/**
 * PromotionEmailNotificationTypeTest.
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
 * <td>Aug 23, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class PromotionEmailNotificationTypeTest extends BaseEnumTest
{

  /**
   * Test the ability to get the List
   */
  public void testGetPickList()
  {
    List promotionEmailNotificationTypeList = PromotionEmailNotificationType.getList();
    assertNotNull( promotionEmailNotificationTypeList );
    assertNotSame( promotionEmailNotificationTypeList, Collections.EMPTY_LIST );
    assertTrue( promotionEmailNotificationTypeList.size() > 0 );
  }

  /**
   * Test the ability to lookup a single list item
   */
  public void testLookupPickListItem()
  {
    List promotionEmailNotificationTypeList = PromotionEmailNotificationType.getList();
    PromotionEmailNotificationType promotionEmailNotificationType = (PromotionEmailNotificationType)promotionEmailNotificationTypeList.get( 0 );
    PromotionEmailNotificationType promotionEmailNotificationType2 = PromotionEmailNotificationType.lookup( promotionEmailNotificationType.getCode() );
    assertEquals( promotionEmailNotificationType, promotionEmailNotificationType2 );
  }

  /**
   * Test to make sure the item name is not null - verify description asset is named right.
   */
  public void testPickListItem()
  {
    List promotionEmailNotificationTypeList = PromotionEmailNotificationType.getList();
    for ( int i = 0; i < promotionEmailNotificationTypeList.size(); i++ )
    {
      PromotionEmailNotificationType promotionEmailNotificationType = (PromotionEmailNotificationType)promotionEmailNotificationTypeList.get( i );
      assertNotNull( promotionEmailNotificationType );
      assertNotNull( promotionEmailNotificationType.getName() );
    }
  }

}

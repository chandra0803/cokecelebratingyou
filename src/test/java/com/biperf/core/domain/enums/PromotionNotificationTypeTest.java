/**
 * 
 */

package com.biperf.core.domain.enums;

import java.util.Collections;
import java.util.List;

/**
 * PromotionNotificationTypeTest.
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
public class PromotionNotificationTypeTest extends BaseEnumTest
{

  /**
   * Test the ability to get the List
   */
  public void testGetPickList()
  {
    List promotionNotificationTypeList = NotificationType.getList();
    assertNotNull( promotionNotificationTypeList );
    assertNotSame( promotionNotificationTypeList, Collections.EMPTY_LIST );
    assertTrue( promotionNotificationTypeList.size() > 0 );
  }

  /**
   * Test the ability to lookup a single list item
   */
  public void testLookupPickListItem()
  {
    List promotionNotificationTypeList = NotificationType.getList();
    NotificationType promotionNotificationType = (NotificationType)promotionNotificationTypeList.get( 0 );
    NotificationType promotionNotificationType2 = NotificationType.lookup( promotionNotificationType.getCode() );
    assertEquals( promotionNotificationType, promotionNotificationType2 );
  }

  /**
   * Test to make sure the item name is not null - verify description asset is named right.
   */
  public void testPickListItem()
  {
    List promotionNotificationTypeList = NotificationType.getList();
    for ( int i = 0; i < promotionNotificationTypeList.size(); i++ )
    {
      NotificationType promotionNotificationType = (NotificationType)promotionNotificationTypeList.get( i );
      assertNotNull( promotionNotificationType );
      assertNotNull( promotionNotificationType.getName() );
    }
  }

}

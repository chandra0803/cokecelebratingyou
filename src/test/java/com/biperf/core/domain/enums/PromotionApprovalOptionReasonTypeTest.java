/**
 * 
 */

package com.biperf.core.domain.enums;

import java.util.Collections;
import java.util.List;

/**
 * PromotionApprovalOptionReasonTypeTest.
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
 * <td>Jul 5, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class PromotionApprovalOptionReasonTypeTest extends BaseEnumTest
{

  /**
   * Test the ability to get the List
   */
  public void testGetPickList()
  {
    List promotionApprovalOptionReasonTypeList = PromotionApprovalOptionReasonType.getList();
    assertNotNull( promotionApprovalOptionReasonTypeList );
    assertNotSame( promotionApprovalOptionReasonTypeList, Collections.EMPTY_LIST );
    assertTrue( promotionApprovalOptionReasonTypeList.size() > 0 );
  }

  /**
   * Test the ability to lookup a single list item
   */
  public void testLookupPickListItem()
  {
    List promotionApprovalOptionReasonTypeList = PromotionApprovalOptionReasonType.getList();
    PromotionApprovalOptionReasonType promotionApprovalOptionReasonType = (PromotionApprovalOptionReasonType)promotionApprovalOptionReasonTypeList.get( 0 );
    PromotionApprovalOptionReasonType promotionApprovalOptionReasonType2 = PromotionApprovalOptionReasonType.lookup( promotionApprovalOptionReasonType.getCode() );
    assertEquals( promotionApprovalOptionReasonType, promotionApprovalOptionReasonType2 );
  }

  /**
   * Test to make sure the item name is not null - verify description asset is named right.
   */
  public void testPickListItem()
  {
    List promotionApprovalOptionReasonTypeList = PromotionApprovalOptionReasonType.getList();
    for ( int i = 0; i < promotionApprovalOptionReasonTypeList.size(); i++ )
    {
      PromotionApprovalOptionReasonType promotionApprovalOptionReasonType = (PromotionApprovalOptionReasonType)promotionApprovalOptionReasonTypeList.get( i );
      assertNotNull( promotionApprovalOptionReasonType );
      assertNotNull( promotionApprovalOptionReasonType.getName() );
    }
  }

}

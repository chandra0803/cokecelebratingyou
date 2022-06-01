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
 * <td>sedey</td>
 * <td>Jul 5, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class PromotionApprovalOptionTypeTest extends BaseEnumTest
{

  /**
   * Test the ability to get the List
   */
  public void testGetPickList()
  {
    List promotionApprovalOptionTypeList = PromotionApprovalOptionType.getList();
    assertNotNull( promotionApprovalOptionTypeList );
    assertNotSame( promotionApprovalOptionTypeList, Collections.EMPTY_LIST );
    assertTrue( promotionApprovalOptionTypeList.size() > 0 );
  }

  /**
   * Test the ability to lookup a single list item
   */
  public void testLookupPickListItem()
  {
    List promotionApprovalOptionTypeList = PromotionApprovalOptionType.getList();
    PromotionApprovalOptionType promotionApprovalOptionType = (PromotionApprovalOptionType)promotionApprovalOptionTypeList.get( 0 );
    PromotionApprovalOptionType promotionApprovalOptionType2 = PromotionApprovalOptionType.lookup( promotionApprovalOptionType.getCode() );
    assertEquals( promotionApprovalOptionType, promotionApprovalOptionType2 );
  }

  /**
   * Test to make sure the item name is not null - verify description asset is named right.
   */
  public void testPickListItem()
  {
    List promotionApprovalOptionTypeList = PromotionApprovalOptionType.getList();
    for ( int i = 0; i < promotionApprovalOptionTypeList.size(); i++ )
    {
      PromotionApprovalOptionType promotionApprovalOptionType = (PromotionApprovalOptionType)promotionApprovalOptionTypeList.get( i );
      assertNotNull( promotionApprovalOptionType );
      assertNotNull( promotionApprovalOptionType.getName() );
    }
  }

}

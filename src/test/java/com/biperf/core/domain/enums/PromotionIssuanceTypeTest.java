/**
 * 
 */

package com.biperf.core.domain.enums;

import java.util.Collections;
import java.util.List;

/**
 * PromotionIssuanceTypeTest.
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
 * <td>Oct 10, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class PromotionIssuanceTypeTest extends BaseEnumTest
{

  /**
   * Test the ability to get the List
   */
  public void testGetPickList()
  {
    List promotionAudienceList = PromotionIssuanceType.getList();
    assertNotNull( promotionAudienceList );
    assertNotSame( promotionAudienceList, Collections.EMPTY_LIST );
    assertTrue( promotionAudienceList.size() > 0 );
  }

  /**
   * Test the ability to lookup a single list item
   */
  public void testLookupPickListItem()
  {
    List promotionIssuanceList = PromotionIssuanceType.getList();
    PromotionIssuanceType promotionIssuanceType = (PromotionIssuanceType)promotionIssuanceList.get( 0 );
    PromotionIssuanceType promotionIssuanceType2 = PromotionIssuanceType.lookup( promotionIssuanceType.getCode() );
    assertEquals( promotionIssuanceType, promotionIssuanceType2 );
  }

  /**
   * Test to make sure the item name is not null - verify description asset is named right.
   */
  public void testPickListItem()
  {
    List promotionIssuanceList = PromotionIssuanceType.getList();
    for ( int i = 0; i < promotionIssuanceList.size(); i++ )
    {
      PromotionIssuanceType PromotionIssuanceType = (PromotionIssuanceType)promotionIssuanceList.get( i );
      assertNotNull( PromotionIssuanceType );
      assertNotNull( PromotionIssuanceType.getName() );
    }
  }

}

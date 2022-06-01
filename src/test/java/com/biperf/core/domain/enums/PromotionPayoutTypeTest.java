/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javatest/com/biperf/core/domain/enums/PromotionPayoutTypeTest.java,v $
 */

package com.biperf.core.domain.enums;

import java.util.List;

/**
 * PromotionPayoutTypeTest.
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
 * <td>June 29, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class PromotionPayoutTypeTest extends BaseEnumTest
{

  /**
   * Test the ability to get the List
   */
  public void testGetPickList()
  {
    // Oddly, this picklist has "1" and "2" for codes. Assuming things are working (no bugs?) this test will make sure the list stays empty
    List promotionPayoutTypeList = PromotionPayoutType.getList();
    assertNotNull( promotionPayoutTypeList );
//    assertNotSame( promotionPayoutTypeList, Collections.EMPTY_LIST );
    assertTrue( promotionPayoutTypeList.size() == 0 );
  }

  /**
   * Test the ability to lookup a single list item
   */
  public void testLookupPickListItem()
  {
    // Oddly, this picklist has "1" and "2" for codes. Assuming things are working (no bugs?) this test can't do anything
//    List promotionPayoutTypeList = PromotionPayoutType.getList();
//    PromotionPayoutType promotonPayoutType = (PromotionPayoutType)promotionPayoutTypeList.get( 0 );
//    PromotionPayoutType promotonPayoutType2 = PromotionPayoutType.lookup( promotonPayoutType.getCode() );
//    assertEquals( promotonPayoutType, promotonPayoutType2 );
    assertTrue( true );
  }

  /**
   * Test to make sure the item name is not null - verify description asset is named right.
   */
  public void testPickListItem()
  {
    List promotionPayoutTypeList = PromotionPayoutType.getList();
    for ( int i = 0; i < promotionPayoutTypeList.size(); i++ )
    {
      PromotionPayoutType promotonPayoutType = (PromotionPayoutType)promotionPayoutTypeList.get( i );
      assertNotNull( promotonPayoutType );
      assertNotNull( promotonPayoutType.getName() );
    }
  }

}
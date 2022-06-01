/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javatest/com/biperf/core/domain/enums/PromotionAudienceTypeTest.java,v $
 */

package com.biperf.core.domain.enums;

import java.util.Collections;
import java.util.List;

/**
 * PromotionAudienceTypeTest.
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
 * <td>crosenquest</td>
 * <td>Aug 29, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class PromotionAudienceTypeTest extends BaseEnumTest
{

  /**
   * Test the ability to get the List
   */
  public void testGetPickList()
  {
    List promotionAudienceList = PromotionAudienceType.getList();
    assertNotNull( promotionAudienceList );
    assertNotSame( promotionAudienceList, Collections.EMPTY_LIST );
    assertTrue( promotionAudienceList.size() > 0 );
  }

  /**
   * Test the ability to lookup a single list item
   */
  public void testLookupPickListItem()
  {
    List promotionAudienceList = PromotionAudienceType.getList();
    PromotionAudienceType promotionAudienceType = (PromotionAudienceType)promotionAudienceList.get( 0 );
    PromotionAudienceType promotionAudienceType2 = PromotionAudienceType.lookup( promotionAudienceType.getCode() );
    assertEquals( promotionAudienceType, promotionAudienceType2 );
  }

  /**
   * Test to make sure the item name is not null - verify description asset is named right.
   */
  public void testPickListItem()
  {
    List promotionAudienceList = PromotionAudienceType.getList();
    for ( int i = 0; i < promotionAudienceList.size(); i++ )
    {
      PromotionAudienceType promotionAudienceType = (PromotionAudienceType)promotionAudienceList.get( i );
      assertNotNull( promotionAudienceType );
      assertNotNull( promotionAudienceType.getName() );
    }
  }
}

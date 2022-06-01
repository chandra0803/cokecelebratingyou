/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javatest/com/biperf/core/domain/enums/PromotionJobPositionTypeTest.java,v $
 */

package com.biperf.core.domain.enums;

import java.util.Collections;
import java.util.List;

/*
 * PromotionJobPositionTypeTest <p> <b>Change History:</b><br> <table border="1"> <tr> <th>Author</th>
 * <th>Date</th> <th>Version</th> <th>Comments</th> </tr> <tr> <td>OPI Admin</td> <td>Sep
 * 1, 2005</td> <td>1.0</td> <td>created</td> </tr> </table> </p>
 * 
 *
 */

public class PromotionJobPositionTypeTest extends BaseEnumTest
{

  /**
   * Test the ability to get the List
   */
  public void testGetPickList()
  {
    List promotionJobPositionTypeList = PromotionJobPositionType.getList();
    assertNotNull( promotionJobPositionTypeList );
    assertNotSame( promotionJobPositionTypeList, Collections.EMPTY_LIST );
    assertTrue( promotionJobPositionTypeList.size() > 0 );
  }

  /**
   * Test the ability to lookup a single list item
   */
  public void testLookupPickListItem()
  {
    List promotionJobPositionTypeList = PromotionJobPositionType.getList();
    PromotionJobPositionType promotionJobPositionType = (PromotionJobPositionType)promotionJobPositionTypeList.get( 0 );
    PromotionJobPositionType promotionJobPositionType2 = PromotionJobPositionType.lookup( promotionJobPositionType.getCode() );
    assertEquals( promotionJobPositionType, promotionJobPositionType2 );
  }

  /**
   * Test to make sure the item name is not null - verify description asset is named right.
   */
  public void testPickListItem()
  {
    List jobPositionTypeList = PromotionJobPositionType.getList();
    for ( int i = 0; i < jobPositionTypeList.size(); i++ )
    {
      PromotionJobPositionType promotionJobPositionType = (PromotionJobPositionType)jobPositionTypeList.get( i );
      assertNotNull( promotionJobPositionType );
      assertNotNull( promotionJobPositionType.getName() );
    }
  }

}

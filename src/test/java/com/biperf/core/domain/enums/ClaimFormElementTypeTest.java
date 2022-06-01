
package com.biperf.core.domain.enums;/*
                                     * (c) 2005 BI, Inc.  All rights reserved.
                                     * $Source: /usr/local/ndscvsroot/products/beacon/src/javatest/com/biperf/core/domain/enums/ClaimFormElementTypeTest.java,v $
                                     */

import java.util.Collections;
import java.util.List;

/**
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
 * <td>tennant</td>
 * <td>June 01, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ClaimFormElementTypeTest extends BaseEnumTest
{

  /**
   * Test the ability to get the List
   */
  public void testGetPickList()
  {
    List picklistItem = ClaimFormElementType.getList();
    assertNotNull( picklistItem );
    assertNotSame( picklistItem, Collections.EMPTY_LIST );
    assertTrue( picklistItem.size() > 0 );
  }

  /**
   * Test the ability to lookup a single list item
   */
  public void testLookupPickListItem()
  {
    List picklistItem = ClaimFormElementType.getList();
    ClaimFormElementType item1 = (ClaimFormElementType)picklistItem.get( 0 );
    ClaimFormElementType item2 = ClaimFormElementType.lookup( item1.getCode().toUpperCase() );
    assertEquals( item1, item2 );
  }

  /**
   * Test to make sure the item name is not null - verify description asset is named right.
   */
  public void testPickListItem()
  {
    List picklistList = ClaimFormElementType.getList();
    for ( int i = 0; i < picklistList.size(); i++ )
    {
      ClaimFormElementType item = (ClaimFormElementType)picklistList.get( i );
      assertNotNull( item );
      assertNotNull( item.getName() );
    }
  }

}
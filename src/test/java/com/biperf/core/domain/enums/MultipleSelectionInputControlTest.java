
package com.biperf.core.domain.enums;/*
                                     * (c) 2005 BI, Inc.  All rights reserved.
                                     * $Source: /usr/local/ndscvsroot/products/beacon/src/javatest/com/biperf/core/domain/enums/MultipleSelectionInputControlTest.java,v $
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
public class MultipleSelectionInputControlTest extends BaseEnumTest
{

  /**
   * Test the ability to get the List
   */
  public void testGetPickList()
  {
    List picklistItem = MultipleSelectionInputControlType.getList();
    assertNotNull( picklistItem );
    assertNotSame( picklistItem, Collections.EMPTY_LIST );
    assertTrue( picklistItem.size() > 0 );
  }

  /**
   * Test the ability to lookup a single list item
   */
  public void testLookupPickListItem()
  {
    List picklistItem = MultipleSelectionInputControlType.getList();
    MultipleSelectionInputControlType item1 = (MultipleSelectionInputControlType)picklistItem.get( 0 );
    MultipleSelectionInputControlType item2 = MultipleSelectionInputControlType.lookup( item1.getCode().toUpperCase() );
    assertEquals( item1, item2 );
  }

  /**
   * Test to make sure the item name is not null - verify description asset is named right.
   */
  public void testPickListItem()
  {
    List picklistList = MultipleSelectionInputControlType.getList();
    for ( int i = 0; i < picklistList.size(); i++ )
    {
      MultipleSelectionInputControlType item = (MultipleSelectionInputControlType)picklistList.get( i );
      assertNotNull( item );
      assertNotNull( item.getName() );
    }
  }

}
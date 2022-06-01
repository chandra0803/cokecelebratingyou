/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.domain.enums;

import java.util.Collections;
import java.util.List;

/**
 * QuickSearchClaimSearchByFieldTest.
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
 * <td>wadzinsk</td>
 * <td>Aug 22, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class QuickSearchClaimSearchByFieldTest extends BaseEnumTest
{

  /**
   * Test the ability to get the List
   */
  public void testGetPickList()
  {
    List pickList = QuickSearchClaimSearchByField.getList();
    assertNotNull( pickList );
    assertNotSame( pickList, Collections.EMPTY_LIST );
    assertTrue( pickList.size() > 0 );
  }

  /**
   * Test the ability to lookup a single list item
   */
  public void testLookupPickListItem()
  {
    List pickList = QuickSearchClaimSearchByField.getList();
    QuickSearchClaimSearchByField item1 = (QuickSearchClaimSearchByField)pickList.get( 0 );
    QuickSearchClaimSearchByField item2 = QuickSearchClaimSearchByField.lookup( item1.getCode() );
    assertEquals( item1, item2 );
  }

  /**
   * Test to make sure the item name is not null - verify description asset is named right.
   */
  public void testPickListItem()
  {
    List pickList = QuickSearchClaimSearchByField.getList();
    for ( int i = 0; i < pickList.size(); i++ )
    {
      QuickSearchClaimSearchByField item = (QuickSearchClaimSearchByField)pickList.get( i );
      assertNotNull( item );
      assertNotNull( item.getName() );
    }
  }

}
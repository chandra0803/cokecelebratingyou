/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javatest/com/biperf/core/domain/enums/TitleTypeTest.java,v $
 */

package com.biperf.core.domain.enums;

import java.util.Collections;
import java.util.List;

/**
 * TitleTypeTest.
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
 * <td>dunne</td>
 * <td>Apr 22, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class TitleTypeTest extends BaseEnumTest
{

  /**
   * Test the ability to get the List
   */
  public void testGetPickList()
  {
    List titleList = TitleType.getList();
    assertNotNull( titleList );
    assertNotSame( titleList, Collections.EMPTY_LIST );
    assertTrue( titleList.size() > 0 );
  }

  /**
   * Test the ability to lookup a single list item
   */
  public void testLookupPickListItem()
  {
    List titleList = TitleType.getList();
    TitleType title = (TitleType)titleList.get( 0 );
    TitleType title2 = TitleType.lookup( title.getCode() );
    assertEquals( title, title2 );
  }

  /**
   * Test to make sure the item name is not null - verify description asset is named right.
   */
  public void testPickListItem()
  {
    List titleList = TitleType.getList();
    for ( int i = 0; i < titleList.size(); i++ )
    {
      TitleType title = (TitleType)titleList.get( i );
      assertNotNull( title );
      assertNotNull( title.getName() );
    }
  }

}
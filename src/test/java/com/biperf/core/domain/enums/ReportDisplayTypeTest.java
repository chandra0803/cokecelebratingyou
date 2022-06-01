/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javatest/com/biperf/core/domain/enums/ReportDisplayTypeTest.java,v $
 */

package com.biperf.core.domain.enums;

import java.util.Collections;
import java.util.List;

/**
 * ReportDisplayTypeTest.
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
 * <td>robinsra</td>
 * <td>Nov 28, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ReportDisplayTypeTest extends BaseEnumTest
{

  /**
   * Test the ability to get the List
   */
  public void testGetPickList()
  {
    List reportDisplayList = ReportDisplayType.getList();
    assertNotNull( reportDisplayList );
    assertNotSame( reportDisplayList, Collections.EMPTY_LIST );
    assertTrue( reportDisplayList.size() > 0 );
  }

  /**
   * Test the ability to lookup a single list item
   */
  public void testLookupPickListItem()
  {
    List reportDisplayList = ReportDisplayType.getList();
    ReportDisplayType reportDisplay = (ReportDisplayType)reportDisplayList.get( 0 );
    ReportDisplayType reportDisplay2 = ReportDisplayType.lookup( reportDisplay.getCode() );
    assertEquals( reportDisplay, reportDisplay2 );
  }

  /**
   * Test to make sure the item name is not null - verify description asset is named right.
   */
  public void testPickListItem()
  {
    List reportDisplayList = ReportDisplayType.getList();
    for ( int i = 0; i < reportDisplayList.size(); i++ )
    {
      ReportDisplayType reportDisplay = (ReportDisplayType)reportDisplayList.get( i );
      assertNotNull( reportDisplay );
      assertNotNull( reportDisplay.getName() );
    }
  }

}
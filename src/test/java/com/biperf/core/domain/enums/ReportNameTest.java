/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javatest/com/biperf/core/domain/enums/ReportNameTest.java,v $
 */

package com.biperf.core.domain.enums;

import java.util.Collections;
import java.util.List;

/**
 * ReportNameTest.
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
 * <td>Oct 25, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ReportNameTest extends BaseEnumTest
{

  /**
   * Test the ability to get the List
   */
  public void testGetPickList()
  {
    List reportNameList = ReportName.getList();
    assertNotNull( reportNameList );
    assertNotSame( reportNameList, Collections.EMPTY_LIST );
    assertTrue( reportNameList.size() > 0 );
  }

  /**
   * Test the ability to lookup a single list item
   */
  public void testLookupPickListItem()
  {
    List reportNameList = ReportName.getList();
    ReportName reportName = (ReportName)reportNameList.get( 0 );
    ReportName reportName2 = ReportName.lookup( reportName.getCode() );
    assertEquals( reportName, reportName2 );
  }

  /**
   * Test to make sure the item name is not null - verify description asset is named right.
   */
  public void testPickListItem()
  {
    List reportNameList = ReportName.getList();
    for ( int i = 0; i < reportNameList.size(); i++ )
    {
      ReportName reportName = (ReportName)reportNameList.get( i );
      assertNotNull( reportName );
      assertNotNull( reportName.getName() );
    }
  }

}

/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javatest/com/biperf/core/domain/enums/ReportTypeTest.java,v $
 */

package com.biperf.core.domain.enums;

import java.util.Collections;
import java.util.List;

/**
 * ReportTypeTest.
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
public class ReportTypeTest extends BaseEnumTest
{

  /**
   * Test the ability to get the List
   */
  public void testGetPickList()
  {
    List reportTypeList = ReportType.getList();
    assertNotNull( reportTypeList );
    assertNotSame( reportTypeList, Collections.EMPTY_LIST );
    assertTrue( reportTypeList.size() > 0 );
  }

  /**
   * Test the ability to lookup a single list item
   */
  public void testLookupPickListItem()
  {
    List reportTypeList = ReportType.getList();
    ReportType reportType = (ReportType)reportTypeList.get( 0 );
    ReportType reportType2 = ReportType.lookup( reportType.getCode() );
    assertEquals( reportType, reportType2 );
  }

  /**
   * Test to make sure the item name is not null - verify description asset is named right.
   */
  public void testPickListItem()
  {
    List reportTypeList = ReportType.getList();
    for ( int i = 0; i < reportTypeList.size(); i++ )
    {
      ReportType reportType = (ReportType)reportTypeList.get( i );
      assertNotNull( reportType );
      assertNotNull( reportType.getName() );
    }
  }

}

/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javatest/com/biperf/core/domain/enums/StatusTypeTest.java,v $
 */

package com.biperf.core.domain.enums;

import java.util.Collections;
import java.util.List;

/**
 * StatusTypeTest.
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
 * <td>Apr 20, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class StatusTypeTest extends BaseEnumTest
{

  /**
   * Test the ability to get the List
   */
  public void testGetPickList()
  {
    List statusList = StatusType.getList();
    assertNotNull( statusList );
    assertNotSame( statusList, Collections.EMPTY_LIST );
    assertTrue( statusList.size() > 0 );
  }

  /**
   * Test the ability to lookup a single list item
   */
  public void testLookupPickListItem()
  {
    List statusList = StatusType.getList();
    StatusType status = (StatusType)statusList.get( 0 );
    StatusType status2 = StatusType.lookup( status.getCode() );
    assertEquals( status, status2 );
  }

  /**
   * Test to make sure the item name is not null - verify description asset is named right.
   */
  public void testPickListItem()
  {
    List statusList = StatusType.getList();
    StatusType status = (StatusType)statusList.get( 0 );
    assertNotNull( status );
    assertNotNull( status.getName() );
  }

}
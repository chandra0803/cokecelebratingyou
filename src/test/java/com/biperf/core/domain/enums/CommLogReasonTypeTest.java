/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javatest/com/biperf/core/domain/enums/CommLogReasonTypeTest.java,v $
 *
 */

package com.biperf.core.domain.enums;

import java.util.Collections;
import java.util.List;

/**
 * AddressTypeTest.
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
public class CommLogReasonTypeTest extends BaseEnumTest
{

  /**
   * Test the ability to get the List
   */
  public void testGetPickList()
  {
    List commLogReason = CommLogReasonType.getList();
    assertNotNull( commLogReason );
    assertNotSame( commLogReason, Collections.EMPTY_LIST );
    assertTrue( commLogReason.size() > 0 );
  }

  /**
   * Test the ability to lookup a single list item
   */
  public void testLookupPickListItem()
  {
    List commLogReasonList = CommLogReasonType.getList();
    CommLogReasonType commLogReason = (CommLogReasonType)commLogReasonList.get( 0 );
    CommLogReasonType commLogReason2 = CommLogReasonType.lookup( commLogReason.getCode() );
    assertEquals( commLogReason, commLogReason2 );
  }

  /**
   * Test to make sure the item name is not null - verify description asset is named right.
   */
  public void testPickListItem()
  {
    List commLogReasonList = CommLogReasonType.getList();
    for ( int i = 0; i < commLogReasonList.size(); i++ )
    {
      CommLogReasonType commLogReason = (CommLogReasonType)commLogReasonList.get( i );
      assertNotNull( commLogReason );
      assertNotNull( commLogReason.getName() );
    }
  }

}
/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javatest/com/biperf/core/domain/enums/ApprovalStatusTypeTest.java,v $
 */

package com.biperf.core.domain.enums;

import java.util.Collections;
import java.util.List;

/**
 * ApprovalStatusTypeTest.
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
 * <td>zahler</td>
 * <td>Aug 26, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ApprovalStatusTypeTest extends BaseEnumTest
{
  /**
   * Test the ability to get the List
   */
  public void testGetPickList()
  {
    List approvalStatusTypeList = ApprovalStatusType.getList();
    assertNotNull( approvalStatusTypeList );
    assertNotSame( approvalStatusTypeList, Collections.EMPTY_LIST );
    assertTrue( approvalStatusTypeList.size() > 0 );
  }

  /**
   * Test the ability to lookup a single list item
   */
  public void testLookupPickListItem()
  {
    List approvalStatusTypeList = ApprovalStatusType.getList();
    ApprovalStatusType approvalStatusType = (ApprovalStatusType)approvalStatusTypeList.get( 0 );
    ApprovalStatusType approvalStatusType2 = ApprovalStatusType.lookup( approvalStatusType.getCode() );
    assertEquals( approvalStatusType, approvalStatusType2 );
  }

  /**
   * Test to make sure the item name is not null - verify description asset is named right.
   */
  public void testPickListItem()
  {
    List approvalStatusTypeList = ApprovalStatusType.getList();
    for ( int i = 0; i < approvalStatusTypeList.size(); i++ )
    {
      ApprovalStatusType approvalStatusType = (ApprovalStatusType)approvalStatusTypeList.get( i );
      assertNotNull( approvalStatusType );
      assertNotNull( approvalStatusType.getName() );
    }
  }
}

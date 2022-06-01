/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javatest/com/biperf/core/domain/enums/ClaimFormStatusTypeTest.java,v $
 */

package com.biperf.core.domain.enums;

import java.util.Collections;
import java.util.List;

/**
 * ClaimFormStatusTypeTest.
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
 * <td>Jun 2, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ClaimFormStatusTypeTest extends BaseEnumTest
{

  /**
   * Test the ability to get the List
   */
  public void testGetPickList()
  {
    List claimFormStatusTypeList = ClaimFormStatusType.getList();
    assertNotNull( claimFormStatusTypeList );
    assertNotSame( claimFormStatusTypeList, Collections.EMPTY_LIST );
    assertTrue( claimFormStatusTypeList.size() > 0 );
  }

  /**
   * Test the ability to lookup a single list item
   */
  public void testLookupPickListItem()
  {
    List claimFormStatusTypeList = ClaimFormStatusType.getList();
    ClaimFormStatusType claimFormStatusType = (ClaimFormStatusType)claimFormStatusTypeList.get( 0 );
    ClaimFormStatusType claimFormStatusType2 = ClaimFormStatusType.lookup( claimFormStatusType.getCode() );
    assertEquals( claimFormStatusType, claimFormStatusType2 );
  }

  /**
   * Test to make sure the item name is not null - verify description asset is named right.
   */
  public void testPickListItem()
  {
    List claimFormStatusTypeList = ClaimFormStatusType.getList();
    for ( int i = 0; i < claimFormStatusTypeList.size(); i++ )
    {
      ClaimFormStatusType claimFormStatusType = (ClaimFormStatusType)claimFormStatusTypeList.get( i );
      assertNotNull( claimFormStatusType );
      assertNotNull( claimFormStatusType.getName() );
    }
  }

} // end ClaimFormStatusTypeTest

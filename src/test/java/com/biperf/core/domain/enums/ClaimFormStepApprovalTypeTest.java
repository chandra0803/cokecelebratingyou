/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javatest/com/biperf/core/domain/enums/ClaimFormStepApprovalTypeTest.java,v $
 */

package com.biperf.core.domain.enums;

import java.util.Collections;
import java.util.List;

/**
 * ClaimFormStepApprovalTypeTest.
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
 * <td>Jun 8, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ClaimFormStepApprovalTypeTest extends BaseEnumTest
{

  /**
   * Test the ability to get the List
   */
  public void testGetPickList()
  {
    List claimFormStepApprovalTypeList = ClaimFormStepApprovalType.getList();
    assertNotNull( claimFormStepApprovalTypeList );
    assertNotSame( claimFormStepApprovalTypeList, Collections.EMPTY_LIST );
    assertTrue( claimFormStepApprovalTypeList.size() > 0 );
  }

  /**
   * Test the ability to lookup a single list item
   */
  public void testLookupPickListItem()
  {
    List claimFormStepApprovalTypeList = ClaimFormStepApprovalType.getList();
    ClaimFormStepApprovalType claimFormStepApprovalType = (ClaimFormStepApprovalType)claimFormStepApprovalTypeList.get( 0 );
    ClaimFormStepApprovalType claimFormStepApprovalType2 = ClaimFormStepApprovalType.lookup( claimFormStepApprovalType.getCode() );
    assertEquals( claimFormStepApprovalType, claimFormStepApprovalType2 );
  }

  /**
   * Test to make sure the item name is not null - verify description asset is named right.
   */
  public void testPickListItem()
  {
    List claimFormStepApprovalTypeList = ClaimFormStepApprovalType.getList();
    for ( int i = 0; i < claimFormStepApprovalTypeList.size(); i++ )
    {
      ClaimFormStepApprovalType claimFormStepApprovalType = (ClaimFormStepApprovalType)claimFormStepApprovalTypeList.get( i );
      assertNotNull( claimFormStepApprovalType );
      assertNotNull( claimFormStepApprovalType.getName() );
    }
  }

}

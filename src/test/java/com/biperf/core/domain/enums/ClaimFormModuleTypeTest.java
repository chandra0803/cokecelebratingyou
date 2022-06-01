/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javatest/com/biperf/core/domain/enums/ClaimFormModuleTypeTest.java,v $
 */

package com.biperf.core.domain.enums;

import java.util.Collections;
import java.util.List;

/**
 * ClaimFormModuleTypeTest.
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
public class ClaimFormModuleTypeTest extends BaseEnumTest
{
  /**
   * Test the ability to get the List
   */
  public void testGetPickList()
  {
    List claimFormModuleTypeList = ClaimFormModuleType.getList();
    assertNotNull( claimFormModuleTypeList );
    assertNotSame( claimFormModuleTypeList, Collections.EMPTY_LIST );
    assertTrue( claimFormModuleTypeList.size() > 0 );
  }

  /**
   * Test the ability to lookup a single list item
   */
  public void testLookupPickListItem()
  {
    List claimFormModuleTypeList = ClaimFormModuleType.getList();
    ClaimFormModuleType claimFormModuleType = (ClaimFormModuleType)claimFormModuleTypeList.get( 0 );
    ClaimFormModuleType claimFormModuleType2 = ClaimFormModuleType.lookup( claimFormModuleType.getCode() );
    assertEquals( claimFormModuleType, claimFormModuleType2 );
  }

  /**
   * Test to make sure the item name is not null - verify description asset is named right.
   */
  public void testPickListItem()
  {
    List claimFormModuleTypeList = ClaimFormModuleType.getList();
    for ( int i = 0; i < claimFormModuleTypeList.size(); i++ )
    {
      ClaimFormModuleType claimFormModuleType = (ClaimFormModuleType)claimFormModuleTypeList.get( i );
      assertNotNull( claimFormModuleType );
      assertNotNull( claimFormModuleType.getName() );
    }
  }

} // ClaimFormModuleTypeTest

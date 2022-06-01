/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javatest/com/biperf/core/domain/enums/ClaimFormConditionsTypeTest.java,v $
 */

package com.biperf.core.domain.enums;

import java.util.Collections;
import java.util.List;

/**
 * ClaimFormConditionsTypeTest.
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
 * <td>Jun 07, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ClaimFormConditionsTypeTest extends BaseEnumTest
{

  /**
   * Test the ability to get the List
   */
  public void testGetPickList()
  {
    List claimformconditions = ClaimFormConditionsType.getList();
    assertNotNull( claimformconditions );
    assertNotSame( claimformconditions, Collections.EMPTY_LIST );
    assertTrue( claimformconditions.size() > 0 );
  }

  /**
   * Test the ability to lookup a single list item
   */
  public void testLookupPickListItem()
  {
    List claimformconditionsList = ClaimFormConditionsType.getList();
    ClaimFormConditionsType claimformconditions = (ClaimFormConditionsType)claimformconditionsList.get( 0 );
    ClaimFormConditionsType claimformconditions2 = ClaimFormConditionsType.lookup( claimformconditions.getCode() );
    assertEquals( claimformconditions, claimformconditions2 );
  }

  /**
   * Test to make sure the item name is not null - verify description asset is named right.
   */
  public void testPickListItem()
  {
    List claimformconditionsList = ClaimFormConditionsType.getList();
    for ( int i = 0; i < claimformconditionsList.size(); i++ )
    {
      ClaimFormConditionsType claimformconditions = (ClaimFormConditionsType)claimformconditionsList.get( i );
      assertNotNull( claimformconditions );
      assertNotNull( claimformconditions.getName() );
    }
  }

}
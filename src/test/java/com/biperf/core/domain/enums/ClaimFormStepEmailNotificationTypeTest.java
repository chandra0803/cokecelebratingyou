/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javatest/com/biperf/core/domain/enums/ClaimFormStepEmailNotificationTypeTest.java,v $
 */

package com.biperf.core.domain.enums;

import java.util.Collections;
import java.util.List;

/**
 * ClaimFormStepEmailNotificationTypeTest.
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
public class ClaimFormStepEmailNotificationTypeTest extends BaseEnumTest
{

  /**
   * Test the ability to get the List
   */
  public void testGetPickList()
  {
    List claimFormStepEmailNotificationTypeList = ClaimFormStepEmailNotificationType.getList();
    assertNotNull( claimFormStepEmailNotificationTypeList );
    assertNotSame( claimFormStepEmailNotificationTypeList, Collections.EMPTY_LIST );
    assertTrue( claimFormStepEmailNotificationTypeList.size() > 0 );
  }

  /**
   * Test the ability to lookup a single list item
   */
  public void testLookupPickListItem()
  {
    List claimFormStepEmailNotificationTypeList = ClaimFormStepEmailNotificationType.getList();
    ClaimFormStepEmailNotificationType claimFormStepEmailNotificationType = (ClaimFormStepEmailNotificationType)claimFormStepEmailNotificationTypeList.get( 0 );
    ClaimFormStepEmailNotificationType claimFormStepEmailNotificationType2 = ClaimFormStepEmailNotificationType.lookup( claimFormStepEmailNotificationType.getCode() );
    assertEquals( claimFormStepEmailNotificationType, claimFormStepEmailNotificationType2 );
  }

  /**
   * Test to make sure the item name is not null - verify description asset is named right.
   */
  public void testPickListItem()
  {
    List claimFormStepEmailNotificationTypeList = ClaimFormStepEmailNotificationType.getList();
    for ( int i = 0; i < claimFormStepEmailNotificationTypeList.size(); i++ )
    {
      ClaimFormStepEmailNotificationType claimFormStepEmailNotificationType = (ClaimFormStepEmailNotificationType)claimFormStepEmailNotificationTypeList.get( i );
      assertNotNull( claimFormStepEmailNotificationType );
      assertNotNull( claimFormStepEmailNotificationType.getName() );
    }
  }

}

/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javatest/com/biperf/core/domain/enums/ParticipantSuspensionStatusTest.java,v $
 */

package com.biperf.core.domain.enums;

import java.util.Collections;
import java.util.List;

/**
 * ParticipantSuspensionStatusTest.
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
 * <td>Apr 29, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ParticipantSuspensionStatusTest extends BaseEnumTest
{

  /**
   * Test the ability to get the List
   */
  public void testGetPickList()
  {
    List participantSuspensionStatus = ParticipantSuspensionStatus.getList();
    assertNotNull( participantSuspensionStatus );
    assertNotSame( participantSuspensionStatus, Collections.EMPTY_LIST );
    assertTrue( participantSuspensionStatus.size() > 0 );
  }

  /**
   * Test the ability to lookup a single list item
   */
  public void testLookupPickListItem()
  {
    List participantSuspensionStatusList = ParticipantSuspensionStatus.getList();
    ParticipantSuspensionStatus status = (ParticipantSuspensionStatus)participantSuspensionStatusList.get( 0 );
    ParticipantSuspensionStatus status2 = ParticipantSuspensionStatus.lookup( status.getCode() );
    assertEquals( status, status2 );
  }

  /**
   * Test to make sure the item name is not null - verify description asset is named right.
   */
  public void testPickListItem()
  {
    List participantSuspensionStatusList = ParticipantSuspensionStatus.getList();
    for ( int i = 0; i < participantSuspensionStatusList.size(); i++ )
    {
      ParticipantSuspensionStatus status = (ParticipantSuspensionStatus)participantSuspensionStatusList.get( i );
      assertNotNull( status );
      assertNotNull( status.getName() );
    }
  }

}
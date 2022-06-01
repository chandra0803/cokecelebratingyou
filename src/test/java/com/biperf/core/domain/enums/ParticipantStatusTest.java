/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javatest/com/biperf/core/domain/enums/ParticipantStatusTest.java,v $
 */

package com.biperf.core.domain.enums;

import java.util.Collections;
import java.util.List;

/**
 * ParticipantStatusTest.
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
public class ParticipantStatusTest extends BaseEnumTest
{

  /**
   * Test the ability to get the List
   */
  public void testGetPickList()
  {
    List participantStatus = ParticipantStatus.getList();
    assertNotNull( participantStatus );
    assertNotSame( participantStatus, Collections.EMPTY_LIST );
    assertTrue( participantStatus.size() > 0 );
  }

  /**
   * Test the ability to lookup a single list item
   */
  public void testLookupPickListItem()
  {
    List participantStatusList = ParticipantStatus.getList();
    ParticipantStatus status = (ParticipantStatus)participantStatusList.get( 0 );
    ParticipantStatus status2 = ParticipantStatus.lookup( status.getCode() );
    assertEquals( status, status2 );
  }

  /**
   * Test to make sure the item name is not null - verify description asset is named right.
   */
  public void testPickListItem()
  {
    List participantStatusList = ParticipantStatus.getList();
    for ( int i = 0; i < participantStatusList.size(); i++ )
    {
      ParticipantStatus status = (ParticipantStatus)participantStatusList.get( i );
      assertNotNull( status );
      assertNotNull( status.getName() );
    }
  }

}
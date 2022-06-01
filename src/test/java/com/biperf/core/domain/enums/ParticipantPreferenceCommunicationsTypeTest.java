/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javatest/com/biperf/core/domain/enums/ParticipantPreferenceCommunicationsTypeTest.java,v $
 */

package com.biperf.core.domain.enums;

import java.util.Collections;
import java.util.List;

/**
 * ParticipantPreferenceCommunicationsTypeTest.
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
 * <td>Sep 22, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 *          Exp $
 */
public class ParticipantPreferenceCommunicationsTypeTest extends BaseEnumTest
{

  /**
   * Test the ability to get the List
   */
  public void testGetPickList()
  {
    List participantPreferenceCommunicationsTypeList = getPickList();
    assertNotNull( participantPreferenceCommunicationsTypeList );
    assertNotSame( participantPreferenceCommunicationsTypeList, Collections.EMPTY_LIST );
    assertTrue( participantPreferenceCommunicationsTypeList.size() > 0 );
  }

  /**
   * Test the ability to lookup a single list item
   */
  public void testLookupPickListItem()
  {

    List participantPreferenceCommunicationsTypeList = getPickList();

    ParticipantPreferenceCommunicationsType communicationPreference = (ParticipantPreferenceCommunicationsType)participantPreferenceCommunicationsTypeList.get( 0 );

    ParticipantPreferenceCommunicationsType communicationPreference2 = ParticipantPreferenceCommunicationsType.lookup( communicationPreference.getCode() );

    assertEquals( communicationPreference, communicationPreference2 );
  }

  /**
   * Test to make sure the item name is not null - verify description asset is named right.
   */
  public void testPickListItem()
  {

    List participantPreferenceCommunicationsTypeList = getPickList();

    for ( int i = 0; i < participantPreferenceCommunicationsTypeList.size(); i++ )
    {
      ParticipantPreferenceCommunicationsType participantPreferenceCommunicationsType = (ParticipantPreferenceCommunicationsType)participantPreferenceCommunicationsTypeList.get( i );
      assertNotNull( participantPreferenceCommunicationsType );
      assertNotNull( participantPreferenceCommunicationsType.getName() );
    }
  }

  /**
   * Get the participantPreferenceCommunicationType list.
   * 
   * @return List
   */
  private List getPickList()
  {
    return ParticipantPreferenceCommunicationsType.getList();
  }

}
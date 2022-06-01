/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javatest/com/biperf/core/domain/enums/ParticipantRelationshipTest.java,v $
 */

package com.biperf.core.domain.enums;

import java.util.Collections;
import java.util.List;

/**
 * ParticipantRelationshipTest.
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
public class ParticipantRelationshipTest extends BaseEnumTest
{

  /**
   * Test the ability to get the List
   */
  public void testGetPickList()
  {
    List participantRelationship = ParticipantRelationshipType.getList();
    assertNotNull( participantRelationship );
    assertNotSame( participantRelationship, Collections.EMPTY_LIST );
    assertTrue( participantRelationship.size() > 0 );
  }

  /**
   * Test the ability to lookup a single list item
   */
  public void testLookupPickListItem()
  {
    List participantRelationshipList = ParticipantRelationshipType.getList();
    ParticipantRelationshipType relationship = (ParticipantRelationshipType)participantRelationshipList.get( 0 );
    ParticipantRelationshipType relationship2 = ParticipantRelationshipType.lookup( relationship.getCode() );
    assertEquals( relationship, relationship2 );
  }

  /**
   * Test to make sure the item name is not null - verify description asset is named right.
   */
  public void testPickListItem()
  {
    List participantRelationshipList = ParticipantRelationshipType.getList();
    for ( int i = 0; i < participantRelationshipList.size(); i++ )
    {
      ParticipantRelationshipType relationship = (ParticipantRelationshipType)participantRelationshipList.get( i );
      assertNotNull( relationship );
      assertNotNull( relationship.getName() );
    }
  }

}
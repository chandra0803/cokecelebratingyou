/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javatest/com/biperf/core/domain/enums/ClaimParticipantRoleTypeTest.java,v $
 */

package com.biperf.core.domain.enums;

import java.util.Collections;
import java.util.List;

/**
 * ClaimParticipantRoleType.
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
 * <td>Jun 28, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ClaimParticipantRoleTypeTest extends BaseEnumTest
{

  /**
   * Test the ability to get the List
   */
  public void testGetPickList()
  {
    List claimParticipantTypeList = ClaimParticipantRoleType.getList();
    assertNotNull( claimParticipantTypeList );
    assertNotSame( claimParticipantTypeList, Collections.EMPTY_LIST );
    assertTrue( claimParticipantTypeList.size() > 0 );
  }

  /**
   * Test the ability to lookup a single list item
   */
  public void testLookupPickListItem()
  {
    List claimParticipantTypeList = ClaimParticipantRoleType.getList();
    ClaimParticipantRoleType claimParticipantRole = (ClaimParticipantRoleType)claimParticipantTypeList.get( 0 );
    ClaimParticipantRoleType claimParticipant2Role = ClaimParticipantRoleType.lookup( claimParticipantRole.getCode().toUpperCase() );
    assertEquals( claimParticipantRole, claimParticipant2Role );
  }

  /**
   * Test to make sure the item name is not null - verify description asset is named right.
   */
  public void testPickListItem()
  {
    List claimParticipantTypeList = ClaimParticipantRoleType.getList();
    for ( int i = 0; i < claimParticipantTypeList.size(); i++ )
    {
      ClaimParticipantRoleType claimParticipantRole = (ClaimParticipantRoleType)claimParticipantTypeList.get( i );
      assertNotNull( claimParticipantRole );
      assertNotNull( claimParticipantRole.getName() );
    }
  }

}
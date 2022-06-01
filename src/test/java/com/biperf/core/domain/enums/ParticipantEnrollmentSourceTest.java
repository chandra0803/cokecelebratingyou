/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javatest/com/biperf/core/domain/enums/ParticipantEnrollmentSourceTest.java,v $
 */

package com.biperf.core.domain.enums;

import java.util.Collections;
import java.util.List;

/**
 * ParticipantEnrollmentSourceTest.
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
public class ParticipantEnrollmentSourceTest extends BaseEnumTest
{

  /**
   * Test the ability to get the List
   */
  public void testGetPickList()
  {
    List participantEnrollmentSource = ParticipantEnrollmentSource.getList();
    assertNotNull( participantEnrollmentSource );
    assertNotSame( participantEnrollmentSource, Collections.EMPTY_LIST );
    assertTrue( participantEnrollmentSource.size() > 0 );
  }

  /**
   * Test the ability to lookup a single list item
   */
  public void testLookupPickListItem()
  {
    List participantEnrollmentSourceList = ParticipantEnrollmentSource.getList();
    ParticipantEnrollmentSource enrollmentSource = (ParticipantEnrollmentSource)participantEnrollmentSourceList.get( 0 );
    ParticipantEnrollmentSource enrollmentSource2 = ParticipantEnrollmentSource.lookup( enrollmentSource.getCode() );
    assertEquals( enrollmentSource, enrollmentSource2 );
  }

  /**
   * Test to make sure the item name is not null - verify description asset is named right.
   */
  public void testPickListItem()
  {
    List enrollmentSourceList = ParticipantEnrollmentSource.getList();
    for ( int i = 0; i < enrollmentSourceList.size(); i++ )
    {
      ParticipantEnrollmentSource enrollmentSource = (ParticipantEnrollmentSource)enrollmentSourceList.get( i );
      assertNotNull( enrollmentSource );
      assertNotNull( enrollmentSource.getName() );
    }
  }

}
/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javatest/com/biperf/core/domain/enums/JournalStatusTypeTest.java,v $
 */

package com.biperf.core.domain.enums;

import java.util.Collections;
import java.util.List;

/**
 * JournalStatusTypeTest.
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
 * <td>Sep 19, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class JournalStatusTypeTest extends BaseEnumTest
{

  /**
   * Test the ability to get the List
   */
  public void testGetPickList()
  {
    List journalStatusList = JournalStatusType.getList();
    assertNotNull( journalStatusList );
    assertNotSame( journalStatusList, Collections.EMPTY_LIST );
    assertTrue( journalStatusList.size() > 0 );
  }

  /**
   * Test the ability to lookup a single list item
   */
  public void testLookupPickListItem()
  {
    List journalStatusList = JournalStatusType.getList();
    JournalStatusType journalStatus = (JournalStatusType)journalStatusList.get( 0 );
    JournalStatusType journalStatus2 = JournalStatusType.lookup( journalStatus.getCode() );
    assertEquals( journalStatus, journalStatus2 );
  }

  /**
   * Test to make sure the item name is not null - verify description asset is named right.
   */
  public void testPickListItem()
  {
    List journalStatusList = JournalStatusType.getList();
    for ( int i = 0; i < journalStatusList.size(); i++ )
    {
      JournalStatusType journalStatus = (JournalStatusType)journalStatusList.get( i );
      assertNotNull( journalStatus );
      assertNotNull( journalStatus.getName() );
    }
  }

}
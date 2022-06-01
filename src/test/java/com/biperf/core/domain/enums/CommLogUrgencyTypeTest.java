/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javatest/com/biperf/core/domain/enums/CommLogUrgencyTypeTest.java,v $
 *
 */

package com.biperf.core.domain.enums;

import java.util.Collections;
import java.util.List;

/**
 * AddressTypeTest.
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
public class CommLogUrgencyTypeTest extends BaseEnumTest
{

  /**
   * Test the ability to get the List
   */
  public void testGetPickList()
  {
    List commLogUrgency = CommLogUrgencyType.getList();
    assertNotNull( commLogUrgency );
    assertNotSame( commLogUrgency, Collections.EMPTY_LIST );
    assertTrue( commLogUrgency.size() > 0 );
  }

  /**
   * Test the ability to lookup a single list item
   */
  public void testLookupPickListItem()
  {
    List commLogUrgencyList = CommLogUrgencyType.getList();
    CommLogUrgencyType commLogUrgency = (CommLogUrgencyType)commLogUrgencyList.get( 0 );
    CommLogUrgencyType commLogUrgency2 = CommLogUrgencyType.lookup( commLogUrgency.getCode() );
    assertEquals( commLogUrgency, commLogUrgency2 );
  }

  /**
   * Test to make sure the item name is not null - verify description asset is named right.
   */
  public void testPickListItem()
  {
    List commLogUrgencyList = CommLogUrgencyType.getList();
    for ( int i = 0; i < commLogUrgencyList.size(); i++ )
    {
      CommLogUrgencyType commLogUrgency = (CommLogUrgencyType)commLogUrgencyList.get( i );
      assertNotNull( commLogUrgency );
      assertNotNull( commLogUrgency.getName() );
    }
  }

}
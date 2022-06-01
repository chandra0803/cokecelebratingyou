/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javatest/com/biperf/core/domain/enums/CommLogStatusTypeTest.java,v $
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
public class CommLogStatusTypeTest extends BaseEnumTest
{

  /**
   * Test the ability to get the List
   */
  public void testGetPickList()
  {
    List commLogStatus = CommLogStatusType.getList();
    assertNotNull( commLogStatus );
    assertNotSame( commLogStatus, Collections.EMPTY_LIST );
    assertTrue( commLogStatus.size() > 0 );
  }

  /**
   * Test the ability to lookup a single list item
   */
  public void testLookupPickListItem()
  {
    List commLogStatusList = CommLogStatusType.getList();
    CommLogStatusType commLogStatus = (CommLogStatusType)commLogStatusList.get( 0 );
    CommLogStatusType commLogStatus2 = CommLogStatusType.lookup( commLogStatus.getCode() );
    assertEquals( commLogStatus, commLogStatus2 );
  }

  /**
   * Test to make sure the item name is not null - verify description asset is named right.
   */
  public void testPickListItem()
  {
    List commLogStatusList = CommLogStatusType.getList();
    for ( int i = 0; i < commLogStatusList.size(); i++ )
    {
      CommLogStatusType commLogStatus = (CommLogStatusType)commLogStatusList.get( i );
      assertNotNull( commLogStatus );
      assertNotNull( commLogStatus.getName() );
    }
  }

}
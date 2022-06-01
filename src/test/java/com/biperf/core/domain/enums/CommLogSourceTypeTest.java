/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javatest/com/biperf/core/domain/enums/CommLogSourceTypeTest.java,v $
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
public class CommLogSourceTypeTest extends BaseEnumTest
{

  /**
   * Test the ability to get the List
   */
  public void testGetPickList()
  {
    List commLogSource = CommLogSourceType.getList();
    assertNotNull( commLogSource );
    assertNotSame( commLogSource, Collections.EMPTY_LIST );
    assertTrue( commLogSource.size() > 0 );
  }

  /**
   * Test the ability to lookup a single list item
   */
  public void testLookupPickListItem()
  {
    List commLogSourceList = CommLogSourceType.getList();
    CommLogSourceType commLogSource = (CommLogSourceType)commLogSourceList.get( 0 );
    CommLogSourceType commLogSource2 = CommLogSourceType.lookup( commLogSource.getCode() );
    assertEquals( commLogSource, commLogSource2 );
  }

  /**
   * Test to make sure the item name is not null - verify description asset is named right.
   */
  public void testPickListItem()
  {
    List commLogSourceList = CommLogSourceType.getList();
    for ( int i = 0; i < commLogSourceList.size(); i++ )
    {
      CommLogSourceType commLogSource = (CommLogSourceType)commLogSourceList.get( i );
      assertNotNull( commLogSource );
      assertNotNull( commLogSource.getName() );
    }
  }

}
/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javatest/com/biperf/core/domain/enums/MessageModuleTypeTest.java,v $
 */

package com.biperf.core.domain.enums;

import java.util.Collections;
import java.util.List;

/**
 * MessageModuleTypeTest.
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
 * <td>Jun 2, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class MessageModuleTypeTest extends BaseEnumTest
{
  /**
   * Test the ability to get the List
   */
  public void testGetPickList()
  {
    List messageModuleTypeList = MessageModuleType.getList();
    assertNotNull( messageModuleTypeList );
    assertNotSame( messageModuleTypeList, Collections.EMPTY_LIST );
    assertTrue( messageModuleTypeList.size() > 0 );
  }

  /**
   * Test the ability to lookup a single list item
   */
  public void testLookupPickListItem()
  {
    List messageModuleTypeList = MessageModuleType.getList();
    MessageModuleType messageModuleType = (MessageModuleType)messageModuleTypeList.get( 0 );
    MessageModuleType messageModuleType2 = MessageModuleType.lookup( messageModuleType.getCode() );
    assertEquals( messageModuleType, messageModuleType2 );
  }

  /**
   * Test to make sure the item name is not null - verify description asset is named right.
   */
  public void testPickListItem()
  {
    List messageModuleTypeList = MessageModuleType.getList();
    for ( int i = 0; i < messageModuleTypeList.size(); i++ )
    {
      MessageModuleType messageModuleType = (MessageModuleType)messageModuleTypeList.get( i );
      assertNotNull( messageModuleType );
      assertNotNull( messageModuleType.getName() );
    }
  }

} // MessageModuleTypeTest

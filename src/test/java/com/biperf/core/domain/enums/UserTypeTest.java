/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javatest/com/biperf/core/domain/enums/UserTypeTest.java,v $
 */

package com.biperf.core.domain.enums;

import java.util.Collections;
import java.util.List;

/**
 * UserTypeTest.
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
public class UserTypeTest extends BaseEnumTest
{

  /**
   * Test the ability to get the List
   */
  public void testGetPickList()
  {
    List userList = UserType.getList();
    assertNotNull( userList );
    assertNotSame( userList, Collections.EMPTY_LIST );
    assertTrue( userList.size() > 0 );
  }

  /**
   * Test the ability to lookup a single list item
   */
  public void testLookupPickListItem()
  {
    List userList = UserType.getList();
    UserType user = (UserType)userList.get( 0 );
    UserType user2 = UserType.lookup( user.getCode() );
    assertEquals( user, user2 );
  }

  /**
   * Test to make sure the item name is not null - verify description asset is named right.
   */
  public void testPickListItem()
  {
    List userList = UserType.getList();
    for ( int i = 0; i < userList.size(); i++ )
    {
      UserType user = (UserType)userList.get( i );
      assertNotNull( user );
      assertNotNull( user.getName() );
    }
  }

}
/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javatest/com/biperf/core/domain/enums/ContactMethodsTypeTest.java,v $
 */

package com.biperf.core.domain.enums;

import java.util.Collections;
import java.util.List;

/**
 * ContactMethodsTypeTest.
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
public class ContactMethodsTypeTest extends BaseEnumTest
{

  /**
   * Test the ability to get the List
   */
  public void testGetPickList()
  {
    List contactList = ContactMethodsType.getList();
    assertNotNull( contactList );
    assertNotSame( contactList, Collections.EMPTY_LIST );
    assertTrue( contactList.size() > 0 );
  }

  /**
   * Test the ability to lookup a single list item
   */
  public void testLookupPickListItem()
  {
    List contactList = ContactMethodsType.getList();
    ContactMethodsType contact = (ContactMethodsType)contactList.get( 0 );
    ContactMethodsType contact2 = ContactMethodsType.lookup( contact.getCode() );
    assertEquals( contact, contact2 );
  }

  /**
   * Test to make sure the item name is not null - verify description asset is named right.
   */
  public void testPickListItem()
  {
    List contactList = ContactMethodsType.getList();
    for ( int i = 0; i < contactList.size(); i++ )
    {
      ContactMethodsType contact = (ContactMethodsType)contactList.get( i );
      assertNotNull( contact );
      assertNotNull( contact.getName() );
    }
  }

}
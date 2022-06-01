/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javatest/com/biperf/core/domain/enums/AddressTypeTest.java,v $
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
public class AddressTypeTest extends BaseEnumTest
{

  /**
   * Test the ability to get the List
   */
  public void testGetPickList()
  {
    List address = AddressType.getList();
    assertNotNull( address );
    assertNotSame( address, Collections.EMPTY_LIST );
    assertTrue( address.size() > 0 );
  }

  /**
   * Test the ability to lookup a single list item
   */
  public void testLookupPickListItem()
  {
    List addressList = AddressType.getList();
    AddressType address = (AddressType)addressList.get( 0 );
    AddressType address2 = AddressType.lookup( address.getCode() );
    assertEquals( address, address2 );
  }

  /**
   * Test to make sure the item name is not null - verify description asset is named right.
   */
  public void testPickListItem()
  {
    List addressList = AddressType.getList();
    for ( int i = 0; i < addressList.size(); i++ )
    {
      AddressType address = (AddressType)addressList.get( i );
      assertNotNull( address );
      assertNotNull( address.getName() );
    }
  }

}
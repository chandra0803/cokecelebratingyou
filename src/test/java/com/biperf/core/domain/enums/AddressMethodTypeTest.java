/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javatest/com/biperf/core/domain/enums/AddressMethodTypeTest.java,v $
 */

package com.biperf.core.domain.enums;

import java.util.Collections;
import java.util.List;

/**
 * AddressMethodTypeTest.
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
 * <td>sedey</td>
 * <td>June 2, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class AddressMethodTypeTest extends BaseEnumTest
{

  /**
   * Test the ability to get the List
   */
  public void testGetPickList()
  {
    List addressMethodList = AddressMethodType.getList();
    assertNotNull( addressMethodList );
    assertNotSame( addressMethodList, Collections.EMPTY_LIST );
    assertTrue( addressMethodList.size() > 0 );
  }

  /**
   * Test the ability to lookup a single list item
   */
  public void testLookupPickListItem()
  {
    List addressMethodList = AddressMethodType.getList();
    AddressMethodType addressMethod = (AddressMethodType)addressMethodList.get( 0 );
    AddressMethodType addressMethod2 = AddressMethodType.lookup( addressMethod.getCode() );
    assertEquals( addressMethod, addressMethod2 );
  }

  /**
   * Test to make sure the item name is not null - verify description asset is named right.
   */
  public void testPickListItem()
  {
    List addressMethodList = AddressMethodType.getList();
    for ( int i = 0; i < addressMethodList.size(); i++ )
    {
      AddressMethodType addressMethod = (AddressMethodType)addressMethodList.get( i );
      assertNotNull( addressMethod );
      assertNotNull( addressMethod.getName() );
    }
  }

}
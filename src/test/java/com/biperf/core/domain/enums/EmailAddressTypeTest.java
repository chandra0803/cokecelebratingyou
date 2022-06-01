/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javatest/com/biperf/core/domain/enums/EmailAddressTypeTest.java,v $
 */

package com.biperf.core.domain.enums;

import java.util.Collections;
import java.util.List;

/**
 * EmailAddressTypeTest.
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
public class EmailAddressTypeTest extends BaseEnumTest
{

  /**
   * Test the ability to get the List
   */
  public void testGetPickList()
  {
    List emailAddresses = EmailAddressType.getList();
    assertNotNull( emailAddresses );
    assertNotSame( emailAddresses, Collections.EMPTY_LIST );
    assertTrue( emailAddresses.size() > 0 );
  }

  /**
   * Test the ability to lookup a single list item
   */
  public void testLookupPickListItem()
  {
    List emailAddresses = EmailAddressType.getList();
    EmailAddressType emailAddress = (EmailAddressType)emailAddresses.get( 0 );
    EmailAddressType emailAddress2 = EmailAddressType.lookup( emailAddress.getCode() );
    assertEquals( emailAddress, emailAddress2 );
  }

  /**
   * Test to make sure the item name is not null - verify description asset is named right.
   */
  public void testPickListItem()
  {
    List emailAddresses = EmailAddressType.getList();
    for ( int i = 0; i < emailAddresses.size(); i++ )
    {
      EmailAddressType emailAddress = (EmailAddressType)emailAddresses.get( i );
      assertNotNull( emailAddress );
      assertNotNull( emailAddress.getName() );
    }
  }

}
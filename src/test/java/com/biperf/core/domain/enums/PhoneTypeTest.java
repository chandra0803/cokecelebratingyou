/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javatest/com/biperf/core/domain/enums/PhoneTypeTest.java,v $
 */

package com.biperf.core.domain.enums;

import java.util.Collections;
import java.util.List;

/**
 * PhoneTypeTest.
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
public class PhoneTypeTest extends BaseEnumTest
{

  /**
   * Test the ability to get the List
   */
  public void testGetPickList()
  {
    List phoneList = PhoneType.getList();
    assertNotNull( phoneList );
    assertNotSame( phoneList, Collections.EMPTY_LIST );
    assertTrue( phoneList.size() > 0 );
  }

  /**
   * Test the ability to lookup a single list item
   */
  public void testLookupPickListItem()
  {
    List phoneList = PhoneType.getList();
    PhoneType phone = (PhoneType)phoneList.get( 0 );
    PhoneType phone2 = PhoneType.lookup( phone.getCode() );
    assertEquals( phone, phone2 );
  }

  /**
   * Test to make sure the item name is not null - verify description asset is named right.
   */
  public void testPickListItem()
  {
    List phoneList = PhoneType.getList();
    for ( int i = 0; i < phoneList.size(); i++ )
    {
      PhoneType phone = (PhoneType)phoneList.get( i );
      assertNotNull( phone );
      assertNotNull( phone.getName() );
    }
  }

}
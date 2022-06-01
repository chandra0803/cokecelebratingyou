/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javatest/com/biperf/core/domain/enums/ContactMethodTest.java,v $
 */

package com.biperf.core.domain.enums;

import java.util.Collections;
import java.util.List;

/**
 * ParticipantContactMethodsTest.
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
 * <td>frozenquest</td>
 * <td>Apr 22, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ContactMethodTest extends BaseEnumTest
{

  /**
   * Test the ability to get the List
   */
  public void testGetPickList()
  {
    List participantContactMethods = ContactMethod.getList();
    assertNotNull( participantContactMethods );
    assertNotSame( participantContactMethods, Collections.EMPTY_LIST );
    assertTrue( participantContactMethods.size() > 0 );
  }

  /**
   * Test the ability to lookup a single list item
   */
  public void testLookupPickListItem()
  {

    List participantContactMethodsList = ContactMethod.getList();

    ContactMethod contactMethod = (ContactMethod)participantContactMethodsList.get( 0 );

    ContactMethod contactMethod2 = ContactMethod.lookup( contactMethod.getCode() );

    assertEquals( contactMethod, contactMethod2 );
  }

  /**
   * Test to make sure the item name is not null - verify description asset is named right.
   */
  public void testPickListItem()
  {
    List contactMethodList = ContactMethod.getList();
    for ( int i = 0; i < contactMethodList.size(); i++ )
    {
      ContactMethod contactMethod = (ContactMethod)contactMethodList.get( i );

      assertNotNull( contactMethod );
      assertNotNull( contactMethod.getName() );
    }
  }

}
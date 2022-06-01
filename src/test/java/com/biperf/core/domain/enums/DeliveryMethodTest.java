/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javatest/com/biperf/core/domain/enums/DeliveryMethodTest.java,v $
 */

package com.biperf.core.domain.enums;

import java.util.Collections;
import java.util.List;

/*
 * DeliveryMethodTest <p> <b>Change History:</b><br> <table border="1"> <tr> <th>Author</th>
 * <th>Date</th> <th>Version</th> <th>Comments</th> </tr> <tr> <td>Thomas Eaton</td> <td>Dec
 * 7, 2005</td> <td>1.0</td> <td>created</td> </tr> </table> </p>
 * 
 *
 */

public class DeliveryMethodTest extends BaseEnumTest
{
  /**
   * Test the ability to get the List
   */
  public void testGetPickList()
  {
    List deliveryMethodList = DeliveryMethod.getList();
    assertNotNull( deliveryMethodList );
    assertNotSame( deliveryMethodList, Collections.EMPTY_LIST );
    assertTrue( deliveryMethodList.size() > 0 );
  }

  /**
   * Test the ability to lookup a single list item
   */
  public void testLookupPickListItem()
  {
    List deliveryMethodList = DeliveryMethod.getList();
    DeliveryMethod deliveryMethod = (DeliveryMethod)deliveryMethodList.get( 0 );
    DeliveryMethod deliveryMethod2 = DeliveryMethod.lookup( deliveryMethod.getCode() );
    assertEquals( deliveryMethod, deliveryMethod2 );
  }

  /**
   * Test to make sure the item name is not null - verify description asset is named right.
   */
  public void testPickListItem()
  {
    List deliveryMethodList = DeliveryMethod.getList();
    for ( int i = 0; i < deliveryMethodList.size(); i++ )
    {
      DeliveryMethod deliveryMethod = (DeliveryMethod)deliveryMethodList.get( i );
      assertNotNull( deliveryMethod );
      assertNotNull( deliveryMethod.getName() );
    }
  }
}

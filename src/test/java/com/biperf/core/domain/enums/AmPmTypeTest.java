/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javatest/com/biperf/core/domain/enums/AmPmTypeTest.java,v $
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
public class AmPmTypeTest extends BaseEnumTest
{

  /**
   * Test the ability to get the List
   */
  public void testGetPickList()
  {
    List amPmList = AmPmType.getList();
    assertNotNull( amPmList );
    assertNotSame( amPmList, Collections.EMPTY_LIST );
    assertTrue( amPmList.size() > 0 );
  }

  /**
   * Test the ability to lookup a single list item
   */
  public void testLookupPickListItem()
  {
    List amPmList = AmPmType.getList();
    AmPmType amPm = (AmPmType)amPmList.get( 0 );
    AmPmType amPm2 = AmPmType.lookup( amPm.getCode() );
    assertEquals( amPm, amPm2 );
  }

  /**
   * Test to make sure the item name is not null - verify description asset is named right.
   */
  public void testPickListItem()
  {
    List amPmList = AmPmType.getList();
    for ( int i = 0; i < amPmList.size(); i++ )
    {
      AmPmType amPm = (AmPmType)amPmList.get( i );
      assertNotNull( amPm );
      assertNotNull( amPm.getName() );
    }
  }

}
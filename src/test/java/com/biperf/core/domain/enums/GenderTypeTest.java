/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javatest/com/biperf/core/domain/enums/GenderTypeTest.java,v $
 */

package com.biperf.core.domain.enums;

import java.util.Collections;
import java.util.List;

/**
 * GenderTypeTest.
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
public class GenderTypeTest extends BaseEnumTest
{

  /**
   * Test the ability to get the List
   */
  public void testGetPickList()
  {
    List genderList = GenderType.getList();
    assertNotNull( genderList );
    assertNotSame( genderList, Collections.EMPTY_LIST );
    assertTrue( genderList.size() > 0 );
  }

  /**
   * Test the ability to lookup a single list item
   */
  public void testLookupPickListItem()
  {
    List genderList = GenderType.getList();
    GenderType gender = (GenderType)genderList.get( 0 );
    GenderType gender2 = GenderType.lookup( gender.getCode() );
    assertEquals( gender, gender2 );
  }

  /**
   * Test to make sure the item name is not null - verify description asset is named right.
   */
  public void testPickListItem()
  {
    List genderList = GenderType.getList();
    for ( int i = 0; i < genderList.size(); i++ )
    {
      GenderType gender = (GenderType)genderList.get( i );
      assertNotNull( gender );
      assertNotNull( gender.getName() );
    }
  }

}
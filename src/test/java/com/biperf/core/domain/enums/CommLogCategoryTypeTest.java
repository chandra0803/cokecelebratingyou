/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javatest/com/biperf/core/domain/enums/CommLogCategoryTypeTest.java,v $
 *
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
public class CommLogCategoryTypeTest extends BaseEnumTest
{

  /**
   * Test the ability to get the List
   */
  public void testGetPickList()
  {
    List commLogCategory = CommLogCategoryType.getList();
    assertNotNull( commLogCategory );
    assertNotSame( commLogCategory, Collections.EMPTY_LIST );
    assertTrue( commLogCategory.size() > 0 );
  }

  /**
   * Test the ability to lookup a single list item
   */
  public void testLookupPickListItem()
  {
    List commLogCategoryList = CommLogCategoryType.getList();
    CommLogCategoryType commLogCategory = (CommLogCategoryType)commLogCategoryList.get( 0 );
    CommLogCategoryType commLogCategory2 = CommLogCategoryType.lookup( commLogCategory.getCode() );
    assertEquals( commLogCategory, commLogCategory2 );
  }

  /**
   * Test to make sure the item name is not null - verify description asset is named right.
   */
  public void testPickListItem()
  {
    List commLogCategoryList = CommLogCategoryType.getList();
    for ( int i = 0; i < commLogCategoryList.size(); i++ )
    {
      CommLogCategoryType commLogCategory = (CommLogCategoryType)commLogCategoryList.get( i );
      assertNotNull( commLogCategory );
      assertNotNull( commLogCategory.getName() );
    }
  }

}
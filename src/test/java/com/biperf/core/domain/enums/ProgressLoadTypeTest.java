/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javatest/com/biperf/core/domain/enums/ProgressLoadTypeTest.java,v $
 */

package com.biperf.core.domain.enums;

import java.util.Collections;
import java.util.List;

/**
 * ProgressLoadTypeTest.
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
 * <td>March 22, 2007</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ProgressLoadTypeTest extends BaseEnumTest
{

  /**
   * Test the ability to get the List
   */
  public void testGetPickList()
  {
    List progressLoadTypeList = ProgressLoadType.getList();
    assertNotNull( progressLoadTypeList );
    assertNotSame( progressLoadTypeList, Collections.EMPTY_LIST );
    assertTrue( progressLoadTypeList.size() > 0 );
  }

  /**
   * Test the ability to lookup a single list item
   */
  public void testLookupPickListItem()
  {
    List progressLoadTypeList = ProgressLoadType.getList();
    ProgressLoadType progressLoadType = (ProgressLoadType)progressLoadTypeList.get( 0 );
    ProgressLoadType progressLoadType2 = ProgressLoadType.lookup( progressLoadType.getCode() );
    assertEquals( progressLoadType, progressLoadType2 );
  }

  /**
   * Test to make sure the item name is not null - verify description asset is named right.
   */
  public void testPickListItem()
  {
    List progressLoadTypeList = ProgressLoadType.getList();
    for ( int i = 0; i < progressLoadTypeList.size(); i++ )
    {
      ProgressLoadType progressLoadType = (ProgressLoadType)progressLoadTypeList.get( i );
      assertNotNull( progressLoadTypeList );
      assertNotNull( progressLoadType.getName() );
    }
  }

}
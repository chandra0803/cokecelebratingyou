/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.domain.enums;

import java.util.Collections;
import java.util.List;

/**
 * ProgressTransactionTypeTest.
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
 * <td>meadows</td>
 * <td>March 23, 2007</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ProgressTransactionTypeTest extends BaseEnumTest
{

  /**
   * Test the ability to get the List
   */
  public void testGetPickList()
  {
    List progressTransactionTypeList = ProgressTransactionType.getList();
    assertNotNull( progressTransactionTypeList );
    assertNotSame( progressTransactionTypeList, Collections.EMPTY_LIST );
    assertTrue( progressTransactionTypeList.size() > 0 );
  }

  /**
   * Test the ability to lookup a single list item
   */
  public void testLookupPickListItem()
  {
    List progressTransactionTypeList = ProgressTransactionType.getList();
    ProgressTransactionType progressTransactionType = (ProgressTransactionType)progressTransactionTypeList.get( 0 );
    ProgressTransactionType progressTransactionType2 = ProgressTransactionType.lookup( progressTransactionType.getCode() );
    assertEquals( progressTransactionType, progressTransactionType2 );
  }

  /**
   * Test to make sure the item name is not null - verify description asset is named right.
   */
  public void testPickListItem()
  {
    List progressTransactionTypeList = ProgressTransactionType.getList();
    for ( int i = 0; i < progressTransactionTypeList.size(); i++ )
    {
      ProgressTransactionType progressTransactionType = (ProgressTransactionType)progressTransactionTypeList.get( i );
      assertNotNull( progressTransactionTypeList );
      assertNotNull( progressTransactionType.getName() );
    }
  }

}
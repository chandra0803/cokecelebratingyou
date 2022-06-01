/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javatest/com/biperf/core/domain/enums/InsertFieldTypeTest.java,v $
 */

package com.biperf.core.domain.enums;

import java.util.Collections;
import java.util.List;

/**
 * InsertFieldTypeTest.
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
public class InsertFieldTypeTest extends BaseEnumTest
{

  /**
   * Test the ability to get the List
   */
  public void testGetPickList()
  {
    List insertFieldList = InsertFieldType.getList();
    assertNotNull( insertFieldList );
    assertNotSame( insertFieldList, Collections.EMPTY_LIST );
    assertTrue( insertFieldList.size() > 0 );
  }

  /**
   * Test the ability to lookup a single list item
   */
  public void testLookupPickListItem()
  {
    List insertFieldList = InsertFieldType.getList();
    InsertFieldType insertFieldType = (InsertFieldType)insertFieldList.get( 0 );
    InsertFieldType insertFieldType2 = InsertFieldType.lookup( insertFieldType.getCode() );
    assertEquals( insertFieldType, insertFieldType2 );
  }

  /**
   * Test to make sure the item name is not null - verify description asset is named right.
   */
  public void testPickListItem()
  {
    List insertFieldList = InsertFieldType.getList();
    for ( int i = 0; i < insertFieldList.size(); i++ )
    {
      InsertFieldType insertFieldType = (InsertFieldType)insertFieldList.get( i );
      assertNotNull( insertFieldType );
      assertNotNull( insertFieldType.getName() );
    }
  }

}
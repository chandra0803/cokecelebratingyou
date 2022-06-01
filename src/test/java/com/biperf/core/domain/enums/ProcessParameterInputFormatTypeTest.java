/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.domain.enums;

import java.util.Collections;
import java.util.List;

/**
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
 * <td>June 29, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ProcessParameterInputFormatTypeTest extends BaseEnumTest
{

  /**
   * Test the ability to get the List
   */
  public void testGetPickList()
  {
    List list = ProcessParameterInputFormatType.getList();
    assertNotNull( list );
    assertNotSame( list, Collections.EMPTY_LIST );
    assertTrue( list.size() > 0 );
  }

  /**
   * Test the ability to lookup a single list item
   */
  public void testLookupPickListItem()
  {
    List list = ProcessParameterInputFormatType.getList();
    ProcessParameterInputFormatType firstType = (ProcessParameterInputFormatType)list.get( 0 );
    ProcessParameterInputFormatType firstTypeByLookup = ProcessParameterInputFormatType.lookup( firstType.getCode() );
    assertEquals( firstType, firstTypeByLookup );
  }

  /**
   * Test to make sure the item name is not null - verify description asset is named right.
   */
  public void testPickListItem()
  {
    List list = ProcessParameterInputFormatType.getList();
    for ( int i = 0; i < list.size(); i++ )
    {
      ProcessParameterInputFormatType type = (ProcessParameterInputFormatType)list.get( i );
      assertNotNull( type );
      assertNotNull( type.getName() );
    }
  }

}
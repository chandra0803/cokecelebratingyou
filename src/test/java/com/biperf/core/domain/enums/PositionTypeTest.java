/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javatest/com/biperf/core/domain/enums/PositionTypeTest.java,v $
 *
 */

package com.biperf.core.domain.enums;

import java.util.Collections;
import java.util.List;

/**
 * PositionType test <p/> <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>dunne</td>
 * <td>May 4, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 * @author dunne
 *
 */
public class PositionTypeTest extends BaseEnumTest
{

  /**
   * Test the ability to get the List
   */
  public void testGetPickList()
  {
    List position = PositionType.getList();
    assertNotNull( position );
    assertNotSame( position, Collections.EMPTY_LIST );
    assertTrue( position.size() > 0 );
  }

  /**
   * Test the ability to lookup a single list item
   */
  public void testLookupPickListItem()
  {
    List positionList = PositionType.getList();
    PositionType position = (PositionType)positionList.get( 0 );
    PositionType position2 = PositionType.lookup( position.getCode() );
    assertEquals( position, position2 );
  }

  /**
   * Test to make sure the item name is not null - verify description asset is named right.
   */
  public void testPickListItem()
  {
    List positionList = PositionType.getList();
    for ( int i = 0; i < positionList.size(); i++ )
    {
      PositionType position = (PositionType)positionList.get( i );
      assertNotNull( position );
      assertNotNull( position.getName() );
    }
  }

}
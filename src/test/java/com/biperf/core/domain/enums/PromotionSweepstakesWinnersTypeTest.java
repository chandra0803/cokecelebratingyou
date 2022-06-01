/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javatest/com/biperf/core/domain/enums/PromotionSweepstakesWinnersTypeTest.java,v $
 */

package com.biperf.core.domain.enums;

import java.util.Collections;
import java.util.List;

/**
 * PromotionSweepstakesWinnersTypeTest.
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
 * <td>jenniget</td>
 * <td>Oct 7, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class PromotionSweepstakesWinnersTypeTest extends BaseEnumTest
{

  /**
   * Test the ability to get the List
   */
  public void testGetPickList()
  {
    List list = SweepstakesWinnersType.getList();
    assertNotNull( list );
    assertNotSame( list, Collections.EMPTY_LIST );
    assertTrue( list.size() > 0 );
  }

  /**
   * Test the ability to lookup a single list item
   */
  public void testLookupPickListItem()
  {
    List list = SweepstakesWinnersType.getList();
    SweepstakesWinnersType type = (SweepstakesWinnersType)list.get( 0 );
    SweepstakesWinnersType type2 = SweepstakesWinnersType.lookup( type.getCode() );
    assertEquals( type, type2 );
  }

  /**
   * Test to make sure the item name is not null - verify description asset is named right.
   */
  public void testPickListItem()
  {
    List list = SweepstakesWinnersType.getList();
    for ( int i = 0; i < list.size(); i++ )
    {
      SweepstakesWinnersType type = (SweepstakesWinnersType)list.get( i );
      System.out.println( "type: " + type );
      assertNotNull( type );
      assertNotNull( type.getName() );
    }
  }
}

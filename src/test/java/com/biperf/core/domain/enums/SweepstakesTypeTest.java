/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javatest/com/biperf/core/domain/enums/SweepstakesTypeTest.java,v $
 */

package com.biperf.core.domain.enums;

import java.util.Collections;
import java.util.List;

/**
 * SweepstakesTypeTest.
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
 * <td>Oct 12, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class SweepstakesTypeTest extends BaseEnumTest
{

  /**
   * Test the ability to get the List
   */
  public void testGetPickList()
  {
    List list = SweepstakesWinnerEligibilityType.getList();
    assertNotNull( list );
    assertNotSame( list, Collections.EMPTY_LIST );
    assertTrue( list.size() > 0 );
  }

  /**
   * Test the ability to lookup a single list item
   */
  public void testLookupPickListItem()
  {
    List list = SweepstakesWinnerEligibilityType.getList();
    SweepstakesWinnerEligibilityType type = (SweepstakesWinnerEligibilityType)list.get( 0 );
    SweepstakesWinnerEligibilityType type2 = SweepstakesWinnerEligibilityType.lookup( type.getCode() );
    assertEquals( type, type2 );
  }

  /**
   * Test to make sure the item name is not null - verify description asset is named right.
   */
  public void testPickListItem()
  {
    List list = SweepstakesWinnerEligibilityType.getList();
    for ( int i = 0; i < list.size(); i++ )
    {
      SweepstakesWinnerEligibilityType type = (SweepstakesWinnerEligibilityType)list.get( i );
      assertNotNull( type );
      assertNotNull( type.getName() );
    }
  }
}

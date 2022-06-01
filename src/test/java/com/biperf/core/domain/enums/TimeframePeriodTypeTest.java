/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javatest/com/biperf/core/domain/enums/TimeframePeriodTypeTest.java,v $
 */

package com.biperf.core.domain.enums;

import java.util.Collections;
import java.util.List;

/**
 * TimeframePeriodTypeTest.
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
public class TimeframePeriodTypeTest extends BaseEnumTest
{

  /**
   * Test the ability to get the List
   */
  public void testGetPickList()
  {
    List timeframeperiod = TimeframePeriodType.getList();
    assertNotNull( timeframeperiod );
    assertNotSame( timeframeperiod, Collections.EMPTY_LIST );
    assertTrue( timeframeperiod.size() > 0 );
  }

  /**
   * Test the ability to lookup a single list item
   */
  public void testLookupPickListItem()
  {
    List timeframeperiodList = TimeframePeriodType.getList();
    TimeframePeriodType timeframeperiod = (TimeframePeriodType)timeframeperiodList.get( 0 );
    TimeframePeriodType timeframeperiod2 = TimeframePeriodType.lookup( timeframeperiod.getCode() );
    assertEquals( timeframeperiod, timeframeperiod2 );
  }

  /**
   * Test to make sure the item name is not null - verify description asset is named right.
   */
  public void testPickListItem()
  {
    List timeframeperiodList = TimeframePeriodType.getList();
    for ( int i = 0; i < timeframeperiodList.size(); i++ )
    {
      TimeframePeriodType timeframeperiod = (TimeframePeriodType)timeframeperiodList.get( i );
      assertNotNull( timeframeperiod );
      assertNotNull( timeframeperiod.getName() );
    }
  }

}
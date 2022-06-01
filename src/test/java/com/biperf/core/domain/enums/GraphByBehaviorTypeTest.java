
package com.biperf.core.domain.enums;

import java.util.Collections;
import java.util.List;

public class GraphByBehaviorTypeTest extends BaseEnumTest
{

  /**
   * Test the ability to get the List
   */
  public void testGetPickList()
  {
    List graphByBehaviorList = GraphByBehaviorType.getList();
    assertNotNull( graphByBehaviorList );
    assertNotSame( graphByBehaviorList, Collections.EMPTY_LIST );
    assertTrue( graphByBehaviorList.size() > 0 );
  }

  /**
   * Test the ability to lookup a single list item
   */
  public void testLookupPickListItem()
  {
    List graphByBehaviorList = GraphByBehaviorType.getList();
    GraphByBehaviorType graphByBehavior = (GraphByBehaviorType)graphByBehaviorList.get( 0 );
    GraphByBehaviorType graphByBehavior2 = GraphByBehaviorType.lookup( graphByBehavior.getCode() );
    assertEquals( graphByBehavior, graphByBehavior2 );
  }

  /**
   * Test to make sure the item name is not null - verify description asset is named right.
   */
  public void testPickListItem()
  {
    List graphByBehaviorList = GraphByBehaviorType.getList();
    for ( int i = 0; i < graphByBehaviorList.size(); i++ )
    {
      GraphByBehaviorType graphByBehavior = (GraphByBehaviorType)graphByBehaviorList.get( i );
      assertNotNull( graphByBehavior );
      assertNotNull( graphByBehavior.getName() );
    }
  }

}

package com.biperf.core.domain.enums;

import java.util.Collections;
import java.util.List;

public class GraphByProductTypeTest extends BaseEnumTest
{

  /**
   * Test the ability to get the List
   */
  public void testGetPickList()
  {
    List graphByProductList = GraphByProductType.getList();
    assertNotNull( graphByProductList );
    assertNotSame( graphByProductList, Collections.EMPTY_LIST );
    assertTrue( graphByProductList.size() > 0 );
  }

  /**
   * Test the ability to lookup a single list item
   */
  public void testLookupPickListItem()
  {
    List graphByProductList = GraphByProductType.getList();
    GraphByProductType graphByProduct = (GraphByProductType)graphByProductList.get( 0 );
    GraphByProductType graphByProduct2 = GraphByProductType.lookup( graphByProduct.getCode() );
    assertEquals( graphByProduct, graphByProduct2 );
  }

  /**
   * Test to make sure the item name is not null - verify description asset is named right.
   */
  public void testPickListItem()
  {
    List graphByProductList = GraphByProductType.getList();
    for ( int i = 0; i < graphByProductList.size(); i++ )
    {
      GraphByProductType graphByProduct = (GraphByProductType)graphByProductList.get( i );
      assertNotNull( graphByProduct );
      assertNotNull( graphByProduct.getName() );
    }
  }

}

package com.biperf.core.domain.enums;

import java.util.Collections;
import java.util.List;

import org.junit.Ignore;

public class ShippingMethodTest extends BaseEnumTest
{

  /**
   * Test the ability to get the List
   */
  public void testGetPickList()
  {
    List shippingMethodList = ShippingMethodType.getList();
    assertNotNull( shippingMethodList );
    assertNotSame( shippingMethodList, Collections.EMPTY_LIST );
    assertTrue( shippingMethodList.size() > 0 );
  }

  /**
   * Test the ability to lookup a single list item
   */
  public void testLookupPickListItem()
  {
    List shippingMethodList = ShippingMethodType.getList();
    ShippingMethodType shippingMethod = (ShippingMethodType)shippingMethodList.get( 0 );
    ShippingMethodType shippingMethod2 = ShippingMethodType.lookup( shippingMethod.getCode() );
    assertEquals( shippingMethod, shippingMethod2 );
  }

  /**
   * Test to make sure the item name is not null - verify description asset is named right.
   */
  public void testPickListItem()
  {
    List shippingMethodList = ShippingMethodType.getList();
    for ( int i = 0; i < shippingMethodList.size(); i++ )
    {
      ShippingMethodType shippingMethod = (ShippingMethodType)shippingMethodList.get( i );
      assertNotNull( shippingMethod );
      assertNotNull( shippingMethod.getName() );
    }
  }

  /**
   * Test the ability to get the correct shipping cost.
   */
  @Ignore
  public void testGetShippingCost()
  {
    // This method is no longer called anywhere, nor is the code behind it valid
//    List shippingMethodList = ShippingMethodType.getList();
//    for ( int i = 0; i < shippingMethodList.size(); i++ )
//    {
//      ShippingMethodType shippingMethod = (ShippingMethodType)shippingMethodList.get( i );
//      float cost = shippingMethod.getShippingCost();
//      assertTrue( cost > 0 );
//    }
  }

}
/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javatest/com/biperf/core/domain/enums/PromoRecognitionBehaviorTypeTest.java,v $
 */

package com.biperf.core.domain.enums;

import java.util.Collections;
import java.util.List;

/**
 * AddressTypeTest.
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
public class PromoRecognitionBehaviorTypeTest extends BaseEnumTest
{

  /**
   * Test the ability to get the List
   */
  public void testGetPickList()
  {
    List promorecognitionbehavior = PromoRecognitionBehaviorType.getList();
    assertNotNull( promorecognitionbehavior );
    assertNotSame( promorecognitionbehavior, Collections.EMPTY_LIST );
    assertTrue( promorecognitionbehavior.size() > 0 );
  }

  /**
   * Test the ability to lookup a single list item
   */
  public void testLookupPickListItem()
  {
    List promorecognitionbehaviorList = PromoRecognitionBehaviorType.getList();
    PromoRecognitionBehaviorType promorecognitionbehavior = (PromoRecognitionBehaviorType)promorecognitionbehaviorList.get( 0 );
    PromoRecognitionBehaviorType promorecognitionbehavior2 = PromoRecognitionBehaviorType.lookup( promorecognitionbehavior.getCode() );
    assertEquals( promorecognitionbehavior, promorecognitionbehavior2 );
  }

  /**
   * Test to make sure the item name is not null - verify description asset is named right.
   */
  public void testPickListItem()
  {
    List promorecognitionbehaviorList = PromoRecognitionBehaviorType.getList();
    for ( int i = 0; i < promorecognitionbehaviorList.size(); i++ )
    {
      PromoRecognitionBehaviorType promorecognitionbehavior = (PromoRecognitionBehaviorType)promorecognitionbehaviorList.get( i );
      assertNotNull( promorecognitionbehavior );
      assertNotNull( promorecognitionbehavior.getName() );
    }
  }

}
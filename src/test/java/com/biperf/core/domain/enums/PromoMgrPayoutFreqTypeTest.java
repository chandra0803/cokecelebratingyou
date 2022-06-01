/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javatest/com/biperf/core/domain/enums/PromoMgrPayoutFreqTypeTest.java,v $
 */

package com.biperf.core.domain.enums;

import java.util.Collections;
import java.util.List;

/**
 * PromoMgrPayoutFreqTypeTest.
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
 * <td>July 1, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class PromoMgrPayoutFreqTypeTest extends BaseEnumTest
{

  /**
   * Test the ability to get the List
   */
  public void testGetPickList()
  {
    List promoMgrPayoutFreqTypeList = PromoMgrPayoutFreqType.getList();
    assertNotNull( promoMgrPayoutFreqTypeList );
    assertNotSame( promoMgrPayoutFreqTypeList, Collections.EMPTY_LIST );
    assertTrue( promoMgrPayoutFreqTypeList.size() > 0 );
  }

  /**
   * Test the ability to lookup a single list item
   */
  public void testLookupPickListItem()
  {
    List promoMgrPayoutFreqTypeList = PromoMgrPayoutFreqType.getList();
    PromoMgrPayoutFreqType promoMgrPayoutFreqType = (PromoMgrPayoutFreqType)promoMgrPayoutFreqTypeList.get( 0 );
    PromoMgrPayoutFreqType promoMgrPayoutFreqType2 = PromoMgrPayoutFreqType.lookup( promoMgrPayoutFreqType.getCode() );
    assertEquals( promoMgrPayoutFreqType, promoMgrPayoutFreqType2 );
  }

  /**
   * Test to make sure the item name is not null - verify description asset is named right.
   */
  public void testPickListItem()
  {
    List promoMgrPayoutFreqTypeList = PromoMgrPayoutFreqType.getList();
    for ( int i = 0; i < promoMgrPayoutFreqTypeList.size(); i++ )
    {
      PromoMgrPayoutFreqType promoMgrPayoutFreqType = (PromoMgrPayoutFreqType)promoMgrPayoutFreqTypeList.get( i );
      assertNotNull( promoMgrPayoutFreqType );
      assertNotNull( promoMgrPayoutFreqType.getName() );
    }
  }

}
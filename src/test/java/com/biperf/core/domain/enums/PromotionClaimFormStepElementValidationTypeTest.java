/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javatest/com/biperf/core/domain/enums/PromotionClaimFormStepElementValidationTypeTest.java,v $
 */

package com.biperf.core.domain.enums;

import java.util.Collections;
import java.util.List;

/**
 * PromotionClaimFormStepElementValidationTypeTest.
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
 * <td>crosenquest</td>
 * <td>Jul 14, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 *          rosenque Exp $
 */
public class PromotionClaimFormStepElementValidationTypeTest extends BaseEnumTest
{

  /**
   * Test the ability to get the List
   */
  public void testGetPickList()
  {
    List promotionClaimFormStepElementValidationTypeList = PromotionClaimFormStepElementValidationType.getList();
    assertNotNull( promotionClaimFormStepElementValidationTypeList );
    assertNotSame( promotionClaimFormStepElementValidationTypeList, Collections.EMPTY_LIST );
    assertTrue( promotionClaimFormStepElementValidationTypeList.size() > 0 );
  }

  /**
   * Test the ability to lookup a single list item
   */
  public void testLookupPickListItem()
  {
    List promotionClaimFormStepElementValidationTypeList = PromotionClaimFormStepElementValidationType.getList();
    PromotionClaimFormStepElementValidationType promotionClaimFormStepElementValidationType = (PromotionClaimFormStepElementValidationType)promotionClaimFormStepElementValidationTypeList.get( 0 );
    PromotionClaimFormStepElementValidationType promotionClaimFormStepElementValidationType2 = PromotionClaimFormStepElementValidationType
        .lookup( promotionClaimFormStepElementValidationType.getCode() );
    assertEquals( promotionClaimFormStepElementValidationType, promotionClaimFormStepElementValidationType2 );
  }

  /**
   * Test to make sure the item name is not null - verify description asset is named right.
   */
  public void testPickListItem()
  {
    List promotionClaimFormStepElementValidationTypeList = PromotionClaimFormStepElementValidationType.getList();
    for ( int i = 0; i < promotionClaimFormStepElementValidationTypeList.size(); i++ )
    {
      PromotionClaimFormStepElementValidationType promotionClaimFormStepElementValidationType = (PromotionClaimFormStepElementValidationType)promotionClaimFormStepElementValidationTypeList.get( i );
      assertNotNull( promotionClaimFormStepElementValidationType );
      assertNotNull( promotionClaimFormStepElementValidationType.getName() );
    }
  }

}
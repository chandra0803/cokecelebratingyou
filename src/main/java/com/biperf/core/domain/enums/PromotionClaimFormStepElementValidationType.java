/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/domain/enums/PromotionClaimFormStepElementValidationType.java,v $
 */

package com.biperf.core.domain.enums;

import java.util.List;

/**
 * PromotionClaimFormStepElementValidationType.
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
 *          repko Exp $
 */
public class PromotionClaimFormStepElementValidationType extends PickListItem
{

  /**
   * Validation type codes
   */
  public static final String COLLECT = "collect";
  public static final String VALIDATE = "validate";

  /**
   * Asset name used in Content Manager
   */
  private static final String PICKLIST_ASSET = "picklist.promotionclaimformstepelementvalidationtype";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  public PromotionClaimFormStepElementValidationType()
  {
    super();
  }

  /**
   * Get the pick list from content manager.
   * 
   * @return List
   */
  public static List getList()
  {
    return getPickListFactory().getPickList( PromotionClaimFormStepElementValidationType.class );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return PickListItem
   */
  public static PromotionClaimFormStepElementValidationType lookup( String code )
  {
    return (PromotionClaimFormStepElementValidationType)getPickListFactory().getPickListItem( PromotionClaimFormStepElementValidationType.class, code );
  }

  /**
   * Returns null if the defaultItem is not defined - or invalid
   * 
   * @return default PickListItem
   */
  // public static PromotionClaimFormStepElementValidationType getDefaultItem()
  // {
  // return
  // (PromotionClaimFormStepElementValidationType)getPickListFactory().getDefaultPickListItem(
  // PromotionClaimFormStepElementValidationType.class );
  // }
  /**
   * Overridden from
   * 
   * @see com.biperf.core.domain.enums.PickListItem#getPickListAssetCode()
   * @return PICKLIST_ASSET
   */
  public String getPickListAssetCode()
  {
    return PICKLIST_ASSET;
  }
}

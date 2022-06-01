/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/domain/enums/ClaimFormStepApprovalType.java,v $
 */

package com.biperf.core.domain.enums;

import java.util.List;

/**
 * ClaimFormStepApprovalType.
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
 * <td>Jun 7, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ClaimFormStepApprovalType extends PickListItem
{

  /**
   * Asset name used in Content Manager
   */
  private static final String PICKLIST_ASSET = "picklist.claimformstepapprovaltype";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected ClaimFormStepApprovalType()
  {
    super();
  }

  /**
   * Get the pick list from content manager.
   * 
   * @return List of ClaimFormStepApprovalType
   */
  public static List getList()
  {
    return getPickListFactory().getPickList( ClaimFormStepApprovalType.class );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return ClaimFormStepApprovalType
   */
  public static ClaimFormStepApprovalType lookup( String code )
  {
    return (ClaimFormStepApprovalType)getPickListFactory().getPickListItem( ClaimFormStepApprovalType.class, code );
  }

  /**
   * Returns null if the defaultItem is not defined - or invalid
   * 
   * @return ClaimFormStepApprovalType
   */
  // public static ClaimFormStepApprovalType getDefaultItem()
  // {
  // return (ClaimFormStepApprovalType)getPickListFactory().getDefaultPickListItem(
  // ClaimFormStepApprovalType.class );
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

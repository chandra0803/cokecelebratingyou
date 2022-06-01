/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/domain/enums/ClaimFormConditionsType.java,v $
 */

package com.biperf.core.domain.enums;

import java.util.List;

/**
 * This is a concrete instance of a PickListItem which wrappes a type safe enum object of a PickList
 * from content manager.
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
 * <td>Jun 6, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ClaimFormConditionsType extends PickListItem
{

  /**
   * Asset name used in Content Manager
   */
  private static final String PICKLIST_ASSET = "picklist.claimform.conditions.type";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected ClaimFormConditionsType()
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
    return getPickListFactory().getPickList( ClaimFormConditionsType.class );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return PickListItem
   */
  public static ClaimFormConditionsType lookup( String code )
  {
    return (ClaimFormConditionsType)getPickListFactory().getPickListItem( ClaimFormConditionsType.class, code );
  }

  /**
   * Returns null if the defaultItem is not defined - or invalid
   * 
   * @return default PickListItem
   */
  // public static ClaimFormConditionsType getDefaultItem()
  // {
  // return (ClaimFormConditionsType)getPickListFactory().getDefaultPickListItem(
  // ClaimFormConditionsType.class );
  // }
  /**
   * Overridden from
   * 
   * @see PickListItem#getPickListAssetCode()
   * @return PICKLIST_ASSET
   */
  public String getPickListAssetCode()
  {
    return PICKLIST_ASSET;
  }
}

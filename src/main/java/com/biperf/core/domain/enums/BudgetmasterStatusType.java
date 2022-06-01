/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/domain/enums/BudgetmasterStatusType.java,v $
 */

package com.biperf.core.domain.enums;

import java.util.List;

/**
 * The BudgetmasterStatusType is a concrete instance of a PickListItem which wrappes a type save
 * enum object of a PickList from content manager.
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
 * <td>June 2, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class BudgetmasterStatusType extends PickListItem
{

  /**
   * Asset name used in Content Manager
   */
  public static final String PICKLIST_ASSET = "picklist.budgetmaster.status";

  public static final String ACTIVE = "active";
  public static final String INACTIVE = "inactive";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected BudgetmasterStatusType()
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
    return getPickListFactory().getPickList( BudgetmasterStatusType.class );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return PickListItem
   */
  public static BudgetmasterStatusType lookup( String code )
  {
    return (BudgetmasterStatusType)getPickListFactory().getPickListItem( BudgetmasterStatusType.class, code );
  }

  /**
   * Returns null if the defaultItem is not defined - or invalid
   * 
   * @return default PickListItem
   */
  // public static BudgetmasterStatusType getDefaultItem()
  // {
  // return (BudgetmasterStatusType)getPickListFactory().getDefaultPickListItem(
  // BudgetmasterStatusType.class
  // );
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

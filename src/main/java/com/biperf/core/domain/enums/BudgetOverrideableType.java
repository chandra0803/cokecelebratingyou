/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/domain/enums/BudgetOverrideableType.java,v $
 */

package com.biperf.core.domain.enums;

import java.util.List;

/**
 * The BudgetOverrideableType is a concrete instance of a PickListItem which wrappes a type save
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
 * <td>May 23, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class BudgetOverrideableType extends PickListItem
{

  /**
   * Asset name used in Content Manager
   */
  private static final String PICKLIST_ASSET = "picklist.budgetoverrideabletype";

  // public static final String SOFT_OVERRIDE = "soft";
  public static final String HARD_OVERRIDE = "hard";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected BudgetOverrideableType()
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
    return getPickListFactory().getPickList( BudgetOverrideableType.class );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return PickListItem
   */
  public static BudgetOverrideableType lookup( String code )
  {
    return (BudgetOverrideableType)getPickListFactory().getPickListItem( BudgetOverrideableType.class, code );
  }

  /**
   * Returns null if the defaultItem is not defined - or invalid
   * 
   * @return default PickListItem
   */
  // public static BudgetOverrideableType getDefaultItem()
  // {
  // return (BudgetOverrideableType)getPickListFactory().getDefaultPickListItem(
  // BudgetOverrideableType.class );
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

  public boolean isHardCap()
  {
    return HARD_OVERRIDE.equals( getCode() );
  }
}

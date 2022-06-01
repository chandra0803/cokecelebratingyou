/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/domain/enums/BudgetType.java,v $
 */

package com.biperf.core.domain.enums;

import java.util.List;

/**
 * The BudgetType is a concrete instance of a PickListItem which wrappes a type save enum object of
 * a PickList from content manager.
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
public class BudgetType extends PickListItem
{

  /**
   * Asset name used in Content Manager
   */
  private static final String PICKLIST_ASSET = "picklist.budgettype";

  public static final String NODE_BUDGET_TYPE = "node";
  public static final String PAX_BUDGET_TYPE = "pax";
  public static final String CENTRAL_BUDGET_TYPE = "central";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected BudgetType()
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
    return getPickListFactory().getPickList( BudgetType.class, new PickListItemSortOrderComparator() );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return PickListItem
   */
  public static BudgetType lookup( String code )
  {
    return (BudgetType)getPickListFactory().getPickListItem( BudgetType.class, code );
  }

  /**
   * Returns null if the defaultItem is not defined - or invalid
   * 
   * @return default PickListItem
   */
  public static BudgetType getDefaultItem()
  {
    return (BudgetType)getPickListFactory().getDefaultPickListItem( BudgetType.class );
  }

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

  /**
   * Returns true if this object represents the central budget type; returns false otherwise.
   *
   * @return true if this object represents the central budget type; return false otherwise.
   */
  public boolean isCentralBudgetType()
  {
    return getCode().equals( CENTRAL_BUDGET_TYPE );
  }

  /**
   * Determines if the current code is a pax type
   * 
   * @return boolean - True if Pax budget type, false if not
   */
  public boolean isPaxBudgetType()
  {
    return this.getCode().equals( PAX_BUDGET_TYPE );
  }

  /**
   * Determines if the current code is a node type
   * 
   * @return boolean - True if Node budget type, false if not
   */
  public boolean isNodeBudgetType()
  {
    return this.getCode().equals( NODE_BUDGET_TYPE );
  }
}

/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/domain/enums/BudgetStatusType.java,v $
 */

package com.biperf.core.domain.enums;

import java.util.List;

/**
 * The BudgetStatusType is a concrete instance of a PickListItem which wrappes a type save enum
 * object of a PickList from content manager.
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
 * <td>May 24, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class BudgetStatusType extends PickListItem
{

  /**
   * Asset name used in Content Manager
   */
  // BugFix17861
  public static final String PICKLIST_ASSET = "picklist.budgetstatustype";

  public static final String ACTIVE = "active";
  public static final String CLOSED = "closed";
  public static final String SUSPENDED = "suspended";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected BudgetStatusType()
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
    return getPickListFactory().getPickList( BudgetStatusType.class );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return PickListItem
   */
  public static BudgetStatusType lookup( String code )
  {
    return (BudgetStatusType)getPickListFactory().getPickListItem( BudgetStatusType.class, code );
  }

  /**
   * Returns null if the defaultItem is not defined - or invalid
   * 
   * @return default PickListItem
   */
  // public static BudgetStatusType getDefaultItem()
  // {
  // return (BudgetStatusType)getPickListFactory().getDefaultPickListItem( BudgetStatusType.class );
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

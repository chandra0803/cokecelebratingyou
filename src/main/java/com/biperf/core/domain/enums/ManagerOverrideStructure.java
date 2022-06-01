/*
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/domain/enums/Attic/ManagerOverrideStructure.java,v $
 * (c) 2005 BI, Inc.  All rights reserved.
 */

package com.biperf.core.domain.enums;

import java.util.List;

/**
 * ManagerOverrideStructure.
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
 * <td>babu</td>
 * <td>Jun 16, 2008</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ManagerOverrideStructure extends PickListItem
{

  /**
   * Asset name used in Content Manager
   */
  private static final String PICKLIST_ASSET = "picklist.gqcp.overridestructure";

  public static final String NONE = "none";
  public static final String STACK_RANKING_LEVEL = "stackrank";
  public static final String AWARD_PER_ACHIEVER = "awdachv";
  public static final String OVERRIDE_PERCENT = "ovrper";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected ManagerOverrideStructure()
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
    return getPickListFactory().getPickList( ManagerOverrideStructure.class, new PickListItemSortOrderComparator() );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return PickListItem
   */
  public static ManagerOverrideStructure lookup( String code )
  {
    return (ManagerOverrideStructure)getPickListFactory().getPickListItem( ManagerOverrideStructure.class, code );
  }

  /**
   * Returns null if the defaultItem is not defined - or invalid
   * 
   * @return PayoutStructure
   */
  public static ManagerOverrideStructure getDefaultItem()
  {
    return (ManagerOverrideStructure)getPickListFactory().getDefaultPickListItem( ManagerOverrideStructure.class );
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
}

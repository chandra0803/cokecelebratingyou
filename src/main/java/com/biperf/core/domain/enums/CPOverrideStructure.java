/*
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/domain/enums/CPOverrideStructure.java,v $
 * (c) 2005 BI, Inc.  All rights reserved.
 */

package com.biperf.core.domain.enums;

import java.util.List;

/**
 * AchievementPrecision.
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
public class CPOverrideStructure extends PickListItem
{

  /**
   * Asset name used in Content Manager
   */
  private static final String PICKLIST_ASSET = "picklist.challengepoint.overridestructure";

  public static final String NONE = "none";
  public static final String PERCENT_TEAM_EARNINGS = "pctteam";
  public static final String AMOUNT_PER_ACHIEVER = "amtachv";
  public static final String MANAGER_ACTUAL_TEAM_ACHIEVEMENT = "mgracteam";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected CPOverrideStructure()
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
    return getPickListFactory().getPickList( CPOverrideStructure.class, new PickListItemSortOrderComparator() );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return PickListItem
   */
  public static CPOverrideStructure lookup( String code )
  {
    return (CPOverrideStructure)getPickListFactory().getPickListItem( CPOverrideStructure.class, code );
  }

  /**
   * Returns null if the defaultItem is not defined - or invalid
   * 
   * @return PayoutStructure
   */
  public static CPOverrideStructure getDefaultItem()
  {
    return (CPOverrideStructure)getPickListFactory().getDefaultPickListItem( CPOverrideStructure.class );
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

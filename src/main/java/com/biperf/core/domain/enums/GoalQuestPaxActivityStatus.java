/*
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/domain/enums/GoalQuestPaxActivityStatus.java,v $
 * (c) 2005 BI, Inc.  All rights reserved.
 */

package com.biperf.core.domain.enums;

import java.util.List;

/**
 * GoalQuestPaxActivityStatusType.
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
 * <td>meadows</td>
 * <td>Jan 2, 2007</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class GoalQuestPaxActivityStatus extends PickListItem
{

  /**
   * Asset name used in Content Manager
   */
  private static final String PICKLIST_ASSET = "picklist.goalquest.paxactivity.status";

  public static final String PENDING = "pending";
  public static final String APPROVED = "approved";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected GoalQuestPaxActivityStatus()
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
    return getPickListFactory().getPickList( GoalQuestPaxActivityStatus.class );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return PickListItem
   */
  public static GoalQuestPaxActivityStatus lookup( String code )
  {
    return (GoalQuestPaxActivityStatus)getPickListFactory().getPickListItem( GoalQuestPaxActivityStatus.class, code );
  }

  /**
   * Returns null if the defaultItem is not defined - or invalid
   * 
   * @return GoalQuestPaxActivityStatus
   */
  public static GoalQuestPaxActivityStatus getDefaultItem()
  {
    return (GoalQuestPaxActivityStatus)getPickListFactory().getDefaultPickListItem( GoalQuestPaxActivityStatus.class );
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

/*
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/domain/enums/GoalQuestPaxActivityType.java,v $
 * (c) 2005 BI, Inc.  All rights reserved.
 */

package com.biperf.core.domain.enums;

import java.util.List;

/**
 * GoalQuestPaxActivityType.
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
public class GoalQuestPaxActivityType extends PickListItem
{

  /**
   * Asset name used in Content Manager
   */
  private static final String PICKLIST_ASSET = "picklist.goalquest.paxactivity.type";

  public static final String INCREMENTAL = "increment";
  public static final String REPLACE = "replace";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected GoalQuestPaxActivityType()
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
    return getPickListFactory().getPickList( GoalQuestPaxActivityType.class );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return PickListItem
   */
  public static GoalQuestPaxActivityType lookup( String code )
  {
    return (GoalQuestPaxActivityType)getPickListFactory().getPickListItem( GoalQuestPaxActivityType.class, code );
  }

  /**
   * Returns null if the defaultItem is not defined - or invalid
   * 
   * @return GoalQuestPaxActivityType
   */
  public static GoalQuestPaxActivityType getDefaultItem()
  {
    return (GoalQuestPaxActivityType)getPickListFactory().getDefaultPickListItem( GoalQuestPaxActivityType.class );
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

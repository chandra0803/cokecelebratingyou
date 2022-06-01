/*
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/domain/enums/ChallengepointPaxActivityType.java,v $
 * (c) 2008 BI, Inc.  All rights reserved.
 */

package com.biperf.core.domain.enums;

import java.util.List;

/**
 * ChallengepointPaxActivityType.
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
 * <td>Jul 21, 2008</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ChallengepointPaxActivityType extends PickListItem
{

  /**
   * Asset name used in Content Manager
   */
  private static final String PICKLIST_ASSET = "picklist.challengepoint.paxactivity.type";

  public static final String INCREMENTAL = "increment";
  public static final String REPLACE = "replace";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected ChallengepointPaxActivityType()
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
    return getPickListFactory().getPickList( ChallengepointPaxActivityType.class );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return PickListItem
   */
  public static ChallengepointPaxActivityType lookup( String code )
  {
    return (ChallengepointPaxActivityType)getPickListFactory().getPickListItem( ChallengepointPaxActivityType.class, code );
  }

  /**
   * Returns null if the defaultItem is not defined - or invalid
   * 
   * @return ChallengepointPaxActivityType
   */
  public static ChallengepointPaxActivityType getDefaultItem()
  {
    return (ChallengepointPaxActivityType)getPickListFactory().getDefaultPickListItem( ChallengepointPaxActivityType.class );
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

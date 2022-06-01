
package com.biperf.core.domain.enums;

import java.util.List;

/**
 * BaseUnitPosition.
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
 * <td>Oct 11, 2007</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */

public class BaseUnitPosition extends PickListItem
{

  /**
   * Asset name used in Content Manager
   */
  private static final String PICKLIST_ASSET = "picklist.goalquest.baseunitposition";

  public static final String NONE = "none";
  public static final String UNIT_AFTER = "after";
  public static final String UNIT_BEFORE = "before";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected BaseUnitPosition()
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
    return getPickListFactory().getPickList( BaseUnitPosition.class, new PickListItemSortOrderComparator() );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return PickListItem
   */
  public static BaseUnitPosition lookup( String code )
  {
    return (BaseUnitPosition)getPickListFactory().getPickListItem( BaseUnitPosition.class, code );
  }

  /**
   * Returns null if the defaultItem is not defined - or invalid
   * 
   * @return PayoutStructure
   */
  public static BaseUnitPosition getDefaultItem()
  {
    return (BaseUnitPosition)getPickListFactory().getDefaultPickListItem( BaseUnitPosition.class );
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

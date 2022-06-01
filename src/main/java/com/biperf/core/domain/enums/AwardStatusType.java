
package com.biperf.core.domain.enums;

import java.util.List;

/**
 * AwardStatusType.
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
 * <td>kandhi</td>
 * <td>Feb 19, 2013</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 */

public class AwardStatusType extends PickListItem
{

  /**
   * Asset name used in Content Manager
   */
  public static final String PICKLIST_ASSET = "picklist.awardstatus.type";

  public static final String REDEEMED = "redeemed";
  public static final String UNREDEEMED = "unredeemed";
  public static final String EXPIRED = "expired";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected AwardStatusType()
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
    return getPickListFactory().getPickList( AwardStatusType.class );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return PickListItem
   */
  public static AwardStatusType lookup( String code )
  {
    return (AwardStatusType)getPickListFactory().getPickListItem( AwardStatusType.class, code );
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

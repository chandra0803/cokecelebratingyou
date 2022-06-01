/**
 * 
 */

package com.biperf.core.domain.enums;

import java.util.List;

/**
 * PromotionProcessingModeType.
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
 * <td>asondgeroth</td>
 * <td>Jul 1, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class PromotionProcessingModeType extends PickListItem
{
  // Picklist codes
  public static final String REAL_TIME = "real_time";
  public static final String BATCH = "batch";

  /**
   * Asset name used in Content Manager
   */
  private static final String PICKLIST_ASSET = "picklist.promotion.processmodetype";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected PromotionProcessingModeType()
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
    return getPickListFactory().getPickList( PromotionProcessingModeType.class );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return PickListItem
   */
  public static PromotionProcessingModeType lookup( String code )
  {
    return (PromotionProcessingModeType)getPickListFactory().getPickListItem( PromotionProcessingModeType.class, code );
  }

  /**
   * Returns null if the defaultItem is not defined - or invalid
   * 
   * @return default PickListItem
   */
  // public static PromotionProcessingModeType getDefaultItem()
  // {
  // return (PromotionProcessingModeType)getPickListFactory().getDefaultPickListItem(
  // PromotionProcessingModeType.class );
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

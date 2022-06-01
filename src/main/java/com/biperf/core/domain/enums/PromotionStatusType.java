/**
 * 
 */

package com.biperf.core.domain.enums;

import java.util.List;

/**
 * PromotionStatusType.
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
public class PromotionStatusType extends PickListItem
{
  /**
   * Asset name used in Content Manager
   */
  private static final String PICKLIST_ASSET = "picklist.promotion.statustype";

  // Picklist codes
  public static final String UNDER_CONSTRUCTION = "under_construction";
  public static final String COMPLETE = "complete";
  public static final String LIVE = "live";
  public static final String EXPIRED = "expired";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected PromotionStatusType()
  {
    super();
  }

  /**
   * Get the pick list from content manager.
   * 
   * @return List of PromotionType
   */
  public static List getList()
  {
    return getPickListFactory().getPickList( PromotionStatusType.class );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return PromotionStatusType
   */
  public static PromotionStatusType lookup( String code )
  {
    return (PromotionStatusType)getPickListFactory().getPickListItem( PromotionStatusType.class, code );
  }

  /**
   * Returns null if the defaultItem is not defined - or invalid
   * 
   * @return default PromotionStatusType
   */
  // public static PromotionStatusType getDefaultItem()
  // {
  // return (PromotionStatusType)getPickListFactory().getDefaultPickListItem(
  // PromotionStatusType.class );
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

  /**
   * Returns true if this object represents the promotion status "under construction"; returns false
   * otherwise.
   * 
   * @return true if this object represents the promotion status "under construction"; false
   *         otherwise.
   */
  public boolean isUnderConstruction()
  {
    return getCode().equalsIgnoreCase( UNDER_CONSTRUCTION );
  }

  /**
   * Returns true if this object represents the promotion status "complete"; returns false
   * otherwise.
   * 
   * @return true if this object represents the promotion status "complete"; false otherwise.
   */
  public boolean isComplete()
  {
    return getCode().equalsIgnoreCase( COMPLETE );
  }

  /**
   * Returns true if this object represents the promotion status "live"; returns false otherwise.
   * 
   * @return true if this object represents the promotion status "live"; false otherwise.
   */
  public boolean isLive()
  {
    return getCode().equalsIgnoreCase( LIVE );
  }

  /**
   * Returns true if this object represents the promotion status "expired"; returns false otherwise.
   * 
   * @return true if this object represents the promotion status "expired"; false otherwise.
   */
  public boolean isExpired()
  {
    return getCode().equalsIgnoreCase( EXPIRED );
  }
}

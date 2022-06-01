
package com.biperf.core.domain.enums;

import java.util.List;

/**
 * The PurlPromotionMediaType is a concrete instance of a PickListItem which wraps a type save enum
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
 * <td>shanmuga</td>
 * <td>Jan 12, 2011</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */

public class PurlPromotionMediaType extends PickListItem
{

  public static final String PICTURE = "picture";
  public static final String VIDEO = "video";

  /**
   * Asset name used in Content Manager
   */
  public static final String PICKLIST_ASSET = "picklist.purl.promotion.media.type";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected PurlPromotionMediaType()
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
    return getPickListFactory().getPickList( PurlPromotionMediaType.class );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return PickListItem
   */
  public static PurlPromotionMediaType lookup( String code )
  {
    return (PurlPromotionMediaType)getPickListFactory().getPickListItem( PurlPromotionMediaType.class, code );
  }

  /**
   * Returns null if the defaultItem is not defined - or invalid
   * 
   * @return default PickListItem
   */
  public static PurlPromotionMediaType getDefaultItem()
  {
    return (PurlPromotionMediaType)getPickListFactory().getDefaultPickListItem( PurlPromotionMediaType.class );
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

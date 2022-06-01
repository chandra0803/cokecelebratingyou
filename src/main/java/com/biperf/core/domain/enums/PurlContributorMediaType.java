
package com.biperf.core.domain.enums;

import java.util.List;

/**
 * The ParticipantStatus is a concrete instance of a PickListItem which wraps a type save enum
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
 * <td>Nov 22, 2010</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */

public class PurlContributorMediaType extends PickListItem
{

  public static final String PICTURE = "picture";
  public static final String VIDEO = "video";
  public static final String VIDEO_URL = "video_url";

  /**
   * Asset name used in Content Manager
   */
  public static final String PICKLIST_ASSET = "picklist.purl.contributor.media.type";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected PurlContributorMediaType()
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
    return getPickListFactory().getPickList( PurlContributorMediaType.class );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return PickListItem
   */
  public static PurlContributorMediaType lookup( String code )
  {
    return (PurlContributorMediaType)getPickListFactory().getPickListItem( PurlContributorMediaType.class, code );
  }

  /**
   * Returns null if the defaultItem is not defined - or invalid
   * 
   * @return default PickListItem
   */
  public static PurlContributorMediaType getDefaultItem()
  {
    return (PurlContributorMediaType)getPickListFactory().getDefaultPickListItem( PurlContributorMediaType.class );
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

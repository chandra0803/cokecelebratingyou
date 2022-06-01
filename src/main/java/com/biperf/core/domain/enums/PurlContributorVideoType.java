
package com.biperf.core.domain.enums;

import java.util.List;

/**
 * The PurlContributorVideoType is a concrete instance of a PickListItem which wraps a type save enum
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
 * <td>drahn</td>
 * <td>Oct 15, 2012</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */

public class PurlContributorVideoType extends PickListItem
{

  public static final String DIRECT = "direct";
  public static final String WEB = "web";

  /**
   * Asset name used in Content Manager
   */
  public static final String PICKLIST_ASSET = "picklist.purl.contributor.video.type";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected PurlContributorVideoType()
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
    return getPickListFactory().getPickList( PurlContributorVideoType.class );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return PickListItem
   */
  public static PurlContributorVideoType lookup( String code )
  {
    return (PurlContributorVideoType)getPickListFactory().getPickListItem( PurlContributorVideoType.class, code );
  }

  /**
   * Returns null if the defaultItem is not defined - or invalid
   * 
   * @return default PickListItem
   */
  public static PurlContributorVideoType getDefaultItem()
  {
    return (PurlContributorVideoType)getPickListFactory().getDefaultPickListItem( PurlContributorVideoType.class );
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

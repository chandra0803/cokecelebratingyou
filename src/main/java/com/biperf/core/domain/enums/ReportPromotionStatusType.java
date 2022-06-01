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
public class ReportPromotionStatusType extends PickListItem
{
  /**
   * Asset name used in Content Manager
   */
  private static final String PICKLIST_ASSET = "picklist.reportpromotion.statustype";

  // Picklist codes
  public static final String ACTIVE = "live";
  public static final String INACTIVE = "expired";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected ReportPromotionStatusType()
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
    return getPickListFactory().getPickList( ReportPromotionStatusType.class );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return PromotionStatusType
   */
  public static ReportPromotionStatusType lookup( String code )
  {
    return (ReportPromotionStatusType)getPickListFactory().getPickListItem( ReportPromotionStatusType.class, code );
  }

  public String getPickListAssetCode()
  {
    return PICKLIST_ASSET;
  }

}

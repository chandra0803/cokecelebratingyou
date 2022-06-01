/**
 * 
 */

package com.biperf.core.domain.enums;

import java.util.List;

/**
 * PromotionApprovalOptionReasonType.
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
 * <td>sedey</td>
 * <td>Jul 1, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class PromotionApprovalOptionReasonType extends PickListItem
{

  /**
   * Asset name used in Content Manager
   */
  private static final String PICKLIST_ASSET = "picklist.promotion.approval.option.reason.type";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected PromotionApprovalOptionReasonType()
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
    return getPickListFactory().getPickList( PromotionApprovalOptionReasonType.class );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return PickListItem
   */
  public static PromotionApprovalOptionReasonType lookup( String code )
  {
    return (PromotionApprovalOptionReasonType)getPickListFactory().getPickListItem( PromotionApprovalOptionReasonType.class, code );
  }

  /**
   * Returns null if the defaultItem is not defined - or invalid
   * 
   * @return default PickListItem
   */
  // public static PromotionApprovalOptionReasonType getDefaultItem()
  // {
  // return (PromotionApprovalOptionReasonType)getPickListFactory().getDefaultPickListItem(
  // PromotionApprovalOptionReasonType.class );
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

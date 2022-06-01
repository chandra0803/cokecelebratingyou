/**
 * 
 */

package com.biperf.core.domain.enums;

import java.util.List;

/**
 * PromotionApprovalOptionType.
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
public class PromotionApprovalOptionType extends PickListItem
{

  /**
   * promotion approval option "approved"
   */
  public static final String APPROVED = "approved";

  /**
   * promotion approval option "PENDING"
   */
  public static final String PENDING = "pending";

  /**
   * promotion approval option "HELD"
   */
  public static final String HELD = "held";

  /**
   * promotion approval option "denied"
   */
  public static final String DENIED = "denied";
  /**
   * Asset name used in Content Manager
   */
  /**
   * promotion approval option "WINNER"
   */
  public static final String WINNER = "winner";

  /**
   * promotion approval option "NONWINNER"
   */
  public static final String NONWINNER = "nonwinner";

  /**
   * promotion approval option "EXPIRED"
   */
  public static final String EXPIRED = "expired";

  private static final String PICKLIST_ASSET = "picklist.promotion.approval.option.type";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected PromotionApprovalOptionType()
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
    return getPickListFactory().getPickList( PromotionApprovalOptionType.class );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return PickListItem
   */
  public static PromotionApprovalOptionType lookup( String code )
  {
    return (PromotionApprovalOptionType)getPickListFactory().getPickListItem( PromotionApprovalOptionType.class, code );
  }

  /**
   * Returns null if the defaultItem is not defined - or invalid
   * 
   * @return default PickListItem
   */
  // public static PromotionApprovalOptionType getDefaultItem()
  // {
  // return (PromotionApprovalOptionType)getPickListFactory().getDefaultPickListItem(
  // PromotionApprovalOptionType.class );
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

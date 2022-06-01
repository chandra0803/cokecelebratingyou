/**
 * 
 */

package com.biperf.core.domain.enums;

import java.util.List;

/**
 * ApprovalConditionalAmmountOperatorType.
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
public class ApprovalConditionalAmmountOperatorType extends PickListItem
{

  public static final String LT = "lt";
  public static final String LTEQ = "lteq";
  public static final String EQ = "eq";
  public static final String GTEQ = "gteq";
  public static final String GT = "gt";

  /**
   * Asset name used in Content Manager
   */
  private static final String PICKLIST_ASSET = "picklist.promotion.approvalconditionalamountoperator";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected ApprovalConditionalAmmountOperatorType()
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
    return getPickListFactory().getPickList( ApprovalConditionalAmmountOperatorType.class );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return PickListItem
   */
  public static ApprovalConditionalAmmountOperatorType lookup( String code )
  {
    return (ApprovalConditionalAmmountOperatorType)getPickListFactory().getPickListItem( ApprovalConditionalAmmountOperatorType.class, code );
  }

  /**
   * Returns null if the defaultItem is not defined - or invalid
   * 
   * @return default PickListItem
   */
  // public static ApprovalConditionalAmmountOperatorType getDefaultItem()
  // {
  // return (ApprovalConditionalAmmountOperatorType)getPickListFactory().getDefaultPickListItem(
  // ApprovalConditionalAmmountOperatorType.class );
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

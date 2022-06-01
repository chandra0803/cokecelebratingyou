/**
 * 
 */

package com.biperf.core.domain.enums;

import java.util.List;

/**
 * ApprovalType.
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
public class ApprovalType extends PickListItem
{
  /**
   * Values defined in Content Manager
   */
  public static final String AUTOMATIC_IMMEDIATE = "auto_approve";
  public static final String AUTOMATIC_DELAYED = "auto_delayed";
  public static final String MANUAL = "manual";
  public static final String CONDITIONAL_NTH_BASED = "cond_nth";
  public static final String CONDITIONAL_AMOUNT_BASED = "cond_amt";
  public static final String CONDITIONAL_PAX_BASED = "cond_pax";
  public static final String COKE_CUSTOM = "coke_custom";////customization wip 42702

  /**
   * Asset name used in Content Manager
   */
  private static final String PICKLIST_ASSET = "picklist.promotion.approvaltype";
  public static final String CONDITIONAL_AMOUNT = "cond_amt";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected ApprovalType()
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
    return getPickListFactory().getPickList( ApprovalType.class );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return PickListItem
   */
  public static ApprovalType lookup( String code )
  {
    return (ApprovalType)getPickListFactory().getPickListItem( ApprovalType.class, code );
  }

  /**
   * Returns null if the defaultItem is not defined - or invalid
   * 
   * @return default PickListItem
   */
  // public static ApprovalType getDefaultItem()
  // {
  // return (ApprovalType)getPickListFactory().getDefaultPickListItem( ApprovalType.class );
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
   * Returns true if this object represents the approval status "automatic immediate"; returns false
   * otherwise.
   * 
   * @return true if this object represents the promotion status "automatic immediate"; false
   *         otherwise.
   */
  public boolean isAutomaticImmediate()
  {
    return getCode().equalsIgnoreCase( AUTOMATIC_IMMEDIATE );
  }

  /**
   * Returns true if this object represents the approval status "automatic delayed"; returns false
   * otherwise.
   * 
   * @return true if this object represents the promotion status "automatic delayed"; false
   *         otherwise.
   */
  public boolean isAutomaticDelayed()
  {
    return getCode().equalsIgnoreCase( AUTOMATIC_DELAYED );
  }

  /**
   * Returns true if this object represents automatic approval, returns false
   * otherwise.
   * 
   * @return true if this object represents the promotion status automatic approval; false
   *         otherwise.
   */
  public boolean isAutomaticApproved()
  {
    return isAutomaticImmediate() || isAutomaticDelayed();
  }
}


package com.biperf.core.domain.enums;

import java.util.List;

/**
 * BudgetFinalPayoutRule. Functionality: "Break the bank" 
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
 * <td>Tammy Cheng</td>
 * <td>May 23, 2006</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class BudgetFinalPayoutRule extends PickListItem
{

  /**
   * Asset name used in Content Manager
   */
  private static final String PICKLIST_ASSET = "picklist.budgetfinalpayoutrule";

  public static final String FULL_PAYOUT = "full";
  public static final String PARTIAL_PAYOUT = "partial";
  public static final String NO_PAYOUT = "nopayout";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected BudgetFinalPayoutRule()
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
    return getPickListFactory().getPickList( BudgetFinalPayoutRule.class, new PickListItemSortOrderComparator() );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return PickListItem
   */
  public static BudgetFinalPayoutRule lookup( String code )
  {
    return (BudgetFinalPayoutRule)getPickListFactory().getPickListItem( BudgetFinalPayoutRule.class, code );
  }

  /**
   * Returns null if the defaultItem is not defined - or invalid
   * 
   * @return default PickListItem
   */
  public static BudgetFinalPayoutRule getDefaultItem()
  {
    return (BudgetFinalPayoutRule)getPickListFactory().getDefaultPickListItem( BudgetFinalPayoutRule.class );
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

  /**
   * Returns true if this object represents full payout; returns false otherwise.
   * For example: quiz is approved, full award amount is paid, budget is overdrawn.
   *
   * @return boolean - true if this object represents full payout; returns false otherwise.
   */
  public boolean isFullPayout()
  {
    return getCode().equals( FULL_PAYOUT );
  }

  /**
   * Returns true if this object represents partial payout; returns false otherwise.
   * For example: quiz is approved, remaining budgt amount is paid, budget is zero.
   * 
   * @return boolean - true if this object represents partial payout; returns false otherwise.
   */
  public boolean isPartialPayout()
  {
    return this.getCode().equals( PARTIAL_PAYOUT );
  }

  /**
   * Determines if this object represents no payout; returns false otherwise.
   * For example: quiz is approved, no award is paid, budget remains as is.
   * 
   *@return boolean - true if this object represents no payout; returns false otherwise.
   */
  public boolean isNoPayout()
  {
    return this.getCode().equals( NO_PAYOUT );
  }
}


package com.biperf.core.domain.enums;

import java.util.List;

public class BudgetActionType extends PickListItem
{

  public static final String PICKLIST_ASSET = "picklist.budgetactiontype";

  public static final String TRANSFER = "transfer";
  public static final String DEDUCT = "deduct";
  public static final String DEPOSIT = "deposit";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected BudgetActionType()
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
    return getPickListFactory().getPickList( BudgetActionType.class );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return PickListItem
   */
  public static BudgetActionType lookup( String code )
  {
    return (BudgetActionType)getPickListFactory().getPickListItem( BudgetActionType.class, code );
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

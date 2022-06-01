
package com.biperf.core.domain.enums;

import java.util.List;

/**
 * CashBudgetDistributionType for cash budget balance report
 */
public class CashBudgetDistributionType extends PickListItem
{
  private static final long serialVersionUID = 1L;

  /**
   * Asset name used in Content Manager
   */
  // BugFix17861
  public static final String PICKLIST_ASSET = "picklist.cashbudgetdistributiontype";

  public static final String CENTRAL = "central";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected CashBudgetDistributionType()
  {
    super();
  }

  /**
   * Get the pick list from content manager.
   * 
   * @return List
   */
  public static List<CashBudgetDistributionType> getList()
  {
    return getPickListFactory().getPickList( CashBudgetDistributionType.class );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return PickListItem
   */
  public static CashBudgetDistributionType lookup( String code )
  {
    return (CashBudgetDistributionType)getPickListFactory().getPickListItem( CashBudgetDistributionType.class, code );
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


package com.biperf.core.domain.enums;

import java.util.List;

public class BudgetReallocationEligType extends PickListItem
{
  /**
   * Asset name used in Content Manager
   */
  public static final String PICKLIST_ASSET = "picklist.budgetrealloc.eligtype";

  public static final String ORG_UNIT_ONLY = "orgunit";
  public static final String ORG_UNIT_AND_BELOW = "orgunitbelow";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected BudgetReallocationEligType()
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
    return getPickListFactory().getPickList( BudgetReallocationEligType.class );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return PickListItem
   */
  public static BudgetReallocationEligType lookup( String code )
  {
    return (BudgetReallocationEligType)getPickListFactory().getPickListItem( BudgetReallocationEligType.class, code );
  }

  /**
   * Returns null if the defaultItem is not defined - or invalid
   * 
   * @return default PickListItem
   */
  public static BudgetReallocationEligType getDefaultItem()
  {
    return (BudgetReallocationEligType)getPickListFactory().getDefaultPickListItem( BudgetReallocationEligType.class );
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

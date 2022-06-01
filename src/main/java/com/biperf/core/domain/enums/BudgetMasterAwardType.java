
package com.biperf.core.domain.enums;

import java.util.List;

/**
 * Budget master award type. I.e. what kind of asset the budget is for.
 * 
 * @author corneliu
 * @since Apr 29, 2016
 */
public class BudgetMasterAwardType extends PickListItem
{

  private static final long serialVersionUID = 1L;

  /**
   * Asset name used in Content Manager
   */
  private static final String PICKLIST_ASSET = "picklist.budget.master.award.type";

  public static final String POINTS = "points";
  public static final String CASH = "cash";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances
   */
  protected BudgetMasterAwardType()
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
    return getPickListFactory().getPickList( BudgetMasterAwardType.class, new PickListItemSortOrderComparator() );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return PickListItem
   */
  public static BudgetMasterAwardType lookup( String code )
  {
    return (BudgetMasterAwardType)getPickListFactory().getPickListItem( BudgetMasterAwardType.class, code );
  }

  /**
   * Returns null if the defaultItem is not defined - or invalid
   * 
   * @return default PickListItem
   */
  public static BudgetMasterAwardType getDefaultItem()
  {
    return (BudgetMasterAwardType)getPickListFactory().getDefaultPickListItem( BudgetMasterAwardType.class );
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
   * @return True if the code is POINTS
   */
  public boolean isPoints()
  {
    return this.getCode().equals( POINTS );
  }

  /**
   * @return True if the code is CASH
   */
  public boolean isCash()
  {
    return this.getCode().equals( CASH );
  }

}

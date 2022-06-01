
package com.biperf.core.domain.enums;

import java.util.List;

public class GoalROICountType extends PickListItem
{
  /**
   * Asset name used in Content Manager
   */
  private static final String PICKLIST_ASSET = "picklist.goalroicounttype";

  public static final String GOALS_ACHIEVED = "goals_achieved";
  public static final String GOALS_NOT_ACHIEVED_SALES_OVER_BASELINE = "goals_not_achieved_sales_over_baseline";
  public static final String SUBTOTAL = "subtotal";
  public static final String GOALS_NOT_ACHIEVED_SALES_UNDER_BASELINE = "goals_not_achieved_sales_under_baseline";
  public static final String TOTAL = "total";
  public static final String DID_NOT_SELECT_GOAL = "did_not_select_goal";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected GoalROICountType()
  {
    super();
  }

  /**
   * Get the pick list from content manager.
   * 
   * @return List of GoalROICountType
   */
  public static List getList()
  {
    return getPickListFactory().getPickList( GoalROICountType.class, new PickListItemSortOrderComparator() );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return GoalROICountType
   */
  public static GoalROICountType lookup( String code )
  {
    return (GoalROICountType)getPickListFactory().getPickListItem( GoalROICountType.class, code );
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

  public boolean isGoalsAchieved()
  {
    return GOALS_ACHIEVED.equals( getCode() );
  }

  public boolean isGoalsNotAchievedSalesOverBaseline()
  {
    return GOALS_NOT_ACHIEVED_SALES_OVER_BASELINE.equals( getCode() );
  }

  public boolean isGoalsNotAchievedSalesUnderBaseline()
  {
    return GOALS_NOT_ACHIEVED_SALES_UNDER_BASELINE.equals( getCode() );
  }

  public boolean isDidNotSelectGoal()
  {
    return DID_NOT_SELECT_GOAL.equals( getCode() );
  }

  public boolean isSubTotal()
  {
    return SUBTOTAL.equals( getCode() );
  }

  public boolean isTotal()
  {
    return TOTAL.equals( getCode() );
  }

}

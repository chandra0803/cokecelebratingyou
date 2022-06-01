
package com.biperf.core.domain.enums;

import java.util.List;

public class GraphByBehaviorType extends PickListItem
{

  /**
   * Asset name used in Content Manager
   */
  public static final String PICKLIST_ASSET = "picklist.report.graphby.behavior.type";

  public static final String BEHAVIOR_BY_HIERARCHY = "byhierarchy";
  public static final String HIERARCHY_BY_BEHAVIOR = "bybehavior";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected GraphByBehaviorType()
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
    return getPickListFactory().getPickList( GraphByBehaviorType.class );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return PickListItem
   */
  public static GraphByBehaviorType lookup( String code )
  {
    return (GraphByBehaviorType)getPickListFactory().getPickListItem( GraphByBehaviorType.class, code );
  }

  /**
   * Returns null if the defaultItem is not defined - or invalid
   * 
   * @return default PickListItem
   */
  // public static GraphByBehaviorType getDefaultItem()
  // {
  // return (GraphByBehaviorType)getPickListFactory().getDefaultPickListItem(
  // GraphByBehaviorType.class
  // );
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

  public boolean isHierarchyByBehavior()
  {
    return HIERARCHY_BY_BEHAVIOR.equals( getCode() );
  }

  public boolean isBehaviorByHierarchy()
  {
    return BEHAVIOR_BY_HIERARCHY.equals( getCode() );
  }

}

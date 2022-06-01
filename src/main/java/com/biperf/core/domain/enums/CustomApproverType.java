
package com.biperf.core.domain.enums;

import java.util.Arrays;
import java.util.List;

public class CustomApproverType extends PickListItem
{
  /**
   * Asset name used in Content Manager
   */
  private static final String PICKLIST_ASSET = "picklist.customapprover";

  public static final String CHARACTERISTIC = "characteristic";
  public static final String BEHAVIOR = "behavior";
  public static final String AWARD = "award";
  public static final String SPECIFIC_APPROVERS = "specific_approv";

  public static final List<String> eligibleForViewApproverModal = Arrays.asList( CHARACTERISTIC, BEHAVIOR, AWARD );

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  public CustomApproverType()
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
    return getPickListFactory().getPickList( CustomApproverType.class );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return PickListItem
   */
  public static CustomApproverType lookup( String code )
  {
    return (CustomApproverType)getPickListFactory().getPickListItem( CustomApproverType.class, code );
  }

  /**
   * Returns null if the defaultItem is not defined - or invalid
   * 
   * @return default PickListItem
   */
  // public static CustomApproverType getDefaultItem()
  // {
  // return (CustomApproverType)getPickListFactory().getDefaultPickListItem(
  // CustomApproverType.class
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
}

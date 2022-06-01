
package com.biperf.core.domain.enums;

import java.util.List;

/**
 * 
 * SSIApproverType.
 * 
 * @author kandhi
 * @since Nov 4, 2014
 * @version 1.0
 */
public class SSIApproverType extends PickListItem
{
  /**
   * Asset name used in Content Manager
   */
  private static final String PICKLIST_ASSET = "picklist.ssi.approvertype";

  public static final String CONTEST_LEVEL1_APPROVER = "contest_level1_approver";
  public static final String CONTEST_LEVEL2_APPROVER = "contest_level2_approver";
  public static final String CLAIM_APPROVER = "claim_approver";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected SSIApproverType()
  {
    super();
  }

  /**
   * Get the pick list from content manager.
   * 
   * @return List of SSIApproverType
   */
  public static List getList()
  {
    return getPickListFactory().getPickList( SSIApproverType.class );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return SSIApproverType
   */
  public static SSIApproverType lookup( String code )
  {
    return (SSIApproverType)getPickListFactory().getPickListItem( SSIApproverType.class, code );
  }

  /**
   * Returns null if the defaultItem is not defined - or invalid
   * 
   * @return SSIApproverType
   */
  public static SSIApproverType getDefaultItem()
  {
    return (SSIApproverType)getPickListFactory().getDefaultPickListItem( SSIApproverType.class );
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

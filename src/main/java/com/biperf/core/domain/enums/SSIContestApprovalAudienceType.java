
package com.biperf.core.domain.enums;

import java.util.List;

/**
 * 
 * SSIContestApprovalAudienceType.
 * 
 * @author kandhi
 * @since Oct 23, 2014
 * @version 1.0
 */
public class SSIContestApprovalAudienceType extends PickListItem
{

  /**
   * Asset name used in Content Manager
   */
  private static final String PICKLIST_ASSET = "picklist.ssi.contestapproval.audience";

  public static final String SPECIFY_AUDIENCE_CODE = "specifyaudience";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected SSIContestApprovalAudienceType()
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
    return getPickListFactory().getPickList( SSIContestApprovalAudienceType.class );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return PickListItem
   */
  public static SSIContestApprovalAudienceType lookup( String code )
  {
    return (SSIContestApprovalAudienceType)getPickListFactory().getPickListItem( SSIContestApprovalAudienceType.class, code );
  }

  /**
   * 
   * @return true if this object represents the "specifyaudience"; false
   *         otherwise.
   */
  public boolean isSpecifyAudienceType()
  {
    return getCode().equalsIgnoreCase( SPECIFY_AUDIENCE_CODE );
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

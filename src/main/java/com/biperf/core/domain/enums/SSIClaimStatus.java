
package com.biperf.core.domain.enums;

import java.util.List;

/**
 * 
 * 
 * @author kancherl
 * @since May 19, 2015
 * @version 1.0
 */

public class SSIClaimStatus extends PickListItem
{

  private static final long serialVersionUID = 1L;

  /**
   * Asset name used in Content Manager
   */
  private static final String PICKLIST_ASSET = "picklist.ssi.claim.status";

  public static final String WAITING_FOR_APPROVAL = "waiting_for_approval";
  public static final String DENIED = "denied";
  public static final String APPROVED = "approved";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected SSIClaimStatus()
  {
    super();
  }

  /**
   * Get the pick list from content manager.
   * 
   * @return List of SSIClaimStatus
   */
  public static List getList()
  {
    return getPickListFactory().getPickList( SSIContestStatus.class );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return SSIClaimStatus
   */
  public static SSIClaimStatus lookup( String code )
  {
    return (SSIClaimStatus)getPickListFactory().getPickListItem( SSIClaimStatus.class, code );
  }

  /**
   * Returns null if the defaultItem is not defined - or invalid
   * 
   * @return SSIClaimStatus
   */
  public static SSIClaimStatus getDefaultItem()
  {
    return (SSIClaimStatus)getPickListFactory().getDefaultPickListItem( SSIClaimStatus.class );
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
   * 
   * @return true if this object represents the "approved" status; false otherwise.
   */
  public boolean isApproved()
  {
    return getCode().equalsIgnoreCase( APPROVED );
  }

  /**
   * 
   * @return true if this object represents the "pending" status; false otherwise.
   */
  public boolean isWaitingForApproval()
  {
    return getCode().equalsIgnoreCase( WAITING_FOR_APPROVAL );
  }

  /**
   * 
   * @return true if this object represents the "denied" status; false otherwise.
   */
  public boolean isDenied()
  {
    return getCode().equalsIgnoreCase( DENIED );
  }

}

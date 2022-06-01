
package com.biperf.core.domain.enums;

import java.util.List;

/**
 * 
 * SSIClaimApprovalSSIClaimApprovalAudienceType.
 * 
 * @author kandhi
 * @since Oct 23, 2014
 * @version 1.0
 */
public class SSIClaimApprovalAudienceType extends PickListItem
{

  /**
   * Asset name used in Content Manager
   */
  private static final String PICKLIST_ASSET = "picklist.ssi.claimapproval.audience";

  public static final String SPECIFY_AUDIENCE_CODE = "specifyaudience";
  public static final String CREATOR_ORG_AND_ABOVE_AUDIENCE_CODE = "creatororgandaboveaudience";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected SSIClaimApprovalAudienceType()
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
    return getPickListFactory().getPickList( SSIClaimApprovalAudienceType.class );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return PickListItem
   */
  public static SSIClaimApprovalAudienceType lookup( String code )
  {
    return (SSIClaimApprovalAudienceType)getPickListFactory().getPickListItem( SSIClaimApprovalAudienceType.class, code );
  }

  /**
   * 
   * @return true if this object represents the "creatororgandaboveaudience"; false
   *         otherwise.
   */
  public boolean isCreatorOrgAndAboveAudienceType()
  {
    return getCode().equalsIgnoreCase( CREATOR_ORG_AND_ABOVE_AUDIENCE_CODE );
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

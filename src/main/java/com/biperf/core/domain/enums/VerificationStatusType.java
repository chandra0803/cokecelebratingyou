
package com.biperf.core.domain.enums;

import java.util.List;

/**
 * Email / Phone number verification
 */
@SuppressWarnings( "serial" )
public class VerificationStatusType extends PickListItem
{

  /**
   * Asset name used in Content Manager
   */
  private static final String PICKLIST_ASSET = "picklist.verification.status.type";

  public static final String VERIFIED = "verified";
  public static final String UNVERIFIED = "unverified";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected VerificationStatusType()
  {
    super();
  }

  /**
   * Get the pick list from content manager.
   * 
   * @return List
   */
  @SuppressWarnings( "unchecked" )
  public static List<VerificationStatusType> getList()
  {
    return getPickListFactory().getPickList( VerificationStatusType.class );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return PickListItem
   */
  public static VerificationStatusType lookup( String code )
  {
    return (VerificationStatusType)getPickListFactory().getPickListItem( VerificationStatusType.class, code );
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

package com.biperf.core.domain.enums;

import java.util.List;

public class TcccClaimFileStatusType extends PickListItem
{
  /**
   * Asset name used in Content Manager
   */
  private static final String PICKLIST_ASSET = "picklist.tccc.claim.file.status.type";

  public static final String ACTIVE = "active";
  public static final String INACTIVE = "inactive";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected TcccClaimFileStatusType()
  {
    super();
  }

  /**
   * Get the pick list from content manager.
   * 
   * @return List of MCSubjectType
   */
  public static List getList()
  {
    return getPickListFactory().getPickList( TcccClaimFileStatusType.class );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return MCSubjectType
   */
  public static TcccClaimFileStatusType lookup( String code )
  {
    return (TcccClaimFileStatusType)getPickListFactory().getPickListItem( TcccClaimFileStatusType.class, code );
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

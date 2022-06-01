
package com.biperf.core.domain.enums;

import java.util.List;

public class SubmittedType extends PickListItem
{

  /**
   * Asset name used in Content Manager
   */
  public static final String PICKLIST_ASSET = "picklist.submitted.type";

  public static final String SUBMITTED = "have";
  public static final String NOT_SUBMITTED = "haveNot";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected SubmittedType()
  {
    super();
  }

  /**
   * Get the pick list from content manager.
   * 
   * @return List of SubmittedType
   */
  public static List getList()
  {
    return getPickListFactory().getPickList( SubmittedType.class );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return SubmittedType
   */
  public static SubmittedType lookup( String code )
  {
    return (SubmittedType)getPickListFactory().getPickListItem( SubmittedType.class, code );
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

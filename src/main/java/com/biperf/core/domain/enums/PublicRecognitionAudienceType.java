
package com.biperf.core.domain.enums;

import java.util.List;

public class PublicRecognitionAudienceType extends PickListItem
{
  private static final long serialVersionUID = 1L;

  private static final String PICKLIST_ASSET = "picklist.publicrecognitionaudience.type";

  public static final String ALL_ACTIVE_PAX_CODE = "allactivepaxaudience";
  public static final String CREATE_AUDIENCE_CODE = "promopublicrecgaudience";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected PublicRecognitionAudienceType()
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
    return getPickListFactory().getPickList( PublicRecognitionAudienceType.class );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return PickListItem
   */
  public static PublicRecognitionAudienceType lookup( String code )
  {
    return (PublicRecognitionAudienceType)getPickListFactory().getPickListItem( PublicRecognitionAudienceType.class, code );
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

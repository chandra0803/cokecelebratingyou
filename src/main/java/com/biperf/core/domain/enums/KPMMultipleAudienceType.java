
package com.biperf.core.domain.enums;

import java.util.List;

public class KPMMultipleAudienceType extends PickListItem
{

  /**
   * Asset name used in Content Manager
   */
  public static final String PICKLIST_ASSET = "picklist.kpm.audiencetype";

  public static final String LOW_EXPECTATION_SCORE = "lowexpectationscore";
  public static final String HIGH_EXPECTATION_SCORE = "highexpectationscore";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected KPMMultipleAudienceType()
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
    return getPickListFactory().getPickList( KPMMultipleAudienceType.class );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return SubmittedType
   */
  public static KPMMultipleAudienceType lookup( String code )
  {
    return (KPMMultipleAudienceType)getPickListFactory().getPickListItem( KPMMultipleAudienceType.class, code );
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

/**
 * 
 */

package com.biperf.core.domain.enums;

import java.util.List;

public class SurveyResponseType extends PickListItem
{

  /**
   * Asset name used in Content Manager
   */
  public static final String PICKLIST_ASSET = "picklist.survey.response.type";

  public static final String STANDARD_RESPONSE = "standardResponse";
  public static final String OPEN_ENDED = "openEnded";
  public static final String SLIDER_SELECTION = "sliderSelection";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected SurveyResponseType()
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
    return getPickListFactory().getPickList( SurveyResponseType.class );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return PickListItem
   */
  public static SurveyResponseType lookup( String code )
  {
    return (SurveyResponseType)getPickListFactory().getPickListItem( SurveyResponseType.class, code );
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

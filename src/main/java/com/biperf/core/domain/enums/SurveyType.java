
package com.biperf.core.domain.enums;

import java.util.List;

public class SurveyType extends PickListItem
{

  private static final String PICKLIST_ASSET = "picklist.survey.type";

  public static final String FIXED = "fixed";
  public static final String RANDOM = "random";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected SurveyType()
  {
    super();
  }

  /**
   * Get the pick list from content manager.
   * 
   * @return List of QuizType
   */
  public static List getList()
  {
    return getPickListFactory().getPickList( SurveyType.class );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return QuizType
   */
  public static SurveyType lookup( String code )
  {
    return (SurveyType)getPickListFactory().getPickListItem( SurveyType.class, code );
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

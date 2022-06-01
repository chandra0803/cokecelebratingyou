
package com.biperf.core.domain.enums;

import java.util.List;

public class SurveyQuestionStatusType extends PickListItem
{
  /**
   * Asset name used in Content Manager
   */
  private static final String PICKLIST_ASSET = "picklist.survey.question.status.type";
  public static final String ACTIVE = "active";
  public static final String INACTIVE = "inactive";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected SurveyQuestionStatusType()
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
    return getPickListFactory().getPickList( SurveyQuestionStatusType.class );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return PickListItem
   */
  public static SurveyQuestionStatusType lookup( String code )
  {
    return (SurveyQuestionStatusType)getPickListFactory().getPickListItem( SurveyQuestionStatusType.class, code );
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

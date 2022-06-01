
package com.biperf.core.domain.enums;

import java.util.List;

public class DIYQuizStatusType extends PickListItem
{
  /**
   * Asset name used in Content Manager
   */
  private static final String PICKLIST_ASSET = "picklist.quiz.diy.status.type";
  public static final String ACTIVE = "active";
  public static final String INACTIVE = "inactive";
  public static final String INCOMPLETE = "incomplete";
  public static final String PENDING = "pending";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected DIYQuizStatusType()
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
    return getPickListFactory().getPickList( DIYQuizStatusType.class );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return PickListItem
   */
  public static DIYQuizStatusType lookup( String code )
  {
    return (DIYQuizStatusType)getPickListFactory().getPickListItem( DIYQuizStatusType.class, code );
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

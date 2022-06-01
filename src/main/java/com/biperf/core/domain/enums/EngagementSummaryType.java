
package com.biperf.core.domain.enums;

import java.util.List;

public class EngagementSummaryType extends PickListItem
{

  /**
   * Asset name used in Content Manager
   */
  public static final String PICKLIST_ASSET = "picklist.engagement.summary.type";

  public static final String PARTICIPATION_SCORE = "score";
  public static final String RECOGNITIONS_SENT = "recSent";
  public static final String RECOGNITIONS_RECEIVED = "recRecv";
  public static final String RECOGNIZED = "paxRecTo";
  public static final String RECOGNIZED_BY = "paxRecBy";
  public static final String VISITS = "visits";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected EngagementSummaryType()
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
    return getPickListFactory().getPickList( EngagementSummaryType.class );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return PickListItem
   */
  public static EngagementSummaryType lookup( String code )
  {
    return (EngagementSummaryType)getPickListFactory().getPickListItem( EngagementSummaryType.class, code );
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

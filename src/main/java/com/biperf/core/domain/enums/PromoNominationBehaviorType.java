/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source$
 *
 */

package com.biperf.core.domain.enums;

import java.util.List;

/**
 * PromoNominationBehaviorType <p/> <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>kumar</td>
 * <td>Oct 10, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 * @author dunne
 *
 */
public class PromoNominationBehaviorType extends PromotionBehaviorType
{

  /**
   * Asset name used in Content Manager
   */
  public static final String PICKLIST_ASSET = "picklist.promo.nomination.behavior";

  public static final String INNOVATION_CODE = "innovation";
  public static final String GREAT_IDEA_CODE = "greatidea";
  public static final String ABOVE_BEYOND_CODE = "abovebeyond";
  public static final String LEADERSHIP_CODE = "leadership";
  public static final String OUTSTANDING_CODE = "outstanding";
  public static final String SUPPORTIVE_CODE = "supportive";
  public static final String TEAM_PLAYER_CODE = "teamplayer";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected PromoNominationBehaviorType()
  {
    super();
  }

  /**
   * Get the pick list from content manager.
   * 
   * @return List of PromoNominationBehaviorType
   */
  public static List getList()
  {
    return getPickListFactory().getPickList( PromoNominationBehaviorType.class, new PickListItemNameComparator() );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return PromoNominationBehaviorType
   */
  public static PromoNominationBehaviorType lookup( String code )
  {
    return (PromoNominationBehaviorType)getPickListFactory().getPickListItem( PromoNominationBehaviorType.class, code );
  }

  /**
   * Returns null if the defaultItem is not defined - or invalid
   * 
   * @return PromoNominationBehaviorType
   */
  public static PromoNominationBehaviorType getDefaultItem()
  {
    return (PromoNominationBehaviorType)getPickListFactory().getDefaultPickListItem( PromoNominationBehaviorType.class );
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

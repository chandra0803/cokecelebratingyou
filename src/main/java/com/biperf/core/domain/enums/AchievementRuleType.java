/**
 * 
 */

package com.biperf.core.domain.enums;

import java.util.List;

/**
 * AchievementRuleType.
 * <p>
 * <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>sedey</td>
 * <td>May 29, 2008</td>8
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class AchievementRuleType extends PickListItem
{

  /**
   * Asset name used in Content Manager
   */
  public static final String PICKLIST_ASSET = "picklist.achievement.rule.type";

  public static final String FIXED = "fixed";
  public static final String PERCENT_OF_BASE = "perofbase";
  public static final String BASE_PLUS_FIXED = "baseFixed";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected AchievementRuleType()
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
    return getPickListFactory().getPickList( AchievementRuleType.class );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return PickListItem
   */
  public static AchievementRuleType lookup( String code )
  {
    return (AchievementRuleType)getPickListFactory().getPickListItem( AchievementRuleType.class, code );
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

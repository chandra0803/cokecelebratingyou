
package com.biperf.core.domain.enums;

import java.util.List;

/**
 * PromotionNotificationFrequencyType.
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
 * <td>Tammy Cheng</td>
 * <td>Aug 22, 2006</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class PromotionNotificationFrequencyType extends PickListItem
{
  public static final String DAILY = "daily";
  public static final String WEEKLY = "weekly";
  public static final String MONTHLY = "monthly";
  public static final String NO_SCHEDULE = "noSchedule";

  /**
   * Asset name used in Content Manager
   */
  private static final String PICKLIST_ASSET = "picklist.approverReminder.frequency.type";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected PromotionNotificationFrequencyType()
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
    return getPickListFactory().getPickList( PromotionNotificationFrequencyType.class );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return PickListItem
   */
  public static PromotionNotificationFrequencyType lookup( String code )
  {
    return (PromotionNotificationFrequencyType)getPickListFactory().getPickListItem( PromotionNotificationFrequencyType.class, code );
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

  public boolean isDaily()
  {
    return DAILY.equals( getCode() );
  }

  public boolean isWeekly()
  {
    return WEEKLY.equals( getCode() );
  }

  public boolean isMonthly()
  {
    return MONTHLY.equals( getCode() );
  }

  public boolean isNoSchedule()
  {
    return NO_SCHEDULE.equals( getCode() );
  }
}

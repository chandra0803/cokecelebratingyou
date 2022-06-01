
package com.biperf.core.domain.enums;

/**
 * 
 */
import java.util.List;

/**
 * BadgeType.
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
 * <td>sharafud</td>
 * <td>Aug 22, 2012</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class BadgeType extends ModuleAwarePickListItem
{
  /**
   * Asset name used in Content Manager
   */
  private static final String PICKLIST_ASSET = "picklist.badgetype";
  public static final String PROGRESS = "progress";
  public static final String BEHAVIOR = "behavior";
  public static final String FILELOAD = "fileload";
  public static final String EARNED_OR_NOT_EARNED = "earned";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected BadgeType()
  {
    super();
  }

  /**
   * Get the pick list from content manager.
   * 
   * @return List of PromotionType
   */
  public static List getList()
  {
    return getPickListFactory().getPickList( BadgeType.class );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return PromotionType
   */
  public static BadgeType lookup( String code )
  {
    return (BadgeType)getPickListFactory().getPickListItem( BadgeType.class, code );
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

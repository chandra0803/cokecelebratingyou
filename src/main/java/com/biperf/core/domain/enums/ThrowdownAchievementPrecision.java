
package com.biperf.core.domain.enums;

import java.util.List;

/**
 * AchievementPrecision.
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
 * <td>meadows</td>
 * <td>Dec 6, 2006</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
@SuppressWarnings( "serial" )
public class ThrowdownAchievementPrecision extends PickListItem
{

  /**
   * Asset name used in Content Manager
   */
  private static final String PICKLIST_ASSET = "picklist.throwdown.achievementprecision";

  public static final String ZERO = "zero";
  public static final String ONE = "one";
  public static final String TWO = "two";
  public static final String THREE = "three";
  public static final String FOUR = "four";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected ThrowdownAchievementPrecision()
  {
    super();
  }

  /**
   * Get the pick list from content manager.
   * 
   * @return List
   */
  @SuppressWarnings( "rawtypes" )
  public static List getList()
  {
    return getPickListFactory().getPickList( ThrowdownAchievementPrecision.class );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return PickListItem
   */
  public static ThrowdownAchievementPrecision lookup( String code )
  {
    return (ThrowdownAchievementPrecision)getPickListFactory().getPickListItem( ThrowdownAchievementPrecision.class, code );
  }

  /**
   * Returns null if the defaultItem is not defined - or invalid
   * 
   * @return PayoutStructure
   */
  public static ThrowdownAchievementPrecision getDefaultItem()
  {
    return (ThrowdownAchievementPrecision)getPickListFactory().getDefaultPickListItem( ThrowdownAchievementPrecision.class );
  }

  public int getPrecision()
  {
    if ( getCode() != null && getCode().equals( ONE ) )
    {
      return 1;
    }
    if ( getCode() != null && getCode().equals( TWO ) )
    {
      return 2;
    }
    if ( getCode() != null && getCode().equals( THREE ) )
    {
      return 3;
    }
    if ( getCode() != null && getCode().equals( FOUR ) )
    {
      return 4;
    }
    return 0;
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

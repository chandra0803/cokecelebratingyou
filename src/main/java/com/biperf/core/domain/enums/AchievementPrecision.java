/*
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/domain/enums/AchievementPrecision.java,v $
 * (c) 2005 BI, Inc.  All rights reserved.
 */

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
public class AchievementPrecision extends PickListItem
{

  /**
   * Asset name used in Content Manager
   */
  private static final String PICKLIST_ASSET = "picklist.goalquest.achievementprecision";

  public static final String ZERO = "zero";
  public static final String ONE = "one";
  public static final String TWO = "two";
  public static final String THREE = "three";
  public static final String FOUR = "four";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected AchievementPrecision()
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
    return getPickListFactory().getPickList( AchievementPrecision.class );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return PickListItem
   */
  public static AchievementPrecision lookup( String code )
  {
    return (AchievementPrecision)getPickListFactory().getPickListItem( AchievementPrecision.class, code );
  }

  /**
   * Returns null if the defaultItem is not defined - or invalid
   * 
   * @return PayoutStructure
   */
  public static AchievementPrecision getDefaultItem()
  {
    return (AchievementPrecision)getPickListFactory().getDefaultPickListItem( AchievementPrecision.class );
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

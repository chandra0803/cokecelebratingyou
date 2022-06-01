/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/domain/enums/CommLogUrgencyType.java,v $
 *
 */

package com.biperf.core.domain.enums;

import java.util.List;

/**
 * CommLogUrgencyType <p/> <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>dunne</td>
 * <td>Nov 18, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 * @author dunne
 *
 */
public class CommLogUrgencyType extends PickListItem
{

  /**
   * Asset name used in Content Manager
   */
  private static final String PICKLIST_ASSET = "picklist.commlog.urgency.type";

  public static final String NORMAL_CODE = "normal";
  public static final String ESCALATED_COLD_CODE = "esc_cold";
  public static final String ESCALATED_WARM_CODE = "esc_warm";
  public static final String ESCALATED_HOT_CODE = "esc_hot";
  public static final String DEFERRED_CODE = "deferred";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected CommLogUrgencyType()
  {
    super();
  }

  /**
   * Get the pick list from content manager.
   * 
   * @return List of CommLogUrgencyType
   */
  public static List getList()
  {
    return getPickListFactory().getPickList( CommLogUrgencyType.class );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return CommLogUrgencyType
   */
  public static CommLogUrgencyType lookup( String code )
  {
    return (CommLogUrgencyType)getPickListFactory().getPickListItem( CommLogUrgencyType.class, code );
  }

  /**
   * Returns null if the defaultItem is not defined - or invalid
   * 
   * @return CommLogUrgencyType
   */
  public static CommLogUrgencyType getDefaultItem()
  {
    return (CommLogUrgencyType)getPickListFactory().getDefaultPickListItem( CommLogUrgencyType.class );
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

  public boolean isDeferred()
  {
    return DEFERRED_CODE.equals( getCode() );
  }

  public boolean isNormalUrgency()
  {
    return NORMAL_CODE.equals( getCode() );
  }

  public boolean isEscalatedWarmUrgency()
  {
    return ESCALATED_WARM_CODE.equals( getCode() );
  }

  public boolean isEscalatedColdUrgency()
  {
    return ESCALATED_COLD_CODE.equals( getCode() );
  }

  public boolean isEscalatedHotUrgency()
  {
    return ESCALATED_HOT_CODE.equals( getCode() );
  }

  public boolean isEscalated()
  {
    return isEscalatedColdUrgency() || isEscalatedWarmUrgency() || isEscalatedHotUrgency();
  }
}

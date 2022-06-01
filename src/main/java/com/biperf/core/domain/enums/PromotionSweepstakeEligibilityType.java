/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/domain/enums/PromotionSweepstakeEligibilityType.java,v $
 */

package com.biperf.core.domain.enums;

import java.util.List;

/**
 * PromotionSweepstakeEligibilityType.
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
 * <td>jenniget</td>
 * <td>Oct 12, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class PromotionSweepstakeEligibilityType extends PickListItem
{

  /**
   * Asset name used in Content Manager
   */
  private static final String PICKLIST_ASSET = "picklist.sweepstaketype";

  // Codes
  public static final String GIVERS_ONLY_CODE = "givers";
  public static final String GIVERS_AND_RECEIVERS_COMBINED_CODE = "combined";
  public static final String GIVERS_AND_RECEIVERS_SEPARATE_CODE = "separate";
  public static final String RECEIVERS_ONLY_CODE = "receivers";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected PromotionSweepstakeEligibilityType()
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
    return getPickListFactory().getPickList( PromotionSweepstakeEligibilityType.class );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return PickListItem
   */
  public static PromotionSweepstakeEligibilityType lookup( String code )
  {
    return (PromotionSweepstakeEligibilityType)getPickListFactory().getPickListItem( PromotionSweepstakeEligibilityType.class, code );
  }

  /**
   * Returns null if the defaultItem is not defined - or invalid
   * 
   * @return default PickListItem
   */
  // public static PromotionAudienceType getDefaultItem()
  // {
  // return (PromotionAudienceType)getPickListFactory().getDefaultPickListItem(
  // PromotionAudienceType.class );
  // }
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

  public boolean isGiversOnly()
  {
    return this.getCode().equals( GIVERS_ONLY_CODE );
  }

  public boolean isGiversReceiversCombined()
  {
    return this.getCode().equals( GIVERS_AND_RECEIVERS_COMBINED_CODE );
  }

  public boolean isGiversReceiversSeparate()
  {
    return this.getCode().equals( GIVERS_AND_RECEIVERS_SEPARATE_CODE );
  }

  public boolean isReceiversOnly()
  {
    return this.getCode().equals( RECEIVERS_ONLY_CODE );
  }

}

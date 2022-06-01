/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/domain/enums/CommLogCategoryType.java,v $
 *
 */

package com.biperf.core.domain.enums;

import java.util.List;

/**
 * CommLogCategoryType <p/> <b>Change History:</b><br>
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
public class CommLogCategoryType extends PickListItem
{
  /*
   * Promotional Mailings Outbound mailings in support of client promotions. BMS All online
   * communications with the participant in BMS Quiz/Survey Communications for Quizzes and Surveys
   * Participant Feedback System Dispositions for Customer Service Reps ECard Electronic Recognition
   * Card PerformanceLinQXL Communications for PerformanceLinQXL processing
   */

  public static String PROMO_MAILING = "promo_mailing";
  public static String ECARD = "ecard";
  public static String PAX_FEEDBACK = "pax_feedback";

  /**
   * Asset name used in Content Manager
   */
  private static final String PICKLIST_ASSET = "picklist.commlog.category.type";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected CommLogCategoryType()
  {
    super();
  }

  /**
   * Get the pick list from content manager.
   * 
   * @return List of CommLogCategoryType
   */
  public static List getList()
  {
    return getPickListFactory().getPickList( CommLogCategoryType.class );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return CommLogCategoryType
   */
  public static CommLogCategoryType lookup( String code )
  {
    return (CommLogCategoryType)getPickListFactory().getPickListItem( CommLogCategoryType.class, code );
  }

  /**
   * Returns null if the defaultItem is not defined - or invalid
   * 
   * @return CommLogCategoryType
   */
  public static CommLogCategoryType getDefaultItem()
  {
    return (CommLogCategoryType)getPickListFactory().getDefaultPickListItem( CommLogCategoryType.class );
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

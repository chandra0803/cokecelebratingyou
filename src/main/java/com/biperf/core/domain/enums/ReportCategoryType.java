/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/domain/enums/ReportCategoryType.java,v $
 *
 */

package com.biperf.core.domain.enums;

import java.util.List;

/**
 * ReportCategoryType <p/> <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>drahn</td>
 * <td>Aug 20, 2012</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 * @author drahn
 *
 */
public class ReportCategoryType extends PickListItem
{
  public static String CORE = "core";
  public static String CLAIM = "claim";
  public static String GQ_CP = "gq_cp";
  public static String GQ = "gq";
  public static String NOMINATION = "nomination";
  public static String QUIZZES = "quizzes";
  public static String RECOGNITION = "recognition";
  public static String CP = "cp";
  public static String THROWDOWN = "throwdown";
  public static String SURVEY = "survey";
  public static String WORKHAPPIER = "workhappier";

  /**
   * Asset name used in Content Manager
   */
  private static final String PICKLIST_ASSET = "picklist.reportcategorytype";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected ReportCategoryType()
  {
    super();
  }

  /**
   * Get the pick list from content manager.
   * 
   * @return List of ReportCategoryType
   */
  public static List getList()
  {
    return getPickListFactory().getPickList( ReportCategoryType.class );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return ReportCategoryType
   */
  public static ReportCategoryType lookup( String code )
  {
    return (ReportCategoryType)getPickListFactory().getPickListItem( ReportCategoryType.class, code );
  }

  /**
   * Returns null if the defaultItem is not defined - or invalid
   * 
   * @return ReportCategoryType
   */
  public static ReportCategoryType getDefaultItem()
  {
    return (ReportCategoryType)getPickListFactory().getDefaultPickListItem( ReportCategoryType.class );
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

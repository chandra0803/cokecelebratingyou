/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/domain/enums/ReportTableRefreshType.java,v $
 *
 */

package com.biperf.core.domain.enums;

import java.util.List;

/**
 * CommLogReasonType <p/> <b>Change History:</b><br>
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
public class ReportTableRefreshType extends ModuleAwarePickListItem
{
  public static String RECOGNITION = "recognition"; // adhoc email
  // public static String NOMINATION = "nomination"; // promotion emailing
  public static String PRODUCT_CLAIM = "product_claim"; // promotion emailing failure
  public static String QUIZ = "quiz";
  public static String GOALQUEST = "goalquest";
  // public static String IDEAS = "idea"; // adhoc email
  // public static String SURVEY = "survey";
  // public static String NEW_HIRE = "new_hire"; //
  // public static String SIX_SIGMA = "six_sigma"; //

  /**
   * Asset name used in Content Manager
   */
  private static final String PICKLIST_ASSET = "picklist.reporttablerefreshtype";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected ReportTableRefreshType()
  {
    super();
  }

  /**
   * Get the pick list from content manager.
   * 
   * @return List of CommLogReasonType
   */
  public static List getList()
  {
    return getPickListFactory().getPickList( ReportTableRefreshType.class );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return CommLogReasonType
   */
  public static ReportTableRefreshType lookup( String code )
  {
    return (ReportTableRefreshType)getPickListFactory().getPickListItem( ReportTableRefreshType.class, code );
  }

  /**
   * Returns null if the defaultItem is not defined - or invalid
   * 
   * @return CommLogReasonType
   */
  public static ReportTableRefreshType getDefaultItem()
  {
    return (ReportTableRefreshType)getPickListFactory().getDefaultPickListItem( ReportTableRefreshType.class );
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

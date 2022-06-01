/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/domain/enums/CommLogReasonType.java,v $
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
public class CommLogReasonType extends CommLogReasonPickListItem
{

  public static String EMAIL_WIZARD = "emailwizard"; // adhoc email
  public static String EMAIL_WIZARD_FAILURE = "emailwizardfailure";
  public static String EMAIL_PROMOTION = "emailpromotion"; // promotion emailing
  public static String EMAIL_PROMOTION_FAILURE = "emailpromotionfailure"; // promotion emailing
  // failure
  public static String EMAIL_OTHER = "emailother";
  public static String EMAIL_OTHER_FAILURE = "emailotherfailure";

  /**
   * Asset name used in Content Manager
   */
  private static final String PICKLIST_ASSET = "picklist.commlog.reason.type";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected CommLogReasonType()
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
    return getPickListFactory().getPickList( CommLogReasonType.class );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return CommLogReasonType
   */
  public static CommLogReasonType lookup( String code )
  {
    return (CommLogReasonType)getPickListFactory().getPickListItem( CommLogReasonType.class, code );
  }

  /**
   * Returns null if the defaultItem is not defined - or invalid
   * 
   * @return CommLogReasonType
   */
  public static CommLogReasonType getDefaultItem()
  {
    return (CommLogReasonType)getPickListFactory().getDefaultPickListItem( CommLogReasonType.class );
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

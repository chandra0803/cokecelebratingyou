/*
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/domain/enums/WebRulesAudienceType.java,v $
 * (c) 2005 BI, Inc.  All rights reserved.
 */

package com.biperf.core.domain.enums;

import java.util.List;

/**
 * WebRulesAudienceType.
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
 * <td>Oct 10, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class WebRulesAudienceType extends PickListItem
{

  /**
   * Asset name used in Content Manager
   */
  private static final String PICKLIST_ASSET = "picklist.webrulesaudiencetype";

  public static final String ALL_ACTIVE_PAX_CODE = "allactivepaxaudience";
  public static final String SAME_AS_PRIMARY_CODE = "sameasprimaryaudience";
  public static final String ALL_ELIGIBLE_PRIMARY_CODE = "alleligibleprimaryaudience";
  public static final String ALL_ELIGIBLE_SECONDARY_CODE = "alleligiblesecondaryaudience";
  public static final String ALL_ELIGIBLE_PRIMARY_AND_SECONDARY_CODE = "alleligibleprimaryandsecondaryaudience";
  public static final String CREATE_AUDIENCE_CODE = "promowebrulesaudience";
  public static final String ALL_SUBMITTER_CODE = "allsubmitter";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected WebRulesAudienceType()
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
    return getPickListFactory().getPickList( WebRulesAudienceType.class );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return PickListItem
   */
  public static WebRulesAudienceType lookup( String code )
  {
    return (WebRulesAudienceType)getPickListFactory().getPickListItem( WebRulesAudienceType.class, code );
  }

  /**
   * Returns null if the defaultItem is not defined - or invalid
   * 
   * @return default PickListItem
   */
  // public static AudienceType getDefaultItem()
  // {
  // return (AudienceType)getPickListFactory().getDefaultPickListItem( AudienceType.class );
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
}

/*
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/domain/enums/Attic/PartnerWebRulesAudienceType.java,v $
 * (c) 2005 BI, Inc.  All rights reserved.
 */

package com.biperf.core.domain.enums;

import java.util.List;

/**
 * PartnerWebRulesAudienceType.
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
public class PartnerWebRulesAudienceType extends PickListItem
{

  /**
   * Asset name used in Content Manager
   */
  private static final String PICKLIST_ASSET = "picklist.webrulespartneraudiencetype";

  public static final String ALL_ACTIVE_PAX_CODE = "allactivepartneraudience";
  public static final String ALL_ELIGIBLE_PRIMARY_AND_SECONDARY_CODE = "partnereligibleprimaryandsecondaryaudience";
  public static final String CREATE_AUDIENCE_CODE = "promopartnerwebrulesaudience";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected PartnerWebRulesAudienceType()
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
    return getPickListFactory().getPickList( PartnerWebRulesAudienceType.class );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return PickListItem
   */
  public static PartnerWebRulesAudienceType lookup( String code )
  {
    return (PartnerWebRulesAudienceType)getPickListFactory().getPickListItem( PartnerWebRulesAudienceType.class, code );
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

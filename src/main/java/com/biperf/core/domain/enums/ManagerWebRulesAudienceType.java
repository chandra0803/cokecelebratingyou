/*
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/domain/enums/Attic/ManagerWebRulesAudienceType.java,v $
 * (c) 2005 BI, Inc.  All rights reserved.
 */

package com.biperf.core.domain.enums;

import java.util.List;

/**
 * ManagerWebRulesAudienceType.
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
public class ManagerWebRulesAudienceType extends PickListItem
{

  /**
   * Asset name used in Content Manager
   */
  private static final String PICKLIST_ASSET = "picklist.webrulesmanageraudiencetype";

  public static final String ALL_ACTIVE_PAX_CODE = "allactivemanageraudience";
  public static final String ALL_ELIGIBLE_PRIMARY_AND_SECONDARY_CODE = "managereligibleprimaryandsecondaryaudience";
  public static final String CREATE_AUDIENCE_CODE = "promomanagerwebrulesaudience";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected ManagerWebRulesAudienceType()
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
    return getPickListFactory().getPickList( ManagerWebRulesAudienceType.class );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return PickListItem
   */
  public static ManagerWebRulesAudienceType lookup( String code )
  {
    return (ManagerWebRulesAudienceType)getPickListFactory().getPickListItem( ManagerWebRulesAudienceType.class, code );
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

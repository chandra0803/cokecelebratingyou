/*
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/domain/enums/SecondaryAudienceType.java,v $
 * (c) 2005 BI, Inc.  All rights reserved.
 */

package com.biperf.core.domain.enums;

import java.util.List;

/**
 * SecondaryAudienceType.
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
public class SecondaryAudienceType extends PickListItem
{

  /**
   * Asset name used in Content Manager
   */
  private static final String PICKLIST_ASSET = "picklist.secondaryaudiencetype";

  public static final String ALL_ACTIVE_PAX_CODE = "allactivepaxaudience";
  public static final String SAME_AS_PRIMARY_CODE = "sameasprimaryaudience";
  public static final String ACTIVE_PAX_PRIMARY_NODE_CODE = "activepaxfromprimarynodeaudience";
  public static final String ACTIVE_PAX_PRIMARY_NODE_BELOW_CODE = "activepaxfromprimarynodebelowaudience";
  public static final String SPECIFY_AUDIENCE_CODE = "specifyaudience";
  public static final String CREATOR_ORG_ONLY_CODE = "creatororgonlyaudience";
  public static final String CREATOR_ORG_AND_BELOW_CODE = "creatororgandbelowaudience";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected SecondaryAudienceType()
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
    return getPickListFactory().getPickList( SecondaryAudienceType.class );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return PickListItem
   */
  public static SecondaryAudienceType lookup( String code )
  {
    return (SecondaryAudienceType)getPickListFactory().getPickListItem( SecondaryAudienceType.class, code );
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

  /**
   * Returns true if this object represents the "specify audiences" secondary audience type; returns
   * false otherwise.
   * 
   * @return true if this object represents the "specify audiences" secondary audience type; false
   *         otherwise.
   */
  public boolean isSpecifyAudienceType()
  {
    return getCode().equalsIgnoreCase( SPECIFY_AUDIENCE_CODE );
  }

  /**
   * Returns true if this object represents the "givers node" secondary audience type; returns
   * false otherwise.
   * 
   * @return true if this object represents the "givers node" secondary audience type; false
   *         otherwise.
   */
  public boolean isSpecificNodeType()
  {
    return getCode().equalsIgnoreCase( ACTIVE_PAX_PRIMARY_NODE_CODE );
  }

  /**
   * Returns true if this object represents the "givers node and below" secondary audience type; returns
   * false otherwise.
   * 
   * @return true if this object represents the "givers node and below" secondary audience type; false
   *         otherwise.
   */
  public boolean isSpecificNodeAndBelowType()
  {
    return getCode().equalsIgnoreCase( ACTIVE_PAX_PRIMARY_NODE_BELOW_CODE );
  }

  /**
   * Returns true if this object represents the "same as primary" secondary audience type; returns
   * false otherwise.
   * 
   * @return true if this object represents the "same as primary" secondary audience type; false
   *         otherwise.
   */
  public boolean isSameAsPrimaryType()
  {
    return getCode().equalsIgnoreCase( SAME_AS_PRIMARY_CODE );
  }

  /**
   * Checks to see if this pickList is of type ALL_ACTIVE_PAX_CODE.
   * 
   * @return boolean
   */
  public boolean isAllActivePaxType()
  {
    return getCode().equalsIgnoreCase( ALL_ACTIVE_PAX_CODE );
  }
}

/*
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/domain/enums/PartnerAudienceType.java,v $
 * (c) 2005 BI, Inc.  All rights reserved.
 */

package com.biperf.core.domain.enums;

import java.util.List;

/**
 * PartnerAudienceType.
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
 * <td>reddy</td>
 * <td>Feb 21, 2008</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 */
public class PartnerAudienceType extends PickListItem
{

  /**
   * Asset name used in Content Manager
   */
  private static final String PICKLIST_ASSET = "picklist.partneraudiencetype";

  public static final String ALL_ACTIVE_PAX_CODE = "allactivepaxaudience";
  public static final String SPECIFY_AUDIENCE_CODE = "specifyaudience";
  public static final String ENTIRE_PARENT_AUDIENCE_CODE = "entireparentaudience";
  public static final String SPECIFIC_PARENT_AUDIENCES_CODE = "specificparentaudiences";
  public static final String NODE_BASED_PARTNER_AUDIENCE_CODE = "nodebasedpartners";
  public static final String USER_CHAR = "userCharacteristics";
  public static final String SELF_ENROLL_ONLY = "selfenrollonly"; // GQ: This means no primary
                                                                  // audience. Uses secondary
                                                                  // audience: specific audience
                                                                  // type, audience name is the
                                                                  // program code.

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected PartnerAudienceType()
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
    return getPickListFactory().getPickList( PartnerAudienceType.class );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return PickListItem
   */
  public static PartnerAudienceType lookup( String code )
  {
    return (PartnerAudienceType)getPickListFactory().getPickListItem( PartnerAudienceType.class, code );
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
   * Checks to see if this pickList is of type ALL_ACTIVE_PAX_CODE.
   * 
   * @return boolean
   */
  public boolean isAllActivePaxType()
  {
    return getCode().equalsIgnoreCase( ALL_ACTIVE_PAX_CODE );
  }

  /**
   * Returns true if this object represents the "specify audiences" primary audience type; returns
   * false otherwise.
   * 
   * @return true if this object represents the "specify audiences" primary audience type; false
   *         otherwise.
   */
  public boolean isSpecifyAudienceType()
  {
    return getCode().equalsIgnoreCase( SPECIFY_AUDIENCE_CODE );
  }

  /**
   * Returns true if this object represents the "specific parent audiences" primary audience type;
   * returns false otherwise.
   * 
   * @return true if this object represents the "specific parent audiences" primary audience type;
   *         false otherwise.
   */
  public boolean isSpecificParentAudiencesType()
  {
    return getCode().equalsIgnoreCase( SPECIFIC_PARENT_AUDIENCES_CODE );
  }

  /**
   * Returns true if this object represents the "all pax self enroll" primary audience type;
   * returns false otherwise.
   * 
   * @return true if this object represents the "all pax self enroll" primary audience type;
   *         false otherwise.
   */
  public boolean isAllPaxSelfEnrollOnly()
  {
    return getCode().equalsIgnoreCase( SELF_ENROLL_ONLY );
  }

  public boolean isPreSelectedPartnerAudienceType()
  {
    return getCode().equalsIgnoreCase( NODE_BASED_PARTNER_AUDIENCE_CODE );
  }

  public boolean isUserCharacteristics()
  {
    return getCode().equalsIgnoreCase( USER_CHAR );
  }
}

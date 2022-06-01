/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/domain/enums/ClaimParticipantRoleType.java,v $
 */

package com.biperf.core.domain.enums;

import java.util.List;

/**
 * ClaimParticipantRoleType - Picklist wrapper to manage the ClaimParticipantRoleType from CM.
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
 * <td>crosenquest</td>
 * <td>Jun 28, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ClaimParticipantRoleType extends PickListItem
{

  /**
   * Asset name used in Content Manager
   */
  private static final String PICKLIST_ASSET = "picklist.claimparticipanttype";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected ClaimParticipantRoleType()
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
    return getPickListFactory().getPickList( ClaimParticipantRoleType.class );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return PickListItem
   */
  public static ClaimParticipantRoleType lookup( String code )
  {
    return (ClaimParticipantRoleType)getPickListFactory().getPickListItem( ClaimParticipantRoleType.class, code );
  }

  /**
   * Returns null if the defaultItem is not defined - or invalid
   * 
   * @return default PickListItem
   */
  // public static ClaimParticipantRoleType getDefaultItem()
  // {
  // return (ClaimParticipantRoleType)getPickListFactory().getDefaultPickListItem(
  // ClaimParticipantRoleType.class );
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

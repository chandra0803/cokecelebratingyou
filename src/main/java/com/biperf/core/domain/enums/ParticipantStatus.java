/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/domain/enums/ParticipantStatus.java,v $
 */

package com.biperf.core.domain.enums;

import java.util.List;

/**
 * The ParticipantStatus is a concrete instance of a PickListItem which wraps a type save enum
 * object of a PickList from content manager.
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
 * <td>rosenquest</td>
 * <td>Apr 29, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ParticipantStatus extends PickListItem
{
  public static final String ACTIVE = "active";
  public static final String INACTIVE = "inactive";

  /**
   * Asset name used in Content Manager
   */
  public static final String PICKLIST_ASSET = "picklist.participantstatus";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected ParticipantStatus()
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
    return getPickListFactory().getPickList( ParticipantStatus.class );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return PickListItem
   */
  public static ParticipantStatus lookup( String code )
  {
    return (ParticipantStatus)getPickListFactory().getPickListItem( ParticipantStatus.class, code );
  }

  /**
   * Returns null if the defaultItem is not defined - or invalid
   * 
   * @return default PickListItem
   */
  public static ParticipantStatus getDefaultItem()
  {
    return (ParticipantStatus)getPickListFactory().getDefaultPickListItem( ParticipantStatus.class );
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

/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/domain/enums/ParticipantSuspensionStatus.java,v $
 */

package com.biperf.core.domain.enums;

import java.util.List;

/**
 * The ParticipantSuspensionStatus is a concrete instance of a PickListItem which wraps a type save
 * enum object of a PickList from content manager.
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
public class ParticipantSuspensionStatus extends PickListItem
{

  /**
   * Asset name used in Content Manager
   */
  private static final String PICKLIST_ASSET = "picklist.participantsuspensionstatus";

  public static final String NONE = "none";
  public static final String SUSPEND_ALL = "susall";
  public static final String SUSPEND_DEPOSITS = "susdep";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected ParticipantSuspensionStatus()
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
    return getPickListFactory().getPickList( ParticipantSuspensionStatus.class );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return PickListItem
   */
  public static ParticipantSuspensionStatus lookup( String code )
  {
    return (ParticipantSuspensionStatus)getPickListFactory().getPickListItem( ParticipantSuspensionStatus.class, code );
  }

  /**
   * Returns null if the defaultItem is not defined - or invalid
   * 
   * @return default PickListItem
   */
  public static ParticipantSuspensionStatus getDefaultItem()
  {
    return (ParticipantSuspensionStatus)getPickListFactory().getDefaultPickListItem( ParticipantSuspensionStatus.class );
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

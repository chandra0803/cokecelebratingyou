/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/domain/enums/ParticipantPreferenceCommunicationsType.java,v $
 */

package com.biperf.core.domain.enums;

import java.util.List;

/**
 * ParticipantCommunicationPreferenceType.
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
 * <td>Sep 22, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ParticipantPreferenceCommunicationsType extends PickListItem
{

  /**
   * Asset name used in Content Manager
   */
  private static final String PICKLIST_ASSET = "picklist.participant.preference.communications";

  public static final String TEXT_MESSAGES = "textmessages";
  public static final String E_STATEMENTS = "estatements";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected ParticipantPreferenceCommunicationsType()
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
    return getPickListFactory().getPickList( ParticipantPreferenceCommunicationsType.class, new PickListItemSortOrderComparator() );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return PickListItem
   */
  public static ParticipantPreferenceCommunicationsType lookup( String code )
  {
    return (ParticipantPreferenceCommunicationsType)getPickListFactory().getPickListItem( ParticipantPreferenceCommunicationsType.class, code );
  }

  /**
   * Returns null if the defaultItem is not defined - or invalid
   * 
   * @return default PickListItem
   */
  public static ContactMethod getDefaultItem()
  {
    return (ContactMethod)getPickListFactory().getDefaultPickListItem( ContactMethod.class );
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

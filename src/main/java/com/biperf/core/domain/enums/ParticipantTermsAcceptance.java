/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.domain.enums;

import java.util.List;

/**
 * The ParticipantTermsAcceptance is a concrete instance of a PickListItem which wraps a type save
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
 * <td>cheng</td>
 * <td>Mar 6, 2006</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 */
public class ParticipantTermsAcceptance extends PickListItem
{
  public static final String ACCEPTED = "accepted";
  public static final String NOTACCEPTED = "notaccepted";
  public static final String DECLINED = "declined";

  /**
   * Asset name used in Content Manager
   */
  public static final String PICKLIST_ASSET = "picklist.participanttermsacceptance";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected ParticipantTermsAcceptance()
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
    return getPickListFactory().getPickList( ParticipantTermsAcceptance.class );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return PickListItem
   */
  public static ParticipantTermsAcceptance lookup( String code )
  {
    return (ParticipantTermsAcceptance)getPickListFactory().getPickListItem( ParticipantTermsAcceptance.class, code );
  }

  /**
   * Returns null if the defaultItem is not defined - or invalid
   * 
   * @return default PickListItem
   */
  public static ParticipantTermsAcceptance getDefaultItem()
  {
    return (ParticipantTermsAcceptance)getPickListFactory().getDefaultPickListItem( ParticipantTermsAcceptance.class );
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

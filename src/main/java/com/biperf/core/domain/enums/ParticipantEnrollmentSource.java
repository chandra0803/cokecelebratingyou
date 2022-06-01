/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/domain/enums/ParticipantEnrollmentSource.java,v $
 */

package com.biperf.core.domain.enums;

import java.util.List;

/**
 */
/**
 * The ParticipantEnrollmentSource is a concrete instance of a PickListItem which wraps a type save
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
public class ParticipantEnrollmentSource extends PickListItem
{
  public static final String WEB = "web";
  public static final String BATCH = "batch";
  public static final String SELF_ENROLL = "self";
  public static final String ROSTER = "roster";

  /**
   * Asset name used in Content Manager
   */
  private static final String PICKLIST_ASSET = "picklist.participantenrollmentsource";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected ParticipantEnrollmentSource()
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
    return getPickListFactory().getPickList( ParticipantEnrollmentSource.class );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return PickListItem
   */
  public static ParticipantEnrollmentSource lookup( String code )
  {
    return (ParticipantEnrollmentSource)getPickListFactory().getPickListItem( ParticipantEnrollmentSource.class, code );
  }

  /**
   * Returns null if the defaultItem is not defined - or invalid
   * 
   * @return default PickListItem
   */
  public static ParticipantEnrollmentSource getDefaultItem()
  {
    return (ParticipantEnrollmentSource)getPickListFactory().getDefaultPickListItem( ParticipantEnrollmentSource.class );
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

  public boolean isSelfEnroll()
  {
    return SELF_ENROLL.equalsIgnoreCase( getCode() );
  }

  public boolean isWeb()
  {
    return WEB.equalsIgnoreCase( getCode() );
  }

  public boolean isBatch()
  {
    return BATCH.equalsIgnoreCase( getCode() );
  }

}

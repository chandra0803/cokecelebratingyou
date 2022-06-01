
package com.biperf.core.domain.enums;

import java.util.List;

/**
 * GiverReceiverType.
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
 * <td>robinsra</td>
 * <td>Nov 18, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class SubmittedReceivedType extends PickListItem
{

  /**
   * Asset name used in Content Manager
   */
  public static final String PICKLIST_ASSET = "picklist.submitted.received.type";

  public static final String SUBMITTED = "submitted";
  public static final String RECEIVED = "received";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected SubmittedReceivedType()
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
    return getPickListFactory().getPickList( SubmittedReceivedType.class );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return PickListItem
   */
  public static SubmittedReceivedType lookup( String code )
  {
    return (SubmittedReceivedType)getPickListFactory().getPickListItem( SubmittedReceivedType.class, code );
  }

  /**
   * Returns null if the defaultItem is not defined - or invalid
   * 
   * @return default PickListItem
   */
  // public static GiverReceiverType getDefaultItem()
  // {
  // return (GiverReceiverType)getPickListFactory().getDefaultPickListItem( GiverReceiverType.class
  // );
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

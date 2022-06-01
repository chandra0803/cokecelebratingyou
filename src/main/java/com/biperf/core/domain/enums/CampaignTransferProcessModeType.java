
package com.biperf.core.domain.enums;

import java.util.List;

/**
 * The CampaignTransferProcessModeType 
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
 * <td>July 23, 2013</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class CampaignTransferProcessModeType extends PickListItem
{

  /**
   * Asset name used in Content Manager
   */
  private static final String PICKLIST_ASSET = "picklist.campaign.transfer.process.mode";

  public static final String ALL = "all";
  public static final String ONLY_ACCT_TRANSFER = "onlyaccttransfer";
  public static final String ONLY_ACCT_BAL_TRANSFER = "onlyacctbaltransfer";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected CampaignTransferProcessModeType()
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
    return getPickListFactory().getPickList( CampaignTransferProcessModeType.class );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return PickListItem
   */
  public static CampaignTransferProcessModeType lookup( String code )
  {
    return (CampaignTransferProcessModeType)getPickListFactory().getPickListItem( CampaignTransferProcessModeType.class, code );
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

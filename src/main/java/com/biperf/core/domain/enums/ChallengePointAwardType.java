/**
 * 
 */

package com.biperf.core.domain.enums;

import java.util.List;

/**
 * ChallengePointAwardType.
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
 * <td>sedey</td>
 * <td>May 29, 2007</td>8
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ChallengePointAwardType extends PickListItem
{

  /**
   * Asset name used in Content Manager
   */
  public static final String PICKLIST_ASSET = "picklist.challenge.point.award.types";

  public static final String POINTS = "points";
  public static final String MERCHTRAVEL = "merchTra";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected ChallengePointAwardType()
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
    return getPickListFactory().getPickList( ChallengePointAwardType.class );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return PickListItem
   */
  public static ChallengePointAwardType lookup( String code )
  {
    return (ChallengePointAwardType)getPickListFactory().getPickListItem( ChallengePointAwardType.class, code );
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

  public boolean isPoints()
  {
    return this.getCode().equals( POINTS );
  }

  public boolean isMerchTravel()
  {
    return this.getCode().equals( MERCHTRAVEL );
  }

}


package com.biperf.core.domain.enums;

import java.util.List;

/**
 * ScoreBy.
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
public class ScoreBy extends PickListItem
{

  /**
   * Asset name used in Content Manager
   */
  public static final String PICKLIST_ASSET = "picklist.score.type";

  public static final String GIVER = "giver";
  public static final String APPROVER = "approver";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected ScoreBy()
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
    return getPickListFactory().getPickList( ScoreBy.class );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return PickListItem
   */
  public static ScoreBy lookup( String code )
  {
    return (ScoreBy)getPickListFactory().getPickListItem( ScoreBy.class, code );
  }

  /**
   * Returns null if the defaultItem is not defined - or invalid
   * 
   * @return default PickListItem
   */
  // public static ScoreBy getDefaultItem()
  // {
  // return (ScoreBy)getPickListFactory().getDefaultPickListItem( ScoreBy.class
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

  /**
   * Returns true if this object represents score by giver; returns
   * false otherwise.
   * 
   * @return true if this object represents score by giver; returns false
   *         otherwise.
   */
  public boolean isScoreByGiver()
  {
    return getCode().equalsIgnoreCase( GIVER );
  }

}

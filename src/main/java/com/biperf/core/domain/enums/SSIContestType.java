/**
 * 
 */

package com.biperf.core.domain.enums;

import java.util.List;

/**
 * PromotionType.
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
 * <td>sondgero</td>
 * <td>Jun 24, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class SSIContestType extends ModuleAwarePickListItem
{
  /**
   * Asset name used in Content Manager
   */
  private static final String PICKLIST_ASSET = "picklist.ssi.contesttype";

  public static final String AWARD_THEM_NOW = "1";
  public static final String DO_THIS_GET_THAT = "2";
  public static final String OBJECTIVES = "4";
  public static final String STACK_RANK = "8";
  public static final String STEP_IT_UP = "16";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected SSIContestType()
  {
    super();
  }

  /**
   * Get the pick list from content manager.
   * 
   * @return List of PromotionType
   */
  public static List getList()
  {
    return getPickListFactory().getPickList( SSIContestType.class );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return PromotionType
   */
  public static SSIContestType lookup( String code )
  {
    return (SSIContestType)getPickListFactory().getPickListItem( SSIContestType.class, code );
  }

  /**
   * Returns null if the defaultItem is not defined - or invalid
   * 
   * @return default PromotionType
   */
  public static SSIContestType getDefaultItem()
  {
    return (SSIContestType)getPickListFactory().getDefaultPickListItem( SSIContestType.class );
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

  public boolean isAwardThemNow()
  {
    return AWARD_THEM_NOW.equals( getCode() );
  }

  public boolean isDoThisGetThat()
  {
    return DO_THIS_GET_THAT.equals( getCode() );
  }

  public boolean isObjectives()
  {
    return OBJECTIVES.equals( getCode() );
  }

  public boolean isStackRank()
  {
    return STACK_RANK.equals( getCode() );
  }

  public boolean isStepItUp()
  {
    return STEP_IT_UP.equals( getCode() );
  }

}

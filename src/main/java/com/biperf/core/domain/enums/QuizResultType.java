
package com.biperf.core.domain.enums;

import java.util.List;

/**
 * QuizResultType.
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
 * <td>Nov 30, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class QuizResultType extends PickListItem
{

  /**
   * Asset name used in Content Manager
   */
  public static final String PICKLIST_ASSET = "picklist.quiz.result.type";

  public static final String PASS = "passed";
  public static final String FAIL = "failed";
  public static final String PROGRESS = "progress";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected QuizResultType()
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
    return getPickListFactory().getPickList( QuizResultType.class );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return PickListItem
   */
  public static QuizResultType lookup( String code )
  {
    return (QuizResultType)getPickListFactory().getPickListItem( QuizResultType.class, code );
  }

  /**
   * Returns null if the defaultItem is not defined - or invalid
   * 
   * @return default PickListItem
   */
  // public static QuizResultType getDefaultItem()
  // {
  // return (QuizResultType)getPickListFactory().getDefaultPickListItem( QuizResultType.class
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

/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/domain/enums/QuizQuestionStatusType.java,v $
 */

package com.biperf.core.domain.enums;

import java.util.List;

/**
 * QuizQuestionStatusType type picklist.
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
 * <td>Oct 31, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class QuizQuestionStatusType extends PickListItem
{
  /**
   * Asset name used in Content Manager
   */
  private static final String PICKLIST_ASSET = "picklist.quiz.question.status.type";
  public static final String ACTIVE = "active";
  public static final String INACTIVE = "inactive";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected QuizQuestionStatusType()
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
    return getPickListFactory().getPickList( QuizQuestionStatusType.class );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return PickListItem
   */
  public static QuizQuestionStatusType lookup( String code )
  {
    return (QuizQuestionStatusType)getPickListFactory().getPickListItem( QuizQuestionStatusType.class, code );
  }

  /**
   * Returns null if the defaultItem is not defined - or invalid
   * 
   * @return default PickListItem
   */
  // public static QuizQuestionStatusType getDefaultItem()
  // {
  // return (QuizQuestionStatusType)getPickListFactory().getDefaultPickListItem(
  // QuizQuestionStatusType.class );
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

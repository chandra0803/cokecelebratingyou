/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/domain/enums/QuizType.java,v $
 */

package com.biperf.core.domain.enums;

import java.util.List;

/**
 * QuizType.
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
 * <td>Oct 25, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class QuizType extends PickListItem
{

  private static final String PICKLIST_ASSET = "picklist.quiz.type";

  public static final String FIXED = "fixed";
  public static final String RANDOM = "random";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected QuizType()
  {
    super();
  }

  /**
   * Get the pick list from content manager.
   * 
   * @return List of QuizType
   */
  public static List getList()
  {
    return getPickListFactory().getPickList( QuizType.class );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return QuizType
   */
  public static QuizType lookup( String code )
  {
    return (QuizType)getPickListFactory().getPickListItem( QuizType.class, code );
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

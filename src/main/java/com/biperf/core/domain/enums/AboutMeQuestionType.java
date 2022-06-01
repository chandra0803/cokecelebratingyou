/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/domain/enums/SecretQuestionType.java,v $
 */

package com.biperf.core.domain.enums;

import java.util.List;

/**
 * The AboutMeQuestionType is a concrete instance of a PickListItem which wraps a type save enum
 * object of a PickList from content manager.
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
 * <td>Apr 7, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class AboutMeQuestionType extends PickListItem
{
  /**
   * Asset name used in Content Manager
   */
  private static final String PICKLIST_ASSET = "picklist.aboutmequestion";
  public static final String DEFAULT_ITEM_CODE = "game";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected AboutMeQuestionType()
  {
    super();
  }

  /**
   * Get the pick list from content manager.
   * 
   * @return List of AboutMeQuestionType
   */
  public static List getList()
  {
    return getPickListFactory().getPickList( AboutMeQuestionType.class );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return AboutMeQuestionType
   */
  public static AboutMeQuestionType lookup( String code )
  {
    return (AboutMeQuestionType)getPickListFactory().getPickListItem( AboutMeQuestionType.class, code );
  }

  /**
   * Returns null if the defaultItem is not defined - or invalid
   * 
   * @return AboutMeQuestionType
   */
  public static AboutMeQuestionType getDefaultItem()
  {
    return (AboutMeQuestionType)getPickListFactory().getDefaultPickListItem( AboutMeQuestionType.class );
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

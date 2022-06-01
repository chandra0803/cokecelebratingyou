/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/domain/enums/SecretQuestionType.java,v $
 */

package com.biperf.core.domain.enums;

import java.util.List;

/**
 * The SecretQuestionType is a concrete instance of a PickListItem which wraps a type save enum
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
public class SecretQuestionType extends PickListItem
{

  /**
   * Asset name used in Content Manager
   */
  private static final String PICKLIST_ASSET = "picklist.secretquestion";
  public static final String DEFAULT_ITEM_CODE = "city";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected SecretQuestionType()
  {
    super();
  }

  /**
   * Get the pick list from content manager.
   * 
   * @return List of SecretQuestionType
   */
  public static List getList()
  {
    return getPickListFactory().getPickList( SecretQuestionType.class );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return SecretQuestionType
   */
  public static SecretQuestionType lookup( String code )
  {
    return (SecretQuestionType)getPickListFactory().getPickListItem( SecretQuestionType.class, code );
  }

  /**
   * Returns null if the defaultItem is not defined - or invalid
   * 
   * @return SecretQuestionType
   */
  public static SecretQuestionType getDefaultItem()
  {
    return (SecretQuestionType)getPickListFactory().getDefaultPickListItem( SecretQuestionType.class );
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

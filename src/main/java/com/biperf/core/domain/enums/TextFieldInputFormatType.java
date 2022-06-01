
package com.biperf.core.domain.enums;/*
                                     * (c) 2005 BI, Inc.  All rights reserved.
                                     * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/domain/enums/TextFieldInputFormatType.java,v $
                                     */

import java.util.List;

/**
 * The com.biperf.core.domain.enums.TextFieldInputFormatType is a concrete instance of a
 * PickListItem which wrappes a type save enum object of a PickList from content manager.
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
 * <td>dunne</td>
 * <td>Apr 20, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class TextFieldInputFormatType extends PickListItem
{

  public static final String NORMAL_TEXT = "text";
  public static final String ALPHA_NUMERIC = "alpha_num";
  public static final String NUMERIC_ONLY = "numeric";
  public static final String PHONE_NUMBER = "phone";
  public static final String EMAIL_ADDRESS = "email";

  /**
   * Asset name used in Content Manager
   */
  private static final String PICKLIST_ASSET = "picklist.textfieldinputformattype";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected TextFieldInputFormatType()
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
    return getPickListFactory().getPickList( TextFieldInputFormatType.class );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return PickListItem
   */
  public static TextFieldInputFormatType lookup( String code )
  {
    return (TextFieldInputFormatType)getPickListFactory().getPickListItem( TextFieldInputFormatType.class, code );
  }

  /**
   * Returns null if the defaultItem is not defined - or invalid
   * 
   * @return default PickListItem
   */
  // public static TextFieldInputFormatType getDefaultItem()
  // {
  // return (TextFieldInputFormatType)getPickListFactory().getDefaultPickListItem(
  // TextFieldInputFormatType.class );
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

  public boolean isAlphaNumeric()
  {
    return ALPHA_NUMERIC.equals( getCode() );
  }

  public boolean isNormalText()
  {
    return NORMAL_TEXT.equals( getCode() );
  }

  public boolean isNumericOnly()
  {
    return NUMERIC_ONLY.equals( getCode() );
  }

  public boolean isPhoneNumber()
  {
    return PHONE_NUMBER.equals( getCode() );
  }

  public boolean isEmailAddress()
  {
    return EMAIL_ADDRESS.equals( getCode() );
  }
}

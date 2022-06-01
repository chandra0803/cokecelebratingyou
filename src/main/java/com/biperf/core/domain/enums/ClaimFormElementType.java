
package com.biperf.core.domain.enums;/*
                                     * (c) 2005 BI, Inc.  All rights reserved.
                                     * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/domain/enums/ClaimFormElementType.java,v $
                                     */

import java.util.List;

/**
 * This is a concrete instance of a PickListItem which wrappes a type safe enum object of a PickList
 * from content manager.
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
 * <td>tennant</td>
 * <td>Jun 6, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ClaimFormElementType extends PickListItem
{
  private static final long serialVersionUID = 1L;
  // Types
  public static final String TEXT_FIELD = "text";
  public static final String TEXT_BOX_FIELD = "text_box";
  public static final String NUMBER_FIELD = "number";
  public static final String SECTION_HEADING = "sect_head";
  public static final String COPY_BLOCK = "copy";
  public static final String LINK = "link";
  public static final String DATE_FIELD = "date";
  public static final String BOOLEAN = "boolean";
  public static final String SELECTION = "selection";
  public static final String MULTI_SELECTION = "multi_selection";
  public static final String BUTTON = "button";
  public static final String ADDRESS_BOOK_SELECTION = "addr_book_sel";
  public static final String BOOLEAN_CHECKBOX = "bool_checkbox";
  public static final String ADDRESS_BLOCK = "address_block";
  public static final String FILE = "file";

  /**
   * Asset name used in Content Manager
   */
  private static final String PICKLIST_ASSET = "picklist.claimformelementtype";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected ClaimFormElementType()
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
    return getPickListFactory().getPickList( ClaimFormElementType.class );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return PickListItem
   */
  public static ClaimFormElementType lookup( String code )
  {
    return (ClaimFormElementType)getPickListFactory().getPickListItem( ClaimFormElementType.class, code );
  }

  /**
   * Returns null if the defaultItem is not defined - or invalid
   * 
   * @return default PickListItem
   */
  // public static ClaimFormElementType getDefaultItem()
  // {
  // return (ClaimFormElementType)getPickListFactory().getDefaultPickListItem(
  // ClaimFormElementType.class );
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

  public boolean isTextField()
  {
    return TEXT_FIELD.equals( getCode() );
  }

  public boolean isTextBoxField()
  {
    return TEXT_BOX_FIELD.equals( getCode() );
  }

  public boolean isNumberField()
  {
    return NUMBER_FIELD.equals( getCode() );
  }

  public boolean isSectionHeading()
  {
    return SECTION_HEADING.equals( getCode() );
  }

  public boolean isCopyBlock()
  {
    return COPY_BLOCK.equals( getCode() );
  }

  public boolean isLink()
  {
    return LINK.equals( getCode() );
  }

  public boolean isDateField()
  {
    return DATE_FIELD.equals( getCode() );
  }

  public boolean isBooleanField()
  {
    return BOOLEAN.equals( getCode() );
  }

  public boolean isSelectField()
  {
    return SELECTION.equals( getCode() );
  }

  public boolean isMultiSelectField()
  {
    return MULTI_SELECTION.equals( getCode() );
  }

  public boolean isButton()
  {
    return BUTTON.equals( getCode() );
  }

  public boolean isAddressBookSelect()
  {
    return ADDRESS_BOOK_SELECTION.equals( getCode() );
  }

  public boolean isBooleanCheckbox()
  {
    return BOOLEAN_CHECKBOX.equals( getCode() );
  }

  public boolean isAddressBlock()
  {
    return ADDRESS_BLOCK.equals( getCode() );
  }

  public boolean isFile()
  {
    return FILE.equals( getCode() );
  }

}

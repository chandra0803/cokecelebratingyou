/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/domain/enums/CharacteristicDataType.java,v $
 */

package com.biperf.core.domain.enums;

import java.util.List;

/**
 * The CharacteristicDataType is a concrete instance of a PickListItem which wrappes a type save
 * enum object of a PickList from content manager.
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
 * <td>Jason</td>
 * <td>Apr 21, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class CharacteristicDataType extends PickListItem
{

  /**
   * Asset name used in Content Manager
   */
  private static final String PICKLIST_ASSET = "picklist.characteristictype";

  public static final String INTEGER = "int";
  public static final String TEXT = "txt";
  public static final String DECIMAL = "decimal";
  public static final String BOOLEAN = "boolean";
  public static final String DATE = "date";
  public static final String SINGLE_SELECT = "single_select";
  public static final String MULTI_SELECT = "multi_select";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected CharacteristicDataType()
  {
    super();
  }

  /**
   * Get the pick list from content manager.
   * 
   * @return List of CharacteristicDataType
   */
  public static List getList()
  {
    return getPickListFactory().getPickList( CharacteristicDataType.class );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return CharacteristicDataType
   */
  public static CharacteristicDataType lookup( String code )
  {
    return (CharacteristicDataType)getPickListFactory().getPickListItem( CharacteristicDataType.class, code );
  }

  /**
   * Returns null if the defaultItem is not defined - or invalid
   * 
   * @return CharacteristicDataType
   */
  // public static CharacteristicDataType getDefaultItem()
  // {
  // return (CharacteristicDataType)getPickListFactory().getDefaultPickListItem(
  // CharacteristicDataType.class );
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

  public boolean isDateType()
  {
    return this.getCode().equals( DATE );
  }

  public boolean isSelectType()
  {
    return this.getCode().equals( SINGLE_SELECT ) || this.getCode().equals( MULTI_SELECT );
  }

  public boolean isNumberType()
  {
    return this.getCode().equals( DECIMAL ) || this.getCode().equals( INTEGER );
  }

  public boolean isBooleanType()
  {
    return this.getCode().equals( BOOLEAN );
  }

  public boolean isTextType()
  {
    return this.getCode().equals( TEXT );
  }

  public boolean isSingleSelect()
  {
    return this.getCode().equals( SINGLE_SELECT );
  }

  public boolean isMultiSelect()
  {
    return this.getCode().equals( MULTI_SELECT );
  }

  public boolean isDecimalType()
  {
    return this.getCode().equals( DECIMAL );
  }

  public boolean isIntegerType()
  {
    return this.getCode().equals( INTEGER );
  }
}

/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/domain/enums/InsertFieldType.java,v $
 */

package com.biperf.core.domain.enums;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * The InsertFieldType is a concrete instance of a PickListItem which wrappes a type save enum
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
 * <td>sedey</td>
 * <td>June 2, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class InsertFieldType extends PickListItem
{

  /**
   * Asset name used in Content Manager
   */
  private static final String PICKLIST_ASSET = "picklist.insert.field.type";

  public static final Pattern GENERAL_PATTERN = Pattern.compile( "\\$[a-zA-Z]+" );
  public static final String FIRST_NAME_CODE = "$firstName";
  public static final Pattern FIRST_NAME_PATTERN = Pattern.compile( "\\$firstName" );
  public static final String LAST_NAME_CODE = "$lastName";
  public static final Pattern LAST_NAME_PATTERN = Pattern.compile( "\\$lastName" );
  public static final String PROMOTION_NAME_CODE = "$promotionName";
  public static final Pattern PROMOTION_NAME_PATTERN = Pattern.compile( "\\$promotionName" );

  public static List getSpecificPatternList()
  {
    List patterns = new ArrayList();
    patterns.add( FIRST_NAME_PATTERN );
    patterns.add( LAST_NAME_PATTERN );
    patterns.add( PROMOTION_NAME_PATTERN );
    return patterns;
  }

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected InsertFieldType()
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
    return getPickListFactory().getPickList( InsertFieldType.class );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return PickListItem
   */
  public static InsertFieldType lookup( String code )
  {
    return (InsertFieldType)getPickListFactory().getPickListItem( InsertFieldType.class, code );
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

/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/domain/enums/AddressType.java,v $
 */

package com.biperf.core.domain.enums;

import java.util.List;

/**
 * The AddressType is a concrete instance of a PickListItem which wraps a type save enum object of a
 * PickList from content manager.
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
 * <td>veeramas</td>
 * <td>Oct 22, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class PublicRecognitionTabType extends PickListItem
{

  /**
   * Asset name used in Content Manager
   */
  private static final String PICKLIST_ASSET = "picklist.publicrecognition.tab";

  public static final String RECOMMENDED_TAB = "recommended";
  public static final String GLOBAL_TAB = "global";
  public static final String TEAM_TAB = "team";
  public static final String FOLLOWED_TAB = "followed";
  public static final String ME_TAB = "mine";
  public static final String COUNRTY_TAB = "country";
  public static final String DEPARTMENT_TAB = "department";
  public static final String DIVISION_TAB = "division";
  
  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected PublicRecognitionTabType()
  {
    super();
  }

  /**
   * Get the pick list from content manager.
   * 
   * @return List of AddressType
   */
  public static List getList()
  {
    return getPickListFactory().getPickList( PublicRecognitionTabType.class );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return AddressType
   */
  public static PublicRecognitionTabType lookup( String code )
  {
    return (PublicRecognitionTabType)getPickListFactory().getPickListItem( PublicRecognitionTabType.class, code );
  }

  /**
   * Returns null if the defaultItem is not defined - or invalid
   * 
   * @return AddressType
   */
  // public static AddressType getDefaultItem()
  // {
  // return (AddressType)getPickListFactory().getDefaultPickListItem( AddressType.class );
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

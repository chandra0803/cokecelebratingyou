/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/domain/enums/PhoneType.java,v $
 */

package com.biperf.core.domain.enums;

import java.util.ArrayList;
import java.util.List;

/**
 * The PhoneType is a concrete instance of a PickListItem which wrappes a type save enum object of a
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
 * <td>dunne</td>
 * <td>Apr 20, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class PhoneType extends PickListItem
{
  // types
  public static final String FAX = "fax";
  public static final String HOME = "hom";
  public static final String BUSINESS = "bus";
  public static final String MOBILE = "mob";
  public static final String OTHER = "oth";
  public static final String RECOVERY = "rec";

  /**
   * Asset name used in Content Manager
   */
  private static final String PICKLIST_ASSET = "picklist.phonetype";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected PhoneType()
  {
    super();
  }

  /**
   * Get the pick list from content manager.
   * 
   * @return List
   */
  public static List<PhoneType> getList()
  {
    return getPickListFactory().getPickList( PhoneType.class );
  }
  
  /** Returns only types that can be used as a primary contact. (i.e. not recovery) */
  public static List<PhoneType> getPrimaryList()
  {
    List<PhoneType> list = new ArrayList<>( getList() );
    list.remove( lookup( RECOVERY ) );
    return list;
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return PickListItem
   */
  public static PhoneType lookup( String code )
  {
    return (PhoneType)getPickListFactory().getPickListItem( PhoneType.class, code );
  }

  /**
   * Returns null if the defaultItem is not defined - or invalid
   * 
   * @return default PickListItem
   */
  // public static PhoneType getDefaultItem()
  // {
  // return (PhoneType)getPickListFactory().getDefaultPickListItem( PhoneType.class );
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

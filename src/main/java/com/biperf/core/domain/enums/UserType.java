/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/domain/enums/UserType.java,v $
 *
 */

package com.biperf.core.domain.enums;

import java.util.ArrayList;
import java.util.List;

/**
 * UserType picklist. <p/> <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>dunne</td>
 * <td>May 20, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 * @author dunne
 *
 */
public class UserType extends PickListItem
{
  /**
   * Static Codes
   */
  public static final String BI = "bi";
  private static final String CLIENT = "clt";
  public static final String PARTICIPANT = "pax";

  /**
   * Asset name used in Content Manager
   */
  private static final String PICKLIST_ASSET = "picklist.usertype";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected UserType()
  {
    super();
  }

  /**
   * Get the pick list from content manager.
   * 
   * @return List
   */
  public static List<UserType> getList()
  {
    // Client type has been removed. Playing it safe, in case of cmdiff / gold copy / translation quirks
    List<UserType> list = new ArrayList<>( getPickListFactory().getPickList( UserType.class ) );
    list.removeIf( userType -> CLIENT.equals( userType.getCode() ) );
    return list;
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return PickListItem
   */
  public static UserType lookup( String code )
  {
    return (UserType)getPickListFactory().getPickListItem( UserType.class, code );
  }

  /**
   * Returns null if the defaultItem is not defined - or invalid
   * 
   * @return default PickListItem
   */
  // public static UserType getDefaultItem()
  // {
  // return (UserType)getPickListFactory().getDefaultPickListItem( UserType.class );
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

  /**
   * self explanatory
   * 
   * @return boolean
   */
  public boolean isParticipant()
  {
    return PARTICIPANT.equals( getCode() );
  }

  public boolean isAdmin()
  {
    return BI.equals( getCode() );
  }
}

/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/domain/enums/StateType.java,v $
 */

package com.biperf.core.domain.enums;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * The StateType is a concrete instance of a PickListItem which wrappes a type save enum object of a
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
public class StateType extends PickListItem implements Comparable
{

  /**
   * Asset name used in Content Manager
   */
  private static final String PICKLIST_ASSET = "picklist.statetype";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected StateType()
  {
    super();
  }

  /**
   * Get the pick list from content manager.
   *
   * @return List
   */
  protected static List getList()
  {
    return getPickListFactory().getPickList( StateType.class );
  }

  /**
   * @param countryCode
   * @return List of States filtered by country
   */
  public static List getList( String countryCode )
  {
    if ( countryCode == null )
    {
      return getList();
    }

    List stateList = getPickListFactory().getPickList( StateType.class );

    List stateListByCountry = new ArrayList();
    Iterator iter = stateList.iterator();
    while ( iter.hasNext() )
    {
      StateType stateType = (StateType)iter.next();
      if ( stateType.getCode().startsWith( countryCode ) )
      {
        stateListByCountry.add( stateType );
      }
    }

    return stateListByCountry;
  }

  /**
   * Returns null if the code is null or invalid for this list.
   *
   * @param code
   * @return PickListItem
   */
  public static StateType lookup( String code )
  {
    return (StateType)getPickListFactory().getPickListItem( StateType.class, code );
  }

  /**
   * Actual stateCode is stored in abbreviation field, lookup by that.
   *
   * @param countryCode
   * @param stateCode
   * @return StateType
   */
  public static StateType lookup( String countryCode, String stateCode )
  {
    String code = countryCode + "_" + stateCode;

    return (StateType)getPickListFactory().getPickListItem( StateType.class, code );
  }

  /**
   * Returns null if the defaultItem is not defined - or invalid
   *
   * @return default PickListItem
   */
  // public static StateType getDefaultItem()
  // {
  // return (StateType)getPickListFactory().getDefaultPickListItem( StateType.class );
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

  public int compareTo( Object obj )
  {
    StateType st = (StateType)obj;
    return this.getName().compareTo( st.getName() );
  }

}

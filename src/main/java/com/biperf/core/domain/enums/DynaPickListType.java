/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/domain/enums/DynaPickListType.java,v $
 */

package com.biperf.core.domain.enums;

import java.util.List;
import java.util.Map;

import com.objectpartners.cms.util.ContentReaderManager;

/**
 * The DynaPickListType is a concrete instance of a PickListItem object of a PickList from content
 * manager.
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
 * <td>Apr 29, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class DynaPickListType extends PickListItem
{

  /**
   * Asset name used in Content Manager
   */
  private String asset = null;
  private Map contentMap;

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected DynaPickListType()
  {
    super();
  }

  /**
   * Get the pick list from content manager.
   * 
   * @param assetCode
   * @return List of PickListType
   */
  public static List getList( String assetCode )
  {
    return DynaPickListFactory.getPickList( assetCode );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param assetCode
   * @param itemCode
   * @return PickListType
   */
  public static DynaPickListType lookup( String assetCode, String itemCode )
  {
    return (DynaPickListType)DynaPickListFactory.getPickListItem( assetCode, itemCode );
  }

  /**
   * Returns null if the defaultItem is not defined - or invalid
   * 
   * @param assetKey
   * @return PickListType
   */
  // public static DynaPickListType getDefaultItem( String assetKey )
  // {
  // return (DynaPickListType)DynaPickListFactory.getDefaultPickListItem( assetKey );
  // }
  /**
   * Overridden from
   * 
   * @see com.biperf.core.domain.enums.PickListItem#getPickListAssetCode()
   * @return PICKLIST_ASSET
   */
  public String getPickListAssetCode()
  {
    return asset;
  }

  /**
   * Set the pickListAssetname property
   * 
   * @param pickListAssetCode
   */
  public void setPickListAssetCode( String pickListAssetCode )
  {
    asset = pickListAssetCode;
  }

  /**
   * Overridden from
   * 
   * @see java.lang.Object#toString()
   * @return <code>java.lang.String</code>
   */
  public String toString()
  {
    final StringBuffer buf = new StringBuffer();
    buf.append( this.getClass().getName() );
    buf.append( "{asset=" ).append( asset );
    buf.append( ",code=" ).append( getCode() );
    buf.append( ",active=" ).append( isActive() );
    buf.append( '}' );
    return buf.toString();
  }

  /**
   * @return value of listName property
   */
  public String getListName()
  {
    String parentCode = ContentReaderManager.getContentReader().getAsset( asset ).getParentAsset().getCode();
    return ContentReaderManager.getText( parentCode, "NAME" );
  }

  public Map getContentMap()
  {
    return contentMap;
  }

  public void setContentMap( Map contentMap )
  {
    this.contentMap = contentMap;
  }

  public String getModuleInstalled()
  {
    if ( !contentMap.containsKey( "MODULE_INSTALLED" ) )
    {
      return null;
    }
    return (String)contentMap.get( "MODULE_INSTALLED" );
  }
}

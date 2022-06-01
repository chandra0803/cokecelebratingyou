/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/domain/enums/DynaPickListFactory.java,v $
 */

package com.biperf.core.domain.enums;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringEscapeUtils;

import com.biperf.core.domain.system.PropertySetItem;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.utils.BeanLocator;
import com.objectpartners.cms.domain.Asset;
import com.objectpartners.cms.domain.Content;
import com.objectpartners.cms.util.ContentReaderManager;

/**
 * The PickListFactory class builds a list of PickLists from the Content Manager.
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
 * <td>Apr 29, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public abstract class DynaPickListFactory
{
  // TODO use or remove the log
  // private static final Log LOG = LogFactory.getLog( DynaPickListFactory.class );

  /**
   * Get the pick list for the specified class from Content Manger. The returned List will be
   * instances of the sDynaPickListType class which is a concrete subclass of PickListItem. This
   * method will order the List using the PickListItemNameComparator class.
   * 
   * @see com.biperf.core.domain.enums.PickListItemNameComparator
   * @param assetCode
   * @return A list of PickListItems
   */
  public static List getPickList( String assetCode )
  {
    return getPickList( assetCode, new PickListItemNameComparator() );
  }

  /**
   * Get the pick list for the specified class from Content Manger. The returned List will be
   * instances of the sDynaPickListType class which is a concrete subclass of PickListItem. The List
   * will be sorted using the supplied Comparator.
   * 
   * @param assetCode
   * @param pickListComparator
   * @return A list of PickListItems
   */
  public static List getPickList( String assetCode, Comparator pickListComparator )
  {
    // the build in cm locale pickList items asset code does not end with .items - we cannot append
    // to it.
    if ( !assetCode.endsWith( ".items" ) && !assetCode.equals( "default.locale.items" ) )
    {
      assetCode = assetCode + ".items";
    }
    List items = new ArrayList();
    Object contentObj = ContentReaderManager.getContentReader().getContent( assetCode );

    List contentList = null;
    if ( contentObj instanceof Content )
    {
      contentList = new ArrayList();
      contentList.add( contentObj );
    }
    else if ( contentObj instanceof List )
    {
      contentList = (List)contentObj;
    }
    else
    {
      contentList = new ArrayList();
    }

    // process the list of ContentKeys on the Asset and return them as a List of objects of type
    // clazz
    for ( Iterator iter = contentList.iterator(); iter.hasNext(); )
    {
      Content content = (Content)iter.next();
      DynaPickListType item = new DynaPickListType();
      item.setCode( (String)content.getContentDataMap().get( PickListItem.ITEM_CODE_KEY ) );
      item.setDesc( StringEscapeUtils.unescapeHtml4( (String)content.getContentDataMap().get( PickListItem.ITEM_DESC_KEY ) ) );
      item.setName( StringEscapeUtils.unescapeHtml4( (String)content.getContentDataMap().get( PickListItem.ITEM_NAME_KEY ) ) );
      item.setAbbr( StringEscapeUtils.unescapeHtml4( (String)content.getContentDataMap().get( PickListItem.ITEM_ABBR_KEY ) ) );
      item.setActive( "true".equalsIgnoreCase( (String)content.getContentDataMap().get( PickListItem.ITEM_STATUS_KEY ) ) );
      item.setPickListAssetCode( assetCode );
      item.setSortOrder( content.getContentKey().getSortOrder() );
      item.setContentMap( content.getContentDataMap() );

      if ( item.getModuleInstalled() != null )
      {

        SystemVariableService systemVariableService = (SystemVariableService)BeanLocator.getBean( SystemVariableService.BEAN_NAME );

        if ( item.isActive() )
        {
          String moduleInstalled = (String)content.getContentDataMap().get( ModuleAwarePickListItem.MODULE_INSTALLED );

          if ( moduleInstalled.equals( ModuleAwarePickListItem.CORE ) )
          {

            items.add( item );

          }
          else if ( systemVariableService.getPropertyByName( moduleInstalled ) != null && systemVariableService.getPropertyByName( moduleInstalled ) != null )
          {

            PropertySetItem property = systemVariableService.getPropertyByName( moduleInstalled );

            if ( property.getBooleanVal() )
            {
              items.add( item );
            }
          }
        }

      }
      else if ( item.isActive() )
      {

        items.add( item );

      }
    }

    Collections.sort( items, pickListComparator );

    return Collections.unmodifiableList( items );
  }

  /**
   * Get the pick list item by code from Content Manager. This item will be an instance of
   * DynaPickListType class which is a concrete subclass of PickListItem.
   * 
   * @param assetCode
   * @param itemCode
   * @return PickListItem with specified code
   */
  public static PickListItem getPickListItem( String assetCode, String itemCode )
  {
    if ( itemCode == null )
    {
      return null;
    }
    List items = getPickList( assetCode );
    if ( items != null )
    {
      for ( Iterator iter = items.iterator(); iter.hasNext(); )
      {
        PickListItem item = (PickListItem)iter.next();
        if ( itemCode.equals( item.getCode() ) )
        {
          return item;
        }
      }
    }
    return null;
  }

  /**
   * get the Asset based on the assetCode
   * 
   * @param assetCode
   * @return Asset
   */
  protected static Asset getAsset( String assetCode )
  {
    return ContentReaderManager.getContentReader().getAsset( assetCode );
  }
}

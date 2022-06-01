/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/domain/enums/PickListFactoryImpl.java,v $
 */

package com.biperf.core.domain.enums;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import com.biperf.core.domain.system.PropertySetItem;
import com.biperf.core.exception.BeaconRuntimeException;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.utils.BeanLocator;
import com.objectpartners.cms.domain.Content;
import com.objectpartners.cms.util.ContentReaderManager;

/**
 * The PickListFactory class builds PickLists from the Content Manager. This class is used by
 * PickListItem subclasses.
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
 * <td>Apr 22, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class PickListFactoryImpl implements PickListFactory
{

  // TODO use or remove the LOG
  // private static final Log LOG = LogFactory.getLog( PickListFactoryImpl.class );
  private SystemVariableService systemVariableService;

  /**
   * Get the pick list for the specified class from Content Manger. The returned List will be
   * instances of the specified class which is a concrete subclass of PickListItem. The List will be
   * sorted using the supplied Comparator.
   * 
   * @param clazz
   * @param pickListComparator
   * @return A list of PickListItems
   */
  public List getPickList( Class clazz, Comparator pickListComparator )
  {
    return getPickList( clazz, pickListComparator, false, null );
  }

  /**
   * Get the pick list for the specified class from Content Manger. The returned List will be
   * instances of the specified class which is a concrete subclass of PickListItem. The List will be
   * sorted using the supplied Comparator. <br/> ignoreModuleAwareCheck paramater added for two
   * reasons, 1) so existing persisted type that is from a module marked uninstalled can still be
   * loaded, and 2) Hibernate throws an Exception when making a service call inside
   * UserType#nullSafeGet()
   * 
   * @param clazz
   * @param pickListComparator
   * @param ignoreModuleAwareCheck
   * @return A list of PickListItems
   */
  private List getPickList( Class clazz, Comparator pickListComparator, boolean ignoreModuleAwareCheck, String requiredCode )
  {
    if ( !isSubClassOfPickListItem( clazz ) )
    {
      throw new BeaconRuntimeException( clazz.getName() + " is not a subclass of PickListItem." );
    }

    List items = new ArrayList();
    try
    {
      Object contentObj = ContentReaderManager.getContentReader().getContent( ( (PickListItem)clazz.newInstance() ).getPickListItemsAssetCode() );

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
        PickListItem item = (PickListItem)clazz.newInstance();
        item.setActive( "true".equalsIgnoreCase( (String)content.getContentDataMap().get( PickListItem.ITEM_STATUS_KEY ) ) );

        String code = (String)content.getContentDataMap().get( PickListItem.ITEM_CODE_KEY );
        // missing code means that code/status is not translatable - therefore were not copied.
        if ( item instanceof LanguageType && code == null )
        {
          code = getSystemVariableService().getDefaultLanguage().getStringVal();
          item.setActive( true );
        }
        else if ( code == null && requiredCode != null )
        {
          code = requiredCode;
          item.setActive( true );
        }
        item.setCode( code );
        item.setDesc( (String)content.getContentDataMap().get( PickListItem.ITEM_DESC_KEY ) );
        item.setName( (String)content.getContentDataMap().get( PickListItem.ITEM_NAME_KEY ) );
        item.setAbbr( (String)content.getContentDataMap().get( PickListItem.ITEM_ABBR_KEY ) );
        item.setSortOrder( content.getContentKey().getSortOrder() );
        item.setContentMap( content.getContentDataMap() );

        if ( item instanceof ModuleAwarePickListItem && !ignoreModuleAwareCheck )
        {

          if ( item.isActive() )
          {
            String moduleInstalled = (String)content.getContentDataMap().get( ModuleAwarePickListItem.MODULE_INSTALLED );

            PropertySetItem property = getSystemVariableService().getPropertyByName( moduleInstalled );

            if ( property == null )
            {
              // property not found. Consider these to be shown.
              items.add( item );
            }
            else if ( property.getBooleanVal() )
            {
              items.add( item );
            }
          }

        }
        else if ( item.isActive() )
        {

          items.add( item );

        }
      }
    }
    catch( InstantiationException e )
    {
      throw new BeaconRuntimeException( "Exception calling newInstance() on " + clazz.getName() + " .", e );
    }
    catch( IllegalAccessException e )
    {
      throw new BeaconRuntimeException( "Exception calling newInstance() on " + clazz.getName() + " .", e );
    }

    Collections.sort( items, pickListComparator );
    return Collections.unmodifiableList( items );
  }

  /**
   * Get the pick list for the specified class from Content Manger. The returned List will be
   * instances of the specified class which is a concrete subclass of PickListItem. This method will
   * order the List using the PickListItemNameComparator class.
   * 
   * @see com.biperf.core.domain.enums.PickListItemNameComparator
   * @param clazz
   * @return A list of PickListItems
   */
  public List getPickList( Class clazz )
  {
    return getPickList( clazz, new PickListItemNameComparator() );
  }

  /**
   * Get the pick list item by code for the specified class from Content Manager. This item will be
   * an instance of the specified class which is a concrete subclass of PickListItem.
   * 
   * @param clazz
   * @param code
   * @return PickListItem with specified code
   */
  public PickListItem getPickListItem( Class clazz, String code )
  {
    if ( code == null )
    {
      return null;
    }
    if ( !isSubClassOfPickListItem( clazz ) )
    {
      throw new BeaconRuntimeException( clazz.getName() + " is not a subclass of PickListItem." );
    }
    List items = getPickList( clazz, new PickListItemNameComparator(), true, code );
    if ( items != null )
    {
      for ( Iterator iter = items.iterator(); iter.hasNext(); )
      {
        PickListItem item = (PickListItem)iter.next();
        if ( code.equalsIgnoreCase( item.getCode() ) )
        {
          return item;
        }
      }
    }
    return null;
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.domain.enums.PickListFactory#getDefaultPickListItem(java.lang.Class)
   * @param clazz
   * @return default picklist item
   */
  public PickListItem getDefaultPickListItem( Class clazz )
  {
    if ( !isSubClassOfPickListItem( clazz ) )
    {
      throw new BeaconRuntimeException( clazz.getName() + " is not a subclass of PickListItem." );
    }
    try
    {
      String parentCode = ContentReaderManager.getContentReader().getAsset( ( (PickListItem)clazz.newInstance() ).getPickListItemsAssetCode() ).getParentAsset().getCode();
      return getPickListItem( clazz, ContentReaderManager.getText( parentCode, "DEFAULT_ITEM_CODE" ) );
    }
    catch( InstantiationException e )
    {
      throw new BeaconRuntimeException( "Exception calling newInstance() on " + clazz.getName(), e );
    }
    catch( IllegalAccessException e )
    {
      throw new BeaconRuntimeException( "Exception calling newInstance() on " + clazz.getName(), e );
    }
  }

  /**
   * Make sure that this is a PickListItem subclass.
   * 
   * @param clazz
   * @return boolean
   */
  private boolean isSubClassOfPickListItem( Class clazz )
  {
    Class superClazz = clazz.getSuperclass();
    while ( superClazz != null )
    {
      if ( superClazz.equals( PickListItem.class ) )
      {
        return true;
      }
      superClazz = superClazz.getSuperclass();
    }
    return false;
  }

  /**
   * 
   */
  private SystemVariableService getSystemVariableService()
  {
    if ( systemVariableService == null )
    {
      systemVariableService = (SystemVariableService)BeanLocator.getBean( SystemVariableService.BEAN_NAME );
    }
    return systemVariableService;
  }
}

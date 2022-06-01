/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javatest/com/biperf/core/domain/enums/MockPickListFactory.java,v $
 */

package com.biperf.core.domain.enums;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.service.system.SystemVariableService;

/**
 * MockPickListFactory.
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
 * <td>Adam</td>
 * <td>May 17, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class MockPickListFactory implements PickListFactory
{

  private SystemVariableService systemVariableService;

  public static PickListItem getMockPickListItem( Class aClass, String code )
  {
    MockPickListFactory factory = new MockPickListFactory();
    return factory.getPickListItem( aClass, code );
  }

  private static final Log LOG = LogFactory.getLog( PickListFactoryImpl.class );

  /**
   * Overridden from
   * 
   * @see com.biperf.core.domain.enums.PickListFactory#getPickListItem(java.lang.Class,
   *      java.lang.String)
   * @param clazz
   * @param code
   * @return PickListItem
   */
  public PickListItem getPickListItem( Class clazz, String code )
  {
    PickListItem pickListItem = null;
    try
    {
      pickListItem = (PickListItem)clazz.newInstance();

      pickListItem.setCode( code );
      pickListItem.setName( code );
      pickListItem.setDesc( code );
    }
    catch( InstantiationException e )
    {
      LOG.error( e.getMessage(), e );
    }
    catch( IllegalAccessException e )
    {
      LOG.error( e.getMessage(), e );
    }

    return pickListItem;
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.domain.enums.PickListFactory#getDefaultPickListItem(java.lang.Class)
   * @param clazz
   * @return PickListItem
   */
  public PickListItem getDefaultPickListItem( Class clazz )
  {
    return getPickListItem( clazz, "1" );
  }

  /**
   * Get the pick list for the specified class from Content Manger. The returned List will be
   * instances of the specified class which is a concrete subclass of PickListItem.
   * 
   * @param clazz
   * @return A list of PickListItems
   */
  public List getPickList( Class clazz )
  {
    List pickList = new ArrayList();
    pickList.add( getPickListItem( clazz, "1" ) );
    pickList.add( getPickListItem( clazz, "2" ) );
    pickList.add( getPickListItem( clazz, "3" ) );
    return pickList;
  }

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
    return getPickList( clazz );
  }

  /**
   * Get a description field of this item. Specify the description asset and key into the asset.
   * 
   * @param key
   * @param asset
   * @return String
   */
  public String getItemDescription( String asset, String key )
  {
    return "Mock Description";
  }

  public void setSystemVariableService( SystemVariableService systemVariableService )
  {
    this.systemVariableService = systemVariableService;
  }

  public SystemVariableService getSystemVariableService()
  {
    return systemVariableService;
  }

}

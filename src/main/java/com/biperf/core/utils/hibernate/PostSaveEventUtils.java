/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/utils/hibernate/PostSaveEventUtils.java,v $
 */

package com.biperf.core.utils.hibernate;

import org.hibernate.event.PostUpdateEvent;
import org.hibernate.util.EqualsHelper;

/**
 * PostSaveEventUtils.
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
 * <td>zahler</td>
 * <td>Dec 12, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class PostSaveEventUtils
{

  protected PostSaveEventUtils()
  {
    super();
  }

  /**
   * Checks to see if property has been modified.
   * 
   * @param name
   * @param propertyNames
   * @param currentState
   * @param previousState
   * @return boolean
   */
  public static boolean isPropertyModified( String name, String[] propertyNames, Object[] currentState, Object[] previousState )
  {
    if ( previousState != null && currentState != null )
    {
      for ( int i = 0; i < propertyNames.length; i++ )
      {
        if ( propertyNames[i].equals( name ) )
        {
          if ( !EqualsHelper.equals( previousState[i], currentState[i] ) )
          {
            return true;
          }
        }
      }
    }
    return false;
  }

  public static boolean isModified( PostUpdateEvent event, String... properties )
  {
    for ( String p : properties )
    {
      if ( event.getOldState() != null && PostSaveEventUtils.isPropertyModified( p, event.getPersister().getPropertyNames(), event.getState(), event.getOldState() ) )
      {
        return true;
      }
    }
    return false;
  }

  /**
   * Gets the object representation of the property.
   * 
   * @param name
   * @param propertyNames
   * @param state
   * @return Object
   */
  public static Object getProperty( String name, String[] propertyNames, Object[] state )
  {
    for ( int i = 0; i < propertyNames.length; i++ )
    {
      if ( propertyNames[i].equals( name ) )
      {
        return state[i];
      }
    }
    return null;
  }
}

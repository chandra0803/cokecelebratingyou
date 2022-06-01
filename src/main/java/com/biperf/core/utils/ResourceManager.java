/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/utils/ResourceManager.java,v $
 */

package com.biperf.core.utils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * ResourceManager.
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
 * <td>jdunne</td>
 * <td>Feb 23, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ResourceManager
{

  private static final Log logger = LogFactory.getLog( ResourceManager.class );

  private static final String COUNT = "Count";

  private static ThreadLocal resources = new ThreadLocal();

  // -------------------------------------------------------------------------
  // Management of transaction-associated resource handles
  // -------------------------------------------------------------------------

  /**
   * to initialize the thread local resources
   */
  public static void initResources()
  {
    if ( null == resources )
    {
      resources = new ThreadLocal();
    }
  }

  /**
   * to release the thread local resources
   */
  public static void destroyResources()
  {
    resources = null;
  }

  /**
   * Return all resources that are bound to the current thread.
   * <p>
   * Mainly for debugging purposes. Resource managers should always invoke hasResource for a
   * specific resource key that they are interested in.
   * 
   * @return Map with resource keys and resource objects, or empty Map if currently none bound
   * @see #hasResource
   */
  public static Map getResourceMap()
  {
    Map map = (Map)resources.get();
    if ( map == null )
    {
      map = new HashMap();
    }
    return Collections.unmodifiableMap( map );
  }

  /**
   * Check if there is a resource for the given key bound to the current thread.
   * 
   * @param key key to check
   * @return if there is a value bound to the current thread
   */
  public static boolean hasResource( Object key )
  {
    Map map = (Map)resources.get();
    if ( map == null )
    {
      map = new HashMap();
      resources.set( map );
    }
    return map.containsKey( key );
  }

  /**
   * Retrieve a resource for the given key that is bound to the current thread.
   * 
   * @param key key to check
   * @return a value bound to the current thread, or null if none
   */
  public static Object getResource( Object key )
  {
    Map map = (Map)resources.get();
    if ( map == null )
    {
      return null;
    }
    Object value = map.get( key );
    return value;
  }

  /**
   * Sets the given resource for the given key to the current thread.
   * 
   * @param key
   * @param value
   */
  public static void setResource( Object key, Object value )
  {
    Map map = (Map)resources.get();
    // set ThreadLocal Map if none found
    if ( map == null )
    {
      map = new HashMap();
      resources.set( map );
    }
    map.put( key, value );
  }

  /**
   * Sets the given resource for the given key reference counted to the current thread.
   * 
   * @param key
   * @param value
   */
  public static void setResourceShallowCopy( String key, Object value )
  {
    Map map = (Map)resources.get();
    // set ThreadLocal Map if none found
    if ( map == null )
    {
      map = new HashMap();
      resources.set( map );
    }
    map.put( key, value );

    String countKey = key + COUNT;

    // set the reference count for the key
    if ( !hasResource( countKey ) )
    {
      ResourceManager.setResource( countKey, new Long( 1 ) );
    }
    else
    {
      ResourceManager.setResource( countKey, new Long( ( (Long)ResourceManager.getResource( countKey ) ).longValue() + 1 ) );
    }

  }

  /**
   * Removes the resource by key.
   * 
   * @param key
   */
  public static void removeResource( Object key )
  {
    ResourceManager.setResource( key, null );
  }

  /**
   * Removes the reference counted resource by key.
   * 
   * @param key
   */
  public static void removeResourceShallowCopy( String key )
  {
    String countKey = key + COUNT;

    // check the reference count, if equals 1 unbind keys else decrement
    if ( !hasResource( countKey ) )
    {
      ResourceManager.unbindResource( key );
    }
    else
    {
      if ( ( (Long)ResourceManager.getResource( countKey ) ).longValue() == 1 )
      {
        ResourceManager.unbindResource( key );
        ResourceManager.unbindResource( countKey );
      }
      else
      {
        ResourceManager.setResource( countKey, new Long( ( (Long)ResourceManager.getResource( countKey ) ).longValue() - 1 ) );
      }
    }
  }

  /**
   * Bind the given resource for the given key to the current thread.
   * 
   * @param key key to bind the value to
   * @param value value to bind
   * @throws IllegalStateException if there is already a value bound to the thread
   */
  public static void bindResource( Object key, Object value ) throws IllegalStateException
  {
    Map map = (Map)resources.get();
    // set ThreadLocal Map if none found
    if ( map == null )
    {
      map = new HashMap();
      resources.set( map );
    }
    if ( map.containsKey( key ) )
    {
      throw new IllegalStateException( "Already value [" + map.get( key ) + "] for key [" + key + "] bound to thread [" + Thread.currentThread().getName() + "]" );
    }
    map.put( key, value );
    if ( logger.isDebugEnabled() )
    {
      logger.debug( "Bound value [" + value + "] for key [" + key + "] to thread [" + Thread.currentThread().getName() + "]" );
    }
  }

  /**
   * Unbind a resource for the given key from the current thread.
   * 
   * @param key key to check
   * @return the previously bound value
   * @throws IllegalStateException if there is no value bound to the thread
   */
  public static Object unbindResource( Object key ) throws IllegalStateException
  {
    Map map = (Map)resources.get();
    if ( map == null || !map.containsKey( key ) )
    {
      throw new IllegalStateException( "No value for key [" + key + "] bound to thread [" + Thread.currentThread().getName() + "]" );
    }
    Object value = map.remove( key );
    // remove entire ThreadLocal if empty
    if ( map.isEmpty() )
    {
      resources.set( null );
    }
    if ( logger.isDebugEnabled() )
    {
      logger.debug( "Removed value [" + value + "] for key [" + key + "] from thread [" + Thread.currentThread().getName() + "]" );
    }
    return value;
  }

}

/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/utils/BeanLocator.java,v $
 */

package com.biperf.core.utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.biperf.core.exception.BeanLocatorException;

/**
 * Utility class to look-up beans from the context.
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
 * <td>crosenquest</td>
 * <td>Mar 11, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class BeanLocator
{

  /**
   * Get the bean by name passed in.
   * 
   * @param beanName
   * @return ParticipantService
   * @throws BeanLocatorException
   */
  public static Object getBean( String beanName ) throws BeanLocatorException
  {

    Object bean = ApplicationContextFactory.getApplicationContext().getBean( beanName );

    if ( bean == null )
    {
      throw new BeanLocatorException( "Failed to find bean with name: " + beanName );
    }

    return bean;

  }

  public static Object getBean( Class<?> clazz ) throws BeanLocatorException
  {

    Object bean = ApplicationContextFactory.getApplicationContext().getBean( clazz );

    if ( bean == null )
    {
      throw new BeanLocatorException( "Failed to find bean with clazz: " + clazz );
    }

    return bean;

  }

  /**
   * Returns the beans of given class type
   * 
   * @param clazz
   * @return
   * @throws BeanLocatorException
   */
  public static <T> Set<T> getBeans( Class<T> clazz ) throws BeanLocatorException
  {
    Map<String, T> beansLst = ApplicationContextFactory.getApplicationContext().getBeansOfType( clazz );

    if ( beansLst == null )
    {
      throw new BeanLocatorException( "Failed to find bean with clazz: " + clazz );
    }

    List<T> beans = new ArrayList<T>( beansLst.values() );

    return new HashSet<T>( beans );
  }

  public static boolean hasBean( String beanName )
  {
    return ApplicationContextFactory.getApplicationContext().containsBean( beanName );
  }
}

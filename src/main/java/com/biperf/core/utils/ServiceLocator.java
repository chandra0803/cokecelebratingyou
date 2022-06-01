/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/utils/ServiceLocator.java,v $
 */

package com.biperf.core.utils;

import com.biperf.core.service.SAO;

/**
 * ServiceLocator.
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
 * <td>Aug 16, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ServiceLocator
{

  /**
   * Bean location through BeanLocator look-up.
   * 
   * @param beanName
   * @return SAO
   */
  public static SAO getService( String beanName )
  {
    return (SAO)BeanLocator.getBean( beanName );
  }

}

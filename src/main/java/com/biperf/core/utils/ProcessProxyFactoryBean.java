/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.utils;

import org.springframework.aop.framework.ProxyFactoryBean;

/**
 * Used so we can search for all process job bean names (searching by the job interface gives that
 * job target bean names, not the job bean names.
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
 * <td>wadzinsk</td>
 * <td>Nov 23, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ProcessProxyFactoryBean extends ProxyFactoryBean
{

  /**
   * 
   */
  public ProcessProxyFactoryBean()
  {
    super();
  }

}

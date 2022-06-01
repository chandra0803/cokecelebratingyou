/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.utils;

import org.hibernate.proxy.HibernateProxy;

import com.biperf.core.domain.BaseDomain;

/**
 * ProxyUtil - Utility Class for performing operations on proxied objects.
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
 * <td>tom</td>
 * <td>Jun 6, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ProxyUtil
{

  /**
   * Standard Util Constructor, protected since only contains static methods.
   */
  protected ProxyUtil()
  {
    super();
  }

  /**
   * Return the actual implementation object for a Proxy. If the object is not a Proxy Object, just
   * return the object. This method should only need to be used when you want to cast a lazy object
   * to a subclass and you don't want to set lazy to true for performance reasons.
   * 
   * @param domainObject
   */
  public static BaseDomain deproxy( BaseDomain domainObject )
  {
    BaseDomain implementation;

    if ( domainObject instanceof HibernateProxy )
    {
      HibernateProxy proxy = (HibernateProxy)domainObject;
      implementation = (BaseDomain)proxy.getHibernateLazyInitializer().getImplementation();
    }
    else
    {
      implementation = domainObject;
    }

    return implementation;
  }

}

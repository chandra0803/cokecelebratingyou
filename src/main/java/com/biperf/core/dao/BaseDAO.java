/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/dao/BaseDAO.java,v $
 */

package com.biperf.core.dao;

import org.hibernate.Session;

import com.biperf.core.utils.HibernateSessionManager;

/**
 * BaseDAO.
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
 * <td>Jun 17, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 */
public abstract class BaseDAO
{
  /**
   * Gets a Hibernate session from the HibernateSessionManager.
   * 
   * @return Session
   */
  protected Session getSession()
  {
    return HibernateSessionManager.getSession();
  }

}

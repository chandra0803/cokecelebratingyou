/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/dao/participant/hibernate/UserNodeHistoryDAOImpl.java,v $
 */

package com.biperf.core.dao.participant.hibernate;

import java.util.List;

import org.hibernate.Query;

import com.biperf.core.dao.BaseDAO;
import com.biperf.core.dao.participant.UserNodeHistoryDAO;

/**
 * UserNodeHistoryDAOImpl.
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
 */
public class UserNodeHistoryDAOImpl extends BaseDAO implements UserNodeHistoryDAO
{

  /**
   * Overridden from
   * 
   * @see com.biperf.core.dao.participant.UserNodeHistoryDAO#getAllUserNodeHistoryByUser(java.lang.Long)
   * @param userId
   * @return List of UserNodeHistory records
   */
  public List getAllUserNodeHistoryByUser( Long userId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.user.GetUserNodeHistoryByUserId" );
    query.setParameter( "userId", userId );
    return query.list();
  }

}

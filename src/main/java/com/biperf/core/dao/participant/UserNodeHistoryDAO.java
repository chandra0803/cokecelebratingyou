/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/dao/participant/UserNodeHistoryDAO.java,v $
 */

package com.biperf.core.dao.participant;

import java.util.List;

import com.biperf.core.dao.DAO;

/**
 * UserNodeHistoryDAO.
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
public interface UserNodeHistoryDAO extends DAO
{
  public static final String BEAN_NAME = "userNodeHistoryDAO";

  /**
   * Returns a list of usernode history by the userId
   * 
   * @param userId
   * @return List
   */
  public List getAllUserNodeHistoryByUser( Long userId );

}

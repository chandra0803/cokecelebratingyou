/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/service/participant/UserNodeHistoryService.java,v $
 */

package com.biperf.core.service.participant;

import java.util.List;

import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.SAO;

/**
 * UserNodeHistoryService.
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
 * <td>Dec 15, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public interface UserNodeHistoryService extends SAO
{
  public static final String BEAN_NAME = "userNodeHistoryService";

  /**
   * Returns a list of usernode history by the userId
   * 
   * @param userId
   * @return List
   */
  public List getAllUserNodeHistoryByUser( Long userId );

  /**
   * Returns a list of usernode history by the userId
   * 
   * @param userId
   * @return List
   */
  public List getAllUserNodeHistoryByUserId( Long userId, AssociationRequestCollection requestCollection );
}

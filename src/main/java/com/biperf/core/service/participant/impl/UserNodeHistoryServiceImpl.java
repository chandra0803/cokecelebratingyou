/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/service/participant/impl/UserNodeHistoryServiceImpl.java,v $
 */

package com.biperf.core.service.participant.impl;

import java.util.Iterator;
import java.util.List;

import com.biperf.core.dao.participant.UserNodeHistoryDAO;
import com.biperf.core.domain.user.UserNodeHistory;
import com.biperf.core.service.AssociationRequest;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.participant.UserNodeHistoryService;

/**
 * UserNodeHistoryServiceImpl.
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
public class UserNodeHistoryServiceImpl implements UserNodeHistoryService
{
  UserNodeHistoryDAO userNodeHistoryDAO;

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.participant.UserNodeHistoryService#getAllUserNodeHistoryByUser(java.lang.Long)
   * @param userId
   * @return List
   */
  public List getAllUserNodeHistoryByUser( Long userId )
  {
    return userNodeHistoryDAO.getAllUserNodeHistoryByUser( userId );
  }

  /**
   * Overridden from UserNodeHistoryService
   * 
   * @see UserNodeHistoryService#getAllUserNodeHistoryByUserId(Long,
   *      com.biperf.core.service.AssociationRequestCollection)
   * @param userId
   * @return List of UserNodeHistory
   */
  public List getAllUserNodeHistoryByUserId( Long userId, AssociationRequestCollection requestCollection )
  {
    List userNodeHistoryList = userNodeHistoryDAO.getAllUserNodeHistoryByUser( userId );
    if ( userNodeHistoryList != null && !userNodeHistoryList.isEmpty() )
    {
      for ( Iterator iter = requestCollection.iterator(); iter.hasNext(); )
      {
        AssociationRequest request = (AssociationRequest)iter.next();
        for ( Iterator logIter = userNodeHistoryList.iterator(); logIter.hasNext(); )
        {
          UserNodeHistory userNodeHistory = (UserNodeHistory)logIter.next();
          request.execute( userNodeHistory );
        }
      }
    }
    return userNodeHistoryList;
  }

  public void setUserNodeHistoryDAO( UserNodeHistoryDAO userNodeHistoryDAO )
  {
    this.userNodeHistoryDAO = userNodeHistoryDAO;
  }

}

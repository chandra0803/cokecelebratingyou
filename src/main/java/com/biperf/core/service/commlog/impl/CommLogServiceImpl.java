/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/commlog/impl/CommLogServiceImpl.java,v $
 */

package com.biperf.core.service.commlog.impl;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.biperf.core.dao.commlog.CommLogDAO;
import com.biperf.core.domain.commlog.CommLog;
import com.biperf.core.service.AssociationRequest;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.commlog.CommLogService;
import com.biperf.core.value.CommLogValueBean;

/**
 * CommLogServiceImpl.
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
 * <td>Ashok Attada</td>
 * <td>Nov 18, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class CommLogServiceImpl implements CommLogService
{

  /**
   * CommLogDAO *
   */
  private CommLogDAO commLogDAO;

  /**
   * Overridden from CommLogService
   * 
   * @see CommLogService#getCommLogsForUser(Long,
   *      com.biperf.core.service.AssociationRequestCollection)
   * @param userId
   * @return List of UserCommLogs
   */
  public List getCommLogsForUser( Long userId, AssociationRequestCollection requestCollection )
  {
    List userCommLogs = commLogDAO.getCommLogsByUser( userId );
    if ( userCommLogs != null && !userCommLogs.isEmpty() )
    {
      for ( Iterator iter = requestCollection.iterator(); iter.hasNext(); )
      {
        AssociationRequest request = (AssociationRequest)iter.next();
        for ( Iterator logIter = userCommLogs.iterator(); logIter.hasNext(); )
        {
          CommLog log = (CommLog)logIter.next();
          request.execute( log );
        }
      }
    }
    return userCommLogs;
  }

  @Override
  public List<CommLogValueBean> getCommLogsForUser( Long userId, int pageNumber, int pageSize, Date startDate, Date endDate, Integer sortedOn, String sortedBy )
  {
    return commLogDAO.getNonFailureCommLogsByUser( userId, pageNumber, pageSize, startDate, endDate, sortedOn, sortedBy );
  }

  @Override
  public Long getCommLogCountForUser( Long userId, Date startDate, Date endDate )
  {
    return commLogDAO.getNonFailureCommLogCountByUser( userId, startDate, endDate );
  }

  /**
   * Overridden from CommLogService
   * 
   * @see CommLogService#saveCommLog(com.biperf.core.domain.commlog.CommLog)
   * @param commLog
   * @return CommLog
   */
  public CommLog saveCommLog( CommLog commLog )
  {
    return commLogDAO.saveCommLog( commLog );
  }

  public List getCommLogsAssignedToUser( Long userId, AssociationRequestCollection requestCollection )
  {
    List userCommLogs = commLogDAO.getCommLogsAssignedToUser( userId );
    hydrateCommLogList( userCommLogs, requestCollection );
    return userCommLogs;
  }

  /**
   * Get a list of all open CommLogs assigned to the user.
   * 
   * @param userId
   * @param associationRequestCollection
   * @return List
   */
  public List getOpenCommLogsAssignedToUser( Long userId, AssociationRequestCollection associationRequestCollection )
  {
    List openCommLogs = commLogDAO.getOpenCommLogsAssignedToUser( userId );
    Iterator openCommLogsIterator = openCommLogs.iterator();
    while ( openCommLogsIterator.hasNext() )
    {
      CommLog log = (CommLog)openCommLogsIterator.next();
      if ( log.getCommLogUrgencyType().isEscalated() )
      {
        openCommLogsIterator.remove();
      }
    }
    hydrateCommLogList( openCommLogs, associationRequestCollection );
    return openCommLogs;
  }

  /**
   * Get all open and escalated CommLogs assigned to the user.
   * 
   * @param userId
   * @param associationRequestCollection
   * @return List
   */
  public List getEscalatedCommLogsAssignedToUser( Long userId, AssociationRequestCollection associationRequestCollection )
  {
    List escalatedCommLogs = commLogDAO.getOpenCommLogsAssignedToUser( userId );
    Iterator escaltedCommLogsIterator = escalatedCommLogs.iterator();
    while ( escaltedCommLogsIterator.hasNext() )
    {
      CommLog log = (CommLog)escaltedCommLogsIterator.next();
      if ( !log.getCommLogUrgencyType().isEscalated() )
      {
        escaltedCommLogsIterator.remove();
      }
    }
    hydrateCommLogList( escalatedCommLogs, associationRequestCollection );
    return escalatedCommLogs;
  }

  /**
   * Hydrate the list of CommLogs.
   * 
   * @param commLogList
   * @param requestCollection
   */
  private void hydrateCommLogList( List commLogList, AssociationRequestCollection requestCollection )
  {
    if ( requestCollection == null )
    {
      return;
    }

    if ( commLogList != null && !commLogList.isEmpty() )
    {
      for ( Iterator iter = requestCollection.iterator(); iter.hasNext(); )
      {
        AssociationRequest request = (AssociationRequest)iter.next();
        for ( Iterator logIter = commLogList.iterator(); logIter.hasNext(); )
        {
          CommLog log = (CommLog)logIter.next();
          request.execute( log );
        }
      }
    }
  }

  public CommLog getCommLogById( Long commLogId, AssociationRequestCollection requestCollection )
  {
    CommLog log = commLogDAO.getCommLogById( commLogId );
    for ( Iterator iter = requestCollection.iterator(); iter.hasNext(); )
    {
      AssociationRequest request = (AssociationRequest)iter.next();
      request.execute( log );
    }
    return log;
  }

  public CommLogDAO getCommLogDAO()
  {
    return commLogDAO;
  }

  public void setCommLogDAO( CommLogDAO commLogDAO )
  {
    this.commLogDAO = commLogDAO;
  }

  public List getAllOpenCommLogs( AssociationRequestCollection requestCollection )
  {
    List commLogs = commLogDAO.getAllOpenCommLogs();
    for ( Iterator iterator = commLogs.iterator(); iterator.hasNext(); )
    {
      CommLog log = (CommLog)iterator.next();
      for ( Iterator iter = requestCollection.iterator(); iter.hasNext(); )
      {
        AssociationRequest request = (AssociationRequest)iter.next();
        request.execute( log );
      }
    }
    return commLogs;
  }

}

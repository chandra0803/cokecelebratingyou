/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/dao/commlog/CommLogDAO.java,v $
 *
 */

package com.biperf.core.dao.commlog;

import java.util.Date;
import java.util.List;

import com.biperf.core.dao.DAO;
import com.biperf.core.domain.commlog.CommLog;

/**
 * CommLogDAO <p/> <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>dunne</td>
 * <td>Nov 21, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 * @author dunne
 */
public interface CommLogDAO extends DAO
{
  /**
   * Get all commLogs for this user.
   * 
   * @param userId
   * @return List
   */
  public List getCommLogsByUser( Long userId );

  public List getNonFailureCommLogsByUser( Long userId, int pageNumber, int pageSize, Date startDate, Date endDate, Integer sortedOn, String sortedBy );

  public Long getNonFailureCommLogCountByUser( Long userId, Date startDate, Date endDate );

  /**
   * Get all commLogs assigned to this user.
   * 
   * @param userId
   * @return List
   */
  public List getCommLogsAssignedToUser( Long userId );

  /**
   * Get all open commLogs assigned to this user.
   * 
   * @param userId
   * @return List
   */
  public List getOpenCommLogsAssignedToUser( Long userId );

  /**
   * Get all open commLogs.
   * 
   * @return List
   */
  public List getAllOpenCommLogs();

  /**
   * Save this commLog.
   * 
   * @param commLog
   * @return CommLog
   */
  public CommLog saveCommLog( CommLog commLog );

  /**
   * Get CommLog by id.
   * 
   * @param commLogId
   * @return CommLog
   */
  public CommLog getCommLogById( Long commLogId );

}

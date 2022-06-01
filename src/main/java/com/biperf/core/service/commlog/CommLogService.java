/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/commlog/CommLogService.java,v $
 */

package com.biperf.core.service.commlog;

import java.util.Date;
import java.util.List;

import com.biperf.core.domain.commlog.CommLog;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.SAO;
import com.biperf.core.value.CommLogValueBean;

/**
 * CommLogService.
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
public interface CommLogService extends SAO
{
  /** Name of service bean in the beanFactory */
  public static final String BEAN_NAME = "commLogService";

  /**
   * Get the comm log list for a user by user id
   * 
   * @param userId
   * @param requestCollection
   * @return List
   */
  public List getCommLogsForUser( Long userId, AssociationRequestCollection requestCollection );

  /**
   * Save this commLog.
   * 
   * @param commLog
   * @return CommLog
   */
  public CommLog saveCommLog( CommLog commLog );

  /**
   * Get all commLogs assigned to this user.
   * 
   * @param userId
   * @return List
   */
  public List getCommLogsAssignedToUser( Long userId, AssociationRequestCollection requestCollection );

  /**
   * Get a list of all open CommLogs assigned to the user.
   * 
   * @param userId
   * @param requestCollection
   * @return List
   */
  public List getOpenCommLogsAssignedToUser( Long userId, AssociationRequestCollection requestCollection );

  /**
   * Get a list of all open CommLogs.
   * 
   * @param requestCollection
   * @return List
   */
  public List getAllOpenCommLogs( AssociationRequestCollection requestCollection );

  /**
   * Get all open and escalated CommLogs assigned to the user.
   * 
   * @param userId
   * @param requestCollection
   * @return List
   */
  public List getEscalatedCommLogsAssignedToUser( Long userId, AssociationRequestCollection requestCollection );

  /**
   * Get CommLog by id.
   * 
   * @param commLogId
   * @return CommLog
   */
  public CommLog getCommLogById( Long commLogId, AssociationRequestCollection requestCollection );

  public List<CommLogValueBean> getCommLogsForUser( Long userId, int pageNumber, int pageSize, Date startDate, Date endDate, Integer sortedOn, String sortedBy );

  public Long getCommLogCountForUser( Long userId, Date startDate, Date endDate );

} // end CommLogService

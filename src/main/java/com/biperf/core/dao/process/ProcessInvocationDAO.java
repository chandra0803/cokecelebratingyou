/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/dao/process/ProcessInvocationDAO.java,v $
 */

package com.biperf.core.dao.process;

import java.util.List;

import com.biperf.core.dao.DAO;
import com.biperf.core.domain.process.Process;
import com.biperf.core.domain.process.ProcessInvocation;
import com.biperf.core.service.AssociationRequestCollection;

/**
 * ProcessInvocationDAO.
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
 * <td>leep</td>
 * <td>November 28, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 */
public interface ProcessInvocationDAO extends DAO
{

  public static final String BEAN_NAME = "processInvocationDAO";

  /**
   * Get the process from the database by the id.
   * 
   * @param id
   * @return ProcessInvocation
   */
  public ProcessInvocation getProcessInvocationById( Long id );

  /**
   * Get the process from the database by the id.
   * 
   * @param id
   * @return Process
   */
  public ProcessInvocation getProcessInvocationByIdWithAssociations( Long id, AssociationRequestCollection associationRequestCollection );

  public int getProcessInvocationCountByProcess( Process process );

  /**
   * Get the process from the database by the id.
   * 
   * @param process
   * @return List
   */
  public List getProcessInvocationsByProcess( Process process );

  /**
   * Get the process from the database by the id.
   * 
   * @param process
   * @param associationRequestCollection
   * @return List
   */
  public List getProcessInvocationsByProcessWithAssociations( Process process, AssociationRequestCollection associationRequestCollection );

  public List getProcessInvocationsByProcessWithAssociations( Process process, AssociationRequestCollection associationRequestCollection, int pageNumber, int pageSize );

  /**
   * Saves the process to the database.
   * 
   * @param processInvocation
   * @return ProcessInvocation
   */
  public ProcessInvocation save( ProcessInvocation processInvocation );
}

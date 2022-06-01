/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/service/process/impl/ProcessInvocationServiceImpl.java,v $
 */

package com.biperf.core.service.process.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.biperf.core.dao.process.ProcessInvocationDAO;
import com.biperf.core.domain.process.Process;
import com.biperf.core.domain.process.ProcessInvocation;
import com.biperf.core.domain.process.ProcessInvocationComment;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.process.ProcessInvocationService;

/**
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
 * <td>wadzinsk</td>
 * <td>Nov 17, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ProcessInvocationServiceImpl implements ProcessInvocationService
{
  private ProcessInvocationDAO processInvocationDAO = null;

  public ProcessInvocation getProcessInvocationById( Long id )
  {
    return this.processInvocationDAO.getProcessInvocationById( id );
  }

  public ProcessInvocation getProcessInvocationById( Long id, AssociationRequestCollection associationRequestCollection )
  {
    return this.processInvocationDAO.getProcessInvocationByIdWithAssociations( id, associationRequestCollection );
  }

  public List getProcessInvocationsByProcess( Process process )
  {
    return this.processInvocationDAO.getProcessInvocationsByProcess( process );
  }

  public List getProcessInvocationsByProcessWithAssociations( Process process, AssociationRequestCollection associationRequestCollection )
  {
    return this.processInvocationDAO.getProcessInvocationsByProcessWithAssociations( process, associationRequestCollection );
  }

  public int getProcessInvocationCountByProcess( Process process )
  {
    return this.processInvocationDAO.getProcessInvocationCountByProcess( process );
  }

  public List getProcessInvocationsByProcessWithAssociations( Process process, AssociationRequestCollection associationRequestCollection, int pageNumber, int pageSize )
  {

    return this.processInvocationDAO.getProcessInvocationsByProcessWithAssociations( process, associationRequestCollection, pageNumber, pageSize );
  }

  public ProcessInvocation save( ProcessInvocation processInvocation )
  {
    return getProcessInvocationDAO().save( processInvocation );
  }

  public ProcessInvocationDAO getProcessInvocationDAO()
  {
    return processInvocationDAO;
  }

  public void setProcessInvocationDAO( ProcessInvocationDAO processInvocationDAO )
  {
    this.processInvocationDAO = processInvocationDAO;
  }

  /**
   * Add a comment. Length will be trucated to {@link ProcessInvocationService#COMMENT_COLUMN_SIZE}
   * 
   * @param processInvocationId
   * @param comment
   */
  public void addComment( Long processInvocationId, String comment )
  {
    addComment( processInvocationId, comment, true );
  }

  /**
   * Add a comment. If truncateComment is true, length will be trucated to
   * {@link ProcessInvocationService#COMMENT_COLUMN_SIZE}
   * 
   * @param processInvocationId
   * @param comment
   * @param truncateComment
   */
  public void addComment( Long processInvocationId, String comment, boolean truncateComment )
  {
    if ( truncateComment )
    {
      comment = StringUtils.left( comment, COMMENT_COLUMN_SIZE );
    }

    ProcessInvocation processInvocation = processInvocationDAO.getProcessInvocationById( processInvocationId );
    processInvocation.addProcessInvocationComment( new ProcessInvocationComment( comment ) );

    processInvocationDAO.save( processInvocation );
  }
}

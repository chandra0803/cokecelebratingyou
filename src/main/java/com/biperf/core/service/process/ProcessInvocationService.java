/*
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/process/ProcessInvocationService.java,v $
 * (c) 2005 BI, Inc.  All rights reserved.
 */

package com.biperf.core.service.process;

import java.util.List;

import com.biperf.core.domain.process.Process;
import com.biperf.core.domain.process.ProcessInvocation;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.SAO;

/**
 * ProcessInvocationService.
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
 * <td>sharma</td>
 * <td>Jun 2, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public interface ProcessInvocationService extends SAO
{
  /** Name of service bean in the beanFactory */
  public static final String BEAN_NAME = "processInvocationService";

  public static final int COMMENT_COLUMN_SIZE = 250;

  public ProcessInvocation getProcessInvocationById( Long id );

  public ProcessInvocation getProcessInvocationById( Long id, AssociationRequestCollection associationRequestCollection );

  public List getProcessInvocationsByProcess( Process process );

  public List getProcessInvocationsByProcessWithAssociations( Process process, AssociationRequestCollection associationRequestCollection );

  public List getProcessInvocationsByProcessWithAssociations( Process process, AssociationRequestCollection associationRequestCollection, int pageNumber, int pageSize );

  public ProcessInvocation save( ProcessInvocation process );

  public void addComment( Long processInvocationId, String string );

  public void addComment( Long processInvocationId, String string, boolean truncateComment );

  public int getProcessInvocationCountByProcess( Process process );
}

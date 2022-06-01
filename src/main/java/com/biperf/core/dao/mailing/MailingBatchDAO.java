/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/dao/mailing/MailingBatchDAO.java,v $
 */

package com.biperf.core.dao.mailing;

import java.util.List;

import com.biperf.core.domain.mailing.MailingBatch;
import com.biperf.core.service.SAO;

/**
 * MailingBatchDAO.
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
 * <td>kumar</td>
 * <td>March 10 2011</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 */
public interface MailingBatchDAO extends SAO
{
  public static final String BEAN_NAME = "mailingBatchDAO";

  /**
   * Save the MailingBatch.
   * 
   * @param mailingBatch
   * @return MailingBatch
   */
  public MailingBatch saveMailingBatch( MailingBatch mailingBatch );

  /**
   * Get the MailingBatch by id
   * 
   * @param id
   * @return MailingBatch
   */
  public MailingBatch getMailingBatchById( Long id );

  /**
   * Get all mailings for given batchId.
   */
  public List getMailingsForBatchId( Long batchId );

}

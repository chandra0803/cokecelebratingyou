/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/dao/mailing/hibernate/MailingBatchDAOImpl.java,v $
 */

package com.biperf.core.dao.mailing.hibernate;

import java.util.List;

import org.hibernate.Query;

import com.biperf.core.dao.BaseDAO;
import com.biperf.core.dao.mailing.MailingBatchDAO;
import com.biperf.core.domain.mailing.MailingBatch;
import com.biperf.core.utils.hibernate.HibernateUtil;

/**
 * MailingBatchDAOImpl.
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
 * <td>May 10,2011</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 */
public class MailingBatchDAOImpl extends BaseDAO implements MailingBatchDAO
{
  /**
   * Overridden from
   * 
   * @see com.biperf.core.dao.mailing.MailingBatchDAO#getMailingBatchById(java.lang.Long)
   * @param id
   * @return MailingBatch
   */
  public MailingBatch getMailingBatchById( Long id )
  {
    return (MailingBatch)getSession().get( MailingBatch.class, id );
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.dao.mailing.MailingBatchDAO#saveMailingBatch(com.biperf.core.domain.mailing.MailingBatch)
   * @param mailingBatch
   * @return MailingBatch
   */
  public MailingBatch saveMailingBatch( MailingBatch mailingBatch )
  {
    return (MailingBatch)HibernateUtil.saveOrUpdateOrShallowMerge( mailingBatch );
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.dao.mailing.MailingBatchDAO#getMailingsForBatchId(java.lang.Long)
   * @param messageId
   * @return List of Mailings with given batch Id
   */
  public List getMailingsForBatchId( Long batchId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.mailing.MailBatchId" );
    query.setLong( "batchId", batchId.longValue() );

    return query.list();
  }

}

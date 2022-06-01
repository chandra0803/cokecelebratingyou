/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/dao/audit/hibernate/PayoutCalculationAuditDAOImpl.java,v $
 */

package com.biperf.core.dao.audit.hibernate;

import java.util.List;

import org.hibernate.Query;

import com.biperf.core.dao.BaseDAO;
import com.biperf.core.dao.audit.PayoutCalculationAuditDAO;
import com.biperf.core.domain.audit.PayoutCalculationAudit;
import com.biperf.core.utils.hibernate.HibernateUtil;

/*
 * PayoutCalculationAuditDAOImpl <p> <b>Change History:</b><br> <table border="1"> <tr> <th>Author</th>
 * <th>Date</th> <th>Version</th> <th>Comments</th> </tr> <tr> <td>Thomas Eaton</td> <td>Aug
 * 18, 2005</td> <td>1.0</td> <td>created</td> </tr> </table> </p>
 * 
 */

public class PayoutCalculationAuditDAOImpl extends BaseDAO implements PayoutCalculationAuditDAO
{

  /**
   * Fetches all payout calculation audit records.
   * 
   * @return all payout calculation audit records, as a <code>List</code> of
   *         {@link PayoutCalculationAudit} objects.
   */
  public List getAll()
  {
    return getSession().getNamedQuery( "com.biperf.core.domain.audit.AllSalesPayoutCalculationAuditRecords" ).list();
  }

  /**
   * Fetches a payout calculation audit record by ID.
   * 
   * @param id the ID of the payout calculation audit record to fetch.
   * @return the specified payout calculation audit record.
   */
  public PayoutCalculationAudit getPayoutCalculationAuditById( Long id )
  {
    return (PayoutCalculationAudit)getSession().get( PayoutCalculationAudit.class, id );
  }

  /**
   * Fetches a payout calculation audit record by ID.
   * 
   * @param claimId
   * @param participantId
   * @return the specified payout calculation audit record.
   */
  public List getPayoutCalculationAuditsByClaimIdAndParticipantId( Long claimId, Long participantId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.audit.PayoutCalculationAuditRecordsListByClaimIdAndParticipantId" );

    query.setLong( "claimId", claimId.longValue() );
    query.setLong( "participantId", participantId.longValue() );

    return query.list();
  }

  /**
   * Saves the given payout calculation audit record.
   * 
   * @param auditRecord the payout calculation audit record to save.
   * @return the saved version of the payout calculation audit record.
   */
  public PayoutCalculationAudit save( PayoutCalculationAudit auditRecord )
  {
    return (PayoutCalculationAudit)HibernateUtil.saveOrUpdateOrShallowMerge( auditRecord );
  }

  /**
   * Fetches a List payout calculation audits record by claimId
   * 
   * @param claimId
   * @return List.
   */
  public List getPayoutCalculationAuditsByClaimId( Long claimId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.audit.PayoutCalculationAuditRecordsListByClaimId" );
    query.setLong( "claimId", claimId.longValue() );

    return query.list();
  }

  public boolean isManagerOverrideExist( Long journalId, Long participantId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.audit.isManagerOverrideExist" );
    query.setParameter( "participantId", participantId );
    query.setParameter( "journalId", journalId );

    return query.list() != null && query.list().size() > 0;
  }

  public boolean isCPAwardExist( Long journalId, Long participantId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.audit.isCPAwardExist" );
    query.setParameter( "participantId", participantId );
    query.setParameter( "journalId", journalId );

    return query.list() != null && query.list().size() > 0;
  }
}

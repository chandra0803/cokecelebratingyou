/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/audit/impl/PayoutCalculationAuditServiceImpl.java,v $
 */

package com.biperf.core.service.audit.impl;

import java.util.List;

import com.biperf.core.dao.audit.PayoutCalculationAuditDAO;
import com.biperf.core.domain.audit.PayoutCalculationAudit;
import com.biperf.core.service.audit.PayoutCalculationAuditService;

/*
 * ActivityServiceImpl <p> <b>Change History:</b><br> <table border="1"> <tr> <th>Author</th>
 * <th>Date</th> <th>Version</th> <th>Comments</th> </tr> <tr> <td>OPI Admin</td> <td>Jul
 * 14, 2005</td> <td>1.0</td> <td>created</td> </tr> </table> </p>
 * 
 *
 */
public class PayoutCalculationAuditServiceImpl implements PayoutCalculationAuditService
{
  private PayoutCalculationAuditDAO payoutCalculationAuditDAO;

  public void setPayoutCalculationAuditDAO( PayoutCalculationAuditDAO payoutCalculationAuditDAO )
  {
    this.payoutCalculationAuditDAO = payoutCalculationAuditDAO;
  }

  /**
   * Fetches all payout calculation audit records.
   * 
   * @return all payout calculation audit records, as a <code>List</code> of
   *         {@link PayoutCalculationAudit} objects.
   */
  public List getAll()
  {
    return this.payoutCalculationAuditDAO.getAll();
  }

  /**
   * Fetches a payout calculation audit record by ID.
   * 
   * @param id the ID of the payout calculation audit record to fetch.
   * @return the specified payout calculation audit record.
   */
  public PayoutCalculationAudit getPayoutCalculationAuditById( Long id )
  {
    return this.payoutCalculationAuditDAO.getPayoutCalculationAuditById( id );
  }

  public List getPayoutCalculationAuditsByClaimIdAndParticipantId( Long claimId, Long participantId )
  {
    return this.payoutCalculationAuditDAO.getPayoutCalculationAuditsByClaimIdAndParticipantId( claimId, participantId );
  }

  /**
   * Saves the given payout calculation audit record.
   * 
   * @param auditRecord the payout calculation audit record to save.
   * @return the saved version of the payout calculation audit record.
   */
  public PayoutCalculationAudit save( PayoutCalculationAudit auditRecord )
  {
    return this.payoutCalculationAuditDAO.save( auditRecord );
  }

  /**
   * Fetches a List payout calculation audits record by claimId
   * 
   * @param claimId
   * @return List.
   */
  public List getPayoutCalculationAuditsByClaimId( Long claimId )
  {
    return this.payoutCalculationAuditDAO.getPayoutCalculationAuditsByClaimId( claimId );
  }

  public boolean isManagerOverrideExist( Long journalId, Long participantId )
  {
    return this.payoutCalculationAuditDAO.isManagerOverrideExist( journalId, participantId );
  }
}

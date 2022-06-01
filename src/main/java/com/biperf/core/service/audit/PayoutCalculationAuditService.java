/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/audit/PayoutCalculationAuditService.java,v $
 */

package com.biperf.core.service.audit;

import java.util.List;

import com.biperf.core.domain.audit.PayoutCalculationAudit;
import com.biperf.core.service.SAO;

/*
 * AuditService <p> <b>Change History:</b><br> <table border="1"> <tr> <th>Author</th> <th>Date</th>
 * <th>Version</th> <th>Comments</th> </tr> <tr> <td>OPI Admin</td> <td>Jul 14, 2005</td>
 * <td>1.0</td> <td>created</td> </tr> </table> </p>
 * 
 *
 */

public interface PayoutCalculationAuditService extends SAO
{
  /**
   * BEAN_NAME for referencing in tests and spring config files.
   */
  public final String BEAN_NAME = "payoutCalculationAuditService";

  /**
   * Fetches all payout calculation audit records.
   * 
   * @return all payout calculation audit records, as a <code>List</code> of
   *         {@link PayoutCalculationAudit} objects.
   */
  public List getAll();

  /**
   * Fetches a payout calculation audit record by ID.
   * 
   * @param id the ID of the payout calculation audit record to fetch.
   * @return the specified payout calculation audit record.
   */
  public PayoutCalculationAudit getPayoutCalculationAuditById( Long id );

  /**
   * Fetches a List payout calculation audits record by claimId and participantId.
   * 
   * @param claimId
   * @param participantId
   * @return List.
   */
  public List getPayoutCalculationAuditsByClaimIdAndParticipantId( Long claimId, Long participantId );

  /**
   * Saves the given payout calculation audit record.
   * 
   * @param auditRecord the payout calculation audit record to save.
   * @return the saved version of the payout calculation audit record.
   */
  public PayoutCalculationAudit save( PayoutCalculationAudit auditRecord );

  /**
   * Fetches a List payout calculation audits record by claimId
   * 
   * @param claimId
   * @return List.
   */
  public List getPayoutCalculationAuditsByClaimId( Long claimId );

  public boolean isManagerOverrideExist( Long journalId, Long participantId );
}

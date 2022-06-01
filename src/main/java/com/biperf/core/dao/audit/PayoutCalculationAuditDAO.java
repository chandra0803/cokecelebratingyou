/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/dao/audit/PayoutCalculationAuditDAO.java,v $
 */

package com.biperf.core.dao.audit;

import java.util.List;

import com.biperf.core.dao.DAO;
import com.biperf.core.domain.audit.PayoutCalculationAudit;

/*
 * PayoutCalculationAuditDAO <p> <b>Change History:</b><br> <table border="1"> <tr> <th>Author</th>
 * <th>Date</th> <th>Version</th> <th>Comments</th> </tr> <tr> <td>Thomas Eaton</td> <td>Aug
 * 18, 2005</td> <td>1.0</td> <td>created</td> </tr> </table> </p>
 * 
 */

public interface PayoutCalculationAuditDAO extends DAO
{
  public static final String BEAN_NAME = "payoutCalculationAuditDAO";

  /**
   * Fetches all payout calculation audit records.
   * 
   * @return all payout calculation audit records, as a <code>List</code> of
   *         {@link PayoutCalculationAudit} objects.
   */
  List getAll();

  /**
   * Fetches a payout calculation audit record by ID.
   * 
   * @param id the ID of the payout calculation audit record to fetch.
   * @return the specified payout calculation audit record.
   */
  PayoutCalculationAudit getPayoutCalculationAuditById( Long id );

  /**
   * Fetches a payout calculation audit record by Claim ID and Participant ID.
   * 
   * @param claimId
   * @param participantId
   * @return the specified payout calculation audit record.
   */
  List getPayoutCalculationAuditsByClaimIdAndParticipantId( Long claimId, Long participantId );

  /**
   * Saves the given payout calculation audit record.
   * 
   * @param auditRecord the payout calculation audit record to save.
   * @return the saved version of the payout calculation audit record.
   */
  PayoutCalculationAudit save( PayoutCalculationAudit auditRecord );

  /**
   * Fetches a List payout calculation audits record by claimId
   * 
   * @param claimId
   * @return List.
   */
  public List getPayoutCalculationAuditsByClaimId( Long claimId );

  public boolean isManagerOverrideExist( Long journalId, Long participantId );

  public boolean isCPAwardExist( Long journalId, Long participantId );
}

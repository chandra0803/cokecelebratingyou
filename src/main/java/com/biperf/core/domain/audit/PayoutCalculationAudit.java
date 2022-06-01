/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/domain/audit/PayoutCalculationAudit.java,v $
 */

package com.biperf.core.domain.audit;

import java.text.MessageFormat;
import java.util.Date;

import com.biperf.core.domain.BaseDomain;
import com.biperf.core.domain.enums.PayoutCalculationAuditReasonType;
import com.biperf.core.domain.journal.Journal;
import com.biperf.core.domain.participant.Participant;

/*
 * PayoutCalculationAudit <p> <b>Change History:</b><br> <table border="1"> <tr> <th>Author</th>
 * <th>Date</th> <th>Version</th> <th>Comments</th> </tr> <tr> <td>Thomas Eaton</td> <td>Aug
 * 18, 2005</td> <td>1.0</td> <td>created</td> </tr> </table> </p>
 * 
 *
 */

public abstract class PayoutCalculationAudit extends BaseDomain
{
  // ---------------------------------------------------------------------------
  // Fields
  // ---------------------------------------------------------------------------

  /**
   * Returns the reason type for a promotion payout calculation. A reason type indicates the result
   * of a promotion payout calculation: Did the calculation succeed or fail? If it failed, why did
   * it fail?
   */
  private PayoutCalculationAuditReasonType reasonType;

  /**
   * Describes the result of a promotion payout calculation. For example, "Claim 120 payed out 10
   * points to participant John Doe on August 1, 2005."
   */
  private String reasonText;

  /**
   * If the promotion payout calculation resulted in a payout, then this field points to the journal
   * entry that describes the payout; otherwise, this field is null.
   */
  private Journal journal;

  private Participant participant;

  private Date processStartDate;

  private Date processEndDate;

  // ---------------------------------------------------------------------------
  // Getter and Setter Methods
  // ---------------------------------------------------------------------------

  /**
   * Returns the journal entry that describes the promotion payout or null if the promotion payout
   * calculation failed.
   * 
   * @return the journal entry that describes the promotion payout or null if the promotion payout
   *         calculation failed.
   */
  public Journal getJournal()
  {
    return journal;
  }

  /**
   * Returns the reason text for a promotion payout calculation.
   * 
   * @return the reason text for a promotion payout calculation.
   */
  public String getReasonText()
  {
    return reasonText;
  }

  /**
   * Returns the reason type for a promotion payout calculation.
   * 
   * @return the reason type for a promotion payout calculation.
   */
  public PayoutCalculationAuditReasonType getReasonType()
  {
    return reasonType;
  }

  /**
   * Sets the journal entry that describes the promotion payout.
   * 
   * @param journal the journal entry that describes the promotion payout.
   */
  public void setJournal( Journal journal )
  {
    this.journal = journal;
  }

  /**
   * Sets the reason text for a promotion payout calculation.
   * 
   * @param reasonText the reason text for a promotion payout calculation.
   */
  public void setReasonText( String reasonText )
  {
    this.reasonText = reasonText;
  }

  /**
   * Sets the reason type for a promotion payout calculation.
   * 
   * @param reasonType the reason type for a promotion payout calculation.
   */
  public void setReasonType( PayoutCalculationAuditReasonType reasonType )
  {
    this.reasonType = reasonType;
  }

  /**
   * Set reasonType and formatted reasonText given the PayoutCalculationAuditReasonType reasonCode
   * and formatting arguments.
   * 
   * @param reasonCode
   * @param arguments
   */
  public void setReason( String reasonCode, String[] arguments )
  {
    // TODO: set locale for lookup to the default language
    reasonType = PayoutCalculationAuditReasonType.lookup( reasonCode );
    reasonText = MessageFormat.format( reasonType.getName(), (Object[])arguments );
  }

  public Participant getParticipant()
  {
    return participant;
  }

  public void setParticipant( Participant participant )
  {
    this.participant = participant;
  }

  public Date getProcessEndDate()
  {
    return processEndDate;
  }

  public void setProcessEndDate( Date processEndDate )
  {
    this.processEndDate = processEndDate;
  }

  public Date getProcessStartDate()
  {
    return processStartDate;
  }

  public void setProcessStartDate( Date processStartDate )
  {
    this.processStartDate = processStartDate;
  }
}

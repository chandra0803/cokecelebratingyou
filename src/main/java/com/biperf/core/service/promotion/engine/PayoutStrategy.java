/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.service.promotion.engine;

import java.util.Set;

import com.biperf.core.domain.journal.Journal;
import com.biperf.core.domain.promotion.Promotion;

/**
 * PayoutStrategy.
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
 * <td>Aug 5, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public interface PayoutStrategy
{

  /**
   * For the specified Promotion, determine the payout (if any) for the specified set of activities.
   * May return one or more failure audit objects that indicate a reason for failure to qualify.
   * 
   * @param activities
   * @param promotion
   * @param payoutCalculationFacts
   * @return Set of PayoutCalculationResult objects
   */
  public Set processActivities( Set activities, Promotion promotion, PayoutCalculationFacts payoutCalculationFacts );

  /**
   * Processing to be done after a successful deposit Journal. eg. Send the recognition email
   * 
   * @param journal
   */
  public void postDeposit( Journal journal );
}

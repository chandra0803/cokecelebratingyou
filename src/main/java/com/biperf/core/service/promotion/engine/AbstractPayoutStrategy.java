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
 * <td>Aug 4, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public abstract class AbstractPayoutStrategy implements PayoutStrategy
{

  /**
   * Subclasses should override this method to perform any processing before processing a claim
   * 
   * @param activities
   * @param promotion
   * @param payoutCalculationFacts
   */
  protected void preProcess( Set activities, Promotion promotion, PayoutCalculationFacts payoutCalculationFacts )
  {
    // Nothing to do
  }

  /**
   * Subclasses should override this method to perform any processing after processing a claim
   * 
   * @param activities
   * @param promotion
   * @param payoutCalculationFacts
   * @param payoutCalculationResults
   */
  protected void postProcess( Set activities, Promotion promotion, PayoutCalculationFacts payoutCalculationFacts, Set payoutCalculationResults )
  {
    // Nothing to do
  }

  /**
   * For the specified Promotion, determine the payout (if any) for the specified activities. May
   * return one or more failure audit objects that indicate a reason for failure to qualify.
   * 
   * @param activities
   * @param promotion
   * @param payoutCalculationFacts
   * @return Set
   */
  public final Set processActivities( Set activities, Promotion promotion, PayoutCalculationFacts payoutCalculationFacts )
  {
    preProcess( activities, promotion, payoutCalculationFacts );

    Set payoutCalculationResults = processActivitiesInternal( activities, promotion, payoutCalculationFacts );

    postProcess( activities, promotion, payoutCalculationFacts, payoutCalculationResults );

    return payoutCalculationResults;
  }

  /**
   * Processing to be done after a successful deposit Journal. eg. Send the recognition email
   * 
   * @param journal
   */
  public void postDeposit( Journal journal )
  {
    // By default, nothing to do. Children can override if necessary.
  }

  /**
   * @param activities
   * @param promotion
   * @param payoutCalculationFacts
   * @return Set
   */
  protected abstract Set processActivitiesInternal( Set activities, Promotion promotion, PayoutCalculationFacts payoutCalculationFacts );

}

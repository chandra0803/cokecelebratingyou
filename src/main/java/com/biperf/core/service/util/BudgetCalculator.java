/*
 * (c) 2006 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/service/util/BudgetCalculator.java,v $
 */

package com.biperf.core.service.util;

import java.util.Set;

import com.biperf.core.domain.claim.Approvable;

public interface BudgetCalculator
{
  /**
   * Updates the current value of the budget based on the total payout for the
   * given payout calculation results.
   *
   * @param payoutCalculationResults  the payouts for the given approvable.
   * @param approvable  a claim or other approvable.
   */
  void calculateBudget( Set payoutCalculationResults, Approvable approvable );
}

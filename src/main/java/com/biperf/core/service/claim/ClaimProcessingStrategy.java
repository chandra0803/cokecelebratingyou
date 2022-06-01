/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/claim/ClaimProcessingStrategy.java,v $
 */

package com.biperf.core.service.claim;

import java.util.Set;

import com.biperf.core.domain.budget.BudgetSegment;
import com.biperf.core.domain.claim.Approvable;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.exception.ServiceErrorException;

/**
 * ClaimProcessingStrategy.
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
 * <td>zahler</td>
 * <td>Oct 18, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public interface ClaimProcessingStrategy
{
  // Fix for bug#56006,55519 start
  /**
   * Process the Claim. Generate activities and payouts.
   * 
   * @param claim
   */
  public void processApprovable( Approvable claim, boolean deductBudget, BudgetSegment budgetSegment ) throws ServiceErrorException;

  /**
   * Calculate and save all payout and related records for the given claim.
   * 
   * @param approvable
   */
  public Set calculateAndSavePayouts( Approvable approvable, boolean deductBudget, BudgetSegment budgetSegment ) throws ServiceErrorException;
  // Fix for bug#56006,55519 end

  /**
   * @param promotion
   * @return String payout type for the promotion
   */
  public String getPromotionPayoutType( Promotion promotion );

  /**
   * @param payoutCalculationResults
   * @param approvable
   */
  public void postProcess( Set payoutCalculationResults, Approvable claim ) throws ServiceErrorException;

}

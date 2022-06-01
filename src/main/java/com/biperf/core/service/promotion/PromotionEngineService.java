/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/promotion/PromotionEngineService.java,v $
 */

package com.biperf.core.service.promotion;

import java.util.List;
import java.util.Set;

import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.ChallengePointPromotion;
import com.biperf.core.domain.promotion.GoalQuestPromotion;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.SAO;
import com.biperf.core.service.awardbanq.GiftcodesResponseValueObject;
import com.biperf.core.service.promotion.engine.GoalCalculationResult;
import com.biperf.core.service.promotion.engine.PayoutCalculationFacts;
import com.biperf.core.value.ChallengepointPaxAwardValueBean;
import com.biperf.core.value.DepositProcessBean;
import com.biperf.core.value.MailingBatchHolder;

/**
 * PromotionEngineService.
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
 * <td>Adam</td>
 * <td>Jul 11, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public interface PromotionEngineService extends SAO
{
  public final String BEAN_NAME = "promotionEngineService";

  /**
   * Calculates the payout for the given promotion and facts.
   * 
   * @param facts facts the help determine the payout.
   * @param promotion the promotion that will be paid out.
   * @param payoutType The payout type of the payout.
   * @return the payouts for the given promotion and facts, as a <code>Set</code> of
   *         {@link com.biperf.core.service.promotion.engine.PayoutCalculationResult} objects.
   */
  public Set calculatePayout( PayoutCalculationFacts facts, Promotion promotion, String payoutType );

  /**
   * Calculates the payout for the given promotion and facts, then persists the various results.
   * 
   * @param facts facts the help determine the payout.
   * @param promotion the promotion that will be paid out.
   * @param participant the participant who will receive the payout.
   * @param payoutType the payout type of the payout.
   * @return the payouts for the given promotion and facts, as a <code>Set</code> of
   *         {@link com.biperf.core.service.promotion.engine.PayoutCalculationResult} objects.
   */
  public Set calculatePayoutAndSaveResults( PayoutCalculationFacts facts, Promotion promotion, Participant participant, String payoutType ) throws ServiceErrorException;

  /**
   * Calculates the payout for the given promotion and activities, then persists the various
   * results.
   * 
   * @param facts facts the help determine the payout.
   * @param promotion the promotion that will be paid out.
   * @param participant the participant who will receive the payout.
   * @param payoutType the payout type of the payout.
   * @param inputActivities the activities to be processed.
   * @return the payouts for the given promotion and facts, as a <code>Set</code> of
   *         {@link com.biperf.core.service.promotion.engine.PayoutCalculationResult} objects.
   */
  public Set calculatePayoutAndSaveResults( PayoutCalculationFacts facts, Promotion promotion, Participant participant, String payoutType, Set inputActivities ) throws ServiceErrorException;

  /**
   * Deposit all of the posted payout record. If a budget is insufficient for deposit of payout
   * Journal record is marked as pending.
   * 
   * @param payoutCalculationResults
   */
  public void depositPostedPayouts( Set payoutCalculationResults ) throws ServiceErrorException;

  /**
   * Deposit all of the approve payout record. If a budget is insufficient for deposit of payout
   * Journal record is marked as pending.
   * 
   * @param payoutCalculationResults
   */
  public void depositApprovedPayouts( Set payoutCalculationResults ) throws ServiceErrorException;

  /**
   * Processes the GoalQuest Calculation Results.
   * Send email with gift code incase of Merchandise or Travel
   * Wrapper method for each goalQuestCalculation
   * @param goalQuestCalculationResults
   * @param promotion
   * @param mailingBatch
   */
  public DepositProcessBean processGoalQuestPayoutCalculationResults( GoalCalculationResult calcResult, GoalQuestPromotion promotion, MailingBatchHolder mailingBatchHolder )
      throws ServiceErrorException;

  /**
   * 
   * Processes the Challengepoint Calculation Results for each cpCaluculationResult
   * Sends email with gift code incase of Merchandise or Travel or Deposit points
   * @param ChallengePointCalculationResults
   * @param promotion
   * @param mailingBatchHolder
   */
  public DepositProcessBean processChallengepointPayoutCalculationResults( List challengepointCalculationResults,
                                                                           ChallengepointPaxAwardValueBean cpPaxAwardValueBean,
                                                                           ChallengePointPromotion promotion,
                                                                           MailingBatchHolder mailingBatchHolder )
      throws ServiceErrorException;

  public void depositSweepstakeApprovedPayouts( Promotion promotion, Set payoutCalculationResults, String winnerType ) throws ServiceErrorException;

  public GiftcodesResponseValueObject retrieveGiftCodesForMerchandiseOrTravelWebService( String programId, Participant participant, long valueOfGiftCode, String batchId, Object... arguments );

}

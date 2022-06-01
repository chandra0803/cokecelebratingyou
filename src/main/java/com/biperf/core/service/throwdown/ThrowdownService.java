/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/throwdown/ThrowdownService.java,v $
 */

package com.biperf.core.service.throwdown;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Set;

import com.biperf.core.domain.promotion.Division;
import com.biperf.core.domain.promotion.Match;
import com.biperf.core.domain.promotion.MatchTeamOutcome;
import com.biperf.core.domain.promotion.Round;
import com.biperf.core.domain.promotion.ThrowdownPromotion;
import com.biperf.core.exception.ServiceErrorExceptionWithRollback;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.SAO;
import com.biperf.core.value.PromotionRoundValue;

public interface ThrowdownService extends SAO
{
  public static String BEAN_NAME = "throwdownService";

  /*
   * Domain save or update methods
   */
  public void deleteDivision( Long divisionId );

  public void buildRoundsForPromotion( Long promotionId );

  /*
   * Domain finder methods
   */
  public ThrowdownPromotion getThrowdownPromotion( Long promotionId );

  public ThrowdownPromotion getThrowdownPromotionByIdWithAssociations( Long promotionId, AssociationRequestCollection associationRequestCollection );

  public Division getDivision( Long divisionId );

  public List<Division> getDivisionsByPromotionId( Long promotionId );

  public List<Round> getRoundsByDivision( Long divisionId );

  public List<Round> getThrowdownRoundsForPromotionByRoundNumber( Long promotionId, int roundNumber );

  public List<Match> getThrowdownMatchesByPromotionAndRoundNumber( Long promotionId, int roundNumber, AssociationRequestCollection associationRequestCollection );

  /*
   * Match scheduling methods
   */
  public void scheduleFirstRound( Long promotionId, Date processStartTime );

  public void scheduleMatchesForRound( Long promotionId, int roundNumber, Date processStartTime );

  public Date getNextMatchSchedulerFiringTimeForPromotion( Long promotionId );

  public ThrowdownAudienceValidationResult getAudienceValidationResults( Set<Division> divisions );

  /*
   * Payout methods
   */
  public ThrowdownRoundCalculationResult generateHeadToHeadAwardSummaryForRound( Long promotionId, int roundNumber );

  public ThrowdownRoundCalculationResult getHeadToHeadAwardSummaryForRound( Long promotionId, int roundNumber );

  public Set<TeamCalculationResult> processMatchPayout( ThrowdownPromotion promotion, Match match ) throws ServiceErrorExceptionWithRollback;

  public StackRankingCalculationResult generateRankingAwardSummaryForRound( Long promotionId, int roundNumber );

  public StackRankingCalculationResult getRankingAwardSummaryForRound( Long promotionId, int roundNumber );

  public void processRankingPayout( TeamRankingResult teamResult, boolean isLastRound ) throws ServiceErrorExceptionWithRollback;

  public void sendEndOfRoundEmailToPaxManager( ThrowdownPromotion promotion, List<Round> rounds );

  public boolean generateAndMailExtractReport( ThrowdownPromotion promotion, int roundNumber, ThrowdownRoundCalculationResult divPayoutResults, StackRankingCalculationResult rankPayoutResults );

  public BigDecimal getCalculatedAverageForRound( ThrowdownPromotion promotion, Long roundId, Long teamId );

  public Set<PromotionRoundValue> getPromotionsForPayout();

  public MatchResolution resolveMatch( MatchTeamOutcome matchTeam1, MatchTeamOutcome matchTeam2 );

  public BigDecimal getShadowScore( Long roundId, Long teamId );

  /*
   * Progress import methods
   */
  public Set<PromotionRoundValue> getPromotionsForProgressLoad();

  public Set<PromotionRoundValue> getRoundsForProgressLoad( Long promotionId );

}

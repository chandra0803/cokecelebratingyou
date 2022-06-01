
package com.biperf.core.dao.throwdown;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import com.biperf.core.dao.DAO;
import com.biperf.core.domain.promotion.Round;

public interface RoundDAO extends DAO
{
  /** name of bean in factory * */
  public static final String BEAN_NAME = "roundDAO";

  public Round save( Round round );

  public Round getRoundById( Long roundId );

  public List<Round> getRoundsByDivision( Long divisionId );

  public List<Round> getRoundsForPromotionByRoundNumber( Long promotionId, int roundNumber );

  public Round getRoundsForPromotionByDivisionAndRoundNumber( Long promotionId, Long divisionId, int roundNumber );

  public BigDecimal getCalculatedAverageForRound( Long roundId, Long teamId, RoundingMode mode );

  public Round getCurrentRound( Long promotionId, Long divisionId );

  public boolean isRoundPaidForDivisionPayouts( Long promotionId, int roundNumber );

  public boolean isRoundCompleted( Long promotionId, int roundNumber );

  public boolean isRoundStarted( Long promotionId, int roundNumber );

}

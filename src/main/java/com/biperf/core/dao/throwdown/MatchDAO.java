
package com.biperf.core.dao.throwdown;

import java.util.List;

import com.biperf.core.dao.DAO;
import com.biperf.core.domain.promotion.Match;
import com.biperf.core.service.AssociationRequestCollection;

public interface MatchDAO extends DAO
{
  /** name of bean in factory * */
  public static final String BEAN_NAME = "matchDAO";

  public Match save( Match match );

  public Match getMatch( Long matchId );

  public Match getMatch( Long matchId, AssociationRequestCollection associationRequestCollection );

  public Match getMatchDetails( Long matchId );

  public List<Match> getUnplayedMatchesForPromotionAndRound( Long roundId );

  public List<Match> getMatchesByPromotionAndTeam( Long promotionId, Long teamId );

  public List<Match> getMatchesByRound( Long roundId );

  public List<Match> getMatchesByPromotionAndRoundNumber( Long promotionId, Integer roundNumber );

  public Match getMatchByPromotionAndRoundIdAndTeam( Long promotionId, Long roundId, Long teamId );

  public Match getMatchByPromotionAndRoundNumberAndTeam( Long promotionId, Integer roundNumber, Long teamId );

  public List<Match> getMatchesByPromotionAndRoundNumber( Long promotionId, Integer roundNumber, AssociationRequestCollection associationRequestCollection );
}

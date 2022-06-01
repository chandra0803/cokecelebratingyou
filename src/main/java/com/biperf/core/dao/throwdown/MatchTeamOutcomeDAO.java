
package com.biperf.core.dao.throwdown;

import com.biperf.core.dao.DAO;
import com.biperf.core.domain.promotion.MatchTeamOutcome;
import com.biperf.core.service.AssociationRequestCollection;

public interface MatchTeamOutcomeDAO extends DAO
{
  /** name of bean in factory * */
  public static final String BEAN_NAME = "matchTeamOutcomeDAO";

  public MatchTeamOutcome save( MatchTeamOutcome matchTeamOutcome );

  public MatchTeamOutcome getMatchTeamOutcome( Long id );

  public MatchTeamOutcome getOutcomeForMatch( Long teamId, Long promotionId, Integer roundNumber, AssociationRequestCollection associationRequestCollection );

  public MatchTeamOutcome getOutcomeForMatch( Long teamId, Long promotionId, Integer roundNumber );

  public MatchTeamOutcome getOutcomeForTeamInSpecificRound( Long teamId, Long roundId );

  public MatchTeamOutcome getOutcomeForTeamInSpecificRound( Long userId, int roundNumber, Long promotionId );

  public MatchTeamOutcome getOutcomeForTeamInSpecificRound( Long userId, int roundNumber, Long promotionId, AssociationRequestCollection associationRequestCollection );

}

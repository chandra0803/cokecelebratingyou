
package com.biperf.core.dao.throwdown;

import java.util.List;
import java.util.Set;

import com.biperf.core.dao.DAO;
import com.biperf.core.domain.enums.SortByType;
import com.biperf.core.domain.enums.SortOnType;
import com.biperf.core.domain.promotion.Match;
import com.biperf.core.domain.promotion.Team;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.throwdown.TeamMatching;
import com.biperf.core.service.throwdown.TeamProgress;

public interface TeamDAO extends DAO
{
  /** name of bean in factory * */
  public static final String BEAN_NAME = "teamDAO";

  public TeamStats getTeamStatsForPromotion( Long teamId, Long promotionId );

  public List<Standing> getStandingsForPromotion( Long promotionId, SortByType sortedByType, SortOnType sortOnType );

  public List<Standing> getStandingsForDivision( Long divisionId );

  public List<Standing> getMyStandings( Long teamId, SortByType sortedByType, SortOnType sortOnType );

  public Match getCurrentMatchForTeam( Long teamId );

  public List<Match> getTeamSchedule( Long promotionId, Long teamId );

  public List<Match> getTeamSchedule( Long promotionId, Long teamId, AssociationRequestCollection associationRequestCollection );

  public List<Long> getPaxPlayingPromotionInRound( Long promotionId, Integer roundNumber );

  public List<TeamProgress> getAllPaxsCumulativeProgressUptoRound( Long promotionId, Integer roundNumber );

  public Team getTeam( Long id );

  public Team getTeam( Long id, AssociationRequestCollection associationRequestCollection );

  public List<Team> findAllActiveTeamsAndPaxForPromotionAndDivision( Long promotionId, Long divisionId );

  public List<Team> findAllActiveTeamsForPromotionAndDivision( Long promotionId, Long divisionId );

  public List<Team> findAllActiveTeamsAndPaxForPromotionAndDivision( Long promotionId, Long divisionId, AssociationRequestCollection associationRequestCollection );

  public Team save( Team team );

  public List<TeamMatching> getTeamMatchingForTeamInDivision( Long divisionId, Long teamId );

  public Team getShadowPlayerForPromotionAndDivision( Long promotionId, Long divisionId );

  public Team getTeamByUserIdForPromotion( Long userId, Long promotionId );

  public List<Team> getTeamsByUserIdsForPromotion( Set<Long> userIds, Long promotionId );

  public Team getTeamByUserIdForPromotionWithAssociations( Long id, Long promotionId, AssociationRequestCollection associationRequestCollection );

  public Team getRandomTeamForPromotionAndDivision( Long promotionId, Long divisionId );

  public boolean isTeamUndefeatedTillNow( Long teamId, Long promotionId, Integer currentRound );

  public List<Team> findAllActiveTeamsAndPaxForPromotionAndParticipants( Long promotionId, List<Long> participantIds );

}

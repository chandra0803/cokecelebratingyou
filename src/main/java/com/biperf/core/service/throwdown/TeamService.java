/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/throwdown/TeamService.java,v $
 */

package com.biperf.core.service.throwdown;

import java.util.Date;
import java.util.List;
import java.util.Set;

import com.biperf.core.dao.throwdown.TeamStats;
import com.biperf.core.domain.enums.SortByType;
import com.biperf.core.domain.enums.SortOnType;
import com.biperf.core.domain.hierarchy.NodeType;
import com.biperf.core.domain.promotion.Division;
import com.biperf.core.domain.promotion.Match;
import com.biperf.core.domain.promotion.MatchTeamOutcome;
import com.biperf.core.domain.promotion.MatchTeamProgress;
import com.biperf.core.domain.promotion.Round;
import com.biperf.core.domain.promotion.StackStanding;
import com.biperf.core.domain.promotion.Team;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.SAO;
import com.biperf.core.utils.AdvancedListPageInfo;
import com.biperf.core.value.SmackTalkMainView;
import com.biperf.core.value.ThrowdownMatchBean;
import com.biperf.core.value.ThrowdownPlayerStatsBean;
import com.biperf.core.value.ThrowdownStackRankingView;
import com.biperf.core.value.ThrowdownStandingBean;

public interface TeamService extends SAO
{
  public static String BEAN_NAME = "teamService";

  /*
   * Domain save or update methods
   */
  public Round markRoundPaidout( Long roundId );

  public Team saveTeam( Team team );

  public Team createOrFindActiveShadowPlayerForPromotionAndDivision( Long promotionId, Long divisionId );

  public List<Team> saveTeamsForPromotion( Long promotionId );

  public Match saveMatch( Match match );

  public String createMatchesForPromotionRound( Long promotionId, int roundNumber );

  public MatchTeamOutcome saveMatchTeamOutcome( MatchTeamOutcome matchTeamOutcome );

  public MatchTeamProgress saveProgress( MatchTeamProgress matchTeamProgress );

  /*
   * Team finder methods
   */
  public Team getTeam( Long id );

  public Team getTeamByUserIdForPromotion( Long userId, Long promotionId );

  public Team getTeamByUserIdForPromotionWithAssociations( Long userId, Long promotionId, AssociationRequestCollection associationRequestCollection );

  public Team getRandomTeamForPromotionAndDivision( Long promotionId, Long divisionId );

  public List<Team> getTeamsByUserIdsForPromotion( Set<Long> userIds, Long promotionId );

  public List<Team> findAllActiveTeamsAndPaxForPromotionAndDivision( Long promotionId, Long divisionId );

  public List<Team> findAllActiveTeamsForPromotionAndDivision( Long promotionId, Long divisionId );

  public List<Team> findAllActiveTeamsAndPaxForPromotionAndParticipants( Long promotionId, List<Long> participantIds );

  public List<Team> findAllActiveTeamsAndPaxForPromotionAndDivision( Long promotionId, Long divisionId, AssociationRequestCollection associationRequestCollection );

  public Team getShadowPlayerForPromotionAndDivision( Long promotionId, Long divisionId );

  public List<TeamMatching> getTeamMatchingForTeamInDivision( Long divisionId, Long teamId );

  public List<Long> getPaxPlayingPromotionInRound( Long promotionId, Integer roundNumber );

  /*
   * Match and match outcome finder methods
   */
  public Match getMatchById( Long matchId );

  public Match getMatchByPromotionAndRoundNumberAndTeam( Long promotionId, Long userId, int roundNumber );

  public List<Match> getTeamSchedule( Long promotionId, Long teamId );

  public List<Match> getTeamSchedule( Long promotionId, Long teamId, AssociationRequestCollection associationRequestCollection );

  public List<Match> getUnplayedMatchesForPromotionAndRound( Long roundId );

  public MatchTeamOutcome getOutcomeForMatch( Long teamId, Long promotionId, Integer roundNumber );

  public MatchTeamOutcome getOutcomeForMatch( Long teamId, Long promotionId, Integer roundNumber, AssociationRequestCollection associationRequestCollection );

  public MatchTeamOutcome getOutcomeForTeamInSpecificRound( Long userId, int roundNumber, Long promotionId );

  public MatchTeamOutcome getOutcomeForTeamInSpecificRound( Long userId, int roundNumber, Long promotionId, AssociationRequestCollection arCollection );

  public List<MatchTeamProgress> getProgressListByOutcome( Long id );

  public List<MatchTeamProgress> getProgressListByUser( Long userId, Long promotionId, Integer roundNumber );

  /*
   * Round finder methods
   */
  public Round getRound( Long promotionId, Integer roundNumber );

  public Round getRound( Long promotionId, Long divisionId, Integer roundNumber );

  public List<Round> getRoundsForPromotionByRoundNumber( Long promotionId, int roundNumber );

  /*
   * Division finder methods
   */
  public Division getCurrentDivisionForUser( Long promotionId, Long userId );

  public Division getLastAssociatedDivisionForUser( Long promotionId, Long userId );

  public Division getDivisionForUser( Long promotionId, Long userId );

  public Division getDivisionForUser( Long promotionId, Long userId, int roundNumber );

  public Division getRandomDivisionForPromotion( Long promotionId );

  /*
   * Pick appropriate match and round
   */
  public Match getAppropriateMatch( Long promotionId );

  public Round getAppropriateRound( Long promotionId );

  /*
   * Throwdown player stats for participant profile in admin site
   */
  public ThrowdownPlayerStatsBean getPlayerStats( Long userId, Long promotionId );

  /*
   * Throwdown player stats for participant profile in client site
   */
  public ThrowdownPlayerStatsBean getPlayerStats( Long promotionId );

  /*
   * Match summary
   */
  public boolean showMatchSummary( Long promotionId, Long userId );

  public ThrowdownMatchBean getAppropriateMatchSummary( Long promotionId );

  public ThrowdownMatchBean getAppropriateMatchSummary( Long promotionId, int badgeSize );

  public ThrowdownMatchBean getMatchSummary( Long matchId );

  public ThrowdownMatchBean getMatchSummary( Long matchId, int badgeSize );

  /*
   * Match detail
   */
  public ThrowdownMatchBean getAppropriateMatchDetails( Long promotionId );

  public ThrowdownMatchBean getAppropriateMatchDetails( Long promotionId, int badgeSize );

  public ThrowdownMatchBean getMatchDetails( Long matchId );

  public ThrowdownMatchBean getMatchDetails( Long matchId, int badgeSize );

  /*
   * Match list
   */
  public void getMatchListForPromotion( Long promotionId, Integer roundNumber, AdvancedListPageInfo<ThrowdownMatchBean> pageInfo );

  public void getMatchListForDivision( Long promotionId, Long divisionId, Long roundId, AdvancedListPageInfo<ThrowdownMatchBean> pageInfo );

  public void getMatchListForMyTeam( Long promotionId, Integer roundNumber, AdvancedListPageInfo<ThrowdownMatchBean> pageInfo );

  public void getMyMatch( Long promotionId, Long userId, Integer roundNumber, AdvancedListPageInfo<ThrowdownMatchBean> pageInfo );

  /*
   * Ranking summary
   */
  public StackStanding getRankingForPromotion( Long promotionId );

  public ThrowdownStackRankingView getRankingSummary( Long promotionId, int pageSize, int pageNumber );

  public ThrowdownStackRankingView getRankingSummary( Long promotionId, Long nodeTypeId, Long nodeId, int pageSize, int pageNumber, boolean summary );

  /*
   * Ranking details
   */
  public List<NodeType> getNodeTypesForRanking( Long promotionId );

  public ThrowdownStackRankingView getRankingDetails( Long promotionId, int pageSize, int pageNumber );

  public ThrowdownStackRankingView getRankingDetails( Long promotionId, Long nodeTypeId, Long nodeId, int pageSize, int pageNumber );

  /*
   * Standing details
   */
  public TeamStats getTeamStatsForPromotion( Long teamId, Long promotionId );

  public ThrowdownStandingBean getStandingsForPromotion( Long promotionId, int pageSize, int pageNumber, SortByType sortedByType, SortOnType sortOnType );

  public ThrowdownStandingBean getStandingsForDivision( Long promotionId, Long divisionId );

  public ThrowdownStandingBean getMyStandings( Long promotionId, Long teamId, int pageSize, int pageNumber, SortByType sortedByType, SortOnType sortOnType );

  /*
   * SmackTalkDetails
   */
  public SmackTalkMainView getSmackTalkDetailsForPromotion( Long promotionId, Long divisionId, Long roundId, Integer roundNumber, Long publicUserId, int pageSize, int pageNumber );

  public SmackTalkMainView getSmackTalkDetailsForTeam( Long promotionId, Long divisionId, Long roundId, Integer roundNumber, Long publicUserId, int pageSize, int pageNumber );

  public SmackTalkMainView getSmackTalkDetailsForMyMatch( Long promotionId, Long divisionId, Long roundId, Integer roundNumber, Long publicUserId, int pageSize, int pageNumber );

  public SmackTalkMainView getSmackTalkDetailsForMyMatchAndMyPosts( Long promotionId, Long divisionId, Long roundId, Integer roundNumber, Long publicUserId, int pageSize, int pageNumber );

  public ThrowdownMatchBean getSmackTalkDetailsForMatch( ThrowdownMatchBean matchBean );

  public Date getLastFileLoadDateForPromotionAndRound( Long promotionId, Integer roundNumber );

  public Date getLastFileLoadDateForPromotion( Long promotionId );

}

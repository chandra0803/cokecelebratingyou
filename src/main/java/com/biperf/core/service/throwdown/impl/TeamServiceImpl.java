
package com.biperf.core.service.throwdown.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.biperf.core.dao.fileload.ImportFileDAO;
import com.biperf.core.dao.throwdown.DivisionDAO;
import com.biperf.core.dao.throwdown.MatchDAO;
import com.biperf.core.dao.throwdown.MatchTeamOutcomeDAO;
import com.biperf.core.dao.throwdown.MatchTeamProgressDAO;
import com.biperf.core.dao.throwdown.RoundDAO;
import com.biperf.core.dao.throwdown.SmackTalkDAO;
import com.biperf.core.dao.throwdown.Standing;
import com.biperf.core.dao.throwdown.TeamDAO;
import com.biperf.core.dao.throwdown.TeamStats;
import com.biperf.core.domain.enums.BadgeLevelType;
import com.biperf.core.domain.enums.HierarchyRoleType;
import com.biperf.core.domain.enums.MatchTeamOutcomeType;
import com.biperf.core.domain.enums.SmackTalkTabType;
import com.biperf.core.domain.enums.SortByType;
import com.biperf.core.domain.enums.SortOnType;
import com.biperf.core.domain.enums.ThrowdownMatchProgressType;
import com.biperf.core.domain.gamification.BadgeDetails;
import com.biperf.core.domain.gamification.BadgeInfo;
import com.biperf.core.domain.gamification.BadgeLibrary;
import com.biperf.core.domain.gamification.BadgeRule;
import com.biperf.core.domain.gamification.ParticipantBadge;
import com.biperf.core.domain.hierarchy.Hierarchy;
import com.biperf.core.domain.hierarchy.Node;
import com.biperf.core.domain.hierarchy.NodeType;
import com.biperf.core.domain.participant.Audience;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.Division;
import com.biperf.core.domain.promotion.DivisionCompetitorsAudience;
import com.biperf.core.domain.promotion.Match;
import com.biperf.core.domain.promotion.MatchTeamOutcome;
import com.biperf.core.domain.promotion.MatchTeamProgress;
import com.biperf.core.domain.promotion.Round;
import com.biperf.core.domain.promotion.SmackTalkComment;
import com.biperf.core.domain.promotion.SmackTalkSet;
import com.biperf.core.domain.promotion.SmackTalkSetCountBean;
import com.biperf.core.domain.promotion.StackStanding;
import com.biperf.core.domain.promotion.StackStandingNode;
import com.biperf.core.domain.promotion.StackStandingParticipant;
import com.biperf.core.domain.promotion.Team;
import com.biperf.core.domain.promotion.ThrowdownPromotion;
import com.biperf.core.domain.system.PropertySetItem;
import com.biperf.core.domain.user.User;
import com.biperf.core.domain.user.UserNode;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.ProjectionAttribute;
import com.biperf.core.service.ProjectionCollection;
import com.biperf.core.service.email.MailingService;
import com.biperf.core.service.gamification.GamificationService;
import com.biperf.core.service.hierarchy.HierarchyService;
import com.biperf.core.service.hierarchy.NodeService;
import com.biperf.core.service.participant.AudienceService;
import com.biperf.core.service.participant.ListBuilderService;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.service.throwdown.ParticipantAudienceConflictResult;
import com.biperf.core.service.throwdown.StackStandingNodeService;
import com.biperf.core.service.throwdown.StackStandingService;
import com.biperf.core.service.throwdown.TeamMatching;
import com.biperf.core.service.throwdown.TeamService;
import com.biperf.core.service.throwdown.ThrowdownAudienceValidationResult;
import com.biperf.core.service.throwdown.ThrowdownService;
import com.biperf.core.service.throwdown.scheduler.ThrowdownMatchSchedulerFactory;
import com.biperf.core.utils.AdvancedListPageInfo;
import com.biperf.core.utils.ClientStateUtils;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.PageConstants;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.BaseJsonView;
import com.biperf.core.value.FormattedValueBean;
import com.biperf.core.value.SmackTalkCommentViewBean;
import com.biperf.core.value.SmackTalkMainView;
import com.biperf.core.value.SmackTalkParticipantView;
import com.biperf.core.value.ThrowdownMatchBean;
import com.biperf.core.value.ThrowdownPlayerStatsBean;
import com.biperf.core.value.ThrowdownStackRanking;
import com.biperf.core.value.ThrowdownStackRankingParticipant;
import com.biperf.core.value.ThrowdownStackRankingSet;
import com.biperf.core.value.ThrowdownStackRankingView;
import com.biperf.core.value.ThrowdownStandingBean;
import com.biperf.core.value.ThrowdownStandingsListMetaView;
import com.biperf.core.value.ThrowdownTeamBean;
import com.objectpartners.cms.util.CmsResourceBundle;
import com.objectpartners.cms.util.ContentReaderManager;

public class TeamServiceImpl implements TeamService
{

  private PromotionService promotionService;
  private ThrowdownService throwdownService;
  private ParticipantService participantService;
  private HierarchyService hierarchyService;
  private ListBuilderService listBuilderService;
  private MailingService mailingService;
  private StackStandingService stackStandingService;
  private StackStandingNodeService stackStandingNodeService;
  private GamificationService gamificationService;
  private UserService userService;
  private SystemVariableService systemVariableService;
  private AudienceService audienceService;
  private ThrowdownMatchSchedulerFactory throwdownMatchSchedulerFactory;
  private TeamDAO teamDAO;
  private RoundDAO roundDAO;
  private MatchDAO matchDAO;
  private DivisionDAO divisionDAO;
  private MatchTeamProgressDAO matchTeamProgressDAO;
  private MatchTeamOutcomeDAO matchTeamOutcomeDAO;
  private SmackTalkDAO smackTalkDAO;
  private NodeService nodeService;
  private ImportFileDAO importFileDAO;

  @SuppressWarnings( "unchecked" )
  @Override
  public List<Team> saveTeamsForPromotion( Long promotionId )
  {
    ThrowdownPromotion promotion = getThrowdownPromotion( promotionId );
    Hierarchy primaryHierarchy = getHierarchyService().getPrimaryHierarchy();
    Long primaryHierarchyId = primaryHierarchy.getId();
    List<Team> allTeams = new ArrayList<Team>();

    for ( Division division : promotion.getDivisions() )
    {
      Set<Audience> audiencesForDivision = new HashSet<Audience>();
      for ( DivisionCompetitorsAudience divisionAudience : division.getCompetitorsAudience() )
      {
        audiencesForDivision.add( divisionAudience.getAudience() );
      }

      List<FormattedValueBean> paxesInAudience = (List<FormattedValueBean>)listBuilderService.searchParticipants( audiencesForDivision, primaryHierarchyId, true, null, false );

      for ( FormattedValueBean valueBean : paxesInAudience )
      {
        Team team = new Team();
        Participant participant = getParticipantService().getParticipantById( valueBean.getId() );
        team.setParticipant( participant );
        team.setDivision( division );
        team.setActive( true );
        team = teamDAO.save( team );
        allTeams.add( team );
      }
    }
    // generate the rounds
    throwdownService.buildRoundsForPromotion( promotion.getId() );

    // Now Schedule the first round's Matches for each division
    throwdownService.scheduleMatchesForRound( promotion.getId(), 1, Calendar.getInstance().getTime() );

    return allTeams;
  }

  @Override
  public List<Team> findAllActiveTeamsAndPaxForPromotionAndParticipants( Long promotionId, List<Long> participantIds )
  {
    return teamDAO.findAllActiveTeamsAndPaxForPromotionAndParticipants( promotionId, participantIds );
  }

  public String createMatchesForPromotionRound( Long promotionId, int roundNumber )
  {
    StringBuilder sb = new StringBuilder();
    ThrowdownPromotion promotion = getThrowdownPromotion( promotionId );
    List<Round> rounds = roundDAO.getRoundsForPromotionByRoundNumber( new Long( promotionId ), roundNumber );

    ThrowdownAudienceValidationResult audienceValidationResults = getThrowdownService().getAudienceValidationResults( promotion.getDivisions() );
    Set<ParticipantAudienceConflictResult> conflicts = audienceValidationResults.getConflictingAudienceMembers();

    for ( Round round : rounds )
    {
      List<Participant> potentialParticipantsForThisDivision = audienceValidationResults.getDivisionParticipantAudiences().get( round.getDivision() );
      Set<Match> matches = throwdownMatchSchedulerFactory.getThrowdownMatchSchedulerService().scheduleIncrementalHeadToHeadMatches( round, potentialParticipantsForThisDivision, conflicts );
      sb.append( "Scheduled " + matches.size() + " matches for Round " + round.getRoundNumber() + " in Division " + round.getDivision().getDivisionNameFromCM() + ". " );
    }

    // now schedule the next one if necessary
    Date nextProcessFiringTime = null;
    if ( roundNumber < promotion.getNumberOfRounds() )
    {
      // schedule the next one here!
      nextProcessFiringTime = throwdownService.getNextMatchSchedulerFiringTimeForPromotion( promotion.getId() );
      sb.append( "Scheduling next set of Matches for Round Number " + ( roundNumber + 1 ) + " on " + nextProcessFiringTime );
      throwdownService.scheduleMatchesForRound( promotion.getId(), roundNumber + 1, nextProcessFiringTime );
    }
    sendSummaryMessage( promotion, rounds, nextProcessFiringTime, roundNumber );
    sendDivisionConflictsAlertMessage( promotion, roundNumber, conflicts );
    return sb.toString();
  }

  @Override
  public TeamStats getTeamStatsForPromotion( Long teamId, Long promotionId )
  {
    return teamDAO.getTeamStatsForPromotion( teamId, promotionId );
  }

  private void getStandingForPage( Long promotionId, ThrowdownStandingBean standingBean, List<Standing> standings, int pageSize, int pageNumber )
  {
    pageNumber = pageNumber != 0 ? pageNumber : 1;
    standingBean.setCurrentPage( pageNumber );
    standingBean.setTotalStandings( standings.size() );
    int startIndex = pageSize * ( pageNumber - 1 );
    int endIndex = startIndex + pageSize;
    if ( endIndex > standings.size() )
    {
      endIndex = standings.size();
    }

    if ( startIndex >= 0 && endIndex <= standings.size() && startIndex <= endIndex )
    {
      List<Standing> rankersSubList = new ArrayList<Standing>( standings ).subList( startIndex, endIndex );
      standingBean.getTabularData().setResults( rankersSubList );
    }

    ThrowdownStandingsListMetaView meta = new ThrowdownStandingsListMetaView();
    Round round = getAppropriateRound( promotionId );
    standingBean.getTabularData().getMeta().setColumns( meta.getColumns() );
    standingBean.getTabularData().setRound( round );
  }

  public ThrowdownStandingBean getStandingsForPromotion( Long promotionId, int pageSize, int pageNumber, SortByType sortedByType, SortOnType sortOnType )
  {
    ThrowdownStandingBean standingBean = getStandingBean( promotionId );
    List<Standing> standings = teamDAO.getStandingsForPromotion( promotionId, sortedByType, sortOnType );
    getStandingForPage( promotionId, standingBean, standings, pageSize, pageNumber );
    return standingBean;
  }

  public ThrowdownStandingBean getStandingsForDivision( Long promotionId, Long divisionId )
  {
    ThrowdownStandingBean standingBean = getStandingBean( promotionId );
    if ( divisionId != null )
    {
      standingBean.setStandings( teamDAO.getStandingsForDivision( divisionId ) );
    }
    return standingBean;
  }

  public ThrowdownStandingBean getMyStandings( Long promotionId, Long teamId, int pageSize, int pageNumber, SortByType sortedByType, SortOnType sortedOnType )
  {
    ThrowdownStandingBean standingBean = getStandingBean( promotionId );
    if ( teamId != null )
    {
      List<Standing> standings = teamDAO.getMyStandings( teamId, sortedByType, sortedOnType );
      getStandingForPage( promotionId, standingBean, standings, pageSize, pageNumber );
    }
    return standingBean;
  }

  private ThrowdownStandingBean getStandingBean( Long promotionId )
  {
    ThrowdownStandingBean standingBean = new ThrowdownStandingBean();
    standingBean.setPromotion( getThrowdownPromotion( promotionId ) );
    return standingBean;
  }

  @Override
  public List<Match> getTeamSchedule( Long promotionId, Long teamId )
  {
    return getTeamSchedule( promotionId, teamId, null );
  }

  @Override
  public List<Match> getTeamSchedule( Long promotionId, Long teamId, AssociationRequestCollection associationRequestCollection )
  {
    List<Match> matches = teamDAO.getTeamSchedule( promotionId, teamId, associationRequestCollection );
    Collections.sort( matches, new Comparator<Match>()
    {
      public int compare( Match match1, Match match2 )
      {
        Integer round1 = new Integer( match1.getRound().getRoundNumber() );
        Integer round2 = new Integer( match2.getRound().getRoundNumber() );
        return round1.compareTo( round2 );
      }
    } );
    return matches;
  }

  @Override
  public List<TeamMatching> getTeamMatchingForTeamInDivision( Long divisionId, Long teamId )
  {
    return teamDAO.getTeamMatchingForTeamInDivision( divisionId, teamId );
  }

  @Override
  public Team getTeam( Long id )
  {
    return teamDAO.getTeam( id );
  }

  @Override
  public Team saveTeam( Team team )
  {
    return teamDAO.save( team );
  }

  @Override
  public Team createOrFindActiveShadowPlayerForPromotionAndDivision( Long promotionId, Long divisionId )
  {
    Team shadowPlayer = teamDAO.getShadowPlayerForPromotionAndDivision( promotionId, divisionId );
    // if null, create it
    if ( shadowPlayer == null )
    {
      shadowPlayer = new Team();
      shadowPlayer.setShadowPlayer( true );
      shadowPlayer.setActive( true );
      shadowPlayer.setDivision( divisionDAO.getDivision( divisionId ) );
      teamDAO.save( shadowPlayer );
    }
    else
    {
      shadowPlayer.setActive( true );
    }

    return shadowPlayer;
  }

  @Override
  public Team getShadowPlayerForPromotionAndDivision( Long promotionId, Long divisionId )
  {
    return teamDAO.getShadowPlayerForPromotionAndDivision( promotionId, divisionId );
  }

  @Override
  public List<Team> findAllActiveTeamsAndPaxForPromotionAndDivision( Long promotionId, Long divisionId )
  {
    return teamDAO.findAllActiveTeamsAndPaxForPromotionAndDivision( promotionId, divisionId );
  }

  @Override
  public List<Team> findAllActiveTeamsForPromotionAndDivision( Long promotionId, Long divisionId )
  {
    return teamDAO.findAllActiveTeamsForPromotionAndDivision( promotionId, divisionId );
  }

  @Override
  public List<Team> findAllActiveTeamsAndPaxForPromotionAndDivision( Long promotionId, Long divisionId, AssociationRequestCollection associationRequestCollection )
  {
    return teamDAO.findAllActiveTeamsAndPaxForPromotionAndDivision( promotionId, divisionId, associationRequestCollection );
  }

  private ThrowdownMatchBean getMatchBean( Long matchId )
  {
    ThrowdownMatchBean matchBean = new ThrowdownMatchBean();
    Match match = matchDAO.getMatchDetails( matchId );
    matchBean.setMatch( match );
    matchBean.setRound( match.getRound() );
    matchBean.setDivision( match.getRound().getDivision() );
    matchBean.setPromotion( getThrowdownPromotion( match.getRound().getDivision().getPromotion().getId() ) );
    return matchBean;
  }

  private ThrowdownMatchBean getMatchSummaryForMatchList( Long matchId )
  {
    ThrowdownMatchBean matchBean = getMatchBean( matchId );

    // build the basic team info
    Iterator<MatchTeamOutcome> teamIter = matchBean.getMatch().getTeamOutcomes().iterator();
    matchBean.setPrimaryTeam( buildTeamBeanWithStats( teamIter.next() ) );
    matchBean.setSecondaryTeam( buildTeamBeanWithStats( teamIter.next() ) );
    buildMatchDetailUrl( matchBean );

    return matchBean;
  }

  private void buildMatchDetailUrl( ThrowdownMatchBean matchBean )
  {
    String siteUrl = systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();
    Map<String, Object> parameterMap = new HashMap<String, Object>();
    parameterMap.put( "matchId", matchBean.getMatchId() );
    parameterMap.put( "roundNumber", new Long( matchBean.getRound().getRoundNumber() ) );
    parameterMap.put( "promotionId", matchBean.getPromotion().getId() );
    matchBean.setMatchUrl( ClientStateUtils.generateEncodedLink( siteUrl, PageConstants.THROWDOWN_MATCH_DETAIL, parameterMap ) );
  }

  private ThrowdownMatchBean getMatchSummaryForPlayerStats( Long matchId, Long userId )
  {
    ThrowdownMatchBean matchBean = getMatchBean( matchId );

    // build the basic team info
    Iterator<MatchTeamOutcome> teamIter = matchBean.getMatch().getTeamOutcomes().iterator();
    ThrowdownTeamBean bean1 = !matchBean.isRoundYetToStart() ? buildTeamBeanWithStats( teamIter.next() ) : buildTeamBean( teamIter.next() );
    ThrowdownTeamBean bean2 = !matchBean.isRoundYetToStart() ? buildTeamBeanWithStats( teamIter.next() ) : buildTeamBean( teamIter.next() );

    // arrange users
    arrangePrimaryAndSecondaryPosition( userId, matchBean, bean1, bean2 );

    return matchBean;
  }

  @Override
  public ThrowdownMatchBean getMatchSummary( Long matchId )
  {
    return getMatchSummary( matchId, 0 );
  }

  @Override
  public ThrowdownMatchBean getMatchSummary( Long matchId, int badgeSize )
  {
    ThrowdownMatchBean matchBean = getMatchBean( matchId );

    // build the basic team info
    Iterator<MatchTeamOutcome> teamIter = matchBean.getMatch().getTeamOutcomes().iterator();
    ThrowdownTeamBean bean1 = buildTeamBeanWithStats( teamIter.next() );
    ThrowdownTeamBean bean2 = buildTeamBeanWithStats( teamIter.next() );

    // arrange users
    arrangePrimaryAndSecondaryPosition( matchBean, bean1, bean2 );

    // add badges
    bean1.setBadges( getBadgesGrouped( matchBean.getPromotionId(), bean1.getTeam().getParticipant(), badgeSize ) );
    bean2.setBadges( getBadgesGrouped( matchBean.getPromotionId(), bean2.getTeam().getParticipant(), badgeSize ) );

    // build rankings
    buildRanking( bean1, matchBean );
    buildRanking( bean2, matchBean );

    return matchBean;
  }

  @Override
  public ThrowdownMatchBean getMatchDetails( Long matchId )
  {
    return getMatchDetails( matchId, 0 );
  }

  @Override
  public ThrowdownMatchBean getMatchDetails( Long matchId, int badgeSize )
  {
    // detailed version
    ThrowdownMatchBean matchBean = getMatchSummary( matchId, badgeSize );

    // add smack talks
    buildSmackTalks( matchBean );

    return matchBean;
  }

  @Override
  public ThrowdownMatchBean getAppropriateMatchSummary( Long promotionId )
  {
    return getAppropriateMatchSummary( promotionId, 0 );
  }

  @Override
  public ThrowdownMatchBean getAppropriateMatchSummary( Long promotionId, int badgeSize )
  {
    Match match = getAppropriateMatch( promotionId );
    return getMatchSummary( match.getId(), badgeSize );
  }

  @Override
  public ThrowdownMatchBean getAppropriateMatchDetails( Long promotionId )
  {
    return getAppropriateMatchDetails( promotionId, 0 );
  }

  @Override
  public ThrowdownMatchBean getAppropriateMatchDetails( Long promotionId, int badgeSize )
  {
    Match match = getAppropriateMatch( promotionId );
    return getMatchDetails( match.getId(), badgeSize );
  }

  @Override
  public Match getAppropriateMatch( Long promotionId )
  {
    Long userId = UserManager.getUserId();
    Round round = getAppropriateRound( promotionId );
    Division division = getDivisionForUser( promotionId, userId, round.getRoundNumber() );
    Team team = null;

    if ( null == division )
    {
      division = getRandomDivisionForPromotion( promotionId );
      team = getRandomTeamForPromotionAndDivision( promotionId, division.getId() );
    }
    else
    {
      team = getTeamByUserIdForPromotion( userId, promotionId );
    }

    round = getRound( promotionId, division.getId(), round.getRoundNumber() );
    return matchDAO.getMatchByPromotionAndRoundIdAndTeam( promotionId, round.getId(), team.getId() );
  }

  public Round getAppropriateRound( Long promotionId )
  {
    ThrowdownPromotion promotion = getThrowdownPromotion( promotionId );
    Long divisionId = promotion.getDivisions().iterator().next().getId();

    // try if there is current active round available
    Round round = roundDAO.getCurrentRound( promotionId, divisionId );
    if ( round != null )
    {
      return round;
    }

    // may be all rounds completed, try fetching last one
    int lastRoundNumber = promotion.getNumberOfRounds();

    round = roundDAO.getRoundsForPromotionByDivisionAndRoundNumber( promotionId, divisionId, lastRoundNumber );
    if ( round != null && !round.getMatches().isEmpty() )
    {
      // last round have matches created.
      return round;
    }

    // may be first round not started, try fetching first round
    return roundDAO.getRoundsForPromotionByDivisionAndRoundNumber( promotionId, divisionId, 1 );
  }

  public Round getRound( Long promotionId, Integer roundNumber )
  {
    ThrowdownPromotion promotion = getThrowdownPromotion( promotionId );
    Long divisionId = promotion.getDivisions().iterator().next().getId();
    return getRound( promotionId, divisionId, roundNumber );
  }

  public Round getRound( Long promotionId, Long divisionId, Integer roundNumber )
  {
    return roundDAO.getRoundsForPromotionByDivisionAndRoundNumber( promotionId, divisionId, roundNumber );
  }

  @Override
  public Team getRandomTeamForPromotionAndDivision( Long promotionId, Long divisionId )
  {
    return teamDAO.getRandomTeamForPromotionAndDivision( promotionId, divisionId );
  }

  public Division getRandomDivisionForPromotion( Long promotionId )
  {
    List<Division> divisions = throwdownService.getDivisionsByPromotionId( promotionId );
    Collections.shuffle( divisions );
    return divisions.get( 0 );
  }

  private void getMatchesForPage( List<Match> matches, AdvancedListPageInfo<ThrowdownMatchBean> pageInfo )
  {
    List<ThrowdownMatchBean> matchBeans = new ArrayList<ThrowdownMatchBean>();
    // get positioned
    int pageSize = pageInfo.getObjectsPerPage();
    int startIndex = 0;
    int endIndex = 0;
    if ( pageSize == AdvancedListPageInfo.FULL_LIST_PAGE_SIZE )
    {
      endIndex = matches.size();
    }
    else
    {
      startIndex = pageInfo.getObjectsPerPage() * ( pageInfo.getPageNumber() - 1 );
      endIndex = startIndex + pageInfo.getObjectsPerPage();
      if ( endIndex > matches.size() )
      {
        endIndex = matches.size();
      }
    }

    List<Match> matchSubList = new ArrayList<Match>( matches ).subList( startIndex, endIndex );
    for ( Match match : matchSubList )
    {
      matchBeans.add( getMatchSummaryForMatchList( match.getId() ) );
    }

    // set on page info
    pageInfo.setList( matchBeans );

    // set full list size
    pageInfo.setFullListSize( matches.size() );
  }

  private void getTeamMatchesForPage( List<Team> teams, Long promotionId, Integer roundNumber, AdvancedListPageInfo<ThrowdownMatchBean> pageInfo )
  {
    List<ThrowdownMatchBean> matchBeans = new ArrayList<ThrowdownMatchBean>();
    // get positioned
    int pageSize = pageInfo.getObjectsPerPage();
    int startIndex = 0;
    int endIndex = 0;
    if ( pageSize == AdvancedListPageInfo.FULL_LIST_PAGE_SIZE )
    {
      endIndex = teams.size();
    }
    else
    {
      startIndex = pageInfo.getObjectsPerPage() * ( pageInfo.getPageNumber() - 1 );
      endIndex = startIndex + pageInfo.getObjectsPerPage();
      if ( endIndex > teams.size() )
      {
        endIndex = teams.size();
      }
    }

    List<Team> teamSubList = new ArrayList<Team>( teams ).subList( startIndex, endIndex );
    for ( Team team : teamSubList )
    {
      Match match = matchDAO.getMatchByPromotionAndRoundNumberAndTeam( promotionId, roundNumber, team.getId() );
      if ( match != null )
      {
        matchBeans.add( getMatchSummaryForMatchList( match.getId() ) );
      }
      else
      {
        // matches not created for round yet.
        break;
      }
    }

    // set on page info
    pageInfo.setList( matchBeans );

    // set full list size
    pageInfo.setFullListSize( matchBeans.size() );
  }

  @Override
  public void getMatchListForPromotion( Long promotionId, Integer roundNumber, AdvancedListPageInfo<ThrowdownMatchBean> pageInfo )
  {
    roundNumber = roundNumber == null ? getAppropriateRound( promotionId ).getRoundNumber() : roundNumber;
    getMatchesForPage( matchDAO.getMatchesByPromotionAndRoundNumber( promotionId, roundNumber ), pageInfo );
  }

  @Override
  public void getMatchListForDivision( Long promotionId, Long divisionId, Long roundId, AdvancedListPageInfo<ThrowdownMatchBean> pageInfo )
  {
    if ( divisionId == null )
    {
      pageInfo.setList( new ArrayList<ThrowdownMatchBean>() );
      pageInfo.setFullListSize( 0 );
      return;
    }
    getMatchesForPage( matchDAO.getMatchesByRound( roundId ), pageInfo );
  }

  @SuppressWarnings( "rawtypes" )
  @Override
  public void getMatchListForMyTeam( Long promotionId, Integer roundNumber, AdvancedListPageInfo<ThrowdownMatchBean> pageInfo )
  {
    Long userId = UserManager.getUserId();
    Set<UserNode> userNodes = userService.getUserNodes( userId );
    Set<UserNode> filteredUserNodes = new HashSet<UserNode>();
    for ( UserNode userNode : userNodes )
    {
      if ( userNode.getHierarchyRoleType().equals( HierarchyRoleType.lookup( HierarchyRoleType.MANAGER ) )
          || userNode.getHierarchyRoleType().equals( HierarchyRoleType.lookup( HierarchyRoleType.OWNER ) ) )
      {
        filteredUserNodes.add( userNode );
      }
    }

    Set<Long> userIds = new HashSet<Long>();
    for ( UserNode filteredUserNode : filteredUserNodes )
    {
      List users = userService.getAllUsersOnNode( filteredUserNode.getNode().getId() );
      for ( Iterator userIter = users.iterator(); userIter.hasNext(); )
      {
        User user = (User)userIter.next();
        userIds.add( user.getId() );
      }
    }

    roundNumber = roundNumber == null ? getAppropriateRound( promotionId ).getRoundNumber() : roundNumber;
    List<Team> teams = teamDAO.getTeamsByUserIdsForPromotion( userIds, promotionId );
    getTeamMatchesForPage( teams, promotionId, roundNumber, pageInfo );
  }

  public void getMyMatch( Long promotionId, Long userId, Integer roundNumber, AdvancedListPageInfo<ThrowdownMatchBean> pageInfo )
  {
    Team team = getTeamByUserIdForPromotion( userId, promotionId );
    roundNumber = roundNumber == null ? getAppropriateRound( promotionId ).getRoundNumber() : roundNumber;
    List<Match> allMatches = null;
    if ( team == null )
    {
      pageInfo.setList( new ArrayList<ThrowdownMatchBean>() );
      allMatches = matchDAO.getMatchesByPromotionAndRoundNumber( promotionId, roundNumber );
      pageInfo.setFullListSize( allMatches.size() );
      return;
    }

    Match match = matchDAO.getMatchByPromotionAndRoundNumberAndTeam( promotionId, roundNumber, team.getId() );
    if ( match == null )
    {
      pageInfo.setList( new ArrayList<ThrowdownMatchBean>() );
      allMatches = matchDAO.getMatchesByPromotionAndRoundNumber( promotionId, roundNumber );
      pageInfo.setFullListSize( allMatches.size() );
      return;
    }
    List<Match> matches = new ArrayList<Match>();
    matches.add( match );
    getMatchesForPage( matches, pageInfo );
  }

  private List<SmackTalkCommentViewBean> getSmackTalksForPage( List<SmackTalkComment> smackTalkComments, int pageSize, int pageNumber )
  {
    Long userId = UserManager.getUserId();
    List<SmackTalkCommentViewBean> commentBeans = new ArrayList<SmackTalkCommentViewBean>();
    // get positioned
    int startIndex = pageSize * ( pageNumber - 1 );
    int endIndex = startIndex + pageSize;
    if ( endIndex > smackTalkComments.size() )
    {
      endIndex = smackTalkComments.size();
    }

    if ( startIndex >= 0 && endIndex <= smackTalkComments.size() && startIndex <= endIndex )
    {
      List<SmackTalkComment> smackTalkCommentSubList = new ArrayList<SmackTalkComment>( smackTalkComments ).subList( startIndex, endIndex );
      for ( SmackTalkComment smackTalkComment : smackTalkCommentSubList )
      {
        SmackTalkCommentViewBean commentBean = new SmackTalkCommentViewBean();
        List<SmackTalkComment> comments = smackTalkDAO.getUserCommentsBySmackTalkPost( smackTalkComment.getId() );
        for ( SmackTalkComment comment : comments )
        {
          comment.setNumLikers( smackTalkDAO.getLikeCountBySmackTalk( comment.getId() ) );
          comment.setLiked( smackTalkDAO.isCurrentUserLikedSmackTalk( comment.getId(), userId ) );
          if ( userId.equals( comment.getUser().getId() ) )
          {
            comment.setMine( true );
          }
          commentBean.getCommentsPerPost().add( new SmackTalkCommentViewBean( comment ) );
        }
        commentBean.setId( smackTalkComment.getId() );
        commentBean.setCommenter( new SmackTalkParticipantView( smackTalkComment.getUser().getId(),
                                                                smackTalkComment.getUser().getFirstName(),
                                                                smackTalkComment.getUser().getLastName(),
                                                                smackTalkComment.getUser().getAvatarSmallFullPath(),
                                                                smackTalkComment.getUser().getPositionType(),
                                                                null,
                                                                null ) );
        commentBean.setComment( smackTalkComment.getComments() );

        Long postLikeCount = smackTalkDAO.getLikeCountBySmackTalk( smackTalkComment.getId() );
        commentBean.setNumLikers( postLikeCount );
        commentBean.setLiked( smackTalkDAO.isCurrentUserLikedSmackTalk( smackTalkComment.getId(), userId ) );
        commentBean.setHidden( smackTalkComment.getIsHidden() );
        commentBean.setMatchBean( getMatchSummaryForMatchList( smackTalkComment.getMatch().getId() ) );
        commentBean.setAuditCreateInfo( smackTalkComment.getAuditCreateInfo() );
        if ( !commentBean.getMatchBean().getPrimaryTeam().getTeam().isShadowPlayer() && commentBean.getMatchBean().getPrimaryTeam().getTeam().getParticipant().getId().equals( userId )
            || !commentBean.getMatchBean().getSecondaryTeam().getTeam().isShadowPlayer() && commentBean.getMatchBean().getSecondaryTeam().getTeam().getParticipant().getId().equals( userId ) )
        {
          commentBean.getMatchBean().setMine( true );
        }
        else
        {
          commentBean.getMatchBean().setMine( false );
        }
        commentBeans.add( commentBean );
      }
    }
    return commentBeans;
  }

  // code added for smack talk tile

  @SuppressWarnings( "rawtypes" )
  private Set<Long> getTeamIds( Long promotionId )
  {
    Long userId = UserManager.getUserId();
    Set<UserNode> userNodes = userService.getUserNodes( userId );

    Set<Long> userIds = new HashSet<Long>();
    for ( UserNode userNode : userNodes )
    {
      List users = userService.getAllUsersOnNode( userNode.getNode().getId() );
      for ( Iterator userIter = users.iterator(); userIter.hasNext(); )
      {
        User user = (User)userIter.next();
        userIds.add( user.getId() );
      }
    }
    List<Team> teams = teamDAO.getTeamsByUserIdsForPromotion( userIds, promotionId );

    Set<Long> teamIds = new HashSet<Long>();

    for ( Iterator<Team> teamIter = teams.iterator(); teamIter.hasNext(); )
    {
      Team team = teamIter.next();
      teamIds.add( team.getId() );
    }

    return teamIds;
  }

  private List<SmackTalkComment> getAllSmackTalkDetailsForPromotion( Long promotionId, Integer roundNumber )
  {
    return smackTalkDAO.getSmackTalkByPromotionAndRoundNumber( promotionId, roundNumber );
  }

  private List<SmackTalkComment> getAllSmackTalkDetailsForTeam( Long promotionId, Integer roundNumber )
  {
    Set<Long> teamIds = getTeamIds( promotionId );
    if ( teamIds.size() == 0 )
    {
      return new ArrayList<SmackTalkComment>();
    }
    return smackTalkDAO.getSmackTalkByTeam( teamIds, roundNumber );
  }

  private List<SmackTalkComment> getAllSmackTalkDetailsForMyMatch( Long promotionId, Long divisionId, Long roundId, Long publicUserId )
  {
    Long userId = publicUserId == null ? UserManager.getUserId() : publicUserId;
    if ( divisionId == null )
    {
      return new ArrayList<SmackTalkComment>();
    }
    Team team = getTeamByUserIdForPromotion( userId, promotionId );
    if ( team == null )
    {
      return new ArrayList<SmackTalkComment>();
    }
    return smackTalkDAO.getSmackTalkByPromotionAndRoundIdAndTeam( promotionId, roundId, team.getId() );
  }

  private SmackTalkMainView getSmackTalkViewForTab( SmackTalkTabType tabType,
                                                    List<SmackTalkComment> smackTalkComments,
                                                    Long promotionId,
                                                    Long divisionId,
                                                    Long roundId,
                                                    Integer roundNumber,
                                                    Long publicUserId,
                                                    int pageSize,
                                                    int pageNumber )
  {
    if ( smackTalkComments == null )
    {
      SmackTalkMainView view = new SmackTalkMainView();
      return view;
    }

    long fullSize = smackTalkComments.size();
    List<SmackTalkCommentViewBean> commentBeans = getSmackTalksForPage( smackTalkComments, pageSize, pageNumber );
    for ( SmackTalkCommentViewBean commentBean : commentBeans )
    {
      buildMatchDetailUrl( commentBean.getMatchBean() );
    }

    SmackTalkSetCountBean countBean = new SmackTalkSetCountBean();
    countBean.setCount( tabType, fullSize );

    SmackTalkSet smackTalkSetvalue = createSmackTalkSet( tabType, countBean.getCount( tabType ) );
    smackTalkSetvalue.setCommentBeans( commentBeans );

    if ( getDefaultTab().equals( tabType.getCode() ) )
    {
      smackTalkSetvalue.setIsDefault( true );
    }

    List<SmackTalkSet> smackTalkSetvalueList = new ArrayList<SmackTalkSet>();
    if ( Arrays.asList( getActiveTabList() ).contains( tabType.getCode() ) )
    {
      smackTalkSetvalueList.add( smackTalkSetvalue );
    }

    if ( !tabType.equals( SmackTalkTabType.lookup( SmackTalkTabType.GLOBAL_TAB ) ) )
    {
      List<SmackTalkComment> allGlobal = getAllSmackTalkDetailsForPromotion( promotionId, roundNumber );
      if ( allGlobal != null )
      {
        countBean.setGlobalTabCount( allGlobal.size() );
      }
      addGlobal( smackTalkSetvalueList, countBean.getGlobalTabCount() );
    }

    if ( !tabType.equals( SmackTalkTabType.lookup( SmackTalkTabType.TEAM_TAB ) ) )
    {
      List<SmackTalkComment> allTeams = getAllSmackTalkDetailsForTeam( promotionId, roundNumber );
      if ( allTeams != null )
      {
        countBean.setTeamTabCount( allTeams.size() );
      }
      addTeam( smackTalkSetvalueList, countBean.getTeamTabCount() );
    }

    if ( !tabType.equals( SmackTalkTabType.lookup( SmackTalkTabType.ME_TAB ) ) )
    {
      List<SmackTalkComment> allMine = getAllSmackTalkDetailsForMyMatch( promotionId, divisionId, roundId, publicUserId );
      if ( allMine != null )
      {
        countBean.setMineTabCount( allMine.size() );
      }
      addMe( smackTalkSetvalueList, countBean.getMineTabCount() );
    }

    SmackTalkMainView view = new SmackTalkMainView( smackTalkSetvalueList );
    return view;
  }

  @Override
  public SmackTalkMainView getSmackTalkDetailsForPromotion( Long promotionId, Long divisionId, Long roundId, Integer roundNumber, Long publicUserId, int pageSize, int pageNumber )
  {
    List<SmackTalkComment> smackTalkComments = smackTalkDAO.getSmackTalkByPromotionAndRoundNumber( promotionId, roundNumber );
    return getSmackTalkViewForTab( SmackTalkTabType.lookup( SmackTalkTabType.GLOBAL_TAB ), smackTalkComments, promotionId, divisionId, roundId, roundNumber, publicUserId, pageSize, pageNumber );
  }

  @Override
  public SmackTalkMainView getSmackTalkDetailsForTeam( Long promotionId, Long divisionId, Long roundId, Integer roundNumber, Long publicUserId, int pageSize, int pageNumber )
  {
    Set<Long> teamIds = getTeamIds( promotionId );
    if ( teamIds.size() == 0 )
    {
      SmackTalkMainView view = new SmackTalkMainView();
      return view;
    }
    List<SmackTalkComment> smackTalkComments = smackTalkDAO.getSmackTalkByTeam( teamIds, roundNumber );
    return getSmackTalkViewForTab( SmackTalkTabType.lookup( SmackTalkTabType.TEAM_TAB ), smackTalkComments, promotionId, divisionId, roundId, roundNumber, publicUserId, pageSize, pageNumber );
  }

  @Override
  public SmackTalkMainView getSmackTalkDetailsForMyMatch( Long promotionId, Long divisionId, Long roundId, Integer roundNumber, Long publicUserId, int pageSize, int pageNumber )
  {
    Long userId = publicUserId == null ? UserManager.getUserId() : publicUserId;
    if ( divisionId == null )
    {
      SmackTalkMainView view = new SmackTalkMainView();
      return view;
    }
    Team team = getTeamByUserIdForPromotion( userId, promotionId );
    if ( team == null )
    {
      SmackTalkMainView view = new SmackTalkMainView();
      return view;
    }
    List<SmackTalkComment> smackTalkComments = smackTalkDAO.getSmackTalkByPromotionAndRoundIdAndTeam( promotionId, roundId, team.getId() );
    return getSmackTalkViewForTab( SmackTalkTabType.lookup( SmackTalkTabType.ME_TAB ), smackTalkComments, promotionId, divisionId, roundId, roundNumber, publicUserId, pageSize, pageNumber );
  }

  @Override
  public SmackTalkMainView getSmackTalkDetailsForMyMatchAndMyPosts( Long promotionId, Long divisionId, Long roundId, Integer roundNumber, Long publicUserId, int pageSize, int pageNumber )
  {
    Long userId = publicUserId == null ? UserManager.getUserId() : publicUserId;
    List<SmackTalkComment> smackTalkComments = smackTalkDAO.getSmackTalkByPromotionAndRoundIdAndUser( promotionId, roundId, userId );
    return getSmackTalkViewForTab( SmackTalkTabType.lookup( SmackTalkTabType.ME_TAB ), smackTalkComments, promotionId, divisionId, roundId, roundNumber, publicUserId, pageSize, pageNumber );
  }

  @Override
  public ThrowdownMatchBean getSmackTalkDetailsForMatch( ThrowdownMatchBean matchBean )
  {
    buildSmackTalks( matchBean );
    return matchBean;
  }

  private void addMe( List<SmackTalkSet> smackTalkSetValueList, long count )
  {
    SmackTalkTabType tab = SmackTalkTabType.lookup( SmackTalkTabType.ME_TAB );
    if ( Arrays.asList( getActiveTabList() ).contains( SmackTalkTabType.ME_TAB ) )
    {
      smackTalkSetValueList.add( createSmackTalkSet( tab, count ) );
    }
  }

  private void addTeam( List<SmackTalkSet> smackTalkSetValueList, long count )
  {
    SmackTalkTabType tab = SmackTalkTabType.lookup( SmackTalkTabType.TEAM_TAB );
    if ( Arrays.asList( getActiveTabList() ).contains( SmackTalkTabType.TEAM_TAB ) )
    {
      smackTalkSetValueList.add( createSmackTalkSet( tab, count ) );
    }
  }

  private void addGlobal( List<SmackTalkSet> smackTalkSetValueList, long count )
  {
    SmackTalkTabType tab = SmackTalkTabType.lookup( SmackTalkTabType.GLOBAL_TAB );
    if ( Arrays.asList( getActiveTabList() ).contains( SmackTalkTabType.GLOBAL_TAB ) )
    {
      smackTalkSetValueList.add( createSmackTalkSet( tab, count ) );
    }
  }

  private SmackTalkSet createSmackTalkSet( SmackTalkTabType tab, long count )
  {
    SmackTalkSet smackTalkSetvalue = new SmackTalkSet( tab.getCode(), tab.getName(), tab.getDescription(), count );
    return smackTalkSetvalue;
  }

  /**
   * To get Active Tab List
   * @param 
   * @return String[]
   * @throws 
   */
  public String[] getActiveTabList()
  {

    PropertySetItem item = systemVariableService.getPropertyByName( SystemVariableService.SMACK_TALK_ACTIVE_TABS );

    if ( item != null )
    {
      return item.getStringVal().split( "," );
    }

    return new String[0];
  }

  /**
   * To get Default Tab
   * @param 
   * @return String
   * @throws 
   */
  public String getDefaultTab()
  {
    String tabType = systemVariableService.getPropertyByName( SystemVariableService.SMACK_TALK_DEFAULT_TAB_NAME ).getStringVal();
    return tabType;
  }

  /**
   * Get Comments 
   * @param list of matchIds
   * @return List<SmackTalkComment>
   */
  public List<SmackTalkComment> getSmackTalkPostsForMatches( Long[] matchIds )
  {
    return smackTalkDAO.getSmackTalkPostsForMatch( matchIds );
  }

  private static Comparator<SmackTalkComment> SORT_COMPARATOR = new Comparator<SmackTalkComment>()
  {
    public int compare( SmackTalkComment prc1, SmackTalkComment prc2 )
    {
      return prc2.getAuditCreateInfo().getDateCreated().compareTo( prc1.getAuditCreateInfo().getDateCreated() );
    }
  };

  // code ending for smack talk tile

  @Override
  public ThrowdownStackRankingView getRankingSummary( Long promotionId, int pageSize, int pageNumber )
  {
    return getRankingSummary( promotionId, null, null, pageSize, pageNumber, true );
  }

  public List<NodeType> getNodeTypesForRanking( Long promotionId )
  {
    StackStanding ranking = getRankingForPromotion( promotionId );
    Set<NodeType> nodeTypes = new HashSet<NodeType>();
    List<NodeType> nodeTypesList = new ArrayList<NodeType>();

    // All Node Type
    nodeTypes.add( getHierarchyPlaceHolderNodeType() );

    if ( ranking != null )
    {
      for ( StackStandingNode rankingNode : ranking.getStackStandingNodes() )
      {
        if ( !rankingNode.isHierarchyRanking() )
        {
          nodeTypes.add( rankingNode.getNode().getNodeType() );
        }
      }
    }

    nodeTypesList.addAll( nodeTypes );
    // sort node types alphabetically
    Collections.sort( nodeTypesList, new Comparator<NodeType>()
    {
      public int compare( NodeType set1, NodeType set2 )
      {
        return set1.getNodeTypeName().compareTo( set2.getNodeTypeName() );
      }
    } );

    return nodeTypesList;
  }

  private NodeType getHierarchyPlaceHolderNodeType()
  {
    NodeType all = new NodeType();
    all.setId( -1L );
    all.setCmAssetCode( "system.general" );
    all.setNameCmKey( "ALL" );
    return all;
  }

  @Override
  public ThrowdownStackRankingView getRankingSummary( Long promotionId, Long nodeTypeId, Long nodeId, int pageSize, int pageNumber, boolean summary )
  {
    // appropriate rankings
    StackStanding standings = getRankingForPromotion( promotionId );

    if ( standings == null )
    {
      // not created yet, return bean with visibility false.
      return ThrowdownStackRankingView.getViewForNoRankings();
    }
    Round round = getRound( promotionId, standings.getRoundNumber() );

    ThrowdownStackRankingView rankingView = new ThrowdownStackRankingView();
    List<ThrowdownStackRankingSet> nodeTypeSets = new ArrayList<ThrowdownStackRankingSet>();
    List<ThrowdownStackRankingSet> otherNodeTypeSets = new ArrayList<ThrowdownStackRankingSet>();
    ThrowdownStackRanking rankings = new ThrowdownStackRanking();
    StackStandingNode standingsNode = null;

    nodeTypeId = nodeTypeId != null && nodeTypeId == -1 ? null : nodeTypeId;
    nodeId = nodeId != null && nodeId == -1 ? null : nodeId;
    if ( nodeId != null && nodeTypeId != null )
    {
      standingsNode = stackStandingNodeService.getStackStandingByNode( standings.getId(), nodeId );
      if ( standingsNode != null && !standingsNode.isHierarchyRanking() && !standingsNode.getNode().getNodeType().getId().equals( nodeTypeId ) )
      {
        // node type changed in filter, reset node id
        nodeId = null;
      }
    }

    if ( nodeTypeId == null )
    {
      ThrowdownStackRankingSet rankingsSet = new ThrowdownStackRankingSet();
      rankingsSet.setNameId( "-1" );
      rankingsSet.setName( ContentReaderManager.getText( "system.general", "ALL" ) );
      rankings = buildThrowdownStackRanking( round, null, standings, pageSize, pageNumber, summary );
      rankingsSet.setRankings( rankings );
      nodeTypeSets.add( rankingsSet );
    }
    else
    {
      boolean rankingFetched = false;
      Set<StackStandingNode> standingsNodes = standings.getStackStandingNodes();
      List<StackStandingNode> standingsNodesList = new ArrayList<StackStandingNode>();
      standingsNodesList.addAll( standingsNodes );
      // sort nodes alphabetically
      Collections.sort( standingsNodesList, new Comparator<StackStandingNode>()
      {
        public int compare( StackStandingNode set1, StackStandingNode set2 )
        {
          return set1.getNodeName().compareTo( set2.getNodeName() );
        }
      } );

      for ( StackStandingNode stackStandingNode : standingsNodesList )
      {
        Node node = stackStandingNode.getNode();
        if ( !stackStandingNode.isHierarchyRanking() && node.getNodeType().getId().equals( nodeTypeId ) )
        {
          ThrowdownStackRankingSet rankingsSet = new ThrowdownStackRankingSet();
          rankingsSet.setNameId( String.valueOf( node.getId() ) );
          rankingsSet.setName( node.getName() );
          if ( ( nodeId == null || node.getId().equals( nodeId ) ) && !rankingFetched )
          {
            rankings = buildThrowdownStackRanking( round, node, standings, pageSize, pageNumber, summary );
            rankingsSet.setRankings( rankings );
            rankingFetched = true;
            nodeTypeSets.add( rankingsSet );
          }
          else
          {
            rankingsSet.setRankings( new BaseJsonView() );
            otherNodeTypeSets.add( rankingsSet );
          }
        }
      }
    }

    nodeTypeSets.addAll( otherNodeTypeSets );
    rankingView.setNodeTypeSets( nodeTypeSets );
    return rankingView;
  }

  private ThrowdownStackRanking buildThrowdownStackRanking( Round round, Node node, StackStanding standings, int pageSize, int pageNumber, boolean summary )
  {
    Long userId = UserManager.getUserId();
    ThrowdownStackRanking ranking = new ThrowdownStackRanking();
    StackStandingNode standingsNode = node != null
        ? stackStandingNodeService.getStackStandingByNode( standings.getId(), node.getId() )
        : stackStandingNodeService.getStackStandingByHierarchy( standings.getId() );
    ranking.setNode( node );
    ThrowdownPromotion promotion = getThrowdownPromotion( standings.getPromotion().getId() );
    ranking.setPromotion( promotion );
    if ( standingsNode != null )
    {
      ranking.setNode( node );
      ranking.setStandingNode( standingsNode );
      Date progressEndDate = getLastFileLoadDateForPromotion( standings.getPromotion().getId() );
      ranking.setProgressEndDate( progressEndDate );
      if ( progressEndDate != null )
      {
        ranking.setProgressLoaded( true );
      }
      else
      {
        ranking.setProgressLoaded( false );
      }
      ranking.setPayoutIssued( standings.isPayoutsIssued() );

      boolean fetchTitleUser = pageNumber == 0 ? true : false;
      StackStandingParticipant loggedInPaxRank = null;
      if ( pageNumber == 0 )
      {
        int totalRankers = stackStandingService.getTotalRankingParticipants( standingsNode.getId() );
        ranking.setTotalNumberLeaders( totalRankers );
        if ( !summary )
        {
          // get page where logged in user available
          int paxPosition = stackStandingService.getPaxPositionInRanking( standingsNode.getId(), userId );
          if ( paxPosition != 0 )
          {
            pageNumber = paxPosition % pageSize == 0 ? paxPosition / pageSize : paxPosition / pageSize + 1;
          }
        }
      }
      pageNumber = pageNumber != 0 ? pageNumber : 1;
      ranking.setCurrentPage( pageNumber );

      int fromIndex = pageSize * pageNumber - pageSize + 1;
      int endIndex = pageSize * pageNumber;
      List<StackStandingParticipant> users = stackStandingService.getPageRankingParticipants( standingsNode.getId(), fromIndex, endIndex );
      Set<Long> participantIds = new HashSet<Long>();
      for ( StackStandingParticipant user : users )
      {
        participantIds.add( user.getParticipant().getId() );
      }

      if ( fetchTitleUser )
      {
        loggedInPaxRank = stackStandingService.getStackStandingParticipant( standingsNode.getId(), userId );
        if ( loggedInPaxRank != null )
        {
          participantIds.add( userId );
        }
      }

      Map<Long, Participant> participantMap = getParticipantMap( participantIds );

      for ( StackStandingParticipant user : users )
      {
        ThrowdownStackRankingParticipant rankingPax = buildRankingParticipant( user, promotion, round, participantMap );
        ranking.addLeader( rankingPax );
        // set the first rank user as title user for spectator
        if ( fetchTitleUser && loggedInPaxRank == null && ranking.getTitleUser() == null )
        {
          ranking.setTitleUser( rankingPax );
        }
      }

      // set the competitor user rank as title user for competitor
      if ( fetchTitleUser && loggedInPaxRank != null )
      {
        ThrowdownStackRankingParticipant rankingPax = buildRankingParticipant( loggedInPaxRank, promotion, round, participantMap );
        ranking.setTitleUser( rankingPax );
      }
    }
    return ranking;
  }

  private Map<Long, Participant> getParticipantMap( Set<Long> participantIds )
  {
    List<Participant> participants = new ArrayList<Participant>();
    Map<Long, Participant> participantMap = new HashMap<Long, Participant>();
    ProjectionCollection collection = new ProjectionCollection();
    collection.add( new ProjectionAttribute( "id" ) );
    collection.add( new ProjectionAttribute( "firstName" ) );
    collection.add( new ProjectionAttribute( "lastName" ) );
    collection.add( new ProjectionAttribute( "userName" ) );
    collection.add( new ProjectionAttribute( "avatarOriginal" ) );
    collection.add( new ProjectionAttribute( "avatarSmall" ) );
    participants = participantService.getParticipantsByIdWithProjections( new ArrayList<Long>( participantIds ), collection );

    for ( Participant pax : participants )
    {
      participantMap.put( pax.getId(), pax );
    }
    return participantMap;
  }

  @Override
  public ThrowdownStackRankingView getRankingDetails( Long promotionId, int pageSize, int pageNumber )
  {
    return getRankingDetails( promotionId, null, null, pageSize, pageSize );
  }

  @Override
  public ThrowdownStackRankingView getRankingDetails( Long promotionId, Long nodeTypeId, Long nodeId, int pageSize, int pageNumber )
  {
    Long userId = UserManager.getUserId();
    if ( nodeId == null )
    {
      nodeId = userService.getPrimaryUserNode( userId ).getNode().getId();
    }

    boolean fetchToppersWithBadges = pageNumber == 0 ? true : false;
    ThrowdownStackRankingView rankingView = getRankingSummary( promotionId, nodeTypeId, nodeId, pageSize, pageNumber, false );
    if ( !rankingView.isVisible() )
    {
      // no ranking exists
      return rankingView;
    }

    // no tie in first three ranks. It could be tie in 3rd and 4th. So fetch 4 users at least.
    if ( fetchToppersWithBadges )
    {
      ThrowdownStackRanking ranking = rankingView.getRankings();
      List<StackStandingParticipant> toppers = stackStandingService.getTopRankingParticipants( ranking.getStandingNode().getId() );
      Iterator<StackStandingParticipant> leaderIter = toppers.iterator();
      boolean showLeaders = true;
      int rank = 1;
      while ( leaderIter.hasNext() && rank <= ThrowdownStackRanking.NUMBER_OF_TOPPERS + 1 )
      {
        StackStandingParticipant leader = leaderIter.next();
        if ( leader.getStanding() != rank )
        {
          showLeaders = false;
        }
        rank = rank + 1;
      }
      ranking.setShowLeaders( showLeaders );

      if ( ranking.isShowLeaders() )
      {
        if ( toppers.size() > ThrowdownStackRanking.NUMBER_OF_TOPPERS )
        {
          toppers.remove( ThrowdownStackRanking.NUMBER_OF_TOPPERS );
        }
        String siteUrlPrefix = getSystemVariableService().getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();
        Set<Long> participantIds = new HashSet<Long>();
        for ( StackStandingParticipant user : toppers )
        {
          participantIds.add( user.getParticipant().getId() );
        }
        Map<Long, Participant> participantMap = getParticipantMap( participantIds );

        for ( StackStandingParticipant rankingParticipant : toppers )
        {
          Round round = ranking.getLeaders().iterator().next().getRound();
          ThrowdownStackRankingParticipant rankingPax = buildRankingParticipant( rankingParticipant, ranking.getPromotion(), round, participantMap );
          List<BadgeInfo> badges = new ArrayList<BadgeInfo>();
          boolean earned = false;
          if ( rankingPax.isPayoutIssued() )
          {
            earned = true;
            badges = getBadges( ranking, rankingPax, promotionId );
          }
          else
          {
            badges = forecastBadges( ranking, rankingPax, promotionId );
          }
          if ( !badges.isEmpty() && !badges.iterator().next().getBadgeDetails().isEmpty() )
          {
            rankingPax.setEarned( earned );
            rankingPax.setBadges( badges );
          }
          rankingPax.setIcon( siteUrlPrefix + "/" + CmsResourceBundle.getCmsBundle().getString( "participant.throwdownstats.RANK_IMG_" + rankingParticipant.getStanding() ) );
          ranking.addBadgeHolder( rankingPax );
        }
      }
    }
    return rankingView;
  }

  @Override
  public ThrowdownPlayerStatsBean getPlayerStats( Long promotionId )
  {
    return getPlayerStats( UserManager.getUserId(), promotionId );
  }

  @Override
  public ThrowdownPlayerStatsBean getPlayerStats( Long userId, Long promotionId )
  {
    ThrowdownPlayerStatsBean playerStats = new ThrowdownPlayerStatsBean();
    ThrowdownPromotion promotion = (ThrowdownPromotion)getPromotionService().getPromotionById( promotionId );
    Round round = getAppropriateRound( promotionId );
    playerStats.setPromotion( promotion );
    Team team = teamDAO.getTeamByUserIdForPromotion( userId, promotionId );
    List<Match> matches = matchDAO.getMatchesByPromotionAndTeam( promotionId, team.getId() );
    Map<Integer, ThrowdownMatchBean> playedMatchBeans = new HashMap<Integer, ThrowdownMatchBean>();

    for ( Match match : matches )
    {
      ThrowdownMatchBean matchBean = getMatchSummaryForPlayerStats( match.getId(), userId );
      buildMatchDetailUrl( matchBean );
      playedMatchBeans.put( matchBean.getRoundNumber(), matchBean );
    }

    // attach self object
    ThrowdownTeamBean self = null;
    for ( ThrowdownMatchBean matchBean : playedMatchBeans.values() )
    {
      if ( !matchBean.getPrimaryTeam().isShadowPlayer() && matchBean.getPrimaryTeam().getTeam().getParticipant().getId().equals( userId ) )
      {
        self = matchBean.getPrimaryTeam();
        break;
      }
      if ( !matchBean.getSecondaryTeam().isShadowPlayer() && matchBean.getSecondaryTeam().getTeam().getParticipant().getId().equals( userId ) )
      {
        self = matchBean.getSecondaryTeam();
        break;
      }
    }
    if ( self != null )
    {
      self.setBadges( getBadges( promotionId, userId ) );
      Integer rank = stackStandingService.getHierarchyRankForUser( promotionId, round.getRoundNumber(), userId );
      if ( rank != null )
      {
        self.setRank( rank );
      }
    }
    playerStats.setSelf( self );

    Division division = promotion.getDivisions().iterator().next();
    int roundNumber = 1;
    while ( roundNumber <= promotion.getNumberOfRounds() )
    {
      if ( playedMatchBeans.keySet().contains( roundNumber ) )
      {
        playerStats.addMatch( playedMatchBeans.get( roundNumber ) );
      }
      else
      {
        playerStats.addMatch( getMatchSummaryForUnscheduledRounds( userId, promotion, division, self.getTeam(), playerStats.getMatches().size() + 1 ) );
      }
      roundNumber = roundNumber + 1;
    }

    return playerStats;
  }

  private ThrowdownMatchBean getMatchSummaryForUnscheduledRounds( Long userId, ThrowdownPromotion promotion, Division division, Team team, int roundNumber )
  {
    ThrowdownMatchBean matchBean = new ThrowdownMatchBean();
    Match match = new Match();
    match.setId( -1L );
    matchBean.setMatch( match );
    matchBean.setRound( roundDAO.getRoundsForPromotionByDivisionAndRoundNumber( promotion.getId(), division.getId(), roundNumber ) );
    matchBean.setPromotion( promotion );

    // build the basic team info
    ThrowdownTeamBean bean1 = new ThrowdownTeamBean();
    bean1.setTeam( team );
    bean1.setAvatarUrl( team.getParticipant().getAvatarSmallFullPath() );
    bean1.setAvatarUrlSmall( team.getParticipant().getAvatarSmallFullPath() );
    matchBean.setPrimaryTeam( bean1 );

    return matchBean;
  }

  public Match getMatchById( Long matchId )
  {
    return matchDAO.getMatch( matchId );
  }

  public List<Match> getUnplayedMatchesForPromotionAndRound( Long roundId )
  {
    return matchDAO.getUnplayedMatchesForPromotionAndRound( roundId );
  }

  public List<Round> getRoundsForPromotionByRoundNumber( Long promotionId, int roundNumber )
  {
    return roundDAO.getRoundsForPromotionByRoundNumber( promotionId, roundNumber );
  }

  /*
   * This method applies the appropriate ordering based on many factors
   */
  private void arrangePrimaryAndSecondaryPosition( Long userId, ThrowdownMatchBean matchBean, ThrowdownTeamBean bean1, ThrowdownTeamBean bean2 )
  {
    Team team1 = bean1.getTeam();
    Team team2 = bean2.getTeam();
    // shadow player is always on right
    if ( team1.isShadowPlayer() )
    {
      matchBean.setPrimaryTeam( bean2 );
      matchBean.setSecondaryTeam( bean1 );
      return;
    }
    if ( team2.isShadowPlayer() )
    {
      matchBean.setPrimaryTeam( bean1 );
      matchBean.setSecondaryTeam( bean2 );
      return;
    }
    // logged in user is always on left (primary)
    if ( team1.getParticipant().getId().equals( userId ) )
    {
      matchBean.setPrimaryTeam( bean1 );
      matchBean.setSecondaryTeam( bean2 );
      return;
    }
    if ( team2.getParticipant().getId().equals( userId ) )
    {
      matchBean.setPrimaryTeam( bean2 );
      matchBean.setSecondaryTeam( bean1 );
      return;
    }
    // highest ranking
    if ( bean1.getRank() > bean2.getRank() )
    {
      matchBean.setPrimaryTeam( bean1 );
      matchBean.setSecondaryTeam( bean2 );
      return;
    }
    if ( bean2.getRank() > bean1.getRank() )
    {
      matchBean.setPrimaryTeam( bean2 );
      matchBean.setSecondaryTeam( bean1 );
      return;
    }
    // progress
    BigDecimal player1Progress = null != bean1.getProgress() ? bean1.getProgress() : new BigDecimal( 0 );
    BigDecimal player2Progress = null != bean2.getProgress() ? bean2.getProgress() : new BigDecimal( 0 );
    if ( player1Progress.compareTo( player2Progress ) > 0 )
    {
      matchBean.setPrimaryTeam( bean1 );
      matchBean.setSecondaryTeam( bean2 );
      return;
    }
    if ( player1Progress.compareTo( player2Progress ) < 0 )
    {
      matchBean.setPrimaryTeam( bean2 );
      matchBean.setSecondaryTeam( bean1 );
      return;
    }
    // sort alpha by lastname/firstname
    if ( team1.getParticipant().getNameLFMNoComma().compareToIgnoreCase( team2.getParticipant().getNameLFMNoComma() ) < 0 )
    {
      matchBean.setPrimaryTeam( bean1 );
      matchBean.setSecondaryTeam( bean2 );
    }
    else
    {
      matchBean.setPrimaryTeam( bean2 );
      matchBean.setSecondaryTeam( bean1 );
    }
  }

  private void arrangePrimaryAndSecondaryPosition( ThrowdownMatchBean matchBean, ThrowdownTeamBean bean1, ThrowdownTeamBean bean2 )
  {
    Long currentUserId = UserManager.getUserId();
    arrangePrimaryAndSecondaryPosition( currentUserId, matchBean, bean1, bean2 );
  }

  private ThrowdownTeamBean buildTeamBeanWithStats( MatchTeamOutcome matchTeamOutcome )
  {
    ThrowdownTeamBean teamBean = buildTeamBean( matchTeamOutcome );
    teamBean.setStats( getTeamStatsForPromotion( matchTeamOutcome.getTeam().getId(), matchTeamOutcome.getPromotion().getId() ) );
    return teamBean;
  }

  private ThrowdownTeamBean buildTeamBean( MatchTeamOutcome matchTeamOutcome )
  {
    String siteUrl = systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();
    ThrowdownTeamBean teamBean = new ThrowdownTeamBean();
    if ( !matchTeamOutcome.getTeam().isShadowPlayer() || !matchTeamOutcome.getOutcome().equals( MatchTeamOutcomeType.lookup( MatchTeamOutcomeType.NONE ) ) )
    {
      teamBean.setCurrentProgress( matchTeamOutcome.getCurrentValueWithPrecisionAndRounding() );
    }
    else
    {
      Long roundId = matchTeamOutcome.getMatch().getRound().getId();
      Iterator<MatchTeamOutcome> teamIter = matchTeamOutcome.getMatch().getTeamOutcomes().iterator();
      Team team1 = teamIter.next().getTeam();
      Team team2 = teamIter.next().getTeam();
      Long oppositionTeamId = !team1.isShadowPlayer() ? team1.getId() : null;
      oppositionTeamId = oppositionTeamId == null && !team2.isShadowPlayer() ? team2.getId() : oppositionTeamId;
      teamBean.setCurrentProgress( throwdownService.getShadowScore( roundId, oppositionTeamId ) );
    }

    teamBean.setTeam( matchTeamOutcome.getTeam() );
    teamBean.setBaseUnitPosition( matchTeamOutcome.getPromotion().getBaseUnitPosition() );
    teamBean.setBaseUnit( matchTeamOutcome.getPromotion().getBaseUnitText() );
    teamBean.setPrecision( matchTeamOutcome.getPromotion().getAchievementPrecision().getPrecision() );
    teamBean.setOutcome( matchTeamOutcome.getOutcome() );
    teamBean.setDisplayProgress( matchTeamOutcome.getPromotion().isDisplayTeamProgress() );

    if ( !matchTeamOutcome.getTeam().isShadowPlayer() )
    {
      Participant pax = matchTeamOutcome.getTeam().getParticipant();
      // Note: large image which is in original ratio of dimensions of image uploaded is not fitting
      // well for UI pages.
      // So bringing the small one which is in square shape as per suggestion by UET team given in
      // bug 52271 and 52113
      teamBean.setAvatarUrl( pax.getAvatarSmallFullPath( null ) );
      teamBean.setAvatarUrlSmall( pax.getAvatarSmallFullPath( null ) );
      Map<String, Object> paramMap = new HashMap<String, Object>();
      paramMap.put( "paxId", pax.getId() );
      paramMap.put( "isFullPage", "true" );
      paramMap.put( "defaultTab", "playerStats" );
      if ( pax.isAllowPublicInformation() )
      {
        teamBean.setProfileUrl( ClientStateUtils.generateEncodedLink( siteUrl, PageConstants.PUBLIC_PROFILE_VIEW, paramMap ) );
      }
    }
    else
    {
      teamBean.setAvatarUrl( null );
      teamBean.setAvatarUrlSmall( null );
    }
    return teamBean;
  }

  private ThrowdownStackRankingParticipant buildRankingParticipant( StackStandingParticipant standing, ThrowdownPromotion promotion, Round round, Map<Long, Participant> participantMap )
  {
    ThrowdownStackRankingParticipant rankingPax = new ThrowdownStackRankingParticipant();
    rankingPax.setParticipant( participantMap.get( standing.getParticipant().getId() ) );
    rankingPax.setRank( standing.getStanding() );
    rankingPax.setScore( standing.getStackStandingFactor() );
    rankingPax.setPayoutIssued( standing.isPayoutsIssued() );
    rankingPax.setDisplayProgress( promotion.isDisplayTeamProgress() );
    rankingPax.setBaseUnit( promotion.getBaseUnitText() );
    rankingPax.setBaseUnitPosition( promotion.getBaseUnitPosition() );
    rankingPax.setRound( round );
    rankingPax.setPromotion( promotion );
    return rankingPax;
  }

  // ranking forecasting
  private List<BadgeInfo> forecastBadges( ThrowdownStackRanking ranking, ThrowdownStackRankingParticipant rankingPax, Long promotionId )
  {
    String siteUrlPrefix = getSystemVariableService().getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();
    NodeType nodeType = ranking.isHierarchyRanking() ? getHierarchyPlaceHolderNodeType() : ranking.getNode().getNodeType();
    List<BadgeInfo> badgeInfos = gamificationService.getTDRankingBadgesEarnable( promotionId, nodeType, rankingPax.getRank(), BadgeLevelType.lookup( BadgeLevelType.STACK_STAND ) );
    for ( BadgeInfo badgeInfo : badgeInfos )
    {
      for ( BadgeDetails badgeDetail : badgeInfo.getBadgeDetails() )
      {
        badgeDetail.setImg( siteUrlPrefix + "/" + badgeDetail.getImg() );
        badgeDetail.setImgLarge( siteUrlPrefix + "/" + badgeDetail.getImgLarge() );
      }
    }
    return badgeInfos;
  }

  // ranking find appropriate one given for ranking selected
  @SuppressWarnings( "rawtypes" )
  private List<BadgeInfo> getBadges( ThrowdownStackRanking ranking, ThrowdownStackRankingParticipant rankingPax, Long promotionId )
  {
    Long userId = rankingPax.getUserId();
    List<BadgeInfo> badges = new ArrayList<BadgeInfo>();
    List<ParticipantBadge> paxBadges = gamificationService.getBadgeByParticipantEarnedForThrowdown( promotionId, userId );
    NodeType nodeType = ranking.isHierarchyRanking() ? getHierarchyPlaceHolderNodeType() : ranking.getNode().getNodeType();
    String siteUrlPrefix = getSystemVariableService().getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();

    for ( ParticipantBadge paxBadge : paxBadges )
    {
      BadgeRule rule = paxBadge.getBadgeRule();
      boolean isBadgeGivenForThisRanking = false;
      if ( rule.getLevelType().equals( BadgeLevelType.lookup( BadgeLevelType.STACK_STAND ) ) && rule.getLevelName().equals( nodeType.getNodeTypeName() ) )
      {
        if ( rankingPax.getRank() >= rule.getMinimumQualifier() && rankingPax.getRank() <= rule.getMaximumQualifier() )
        {
          isBadgeGivenForThisRanking = true;
        }
      }
      if ( !isBadgeGivenForThisRanking )
      {
        continue;
      }
      else
      {
        BadgeDetails badgeDetail = new BadgeDetails();
        badgeDetail.setBadgeId( paxBadge.getId() );
        badgeDetail.setBadgeName( paxBadge.getBadgeRule().getBadgeNameTextFromCM() );
        badgeDetail.setBadgeDescription( rule.getBadgeDescriptionTextFromCM() );
        badgeDetail.setBadgeType( paxBadge.getBadgePromotion().getBadgeType().getCode() );
        badgeDetail.setDateEarned( DateUtils.toDisplayString( paxBadge.getEarnedDate() ) );
        List earnedNotEarnedImageList = gamificationService.getEarnedNotEarnedImageList( paxBadge.getBadgeRule().getBadgeLibraryCMKey() );
        Iterator itr = earnedNotEarnedImageList.iterator();
        while ( itr.hasNext() )
        {
          BadgeLibrary badgeLib = (BadgeLibrary)itr.next();
          badgeDetail.setImg( siteUrlPrefix + "/" + badgeLib.getEarnedImageSmall() );
          badgeDetail.setImgLarge( siteUrlPrefix + "/" + badgeLib.getEarnedImageMedium() );
        }
        BadgeInfo badgeInfo = new BadgeInfo();
        badgeInfo.getBadgeDetails().add( badgeDetail );
        badges.add( badgeInfo );
        break;
      }
    }
    return badges;
  }

  // profile badges
  @SuppressWarnings( "rawtypes" )
  private List<BadgeInfo> getBadges( Long promotionId, Long userId )
  {
    List<BadgeInfo> badges = new ArrayList<BadgeInfo>();
    List<ParticipantBadge> paxBadges = gamificationService.getBadgeByParticipantEarnedForThrowdown( promotionId, userId );
    String siteUrlPrefix = getSystemVariableService().getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();

    for ( ParticipantBadge paxBadge : paxBadges )
    {
      BadgeDetails badgeDetail = new BadgeDetails();
      badgeDetail.setBadgeId( paxBadge.getId() );
      badgeDetail.setBadgeName( paxBadge.getBadgeRule().getBadgeNameTextFromCM() );
      badgeDetail.setBadgeDescription( paxBadge.getBadgeRule().getBadgeDescriptionTextFromCM() );
      badgeDetail.setBadgeType( paxBadge.getBadgePromotion().getBadgeType().getCode() );
      badgeDetail.setDateEarned( DateUtils.toDisplayString( paxBadge.getEarnedDate() ) );
      List earnedNotEarnedImageList = gamificationService.getEarnedNotEarnedImageList( paxBadge.getBadgeRule().getBadgeLibraryCMKey() );
      Iterator itr = earnedNotEarnedImageList.iterator();
      while ( itr.hasNext() )
      {
        BadgeLibrary badgeLib = (BadgeLibrary)itr.next();
        badgeDetail.setImg( siteUrlPrefix + "/" + badgeLib.getEarnedImageSmall() );
        badgeDetail.setImgLarge( siteUrlPrefix + "/" + badgeLib.getEarnedImageMedium() );
      }
      BadgeInfo badgeInfo = new BadgeInfo();
      badgeInfo.getBadgeDetails().add( badgeDetail );
      badges.add( badgeInfo );
    }
    return badges;
  }

  // match detail badges
  @SuppressWarnings( "rawtypes" )
  private List<BadgeInfo> getBadgesGrouped( Long promotionId, Participant pax, int size )
  {
    List<BadgeInfo> badges = new ArrayList<BadgeInfo>();
    if ( pax != null )
    {
      BadgeInfo badgeInfo = new BadgeInfo();
      List<ParticipantBadge> paxBadges = gamificationService.getBadgeByParticipantEarnedForThrowdown( promotionId, pax.getId() );
      if ( paxBadges != null && size > 0 && paxBadges.size() > size )
      {
        paxBadges = paxBadges.subList( 0, size );
      }
      String siteUrlPrefix = getSystemVariableService().getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();

      Map<BadgeRule, List<ParticipantBadge>> groupedBadges = new HashMap<BadgeRule, List<ParticipantBadge>>();

      if ( paxBadges != null )
      {
        for ( ParticipantBadge paxBadge : paxBadges )
        {
          Set<BadgeRule> keys = groupedBadges.keySet();
          if ( keys.contains( paxBadge.getBadgeRule() ) )
          {
            groupedBadges.get( paxBadge.getBadgeRule() ).add( paxBadge );
          }
          else
          {
            List<ParticipantBadge> badgesByRule = new ArrayList<ParticipantBadge>();
            badgesByRule.add( paxBadge );
            groupedBadges.put( paxBadge.getBadgeRule(), badgesByRule );
          }
        }
      }

      for ( BadgeRule badgeRule : groupedBadges.keySet() )
      {
        ParticipantBadge paxBadge = groupedBadges.get( badgeRule ).iterator().next();
        BadgeDetails badgeDetail = new BadgeDetails();
        badgeDetail.setEarnCount( groupedBadges.get( badgeRule ).size() );
        badgeDetail.setBadgeId( paxBadge.getId() );
        badgeDetail.setBadgeName( paxBadge.getBadgeRule().getBadgeNameTextFromCM() );
        badgeDetail.setBadgeDescription( paxBadge.getBadgeRule().getBadgeDescriptionTextFromCM() );
        badgeDetail.setBadgeType( paxBadge.getBadgePromotion().getBadgeType().getCode() );
        badgeDetail.setDateEarned( DateUtils.toDisplayString( paxBadge.getEarnedDate() ) );
        List earnedNotEarnedImageList = gamificationService.getEarnedNotEarnedImageList( paxBadge.getBadgeRule().getBadgeLibraryCMKey() );
        Iterator itr = earnedNotEarnedImageList.iterator();
        while ( itr.hasNext() )
        {
          BadgeLibrary badgeLib = (BadgeLibrary)itr.next();
          badgeDetail.setImg( siteUrlPrefix + "/" + badgeLib.getEarnedImageSmall() );
          badgeDetail.setImgLarge( siteUrlPrefix + "/" + badgeLib.getEarnedImageMedium() );
        }
        badgeInfo.getBadgeDetails().add( badgeDetail );
      }
      badges.add( badgeInfo );
    }
    return badges;
  }

  private void buildRanking( ThrowdownTeamBean teamBean, ThrowdownMatchBean matchBean )
  {
    if ( !teamBean.getTeam().isShadowPlayer() )
    {
      Long userId = teamBean.getTeam().getParticipant().getId();
      Integer rank = stackStandingService.getHierarchyRankForUser( matchBean.getPromotionId(), matchBean.getRoundNumber(), userId );
      if ( null != rank )
      {
        teamBean.setRank( rank );
        String siteUrl = systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.put( "promotionId", matchBean.getPromotionId() );
        parameterMap.put( "nodeTypeId", -1L );
        parameterMap.put( "nodeId", -1L );
        teamBean.setRankUrl( ClientStateUtils.generateEncodedLink( siteUrl, PageConstants.THROWDOWN_RANKING_DETAIL, parameterMap ) );
      }
    }
  }

  private void buildSmackTalks( ThrowdownMatchBean matchBean )
  {
    Long userId = UserManager.getUserId();
    List<SmackTalkCommentViewBean> commentBeans = new ArrayList<SmackTalkCommentViewBean>();
    List<SmackTalkComment> smackTalkPosts = smackTalkDAO.getSmackTalkPostsByMatch( matchBean.getMatch().getId() );
    Collections.sort( smackTalkPosts, SORT_COMPARATOR );
    for ( SmackTalkComment smackTalkComment : smackTalkPosts )
    {
      SmackTalkCommentViewBean commentBean = new SmackTalkCommentViewBean();
      List<SmackTalkComment> comments = smackTalkDAO.getUserCommentsBySmackTalkPost( smackTalkComment.getId() );
      for ( SmackTalkComment comment : comments )
      {
        comment.setNumLikers( smackTalkDAO.getLikeCountBySmackTalk( comment.getId() ) );
        comment.setLiked( smackTalkDAO.isCurrentUserLikedSmackTalk( comment.getId(), userId ) );
        if ( userId.equals( comment.getUser().getId() ) )
        {
          comment.setMine( true );
        }
        commentBean.getCommentsPerPost().add( new SmackTalkCommentViewBean( comment ) );
      }
      commentBean.setId( smackTalkComment.getId() );
      commentBean.setCommenter( new SmackTalkParticipantView( smackTalkComment.getUser().getId(),
                                                              smackTalkComment.getUser().getFirstName(),
                                                              smackTalkComment.getUser().getLastName(),
                                                              smackTalkComment.getUser().getAvatarSmallFullPath(),
                                                              smackTalkComment.getUser().getPositionType(),
                                                              null,
                                                              null ) );
      commentBean.setComment( smackTalkComment.getComments() );

      Long postLikeCount = smackTalkDAO.getLikeCountBySmackTalk( smackTalkComment.getId() );
      commentBean.setNumLikers( postLikeCount );
      commentBean.setLiked( smackTalkDAO.isCurrentUserLikedSmackTalk( smackTalkComment.getId(), userId ) );
      commentBean.setHidden( smackTalkComment.getIsHidden() );
      commentBean.setMatchBean( getMatchSummaryForMatchList( smackTalkComment.getMatch().getId() ) );
      commentBean.setAuditCreateInfo( smackTalkComment.getAuditCreateInfo() );
      if ( !commentBean.getMatchBean().getPrimaryTeam().getTeam().isShadowPlayer() && commentBean.getMatchBean().getPrimaryTeam().getTeam().getParticipant().getId().equals( userId )
          || !commentBean.getMatchBean().getSecondaryTeam().getTeam().isShadowPlayer() && commentBean.getMatchBean().getSecondaryTeam().getTeam().getParticipant().getId().equals( userId ) )
      {
        commentBean.getMatchBean().setMine( true );
      }
      else
      {
        commentBean.getMatchBean().setMine( false );
      }

      commentBeans.add( commentBean );
    }

    matchBean.setSmackTalkPosts( commentBeans );
    // if primary or secondary team matches logged in user, set it as his own match.
    if ( !matchBean.getPrimaryTeam().getTeam().isShadowPlayer() && matchBean.getPrimaryTeam().getTeam().getParticipant().getId().equals( userId )
        || !matchBean.getSecondaryTeam().getTeam().isShadowPlayer() && matchBean.getSecondaryTeam().getTeam().getParticipant().getId().equals( userId ) )
    {
      matchBean.setMine( true );
    }
    else
    {
      matchBean.setMine( false );
    }

  }

  public MatchTeamProgress saveProgress( MatchTeamProgress matchTeamProgress )
  {
    matchTeamProgress = matchTeamProgressDAO.save( matchTeamProgress );
    MatchTeamOutcome matchTeamOutcome = matchTeamOutcomeDAO.getMatchTeamOutcome( matchTeamProgress.getTeamOutcome().getId() );

    BigDecimal currentValue = matchTeamOutcome.getTotalProgress();
    if ( matchTeamProgress.getProgressType().getCode().equals( ThrowdownMatchProgressType.INCREMENTAL ) )
    {
      // add this progress to existing progress
      currentValue = currentValue.add( matchTeamProgress.getProgress() );
    }
    else if ( matchTeamProgress.getProgressType().getCode().equals( ThrowdownMatchProgressType.REPLACE ) )
    {
      // ok, replace/disregard what's there and replace with this number
      currentValue = matchTeamProgress.getProgress();
    }

    matchTeamOutcome.setCurrentValue( currentValue );
    matchTeamOutcome = saveMatchTeamOutcome( matchTeamOutcome );
    matchTeamProgress.setTeamOutcome( matchTeamOutcome );
    return matchTeamProgress;
  }

  public List<MatchTeamProgress> getProgressListByUser( Long userId, Long promotionId, Integer roundNumber )
  {
    List<MatchTeamProgress> progressList = new ArrayList<MatchTeamProgress>();
    Team team = getTeamByUserIdForPromotion( userId, promotionId );
    MatchTeamOutcome matchTeamOutcome = getOutcomeForMatch( team.getId(), promotionId, roundNumber );
    if ( matchTeamOutcome != null )
    {
      progressList = getProgressListByOutcome( matchTeamOutcome.getId() );
    }
    return progressList;
  }

  public StackStanding getRankingForPromotion( Long promotionId )
  {
    Round round = getAppropriateRound( promotionId );
    StackStanding standings = stackStandingService.getRankingForPromotionAndRound( promotionId, round.getRoundNumber() );
    int roundNumber = round.getRoundNumber();
    if ( standings == null )
    {
      // handle progress back logs
      while ( standings == null && roundNumber > 1 )
      {
        roundNumber = roundNumber - 1;
        standings = stackStandingService.getRankingForPromotionAndRound( promotionId, roundNumber );
      }
    }
    return standings;
  }

  public List<MatchTeamProgress> getProgressListByOutcome( Long id )
  {
    return matchTeamProgressDAO.getProgressListByOutcome( id );
  }

  public MatchTeamOutcome saveMatchTeamOutcome( MatchTeamOutcome matchTeamOutcome )
  {
    return matchTeamOutcomeDAO.save( matchTeamOutcome );
  }

  public Team getTeamByUserIdForPromotion( Long userId, Long promotionId )
  {
    Team team = teamDAO.getTeamByUserIdForPromotion( userId, promotionId );
    return team;
  }

  public List<Team> getTeamsByUserIdsForPromotion( Set<Long> userIds, Long promotionId )
  {
    return teamDAO.getTeamsByUserIdsForPromotion( userIds, promotionId );
  }

  public Team getTeamByUserIdForPromotionWithAssociations( Long userId, Long promotionId, AssociationRequestCollection associationRequestCollection )
  {
    return teamDAO.getTeamByUserIdForPromotionWithAssociations( userId, promotionId, associationRequestCollection );
  }

  public MatchTeamOutcome getOutcomeForMatch( Long teamId, Long promotionId, Integer roundNumber )
  {
    return matchTeamOutcomeDAO.getOutcomeForMatch( teamId, promotionId, roundNumber );
  }

  public MatchTeamOutcome getOutcomeForMatch( Long teamId, Long promotionId, Integer roundNumber, AssociationRequestCollection associationRequestCollection )
  {
    return matchTeamOutcomeDAO.getOutcomeForMatch( teamId, promotionId, roundNumber, associationRequestCollection );
  }

  public MatchTeamOutcome getOutcomeForTeamInSpecificRound( Long userId, int roundNumber, Long promotionId )
  {
    return matchTeamOutcomeDAO.getOutcomeForTeamInSpecificRound( userId, roundNumber, promotionId );
  }

  public MatchTeamOutcome getOutcomeForTeamInSpecificRound( Long userId, int roundNumber, Long promotionId, AssociationRequestCollection associationRequestCollection )
  {
    return matchTeamOutcomeDAO.getOutcomeForTeamInSpecificRound( userId, roundNumber, promotionId, associationRequestCollection );
  }

  public List<Long> getPaxPlayingPromotionInRound( Long promotionId, Integer roundNumber )
  {
    return teamDAO.getPaxPlayingPromotionInRound( promotionId, roundNumber );
  }

  public Match saveMatch( Match match )
  {
    return matchDAO.save( match );
  }

  public Round markRoundPaidout( Long roundId )
  {
    Round round = roundDAO.getRoundById( roundId );
    round.setPayoutsIssued( true );
    return roundDAO.save( round );
  }

  public ThrowdownPromotion getThrowdownPromotion( Long promotionId )
  {
    return (ThrowdownPromotion)getPromotionService().getPromotionById( promotionId );
  }

  public TeamDAO getTeamDAO()
  {
    return teamDAO;
  }

  public void setTeamDAO( TeamDAO teamDAO )
  {
    this.teamDAO = teamDAO;
  }

  public MatchDAO getMatchDAO()
  {
    return matchDAO;
  }

  public void setMatchDAO( MatchDAO matchDAO )
  {
    this.matchDAO = matchDAO;
  }

  public PromotionService getPromotionService()
  {
    return promotionService;
  }

  public void setPromotionService( PromotionService promotionService )
  {
    this.promotionService = promotionService;
  }

  public HierarchyService getHierarchyService()
  {
    return hierarchyService;
  }

  public void setHierarchyService( HierarchyService hierarchyService )
  {
    this.hierarchyService = hierarchyService;
  }

  public ListBuilderService getListBuilderService()
  {
    return listBuilderService;
  }

  public void setListBuilderService( ListBuilderService listBuilderService )
  {
    this.listBuilderService = listBuilderService;
  }

  public ParticipantService getParticipantService()
  {
    return participantService;
  }

  public void setParticipantService( ParticipantService participantService )
  {
    this.participantService = participantService;
  }

  public DivisionDAO getDivisionDAO()
  {
    return divisionDAO;
  }

  public void setDivisionDAO( DivisionDAO divisionDAO )
  {
    this.divisionDAO = divisionDAO;
  }

  public ThrowdownService getThrowdownService()
  {
    return throwdownService;
  }

  public void setThrowdownService( ThrowdownService throwdownService )
  {
    this.throwdownService = throwdownService;
  }

  public RoundDAO getRoundDAO()
  {
    return roundDAO;
  }

  public void setRoundDAO( RoundDAO roundDAO )
  {
    this.roundDAO = roundDAO;
  }

  public ThrowdownMatchSchedulerFactory getThrowdownMatchSchedulerFactory()
  {
    return throwdownMatchSchedulerFactory;
  }

  public void setThrowdownMatchSchedulerFactory( ThrowdownMatchSchedulerFactory throwdownMatchSchedulerFactory )
  {
    this.throwdownMatchSchedulerFactory = throwdownMatchSchedulerFactory;
  }

  public MailingService getMailingService()
  {
    return mailingService;
  }

  public void setMailingService( MailingService mailingService )
  {
    this.mailingService = mailingService;
  }

  public void setStackStandingService( StackStandingService stackStandingService )
  {
    this.stackStandingService = stackStandingService;
  }

  public void setStackStandingNodeService( StackStandingNodeService stackStandingNodeService )
  {
    this.stackStandingNodeService = stackStandingNodeService;
  }

  public void setGamificationService( GamificationService gamificationService )
  {
    this.gamificationService = gamificationService;
  }

  public void setUserService( UserService userService )
  {
    this.userService = userService;
  }

  private void sendDivisionConflictsAlertMessage( ThrowdownPromotion promotion, int roundNumber, Set<ParticipantAudienceConflictResult> conflicts )
  {
    // return if there are no conflicts
    if ( conflicts.isEmpty() )
    {
      return;
    }

    StringBuilder sb = new StringBuilder();
    sb.append( "<html><body>" );
    sb.append( "<p>Throwdown Promotion Match Generation Process for promotion <b>" + promotion.getPromoNameFromCM() + "</b> (promotion id " + promotion.getId() + ")" );
    sb.append( " contains the following audience conflicts for Round " );
    sb.append( roundNumber + ".  Please fix the audience settings for the MatchGroups to prevent these emails from occurring." );
    sb.append( "<br/><b>Details:</b></p>" );
    sb.append( "<table border=\"1\" cellpadding=\"5\" cellspacing=\"0\">" );
    sb.append( "<tr><td><b>Participant ID</b></td>" );
    sb.append( "<td><b>Participant</b></td>" );
    sb.append( "<td><b>Assigned To Match Group</b></td>" );
    sb.append( "<td><b>Match Group Conflicts</b></td>" );
    sb.append( "<td><b>Previous Match Group</b></td>" );
    sb.append( "<td><b>Previous Match Group is Active</b></td></tr>" );

    for ( ParticipantAudienceConflictResult conflict : conflicts )
    {
      sb.append( "<tr>" );
      sb.append( "<td>" + conflict.getParticipant().getId() + "</td>" );
      sb.append( "<td>" + conflict.getParticipant().getNameFLNoComma() + "</td>" );
      sb.append( "<td>" + conflict.getAssignedDivision().getDivisionNameFromCM() + "</td>" );
      // conflicting divisions
      sb.append( "<td>" );
      Set<Division> divisions = conflict.getDivisions();
      int counter = 0;
      for ( Division division : divisions )
      {
        sb.append( division.getDivisionNameFromCM() );
        counter++;
        if ( counter < divisions.size() )
        {
          sb.append( "<br/>" );
        }
      }
      sb.append( "</td>" );
      String previousDivision = conflict.getPreviousDivision() != null ? conflict.getPreviousDivision().getDivisionNameFromCM() : "";
      boolean previousDivisionAssigned = conflict.getPreviousDivision() != null ? conflict.isWasActiveInPreviousDivision() : false;
      sb.append( "<td>" + previousDivision + "</td>" );
      sb.append( "<td>" + previousDivisionAssigned + "</td>" );
      sb.append( "</tr>" );
    }
    sb.append( "</table>" );
    sb.append( "</body></html>" );
    getMailingService().submitSystemMailing( "Match Scheduling Process For promotion " + promotion.getPromoNameFromCM(), sb.toString(), sb.toString() );
  }

  private void sendSummaryMessage( ThrowdownPromotion promotion, List<Round> rounds, Date nextProcessFiringTime, int roundNumber )
  {
    StringBuilder sb = new StringBuilder();
    sb.append( "<html><body>" );
    sb.append( "<p>Throwdown Promotion Match Generation Process for promotion <b>" + promotion.getPromoNameFromCM() + "</b> (promotion id " + promotion.getId()
        + ") completed successfully for Round " );
    sb.append( roundNumber + "." );
    if ( null != nextProcessFiringTime )
    {
      sb.append( "  The matches for the next Round Scheduling(round number " + ( roundNumber + 1 ) + ") is scheduled for " + DateUtils.toDisplayString( nextProcessFiringTime ) );
    }

    sb.append( "<br/><b>Detail:</b></p>" );

    for ( Round round : rounds )
    {
      sb.append( "<br> Round " + roundNumber + ": " + DateUtils.toDisplayString( round.getStartDate() ) + " - " + DateUtils.toDisplayString( round.getEndDate() ) );

      Division division = round.getDivision();
      Set<Match> matches = round.getMatches();
      sb.append( "<table border=\"1\" cellpadding=\"5\" cellspacing=\"0\">" );
      sb.append( "<tr><td colpan=\"2\"><b>" + division.getDivisionNameFromCM() + "</td></tr>" );
      sb.append( "<tr><td><b>Team 1</b></td><td><b>Team 2</b></td></tr>" );
      for ( Match match : matches )
      {
        Set<MatchTeamOutcome> teamOutcomes = match.getTeamOutcomes();
        Iterator<MatchTeamOutcome> teamOutcomeIter = teamOutcomes.iterator();
        MatchTeamOutcome team1 = teamOutcomeIter.next();
        MatchTeamOutcome team2 = teamOutcomeIter.next();
        sb.append( "<tr><td>" + ( team1.getTeam().isShadowPlayer() ? "<font color=\"red\"> Shadow Player</font>" : team1.getTeam().getParticipant().getNameFLNoComma() ) + "</td><td>"
            + ( team2.getTeam().isShadowPlayer() ? "<font color=\"red\"> Shadow Player</font>" : team2.getTeam().getParticipant().getNameFLNoComma() ) + "</td></tr>" );
      }
      sb.append( "</table>" );
    }

    sb.append( "</body></html>" );
    getMailingService().submitSystemMailing( "Match Scheduling Process For promotion " + promotion.getPromoNameFromCM(), sb.toString(), sb.toString() );
  }

  public Division getCurrentDivisionForUser( Long promotionId, Long userId )
  {
    // this returns division based on current audiences. real time
    ThrowdownPromotion promotion = getThrowdownPromotion( promotionId );
    Participant pax = participantService.getParticipantById( userId );
    for ( Division div : promotion.getDivisions() )
    {
      if ( audienceService.isUserInPromotionDivisionAudiences( pax, div.getCompetitorsAudience() ) )
      {
        return div;
      }
    }
    return null;
  }

  public Division getLastAssociatedDivisionForUser( Long promotionId, Long userId )
  {
    // this returns division based on last round when match got generated for promotion
    Team team = teamDAO.getTeamByUserIdForPromotion( userId, promotionId );
    if ( team == null )
    {
      // never had team till now for any rounds which got completed
      return null;
    }
    return team.getDivision();
  }

  public Division getDivisionForUser( Long promotionId, Long userId )
  {
    // this returns division based on audiences when matches created for specific round
    Round round = getAppropriateRound( promotionId );
    return getDivisionForUser( promotionId, userId, round.getRoundNumber() );
  }

  public Division getDivisionForUser( Long promotionId, Long userId, int roundNumber )
  {
    // this returns division based on audiences when matches created for specific round
    Team team = teamDAO.getTeamByUserIdForPromotion( userId, promotionId );
    if ( team == null )
    {
      // never had team till now for any rounds which got completed
      return null;
    }
    Match match = matchDAO.getMatchByPromotionAndRoundNumberAndTeam( promotionId, roundNumber, team.getId() );
    if ( match == null )
    {
      // didn't have match for round number. so he wouldn't have belonged to any division
      return null;
    }
    return match.getRound().getDivision();
  }

  public Match getMatchByPromotionAndRoundNumberAndTeam( Long promotionId, Long userId, int roundNumber )
  {
    Team team = teamDAO.getTeamByUserIdForPromotion( userId, promotionId );
    if ( team == null )
    {
      // never had team till now for any rounds which got completed
      return null;
    }
    return matchDAO.getMatchByPromotionAndRoundNumberAndTeam( promotionId, roundNumber, team.getId() );
  }

  public boolean showMatchSummary( Long promotionId, Long userId )
  {
    if ( getDivisionForUser( promotionId, userId ) != null )
    {
      return true;
    }
    Set<UserNode> userNodeSet = userService.getUserNodes( userId );
    for ( UserNode userNode : userNodeSet )
    {
      if ( userNode.getHierarchyRoleType().equals( HierarchyRoleType.lookup( HierarchyRoleType.MANAGER ) )
          || userNode.getHierarchyRoleType().equals( HierarchyRoleType.lookup( HierarchyRoleType.OWNER ) ) )
      {
        return false;
      }
    }
    return true;
  }

  public Date getLastFileLoadDateForPromotionAndRound( Long promotionId, Integer roundNumber )
  {
    roundNumber = roundNumber == null ? getAppropriateRound( promotionId ).getRoundNumber() : roundNumber;
    return importFileDAO.getLastFileLoadDateForPromotionAndRound( promotionId, roundNumber );
  }

  public Date getLastFileLoadDateForPromotion( Long promotionId )
  {
    return importFileDAO.getLastFileLoadDateForPromotion( promotionId );
  }

  public MatchTeamProgressDAO getMatchTeamProgressDAO()
  {
    return matchTeamProgressDAO;
  }

  public void setMatchTeamProgressDAO( MatchTeamProgressDAO matchTeamProgressDAO )
  {
    this.matchTeamProgressDAO = matchTeamProgressDAO;
  }

  public MatchTeamOutcomeDAO getMatchTeamOutcomeDAO()
  {
    return matchTeamOutcomeDAO;
  }

  public void setMatchTeamOutcomeDAO( MatchTeamOutcomeDAO matchTeamOutcomeDAO )
  {
    this.matchTeamOutcomeDAO = matchTeamOutcomeDAO;
  }

  public void setSmackTalkDAO( SmackTalkDAO smackTalkDAO )
  {
    this.smackTalkDAO = smackTalkDAO;
  }

  public void setSystemVariableService( SystemVariableService systemVariableService )
  {
    this.systemVariableService = systemVariableService;
  }

  public SystemVariableService getSystemVariableService()
  {
    return systemVariableService;
  }

  public AudienceService getAudienceService()
  {
    return audienceService;
  }

  public void setAudienceService( AudienceService audienceService )
  {
    this.audienceService = audienceService;
  }

  public NodeService getNodeService()
  {
    return nodeService;
  }

  public void setNodeService( NodeService nodeService )
  {
    this.nodeService = nodeService;
  }

  public ImportFileDAO getImportFileDAO()
  {
    return importFileDAO;
  }

  public void setImportFileDAO( ImportFileDAO importFileDAO )
  {
    this.importFileDAO = importFileDAO;
  }

}

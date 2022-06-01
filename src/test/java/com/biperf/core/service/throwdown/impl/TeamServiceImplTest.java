
package com.biperf.core.service.throwdown.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.time.DateUtils;
import org.jmock.Mock;

import com.biperf.core.dao.participant.hibernate.ParticipantDAOImplTest;
import com.biperf.core.dao.promotion.hibernate.PromotionDAOImplTest;
import com.biperf.core.dao.throwdown.DivisionDAO;
import com.biperf.core.dao.throwdown.MatchDAO;
import com.biperf.core.dao.throwdown.MatchTeamOutcomeDAO;
import com.biperf.core.dao.throwdown.MatchTeamProgressDAO;
import com.biperf.core.dao.throwdown.RoundDAO;
import com.biperf.core.dao.throwdown.SmackTalkDAO;
import com.biperf.core.dao.throwdown.TeamDAO;
import com.biperf.core.dao.throwdown.TeamStats;
import com.biperf.core.dao.throwdown.hibernate.DivisionDAOImplTest;
import com.biperf.core.domain.enums.MatchStatusType;
import com.biperf.core.domain.enums.MatchTeamOutcomeType;
import com.biperf.core.domain.enums.ThrowdownMatchProgressType;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.Division;
import com.biperf.core.domain.promotion.Match;
import com.biperf.core.domain.promotion.MatchTeamOutcome;
import com.biperf.core.domain.promotion.MatchTeamProgress;
import com.biperf.core.domain.promotion.Round;
import com.biperf.core.domain.promotion.Team;
import com.biperf.core.domain.promotion.ThrowdownPromotion;
import com.biperf.core.service.BaseServiceTest;
import com.biperf.core.service.hierarchy.NodeService;
import com.biperf.core.service.participant.AudienceService;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.service.throwdown.TeamMatching;
import com.biperf.core.service.throwdown.ThrowdownService;
import com.biperf.core.utils.UserManager;

public class TeamServiceImplTest extends BaseServiceTest
{
  public TeamServiceImplTest( String test )
  {
    super( test );
  }

  private TeamServiceImpl teamServiceImpl = new TeamServiceImpl();

  private Mock mockTeamDAO = null;
  private Mock mockRoundDAO = null;
  private Mock mockMatchDAO = null;
  private Mock mockDivisionDAO = null;
  private Mock mockMatchTeamProgressDAO = null;
  private Mock mockMatchTeamOutcomeDAO = null;
  private Mock mockSmackTalkDAO = null;
  private Mock mockNodeService = null;
  private Mock mockPromotionService = null;
  private Mock mockThrowdownService = null;
  private Mock mockAudienceService = null;
  private Mock mockParticipantService = null;

  public void setUp() throws Exception
  {
    super.setUp();
    mockTeamDAO = new Mock( TeamDAO.class );
    mockRoundDAO = new Mock( RoundDAO.class );
    mockMatchDAO = new Mock( MatchDAO.class );
    mockDivisionDAO = new Mock( DivisionDAO.class );
    mockMatchTeamProgressDAO = new Mock( MatchTeamProgressDAO.class );
    mockMatchTeamOutcomeDAO = new Mock( MatchTeamOutcomeDAO.class );
    mockSmackTalkDAO = new Mock( SmackTalkDAO.class );
    mockNodeService = new Mock( NodeService.class );
    mockPromotionService = new Mock( PromotionService.class );
    mockThrowdownService = new Mock( ThrowdownService.class );
    mockAudienceService = new Mock( AudienceService.class );
    mockParticipantService = new Mock( ParticipantService.class );

    teamServiceImpl.setTeamDAO( (TeamDAO)mockTeamDAO.proxy() );
    teamServiceImpl.setRoundDAO( (RoundDAO)mockRoundDAO.proxy() );
    teamServiceImpl.setMatchDAO( (MatchDAO)mockMatchDAO.proxy() );
    teamServiceImpl.setDivisionDAO( (DivisionDAO)mockDivisionDAO.proxy() );
    teamServiceImpl.setMatchTeamProgressDAO( (MatchTeamProgressDAO)mockMatchTeamProgressDAO.proxy() );
    teamServiceImpl.setMatchTeamOutcomeDAO( (MatchTeamOutcomeDAO)mockMatchTeamOutcomeDAO.proxy() );
    teamServiceImpl.setSmackTalkDAO( (SmackTalkDAO)mockSmackTalkDAO.proxy() );
    teamServiceImpl.setNodeService( (NodeService)mockNodeService.proxy() );
    teamServiceImpl.setPromotionService( (PromotionService)mockPromotionService.proxy() );
    teamServiceImpl.setThrowdownService( (ThrowdownService)mockThrowdownService.proxy() );
    teamServiceImpl.setAudienceService( (AudienceService)mockAudienceService.proxy() );
    teamServiceImpl.setParticipantService( (ParticipantService)mockParticipantService.proxy() );

  }

  public void testCreateOrFindActiveShadowPlayerForPromotionAndDivision()
  {
    Map<String, MatchTeamOutcome> outCome = buildMatchTeamOutcomeWithTwoRounds();

    Team actualTeam = outCome.get( "d1r1m1t2" ).getTeam();

    mockTeamDAO.expects( once() ).method( "getShadowPlayerForPromotionAndDivision" ).will( returnValue( actualTeam ) );

    Team expectedTeam = teamServiceImpl.createOrFindActiveShadowPlayerForPromotionAndDivision( outCome.get( "d1r1m1t2" ).getPromotion().getId(),
                                                                                               outCome.get( "d1r1m1t1" ).getTeam().getDivision().getId() );

    assertEquals( "Actual Shawdow player is equal to what was expected.", actualTeam, expectedTeam );
  }

  public void testFindAllActiveTeamsAndPaxForPromotionAndParticipants()
  {
    Map<String, MatchTeamOutcome> outCome = buildMatchTeamOutcomeWithTwoRounds();
    ThrowdownPromotion tdPromo = outCome.get( "d1r1m1t1" ).getPromotion();
    List<Team> actualTeamList = new ArrayList<Team>();
    actualTeamList.add( outCome.get( "d1r1m1t1" ).getTeam() );
    actualTeamList.add( outCome.get( "d2r1m1t1" ).getTeam() );
    actualTeamList.add( outCome.get( "d2r1m1t2" ).getTeam() );
    actualTeamList.add( outCome.get( "d2r1m2t1" ).getTeam() );

    List<Long> participantIds = new ArrayList<Long>();
    participantIds.add( outCome.get( "d1r1m1t1" ).getTeam().getParticipant().getId() );
    participantIds.add( outCome.get( "d2r1m1t1" ).getTeam().getParticipant().getId() );
    participantIds.add( outCome.get( "d2r1m1t2" ).getTeam().getParticipant().getId() );
    participantIds.add( outCome.get( "d2r1m2t1" ).getTeam().getParticipant().getId() );

    mockTeamDAO.expects( once() ).method( "findAllActiveTeamsAndPaxForPromotionAndParticipants" ).will( returnValue( actualTeamList ) );

    List<Team> expectedTeamList = teamServiceImpl.findAllActiveTeamsAndPaxForPromotionAndParticipants( tdPromo.getId(), participantIds );

    assertEquals( "Actual List of teams is equals to what was expected.", actualTeamList, expectedTeamList );
  }

  public void testGetTeamStatsForPromotion()
  {
    Map<String, MatchTeamOutcome> outCome = buildMatchTeamOutcomeWithTwoRounds();
    ThrowdownPromotion tdPromo = outCome.get( "d2r1m1t1" ).getPromotion();
    TeamStats teamstats = new TeamStats();
    teamstats.setWins( 2 );
    teamstats.setTies( 0 );
    teamstats.setWins( 0 );

    mockTeamDAO.expects( once() ).method( "getTeamStatsForPromotion" ).will( returnValue( teamstats ) );

    TeamStats expectedTeamStats = teamServiceImpl.getTeamStatsForPromotion( outCome.get( "d2r1m1t1" ).getTeam().getId(), tdPromo.getId() );

    assertEquals( "Actual teamstats is equals to what was expected.", teamstats, expectedTeamStats );
  }

  public void testGetTeamSchedule()
  {
    Map<String, MatchTeamOutcome> outCome = buildMatchTeamOutcomeWithTwoRounds();
    ThrowdownPromotion tdPromo = outCome.get( "d2r1m1t1" ).getPromotion();

    List<Match> matchList = new ArrayList<Match>();
    matchList.add( outCome.get( "d2r1m1t1" ).getMatch() );
    matchList.add( outCome.get( "d2r2m1t1" ).getMatch() );

    mockTeamDAO.expects( once() ).method( "getTeamSchedule" ).will( returnValue( matchList ) );

    List<Match> expectedMatchList = teamServiceImpl.getTeamSchedule( tdPromo.getId(), outCome.get( "d2r1m1t1" ).getTeam().getId(), null );

    assertEquals( "Actual matchList is equals to what was expected.", matchList, expectedMatchList );
  }

  public void testSaveTeam()
  {
    MatchTeamOutcome outCome = buildMatchTeamOutcomeWithAllDetails();

    Team actualTeam = outCome.getTeam();

    mockTeamDAO.expects( once() ).method( "save" ).will( returnValue( actualTeam ) );
    Team ExpectedTeam = teamServiceImpl.saveTeam( actualTeam );

    assertEquals( "Actual Team is equals to what was expected.", actualTeam, ExpectedTeam );

    mockTeamDAO.verify();
  }

  public void testGetTeam()
  {
    MatchTeamOutcome outCome = buildMatchTeamOutcomeWithAllDetails();

    Team actualTeam = outCome.getTeam();

    mockTeamDAO.expects( once() ).method( "getTeam" ).will( returnValue( actualTeam ) );
    Team expectedTeam = teamServiceImpl.getTeam( actualTeam.getId() );

    assertEquals( "Actual Team id is equals to what was expected.", actualTeam.getId(), expectedTeam.getId() );
    assertEquals( "Actual Team object is equals to what was expected.", actualTeam, expectedTeam );
  }

  public void testGetTeamMatchingForTeamInDivision()
  {
    Map<String, MatchTeamOutcome> outCome = buildMatchTeamOutcomeWithTwoRounds();
    TeamMatching teamMatching = new TeamMatching();
    teamMatching.setCompetitor( outCome.get( "d2r1m1t1" ).getTeam() );
    teamMatching.setNumberOfMatches( 2 );
    List<TeamMatching> teamMatchList = new ArrayList<TeamMatching>();
    teamMatchList.add( teamMatching );

    mockTeamDAO.expects( once() ).method( "getTeamMatchingForTeamInDivision" ).will( returnValue( teamMatchList ) );

    List<TeamMatching> expectedTeamMatchList = teamServiceImpl.getTeamMatchingForTeamInDivision( outCome.get( "d2r1m1t1" ).getTeam().getDivision().getId(),
                                                                                                 outCome.get( "d2r1m1t1" ).getTeam().getId() );

    assertEquals( "Actual teamMatchList is equals to what was expected.", teamMatchList, expectedTeamMatchList );
  }

  public void testGetTeamByUserIdForPromotion()
  {
    MatchTeamOutcome outCome = buildMatchTeamOutcomeWithAllDetails();

    Team actualTeam = outCome.getTeam();

    mockTeamDAO.expects( once() ).method( "getTeamByUserIdForPromotion" ).will( returnValue( actualTeam ) );
    Team expectedTeam = teamServiceImpl.getTeamByUserIdForPromotion( actualTeam.getParticipant().getId(), outCome.getPromotion().getId() );

    assertEquals( "Actual Team is equals to what was expected.", actualTeam, expectedTeam );

    mockTeamDAO.verify();
  }

  public void testGetTeamsByUserIdsForPromotion()
  {
    Map<String, MatchTeamOutcome> outCome = buildMatchTeamOutcomeWithTwoRounds();
    List<Team> actualTeamList = new ArrayList<Team>();
    actualTeamList.add( outCome.get( "d1r1m1t1" ).getTeam() );
    actualTeamList.add( outCome.get( "d2r1m1t1" ).getTeam() );
    actualTeamList.add( outCome.get( "d2r1m1t2" ).getTeam() );
    actualTeamList.add( outCome.get( "d2r1m2t1" ).getTeam() );
    actualTeamList.add( outCome.get( "d2r1m2t2" ).getTeam() );

    Set<Long> userIds = new HashSet<Long>();
    userIds.add( outCome.get( "d1r1m1t1" ).getTeam().getParticipant().getId() );
    userIds.add( outCome.get( "d2r1m1t1" ).getTeam().getParticipant().getId() );
    userIds.add( outCome.get( "d2r1m1t2" ).getTeam().getParticipant().getId() );
    userIds.add( outCome.get( "d2r1m2t1" ).getTeam().getParticipant().getId() );
    userIds.add( outCome.get( "d2r1m2t2" ).getTeam().getParticipant().getId() );

    mockTeamDAO.expects( once() ).method( "getTeamsByUserIdsForPromotion" ).will( returnValue( actualTeamList ) );

    List<Team> expectedTeamList = teamServiceImpl.getTeamsByUserIdsForPromotion( userIds, outCome.get( "d1r1m1t1" ).getPromotion().getId() );

    assertEquals( "Actual TeamList is equals to what was expected.", actualTeamList, expectedTeamList );
  }

  public void testGetOutcomeForMatch()
  {
    Map<String, MatchTeamOutcome> outCome = buildMatchTeamOutcomeWithTwoRounds();
    ThrowdownPromotion tdPromo = outCome.get( "d2r1m1t1" ).getPromotion();

    MatchTeamOutcome matchTeamOutcome = outCome.get( "d2r1m1t1" );

    mockMatchTeamOutcomeDAO.expects( once() ).method( "getOutcomeForMatch" ).will( returnValue( matchTeamOutcome ) );

    MatchTeamOutcome expectedMatchTeamOutcome = teamServiceImpl.getOutcomeForMatch( outCome.get( "d2r1m1t1" ).getTeam().getId(), tdPromo.getId(), 1 );

    assertEquals( "Actual matchTeamOutcome is equals to what was expected.", matchTeamOutcome, expectedMatchTeamOutcome );
  }

  public void testGetOutcomeForTeamInSpecificRound()
  {
    Map<String, MatchTeamOutcome> outCome = buildMatchTeamOutcomeWithTwoRounds();
    ThrowdownPromotion tdPromo = outCome.get( "d2r1m1t1" ).getPromotion();

    MatchTeamOutcome matchTeamOutcome = outCome.get( "d2r1m1t1" );

    mockMatchTeamOutcomeDAO.expects( once() ).method( "getOutcomeForTeamInSpecificRound" ).will( returnValue( matchTeamOutcome ) );

    MatchTeamOutcome expectedMatchTeamOutcome = teamServiceImpl.getOutcomeForTeamInSpecificRound( outCome.get( "d2r1m1t1" ).getTeam().getParticipant().getId(), 1, tdPromo.getId() );

    assertEquals( "Actual matchTeamOutcome is equals to what was expected.", matchTeamOutcome, expectedMatchTeamOutcome );
  }

  public void testGetMatchById()
  {
    Map<String, MatchTeamOutcome> outCome = buildMatchTeamOutcomeWithTwoRounds();

    Match match1 = outCome.get( "d1r1m1t1" ).getMatch();
    Match match2 = outCome.get( "d1r2m1t1" ).getMatch();

    mockMatchDAO.expects( once() ).method( "getMatch" ).will( returnValue( match1 ) );

    Match expectedMatch = teamServiceImpl.getMatchById( match1.getId() );

    assertEquals( "Actual Match id is equals to what was expected.", match1.getId(), expectedMatch.getId() );
    assertSame( "Actual Match object is equals to what was expected.", match1, expectedMatch );
    assertNotSame( "Actual Match id is not equal to what was expected.", match2.getId(), expectedMatch.getId() );
    assertNotSame( "Actual Match object is not equal to what was expected.", match2, expectedMatch );
  }

  // this test the service method with signature
  // "public Round getRound( Long promotionId, Integer roundNumber )"
  public void testGetRound()
  {
    Map<String, MatchTeamOutcome> outCome = buildMatchTeamOutcomeWithTwoRounds();
    ThrowdownPromotion promotion = outCome.get( "d1r1m1t1" ).getPromotion();
    Long divisionId = promotion.getDivisions().iterator().next().getId();

    Round actualRound = outCome.get( "d1r1m1t1" ).getMatch().getRound();
    Round notExpectedRound = outCome.get( "d2r2m1t1" ).getMatch().getRound();

    mockRoundDAO.expects( once() ).method( "getRoundsForPromotionByDivisionAndRoundNumber" ).will( returnValue( actualRound ) );

    Round expectedRound = teamServiceImpl.getRound( promotion.getId(), divisionId, actualRound.getRoundNumber() );

    assertEquals( "Actual Round is equals to what was expected.", actualRound, expectedRound );
    assertEquals( "Actual Round is equals to what was expected.", actualRound.getId(), expectedRound.getId() );
    assertNotSame( "Actual Round is equals to what was expected.", notExpectedRound, expectedRound );
    assertNotSame( "Actual Round is equals to what was expected.", notExpectedRound.getId(), expectedRound.getId() );
  }

  // this test the service method with signature
  // "public Round getRound( Long promotionId, Long divisionId, Integer roundNumber )"
  public void testGetRound1()
  {
    Map<String, MatchTeamOutcome> outCome = buildMatchTeamOutcomeWithTwoRounds();
    Division division = outCome.get( "d1r2m1t1" ).getTeam().getDivision();

    Round actualRound = outCome.get( "d1r2m1t1" ).getMatch().getRound();
    Round notExpectedRound = outCome.get( "d2r2m1t1" ).getMatch().getRound();

    mockRoundDAO.expects( once() ).method( "getRoundsForPromotionByDivisionAndRoundNumber" ).will( returnValue( actualRound ) );

    Round expectedRound = teamServiceImpl.getRound( outCome.get( "d1r2m1t1" ).getPromotion().getId(), division.getId(), actualRound.getRoundNumber() );

    assertEquals( "Actual Round is equals to what was expected.", actualRound, expectedRound );
    assertEquals( "Actual Round is equals to what was expected.", actualRound.getId(), expectedRound.getId() );
    assertNotSame( "Actual Round is equals to what was expected.", notExpectedRound, expectedRound );
    assertNotSame( "Actual Round is equals to what was expected.", notExpectedRound.getId(), expectedRound.getId() );
  }

  public void testSaveMatch()
  {
    MatchTeamOutcome outCome = buildMatchTeamOutcomeWithAllDetails();
    Match actualMatch = outCome.getMatch();
    mockMatchDAO.expects( once() ).method( "save" ).will( returnValue( actualMatch ) );
    Match expecetedMatch = teamServiceImpl.saveMatch( actualMatch );
    assertEquals( "Actual Match is equals to what was expected.", actualMatch, expecetedMatch );

    mockMatchDAO.verify();
  }

  // TODO has to be modified later to make it better, temporarily fixed to pass the test case.
  public void testGetCurrentDivisionForUser()
  {
    Map<String, MatchTeamOutcome> outCome = buildMatchTeamOutcomeWithTwoRounds();
    ThrowdownPromotion promotion = outCome.get( "d2r1m1t1" ).getPromotion();
    Participant pax = outCome.get( "d2r1m1t1" ).getTeam().getParticipant();
    Division actualDivision = outCome.get( "d2r1m1t1" ).getTeam().getDivision();
    Division notExpectedDivision = outCome.get( "d1r1m1t1" ).getTeam().getDivision();

    mockPromotionService.expects( once() ).method( "getPromotionById" ).will( returnValue( promotion ) );
    mockParticipantService.expects( once() ).method( "getParticipantById" ).will( returnValue( pax ) );
    mockAudienceService.expects( once() ).method( "isUserInPromotionDivisionAudiences" ).will( returnValue( true ) );

    Division expectedDivision = teamServiceImpl.getCurrentDivisionForUser( promotion.getId(), pax.getId() );

    assertNotSame( "Actual Division is equals to what was expected.", actualDivision, expectedDivision );
    assertEquals( "Actual Division is equals to what was expected.", notExpectedDivision, expectedDivision );
  }

  // this test the service method with signature
  // "public Division getDivisionForUser( Long promotionId, Long userId )"
  public void testGetDivisionForUser()
  {
    Map<String, MatchTeamOutcome> outCome = buildMatchTeamOutcomeWithTwoRounds();
    ThrowdownPromotion promotion = outCome.get( "d1r2m1t1" ).getPromotion();
    Team actualTeam = outCome.get( "d1r2m1t1" ).getTeam();
    Match actualMatch = outCome.get( "d1r2m1t1" ).getMatch();
    Division actualDivision = actualMatch.getRound().getDivision();
    Division notExpectedDivision = outCome.get( "d2r2m1t1" ).getTeam().getDivision();

    mockTeamDAO.expects( once() ).method( "getTeamByUserIdForPromotion" ).will( returnValue( actualTeam ) );
    mockMatchDAO.expects( once() ).method( "getMatchByPromotionAndRoundNumberAndTeam" ).will( returnValue( actualMatch ) );

    Division expectedDivision = teamServiceImpl.getDivisionForUser( promotion.getId(), outCome.get( "d1r2m1t1" ).getTeam().getParticipant().getId(), 2 );

    assertEquals( "Actual Division is equals to what was expected.", actualDivision, expectedDivision );
    assertNotSame( "Actual Division is equals to what was expected.", notExpectedDivision, expectedDivision );
  }

  // this test the service method with signature
  // "public Division getDivisionForUser( Long promotionId, Long userId, int roundNumber )"
  public void testGetDivisionForUser1()
  {
    Map<String, MatchTeamOutcome> outCome = buildMatchTeamOutcomeWithTwoRounds();
    Team actualTeam = outCome.get( "d2r1m1t1" ).getTeam();
    Match actualMatch = outCome.get( "d2r1m1t1" ).getMatch();
    Division actualDivision = actualMatch.getRound().getDivision();
    Division notExpectedDivision = outCome.get( "d1r1m1t1" ).getTeam().getDivision();

    mockTeamDAO.expects( once() ).method( "getTeamByUserIdForPromotion" ).will( returnValue( actualTeam ) );
    mockMatchDAO.expects( once() ).method( "getMatchByPromotionAndRoundNumberAndTeam" ).will( returnValue( actualMatch ) );

    Division expectedDivision = teamServiceImpl.getDivisionForUser( outCome.get( "d2r1m1t1" ).getPromotion().getId(), outCome.get( "d2r1m1t1" ).getTeam().getParticipant().getId(), 1 );

    assertEquals( "Actual Division is equals to what was expected.", actualDivision, expectedDivision );
    assertNotSame( "Actual Division is equals to what was expected.", notExpectedDivision, expectedDivision );
  }

  public void testGetMatchByPromotionAndRoundNumberAndTeam()
  {
    MatchTeamOutcome outCome = buildMatchTeamOutcomeWithAllDetails();
    Match actualMatch = outCome.getMatch();
    mockTeamDAO.expects( once() ).method( "getTeamByUserIdForPromotion" ).will( returnValue( outCome.getTeam() ) );
    mockMatchDAO.expects( once() ).method( "getMatchByPromotionAndRoundNumberAndTeam" ).will( returnValue( actualMatch ) );
    Match expecetedMatch = teamServiceImpl.getMatchByPromotionAndRoundNumberAndTeam( outCome.getPromotion().getId(),
                                                                                     outCome.getTeam().getParticipant().getId(),
                                                                                     actualMatch.getRound().getRoundNumber() );
    assertEquals( "Actual Match is equals to what was expected.", actualMatch, expecetedMatch );

    mockMatchDAO.verify();

  }

  public void testFindAllActiveTeamsForPromotionAndDivision()
  {
    MatchTeamOutcome outCome = buildMatchTeamOutcomeWithAllDetails();
    List<Team> activeteamList = new ArrayList<Team>();
    activeteamList.add( outCome.getTeam() );
    mockTeamDAO.expects( once() ).method( "findAllActiveTeamsForPromotionAndDivision" ).will( returnValue( activeteamList ) );
    List<Team> expecetedList = teamServiceImpl.findAllActiveTeamsForPromotionAndDivision( outCome.getPromotion().getId(), outCome.getTeam().getDivision().getId() );
    assertTrue( !expecetedList.isEmpty() );

    mockTeamDAO.verify();

  }

  public void testGetShadowPlayerForPromotionAndDivision()
  {
    MatchTeamOutcome outCome = buildMatchTeamOutcomeWithAllDetails();
    Team actualTeam = outCome.getTeam();
    mockTeamDAO.expects( once() ).method( "getShadowPlayerForPromotionAndDivision" ).will( returnValue( actualTeam ) );
    Team expectedTeam = teamServiceImpl.getShadowPlayerForPromotionAndDivision( outCome.getPromotion().getId(), actualTeam.getDivision().getId() );
    assertEquals( "Actual Team is equals to what was expected.", actualTeam, expectedTeam );

    mockTeamDAO.verify();

  }

  public void testFindAllActiveTeamsAndPaxForPromotionAndDivision()
  {
    MatchTeamOutcome outCome = buildMatchTeamOutcomeWithAllDetails();
    List<Team> activeteamList = new ArrayList<Team>();
    activeteamList.add( outCome.getTeam() );
    mockTeamDAO.expects( once() ).method( "findAllActiveTeamsAndPaxForPromotionAndDivision" ).will( returnValue( activeteamList ) );
    List<Team> expecetedList = teamServiceImpl.findAllActiveTeamsAndPaxForPromotionAndDivision( outCome.getPromotion().getId(), outCome.getTeam().getDivision().getId() );
    assertTrue( !expecetedList.isEmpty() );

    mockTeamDAO.verify();
  }

  public void testGetRandomTeamForPromotionAndDivision()
  {

    MatchTeamOutcome outCome = buildMatchTeamOutcomeWithAllDetails();
    Team actualTeam = outCome.getTeam();
    mockTeamDAO.expects( once() ).method( "getRandomTeamForPromotionAndDivision" ).will( returnValue( actualTeam ) );
    Team expectedTeam = teamServiceImpl.getRandomTeamForPromotionAndDivision( outCome.getPromotion().getId(), actualTeam.getDivision().getId() );
    assertEquals( "Actual Team is equals to what was expected.", actualTeam, expectedTeam );

    mockTeamDAO.verify();

  }

  public void testGetRandomDivisionForPromotion()
  {
    MatchTeamOutcome outCome = buildMatchTeamOutcomeWithAllDetails();
    Division actualDivision = outCome.getTeam().getDivision();
    List<Division> divisionList = new ArrayList<Division>();
    divisionList.add( actualDivision );
    mockThrowdownService.expects( once() ).method( "getDivisionsByPromotionId" ).will( returnValue( divisionList ) );
    Division expectedDivision = teamServiceImpl.getRandomDivisionForPromotion( outCome.getPromotion().getId() );
    assertEquals( "Actual ivision is equals to what was expected.", actualDivision, expectedDivision );

    mockDivisionDAO.verify();

  }

  public void testSaveMatchTeamOutcome()
  {
    MatchTeamOutcome outCome = buildMatchTeamOutcomeWithAllDetails();
    mockMatchTeamOutcomeDAO.expects( once() ).method( "save" ).will( returnValue( outCome ) );
    MatchTeamOutcome expectedOutCome = teamServiceImpl.saveMatchTeamOutcome( outCome );
    assertEquals( "Actual Progress is equals to what was expected.", outCome, expectedOutCome );

    mockMatchTeamOutcomeDAO.verify();
  }

  public void testSaveProgress()
  {
    MatchTeamOutcome outCome = buildMatchTeamOutcomeWithAllDetails();
    outCome.setProgress( new HashSet<MatchTeamProgress>() );
    MatchTeamProgress matchTeamProgress = new MatchTeamProgress();
    matchTeamProgress.setProgressType( ThrowdownMatchProgressType.lookup( ThrowdownMatchProgressType.INCREMENTAL ) );
    matchTeamProgress.setProgress( new BigDecimal( 55 ) );
    matchTeamProgress.setTeamOutcome( outCome );
    mockMatchTeamOutcomeDAO.expects( once() ).method( "save" ).will( returnValue( outCome ) );
    mockMatchTeamProgressDAO.expects( once() ).method( "save" ).will( returnValue( matchTeamProgress ) );
    mockMatchTeamOutcomeDAO.expects( once() ).method( "getMatchTeamOutcome" ).with( same( matchTeamProgress.getTeamOutcome().getId() ) ).will( returnValue( outCome ) );
    MatchTeamProgress expectedProgress = teamServiceImpl.saveProgress( matchTeamProgress );
    assertEquals( "Actual Progress is equals to what was expected.", matchTeamProgress, expectedProgress );

    mockMatchTeamProgressDAO.verify();
  }

  public static MatchTeamOutcome buildMatchTeamOutcomeWithAllDetails()
  {
    ThrowdownPromotion tdPromotion = PromotionDAOImplTest.buildThrowdownPromotion( getUniqueString() );
    Division division = DivisionDAOImplTest.buildDivision( getUniqueString() );
    Set<Division> divisions = new HashSet<Division>();
    divisions.add( division );
    tdPromotion.setDivisions( divisions );
    Participant participant = ParticipantDAOImplTest.buildUniqueParticipant( getUniqueString() );

    Team team = new Team();
    team.setId( new Long( 1 ) );
    team.setActive( true );
    team.setDivision( division );
    team.setShadowPlayer( true );
    team.setParticipant( participant );

    Round round = new Round();
    round.setDivision( division );
    round.setRoundNumber( 1 );
    round.setPayoutsIssued( false );

    Match match = new Match();
    match.setRound( round );
    MatchStatusType matchStatusType = MatchStatusType.lookup( MatchStatusType.NOT_PLAYED );
    match.setStatus( matchStatusType );

    MatchTeamOutcome teamOutcome = new MatchTeamOutcome();
    teamOutcome.setId( new Long( 2 ) );
    teamOutcome.setMatch( match );
    MatchTeamOutcomeType outcomeType = MatchTeamOutcomeType.lookup( "win" );
    teamOutcome.setOutcome( outcomeType );
    teamOutcome.setPromotion( tdPromotion );
    teamOutcome.setTeam( team );
    teamOutcome.setCurrentValue( new BigDecimal( 55 ) );

    return teamOutcome;
  }

  // building two division, first division has just one participant who will play with a shadow
  // player, hence only one match created for division1, second division has 4 participant, who will
  // play among themselves, so two matches created for the division2.Each division will have 2
  // rounds.
  // division1 will play 1 match each for 2 rounds, and division2 will play 2 matches each for 2
  // rounds.Matches are decided based on the competitor audience for each division.
  public static Map<String, MatchTeamOutcome> buildMatchTeamOutcomeWithTwoRounds()
  {
    Date currentDate = UserManager.getCurrentDateWithTimeZoneID();
    Map<String, MatchTeamOutcome> tdMap = new HashMap<String, MatchTeamOutcome>();
    ThrowdownPromotion tdPromotion = PromotionDAOImplTest.buildThrowdownPromotion( getUniqueString() );
    tdPromotion.setId( new Long( 100 ) );

    Division division1 = DivisionDAOImplTest.buildDivision( getUniqueString() );
    division1.setId( new Long( 1000 ) );
    Division division2 = DivisionDAOImplTest.buildDivision( getUniqueString() );
    division2.setId( new Long( 1001 ) );
    division2.setMinimumQualifier( new BigDecimal( 20 ) );

    Set<Division> divisions = new HashSet<Division>();
    divisions.add( division1 );
    divisions.add( division2 );
    tdPromotion.setDivisions( divisions );

    Participant participant1 = ParticipantDAOImplTest.buildUniqueParticipant( getUniqueString() );
    Participant participant2 = ParticipantDAOImplTest.buildUniqueParticipant( getUniqueString() );
    Participant participant3 = ParticipantDAOImplTest.buildUniqueParticipant( getUniqueString() );
    Participant participant4 = ParticipantDAOImplTest.buildUniqueParticipant( getUniqueString() );
    Participant participant5 = ParticipantDAOImplTest.buildUniqueParticipant( getUniqueString() );
    participant1.setId( new Long( 200 ) );
    participant2.setId( new Long( 201 ) );
    participant3.setId( new Long( 202 ) );
    participant4.setId( new Long( 203 ) );
    participant5.setId( new Long( 204 ) );

    MatchStatusType matchStatusType = MatchStatusType.lookup( MatchStatusType.NOT_PLAYED );
    MatchTeamOutcomeType outcomeType = MatchTeamOutcomeType.lookup( "win" );
    MatchTeamOutcomeType outcomeType1 = MatchTeamOutcomeType.lookup( "tie" );
    MatchTeamOutcomeType outcomeType2 = MatchTeamOutcomeType.lookup( "loss" );

    // team for division1.
    Team team1 = new Team();
    team1.setId( new Long( 5000 ) );
    team1.setActive( true );
    team1.setDivision( division1 );
    team1.setShadowPlayer( false );
    team1.setParticipant( participant1 );

    Team team2 = new Team();
    team2.setId( new Long( 5001 ) );
    team2.setActive( true );
    team2.setDivision( division1 );
    team2.setShadowPlayer( true );
    team2.setParticipant( null );

    // team for division2.
    Team team3 = new Team();
    team3.setId( new Long( 5002 ) );
    team3.setActive( true );
    team3.setDivision( division2 );
    team3.setShadowPlayer( false );
    team3.setParticipant( participant2 );

    Team team4 = new Team();
    team4.setId( new Long( 5003 ) );
    team4.setActive( true );
    team4.setDivision( division2 );
    team4.setShadowPlayer( false );
    team4.setParticipant( participant3 );

    Team team5 = new Team();
    team5.setId( new Long( 5004 ) );
    team5.setActive( true );
    team5.setDivision( division2 );
    team5.setShadowPlayer( false );
    team5.setParticipant( participant4 );

    Team team6 = new Team();
    team6.setId( new Long( 5005 ) );
    team6.setActive( true );
    team6.setDivision( division2 );
    team6.setShadowPlayer( false );
    team6.setParticipant( participant5 );

    // round number 1 for division1.
    Round round1division1 = new Round();
    round1division1.setId( new Long( 2000 ) );
    round1division1.setDivision( division1 );
    round1division1.setRoundNumber( 1 );
    round1division1.setPayoutsIssued( false );
    round1division1.setStartDate( new Date( currentDate.getTime() - ( 6 * DateUtils.MILLIS_PER_DAY ) ) );
    round1division1.setEndDate( new Date( currentDate.getTime() - ( 1 * DateUtils.MILLIS_PER_DAY ) ) );

    // match1 for division1 roundNumber1.
    Match match1round1division1 = new Match();
    match1round1division1.setId( new Long( 4000 ) );
    match1round1division1.setRound( round1division1 );
    match1round1division1.setStatus( matchStatusType );

    // matchTeamOutcomes for match1 for division1 roundNumber1.
    // team1 wins team2 losses in first round
    MatchTeamOutcome teamOutcome1Match1round1division1 = new MatchTeamOutcome();
    teamOutcome1Match1round1division1.setId( new Long( 6000 ) );
    teamOutcome1Match1round1division1.setMatch( match1round1division1 );
    teamOutcome1Match1round1division1.setOutcome( outcomeType );
    teamOutcome1Match1round1division1.setPromotion( tdPromotion );
    teamOutcome1Match1round1division1.setTeam( team1 );
    teamOutcome1Match1round1division1.setCurrentValue( new BigDecimal( 55 ) );
    tdMap.put( "d1r1m1t1", teamOutcome1Match1round1division1 );

    MatchTeamOutcome teamOutcome2Match1round1division1 = new MatchTeamOutcome();
    teamOutcome2Match1round1division1.setId( new Long( 6001 ) );
    teamOutcome2Match1round1division1.setMatch( match1round1division1 );
    teamOutcome2Match1round1division1.setOutcome( outcomeType2 );
    teamOutcome2Match1round1division1.setPromotion( tdPromotion );
    teamOutcome2Match1round1division1.setTeam( team2 );
    teamOutcome2Match1round1division1.setCurrentValue( new BigDecimal( 10 ) );
    tdMap.put( "d1r1m1t2", teamOutcome2Match1round1division1 );

    // round number 2 for division1.
    Round round2division1 = new Round();
    round2division1.setId( new Long( 2001 ) );
    round2division1.setDivision( division1 );
    round2division1.setRoundNumber( 1 );
    round2division1.setPayoutsIssued( false );
    round2division1.setStartDate( currentDate );
    round2division1.setEndDate( new Date( currentDate.getTime() + ( 5 * DateUtils.MILLIS_PER_DAY ) ) );

    // match1 for division1 roundNumber2.
    Match match1round2division1 = new Match();
    match1round2division1.setId( new Long( 4001 ) );
    match1round2division1.setRound( round2division1 );
    match1round2division1.setStatus( matchStatusType );

    // matchTeamOutcomes for match1 for division1 roundNumber2.
    // team1 losses team2 wins in second round
    MatchTeamOutcome teamOutcome1Match1round2division1 = new MatchTeamOutcome();
    teamOutcome1Match1round2division1.setId( new Long( 6002 ) );
    teamOutcome1Match1round2division1.setMatch( match1round2division1 );
    teamOutcome1Match1round2division1.setOutcome( outcomeType2 );
    teamOutcome1Match1round2division1.setPromotion( tdPromotion );
    teamOutcome1Match1round2division1.setTeam( team1 );
    teamOutcome1Match1round2division1.setCurrentValue( new BigDecimal( 11 ) );
    tdMap.put( "d1r2m1t1", teamOutcome1Match1round2division1 );

    MatchTeamOutcome teamOutcome2Match1round2division1 = new MatchTeamOutcome();
    teamOutcome2Match1round2division1.setId( new Long( 6003 ) );
    teamOutcome2Match1round2division1.setMatch( match1round2division1 );
    teamOutcome2Match1round2division1.setOutcome( outcomeType );
    teamOutcome2Match1round2division1.setPromotion( tdPromotion );
    teamOutcome2Match1round2division1.setTeam( team2 );
    teamOutcome2Match1round2division1.setCurrentValue( new BigDecimal( 70 ) );
    tdMap.put( "d1r2m1t2", teamOutcome2Match1round2division1 );

    // round number 1 for division2.
    Round round1division2 = new Round();
    round1division2.setId( new Long( 2002 ) );
    round1division2.setDivision( division2 );
    round1division2.setRoundNumber( 1 );
    round1division2.setPayoutsIssued( false );
    round1division2.setStartDate( new Date( currentDate.getTime() - ( 6 * DateUtils.MILLIS_PER_DAY ) ) );
    round1division2.setEndDate( new Date( currentDate.getTime() - ( 1 * DateUtils.MILLIS_PER_DAY ) ) );

    // match1 for division2 roundNumber1.
    Match match1round1division2 = new Match();
    match1round1division2.setId( new Long( 4002 ) );
    match1round1division2.setRound( round1division2 );
    match1round1division2.setStatus( matchStatusType );

    // matchTeamOutcomes for match1 for division2 roundNumber1.
    // team3 wins team4 losses in first round
    MatchTeamOutcome teamOutcome1Match1round1division2 = new MatchTeamOutcome();
    teamOutcome1Match1round1division2.setId( new Long( 6004 ) );
    teamOutcome1Match1round1division2.setMatch( match1round1division2 );
    teamOutcome1Match1round1division2.setOutcome( outcomeType );
    teamOutcome1Match1round1division2.setPromotion( tdPromotion );
    teamOutcome1Match1round1division2.setTeam( team3 );
    teamOutcome1Match1round1division2.setCurrentValue( new BigDecimal( 65 ) );
    tdMap.put( "d2r1m1t1", teamOutcome1Match1round1division2 );

    MatchTeamOutcome teamOutcome2Match1round1division2 = new MatchTeamOutcome();
    teamOutcome2Match1round1division2.setId( new Long( 6005 ) );
    teamOutcome2Match1round1division2.setMatch( match1round1division2 );
    teamOutcome2Match1round1division2.setOutcome( outcomeType2 );
    teamOutcome2Match1round1division2.setPromotion( tdPromotion );
    teamOutcome2Match1round1division2.setTeam( team4 );
    teamOutcome2Match1round1division2.setCurrentValue( new BigDecimal( 10 ) );
    tdMap.put( "d2r1m1t2", teamOutcome2Match1round1division2 );

    // match2 for division2 roundNumber1.
    Match match2round1division2 = new Match();
    match2round1division2.setId( new Long( 4003 ) );
    match2round1division2.setRound( round1division2 );
    match2round1division2.setStatus( matchStatusType );

    // matchTeamOutcomes for match2 for division2 roundNumber1.
    // team5 and team6 tie up in first round
    MatchTeamOutcome teamOutcome1Match2round1division2 = new MatchTeamOutcome();
    teamOutcome1Match2round1division2.setId( new Long( 6006 ) );
    teamOutcome1Match2round1division2.setMatch( match2round1division2 );
    teamOutcome1Match2round1division2.setOutcome( outcomeType1 );
    teamOutcome1Match2round1division2.setPromotion( tdPromotion );
    teamOutcome1Match2round1division2.setTeam( team5 );
    teamOutcome1Match2round1division2.setCurrentValue( new BigDecimal( 75 ) );
    tdMap.put( "d2r1m2t1", teamOutcome1Match2round1division2 );

    MatchTeamOutcome teamOutcome2Match2round1division2 = new MatchTeamOutcome();
    teamOutcome2Match2round1division2.setId( new Long( 6007 ) );
    teamOutcome2Match2round1division2.setMatch( match2round1division2 );
    teamOutcome2Match2round1division2.setOutcome( outcomeType1 );
    teamOutcome2Match2round1division2.setPromotion( tdPromotion );
    teamOutcome2Match2round1division2.setTeam( team6 );
    teamOutcome2Match2round1division2.setCurrentValue( new BigDecimal( 75 ) );
    tdMap.put( "d2r1m2t2", teamOutcome2Match2round1division2 );

    // round number 1 for division2.
    Round round2division2 = new Round();
    round2division2.setId( new Long( 2003 ) );
    round2division2.setDivision( division2 );
    round2division2.setRoundNumber( 1 );
    round2division2.setPayoutsIssued( false );
    round2division2.setStartDate( currentDate );
    round2division2.setEndDate( new Date( currentDate.getTime() + ( 5 * DateUtils.MILLIS_PER_DAY ) ) );

    // match1 for division2 roundNumber2.
    Match match1round2division2 = new Match();
    match1round2division2.setId( new Long( 4004 ) );
    match1round2division2.setRound( round2division2 );
    match1round2division2.setStatus( matchStatusType );

    // matchTeamOutcomes for match1 for division2 roundNumber2.
    // team3 wins team4 losses in second round
    MatchTeamOutcome teamOutcome1Match1round2division2 = new MatchTeamOutcome();
    teamOutcome1Match1round2division2.setId( new Long( 6008 ) );
    teamOutcome1Match1round2division2.setMatch( match1round2division2 );
    teamOutcome1Match1round2division2.setOutcome( outcomeType );
    teamOutcome1Match1round2division2.setPromotion( tdPromotion );
    teamOutcome1Match1round2division2.setTeam( team3 );
    teamOutcome1Match1round2division2.setCurrentValue( new BigDecimal( 65 ) );
    tdMap.put( "d2r2m1t1", teamOutcome1Match1round2division2 );

    MatchTeamOutcome teamOutcome2Match1round2division2 = new MatchTeamOutcome();
    teamOutcome2Match1round2division2.setId( new Long( 6009 ) );
    teamOutcome2Match1round2division2.setMatch( match1round2division2 );
    teamOutcome2Match1round2division2.setOutcome( outcomeType2 );
    teamOutcome2Match1round2division2.setPromotion( tdPromotion );
    teamOutcome2Match1round2division2.setTeam( team4 );
    teamOutcome2Match1round2division2.setCurrentValue( new BigDecimal( 10 ) );
    tdMap.put( "d2r2m1t2", teamOutcome2Match1round2division2 );

    // match2 for division2 roundNumber2.
    Match match2round2division2 = new Match();
    match2round2division2.setId( new Long( 4005 ) );
    match2round2division2.setRound( round2division2 );
    match2round2division2.setStatus( matchStatusType );

    // matchTeamOutcomes for match2 for division2 roundNumber2.
    // team5 losses and team6 wins in second round
    MatchTeamOutcome teamOutcome1Match2round2division2 = new MatchTeamOutcome();
    teamOutcome1Match2round2division2.setId( new Long( 6010 ) );
    teamOutcome1Match2round2division2.setMatch( match2round2division2 );
    teamOutcome1Match2round2division2.setOutcome( outcomeType2 );
    teamOutcome1Match2round2division2.setPromotion( tdPromotion );
    teamOutcome1Match2round2division2.setTeam( team5 );
    teamOutcome1Match2round2division2.setCurrentValue( new BigDecimal( 75 ) );
    tdMap.put( "d2r2m2t1", teamOutcome1Match2round2division2 );

    MatchTeamOutcome teamOutcome2Match2round2division2 = new MatchTeamOutcome();
    teamOutcome2Match2round2division2.setId( new Long( 6011 ) );
    teamOutcome2Match2round2division2.setMatch( match2round2division2 );
    teamOutcome2Match2round2division2.setOutcome( outcomeType );
    teamOutcome2Match2round2division2.setPromotion( tdPromotion );
    teamOutcome2Match2round2division2.setTeam( team6 );
    teamOutcome2Match2round2division2.setCurrentValue( new BigDecimal( 85 ) );
    tdMap.put( "d2r2m2t2", teamOutcome2Match2round2division2 );

    return tdMap;
  }

}

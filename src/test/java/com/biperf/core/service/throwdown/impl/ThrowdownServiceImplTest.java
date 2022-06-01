
package com.biperf.core.service.throwdown.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jmock.Mock;

import com.biperf.core.dao.activity.ActivityDAO;
import com.biperf.core.dao.throwdown.DivisionDAO;
import com.biperf.core.dao.throwdown.MatchDAO;
import com.biperf.core.dao.throwdown.MatchTeamOutcomeDAO;
import com.biperf.core.dao.throwdown.RoundDAO;
import com.biperf.core.dao.throwdown.TeamDAO;
import com.biperf.core.domain.AuditCreateInfo;
import com.biperf.core.domain.activity.StackStandingActivity;
import com.biperf.core.domain.enums.AchievementPrecision;
import com.biperf.core.domain.enums.TeamUnavailableResolverType;
import com.biperf.core.domain.enums.ThrowdownAchievementPrecision;
import com.biperf.core.domain.enums.ThrowdownRoundingMethod;
import com.biperf.core.domain.gamification.Badge;
import com.biperf.core.domain.journal.Journal;
import com.biperf.core.domain.promotion.Division;
import com.biperf.core.domain.promotion.Match;
import com.biperf.core.domain.promotion.MatchTeamOutcome;
import com.biperf.core.domain.promotion.Round;
import com.biperf.core.domain.promotion.StackStanding;
import com.biperf.core.domain.promotion.StackStandingPayout;
import com.biperf.core.domain.promotion.StackStandingPayoutGroup;
import com.biperf.core.domain.promotion.ThrowdownPromotion;
import com.biperf.core.service.BaseServiceTest;
import com.biperf.core.service.gamification.GamificationService;
import com.biperf.core.service.hierarchy.HierarchyService;
import com.biperf.core.service.journal.JournalService;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.service.throwdown.MatchResolution;
import com.biperf.core.service.throwdown.StackRankingCalculationResult;
import com.biperf.core.service.throwdown.StackStandingService;
import com.biperf.core.service.throwdown.ThrowdownService;
import com.biperf.core.utils.GuidUtils;
import com.biperf.core.value.PromotionRoundValue;

public class ThrowdownServiceImplTest extends BaseServiceTest
{

  public ThrowdownServiceImplTest( String test )
  {
    super( test );
  }

  private ThrowdownServiceImpl throwdownServiceImpl = new ThrowdownServiceImpl();

  private Mock mockUserService = null;
  private Mock mockPromotionService = null;
  private Mock mockParticipantService = null;
  private Mock mockHierarchyService = null;
  private Mock mockStackStandingService = null;
  private Mock mockGamificationService = null;
  private Mock mockThrowdownService = null;
  private Mock mockJournalService = null;

  private Mock mockDivisionDAO = null;
  private Mock mockRoundDAO = null;
  private Mock mockMatchDAO = null;
  private Mock mockActivityDAO = null;
  private Mock mockTeamDAO = null;
  private Mock mockMatchTeamOutcomeDAO = null;

  protected void setUp() throws Exception
  {
    super.setUp();
    mockDivisionDAO = new Mock( DivisionDAO.class );
    mockRoundDAO = new Mock( RoundDAO.class );
    mockMatchDAO = new Mock( MatchDAO.class );
    mockActivityDAO = new Mock( ActivityDAO.class );
    mockTeamDAO = new Mock( TeamDAO.class );
    mockMatchTeamOutcomeDAO = new Mock( MatchTeamOutcomeDAO.class );

    mockUserService = new Mock( UserService.class );
    mockPromotionService = new Mock( PromotionService.class );
    mockParticipantService = new Mock( ParticipantService.class );
    mockHierarchyService = new Mock( HierarchyService.class );
    mockStackStandingService = new Mock( StackStandingService.class );
    mockGamificationService = new Mock( GamificationService.class );
    mockThrowdownService = new Mock( ThrowdownService.class );
    mockJournalService = new Mock( JournalService.class );

    throwdownServiceImpl.setDivisionDAO( (DivisionDAO)mockDivisionDAO.proxy() );
    throwdownServiceImpl.setRoundDAO( (RoundDAO)mockRoundDAO.proxy() );
    throwdownServiceImpl.setMatchDAO( (MatchDAO)mockMatchDAO.proxy() );
    throwdownServiceImpl.setActivityDAO( (ActivityDAO)mockActivityDAO.proxy() );
    throwdownServiceImpl.setTeamDAO( (TeamDAO)mockTeamDAO.proxy() );
    throwdownServiceImpl.setMatchTeamOutcomeDAO( (MatchTeamOutcomeDAO)mockMatchTeamOutcomeDAO.proxy() );

    throwdownServiceImpl.setUserService( (UserService)mockUserService.proxy() );
    throwdownServiceImpl.setPromotionService( (PromotionService)mockPromotionService.proxy() );
    throwdownServiceImpl.setParticipantService( (ParticipantService)mockParticipantService.proxy() );
    throwdownServiceImpl.setHierarchyService( (HierarchyService)mockHierarchyService.proxy() );
    throwdownServiceImpl.setStackStandingService( (StackStandingService)mockStackStandingService.proxy() );
    throwdownServiceImpl.setGamificationService( (GamificationService)mockGamificationService.proxy() );
    throwdownServiceImpl.setJournalService( (JournalService)mockJournalService.proxy() );
  }

  public void testGetThrowdownPromotion()
  {
    MatchTeamOutcome teamOutcome = TeamServiceImplTest.buildMatchTeamOutcomeWithAllDetails();
    ThrowdownPromotion actualPromotion = teamOutcome.getPromotion();
    mockPromotionService.expects( once() ).method( "getPromotionById" ).with( same( actualPromotion.getId() ) ).will( returnValue( actualPromotion ) );
    ThrowdownPromotion expectPromtion = throwdownServiceImpl.getThrowdownPromotion( actualPromotion.getId() );
    assertEquals( "Actual Promotion is equals to what was expected.", actualPromotion, expectPromtion );
  }

  public void testGetThrowdownRoundsForPromotionByRoundNumber()
  {
    MatchTeamOutcome teamOutcome = TeamServiceImplTest.buildMatchTeamOutcomeWithAllDetails();
    List<Round> roundList = new ArrayList<Round>();
    roundList.add( teamOutcome.getMatch().getRound() );
    mockRoundDAO.expects( once() ).method( "getRoundsForPromotionByRoundNumber" ).will( returnValue( roundList ) );
    List<Round> expectedRounds = throwdownServiceImpl.getThrowdownRoundsForPromotionByRoundNumber( teamOutcome.getPromotion().getId(), teamOutcome.getMatch().getRound().getRoundNumber() );
    assertTrue( !expectedRounds.isEmpty() );

    mockRoundDAO.verify();
  }

  public void testGetDivisionsByPromotionId()
  {
    MatchTeamOutcome teamOutcome = TeamServiceImplTest.buildMatchTeamOutcomeWithAllDetails();
    List<Division> divisionList = new ArrayList<Division>();
    divisionList.addAll( teamOutcome.getPromotion().getDivisions() );
    mockDivisionDAO.expects( once() ).method( "getDivisionsByPromotionId" ).will( returnValue( divisionList ) );
    List<Division> expectedDivisions = throwdownServiceImpl.getDivisionsByPromotionId( teamOutcome.getPromotion().getId() );
    assertTrue( !expectedDivisions.isEmpty() );

    mockDivisionDAO.verify();
  }

  public void testGetRoundsByDivision()
  {
    MatchTeamOutcome teamOutcome = TeamServiceImplTest.buildMatchTeamOutcomeWithAllDetails();
    List<Round> roundList = new ArrayList<Round>();
    roundList.add( teamOutcome.getMatch().getRound() );
    mockRoundDAO.expects( once() ).method( "getRoundsByDivision" ).will( returnValue( roundList ) );
    List<Round> expectedRounds = throwdownServiceImpl.getRoundsByDivision( teamOutcome.getTeam().getDivision().getId() );
    assertTrue( !expectedRounds.isEmpty() );

    mockRoundDAO.verify();
  }

  public void testGetThrowdownMatchesByPromotionAndRoundNumber()
  {
    MatchTeamOutcome teamOutcome = TeamServiceImplTest.buildMatchTeamOutcomeWithAllDetails();
    List<Match> matchList = new ArrayList<Match>();
    matchList.add( teamOutcome.getMatch() );
    mockMatchDAO.expects( once() ).method( "getMatchesByPromotionAndRoundNumber" ).will( returnValue( matchList ) );
    List<Match> expectedMatches = throwdownServiceImpl.getThrowdownMatchesByPromotionAndRoundNumber( teamOutcome.getPromotion().getId(), teamOutcome.getMatch().getRound().getRoundNumber(), null );
    assertTrue( !expectedMatches.isEmpty() );

    mockRoundDAO.verify();
  }

  public void testGetRoundsForProgressLoad()
  {
    MatchTeamOutcome teamOutcome = TeamServiceImplTest.buildMatchTeamOutcomeWithAllDetails();
    ThrowdownPromotion promotion = teamOutcome.getPromotion();
    Set<PromotionRoundValue> actualPromoValues = new HashSet<PromotionRoundValue>();
    boolean roundStarted = true;
    boolean isRoundPaidForDivisionPayouts = true;
    StackStanding actualStackStanding = StackStandingServiceImplTest.buildStackStanding();
    mockPromotionService.expects( once() ).method( "getPromotionById" ).with( same( promotion.getId() ) ).will( returnValue( promotion ) );
    mockRoundDAO.expects( atLeastOnce() ).method( "isRoundStarted" ).will( returnValue( roundStarted ) );
    mockRoundDAO.expects( atLeastOnce() ).method( "isRoundPaidForDivisionPayouts" ).will( returnValue( isRoundPaidForDivisionPayouts ) );
    mockStackStandingService.expects( atLeastOnce() ).method( "getUnapprovedRankingForPromotionAndRound" ).will( returnValue( actualStackStanding ) );

    Set<PromotionRoundValue> expectedPromoValues = throwdownServiceImpl.getRoundsForProgressLoad( teamOutcome.getPromotion().getId() );
    assertTrue( !expectedPromoValues.isEmpty() );

    mockThrowdownService.verify();
  }

  public void testResolveMatch()
  {
    MatchTeamOutcome teamOutcome1 = TeamServiceImplTest.buildMatchTeamOutcomeWithAllDetails();
    MatchTeamOutcome teamOutcome2 = TeamServiceImplTest.buildMatchTeamOutcomeWithAllDetails();
    ThrowdownPromotion promotion = teamOutcome1.getPromotion();
    promotion.setRoundingMethod( ThrowdownRoundingMethod.lookup( ThrowdownRoundingMethod.STANDARD ) );
    promotion.setTeamUnavailableResolverType( TeamUnavailableResolverType.lookup( TeamUnavailableResolverType.CALCULATED_AVERAGE_EXCLUSIVE ) );
    promotion.setAchievementPrecision( ThrowdownAchievementPrecision.lookup( AchievementPrecision.THREE ) );
    Round round = teamOutcome1.getMatch().getRound();
    round.getDivision().setPromotion( promotion );
    teamOutcome1.getMatch().setRound( round );
    teamOutcome2.getMatch().setRound( round );
    teamOutcome1.setPromotion( promotion );
    teamOutcome2.setPromotion( promotion );
    teamOutcome1.setProgress( new HashSet<>() );
    teamOutcome2.setProgress( new HashSet<>() );
    BigDecimal average = new BigDecimal( 658 );
    mockRoundDAO.expects( once() ).method( "getRoundById" ).will( returnValue( round ) );
    mockRoundDAO.expects( once() ).method( "getCalculatedAverageForRound" ).will( returnValue( average ) );

    MatchResolution matchResolution = throwdownServiceImpl.resolveMatch( teamOutcome1, teamOutcome2 );
    assertTrue( "Expected match resolution is not null ", matchResolution != null );
    mockThrowdownService.verify();

  }

  public void testBuildStackRankingCalculationResult()
  {
    StackStanding stackStand = StackStandingServiceImplTest.buildStackStanding();
    ThrowdownPromotion promotion = stackStand.getPromotion();
    StackStandingPayoutGroup stackStandingPayoutGroup = new StackStandingPayoutGroup();

    AuditCreateInfo auditCreateInfo = new AuditCreateInfo();
    auditCreateInfo.setCreatedBy( new Long( 5662 ) );
    auditCreateInfo.setDateCreated( new Timestamp( new Date().getTime() ) );

    stackStandingPayoutGroup.setId( new Long( 1 ) );
    stackStandingPayoutGroup.setPromotion( promotion );
    stackStandingPayoutGroup.setAuditCreateInfo( auditCreateInfo );
    stackStandingPayoutGroup.setVersion( new Long( 1 ) );
    stackStandingPayoutGroup.setNodeType( stackStand.getStackStandingNodes().iterator().next().getNode().getNodeType() );

    StackStandingPayout stackStandingPayout = new StackStandingPayout();
    stackStandingPayout.setId( new Long( 10 ) );
    stackStandingPayout.setStackStandingPayoutGroup( stackStandingPayoutGroup );
    stackStandingPayout.setStartStanding( 1 );
    stackStandingPayout.setEndStanding( 5 );
    stackStandingPayout.setPayout( 123 );
    stackStandingPayout.setAuditCreateInfo( auditCreateInfo );
    stackStandingPayout.setVersion( new Long( 1 ) );

    stackStandingPayoutGroup.addStackStandingPayout( stackStandingPayout );
    promotion.addStackStandingPayoutGroup( stackStandingPayoutGroup );

    StackStandingActivity rankingActivity = new StackStandingActivity( GuidUtils.generateGuid() );
    Journal journal = new Journal();
    mockStackStandingService.expects( once() ).method( "getUnapprovedRankingForPromotionAndRound" ).will( returnValue( stackStand ) );
    mockPromotionService.expects( once() ).method( "getPromotionByIdWithAssociations" ).will( returnValue( promotion ) );
    mockActivityDAO.expects( once() ).method( "saveActivity" ).will( returnValue( rankingActivity ) );
    mockJournalService.expects( once() ).method( "saveAndLaunchPointsDepositProcess" );
    mockGamificationService.expects( once() ).method( "getBadgeByPromotion" ).will( returnValue( new ArrayList<Badge>() ) );
    StackRankingCalculationResult calculationResult = throwdownServiceImpl.getRankingAwardSummaryForRound( promotion.getId(), stackStand.getRoundNumber() );

    assertTrue( "StackRankingCalculationResult is not null ", calculationResult != null );

  }

}

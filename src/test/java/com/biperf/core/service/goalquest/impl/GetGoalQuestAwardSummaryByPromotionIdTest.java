
package com.biperf.core.service.goalquest.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.easymock.EasyMock;
import org.junit.Ignore;
import org.junit.Test;

import com.biperf.core.dao.participant.ParticipantPartnerDAO;
import com.biperf.core.domain.enums.HierarchyRoleType;
import com.biperf.core.domain.enums.ManagerOverrideStructure;
import com.biperf.core.domain.enums.PartnerAudienceType;
import com.biperf.core.domain.enums.PartnerEarnings;
import com.biperf.core.domain.enums.PayoutStructure;
import com.biperf.core.domain.enums.PrimaryAudienceType;
import com.biperf.core.domain.enums.PromotionAwardsType;
import com.biperf.core.domain.goalquest.GoalQuestAwardSummary;
import com.biperf.core.domain.goalquest.PaxGoal;
import com.biperf.core.domain.goalquest.PendingGoalQuestAwardSummary;
import com.biperf.core.domain.hierarchy.Hierarchy;
import com.biperf.core.domain.hierarchy.Node;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.participant.ParticipantPartner;
import com.biperf.core.domain.promotion.AbstractGoalLevel;
import com.biperf.core.domain.promotion.GoalLevel;
import com.biperf.core.domain.promotion.GoalQuestPromotion;
import com.biperf.core.domain.promotion.ManagerOverrideGoalLevel;
import com.biperf.core.domain.user.UserNode;
import com.biperf.core.exception.BeaconRuntimeException;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.promotion.engine.GoalCalculationResult;
import com.biperf.core.service.promotion.engine.GoalPayoutStrategy;
import com.biperf.core.service.promotion.engine.ManagerOverrideGoalStrategy;
import com.biperf.core.service.promotion.engine.PartnerGoalStrategy;
import com.biperf.core.service.promotion.engine.PartnerGoalStrategyFactory;
import com.biperf.core.value.FormattedValueBean;

public class GetGoalQuestAwardSummaryByPromotionIdTest extends BaseGQTest
{
  Long testPromoId = null;
  GoalQuestPromotion promotion = null;
  List<PaxGoal> goalList = null;
  List<GoalCalculationResult> expectedManagerGQAwardSummaryList = null;
  List<GoalQuestAwardSummary> expectedParticipantGQAwardSummaryList = null;
  List<GoalQuestAwardSummary> expectedPartnerGQAwardSummaryList = null;
  List<GoalCalculationResult> expectedParticipantGQResults = null;
  GoalPayoutStrategy mockGoalPayoutStrategy = null;

  @Override
  protected void setUp() throws Exception
  {
    super.setUp();

    // set the input id here, because changing it doesn't affect anything while testing.
    testPromoId = 1L;
    // Cook up a promotion. various attributes of this will be modified in tests
    // to exercise different code paths through the function.
    promotion = new GoalQuestPromotion();

    goalList = new ArrayList<PaxGoal>();
    expectedManagerGQAwardSummaryList = new ArrayList<GoalCalculationResult>();
    expectedParticipantGQAwardSummaryList = new ArrayList<GoalQuestAwardSummary>();
    expectedPartnerGQAwardSummaryList = new ArrayList<GoalQuestAwardSummary>();
    expectedParticipantGQResults = new ArrayList<GoalCalculationResult>();
    mockGoalPayoutStrategy = mockControl.createMock( GoalPayoutStrategy.class );
  }

  /**
   * <b>Goal:</b> Confirm behavior of the method when the goal list is empty
   * <br/><br/>
   * <b>Setup:</b> Mock out the service calls for initialization, and don't add
   * any members to goalList.
   * <br/><br/>
   * <b>Expected Behavior:</b> The bulk of the method should be skipped,
   * returning a PendingGoalQuestAwardSummary that is populated with empty
   * collections.
   */
  @Test
  public void testGoalListEmpty()
  {
    EasyMock.expect( mockPromotionService.getPromotionById( testPromoId ) ).andReturn( promotion );
    EasyMock.expect( mockPaxGoalService.getPaxGoalByPromotionId( EasyMock.eq( testPromoId ), (AssociationRequestCollection)EasyMock.anyObject() ) ).andReturn( goalList );
    // We tested this earlier, so I threw the related expect() calls in a helper.
    MockGetEligibleParticipantsForPromotion();

    mockControl.replay();
    PendingGoalQuestAwardSummary actual = testInstance.getGoalQuestAwardSummaryByPromotionId( testPromoId );
    mockControl.verify();
    assertTrue( expectedManagerGQAwardSummaryList.equals( actual.getManagerGoalQuestAwardSummaryList() ) );
    assertTrue( expectedParticipantGQAwardSummaryList.equals( actual.getParticipantGoalQuestAwardSummaryList() ) );
    assertTrue( expectedPartnerGQAwardSummaryList.equals( actual.getPartnerGoalQuestAwardSummaryList() ) );
    assertTrue( expectedParticipantGQResults.equals( actual.getParticipantGQResults() ) );

  }

  /**
   * <b>Goal:</b> Confirm behavior of the method when a participant is not eligible
   * for payout
   * <b>Setup:</b> Add a PaxGoal with an ineligible user to the goalList, initialize
   * and mock the necessary parts to skip/slide through the other conditionals.
   * <br/><br/>
   * <b>Expected Behavior:</b> The while loop should be skipped (the only
   * iteration will hit a continue right away because the pax is ineligible).
   * If we are wrong, it should fail with a null pointer.
   */
  @Test
  public void testPaxIneligibleForPayout()
  {
    PaxGoal goal1 = new PaxGoal();
    Participant pax1 = new Participant();
    pax1.setActive( false );
    goal1.setParticipant( pax1 );

    // Can't avoid these getting checked in the last if/else
    PromotionAwardsType awardsType = PromotionAwardsType.lookup( PromotionAwardsType.POINTS );
    PayoutStructure payoutStructure = PayoutStructure.lookup( PayoutStructure.BOTH );
    promotion.setAwardType( awardsType );
    promotion.setPayoutStructure( payoutStructure );

    goalList.add( goal1 );
    EasyMock.expect( mockPromotionService.getPromotionById( testPromoId ) ).andReturn( promotion );
    EasyMock.expect( mockPaxGoalService.getPaxGoalByPromotionId( EasyMock.eq( testPromoId ), (AssociationRequestCollection)EasyMock.anyObject() ) ).andReturn( goalList );
    // We tested this earlier, so I threw the related expect() calls in a helper.
    MockGetEligibleParticipantsForPromotion();

    EasyMock.expect( mockGoalPayoutStrategyFactory.getGoalPayoutStrategy( PayoutStructure.BOTH ) ).andReturn( mockGoalPayoutStrategy );

    // We aren't testing this part now, so just get past it with known results.
    EasyMock.expect( mockGoalPayoutStrategy.summarizeResults( (Map)EasyMock.anyObject() ) ).andReturn( expectedParticipantGQAwardSummaryList );

    mockControl.replay();
    PendingGoalQuestAwardSummary actual = testInstance.getGoalQuestAwardSummaryByPromotionId( testPromoId );
    mockControl.verify();
    assertTrue( expectedManagerGQAwardSummaryList.equals( actual.getManagerGoalQuestAwardSummaryList() ) );
    assertTrue( expectedParticipantGQAwardSummaryList.equals( actual.getParticipantGoalQuestAwardSummaryList() ) );
    assertTrue( expectedPartnerGQAwardSummaryList.equals( actual.getPartnerGoalQuestAwardSummaryList() ) );
    assertTrue( expectedParticipantGQResults.equals( actual.getParticipantGQResults() ) );
  }

  /**
   * <b>Goal:</b> Confirm the behavior when a participant is eligible for payout
   * and a manager.
   * <br/><br/>
   * <b>Setup:</b> Create a PaxGoal and add a user to it that owns one or more
   * nodes, indicating manager status. Add the PaxGoal to the list of
   * PaxGoals for the promotion we are looking up. Give the promotion
   * a ManagerOverrideStructure.
   * <br/><br/>
   * <b>Expected Behavior:</b> The goal will be added to the managerGoals list,
   * and because an override is set calls will be made to process the
   * override and populate the ManagerGoalQuestAwardSummaryList attribute
   * of the returned PendingGoalQuestAwardSummary.
   */
  @Test
  public void testPaxEligibleAndIsManager()
  {
    /////// Basic setup////////
    PaxGoal goal1 = new PaxGoal();
    Participant pax1 = new Participant();
    PromotionAwardsType awardsType = PromotionAwardsType.lookup( PromotionAwardsType.POINTS );
    PayoutStructure payoutStructure = PayoutStructure.lookup( PayoutStructure.BOTH );
    promotion.setAwardType( awardsType );
    promotion.setPayoutStructure( payoutStructure );

    ////// Test specific setup////////
    pax1.setActive( true );
    pax1.setId( 1L );
    // Give it a node, since that's what we are testing here.
    UserNode node1 = new UserNode();
    node1.setHierarchyRoleType( HierarchyRoleType.lookup( HierarchyRoleType.OWNER ) );
    pax1.addUserNode( node1 );
    goal1.setParticipant( pax1 );
    ManagerOverrideStructure overrideStructure = ManagerOverrideStructure.lookup( ManagerOverrideStructure.AWARD_PER_ACHIEVER );
    promotion.setOverrideStructure( overrideStructure );
    goalList.add( goal1 );
    ManagerOverrideGoalStrategy mockManagerOverrideGoalStrategy = mockControl.createMock( ManagerOverrideGoalStrategy.class );
    // add something to the ManagerGQAwardsummaryList so we can confirm that it is updated
    expectedManagerGQAwardSummaryList.add( new GoalCalculationResult() );

    /////// Expectations///////
    EasyMock.expect( mockPromotionService.getPromotionById( testPromoId ) ).andReturn( promotion );

    EasyMock.expect( mockPaxGoalService.getPaxGoalByPromotionId( EasyMock.eq( testPromoId ), (AssociationRequestCollection)EasyMock.anyObject() ) ).andReturn( goalList );

    MockGetEligibleParticipantsForPromotion();

    // We are expecting these two calls to be triggered because the
    // promotion has an override structure set.
    EasyMock.expect( mockManagerOverrideGoalStrategyFactory.getManagerOverrideGoalStrategy( overrideStructure.getCode() ) ).andReturn( mockManagerOverrideGoalStrategy );
    EasyMock.expect( mockManagerOverrideGoalStrategy.summarizeResults( promotion, new ArrayList<GoalCalculationResult>(), goalList ) ).andReturn( expectedManagerGQAwardSummaryList );

    // These get called no matter what, but don't relate to this test.
    EasyMock.expect( mockGoalPayoutStrategyFactory.getGoalPayoutStrategy( PayoutStructure.BOTH ) ).andReturn( mockGoalPayoutStrategy );
    EasyMock.expect( mockGoalPayoutStrategy.summarizeResults( (Map)EasyMock.anyObject() ) ).andReturn( expectedParticipantGQAwardSummaryList );

    /////// Call being tested///////
    mockControl.replay();
    PendingGoalQuestAwardSummary actual = testInstance.getGoalQuestAwardSummaryByPromotionId( testPromoId );
    mockControl.verify();
    /////// Postconditions////////
    assertTrue( expectedManagerGQAwardSummaryList.equals( actual.getManagerGoalQuestAwardSummaryList() ) );
    assertTrue( expectedParticipantGQAwardSummaryList.equals( actual.getParticipantGoalQuestAwardSummaryList() ) );
    assertTrue( expectedPartnerGQAwardSummaryList.equals( actual.getPartnerGoalQuestAwardSummaryList() ) );
    assertTrue( expectedParticipantGQResults.equals( actual.getParticipantGQResults() ) );
  }

  /**
   * <b>Goal:</b> Confirm that the method properly throws a BeaconRunTimeException
   * when the goal is a ManagerOverrideGoal
   * <br/><br/>
   * <b>Setup:</b> Create a PaxGoal, give it an active, eligible participant,
   * and set it's level to be a ManagerOverrideGoalLevel.
   * <br/><br/>
   * <b>Expected Behavior:</b> A BeaconRunTimeException will be thrown.
   */
  @Test
  public void testGoalIsManagerOverrideGoal()
  {
    PaxGoal goal1 = new PaxGoal();
    Participant pax1 = new Participant();
    pax1.setActive( true );
    pax1.setId( 1L );

    goal1.setGoalLevel( new ManagerOverrideGoalLevel() );
    goal1.setParticipant( pax1 );
    goalList.add( goal1 );

    EasyMock.expect( mockPromotionService.getPromotionById( testPromoId ) ).andReturn( promotion );

    EasyMock.expect( mockPaxGoalService.getPaxGoalByPromotionId( EasyMock.eq( testPromoId ), (AssociationRequestCollection)EasyMock.anyObject() ) ).andReturn( goalList );

    MockGetEligibleParticipantsForPromotion();

    mockControl.replay();
    boolean pass = false;
    try
    {
      testInstance.getGoalQuestAwardSummaryByPromotionId( testPromoId );
    }
    catch( BeaconRuntimeException exception )
    {
      pass = true;
    }
    finally
    {
      assertTrue( "BeaconRuntimeException not thrown", pass );
      mockControl.verify();
    }
  }

  /**
   * <b>Goal:</b> Test behavior of the block of calculations that is 
   * performed if a paxGoal has a valid goalLevel (non-null, not a 
   * ManagerOverrideGoalLevel) when 'userNodes' is null, but 
   * 'goalCalculationResult' has value.
   * <br/><br/>
   * <b>Setup:</b> Create a paxGoal with an eligible participant and a non-null
   * goal level that is not a ManagerOverrideGoalLevel.
   * <br/><br/>
   * <b>Expected Behavior:</b> a call to addOrIncrementTotals will be made, and
   * participantGQResults will be updated.
   */
  @Test
  public void testValidGoalLevelAndCalculationResult()
  {
    /////// Basic setup////////
    PaxGoal goal1 = new PaxGoal();
    Participant pax1 = new Participant();
    pax1.setActive( true );
    pax1.setId( 1L );
    pax1.setUserNodes( new HashSet<UserNode>() );
    goal1.setParticipant( pax1 );
    PromotionAwardsType awardsType = PromotionAwardsType.lookup( PromotionAwardsType.POINTS );
    PayoutStructure payoutStructure = PayoutStructure.lookup( PayoutStructure.BOTH );
    promotion.setAwardType( awardsType );
    promotion.setPayoutStructure( payoutStructure );

    ////// Test specific setup////////
    GoalLevel level = new GoalLevel();
    level.setId( 2L );

    Set<AbstractGoalLevel> goalLevels = new HashSet<AbstractGoalLevel>();
    goalLevels.add( level );
    promotion.setGoalLevels( goalLevels );
    goal1.setGoalLevel( level );
    goal1.setGoalQuestPromotion( promotion );
    goalList.add( goal1 );

    // add something to the participantsGQResults list so we can confirm that it gets updated
    GoalCalculationResult goalCalculationRes = new GoalCalculationResult();
    expectedParticipantGQResults.add( goalCalculationRes );

    EasyMock.expect( mockPromotionService.getPromotionById( testPromoId ) ).andReturn( promotion );

    EasyMock.expect( mockPaxGoalService.getPaxGoalByPromotionId( EasyMock.eq( testPromoId ), (AssociationRequestCollection)EasyMock.anyObject() ) ).andReturn( goalList );

    MockGetEligibleParticipantsForPromotion();

    EasyMock.expect( mockGoalPayoutStrategyFactory.getGoalPayoutStrategy( PayoutStructure.BOTH ) ).andReturn( mockGoalPayoutStrategy );
    // This call should be made to update the participantGQResults in
    // GoalQuestServiceImpl.addOrIncrementTotals()
    EasyMock.expect( mockGoalPayoutStrategy.processGoal( goal1 ) ).andReturn( goalCalculationRes );

    // These get called no matter what, but don't relate to this test.
    EasyMock.expect( mockGoalPayoutStrategyFactory.getGoalPayoutStrategy( PayoutStructure.BOTH ) ).andReturn( mockGoalPayoutStrategy );
    EasyMock.expect( mockGoalPayoutStrategy.summarizeResults( (Map)EasyMock.anyObject() ) ).andReturn( expectedParticipantGQAwardSummaryList );

    /////// Call being tested///////
    mockControl.replay();
    PendingGoalQuestAwardSummary actual = testInstance.getGoalQuestAwardSummaryByPromotionId( testPromoId );
    mockControl.verify();
    /////// Postconditions////////
    assertTrue( expectedManagerGQAwardSummaryList.equals( actual.getManagerGoalQuestAwardSummaryList() ) );
    assertTrue( expectedParticipantGQAwardSummaryList.equals( actual.getParticipantGoalQuestAwardSummaryList() ) );
    assertTrue( expectedPartnerGQAwardSummaryList.equals( actual.getPartnerGoalQuestAwardSummaryList() ) );
    assertTrue( expectedParticipantGQResults.equals( actual.getParticipantGQResults() ) );
  }

  /**
   * <b>Goal:</b> Confirm behavior when there is a paxGoal with an eligible
   * participant and valid goal, but null calculationResult.
   * <br/><br/>
   * <b>Setup:</b> Create a PaxGoal, give it an active, eligible participant,
   * give it a valid GoalLevel, and set it up so addOrIncrementTotals will return null
   * <br/><br/>
   * <b>Expected Behavior:</b> The block to add the goalCalculationResul to the
   * participantGQResults should be skipped.
   * 
   * TODO: As far as I can see, addOrIncrementTotals can't return null, it would raise a NullPointerException first.
   */
  public void testValidGoalLevelAndNullResult()
  {
    /////// Basic setup////////
    PaxGoal goal1 = new PaxGoal();
    Participant pax1 = new Participant();
    pax1.setActive( true );
    pax1.setId( 1L );
    pax1.setUserNodes( new HashSet<UserNode>() );
    goal1.setParticipant( pax1 );
    PromotionAwardsType awardsType = PromotionAwardsType.lookup( PromotionAwardsType.POINTS );
    PayoutStructure payoutStructure = PayoutStructure.lookup( PayoutStructure.BOTH );
    promotion.setAwardType( awardsType );
    promotion.setPayoutStructure( payoutStructure );

    ////// Test specific setup////////
    GoalLevel level = new GoalLevel();
    level.setId( 2L );

    Set<AbstractGoalLevel> goalLevels = new HashSet<AbstractGoalLevel>();
    goalLevels.add( level );
    promotion.setGoalLevels( goalLevels );
    goal1.setGoalLevel( level );
    goal1.setGoalQuestPromotion( promotion );
    goalList.add( goal1 );

    // add something to the participantsGQResults list so we can confirm that it gets updated
    GoalCalculationResult goalCalculationRes = new GoalCalculationResult();

    EasyMock.expect( mockPromotionService.getPromotionById( testPromoId ) ).andReturn( promotion );

    EasyMock.expect( mockPaxGoalService.getPaxGoalByPromotionId( EasyMock.eq( testPromoId ), (AssociationRequestCollection)EasyMock.anyObject() ) ).andReturn( goalList );

    MockGetEligibleParticipantsForPromotion();

    EasyMock.expect( mockGoalPayoutStrategyFactory.getGoalPayoutStrategy( PayoutStructure.BOTH ) ).andReturn( mockGoalPayoutStrategy );

    // Nulling this out to test the other conditional path.
    final GoalCalculationResult gcr = new GoalCalculationResult();
    gcr.setAchieved( false );
    expectedParticipantGQResults.add( gcr );
    EasyMock.expect( mockGoalPayoutStrategy.processGoal( goal1 ) ).andReturn( gcr );

    // These get called no matter what, but don't relate to this test.
    EasyMock.expect( mockGoalPayoutStrategyFactory.getGoalPayoutStrategy( PayoutStructure.BOTH ) ).andReturn( mockGoalPayoutStrategy );
    EasyMock.expect( mockGoalPayoutStrategy.summarizeResults( (Map)EasyMock.anyObject() ) ).andReturn( expectedParticipantGQAwardSummaryList );

    /////// Call being tested///////
    mockControl.replay();
    PendingGoalQuestAwardSummary actual = testInstance.getGoalQuestAwardSummaryByPromotionId( testPromoId );
    mockControl.verify();
    /////// Postconditions////////
    assertTrue( expectedManagerGQAwardSummaryList.equals( actual.getManagerGoalQuestAwardSummaryList() ) );
    assertTrue( expectedParticipantGQAwardSummaryList.equals( actual.getParticipantGoalQuestAwardSummaryList() ) );
    assertTrue( expectedPartnerGQAwardSummaryList.equals( actual.getPartnerGoalQuestAwardSummaryList() ) );
    assertTrue( expectedParticipantGQResults.equals( actual.getParticipantGQResults() ) );
  }

  /**
   * <b>Goal:</b> Test behavior of the block of calculations that is 
   * performed if a paxGoal has a valid goalLevel (non-null, not a 
   * ManagerOverrideGoalLevel) when 'userNodes' is not null
   * <br/><br/>
   * <b>Setup:</b> Create a paxGoal with an eligible participant and a non-null
   * goal level that is not a ManagerOverrideGoalLevel. Give the goal a Participant
   * who has UserNodes.
   * <br/><br/>
   * <b>Expected Behavior:</b> a call to addOrIncrementTotals will be made, and
   * participantGQResults will be updated.
   */
  @Test
  public void testValidGoalLevelAndUserNodes()
  {
    /////// Basic setup////////
    PaxGoal goal1 = new PaxGoal();
    Participant pax1 = new Participant();
    pax1.setActive( true );
    pax1.setId( 1L );
    Set<UserNode> userNodes = new HashSet<UserNode>();
    UserNode node = new UserNode();
    node.setHierarchyRoleType( HierarchyRoleType.lookup( HierarchyRoleType.MEMBER ) );
    node.setNode( new Node() );
    userNodes.add( node );
    node.setIsPrimary( Boolean.TRUE );
    pax1.setUserNodes( userNodes );
    goal1.setParticipant( pax1 );
    PromotionAwardsType awardsType = PromotionAwardsType.lookup( PromotionAwardsType.POINTS );
    PayoutStructure payoutStructure = PayoutStructure.lookup( PayoutStructure.BOTH );
    promotion.setAwardType( awardsType );
    promotion.setPayoutStructure( payoutStructure );

    ////// Test specific setup////////
    GoalLevel level = new GoalLevel();
    level.setId( 2L );

    Set<AbstractGoalLevel> goalLevels = new HashSet<AbstractGoalLevel>();
    goalLevels.add( level );
    promotion.setGoalLevels( goalLevels );
    goal1.setGoalLevel( level );
    goal1.setGoalQuestPromotion( promotion );
    goalList.add( goal1 );

    // add something to the participantsGQResults list so we can confirm that it gets updated
    GoalCalculationResult goalCalculationRes = new GoalCalculationResult();
    expectedParticipantGQResults.add( goalCalculationRes );

    EasyMock.expect( mockPromotionService.getPromotionById( testPromoId ) ).andReturn( promotion );

    EasyMock.expect( mockPaxGoalService.getPaxGoalByPromotionId( EasyMock.eq( testPromoId ), (AssociationRequestCollection)EasyMock.anyObject() ) ).andReturn( goalList );

    MockGetEligibleParticipantsForPromotion();

    EasyMock.expect( mockGoalPayoutStrategyFactory.getGoalPayoutStrategy( PayoutStructure.BOTH ) ).andReturn( mockGoalPayoutStrategy );
    // This call should be made to update the participantGQResults in
    // GoalQuestServiceImpl.addOrIncrementTotals()
    EasyMock.expect( mockGoalPayoutStrategy.processGoal( goal1 ) ).andReturn( goalCalculationRes );

    // These get called no matter what, but don't relate to this test.
    EasyMock.expect( mockGoalPayoutStrategyFactory.getGoalPayoutStrategy( PayoutStructure.BOTH ) ).andReturn( mockGoalPayoutStrategy );
    EasyMock.expect( mockGoalPayoutStrategy.summarizeResults( (Map)EasyMock.anyObject() ) ).andReturn( expectedParticipantGQAwardSummaryList );

    /////// Call being tested///////
    mockControl.replay();
    PendingGoalQuestAwardSummary actual = testInstance.getGoalQuestAwardSummaryByPromotionId( testPromoId );
    mockControl.verify();
    /////// Postconditions////////
    assertTrue( expectedManagerGQAwardSummaryList.equals( actual.getManagerGoalQuestAwardSummaryList() ) );
    assertTrue( expectedParticipantGQAwardSummaryList.equals( actual.getParticipantGoalQuestAwardSummaryList() ) );
    assertTrue( expectedPartnerGQAwardSummaryList.equals( actual.getPartnerGoalQuestAwardSummaryList() ) );
    assertTrue( expectedParticipantGQResults.equals( actual.getParticipantGQResults() ) );
  }

  /**
   * <b>Goal:</b> Test the block that is conditional on
   * promotion.partnerAudienceType != null
   * <br/><br/>
   * <b>Setup:</b> Create a paxGoal with an eligible participant and a non-null
   * goal level that is not a ManagerOverrideGoalLevel. Give the goal a Participant
   * who has UserNodes.
   * <br/><br/>
   * <b>Expected Behavior:</b> a call to addOrIncrementTotals will be made, and
   * participantGQResults will be updated.
   */
  @Test
  public void testPartnerAudienceTypeHasValue()
  {
    /////// Basic setup////////
    PaxGoal goal1 = new PaxGoal();
    Participant pax1 = new Participant();
    pax1.setActive( true );
    pax1.setId( 1L );
    goal1.setParticipant( pax1 );
    goal1.setGoalLevel( null );
    PromotionAwardsType awardsType = PromotionAwardsType.lookup( PromotionAwardsType.POINTS );
    PayoutStructure payoutStructure = PayoutStructure.lookup( PayoutStructure.BOTH );
    promotion.setAwardType( awardsType );
    promotion.setPayoutStructure( payoutStructure );
    promotion.setId( testPromoId );

    ////// Test specific setup////////
    promotion.setPartnerAudienceType( PartnerAudienceType.lookup( PartnerAudienceType.ALL_ACTIVE_PAX_CODE ) );
    goal1.setGoalQuestPromotion( promotion );
    goalList.add( goal1 );
    ParticipantPartnerDAO mockParticipantPartnerDAO = mockControl.createMock( ParticipantPartnerDAO.class );
    testInstance.setParticipantPartnerDAO( mockParticipantPartnerDAO );

    EasyMock.expect( mockPromotionService.getPromotionById( testPromoId ) ).andReturn( promotion );

    EasyMock.expect( mockPaxGoalService.getPaxGoalByPromotionId( EasyMock.eq( testPromoId ), (AssociationRequestCollection)EasyMock.anyObject() ) ).andReturn( goalList );

    MockGetEligibleParticipantsForPromotion();

    EasyMock.expect( mockParticipantPartnerDAO.getParticipantPartnersByPromotion( testPromoId ) ).andReturn( new ArrayList<ParticipantPartner>() );

    // These get called no matter what, but don't relate to this test.
    EasyMock.expect( mockGoalPayoutStrategyFactory.getGoalPayoutStrategy( PayoutStructure.BOTH ) ).andReturn( mockGoalPayoutStrategy );
    EasyMock.expect( mockGoalPayoutStrategy.summarizeResults( (Map)EasyMock.anyObject() ) ).andReturn( expectedParticipantGQAwardSummaryList );

    /////// Call being tested///////
    mockControl.replay();
    PendingGoalQuestAwardSummary actual = testInstance.getGoalQuestAwardSummaryByPromotionId( testPromoId );
    mockControl.verify();
    /////// Postconditions////////
    assertTrue( expectedManagerGQAwardSummaryList.equals( actual.getManagerGoalQuestAwardSummaryList() ) );
    assertTrue( expectedParticipantGQAwardSummaryList.equals( actual.getParticipantGoalQuestAwardSummaryList() ) );
    assertTrue( expectedPartnerGQAwardSummaryList.equals( actual.getPartnerGoalQuestAwardSummaryList() ) );
    assertTrue( expectedParticipantGQResults.equals( actual.getParticipantGQResults() ) );
  }

  /**
   * <b>Goal:</b> Confirm behavior of the conditionals based on PartnerAudienceType
   * and PartnerGQResults
   * <br/><br/>
   * <b>Setup:</b> Create extra mocks that aren't used in all the other tests,
   * create a ParticipantPartner, give it a type and goalquest results.
   * <br/><br/>
   * <b>Expected Behavior:</b> The goalQuestAwardSummaryList of the returned object
   * will be updated.
   */
  @Test
  public void testPartnerAudienceTypeHasValueAndPartnerGQResults()
  {
    /////// Basic setup////////
    // need an active participant
    PaxGoal goal1 = new PaxGoal();
    Participant pax1 = new Participant();
    pax1.setActive( true );
    pax1.setId( 1L );

    goal1.setParticipant( pax1 );
    goal1.setGoalLevel( null );
    PromotionAwardsType awardsType = PromotionAwardsType.lookup( PromotionAwardsType.POINTS );
    PayoutStructure payoutStructure = PayoutStructure.lookup( PayoutStructure.BOTH );
    promotion.setAwardType( awardsType );
    promotion.setPayoutStructure( payoutStructure );
    promotion.setId( testPromoId );

    ////// Test specific setup////////
    PartnerEarnings partnerEarnings = PartnerEarnings.lookup( PartnerEarnings.HIGHEST );
    PartnerGoalStrategyFactory mockPartnerGoalStrategyFactory = mockControl.createMock( PartnerGoalStrategyFactory.class );
    ParticipantPartnerDAO mockParticipantPartnerDAO = mockControl.createMock( ParticipantPartnerDAO.class );
    PartnerGoalStrategy mockPartnerGoalStrategy = mockControl.createMock( PartnerGoalStrategy.class );
    List<ParticipantPartner> fakeRetList = new ArrayList<ParticipantPartner>();
    ParticipantPartner partner = new ParticipantPartner();
    GoalCalculationResult goalCalcResult = new GoalCalculationResult();
    GoalLevel level = new GoalLevel();

    // This test needs partner things populated
    promotion.setPartnerAudienceType( PartnerAudienceType.lookup( PartnerAudienceType.ALL_ACTIVE_PAX_CODE ) );
    promotion.setPartnerEarnings( partnerEarnings );

    // Gotta add this or we won't get far at all (if GoalList is empty, skips everything)
    goal1.setGoalQuestPromotion( promotion );
    goalList.add( goal1 );

    // Pax1 is abused a little here, instead of making a new one I just reused it.
    partner.setPartner( pax1 );
    partner.setParticipant( pax1 );
    fakeRetList.add( partner );

    // We need a GoalCalculationResult with a level that has an id later.
    level.setId( 1L );
    goalCalcResult.setGoalLevel( level );

    testInstance.setParticipantPartnerDAO( mockParticipantPartnerDAO );
    testInstance.setPartnerGoalStrategyFactory( mockPartnerGoalStrategyFactory );

    GoalQuestAwardSummary extra1 = new GoalQuestAwardSummary();
    expectedPartnerGQAwardSummaryList.add( extra1 );

    MockGetEligibleParticipantsForPromotion();

    EasyMock.expect( mockPromotionService.getPromotionById( testPromoId ) ).andReturn( promotion );

    EasyMock.expect( mockPaxGoalService.getPaxGoalByPromotionId( EasyMock.eq( testPromoId ), (AssociationRequestCollection)EasyMock.anyObject() ) ).andReturn( goalList );

    EasyMock.expect( mockParticipantPartnerDAO.getParticipantPartnersByPromotion( testPromoId ) ).andReturn( fakeRetList );

    EasyMock.expect( mockUserService.getUserByIdWithAssociations( EasyMock.eq( testPromoId ), (AssociationRequestCollection)EasyMock.anyObject() ) ).andReturn( pax1 );

    EasyMock.expect( mockPartnerGoalStrategyFactory.getPartnerGoalStrategy( partnerEarnings.getCode() ) ).andReturn( mockPartnerGoalStrategy ).times( 2 );

    EasyMock.expect( mockPartnerGoalStrategy.summarizeResults( (Map)EasyMock.anyObject() ) ).andReturn( expectedPartnerGQAwardSummaryList );

    EasyMock.expect( mockPartnerGoalStrategy.processGoal( EasyMock.eq( partner ), (Map)EasyMock.anyObject(), (Map)EasyMock.anyObject(), (Map)EasyMock.anyObject() ) ).andReturn( goalCalcResult );

    // These get called no matter what, but don't relate to this test.
    EasyMock.expect( mockGoalPayoutStrategyFactory.getGoalPayoutStrategy( PayoutStructure.BOTH ) ).andReturn( mockGoalPayoutStrategy );

    EasyMock.expect( mockGoalPayoutStrategy.summarizeResults( (Map)EasyMock.anyObject() ) ).andReturn( expectedParticipantGQAwardSummaryList );

    /////// Call being tested///////
    mockControl.replay();
    PendingGoalQuestAwardSummary actual = testInstance.getGoalQuestAwardSummaryByPromotionId( testPromoId );
    mockControl.verify();
    /////// Postconditions////////
    assertTrue( expectedManagerGQAwardSummaryList.equals( actual.getManagerGoalQuestAwardSummaryList() ) );
    assertTrue( expectedParticipantGQAwardSummaryList.equals( actual.getParticipantGoalQuestAwardSummaryList() ) );
    assertTrue( expectedPartnerGQAwardSummaryList.equals( actual.getPartnerGoalQuestAwardSummaryList() ) );
    assertTrue( expectedParticipantGQResults.equals( actual.getParticipantGQResults() ) );
  }

  /**
   * <b>Goal:</b> Test the result of a non-points reward type (Merch or Travel)
   * <br/><br/>
   * <b>Setup:</b> Set the award type to something besides points, create
   * a PaxGoal with an active participant.
   * <br/><br/>
   * <b>Expected Behavior: set the participantgoalQuestAwardSummaryList attr
   * to our expected value.
   */
  @Test
  public void testAwardTypeNotPoints()
  {
    promotion.setAwardType( PromotionAwardsType.lookup( PromotionAwardsType.MERCHANDISE ) );
    promotion.setId( testPromoId );

    PaxGoal goal1 = new PaxGoal();

    Participant pax1 = new Participant();
    pax1.setActive( true );
    pax1.setId( 1L );

    goal1.setParticipant( pax1 );
    goal1.setGoalLevel( null );
    goalList.add( goal1 );

    // Add something so we can check that it gets updated by our calls
    expectedParticipantGQAwardSummaryList.add( new GoalQuestAwardSummary() );

    EasyMock.expect( mockPromotionService.getPromotionById( testPromoId ) ).andReturn( promotion );
    EasyMock.expect( mockPaxGoalService.getPaxGoalByPromotionId( EasyMock.eq( testPromoId ), (AssociationRequestCollection)EasyMock.anyObject() ) ).andReturn( goalList );
    EasyMock.expect( mockGoalPayoutStrategyFactory.getGoalPayoutStrategy() ).andReturn( mockGoalPayoutStrategy );
    EasyMock.expect( mockGoalPayoutStrategy.summarizeResults( (Map<Long, GoalQuestAwardSummary>)EasyMock.anyObject() ) ).andReturn( expectedParticipantGQAwardSummaryList );
    MockGetEligibleParticipantsForPromotion();
    mockControl.replay();
    PendingGoalQuestAwardSummary actual = testInstance.getGoalQuestAwardSummaryByPromotionId( testPromoId );
    mockControl.verify();
  }

  /**
   * These tests all call getEligibleParticipantsForPromotion, but that
   * method is tested elsewhere, so I enclosed the necessary 
   * expectations/results for it here to simplify the code.
   */
  @Ignore
  private void MockGetEligibleParticipantsForPromotion()
  {
    Hierarchy primaryHierarchy = null;
    List<Long> activePaxIds = null;
    List<FormattedValueBean> fvbList = null;
    List<FormattedValueBean> fvbList2 = null;

    // For use in our mockListBuilderService calls
    primaryHierarchy = new Hierarchy();
    primaryHierarchy.setId( 1L );

    activePaxIds = new ArrayList();
    fvbList = new ArrayList();
    fvbList2 = new ArrayList();

    // initialize activePaxIds
    activePaxIds.add( 1L );
    activePaxIds.add( 2L );
    activePaxIds.add( 3L );

    // Initialize fvblists
    FormattedValueBean[] fvbs = new FormattedValueBean[6];
    for ( Integer i = 0; i < fvbs.length; i++ )
    {
      fvbs[i] = new FormattedValueBean();
      fvbs[i].setId( i + 4L );
      if ( i < 3 )
      {
        fvbList.add( fvbs[i] );
      }
      else
      {
        fvbList2.add( fvbs[i] );
      }
    }

    promotion.setPrimaryAudienceType( PrimaryAudienceType.lookup( PrimaryAudienceType.ALL_ACTIVE_PAX_CODE ) );
    EasyMock.expect( mockHierarchyService.getPrimaryHierarchy() ).andReturn( primaryHierarchy );
    EasyMock.expect( mockParticipantService.getAllActivePaxIds() ).andReturn( activePaxIds );
    EasyMock.expect( mockListBuilderService.searchParticipants( promotion.getSecondaryAudiences(), primaryHierarchy.getId(), true, null, true ) ).andReturn( fvbList2 );

    // activePaxIds + fvbList2
    // List<Long> expected = Arrays.asList( new Long[]{1L, 2L, 3L, 7L, 8L, 9L});
  }
}
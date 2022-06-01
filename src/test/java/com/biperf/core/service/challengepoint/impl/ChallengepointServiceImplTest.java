
package com.biperf.core.service.challengepoint.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jmock.Mock;
import org.jmock.core.constraint.IsAnything;

import com.biperf.core.dao.promotion.hibernate.PromotionDAOImplTest;
import com.biperf.core.domain.enums.HierarchyRoleType;
import com.biperf.core.domain.enums.PromotionAwardsType;
import com.biperf.core.domain.enums.UserType;
import com.biperf.core.domain.goalquest.PaxGoal;
import com.biperf.core.domain.hierarchy.Node;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.ChallengePointPromotion;
import com.biperf.core.domain.promotion.GoalLevel;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.user.UserNode;
import com.biperf.core.service.BaseServiceTest;
import com.biperf.core.service.email.MailingService;
import com.biperf.core.service.goalquest.PaxGoalService;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.service.promotion.impl.PromotionServiceImplTest;

/**
 * ChallengePointServiceImplTest.
 * <p>
 * <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>viswanat</td>
 * <td>Feb 2, 2007</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ChallengepointServiceImplTest extends BaseServiceTest
{

  private Mock mockChallengePointPaxActivityService = null;
  private Mock mockMailingService = null;
  private Mock mockMessageService = null;
  private Mock mockSystemVariableService = null;
  private Mock mockPaxGoalService = null;
  private Mock mockUserService = null;
  private Mock mockPromotionService = null;

  /**
   * Constructor to take the name of the test.
   * 
   * @param test
   */
  public ChallengepointServiceImplTest( String test )
  {
    super( test );
  }

  /**
   * Setup this test case. Overridden from
   * 
   * @see junit.framework.TestCase#setUp()
   * @throws Exception
   */
  protected void setUp() throws Exception
  {
    super.setUp();
    mockUserService = new Mock( UserService.class );

    mockMailingService = new Mock( MailingService.class );

    mockPaxGoalService = new Mock( PaxGoalService.class );

    mockPromotionService = new Mock( PromotionService.class );
  }

  /**
   * Test getAllLiveChallengePointPromotionsByUserId
   */
  public void testGetAllLiveChallengePointPromotionsByUserId()
  {

    ChallengePointPromotion promotion = buildChallengePointPromotion();
    // set award type as POINTS
    promotion.setAwardType( PromotionAwardsType.lookup( PromotionAwardsType.POINTS ) );
    Participant participant1 = buildParticipant( 1 );
    Node node1 = PromotionServiceImplTest.buildNode( 1 );
    UserNode userNode1 = new UserNode();
    userNode1.setIsPrimary( Boolean.TRUE );
    userNode1.setHierarchyRoleType( HierarchyRoleType.lookup( HierarchyRoleType.MEMBER ) );
    userNode1.setNode( node1 );
    Set nodeSet = new HashSet();
    nodeSet.add( userNode1 );
    participant1.setUserNodes( nodeSet );
    participant1.setActive( Boolean.TRUE );

    List paxChallengePointList = new ArrayList();
    PaxGoal paxChallengePoint = buildPaxGoal( promotion, participant1 );
    paxChallengePointList.add( paxChallengePoint );
    mockPaxGoalService.expects( once() ).method( "getPaxGoalByPromotionId" ).with( same( promotion.getId() ), new IsAnything() ).will( returnValue( paxChallengePointList ) );
    PaxGoalService pgs = (PaxGoalService)mockPaxGoalService.proxy();
    pgs.getPaxGoalByPromotionId( promotion.getId(), null );
    /*
     * mockUserService.expects( once() ).method( "getAllUsersOnNodeHavingRole" ) .with( same(
     * node1.getId() ), new IsAnything(), new IsAnything() ) .will( returnValue( new ArrayList() )
     * ); mockPromotionService.expects( once() ).method( "getPromotionById" ) .with( same(
     * promotion.getId() ) ).will( returnValue( promotion ) ); mockPromotionService.expects( once()
     * ).method( "isParticipantInAudience" ) .with( same( participant1 ), same( promotion ) ).will(
     * returnValue( true ) );
     */

  }

  /**
   * Creates a ChallengePointPromotion.
   * 
   * @return ChallengePointPromotion
   */
  public static ChallengePointPromotion buildChallengePointPromotion()
  {
    String uniqueString = getUniqueString();
    ChallengePointPromotion challengePointPromotion = PromotionDAOImplTest.buildChallengePointPromotion( "ChallengePointPROMOTION_" + uniqueString );
    challengePointPromotion.setId( new Long( 836 ) );
    GoalLevel level = new GoalLevel();
    level.setSequenceNumber( 1 );
    /*
     * level.setGoalLevelName( "Level One" ); level.setGoalLevelDescription( "Level One Desc" );
     */
    level.setAward( new BigDecimal( "10.5" ) );
    level.setManagerAward( new BigDecimal( "50.5" ) );
    challengePointPromotion.getGoalLevels().add( level );
    level.setPromotion( challengePointPromotion );
    GoalLevel level2 = new GoalLevel();
    level2.setSequenceNumber( 2 );
    /*
     * level2.setGoalLevelName( "Level Two" ); level2.setGoalLevelDescription( "Level Two Desc" );
     */
    level2.setAward( new BigDecimal( "10.6" ) );
    level2.setManagerAward( new BigDecimal( "60.5" ) );
    challengePointPromotion.getGoalLevels().add( level2 );
    // level2.setPromotion( promotion );
    return challengePointPromotion;
  }

  /**
   * @param promotion
   * @param participant
   * @return PaxChallengePoint
   */
  public static PaxGoal buildPaxGoal( Promotion promotion, Participant participant )
  {
    PaxGoal pl = new PaxGoal();
    pl.setParticipant( participant );
    ChallengePointPromotion challengePointPromotion = (ChallengePointPromotion)promotion;
    pl.setGoalQuestPromotion( challengePointPromotion );
    GoalLevel goalLevel = (GoalLevel)challengePointPromotion.getGoalLevels().iterator().next();
    pl.setGoalLevel( goalLevel );
    pl.setBaseQuantity( new BigDecimal( 10 ) );
    pl.setOverrideQuantity( new BigDecimal( 1 ) );
    pl.setCurrentValue( new BigDecimal( 20 ) );
    return pl;
  }

  public static Participant buildParticipant( long id )
  {
    Participant participant = new Participant();
    participant.setId( new Long( id ) );
    participant.setFirstName( String.valueOf( id ) );
    participant.setLastName( String.valueOf( id ) );
    participant.setUserName( String.valueOf( id ) );
    participant.setUserType( UserType.lookup( UserType.PARTICIPANT ) );
    return participant;
  }

}


package com.biperf.core.service.challengepoint.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jmock.Mock;

import com.biperf.core.dao.challengepoint.ChallengepointAwardDAO;
import com.biperf.core.dao.challengepoint.ChallengepointProgressDAO;
import com.biperf.core.dao.challengepoint.hibernate.ChallengepointProgressDAOImplTest;
import com.biperf.core.dao.promotion.PromotionDAO;
import com.biperf.core.dao.promotion.hibernate.PromotionDAOImplTest;
import com.biperf.core.domain.challengepoint.ChallengepointProgress;
import com.biperf.core.domain.goalquest.PaxGoal;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.ChallengePointPromotion;
import com.biperf.core.domain.promotion.GoalLevel;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.BaseServiceTest;
import com.biperf.core.service.challengepoint.ChallengePointService;
import com.biperf.core.service.email.MailingService;
import com.biperf.core.service.goalquest.PaxGoalService;
import com.biperf.core.service.message.MessageService;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.service.promotion.engine.ChallengePointAchievementStrategyFactory;
import com.biperf.core.service.promotion.engine.ChallengePointManagerPayoutStrategyFactory;
import com.biperf.core.utils.ApplicationContextFactory;
import com.biperf.core.value.ChallengepointPaxValueBean;

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
public class ChallengepointProgressServiceImplTest extends BaseServiceTest
{
  private Mock mockChallengePointService = null;
  private Mock mockChallengepointProgressDAO = null;
  private Mock mockChallengepointAwardDAO = null;
  private Mock mockPromotionService = null;
  private Mock mockPaxGoalService = null;
  private Mock mockMailingService = null;
  private Mock mockMessageService = null;
  private ChallengepointProgressServiceImpl progressServiceImpl = new ChallengepointProgressServiceImpl();
  private Mock mockChallengePointAchievementStrategyFactory = null;
  private Mock mockChallengePointManagerPayoutStrategyFactory = null;

  /**
   * Constructor to take the name of the test.
   * 
   * @param test
   */
  public ChallengepointProgressServiceImplTest( String test )
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
    mockChallengePointService = new Mock( ChallengePointService.class );
    mockChallengepointProgressDAO = new Mock( ChallengepointProgressDAO.class );
    mockChallengepointAwardDAO = new Mock( ChallengepointAwardDAO.class );
    mockPromotionService = new Mock( PromotionService.class );
    mockPaxGoalService = new Mock( PaxGoalService.class );
    mockMailingService = new Mock( MailingService.class );
    mockMessageService = new Mock( MessageService.class );
    mockChallengePointAchievementStrategyFactory = new Mock( ChallengePointAchievementStrategyFactory.class );
    mockChallengePointManagerPayoutStrategyFactory = new Mock( ChallengePointManagerPayoutStrategyFactory.class );

    progressServiceImpl.setPaxGoalService( (PaxGoalService)mockPaxGoalService.proxy() );
    progressServiceImpl.setChallengePointService( (ChallengePointService)mockChallengePointService.proxy() );
    progressServiceImpl.setChallengepointProgressDAO( (ChallengepointProgressDAO)mockChallengepointProgressDAO.proxy() );

  }

  private static PromotionDAO getPromotionDAO()
  {
    return (PromotionDAO)ApplicationContextFactory.getApplicationContext().getBean( PromotionDAO.BEAN_NAME );
  }

  /**
   * Test getAllLiveChallengePointPromotionsByUserId
   */
  public void testSaveChallengepointProgress()
  {

    // create a promotion object

    ChallengePointPromotion cpPromotion = buildChallengePointPromotion();

    ChallengepointProgress expectedProgress = buildChallengePointProgress( cpPromotion );

    PaxGoal paxGoal = buildPaxGoal( cpPromotion, expectedProgress.getParticipant() );
    paxGoal.setId( new Long( 1 ) );
    // PromotionDAO expected to call save once with the Promotion
    // which will return the Promotion expected
    mockChallengepointProgressDAO.expects( once() ).method( "saveChallengepointProgress" ).with( same( expectedProgress ) ).will( returnValue( expectedProgress ) );
    mockPaxGoalService.expects( once() ).method( "getPaxGoalByPromotionIdAndUserId" )
        .with( same( expectedProgress.getChallengePointPromotion().getId() ), same( expectedProgress.getParticipant().getId() ) ).will( returnValue( paxGoal ) );
    mockPaxGoalService.expects( once() ).method( "savePaxGoal" ).with( same( paxGoal ) ).will( returnValue( paxGoal ) );
    ChallengepointProgress actualProgress = progressServiceImpl.saveChallengepointProgress( expectedProgress );
    mockChallengepointProgressDAO.verify();
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

    GoalLevel level = new GoalLevel();
    level.setSequenceNumber( 1 );
    level.setAward( new BigDecimal( "10.5" ) );
    level.setManagerAward( new BigDecimal( "50.5" ) );
    challengePointPromotion.getGoalLevels().add( level );
    level.setPromotion( challengePointPromotion );
    GoalLevel level2 = new GoalLevel();
    level2.setSequenceNumber( 2 );
    level2.setAward( new BigDecimal( "10.6" ) );
    level2.setManagerAward( new BigDecimal( "60.5" ) );
    challengePointPromotion.getGoalLevels().add( level2 );
    level2.setPromotion( challengePointPromotion );

    return challengePointPromotion;
  }

  public static ChallengepointProgress buildChallengePointProgress( ChallengePointPromotion promotion )
  {
    String uniqueString = getUniqueString();
    ChallengepointProgress challengepointProgress = ChallengepointProgressDAOImplTest.buildChallengepointProgress( "ChallengePointPROMOTION_" + uniqueString, promotion );

    return challengepointProgress;
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

  /**
   * Test getChallengepointProgressByPromotionIdAndUserId
   */
  public void testGetChallengepointProgressByPromotionIdAndUserId() throws ServiceErrorException
  {
    Long userId = 123L;
    ChallengePointPromotion cpPromotion = buildChallengePointPromotion();
    cpPromotion.setId( new Long( 12 ) );
    List<ChallengepointProgress> cpProgressList = new ArrayList<ChallengepointProgress>();
    ChallengepointProgress expectedProgress = buildChallengePointProgress( cpPromotion );
    cpProgressList.add( expectedProgress );
    PaxGoal paxGoal = buildPaxGoal( cpPromotion, expectedProgress.getParticipant() );
    paxGoal.setId( new Long( 1 ) );
    ChallengepointPaxValueBean cpPaxValueBean = new ChallengepointPaxValueBean();
    cpPaxValueBean.setAmountToAchieve( new BigDecimal( 20.00 ) );
    mockChallengepointProgressDAO.expects( once() ).method( "getChallengepointProgressByPromotionIdAndUserId" ).will( returnValue( cpProgressList ) );
    mockChallengePointService.expects( once() ).method( "getPaxChallengePoint" ).will( returnValue( paxGoal ) );
    mockChallengePointService.expects( once() ).method( "populateChallengepointPaxValueBean" ).will( returnValue( cpPaxValueBean ) );
    Map<String, Object> challengPointPRogress = progressServiceImpl.getChallengepointProgressByPromotionIdAndUserId( cpPromotion.getId(), userId );
    assertNotNull( challengPointPRogress );
  }

}


package com.biperf.core.dao.challengepoint.hibernate;

import java.math.BigDecimal;
import java.util.List;

import com.biperf.core.dao.goalquest.PaxGoalDAO;
import com.biperf.core.dao.hibernate.BaseDAOTest;
import com.biperf.core.dao.participant.ParticipantDAO;
import com.biperf.core.dao.participant.hibernate.ParticipantDAOImplTest;
import com.biperf.core.dao.promotion.PromotionDAO;
import com.biperf.core.dao.promotion.hibernate.PromotionDAOImplTest;
import com.biperf.core.domain.goalquest.PaxGoal;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.ChallengePointPromotion;
import com.biperf.core.domain.promotion.GoalLevel;
import com.biperf.core.domain.promotion.GoalQuestPromotion;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.challengepoint.impl.ChallengePointServiceImpl;
import com.biperf.core.service.goalquest.impl.PaxGoalAssociationRequest;
import com.biperf.core.service.goalquest.impl.PaxGoalServiceImpl;
import com.biperf.core.utils.ApplicationContextFactory;
import com.biperf.core.utils.HibernateSessionManager;

public class ChallengepointPaxGoalDAOImplTest extends BaseDAOTest
{

  // ---------------------------------------------------------------------------
  // Test Methods
  // ---------------------------------------------------------------------------

  public void testSaveGetPaxGoalById()
  {
    String uniqueString = String.valueOf( System.currentTimeMillis() % 7503032 );

    PaxGoalDAO challengePointPaxGoalDAO = getPaxGoalDAO();

    PaxGoal expectedPaxGoal = buildAndSavePaxGoal( uniqueString );

    // Test Save
    challengePointPaxGoalDAO.savePaxGoal( expectedPaxGoal );

    flushAndClearSession();
    // 1. Test Get by Pax Level Id
    PaxGoal actualPaxGoal = challengePointPaxGoalDAO.getPaxGoalById( expectedPaxGoal.getId() );
    assertEquals( "Test Get by Pax Level Id: PaxGoals are not equal", expectedPaxGoal, actualPaxGoal );

    // 2. Test Get by Pax Level Id with Assoication Request - hydrate all pieces
    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
    associationRequestCollection.add( new PaxGoalAssociationRequest( PaxGoalAssociationRequest.ALL ) );
    PaxGoal actualPaxGoal2 = challengePointPaxGoalDAO.getPaxGoalByIdWithAssociations( expectedPaxGoal.getId(), associationRequestCollection );
    assertEquals( "Test Get by Assoication Request: LevelQuestPaxGoals are not equal", expectedPaxGoal, actualPaxGoal2 );
    assertNotNull( "Test Get by Assoication Request: Expected promotion to be not null.", actualPaxGoal2.getGoalQuestPromotion() );
    assertNotNull( "Test Get by Assoication Request: Expected participant to be not null.", actualPaxGoal2.getParticipant() );
    assertNotNull( "Test Get by Assoication Request: Expected goal level to be not null.", actualPaxGoal2.getGoalLevel() );

    // 3. Test Get by Promotion Id and Participant's User Id
    PaxGoal actualLevelPaxGoal3 = challengePointPaxGoalDAO.getPaxGoalByPromotionIdAndUserId( expectedPaxGoal.getGoalQuestPromotion().getId(), expectedPaxGoal.getParticipant().getId() );
    assertEquals( "Test Get by Participant's User Id: LevelQuestPaxGoals are not equal", expectedPaxGoal, actualLevelPaxGoal3 );

    // 4. Test Get by Promotion Id and Participant's User Id
    PaxGoal actualLevelPaxGoal4 = challengePointPaxGoalDAO.getPaxGoalByPromotionIdAndUserId( new Long( 0 ), new Long( 0 ) ); // pass
                                                                                                                             // such
                                                                                                                             // that
                                                                                                                             // no
                                                                                                                             // record
                                                                                                                             // returns
    assertEquals( "Test Get by Participant's User Id: is null", null, actualLevelPaxGoal4 );

    // 5. Test Get by Promotion Id with Assoication Request - hydrate all pieces
    AssociationRequestCollection associationRequestCollection2 = new AssociationRequestCollection();
    associationRequestCollection2.add( new PaxGoalAssociationRequest( PaxGoalAssociationRequest.ALL ) );
    List<PaxGoal> listOfPaxGoals = challengePointPaxGoalDAO.getPaxGoalByPromotionId( expectedPaxGoal.getGoalQuestPromotion().getId(), associationRequestCollection );
    PaxGoal actualLevelPaxGoal5 = (PaxGoal)listOfPaxGoals.get( 0 );
    assertEquals( "Test Get by Assoication Request: LevelCPPaxGoals are not equal", expectedPaxGoal, actualLevelPaxGoal5 );
    assertNotNull( "Test Get by Assoication Request: Expected promotion to be not null.", actualLevelPaxGoal5.getGoalQuestPromotion() );
    assertNotNull( "Test Get by Assoication Request: Expected participant to be not null.", actualLevelPaxGoal5.getParticipant() );
    assertNotNull( "Test Get by Assoication Request: Expected cp level to be not null.", actualLevelPaxGoal5.getGoalLevel() );

    ChallengePointServiceImpl cpService = new ChallengePointServiceImpl();
    PaxGoalServiceImpl pxService = new PaxGoalServiceImpl();
    pxService.setPaxGoalDAO( getPaxGoalDAO() );
    cpService.setPromotionDAO( getPromotionDAO() );
    cpService.setPaxGoalService( pxService );
    List<PaxGoal> allPaxPromoLevels = null;

    allPaxPromoLevels = cpService.getAllLivePromotionsWithPaxGoalsByUserId( expectedPaxGoal.getGoalQuestPromotion().getId() );

    PaxGoal actualLevelPaxGoal6 = (PaxGoal)allPaxPromoLevels.get( 0 );

    assertNotNull( "Test Get by Assoication Request: Expected promotion to be not null.", actualLevelPaxGoal6.getGoalQuestPromotion() );
    assertNull( "Test Get by Assoication Request: Expected participant to be not null.", actualLevelPaxGoal6.getParticipant() );
    assertNotNull( "Test Get by Assoication Request: Expected cp level to be not null.", actualLevelPaxGoal6.getGoalQuestPromotion().getGoalLevels() );

  }

  public void testGetUserIdsWithPaxGoalByPromotionId()
  {
    String uniqueString = String.valueOf( System.currentTimeMillis() % 7503032 );

    PaxGoalDAO challengePointPaxGoalDAO = getPaxGoalDAO();

    PaxGoal expectedPaxGoal = buildAndSavePaxGoal( uniqueString );

    // Test Save
    challengePointPaxGoalDAO.savePaxGoal( expectedPaxGoal );

    flushAndClearSession();

    Long promotionId = expectedPaxGoal.getGoalQuestPromotion().getId();

    List<PaxGoal> usersWithPaxGoal = challengePointPaxGoalDAO.getUserIdsWithPaxGoalByPromotionId( promotionId );

    assertNotNull( usersWithPaxGoal );
    assertEquals( "Users with PaxGoal size", 1, usersWithPaxGoal.size() );

  }

  // ---------------------------------------------------------------------------
  // Private Methods
  // ---------------------------------------------------------------------------

  /**
   * Returns a {@link PaxGoalDAO} object.
   * 
   * @return a {@link PaxGoalDAO} object.
   */
  private PaxGoalDAO getPaxGoalDAO()
  {
    return (PaxGoalDAO)ApplicationContextFactory.getApplicationContext().getBean( PaxGoalDAO.BEAN_NAME );
  }

  /**
   * Returns a ParticipantDAO object.
   * 
   * @return a {@link ParticipantDAO} object.
   */
  private static ParticipantDAO getParticipantDAO()
  {
    return (ParticipantDAO)ApplicationContextFactory.getApplicationContext().getBean( ParticipantDAO.BEAN_NAME );
  }

  private static PromotionDAO getPromotionDAO()
  {
    return (PromotionDAO)ApplicationContextFactory.getApplicationContext().getBean( PromotionDAO.BEAN_NAME );
  }

  // ---------------------------------------------------------------------------
  // Build Methods
  // ---------------------------------------------------------------------------

  /**
   * Builds a {@link PaxGoal} object given a unique string.
   * 
   * @param uniqueString a unique string.
   * @return a new {@link PaxGoal} object.
   */
  public static PaxGoal buildAndSavePaxGoal( String uniqueString )
  {
    Promotion promotion = PromotionDAOImplTest.buildChallengePointPromotion( "MyLevelChallengePointPromo-" + uniqueString );

    GoalLevel level = new GoalLevel();
    level.setSequenceNumber( 1 );
    level.setAward( new BigDecimal( "10.5" ) );
    level.setManagerAward( new BigDecimal( "50.5" ) );
    ( (GoalQuestPromotion)promotion ).getGoalLevels().add( level );
    level.setPromotion( (GoalQuestPromotion)promotion );
    GoalLevel level2 = new GoalLevel();
    level2.setSequenceNumber( 2 );
    level2.setAward( new BigDecimal( "10.6" ) );
    level2.setManagerAward( new BigDecimal( "60.5" ) );
    ( (GoalQuestPromotion)promotion ).getGoalLevels().add( level2 );
    level2.setPromotion( (GoalQuestPromotion)promotion );

    getPromotionDAO().save( promotion );
    return buildAndSavePaxGoal( uniqueString, promotion );
  }

  /**
   * Builds a {@link PaxGoalDAO} object given a unique string.
   * 
   * @param uniqueString a unique string.
   * @param promotion
   * @return a new {@link PaxGoalDAO} object.
   */
  public static PaxGoal buildAndSavePaxGoal( String uniqueString, Promotion promotion )
  {
    PaxGoal challengePointPaxGoal = new PaxGoal();
    Participant newParticipant = ParticipantDAOImplTest.buildUniqueParticipant( "MyPax-" + uniqueString );
    newParticipant = getParticipantDAO().saveParticipant( newParticipant );
    HibernateSessionManager.getSession().flush();
    // flushAndClearSession();
    challengePointPaxGoal.setParticipant( newParticipant );
    ChallengePointPromotion challengePointPromotion = (ChallengePointPromotion)promotion;
    challengePointPaxGoal.setGoalQuestPromotion( challengePointPromotion );
    GoalLevel goalLevel = (GoalLevel)challengePointPromotion.getGoalLevels().iterator().next();
    challengePointPaxGoal.setGoalLevel( goalLevel );
    challengePointPaxGoal.setBaseQuantity( new BigDecimal( 10 ) );
    challengePointPaxGoal.setOverrideQuantity( new BigDecimal( 1 ) );
    challengePointPaxGoal.setCurrentValue( new BigDecimal( 0 ) );

    return challengePointPaxGoal;
  }

}

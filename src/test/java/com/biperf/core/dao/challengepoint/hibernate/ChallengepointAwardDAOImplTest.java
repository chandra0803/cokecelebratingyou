
package com.biperf.core.dao.challengepoint.hibernate;

import java.math.BigDecimal;

import com.biperf.core.dao.challengepoint.ChallengepointAwardDAO;
import com.biperf.core.dao.hibernate.BaseDAOTest;
import com.biperf.core.dao.participant.ParticipantDAO;
import com.biperf.core.dao.participant.hibernate.ParticipantDAOImplTest;
import com.biperf.core.dao.promotion.PromotionDAO;
import com.biperf.core.dao.promotion.hibernate.PromotionDAOImplTest;
import com.biperf.core.domain.challengepoint.ChallengepointAward;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.ChallengePointPromotion;
import com.biperf.core.domain.promotion.GoalLevel;
import com.biperf.core.domain.promotion.GoalQuestPromotion;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.utils.ApplicationContextFactory;
import com.biperf.core.utils.HibernateSessionManager;

public class ChallengepointAwardDAOImplTest extends BaseDAOTest
{

  // ---------------------------------------------------------------------------
  // Test Methods
  // ---------------------------------------------------------------------------

  public void testSaveGetChallengepointAwardById()
  {
    String uniqueString = String.valueOf( System.currentTimeMillis() % 7503032 );

    ChallengepointAwardDAO challengePointChallengepointAwardDAO = getChallengepointAwardDAO();

    ChallengepointAward expectedChallengepointAward = buildAndSaveChallengepointAward( uniqueString );

    // Test Save
    challengePointChallengepointAwardDAO.saveChallengepointAward( expectedChallengepointAward );

    flushAndClearSession();
    // 1. Test Get by Pax Level Id

    // 3. Test Get by Promotion Id and Participant's User Id
    // ChallengepointAward actualLevelChallengepointAward3 =
    // challengePointChallengepointAwardDAO.getChallengepointAwardByPromotionIdAndUserId(
    // expectedChallengepointAward.getChallengePointPromotion().getId(),
    // expectedChallengepointAward.getParticipant().getId() );
    // assertEquals(
    // "Test Get by Participant's User Id: LevelQuestChallengepointAwards are not equal",
    // expectedChallengepointAward, actualLevelChallengepointAward3 );

    // 4. Test Get by Promotion Id and Participant's User Id
    ChallengepointAward actualLevelChallengepointAward4 = challengePointChallengepointAwardDAO.getChallengepointAwardByPromotionIdAndUserId( new Long( 0 ), new Long( 0 ) ); // pass
                                                                                                                                                                             // such
                                                                                                                                                                             // that
                                                                                                                                                                             // no
                                                                                                                                                                             // record
                                                                                                                                                                             // returns
    assertEquals( "Test Get by Participant's User Id: is null", null, actualLevelChallengepointAward4 );

    // 5. Test Get by Promotion Id and Participant's User Id
    // List actualListAward5 =
    // challengePointChallengepointAwardDAO.getAllChallengepointAwardsByPromotionIdAndUserId(new
    // Long(0), new Long(0)); //pass such that no record returns
    // assertEquals( "Test Get by Participant's User Id: is null",null, actualListAward5);

  }

  // ---------------------------------------------------------------------------
  // Private Methods
  // ---------------------------------------------------------------------------

  /**
   * Returns a {@link ChallengepointAwardDAO} object.
   * 
   * @return a {@link ChallengepointAwardDAO} object.
   */
  private static ChallengepointAwardDAO getChallengepointAwardDAO()
  {
    return (ChallengepointAwardDAO)ApplicationContextFactory.getApplicationContext().getBean( ChallengepointAwardDAO.BEAN_NAME );
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
   * Builds a {@link ChallengepointAward} object given a unique string.
   * 
   * @param uniqueString a unique string.
   * @return a new {@link ChallengepointAward} object.
   */
  public static ChallengepointAward buildAndSaveChallengepointAward( String uniqueString )
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
    return buildAndSaveChallengepointAward( uniqueString, promotion );
  }

  /**
   * Builds a {@link ChallengepointAwardDAO} object given a unique string.
   * 
   * @param uniqueString a unique string.
   * @param promotion
   * @return a new {@link ChallengepointAwardDAO} object.
   */
  public static ChallengepointAward buildAndSaveChallengepointAward( String uniqueString, Promotion promotion )
  {
    ChallengepointAward challengePointAward = new ChallengepointAward();
    Participant newParticipant = ParticipantDAOImplTest.buildUniqueParticipant( "MyPax-" + uniqueString );
    newParticipant = getParticipantDAO().saveParticipant( newParticipant );
    HibernateSessionManager.getSession().flush();
    // flushAndClearSession();
    challengePointAward.setParticipant( newParticipant );
    ChallengePointPromotion challengePointPromotion = (ChallengePointPromotion)promotion;
    challengePointAward.setChallengePointPromotion( challengePointPromotion );
    GoalLevel cpLevel = (GoalLevel)challengePointPromotion.getGoalLevels().iterator().next();

    getChallengepointAwardDAO().saveChallengepointAward( challengePointAward );
    return challengePointAward;
  }

}

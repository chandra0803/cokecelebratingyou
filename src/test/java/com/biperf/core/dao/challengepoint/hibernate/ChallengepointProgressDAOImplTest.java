
package com.biperf.core.dao.challengepoint.hibernate;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.biperf.core.dao.challengepoint.ChallengepointProgressDAO;
import com.biperf.core.dao.hibernate.BaseDAOTest;
import com.biperf.core.dao.participant.ParticipantDAO;
import com.biperf.core.dao.participant.hibernate.ParticipantDAOImplTest;
import com.biperf.core.dao.promotion.PromotionDAO;
import com.biperf.core.dao.promotion.hibernate.PromotionDAOImplTest;
import com.biperf.core.domain.challengepoint.ChallengepointProgress;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.ChallengePointPromotion;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.challengepoint.impl.ChallengepointProgressAssociationRequest;
import com.biperf.core.utils.ApplicationContextFactory;
import com.biperf.core.utils.HibernateSessionManager;

public class ChallengepointProgressDAOImplTest extends BaseDAOTest
{

  // ---------------------------------------------------------------------------
  // Test Methods
  // ---------------------------------------------------------------------------

  public void testSaveGetChallengepointProgressById()
  {
    String uniqueString = String.valueOf( System.currentTimeMillis() % 7503032 );

    ChallengepointProgressDAO challengePointProgressDAO = getChallengepointProgressDAO();

    ChallengepointProgress expectedChallengepointProgress = buildAndSaveChallengepointProgress( uniqueString );

    // Test Save
    // challengePointProgressDAO.saveChallengepointProgress( expectedChallengepointProgress );

    flushAndClearSession();
    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
    associationRequestCollection.add( new ChallengepointProgressAssociationRequest( ChallengepointProgressAssociationRequest.ALL ) );
    // 1. Test Get by Pax Level Id
    ChallengepointProgress actualChallengepointProgress = challengePointProgressDAO.getChallengepointProgressById( expectedChallengepointProgress.getId() );

    assertEquals( "Test Get by progress Id: ChallengepointProgresss are not equal", expectedChallengepointProgress.getId(), actualChallengepointProgress.getId() );

    // 3. Test Get by Promotion Id and Participant's User Id
    List<ChallengepointProgress> actualLevelChallengepointProgress3 = challengePointProgressDAO
        .getChallengepointProgressByPromotionIdAndUserId( expectedChallengepointProgress.getChallengePointPromotion().getId(),
                                                          expectedChallengepointProgress.getParticipant().getId(),
                                                          associationRequestCollection );
    assertEquals( "Progress not got persisted in database", 1, actualLevelChallengepointProgress3.size() );

    // 4. Test Get by Promotion Id and Participant's User Id
    List actualLevelChallengepointProgress4 = challengePointProgressDAO.getChallengepointProgressByPromotionIdAndUserId( new Long( 0 ), new Long( 0 ), associationRequestCollection ); // pass
                                                                                                                                                                                       // such
                                                                                                                                                                                       // that
                                                                                                                                                                                       // no
                                                                                                                                                                                       // record
                                                                                                                                                                                       // returns
    assertEquals( "Progress not got persisted in database", 0, actualLevelChallengepointProgress4.size() );

    // 5. Test Get by Promotion Id and Participant's User Id
    // List actualListProgress5 =
    // challengePointChallengepointProgressDAO.getAllChallengepointProgresssByPromotionIdAndUserId(new
    // Long(0), new Long(0)); //pass such that no record returns
    // assertEquals( "Test Get by Participant's User Id: is null",null, actualListProgress5);

  }

  // ---------------------------------------------------------------------------
  // Private Methods
  // ---------------------------------------------------------------------------

  /**
   * Returns a {@link ChallengepointProgressDAO} object.
   * 
   * @return a {@link ChallengepointProgressDAO} object.
   */
  private static ChallengepointProgressDAO getChallengepointProgressDAO()
  {
    return (ChallengepointProgressDAO)ApplicationContextFactory.getApplicationContext().getBean( ChallengepointProgressDAO.BEAN_NAME );
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
   * Builds a {@link ChallengepointProgress} object given a unique string.
   * 
   * @param uniqueString a unique string.
   * @return a new {@link ChallengepointProgress} object.
   */
  public static ChallengepointProgress buildAndSaveChallengepointProgress( String uniqueString )
  {
    Promotion promotion = PromotionDAOImplTest.buildChallengePointPromotion( "MyLevelChallengePointPromo-" + uniqueString );

    getPromotionDAO().save( promotion );
    return buildAndSaveChallengepointProgress( uniqueString, promotion );
  }

  /**
   * Builds a {@link ChallengepointProgressDAO} object given a unique string.
   * 
   * @param uniqueString a unique string.
   * @param promotion
   * @return a new {@link ChallengepointProgressDAO} object.
   */
  public static ChallengepointProgress buildAndSaveChallengepointProgress( String uniqueString, Promotion promotion )
  {
    ChallengepointProgress challengePointProgress = new ChallengepointProgress();
    Participant newParticipant = ParticipantDAOImplTest.buildUniqueParticipant( "MyPax-" + uniqueString );
    newParticipant = getParticipantDAO().saveParticipant( newParticipant );
    HibernateSessionManager.getSession().flush();

    // flushAndClearSession();
    challengePointProgress.setParticipant( newParticipant );
    challengePointProgress.setType( "increment" );
    challengePointProgress.setSubmissionDate( new Date() );
    ChallengePointPromotion challengePointPromotion = (ChallengePointPromotion)promotion;
    challengePointProgress.setChallengePointPromotion( challengePointPromotion );
    // ChallengePointLevel cpLevel =
    // (ChallengePointLevel)challengePointPromotion.getLevels().iterator().next();

    getChallengepointProgressDAO().saveChallengepointProgress( challengePointProgress );
    return challengePointProgress;
  }

  /**
   * Builds a {@link ChallengepointProgressDAO} object given a unique string.
   * 
   * @param uniqueString a unique string.
   * @param promotion
   * @return a new {@link ChallengepointProgressDAO} object.
   */
  public static ChallengepointProgress buildChallengepointProgress( String uniqueString, Promotion promotion )
  {
    ChallengepointProgress challengePointProgress = new ChallengepointProgress();
    Participant newParticipant = ParticipantDAOImplTest.buildUniqueParticipant( "MyPax-" + uniqueString );
    // newParticipant = getParticipantDAO().saveParticipant( newParticipant );
    // HibernateSessionManager.getSession().flush();

    // flushAndClearSession();
    challengePointProgress.setParticipant( newParticipant );
    challengePointProgress.setType( "increment" );
    challengePointProgress.setSubmissionDate( new Date() );
    challengePointProgress.setQuantity( new BigDecimal( 50 ) );
    ChallengePointPromotion challengePointPromotion = (ChallengePointPromotion)promotion;
    challengePointProgress.setChallengePointPromotion( challengePointPromotion );
    // ChallengePointLevel cpLevel =
    // (ChallengePointLevel)challengePointPromotion.getLevels().iterator().next();

    // getChallengepointProgressDAO().saveChallengepointProgress(challengePointProgress);
    return challengePointProgress;
  }
}

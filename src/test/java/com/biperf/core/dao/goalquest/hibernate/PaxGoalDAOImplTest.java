
package com.biperf.core.dao.goalquest.hibernate;

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
import com.biperf.core.domain.promotion.GoalLevel;
import com.biperf.core.domain.promotion.GoalQuestPromotion;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.goalquest.impl.PaxGoalAssociationRequest;
import com.biperf.core.utils.ApplicationContextFactory;
import com.biperf.core.utils.HibernateSessionManager;

public class PaxGoalDAOImplTest extends BaseDAOTest
{

  // ---------------------------------------------------------------------------
  // Test Methods
  // ---------------------------------------------------------------------------

  public void testSaveGetGoalQuestPaxGoalById()
  {
    String uniqueString = String.valueOf( System.currentTimeMillis() % 7503032 );

    PaxGoalDAO goalQuestPaxGoalDAO = getGoalQuestPaxGoalDAO();

    PaxGoal expectedGoalQuestPaxGoal = buildAndSaveGoalQuestPaxGoal( uniqueString );

    // Test Save
    goalQuestPaxGoalDAO.savePaxGoal( expectedGoalQuestPaxGoal );

    flushAndClearSession();

    // 1. Test Get by Pax Goal Id
    PaxGoal actualGoalQuestPaxGoal = goalQuestPaxGoalDAO.getPaxGoalById( expectedGoalQuestPaxGoal.getId() );
    assertEquals( "Test Get by Pax Goal Id: GoalQuestPaxGoals are not equal", expectedGoalQuestPaxGoal, actualGoalQuestPaxGoal );

    // 2. Test Get by Pax Goal Id with Assoication Request - hydrate all pieces
    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
    associationRequestCollection.add( new PaxGoalAssociationRequest( PaxGoalAssociationRequest.ALL ) );
    PaxGoal actualGoalQuestPaxGoal2 = goalQuestPaxGoalDAO.getPaxGoalByIdWithAssociations( expectedGoalQuestPaxGoal.getId(), associationRequestCollection );
    assertEquals( "Test Get by Assoication Request: GoalQuestPaxGoals are not equal", expectedGoalQuestPaxGoal, actualGoalQuestPaxGoal2 );
    assertNotNull( "Test Get by Assoication Request: Expected promotion to be not null.", actualGoalQuestPaxGoal2.getGoalQuestPromotion() );
    assertNotNull( "Test Get by Assoication Request: Expected participant to be not null.", actualGoalQuestPaxGoal2.getParticipant() );
    assertNotNull( "Test Get by Assoication Request: Expected goal level to be not null.", actualGoalQuestPaxGoal2.getGoalLevel() );

    // 3. Test Get by Promotion Id and Participant's User Id
    PaxGoal actualGoalQuestPaxGoal3 = goalQuestPaxGoalDAO.getPaxGoalByPromotionIdAndUserId( expectedGoalQuestPaxGoal.getGoalQuestPromotion().getId(),
                                                                                            expectedGoalQuestPaxGoal.getParticipant().getId() );
    assertEquals( "Test Get by Participant's User Id: GoalQuestPaxGoals are not equal", expectedGoalQuestPaxGoal, actualGoalQuestPaxGoal3 );

    // 4. Test Get by Promotion Id with Assoication Request - hydrate all pieces
    AssociationRequestCollection associationRequestCollection2 = new AssociationRequestCollection();
    associationRequestCollection2.add( new PaxGoalAssociationRequest( PaxGoalAssociationRequest.ALL ) );
    List<PaxGoal> listOfPaxGoals = goalQuestPaxGoalDAO.getPaxGoalByPromotionId( expectedGoalQuestPaxGoal.getGoalQuestPromotion().getId(), associationRequestCollection );
    PaxGoal actualGoalQuestPaxGoal4 = (PaxGoal)listOfPaxGoals.get( 0 );
    assertEquals( "Test Get by Assoication Request: GoalQuestPaxGoals are not equal", expectedGoalQuestPaxGoal, actualGoalQuestPaxGoal4 );
    assertNotNull( "Test Get by Assoication Request: Expected promotion to be not null.", actualGoalQuestPaxGoal4.getGoalQuestPromotion() );
    assertNotNull( "Test Get by Assoication Request: Expected participant to be not null.", actualGoalQuestPaxGoal4.getParticipant() );
    assertNotNull( "Test Get by Assoication Request: Expected goal level to be not null.", actualGoalQuestPaxGoal4.getGoalLevel() );

  }

  public void testGetUserIdsWithPaxGoalByPromotionId()
  {
    String uniqueString = String.valueOf( System.currentTimeMillis() % 7503032 );

    PaxGoalDAO goalQuestPaxGoalDAO = getGoalQuestPaxGoalDAO();

    PaxGoal expectedGoalQuestPaxGoal = buildAndSaveGoalQuestPaxGoal( uniqueString );

    // Test Save
    goalQuestPaxGoalDAO.savePaxGoal( expectedGoalQuestPaxGoal );

    flushAndClearSession();

    Long promotionId = expectedGoalQuestPaxGoal.getGoalQuestPromotion().getId();

    List usersWithPaxGoal = goalQuestPaxGoalDAO.getUserIdsWithPaxGoalByPromotionId( promotionId );
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
  private PaxGoalDAO getGoalQuestPaxGoalDAO()
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
  public static PaxGoal buildAndSaveGoalQuestPaxGoal( String uniqueString )
  {
    Promotion promotion = PromotionDAOImplTest.buildGoalQuestPromotion( "MyGoalQuestPromo-" + uniqueString );
    getPromotionDAO().save( promotion );
    return buildAndSaveGoalQuestPaxGoal( uniqueString, promotion );
  }

  /**
   * Builds a {@link PaxGoalDAO} object given a unique string.
   * 
   * @param uniqueString a unique string.
   * @param promotion
   * @return a new {@link PaxGoalDAO} object.
   */
  public static PaxGoal buildAndSaveGoalQuestPaxGoal( String uniqueString, Promotion promotion )
  {
    PaxGoal goalQuestPaxGoal = new PaxGoal();

    Participant newParticipant = ParticipantDAOImplTest.buildUniqueParticipant( "MyPax-" + uniqueString );
    newParticipant = getParticipantDAO().saveParticipant( newParticipant );
    HibernateSessionManager.getSession().flush();
    // flushAndClearSession();
    goalQuestPaxGoal.setParticipant( newParticipant );
    GoalQuestPromotion goalQuestPromotion = (GoalQuestPromotion)promotion;
    goalQuestPaxGoal.setGoalQuestPromotion( goalQuestPromotion );
    GoalLevel goalLevel = (GoalLevel)goalQuestPromotion.getGoalLevels().iterator().next();
    goalQuestPaxGoal.setGoalLevel( goalLevel );
    goalQuestPaxGoal.setBaseQuantity( new BigDecimal( 10 ) );
    goalQuestPaxGoal.setOverrideQuantity( new BigDecimal( 1 ) );
    goalQuestPaxGoal.setCurrentValue( new BigDecimal( 0 ) );

    return goalQuestPaxGoal;
  }

}

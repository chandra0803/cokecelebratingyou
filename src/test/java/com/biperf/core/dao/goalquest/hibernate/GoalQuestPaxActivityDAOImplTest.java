/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.dao.goalquest.hibernate;

import java.math.BigDecimal;
import java.util.Date;

import com.biperf.core.dao.activity.ActivityDAO;
import com.biperf.core.dao.goalquest.GoalQuestPaxActivityDAO;
import com.biperf.core.dao.hibernate.BaseDAOTest;
import com.biperf.core.dao.participant.ParticipantDAO;
import com.biperf.core.dao.participant.hibernate.ParticipantDAOImplTest;
import com.biperf.core.dao.promotion.PromotionDAO;
import com.biperf.core.dao.promotion.hibernate.PromotionDAOImplTest;
import com.biperf.core.domain.enums.GoalQuestPaxActivityStatus;
import com.biperf.core.domain.enums.GoalQuestPaxActivityType;
import com.biperf.core.domain.enums.ProgressTransactionType;
import com.biperf.core.domain.goalquest.GoalQuestParticipantActivity;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.GoalQuestPromotion;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.utils.ApplicationContextFactory;
import com.biperf.core.utils.HibernateSessionManager;

/*
 * GoalQuestPaxActivityDAOImplTest <p> <b>Change History:</b><br> <table border="1"> <tr> <th>Author</th>
 * <th>Date</th> <th>Version</th> <th>Comments</th> </tr> <tr> <td>meadows</td> <td>Jan
 * 01, 2007</td> <td>1.0</td> <td>created</td> </tr> </table> </p>
 * 
 *
 */

public class GoalQuestPaxActivityDAOImplTest extends BaseDAOTest
{

  // ---------------------------------------------------------------------------
  // Test Methods
  // ---------------------------------------------------------------------------

  public void testDeleteGoalQuestPaxActivity()
  {
    GoalQuestPaxActivityDAO goalQuestPaxActivityDAO = getGoalQuestPaxActivityDAO();

    GoalQuestParticipantActivity goalQuestParticipantActivity = buildAndSaveGoalQuestPaxActivity( getUniqueString() );
    goalQuestPaxActivityDAO.saveGoalQuestPaxActivity( goalQuestParticipantActivity );
    flushAndClearSession();

    GoalQuestParticipantActivity retrievedGoalQuestParticipantActivity = goalQuestPaxActivityDAO.getGoalQuestParticipantActivityById( goalQuestParticipantActivity.getId() );
    assertNotNull( retrievedGoalQuestParticipantActivity );

    goalQuestPaxActivityDAO.deleteGoalQuestPaxActivity( retrievedGoalQuestParticipantActivity );

    GoalQuestParticipantActivity newGQPaxActivity = goalQuestPaxActivityDAO.getGoalQuestParticipantActivityById( retrievedGoalQuestParticipantActivity.getId() );
    if ( newGQPaxActivity != null )
    {
      fail( "Expected method goalQuestPaxActivityDAO.getGoalQuestParticipantActivityById to return null." );
    }
  }

  public void testSaveGetGoalQuestPaxActivityById()
  {
    String uniqueString = String.valueOf( System.currentTimeMillis() % 5503032 );

    GoalQuestPaxActivityDAO goalQuestPaxActivityDAO = getGoalQuestPaxActivityDAO();

    GoalQuestParticipantActivity expectedGoalQuestParticipantActivity = buildAndSaveGoalQuestPaxActivity( uniqueString );
    goalQuestPaxActivityDAO.saveGoalQuestPaxActivity( expectedGoalQuestParticipantActivity );
    flushAndClearSession();

    GoalQuestParticipantActivity actualGoalQuestPaxActivity = goalQuestPaxActivityDAO.getGoalQuestParticipantActivityById( expectedGoalQuestParticipantActivity.getId() );
    assertEquals( "GoalQuestParticipantActivitys are not equal", expectedGoalQuestParticipantActivity, actualGoalQuestPaxActivity );
  }

  // ---------------------------------------------------------------------------
  // Private Methods
  // ---------------------------------------------------------------------------

  /**
   * Returns an {@link ActivityDAO} object.
   * 
   * @return an {@link ActivityDAO} object.
   */
  private ActivityDAO getActivityDAO()
  {
    return (ActivityDAO)ApplicationContextFactory.getApplicationContext().getBean( ActivityDAO.BEAN_NAME );
  }

  /**
   * Returns a {@link GoalQuestPaxActivityDAO} object.
   * 
   * @return a {@link GoalQuestPaxActivityDAO} object.
   */
  private GoalQuestPaxActivityDAO getGoalQuestPaxActivityDAO()
  {
    return (GoalQuestPaxActivityDAO)ApplicationContextFactory.getApplicationContext().getBean( GoalQuestPaxActivityDAO.BEAN_NAME );
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
   * Builds a {@link GoalQuestParticipantActivity} object given a unique string.
   * 
   * @param uniqueString a unique string.
   * @return a new {@link GoalQuestParticipantActivity} object.
   */
  public static GoalQuestParticipantActivity buildAndSaveGoalQuestPaxActivity( String uniqueString )
  {
    Promotion promotion = PromotionDAOImplTest.buildGoalQuestPromotion( "promo-" + uniqueString );
    getPromotionDAO().save( promotion );
    return buildAndSaveGoalQuestPaxActivity( uniqueString, promotion );
  }

  /**
   * Builds a {@link GoalQuestPaxActivityDAO} object given a unique string.
   * 
   * @param uniqueString a unique string.
   * @param promotion
   * @return a new {@link GoalQuestPaxActivityDAO} object.
   */
  public static GoalQuestParticipantActivity buildAndSaveGoalQuestPaxActivity( String uniqueString, Promotion promotion )
  {
    GoalQuestParticipantActivity goalQuestParticipantActivity = new GoalQuestParticipantActivity();

    Participant newParticipant = ParticipantDAOImplTest.buildUniqueParticipant( "Pax" + uniqueString );
    newParticipant = getParticipantDAO().saveParticipant( newParticipant );
    HibernateSessionManager.getSession().flush();
    // flushAndClearSession();
    goalQuestParticipantActivity.setGoalQuestPromotion( (GoalQuestPromotion)promotion );
    goalQuestParticipantActivity.setParticipant( newParticipant );
    goalQuestParticipantActivity.setType( GoalQuestPaxActivityType.lookup( GoalQuestPaxActivityType.INCREMENTAL ) );
    goalQuestParticipantActivity.setAutomotive( true );
    goalQuestParticipantActivity.setVin( "TESTVIN" );
    goalQuestParticipantActivity.setStatus( GoalQuestPaxActivityStatus.lookup( GoalQuestPaxActivityStatus.PENDING ) );
    goalQuestParticipantActivity.setQuantity( new BigDecimal( "55.44" ) );
    goalQuestParticipantActivity.setSubmissionDate( new Date() );
    goalQuestParticipantActivity.setTransactionType( ProgressTransactionType.lookup( ProgressTransactionType.SALE ) );

    return goalQuestParticipantActivity;
  }

  /**
   * Returns an account number.
   * 
   * @param uniqueString used to generate the account number.
   * @return an account number.
   */
  private static String buildAccountNumber( String uniqueString )
  {
    return ( uniqueString.length() > 30 ) ? uniqueString.substring( 0, 29 ) : uniqueString;
  }

  public void testGetPaxActivityByPromotionIdAndUserIdAndSubDate()
  {
    GoalQuestPaxActivityDAO goalQuestPaxActivityDAO = getGoalQuestPaxActivityDAO();

    GoalQuestParticipantActivity goalQstPaxActivity = buildAndSaveGoalQuestPaxActivity( getUniqueString() );
    goalQuestPaxActivityDAO.saveGoalQuestPaxActivity( goalQstPaxActivity );
    flushAndClearSession();

    GoalQuestParticipantActivity GoalQuestPaxActivity = goalQuestPaxActivityDAO.getPaxActivityByPromotionIdAndUserIdAndSubDate( 532L, 12L, goalQstPaxActivity.getSubmissionDate() );
    assertNull( GoalQuestPaxActivity );
  }

}
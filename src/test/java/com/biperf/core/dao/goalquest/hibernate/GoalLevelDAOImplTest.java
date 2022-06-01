
package com.biperf.core.dao.goalquest.hibernate;

import com.biperf.core.dao.goalquest.GoalLevelDAO;
import com.biperf.core.dao.hibernate.BaseDAOTest;
import com.biperf.core.dao.promotion.PromotionDAO;
import com.biperf.core.dao.promotion.hibernate.PromotionDAOImplTest;
import com.biperf.core.domain.promotion.AbstractGoalLevel;
import com.biperf.core.domain.promotion.GoalLevel;
import com.biperf.core.domain.promotion.GoalQuestPromotion;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.utils.ApplicationContextFactory;
import com.biperf.core.utils.HibernateSessionManager;

public class GoalLevelDAOImplTest extends BaseDAOTest
{

  // ---------------------------------------------------------------------------
  // Test Methods
  // ---------------------------------------------------------------------------

  public void testSaveGetGoalQuestGoalLevelById()
  {
    String uniqueString = String.valueOf( System.currentTimeMillis() % 7503032 );

    GoalLevel expectedGoalLevel = buildAndSaveGoalQuestGoalLevel( uniqueString );

    flushAndClearSession();

    // 1. Test Get by Pax Goal Id
    AbstractGoalLevel actualGoalLevel = getGoalLevelDAO().getGoalLevelById( expectedGoalLevel.getId() );
    assertEquals( "Test Get by Pax Goal Id: GoalQuestGoalLevels are not equal", expectedGoalLevel, actualGoalLevel );

  }

  public void testgetMaxSequence()
  {
    String uniqueString = String.valueOf( System.currentTimeMillis() % 7503032 );

    GoalLevel expectedGoalLevel = buildAndSaveGoalQuestGoalLevel( uniqueString );

    flushAndClearSession();

    // 1. Test Get by Pax Goal Id
    int max = getGoalLevelDAO().getMaxSequence();
    assertTrue( max != 0 );

  }

  // ---------------------------------------------------------------------------
  // Private Methods
  // ---------------------------------------------------------------------------

  /**
   * Returns a GoalLevelDAO object.
   * 
   * @return a {@link GoalLevelDAO} object.
   */
  private static GoalLevelDAO getGoalLevelDAO()
  {
    return (GoalLevelDAO)ApplicationContextFactory.getApplicationContext().getBean( GoalLevelDAO.BEAN_NAME );
  }

  private static PromotionDAO getPromotionDAO()
  {
    return (PromotionDAO)ApplicationContextFactory.getApplicationContext().getBean( PromotionDAO.BEAN_NAME );
  }

  // ---------------------------------------------------------------------------
  // Build Methods
  // ---------------------------------------------------------------------------

  /**
   * Builds a {@link GoalLevel} object given a unique string.
   * 
   * @param uniqueString a unique string.
   * @return a new {@link GoalLevel} object.
   */
  public static GoalLevel buildAndSaveGoalQuestGoalLevel( String uniqueString )
  {
    Promotion promotion = PromotionDAOImplTest.buildGoalQuestPromotion( "MyGoalQuestPromo-" + uniqueString );
    getPromotionDAO().save( promotion );
    HibernateSessionManager.getSession().flush();
    GoalLevel goalLevel = (GoalLevel) ( (GoalQuestPromotion)promotion ).getGoalLevels().iterator().next();
    return goalLevel;
  }

}

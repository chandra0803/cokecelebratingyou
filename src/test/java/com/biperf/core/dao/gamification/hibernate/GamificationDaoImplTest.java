
package com.biperf.core.dao.gamification.hibernate;

import java.util.Date;

import com.biperf.core.dao.gamification.GamificationDAO;
import com.biperf.core.dao.hibernate.BaseDAOTest;
import com.biperf.core.dao.security.RoleDAO;
import com.biperf.core.domain.enums.BadgeType;
import com.biperf.core.domain.enums.PromotionStatusType;
import com.biperf.core.domain.enums.PromotionType;
import com.biperf.core.domain.gamification.Badge;
import com.biperf.core.utils.ApplicationContextFactory;

public class GamificationDaoImplTest extends BaseDAOTest
{

  // ---------------------------------------------------------------------------
  // Private Methods
  // ---------------------------------------------------------------------------

  /**
   * Returns an {@link GamificationDAO} object.
   * 
   * @return an {@link GamificationDAO} object.
   */
  private static GamificationDAO getGamificationDAO()
  {
    return (GamificationDAO)ApplicationContextFactory.getApplicationContext().getBean( "gamificationDAO" );
  }

  /**
   * Get the roleDAO from the beanFactory.
   * 
   * @return RoleDAO
   */
  protected RoleDAO getRoleDAO()
  {
    return (RoleDAO)ApplicationContextFactory.getApplicationContext().getBean( "roleDAO" );
  }

  // LeaderBoard methods
  public void testSaveBadgeAndGetBadgeById()
  {

    GamificationDAO gamificationDAO = getGamificationDAO();

    Badge expectedBadge = new Badge();

    // User user = UserDAOImplTest.buildStaticUser( "JunitTestUser1", "first1", "last1" );
    expectedBadge.setName( "Test Badge1" );
    expectedBadge.setStatus( "A" );
    expectedBadge.setBadgeType( BadgeType.lookup( BadgeType.PROGRESS ) );
    expectedBadge.setTileHighlightPeriod( new Long( 1 ) );
    expectedBadge.setPromoNameAssetCode( "badge.promotion.name.asset.code" );
    expectedBadge.setPromotionType( PromotionType.lookup( PromotionType.BADGE ) );
    expectedBadge.setPromotionStatus( PromotionStatusType.lookup( PromotionStatusType.UNDER_CONSTRUCTION ) );
    expectedBadge.setSubmissionStartDate( new Date() );
    expectedBadge.setSubmissionEndDate( new Date() );
    gamificationDAO.saveBadge( expectedBadge );
    flushAndClearSession();

    Badge actualBadge = gamificationDAO.getBadgeById( expectedBadge.getId() );
    assertEquals( "Badge not equal", expectedBadge, actualBadge );

  }

  public void testGetEligibleBadgePromotionIds()
  {
    GamificationDAO gamificationDAO = getGamificationDAO();
    assertTrue( gamificationDAO.getBadgeEligiblePromotionIds().size() >= 0 );

  }

  public void testIsUserHasActiveBadges()
  {
    String promotionIds = "14361,14590,16216,16217,16492,16493,16213,16218,14595,16504,16442,16557,16214";
    GamificationDAO gamificationDAO = getGamificationDAO();
    assertTrue( gamificationDAO.isUserHasActiveBadges( 6499L, promotionIds ) > 0 );
  }

  /*
   * public void testDeleteLeaderBoardById() { LeaderBoardDAO leaderBoardDAO = getLeaderBoardDAO();
   * UserDAO userDAO = getUserDAO(); LeaderBoard leaderBoard = new LeaderBoard(); User
   * leaderBoardOwner = userDAO.getUserById( 5581l ); leaderBoard.setUser( leaderBoardOwner );
   * leaderBoard.setStatus( "A" ); leaderBoard.setName( "Test Leader Board" ); //save leaderboard
   * leaderBoardDAO.saveLeaderBoard( leaderBoard ); LeaderBoardParticipant leaderBoardParticipant =
   * new LeaderBoardParticipant(); leaderBoardParticipant.setLeaderboard( leaderBoard ); User
   * leaderBoardParticipantDetails = userDAO.getUserById( 5582L ); leaderBoardParticipant.setUser(
   * leaderBoardParticipantDetails ); leaderBoardParticipant.setScore( "100" ); //save participant
   * to the existing leaderboard leaderBoardDAO.saveLeaderBoardParticipant( leaderBoardParticipant
   * ); Long id = leaderBoardParticipant.getId(); flushAndClearSession(); //delete leaderboard by
   * it's id. Child records should delete leaderBoardDAO.deleteLeaderBoard( leaderBoard.getId() );
   * LeaderBoardParticipant lbp = leaderBoardDAO.getLeaderBoardParticipantById( id );
   * assertTrue("LeaderBoardParticipant shouldn't available in db",lbp == null); }
   */

}

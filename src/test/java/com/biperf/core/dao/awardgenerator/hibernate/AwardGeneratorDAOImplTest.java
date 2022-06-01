
package com.biperf.core.dao.awardgenerator.hibernate;

import com.biperf.core.dao.awardgenerator.AwardGeneratorDAO;
import com.biperf.core.dao.hibernate.BaseDAOTest;
import com.biperf.core.dao.promotion.PromotionDAO;
import com.biperf.core.dao.promotion.hibernate.PromotionDAOImplTest;
import com.biperf.core.domain.awardgenerator.AwardGenAward;
import com.biperf.core.domain.awardgenerator.AwardGenerator;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.utils.ApplicationContextFactory;
import com.biperf.core.utils.HibernateSessionManager;

/**
 * AwardGeneratorDAOImplTest
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
 * <td>chowdhur</td>
 * <td>Jul 20, 2013</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class AwardGeneratorDAOImplTest extends BaseDAOTest
{

  public void testSaveAwardGenerator()
  {
    AwardGenerator awardGenerator = buildAwardGenerator();

    AwardGenerator savedAwardGen = getAwardGeneratorDAO().saveAwardGenerator( awardGenerator );

    System.out.println( "Saved Award Gen: " + savedAwardGen );
    HibernateSessionManager.getSession().flush();
  }

  public static AwardGenerator buildAwardGenerator()
  {
    String uniqueString = buildUniqueString();
    AwardGenerator awardGenerator = new AwardGenerator();

    Promotion promotion = PromotionDAOImplTest.buildGoalQuestPromotion( "GOALQUEST" + uniqueString );
    getPromotionDAO().save( promotion );
    awardGenerator.setPromotion( promotion );
    awardGenerator.setName( "Birthday Awards" );
    awardGenerator.setExaminerField( "Examiner Field" );

    AwardGenAward awardGenAward = new AwardGenAward();
    awardGenAward.setYears( new Integer( 1 ) );
    awardGenAward.setAwardAmount( new Long( 100 ) );

    awardGenerator.addAwardGenAward( awardGenAward );

    return awardGenerator;
  }

  private static AwardGeneratorDAO getAwardGeneratorDAO()
  {
    return (AwardGeneratorDAO)ApplicationContextFactory.getApplicationContext().getBean( "awardGeneratorDAO" );
  }

  private static PromotionDAO getPromotionDAO()
  {
    return (PromotionDAO)ApplicationContextFactory.getApplicationContext().getBean( "promotionDAO" );
  }
}

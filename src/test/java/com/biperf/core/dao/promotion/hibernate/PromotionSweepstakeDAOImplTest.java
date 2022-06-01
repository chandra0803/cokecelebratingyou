/**
 * 
 */

package com.biperf.core.dao.promotion.hibernate;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.biperf.core.dao.hibernate.BaseDAOTest;
import com.biperf.core.dao.participant.hibernate.ParticipantDAOImplTest;
import com.biperf.core.dao.promotion.PromotionDAO;
import com.biperf.core.dao.promotion.PromotionSweepstakeDAO;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.PromotionSweepstake;
import com.biperf.core.domain.promotion.PromotionSweepstakeWinner;
import com.biperf.core.service.SAO;
import com.biperf.core.utils.ApplicationContextFactory;
import com.biperf.core.utils.BeanLocator;

/**
 * PromotionSweepstakeDAOImplTest.
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
 * <td>asondgeroth</td>
 * <td>Nov 3, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class PromotionSweepstakeDAOImplTest extends BaseDAOTest
{
  private PromotionSweepstakeDAO promotionSweepstakeDAO;
  private PromotionDAO promotionDAO;

  /**
   * Overridden from
   * 
   * @see junit.framework.TestCase#setUp() Sets up the fixture, for example, open a network
   *      connection. This method is called before a test is executed.
   * @throws Exception
   */
  public void setUp() throws Exception
  {
    super.setUp();

    promotionSweepstakeDAO = getPromotionSweepstakeDAO();
    promotionDAO = getPromotionDAO();
  }

  /**
   * Uses the ApplicationContextFactory to look up the PromotionSweepstakeDAO implementation.
   * 
   * @return PromotionSweepstakeDAO
   */
  private PromotionSweepstakeDAO getPromotionSweepstakeDAO()
  {
    return (PromotionSweepstakeDAO)ApplicationContextFactory.getApplicationContext().getBean( PromotionSweepstakeDAO.BEAN_NAME );
  }

  /**
   * Uses the ApplicationContextFactory to look up the PromotionDAO implementation.
   * 
   * @return PromotionDAO
   */
  private PromotionDAO getPromotionDAO()
  {
    return (PromotionDAO)ApplicationContextFactory.getApplicationContext().getBean( PromotionDAO.BEAN_NAME );
  }

  /**
   * Tests retrieval of all the PromotionSweepstakes sorted by date from the database.
   */
  public void testGetAllPromotionSweepstakesListSortedByDate()
  {

    Promotion promotion = PromotionDAOImplTest.buildRecognitionPromotion( "Sweepstake3" );
    Set promotionSweepstakes = new HashSet();
    PromotionSweepstake savedPromotionSweepstake3 = PromotionSweepstakeDAOImplTest.buildPromotionSweepstake();
    Date twoDaysEarlier = new Date( System.currentTimeMillis() - ( 8640000 * 2 ) );
    savedPromotionSweepstake3.setEndDate( twoDaysEarlier );
    savedPromotionSweepstake3.setProcessed( true );

    PromotionSweepstake savedPromotionSweepstake1 = PromotionSweepstakeDAOImplTest.buildPromotionSweepstake();
    // Same end date as some other sweepstakes
    savedPromotionSweepstake1.setEndDate( twoDaysEarlier );
    savedPromotionSweepstake1.setProcessed( true );

    PromotionSweepstake savedPromotionSweepstake2 = PromotionSweepstakeDAOImplTest.buildPromotionSweepstake();
    Date oneDayEarlier = new Date( System.currentTimeMillis() - 8640000 );
    savedPromotionSweepstake2.setEndDate( oneDayEarlier );
    savedPromotionSweepstake2.setProcessed( true );

    promotionSweepstakes.add( savedPromotionSweepstake3 );
    promotionSweepstakes.add( savedPromotionSweepstake2 );
    promotionSweepstakes.add( savedPromotionSweepstake1 );

    new PromotionDAOImpl().save( promotion );

    List promotionList = promotionSweepstakeDAO.getAllPromotionSweepstakesListSortedByDate();

    Iterator iter = promotionList.iterator();

    // check order
    long prevDateMilli = 0;
    while ( iter.hasNext() )
    {
      PromotionSweepstake promotionSweepstake = (PromotionSweepstake)iter.next();
      long endDateMilli = promotionSweepstake.getEndDate().getTime();
      assertTrue( "Sweepstakes ordering was not in correct order: \n" + "endDateMilli=" + endDateMilli + " should be greater than " + "prevDateMilli=" + prevDateMilli, endDateMilli >= prevDateMilli );

      prevDateMilli = endDateMilli;
    }

  }

  /**
   * Tests retrieval of all the PromotionSweepstakes sorted by date from the database.
   */
  public void testGetAllPromotionSweepstakesListByPromotionIdSortedByDate()
  {

    Promotion promotion = PromotionDAOImplTest.buildRecognitionPromotion( "Sweepstake3" );
    // promotion.setId(new Long(expectedPromotionId));
    Set promotionSweepstakes = new HashSet();
    PromotionSweepstake savedPromotionSweepstake3 = PromotionSweepstakeDAOImplTest.buildPromotionSweepstake();
    Date twoDaysEarlier = new Date( System.currentTimeMillis() - ( 8640000 * 2 ) );
    savedPromotionSweepstake3.setEndDate( twoDaysEarlier );
    savedPromotionSweepstake3.setProcessed( false );

    PromotionSweepstake savedPromotionSweepstake1 = PromotionSweepstakeDAOImplTest.buildPromotionSweepstake();

    PromotionSweepstake savedPromotionSweepstake2 = PromotionSweepstakeDAOImplTest.buildPromotionSweepstake();
    Date oneDayEarlier = new Date( System.currentTimeMillis() - 8640000 );
    savedPromotionSweepstake2.setEndDate( oneDayEarlier );

    promotionSweepstakes.add( savedPromotionSweepstake3 );
    promotionSweepstakes.add( savedPromotionSweepstake1 );

    // before insert id should be null
    assertNull( promotion.getId() );
    new PromotionDAOImpl().save( promotion );
    // after insert id shoudl be set
    assertNotNull( promotion.getId() );
    Long expectedPromotionId = promotion.getId();

    List promotionSweepstakesList = promotionSweepstakeDAO.getAllPromotionSweepstakesListByPromotionIdSortedByDate( expectedPromotionId );

    Iterator iter = promotionSweepstakesList.iterator();

    // check order and promotionId for sweepstakes
    long prevDateMilli = 0;
    while ( iter.hasNext() )
    {
      PromotionSweepstake promotionSweepstake = (PromotionSweepstake)iter.next();
      long endDateMilli = promotionSweepstake.getEndDate().getTime();
      assertTrue( "Sweepstakes ordering was not in correct order.", endDateMilli > prevDateMilli );
      assertTrue( "Sweepstake did not have the correct promotionId.", promotionSweepstake.getPromotion().getId().longValue() == expectedPromotionId.longValue() );
      prevDateMilli = endDateMilli;
    }

  }

  /**
   * Builds a PromotionSweepstake from scratch. Test getting all products in the database.
   */
  public void testGetAllProducts()
  {
    List expectedList = new ArrayList();

    String uniqueString = "Test" + ( System.currentTimeMillis() % 3122131 );
    Promotion promotion = PromotionDAOImplTest.buildRecognitionPromotion( uniqueString );
    promotion = promotionDAO.save( promotion );

    // create sweepstakes list
    PromotionSweepstake savedPromotionSweepstake1 = PromotionSweepstakeDAOImplTest.buildPromotionSweepstake( promotion );
    savedPromotionSweepstake1 = promotionSweepstakeDAO.save( savedPromotionSweepstake1 );
    expectedList.add( savedPromotionSweepstake1 );

    PromotionSweepstake savedPromotionSweepstake2 = PromotionSweepstakeDAOImplTest.buildPromotionSweepstake( promotion );
    savedPromotionSweepstake2 = promotionSweepstakeDAO.save( savedPromotionSweepstake2 );
    expectedList.add( savedPromotionSweepstake2 );

    PromotionSweepstake savedPromotionSweepstake3 = PromotionSweepstakeDAOImplTest.buildPromotionSweepstake( promotion );
    savedPromotionSweepstake3 = promotionSweepstakeDAO.save( savedPromotionSweepstake3 );
    expectedList.add( savedPromotionSweepstake3 );

    expectedList.add( savedPromotionSweepstake1 );
    expectedList.add( savedPromotionSweepstake2 );
    expectedList.add( savedPromotionSweepstake3 );

    flushAndClearSession();

    List actualList = promotionSweepstakeDAO.getAllPromotionSweepstakes();

    assertTrue( "The list of PromotionSweepstakes from the database doesn't contain the expected set", actualList.containsAll( expectedList ) );

  }

  /**
   * Builds a PromotionSweepstake from scratch.
   * 
   * @return PromotionSweepstake
   */
  public static PromotionSweepstake buildPromotionSweepstake()
  {
    PromotionSweepstake promotionSweepstake = new PromotionSweepstake();
    promotionSweepstake.setStartDate( new Date() );
    promotionSweepstake.setEndDate( new Date() );
    promotionSweepstake.setProcessed( false );

    return promotionSweepstake;
  }

  /**
   * Builds a PromotionSweepstake from scratch.
   * 
   * @return PromotionSweepstake
   */
  public static PromotionSweepstake buildPromotionSweepstake( Promotion promotion )
  {
    PromotionSweepstake promotionSweepstake = buildPromotionSweepstake();
    promotionSweepstake.setPromotion( promotion );

    return promotionSweepstake;
  }

  /**
   * Builds a PromotionSweepstakeWinner from scratch.
   * 
   * @return PromotionSweepstakeWinner
   */
  public static PromotionSweepstakeWinner buildPromotionSweepstakeWinner()
  {
    String uniqueString = "Test" + ( System.currentTimeMillis() % 3122131 );

    PromotionSweepstakeWinner sweepstakeWinner = new PromotionSweepstakeWinner();
    sweepstakeWinner.setWinnerType( PromotionSweepstakeWinner.GIVER_TYPE );

    sweepstakeWinner.setParticipant( ParticipantDAOImplTest.buildUniqueParticipant( uniqueString ) );

    return sweepstakeWinner;
  }

  protected static SAO getService( String beanName )
  {
    return (SAO)BeanLocator.getBean( beanName );
  }
}
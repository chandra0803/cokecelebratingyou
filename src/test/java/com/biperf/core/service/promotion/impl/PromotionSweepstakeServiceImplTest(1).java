/**
 * 
 */

package com.biperf.core.service.promotion.impl;

import java.util.Date;

import org.jmock.Mock;

import com.biperf.core.dao.participant.hibernate.ParticipantDAOImplTest;
import com.biperf.core.dao.promotion.PromotionSweepstakeDAO;
import com.biperf.core.dao.promotion.hibernate.PromotionDAOImplTest;
import com.biperf.core.domain.promotion.PromotionSweepstake;
import com.biperf.core.domain.promotion.PromotionSweepstakeWinner;
import com.biperf.core.domain.promotion.RecognitionPromotion;
import com.biperf.core.service.BaseServiceTest;
import com.biperf.core.service.activity.ActivityService;
import com.biperf.core.service.promotion.PromotionEngineService;

/**
 * PromotionSweepstakeServiceImplTest.
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
 * <td>Nov 4, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class PromotionSweepstakeServiceImplTest extends BaseServiceTest
{
  private PromotionSweepstakeServiceImpl promotionSweepstakeServiceImpl = new PromotionSweepstakeServiceImpl();

  private Mock mockPromotionSweepstakeDAO = null;

  private Mock mockActivityService;

  private Mock mockPromotionEngineService;

  /**
   * Constructor to take the name of the test.
   * 
   * @param test
   */
  public PromotionSweepstakeServiceImplTest( String test )
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

    mockPromotionSweepstakeDAO = new Mock( PromotionSweepstakeDAO.class );
    promotionSweepstakeServiceImpl.setPromotionSweepstakeDAO( (PromotionSweepstakeDAO)mockPromotionSweepstakeDAO.proxy() );

    mockActivityService = new Mock( ActivityService.class );
    promotionSweepstakeServiceImpl.setActivityService( (ActivityService)mockActivityService.proxy() );

    mockPromotionEngineService = new Mock( PromotionEngineService.class );
    promotionSweepstakeServiceImpl.setPromotionEngineService( (PromotionEngineService)mockPromotionEngineService.proxy() );
  }

  /**
   * Test to save a promotion.
   */
  public void testSave()
  {
    // create a PromotionSweepstake object
    String uniqueString = "Test" + ( System.currentTimeMillis() % 3122131 );
    PromotionSweepstake expectedPromotionSweepstake = buildPromotionSweepstake( PromotionDAOImplTest.buildRecognitionPromotion( uniqueString ) );
    expectedPromotionSweepstake.setId( new Long( 1 ) );

    // PromotionSweepstakesDAO expected to call save once with the Promotion
    // which will return the Promotion expected
    mockPromotionSweepstakeDAO.expects( once() ).method( "save" ).with( same( expectedPromotionSweepstake ) ).will( returnValue( expectedPromotionSweepstake ) );

    // make the service call
    PromotionSweepstake actualPromotionSweepstake = promotionSweepstakeServiceImpl.save( expectedPromotionSweepstake );

    assertEquals( "Actual PromotionSweepstake didn't match with what is expected", expectedPromotionSweepstake, actualPromotionSweepstake );

    mockPromotionSweepstakeDAO.verify();
  }

  /**
   * Tests getting the PromotionSweepstake from the service through the DAO.
   */
  public void testGetById()
  {
    String uniqueString = "Test" + ( System.currentTimeMillis() % 3122131 );
    PromotionSweepstake expectedPromotionSweepstake = buildPromotionSweepstake( PromotionDAOImplTest.buildRecognitionPromotion( uniqueString ) );
    expectedPromotionSweepstake.setId( new Long( 1 ) );

    mockPromotionSweepstakeDAO.expects( once() ).method( "getPromotionSweepstakeById" ).with( same( expectedPromotionSweepstake.getId() ) ).will( returnValue( expectedPromotionSweepstake ) );

    PromotionSweepstake actualPromotionSweepstake = promotionSweepstakeServiceImpl.getPromotionSweepstakeById( expectedPromotionSweepstake.getId() );

    assertEquals( "Actual PromotionSweepstake does not match to what was expected", expectedPromotionSweepstake, actualPromotionSweepstake );

    mockPromotionSweepstakeDAO.verify();
  }

  /**
   * Tests deleting the PromotionSweepstake from the database through the service.
   * 
   * @throws Exception
   */
  public void testDelete() throws Exception
  {
    String uniqueString = "Test" + ( System.currentTimeMillis() % 3122131 );
    PromotionSweepstake expectedPromotionSweepstake = buildPromotionSweepstake( PromotionDAOImplTest.buildRecognitionPromotion( uniqueString ) );
    expectedPromotionSweepstake.setId( new Long( 1 ) );

    mockPromotionSweepstakeDAO.expects( once() ).method( "delete" ).with( same( expectedPromotionSweepstake ) );

    promotionSweepstakeServiceImpl.delete( expectedPromotionSweepstake );

    mockPromotionSweepstakeDAO.verify();
  }

  /**
   * Tests removing Winners from a PromotionSweepstake through the service.
   * 
   * @throws Exception
   */
  // public void testRemoveWinners() throws Exception
  // {
  // AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
  // associationRequestCollection.add( new
  // PromotionAssociationRequest(PromotionAssociationRequest.PROMOTION_SWEEPSTAKES) );
  //
  // RecognitionPromotion promo = PromotionServiceImplTest.buildRecognitionPromotion();
  // promo.setId( new Long( 1 ) );
  // PromotionSweepstake promotionSweepstake = buildPromotionSweepstake( promo );
  // List ids = new ArrayList( 2 );
  // Set winners = new HashSet( 2 );
  // PromotionSweepstakeWinner winner = buildWinner( promotionSweepstake );
  // winner.setId( new Long( 2 ) );
  // winners.add( winner );
  // ids.add( winner.getId() );
  // PromotionSweepstakeWinner winner2 = buildWinner( promotionSweepstake );
  // winner2.setId( new Long( 3 ) );
  // winners.add( winner2 );
  // ids.add( winner2.getId() );
  // promotionSweepstake.setWinners( winners );
  // Set sweeps = new HashSet( 1 );
  // sweeps.add( promotionSweepstake );
  // promo.setPromotionSweepstakes( sweeps );
  //
  // mockPromotionDAO.expects( once() ).method( "getPromotionByIdWithAssociations" )
  // .with( same( promo.getId() ), eq( associationRequestCollection ) )
  // .will( returnValue( promo ) );
  //
  // mockPromotionDAO.expects( once() ).method( "save" ).with( same( promo ) )
  // .will( returnValue( promo ) );
  //
  // promo = (RecognitionPromotion)promotionSweepstakeServiceImpl.removeWinners( ids, promo.getId()
  // );
  //
  // mockPromotionDAO.verify();
  // }
  /**
   * Tests processing a PromotionSweepstake through the service.
   * 
   * @throws Exception
   */
  // public void testProcessAward() throws Exception
  // {
  // Long expectedGiversAwardAmount = new Long( 5 );
  // Long expectedReceiversAwardAmount = new Long( 6 );
  //
  // AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
  // associationRequestCollection
  // .add( new PromotionAssociationRequest( PromotionAssociationRequest.PROMOTION_SWEEPSTAKES ) );
  //
  // RecognitionPromotion promo = PromotionServiceImplTest.buildRecognitionPromotion();
  // promo.setId( new Long( 1 ) );
  // promo.setSweepstakesPrimaryAwardAmount( expectedGiversAwardAmount );
  // promo.setSweepstakesSecondaryAwardAmount( expectedReceiversAwardAmount );
  // PromotionSweepstake promotionSweepstake = buildPromotionSweepstake( promo );
  //
  // PromotionSweepstakeWinner winner = buildWinner( promotionSweepstake );
  // winner.setId( new Long( 2 ) );
  // promotionSweepstake.addWinner( winner );
  //
  // Set sweeps = new HashSet( 1 );
  // sweeps.add( promotionSweepstake );
  // promo.setPromotionSweepstakes( sweeps );
  //
  // mockActivityService.expects( once() ).method( "saveActivities" );
  // mockPromotionEngineService.expects( once() ).method( "depositPostedPayouts" );
  // mockPromotionEngineService.expects( once() ).method( "calculatePayoutAndSaveResults" );
  //
  // promotionSweepstakeServiceImpl.processAward( promo.getId() );
  // }
  /**
   * Tests replacing Winners from a PromotionSweepstake through the service.
   * 
   * @throws Exception
   */

  // public void testReplaceWinners() throws Exception
  // {
  // AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
  // associationRequestCollection
  // .add( new PromotionAssociationRequest( PromotionAssociationRequest.PROMOTION_SWEEPSTAKES ) );
  //
  // RecognitionPromotion promo = PromotionServiceImplTest.buildRecognitionPromotion();
  // promo.setId( new Long( 1 ) );
  // PromotionSweepstake promotionSweepstake = buildPromotionSweepstake( promo );
  // List ids = new ArrayList( 2 );
  // Set winners = new HashSet( 2 );
  // PromotionSweepstakeWinner winner = buildWinner( promotionSweepstake );
  // winner.setId( new Long( 2 ) );
  // winners.add( winner );
  // ids.add( winner.getId() );
  // PromotionSweepstakeWinner winner2 = buildWinner( promotionSweepstake );
  // winner2.setId( new Long( 3 ) );
  // winners.add( winner2 );
  // ids.add( winner2.getId() );
  // promotionSweepstake.setWinners( winners );
  // Set sweeps = new HashSet( 1 );
  // sweeps.add( promotionSweepstake );
  // promo.setPromotionSweepstakes( sweeps );
  //
  // promotionSweepstakeServiceImpl.replaceWinners( ids, promo.getId() );
  // }
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
  public static PromotionSweepstake buildPromotionSweepstake( RecognitionPromotion promotion )
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
  public static PromotionSweepstakeWinner buildWinner( PromotionSweepstake promotionSweepstake )
  {
    String uniqueString = "Test" + ( System.currentTimeMillis() % 3122131 );

    PromotionSweepstakeWinner winner = new PromotionSweepstakeWinner();
    winner.setParticipant( ParticipantDAOImplTest.buildUniqueParticipant( uniqueString ) );
    winner.setRemoved( false );
    winner.setPromotionSweepstake( promotionSweepstake );

    return winner;
  }

}

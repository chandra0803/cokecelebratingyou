
package com.biperf.core.strategy.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.easymock.EasyMock;

import com.biperf.core.domain.activity.Activity;
import com.biperf.core.domain.activity.QuizActivity;
import com.biperf.core.domain.enums.MockPickListFactory;
import com.biperf.core.domain.enums.ParticipantStatus;
import com.biperf.core.domain.enums.SweepstakesWinnersType;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.PromotionSweepstake;
import com.biperf.core.domain.promotion.QuizPromotion;
import com.biperf.core.service.activity.ActivityService;

import junit.framework.TestCase;

/**
 * QuizWinnersListStrategyTest
 * 
 *
 */

public class QuizWinnersListStrategyTest extends TestCase
{

  private QuizWinnersListStrategy quizWinnersListStrategy = new QuizWinnersListStrategy();

  private MockPickListFactory mockFactory = new MockPickListFactory();
  private ActivityService activityServiceMock;

  /**
   * Sets up the fixture, for example, open a network connection. This method is called before a
   * test is executed.
   * 
   * @throws Exception
   */
  protected void setUp() throws Exception
  {
    super.setUp();
    activityServiceMock = EasyMock.createMock( ActivityService.class );
    quizWinnersListStrategy.setActivityService( activityServiceMock );
  }

  public void testBuildWinnersList()
  {

    // SweepstakesMultipleAwardsType
    // PERIOD_CODE = "period";
    // MULTIPLE_CODE = "multiple";
    // DRAWING_CODE = "drawing";
    // PromotionSweepstakesWinnersTypes
    // SPECIFIC_CODE = "specific";
    // PERCENTAGE_CODE = "percentage";

    // build a set of participants for use
    Participant part1 = buildParticipant( 1 );
    Participant part2 = buildParticipant( 2 );
    Participant part3 = buildParticipant( 3 );
    Participant part4 = buildParticipant( 4 );
    Participant part5 = buildParticipant( 5 );

    // TEST 1 - TEST SPECIFIC
    Promotion promotion = buildQuizPromotion( 1, SweepstakesWinnersType.SPECIFIC_CODE, 3 );

    PromotionSweepstake promotionSweepstake = buildPromotionSweepstake();

    int giverWinnerReplacementTotal = 3;

    // build activities
    List<Activity> activities = new ArrayList<>();
    activities.add( buildActivity( 1, true, part1 ) );
    activities.add( buildActivity( 2, true, part2 ) );
    activities.add( buildActivity( 3, true, part3 ) );
    activities.add( buildActivity( 4, true, part4 ) );
    activities.add( buildActivity( 5, true, part5 ) );

    EasyMock.expect( activityServiceMock.getQuizActivityList( promotion.getId(), new Boolean( true ), promotionSweepstake ) ).andReturn( activities );

    EasyMock.replay( activityServiceMock );

    List actualWinners = quizWinnersListStrategy.buildWinnersList( promotion, promotionSweepstake, giverWinnerReplacementTotal, 0 );

    TestCase.assertEquals( 3, actualWinners.size() );

    EasyMock.reset( activityServiceMock );

    // TEST 2 - TEST PERCENTAGE (LOWER)
    promotion = buildQuizPromotion( 2, SweepstakesWinnersType.PERCENTAGE_CODE, 15 );

    promotionSweepstake = buildPromotionSweepstake();
    // if these values are more
    giverWinnerReplacementTotal = 0;

    // build activities
    activities = new ArrayList<Activity>();
    activities.add( buildActivity( 1, true, part1 ) );
    activities.add( buildActivity( 2, true, part2 ) );
    activities.add( buildActivity( 3, true, part3 ) );
    activities.add( buildActivity( 4, true, part4 ) );
    activities.add( buildActivity( 5, true, part5 ) );

    EasyMock.expect( activityServiceMock.getQuizActivityList( promotion.getId(), new Boolean( true ), promotionSweepstake ) ).andReturn( activities );

    EasyMock.replay( activityServiceMock );

    actualWinners = quizWinnersListStrategy.buildWinnersList( promotion, promotionSweepstake, giverWinnerReplacementTotal, 0 );
    assertEquals( 1, actualWinners.size() );

    EasyMock.reset( activityServiceMock );

    // TEST 3 - TEST PERCENTAGE (HIGHER)
    promotion = buildQuizPromotion( 3, SweepstakesWinnersType.PERCENTAGE_CODE, 25 );

    promotionSweepstake = buildPromotionSweepstake();
    // if these values are more
    giverWinnerReplacementTotal = 0;

    EasyMock.expect( activityServiceMock.getQuizActivityList( promotion.getId(), new Boolean( true ), promotionSweepstake ) ).andReturn( activities );

    EasyMock.replay( activityServiceMock );

    actualWinners = quizWinnersListStrategy.buildWinnersList( promotion, promotionSweepstake, giverWinnerReplacementTotal, 0 );
    assertEquals( 1, actualWinners.size() );
  }

  private QuizPromotion buildQuizPromotion( long id, String sweepstakeGiverTypeCode, int sweepstakeWinners )
  {

    QuizPromotion promotion = new QuizPromotion();
    promotion.setId( new Long( id ) );
    promotion.setSweepstakesPrimaryBasisType( (SweepstakesWinnersType)mockFactory.getPickListItem( SweepstakesWinnersType.class, sweepstakeGiverTypeCode ) );
    promotion.setSweepstakesPrimaryWinners( new Integer( sweepstakeWinners ) );

    return promotion;
  }

  private PromotionSweepstake buildPromotionSweepstake()
  {
    PromotionSweepstake promotionSweepstake = new PromotionSweepstake();
    promotionSweepstake.setStartDate( new Date() );
    promotionSweepstake.setEndDate( new Date() );
    promotionSweepstake.setProcessed( false );

    return promotionSweepstake;
  }

  private Activity buildActivity( long id, boolean isSubmitter, Participant participant )
  {
    if ( participant == null )
    {
      participant = buildParticipant( id );
    }
    QuizActivity activity = new QuizActivity();
    activity.setId( new Long( id ) );
    activity.setParticipant( participant );
    return activity;
  }

  private Participant buildParticipant( long id )
  {
    Participant participant = new Participant();
    participant.setId( new Long( id ) );
    participant.setFirstName( String.valueOf( id ) );
    participant.setLastName( String.valueOf( id ) );
    participant.setUserName( String.valueOf( id ) );
    participant.setStatus( (ParticipantStatus)mockFactory.getPickListItem( ParticipantStatus.class, ParticipantStatus.ACTIVE ) );
    return participant;
  }

}

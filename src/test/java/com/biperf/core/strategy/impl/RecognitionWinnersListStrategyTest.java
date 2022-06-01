/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.strategy.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.easymock.EasyMock;

import com.biperf.core.domain.activity.Activity;
import com.biperf.core.domain.activity.RecognitionActivity;
import com.biperf.core.domain.enums.MockPickListFactory;
import com.biperf.core.domain.enums.ParticipantStatus;
import com.biperf.core.domain.enums.PromotionType;
import com.biperf.core.domain.enums.SweepstakesMultipleAwardsType;
import com.biperf.core.domain.enums.SweepstakesWinnerEligibilityType;
import com.biperf.core.domain.enums.SweepstakesWinnersType;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.PromotionSweepstake;
import com.biperf.core.domain.promotion.PromotionSweepstakeWinner;
import com.biperf.core.domain.promotion.RecognitionPromotion;
import com.biperf.core.service.BaseServiceTest;
import com.biperf.core.service.activity.ActivityService;

import junit.framework.TestCase;

/*
 * RecognitionWinnersListStrategyTest.
 * 
 *
 */

public class RecognitionWinnersListStrategyTest extends BaseServiceTest
{

  private RecognitionWinnersListStrategy recWinnersListStrategy = new RecognitionWinnersListStrategy();

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
    recWinnersListStrategy.setActivityService( activityServiceMock );
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
    Participant part6 = buildParticipant( 6 );
    Participant part7 = buildParticipant( 7 );
    Participant part8 = buildParticipant( 8 );
    Participant part9 = buildParticipant( 9 );
    Participant part10 = buildParticipant( 10 );

    // TEST 1 - TEST GIVER ONLY
    Promotion promotion = buildRecognitionPromotion( 1,
                                                     SweepstakesWinnerEligibilityType.GIVERS_ONLY_CODE,
                                                     SweepstakesMultipleAwardsType.PERIOD_CODE,
                                                     SweepstakesWinnersType.SPECIFIC_CODE,
                                                     SweepstakesWinnersType.SPECIFIC_CODE,
                                                     2,
                                                     0 );

    PromotionSweepstake promotionSweepstake = buildPromotionSweepstake();
    int giverWinnerReplacementTotal = 0;
    int receiverWinnerReplacementTotal = 0;

    // build activities
    List<Activity> activities = new ArrayList<>();
    activities.add( buildActivity( 1, true, part1 ) );
    activities.add( buildActivity( 2, true, part2 ) );
    activities.add( buildActivity( 3, true, part3 ) );
    activities.add( buildActivity( 4, true, part4 ) );

    EasyMock.expect( activityServiceMock.getGiversOnlyRecognitionActivityList( promotion.getId(), promotionSweepstake ) ).andReturn( activities );

    EasyMock.replay( activityServiceMock );

    List actualWinners = recWinnersListStrategy.buildWinnersList( promotion, promotionSweepstake, giverWinnerReplacementTotal, receiverWinnerReplacementTotal );
    TestCase.assertEquals( 2, actualWinners.size() );

    EasyMock.reset( activityServiceMock );
    // TEST 2 - TEST RECEIVERS ONLY
    promotion = buildRecognitionPromotion( 2,
                                           SweepstakesWinnerEligibilityType.RECEIVERS_ONLY_CODE,
                                           SweepstakesMultipleAwardsType.PERIOD_CODE,
                                           SweepstakesWinnersType.SPECIFIC_CODE,
                                           SweepstakesWinnersType.SPECIFIC_CODE,
                                           0,
                                           3 );

    promotionSweepstake = buildPromotionSweepstake();

    giverWinnerReplacementTotal = 0;
    receiverWinnerReplacementTotal = 0;

    // build activities
    activities = new ArrayList<Activity>();
    activities.add( buildActivity( 1, false, part1 ) );
    activities.add( buildActivity( 2, false, part2 ) );
    activities.add( buildActivity( 3, false, part3 ) );
    activities.add( buildActivity( 4, false, part4 ) );
    activities.add( buildActivity( 5, false, part5 ) );

    EasyMock.expect( activityServiceMock.getReceiversOnlyRecognitionActivityList( promotion.getId(), promotionSweepstake ) ).andReturn( activities );

    EasyMock.replay( activityServiceMock );

    actualWinners = recWinnersListStrategy.buildWinnersList( promotion, promotionSweepstake, giverWinnerReplacementTotal, receiverWinnerReplacementTotal );

    assertEquals( 3, actualWinners.size() );

    EasyMock.reset( activityServiceMock );
    // TEST 3 - TEST GIVERS AND RECEIVERS COMBINED
    promotion = buildRecognitionPromotion( 2,
                                           SweepstakesWinnerEligibilityType.GIVERS_AND_RECEIVERS_COMBINED_CODE,
                                           SweepstakesMultipleAwardsType.PERIOD_CODE,
                                           SweepstakesWinnersType.SPECIFIC_CODE,
                                           SweepstakesWinnersType.SPECIFIC_CODE,
                                           5,
                                           0 );

    promotionSweepstake = buildPromotionSweepstake();
    giverWinnerReplacementTotal = 0;
    receiverWinnerReplacementTotal = 0;

    // build activities
    activities = new ArrayList<Activity>();
    activities.add( buildActivity( 1, false, part1 ) );
    activities.add( buildActivity( 2, false, part2 ) );
    activities.add( buildActivity( 3, false, part3 ) );
    activities.add( buildActivity( 4, false, part4 ) );
    activities.add( buildActivity( 5, true, part5 ) );
    activities.add( buildActivity( 6, true, part6 ) );
    activities.add( buildActivity( 7, true, part7 ) );
    activities.add( buildActivity( 8, true, part8 ) );

    EasyMock.expect( activityServiceMock.getGiversAndReceiversRecognitionActivityList( promotion.getId(), promotionSweepstake ) ).andReturn( activities );

    EasyMock.replay( activityServiceMock );

    actualWinners = recWinnersListStrategy.buildWinnersList( promotion, promotionSweepstake, giverWinnerReplacementTotal, receiverWinnerReplacementTotal );

    assertEquals( 5, actualWinners.size() );

    EasyMock.reset( activityServiceMock );

    // TEST 4 - TEST GIVERS AND RECEIVERS SEPERATE
    promotion = buildRecognitionPromotion( 4,
                                           SweepstakesWinnerEligibilityType.GIVERS_AND_RECEIVERS_SEPARATE_CODE,
                                           SweepstakesMultipleAwardsType.PERIOD_CODE,
                                           SweepstakesWinnersType.SPECIFIC_CODE,
                                           SweepstakesWinnersType.SPECIFIC_CODE,
                                           2,
                                           3 );

    promotionSweepstake = buildPromotionSweepstake();
    giverWinnerReplacementTotal = 0;
    receiverWinnerReplacementTotal = 0;

    // build activities
    List<Activity> receiverActivities = new ArrayList<>();
    receiverActivities.add( buildActivity( 1, false, part1 ) );
    receiverActivities.add( buildActivity( 2, false, part2 ) );
    receiverActivities.add( buildActivity( 3, false, part3 ) );
    receiverActivities.add( buildActivity( 4, false, part4 ) );
    List<Activity> giverActivities = new ArrayList<>();
    giverActivities.add( buildActivity( 5, true, part5 ) );
    giverActivities.add( buildActivity( 6, true, part6 ) );
    giverActivities.add( buildActivity( 7, true, part7 ) );
    giverActivities.add( buildActivity( 8, true, part8 ) );

    EasyMock.expect( activityServiceMock.getGiversOnlyRecognitionActivityList( promotion.getId(), promotionSweepstake ) ).andReturn( giverActivities );

    EasyMock.expect( activityServiceMock.getReceiversOnlyRecognitionActivityList( promotion.getId(), promotionSweepstake ) ).andReturn( receiverActivities );

    EasyMock.replay( activityServiceMock );

    actualWinners = recWinnersListStrategy.buildWinnersList( promotion, promotionSweepstake, giverWinnerReplacementTotal, receiverWinnerReplacementTotal );

    assertEquals( 5, actualWinners.size() );

    EasyMock.reset( activityServiceMock );

    // TEST 5 - TEST PERCENTAGE
    promotion = buildRecognitionPromotion( 1,
                                           SweepstakesWinnerEligibilityType.GIVERS_AND_RECEIVERS_SEPARATE_CODE,
                                           SweepstakesMultipleAwardsType.PERIOD_CODE,
                                           SweepstakesWinnersType.PERCENTAGE_CODE,
                                           SweepstakesWinnersType.PERCENTAGE_CODE,
                                           50,
                                           25 );

    promotionSweepstake = buildPromotionSweepstake();
    // if these values are more
    giverWinnerReplacementTotal = 0;
    receiverWinnerReplacementTotal = 0;

    // build activities
    receiverActivities = new ArrayList<Activity>();
    receiverActivities.add( buildActivity( 1, false, part1 ) );
    receiverActivities.add( buildActivity( 2, false, part2 ) );
    receiverActivities.add( buildActivity( 3, false, part3 ) );
    receiverActivities.add( buildActivity( 4, false, part4 ) );
    giverActivities = new ArrayList<Activity>();
    giverActivities.add( buildActivity( 5, true, part5 ) );
    giverActivities.add( buildActivity( 6, true, part6 ) );
    giverActivities.add( buildActivity( 7, true, part7 ) );
    giverActivities.add( buildActivity( 8, true, part8 ) );

    EasyMock.expect( activityServiceMock.getGiversOnlyRecognitionActivityList( promotion.getId(), promotionSweepstake ) ).andReturn( giverActivities );

    EasyMock.expect( activityServiceMock.getReceiversOnlyRecognitionActivityList( promotion.getId(), promotionSweepstake ) ).andReturn( receiverActivities );

    EasyMock.replay( activityServiceMock );

    actualWinners = recWinnersListStrategy.buildWinnersList( promotion, promotionSweepstake, giverWinnerReplacementTotal, receiverWinnerReplacementTotal );

    assertEquals( 3, actualWinners.size() );

    EasyMock.reset( activityServiceMock );

    // TEST 6 - GIVER, MULTIPLE, GIVER SPECIFIC, RECEIVER SPECIFIC
    // TEST MULTIPLE
    promotion = buildRecognitionPromotion( 6,
                                           SweepstakesWinnerEligibilityType.GIVERS_ONLY_CODE,
                                           SweepstakesMultipleAwardsType.MULTIPLE_CODE,
                                           SweepstakesWinnersType.SPECIFIC_CODE,
                                           SweepstakesWinnersType.SPECIFIC_CODE,
                                           19,
                                           0 );

    promotionSweepstake = buildPromotionSweepstake();
    // if these values are more
    giverWinnerReplacementTotal = 19;
    receiverWinnerReplacementTotal = 0;

    // build activities
    activities = new ArrayList<Activity>();
    activities.add( buildActivity( 1, true, part1 ) );
    activities.add( buildActivity( 2, true, part2 ) );
    activities.add( buildActivity( 3, true, part3 ) );
    activities.add( buildActivity( 4, true, part4 ) );
    activities.add( buildActivity( 5, true, part5 ) );
    activities.add( buildActivity( 6, true, part6 ) );
    activities.add( buildActivity( 7, true, part7 ) );
    activities.add( buildActivity( 8, true, part8 ) );
    activities.add( buildActivity( 9, true, part9 ) );
    activities.add( buildActivity( 10, true, part10 ) );
    activities.add( buildActivity( 11, true, part1 ) );
    activities.add( buildActivity( 12, true, part2 ) );
    activities.add( buildActivity( 13, true, part3 ) );
    activities.add( buildActivity( 14, true, part4 ) );
    activities.add( buildActivity( 15, true, part5 ) );
    activities.add( buildActivity( 16, true, part6 ) );
    activities.add( buildActivity( 17, true, part7 ) );
    activities.add( buildActivity( 18, true, part8 ) );
    activities.add( buildActivity( 19, true, part9 ) );
    activities.add( buildActivity( 20, true, part10 ) );

    EasyMock.expect( activityServiceMock.getGiversOnlyRecognitionActivityList( promotion.getId(), promotionSweepstake ) ).andReturn( activities );

    EasyMock.replay( activityServiceMock );

    actualWinners = recWinnersListStrategy.buildWinnersList( promotion, promotionSweepstake, giverWinnerReplacementTotal, receiverWinnerReplacementTotal );

    // check size
    assertEquals( 19, actualWinners.size() );
    // check that winners have been used more than once
    HashMap map = new HashMap();
    Iterator iter = actualWinners.iterator();
    boolean multipleEntriesFound = false;
    while ( iter.hasNext() )
    {
      PromotionSweepstakeWinner winner = (PromotionSweepstakeWinner)iter.next();
      if ( map.containsKey( winner.getParticipant().getId() ) )
      {
        multipleEntriesFound = true;
        break;
      }
      map.put( winner.getParticipant().getId(), winner );

    }

    assertTrue( "Multiple entries expected to be found but were not", multipleEntriesFound );
    EasyMock.reset( activityServiceMock );

    // TEST 7 - DRAWING TEST - ENSURE UNIQUE PAX FOR GIVER AND FOR RECEIVER
    // PAX CAN WIN ONCE WITH GIVER AND ONCE WITH RECEIVER - NO MORE
    //
    promotion = buildRecognitionPromotion( 7,
                                           SweepstakesWinnerEligibilityType.GIVERS_AND_RECEIVERS_SEPARATE_CODE,
                                           SweepstakesMultipleAwardsType.DRAWING_CODE,
                                           SweepstakesWinnersType.SPECIFIC_CODE,
                                           SweepstakesWinnersType.SPECIFIC_CODE,
                                           10,
                                           10 );

    promotionSweepstake = buildPromotionSweepstake();

    // TEST 7.5 - TEST THAT REPLACEMENT TOTALS DO NOT CAUSE RETURN OF MORE THAN
    // SETUP SPECIFIES
    giverWinnerReplacementTotal = 20;
    receiverWinnerReplacementTotal = 50;

    // build activities
    // large number of participants to make
    giverActivities = new ArrayList<Activity>();
    giverActivities.add( buildActivity( 1, true, part1 ) );
    giverActivities.add( buildActivity( 2, true, part2 ) );
    giverActivities.add( buildActivity( 3, true, part3 ) );
    giverActivities.add( buildActivity( 4, true, part4 ) );
    giverActivities.add( buildActivity( 5, true, part5 ) );
    giverActivities.add( buildActivity( 6, true, part6 ) );
    giverActivities.add( buildActivity( 7, true, part7 ) );
    giverActivities.add( buildActivity( 8, true, part8 ) );
    giverActivities.add( buildActivity( 9, true, part9 ) );
    giverActivities.add( buildActivity( 10, true, part10 ) );

    receiverActivities = new ArrayList<Activity>();
    receiverActivities.add( buildActivity( 11, false, part1 ) );
    receiverActivities.add( buildActivity( 12, false, part2 ) );
    receiverActivities.add( buildActivity( 13, false, part3 ) );
    receiverActivities.add( buildActivity( 14, false, part4 ) );
    receiverActivities.add( buildActivity( 15, false, part5 ) );
    receiverActivities.add( buildActivity( 16, false, part6 ) );
    receiverActivities.add( buildActivity( 17, false, part7 ) );
    receiverActivities.add( buildActivity( 18, false, part8 ) );
    receiverActivities.add( buildActivity( 19, false, part9 ) );
    receiverActivities.add( buildActivity( 20, false, part10 ) );

    EasyMock.expect( activityServiceMock.getGiversOnlyRecognitionActivityList( promotion.getId(), promotionSweepstake ) ).andReturn( giverActivities );

    EasyMock.expect( activityServiceMock.getReceiversOnlyRecognitionActivityList( promotion.getId(), promotionSweepstake ) ).andReturn( receiverActivities );

    EasyMock.replay( activityServiceMock );

    actualWinners = recWinnersListStrategy.buildWinnersList( promotion, promotionSweepstake, giverWinnerReplacementTotal, receiverWinnerReplacementTotal );

    // check size
    assertEquals( 20, actualWinners.size() );

    // split winners into two maps
    // check that if winners have been used more than once
    // that they are of different types (giver or receiver)
    HashMap giverMap = new HashMap();
    HashMap receiverMap = new HashMap();

    iter = actualWinners.iterator();
    boolean multipleEntriesFoundGivers = false;
    boolean multipleEntriesFoundReceivers = false;
    while ( iter.hasNext() )
    {
      PromotionSweepstakeWinner winner = (PromotionSweepstakeWinner)iter.next();
      if ( winner.getWinnerType().equals( PromotionSweepstakeWinner.GIVER_TYPE ) )
      {

        if ( giverMap.containsKey( winner.getParticipant().getId() ) )
        {
          multipleEntriesFoundGivers = true;
          break;
        }
        giverMap.put( winner.getParticipant().getId(), winner );
      }

      if ( winner.getWinnerType().equals( PromotionSweepstakeWinner.RECEIVER_TYPE ) )
      {

        if ( receiverMap.containsKey( winner.getParticipant().getId() ) )
        {
          multipleEntriesFoundReceivers = true;
          break;
        }
        receiverMap.put( winner.getParticipant().getId(), winner );
      }

    }

    assertEquals( 10, giverMap.size() );
    assertEquals( 10, receiverMap.size() );
    assertFalse( "Multiple entries found givers - expected none", multipleEntriesFoundGivers );
    assertFalse( "Multiple entries found in receivers - expected none", multipleEntriesFoundReceivers );

    EasyMock.reset( activityServiceMock );

    // TEST 8 - TEST DRAWING - ENSURE ALL UNIQUE WINNERS
    promotion = buildRecognitionPromotion( 7,
                                           SweepstakesWinnerEligibilityType.GIVERS_ONLY_CODE,
                                           SweepstakesMultipleAwardsType.DRAWING_CODE,
                                           SweepstakesWinnersType.SPECIFIC_CODE,
                                           SweepstakesWinnersType.SPECIFIC_CODE,
                                           10,
                                           0 );

    promotionSweepstake = buildPromotionSweepstake();
    // if these values are more
    giverWinnerReplacementTotal = 10;
    receiverWinnerReplacementTotal = 0;

    // build activities
    activities = new ArrayList<Activity>();
    activities.add( buildActivity( 1, true, part1 ) );
    activities.add( buildActivity( 2, true, part2 ) );
    activities.add( buildActivity( 3, true, part3 ) );
    activities.add( buildActivity( 4, true, part4 ) );
    activities.add( buildActivity( 5, true, part5 ) );
    activities.add( buildActivity( 6, true, part6 ) );
    activities.add( buildActivity( 7, true, part7 ) );
    activities.add( buildActivity( 8, true, part8 ) );
    activities.add( buildActivity( 9, true, part9 ) );
    activities.add( buildActivity( 10, true, part10 ) );

    EasyMock.expect( activityServiceMock.getGiversOnlyRecognitionActivityList( promotion.getId(), promotionSweepstake ) ).andReturn( activities );

    EasyMock.replay( activityServiceMock );

    actualWinners = recWinnersListStrategy.buildWinnersList( promotion, promotionSweepstake, giverWinnerReplacementTotal, receiverWinnerReplacementTotal );

    // check size
    assertEquals( 10, actualWinners.size() );

    // check that winners have not been used more than once
    // should be 10 unique winners
    map = new HashMap();
    iter = actualWinners.iterator();
    while ( iter.hasNext() )
    {
      PromotionSweepstakeWinner winner = (PromotionSweepstakeWinner)iter.next();
      map.put( winner.getParticipant().getId(), winner );

    }

    assertEquals( 10, map.size() );
    EasyMock.reset( activityServiceMock );

  }

  /**
   * @param id
   * @param sweepstakeEligibilityTypeCode
   * @param sweepstakesMultipleAwardTypeCode
   * @param sweepstakeGiverTypeCode
   * @param sweepstakeReceiverTypeCode
   * @param sweepstakeGiversWinners
   * @param sweepstakeReceiversWinners
   * @return RecognitionPromotion
   */
  private RecognitionPromotion buildRecognitionPromotion( long id,
                                                          String sweepstakeEligibilityTypeCode,
                                                          String sweepstakesMultipleAwardTypeCode,
                                                          String sweepstakeGiverTypeCode,
                                                          String sweepstakeReceiverTypeCode,
                                                          int sweepstakeGiversWinners,
                                                          int sweepstakeReceiversWinners )
  {

    RecognitionPromotion promotion = new RecognitionPromotion();
    promotion.setPromotionType( PromotionType.lookup( PromotionType.RECOGNITION ) );
    promotion.setId( new Long( id ) );

    promotion.setSweepstakesWinnerEligibilityType( (SweepstakesWinnerEligibilityType)mockFactory.getPickListItem( SweepstakesWinnerEligibilityType.class, sweepstakeEligibilityTypeCode ) );

    promotion.setSweepstakesMultipleAwardType( (SweepstakesMultipleAwardsType)mockFactory.getPickListItem( SweepstakesMultipleAwardsType.class, sweepstakesMultipleAwardTypeCode ) );

    promotion.setSweepstakesPrimaryBasisType( (SweepstakesWinnersType)mockFactory.getPickListItem( SweepstakesWinnersType.class, sweepstakeGiverTypeCode ) );

    promotion.setSweepstakesSecondaryBasisType( (SweepstakesWinnersType)mockFactory.getPickListItem( SweepstakesWinnersType.class, sweepstakeReceiverTypeCode ) );

    promotion.setSweepstakesPrimaryWinners( new Integer( sweepstakeGiversWinners ) );
    promotion.setSweepstakesSecondaryWinners( new Integer( sweepstakeReceiversWinners ) );

    return promotion;
  }

  /**
   * Builds a PromotionSweepstake from scratch.
   * 
   * @return PromotionSweepstake
   */
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
    RecognitionActivity activity = new RecognitionActivity();
    activity.setId( new Long( id ) );
    activity.setParticipant( participant );
    activity.setSubmitter( isSubmitter );
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

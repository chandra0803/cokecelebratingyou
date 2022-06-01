
package com.biperf.core.service.goalquest.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.easymock.EasyMock;

import com.biperf.core.domain.enums.GoalQuestPaxActivityType;
import com.biperf.core.domain.goalquest.GoalQuestParticipantActivity;
import com.biperf.core.domain.goalquest.GoalQuestReviewProgress;
import com.biperf.core.domain.goalquest.PaxGoal;
import com.biperf.core.domain.promotion.GoalLevel;
import com.biperf.core.exception.BeaconRuntimeException;
import com.biperf.core.service.AssociationRequestCollection;

public class GetGoalQuestProgressByPromotionIdAndUserIdTest extends BaseGQTest
{
  Long promoId = null;
  Long userId = null;
  Map<String, Object> expectedResult = null;
  GoalQuestReviewProgress expectedReviewProgress = null;

  @Override
  protected void setUp() throws Exception
  {
    super.setUp();
    expectedResult = new HashMap<String, Object>();
    expectedReviewProgress = new GoalQuestReviewProgress();
    // default values for the ReviewProgress (found at the bottom of the method)
    BigDecimal zero = new BigDecimal( "0" );
    expectedReviewProgress.setAmountToAchieve( zero );
    expectedReviewProgress.setQuantity( zero );
    expectedReviewProgress.setCumulativeTotal( zero );
  }

  /**
   * <b>Goal:</b> Confirm behavior when the promotion ID is null
   * <br/><br/>
   * <b>Setup:</b> don't bother initializing the promotion ID
   * <br/><br/>
   * <b>Expected Behavior:</b> A BeaconRuntimeException will be thrown
   */
  public void testNullPromoId()
  {
    userId = 1L;
    boolean pass = false;

    try
    {
      Map<String, Object> result = testInstance.getGoalQuestProgressByPromotionIdAndUserId( promoId, userId );
    }
    catch( BeaconRuntimeException exception )
    {
      pass = true;
    }
    finally
    {
      assertTrue( "BeaconRuntimeException not thrown", pass );
    }
  }

  /**
   * <b>Goal:</b> Confirm behavior when User Id is null
   * <br/><br/>
   * <b>Setup:</b> Don't both initializing userId
   * <br/><br/>
   * <b>Expected Behavior:</b> A Null pointer exception will be thrown.
   */
  public void testNullUserId()
  {
    promoId = 1L;
    boolean pass = false;

    try
    {
      Map<String, Object> result = testInstance.getGoalQuestProgressByPromotionIdAndUserId( promoId, userId );
    }
    catch( BeaconRuntimeException exception )
    {
      pass = true;
    }
    finally
    {
      assertTrue( "BeaconRuntimeException not thrown", pass );
    }
  }

  /**
   * <b>Goal:</b> Skip every conditional, but don't crash it
   * <br/><br/>
   * <b>Setup:</b> Initialize the IDs, make a null activity list and goal
   * <br/><br/>
   * <b>Expected Behavior:</b> The default return will be constructed
   */
  public void testSkipAllConditionals()
  {
    // Pass the first conditional
    userId = 1L;
    promoId = 1L;
    List<GoalQuestParticipantActivity> gqPaxActivityList = null;
    PaxGoal goal1 = null;

    EasyMock
        .expect( mockGoalQuestPaxActivityService.getGoalQuestParticipantActivityByPromotionIdAndUserId( EasyMock.eq( promoId ),
                                                                                                        EasyMock.eq( userId ),
                                                                                                        (AssociationRequestCollection)EasyMock.anyObject() ) )
        .andReturn( gqPaxActivityList );
    EasyMock.expect( mockPaxGoalService.getPaxGoalByPromotionIdAndUserId( EasyMock.eq( promoId ), EasyMock.eq( userId ), (AssociationRequestCollection)EasyMock.anyObject() ) ).andReturn( goal1 );

    mockControl.replay();
    Map<String, Object> result = testInstance.getGoalQuestProgressByPromotionIdAndUserId( promoId, userId );
    mockControl.verify();
    List<GoalQuestReviewProgress> gqReviewProgressList = (List<GoalQuestReviewProgress>)result.get( "goalQuestProgressList" );
    GoalQuestReviewProgress progress = gqReviewProgressList.get( 0 );

    assertTrue( gqReviewProgressList.size() == 1 );
    assertTrue( compareGQProcs( progress, expectedReviewProgress ) );

    // This is the one default key, added at the end
    assertTrue( result.size() == 1 );
    assertTrue( result.containsKey( "goalQuestProgressList" ) );
  }

  /**
   * <b>Goal:</b> Test that the paxgoal gets added to the return map if it is non-null
   * <br/><br/>
   * <b>Setup:</b> create a non-null goal to return from the mockPaxGoalService 
   * <br/><br/>
   * <b>Expected Behavior:</b> The returned map will have a key/value for paxGoal
   */
  public void testGoalNotNull()
  {
    // Pass the first conditional
    userId = 1L;
    promoId = 1L;
    List<GoalQuestParticipantActivity> gqPaxActivityList = null;
    PaxGoal goal1 = new PaxGoal();
    goal1.setGoalLevel( new GoalLevel() );
    expectedReviewProgress.setAmountToAchieve( null );

    EasyMock
        .expect( mockGoalQuestPaxActivityService.getGoalQuestParticipantActivityByPromotionIdAndUserId( EasyMock.eq( promoId ),
                                                                                                        EasyMock.eq( userId ),
                                                                                                        (AssociationRequestCollection)EasyMock.anyObject() ) )
        .andReturn( gqPaxActivityList );
    EasyMock.expect( mockPaxGoalService.getPaxGoalByPromotionIdAndUserId( EasyMock.eq( promoId ), EasyMock.eq( userId ), (AssociationRequestCollection)EasyMock.anyObject() ) ).andReturn( goal1 );

    mockControl.replay();
    Map<String, Object> result = testInstance.getGoalQuestProgressByPromotionIdAndUserId( promoId, userId );
    mockControl.verify();

    List<GoalQuestReviewProgress> gqReviewProgressList = (List<GoalQuestReviewProgress>)result.get( "goalQuestProgressList" );
    GoalQuestReviewProgress progress = gqReviewProgressList.get( 0 );

    // This is the one default key, added at the end
    assertTrue( result.size() == 2 );
    assertTrue( result.containsKey( "paxGoal" ) );
    assertTrue( result.get( "paxGoal" ).equals( goal1 ) );
    assertTrue( result.containsKey( "goalQuestProgressList" ) );

    assertTrue( compareGQProcs( progress, expectedReviewProgress ) );
  }

  /**
   * <b>Goal:</b> Test handling an incremental type activity
   * <br/><br/>
   * <b>Setup:</b> Create a GoalQuestParticipantActivity with PaxActivityType Incremental
   * <br/><br/>
   * <b>Expected Behavior:</b> goalQuestReviewProgress gets added to the list,
   * but not from the default case.
   */
  public void testActivityTypeIncremental()
  {
    // Pass the first conditional
    userId = 1L;
    promoId = 1L;
    List<GoalQuestParticipantActivity> gqPaxActivityList = new ArrayList<GoalQuestParticipantActivity>();
    GoalQuestParticipantActivity activity1 = new GoalQuestParticipantActivity();
    PaxGoal goal1 = null;
    Date submitDate = new Date();

    activity1.setSubmissionDate( submitDate );
    activity1.setType( GoalQuestPaxActivityType.lookup( GoalQuestPaxActivityType.INCREMENTAL ) );
    activity1.setQuantity( new BigDecimal( 3 ) );
    activity1.setAutomotive( false );
    gqPaxActivityList.add( activity1 );

    expectedReviewProgress.setSubmissionDate( submitDate );
    expectedReviewProgress.setQuantity( activity1.getQuantity() );
    expectedReviewProgress.setLoadType( GoalQuestPaxActivityType.INCREMENTAL );
    expectedReviewProgress.setCumulativeTotal( activity1.getQuantity() );
    expectedReviewProgress.setAutomotive( false );
    EasyMock
        .expect( mockGoalQuestPaxActivityService.getGoalQuestParticipantActivityByPromotionIdAndUserId( EasyMock.eq( promoId ),
                                                                                                        EasyMock.eq( userId ),
                                                                                                        (AssociationRequestCollection)EasyMock.anyObject() ) )
        .andReturn( gqPaxActivityList );
    EasyMock.expect( mockPaxGoalService.getPaxGoalByPromotionIdAndUserId( EasyMock.eq( promoId ), EasyMock.eq( userId ), (AssociationRequestCollection)EasyMock.anyObject() ) ).andReturn( goal1 );

    mockControl.replay();
    Map<String, Object> result = testInstance.getGoalQuestProgressByPromotionIdAndUserId( promoId, userId );
    mockControl.verify();

    List<GoalQuestReviewProgress> gqReviewProgressList = (List<GoalQuestReviewProgress>)result.get( "goalQuestProgressList" );
    GoalQuestReviewProgress progress = gqReviewProgressList.get( 0 );

    // This is the one default key, added at the end
    assertTrue( result.size() == 1 );
    assertTrue( result.containsKey( "goalQuestProgressList" ) );
    assertTrue( compareGQProcs( progress, expectedReviewProgress ) );
  }

  /**
   * <b>Goal:</b> Test handling 'replace' acivity
   * <br/><br/>
   * <b>Setup:</b> create a new expected goal with activity type 'replace'
   * <br/><br/>
   * <b>Expected Behavior:</b> goalQuestReviewProgress gets added to the list,
   * but not from the default case.
   */
  public void testActivityTypeReplace()
  {
    // Pass the first conditional
    userId = 1L;
    promoId = 1L;
    List<GoalQuestParticipantActivity> gqPaxActivityList = new ArrayList<GoalQuestParticipantActivity>();
    GoalQuestParticipantActivity activity1 = new GoalQuestParticipantActivity();
    Date submitDate = new Date();
    PaxGoal goal1 = null;

    activity1.setSubmissionDate( submitDate );
    activity1.setType( GoalQuestPaxActivityType.lookup( GoalQuestPaxActivityType.REPLACE ) );
    activity1.setQuantity( new BigDecimal( 3 ) );
    activity1.setAutomotive( false );
    gqPaxActivityList.add( activity1 );

    expectedReviewProgress.setSubmissionDate( submitDate );
    expectedReviewProgress.setQuantity( activity1.getQuantity() );
    expectedReviewProgress.setLoadType( GoalQuestPaxActivityType.REPLACE );
    expectedReviewProgress.setCumulativeTotal( activity1.getQuantity() );
    expectedReviewProgress.setAutomotive( false );

    EasyMock
        .expect( mockGoalQuestPaxActivityService.getGoalQuestParticipantActivityByPromotionIdAndUserId( EasyMock.eq( promoId ),
                                                                                                        EasyMock.eq( userId ),
                                                                                                        (AssociationRequestCollection)EasyMock.anyObject() ) )
        .andReturn( gqPaxActivityList );
    EasyMock.expect( mockPaxGoalService.getPaxGoalByPromotionIdAndUserId( EasyMock.eq( promoId ), EasyMock.eq( userId ), (AssociationRequestCollection)EasyMock.anyObject() ) ).andReturn( goal1 );

    mockControl.replay();
    Map<String, Object> result = testInstance.getGoalQuestProgressByPromotionIdAndUserId( promoId, userId );
    mockControl.verify();

    List<GoalQuestReviewProgress> gqReviewProgressList = (List<GoalQuestReviewProgress>)result.get( "goalQuestProgressList" );
    GoalQuestReviewProgress progress = gqReviewProgressList.get( 0 );

    // This is the one default key, added at the end
    assertTrue( result.size() == 1 );
    assertTrue( result.containsKey( "goalQuestProgressList" ) );
    assertTrue( compareGQProcs( progress, expectedReviewProgress ) );
  }

  /**
   * <b>Goal:</b> Check that the proper attributes are set when the activity is automotive
   * <br/><br/>
   * <b>Setup:</b> Create an activity of type automotive.
   * <br/><br/>
   * <b>Expected Behavior:</b> isAutomotive and DeliveryDate should be set
   */
  public void testActivityIsAutomotive()
  {
    // Pass the first conditional
    userId = 1L;
    promoId = 1L;
    List<GoalQuestParticipantActivity> gqPaxActivityList = new ArrayList<GoalQuestParticipantActivity>();
    GoalQuestParticipantActivity activity1 = new GoalQuestParticipantActivity();
    Date submitDate = new Date();
    PaxGoal goal1 = null;
    activity1.setSubmissionDate( submitDate );
    activity1.setType( GoalQuestPaxActivityType.lookup( GoalQuestPaxActivityType.INCREMENTAL ) );
    activity1.setQuantity( null );
    activity1.setAutomotive( true );
    activity1.setDeliveryDate( submitDate );

    gqPaxActivityList.add( activity1 );

    expectedReviewProgress.setSubmissionDate( submitDate );
    expectedReviewProgress.setQuantity( activity1.getQuantity() );
    expectedReviewProgress.setLoadType( GoalQuestPaxActivityType.INCREMENTAL );
    expectedReviewProgress.setCumulativeTotal( BigDecimal.ZERO );
    expectedReviewProgress.setAutomotive( true );
    // More get set, but they all get set to null. Just checking this one
    // to confirm the right block is hit.
    expectedReviewProgress.setDeliveryDate( submitDate );
    EasyMock
        .expect( mockGoalQuestPaxActivityService.getGoalQuestParticipantActivityByPromotionIdAndUserId( EasyMock.eq( promoId ),
                                                                                                        EasyMock.eq( userId ),
                                                                                                        (AssociationRequestCollection)EasyMock.anyObject() ) )
        .andReturn( gqPaxActivityList );
    EasyMock.expect( mockPaxGoalService.getPaxGoalByPromotionIdAndUserId( EasyMock.eq( promoId ), EasyMock.eq( userId ), (AssociationRequestCollection)EasyMock.anyObject() ) ).andReturn( goal1 );
    EasyMock.expect( mockPromotionService.getPromotionById( 1L ) ).andReturn( null );
    
    mockControl.replay();
    Map<String, Object> result = testInstance.getGoalQuestProgressByPromotionIdAndUserId( promoId, userId );
    mockControl.verify();
    List<GoalQuestReviewProgress> gqReviewProgressList = (List<GoalQuestReviewProgress>)result.get( "goalQuestProgressList" );
    GoalQuestReviewProgress progress = gqReviewProgressList.get( 0 );
    // This is the one default key, added at the end
    assertTrue( result.size() == 1 );
    assertTrue( result.containsKey( "goalQuestProgressList" ) );
    assertTrue( compareGQProcs( progress, expectedReviewProgress ) );
  }

  private boolean compareGQProcs( GoalQuestReviewProgress check1, GoalQuestReviewProgress check2 )
  {
    return compareSafe( check1.getSubmissionDate(), check2.getSubmissionDate() ) && compareSafe( check1.getAmountToAchieve(), check2.getAmountToAchieve() )
        && compareSafe( check1.getQuantity(), check2.getQuantity() ) && compareSafe( check1.isAutomotive(), check2.isAutomotive() ) && compareSafe( check1.getSaleDate(),
                                                                                                                                                    check2.getSaleDate() )
        && compareSafe( check1.getDeliveryDate(),
                        check2.getDeliveryDate() )
        && compareSafe( check1.getModel(), check2.getModel() ) && compareSafe( check1.getVin(), check2.getVin() ) && compareSafe( check1.getTransactionType(),
                                                                                                                                  check2.getTransactionType() )
        && compareSafe( check1.getDealerCode(), check2.getDealerCode() ) && compareSafe( check1.getDealerName(),
                                                                                         check2.getDealerName() )
        && compareSafe( check1.getLoadType(), check2.getLoadType() ) && compareSafe( check1.getCumulativeTotal(),
                                                                                     check2.getCumulativeTotal() );
  }

  private boolean compareSafe( Object check1, Object check2 )
  {
    if ( check1 == check2 )
    {
      return true;
    }
    if ( check1 != null && check2 != null )
    {
      return check1.equals( check2 );
    }
    return false;
  }
}
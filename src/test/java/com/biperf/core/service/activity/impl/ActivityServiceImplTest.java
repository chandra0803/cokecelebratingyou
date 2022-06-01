/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javatest/com/biperf/core/service/activity/impl/ActivityServiceImplTest.java,v $
 */

package com.biperf.core.service.activity.impl;

import java.util.ArrayList;
import java.util.List;

import org.easymock.EasyMock;

import com.biperf.core.dao.activity.ActivityDAO;
import com.biperf.core.domain.activity.ManagerOverrideActivity;
import com.biperf.core.domain.activity.QuizActivity;
import com.biperf.core.domain.activity.SalesActivity;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.ProductClaimPromotion;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.utils.GuidUtils;

import junit.framework.TestCase;

public class ActivityServiceImplTest extends TestCase
{
  private ActivityServiceImpl classUnderTest;
  private ActivityDAO mock;

  public void setUp() throws Exception
  {
    mock = EasyMock.createMock( ActivityDAO.class );
    classUnderTest = new ActivityServiceImpl();
    classUnderTest.setActivityDAO( mock );
  }

  /**
   * Test the saving of an activity
   */
  public void testSaveActivity()
  {
    SalesActivity activity = new SalesActivity( GuidUtils.generateGuid() );
    EasyMock.expect( mock.saveActivity( activity ) ).andReturn( activity );
    EasyMock.replay( mock );
    classUnderTest.saveActivity( activity );
    EasyMock.verify( mock );
  }

  /**
   * Test getting an activity by id
   */
  public void testGetActivityById()
  {
    Long id = Long.valueOf( "0" );
    SalesActivity activity = new SalesActivity( GuidUtils.generateGuid() );
    EasyMock.expect( mock.getActivityById( id ) ).andReturn( activity );
    EasyMock.replay( mock );
    classUnderTest.getActivityById( id );
    EasyMock.verify( mock );
  }

  /**
   * Test getting all activities
   */
  public void testGetAllActivities()
  {
    SalesActivity activity = new SalesActivity( GuidUtils.generateGuid() );
    List<SalesActivity> activities = new ArrayList<SalesActivity>();
    activities.add( activity );
    EasyMock.expect( mock.getAllActivities() ).andReturn( activities );
    EasyMock.replay( mock );
    List result = classUnderTest.getAllActivities();
    EasyMock.verify( mock );
    assertSame( activities, result );
  }

  /**
   * Test getting all activities not posted
   */
  public void testGetAllActivitiesNotPosted()
  {
    SalesActivity activity = new SalesActivity( GuidUtils.generateGuid() );
    List<SalesActivity> activities = new ArrayList<SalesActivity>();
    activities.add( activity );
    EasyMock.expect( mock.getAllSalesActivitiesNotPosted() ).andReturn( activities );
    EasyMock.replay( mock );
    List result = classUnderTest.getAllActivitiesNotPosted();
    EasyMock.verify( mock );
    assertSame( activities, result );
  }

  /**
   * Test getting activities by participant id
   */
  public void testGetSalesActivitesByParticipantId()
  {
    Long id = Long.valueOf( "0" );
    SalesActivity activity = new SalesActivity( GuidUtils.generateGuid() );
    List<SalesActivity> activities = new ArrayList<SalesActivity>();
    activities.add( activity );
    EasyMock.expect( mock.getActivitiesByParticipantId( id ) ).andReturn( activities );
    EasyMock.replay( mock );
    List result = classUnderTest.getActivitiesByParticipantId( id );
    EasyMock.verify( mock );
    assertSame( activities, result );
  }

  /**
   * Test getting activities by participant id
   */
  public void testGetManagerOverrideActivitesByClaimIdAndUserId()
  {
    Long claimId = Long.valueOf( "0" );
    Long userId = Long.valueOf( "0" );
    ManagerOverrideActivity managerOverrideActivity = new ManagerOverrideActivity( GuidUtils.generateGuid() );
    List<ManagerOverrideActivity> activities = new ArrayList<ManagerOverrideActivity>();
    activities.add( managerOverrideActivity );
    EasyMock.expect( mock.getManagerOverrideActivitiesByClaimAndUserId( claimId, userId ) ).andReturn( activities );
    EasyMock.replay( mock );
    List result = classUnderTest.getManagerOverrideActivityByClaimIdAndUserId( claimId, userId );
    EasyMock.verify( mock );
    assertSame( activities, result );
  }

  /**
   * Test getting activities by participant id
   */
  public void testGetQuizActivitesByClaimIdAndUserId()
  {
    Long claimId = Long.valueOf( "0" );
    Long userId = Long.valueOf( "0" );
    QuizActivity quizActivity = new QuizActivity( GuidUtils.generateGuid() );
    List<QuizActivity> activities = new ArrayList<QuizActivity>();
    activities.add( quizActivity );
    EasyMock.expect( mock.getQuizActivitiesByClaimAndUserId( claimId, userId ) ).andReturn( activities );
    EasyMock.replay( mock );
    List result = classUnderTest.getQuizActivityByClaimIdAndUserId( claimId, userId );
    EasyMock.verify( mock );
    assertSame( activities, result );
  }

  /**
   * Test getting all activities who has carry over balance
   */
  public void testGetAllActivitiesByCarryOver()
  {
    SalesActivity activity = new SalesActivity( GuidUtils.generateGuid() );
    Long promotionId = Long.valueOf( "0" );
    Long participantId = Long.valueOf( "0" );
    Participant participant = new Participant();
    participant.setId( participantId );
    Promotion promotion = new ProductClaimPromotion();
    promotion.setId( promotionId );

    activity.setParticipant( participant );
    activity.setPromotion( promotion );
    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
    List<SalesActivity> activities = new ArrayList<SalesActivity>();
    activities.add( activity );

    EasyMock.expect( mock.getActivitiesByCarryOverBalance( promotionId, participantId, associationRequestCollection ) ).andReturn( activities );
    EasyMock.replay( mock );
    List result = classUnderTest.getActivitiesByCarryOverBalance( promotionId, participantId, associationRequestCollection );
    EasyMock.verify( mock );
    assertSame( activities, result );
  }
}


package com.biperf.core.dao.ssi.hibernate;

import com.biperf.core.dao.participant.AudienceDAO;
import com.biperf.core.dao.participant.ParticipantDAO;
import com.biperf.core.dao.participant.hibernate.AudienceDAOImplTest;
import com.biperf.core.dao.participant.hibernate.ParticipantDAOImplTest;
import com.biperf.core.dao.ssi.SSIPromotionDAO;
import com.biperf.core.domain.enums.PromotionEmailNotificationType;
import com.biperf.core.domain.enums.PromotionStatusType;
import com.biperf.core.domain.participant.Audience;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.PromotionNotificationType;
import com.biperf.core.domain.promotion.PromotionPrimaryAudience;
import com.biperf.core.domain.promotion.PromotionSecondaryAudience;
import com.biperf.core.domain.ssi.SSIPromotion;
import com.biperf.core.domain.ssi.SSIPromotionContestApprovalLevel1Audience;
import com.biperf.core.domain.ssi.SSIPromotionContestApprovalLevel2Audience;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.promotion.PromotionAssociationRequest;

/**
 * 
 * SSIPromotionDAOImplTest.
 * 
 * @author kandhi
 * @since Oct 27, 2014
 * @version 1.0
 */
public class SSIPromotionDAOImplTest extends SSIBaseDAOTest
{

  private static SSIPromotionDAO getSSIPromotionDAO()
  {
    return (SSIPromotionDAO)getDAO( SSIPromotionDAO.BEAN_NAME );
  }

  private static AudienceDAO getAudienceDAO()
  {
    return (AudienceDAO)getDAO( AudienceDAO.BEAN_NAME );
  }

  protected static ParticipantDAO getParticipantDAO()
  {
    return (ParticipantDAO)getDAO( ParticipantDAO.BEAN_NAME );
  }

  /**
   * Test get live promotion
   */
  public void testGetLiveSSIPromotion()
  {
    try
    {
      SSIPromotion expectedPromotion = buildSSIPromotion();
      getPromotionDAO().save( expectedPromotion );
      assertEquals( "Actual promotion doesn't match with expected", expectedPromotion, getPromotionDAO().getPromotionById( expectedPromotion.getId() ) );
      flushAndClearSession();

      SSIPromotion promotion = getSSIPromotionDAO().getLiveSSIPromotion();
      assertNotNull( promotion );
      assertEquals( promotion.getPromotionStatus(), PromotionStatusType.lookup( PromotionStatusType.LIVE ) );
    }
    catch( Exception e )
    {
      fail( e.getMessage() );
    }
  }

  /**
   * Test get live or completed promotion
   */
  public void testGetLiveOrCompletedSSIPromotionForLivePromo()
  {
    try
    {
      SSIPromotion expectedPromotion = buildSSIPromotion();
      getPromotionDAO().save( expectedPromotion );
      assertEquals( "Actual promotion doesn't match with expected", expectedPromotion, getPromotionDAO().getPromotionById( expectedPromotion.getId() ) );
      flushAndClearSession();

      SSIPromotion promotion = getSSIPromotionDAO().getLiveOrCompletedSSIPromotion();
      assertNotNull( promotion );
      assertEquals( promotion.getPromotionStatus(), PromotionStatusType.lookup( PromotionStatusType.LIVE ) );
    }
    catch( Exception e )
    {
      fail( e.getMessage() );
    }
  }

  /**
   * Test the case where there are no live or completed SSI promotions
   */
  public void testNoLiveOrCompletedSSIPromotionForCompletedPromo()
  {
    try
    {
      SSIPromotion expectedPromotion = buildSSIPromotion();
      expectedPromotion.setPromotionStatus( PromotionStatusType.lookup( PromotionStatusType.COMPLETE ) );
      getPromotionDAO().save( expectedPromotion );
      getPromotionDAO().save( expectedPromotion );
      assertEquals( "Actual promotion doesn't match with expected", expectedPromotion, getPromotionDAO().getPromotionById( expectedPromotion.getId() ) );
      flushAndClearSession();

      SSIPromotion promotion = getSSIPromotionDAO().getLiveOrCompletedSSIPromotion();
      assertNotNull( promotion );
      assertEquals( promotion.getPromotionStatus(), PromotionStatusType.lookup( PromotionStatusType.COMPLETE ) );
    }
    catch( Exception e )
    {
      fail( e.getMessage() );
    }
  }

  /**
   * Test save promotion with creator and participant audiences
   */
  public void testSSIPromotionWithAudiences()
  {
    String uniqueString = buildUniqueString();
    SSIPromotion expectedPromotion = buildSSIPromotion();
    Participant pax = ParticipantDAOImplTest.buildUniqueParticipant( uniqueString );
    getParticipantDAO().saveParticipant( pax );
    expectedPromotion.addPromotionPrimaryAudience( buildAndSavePromotionCreatorAudience( pax, buildUniqueString(), expectedPromotion ) );
    expectedPromotion.addPromotionPrimaryAudience( buildAndSavePromotionCreatorAudience( pax, buildUniqueString(), expectedPromotion ) );
    expectedPromotion.addPromotionSecondaryAudience( buildAndSavePromotionParticipantAudience( pax, buildUniqueString(), expectedPromotion ) );
    expectedPromotion.addPromotionSecondaryAudience( buildAndSavePromotionParticipantAudience( pax, buildUniqueString(), expectedPromotion ) );

    getPromotionDAO().save( expectedPromotion );

    flushAndClearSession();

    // retrieve the promotion
    SSIPromotion actualPromotion = (SSIPromotion)getPromotionDAO().getPromotionById( expectedPromotion.getId() );

    assertDomainObjectEquals( "Actual promotion doesn't match with expected", expectedPromotion, actualPromotion );
    assertEquals( "Actual promotion Creator Audiences doesn't match with expected", expectedPromotion.getPromotionPrimaryAudiences(), actualPromotion.getPromotionPrimaryAudiences() );
    assertEquals( "Actual promotion Participant Audiences doesn't match with expected", expectedPromotion.getPromotionSecondaryAudiences(), actualPromotion.getPromotionSecondaryAudiences() );
  }

  /**
   * Test when the contest approval level 1 audiences
   */
  public void testSSIPromotionContestApprovalLevel1Audiences()
  {
    String uniqueString = buildUniqueString();
    SSIPromotion expectedPromotion = buildSSIPromotion();
    expectedPromotion.setUpperCaseName( expectedPromotion.getPromotionName().toUpperCase() );
    SSIPromotionContestApprovalLevel1Audience ssiPromotionContestApprovalLevel1Audience = new SSIPromotionContestApprovalLevel1Audience();
    Audience audience = saveAudience( uniqueString );
    ssiPromotionContestApprovalLevel1Audience.setAudience( audience );
    expectedPromotion.addContestApprovalLevel1Audiences( ssiPromotionContestApprovalLevel1Audience );
    getPromotionDAO().save( expectedPromotion );

    flushAndClearSession();

    // retrieve the promotion
    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
    associationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.SSI_CONTEST_APP_LVL_1_AUDIENCE ) );
    SSIPromotion actualPromotion = (SSIPromotion)getPromotionDAO().getPromotionByIdWithAssociations( expectedPromotion.getId(), associationRequestCollection );

    assertDomainObjectEquals( "Actual promotion doesn't match with expected", expectedPromotion, actualPromotion );
    assertEquals( "Actual contest approval audiences doesn't match with expected", expectedPromotion.getContestApprovalLevel1Audiences(), actualPromotion.getContestApprovalLevel1Audiences() );
  }

  /**
   * Test when the contest approval level 2 audiences
   */
  public void testSSIPromotionContestApprovalLevel2Audiences()
  {
    String uniqueString = buildUniqueString();

    // create a new recognition promotion
    SSIPromotion expectedPromotion = buildSSIPromotion();
    SSIPromotionContestApprovalLevel2Audience ssiPromotionContestApprovalLevel2Audience = new SSIPromotionContestApprovalLevel2Audience();
    Audience audience = saveAudience( uniqueString );
    ssiPromotionContestApprovalLevel2Audience.setAudience( audience );
    expectedPromotion.addContestApprovalLevel2Audiences( ssiPromotionContestApprovalLevel2Audience );
    getPromotionDAO().save( expectedPromotion );

    flushAndClearSession();

    // retrieve the promotion
    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
    associationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.SSI_CONTEST_APP_LVL_2_AUDIENCE ) );
    SSIPromotion actualPromotion = (SSIPromotion)getPromotionDAO().getPromotionByIdWithAssociations( expectedPromotion.getId(), associationRequestCollection );

    assertDomainObjectEquals( "Actual promotion doesn't match with expected", expectedPromotion, actualPromotion );
    assertEquals( "Actual contest approval audiences doesn't match with expected", expectedPromotion.getContestApprovalLevel2Audiences(), actualPromotion.getContestApprovalLevel2Audiences() );
  }

  public void testSaveNotifications()
  {
    SSIPromotion expectedPromotion = buildSSIPromotion();

    PromotionNotificationType expectedPromoNotification = new PromotionNotificationType();
    expectedPromoNotification.setPromotionEmailNotificationType( PromotionEmailNotificationType.lookup( PromotionEmailNotificationType.CONTEST_SETUP_LAUNCH_NOTIFY_CREATOR ) );
    expectedPromotion.addPromotionNotification( expectedPromoNotification );

    expectedPromoNotification = new PromotionNotificationType();
    expectedPromoNotification.setPromotionEmailNotificationType( PromotionEmailNotificationType.lookup( PromotionEmailNotificationType.CONTEST_PROGRESS_STATUS_NOTIFY_CREATOR ) );
    expectedPromotion.addPromotionNotification( expectedPromoNotification );

    expectedPromoNotification = new PromotionNotificationType();
    expectedPromoNotification.setPromotionEmailNotificationType( PromotionEmailNotificationType.lookup( PromotionEmailNotificationType.CONTEST_LAUNCH_NOTIFY_CREATOR_DO_THIS_GET_THAT ) );
    expectedPromotion.addPromotionNotification( expectedPromoNotification );

    expectedPromoNotification = new PromotionNotificationType();
    expectedPromoNotification.setPromotionEmailNotificationType( PromotionEmailNotificationType.lookup( PromotionEmailNotificationType.CONTEST_LAUNCH_NOTIFY_MGR_DO_THIS_GET_THAT ) );
    expectedPromotion.addPromotionNotification( expectedPromoNotification );

    expectedPromoNotification = new PromotionNotificationType();
    expectedPromoNotification.setPromotionEmailNotificationType( PromotionEmailNotificationType.lookup( PromotionEmailNotificationType.CONTEST_END_NOTIFY_CREATOR_DO_THIS_GET_THAT ) );
    expectedPromoNotification.setNumberOfDays( 5 );
    expectedPromotion.addPromotionNotification( expectedPromoNotification );

    expectedPromoNotification = new PromotionNotificationType();
    expectedPromoNotification.setPromotionEmailNotificationType( PromotionEmailNotificationType.lookup( PromotionEmailNotificationType.CONTEST_FINAL_RESULT_NOTIFY_PAX_DO_THIS_GET_THAT ) );
    expectedPromotion.addPromotionNotification( expectedPromoNotification );

    expectedPromoNotification = new PromotionNotificationType();
    expectedPromoNotification.setPromotionEmailNotificationType( PromotionEmailNotificationType.lookup( PromotionEmailNotificationType.CONTEST_APPROVAL_NOTIFY_APPROVER ) );
    expectedPromotion.addPromotionNotification( expectedPromoNotification );

    expectedPromoNotification = new PromotionNotificationType();
    expectedPromoNotification.setPromotionEmailNotificationType( PromotionEmailNotificationType.lookup( PromotionEmailNotificationType.CONTEST_APPROVAL_REMINDER_NOTIFY_APPROVER ) );
    expectedPromoNotification.setNumberOfDays( 5 );
    expectedPromotion.addPromotionNotification( expectedPromoNotification );

    expectedPromoNotification = new PromotionNotificationType();
    expectedPromoNotification.setPromotionEmailNotificationType( PromotionEmailNotificationType.lookup( PromotionEmailNotificationType.CONTEST_APPROVAL_STATUS_NOTIFY_CREATOR ) );
    expectedPromotion.addPromotionNotification( expectedPromoNotification );

    getPromotionDAO().save( expectedPromotion );

    flushAndClearSession();

    // retrieve the promotion
    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
    associationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.NOTIFICATIONS ) );
    SSIPromotion actualPromotion = (SSIPromotion)getPromotionDAO().getPromotionByIdWithAssociations( expectedPromotion.getId(), associationRequestCollection );

    assertDomainObjectEquals( "Actual promotion doesn't match with expected", expectedPromotion, actualPromotion );
    assertTrue( actualPromotion.getPromotionNotifications().size() > 0 );
    assertEquals( "Actual promotion doesn't match with expected", expectedPromotion.getPromotionNotifications().size(), actualPromotion.getPromotionNotifications().size() );
    assertEquals( "Actual promotion Claim approval doesn't match with expected", expectedPromotion.getPromotionNotifications(), actualPromotion.getPromotionNotifications() );
  }

  /**
   * Create a participant and save audience
   * @param uniqueString
   * @return
   */
  protected Audience saveAudience( String uniqueString )
  {
    Participant pax = ParticipantDAOImplTest.buildUniqueParticipant( uniqueString );
    getParticipantDAO().saveParticipant( pax );
    Audience audience = AudienceDAOImplTest.getPaxAudience( pax, uniqueString );
    getAudienceDAO().save( audience );
    return audience;
  }

  /**
   * buildAndSavePromotionCreatorAudience
   * 
   * @param pax
   * @param uniqueString
   * @param promotion
   * 
   * @return 
   */
  public static PromotionPrimaryAudience buildAndSavePromotionCreatorAudience( Participant pax, String uniqueString, Promotion promotion )
  {
    Audience audience = AudienceDAOImplTest.getPaxAudience( pax, uniqueString );
    getAudienceDAO().save( audience );
    return new PromotionPrimaryAudience( audience, promotion );
  }

  /**
   * buildAndSavePromotionParticipantAudience
   * @param pax
   * @param uniqueString
   * @param promotion
   * 
   * @return 
   */
  public static PromotionSecondaryAudience buildAndSavePromotionParticipantAudience( Participant pax, String uniqueString, Promotion promotion )
  {
    Audience audience = AudienceDAOImplTest.getPaxAudience( pax, uniqueString );
    getAudienceDAO().save( audience );
    return new PromotionSecondaryAudience( audience, promotion );
  }

}

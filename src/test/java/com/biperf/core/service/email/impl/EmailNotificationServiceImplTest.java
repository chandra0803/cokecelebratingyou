/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javatest/com/biperf/core/service/email/impl/EmailNotificationServiceImplTest.java,v $
 */

package com.biperf.core.service.email.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.easymock.EasyMock;

import com.biperf.core.domain.claim.Claim;
import com.biperf.core.domain.claim.ClaimElement;
import com.biperf.core.domain.claim.ClaimForm;
import com.biperf.core.domain.claim.ClaimFormStep;
import com.biperf.core.domain.claim.ClaimFormStepElement;
import com.biperf.core.domain.claim.ClaimFormStepEmailNotification;
import com.biperf.core.domain.claim.ProductClaim;
import com.biperf.core.domain.enums.ApprovalType;
import com.biperf.core.domain.enums.ApproverType;
import com.biperf.core.domain.enums.ClaimFormStepEmailNotificationType;
import com.biperf.core.domain.enums.EmailAddressType;
import com.biperf.core.domain.enums.HierarchyRoleType;
import com.biperf.core.domain.enums.InsertFieldType;
import com.biperf.core.domain.enums.LanguageType;
import com.biperf.core.domain.enums.MockPickListFactory;
import com.biperf.core.domain.enums.PromotionType;
import com.biperf.core.domain.enums.VerificationStatusType;
import com.biperf.core.domain.hierarchy.Node;
import com.biperf.core.domain.mailing.Mailing;
import com.biperf.core.domain.message.Message;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.ClaimFormNotificationType;
import com.biperf.core.domain.promotion.ProductClaimPromotion;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.PromotionNotification;
import com.biperf.core.domain.user.User;
import com.biperf.core.domain.user.UserEmailAddress;
import com.biperf.core.domain.user.UserNode;
import com.biperf.core.service.BaseServiceTest;
import com.biperf.core.service.claim.ClaimFormDefinitionService;
import com.biperf.core.service.email.MailingService;
import com.biperf.core.service.message.MessageService;
import com.biperf.core.service.promotion.PromotionNotificationService;
import com.objectpartners.cms.service.ContentReader;
import com.objectpartners.cms.util.ContentReaderManager;

/**
 * EmailNotificationServiceImplTest
 * 
 *
 */
public class EmailNotificationServiceImplTest extends BaseServiceTest
{

  private EmailNotificationServiceImpl classUnderTest;

  private MockPickListFactory mockFactory = new MockPickListFactory();

  private ClaimFormDefinitionService claimFormDefinitionServiceMock;
  private MessageService messageServiceMock;
  private PromotionNotificationService promotionNotificationServiceMock;
  private MailingService mailingServiceMock;

  private static final Long PROMOTION_ID = new Long( 5 );
  private static final String PROMOTION_NAME = "New Promotion";
  private static final Long CLAIM_ID = new Long( 1 );
  private static final Long SUBMITTER_ID = new Long( 500 );
  private static final Long APPROVER_ID = new Long( 750 );
  private static final Long EMAIL_ADDRESS_ID = new Long( 1000 );
  private static final Long APPROVER_EMAIL_ADDRESS_ID = new Long( 1001 );
  private static final Long PROMO_NOTIFICATION_ID = new Long( 2000 );

  private static final Long MESSAGE_ID = new Long( 5000 );
  private static final Long CLAIM_FORM_STEP_ID = new Long( 6000 );

  private static final Long NODE_ID = new Long( 7000 );

  private static final Long SUBMITTED_EMAIL_NOTIFICATION_ID = new Long( 3000 );
  private static final Long SUBMITTED_NOTIFICATION_MESSAGE_ID = new Long( 4000 );
  private static final String SUBMITTED_CM_ASSETCODE = new String( "message_data.message.10000831" );

  private static final Long CLOSED_EMAIL_NOTIFICATION_ID = new Long( 3000 );
  private static final Long CLOSED_NOTIFICATION_MESSAGE_ID = new Long( 4001 );
  private static final String CLOSED_CM_ASSETCODE = new String( "message_data.message.10000832" );

  private ContentReader oldContentReader = null;

  /**
   * Test setup Overridden from
   * 
   * @see junit.framework.TestCase#setUp()
   * @throws Exception
   */
  @Override
  public void setUp() throws Exception
  {
    super.setUp();

    messageServiceMock = EasyMock.createMock( MessageService.class );
    mailingServiceMock = EasyMock.createMock( MailingService.class );
    promotionNotificationServiceMock = EasyMock.createMock( PromotionNotificationService.class );
    claimFormDefinitionServiceMock = EasyMock.createMock( ClaimFormDefinitionService.class );

    classUnderTest = new EmailNotificationServiceImpl();

    classUnderTest.setClaimFormDefinitionService( claimFormDefinitionServiceMock );
    classUnderTest.setMessageService( messageServiceMock );
    classUnderTest.setMailingService( mailingServiceMock );
    oldContentReader = ContentReaderManager.getContentReader();

  }

  @Override
  public void tearDown() throws Exception
  {
    // restore the content reader for incontainer tests.
    if ( oldContentReader != null )
    {
      ContentReaderManager.setContentReader( oldContentReader );
    }
    super.tearDown();
  }

  /**
   * Tests processing of submitted AND approved claim notifications specifically methods:
   * testProcessClosedClaimNotifications and testProcessSubmittedClaimNotifications
   * 
   * @throws Exception
   */
  public void testProcessClaimNotifications() throws Exception
  {

    // Setup - build claim for both submitted and closed notifications
    String approvalTypeCode = ApprovalType.AUTOMATIC_IMMEDIATE;
    Participant submitter = buildSubmitter();
    Participant approver = buildApprover();
    ClaimFormStep claimFormStep = buildClaimFormStep();

    Node node = buildNode( approver, submitter );

    // Add node to submitter & approver (this is where it will be accessed)
    submitter.addNode( node, new Boolean( true ), (HierarchyRoleType)mockFactory.getPickListItem( HierarchyRoleType.class, HierarchyRoleType.MEMBER ), false );

    approver.addNode( node, new Boolean( true ), (HierarchyRoleType)mockFactory.getPickListItem( HierarchyRoleType.class, HierarchyRoleType.MANAGER ), false );

    // Build the claim
    Claim claim = buildTestClaim( submitter, approvalTypeCode, claimFormStep );

    // TEST
    // SUBMITTED CLAIM
    EasyMock.expect( claimFormDefinitionServiceMock.getClaimFormStep( CLAIM_FORM_STEP_ID ) ).andReturn( claimFormStep );
    EasyMock.replay( claimFormDefinitionServiceMock );

    PromotionNotification promotionNotification = buildPromotionNotification( SUBMITTED_NOTIFICATION_MESSAGE_ID );

    EasyMock.expect( promotionNotificationServiceMock.getClaimPromotionNotificationByClaimFormStepEmailNotificationId( claim.getPromotion().getId(), SUBMITTED_EMAIL_NOTIFICATION_ID ) )
        .andReturn( promotionNotification );
    EasyMock.replay( promotionNotificationServiceMock );

    Message message = buildMessage( SUBMITTED_CM_ASSETCODE );
    EasyMock.expect( messageServiceMock.getMessageById( SUBMITTED_NOTIFICATION_MESSAGE_ID ) ).andReturn( message );
    EasyMock.replay( messageServiceMock );

    Map<Object, Object> objectMap = new HashMap<>();
    Promotion promotion = new ProductClaimPromotion();
    promotion.setId( PROMOTION_ID );

    objectMap.put( InsertFieldType.PROMOTION_NAME_CODE, PROMOTION_NAME );

    EasyMock.expect( mailingServiceMock.submitMailing( new Mailing(), objectMap ) ).andReturn( new Mailing() );
    EasyMock.replay( mailingServiceMock );

    classUnderTest.processSubmittedClaimNotifications( claim, CLAIM_FORM_STEP_ID, null, true );

    // RESET
    EasyMock.reset( promotionNotificationServiceMock );
    EasyMock.reset( messageServiceMock );
    EasyMock.reset( claimFormDefinitionServiceMock );
    EasyMock.reset( mailingServiceMock );

    // CLOSED CLAIM
    promotionNotification = buildPromotionNotification( CLOSED_NOTIFICATION_MESSAGE_ID );

    EasyMock.expect( promotionNotificationServiceMock.getClaimPromotionNotificationByClaimFormStepEmailNotificationId( claim.getPromotion().getId(), CLOSED_EMAIL_NOTIFICATION_ID ) )
        .andReturn( promotionNotification );
    EasyMock.replay( promotionNotificationServiceMock );

    message = buildMessage( CLOSED_CM_ASSETCODE );
    EasyMock.expect( messageServiceMock.getMessageById( CLOSED_NOTIFICATION_MESSAGE_ID ) ).andReturn( message );
    EasyMock.replay( messageServiceMock );

    EasyMock.expect( mailingServiceMock.submitMailing( new Mailing(), objectMap ) ).andReturn( new Mailing() );
    EasyMock.replay( mailingServiceMock );

    classUnderTest.processClosedClaimNotifications( claim );

  }

  /**
   * Build test claim for use by both submitted and closed notifications
   * 
   * @param userToReturn
   * @param approvalTypeCode
   * @param claimFormStep
   * @return Claim
   */
  private Claim buildTestClaim( Participant userToReturn, String approvalTypeCode, ClaimFormStep claimFormStep )

  {
    // create promotion
    // we are adding approvers here (this is where it is referenced to
    // by Claim
    Promotion promotion = new ProductClaimPromotion();
    promotion.setPromotionType( PromotionType.lookup( PromotionType.PRODUCT_CLAIM ) );
    promotion.setId( PROMOTION_ID );
    promotion.setApprovalType( (ApprovalType)mockFactory.getPickListItem( ApprovalType.class, approvalTypeCode ) );
    promotion.setApproverType( (ApproverType)mockFactory.getPickListItem( ApproverType.class, ApproverType.SUBMITTERS_MANAGER ) );
    promotion.setName( PROMOTION_NAME );

    // create claimform
    ClaimForm claimForm = new ClaimForm();

    // claim form step generated outside this method for mock
    // claimFormStepService usage

    // add claimFormStepList to claimForm
    ArrayList<ClaimFormStep> claimFormStepList = new ArrayList<>();
    claimFormStepList.add( claimFormStep );
    claimForm.setClaimFormSteps( claimFormStepList );

    // add claimForm to promotion
    promotion.setClaimForm( claimForm );

    // create claim
    ProductClaim claim = new ProductClaim();
    claim.setId( CLAIM_ID );
    claim.setSubmissionDate( new Date() );
    claim.setPromotion( promotion );
    claim.setSubmitter( userToReturn );

    ClaimElement claimElement = new ClaimElement();
    claimElement.setId( new Long( 888 ) );
    // add the related claimFormStepElement (from above)
    claimElement.setClaimFormStepElement( (ClaimFormStepElement)claimFormStep.getClaimFormStepElements().get( 0 ) );
    claimElement.setValue( "Jimmy" );

    ArrayList<ClaimElement> claimElements = new ArrayList<>();
    claimElements.add( claimElement );

    claim.setClaimElements( claimElements );
    return claim;
  }

  private ClaimFormStep buildClaimFormStep()
  {
    // create claimformstep to add to claimForm
    ClaimFormStep claimFormStep = new ClaimFormStep();
    claimFormStep.setId( CLAIM_FORM_STEP_ID );

    // create claimformstep element to add to claimFormStep
    ClaimFormStepElement claimFormStepElement = new ClaimFormStepElement();
    claimFormStepElement.setId( new Long( 100 ) );
    claimFormStepElement.setClaimFormStep( claimFormStep );
    claimFormStep.addClaimFormStepElement( claimFormStepElement );

    ClaimFormStepEmailNotification submittedClaimFormStepEmailNotification = new ClaimFormStepEmailNotification();

    submittedClaimFormStepEmailNotification.setId( SUBMITTED_EMAIL_NOTIFICATION_ID );
    submittedClaimFormStepEmailNotification.setClaimFormStep( claimFormStep );

    submittedClaimFormStepEmailNotification.setClaimFormStepEmailNotificationType( (ClaimFormStepEmailNotificationType)mockFactory
        .getPickListItem( ClaimFormStepEmailNotificationType.class, ClaimFormStepEmailNotificationType.CLAIM_SUBMITTED ) );

    claimFormStep.addClaimFormStepEmailNotification( submittedClaimFormStepEmailNotification );

    ClaimFormStepEmailNotification closedClaimFormStepEmailNotification = new ClaimFormStepEmailNotification();

    closedClaimFormStepEmailNotification.setId( CLOSED_EMAIL_NOTIFICATION_ID );
    closedClaimFormStepEmailNotification.setClaimFormStep( claimFormStep );

    closedClaimFormStepEmailNotification.setClaimFormStepEmailNotificationType( (ClaimFormStepEmailNotificationType)mockFactory.getPickListItem( ClaimFormStepEmailNotificationType.class,
                                                                                                                                                 ClaimFormStepEmailNotificationType.CLAIM_APPROVED ) );

    claimFormStep.addClaimFormStepEmailNotification( closedClaimFormStepEmailNotification );

    return claimFormStep;
  }

  private PromotionNotification buildPromotionNotification( Long notificationMessageId )
  {
    PromotionNotification promoNotification = new ClaimFormNotificationType();
    promoNotification.setId( PROMO_NOTIFICATION_ID );
    // do not need to set promotion here
    promoNotification.setNotificationMessageId( notificationMessageId.longValue() );

    promoNotification.setPromotion( buildPromotion() );
    return promoNotification;
  }

  private Promotion buildPromotion()
  {
    Promotion promotion = new ProductClaimPromotion();
    promotion.setName( PROMOTION_NAME );
    return promotion;
  }

  private Message buildMessage( String cmAssetCode )
  {
    Message message = new Message();
    message.setId( MESSAGE_ID );
    message.setCmAssetCode( cmAssetCode );
    return message;
  }

  private Node buildNode( Participant approver, Participant submitter )
  {
    Node node = new Node();
    node.setName( "Test" );
    node.setId( NODE_ID );

    Set<UserNode> userNodes = new LinkedHashSet<>();

    userNodes.add( buildSubmitterUserNode( submitter, node ) );
    userNodes.add( buildApproverUserNode( approver, node ) );

    node.setUserNodes( userNodes );

    return node;
  }

  private UserNode buildSubmitterUserNode( User submitter, Node node )
  {
    UserNode submitterUserNode = new UserNode();
    submitterUserNode.setIsPrimary( Boolean.TRUE );
    submitterUserNode.setUser( submitter );
    submitterUserNode.setNode( node );
    submitterUserNode.setActive( new Boolean( true ) );
    submitterUserNode.setHierarchyRoleType( (HierarchyRoleType)mockFactory.getPickListItem( HierarchyRoleType.class, HierarchyRoleType.MEMBER ) );
    return submitterUserNode;
  }

  private UserNode buildApproverUserNode( User approver, Node node )
  {
    UserNode approverUserNode = new UserNode();
    approverUserNode.setIsPrimary( Boolean.TRUE );
    approverUserNode.setUser( approver );
    approverUserNode.setNode( node );
    approverUserNode.setActive( new Boolean( true ) );
    approverUserNode.setHierarchyRoleType( (HierarchyRoleType)mockFactory.getPickListItem( HierarchyRoleType.class, HierarchyRoleType.MANAGER ) );
    return approverUserNode;
  }

  private Participant buildSubmitter()
  {

    Participant submitter = new Participant();
    submitter.setUserName( "submitter" );
    submitter.setFirstName( "Harry" );
    submitter.setLastName( "Caray" );
    submitter.setId( SUBMITTER_ID );
    submitter.setLanguageType( (LanguageType)mockFactory.getPickListItem( LanguageType.class, LanguageType.ENGLISH ) );

    UserEmailAddress userEmailAddress = new UserEmailAddress();
    userEmailAddress.setId( EMAIL_ADDRESS_ID );
    userEmailAddress.setEmailAddr( "submitter@client.com" );

    EmailAddressType emailAddressType = (EmailAddressType)mockFactory.getPickListItem( EmailAddressType.class, EmailAddressType.BUSINESS );
    VerificationStatusType verificationStatus = (VerificationStatusType)mockFactory.getPickListItem( VerificationStatusType.class, VerificationStatusType.UNVERIFIED );

    userEmailAddress.setEmailType( emailAddressType );
    userEmailAddress.setVerificationStatus( verificationStatus );
    userEmailAddress.setIsPrimary( new Boolean( true ) );

    submitter.addUserEmailAddress( userEmailAddress );

    return submitter;
  }

  private Participant buildApprover()
  {

    Participant approver = new Participant();
    approver.setUserName( "manager" );
    approver.setFirstName( "Manager" );
    approver.setLastName( "Approver" );
    approver.setId( APPROVER_ID );
    approver.setLanguageType( (LanguageType)mockFactory.getPickListItem( LanguageType.class, LanguageType.ENGLISH ) );

    UserEmailAddress userEmailAddress = new UserEmailAddress();
    userEmailAddress.setId( APPROVER_EMAIL_ADDRESS_ID );
    userEmailAddress.setEmailAddr( "approver@client.com" );

    EmailAddressType emailAddressType = (EmailAddressType)mockFactory.getPickListItem( EmailAddressType.class, EmailAddressType.BUSINESS );
    VerificationStatusType verificationStatus = (VerificationStatusType)mockFactory.getPickListItem( VerificationStatusType.class, VerificationStatusType.UNVERIFIED );

    userEmailAddress.setEmailType( emailAddressType );
    userEmailAddress.setVerificationStatus( verificationStatus );
    userEmailAddress.setIsPrimary( new Boolean( true ) );

    approver.addUserEmailAddress( userEmailAddress );

    return approver;
  }
}

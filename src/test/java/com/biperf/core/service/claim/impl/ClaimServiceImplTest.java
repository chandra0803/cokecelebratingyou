/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javatest/com/biperf/core/service/claim/impl/ClaimServiceImplTest.java,v $
 */

package com.biperf.core.service.claim.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.time.DateUtils;
import org.easymock.EasyMock;

import com.biperf.core.dao.claim.ClaimDAO;
import com.biperf.core.dao.claim.hibernate.ClaimFormDAOImplTest;
import com.biperf.core.dao.claim.hibernate.ClaimQueryConstraint;
import com.biperf.core.dao.claim.hibernate.QuizClaimQueryConstraint;
import com.biperf.core.dao.product.hibernate.ProductDAOImplTest;
import com.biperf.core.domain.claim.Claim;
import com.biperf.core.domain.claim.ClaimElement;
import com.biperf.core.domain.claim.ClaimForm;
import com.biperf.core.domain.claim.ClaimFormStep;
import com.biperf.core.domain.claim.ClaimFormStepElement;
import com.biperf.core.domain.claim.ClaimProduct;
import com.biperf.core.domain.claim.ProductClaim;
import com.biperf.core.domain.claim.QuizClaim;
import com.biperf.core.domain.enums.ApprovalConditionalAmmountOperatorType;
import com.biperf.core.domain.enums.ApprovalStatusType;
import com.biperf.core.domain.enums.ApprovalType;
import com.biperf.core.domain.enums.PromotionType;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.product.Product;
import com.biperf.core.domain.promotion.ProductClaimPromotion;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.PromotionParticipantSubmitter;
import com.biperf.core.domain.promotion.QuizPromotion;
import com.biperf.core.exception.BeaconRuntimeException;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.BaseServiceTest;
import com.biperf.core.service.claim.ClaimApproverSnapshotService;
import com.biperf.core.service.claim.ClaimProcessingStrategy;
import com.biperf.core.service.claim.ClaimProcessingStrategyFactory;
import com.biperf.core.service.email.EmailNotificationService;
import com.biperf.core.service.gamification.GamificationService;
import com.biperf.core.strategy.RandomNumberStrategy;
import com.biperf.core.utils.ClaimApproveUtils;
import com.biperf.core.utils.ClaimElementValidator;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.ClaimApproversValue;
import com.biperf.core.value.ClaimInfoBean;
import com.biperf.core.value.ParticipantQuizClaimHistory;

/**
 * ClaimServiceImplTest.
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
 * <td>Adam</td>
 * <td>Jun 29, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ClaimServiceImplTest extends BaseServiceTest
{
  private ClaimServiceImpl classUnderTest;
  private ClaimDAO claimDAOMock;
  private EmailNotificationService emailNotificationServiceMock;
  // private ParticipantService participantServiceMock;
  // private PromotionService promotionServiceMock;
  private ClaimProcessingStrategyFactory claimProcessingStrategyFactoryMock;
  private ClaimProcessingStrategy claimProcessingStrategyMock;
  private ClaimElementValidator claimElementValidatorMock;
  private RandomNumberStrategy randomNumberStrategyMock;

  private ClaimApproverSnapshotService claimApproverSnapshotServiceMock;
  private GamificationService gamificationServiceMock;

  public void setUp() throws Exception
  {
    super.setUp();

    emailNotificationServiceMock = EasyMock.createMock( EmailNotificationService.class );
    claimDAOMock = EasyMock.createMock( ClaimDAO.class );
    claimApproverSnapshotServiceMock = EasyMock.createMock( ClaimApproverSnapshotService.class );
    gamificationServiceMock = EasyMock.createMock( GamificationService.class );
    claimProcessingStrategyFactoryMock = EasyMock.createMock( ClaimProcessingStrategyFactory.class );
    claimProcessingStrategyMock = EasyMock.createMock( ClaimProcessingStrategy.class );
    claimElementValidatorMock = EasyMock.createMock( ClaimElementValidator.class );
    randomNumberStrategyMock = EasyMock.createMock( RandomNumberStrategy.class );

    classUnderTest = new ClaimServiceImpl();
    classUnderTest.setEmailNotificationService( emailNotificationServiceMock );
    classUnderTest.setClaimDAO( claimDAOMock );
    classUnderTest.setClaimProcessingStrategyFactory( claimProcessingStrategyFactoryMock );
    classUnderTest.setClaimElementValidator( claimElementValidatorMock );
    classUnderTest.setRandomNumberStrategy( randomNumberStrategyMock );
    classUnderTest.setClaimApproverSnapshotService( claimApproverSnapshotServiceMock );
    classUnderTest.setGamificationService( gamificationServiceMock );

  }

  public void testSaveClaimAutoImmediateApproval() throws Exception
  {
    Participant userToReturn = new Participant();
    userToReturn.setId( UserManager.getUserId() );

    String approvalTypeCode = ApprovalType.AUTOMATIC_IMMEDIATE;

    ProductClaim claim = buildTestClaim( userToReturn, approvalTypeCode );

    boolean expectClaimClosed = true;

    runInsertClaim( userToReturn, claim, expectClaimClosed );
  }

  public void testSaveClaimAutoDelayedApproval() throws Exception
  {
    Participant userToReturn = new Participant();
    userToReturn.setId( UserManager.getUserId() );

    String approvalTypeCode = ApprovalType.AUTOMATIC_DELAYED;

    ProductClaim claim = buildTestClaim( userToReturn, approvalTypeCode );

    boolean expectClaimClosed = false;

    runInsertClaim( userToReturn, claim, expectClaimClosed );
  }

  public void testSaveClaimManualApproval() throws Exception
  {
    Participant userToReturn = new Participant();
    userToReturn.setId( UserManager.getUserId() );

    String approvalTypeCode = ApprovalType.MANUAL;

    ProductClaim claim = buildTestClaim( userToReturn, approvalTypeCode );

    boolean expectClaimClosed = false;

    runInsertClaim( userToReturn, claim, expectClaimClosed );
  }

  public void testSaveClaimConditionalAmountBasedApproval() throws Exception
  {
    Participant userToReturn = new Participant();
    userToReturn.setId( UserManager.getUserId() );

    String approvalTypeCode = ApprovalType.CONDITIONAL_AMOUNT_BASED;

    ProductClaim claim = buildTestClaim( userToReturn, approvalTypeCode );

    ClaimForm claimForm = ClaimFormDAOImplTest.buildClaimFormDomainObjectWithStepsAndElements();

    ClaimFormStep claimFormStep = (ClaimFormStep)claimForm.getClaimFormSteps().get( 0 );

    ClaimElement claimElement = new ClaimElement();
    ClaimFormStepElement claimFormStepElement = (ClaimFormStepElement)claimFormStep.getClaimFormStepElements().get( 0 );

    claimElement.setClaimFormStepElement( claimFormStepElement );
    claim.addClaimElement( claimElement );

    claim.getPromotion().setApprovalConditionalAmount( new Double( 10.2 ) );
    claim.getPromotion().setApprovalConditionalAmountField( claimFormStepElement );

    runConditionalAmountBoundaryTests( userToReturn, claim, claimElement, ApprovalConditionalAmmountOperatorType.LT, false, true, true );
    runConditionalAmountBoundaryTests( userToReturn, claim, claimElement, ApprovalConditionalAmmountOperatorType.LTEQ, false, false, true );
    runConditionalAmountBoundaryTests( userToReturn, claim, claimElement, ApprovalConditionalAmmountOperatorType.EQ, true, false, true );
    runConditionalAmountBoundaryTests( userToReturn, claim, claimElement, ApprovalConditionalAmmountOperatorType.GTEQ, true, false, false );
    runConditionalAmountBoundaryTests( userToReturn, claim, claimElement, ApprovalConditionalAmmountOperatorType.GT, true, true, false );

    // test when no claim for step exists on claim
    EasyMock.reset( claimElementValidatorMock );
    EasyMock.expect( claimElementValidatorMock.isValid( claimElement, claim.getPromotion() ) ).andReturn( true );
    EasyMock.replay( claimElementValidatorMock );
    claimElement.setValue( null );

    claim.getPromotion().setApprovalConditionalAmountOperator( ApprovalConditionalAmmountOperatorType.lookup( ApprovalConditionalAmmountOperatorType.GT ) );
    boolean expectClaimClosed = true;
    runInsertClaim( userToReturn, claim, expectClaimClosed );
  }

  public void testSaveClaimPaxBasedApproval() throws Exception
  {
    Participant userToReturn = new Participant();
    userToReturn.setId( UserManager.getUserId() );

    String approvalTypeCode = ApprovalType.CONDITIONAL_PAX_BASED;

    ProductClaim claim = buildTestClaim( userToReturn, approvalTypeCode );

    // Case where no users are in pax list
    boolean expectClaimClosed = true;
    runInsertClaim( userToReturn, claim, expectClaimClosed );

    // Reset approval status
    ClaimApproveUtils.markUndeniedApprovableItemsPending( claim, null );

    // Add our participant to approval pax list
    PromotionParticipantSubmitter promotionParticipantSubmitter = new PromotionParticipantSubmitter();
    promotionParticipantSubmitter.setParticipant( userToReturn );
    promotionParticipantSubmitter.setPromotion( claim.getPromotion() );
    claim.getPromotion().addPromotionParticipantSubmitter( promotionParticipantSubmitter );

    expectClaimClosed = false;
    runInsertClaim( userToReturn, claim, expectClaimClosed );

  }

  public void testSaveClaimNthBasedApproval() throws Exception
  {
    Participant userToReturn = new Participant();
    userToReturn.setId( UserManager.getUserId() );

    String approvalTypeCode = ApprovalType.CONDITIONAL_NTH_BASED;

    ProductClaim claim = buildTestClaim( userToReturn, approvalTypeCode );

    // Set manual approve on every 2nd claim submitted
    claim.getPromotion().setApprovalConditionalClaimCount( new Integer( 3 ) );

    // #1 claim should not met critieria, thus claim stays open
    boolean expectClaimClosed = true;
    runInsertClaim( userToReturn, claim, expectClaimClosed, new Integer( 0 ) );

    // Reset approval status
    ClaimApproveUtils.markUndeniedApprovableItemsPending( claim, null );

    // #2 claim should not met critieria, thus claim should close
    expectClaimClosed = false;
    claim.getPromotion().setApprovalNodeLevels( new Integer( 3 ) );
    runInsertClaim( userToReturn, claim, expectClaimClosed, new Integer( 1 ) );

    // Reset approval status
    ClaimApproveUtils.markUndeniedApprovableItemsPending( claim, null );

    // #3 claim should met critieria, thus claim should close
    claim.getPromotion().setApprovalNodeLevels( new Integer( 3 ) );
    expectClaimClosed = false;
    runInsertClaim( userToReturn, claim, expectClaimClosed, new Integer( 2 ) );

    // Reset approval status
    ClaimApproveUtils.markUndeniedApprovableItemsPending( claim, null );

    // #4 claim should not met critieria, thus claim stays open
    expectClaimClosed = true;
    runInsertClaim( userToReturn, claim, expectClaimClosed, new Integer( 3 ) );
  }

  public void testGetParticipantQuizClaimHistoryByPromotionMap()
  {
    QuizClaimQueryConstraint claimQueryConstraint = new QuizClaimQueryConstraint();

    QuizPromotion expectedPromotion1 = new QuizPromotion();
    expectedPromotion1.setName( "promo1" );
    QuizClaim promo1claim1 = new QuizClaim();
    promo1claim1.setPromotion( expectedPromotion1 );
    QuizClaim promo1claim2 = new QuizClaim();
    promo1claim2.setPromotion( expectedPromotion1 );
    QuizClaim promo1claim3 = new QuizClaim();
    promo1claim3.setPromotion( expectedPromotion1 );
    QuizClaim promo1claim4 = new QuizClaim();
    promo1claim4.setPromotion( expectedPromotion1 );
    ParticipantQuizClaimHistory participantQuizClaimHistory1 = new ParticipantQuizClaimHistory();
    participantQuizClaimHistory1.setPromotion( expectedPromotion1 );
    List<QuizClaim> promo1Claims = new ArrayList<>();
    promo1Claims.add( promo1claim1 );
    promo1Claims.add( promo1claim2 );
    promo1Claims.add( promo1claim3 );
    promo1Claims.add( promo1claim4 );
    participantQuizClaimHistory1.addQuizClaim( promo1claim1 );
    participantQuizClaimHistory1.addQuizClaim( promo1claim2 );

    QuizPromotion expectedPromotion2 = new QuizPromotion();
    expectedPromotion2.setName( "promo2" );
    QuizClaim promo2claim1 = new QuizClaim();
    promo2claim1.setPromotion( expectedPromotion2 );
    QuizClaim promo2claim2 = new QuizClaim();
    promo2claim2.setPromotion( expectedPromotion2 );
    QuizClaim promo2claim3 = new QuizClaim();
    promo2claim3.setPromotion( expectedPromotion2 );
    QuizClaim promo2claim4 = new QuizClaim();
    promo2claim4.setPromotion( expectedPromotion2 );
    ParticipantQuizClaimHistory participantQuizClaimHistory2 = new ParticipantQuizClaimHistory();
    participantQuizClaimHistory2.setPromotion( expectedPromotion2 );
    List<Object> promo2Claims = new ArrayList<>();
    promo2Claims.add( promo2claim1 );
    promo2Claims.add( promo2claim2 );
    promo2Claims.add( promo2claim3 );
    promo2Claims.add( promo2claim4 );
    participantQuizClaimHistory2.addQuizClaim( promo2claim1 );
    participantQuizClaimHistory2.addQuizClaim( promo2claim2 );

    ArrayList<Object> claims = new ArrayList<>();
    claims.add( promo1claim1 );
    claims.add( promo1claim2 );
    claims.add( promo2claim1 );
    claims.add( promo2claim2 );

    EasyMock.expect( claimDAOMock.getClaimList( claimQueryConstraint ) ).andReturn( claims );
    EasyMock.replay( claimDAOMock );
    Map<Object, Object> participantQuizClaimHistoryByPromotionMap = classUnderTest.getParticipantQuizClaimHistoryByPromotionMap( claimQueryConstraint, null );

    assertEquals( 2, participantQuizClaimHistoryByPromotionMap.size() );

    ParticipantQuizClaimHistory actualParticipantQuizClaimHistory1 = (ParticipantQuizClaimHistory)participantQuizClaimHistoryByPromotionMap.get( expectedPromotion1 );
    assertEquals( promo1Claims.subList( 0, 2 ), actualParticipantQuizClaimHistory1.getQuizClaimsBySubmissionDate() );

    ParticipantQuizClaimHistory actualParticipantQuizClaimHistory2 = (ParticipantQuizClaimHistory)participantQuizClaimHistoryByPromotionMap.get( expectedPromotion2 );
    assertEquals( promo2Claims.subList( 0, 2 ), actualParticipantQuizClaimHistory2.getQuizClaimsBySubmissionDate() );

  }

  /**
   * Try the condition for a value below, at, and above the conditional value. For the operator
   * type, set the expected claim open result for below, at , and above.
   * 
   * @param userToReturn
   * @param claim
   * @param claimElement
   * @param operatorTypeCode
   * @param expectClaimClosedValueBelow
   * @param expectClaimClosedValueAt
   * @param expectClaimClosedValueAbove
   * @throws ServiceErrorException
   */
  private void runConditionalAmountBoundaryTests( Participant userToReturn,
                                                  ProductClaim claim,
                                                  ClaimElement claimElement,
                                                  String operatorTypeCode,
                                                  boolean expectClaimClosedValueBelow,
                                                  boolean expectClaimClosedValueAt,
                                                  boolean expectClaimClosedValueAbove )
      throws ServiceErrorException
  {
    boolean expectClaimClosed;

    // Reset approval status
    ClaimApproveUtils.markUndeniedApprovableItemsPending( claim, null );

    // test "="
    EasyMock.reset( claimElementValidatorMock );
    EasyMock.expect( claimElementValidatorMock.isValid( claimElement, claim.getPromotion() ) ).andReturn( true );
    EasyMock.replay( claimElementValidatorMock );
    claimElement.setValue( "10.2" );

    claim.getPromotion().setApprovalConditionalAmountOperator( ApprovalConditionalAmmountOperatorType.lookup( operatorTypeCode ) );
    expectClaimClosed = expectClaimClosedValueAt;
    runInsertClaim( userToReturn, claim, expectClaimClosed );

    // Reset approval status
    ClaimApproveUtils.markUndeniedApprovableItemsPending( claim, null );

    // test "<"
    EasyMock.reset( claimElementValidatorMock );
    EasyMock.expect( claimElementValidatorMock.isValid( claimElement, claim.getPromotion() ) ).andReturn( true );
    EasyMock.replay( claimElementValidatorMock );
    claimElement.setValue( "10.1" );
    claim.getPromotion().setApprovalConditionalAmountOperator( ApprovalConditionalAmmountOperatorType.lookup( operatorTypeCode ) );
    expectClaimClosed = expectClaimClosedValueBelow;
    runInsertClaim( userToReturn, claim, expectClaimClosed );

    // Reset approval status
    ClaimApproveUtils.markUndeniedApprovableItemsPending( claim, null );

    // test ">"
    EasyMock.reset( claimElementValidatorMock );
    EasyMock.expect( claimElementValidatorMock.isValid( claimElement, claim.getPromotion() ) ).andReturn( true );
    EasyMock.replay( claimElementValidatorMock );
    claimElement.setValue( "10.3" );
    claim.getPromotion().setApprovalConditionalAmountOperator( ApprovalConditionalAmmountOperatorType.lookup( operatorTypeCode ) );
    expectClaimClosed = expectClaimClosedValueAbove;
    runInsertClaim( userToReturn, claim, expectClaimClosed );
  }

  /**
   * @param userToReturn
   * @param claim
   * @param expectClaimClosed
   * @throws ServiceErrorException
   */
  private void runInsertClaim( Participant userToReturn, ProductClaim claim, boolean expectClaimClosed ) throws ServiceErrorException
  {
    runInsertClaim( userToReturn, claim, expectClaimClosed, null );

  }

  private void runInsertClaim( Participant userToReturn, ProductClaim claim, boolean expectClaimClosed, Integer optionalNthBasedCurrentClaimCount ) throws ServiceErrorException
  {

    EasyMock.expect( claimDAOMock.saveClaim( claim ) ).andReturn( claim );

    if ( optionalNthBasedCurrentClaimCount != null )
    {
      EasyMock.expect( claimDAOMock.getClaimSubmittedCount( claim.getPromotion().getId() ) ).andReturn( optionalNthBasedCurrentClaimCount.intValue() );
    }

    if ( expectClaimClosed )
    {
      claimProcessingStrategyMock.processApprovable( claim, true, null ); // Fixed as part of bug
                                                                          // #56006,55519
      EasyMock.replay( claimProcessingStrategyMock );

      EasyMock.expect( claimProcessingStrategyFactoryMock.getClaimProcessingStrategy( claim.getPromotion().getPromotionType() ) ).andReturn( claimProcessingStrategyMock );

      EasyMock.replay( claimProcessingStrategyFactoryMock );

      if ( !claim.isOpen() )
      {
        EasyMock.expect( gamificationServiceMock.populateBadgePartcipant( (Claim)claim ) ).andReturn( null );
        EasyMock.replay( gamificationServiceMock );
      }

      emailNotificationServiceMock.processClosedClaimNotifications( claim );
    }
    else
    {
      EasyMock.expect( claimProcessingStrategyFactoryMock.getClaimProcessingStrategy( claim.getPromotion().getPromotionType() ) ).andReturn( claimProcessingStrategyMock );

      EasyMock.replay( claimProcessingStrategyFactoryMock );
      claim.setOpen( true );
      emailNotificationServiceMock.processSubmittedClaimNotifications( claim, null, null, true );
    }
    EasyMock.expect( claimDAOMock.saveClaim( claim ) ).andReturn( claim );

    claimApproverSnapshotServiceMock.updateClaimApproverSnapshot( claim, null, true );

    final ClaimApproversValue claimApproversValue = new ClaimApproversValue();
    claimApproversValue.setApproverUsers( new HashSet<>() );
    EasyMock.expect( claimApproverSnapshotServiceMock.getApprovers( EasyMock.anyObject() ) ).andReturn( claimApproversValue );

    EasyMock.replay( claimApproverSnapshotServiceMock );

    EasyMock.expect( claimDAOMock.getProductClaimPromotionTeamMaxCount( claim.getPromotion().getId() ) ).andReturn( 10 );

    EasyMock.replay( claimDAOMock );

    EasyMock.expect( randomNumberStrategyMock.getRandomizedClaimNumber() ).andReturn( null );
    EasyMock.replay( randomNumberStrategyMock );

    classUnderTest.saveClaim( claim, null, null, false, true );

    assertEquals( expectClaimClosed, !claim.isOpen() );

    assertTrue( claim.getSubmitter().equals( userToReturn ) );

    EasyMock.reset( claimDAOMock );
    EasyMock.reset( claimProcessingStrategyFactoryMock );
    EasyMock.reset( randomNumberStrategyMock );
    EasyMock.reset( claimProcessingStrategyMock );
    EasyMock.reset( claimApproverSnapshotServiceMock );
    EasyMock.reset( gamificationServiceMock );
  }

  private ProductClaim buildTestClaim( Participant userToReturn, String approvalTypeCode )
  {
    Promotion promotion = new ProductClaimPromotion();
    promotion.setId( new Long( 1 ) );
    promotion.setApprovalType( ApprovalType.lookup( approvalTypeCode ) );
    promotion.setPromotionType( PromotionType.lookup( PromotionType.PRODUCT_CLAIM ) );
    promotion.setSubmissionStartDate( new Date( new Date().getTime() - ( DateUtils.MILLIS_PER_DAY * 2 ) ) );
    promotion.setSubmissionEndDate( new Date( new Date().getTime() + ( DateUtils.MILLIS_PER_DAY * 2 ) ) );
    ClaimForm claimForm = new ClaimForm();
    claimForm.setCmAssetCode( "123456" );
    promotion.setClaimForm( claimForm );
    ( (ProductClaimPromotion)promotion ).setTeamMaxCount( 10 );
    ProductClaim claim = new ProductClaim();
    claim.setSubmissionDate( new Date() );
    claim.setPromotion( promotion );
    claim.setSubmitter( userToReturn );

    Product product1 = ProductDAOImplTest.buildStaticProductDomainObject( "product12", ProductDAOImplTest.getProductCategoryDomainObject( "cat12" ) );

    ClaimProduct claimProduct1 = new ClaimProduct( product1, 1 );
    claimProduct1.setApprovalStatusType( ApprovalStatusType.lookup( ApprovalStatusType.PENDING ) );
    claimProduct1.setQuantity( 5 );

    claim.addClaimProduct( claimProduct1 );

    return claim;
  }

  /**
   * Tests deleting multiple claims.
   */
  public void testDeleteClaims()
  {
    // TODO: Implement this method.
  }

  /**
   * Test updating a claim through the service.
   * 
   * @throws ServiceErrorException
   */
  public void testUpdateClaim() throws ServiceErrorException
  {
    Participant userToReturn = new Participant();
    userToReturn.setId( UserManager.getUserId() );

    Promotion promotion = new ProductClaimPromotion();
    promotion.setId( new Long( 1 ) );
    promotion.setPromotionType( PromotionType.lookup( PromotionType.PRODUCT_CLAIM ) );
    ClaimForm claimForm = new ClaimForm();
    claimForm.setCmAssetCode( "123456" );
    promotion.setClaimForm( claimForm );
    promotion.setSubmissionStartDate( new Date( new Date().getTime() - ( DateUtils.MILLIS_PER_DAY * 2 ) ) );
    promotion.setSubmissionEndDate( new Date( new Date().getTime() + ( DateUtils.MILLIS_PER_DAY * 2 ) ) );
    promotion.setApprovalType( ApprovalType.lookup( ApprovalType.MANUAL ) );

    ProductClaim claim = new ProductClaim();
    claim.setOpen( true );
    claim.setSubmissionDate( new Date() );
    claim.setPromotion( promotion );
    claim.setSubmitter( userToReturn );
    ClaimProduct claimProduct = new ClaimProduct();

    claim.addClaimProduct( claimProduct );

    // Service checks insert/update based on id
    claim.setId( new Long( 1 ) );

    // all claim products approved, so claim should close and run promo engine
    claimProduct.setApprovalStatusType( ApprovalStatusType.lookup( ApprovalStatusType.APPROVED ) );
    boolean expectClaimClosed = false;
    runUpdateClaim( userToReturn, claim, expectClaimClosed );

    // not all claim products approved or denied, so claim should open - not yet supported
    claimProduct.setApprovalStatusType( ApprovalStatusType.lookup( ApprovalStatusType.PENDING ) );
    expectClaimClosed = true;
    try
    {
      runUpdateClaim( userToReturn, claim, expectClaimClosed );
      fail( "Should have failed since update on closed claims not yet supported" );
    }
    catch( BeaconRuntimeException e )
    {
      // expected
    }
  }

  private void runUpdateClaim( Participant userToReturn, ProductClaim claim, boolean expectClaimClosed ) throws ServiceErrorException
  {

    EasyMock.expect( claimDAOMock.saveClaim( claim ) ).andReturn( claim );

    EasyMock.expect( claimProcessingStrategyFactoryMock.getClaimProcessingStrategy( PromotionType.lookup( PromotionType.PRODUCT_CLAIM ) ) ).andReturn( claimProcessingStrategyMock );

    EasyMock.replay( claimProcessingStrategyFactoryMock );
    EasyMock.replay( claimDAOMock );
    classUnderTest.saveClaim( claim, null, null, false, true );
    EasyMock.verify( claimDAOMock );
    assertTrue( claim.getSubmitter().equals( userToReturn ) );
    EasyMock.reset( claimDAOMock );
    EasyMock.reset( claimProcessingStrategyFactoryMock );

  }

  /**
   * Test getting a Claim by claim id.
   */
  public void testGetClaimById()
  {
    Long id = Long.valueOf( "0" );
    ProductClaim claim = new ProductClaim();
    EasyMock.expect( claimDAOMock.getClaimById( id ) ).andReturn( claim );
    EasyMock.replay( claimDAOMock );
    classUnderTest.getClaimById( id );
    EasyMock.verify( claimDAOMock );
  }

  public void testGetEarningsForClaim()
  {
    Long id = Long.valueOf( "0" );
    Long userId = new Long( 1 );
    Long expectedResult = new Long( 10 );
    EasyMock.expect( claimDAOMock.getEarningsForClaim( id, userId ) ).andReturn( expectedResult );
    EasyMock.replay( claimDAOMock );
    Long result = classUnderTest.getEarningsForClaim( id, userId );
    assertEquals( expectedResult, result );
    EasyMock.verify( claimDAOMock );
  }

  /**
   * Test getting a Claim by claim id.
   */
  public void testGetClaimBySubmitter()
  {
    Participant pax = new Participant();
    ProductClaim claim = new ProductClaim();
    List<Object> claims = new LinkedList<>();
    claims.add( claim );
    ClaimQueryConstraint claimQueryConstraint = new ClaimQueryConstraint();
    claimQueryConstraint.setSubmitterId( pax.getId() );

    EasyMock.expect( claimDAOMock.getClaimList( claimQueryConstraint ) ).andReturn( claims );
    EasyMock.replay( claimDAOMock );
    List result = classUnderTest.getClaimList( claimQueryConstraint );
    EasyMock.verify( claimDAOMock );
    assertSame( claims, result );
  }

  /**
   * Test getting a Claim by claim id.
   */
  public void testGetGiversForParticipant()
  {
    Long paxId = new Long( 60020 );
    int count = 1;
    List<Participant> expectedResult = new ArrayList<Participant>();
    EasyMock.expect( claimDAOMock.getGiversForParticipant( paxId, count ) ).andReturn( expectedResult );
    EasyMock.replay( claimDAOMock );
    List result = classUnderTest.getGiversForParticipant( paxId, count );
    assertEquals( expectedResult, result );
    EasyMock.verify( claimDAOMock );
  }

  /**
   * Test getting an eligible users count by participant Id for Celebration Module.
   */
  public void testGetEligibleUsersCountForCelebrationModule()
  {
    Long claimId = new Long( 5001 );
    Long paxId = new Long( 60020 );
    int expectedCount = 1;
    EasyMock.expect( claimDAOMock.getEligibleUsersCountForCelebrationModule( claimId, paxId ) ).andReturn( expectedCount );
    EasyMock.replay( claimDAOMock );
    int resultCount = classUnderTest.getEligibleUsersCountForCelebrationModule( claimId, paxId );
    assertEquals( expectedCount, resultCount );
    EasyMock.verify( claimDAOMock );
  }

  /**
   * Test by claim id.
   */
  public void testGetActivityTimePeriod()
  {
    Long claimId = new Long( 5001 );
    List<ClaimInfoBean> expectedResult = new ArrayList<ClaimInfoBean>();
    EasyMock.expect( claimDAOMock.getActivityTimePeriod( claimId ) ).andReturn( expectedResult );
    EasyMock.replay( claimDAOMock );
    List result = classUnderTest.getActivityTimePeriod( claimId );
    assertEquals( expectedResult, result );
    EasyMock.verify( claimDAOMock );
  }
}

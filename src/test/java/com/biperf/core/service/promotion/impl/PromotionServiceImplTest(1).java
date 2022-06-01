/**
 * 
 */

package com.biperf.core.service.promotion.impl;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;

import javax.security.auth.login.LoginException;

import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.easymock.EasyMock;
import org.jmock.Mock;
import org.joda.time.DateTime;

import com.biperf.core.dao.budget.BudgetMasterDAO;
import com.biperf.core.dao.budget.hibernate.BudgetMasterDAOImplTest;
import com.biperf.core.dao.claim.ClaimFormDAO;
import com.biperf.core.dao.hierarchy.NodeDAO;
import com.biperf.core.dao.hierarchy.hibernate.NodeDAOImplTest;
import com.biperf.core.dao.participant.ParticipantDAO;
import com.biperf.core.dao.participant.UserDAO;
import com.biperf.core.dao.participant.hibernate.AudienceDAOImplTest;
import com.biperf.core.dao.participant.hibernate.ParticipantDAOImplTest;
import com.biperf.core.dao.promotion.PromotionDAO;
import com.biperf.core.dao.promotion.hibernate.PromotionDAOImplTest;
import com.biperf.core.domain.budget.Budget;
import com.biperf.core.domain.budget.BudgetMaster;
import com.biperf.core.domain.budget.BudgetSegment;
import com.biperf.core.domain.claim.ClaimForm;
import com.biperf.core.domain.claim.ClaimFormStep;
import com.biperf.core.domain.claim.ClaimFormStepElement;
import com.biperf.core.domain.claim.RecognitionClaim;
import com.biperf.core.domain.enums.ActivityType;
import com.biperf.core.domain.enums.ApprovalConditionalAmmountOperatorType;
import com.biperf.core.domain.enums.ApprovalType;
import com.biperf.core.domain.enums.BudgetFinalPayoutRule;
import com.biperf.core.domain.enums.BudgetMasterAwardType;
import com.biperf.core.domain.enums.BudgetOverrideableType;
import com.biperf.core.domain.enums.BudgetType;
import com.biperf.core.domain.enums.CustomApproverRoutingType;
import com.biperf.core.domain.enums.CustomApproverType;
import com.biperf.core.domain.enums.HierarchyRoleType;
import com.biperf.core.domain.enums.LanguageType;
import com.biperf.core.domain.enums.PrimaryAudienceType;
import com.biperf.core.domain.enums.PromotionStatusType;
import com.biperf.core.domain.enums.PromotionType;
import com.biperf.core.domain.enums.UserType;
import com.biperf.core.domain.hierarchy.Hierarchy;
import com.biperf.core.domain.hierarchy.Node;
import com.biperf.core.domain.hierarchy.NodeType;
import com.biperf.core.domain.multimedia.ECard;
import com.biperf.core.domain.participant.Audience;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.participant.PaxAudience;
import com.biperf.core.domain.promotion.Approver;
import com.biperf.core.domain.promotion.ApproverCriteria;
import com.biperf.core.domain.promotion.ApproverOption;
import com.biperf.core.domain.promotion.InstantPoll;
import com.biperf.core.domain.promotion.NominationPromotion;
import com.biperf.core.domain.promotion.ProductClaimPromotion;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.PromotionCert;
import com.biperf.core.domain.promotion.PromotionClaimFormStepElementValidation;
import com.biperf.core.domain.promotion.PromotionECard;
import com.biperf.core.domain.promotion.PromotionPrimaryAudience;
import com.biperf.core.domain.promotion.PromotionSecondaryAudience;
import com.biperf.core.domain.promotion.QuizPromotion;
import com.biperf.core.domain.promotion.RecognitionPromotion;
import com.biperf.core.domain.system.PropertySetItem;
import com.biperf.core.domain.user.User;
import com.biperf.core.domain.user.UserNode;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.exception.UniqueConstraintViolationException;
import com.biperf.core.security.AuthenticatedUser;
import com.biperf.core.service.AssociationRequest;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.BaseServiceTest;
import com.biperf.core.service.budget.BudgetMasterService;
import com.biperf.core.service.claim.ClaimFormDefinitionService;
import com.biperf.core.service.claim.ClaimService;
import com.biperf.core.service.cms.CMAssetService;
import com.biperf.core.service.instantpoll.InstantPollService;
import com.biperf.core.service.maincontent.MainContentService;
import com.biperf.core.service.participant.AudienceService;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.service.promotion.PromotionApprovalUpdateAssociation;
import com.biperf.core.service.proxy.ProxyService;
import com.biperf.core.service.security.AuthenticationService;
import com.biperf.core.service.security.AuthorizationService;
import com.biperf.core.service.security.impl.AuthenticationServiceImpl;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.strategy.AccountExpirationStrategy;
import com.biperf.core.strategy.PasswordPolicyStrategy;
import com.biperf.core.strategy.UserLockoutStrategy;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.AlertsValueBean;
import com.biperf.core.value.NameableBean;
import com.biperf.core.value.PromotionMenuBean;
import com.biperf.core.value.PromotionPaxValue;
import com.biperf.core.value.celebration.CelebrationImageFillerValue;
import com.biperf.core.value.nomination.NominationApproverValueBean;
import com.google.common.collect.Lists;

/**
 * PromotionServiceImplTest
 * 
 *
 */
public class PromotionServiceImplTest extends BaseServiceTest
{
  private static final Log logger = LogFactory.getLog( PromotionServiceImplTest.class );

  private PromotionServiceImpl promotionServiceImpl;
  private AuthenticationService authenticationService = new AuthenticationServiceImpl();
  // TODO: David Richardson: We use two different types of mock in this file :(
  private Mock mockUserDAO = null;
  private Mock mockUserLockoutStrategy = null;
  private Mock mockAccountExpirationStrategy = null;
  private Mock mockPasswordPolicyStrategy = null;
  private Mock mockPromotionDAO = null;
  private Mock mockParticipantDAO = null;
  private Mock mockCmAssetService = null;
  private Mock mockClaimFormDefinitionService = null;
  private Mock mockParticipantService = null;
  private Mock mockAudienceService = null;
  private Mock mockUserService = null;
  private Mock mockProxyService = null;
  private Mock mockBudgetMasterDAO = null;
  private Mock mockClaimFormDAO = null;
  private Mock mockNodeDAO = null;
  private Mock mockBudgetMasterService = null;
  private Mock mockInstantPollService = null;
  private Mock mockClaimService = null;
  private Mock mockSystemVariableService = null;
  private Mock mockAuthorizationService = null;

  private AuthenticatedUser user, prevUser;
  private static int uniqueStringCounter = 1000;

  Mock mockMainContentService;

  private AssociationRequest mockAssociationRequest = new AssociationRequest()
  {
    public void execute( Object domainObject )
    {
    }
  };

  /**
   * Constructor to take the name of the test.
   * 
   * @param test
   */
  public PromotionServiceImplTest( String test )
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

    mockUserDAO = new Mock( UserDAO.class );
    mockUserLockoutStrategy = new Mock( UserLockoutStrategy.class );
    mockAccountExpirationStrategy = new Mock( AccountExpirationStrategy.class );
    mockPasswordPolicyStrategy = new Mock( PasswordPolicyStrategy.class );
    mockPromotionDAO = new Mock( PromotionDAO.class );
    mockParticipantDAO = new Mock( ParticipantDAO.class );
    mockCmAssetService = new Mock( CMAssetService.class );
    mockClaimFormDefinitionService = new Mock( ClaimFormDefinitionService.class );
    mockParticipantService = new Mock( ParticipantService.class );
    mockAudienceService = new Mock( AudienceService.class );
    mockUserService = new Mock( UserService.class );
    mockBudgetMasterDAO = new Mock( BudgetMasterDAO.class );
    mockClaimFormDAO = new Mock( ClaimFormDAO.class );
    mockNodeDAO = new Mock( NodeDAO.class );
    mockProxyService = new Mock( ProxyService.class );
    mockBudgetMasterService = new Mock( BudgetMasterService.class );

    mockMainContentService = new Mock( MainContentService.class );
    mockInstantPollService = new Mock( InstantPollService.class );
    mockClaimService = new Mock( ClaimService.class );
    mockSystemVariableService = new Mock( SystemVariableService.class );
    mockAuthorizationService = new Mock( AuthorizationService.class );

    authenticationService.setUserDAO( (UserDAO)mockUserDAO.proxy() );
    authenticationService.setUserLockoutStrategy( (UserLockoutStrategy)mockUserLockoutStrategy.proxy() );
    authenticationService.setAccountExpirationStrategy( (AccountExpirationStrategy)mockAccountExpirationStrategy.proxy() );
    authenticationService.setPasswordPolicyStrategy( (PasswordPolicyStrategy)mockPasswordPolicyStrategy.proxy() );
    authenticationService.setProxyService( (ProxyService)mockProxyService.proxy() );

    promotionServiceImpl = new PromotionServiceImpl()
    {
      public MainContentService getMainContentService()
      {
        return (MainContentService)mockMainContentService.proxy();
      }
    };
    promotionServiceImpl.setPromotionDAO( (PromotionDAO)mockPromotionDAO.proxy() );
    promotionServiceImpl.setParticipantDAO( (ParticipantDAO)mockParticipantDAO.proxy() );
    promotionServiceImpl.setCmAssetService( (CMAssetService)mockCmAssetService.proxy() );
    promotionServiceImpl.setClaimFormDefinitionService( (ClaimFormDefinitionService)mockClaimFormDefinitionService.proxy() );
    promotionServiceImpl.setParticipantService( (ParticipantService)mockParticipantService.proxy() );
    promotionServiceImpl.setAudienceService( (AudienceService)mockAudienceService.proxy() );
    promotionServiceImpl.setUserService( (UserService)mockUserService.proxy() );
    promotionServiceImpl.setBudgetMasterDAO( (BudgetMasterDAO)mockBudgetMasterDAO.proxy() );
    promotionServiceImpl.setClaimFormDAO( (ClaimFormDAO)mockClaimFormDAO.proxy() );
    promotionServiceImpl.setNodeDAO( (NodeDAO)mockNodeDAO.proxy() );
    promotionServiceImpl.setBudgetMasterService( (BudgetMasterService)mockBudgetMasterService.proxy() );
    promotionServiceImpl.setInstantPollService( (InstantPollService)mockInstantPollService.proxy() );
    promotionServiceImpl.setClaimService( (ClaimService)mockClaimService.proxy() );
    // promotionServiceImpl.setMainContentService(
    // (MainContentService)mockMainContentService.proxy() );
    promotionServiceImpl.setSystemVariableService( (SystemVariableService)mockSystemVariableService.proxy() );

    user = new AuthenticatedUser();
    user.setLocale( Locale.US );
    user.setPrimaryCountryCode( "us" );
    prevUser = UserManager.getUser();
    UserManager.setUser( user );
  }

  @Override
  protected void tearDown() throws Exception
  {
    super.tearDown();
    UserManager.setUser( prevUser );
  }

  public void testSavePromotion() throws UniqueConstraintViolationException
  {
    NominationPromotion attachedPromotion = buildNominationPromotion( "100" );

    ClaimForm claimForm = new ClaimForm();
    ClaimFormStep claimFormStep = new ClaimFormStep();
    claimForm.setClaimFormSteps( Lists.newArrayList( claimFormStep ) );
    attachedPromotion.setClaimForm( claimForm );

    PromotionApprovalUpdateAssociation promotionApprovalUpdateAssociation = new PromotionApprovalUpdateAssociation( attachedPromotion );

    mockPromotionDAO.expects( once() ).method( "getPromotionById" ).with( same( attachedPromotion.getId() ) ).will( returnValue( attachedPromotion ) );
    mockPromotionDAO.expects( once() ).method( "isPromotionNameUnique" ).will( returnValue( true ) );
    mockClaimFormDefinitionService.expects( once() ).method( "updateClaimFormStatus" );

    NominationPromotion detachedPromotion = buildNominationPromotion( "100" );
    detachedPromotion.setName( "test" );
    detachedPromotion.setId( 123L );
    detachedPromotion.setApprovalType( ApprovalType.lookup( ApprovalType.AUTOMATIC_DELAYED ) );
    detachedPromotion.setApprovalNodeType( new NodeType() );
    detachedPromotion.setApprovalHierarchy( new Hierarchy() );
    detachedPromotion.setApprovalAutoDelayDays( 10 );
    detachedPromotion.setApprovalConditionalClaimCount( 10 );
    detachedPromotion.setApprovalConditionalAmountOperator( ApprovalConditionalAmmountOperatorType.lookup( ApprovalConditionalAmmountOperatorType.EQ ) );
    detachedPromotion.setApprovalConditionalAmount( 10.0 );
    detachedPromotion.setApprovalStartDate( new Date() );
    detachedPromotion.setApprovalEndDate( new Date() );
    detachedPromotion.setApproverNode( new Node() );
    detachedPromotion.setApprovalConditionalAmountField( new ClaimFormStepElement() );

    detachedPromotion.setPromotionType( PromotionType.lookup( PromotionType.NOMINATION ) );
    detachedPromotion.setClaimForm( claimForm );

    attachedPromotion.setPromotionStatus( PromotionStatusType.lookup( PromotionStatusType.LIVE ) );

    List<ApproverOption> attachedCustomApproverOptions = Lists.newArrayList();

    ApproverOption attachedApproverOption = new ApproverOption();
    attachedApproverOption.setNominationPromotion( attachedPromotion );
    attachedApproverOption.setApprovalLevel( 1L );
    attachedApproverOption.setSequenceNum( 1 );
    attachedApproverOption.setCharacteristicId( 1L );
    attachedApproverOption.setApproverType( CustomApproverType.lookup( CustomApproverType.BEHAVIOR ) );
    attachedApproverOption.setApproverRoutingType( CustomApproverRoutingType.lookup( CustomApproverRoutingType.BYNOMINEE ) );

    ApproverOption attachedApproverOption2 = new ApproverOption();
    attachedApproverOption2.setNominationPromotion( attachedPromotion );
    attachedApproverOption2.setApprovalLevel( 2L );
    attachedApproverOption2.setSequenceNum( 2 );
    attachedApproverOption2.setCharacteristicId( 2L );
    attachedApproverOption2.setApproverType( CustomApproverType.lookup( CustomApproverType.BEHAVIOR ) );
    attachedApproverOption2.setApproverRoutingType( CustomApproverRoutingType.lookup( CustomApproverRoutingType.BYNOMINEE ) );

    attachedCustomApproverOptions.add( attachedApproverOption );
    attachedCustomApproverOptions.add( attachedApproverOption2 );

    attachedPromotion.setCustomApproverOptions( attachedCustomApproverOptions );

    List<ApproverOption> detachedCustomApproverOptions = Lists.newArrayList();

    ApproverOption detachedApproverOption3 = new ApproverOption();
    detachedApproverOption3.setNominationPromotion( attachedPromotion );
    detachedApproverOption3.setApprovalLevel( 3L );
    detachedApproverOption3.setSequenceNum( 3 );
    detachedApproverOption3.setCharacteristicId( 3L );
    detachedApproverOption3.setApproverType( CustomApproverType.lookup( CustomApproverType.AWARD ) );
    detachedApproverOption3.setApproverRoutingType( CustomApproverRoutingType.lookup( CustomApproverRoutingType.BYNOMINEE ) );

    detachedCustomApproverOptions.add( detachedApproverOption3 );

    detachedPromotion.setCustomApproverOptions( detachedCustomApproverOptions );

    promotionApprovalUpdateAssociation.setDetachedDomain( detachedPromotion );

    mockPromotionDAO.expects( once() ).method( "save" ).with( same( attachedPromotion ) ).will( returnValue( attachedPromotion ) );

    attachedPromotion = (NominationPromotion)promotionServiceImpl.savePromotion( attachedPromotion.getId(), promotionApprovalUpdateAssociation );

    assertEquals( "Number of approvals do not match after changing the custom approvals for a promotion ", attachedPromotion.getCustomApproverOptions().size(), 1 );
  }

  /**
   * Test getting a validation for a claimFormStepElement by id
   */
  public void testSavePromotionClaimFormStepElementValidation()
  {

    PromotionClaimFormStepElementValidation pcfsev = new PromotionClaimFormStepElementValidation();

    pcfsev.setId( new Long( 3423523 ) );
    pcfsev.setPromotion( new ProductClaimPromotion() );
    pcfsev.setClaimFormStepElement( new ClaimFormStepElement() );

    // PromotionDAO expected to call save once with the Promotion which will return the Promotion
    // expected
    mockPromotionDAO.expects( once() ).method( "savePromotionClaimFormStepElementValidation" ).with( same( pcfsev ) ).will( returnValue( pcfsev ) );

    // make the service call
    PromotionClaimFormStepElementValidation actualPCFSEV = promotionServiceImpl.savePromotionClaimFormStepElementValidation( pcfsev );

    assertEquals( "Actual ClaimFormStepElement validation didn't match what was expected.", pcfsev, actualPCFSEV );

    mockPromotionDAO.verify();

  }

  /**
   * Test getting the claimFormStepElement validation for the id param.
   */
  public void testGetPromotionClaimFormStepElementValidationById()
  {

    PromotionClaimFormStepElementValidation pcfsev = new PromotionClaimFormStepElementValidation();

    pcfsev.setId( new Long( 3423523 ) );
    pcfsev.setPromotion( new ProductClaimPromotion() );
    pcfsev.setClaimFormStepElement( new ClaimFormStepElement() );

    // PromotionDAO expected to call save once with the Promotion which will return the Promotion
    // expected
    mockPromotionDAO.expects( once() ).method( "getPromotionClaimFormStepElementValidationById" ).with( same( pcfsev.getId() ) ).will( returnValue( pcfsev ) );

    // make the service call
    PromotionClaimFormStepElementValidation actualPCFSEV = promotionServiceImpl.getPromotionClaimFormStepElementValidationById( pcfsev.getId() );

    assertEquals( "Actual ClaimFormStepElement validation didn't match what was expected.", pcfsev, actualPCFSEV );

    mockPromotionDAO.verify();
  }

  /**
   * Test getting a list of claimFormStepElement validations.
   */
  public void testGetAllPromotionClaimFormStepElementValidations()
  {

    Promotion promotion = new ProductClaimPromotion();
    ClaimFormStep claimFormStep = new ClaimFormStep();
    ClaimFormStepElement claimFormStepElement = new ClaimFormStepElement();
    claimFormStepElement.setClaimFormStep( claimFormStep );

    List expectedList = new ArrayList();

    PromotionClaimFormStepElementValidation pcfsev1 = new PromotionClaimFormStepElementValidation();
    pcfsev1.setId( new Long( 3423523 ) );
    pcfsev1.setPromotion( promotion );
    pcfsev1.setClaimFormStepElement( new ClaimFormStepElement() );
    expectedList.add( pcfsev1 );

    PromotionClaimFormStepElementValidation pcfsev2 = new PromotionClaimFormStepElementValidation();
    pcfsev2.setId( new Long( 3423521 ) );
    pcfsev2.setPromotion( promotion );
    pcfsev2.setClaimFormStepElement( new ClaimFormStepElement() );
    expectedList.add( pcfsev2 );

    PromotionClaimFormStepElementValidation pcfsev3 = new PromotionClaimFormStepElementValidation();
    pcfsev3.setId( new Long( 3423521 ) );
    pcfsev3.setPromotion( promotion );
    pcfsev3.setClaimFormStepElement( new ClaimFormStepElement() );
    expectedList.add( pcfsev3 );

    // PromotionDAO expected to call save once with the Promotion which will return the Promotion
    // expected
    mockPromotionDAO.expects( once() ).method( "getAllPromotionClaimFormStepElementValidations" ).will( returnValue( expectedList ) );

    // make the service call
    List actualList = promotionServiceImpl.getAllPromotionClaimFormStepElementValidations( promotion, claimFormStep );

    assertEquals( "Actual ClaimFormStepElement Validation List didn't match what was expected.", expectedList, actualList );

    mockPromotionDAO.verify();
  }

  /**
   * Test to save a promotion.
   * 
   * @throws UniqueConstraintViolationException
   */
  public void testSave() throws UniqueConstraintViolationException
  {
    // create a promotion object
    Promotion expectedPromotion = buildProductClaimPromotion( "100" );
    expectedPromotion.setId( new Long( 1 ) );

    mockClaimFormDefinitionService.expects( once() ).method( "updateClaimFormStatus" );

    mockPromotionDAO.expects( once() ).method( "isPromotionNameUnique" ).will( returnValue( true ) );

    // PromotionDAO expected to call save once with the Promotion
    // which will return the Promotion expected
    mockPromotionDAO.expects( once() ).method( "save" ).with( same( expectedPromotion ) ).will( returnValue( expectedPromotion ) );

    // make the service call
    Promotion actualPromotion = promotionServiceImpl.savePromotion( expectedPromotion );

    assertEquals( "Actual promotion didn't match with what is expected", expectedPromotion, actualPromotion );

    mockPromotionDAO.verify();
  }

  /**
   * Tests getting the node from the service through the DAO.
   */
  public void testGetById()
  {
    Promotion expectedPromotion = buildProductClaimPromotion( "100" );
    expectedPromotion.setId( new Long( 1 ) );

    mockPromotionDAO.expects( once() ).method( "getPromotionById" ).with( same( expectedPromotion.getId() ) ).will( returnValue( expectedPromotion ) );

    Promotion actualPromotion = promotionServiceImpl.getPromotionById( expectedPromotion.getId() );

    assertEquals( "Actual Promotion does not match to what was expected", expectedPromotion, actualPromotion );

    mockPromotionDAO.verify();
  }

  /**
   * Tests getting the promotion with associations from the service through the DAO.
   */
  public void testGetByIdWithAssociations()
  {
    Promotion expectedPromotion = buildProductClaimPromotion( "100" );
    expectedPromotion.setId( new Long( 1 ) );

    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
    associationRequestCollection.add( mockAssociationRequest );

    mockPromotionDAO.expects( once() ).method( "getPromotionByIdWithAssociations" ).with( same( expectedPromotion.getId() ), eq( associationRequestCollection ) )
        .will( returnValue( expectedPromotion ) );

    Promotion actualPromotion = promotionServiceImpl.getPromotionByIdWithAssociations( expectedPromotion.getId(), associationRequestCollection );

    assertEquals( "Actual Promotion does not match to what was expected", expectedPromotion, actualPromotion );

    mockPromotionDAO.verify();
  }

  /**
   * Tests deleting the hierachy from the database through the service.
   * 
   * @throws Exception
   */
  public void testDelete() throws Exception
  {
    Promotion expectedPromotion = buildProductClaimPromotion( "100" );
    expectedPromotion.setId( new Long( 1 ) );

    // Can't be live or delete will fail.
    expectedPromotion.setPromotionStatus( PromotionStatusType.lookup( PromotionStatusType.COMPLETE ) );

    ClaimForm expectedClaimForm = new ClaimForm();
    expectedClaimForm.setId( new Long( 1 ) );
    Set expectedPromoSet = new LinkedHashSet();
    expectedPromoSet.add( expectedPromotion );
    expectedClaimForm.setPromotions( expectedPromoSet );

    mockClaimFormDefinitionService.expects( once() ).method( "updateClaimFormStatus" );

    mockPromotionDAO.expects( once() ).method( "getPromotionById" ).with( same( expectedPromotion.getId() ) ).will( returnValue( expectedPromotion ) );

    mockPromotionDAO.expects( once() ).method( "getPromotionClaimFormStepElementValidationsByPromotion" ).with( same( expectedPromotion ) ).will( returnValue( new ArrayList() ) );

    mockPromotionDAO.expects( once() ).method( "delete" ).with( same( expectedPromotion ) );

    // mockClaimFormDAO.expects( once() ).method( "getClaimFormById" ).with( same( expectedPromotion
    // .getClaimForm().getId() ) ).will( returnValue(expectedClaimForm ) );

    mockClaimFormDAO.expects( once() ).method( "getClaimFormById" ).withAnyArguments().will( returnValue( expectedClaimForm ) );

    promotionServiceImpl.deletePromotion( expectedPromotion.getId() );

    mockPromotionDAO.verify();
    mockClaimFormDAO.verify();
  }

  /**
   * Tests deleting the hierachy from the database through the service.
   */
  public void testDeleteLivePromotion()
  {

    // test deleting a live promotion (which should fail)
    Promotion livePromotion = buildProductClaimPromotion( "999" );
    livePromotion.setId( new Long( 2 ) );
    livePromotion.setPromotionStatus( PromotionStatusType.lookup( PromotionStatusType.LIVE ) );

    mockPromotionDAO.expects( once() ).method( "getPromotionById" ).with( same( livePromotion.getId() ) ).will( returnValue( livePromotion ) );

    try
    {
      // This should throw an exception
      promotionServiceImpl.deletePromotion( livePromotion.getId() );

      // this line should be skipped
      assertTrue( "Deleting a live promotion should produce a ServiceErrorException.", false );
    }
    catch( ServiceErrorException e )
    {
      // This line should be executed
      assertNotNull( "Deleting a live promotion should produce a ServiceErrorException.", e );
    }

    mockPromotionDAO.verify();
  }

  /**
   * Tests deleting an non-expired promotion with child(ren) promotion from the database through the
   * service.
   * 
   * @throws Exception
   */
  public void testDeleteNonExpiredPromotionWithChildren() throws Exception
  {
    ProductClaimPromotion parentPromotion = buildProductClaimPromotion( "100" );
    parentPromotion.setId( new Long( 1 ) );
    parentPromotion.setPromotionStatus( PromotionStatusType.lookup( PromotionStatusType.COMPLETE ) );

    Promotion childPromotionA = buildProductClaimPromotion( "101" );
    childPromotionA.setId( new Long( 2 ) );
    childPromotionA.setPromotionStatus( PromotionStatusType.lookup( PromotionStatusType.COMPLETE ) );

    Promotion childPromotionB = buildProductClaimPromotion( "102" );
    childPromotionB.setId( new Long( 3 ) );
    childPromotionA.setPromotionStatus( PromotionStatusType.lookup( PromotionStatusType.UNDER_CONSTRUCTION ) );

    Promotion childPromotionC = buildProductClaimPromotion( "103" );
    childPromotionC.setId( new Long( 4 ) );
    childPromotionA.setPromotionStatus( PromotionStatusType.lookup( PromotionStatusType.UNDER_CONSTRUCTION ) );

    Set childrenSet = new LinkedHashSet();
    childrenSet.add( childPromotionA );
    childrenSet.add( childPromotionB );
    childrenSet.add( childPromotionC );

    parentPromotion.setChildPromotions( childrenSet );
    parentPromotion.setChildrenCount( 3 );

    List childrenList = new ArrayList();
    childrenList.add( childPromotionA );
    childrenList.add( childPromotionB );
    childrenList.add( childPromotionC );

    mockClaimFormDefinitionService.expects( once() ).method( "updateClaimFormStatus" );

    mockPromotionDAO.expects( once() ).method( "getPromotionById" ).with( same( parentPromotion.getId() ) ).will( returnValue( parentPromotion ) );

    mockPromotionDAO.expects( once() ).method( "getNonExpiredChildPromotions" ).with( same( parentPromotion.getId() ) ).will( returnValue( childrenList ) );

    mockClaimFormDAO.expects( once() ).method( "getClaimFormById" ).with( same( parentPromotion.getClaimForm().getId() ) ).will( returnValue( new ClaimForm() ) );

    mockPromotionDAO.expects( once() ).method( "getPromotionClaimFormStepElementValidationsByPromotion" ).with( same( parentPromotion ) ).will( returnValue( new ArrayList() ) );

    mockPromotionDAO.expects( once() ).method( "delete" ).with( same( parentPromotion ) );

    mockPromotionDAO.expects( once() ).method( "delete" ).with( same( childPromotionA ) );
    mockPromotionDAO.expects( once() ).method( "delete" ).with( same( childPromotionB ) );
    mockPromotionDAO.expects( once() ).method( "delete" ).with( same( childPromotionC ) );

    promotionServiceImpl.deletePromotion( parentPromotion.getId() );

    mockPromotionDAO.verify();
    mockClaimFormDAO.verify();
  }

  /**
   * Tests deleting an expired promotion with child(ren) promotion from the database through the
   * service.
   * 
   * @throws Exception
   */

  /*
   * public void testDeleteExpiredPromotionWithChildren() throws Exception { Promotion
   * parentPromotion = buildProductClaimPromotion( "100" ); parentPromotion.setId( new Long( 1 ) );
   * parentPromotion.setPromotionStatus( PromotionStatusType.lookup( PromotionStatusType.EXPIRED )
   * ); Promotion childPromotionA = buildProductClaimPromotion( "101" ); childPromotionA.setId( new
   * Long( 2 )); childPromotionA.setPromotionStatus( PromotionStatusType.lookup(
   * PromotionStatusType.EXPIRED ) ); Promotion childPromotionB = buildProductClaimPromotion( "102"
   * ); childPromotionB.setId( new Long( 3 )); childPromotionA.setPromotionStatus(
   * PromotionStatusType.lookup( PromotionStatusType.EXPIRED ) ); Promotion childPromotionC =
   * buildProductClaimPromotion( "103" ); childPromotionC.setId( new Long( 4 ));
   * childPromotionA.setPromotionStatus( PromotionStatusType.lookup( PromotionStatusType.EXPIRED )
   * ); Set childrenSet = new LinkedHashSet(); childrenSet.add(childPromotionA);
   * childrenSet.add(childPromotionB); childrenSet.add(childPromotionC);
   * parentPromotion.setChildPromotions(childrenSet); parentPromotion.setChildrenCount(3); List
   * childrenList = new ArrayList(); childrenList.add(childPromotionA);
   * childrenList.add(childPromotionB); childrenList.add(childPromotionC); mockPromotionDAO.expects(
   * once() ).method( "getPromotionById" ).with( same( parentPromotion .getId() ) ).will(
   * returnValue( parentPromotion ) ); mockPromotionDAO.expects( once() ).method(
   * "isPromotionNameUnique" ).with( same( parentPromotion .getName() ), same( parentPromotion
   * .getId() ) ).will( returnValue( true ) ); mockPromotionDAO.expects( once() ).method(
   * "getChildPromotions" ).with( same( parentPromotion .getId() ) ).will( returnValue( childrenList
   * ) ); mockPromotionDAO.expects( once() ).method( "save" ).with( same( parentPromotion) ).will(
   * returnValue( parentPromotion ) ); mockClaimFormDefinitionService.expects( once() ).method(
   * "updateClaimFormStatus" ); promotionServiceImpl.deletePromotion( parentPromotion.getId() );
   * mockPromotionDAO.verify(); }
   */

  /**
   * Test getting all nodes from the database through the service.
   */
  public void testGetAll()
  {
    List expectedList = getPromotionList();

    mockPromotionDAO.expects( once() ).method( "getAll" ).will( returnValue( expectedList ) );

    List actualList = promotionServiceImpl.getAll();

    assertTrue( "Actual set didn't contain expected set for getAll.", actualList.containsAll( expectedList ) );

    mockPromotionDAO.verify();
  }

  /**
   * Test getting all non expired promotions from the database through the service.
   */
  public void testGetAllNonExpired()
  {
    List expectedList = getPromotionList();

    mockPromotionDAO.expects( once() ).method( "getAllNonExpired" ).will( returnValue( expectedList ) );

    List actualList = promotionServiceImpl.getAllNonExpired();

    assertTrue( "Actual set didn't contain expected set for getAll.", actualList.containsAll( expectedList ) );

    mockPromotionDAO.verify();
  }

  /**
   * Test getting all expired promotions from the database through the service.
   */
  public void testGetAllExpired()
  {
    List expectedList = getPromotionList();

    mockPromotionDAO.expects( once() ).method( "getAllExpired" ).will( returnValue( expectedList ) );

    List actualList = promotionServiceImpl.getAllExpired();

    assertTrue( "Actual set didn't contain expected set for getAll.", actualList.containsAll( expectedList ) );

    mockPromotionDAO.verify();
  }

  public void testSaveWebRulesCmText()
  {
    Promotion expectedPromotion = buildProductClaimPromotion( "100" );
    String uniqueAssetName = "Asset Name";
    String data = "data";

    mockCmAssetService.expects( once() ).method( "getUniqueAssetCode" ).will( returnValue( uniqueAssetName ) );
    mockCmAssetService.expects( once() ).method( "createOrUpdateAsset" );

    mockPromotionDAO.expects( once() ).method( "getPromotionById" ).with( same( expectedPromotion.getId() ) ).will( returnValue( expectedPromotion ) );

    try
    {
      Promotion p = promotionServiceImpl.saveWebRulesCmText( expectedPromotion, data );
      Promotion p2 = promotionServiceImpl.getPromotionById( p.getId() );
    }
    catch( ServiceErrorException e )
    {
      logger.error( e.getMessage(), e );
    }

  }

  // /**
  // * Test the processPromotionForActivities method.
  // */
  // public void ignoretestProcessPromotionForActivities()
  // {
  // Participant pax1 = new Participant();
  // pax1.setId( new Long(1) );
  //
  // Participant pax2 = new Participant();
  // pax2.setId( new Long(2) );
  //
  // Participant pax3 = new Participant();
  // pax3.setId( new Long(3) );
  //
  // Claim claim1 = new Claim();
  // claim1.setId( new Long(1) );
  //
  // Claim claim2 = new Claim();
  // claim2.setId( new Long(2) );
  //
  // SalesActivity activity1 = new SalesActivity();
  // activity1.setId( new Long(1) );
  // activity1.setParticipant( pax1 );
  // activity1.setClaim( claim1 );
  //
  // SalesActivity activity2 = new SalesActivity();
  // activity2.setId( new Long(2) );
  // activity2.setParticipant( pax1 );
  // activity2.setClaim( claim1 );
  //
  // SalesActivity activity3 = new SalesActivity();
  // activity3.setId( new Long(3) );
  // activity3.setParticipant( pax2 );
  // activity3.setClaim( claim1 );
  //
  // SalesActivity activity4 = new SalesActivity();
  // activity4.setId( new Long(4) );
  // activity4.setParticipant( pax3 );
  // activity4.setClaim( claim2 );
  //
  // SalesActivity activity5 = new SalesActivity();
  // activity5.setId( new Long(5) );
  // activity5.setParticipant( pax1 );
  // activity5.setClaim( claim2 );
  //
  // List activities = new ArrayList();
  // activities.add( activity1 );
  // activities.add( activity2 );
  // activities.add( activity3 );
  // activities.add( activity4 );
  // activities.add( activity5 );
  //
  // Set journals = new LinkedHashSet();
  // Journal journal1 = new Journal();
  // journals.add(journal1);
  //
  // promotionEngineServiceControl.expectAndReturn(
  // promotionEngineServiceMock.calculatePayout(Collections.singletonList(activity3)),journals );
  // promotionEngineServiceControl.expectAndReturn(
  // promotionEngineServiceMock.calculatePayout(Collections.singletonList(activity4)),journals );
  // promotionEngineServiceControl.expectAndReturn(
  // promotionEngineServiceMock.calculatePayout(Collections.singletonList(activity5)),journals );
  // List expectedActivities = new ArrayList();
  // expectedActivities.add(activity1);
  // expectedActivities.add(activity2);
  // promotionEngineServiceControl.expectAndReturn(
  // promotionEngineServiceMock.calculatePayout(expectedActivities),journals );
  //
  // for (int i = 0; i < 4; i++)
  // {
  // mockAccountTransactionDAOControl.expectAndReturn(mockAccountTransactionDAOMock.saveJournalEntryToAwardsBanq(journal1),
  // journal1);
  // journalDAOControl.expectAndReturn(journalDAOMock.saveJournalEntry(journal1), journal1);
  // }
  //
  // promotionEngineServiceControl.replay();
  // mockAccountTransactionDAOControl.replay();
  // journalDAOControl.replay();
  //
  // Map mapOfLists = classUnderTest.processPromotionForActivities( activities );
  //
  // promotionEngineServiceControl.verify();
  // mockAccountTransactionDAOControl.verify();
  // journalDAOControl.verify();
  //
  // String keyClaim1Pax1 = claim1.getId() + "_" + pax1.getId();
  // String keyClaim2Pax1 = claim2.getId() + "_" + pax1.getId();
  //
  // assertEquals( 4, mapOfLists.keySet().size() );
  // assertEquals( 2, ((List) mapOfLists.get( keyClaim1Pax1)).size() );
  // assertEquals( 1, ((List) mapOfLists.get( keyClaim2Pax1)).size() );
  //
  // List pax1Claim2List = (List) mapOfLists.get( keyClaim2Pax1 );
  // SalesActivity activityResult = (SalesActivity)pax1Claim2List.get(0);
  //
  // assertEquals( activityResult.getId().longValue(), activity5.getId().longValue() );
  // }

  /**
   * Test the isDateValidForPromotion method.
   */
  public void testIsDateValidForPromotion()
  {
    PromotionDAO mock = EasyMock.createMock( PromotionDAO.class );
    PromotionServiceImpl localClassUnderTest = new PromotionServiceImpl();
    localClassUnderTest.setPromotionDAO( mock );

    Promotion promotionInsideDateRange = new ProductClaimPromotion();
    promotionInsideDateRange.setSubmissionStartDate( new Date( new Date().getTime() - ( DateUtils.MILLIS_PER_DAY * 2 ) ) );
    promotionInsideDateRange.setSubmissionEndDate( new Date( new Date().getTime() + ( DateUtils.MILLIS_PER_DAY * 2 ) ) );

    Promotion promotionSameDayDateRange = new ProductClaimPromotion();
    promotionSameDayDateRange.setSubmissionStartDate( new Date( new Date().getTime() - ( DateUtils.MILLIS_PER_DAY * 2 ) ) );
    promotionSameDayDateRange.setSubmissionEndDate( new Date( new Date().getTime() - ( DateUtils.MILLIS_PER_DAY * 2 ) ) );

    Promotion promotionOutsideDateRange = new ProductClaimPromotion();
    promotionOutsideDateRange.setSubmissionStartDate( new Date( new Date().getTime() - ( DateUtils.MILLIS_PER_DAY * 10 ) ) );
    promotionOutsideDateRange.setSubmissionEndDate( new Date( new Date().getTime() - ( DateUtils.MILLIS_PER_DAY * 8 ) ) );

    Promotion promotionWithNullEndDate = new ProductClaimPromotion();
    promotionWithNullEndDate.setSubmissionStartDate( new Date( new Date().getTime() - ( DateUtils.MILLIS_PER_DAY * 10 ) ) );
    promotionWithNullEndDate.setSubmissionEndDate( null );

    Date dateToCheck = new Date( new Date().getTime() - ( DateUtils.MILLIS_PER_DAY * 1 ) );
    Date dateToCheckOnStartDate = new Date( new Date().getTime() - ( DateUtils.MILLIS_PER_DAY * 2 ) );
    Date dateToCheckOnEndDate = new Date( new Date().getTime() + ( DateUtils.MILLIS_PER_DAY * 2 ) );

    assertTrue( promotionInsideDateRange.isDateValidForSubmission( dateToCheck ) );
    assertTrue( promotionInsideDateRange.isDateValidForSubmission( dateToCheckOnStartDate ) );
    assertTrue( promotionInsideDateRange.isDateValidForSubmission( dateToCheckOnEndDate ) );
    assertFalse( promotionOutsideDateRange.isDateValidForSubmission( dateToCheck ) );
    assertTrue( promotionSameDayDateRange.isDateValidForSubmission( dateToCheckOnStartDate ) );
    assertTrue( promotionWithNullEndDate.isDateValidForSubmission( dateToCheck ) );

  }

  /**
   * Test the copyPromotion method.
   * 
   * @throws UniqueConstraintViolationException
   * @throws ServiceErrorException
   */
  /*
   * public void testCopyProductClaimPromotion() throws UniqueConstraintViolationException,
   * ServiceErrorException { // create a promotion object Long id = new Long( 1 ); Promotion
   * expectedPromotion = buildProductClaimPromotion( "100" ); expectedPromotion.setName(
   * "Copied Promotion Name" ); expectedPromotion.setId( id ); expectedPromotion.setCmAssetCode(
   * "cmAssetName" ); expectedPromotion.setWebRulesCmKey( "cmKey" ); logger.info(
   * "expectedPromotion: " + expectedPromotion ); Promotion copiedPromotion = null;
   * mockPromotionDAO.expects( once() ).method( "getPromotionByIdWithAssociations" ) .with( same(
   * expectedPromotion.getId() ), isA( AssociationRequestCollection.class ) ) .will( returnValue(
   * expectedPromotion ) ); mockCmAssetService.expects( once() ).method( "getUniqueAssetCode"
   * ).with( same(Promotion.PROMOTION_NAME_ASSET_PREFIX)) .will( returnValue(
   * expectedPromotion.getCmAssetCode() ) ); mockCmAssetService.expects( once() ).method(
   * "getUniqueAssetCode" ).with( same(Promotion.CM_PROMOTION_DATA_WEBRULES_ASSET_PREFIX)) .will(
   * returnValue( expectedPromotion.getWebRulesCmKey() ) ); mockPromotionDAO.expects( once()
   * ).method( "isPromotionNameUnique" ).will( returnValue( true ) ); mockPromotionDAO.expects(
   * once() ).method( "isPromotionNameUnique" ).will( returnValue( true ) );
   * mockPromotionDAO.expects( once() ).method( "save" ).will( returnValue( expectedPromotion ) );
   * mockClaimFormDefinitionService.expects( once() ).method( "updateClaimFormStatus" );
   * mockCmAssetService.expects( once() ).method( "copyCMAsset" ); mockCmAssetService.expects(
   * once() ).method( "createOrUpdateAsset" ); copiedPromotion = promotionServiceImpl.copyPromotion(
   * expectedPromotion.getId(), expectedPromotion.getName(), null ); logger.info(
   * "copiedPromotion: " + copiedPromotion ); expectedPromotion.setId( null ); assertEquals(
   * "Actual promotion didn't match with what is expected", expectedPromotion, copiedPromotion ); }
   */

  /**
   * Test the copyPromotion method.
   * 
   * @throws UniqueConstraintViolationException
   * @throws ServiceErrorException
   */
  public void testCopyRecognitionPromotion() throws UniqueConstraintViolationException, ServiceErrorException
  {
    // create a promotion object
    Long id = new Long( 1 );
    RecognitionPromotion expectedPromotion = PromotionDAOImplTest.buildRecognitionPromotion( "100" );
    expectedPromotion.setName( "Copied Promotion Name" );
    expectedPromotion.setId( id );
    expectedPromotion.setCmAssetCode( "cmAssetName" );
    expectedPromotion.setWebRulesCmKey( "cmKey" );

    // set Award Type
    expectedPromotion.setAwardAmountTypeFixed( Boolean.TRUE );

    // set budgetMaster
    Participant pax = ParticipantDAOImplTest.buildUniqueParticipant( getUniqueString() );
    pax.setId( id );
    BudgetMaster budgetMaster = BudgetMasterDAOImplTest.buildBudgetMaster( "TestBudget" + getUniqueString(), BudgetMasterDAOImplTest.buildBudgetSegment() );
    budgetMaster.setId( id );
    expectedPromotion.setBudgetMaster( budgetMaster );
    // set promoGiverAudiences
    Audience audience = AudienceDAOImplTest.getPaxAudience( pax, getUniqueString() );
    expectedPromotion.addPromotionPrimaryAudience( new PromotionPrimaryAudience( audience, expectedPromotion ) );

    // set promoRecieverAudiences
    Audience audience2 = AudienceDAOImplTest.getPaxAudience( pax, getUniqueString() );
    expectedPromotion.addPromotionSecondaryAudience( new PromotionSecondaryAudience( audience2, expectedPromotion ) );
    // set promoECard
    PromotionECard promotionECard = new PromotionECard();
    ECard ecard = new ECard();
    ecard.setId( id );
    ecard.setName( getUniqueString() );
    promotionECard.seteCard( ecard );
    expectedPromotion.addECard( promotionECard );
    expectedPromotion.setAllowYourOwnCard( Boolean.TRUE );
    expectedPromotion.setDrawYourOwnCard( Boolean.TRUE );

    // set certificate
    PromotionCert promotionCert = new PromotionCert();
    promotionCert.setId( id );
    promotionCert.setCertificateId( "1" );
    expectedPromotion.addCertificate( promotionCert );

    // set Public recognition
    expectedPromotion.setAllowPublicRecognition( Boolean.FALSE );
    expectedPromotion.setAllowPublicRecognitionPoints( Boolean.FALSE );
    /*
     * expectedPromotion.setPublicRecogAwardAmountTypeFixed( Boolean.TRUE );
     * expectedPromotion.setPublicRecogAwardAmountFixed( new Long(10) );
     */

    // set mandatory
    expectedPromotion.setCopyOthers( Boolean.FALSE );
    expectedPromotion.setAllowManagerAward( Boolean.FALSE );
    expectedPromotion.setApqConversion( Boolean.FALSE );
    expectedPromotion.setAllowRecognitionSendDate( Boolean.TRUE );

    logger.info( "expectedPromotion: " + expectedPromotion );

    Promotion copiedPromotion = null;

    mockPromotionDAO.expects( once() ).method( "getPromotionByIdWithAssociations" ).with( same( expectedPromotion.getId() ), isA( AssociationRequestCollection.class ) )
        .will( returnValue( expectedPromotion ) );

    mockCmAssetService.expects( once() ).method( "getUniqueAssetCode" ).with( same( Promotion.PROMOTION_NAME_ASSET_PREFIX ) ).will( returnValue( expectedPromotion.getCmAssetCode() ) );

    mockCmAssetService.expects( once() ).method( "getUniqueAssetCode" ).with( same( Promotion.CM_PROMOTION_DATA_WEBRULES_ASSET_PREFIX ) ).will( returnValue( expectedPromotion.getWebRulesCmKey() ) );

    mockPromotionDAO.expects( once() ).method( "isPromotionNameUnique" ).will( returnValue( true ) );

    mockPromotionDAO.expects( once() ).method( "isPromotionNameUnique" ).will( returnValue( true ) );

    mockPromotionDAO.expects( once() ).method( "save" ).will( returnValue( expectedPromotion ) );

    mockClaimFormDefinitionService.expects( once() ).method( "updateClaimFormStatus" );

    mockCmAssetService.expects( once() ).method( "copyCMAsset" );
    mockCmAssetService.expects( once() ).method( "createOrUpdateAsset" );

    copiedPromotion = promotionServiceImpl.copyPromotion( expectedPromotion.getId(), expectedPromotion.getName(), null );

    logger.info( "copiedPromotion: " + copiedPromotion );
    expectedPromotion.setId( null ); // expected promotion would not have an id

    Map<String, Object[]> results = null;
    try
    {
      // These are attributes that we expect to be different between the two promotion objects
      String[] ignoreKeys = new String[] { "promotionStatus", // The new one gets a new status
                                           "promoNameFromCM", // Copied promotion gets its own name
                                           "promoNameAssetCode", // The copy method generates new
                                                                 // codes, but copies the associated
                                                                 // data
                                           "hasSweepstakesToProcess", // Sweepstakes was ignored by
                                                                      // the original test
                                           "promotionSweepstakes", // "
                                           "cmAssetCode", // Codes are reset
                                           "sortName", // Name is changed
                                           "budgetMaster", // Ignored by original test
                                           "budgetUsed", // "
                                           "promotionBehaviors", // "
                                           "live", // Copy promo isn't live yet
                                           "underConstruction", // The new promo is, old one isn't
                                           "name", // Name is changed
                                           "promotionNameSplit", // "
                                           "version", // version is reset to 0
                                           "splitPromotionNames", // This one isn't actually an
                                                                  // attribute, nor is it referenced
                                                                  // anywhere
                                           "livePrimaryAndChildPromotions", // No associated
                                                                            // attribute with this
                                                                            // get method
                                           "promotionSecondaryAudiences", // These should be
                                                                          // similar, so we check
                                                                          // more later
                                           "promotionPrimaryAudiences", // These should be similar,
                                                                        // so we check more later
                                           "webRulesText", // This references codes, which are
                                                           // regenerated for copy
                                           "promotionBudgetSweeps" };
      results = handyCompare( copiedPromotion, expectedPromotion, "com.biperf.core.domain.promotion.RecognitionPromotion" );

      for ( String key : ignoreKeys )
      {
        results.remove( key );
      }

      for ( String s : results.keySet() )
      {
        System.out.println( s + ": " + results.get( s )[0] + " != " + results.get( s )[1] );
      }
    }
    catch( Exception e )
    {
      e.printStackTrace();
      fail();
    }

    assertTrue( expectedPromotion.getPromotionSecondaryAudiences().size() == copiedPromotion.getPromotionSecondaryAudiences().size() );

    assertTrue( expectedPromotion.getPromotionPrimaryAudiences().size() == copiedPromotion.getPromotionPrimaryAudiences().size() );

    assertTrue( results.size() != 0 );

    // BaseDAOTest.assertDomainObjectEquals( "Actual promotion didn't match with what is expected",
    // expectedPromotion,
    // copiedPromotion,
    // true,
    // ignoredProperties );
  }

  /**
   * Test the createChildPromotion method.
   * 
   * @throws ServiceErrorException
   */
  /*
   * public void testCreateChildPromotion() throws ServiceErrorException { // create a parent
   * promotion object Long id = new Long( 1 ); ProductClaimPromotion parentPromotion =
   * buildProductClaimPromotion( "100" ); parentPromotion.setName( "Parent Promotion Name" );
   * parentPromotion.setId( id ); // create an expected child promotion ProductClaimPromotion
   * expectedPromotion = buildProductClaimPromotion( "100" ); expectedPromotion.setParentPromotion(
   * parentPromotion ); expectedPromotion.setPayoutManagerPeriod( null ); expectedPromotion.setName(
   * null ); logger.info( "expectedPromotion: " + expectedPromotion ); Promotion childPromotion =
   * null; mockPromotionDAO.expects( once() ).method( "getPromotionByIdWithAssociations" ) .with(
   * same( parentPromotion.getId() ), isA( AssociationRequestCollection.class ) ) .will(
   * returnValue( parentPromotion ) ); childPromotion = promotionServiceImpl.createChildPromotion(
   * parentPromotion.getId() ); logger.info( "childPromotion: " + childPromotion ); assertEquals(
   * "Actual promotion didn't match with what is expected", expectedPromotion, childPromotion ); }
   */

  /**
   * Tests to get a list of participants in a recoginition's giverAudience who aren't budget owners
   * in the same recognition promotion's budgetMaster.
   */
  public void testGetParticipantsWithoutBudgetAllocationWithinPromotion()
  {

    String uniqueString = getUniqueString();

    // Build a promotion with a giverAudience of participants.
    RecognitionPromotion recognitionPromotion = buildRecognitionPromotion();
    recognitionPromotion.setPrimaryAudienceType( PrimaryAudienceType.lookup( PrimaryAudienceType.SPECIFY_AUDIENCE_CODE ) );

    // Build the giver audience for this promotion
    PromotionPrimaryAudience promotionGiverAudience = new PromotionPrimaryAudience();
    PaxAudience paxAudience = new PaxAudience();

    Participant pax1 = ParticipantDAOImplTest.buildUniqueParticipant( "pax1_" + uniqueString );
    Participant pax2 = ParticipantDAOImplTest.buildUniqueParticipant( "pax2_" + uniqueString );
    Participant pax3 = ParticipantDAOImplTest.buildUniqueParticipant( "pax3_" + uniqueString );

    paxAudience.addParticipant( pax1 );
    paxAudience.addParticipant( pax2 );
    paxAudience.addParticipant( pax3 );

    promotionGiverAudience.setAudience( paxAudience );
    recognitionPromotion.addPromotionPrimaryAudience( promotionGiverAudience );

    // build and save budget master with segment
    BudgetMaster budgetMaster = buildBudgetMaster( "test" + uniqueString, buildBudgetSegment() );

    BudgetSegment budgetSegment = budgetMaster.getCurrentBudgetSegment( null );

    // Build the list of
    Budget budget1 = new Budget();
    budget1.setUser( pax1 );
    budget1.setCurrentValue( BigDecimal.valueOf( 123 ) );

    Budget budget2 = new Budget();
    budget2.setUser( pax2 );
    budget2.setCurrentValue( BigDecimal.valueOf( 123 ) );

    Budget budget3 = new Budget();
    budget3.setUser( pax3 );
    budget3.setCurrentValue( BigDecimal.valueOf( 123 ) );

    // add budget to segment
    budgetSegment.addBudget( budget1 );
    budgetSegment.addBudget( budget2 );

    recognitionPromotion.setBudgetMaster( budgetMaster );

    List budgetList = new ArrayList();
    budgetList.add( budget1 );
    budgetList.add( budget2 );

    // Pax2 is in the giver audience but doesn't have a bugdet
    Set expectedParticipantSet = new LinkedHashSet();
    expectedParticipantSet.add( pax3 );

    mockPromotionDAO.expects( once() ).method( "getPromotionById" ).with( same( recognitionPromotion.getId() ) ).will( returnValue( recognitionPromotion ) );

    mockBudgetMasterDAO.expects( once() ).method( "getBudgetsByBudgetSegmentId" ).with( same( budgetSegment.getId() ) ).will( returnValue( budgetList ) );

    Set actualParticipantSet = promotionServiceImpl.getParticipantsWithoutBudgetAllocationWithinPromotion( recognitionPromotion.getId(), budgetSegment.getId() );

    assertEquals( "Actual participant set wasn't equals to what was expected.", expectedParticipantSet, actualParticipantSet );
    mockPromotionDAO.reset();

    budgetMaster = buildBudgetMaster( "test1" + uniqueString, buildBudgetSegment() );
    budgetSegment = budgetMaster.getCurrentBudgetSegment( null );
    budgetSegment.addBudget( budget1 );
    budgetSegment.addBudget( budget2 );
    budgetSegment.addBudget( budget3 );

    recognitionPromotion.setBudgetMaster( budgetMaster );

    budgetList = new ArrayList();
    budgetList.add( budget1 );
    budgetList.add( budget2 );
    budgetList.add( budget3 );

    mockPromotionDAO.expects( once() ).method( "getPromotionById" ).with( same( recognitionPromotion.getId() ) ).will( returnValue( recognitionPromotion ) );

    mockBudgetMasterDAO.expects( once() ).method( "getBudgetsByBudgetSegmentId" ).with( same( budgetSegment.getId() ) ).will( returnValue( budgetList ) );
    actualParticipantSet = promotionServiceImpl.getParticipantsWithoutBudgetAllocationWithinPromotion( recognitionPromotion.getId(), budgetSegment.getId() );

    assertTrue( "All participants in the giverAudience didn't have budgets.", actualParticipantSet.size() == 0 );
    mockPromotionDAO.reset();

    budgetMaster = buildBudgetMaster( "test2" + uniqueString, buildBudgetSegment() );
    budgetSegment = budgetMaster.getCurrentBudgetSegment( null );

    recognitionPromotion.setBudgetMaster( budgetMaster );

    mockPromotionDAO.expects( once() ).method( "getPromotionById" ).with( same( recognitionPromotion.getId() ) ).will( returnValue( recognitionPromotion ) );

    mockBudgetMasterDAO.expects( once() ).method( "getBudgetsByBudgetSegmentId" ).with( same( budgetSegment.getId() ) ).will( returnValue( new ArrayList<Budget>() ) );
    actualParticipantSet = promotionServiceImpl.getParticipantsWithoutBudgetAllocationWithinPromotion( recognitionPromotion.getId(), budgetSegment.getId() );

    expectedParticipantSet.clear();
    expectedParticipantSet.add( pax1 );
    expectedParticipantSet.add( pax2 );
    expectedParticipantSet.add( pax3 );

    assertEquals( "All participants in the promotion's giver audience doesn't own budgets", expectedParticipantSet, actualParticipantSet );
    mockPromotionDAO.reset();

    recognitionPromotion.setPrimaryAudienceType( PrimaryAudienceType.lookup( PrimaryAudienceType.ALL_ACTIVE_PAX_CODE ) );

    budgetMaster = buildBudgetMaster( "test3" + uniqueString, buildBudgetSegment() );
    budgetSegment = budgetMaster.getCurrentBudgetSegment( null );
    budgetSegment.addBudget( budget1 );
    budgetSegment.addBudget( budget3 );

    recognitionPromotion.setBudgetMaster( budgetMaster );

    budgetList = new ArrayList();
    budgetList.add( budget1 );
    budgetList.add( budget3 );

    // Build the participant list
    List participantList = new ArrayList();
    participantList.add( pax1 );
    participantList.add( pax2 );
    participantList.add( pax3 );

    expectedParticipantSet.clear();
    expectedParticipantSet.add( pax2 );

    mockPromotionDAO.expects( once() ).method( "getPromotionById" ).with( same( recognitionPromotion.getId() ) ).will( returnValue( recognitionPromotion ) );
    mockParticipantDAO.expects( once() ).method( "getAllActive" ).will( returnValue( participantList ) );

    mockBudgetMasterDAO.expects( once() ).method( "getBudgetsByBudgetSegmentId" ).with( same( budgetSegment.getId() ) ).will( returnValue( budgetList ) );
    actualParticipantSet = promotionServiceImpl.getParticipantsWithoutBudgetAllocationWithinPromotion( recognitionPromotion.getId(), budgetSegment.getId() );
    assertEquals( "All participants in the active participant list doesn't own a budget for this promotion", expectedParticipantSet, actualParticipantSet );

  }

  public static String buildUniqueString()
  {
    return "TEST" + ( System.currentTimeMillis() % 3432423 ) + "." + uniqueStringCounter++;
  }

  public static BudgetMaster saveBudgetMaster()
  {
    String uniqueString = buildUniqueString();
    BudgetMaster budgetMaster = BudgetMasterDAOImplTest.buildAndSaveBudgetMaster( "TestBudget" + uniqueString, BudgetMasterDAOImplTest.buildBudgetSegment(), null );
    return budgetMaster;
  }

  public static BudgetMaster buildBudgetMaster( String budgetName, BudgetSegment budgetSegment )
  {
    BudgetMaster budgetMaster = new BudgetMaster();
    budgetMaster.setBudgetType( BudgetType.lookup( BudgetType.PAX_BUDGET_TYPE ) );
    budgetMaster.setAwardType( BudgetMasterAwardType.lookup( BudgetMasterAwardType.POINTS ) );
    budgetMaster.setOverrideableType( BudgetOverrideableType.lookup( BudgetOverrideableType.HARD_OVERRIDE ) );
    budgetMaster.setFinalPayoutRule( BudgetFinalPayoutRule.lookup( BudgetFinalPayoutRule.FULL_PAYOUT ) );
    budgetMaster.setNameCmKey( "New Asset Key" );
    budgetMaster.setCmAssetCode( "New Asset Name" );
    budgetMaster.setMultiPromotion( false );
    budgetMaster.setStartDate( new Date() );
    budgetMaster.addBudgetSegment( budgetSegment );
    return budgetMaster;
  }

  public static BudgetSegment buildBudgetSegment()
  {
    BudgetSegment budgetSegment = new BudgetSegment();
    budgetSegment.setName( "Default Segment" );
    Date currentDate = new Date();
    budgetSegment.setStartDate( currentDate );
    budgetSegment.setEndDate( com.biperf.core.utils.DateUtils.getNextDay( currentDate ) );
    budgetSegment.setStatus( Boolean.TRUE );

    budgetSegment.setId( new Long( 1 ) );

    return budgetSegment;
  }

  /**
   * Tests to get a list of participants in a recoginition's giverAudience who aren't budget owners
   * in the same recognition promotion's budgetMaster.
   */
  public void testGetNodesWithoutBudgetAllocationWithinPromotion()
  {

    String uniqueString = getUniqueString();

    // Build a promotion with a giverAudience of participants.
    RecognitionPromotion recognitionPromotion = buildRecognitionPromotion();
    recognitionPromotion.setPrimaryAudienceType( PrimaryAudienceType.lookup( PrimaryAudienceType.SPECIFY_AUDIENCE_CODE ) );

    // Build the giver audience for this promotion
    PromotionPrimaryAudience promotionGiverAudience = new PromotionPrimaryAudience();
    PaxAudience paxAudience = new PaxAudience();

    Participant pax1 = ParticipantDAOImplTest.buildUniqueParticipant( "pax1_" + uniqueString );
    Participant pax2 = ParticipantDAOImplTest.buildUniqueParticipant( "pax2_" + uniqueString );
    Participant pax3 = ParticipantDAOImplTest.buildUniqueParticipant( "pax3_" + uniqueString );

    paxAudience.addParticipant( pax1 );
    paxAudience.addParticipant( pax2 );
    paxAudience.addParticipant( pax3 );

    promotionGiverAudience.setAudience( paxAudience );
    recognitionPromotion.addPromotionPrimaryAudience( promotionGiverAudience );

    // Build the list of Nodes which will be returned for each promotion
    List pax1Nodes = new ArrayList();
    Node pax1Node1 = NodeDAOImplTest.buildUniqueNodeInPrimaryHierarchy( "pax1Node1_" + uniqueString );
    Node pax1Node2 = NodeDAOImplTest.buildUniqueNodeInPrimaryHierarchy( "pax1Node2_" + uniqueString );
    Node pax1Node3 = NodeDAOImplTest.buildUniqueNodeInPrimaryHierarchy( "pax1Node3_" + uniqueString );
    pax1Nodes.add( pax1Node1 );
    pax1Nodes.add( pax1Node2 );
    pax1Nodes.add( pax1Node3 );

    List pax2Nodes = new ArrayList();
    Node pax2Node1 = NodeDAOImplTest.buildUniqueNodeInPrimaryHierarchy( "pax2Node1_" + uniqueString );
    Node pax2Node2 = NodeDAOImplTest.buildUniqueNodeInPrimaryHierarchy( "pax2Node2_" + uniqueString );
    pax2Nodes.add( pax2Node1 );
    pax2Nodes.add( pax2Node2 );

    List pax3Nodes = new ArrayList();
    Node pax3Node1 = NodeDAOImplTest.buildUniqueNodeInPrimaryHierarchy( "pax3Node1_" + uniqueString );
    Node pax3Node2 = NodeDAOImplTest.buildUniqueNodeInPrimaryHierarchy( "pax3Node2_" + uniqueString );
    pax3Nodes.add( pax3Node1 );
    pax3Nodes.add( pax3Node2 );

    // These two are duplicate Nodes
    pax3Nodes.add( pax2Node2 );
    pax3Nodes.add( pax1Node2 );

    pax1.addNode( pax1Node1, Boolean.TRUE, HierarchyRoleType.lookup( HierarchyRoleType.MEMBER ), false );
    pax1.addNode( pax1Node2, Boolean.TRUE, HierarchyRoleType.lookup( HierarchyRoleType.MEMBER ), false );
    pax1.addNode( pax1Node3, Boolean.TRUE, HierarchyRoleType.lookup( HierarchyRoleType.MEMBER ), false );

    pax2.addNode( pax2Node1, Boolean.TRUE, HierarchyRoleType.lookup( HierarchyRoleType.MEMBER ), false );
    pax2.addNode( pax2Node2, Boolean.TRUE, HierarchyRoleType.lookup( HierarchyRoleType.MEMBER ), false );

    pax3.addNode( pax3Node1, Boolean.TRUE, HierarchyRoleType.lookup( HierarchyRoleType.MEMBER ), false );
    pax3.addNode( pax3Node2, Boolean.TRUE, HierarchyRoleType.lookup( HierarchyRoleType.MEMBER ), false );

    // Build the budgetMaster and budgets for this promotion.
    BudgetMaster budgetMaster = new BudgetMaster();

    // Build the list of
    Budget budget1 = new Budget();
    budget1.setNode( pax1Node1 );
    budget1.setCurrentValue( BigDecimal.valueOf( 312 ) );

    Budget budget2 = new Budget();
    budget2.setNode( pax1Node2 );
    budget2.setCurrentValue( BigDecimal.valueOf( 123 ) );

    Budget budget3 = new Budget();
    budget3.setNode( pax1Node3 );
    budget3.setCurrentValue( BigDecimal.valueOf( 123 ) );

    Budget budget4 = new Budget();
    budget4.setNode( pax2Node1 );
    budget4.setCurrentValue( BigDecimal.valueOf( 123 ) );

    Budget budget5 = new Budget();
    budget5.setNode( pax2Node2 );
    budget5.setCurrentValue( BigDecimal.valueOf( 123 ) );

    Budget budget6 = new Budget();
    budget6.setNode( pax3Node1 );
    budget6.setCurrentValue( BigDecimal.valueOf( 123 ) );

    Budget budget7 = new Budget();
    budget7.setNode( pax3Node2 );
    budget7.setCurrentValue( BigDecimal.valueOf( 123 ) );

    Budget budget8 = new Budget();
    budget8.setNode( pax2Node2 );
    budget8.setCurrentValue( BigDecimal.valueOf( 123 ) );

    Budget budget9 = new Budget();
    budget9.setNode( pax1Node2 );
    budget9.setCurrentValue( BigDecimal.valueOf( 123 ) );

    budgetMaster.addBudgetSegment( buildBudgetSegment() );
    BudgetSegment budgetSegment = budgetMaster.getCurrentBudgetSegment( null );

    budgetSegment.addBudget( budget1 );
    budgetSegment.addBudget( budget2 );
    budgetSegment.addBudget( budget3 );
    budgetSegment.addBudget( budget4 );
    budgetSegment.addBudget( budget5 );
    budgetSegment.addBudget( budget6 );
    budgetSegment.addBudget( budget7 );
    budgetSegment.addBudget( budget8 );
    budgetSegment.addBudget( budget9 );

    recognitionPromotion.setBudgetMaster( budgetMaster );

    List budgetList = new ArrayList();
    budgetList.add( budget1 );
    budgetList.add( budget2 );
    budgetList.add( budget3 );
    budgetList.add( budget4 );
    budgetList.add( budget5 );
    budgetList.add( budget6 );
    budgetList.add( budget7 );
    budgetList.add( budget8 );
    budgetList.add( budget9 );

    // Pax2 is in the giver audience but doesn't have a bugdet
    Set expectedNodeSet = new LinkedHashSet();
    expectedNodeSet.add( pax1Node1 );
    expectedNodeSet.add( pax1Node2 );
    expectedNodeSet.add( pax1Node3 );
    expectedNodeSet.add( pax2Node1 );
    expectedNodeSet.add( pax2Node2 );
    expectedNodeSet.add( pax3Node1 );
    expectedNodeSet.add( pax3Node2 );

    mockPromotionDAO.expects( once() ).method( "getPromotionById" ).with( same( recognitionPromotion.getId() ) ).will( returnValue( recognitionPromotion ) );

    mockBudgetMasterDAO.expects( once() ).method( "getBudgetsByBudgetSegmentId" ).with( same( budgetSegment.getId() ) ).will( returnValue( budgetList ) );

    Set actualNodeSet = promotionServiceImpl.getNodesWithoutBudgetAllocationWithinPromotion( recognitionPromotion.getId(), budgetSegment.getId() );

    assertTrue( "All nodes assigned to users in the giver Audience has budgets.", actualNodeSet.size() == 0 );

    mockPromotionDAO.reset();
    mockUserDAO.reset();

    budgetMaster = new BudgetMaster();
    budgetMaster.addBudgetSegment( buildBudgetSegment() );
    budgetSegment = budgetMaster.getCurrentBudgetSegment( null );
    budgetSegment.addBudget( budget1 );
    budgetSegment.addBudget( budget2 );

    recognitionPromotion.setBudgetMaster( budgetMaster );

    budgetList = new ArrayList();
    budgetList.add( budget1 );
    budgetList.add( budget2 );

    expectedNodeSet.clear();
    expectedNodeSet.add( pax1Node3 );
    expectedNodeSet.add( pax2Node2 );
    expectedNodeSet.add( pax2Node1 );
    expectedNodeSet.add( pax3Node1 );
    expectedNodeSet.add( pax3Node2 );

    mockPromotionDAO.expects( once() ).method( "getPromotionById" ).with( same( recognitionPromotion.getId() ) ).will( returnValue( recognitionPromotion ) );

    mockBudgetMasterDAO.expects( once() ).method( "getBudgetsByBudgetSegmentId" ).with( same( budgetSegment.getId() ) ).will( returnValue( budgetList ) );

    actualNodeSet = promotionServiceImpl.getNodesWithoutBudgetAllocationWithinPromotion( recognitionPromotion.getId(), budgetSegment.getId() );

    assertEquals( "All Nodes associated to the participants in the giverAudience didn't have budgets.", expectedNodeSet, actualNodeSet );

    mockPromotionDAO.reset();
    mockUserDAO.reset();

    budgetMaster = new BudgetMaster();

    recognitionPromotion.setBudgetMaster( budgetMaster );
    budgetList = new ArrayList<Budget>();

    mockPromotionDAO.expects( once() ).method( "getPromotionById" ).with( same( recognitionPromotion.getId() ) ).will( returnValue( recognitionPromotion ) );

    mockBudgetMasterDAO.expects( once() ).method( "getBudgetsByBudgetSegmentId" ).with( same( budgetSegment.getId() ) ).will( returnValue( budgetList ) );
    actualNodeSet = promotionServiceImpl.getNodesWithoutBudgetAllocationWithinPromotion( recognitionPromotion.getId(), budgetSegment.getId() );

    expectedNodeSet.clear();
    expectedNodeSet.add( pax1Node1 );
    expectedNodeSet.add( pax1Node2 );
    expectedNodeSet.add( pax1Node3 );
    expectedNodeSet.add( pax2Node1 );
    expectedNodeSet.add( pax2Node2 );
    expectedNodeSet.add( pax3Node1 );
    expectedNodeSet.add( pax3Node2 );

    assertEquals( "All nodes in the promotion's giver audience doesn't own budgets", expectedNodeSet, actualNodeSet );

    mockPromotionDAO.reset();
    mockUserDAO.reset();

    recognitionPromotion.setPrimaryAudienceType( PrimaryAudienceType.lookup( PrimaryAudienceType.ALL_ACTIVE_PAX_CODE ) );

    budgetMaster = new BudgetMaster();
    budgetMaster.addBudgetSegment( buildBudgetSegment() );
    budgetSegment = budgetMaster.getCurrentBudgetSegment( null );
    budgetSegment.addBudget( budget1 );
    budgetSegment.addBudget( budget3 );
    budgetSegment.addBudget( budget4 );
    budgetSegment.addBudget( budget5 );

    recognitionPromotion.setBudgetMaster( budgetMaster );

    budgetList = new ArrayList();
    budgetList.add( budget1 );
    budgetList.add( budget3 );
    budgetList.add( budget4 );
    budgetList.add( budget5 );

    // Build the participant list
    List participantList = new ArrayList();
    participantList.add( pax1 );
    participantList.add( pax2 );
    participantList.add( pax3 );

    // build the expectedNodeSet
    expectedNodeSet.clear();
    expectedNodeSet.add( pax1Node2 );
    expectedNodeSet.add( pax3Node1 );
    expectedNodeSet.add( pax3Node2 );

    mockPromotionDAO.expects( once() ).method( "getPromotionById" ).with( same( recognitionPromotion.getId() ) ).will( returnValue( recognitionPromotion ) );
    mockParticipantDAO.expects( once() ).method( "getAllActive" ).will( returnValue( participantList ) );

    mockBudgetMasterDAO.expects( once() ).method( "getBudgetsByBudgetSegmentId" ).with( same( budgetSegment.getId() ) ).will( returnValue( budgetList ) );

    actualNodeSet = promotionServiceImpl.getNodesWithoutBudgetAllocationWithinPromotion( recognitionPromotion.getId(), budgetSegment.getId() );

    assertEquals( "All node for all participants in this promotion doesn't own budgets", expectedNodeSet, actualNodeSet );

  }

  public void testGetAvailableBudget()
  {
    String uniqueString = getUniqueString();

    // Build a promotion with a giverAudience of participants.
    RecognitionPromotion recognitionPromotion = buildRecognitionPromotion();
    recognitionPromotion.setPrimaryAudienceType( PrimaryAudienceType.lookup( PrimaryAudienceType.SPECIFY_AUDIENCE_CODE ) );
    recognitionPromotion.setId( new Long( 5001 ) );

    Participant pax1 = ParticipantDAOImplTest.buildUniqueParticipant( "pax1_" + uniqueString );
    pax1.setId( new Long( 101 ) );

    // Build the list of Nodes which will be returned for each promotion
    List pax1Nodes = new ArrayList();
    Node pax1Node1 = NodeDAOImplTest.buildUniqueNode( "pax1Node1_" + uniqueString );
    pax1Node1.setId( new Long( 1 ) );
    Node pax1Node2 = NodeDAOImplTest.buildUniqueNode( "pax1Node2_" + uniqueString );
    pax1Node2.setId( new Long( 2 ) );
    Node pax1Node3 = NodeDAOImplTest.buildUniqueNode( "pax1Node3_" + uniqueString );
    pax1Node3.setId( new Long( 3 ) );
    pax1Nodes.add( pax1Node1 );
    pax1Nodes.add( pax1Node2 );
    pax1Nodes.add( pax1Node3 );

    pax1.addNode( pax1Node1, Boolean.TRUE, HierarchyRoleType.lookup( HierarchyRoleType.MEMBER ), false );
    pax1.addNode( pax1Node2, Boolean.TRUE, HierarchyRoleType.lookup( HierarchyRoleType.MEMBER ), false );
    pax1.addNode( pax1Node3, Boolean.TRUE, HierarchyRoleType.lookup( HierarchyRoleType.MEMBER ), false );

    // Build the budgetMaster and budgets for this promotion.
    Long id = new Long( 1 );
    BudgetMaster budgetMaster = buildBudgetMaster( "TestBudget" + getUniqueString(), buildBudgetSegment() );
    budgetMaster.setId( id );
    budgetMaster.setBudgetType( BudgetType.lookup( BudgetType.NODE_BUDGET_TYPE ) );

    // Build the list of
    Budget budget1 = new Budget();
    budget1.setNode( pax1Node1 );
    budget1.setCurrentValue( BigDecimal.valueOf( 312 ) );

    Budget budget2 = new Budget();
    budget2.setNode( pax1Node2 );
    budget2.setCurrentValue( BigDecimal.valueOf( 123 ) );

    Budget budget3 = new Budget();
    budget3.setNode( pax1Node3 );
    budget3.setCurrentValue( BigDecimal.valueOf( 123 ) );

    BudgetSegment budgetSegment = budgetMaster.getCurrentBudgetSegment( null );
    budgetSegment.addBudget( budget1 );

    recognitionPromotion.setBudgetMaster( budgetMaster );

    // Pax2 is in the giver audience but doesn't have a bugdet
    Set expectedNodeSet = new LinkedHashSet();
    expectedNodeSet.add( pax1Node1 );
    expectedNodeSet.add( pax1Node2 );
    expectedNodeSet.add( pax1Node3 );

    mockPromotionDAO.expects( once() ).method( "getPromotionById" ).with( same( recognitionPromotion.getId() ) ).will( returnValue( recognitionPromotion ) );

    mockParticipantDAO.expects( once() ).method( "getParticipantById" ).with( same( pax1.getId() ) ).will( returnValue( pax1 ) );
    mockNodeDAO.expects( once() ).method( "getNodeById" ).with( same( pax1Node1.getId() ) ).will( returnValue( pax1Node1 ) );

    mockBudgetMasterService.expects( once() ).method( "getBudgetMasterById" ).with( same( budgetMaster.getId() ), isA( AssociationRequestCollection.class ) ).will( returnValue( budgetMaster ) );

    mockAudienceService.expects( once() ).method( "isParticipantInPrimaryAudience" ).with( same( recognitionPromotion ), same( pax1 ) ).will( returnValue( true ) );

    mockUserService.expects( once() ).method( "getUserTimeZoneByUserCountry" ).with( eq( pax1.getId() ) ).will( returnValue( new String() ) );

    mockBudgetMasterDAO.expects( once() ).method( "getBudgetForNodeByBudgetSegmentId" ).with( eq( budgetSegment.getId() ), eq( pax1Node1.getId() ) ).will( returnValue( budget1 ) );

    Budget budget = promotionServiceImpl.getAvailableBudget( recognitionPromotion.getId(), pax1.getId(), pax1Node1.getId() );

    assertTrue( "Budget Not Found", budget != null );
  }

  public void testGetAllLiveAndExpiredByTypeAndUserId()
  {

    Participant participant1 = PromotionDAOImplTest.buildParticipant( 1 );
    Node node1 = buildNode( 1 );
    UserNode userNode1 = new UserNode();
    userNode1.setIsPrimary( Boolean.TRUE );
    userNode1.setNode( node1 );

    Set nodeSet = new HashSet();
    nodeSet.add( userNode1 );

    participant1.setUserNodes( nodeSet );
    Participant participant2 = PromotionDAOImplTest.buildParticipant( 2 );
    participant2.setUserNodes( nodeSet );

    Participant participant3 = PromotionDAOImplTest.buildParticipant( 3 );
    participant3.setUserNodes( nodeSet );

    Participant participant4 = PromotionDAOImplTest.buildParticipant( 4 );
    participant4.setUserNodes( nodeSet );

    List promotions = new ArrayList();

    Promotion promotion1 = buildRecognitionPromotion( "1" );
    promotion1.setId( new Long( 1 ) );
    promotion1.setApproverNode( node1 );
    promotion1.setPromotionStatus( PromotionStatusType.lookup( PromotionStatusType.EXPIRED ) );

    PaxAudience primaryPaxAudience = new PaxAudience();

    primaryPaxAudience.setId( new Long( 1 ) );
    primaryPaxAudience.setName( String.valueOf( 1 ) );
    primaryPaxAudience.addParticipant( participant1 );

    PaxAudience secondaryPaxAudience = new PaxAudience();
    secondaryPaxAudience.setId( new Long( 2 ) );
    secondaryPaxAudience.setName( String.valueOf( 2 ) );
    secondaryPaxAudience.addParticipant( participant2 );
    secondaryPaxAudience.addParticipant( participant3 );
    secondaryPaxAudience.addParticipant( participant4 );

    Set primaryAudience = new HashSet();
    primaryAudience.add( primaryPaxAudience );
    Set secondaryAudience = new HashSet();
    secondaryAudience.add( secondaryPaxAudience );

    promotion1.setPromotionPrimaryAudiences( primaryAudience );
    promotion1.setPromotionSecondaryAudiences( secondaryAudience );

    promotions.add( promotion1 );

    mockPromotionDAO.expects( once() ).method( "getAllLiveAndExpiredByType" ).with( same( PromotionType.RECOGNITION ) ).will( returnValue( promotions ) );

    mockParticipantService.expects( once() ).method( "getParticipantById" ).with( same( participant1.getId() ) ).will( returnValue( participant1 ) );

    mockAudienceService.expects( once() ).method( "isParticipantInPrimaryAudience" ).with( same( promotion1 ), same( participant1 ) ).will( returnValue( true ) );

    mockAudienceService.expects( once() ).method( "isParticipantInSecondaryAudience" ).with( same( promotion1 ), same( participant1 ), same( null ) ).will( returnValue( true ) );

    // build up user list to return
    List userList = new ArrayList();
    userList.add( participant1 );
    userList.add( participant2 );
    userList.add( participant3 );
    userList.add( participant4 );

    List promotionPaxValueList = promotionServiceImpl.getAllLiveAndExpiredByTypeAndUserId( PromotionType.RECOGNITION, participant1.getId() );

    PromotionPaxValue promoPaxValue1 = (PromotionPaxValue)promotionPaxValueList.get( 0 );
    PromotionPaxValue promoPaxValue2 = (PromotionPaxValue)promotionPaxValueList.get( 1 );

    assertEquals( 2, promotionPaxValueList.size() );

    assertEquals( PromotionServiceImpl.RECOGNITION_PRIMARY, promoPaxValue1.getRoleKey() );
    assertEquals( PromotionType.RECOGNITION, promoPaxValue1.getModuleCode() );
    assertEquals( 1, promoPaxValue1.getPromotion().getId().longValue() );
    assertEquals( PromotionServiceImpl.RECOGNITION_SECONDARY, promoPaxValue2.getRoleKey() );
    assertEquals( PromotionType.RECOGNITION, promoPaxValue2.getModuleCode() );
    assertEquals( 1, promoPaxValue2.getPromotion().getId().longValue() );

  }

  /**
   * Build a list of promotion domain objects for testing.
   * 
   * @return List
   */
  private List getPromotionList()
  {
    List promotionList = new ArrayList();

    Promotion promotion1 = PromotionServiceImplTest.buildProductClaimPromotion( "100" );
    promotion1.setId( new Long( 1 ) );

    Promotion promotion2 = PromotionServiceImplTest.buildProductClaimPromotion( "101" );
    promotion2.setId( new Long( 2 ) );

    Promotion promotion3 = PromotionServiceImplTest.buildProductClaimPromotion( "102" );
    promotion3.setId( new Long( 3 ) );

    promotionList.add( promotion1 );
    promotionList.add( promotion2 );
    promotionList.add( promotion3 );

    return promotionList;

  }

  /*
   * TODO: Davd Richardson: We probably shouldn't be referencing out to DAO tests for utility
   * methods, they may get updated to fix DAO tests, then break with respect to service tests.
   */

  /**
   * creates a promotion domain object
   * 
   * @param suffix
   * @return Promotion
   */
  public static ProductClaimPromotion buildProductClaimPromotion( String suffix )
  {
    return PromotionDAOImplTest.buildProductClaimPromotion( suffix );
  }

  /**
   * Creates a recognitionPromotion.
   * 
   * @return RecognitionPromotion
   */
  public static RecognitionPromotion buildRecognitionPromotion()
  {

    String uniqueString = getUniqueString();

    RecognitionPromotion recognitionPromotion = PromotionDAOImplTest.buildRecognitionPromotion( "RECOGNITIONPROMOTION_" + uniqueString );

    return recognitionPromotion;
  }

  /**
   * Creates a recognition promotion domain object
   * 
   * @param suffix
   * @return Promotion
   */
  public static RecognitionPromotion buildRecognitionPromotion( String suffix )
  {

    return PromotionDAOImplTest.buildRecognitionPromotion( suffix );

  }

  /**
   * Creates a quiz promotion domain object
   * 
   * @param suffix
   * @return Promotion
   */
  public static QuizPromotion buildQuizPromotion( String suffix )
  {

    return PromotionDAOImplTest.buildQuizPromotion( suffix );

  }

  public static Node buildNode( long id )
  {
    Node node = new Node();
    node.setId( new Long( id ) );
    node.setName( String.valueOf( id ) );

    return node;
  }

  public void testGetPaxPromoRules() throws LoginException
  {
    User user = new User();
    user.setId( new Long( 60020 ) );
    user.setUserName( "BHD-070" );
    user.setActive( Boolean.TRUE );
    user.setLanguageType( LanguageType.lookup( "en_US" ) );

    final String PASSWORD = "password";
    user.setPassword( PASSWORD );

    user.setLoginFailuresCount( new Integer( 0 ) );
    user.setUserType( UserType.lookup( UserType.PARTICIPANT ) );
    String country = "en_US";

    List<Participant> delegates = new ArrayList<Participant>();

    mockUserDAO.expects( once() ).method( "isPasswordValid" ).with( same( user ), same( PASSWORD ) ).will( returnValue( true ) );

    mockUserDAO.expects( once() ).method( "getUserByUserName" ).with( eq( user.getUserName() ) ).will( returnValue( user ) );

    mockUserDAO.expects( once() ).method( "getUserTimeZone" ).with( eq( user.getId() ) ).will( returnValue( country ) );
    mockUserDAO.expects( once() ).method( "saveUser" ).with( same( user ) ).will( returnValue( user ) );

    mockUserLockoutStrategy.expects( once() ).method( "isUserLockedout" ).with( same( user ) ).will( returnValue( Boolean.FALSE ) );

    mockAccountExpirationStrategy.expects( once() ).method( "isAccountExpired" ).with( same( user ) ).will( returnValue( Boolean.FALSE ) );

    mockUserLockoutStrategy.expects( atLeastOnce() ).method( "handleAuthentication" );

    // mockUserLockoutStrategy.expects( once() ).method( "handleAuthentication" ).with( eq( user ),
    // eq( true ) );

    mockPasswordPolicyStrategy.expects( once() ).method( "isPasswordExpired" ).with( same( user ) ).will( returnValue( Boolean.FALSE ) );

    mockProxyService.expects( once() ).method( "getUsersByProxyUserId" ).with( eq( user.getId() ) ).will( returnValue( delegates ) );

    mockMainContentService.expects( once() ).method( "buildEligiblePromoList" ).will( returnValue( new ArrayList<>() ) );

    AuthenticatedUser authenticatedUser = authenticationService.authenticate( user.getUserName(), user.getPassword() );
    try
    {
      List expectedList = promotionServiceImpl.getPaxPromoRules( authenticatedUser );
      logger.info( "expectedList: " + expectedList );
    }
    catch( Exception e )
    {
      logger.error( e.getMessage(), e );
    }
  }

  public void testGetInProgressNominationClaimsCount()
  {
    List<AlertsValueBean> alertsList = buildAlertsValueBeanList();
    int cnt = 2;
    mockClaimService.expects( atLeastOnce() ).method( "getClaimListCount" ).will( returnValue( cnt ) );

    List eligiblePromotions = Lists.newArrayList();
    PromotionMenuBean promotionMenuBean1 = new PromotionMenuBean();
    Promotion promotion1 = new NominationPromotion();
    promotion1.setName( "name" );
    promotion1.setPromotionType( PromotionType.lookup( PromotionType.NOMINATION ) );
    promotion1.setSubmissionEndDate( new Date() );
    promotionMenuBean1.setPromotion( promotion1 );
    eligiblePromotions.add( promotionMenuBean1 );

    PromotionMenuBean promotionMenuBean2 = new PromotionMenuBean();
    Promotion promotion2 = new NominationPromotion();
    promotion2.setName( "name" );
    promotion2.setPromotionType( PromotionType.lookup( PromotionType.NOMINATION ) );
    promotion2.setSubmissionEndDate( java.sql.Date.valueOf( LocalDate.now().minusDays( 1 ) ) );
    promotionMenuBean2.setPromotion( promotion2 );
    eligiblePromotions.add( promotionMenuBean2 );

    PromotionMenuBean promotionMenuBean3 = new PromotionMenuBean();
    Promotion promotion3 = new NominationPromotion();
    promotion3.setName( "name" );
    promotion3.setPromotionType( PromotionType.lookup( PromotionType.NOMINATION ) );
    promotion3.setSubmissionEndDate( java.sql.Date.valueOf( LocalDate.now().plusDays( 1 ) ) );
    promotionMenuBean3.setPromotion( promotion3 );
    eligiblePromotions.add( promotionMenuBean3 );

    PromotionMenuBean promotionMenuBean4 = new PromotionMenuBean();
    Promotion recogPromo = new RecognitionPromotion();
    recogPromo.setName( "name" );
    recogPromo.setPromotionType( PromotionType.lookup( PromotionType.RECOGNITION ) );
    recogPromo.setSubmissionEndDate( new Date() );
    promotionMenuBean4.setPromotion( recogPromo );
    eligiblePromotions.add( promotionMenuBean4 );

    Participant user = new Participant();
    user.setId( new Long( 60020 ) );
    user.setUserName( "BHD-070" );
    user.setActive( Boolean.TRUE );
    user.setLanguageType( LanguageType.lookup( "en_US" ) );

    final String PASSWORD = "password";
    user.setPassword( PASSWORD );

    user.setLoginFailuresCount( new Integer( 0 ) );
    user.setUserType( UserType.lookup( UserType.PARTICIPANT ) );
    String country = "en_US";
    List actualResult = promotionServiceImpl.getInProgressNominationClaimsCount( eligiblePromotions, user );

    AlertsValueBean avb = (AlertsValueBean)actualResult.get( 0 );
    assertNotNull( "The saved nomination is null", actualResult );
    assertTrue( "The saved nomination list is empty", !actualResult.isEmpty() );
    assertNotNull( "The saved nomination VALUE is null", avb.getPaxNomineeInCompleteSubmissions() );
  }

  private List<AlertsValueBean> buildAlertsValueBeanList()
  {
    List<AlertsValueBean> beans = new ArrayList<AlertsValueBean>();

    AlertsValueBean alertsVB = new AlertsValueBean();

    alertsVB.setActivityType( ActivityType.NOMINATION_PAX_SAVED_SUBMISSIONS );
    alertsVB.setPaxNomineeInCompleteSubmissions( 1 );

    beans.add( alertsVB );
    return beans;
  }

  public void testGetCelebrationImageFillersForPromotion()
  {
    RecognitionPromotion promotion = buildRecognitionPromotion();
    promotion.setIncludeCelebrations( true );
    promotion.setFillerImg1AwardNumberEnabled( true );
    promotion.setFillerImg2AwardNumberEnabled( true );
    promotion.setFillerImg1AwardNumberEnabled( false );
    promotion.setFillerImg1AwardNumberEnabled( false );
    promotion.setFillerImg1AwardNumberEnabled( false );

    List<CelebrationImageFillerValue> expectedImageList = new ArrayList<CelebrationImageFillerValue>();
    CelebrationImageFillerValue fillerImgValue = new CelebrationImageFillerValue();
    fillerImgValue.setImage1NumberEnabled( promotion.isFillerImg1AwardNumberEnabled() );
    fillerImgValue.setImage1Name( null );
    fillerImgValue.setImage2NumberEnabled( promotion.isFillerImg2AwardNumberEnabled() );
    fillerImgValue.setImage2Name( null );
    fillerImgValue.setImage3NumberEnabled( promotion.isFillerImg3AwardNumberEnabled() );
    fillerImgValue.setImage3Name( null );
    fillerImgValue.setImage4NumberEnabled( promotion.isFillerImg4AwardNumberEnabled() );
    fillerImgValue.setImage4Name( null );
    fillerImgValue.setImage5NumberEnabled( promotion.isFillerImg5AwardNumberEnabled() );
    fillerImgValue.setImage5Name( null );
    expectedImageList.add( fillerImgValue );

    mockPromotionDAO.expects( once() ).method( "getCelebrationImageFillersForPromotion" ).with( eq( promotion.getId() ) ).will( returnValue( expectedImageList ) );

    List<CelebrationImageFillerValue> actualImageList = promotionServiceImpl.getCelebrationImageFillersForPromotion( promotion.getId() );

    CelebrationImageFillerValue actualFillerValue = actualImageList.get( 0 );
    CelebrationImageFillerValue expectedFillerValue = expectedImageList.get( 0 );

    assertEquals( "Actual Image 1 filler validation didn't match what was expected.", expectedFillerValue.isImage1NumberEnabled(), actualFillerValue.isImage1NumberEnabled() );

    assertTrue( "Image 1 filler values are not same", expectedFillerValue.isImage1NumberEnabled() == actualFillerValue.isImage1NumberEnabled() );
  }

  /**
   * Test getting the EasyRecognitionSubmissionList.
   */
  public void testGetEasyRecognitionSubmissionList()
  {
    try
    {
      Participant pax1 = ParticipantDAOImplTest.buildUniqueParticipant( "pax1_" );
      mockMainContentService.expects( atLeastOnce() ).method( "getAllLivePromotionsFromCache" ).will( returnValue( new ArrayList<>() ) );
      mockParticipantDAO.expects( atLeastOnce() ).method( "getParticipantById" ).will( returnValue( pax1 ) );
      List easyPromotionsList = promotionServiceImpl.getEasyRecognitionSubmissionList( new Long( 5634 ), true, new Long( 5583 ) );
      logger.info( "easyPromotionsList: " + easyPromotionsList );
    }
    catch( Exception e )
    {
      logger.error( e.getMessage(), e );
    }
  }

  public void testGetPromotionNameByLocale()
  {
    String locale = "en";
    String promoNameAssetCode = "testCode";
    String expected = "testName";
    // cmAssetService.getString( promoNameAssetCode, Promotion.PROMOTION_NAME_KEY_PREFIX, locale,
    // true );
    mockCmAssetService.expects( once() ).method( "getString" ).with( eq( promoNameAssetCode ), eq( Promotion.PROMOTION_NAME_KEY_PREFIX ), eq( Locale.ENGLISH ), eq( true ) )
        .will( returnValue( expected ) );

    String actual = promotionServiceImpl.getPromotionNameByLocale( promoNameAssetCode, locale );
    mockCmAssetService.verify();
    // The method should return whatever cmAssetService.getString(...) does
    assertEquals( actual, expected );
  }

  /**
   * A crazy little helper method that uses introspection to compare two objects
   * by iterating over their get methods and boolean checks. It identifies
   * said methods by checking for prefixes of 'get' and 'is'
   * @param a
   * @param b
   * @param className
   * @return
   * @throws ClassNotFoundException
   * @throws IllegalArgumentException
   * @throws IllegalAccessException
   * @throws InvocationTargetException
   */
  @SuppressWarnings( "unchecked" )
  public static Map<String, Object[]> handyCompare( Object a, Object b, String className ) throws ClassNotFoundException, IllegalArgumentException, IllegalAccessException, InvocationTargetException
  {
    Map<String, Object[]> ret = new HashMap<String, Object[]>();
    Class<?> clazz = Class.forName( className );
    Field[] fields = clazz.getFields();
    Method[] methods = clazz.getMethods();
    Object attr1, attr2;

    for ( Method m : methods )
    {
      boolean get, is;
      get = m.getName().startsWith( "get" );
      is = m.getName().startsWith( "is" );
      if ( get || is )
      {
        // TODO: mock this in tests instead of avoiding Date getters, as they are important to some
        // tests.
        if ( m.getParameterTypes().length > 0 || m.getName().contains( "Date" ) )
        {
          continue;
        }
        attr1 = m.invoke( a, new Object[] {} );
        attr2 = m.invoke( b, new Object[] {} );
        if ( !compare( attr1, attr2, m.getReturnType() ) )
        {
          String name = m.getName();
          if ( get )
          {
            name = name.substring( 3 );
          }
          if ( is )
          {
            name = name.substring( 2 );
          }
          // convert the first character to lower case
          name = name.substring( 0, 1 ).toLowerCase().concat( name.substring( 1 ) );
          ret.put( name, new Object[] { attr1, attr2 } );
        }
      }
    }

    for ( Field f : fields )
    {
      String attrName = f.getName();
      attr1 = f.get( a );
      attr2 = f.get( b );
      if ( !compare( attr1, attr2, f.getType() ) )
      {
        String name = attrName;
        ret.put( name, new Object[] { attr1, attr2 } );
      }
    }
    return ret;
  }

  /**
   * Carry out a basic comparison of two objects, given their class type.
   * Checks for null, then does an array compare if they are arrays. Otherwise
   * just uses the equals method to compare.
   * @param attr1
   * @param attr2
   * @param clazz
   * @return
   */
  public static boolean compare( Object attr1, Object attr2, Class clazz )
  {
    if ( attr1 == null && attr2 == null )
    {
      return true;
    }
    if ( attr1 == null || attr2 == null )
    {
      return false;
    }
    else if ( clazz.isArray() )
    {
      if ( arrayCompare( (Object[])attr1, (Object[])attr2 ) )
      {
        return true;
      }
    }
    else if ( attr1.equals( attr2 ) )
    {
      return true;
    }

    return false;
  }

  public static boolean arrayCompare( Object[] a, Object[] b )
  {
    if ( a.length != b.length )
    {
      return false;
    }
    for ( int i = 0; i < a.length; i++ )
    {
      if ( a[i] != b[i] )
      {
        if ( a[i] != null && !a[i].equals( b[i] ) )
        {
          return false;
        }
      }
    }
    return true;
  }

  public void testGetCustomApproverList()
  {
    int levelId = 1;
    Long promotionId = new Long( 101 );

    List<NominationApproverValueBean> customApprovers = getCustomApproversList();
    mockPromotionDAO.expects( once() ).method( "getApproverOptions" ).with( same( levelId ), same( promotionId ) ).will( returnValue( customApprovers ) );

    List actualResult = promotionServiceImpl.getCustomApproverList( levelId, promotionId );
    assertTrue( "Actual set didn't contain expected set for getCustomApproverList", actualResult.containsAll( customApprovers ) );
    mockPromotionDAO.verify();
  }

  public void testGetCustomApproversList()
  {
    int optionId = 100;

    List<ApproverOption> options = getApproverOptions();
    List<NominationApproverValueBean> customApprovers = getCustomApproverList();
    mockPromotionDAO.expects( once() ).method( "getApproverOptions" ).with( same( optionId ) ).will( returnValue( options ) );

    List actualResult = promotionServiceImpl.getCustomApproverList( optionId );
    assertTrue( "Actual set didn't contain expected set for getCustomApproverList", actualResult.containsAll( customApprovers ) );
    mockPromotionDAO.verify();
  }

  public void testSaveLevelLabelCmText() throws UniqueConstraintViolationException
  {
    NominationPromotion expectedPromotion = buildNominationPromotion( "100" );
    String uniqueAssetName = "Asset Name";
    // String data = "data";

    /*
     * mockCmAssetService.expects( once() ).method( "getUniqueAssetCode" ) .will( returnValue(
     * uniqueAssetName ) );
     */
    mockCmAssetService.expects( once() ).method( "createOrUpdateAsset" );

    mockPromotionDAO.expects( once() ).method( "getPromotionById" ).with( same( expectedPromotion.getId() ) ).will( returnValue( expectedPromotion ) );

    try
    {
      Promotion p = promotionServiceImpl.saveLevelLabelCmText( expectedPromotion );
      Promotion p2 = promotionServiceImpl.getPromotionById( p.getId() );
    }
    catch( ServiceErrorException e )
    {
      logger.error( e.getMessage(), e );
    }
  }

  public void testSaveTimePeriodNameCmText() throws UniqueConstraintViolationException
  {
    NominationPromotion expectedPromotion = buildNominationPromotion( "100" );
    String uniqueAssetName = "Asset Name";

    mockCmAssetService.expects( once() ).method( "createOrUpdateAsset" );

    mockPromotionDAO.expects( once() ).method( "getPromotionById" ).with( same( expectedPromotion.getId() ) ).will( returnValue( expectedPromotion ) );

    try
    {
      Promotion p = promotionServiceImpl.saveTimePeriodNameCmText( expectedPromotion );
      Promotion p2 = promotionServiceImpl.getPromotionById( p.getId() );
    }
    catch( ServiceErrorException e )
    {
      logger.error( e.getMessage(), e );
    }
  }

  public void testGetNominationPromotionListForApproverFileLoad()
  {
    List<NameableBean> beans = buildNameableBeanList();
    mockPromotionDAO.expects( once() ).method( "getNominationPromotionListForApproverFileLoad" ).will( returnValue( beans ) );

    List actualResult = promotionServiceImpl.getNominationPromotionListForApproverFileLoad();
    assertTrue( "Actual set didn't contain expected set for getCustomApproverList", actualResult.containsAll( beans ) );
    mockPromotionDAO.verify();
  }

  public void testGetApprovalOptionsByApproverId()
  {
    Long approverId = 501L;

    List<ApproverOption> options = getApproverOptions();
    mockPromotionDAO.expects( once() ).method( "getApprovalOptionsByApproverId" ).with( same( approverId ) ).will( returnValue( options ) );

    List actualResult = promotionServiceImpl.getApprovalOptionsByApproverId( approverId );
    assertTrue( "Actual set didn't contain expected set for getCustomApproverList", actualResult.containsAll( options ) );
    mockPromotionDAO.verify();
  }

  public void testGetPromotionIdsForBehaviorList()
  {
    String behaviorType = "Asset Name";

    List<Long> behaviors = getBehaviorList();
    mockPromotionDAO.expects( once() ).method( "getPromotionIdsForBehavior" ).with( same( behaviorType ) ).will( returnValue( behaviors ) );

    List actualResult = promotionServiceImpl.getPromotionIdsForBehavior( behaviorType );
    assertTrue( "Actual set didn't contain expected set for getCustomApproverList", actualResult.containsAll( behaviors ) );
    mockPromotionDAO.verify();
  }

  public static NominationPromotion buildNominationPromotion( String suffix )
  {
    return PromotionDAOImplTest.buildNominationPromotion( suffix );
  }

  public void testInstantPollAlerts()
  {
    User user = new User();
    user.setId( new Long( 60020 ) );
    user.setUserName( "BHD-070" );
    user.setActive( Boolean.TRUE );
    user.setLanguageType( LanguageType.lookup( "en_US" ) );
    user.setPassword( "password" );
    user.setLoginFailuresCount( new Integer( 0 ) );
    user.setUserType( UserType.lookup( UserType.PARTICIPANT ) );

    List<InstantPoll> instantPolls = new ArrayList<InstantPoll>();

    InstantPoll instantPoll1 = new InstantPoll();
    instantPoll1.setId( 1L );
    instantPoll1.setSubmissionStartDate( new DateTime( new Date() ).minusDays( 10 ).toDate() );
    instantPoll1.setSubmissionEndDate( new DateTime( new Date() ).minusDays( 9 ).toDate() );

    InstantPoll instantPoll2 = new InstantPoll();
    instantPoll2.setId( 2L );
    instantPoll2.setSubmissionStartDate( new DateTime( new Date() ).minusDays( 6 ).toDate() );
    instantPoll2.setSubmissionEndDate( new DateTime( new Date() ).minusDays( 5 ).toDate() );

    InstantPoll instantPoll3 = new InstantPoll();
    instantPoll3.setId( 3L );
    instantPoll3.setSubmissionStartDate( new DateTime( new Date() ).minusDays( 8 ).toDate() );
    instantPoll3.setSubmissionEndDate( new DateTime( new Date() ).minusDays( 7 ).toDate() );

    instantPolls.add( instantPoll1 );
    instantPolls.add( instantPoll2 );
    instantPolls.add( instantPoll3 );

    mockInstantPollService.expects( once() ).method( "getInstantPollsForTileDisplay" ).with( same( user.getId() ), isA( AssociationRequestCollection.class ) ).will( returnValue( instantPolls ) );

    List<AlertsValueBean> actualResult = promotionServiceImpl.getInstantPollForAlerts( user );
    assertEquals( "Instant poll Alerts size .", 3, actualResult.size() );

    mockPromotionDAO.verify();

  }

  public void testCelebrationAlerts()
  {
    User user = new User();
    user.setId( new Long( 60020 ) );
    user.setUserName( "BHD-070" );
    user.setActive( Boolean.TRUE );
    user.setLanguageType( LanguageType.lookup( "en_US" ) );
    user.setPassword( "password" );
    user.setLoginFailuresCount( new Integer( 0 ) );
    user.setUserType( UserType.lookup( UserType.PARTICIPANT ) );

    List<Long> claimIds = new ArrayList<>();
    claimIds.add( new Long( 1000 ) );

    RecognitionClaim recognitionClaim = new RecognitionClaim();
    RecognitionPromotion recognitionPromotion = new RecognitionPromotion();
    recognitionPromotion.setIncludeCelebrations( true );
    recognitionClaim.setPromotion( recognitionPromotion );

    mockClaimService.expects( once() ).method( "displayCelebration" ).with( same( null ), isA( Long.class ) ).will( returnValue( true ) );
    mockClaimService.expects( once() ).method( "getCelebrationClaims" ).with( same( user.getId() ) ).will( returnValue( claimIds ) );
    mockClaimService.expects( once() ).method( "getClaimByIdWithAssociations" ).with( isA( Long.class ), isA( AssociationRequestCollection.class ) ).will( returnValue( recognitionClaim ) );

    List<AlertsValueBean> actualResult = promotionServiceImpl.getCelebrationAlerts( user );
    assertEquals( "Instant poll Alerts size .", ActivityType.CELEBRATION_PAX_ALERT, actualResult.get( 0 ).getActivityType() );

    mockPromotionDAO.verify();

  }

  public void testAlerts()
  {
    Participant participant1 = PromotionDAOImplTest.buildParticipant( 1 );
    PaxAudience primaryPaxAudience = new PaxAudience();

    primaryPaxAudience.setId( new Long( 1 ) );
    primaryPaxAudience.setName( String.valueOf( 1 ) );
    primaryPaxAudience.addParticipant( participant1 );

    mockParticipantService.expects( once() ).method( "getParticipantById" ).with( isA( Long.class ) ).will( returnValue( participant1 ) );

    List eligiblePromotions = Lists.newArrayList();
    PromotionMenuBean promotionMenuBean1 = new PromotionMenuBean();
    Promotion promotion = new RecognitionPromotion();
    promotion.setName( "name" );
    promotionMenuBean1.setPromotion( promotion );
    eligiblePromotions.add( promotionMenuBean1 );

    PropertySetItem propertySetItem = new PropertySetItem();
    propertySetItem.setBooleanVal( true );
    propertySetItem.setEntityName( "entityname" );
    propertySetItem.setKey( "key" );

    mockSystemVariableService.expects( atLeastOnce() ).method( "getPropertyByName" ).with( isA( String.class ) ).will( returnValue( propertySetItem ) );

    mockAuthorizationService.expects( atLeastOnce() ).method( "isUserInRole" ).with( isA( String.class ) ).will( returnValue( true ) );

    Callable<List<AlertsValueBean>> task = new Callable<List<AlertsValueBean>>()
    {
      @Override
      public List<AlertsValueBean> call()
      {
        List<AlertsValueBean> alertsValueBeans = Lists.newArrayList();
        AlertsValueBean alertsValueBean = new AlertsValueBean();
        alertsValueBean.setBatchId( 1000L );
        alertsValueBeans.add( alertsValueBean );
        return alertsValueBeans;
      }
    };

    PromotionServiceMockTest test = new PromotionServiceMockTest();
    test.setParticipantService( (ParticipantService)mockParticipantService.proxy() );
    test.setSystemVariableService( (SystemVariableService)mockSystemVariableService.proxy() );
    test.setAuthorizationService( (AuthorizationService)mockAuthorizationService.proxy() );

    user.setUserId( 1L );
    List<AlertsValueBean> alertsValueBeans = test.getAlertsList( user, user.getUserId(), false, eligiblePromotions, true );

    assertEquals( "Alerts size .", 18, alertsValueBeans.size() );
  }

  class PromotionServiceMockTest extends PromotionServiceImpl
  {
    @Override
    public Callable<List<AlertsValueBean>> createLiveOrExpiredPromotions( List eligiblePromotions, Participant participant, boolean isMessagesPage )
    {
      Callable<List<AlertsValueBean>> c = () ->
      {
        AlertsValueBean alertsValueBean = new AlertsValueBean();
        alertsValueBean.setBatchId( 1L );
        List list = new ArrayList<AlertsValueBean>();
        list.add( alertsValueBean );
        return list;
      };
      return c;
    }

    @Override
    public Callable<List<AlertsValueBean>> createGQPromotionAlerts( List eligiblePromotions, Participant participant )
    {
      Callable<List<AlertsValueBean>> c = () ->
      {
        AlertsValueBean alertsValueBean = new AlertsValueBean();
        alertsValueBean.setBatchId( 2L );
        List list = new ArrayList<AlertsValueBean>();
        list.add( alertsValueBean );
        return list;
      };
      return c;
    }

    @Override
    public Callable<List<AlertsValueBean>> createGQPromotionSurveyAlerts( List eligiblePromotions, Participant participant )
    {
      Callable<List<AlertsValueBean>> c = () ->
      {
        AlertsValueBean alertsValueBean = new AlertsValueBean();
        alertsValueBean.setBatchId( 3L );
        List list = new ArrayList<AlertsValueBean>();
        list.add( alertsValueBean );
        return list;
      };
      return c;
    }

    @Override
    public Callable<List<AlertsValueBean>> createSurveyPromotionAlerts( List eligiblePromotions, Participant participant )
    {
      Callable<List<AlertsValueBean>> c = () ->
      {
        AlertsValueBean alertsValueBean = new AlertsValueBean();
        alertsValueBean.setBatchId( 4L );
        List list = new ArrayList<AlertsValueBean>();
        list.add( alertsValueBean );
        return list;
      };
      return c;
    }

    @Override
    public Callable<List<AlertsValueBean>> createQuizPromotionsForAlers( List eligiblePromotions, Participant participant )
    {
      Callable<List<AlertsValueBean>> c = () ->
      {
        AlertsValueBean alertsValueBean = new AlertsValueBean();
        alertsValueBean.setBatchId( 5L );
        List list = new ArrayList<AlertsValueBean>();
        list.add( alertsValueBean );
        return list;
      };
      return c;
    }

    @Override
    public Callable<List<AlertsValueBean>> createAwardGeneratorAlertForManger( List eligiblePromotions, Participant participant )
    {
      Callable<List<AlertsValueBean>> c = () ->
      {
        AlertsValueBean alertsValueBean = new AlertsValueBean();
        alertsValueBean.setBatchId( 6L );
        List list = new ArrayList<AlertsValueBean>();
        list.add( alertsValueBean );
        return list;
      };
      return c;
    }

    @Override
    public Callable<List<AlertsValueBean>> createCelebrationAlertForManager( List eligiblePromotions, Participant participant )
    {
      Callable<List<AlertsValueBean>> c = () ->
      {
        AlertsValueBean alertsValueBean = new AlertsValueBean();
        alertsValueBean.setBatchId( 7L );
        List list = new ArrayList<AlertsValueBean>();
        list.add( alertsValueBean );
        return list;
      };
      return c;
    }

    @Override
    public Callable<List<AlertsValueBean>> createApproveReminderAlerts( List eligiblePromotions, Participant participant )
    {
      Callable<List<AlertsValueBean>> c = () ->
      {
        AlertsValueBean alertsValueBean = new AlertsValueBean();
        alertsValueBean.setBatchId( 8L );
        List list = new ArrayList<AlertsValueBean>();
        list.add( alertsValueBean );
        return list;
      };
      return c;
    }

    @Override
    public Callable<List<AlertsValueBean>> createNominatorRequestMoreInfoAlerts( Participant participant )
    {
      Callable<List<AlertsValueBean>> c = () ->
      {
        AlertsValueBean alertsValueBean = new AlertsValueBean();
        alertsValueBean.setBatchId( 9L );
        List list = new ArrayList<AlertsValueBean>();
        list.add( alertsValueBean );
        return list;
      };
      return c;
    }

    // Alerts Performance Tuning
    @Override
    public Callable<List<AlertsValueBean>> createAwardReminderListForAlerts( List eligiblePromotions, Participant participant, Map<Long, Promotion> eligiblePromotionsMap )
    {
      Callable<List<AlertsValueBean>> c = () ->
      {
        AlertsValueBean alertsValueBean = new AlertsValueBean();
        alertsValueBean.setBatchId( 10L );
        List list = new ArrayList<AlertsValueBean>();
        list.add( alertsValueBean );
        return list;
      };
      return c;
    }

    @Override
    public Callable<List<AlertsValueBean>> createBudgetEndAlertForAlerts( List eligiblePromotions, Participant participant )
    {
      Callable<List<AlertsValueBean>> c = () ->
      {
        AlertsValueBean alertsValueBean = new AlertsValueBean();
        alertsValueBean.setBatchId( 11L );
        List list = new ArrayList<AlertsValueBean>();
        list.add( alertsValueBean );
        return list;
      };
      return c;
    }

    @Override
    public Callable<List<AlertsValueBean>> createPendingPurlInvitationsForAlerts( Participant participant )
    {
      Callable<List<AlertsValueBean>> c = () ->
      {
        AlertsValueBean alertsValueBean = new AlertsValueBean();
        alertsValueBean.setBatchId( 12L );
        List list = new ArrayList<AlertsValueBean>();
        list.add( alertsValueBean );
        return list;
      };
      return c;
    }

    @Override
    public Callable<List<AlertsValueBean>> createPendingPurlContributionsForAlerts( Participant participant, boolean isModalWindow )
    {
      Callable<List<AlertsValueBean>> c = () ->
      {
        AlertsValueBean alertsValueBean = new AlertsValueBean();
        alertsValueBean.setBatchId( 13L );
        List list = new ArrayList<AlertsValueBean>();
        list.add( alertsValueBean );
        return list;
      };
      return c;
    }

    @Override
    public Callable<List<AlertsValueBean>> createPendingViewPurlsForAlerts( Participant participant )
    {
      Callable<List<AlertsValueBean>> c = () ->
      {
        AlertsValueBean alertsValueBean = new AlertsValueBean();
        alertsValueBean.setBatchId( 14L );
        List list = new ArrayList<AlertsValueBean>();
        list.add( alertsValueBean );
        return list;
      };
      return c;
    }

    @Override
    public Callable<List<AlertsValueBean>> createPendingFileDownloadsForAlerts( Participant participant )
    {
      Callable<List<AlertsValueBean>> c = () ->
      {
        AlertsValueBean alertsValueBean = new AlertsValueBean();
        alertsValueBean.setBatchId( 15L );
        List list = new ArrayList<AlertsValueBean>();
        list.add( alertsValueBean );
        return list;
      };
      return c;
    }

    @Override
    public Callable<List<AlertsValueBean>> createInstantPollForAlerts( Participant participant )
    {
      Callable<List<AlertsValueBean>> c = () ->
      {
        AlertsValueBean alertsValueBean = new AlertsValueBean();
        alertsValueBean.setBatchId( 16L );
        List list = new ArrayList<AlertsValueBean>();
        list.add( alertsValueBean );
        return list;
      };
      return c;
    }

    @Override
    public Callable<List<AlertsValueBean>> createCelebrationAlerts( Participant participant )
    {
      Callable<List<AlertsValueBean>> c = () ->
      {
        AlertsValueBean alertsValueBean = new AlertsValueBean();
        alertsValueBean.setBatchId( 17L );
        List list = new ArrayList<AlertsValueBean>();
        list.add( alertsValueBean );
        return list;
      };
      return c;
    }

    @Override
    public Callable<List<AlertsValueBean>> createInProgressNominationClaimsCount( List eligiblePromotions, Participant participant )
    {
      Callable<List<AlertsValueBean>> c = () ->
      {
        AlertsValueBean alertsValueBean = new AlertsValueBean();
        alertsValueBean.setBatchId( 18L );
        List list = new ArrayList<AlertsValueBean>();
        list.add( alertsValueBean );
        return list;
      };
      return c;
    }

  }

  private List<NominationApproverValueBean> getCustomApproversList()
  {
    List<NominationApproverValueBean> approvers = new ArrayList<NominationApproverValueBean>();

    NominationApproverValueBean approver1 = new NominationApproverValueBean();
    approver1.setUsername( "USERNAMETESTPAX1" );
    approver1.setFirstname( "firstnameTestPax1" );
    approver1.setLastname( "lastnameTestPax1" );
    approver1.setApproverType( CustomApproverType.AWARD );
    approvers.add( approver1 );

    NominationApproverValueBean approver2 = new NominationApproverValueBean();
    approver2.setUsername( "BHD-071" );
    approver2.setFirstname( "Bob" );
    approver2.setLastname( "Reagan" );
    approver2.setApproverType( CustomApproverType.AWARD );
    approvers.add( approver2 );

    return approvers;
  }

  private List<NominationApproverValueBean> getCustomApproverList()
  {
    List<NominationApproverValueBean> approvers = new ArrayList<NominationApproverValueBean>();

    NominationApproverValueBean approver1 = new NominationApproverValueBean();
    approver1.setUsername( "USERNAMETESTPAX1" );
    approver1.setFirstname( "firstnameTestPax1" );
    approver1.setLastname( "lastnameTestPax1" );
    approver1.setApproverType( CustomApproverType.AWARD );
    approver1.setApproverValue( "1-50" );
    approvers.add( approver1 );

    return approvers;
  }

  private List<ApproverOption> getApproverOptions()
  {
    List<ApproverOption> options = new ArrayList<ApproverOption>();

    ApproverOption option1 = new ApproverOption();
    option1.setId( 100L );
    option1.setApprovalLevel( 1L );
    option1.setNominationPromotion( buildNominationPromotion( "test1" ) );
    option1.setApproverType( CustomApproverType.lookup( CustomApproverType.AWARD ) );

    Set<ApproverCriteria> criteriaSet = new HashSet<ApproverCriteria>();
    ApproverCriteria criteria = new ApproverCriteria();
    criteria.setApproverOption( option1 );
    criteria.setId( 101L );
    criteria.setMaxVal( 50 );
    criteria.setMinVal( 1 );
    criteria.setApproverValue( "1-50" );

    Set<Approver> approvers = new HashSet<Approver>();
    Participant pax1 = ParticipantDAOImplTest.buildUniqueParticipant( "TestPax1" );
    Approver approver = new Approver();
    approver.setParticipant( pax1 );
    approver.setId( 501L );
    approver.setApproverCriteria( criteria );
    approvers.add( approver );

    criteria.setApprovers( approvers );
    criteriaSet.add( criteria );
    option1.setApproverCriteria( criteriaSet );
    options.add( option1 );

    return options;
  }

  private List<NameableBean> buildNameableBeanList()
  {
    List<NameableBean> beans = new ArrayList<NameableBean>();

    NameableBean bean1 = new NameableBean( 1L, "Bean1" );
    beans.add( bean1 );

    NameableBean bean2 = new NameableBean( 2L, "Bean2" );
    beans.add( bean2 );

    return beans;
  }

  private List<Long> getBehaviorList()
  {
    List<Long> returnList = new ArrayList<Long>();
    returnList.add( 101L );
    returnList.add( 102L );
    returnList.add( 103L );

    return returnList;

  }

  public void testGetAllUniqueBillCodes()
  {
    List<String> billCodes = new ArrayList<String>();
    mockPromotionDAO.expects( once() ).method( "getAllUniqueBillCodes" ).will( returnValue( billCodes ) );
    List<String> actualBillCodes = promotionServiceImpl.getAllUniqueBillCodes( 2419L );
    assertNotNull( actualBillCodes );

    billCodes = new ArrayList<String>();
    billCodes.add( "TestBillCodeOne" );
    mockPromotionDAO.expects( once() ).method( "getAllUniqueBillCodes" ).will( returnValue( billCodes ) );
    List<String> actBillCodes = promotionServiceImpl.getAllUniqueBillCodes( 2419L );
    assertNotNull( actBillCodes );
    assertEquals( actBillCodes.size(), 1 );
    mockPromotionDAO.verify();
  }

  public void testGetClaimAwardQuantity()
  {
    Long claimId = new Long( 10021 );
    Long awardQuantity = new Long( 100 );
    mockPromotionDAO.expects( once() ).method( "getClaimAwardQuantity" ).with( same( claimId ) ).will( returnValue( awardQuantity ) );

    Long actualResult = promotionServiceImpl.getClaimAwardQuantity( claimId );
    assertNotNull( actualResult );
    assertTrue( "Award Quantity Found", actualResult != null );
    mockPromotionDAO.verify();
  }

  public void testGetTotalUnapprovedAwardQuantity()
  {
    Long promotionId = new Long( 10021 );
    Long userId = new Long( 100 );
    Long nodeId = new Long( 100 );
    Long budgetMasterId = new Long( 5040 );

    mockPromotionDAO.expects( once() ).method( "getTotalUnapprovedAwardQuantity" ).will( returnValue( new BigDecimal( 100 ) ) );

    BigDecimal d = promotionServiceImpl.getTotalUnapprovedAwardQuantity( promotionId, userId, nodeId, budgetMasterId );
    assertNotNull( d );
    mockPromotionDAO.verify();

  }

  public void testGetTotalUnapprovedAwardQuantityPurl()
  {
    Long promotionId = new Long( 10021 );
    Long userId = new Long( 100 );
    Long nodeId = new Long( 100 );
    Long budgetMasterId = new Long( 5040 );
    Long awardQuantity = new Long( 200 );

    mockPromotionDAO.expects( once() ).method( "getTotalUnapprovedAwardQuantityPurl" ).will( returnValue( new BigDecimal( 200 ) ) );

    BigDecimal d = promotionServiceImpl.getTotalUnapprovedAwardQuantityPurl( promotionId, userId, nodeId, budgetMasterId );
    assertNotNull( d );
    mockPromotionDAO.verify();

  }

  public void testGetTotalImportPaxAwardQuantity()
  {
    Long importFileId = new Long( 10021 );
    Long awardQuantity = new Long( 100 );
    mockPromotionDAO.expects( once() ).method( "getTotalImportPaxAwardQuantity" ).with( same( importFileId ) ).will( returnValue( awardQuantity ) );

    Long actualResult = promotionServiceImpl.getTotalImportPaxAwardQuantity( importFileId );
    assertNotNull( actualResult );
    mockPromotionDAO.verify();
  }

}

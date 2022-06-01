
package com.biperf.integration.service.promotion;

import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.annotation.Resource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.biperf.core.dao.calculator.CalculatorDAO;
import com.biperf.core.dao.claim.ClaimFormDAO;
import com.biperf.core.dao.claim.hibernate.ClaimDAOImplTest;
import com.biperf.core.dao.promotion.PromotionDAO;
import com.biperf.core.dao.promotion.hibernate.PromotionDAOImplTest;
import com.biperf.core.domain.claim.ClaimForm;
import com.biperf.core.domain.claim.ClaimRecipient;
import com.biperf.core.domain.claim.NominationClaim;
import com.biperf.core.domain.enums.ApprovalConditionalAmmountOperatorType;
import com.biperf.core.domain.enums.ClaimFormStatusType;
import com.biperf.core.domain.enums.NominationAwardGroupType;
import com.biperf.core.domain.enums.NominationClaimStatusType;
import com.biperf.core.domain.enums.NominationEvaluationType;
import com.biperf.core.domain.enums.PromotionAwardsType;
import com.biperf.core.domain.enums.PromotionStatusType;
import com.biperf.core.domain.enums.PromotionType;
import com.biperf.core.domain.hierarchy.Node;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.NominationPromotion;
import com.biperf.core.domain.promotion.NominationPromotionLevel;
import com.biperf.core.domain.promotion.NominationPromotionTimePeriod;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.user.UserNode;
import com.biperf.core.service.claim.NominationClaimService;
import com.biperf.core.service.maincontent.MainContentService;
import com.biperf.core.service.oracle.OracleSequenceService;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.promotion.NominationPromotionService;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.service.promotion.impl.NominationPromotionServiceImpl;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.StringUtil;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.PromotionMenuBean;
import com.biperf.core.value.nomination.EligibleNominationPromotionValueObject;
import com.biperf.core.value.nomination.NominationSubmitDataPromotionValueBean;
import com.biperf.core.value.promo.PromoAwards;
import com.biperf.core.value.promo.PromotionBasics;
import com.biperf.integration.BaseIntegrationTest;

public class NominationPromotionServiceIntegTest extends BaseIntegrationTest
{

  MainContentService mockMainContentService = createNiceMock( MainContentService.class );

  @Resource
  NominationPromotionService nominationPromotionService;

  @Resource
  PromotionService promotionService;
  @Resource
  ParticipantService participantService;
  @Resource
  OracleSequenceService oracleSequenceService;

  @Resource
  NominationClaimService nominationClaimService;

  @Resource
  CalculatorDAO calcDao;

  @Resource
  PromotionDAO promoDao;

  @Resource
  ClaimFormDAO claimFormDao;

  List<Long> promoIds = new ArrayList<Long>();

  @Before
  public void beforeEach() throws Exception
  {

  }

  @After
  public void afterEach() throws Exception
  {
    for ( Long id : promoIds )
    {
      promotionService.deletePromotion( id );
    }
  }

  /**
   * Test obtaining the object that'll represent all of the JSON data needed when going to the nomination submission wizard 
   * for a new submission.
   */
  @Test
  public void getNewNominationForSubmission() throws Exception
  {
    String name = "test_promo_getForSubmission";

    // First build up a promotion object to save
    NominationPromotion promotion = PromotionDAOImplTest.buildNominationPromotion( "nom-for-submission" );
    promotion.setAwardGroupType( NominationAwardGroupType.lookup( NominationAwardGroupType.INDIVIDUAL_OR_TEAM ) );
    promotion.setAwardActive( true );
    promotion.setBehaviorActive( false );
    promotion.setCardActive( true );
    Promotion promo = promotionService.savePromotion( promotion );
    promo = promotionService.savePromoNameCmText( promotion, name, Locale.US, true );

    // Call method under test to retrieve data
    NominationSubmitDataPromotionValueBean result = nominationPromotionService.getNominationForSubmission( promo.getId(), "en_US" );

    assertEquals( promo.getId(), result.getId() );
    assertNotNull( result.getName() );
    assertEquals( NominationAwardGroupType.INDIVIDUAL_OR_TEAM, result.getIndividualOrTeam() );
    assertTrue( result.isAwardsActive() );
    assertFalse( result.isBehaviorsActive() );
    assertTrue( result.iseCardsActive() );
    promoIds.add( promo.getId() );
  }

  @Test
  public void saveNomPromoWithTimePeriodAndVerify() throws Exception
  {

    String timePeriodName = "timePeriodName";
    Date timePeriodStartDate = DateUtils.getCurrentDateTrimmed();
    Date timePeriodEndDate = DateUtils.getCurrentDateTrimmed();

    NominationPromotion expectedPromo = PromotionDAOImplTest.buildNominationPromotion( "-IntegTest" );
    NominationPromotionTimePeriod expectedTimePeriod = PromotionDAOImplTest.buildNomPromoPeriod( 10, timePeriodName, timePeriodStartDate, timePeriodEndDate, expectedPromo );
    PromoAwards awards = PromotionDAOImplTest.buidNomPromoAwards( Arrays.asList( expectedTimePeriod ) );
    PromotionDAOImplTest.populateNomPromoAwards( expectedPromo, awards );
    expectedPromo.setAwardAmountTypeFixed( false );
    expectedPromo.setAwardAmountFixed( 1L );
    promotionService.savePromotion( expectedPromo );

    Promotion actualPromo = promotionService.getPromotionById( expectedPromo.getId() );
    assertNotNull( actualPromo );
    Set<NominationPromotionTimePeriod> timePeriodList = nominationPromotionService.getPromotionsTimePeriods( actualPromo.getId() );
    assertTrue( timePeriodList.size() == 1 );
    for ( NominationPromotionTimePeriod actualPeriod : timePeriodList )
    {
      assertTrue( actualPeriod.getMaxSubmissionAllowed() == expectedTimePeriod.getMaxSubmissionAllowed() );
      assertTrue( actualPeriod.getTimePeriodStartDate().getTime() == ( (Date)expectedTimePeriod.getTimePeriodStartDate() ).getTime() );
      assertTrue( actualPeriod.getTimePeriodEndDate().getTime() == ( (Date)expectedTimePeriod.getTimePeriodEndDate() ).getTime() );
    }

    promoIds.add( actualPromo.getId() );
    tx.commit();
  }

  @Test
  public void launchNomPromoWithTimePeriod_AndSubmitClaim_ThenVerifySubmitCount() throws Exception
  {

    List<PromotionMenuBean> menuBeanList = new ArrayList<PromotionMenuBean>();
    Date today = new Date();

    PromotionBasics basics = new PromotionBasics();
    basics.setPromoType( PromotionType.lookup( PromotionType.NOMINATION ) );
    basics.setStatusType( PromotionStatusType.lookup( PromotionStatusType.LIVE ) );
    basics.setApprovalConditionalAmountOperator( ApprovalConditionalAmmountOperatorType.lookup( ApprovalConditionalAmmountOperatorType.EQ ) );
    basics.setPromoNameAssetCode( "testAssetCode" );
    basics.setSubmissionStartDate( today );
    basics.setSubmissionEndDate( DateUtils.getDateAfterNumberOfDays( today, 365 ) );
    basics.setSelfNomination( true );
    basics.setAwardGroupType( NominationAwardGroupType.lookup( NominationAwardGroupType.INDIVIDUAL ) );
    basics.setEvaluationType( NominationEvaluationType.lookup( NominationEvaluationType.INDEPENDENT ) );

    Node submitterNode = null;
    String periodName1 = "periodName1";
    String periodName2 = "periodName2";

    Date P1_periodStartDate = DateUtils.getCurrentDateTrimmed();
    Date P1_periodEndDate = DateUtils.getDateAfterNumberOfDays( P1_periodStartDate, 1 );

    Date P2_periodStartDate = DateUtils.getDateAfterNumberOfDays( P1_periodEndDate, 1 );
    Date P2_periodEndDate = DateUtils.getDateAfterNumberOfDays( P2_periodStartDate, 1 );

    long suffix = System.currentTimeMillis();

    NominationPromotion promo1 = PromotionDAOImplTest.buildNominationPromotion( suffix + "1" );
    promo1.getClaimForm().setClaimFormStatusType( ClaimFormStatusType.lookup( ClaimFormStatusType.ASSIGNED ) );
    basics.setPromoName( System.currentTimeMillis() + "" );
    basics.setShortName( System.currentTimeMillis() + "" );
    PromotionDAOImplTest.populateNomPromoBasics( promo1, basics );
    NominationPromotionTimePeriod time_period_1 = PromotionDAOImplTest.buildNomPromoPeriod( 1, periodName1, P1_periodStartDate, P1_periodEndDate, promo1 );
    time_period_1.setNominationPromotion( promo1 );
    PromoAwards awards = PromotionDAOImplTest.buidNomPromoAwards( Arrays.asList( time_period_1 ) );
    PromotionDAOImplTest.populateNomPromoAwards( promo1, awards );
    promotionService.savePromotion( promo1 );

    NominationPromotion promo2 = PromotionDAOImplTest.buildNominationPromotion( suffix + "2" );
    promo2.getClaimForm().setClaimFormStatusType( ClaimFormStatusType.lookup( ClaimFormStatusType.ASSIGNED ) );
    basics.setPromoName( System.currentTimeMillis() + "" );
    basics.setShortName( System.currentTimeMillis() + "" );
    PromotionDAOImplTest.populateNomPromoBasics( promo2, basics );
    NominationPromotionTimePeriod time_period_2 = PromotionDAOImplTest.buildNomPromoPeriod( 1, periodName2, P2_periodStartDate, P2_periodEndDate, promo2 );
    time_period_2.setNominationPromotion( promo2 );
    PromoAwards awards2 = PromotionDAOImplTest.buidNomPromoAwards( Arrays.asList( time_period_2 ) );
    PromotionDAOImplTest.populateNomPromoAwards( promo2, awards2 );
    promotionService.savePromotion( promo2 );

    tx.commit();

    PromotionMenuBean menuBean1 = new PromotionMenuBean();
    menuBean1.setCanSubmit( true );
    menuBean1.setPromotion( promo1 );

    PromotionMenuBean menuBean2 = new PromotionMenuBean();
    menuBean2.setCanSubmit( true );
    menuBean2.setPromotion( promo2 );

    menuBeanList.add( menuBean1 );
    menuBeanList.add( menuBean2 );

    expect( mockMainContentService.buildEligiblePromoList( UserManager.getUser() ) ).andReturn( menuBeanList ).times( 1 );
    replay( mockMainContentService );

    Object bean2 = BeanLocator.getBean( NominationPromotionService.BEAN_NAME );
    if ( bean2 instanceof NominationPromotionServiceImpl )
    {
      System.out.println( "**************" );
    }

    NominationPromotionServiceImpl bean = (NominationPromotionServiceImpl)bean2;
    bean.setMainContentService( mockMainContentService );

    Participant submitter = participantService.getParticipantById( PAX_ID );
    for ( UserNode n : submitter.getUserNodes() )
    {
      submitterNode = n.getNode();
      break;
    }

    Node recipient1Node1 = getRecipinetNode( submitter );
    ClaimRecipient claimRecipient1 = buildClaimRecipient( submitter, recipient1Node1 );
    NominationClaim claim_1 = buildNominationClaim( promo1, time_period_1, submitter, submitterNode, claimRecipient1 );

    nominationClaimService.saveNominationClaim( claim_1 );

    List<EligibleNominationPromotionValueObject> eligibleNomPromotions = nominationPromotionService.getEligibleNomPromotions( UserManager.getUser() );
    assertNotNull( eligibleNomPromotions );
    assertTrue( eligibleNomPromotions.size() == 2 );

    verify( mockMainContentService );

    promoIds.add( promo1.getId() );
    promoIds.add( promo2.getId() );
    tx.commit();

  }

  @Test
  public void createPromoWithApprovalLevel() throws Exception
  {
    Date today = new Date();
    PromotionAwardsType payout = PromotionAwardsType.lookup( PromotionAwardsType.POINTS );

    ClaimForm claimForm = claimFormDao.getClaimFormById( 5000L );

    NominationPromotionLevel level = getNominationPromoLevel( new BigDecimal( 10L ), new BigDecimal( 100L ), 1L, payout, false, null );

    PromotionBasics basics = new PromotionBasics();
    basics.setPromoType( PromotionType.lookup( PromotionType.NOMINATION ) );
    basics.setStatusType( PromotionStatusType.lookup( PromotionStatusType.LIVE ) );
    basics.setApprovalConditionalAmountOperator( ApprovalConditionalAmmountOperatorType.lookup( ApprovalConditionalAmmountOperatorType.EQ ) );
    basics.setPromoNameAssetCode( "testAssetCode" );
    basics.setSubmissionStartDate( today );
    basics.setSubmissionEndDate( DateUtils.getDateAfterNumberOfDays( today, 365 ) );
    basics.setSelfNomination( true );
    basics.setAwardGroupType( NominationAwardGroupType.lookup( NominationAwardGroupType.INDIVIDUAL ) );
    basics.setEvaluationType( NominationEvaluationType.lookup( NominationEvaluationType.INDEPENDENT ) );

    String periodName1 = "periodName1";

    Date P1_periodStartDate = DateUtils.getCurrentDateTrimmed();
    Date P1_periodEndDate = DateUtils.getDateAfterNumberOfDays( P1_periodStartDate, 1 );

    long suffix = System.currentTimeMillis();

    NominationPromotion promo1 = PromotionDAOImplTest.buildNominationPromotion( suffix + "1" );
    promo1.getClaimForm().setClaimFormStatusType( ClaimFormStatusType.lookup( ClaimFormStatusType.ASSIGNED ) );

    basics.setPromoName( "promoWithCalc" + "-" + System.currentTimeMillis() );
    basics.setShortName( "promoWithCalc" + "-" + System.currentTimeMillis() );

    PromotionDAOImplTest.populateNomPromoBasics( promo1, basics );

    NominationPromotionTimePeriod time_period_1 = PromotionDAOImplTest.buildNomPromoPeriod( 1, periodName1, P1_periodStartDate, P1_periodEndDate, promo1 );
    time_period_1.setNominationPromotion( promo1 );
    PromoAwards awards = PromotionDAOImplTest.buidNomPromoAwards( Arrays.asList( time_period_1 ) );
    PromotionDAOImplTest.populateNomPromoAwards( promo1, awards );
    promo1.setAwardActive( true );
    promo1.setAwardType( PromotionAwardsType.lookup( PromotionAwardsType.POINTS ) );
    promo1.setAwardAmountMin( 10L );
    promo1.setAwardAmountMax( 100L );

    promo1.setNominationLevels( new LinkedHashSet<NominationPromotionLevel>( Arrays.asList( level ) ) );

    level.setNominationPromotion( promo1 );

    promo1.setClaimForm( claimForm );

    promoDao.save( promo1 );

    tx.commit();
  }

  @Test
  public void abc() throws Exception
  {
    PromotionAwardsType payout = PromotionAwardsType.lookup( PromotionAwardsType.POINTS );
    NominationPromotionLevel level = getNominationPromoLevel( new BigDecimal( 10L ), new BigDecimal( 100L ), 1L, payout, false, null );
    NominationPromotion p = (NominationPromotion)promoDao.getPromotionById( 258L );
    p.addNominationLevels( level );
    promoDao.save( p );
    tx.commit();
  }

  private NominationPromotionLevel getNominationPromoLevel( BigDecimal min, BigDecimal max, Long index, PromotionAwardsType payout, boolean isfixed, BigDecimal fixedAmt )
  {
    NominationPromotionLevel level = new NominationPromotionLevel();

    level.setAwardPayoutType( payout );
    level.setNominationAwardAmountTypeFixed( isfixed );
    level.setNominationAwardAmountFixed( StringUtil.convertStringToDecimal( String.valueOf( fixedAmt ) ) );
    level.setNominationAwardAmountMin( StringUtil.convertStringToDecimal( String.valueOf( min ) ) );
    level.setNominationAwardAmountMax( StringUtil.convertStringToDecimal( String.valueOf( max ) ) );

    return level;
  }

  private NominationClaim buildNominationClaim( NominationPromotion promo1, NominationPromotionTimePeriod time_period_1, Participant submitter, Node node, ClaimRecipient claimRecipient1 )
  {
    NominationClaim claim_1 = ClaimDAOImplTest.buildStaticNominationClaim( true );
    claim_1.setNominationStatusType( NominationClaimStatusType.lookup( NominationClaimStatusType.COMPLETE ) );
    claim_1.setTimPeriod( time_period_1 );
    claim_1.setSubmitter( submitter );
    claim_1.setPromotion( promo1 );
    claim_1.setNode( node );
    claim_1.setSubmissionDate( new Date() );
    claim_1.setClaimNumber( new Long( oracleSequenceService.getOracleSequenceNextValue( "claim_nbr_sq" ) ).toString() );
    claim_1.addClaimRecipient( claimRecipient1 );
    return claim_1;
  }

  @SuppressWarnings( "rawtypes" )
  private Node getRecipinetNode( Participant recipient1 )
  {
    Set userNodes1 = recipient1.getUserNodes();
    Node recipient1Node1 = null;
    Iterator iter1 = userNodes1.iterator();
    if ( iter1.hasNext() )
    {
      recipient1Node1 = ( (UserNode)iter1.next() ).getNode();
    }
    return recipient1Node1;
  }

  public static ClaimRecipient buildClaimRecipient( Participant recipient1, Node recipient1Node1 )
  {
    ClaimRecipient claimRecipient1 = new ClaimRecipient();
    claimRecipient1.setRecipient( recipient1 );
    claimRecipient1.setNode( recipient1Node1 );
    claimRecipient1.setAwardQuantity( new Long( 5 ) );
    return claimRecipient1;
  }

}

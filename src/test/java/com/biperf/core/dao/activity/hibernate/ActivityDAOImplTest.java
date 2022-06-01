/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javatest/com/biperf/core/dao/activity/hibernate/ActivityDAOImplTest.java,v $
 */

package com.biperf.core.dao.activity.hibernate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.lang3.time.DateUtils;

import com.biperf.core.dao.activity.ActivityDAO;
import com.biperf.core.dao.claim.ClaimDAO;
import com.biperf.core.dao.claim.hibernate.ClaimDAOImplTest;
import com.biperf.core.dao.country.CountryDAO;
import com.biperf.core.dao.hibernate.BaseDAOTest;
import com.biperf.core.dao.hierarchy.NodeDAO;
import com.biperf.core.dao.hierarchy.hibernate.NodeDAOImplTest;
import com.biperf.core.dao.journal.JournalDAO;
import com.biperf.core.dao.journal.hibernate.JournalDAOImplTest;
import com.biperf.core.dao.merchandise.MerchOrderDAO;
import com.biperf.core.dao.merchandise.hibernate.MerchOrderActivityQueryConstraint;
import com.biperf.core.dao.oracle.OracleSequenceDAO;
import com.biperf.core.dao.participant.ParticipantDAO;
import com.biperf.core.dao.participant.hibernate.ParticipantDAOImplTest;
import com.biperf.core.dao.product.ProductCategoryDAO;
import com.biperf.core.dao.product.ProductDAO;
import com.biperf.core.dao.product.hibernate.ProductDAOImplTest;
import com.biperf.core.dao.promotion.PromoMerchCountryDAO;
import com.biperf.core.dao.promotion.PromotionDAO;
import com.biperf.core.dao.promotion.PromotionSweepstakeDAO;
import com.biperf.core.dao.promotion.hibernate.PromotionDAOImplTest;
import com.biperf.core.dao.promotion.hibernate.PromotionSweepstakeDAOImplTest;
import com.biperf.core.domain.BaseDomain;
import com.biperf.core.domain.activity.Activity;
import com.biperf.core.domain.activity.ManagerOverrideActivity;
import com.biperf.core.domain.activity.MerchOrderActivity;
import com.biperf.core.domain.activity.NominationActivity;
import com.biperf.core.domain.activity.QuizActivity;
import com.biperf.core.domain.activity.RecognitionActivity;
import com.biperf.core.domain.activity.SalesActivity;
import com.biperf.core.domain.activity.SweepstakesActivity;
import com.biperf.core.domain.activity.SweepstakesAwardAmountActivity;
import com.biperf.core.domain.claim.Claim;
import com.biperf.core.domain.claim.ClaimRecipient;
import com.biperf.core.domain.claim.QuizClaim;
import com.biperf.core.domain.claim.RecognitionClaim;
import com.biperf.core.domain.country.Country;
import com.biperf.core.domain.enums.MerchGiftCodeType;
import com.biperf.core.domain.hierarchy.Hierarchy;
import com.biperf.core.domain.hierarchy.Node;
import com.biperf.core.domain.hierarchy.NodeType;
import com.biperf.core.domain.journal.ActivityJournal;
import com.biperf.core.domain.journal.Journal;
import com.biperf.core.domain.merchandise.MerchOrder;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.product.Product;
import com.biperf.core.domain.product.ProductCategory;
import com.biperf.core.domain.promotion.PromoMerchCountry;
import com.biperf.core.domain.promotion.PromoMerchProgramLevel;
import com.biperf.core.domain.promotion.PromoMerchProgramLevelComparator;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.PromotionSweepstake;
import com.biperf.core.domain.promotion.QuizPromotion;
import com.biperf.core.domain.quiz.QuizQuestion;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.utils.ApplicationContextFactory;
import com.biperf.core.utils.GuidUtils;
import com.biperf.core.utils.HibernateSessionManager;

/*
 * ActivityDAOImplTest <p> <b>Change History:</b><br> <table border="1"> <tr> <th>Author</th>
 * <th>Date</th> <th>Version</th> <th>Comments</th> </tr> <tr> <td>OPI Admin</td> <td>Jul
 * 13, 2005</td> <td>1.0</td> <td>created</td> </tr> </table> </p>
 * 
 *
 */

public class ActivityDAOImplTest extends BaseDAOTest
{
  // ---------------------------------------------------------------------------
  // Build Methods
  // ---------------------------------------------------------------------------

  /**
   * Builds an {@link SalesActivity} object.
   * 
   * @param suffix
   * @param isPosted
   * @return a new {@link SalesActivity} object.
   */
  public static SalesActivity buildSalesActivity( String suffix, boolean isPosted )
  {
    // Node newNode = null;
    // Participant newParticipant = null;
    // Product newProduct = null;
    // Promotion promotion = null;

    Hierarchy hierarchy = new Hierarchy();
    hierarchy.setName( "Test NodeHierarchy1" + suffix );
    hierarchy.setDescription( "description goes here" );
    hierarchy.setPrimary( false );
    hierarchy.setActive( true );
    hierarchy.setCmAssetCode( "CM name ASSET" );
    hierarchy.setNameCmKey( "CM name KEY" );
    // flushAndClearSession();

    NodeType nodeType = new NodeType();
    nodeType.setCmAssetCode( "test.asset" + suffix );
    nodeType.setNameCmKey( "testkey" + suffix );
    // flushAndClearSession();

    Node newNode = NodeDAOImplTest.buildUniqueNode( "Node" + suffix, nodeType, hierarchy );
    newNode = getNodeDAO().saveNode( newNode );
    flushAndClearSession();

    Participant newParticipant = ParticipantDAOImplTest.buildUniqueParticipant( "Participant" + suffix );
    newParticipant = getParticipantDAO().saveParticipant( newParticipant );
    flushAndClearSession();

    ProductCategory newProdCat = ProductDAOImplTest.getProductCategoryDomainObject( "ProdCat1" );
    newProdCat = getProductCategoryDAO().saveProductCategory( newProdCat );
    // DO NOT FLUSH AND CLEAR HERE!!! -JasonSiverson

    Product newProduct = ProductDAOImplTest.buildStaticProductDomainObject( "Prod" + suffix, newProdCat );
    newProduct = getProductDAO().save( newProduct );
    flushAndClearSession();

    Promotion promotion = PromotionDAOImplTest.buildProductClaimPromotion( "Promo" + suffix );
    promotion = getPromotionDAO().save( promotion );
    flushAndClearSession();

    Claim claim = ClaimDAOImplTest.buildStaticProductClaim();
    claim.setPromotion( promotion );
    claim.setSubmitter( newParticipant );
    claim.setNode( newNode );
    claim.setSubmissionDate( new Date() );
    claim.setClaimNumber( new Long( getOracleSequenceDao().getOracleSequenceNextValue( "claim_nbr_sq" ) ).toString() );
    claim = getClaimDAO().saveClaim( claim );
    flushAndClearSession();

    SalesActivity expectedActivity = new SalesActivity( GuidUtils.generateGuid() );
    expectedActivity.setNode( newNode );
    expectedActivity.setParticipant( newParticipant );
    expectedActivity.setPosted( isPosted );
    expectedActivity.setProduct( newProduct );
    expectedActivity.setPromotion( promotion );
    expectedActivity.setClaim( claim );
    expectedActivity.setSubmissionDate( new Date() );

    return expectedActivity;
  }

  /**
   * Test getting a list of activities by various constraints.
   */
  public void testGetActivityListGeneralAndQuizActivity()
  {

    List expectedAllActivitesForThisPromo = new ArrayList();
    List expectedPostedActivitesForThisPromo = new ArrayList();
    List expectedUnpostedActivitesForThisPromo = new ArrayList();
    List expectedPassedActivitesForThisPromo = new ArrayList();
    List expectedFailedActivitesForThisPromo = new ArrayList();

    QuizActivity postedQuizActivity = buildQuizActivity( "1", true );
    getActivityDAO().saveActivity( postedQuizActivity );

    expectedAllActivitesForThisPromo.add( postedQuizActivity );
    expectedPostedActivitesForThisPromo.add( postedQuizActivity );
    expectedPassedActivitesForThisPromo.add( postedQuizActivity );

    QuizClaim claim2 = ClaimDAOImplTest.buildStaticQuizClaim( false );
    QuizPromotion testPromotion = (QuizPromotion)postedQuizActivity.getPromotion();
    claim2.setPromotion( testPromotion );
    claim2.setSubmitter( postedQuizActivity.getParticipant() );
    claim2.setNode( postedQuizActivity.getNode() );
    claim2.setSubmissionDate( new Date() );
    claim2.setClaimNumber( new Long( getOracleSequenceDao().getOracleSequenceNextValue( "claim_nbr_sq" ) ).toString() );
    claim2.setCurrentQuizQuestion( (QuizQuestion)testPromotion.getQuiz().getQuizQuestions().get( 0 ) );
    claim2.setPass( Boolean.FALSE );

    claim2 = (QuizClaim)getClaimDAO().saveClaim( claim2 );
    flushAndClearSession();

    QuizActivity unpostedQuizActivity = new QuizActivity( GuidUtils.generateGuid() );
    unpostedQuizActivity.setNode( postedQuizActivity.getNode() );
    unpostedQuizActivity.setParticipant( postedQuizActivity.getParticipant() );
    unpostedQuizActivity.setPosted( false );
    unpostedQuizActivity.setPromotion( testPromotion );
    unpostedQuizActivity.setClaim( claim2 );
    unpostedQuizActivity.setSubmissionDate( new Date() );
    getActivityDAO().saveActivity( unpostedQuizActivity );

    expectedAllActivitesForThisPromo.add( unpostedQuizActivity );
    expectedUnpostedActivitesForThisPromo.add( unpostedQuizActivity );
    expectedFailedActivitesForThisPromo.add( unpostedQuizActivity );

    flushAndClearSession();

    // get all for the test promo
    ActivityQueryConstraint allForPromoQueryConstraint = new ActivityQueryConstraint();
    allForPromoQueryConstraint.setPromotionId( testPromotion.getId() );
    assertEquals( expectedAllActivitesForThisPromo, getActivityDAO().getActivityList( allForPromoQueryConstraint ) );

    // get all for the test promo with all inclusive date range
    ActivityQueryConstraint allForPromoQueryConstraintWithWideDateRange = new ActivityQueryConstraint();
    allForPromoQueryConstraintWithWideDateRange.setPromotionId( testPromotion.getId() );
    Date yesterday = new Date( new Date().getTime() - DateUtils.MILLIS_PER_DAY );
    allForPromoQueryConstraintWithWideDateRange.setStartDate( yesterday );
    Date tomorrow = new Date( new Date().getTime() + DateUtils.MILLIS_PER_DAY );
    allForPromoQueryConstraintWithWideDateRange.setEndDate( tomorrow );
    assertEquals( expectedAllActivitesForThisPromo, getActivityDAO().getActivityList( allForPromoQueryConstraintWithWideDateRange ) );

    // get none for the test promo with early date range
    ActivityQueryConstraint allForPromoQueryConstraintWithEarlyDateRange = new ActivityQueryConstraint();
    allForPromoQueryConstraintWithEarlyDateRange.setPromotionId( testPromotion.getId() );
    Date twoDaysAgo = new Date( new Date().getTime() - ( 2 * DateUtils.MILLIS_PER_DAY ) );
    allForPromoQueryConstraintWithEarlyDateRange.setStartDate( twoDaysAgo );
    allForPromoQueryConstraintWithEarlyDateRange.setEndDate( twoDaysAgo );
    assertTrue( getActivityDAO().getActivityList( allForPromoQueryConstraintWithEarlyDateRange ).isEmpty() );

    // get all quiz activities for the test promo
    QuizActivityQueryConstraint allQuizForPromoQueryConstraint = new QuizActivityQueryConstraint();
    allQuizForPromoQueryConstraint.setPromotionId( testPromotion.getId() );
    assertEquals( expectedAllActivitesForThisPromo, getActivityDAO().getActivityList( allQuizForPromoQueryConstraint ) );

    // get all quiz activities for another unknown promo - should return nothing
    QuizActivityQueryConstraint allQuizForUnknownPromoQueryConstraint = new QuizActivityQueryConstraint();
    allQuizForUnknownPromoQueryConstraint.setPromotionId( new Long( -1 ) );
    assertTrue( getActivityDAO().getActivityList( allQuizForUnknownPromoQueryConstraint ).isEmpty() );

    // get posted quiz activities for the test promo
    QuizActivityQueryConstraint postedQuizForPromoQueryConstraint = new QuizActivityQueryConstraint();
    postedQuizForPromoQueryConstraint.setPromotionId( testPromotion.getId() );
    postedQuizForPromoQueryConstraint.setPosted( Boolean.TRUE );
    assertEquals( expectedPostedActivitesForThisPromo, getActivityDAO().getActivityList( postedQuizForPromoQueryConstraint ) );

    // get unposted quiz activities for the test promo
    QuizActivityQueryConstraint unpostedQuizForPromoQueryConstraint = new QuizActivityQueryConstraint();
    unpostedQuizForPromoQueryConstraint.setPromotionId( testPromotion.getId() );
    unpostedQuizForPromoQueryConstraint.setPosted( Boolean.FALSE );
    assertEquals( expectedUnpostedActivitesForThisPromo, getActivityDAO().getActivityList( unpostedQuizForPromoQueryConstraint ) );

    // get passing quiz activities for the test promo
    QuizActivityQueryConstraint passingQuizForPromoQueryConstraint = new QuizActivityQueryConstraint();
    passingQuizForPromoQueryConstraint.setPromotionId( testPromotion.getId() );
    passingQuizForPromoQueryConstraint.setPass( Boolean.TRUE );
    assertEquals( expectedPassedActivitesForThisPromo, getActivityDAO().getActivityList( passingQuizForPromoQueryConstraint ) );

    // get failing quiz activities for the test promo
    QuizActivityQueryConstraint failingQuizForPromoQueryConstraint = new QuizActivityQueryConstraint();
    failingQuizForPromoQueryConstraint.setPromotionId( testPromotion.getId() );
    failingQuizForPromoQueryConstraint.setPass( Boolean.FALSE );
    assertEquals( expectedFailedActivitesForThisPromo, getActivityDAO().getActivityList( failingQuizForPromoQueryConstraint ) );

  }

  /**
   * Test getting a list of recognition activities by recognitino specific constraints.
   */
  public void testGetActivityListRecognitionActivity()
  {

    List expectedAllActivitesForThisPromo = new ArrayList();
    List expectedGiverActivitesForThisPromo = new ArrayList();
    List expectedReceiverActivitesForThisPromo = new ArrayList();

    RecognitionActivity giverActivity = buildRecognitionActivity( "1", true );
    giverActivity.setSubmitter( true );
    getActivityDAO().saveActivity( giverActivity );

    Promotion testPromotion = giverActivity.getPromotion();

    expectedAllActivitesForThisPromo.add( giverActivity );
    expectedGiverActivitesForThisPromo.add( giverActivity );

    Participant receiver = ParticipantDAOImplTest.getSavedParticipantForTesting( "test-receiver" );
    RecognitionClaim claim = (RecognitionClaim)giverActivity.getClaim();
    ClaimRecipient claimRecipient = new ClaimRecipient();
    claimRecipient.setRecipient( receiver );

    // refetch claim to insure not evicition issues
    claim = (RecognitionClaim)getClaimDAO().getClaimById( claim.getId() );
    claim.addClaimRecipient( claimRecipient );
    getClaimDAO().saveClaim( claim );

    flushAndClearSession();

    RecognitionActivity receiverActivity = new RecognitionActivity( GuidUtils.generateGuid() );
    receiverActivity.setNode( giverActivity.getNode() );
    receiverActivity.setParticipant( receiver );
    receiverActivity.setPosted( false );
    receiverActivity.setPromotion( testPromotion );
    receiverActivity.setClaim( claim );
    receiverActivity.setSubmissionDate( new Date() );
    receiverActivity.setSubmitter( false );
    getActivityDAO().saveActivity( receiverActivity );

    expectedAllActivitesForThisPromo.add( receiverActivity );
    expectedReceiverActivitesForThisPromo.add( receiverActivity );

    flushAndClearSession();

    // get all recog activities for the test promo
    RecognitionActivityQueryConstraint allForPromoQueryConstraint = new RecognitionActivityQueryConstraint();
    allForPromoQueryConstraint.setPromotionId( testPromotion.getId() );
    assertEquals( expectedAllActivitesForThisPromo, getActivityDAO().getActivityList( allForPromoQueryConstraint ) );

    // get giver activities for the test promo
    RecognitionActivityQueryConstraint giverForPromoQueryConstraint = new RecognitionActivityQueryConstraint();
    giverForPromoQueryConstraint.setPromotionId( testPromotion.getId() );
    giverForPromoQueryConstraint.setGiverOrReceiver( Boolean.TRUE );
    assertEquals( expectedGiverActivitesForThisPromo, getActivityDAO().getActivityList( giverForPromoQueryConstraint ) );

    // get receiver activities for the test promo
    RecognitionActivityQueryConstraint receiverForPromoQueryConstraint = new RecognitionActivityQueryConstraint();
    receiverForPromoQueryConstraint.setPromotionId( testPromotion.getId() );
    receiverForPromoQueryConstraint.setGiverOrReceiver( Boolean.FALSE );
    assertEquals( expectedReceiverActivitesForThisPromo, getActivityDAO().getActivityList( receiverForPromoQueryConstraint ) );

  }

  /**
   * Test getting a list of Sweepstakes activities by recognitino specific constraints.
   */
  public void testGetActivityListSweepstakesActivity()
  {

    List expectedAllActivitesForThisDrawing = new ArrayList();

    SweepstakesActivity activity = buildSweepstakesActivity( "1", true );
    activity.setSubmitter( true );
    getActivityDAO().saveActivity( activity );
    flushAndClearSession();

    PromotionSweepstake testPromotionSweepstake = activity.getPromotionSweepstake();

    expectedAllActivitesForThisDrawing.add( activity );

    // get all activities for the drawing
    SweepstakesActivityQueryConstraint allForDrawingQueryConstraint = new SweepstakesActivityQueryConstraint();
    allForDrawingQueryConstraint.setPromotionSweepstakeId( testPromotionSweepstake.getId() );
    List actualActivityList = getActivityDAO().getActivityList( allForDrawingQueryConstraint );
    assertEquals( expectedAllActivitesForThisDrawing, actualActivityList );
    assertDomainObjectEquals( (BaseDomain)expectedAllActivitesForThisDrawing.get( 0 ), (BaseDomain)actualActivityList.get( 0 ) );
  }

  /**
   * Builds a {@link NominationActivity} object.
   * 
   * @param suffix
   * @param isPosted
   * @return a new {@link NominationActivity} object.
   */
  public static NominationActivity buildNominationActivity( String suffix, boolean isPosted, Participant submitter, Promotion promotion, Claim claim )
  {
    Hierarchy hierarchy = new Hierarchy();
    hierarchy.setName( "Test NodeHierarchy1" + suffix );
    hierarchy.setDescription( "description goes here" );
    hierarchy.setPrimary( false );
    hierarchy.setActive( true );
    hierarchy.setCmAssetCode( "CM name ASSET" );
    hierarchy.setNameCmKey( "CM name KEY" );
    // flushAndClearSession();

    NodeType nodeType = new NodeType();
    nodeType.setCmAssetCode( "test.asset" + suffix );
    nodeType.setNameCmKey( "testkey" + suffix );
    // flushAndClearSession();

    Node newNode = NodeDAOImplTest.buildUniqueNode( "Node" + suffix, nodeType, hierarchy );
    newNode = getNodeDAO().saveNode( newNode );
    flushAndClearSession();

    ProductCategory newProdCat = ProductDAOImplTest.getProductCategoryDomainObject( suffix );
    newProdCat = getProductCategoryDAO().saveProductCategory( newProdCat );
    // DO NOT FLUSH AND CLEAR HERE!!! -JasonSiverson

    Product newProduct = ProductDAOImplTest.buildStaticProductDomainObject( "Prod" + suffix, newProdCat );
    newProduct = getProductDAO().save( newProduct );
    flushAndClearSession();

    claim.setNode( newNode );
    claim = getClaimDAO().saveClaim( claim );
    flushAndClearSession();

    NominationActivity expectedActivity = new NominationActivity( GuidUtils.generateGuid() );
    expectedActivity.setNode( newNode );
    expectedActivity.setParticipant( submitter );
    expectedActivity.setPosted( isPosted );
    expectedActivity.setPromotion( promotion );
    expectedActivity.setClaim( claim );
    expectedActivity.setAwardQuantity( new Long( 50 ) );
    expectedActivity.setSubmissionDate( new Date() );

    return expectedActivity;
  }

  /**
   * Builds a {@link RecognitionActivity} object.
   * 
   * @param suffix
   * @param isPosted
   * @return a new {@link RecognitionActivity} object.
   */
  public static RecognitionActivity buildRecognitionActivity( String suffix, boolean isPosted )
  {
    Hierarchy hierarchy = new Hierarchy();
    hierarchy.setName( "Test NodeHierarchy1" + suffix );
    hierarchy.setDescription( "description goes here" );
    hierarchy.setPrimary( false );
    hierarchy.setActive( true );
    hierarchy.setCmAssetCode( "CM name ASSET" );
    hierarchy.setNameCmKey( "CM name KEY" );
    // flushAndClearSession();

    NodeType nodeType = new NodeType();
    nodeType.setCmAssetCode( "test.asset" + suffix );
    nodeType.setNameCmKey( "testkey" + suffix );
    // flushAndClearSession();

    Node newNode = NodeDAOImplTest.buildUniqueNode( "Node" + suffix, nodeType, hierarchy );
    newNode = getNodeDAO().saveNode( newNode );
    flushAndClearSession();

    Participant newParticipant = ParticipantDAOImplTest.buildUniqueParticipant( "Participant" + suffix );
    newParticipant = getParticipantDAO().saveParticipant( newParticipant );
    flushAndClearSession();

    ProductCategory newProdCat = ProductDAOImplTest.getProductCategoryDomainObject( "ProdCat1" );
    newProdCat = getProductCategoryDAO().saveProductCategory( newProdCat );
    // DO NOT FLUSH AND CLEAR HERE!!! -JasonSiverson

    Product newProduct = ProductDAOImplTest.buildStaticProductDomainObject( "Prod" + suffix, newProdCat );
    newProduct = getProductDAO().save( newProduct );
    flushAndClearSession();

    Promotion promotion = PromotionDAOImplTest.buildRecognitionPromotion( "Promo" + suffix );
    promotion = getPromotionDAO().save( promotion );
    flushAndClearSession();

    Claim claim = ClaimDAOImplTest.buildStaticRecognitionClaim();
    claim.setPromotion( promotion );
    claim.setSubmitter( newParticipant );
    claim.setNode( newNode );
    claim.setSubmissionDate( new Date() );
    claim.setClaimNumber( new Long( getOracleSequenceDao().getOracleSequenceNextValue( "claim_nbr_sq" ) ).toString() );
    claim = getClaimDAO().saveClaim( claim );
    flushAndClearSession();

    RecognitionActivity expectedActivity = new RecognitionActivity( GuidUtils.generateGuid() );
    expectedActivity.setNode( newNode );
    expectedActivity.setParticipant( newParticipant );
    expectedActivity.setPosted( isPosted );
    expectedActivity.setPromotion( promotion );
    expectedActivity.setClaim( claim );
    expectedActivity.setAwardQuantity( new Long( 50 ) );
    expectedActivity.setSubmissionDate( new Date() );

    return expectedActivity;
  }

  /**
   * Builds a {@link RecognitionActivity} object.
   * 
   * @param suffix
   * @param isPosted
   * @return a new {@link RecognitionActivity} object.
   */
  @SuppressWarnings( "unchecked" )
  public static MerchOrderActivity buildMerchOrderActivity( String suffix, boolean isPosted )
  {
    Hierarchy hierarchy = new Hierarchy();
    hierarchy.setName( "Test NodeHierarchy1" + suffix );
    hierarchy.setDescription( "description goes here" );
    hierarchy.setPrimary( false );
    hierarchy.setActive( true );
    hierarchy.setCmAssetCode( "CM name ASSET" );
    hierarchy.setNameCmKey( "CM name KEY" );
    // flushAndClearSession();

    NodeType nodeType = new NodeType();
    nodeType.setCmAssetCode( "test.asset" + suffix );
    nodeType.setNameCmKey( "testkey" + suffix );
    // flushAndClearSession();

    Node newNode = NodeDAOImplTest.buildUniqueNode( "Node" + suffix, nodeType, hierarchy );
    newNode = getNodeDAO().saveNode( newNode );
    flushAndClearSession();

    Participant newParticipant = ParticipantDAOImplTest.buildUniqueParticipant( "Participant" + suffix );
    newParticipant = getParticipantDAO().saveParticipant( newParticipant );
    flushAndClearSession();

    Promotion promotion = PromotionDAOImplTest.buildRecognitionPromotion( "Promo" + suffix );
    promotion = getPromotionDAO().save( promotion );
    flushAndClearSession();

    Claim claim = ClaimDAOImplTest.buildStaticRecognitionClaim();
    claim.setPromotion( promotion );
    claim.setSubmitter( newParticipant );
    claim.setNode( newNode );
    claim.setSubmissionDate( new Date() );
    claim.setClaimNumber( new Long( getOracleSequenceDao().getOracleSequenceNextValue( "claim_nbr_sq" ) ).toString() );
    claim = getClaimDAO().saveClaim( claim );
    flushAndClearSession();

    MerchOrderActivity expectedActivity = new MerchOrderActivity( GuidUtils.generateGuid() );
    expectedActivity.setNode( newNode );
    expectedActivity.setParticipant( newParticipant );
    expectedActivity.setPosted( isPosted );
    expectedActivity.setPromotion( promotion );
    expectedActivity.setClaim( claim );
    expectedActivity.setSubmissionDate( new Date() );

    MerchOrder merchOrder = new MerchOrder();
    merchOrder.setClaim( claim );
    merchOrder.setParticipant( newParticipant );
    merchOrder.setMerchGiftCodeType( MerchGiftCodeType.lookup( MerchGiftCodeType.PRODUCT ) );

    PromoMerchCountry promoMerchCountry = new PromoMerchCountry();
    Country country = getCountryDAO().getCountryByCode( Country.UNITED_STATES );
    promoMerchCountry.setCountry( country );
    promoMerchCountry.setPromotion( promotion );
    promoMerchCountry.setProgramId( "12345" );
    PromoMerchProgramLevel promoMerchProgramLevel = new PromoMerchProgramLevel();
    promoMerchProgramLevel.setLevelName( "Test Level" );
    promoMerchProgramLevel.setPromoMerchCountry( promoMerchCountry );
    promoMerchProgramLevel.setOrdinalPosition( 1 );
    SortedSet<PromoMerchProgramLevel> levels = new TreeSet<PromoMerchProgramLevel>( new PromoMerchProgramLevelComparator() );
    levels.add( promoMerchProgramLevel );
    promoMerchCountry.setLevels( levels );
    PromoMerchCountryDAO promoMerchCountryDAO = getPromoMerchCountryDAO();
    /* PromoMerchCountry pmc = */promoMerchCountryDAO.savePromoMerchCountry( promoMerchCountry );
    flushAndClearSession();

    merchOrder.setPromoMerchProgramLevel( promoMerchProgramLevel );
    merchOrder.setBatchId( getMerchOrderDAO().getNextBatchId() );

    merchOrder = getMerchOrderDAO().saveMerchOrder( merchOrder );
    flushAndClearSession();

    expectedActivity.setAwardQuantity( new Long( 50 ) );
    expectedActivity.setMerchOrder( merchOrder );

    return expectedActivity;
  }

  /**
   * Builds an {@link SalesActivity} object.
   * 
   * @param suffix
   * @param isPosted
   * @return a new {@link SalesActivity} object.
   */
  public static ManagerOverrideActivity buildManagerOverrideActivity( String suffix, boolean isPosted )
  {
    Hierarchy hierarchy = new Hierarchy();
    hierarchy.setName( "Test NodeHierarchy1" + suffix );
    hierarchy.setDescription( "description goes here" );
    hierarchy.setPrimary( false );
    hierarchy.setActive( true );
    hierarchy.setCmAssetCode( "CM name ASSET" );
    hierarchy.setNameCmKey( "CM name KEY" );
    // flushAndClearSession();

    NodeType nodeType = new NodeType();
    nodeType.setCmAssetCode( "test.asset" + suffix );
    nodeType.setNameCmKey( "testkey" + suffix );
    // flushAndClearSession();

    Node newNode = NodeDAOImplTest.buildUniqueNode( "Node" + suffix, nodeType, hierarchy );
    newNode = getNodeDAO().saveNode( newNode );
    flushAndClearSession();

    Participant newParticipant = ParticipantDAOImplTest.buildUniqueParticipant( "Participant" + suffix );
    newParticipant = getParticipantDAO().saveParticipant( newParticipant );
    flushAndClearSession();

    Promotion promotion = PromotionDAOImplTest.buildProductClaimPromotion( "Promo" + suffix );
    promotion = getPromotionDAO().save( promotion );
    flushAndClearSession();

    Claim claim = ClaimDAOImplTest.buildStaticProductClaim();
    claim.setPromotion( promotion );
    claim.setSubmitter( newParticipant );
    claim.setNode( newNode );
    claim.setSubmissionDate( new Date() );
    claim.setClaimNumber( new Long( getOracleSequenceDao().getOracleSequenceNextValue( "claim_nbr_sq" ) ).toString() );
    claim = getClaimDAO().saveClaim( claim );
    flushAndClearSession();

    ManagerOverrideActivity expectedActivity = new ManagerOverrideActivity( GuidUtils.generateGuid() );
    expectedActivity.setNode( newNode );
    expectedActivity.setParticipant( newParticipant );
    expectedActivity.setPosted( isPosted );
    expectedActivity.setPromotion( promotion );
    expectedActivity.setClaim( claim );
    expectedActivity.setSubmissionDate( new Date() );

    return expectedActivity;
  }

  /**
   * Builds an {@link SalesActivity} object.
   * 
   * @param suffix
   * @param isPosted
   * @return a new {@link SalesActivity} object.
   */
  public static QuizActivity buildQuizActivity( String suffix, boolean isPosted )
  {
    Hierarchy hierarchy = new Hierarchy();
    hierarchy.setName( "Test NodeHierarchy1" + suffix );
    hierarchy.setDescription( "description goes here" );
    hierarchy.setPrimary( false );
    hierarchy.setActive( true );
    hierarchy.setCmAssetCode( "CM name ASSET" );
    hierarchy.setNameCmKey( "CM name KEY" );
    // flushAndClearSession();

    NodeType nodeType = new NodeType();
    nodeType.setCmAssetCode( "test.asset" + suffix );
    nodeType.setNameCmKey( "testkey" + suffix );
    // flushAndClearSession();

    Node newNode = NodeDAOImplTest.buildUniqueNode( "Node" + suffix, nodeType, hierarchy );
    newNode = getNodeDAO().saveNode( newNode );
    flushAndClearSession();

    Participant newParticipant = ParticipantDAOImplTest.buildUniqueParticipant( "Participant" + suffix );
    newParticipant = getParticipantDAO().saveParticipant( newParticipant );
    flushAndClearSession();

    QuizPromotion promotion = PromotionDAOImplTest.buildQuizPromotion( "Promo" + suffix );
    promotion = (QuizPromotion)getPromotionDAO().save( promotion );
    flushAndClearSession();

    QuizClaim claim = ClaimDAOImplTest.buildStaticQuizClaim( false );
    claim.setPromotion( promotion );
    claim.setSubmitter( newParticipant );
    claim.setNode( newNode );
    claim.setSubmissionDate( new Date() );
    claim.setClaimNumber( new Long( getOracleSequenceDao().getOracleSequenceNextValue( "claim_nbr_sq" ) ).toString() );
    claim.setCurrentQuizQuestion( (QuizQuestion)promotion.getQuiz().getQuizQuestions().get( 0 ) );
    claim = (QuizClaim)getClaimDAO().saveClaim( claim );
    claim.setPass( Boolean.TRUE );
    flushAndClearSession();

    QuizActivity expectedActivity = new QuizActivity( GuidUtils.generateGuid() );
    expectedActivity.setNode( newNode );
    expectedActivity.setParticipant( newParticipant );
    expectedActivity.setPosted( isPosted );
    expectedActivity.setPromotion( promotion );
    expectedActivity.setClaim( claim );
    expectedActivity.setSubmissionDate( new Date() );

    return expectedActivity;
  }

  public static SweepstakesActivity buildSweepstakesActivity( String suffix, boolean isPosted )
  {
    Hierarchy hierarchy = new Hierarchy();
    hierarchy.setName( "Test NodeHierarchy1" + suffix );
    hierarchy.setDescription( "description goes here" );
    hierarchy.setPrimary( false );
    hierarchy.setActive( true );
    hierarchy.setCmAssetCode( "CM name ASSET" );
    hierarchy.setNameCmKey( "CM name KEY" );
    // flushAndClearSession();

    // NodeType nodeType = new NodeType();
    // nodeType.setCmAssetCode( "test.asset" + suffix );
    // nodeType.setNameCmKey( "testkey" + suffix );
    // // flushAndClearSession();
    //
    // Node newNode = NodeDAOImplTest.buildUniqueNode( "Node" + suffix, nodeType, hierarchy );
    // newNode = getNodeDAO().saveNode( newNode );
    // flushAndClearSession();

    Participant newParticipant = ParticipantDAOImplTest.buildUniqueParticipant( "Participant" + suffix );
    newParticipant = getParticipantDAO().saveParticipant( newParticipant );
    flushAndClearSession();

    QuizPromotion promotion = PromotionDAOImplTest.buildQuizPromotion( "Promo" + suffix );
    promotion = (QuizPromotion)getPromotionDAO().save( promotion );
    flushAndClearSession();

    PromotionSweepstake promotionSweepstake = PromotionSweepstakeDAOImplTest.buildPromotionSweepstake( promotion );
    promotionSweepstake = getPromotionSweepstakeDAO().save( promotionSweepstake );
    flushAndClearSession();

    SweepstakesAwardAmountActivity expectedActivity = new SweepstakesAwardAmountActivity();
    expectedActivity.setGuid( GuidUtils.generateGuid() );
    expectedActivity.setParticipant( newParticipant );
    expectedActivity.setPosted( isPosted );
    expectedActivity.setPromotion( promotion );
    expectedActivity.setSubmissionDate( new Date() );
    expectedActivity.setPromotionSweepstake( promotionSweepstake );
    expectedActivity.setAwardQuantity( new Long( 5 ) );
    return expectedActivity;
  }

  // ---------------------------------------------------------------------------
  // Test Methods
  // ---------------------------------------------------------------------------

  /**
   * Test save and getting all activities by Id
   */
  public void testSaveUpdateGetActivityById()
  {
    // Build a unique string to be used during testing.
    String uniqueString = String.valueOf( System.currentTimeMillis() % 5503032 );

    ActivityDAO activityDAO = getActivityDAO();

    SalesActivity expectedActivity = null;

    expectedActivity = buildSalesActivity( uniqueString, false );
    activityDAO.saveActivity( expectedActivity );
    flushAndClearSession();

    Activity actualActivity = activityDAO.getActivityById( expectedActivity.getId() );

    assertEquals( "Activities not equal", expectedActivity, actualActivity );

    expectedActivity.setPosted( true );

    activityDAO.saveActivity( expectedActivity );

    flushAndClearSession();

    actualActivity = activityDAO.getActivityById( expectedActivity.getId() );

    assertEquals( "Activities not equal", expectedActivity, actualActivity );
  }

  /**
   * Test save and getting all activities
   */
  public void testGetAllActivities()
  {
    // Build a unique string to be used during testing.
    String uniqueString = String.valueOf( System.currentTimeMillis() % 5503032 );

    ActivityDAO activityDAO = getActivityDAO();

    int count = 0;

    count = activityDAO.getAllActivities().size();

    List expectedActivities = new ArrayList();

    SalesActivity expectedActivity1 = buildSalesActivity( uniqueString, false );
    expectedActivities.add( expectedActivity1 );
    activityDAO.saveActivity( expectedActivity1 );

    flushAndClearSession();

    List actualActivities = activityDAO.getAllActivities();

    assertEquals( "Lists of activities aren't the same size.", expectedActivities.size() + count, actualActivities.size() );
  }

  /**
   * Test save and getting all activities by participant
   */
  public void testGetActivitiesByParticipant()
  {
    // Build a unique string to be used during testing.
    String uniqueString = String.valueOf( System.currentTimeMillis() % 5503032 );

    ActivityDAO activityDAO = getActivityDAO();

    List expectedActivities = new ArrayList();

    SalesActivity expectedActivity1 = buildSalesActivity( uniqueString, false );
    expectedActivities.add( expectedActivity1 );
    activityDAO.saveActivity( expectedActivity1 );

    flushAndClearSession();

    List actualActivities = activityDAO.getActivitiesByParticipantId( expectedActivity1.getParticipant().getId() );

    assertEquals( "Lists of activities aren't the same size.", expectedActivities.size(), actualActivities.size() );

  }

  /**
   * Test save and getting all activities not posted
   */
  public void testGetAllActivitiesNotPosted()
  {
    // Build a unique string to be used during testing.
    String uniqueString = String.valueOf( System.currentTimeMillis() % 5503032 );

    ActivityDAO activityDAO = getActivityDAO();

    int count = 0;

    count = activityDAO.getAllSalesActivitiesNotPosted().size();

    List expectedActivities = new ArrayList();

    SalesActivity expectedActivity1 = buildSalesActivity( uniqueString, false );
    expectedActivities.add( expectedActivity1 );
    activityDAO.saveActivity( expectedActivity1 );

    flushAndClearSession();

    List actualActivities = activityDAO.getAllSalesActivitiesNotPosted();

    assertEquals( "Lists of activities aren't the same size.", expectedActivities.size() + count, actualActivities.size() );

  }

  /**
   * Test save and getting all activities not posted
   */
  public void testGetAllRecognitionActivitiesNotPosted()
  {
    // Build a unique string to be used during testing.
    String uniqueString = String.valueOf( System.currentTimeMillis() % 5503032 );

    ActivityDAO activityDAO = getActivityDAO();

    int count = 0;

    count = activityDAO.getAllRecognitionActivitiesNotPosted().size();

    List expectedActivities = new ArrayList();

    RecognitionActivity expectedActivity1 = buildRecognitionActivity( uniqueString, false );
    expectedActivities.add( expectedActivity1 );
    activityDAO.saveActivity( expectedActivity1 );

    flushAndClearSession();

    List actualActivities = activityDAO.getAllRecognitionActivitiesNotPosted();

    assertEquals( "Lists of activities aren't the same size.", expectedActivities.size() + count, actualActivities.size() );

  }

  /**
   * Test save and getting all activities not posted
   */
  public void testGetUnpostedActivities()
  {
    // Build a unique string to be used during testing.
    String uniqueString = String.valueOf( System.currentTimeMillis() % 5503032 );

    SalesActivity expectedActivity1 = buildSalesActivity( uniqueString, false );
    getActivityDAO().saveActivity( expectedActivity1 );

    flushAndClearSession();

    Set actualActivities = getActivityDAO().getUnpostedSalesActivities( expectedActivity1.getParticipant().getId(), expectedActivity1.getClaim().getId(), expectedActivity1.getPromotion().getId() );

    assertEquals( 1, actualActivities.size() );

  }

  /**
   * Tests adding an activity to a journal transaction.
   */
  public void testActivityJournal()
  {
    String uniqueString = String.valueOf( System.currentTimeMillis() % 5503032 );

    // Build and save an activity.
    SalesActivity activity = buildSalesActivity( uniqueString, false );
    getActivityDAO().saveActivity( activity );
    HibernateSessionManager.getSession().flush();

    // Build and save a journal transaction.
    Journal journal = JournalDAOImplTest.buildAndSaveJournal( uniqueString );
    getJournalDAO().saveJournalEntry( journal );
    HibernateSessionManager.getSession().flush();

    // Associate the activity and the journal transaction.
    ActivityJournal activityJournal = new ActivityJournal();
    activityJournal.setJournal( journal );
    activity.addActivityJournal( activityJournal );
    getActivityDAO().saveActivity( activity );
    flushAndClearSession();

    // Did the activity DAO save the activity/journal too?
    Activity actualActivity = getActivityDAO().getActivityById( activity.getId() );
    assertEquals( 1, actualActivity.getActivityJournals().size() );
  }

  /**
   * Tests adding an activity to a journal transaction.
   */
  public void testSalesActivityGetByClaimAndUserId()
  {
    String uniqueString = String.valueOf( System.currentTimeMillis() % 5503032 );

    // Build and save an activity.
    SalesActivity activity = buildSalesActivity( uniqueString, false );
    getActivityDAO().saveActivity( activity );
    HibernateSessionManager.getSession().flush();

    Long claimId = activity.getClaim().getId();
    Long userId = activity.getParticipant().getId();

    flushAndClearSession();

    List claimList = getActivityDAO().getSalesActivitiesByClaimAndUserId( claimId, userId );
    assertEquals( 1, claimList.size() );
  }

  /**
   * Tests adding an activity to a journal transaction.
   */
  public void testManagerOverrideActivityGetByClaimAndUserId()
  {
    String uniqueString = String.valueOf( System.currentTimeMillis() % 5503032 );

    // Build and save an activity.
    ManagerOverrideActivity managerOverrideActivity = buildManagerOverrideActivity( uniqueString, false );
    getActivityDAO().saveActivity( managerOverrideActivity );
    HibernateSessionManager.getSession().flush();

    Long claimId = managerOverrideActivity.getClaim().getId();
    Long userId = managerOverrideActivity.getParticipant().getId();

    flushAndClearSession();

    List claimList = getActivityDAO().getManagerOverrideActivitiesByClaimAndUserId( claimId, userId );
    assertEquals( 1, claimList.size() );
  }

  /**
   * Tests adding an activity to a journal transaction.
   */
  public void testMerchOrderActivityGetByClaimAndUserId()
  {
    String uniqueString = String.valueOf( System.currentTimeMillis() % 5503032 );

    // Build and save an activity.
    MerchOrderActivity merchOrderActivity = buildMerchOrderActivity( uniqueString, false );
    getActivityDAO().saveActivity( merchOrderActivity );

    HibernateSessionManager.getSession().flush();

    Long claimId = merchOrderActivity.getClaim().getId();
    Long userId = merchOrderActivity.getParticipant().getId();

    flushAndClearSession();

    MerchOrderActivityQueryConstraint merchOrderActivityQueryConstraint = new MerchOrderActivityQueryConstraint();
    merchOrderActivityQueryConstraint.setClaimId( claimId );
    merchOrderActivityQueryConstraint.setParticipantId( userId );

    List claimList = getMerchOrderDAO().getMerchOrderList( merchOrderActivityQueryConstraint );
    assertEquals( 1, claimList.size() );
  }

  /**
   * Tests adding an activity to a journal transaction.
   */
  public void testQuizActivityGetByClaimAndUserId()
  {
    String uniqueString = String.valueOf( System.currentTimeMillis() % 5503032 );

    // Build and save an activity.
    QuizActivity quizActivity = buildQuizActivity( uniqueString, false );
    getActivityDAO().saveActivity( quizActivity );
    HibernateSessionManager.getSession().flush();

    Long claimId = quizActivity.getClaim().getId();
    Long userId = quizActivity.getParticipant().getId();

    flushAndClearSession();

    List claimList = getActivityDAO().getQuizActivitiesByClaimAndUserId( claimId, userId );
    assertEquals( 1, claimList.size() );
  }

  /**
   * Tests adding an activity to a journal transaction.
   */
  public void testActivitiesByCarryOver()
  {
    String uniqueString = getUniqueString();

    // Build and save an activity.
    SalesActivity activity = buildSalesActivity( uniqueString, false );
    activity.setCarryover( true );
    getActivityDAO().saveActivity( activity );
    flushAndClearSession();

    Long promotionId = activity.getPromotion().getId();
    Long participantId = activity.getParticipant().getId();
    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();

    List activityList = getActivityDAO().getActivitiesByCarryOverBalance( promotionId, participantId, associationRequestCollection );
    assertEquals( 1, activityList.size() );
  }

  public void testActivitiesByMerchOrderId()
  {
    String uniqueString = getUniqueString();

    // Build and save an activity.
    MerchOrderActivity activity = buildMerchOrderActivity( uniqueString, true );
    getActivityDAO().saveActivity( activity );
    flushAndClearSession();
    Long merchOrderId = activity.getMerchOrder().getId();
    Activity test = getActivityDAO().getActivityForMerchOrderId( merchOrderId );
    assertNotNull( test );
  }

  // ---------------------------------------------------------------------------
  // Private Methods
  // ---------------------------------------------------------------------------

  /**
   * Returns an {@link ActivityDAO} object.
   * 
   * @return an {@link ActivityDAO} object.
   */
  private static ActivityDAO getActivityDAO()
  {
    return (ActivityDAO)ApplicationContextFactory.getApplicationContext().getBean( ActivityDAO.BEAN_NAME );
  }

  /**
   * Returns a {@link ClaimDAO} object.
   * 
   * @return a {@link ClaimDAO} object.
   */
  private static ClaimDAO getClaimDAO()
  {
    return (ClaimDAO)ApplicationContextFactory.getApplicationContext().getBean( ClaimDAO.BEAN_NAME );
  }

  /**
   * Returns a {@link JournalDAO} object.
   * 
   * @return a {@link com.biperf.core.dao.journal.JournalDAO} object.
   */
  private static JournalDAO getJournalDAO()
  {
    return (JournalDAO)ApplicationContextFactory.getApplicationContext().getBean( JournalDAO.BEAN_NAME );
  }

  /**
   * Returns a {@link NodeDAO} object.
   * 
   * @return a {@link NodeDAO} object.
   */
  private static NodeDAO getNodeDAO()
  {
    return (NodeDAO)ApplicationContextFactory.getApplicationContext().getBean( NodeDAO.BEAN_NAME );
  }

  /**
   * Returns a {@link ParticipantDAO} object.
   * 
   * @return a {@link ParticipantDAO} object.
   */
  private static ParticipantDAO getParticipantDAO()
  {
    return (ParticipantDAO)ApplicationContextFactory.getApplicationContext().getBean( ParticipantDAO.BEAN_NAME );
  }

  /**
   * Returns a {@link ProductCategoryDAO} object.
   * 
   * @return a {@link ProductCategoryDAO} object.
   */
  private static ProductCategoryDAO getProductCategoryDAO()
  {
    return (ProductCategoryDAO)ApplicationContextFactory.getApplicationContext().getBean( ProductCategoryDAO.BEAN_NAME );
  }

  /**
   * Returns a {@link ProductDAO} object.
   * 
   * @return a {@link ProductDAO} object.
   */
  private static ProductDAO getProductDAO()
  {
    return (ProductDAO)ApplicationContextFactory.getApplicationContext().getBean( ProductDAO.BEAN_NAME );
  }

  /**
   * Returns a {@link PromotionDAO} object.
   * 
   * @return a {@link PromotionDAO} object.
   */
  private static PromotionDAO getPromotionDAO()
  {
    return (PromotionDAO)ApplicationContextFactory.getApplicationContext().getBean( PromotionDAO.BEAN_NAME );
  }

  /**
   * Uses the ApplicationContextFactory to look up the PromotionSweepstakeDAO implementation.
   * 
   * @return PromotionSweepstakeDAO
   */
  private static PromotionSweepstakeDAO getPromotionSweepstakeDAO()
  {
    return (PromotionSweepstakeDAO)ApplicationContextFactory.getApplicationContext().getBean( PromotionSweepstakeDAO.BEAN_NAME );
  }

  /**
   * Uses the ApplicationContextFactory to look up the MerchOrderDAO implementation.
   * 
   * @return MerchGiftCodeDAO
   */
  private static MerchOrderDAO getMerchOrderDAO()
  {
    return (MerchOrderDAO)ApplicationContextFactory.getApplicationContext().getBean( MerchOrderDAO.BEAN_NAME );
  }

  /**
   * Returns a reference to the oracle sequence DAO.
   * 
   * @return a reference to the oracle sequence DAO.
   */
  private static OracleSequenceDAO getOracleSequenceDao()
  {
    return (OracleSequenceDAO)ApplicationContextFactory.getApplicationContext().getBean( OracleSequenceDAO.BEAN_NAME );
  }

  /**
   * Get the CountryDAO.
   * 
   * @return CountryDAO
   */
  private static CountryDAO getCountryDAO()
  {
    return (CountryDAO)ApplicationContextFactory.getApplicationContext().getBean( "countryDAO" );
  }

  /**
   * Returns a {@link PromoMerchCountryDAO} object.
   * 
   * @return a {@link PromoMerchCountryDAO} object.
   */
  private static PromoMerchCountryDAO getPromoMerchCountryDAO()
  {
    return (PromoMerchCountryDAO)ApplicationContextFactory.getApplicationContext().getBean( PromoMerchCountryDAO.BEAN_NAME );
  }

}
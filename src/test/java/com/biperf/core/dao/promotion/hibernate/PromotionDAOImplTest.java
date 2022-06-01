/**
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javatest/com/biperf/core/dao/promotion/hibernate/PromotionDAOImplTest.java,v $
 */

package com.biperf.core.dao.promotion.hibernate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.dao.budget.hibernate.BudgetMasterDAOImplTest;
import com.biperf.core.dao.claim.ClaimDAO;
import com.biperf.core.dao.claim.ClaimFormDAO;
import com.biperf.core.dao.claim.hibernate.ClaimDAOImplTest;
import com.biperf.core.dao.claim.hibernate.ClaimFormDAOImplTest;
import com.biperf.core.dao.hibernate.BaseDAOTest;
import com.biperf.core.dao.hierarchy.NodeTypeDAO;
import com.biperf.core.dao.hierarchy.hibernate.NodeTypeDAOImplTest;
import com.biperf.core.dao.multimedia.hibernate.MultimediaDAOImplTest;
import com.biperf.core.dao.participant.AudienceDAO;
import com.biperf.core.dao.participant.ParticipantDAO;
import com.biperf.core.dao.participant.hibernate.AudienceDAOImplTest;
import com.biperf.core.dao.participant.hibernate.ParticipantDAOImplTest;
import com.biperf.core.dao.promotion.PromotionDAO;
import com.biperf.core.dao.quiz.hibernate.QuizDAOImplTest;
import com.biperf.core.domain.budget.BudgetMaster;
import com.biperf.core.domain.claim.Claim;
import com.biperf.core.domain.claim.ClaimElement;
import com.biperf.core.domain.claim.ClaimForm;
import com.biperf.core.domain.claim.ClaimFormStep;
import com.biperf.core.domain.claim.ClaimFormStepElement;
import com.biperf.core.domain.enums.AchievementPrecision;
import com.biperf.core.domain.enums.AchievementRuleType;
import com.biperf.core.domain.enums.ApprovalConditionalAmmountOperatorType;
import com.biperf.core.domain.enums.ApprovalType;
import com.biperf.core.domain.enums.ApproverType;
import com.biperf.core.domain.enums.ChallengePointAwardType;
import com.biperf.core.domain.enums.ClaimFormElementType;
import com.biperf.core.domain.enums.ManagerOverrideStructure;
import com.biperf.core.domain.enums.NominationAwardGroupType;
import com.biperf.core.domain.enums.NominationEvaluationType;
import com.biperf.core.domain.enums.PayoutStructure;
import com.biperf.core.domain.enums.PrimaryAudienceType;
import com.biperf.core.domain.enums.PromoMgrPayoutFreqType;
import com.biperf.core.domain.enums.PromoNominationBehaviorType;
import com.biperf.core.domain.enums.PromoRecognitionBehaviorType;
import com.biperf.core.domain.enums.PromotionApprovalOptionReasonType;
import com.biperf.core.domain.enums.PromotionApprovalOptionType;
import com.biperf.core.domain.enums.PromotionAwardsType;
import com.biperf.core.domain.enums.PromotionClaimFormStepElementValidationType;
import com.biperf.core.domain.enums.PromotionJobPositionType;
import com.biperf.core.domain.enums.PromotionPayoutType;
import com.biperf.core.domain.enums.PromotionProcessingModeType;
import com.biperf.core.domain.enums.PromotionStatusType;
import com.biperf.core.domain.enums.PromotionType;
import com.biperf.core.domain.enums.RoundingMethod;
import com.biperf.core.domain.enums.SecondaryAudienceType;
import com.biperf.core.domain.enums.SubmittersToRankType;
import com.biperf.core.domain.enums.SweepstakesMultipleAwardsType;
import com.biperf.core.domain.enums.SweepstakesWinnerEligibilityType;
import com.biperf.core.domain.enums.SweepstakesWinnersType;
import com.biperf.core.domain.enums.TeamUnavailableResolverType;
import com.biperf.core.domain.enums.ThrowdownPromotionType;
import com.biperf.core.domain.enums.UserType;
import com.biperf.core.domain.enums.WebRulesAudienceType;
import com.biperf.core.domain.hierarchy.NodeType;
import com.biperf.core.domain.multimedia.ECard;
import com.biperf.core.domain.participant.Audience;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.ChallengePointPromotion;
import com.biperf.core.domain.promotion.GoalLevel;
import com.biperf.core.domain.promotion.GoalQuestPromotion;
import com.biperf.core.domain.promotion.ManagerOverrideGoalLevel;
import com.biperf.core.domain.promotion.NominationPromotion;
import com.biperf.core.domain.promotion.NominationPromotionLevel;
import com.biperf.core.domain.promotion.NominationPromotionTimePeriod;
import com.biperf.core.domain.promotion.ProductClaimPromotion;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.PromotionApprovalOption;
import com.biperf.core.domain.promotion.PromotionApprovalOptionReason;
import com.biperf.core.domain.promotion.PromotionAudience;
import com.biperf.core.domain.promotion.PromotionCert;
import com.biperf.core.domain.promotion.PromotionClaimFormStepElementValidation;
import com.biperf.core.domain.promotion.PromotionECard;
import com.biperf.core.domain.promotion.PromotionPrimaryAudience;
import com.biperf.core.domain.promotion.PromotionSecondaryAudience;
import com.biperf.core.domain.promotion.PromotionSweepstake;
import com.biperf.core.domain.promotion.PromotionTeamPosition;
import com.biperf.core.domain.promotion.PromotionWebRulesAudience;
import com.biperf.core.domain.promotion.QuizPromotion;
import com.biperf.core.domain.promotion.RecognitionPromotion;
import com.biperf.core.domain.promotion.StackRankPayout;
import com.biperf.core.domain.promotion.StackRankPayoutGroup;
import com.biperf.core.domain.promotion.SurveyPromotion;
import com.biperf.core.domain.promotion.ThrowdownPromotion;
import com.biperf.core.domain.quiz.Quiz;
import com.biperf.core.domain.ssi.SSIPromotion;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.promotion.PromotionAssociationRequest;
import com.biperf.core.utils.ApplicationContextFactory;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.HibernateSessionManager;
import com.biperf.core.value.PromotionBehaviorsValueBean;
import com.biperf.core.value.nomination.NominationSubmitDataDrawSettingsValueBean;
import com.biperf.core.value.nomination.NominationSubmitDataECardValueBean;
import com.biperf.core.value.promo.PromoAwards;
import com.biperf.core.value.promo.PromoBehaviours;
import com.biperf.core.value.promo.PromoEcards;
import com.biperf.core.value.promo.PromoFormRules;
import com.biperf.core.value.promo.PromoSweeptakes;
import com.biperf.core.value.promo.PromotionBasics;

/**
 * PromotionDAOImplTest.
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
 * <td>asondgeroth</td>
 * <td>Jun 30, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class PromotionDAOImplTest extends BaseDAOTest
{
  private static final Log logger = LogFactory.getLog( PromotionDAOImplTest.class );

  static ClaimFormDAO claimFormDao = getClaimFormDao();

  /**
   * Tests create, update and selecting the promotion by the Id._pro
   */
  public void testRecognitionPromotionPromotionSaveAndGetById()
  {
    // create a new product claim promotion
    PromotionDAO promotionDAO = getPromotionDAO();

    String uniqueString = buildUniqueString();

    Promotion expectedPromotion = PromotionDAOImplTest.buildProductClaimPromotion( "PRODUCT_CLAIM" + uniqueString );
    promotionDAO.save( expectedPromotion );
    assertEquals( "Actual promotion doesn't match with expected", expectedPromotion, promotionDAO.getPromotionById( expectedPromotion.getId() ) );

    // do an update on the saved promotion
    expectedPromotion.setName( "PRODUCT_CLAIM_UPDATED: " + uniqueString );
    expectedPromotion.setUpperCaseName( expectedPromotion.getPromotionName().toUpperCase() );
    promotionDAO.save( expectedPromotion );

    flushAndClearSession();

    // retrieve the promotion
    Promotion actualPromotion = promotionDAO.getPromotionById( expectedPromotion.getId() );
    assertDomainObjectEquals( "Actual promotion doesn't match with expected", expectedPromotion, actualPromotion );

    // create a new recognition promotion
    RecognitionPromotion expectedPromotion2 = PromotionDAOImplTest.buildRecognitionPromotion( "RECOGNITION" + uniqueString );
    promotionDAO.save( expectedPromotion2 );

    assertEquals( "Actual promotion doesn't match with expected", expectedPromotion2, promotionDAO.getPromotionById( expectedPromotion2.getId() ) );

    // do an update on the saved promotion
    expectedPromotion2.setName( "RECOGNITION_UPDATED" + uniqueString );
    expectedPromotion2.setUpperCaseName( expectedPromotion2.getPromotionName().toUpperCase() );
    promotionDAO.save( expectedPromotion2 );

    flushAndClearSession();

    // retrieve the promotion
    RecognitionPromotion actualPromotion2 = (RecognitionPromotion)promotionDAO.getPromotionById( expectedPromotion2.getId() );

    assertDomainObjectEquals( "Actual promotion doesn't match with expected", expectedPromotion2, actualPromotion2 );

    assertDomainObjectEquals( "Actual promotion BudgetMaster doesn't match with expected", expectedPromotion2.getBudgetMaster(), actualPromotion2.getBudgetMaster() );

    assertEquals( "Actual promotion ecards weren't equal to expected. ", actualPromotion2.getPromotionECard(), expectedPromotion2.getPromotionECard() );

    assertEquals( "Actual promotion sweepstakes weren't equal to expected. ", actualPromotion2.getPromotionSweepstakes(), expectedPromotion2.getPromotionSweepstakes() );

  }

  public void testNominationPromotionPromotionSaveAndGetById()
  {
    // create a new product claim promotion
    PromotionDAO promotionDAO = getPromotionDAO();

    String uniqueString = buildUniqueString();

    NominationPromotion expectedPromotion = PromotionDAOImplTest.buildNominationPromotion( "NOMINATION" + uniqueString );
    promotionDAO.save( expectedPromotion );

    assertEquals( "Actual promotion doesn't match with expected", expectedPromotion, promotionDAO.getPromotionById( expectedPromotion.getId() ) );

    // do an update on the saved promotion
    expectedPromotion.setName( "NOMINATION_UPDATED: " + uniqueString );
    expectedPromotion.setUpperCaseName( expectedPromotion.getPromotionName().toUpperCase() );
    promotionDAO.save( expectedPromotion );

    flushAndClearSession();

    // retrieve the promotion
    Promotion actualPromotion = promotionDAO.getPromotionById( expectedPromotion.getId() );
    assertDomainObjectEquals( "Actual promotion doesn't match with expected", expectedPromotion, actualPromotion );

  }

  /**
   * Tests create, update and selecting the promotion by the Id.
   */
  public void testProductClaimPromotionSaveAndGetById()
  {
    // create a new product claim promotion
    PromotionDAO promotionDAO = getPromotionDAO();

    String uniqueString = buildUniqueString();

    Promotion expectedPromotion = PromotionDAOImplTest.buildProductClaimPromotion( "PRODUCT_CLAIM" + uniqueString );
    promotionDAO.save( expectedPromotion );

    assertEquals( "Actual promotion doesn't match with expected", expectedPromotion, promotionDAO.getPromotionById( expectedPromotion.getId() ) );

    // do an update on the saved promotion
    expectedPromotion.setName( "PRODUCT_CLAIM_UPDATED: " + uniqueString );
    expectedPromotion.setUpperCaseName( expectedPromotion.getPromotionName().toUpperCase() );
    promotionDAO.save( expectedPromotion );

    flushAndClearSession();

    // retrieve the promotion
    Promotion actualPromotion = promotionDAO.getPromotionById( expectedPromotion.getId() );
    assertDomainObjectEquals( "Actual promotion doesn't match with expected", expectedPromotion, actualPromotion );

    assertDomainObjectEquals( "Actual promotion ClaimForm doesn't match with expected", expectedPromotion.getClaimForm(), actualPromotion.getClaimForm() );
  }

  /**
   * Tests create, update and selecting the promotion by the Id and stackRank.
   */
  public void testProductClaimPromotionSaveStackRankAndGetById()
  {
    // create a new product claim promotion
    PromotionDAO promotionDAO = getPromotionDAO();

    String uniqueString = buildUniqueString();

    ProductClaimPromotion expectedPromotion = PromotionDAOImplTest.buildProductClaimPromotionWithStackRank( "PRODUCT_CLAIM" + uniqueString );

    int expectedStackRankPayoutSize = expectedPromotion.getStackRankPayoutGroups().size();

    promotionDAO.save( expectedPromotion );

    flushAndClearSession();

    // retrieve the promotion
    ProductClaimPromotion actualPromotion = (ProductClaimPromotion)promotionDAO.getPromotionById( expectedPromotion.getId() );

    int actualStackRankPayoutSize = actualPromotion.getStackRankPayoutGroups().size();

    assertEquals( "Actual promotion's stackRankPayoutSize doesn't match with expected", expectedStackRankPayoutSize, actualStackRankPayoutSize );

    assertEquals( "Actual promotion doesn't match with expected", expectedPromotion, actualPromotion );

    // update
    Set actualStackRankPayoutGroups = actualPromotion.getStackRankPayoutGroups();
    Iterator iter = actualStackRankPayoutGroups.iterator();

    StackRankPayoutGroup stackRankPayoutGroup = (StackRankPayoutGroup)iter.next();
    Set stackRankPayouts = stackRankPayoutGroup.getStackRankPayouts();
    Iterator payoutIter = stackRankPayouts.iterator();
    StackRankPayout payout = (StackRankPayout)payoutIter.next();

    // now remove the payout from stackRankPayoutGroup
    stackRankPayouts.remove( payout );
    int sizeAfterRemovingPayout = stackRankPayouts.size();

    promotionDAO.save( actualPromotion );
    flushAndClearSession();

    // retrieve the promotion
    ProductClaimPromotion stackRankUpdatePromotion = (ProductClaimPromotion)promotionDAO.getPromotionById( actualPromotion.getId() );

    // now get the stackRankPayout size and compare
    Set updateStackRankPayoutGroups = stackRankUpdatePromotion.getStackRankPayoutGroups();
    Iterator updateIter = updateStackRankPayoutGroups.iterator();

    StackRankPayoutGroup updateStackRankPayoutGroup = (StackRankPayoutGroup)updateIter.next();
    Set updateStackRankPayouts = updateStackRankPayoutGroup.getStackRankPayouts();
    int updateStackRankPayoutSize = updateStackRankPayouts.size();

    assertEquals( "Actual promotion's update stack rank payout's size doesn't match with expected", sizeAfterRemovingPayout, updateStackRankPayoutSize );
  }

  /**
   * Tests creating a promotion with approval options.
   */
  public void testSaveWithApprovalOptions()
  {
    // create a new promotion
    PromotionDAO promotionDAO = getPromotionDAO();

    Promotion expectedPromotion = buildProductClaimPromotion( "55" );

    PromotionApprovalOption promoApprovalOption1 = buildPromotionApprovalOption( "approved", false );
    PromotionApprovalOption promoApprovalOption2 = buildPromotionApprovalOption( "held", true );

    expectedPromotion.addPromotionApprovalOption( promoApprovalOption1 );
    expectedPromotion.addPromotionApprovalOption( promoApprovalOption2 );

    promotionDAO.save( expectedPromotion );

    assertEquals( "Actual promotion doesn't match with expected", expectedPromotion, promotionDAO.getPromotionById( expectedPromotion.getId() ) );

    flushAndClearSession();

    // retrieve the promotion
    Promotion actualPromotion = promotionDAO.getPromotionById( expectedPromotion.getId() );

    assertEquals( "Option Size doesn't match", expectedPromotion.getPromotionApprovalOptions().size(), actualPromotion.getPromotionApprovalOptions().size() );

    Iterator promotionApprovalOptionIterator = actualPromotion.getPromotionApprovalOptions().iterator();

    while ( promotionApprovalOptionIterator.hasNext() )
    {
      PromotionApprovalOption promoApprovalOption = (PromotionApprovalOption)promotionApprovalOptionIterator.next();
      if ( promoApprovalOption.equals( promoApprovalOption2 ) )
      {
        // actualPromotion.getPromotionApprovalOptions().remove(promoApprovalOption);
        promotionApprovalOptionIterator.remove();
      }
    }
    promotionDAO.save( actualPromotion );

    flushAndClearSession();

    Promotion checkPromotion = promotionDAO.getPromotionById( expectedPromotion.getId() );

    assertEquals( "Option Size doesn't match", actualPromotion.getPromotionApprovalOptions().size(), checkPromotion.getPromotionApprovalOptions().size() );

  }

  /**
   * Tests creating a promotion with TeamPositions.
   */
  public void testSaveWithTeamPositions()
  {
    // create a new promotion
    PromotionDAO promotionDAO = getPromotionDAO();

    ProductClaimPromotion expectedPromotion = buildProductClaimPromotion( "55" );

    PromotionTeamPosition promoTeamPosition1 = buildPromotionTeamPosition();
    expectedPromotion.addPromotionTeamPosition( promoTeamPosition1 );

    promotionDAO.save( expectedPromotion );

    assertEquals( "Actual promotion doesn't match with expected", expectedPromotion, promotionDAO.getPromotionById( expectedPromotion.getId() ) );

    flushAndClearSession();

    // retrieve the promotion
    ProductClaimPromotion actualPromotion = (ProductClaimPromotion)promotionDAO.getPromotionById( expectedPromotion.getId() );

    assertEquals( "Option Size doesn't match", expectedPromotion.getPromotionTeamPositions().size(), actualPromotion.getPromotionTeamPositions().size() );

    Iterator promotionTeamPositionIterator = actualPromotion.getPromotionTeamPositions().iterator();

    while ( promotionTeamPositionIterator.hasNext() )
    {
      PromotionTeamPosition promoTeamPosition = (PromotionTeamPosition)promotionTeamPositionIterator.next();
      if ( promoTeamPosition.equals( promoTeamPosition1 ) )
      {
        promotionTeamPositionIterator.remove();
      }
    }
    promotionDAO.save( actualPromotion );

    flushAndClearSession();

    ProductClaimPromotion checkPromotion = (ProductClaimPromotion)promotionDAO.getPromotionById( expectedPromotion.getId() );

    assertEquals( "Option Size doesn't match", actualPromotion.getPromotionTeamPositions().size(), checkPromotion.getPromotionTeamPositions().size() );

  }

  /**
   * Tests saving and getting all the promotion records saved.
   */
  public void testGetAll()
  {
    PromotionDAO promotionDAO = getPromotionDAO();

    int count = 0;

    // Get a count of the promotions in the database
    List list = promotionDAO.getAll();
    count = list.size();

    List expectedPromotions = new ArrayList();

    // Create a promotion record to add to the list
    Promotion expectedPromotion = buildProductClaimPromotion( "testSuite" );
    promotionDAO.save( expectedPromotion );
    expectedPromotions.add( expectedPromotion );

    // Create a promotion record to add to the list
    Promotion expectedPromotion2 = buildRecognitionPromotion( "testSuite2" );
    promotionDAO.save( expectedPromotion2 );
    expectedPromotions.add( expectedPromotion2 );

    flushAndClearSession();

    List actualPromotions = promotionDAO.getAll();

    assertEquals( "List of promotions aren't the same size.", expectedPromotions.size() + count, actualPromotions.size() );
  }

  /**
   * Tests getting a {@link PromotionClaimFormStepElementValidation} object by promotion and claim
   * form step element.
   */
  public void testGetPromotionClaimFormStepElementValidation()
  {
    // Create and save a claim.
    Claim claim = ClaimDAOImplTest.buildProductClaim( getUniqueString() );
    claim = getClaimDAO().saveClaim( claim );

    // Get the claim's promotion.
    Promotion promotion = claim.getPromotion();

    // Get the claim's first claim form step element.
    ClaimElement claimElement = (ClaimElement)claim.getClaimElements().get( 0 );
    ClaimFormStepElement claimFormStepElement = claimElement.getClaimFormStepElement();
    claimFormStepElement.setClaimFormElementType( ClaimFormElementType.lookup( ClaimFormElementType.NUMBER_FIELD ) );

    // Create and save a promotion claim form step element validation.
    PromotionClaimFormStepElementValidation pcfsev = new PromotionClaimFormStepElementValidation();
    pcfsev.setPromotion( promotion );
    pcfsev.setClaimFormStepElement( claimFormStepElement );
    pcfsev.setValidationType( PromotionClaimFormStepElementValidationType.lookup( PromotionClaimFormStepElementValidationType.VALIDATE ) );
    pcfsev.setMinValue( new Integer( 0 ) );
    pcfsev.setMaxValue( new Integer( 5 ) );

    // Save the promotion claim form step element validation.
    pcfsev = getPromotionDAO().savePromotionClaimFormStepElementValidation( pcfsev );
    assertTrue( pcfsev.getId() != null );

    // Get the promotion claim form step element validation.
    PromotionClaimFormStepElementValidation pcfsev2 = getPromotionDAO().getPromotionClaimFormStepElementValidation( promotion, claimFormStepElement );
    assertEquals( pcfsev, pcfsev2 );
    assertEquals( pcfsev.getValidationType(), pcfsev2.getValidationType() );
    assertEquals( pcfsev.getMinValue(), pcfsev2.getMinValue() );
    assertEquals( pcfsev.getMaxValue(), pcfsev2.getMaxValue() );
  }

  /**
   * Tests saving and getting all the non expired promotion records saved.
   */
  public void testGetAllNonExpired()
  {
    PromotionDAO promotionDAO = getPromotionDAO();

    int count = 0;

    // Get a count of the promotions in the database
    count = promotionDAO.getAllNonExpired().size();

    List expectedPromotions = new ArrayList();

    // Create a promotion record to add to the list
    Promotion expectedPromotion = buildProductClaimPromotion( "testSuite" );
    promotionDAO.save( expectedPromotion );
    expectedPromotions.add( expectedPromotion );

    // Create a promotion record to add to the list
    Promotion expectedPromotion2 = buildRecognitionPromotion( "testSuite2" );
    promotionDAO.save( expectedPromotion2 );
    expectedPromotions.add( expectedPromotion2 );

    flushAndClearSession();

    List actualPromotions = promotionDAO.getAllNonExpired();

    assertEquals( "List of promotions aren't the same size.", expectedPromotions.size() + count, actualPromotions.size() );
  }

  /**
   * Tests saving and getting all the expired promotion records saved.
   */
  public void testGetAllExpired()
  {
    PromotionDAO promotionDAO = getPromotionDAO();

    int count = 0;

    // Get a count of the promotions in the database
    count = promotionDAO.getAllExpired().size();

    List expectedPromotions = new ArrayList();

    // Create a promotion record to add to the list
    Promotion expectedPromotion = buildExpiredPromotion( "testSuite" );
    promotionDAO.save( expectedPromotion );
    expectedPromotions.add( expectedPromotion );

    // Create a promotion record to add to the list
    Promotion expectedPromotion2 = buildExpiredPromotion( "testSuite2" );
    promotionDAO.save( expectedPromotion2 );
    expectedPromotions.add( expectedPromotion2 );

    flushAndClearSession();

    List actualPromotions = promotionDAO.getAllExpired();

    assertEquals( "List of promotions aren't the same size.", expectedPromotions.size() + count, actualPromotions.size() );
  }

  /**
   * Tests saving and getting all the expired promotion records saved.
   */
  public void testGetAllWithSweepstakes()
  {
    PromotionDAO promotionDAO = getPromotionDAO();

    int count = 0;

    // Get a count of the promotions in the database
    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
    associationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.PROMOTION_SWEEPSTAKES ) );
    count = promotionDAO.getAllWithSweepstakesWithAssociations( associationRequestCollection ).size();

    List expectedPromotions = new ArrayList();

    // Create a promotion record to add to the list
    RecognitionPromotion expectedPromotion = buildRecognitionPromotion( "testSuite" );
    expectedPromotion.setSweepstakesActive( false );
    promotionDAO.save( expectedPromotion );
    // expectedPromotions.add( expectedPromotion );

    // Create a promotion record to add to the list
    RecognitionPromotion expectedPromotion2 = buildRecognitionPromotion( "testSuite2" );
    expectedPromotion2.setSweepstakesActive( true );
    promotionDAO.save( expectedPromotion2 );
    expectedPromotions.add( expectedPromotion2 );

    // Add a quiz promotion
    QuizPromotion expectedPromotion3 = buildQuizPromotion( "testSuite3" );
    expectedPromotion3.setSweepstakesActive( true );
    promotionDAO.save( expectedPromotion3 );
    expectedPromotions.add( expectedPromotion3 );

    flushAndClearSession();

    List actualPromotions = promotionDAO.getAllWithSweepstakes();

    assertEquals( "List of sweepstakes promotions aren't the same size.", expectedPromotions.size() + count, actualPromotions.size() );
  }

  /**
   *
   */
  public void testAddingPromotionWebRulesAudience()
  {

    String unique = buildUniqueString();

    Audience audience = AudienceDAOImplTest.getSavedPaxAudience( unique );

    flushAndClearSession();

    Promotion promotion = PromotionDAOImplTest.getSavedPromotionForTesting( unique );

    PromotionWebRulesAudience promotionWebRulesAudience = new PromotionWebRulesAudience();

    promotionWebRulesAudience.setAudience( audience );

    promotion.addPromotionWebRulesAudience( promotionWebRulesAudience );

    PromotionDAO promotionDAO = getPromotionDAO();
    promotionDAO.save( promotion );

    flushAndClearSession();

  }

  /**
   * Test building, saving and getting a promotion element validation by Id.
   */
  public void testSaveAndGetPromotionClaimFormStepElementValidationById()
  {

    String uniqueString = "TEST" + String.valueOf( System.currentTimeMillis() % 200299343 );

    PromotionDAO promotionDAO = getPromotionDAO();
    ClaimFormDAO claimFormDAO = getClaimFormDAO();

    // Create a claim and some elements.
    ClaimForm claimForm = ClaimFormDAOImplTest.buildClaimFormDomainObjectWithStepsAndElements();
    claimFormDAO.saveClaimForm( claimForm );

    // Create a promotion
    Promotion promotion = PromotionDAOImplTest.buildProductClaimPromotion( uniqueString );
    promotion.setClaimForm( claimForm );
    promotionDAO.save( promotion );

    HibernateSessionManager.getSession().flush();
    HibernateSessionManager.getSession().clear();

    // Prepare the list of claimFormElementValidations
    List expectedElementValidations = new ArrayList();

    // Iterate over the claimForm's Steps
    Iterator claimFormStepIter = claimForm.getClaimFormSteps().iterator();

    try
    {

      while ( claimFormStepIter.hasNext() )
      {

        ClaimFormStep claimFormStep = (ClaimFormStep)claimFormStepIter.next();

        // Iterate over the claimFormStepElements
        Iterator claimFormStepElementIter = claimFormStep.getClaimFormStepElements().iterator();
        boolean changeValidationType = false;
        while ( claimFormStepElementIter.hasNext() )
        {
          ClaimFormStepElement claimFormStepElement = (ClaimFormStepElement)claimFormStepElementIter.next();

          // Create some validation for those elements.
          PromotionClaimFormStepElementValidation pcfse = new PromotionClaimFormStepElementValidation();
          pcfse.setClaimFormStepElement( claimFormStepElement );
          pcfse.setPromotion( promotion );
          if ( changeValidationType )
          {
            pcfse.setValidationType( PromotionClaimFormStepElementValidationType.lookup( "validate" ) );
            if ( claimFormStepElement.getClaimFormElementType().getCode().equals( "number" ) )
            {
              pcfse.setMaxValue( new Integer( 50 ) );
              pcfse.setMaxValue( new Integer( 100 ) );
            }
            if ( claimFormStepElement.getClaimFormElementType().getCode().equals( "date" ) )
            {
              pcfse.setStartDate( new Date() );
            }
            if ( claimFormStepElement.getClaimFormElementType().getCode().equals( "text" ) )
            {
              pcfse.setMaxLength( new Integer( 50 ) );
              pcfse.setStartsWith( "some text" );
              pcfse.setContains( "some text" );
            }
            changeValidationType = false;
          }
          else
          {
            pcfse.setValidationType( PromotionClaimFormStepElementValidationType.lookup( "collect" ) );
            changeValidationType = true;
          }

          promotionDAO.savePromotionClaimFormStepElementValidation( pcfse );
          expectedElementValidations.add( pcfse );
        }
      }

      HibernateSessionManager.getSession().flush();
      HibernateSessionManager.getSession().clear();

    }
    catch( Exception e )
    {
      logger.error( e.getMessage(), e );
    }

    // Build the list of actual claimFormStepElement validations.
    List actualElementValidations = new ArrayList();
    List updatedElementValidations = new ArrayList();
    Iterator claimFormStepIter2 = claimForm.getClaimFormSteps().iterator();

    // Get the validations for the elements.
    while ( claimFormStepIter2.hasNext() )
    {
      ClaimFormStep claimFormStep = (ClaimFormStep)claimFormStepIter2.next();
      List validations = promotionDAO.getAllPromotionClaimFormStepElementValidations( promotion, claimFormStep );
      actualElementValidations.addAll( validations );
    }

    // Assert the elements.
    assertTrue( "The actual list of validations wasn't equals to what was expected.", expectedElementValidations.containsAll( actualElementValidations ) );

    Iterator updateFormValidations = actualElementValidations.iterator();
    while ( updateFormValidations.hasNext() )
    {
      PromotionClaimFormStepElementValidation formValidationElement = (PromotionClaimFormStepElementValidation)updateFormValidations.next();
      if ( formValidationElement.getValidationType().getCode().equals( "validate" ) )
      {
        formValidationElement.setValidationType( PromotionClaimFormStepElementValidationType.lookup( "collect" ) );
        formValidationElement.setMaxValue( null );
        formValidationElement.setMaxValue( null );
        formValidationElement.setMaxLength( null );
        formValidationElement.setStartDate( null );
        formValidationElement.setEndDate( null );
        formValidationElement.setStartsWith( null );
        formValidationElement.setNotStartWith( null );
        formValidationElement.setEndsWith( null );
        formValidationElement.setNotEndWith( null );
        formValidationElement.setContains( null );
        formValidationElement.setNotContain( null );
      }
      else
      {
        if ( formValidationElement.getClaimFormStepElement().getClaimFormElementType().getCode().equals( "number" ) )
        {
          formValidationElement.setMaxValue( new Integer( 50 ) );
          formValidationElement.setMaxValue( new Integer( 100 ) );
        }
        if ( formValidationElement.getClaimFormStepElement().getClaimFormElementType().getCode().equals( "date" ) )
        {
          formValidationElement.setStartDate( new Date() );
        }
        if ( formValidationElement.getClaimFormStepElement().getClaimFormElementType().getCode().equals( "text" ) )
        {
          formValidationElement.setMaxLength( new Integer( 50 ) );
          formValidationElement.setStartsWith( "some text" );
          formValidationElement.setContains( "some text" );
        }
      }
      updatedElementValidations.add( formValidationElement );
      promotionDAO.savePromotionClaimFormStepElementValidation( formValidationElement );
    }
    HibernateSessionManager.getSession().flush();
    HibernateSessionManager.getSession().clear();

    // Build the list of actual claimFormStepElement validations.
    List actualElementValidations2 = new ArrayList();

    // Get the validations for the elements.
    while ( claimFormStepIter2.hasNext() )
    {
      ClaimFormStep claimFormStep = (ClaimFormStep)claimFormStepIter2.next();
      List validations = promotionDAO.getAllPromotionClaimFormStepElementValidations( promotion, claimFormStep );
      actualElementValidations2.addAll( validations );
    }

    // Assert the elements.
    assertTrue( "The actual list of validations wasn't equals to what was expected.", updatedElementValidations.containsAll( actualElementValidations2 ) );

    // Delete the validations created.
    Iterator validationIter = updatedElementValidations.iterator();

    while ( validationIter.hasNext() )
    {
      PromotionClaimFormStepElementValidation pcfsev = (PromotionClaimFormStepElementValidation)validationIter.next();

      promotionDAO.deletePromotionClaimFormStepElementValidation( pcfsev );

    }

    Iterator claimStepListIter = claimForm.getClaimFormSteps().iterator();

    while ( claimStepListIter.hasNext() )
    {
      ClaimFormStep claimFormStep = (ClaimFormStep)claimStepListIter.next();

      List pcfsevList = promotionDAO.getAllPromotionClaimFormStepElementValidations( promotion, claimFormStep );

      assertTrue( "Validation list isn't empty as expected", pcfsevList.isEmpty() );

    }

  }

  /**
   * testIsPromotionNameUnique
   */
  public void testIsPromotionNameUnique()
  {
    String suffix = String.valueOf( System.currentTimeMillis() % 200299343 );

    Promotion promotion = buildProductClaimPromotion( suffix );
    promotion.setName( "testUniqueName" + suffix );

    getPromotionDAO().save( promotion );
    flushAndClearSession();

    assertTrue( getPromotionDAO().isPromotionNameUnique( "Some--Unique--N-a-m-e" + suffix, promotion.getId() ) );
    assertTrue( getPromotionDAO().isPromotionNameUnique( promotion.getPromotionName(), promotion.getId() ) );

    // assertFalse( getPromotionDAO().isPromotionNameUnique( "TESTUNIQUENAME" + suffix, new Long( 34
    // ) ) );
    assertFalse( getPromotionDAO().isPromotionNameUnique( promotion.getPromotionName(), null ) );
    assertFalse( getPromotionDAO().isPromotionNameUnique( promotion.getPromotionName(), new Long( 2 ) ) );
  }

  /**
   * Tests saving and getting all the expired promotion records saved.
   */
  public void testGetAllLiveAndExpired()
  {

    PromotionDAO promotionDAO = getPromotionDAO();

    List expectedPromotions = promotionDAO.getAllLiveAndExpired();

    // make a promotion where test user is in the primary audience
    Promotion promotion = buildProductClaimPromotion( String.valueOf( 1 ) );
    promotion.setPromotionStatus( PromotionStatusType.lookup( PromotionStatusType.UNDER_CONSTRUCTION ) );
    promotionDAO.save( promotion );

    promotion = buildQuizPromotion( String.valueOf( 2 ) );
    promotion.setPromotionStatus( PromotionStatusType.lookup( PromotionStatusType.LIVE ) );
    promotionDAO.save( promotion );
    expectedPromotions.add( promotion );

    promotion = buildRecognitionPromotion( String.valueOf( 3 ) );
    promotion.setPromotionStatus( PromotionStatusType.lookup( PromotionStatusType.EXPIRED ) );
    promotionDAO.save( promotion );
    expectedPromotions.add( promotion );

    promotion = buildRecognitionPromotion( String.valueOf( 4 ) );
    promotion.setPromotionStatus( PromotionStatusType.lookup( PromotionStatusType.LIVE ) );
    promotionDAO.save( promotion );
    expectedPromotions.add( promotion );

    promotion = buildRecognitionPromotion( String.valueOf( 5 ) );
    promotion.setPromotionStatus( PromotionStatusType.lookup( PromotionStatusType.LIVE ) );
    promotionDAO.save( promotion );
    expectedPromotions.add( promotion );

    flushAndClearSession();
    List actualPromotions = promotionDAO.getAllLiveAndExpired();

    assertEquals( expectedPromotions.size(), actualPromotions.size() );
  }

  /**
   * Tests saving and getting all the expired promotion records saved.
   */
  public void testGetAllLiveAndExpiredByType()
  {

    PromotionDAO promotionDAO = getPromotionDAO();

    List expectedPromotions = promotionDAO.getAllLiveAndExpiredByType( PromotionType.RECOGNITION );

    // make a promotion where test user is in the primary audience
    Promotion promotion = buildProductClaimPromotion( String.valueOf( 1 ) );
    promotion.setPromotionStatus( PromotionStatusType.lookup( PromotionStatusType.UNDER_CONSTRUCTION ) );
    promotionDAO.save( promotion );

    promotion = buildQuizPromotion( String.valueOf( 2 ) );
    promotion.setPromotionStatus( PromotionStatusType.lookup( PromotionStatusType.LIVE ) );
    promotionDAO.save( promotion );

    promotion = buildRecognitionPromotion( String.valueOf( 3 ) );
    promotion.setPromotionStatus( PromotionStatusType.lookup( PromotionStatusType.EXPIRED ) );
    promotionDAO.save( promotion );
    expectedPromotions.add( promotion );

    promotion = buildRecognitionPromotion( String.valueOf( 4 ) );
    promotion.setPromotionStatus( PromotionStatusType.lookup( PromotionStatusType.LIVE ) );
    promotionDAO.save( promotion );
    expectedPromotions.add( promotion );

    promotion = buildRecognitionPromotion( String.valueOf( 5 ) );
    promotion.setPromotionStatus( PromotionStatusType.lookup( PromotionStatusType.LIVE ) );
    promotionDAO.save( promotion );
    expectedPromotions.add( promotion );

    flushAndClearSession();
    List actualPromotions = promotionDAO.getAllLiveAndExpiredByType( PromotionType.RECOGNITION );

    assertEquals( expectedPromotions.size(), actualPromotions.size() );
  }

  /**
   * test to add a child promotion
   */
  public void testAddChildProductClaimPromotion()
  {
    String suffix = String.valueOf( System.currentTimeMillis() % 200299343 );

    PromotionDAO promotionDAO = getPromotionDAO();

    ProductClaimPromotion parentPromotion = buildProductClaimPromotion( suffix );

    promotionDAO.save( parentPromotion );
    flushAndClearSession();

    ProductClaimPromotion childPromotion = buildProductClaimPromotion( "child" + suffix );
    childPromotion.setParentPromotion( parentPromotion );
    promotionDAO.save( childPromotion );
    flushAndClearSession();

    Promotion promotion1 = promotionDAO.getPromotionById( parentPromotion.getId() );
    List list = promotionDAO.getChildPromotions( parentPromotion.getId() );

    assertEquals( "Parent Promotion doesn't match", parentPromotion, promotion1 );
    assertEquals( "Child Promotions don't match", childPromotion, list.get( 0 ) );
  }

  public void testRecognitionPromotionWithBudgetMaster()
  {
    // create a new product claim promotion
    PromotionDAO promotionDAO = getPromotionDAO();

    String uniqueString = buildUniqueString();

    // create a new recognition promotion
    RecognitionPromotion expectedPromotion2 = PromotionDAOImplTest.buildRecognitionPromotion( "RECOGNITION" + uniqueString );
    expectedPromotion2.setUpperCaseName( expectedPromotion2.getPromotionName().toUpperCase() );
    Participant pax = ParticipantDAOImplTest.buildUniqueParticipant( uniqueString );
    getParticipantDAO().saveParticipant( pax );
    BudgetMaster budgetMaster = BudgetMasterDAOImplTest.buildAndSaveBudgetMaster( "TestBudget" + uniqueString, BudgetMasterDAOImplTest.buildBudgetSegment(), pax );
    flushAndClearSession();
    expectedPromotion2.setBudgetMaster( budgetMaster );
    promotionDAO.save( expectedPromotion2 );

    flushAndClearSession();

    // retrieve the promotion
    RecognitionPromotion actualPromotion2 = (RecognitionPromotion)promotionDAO.getPromotionById( expectedPromotion2.getId() );

    assertDomainObjectEquals( "Actual promotion doesn't match with expected", expectedPromotion2, actualPromotion2 );

    assertEquals( "Actual promotion BudgetMaster doesn't match with expected", expectedPromotion2.getBudgetMaster(), actualPromotion2.getBudgetMaster() );

  }

  public void testRecognitionPromotionWithPromotionBehaviors()
  {
    PromotionDAO promotionDAO = getPromotionDAO();
    String uniqueString = buildUniqueString();

    // create a new recognition promotion
    RecognitionPromotion expectedPromotion2 = PromotionDAOImplTest.buildRecognitionPromotion( "RECOGNITION" + uniqueString );
    expectedPromotion2.setPromotionBehaviors( new HashSet() );
    expectedPromotion2.addPromotionBehavior( PromoRecognitionBehaviorType.lookup( PromoRecognitionBehaviorType.INNOVATION_CODE ) );
    expectedPromotion2.addPromotionBehavior( PromoRecognitionBehaviorType.lookup( PromoRecognitionBehaviorType.GREAT_IDEA_CODE ) );

    promotionDAO.save( expectedPromotion2 );

    assertEquals( "Actual promotion doesn't match with expected", expectedPromotion2, promotionDAO.getPromotionById( expectedPromotion2.getId() ) );

    // do an update on the saved promotion
    expectedPromotion2.setName( "RECOGNITION_UPDATED" + uniqueString );
    expectedPromotion2.setUpperCaseName( expectedPromotion2.getPromotionName().toUpperCase() );
    promotionDAO.save( expectedPromotion2 );

    flushAndClearSession();

    // retrieve the promotion
    RecognitionPromotion actualPromotion2 = (RecognitionPromotion)promotionDAO.getPromotionById( expectedPromotion2.getId() );

    assertDomainObjectEquals( "Actual promotion doesn't match with expected", expectedPromotion2, actualPromotion2 );

    assertEquals( "Actual promotion behaviors weren't equal to expected. ", actualPromotion2.getPromotionBehaviors(), expectedPromotion2.getPromotionBehaviors() );
  }

  public void testRecognitionPromotionWithCards()
  {
    PromotionDAO promotionDAO = getPromotionDAO();
    String uniqueString = buildUniqueString();
    String uniqueString2 = buildUniqueString();

    // create a new recognition promotion
    RecognitionPromotion expectedPromotion2 = PromotionDAOImplTest.buildRecognitionPromotion( "RECOGNITION" + uniqueString );

    PromotionECard promotionECard = new PromotionECard();
    promotionECard.seteCard( MultimediaDAOImplTest.buildECard( uniqueString ) );
    PromotionECard promotionECard2 = new PromotionECard();
    promotionECard2.seteCard( MultimediaDAOImplTest.buildECard( uniqueString2 ) );

    expectedPromotion2.setPromotionECard( new TreeSet() );
    expectedPromotion2.addECard( promotionECard );
    expectedPromotion2.addECard( promotionECard2 );

    promotionDAO.save( expectedPromotion2 );

    assertEquals( "Actual promotion doesn't match with expected", expectedPromotion2, promotionDAO.getPromotionById( expectedPromotion2.getId() ) );

    // do an update on the saved promotion
    expectedPromotion2.setName( "RECOGNITION_UPDATED" + uniqueString );
    expectedPromotion2.setUpperCaseName( expectedPromotion2.getPromotionName().toUpperCase() );
    promotionDAO.save( expectedPromotion2 );

    try
    {
      flushAndClearSession();
    }
    catch( Exception e )
    {
      System.out.println( e );
    }

    // retrieve the promotion
    RecognitionPromotion actualPromotion2 = (RecognitionPromotion)promotionDAO.getPromotionById( expectedPromotion2.getId() );

    assertDomainObjectEquals( "Actual promotion doesn't match with expected", expectedPromotion2, actualPromotion2 );

    assertEquals( "Actual promotion ecards weren't equal to expected. ", actualPromotion2.getPromotionECard().size(), expectedPromotion2.getPromotionECard().size() );

  }

  public void testRecognitionPromotionWithAudiences()
  {
    PromotionDAO promotionDAO = getPromotionDAO();
    String uniqueString = buildUniqueString();

    // create a new recognition promotion
    RecognitionPromotion expectedPromotion2 = PromotionDAOImplTest.buildRecognitionPromotion( "RECOGNITION" + uniqueString );
    expectedPromotion2.setUpperCaseName( expectedPromotion2.getPromotionName().toUpperCase() );
    Participant pax = ParticipantDAOImplTest.buildUniqueParticipant( uniqueString );
    getParticipantDAO().saveParticipant( pax );
    expectedPromotion2.addPromotionPrimaryAudience( buildAndSavePromotionGiverAudience( pax, buildUniqueString(), expectedPromotion2 ) );
    expectedPromotion2.addPromotionPrimaryAudience( buildAndSavePromotionGiverAudience( pax, buildUniqueString(), expectedPromotion2 ) );
    expectedPromotion2.addPromotionSecondaryAudience( buildAndSavePromotionReceiverAudience( pax, buildUniqueString(), expectedPromotion2 ) );
    expectedPromotion2.addPromotionSecondaryAudience( buildAndSavePromotionReceiverAudience( pax, buildUniqueString(), expectedPromotion2 ) );

    promotionDAO.save( expectedPromotion2 );

    flushAndClearSession();

    // retrieve the promotion
    RecognitionPromotion actualPromotion2 = (RecognitionPromotion)promotionDAO.getPromotionById( expectedPromotion2.getId() );

    assertDomainObjectEquals( "Actual promotion doesn't match with expected", expectedPromotion2, actualPromotion2 );
    assertEquals( "Actual promotion GiverAudiences doesn't match with expected", expectedPromotion2.getPromotionPrimaryAudiences(), actualPromotion2.getPromotionPrimaryAudiences() );
    assertEquals( "Actual promotion ReceiverAudiences doesn't match with expected", expectedPromotion2.getPromotionSecondaryAudiences(), actualPromotion2.getPromotionSecondaryAudiences() );
  }

  public void testRecognitionPromotionWithSweepstakes()
  {
    PromotionDAO promotionDAO = getPromotionDAO();
    String uniqueString = buildUniqueString();

    // create a new recognition promotion
    RecognitionPromotion expectedPromotion2 = PromotionDAOImplTest.buildRecognitionPromotion( "RECOGNITION" + uniqueString );
    expectedPromotion2.setUpperCaseName( expectedPromotion2.getPromotionName().toUpperCase() );
    expectedPromotion2.setPromotionSweepstakes( new HashSet() );
    PromotionSweepstake sweepstake = new PromotionSweepstake();
    sweepstake.setStartDate( new Date() );
    sweepstake.setEndDate( new Date() );
    expectedPromotion2.addPromotionSweepstake( sweepstake );

    promotionDAO.save( expectedPromotion2 );

    flushAndClearSession();

    // retrieve the promotion
    RecognitionPromotion actualPromotion2 = (RecognitionPromotion)promotionDAO.getPromotionById( expectedPromotion2.getId() );

    assertDomainObjectEquals( "Actual promotion doesn't match with expected", expectedPromotion2, actualPromotion2 );

    assertEquals( "Actual promotion sweepstakes doesn't match with expected", expectedPromotion2.getPromotionSweepstakes().size(), actualPromotion2.getPromotionSweepstakes().size() );
  }

  public void testRecognitionPromotionWithCertificates()
  {
    PromotionDAO promotionDAO = getPromotionDAO();
    String uniqueString = buildUniqueString();
    String uniqueString2 = buildUniqueString();

    // create a new recognition promotion
    RecognitionPromotion expectedPromotion = PromotionDAOImplTest.buildRecognitionPromotion( "RECOGNITION" + uniqueString );

    PromotionCert promotionCert = new PromotionCert();
    promotionCert.setCertificateId( "1" );
    PromotionCert promotionCert2 = new PromotionCert();
    promotionCert2.setCertificateId( "2" );

    expectedPromotion.addCertificate( promotionCert );
    expectedPromotion.addCertificate( promotionCert2 );

    promotionDAO.save( expectedPromotion );

    assertEquals( "Actual promotion doesn't match with expected", expectedPromotion, promotionDAO.getPromotionById( expectedPromotion.getId() ) );

    // do an update on the saved promotion
    expectedPromotion.setName( "RECOGNITION_UPDATED" + uniqueString );
    expectedPromotion.setUpperCaseName( expectedPromotion.getPromotionName().toUpperCase() );
    promotionDAO.save( expectedPromotion );

    flushAndClearSession();

    // retrieve the promotion
    RecognitionPromotion actualPromotion = (RecognitionPromotion)promotionDAO.getPromotionById( expectedPromotion.getId() );

    assertDomainObjectEquals( "Actual promotion doesn't match with expected", expectedPromotion, actualPromotion );

    assertEquals( "Actual promotion certificates weren't equal to expected. ", actualPromotion.getPromotionCertificates().size(), expectedPromotion.getPromotionCertificates().size() );

  }

  public void testGoalQuestPromotionSaveAndGetById()
  {
    // create a new goalquest promotion
    PromotionDAO promotionDAO = getPromotionDAO();

    // Promotion testPromo = promotionDAO.getPromotionById( new Long(270) );
    // Iterator test = ((GoalQuestPromotion)testPromo).getManagerOverrideGoalLevels().iterator();
    // while (test.hasNext()) {
    // AbstractGoalLevel goalLevel = (AbstractGoalLevel)test.next();
    // System.out.println(goalLevel);
    // }
    // Iterator test2 = ((GoalQuestPromotion)testPromo).getGoalLevels().iterator();
    // while (test2.hasNext()) {
    // AbstractGoalLevel goalLevel = (AbstractGoalLevel)test2.next();
    // System.out.println(goalLevel);
    // }

    String uniqueString = buildUniqueString();

    Promotion expectedPromotion = PromotionDAOImplTest.buildGoalQuestPromotion( "GOALQUEST" + uniqueString );
    expectedPromotion = promotionDAO.save( expectedPromotion );

    assertEquals( "Actual promotion doesn't match with expected", expectedPromotion, promotionDAO.getPromotionById( expectedPromotion.getId() ) );

    Promotion newCopy = promotionDAO.getPromotionById( expectedPromotion.getId() );
    // do an update on the saved promotion
    newCopy.setName( "GQ_UPDATED: " + uniqueString );
    newCopy.setUpperCaseName( expectedPromotion.getPromotionName().toUpperCase() );
    GoalLevel goalLevel = new GoalLevel();
    goalLevel.setSequenceNumber( 3 );
    /*
     * goalLevel.setGoalLevelName( "Test New Level" ); goalLevel.setGoalLevelDescription(
     * "Test new Description" );
     */
    goalLevel.setAward( new BigDecimal( "22.2" ) );
    ( (GoalQuestPromotion)newCopy ).addGoalLevel( goalLevel );
    promotionDAO.save( newCopy );

    flushAndClearSession();
    // HibernateSessionManager.getSession().

    // retrieve the promotion
    Promotion actualPromotion = promotionDAO.getPromotionById( expectedPromotion.getId() );
    assertEquals( "Number of goal Levels", 3, ( (GoalQuestPromotion)actualPromotion ).getGoalLevels().size() );
    assertEquals( "Number of Manager overide goal levels", 1, ( (GoalQuestPromotion)actualPromotion ).getManagerOverrideGoalLevels().size() );
    assertDomainObjectEquals( "Actual promotion doesn't match with expected", expectedPromotion, actualPromotion );

  }

  /**
   * Tests create, update and selecting the promotion by the Id._pro
   */
  public void testChallengePointPromotionSaveAndGetById()
  {
    // create a new ChallangePoint promotion
    PromotionDAO promotionDAO = getPromotionDAO();

    String uniqueString = buildUniqueString();

    Promotion expectedPromotion = PromotionDAOImplTest.buildChallengePointPromotion( "CHALLENGE_POINT" + uniqueString );
    promotionDAO.save( expectedPromotion );

    assertEquals( "Actual promotion doesn't match with expected", expectedPromotion, promotionDAO.getPromotionById( expectedPromotion.getId() ) );

    // do an update on the saved promotion
    expectedPromotion.setName( "CHALLENGE_POINT_UPDATED: " + uniqueString );
    expectedPromotion.setUpperCaseName( expectedPromotion.getPromotionName().toUpperCase() );
    promotionDAO.save( expectedPromotion );

    flushAndClearSession();

    // retrieve the promotion
    Promotion actualPromotion = promotionDAO.getPromotionById( expectedPromotion.getId() );
    assertDomainObjectEquals( "Actual promotion doesn't match with expected", expectedPromotion, actualPromotion );
  }

  /**
   * Creates a product claim promotion domain object
   * 
   * @param suffix
   * @return Promotion
   */
  public static ProductClaimPromotion buildProductClaimPromotion( String suffix )
  {
    ProductClaimPromotion promotion = new ProductClaimPromotion();

    // Required fields
    promotion.setName( "testPromotion" + suffix );
    promotion.setPromotionType( PromotionType.lookup( PromotionType.PRODUCT_CLAIM ) );
    promotion.setPromotionStatus( PromotionStatusType.lookup( PromotionStatusType.LIVE ) );
    promotion.setApprovalConditionalAmountOperator( ApprovalConditionalAmmountOperatorType.lookup( ApprovalConditionalAmmountOperatorType.EQ ) );
    promotion.setApprovalType( ApprovalType.lookup( ApprovalType.AUTOMATIC_IMMEDIATE ) );
    promotion.setApproverType( ApproverType.lookup( ApproverType.SUBMITTERS_MANAGER ) );
    promotion.setAwardType( PromotionAwardsType.lookup( PromotionAwardsType.POINTS ) );
    promotion.setPromoNameAssetCode( "testAssetCode" );
    ClaimForm claimForm = ClaimFormDAOImplTest.buildClaimFormDomainObjectWithStepsAndElements();

    promotion.setClaimForm( claimForm );
    promotion.setSubmissionStartDate( new Date() );
    promotion.setSubmissionEndDate( new Date() );

    promotion.setWebRulesStartDate( new Date() );
    promotion.setWebRulesEndDate( new Date() );
    promotion.setWebRulesActive( true );
    promotion.setApprovalStartDate( DateUtils.toStartDate( new Date() ) );

    promotion.setPayoutType( PromotionPayoutType.lookup( PromotionPayoutType.ONE_TO_ONE ) );
    promotion.setPromotionProcessingMode( PromotionProcessingModeType.lookup( PromotionProcessingModeType.REAL_TIME ) );

    promotion.setPayoutManagerPeriod( PromoMgrPayoutFreqType.lookup( PromoMgrPayoutFreqType.MONTHLY ) );

    promotion.setWebRulesAudienceType( WebRulesAudienceType.lookup( WebRulesAudienceType.ALL_ACTIVE_PAX_CODE ) );
    return promotion;
  }

  /**
   * Creates a product claim promotion domain object
   * 
   * @param suffix
   * @return Promotion
   */
  public static SSIPromotion buildSSIPromotion( String suffix )
  {
    SSIPromotion promotion = new SSIPromotion();

    // Required fields
    promotion.setName( "testPromotion" + suffix );
    promotion.setPromotionType( PromotionType.lookup( PromotionType.SELF_SERV_INCENTIVES ) );
    promotion.setPromotionStatus( PromotionStatusType.lookup( PromotionStatusType.LIVE ) );
    promotion.setApprovalConditionalAmountOperator( ApprovalConditionalAmmountOperatorType.lookup( ApprovalConditionalAmmountOperatorType.EQ ) );
    promotion.setApprovalType( ApprovalType.lookup( ApprovalType.AUTOMATIC_IMMEDIATE ) );
    promotion.setApproverType( ApproverType.lookup( ApproverType.SUBMITTERS_MANAGER ) );
    promotion.setAwardType( PromotionAwardsType.lookup( PromotionAwardsType.POINTS ) );
    promotion.setPromoNameAssetCode( "testAssetCode" );
    ClaimForm claimForm = ClaimFormDAOImplTest.buildClaimFormDomainObjectWithStepsAndElements();

    promotion.setClaimForm( claimForm );
    promotion.setSubmissionStartDate( new Date() );
    promotion.setSubmissionEndDate( new Date() );

    promotion.setWebRulesStartDate( new Date() );
    promotion.setWebRulesEndDate( new Date() );
    promotion.setWebRulesActive( true );
    promotion.setApprovalStartDate( DateUtils.toStartDate( new Date() ) );
    return promotion;
  }

  /**
   * Creates a recognition promotion domain object
   * 
   * @param suffix
   * @return Promotion
   */
  public static RecognitionPromotion buildRecognitionPromotion( String suffix )
  {
    RecognitionPromotion promotion = new RecognitionPromotion();

    // Required fields
    promotion.setName( "testPromotion" + suffix );

    promotion.setPromotionType( PromotionType.lookup( PromotionType.RECOGNITION ) );
    promotion.setPromotionStatus( PromotionStatusType.lookup( PromotionStatusType.LIVE ) );
    promotion.setApprovalConditionalAmountOperator( ApprovalConditionalAmmountOperatorType.lookup( ApprovalConditionalAmmountOperatorType.EQ ) );
    promotion.setApprovalType( ApprovalType.lookup( ApprovalType.AUTOMATIC_IMMEDIATE ) );
    promotion.setApproverType( ApproverType.lookup( ApproverType.SUBMITTERS_MANAGER ) );
    promotion.setPromoNameAssetCode( "testAssetCode" );
    ClaimForm claimForm = ClaimFormDAOImplTest.buildClaimFormDomainObjectWithStepsAndElements();

    promotion.setClaimForm( claimForm );
    promotion.setSubmissionStartDate( new Date() );
    promotion.setSubmissionEndDate( new Date() );

    promotion.setWebRulesStartDate( new Date() );
    promotion.setWebRulesEndDate( new Date() );
    promotion.setWebRulesActive( true );
    promotion.setApprovalStartDate( DateUtils.toStartDate( new Date() ) );

    // recognition-specific setters for required fields
    // promotion.setIssuanceType( PromotionIssuanceType.lookup( PromotionIssuanceType.ONLINE ) );
    promotion.setOnlineEntry( true );
    promotion.setFileLoadEntry( false );

    promotion.setIncludePurl( false );

    promotion.setIncludeCertificate( false );
    promotion.setCopyRecipientManager( false );
    promotion.setAwardActive( true );
    promotion.setAwardType( PromotionAwardsType.lookup( PromotionAwardsType.POINTS ) );
    promotion.setAwardAmountFixed( new Long( 13 ) );
    promotion.setFileloadBudgetAmount( false );
    promotion.setSweepstakesActive( true );
    promotion.setSweepstakesWinnerEligibilityType( SweepstakesWinnerEligibilityType.lookup( SweepstakesWinnerEligibilityType.GIVERS_ONLY_CODE ) );
    promotion.setSweepstakesMultipleAwardType( SweepstakesMultipleAwardsType.lookup( SweepstakesMultipleAwardsType.MULTIPLE_CODE ) );
    promotion.setBehaviorActive( false );
    promotion.setCardActive( true );
    promotion.setCardClientSetupDone( false );

    promotion.addPromotionBehavior( PromoRecognitionBehaviorType.lookup( PromoRecognitionBehaviorType.INNOVATION_CODE ) );
    promotion.addPromotionBehavior( PromoRecognitionBehaviorType.lookup( PromoRecognitionBehaviorType.GREAT_IDEA_CODE ) );

    PromotionSweepstake sweepstake = new PromotionSweepstake();
    sweepstake.setStartDate( new Date() );
    sweepstake.setEndDate( new Date() );
    sweepstake.setProcessed( false );
    promotion.addPromotionSweepstake( sweepstake );
    return promotion;
  }

  /**
   * Creates a recognition promotion domain object
   * 
   * @param suffix
   * @return Promotion
   */
  public static NominationPromotion buildNominationPromotion( String suffix )
  {
    NominationPromotion promotion = new NominationPromotion();
    promotion.setName( "testPromotion" + suffix );
    // promo basics
    populateNomPromoBasics( promotion, null );
    // form
    populateNomPromoForm( promotion, null );
    // audience

    // awards
    populateNomPromoAwards( promotion, null );

    // sweep
    populateNomPromoSweep( promotion, null );

    // beahaviour
    populateNomPromoBehaviour( promotion, null );

    // ecard and cert
    populateNomPromoEcards( promotion, null );

    // approval
    populateNomPromoApprovals( promotion );

    populateNomPromoApprovalLevels( promotion );

    populateTimePeriod( promotion );

    // notification

    // rule test

    // bill code

    // translation

    // miscellaneous
    promotion.setWebRulesStartDate( new Date() );
    promotion.setWebRulesEndDate( new Date() );
    promotion.setWebRulesActive( true );
    promotion.setFileloadBudgetAmount( false );
    promotion.setPrimaryAudienceType( PrimaryAudienceType.lookup( PrimaryAudienceType.ALL_ACTIVE_PAX_CODE ) );
    promotion.setSecondaryAudienceType( SecondaryAudienceType.lookup( SecondaryAudienceType.ALL_ACTIVE_PAX_CODE ) );

    return promotion;
  }

  public static void populateNomPromoApprovals( NominationPromotion promotion )
  {
    promotion.setApprovalType( ApprovalType.lookup( ApprovalType.AUTOMATIC_IMMEDIATE ) );
    promotion.setApproverType( ApproverType.lookup( ApproverType.SUBMITTERS_MANAGER ) );
    promotion.setApprovalStartDate( DateUtils.toStartDate( new Date() ) );
  }

  public static void populateTimePeriod( NominationPromotion promotion )
  {
    NominationPromotionTimePeriod timePeriod = new NominationPromotionTimePeriod();
    timePeriod.setNominationPromotion( promotion );
    timePeriod.setTimePeriodName( "Time Period Test" );
    timePeriod.setTimePeriodNameAssetCode( "Asset Code" );
    promotion.getNominationTimePeriods().add( timePeriod );
  }

  public static void populateNomPromoApprovalLevels( NominationPromotion promotion )
  {
    NominationPromotionLevel level = new NominationPromotionLevel();
    level.setLevelLabel( "Label 1" );
    level.setLevelLabelAssetCode( "AssetCode" );
    level.setNominationPromotion( promotion );
    level.setAwardPayoutType( PromotionAwardsType.lookup( PromotionAwardsType.POINTS ) );

    promotion.getNominationLevels().add( level );
  }

  public static void populateNomPromoEcards( NominationPromotion promotion, PromoEcards ecards )
  {
    promotion.setCardActive( true );
    promotion.setCardClientSetupDone( false );
    promotion.setIncludeCertificate( false );
  }

  public static void populateNomPromoBehaviour( NominationPromotion promotion, PromoBehaviours behaviours )
  {
    promotion.setBehaviorActive( false );
  }

  public static void populateNomPromoSweep( NominationPromotion promotion, PromoSweeptakes sweeps )
  {
    PromotionSweepstake sweepstake = new PromotionSweepstake();
    sweepstake.setStartDate( new Date() );
    sweepstake.setEndDate( new Date() );
    sweepstake.setProcessed( false );
    promotion.addPromotionSweepstake( sweepstake );
    promotion.setSweepstakesActive( true );
    promotion.setSweepstakesWinnerEligibilityType( SweepstakesWinnerEligibilityType.lookup( SweepstakesWinnerEligibilityType.GIVERS_ONLY_CODE ) );
    promotion.setSweepstakesMultipleAwardType( SweepstakesMultipleAwardsType.lookup( SweepstakesMultipleAwardsType.MULTIPLE_CODE ) );
  }

  public static NominationPromotion populateNomPromoAwards( NominationPromotion promotion, PromoAwards awards )
  {

    if ( awards == null )
    {
      promotion.setAwardActive( true );
      promotion.setAwardType( PromotionAwardsType.lookup( PromotionAwardsType.POINTS ) );
      promotion.setAwardAmountFixed( new Long( 13 ) );
      // promotion.setAwardSpecifierType( NominationAwardSpecifierType.lookup(
      // NominationAwardSpecifierType.APPROVER_AND_NOMINATOR ) );
    }
    else
    {
      promotion.setAwardActive( awards.isAwardsactive() );
      promotion.setAwardType( awards.getAwardType() );
      promotion.setAwardAmountFixed( awards.getAwardAmount() );
      // promotion.setAwardSpecifierType( NominationAwardSpecifierType.lookup(
      // NominationAwardSpecifierType.APPROVER_AND_NOMINATOR ) );
      promotion.setTimePeriodActive( awards.isTimePeriodActive() );
      promotion.setNominationTimePeriods( new HashSet<NominationPromotionTimePeriod>( awards.getTimePeriods() ) );

    }
    return promotion;
  }

  public static void populateNomPromoForm( NominationPromotion promotion, PromoFormRules formRules )
  {
    ClaimForm claimForm = ClaimFormDAOImplTest.buildClaimFormDomainObjectWithStepsAndElements();
    promotion.setClaimForm( claimForm );
  }

  public static NominationPromotion populateNomPromoBasics( NominationPromotion promotion, PromotionBasics basics )
  {

    if ( basics == null )
    {
      promotion.setPromotionType( PromotionType.lookup( PromotionType.NOMINATION ) );
      promotion.setPromotionStatus( PromotionStatusType.lookup( PromotionStatusType.UNDER_CONSTRUCTION ) );
      promotion.setApprovalConditionalAmountOperator( ApprovalConditionalAmmountOperatorType.lookup( ApprovalConditionalAmmountOperatorType.EQ ) );
      promotion.setPromoNameAssetCode( "testAssetCode" );
      promotion.setSubmissionStartDate( new Date() );
      promotion.setSubmissionEndDate( new Date() );
      promotion.setSelfNomination( false );
      promotion.setAwardGroupType( NominationAwardGroupType.lookup( NominationAwardGroupType.INDIVIDUAL ) );
      promotion.setEvaluationType( NominationEvaluationType.lookup( NominationEvaluationType.INDEPENDENT ) );
    }
    else
    {
      promotion.setName( basics.getPromoName() );
      promotion.setPromotionType( basics.getPromoType() );
      promotion.setPromotionStatus( basics.getStatusType() );
      promotion.setApprovalConditionalAmountOperator( basics.getApprovalConditionalAmountOperator() );
      promotion.setPromoNameAssetCode( basics.getPromoNameAssetCode() );
      promotion.setSubmissionStartDate( basics.getSubmissionStartDate() );
      promotion.setSubmissionEndDate( basics.getSubmissionEndDate() );
      promotion.setSelfNomination( basics.isSelfNomination() );
      promotion.setAwardGroupType( basics.getAwardGroupType() );
      promotion.setEvaluationType( basics.getEvaluationType() );
    }
    return promotion;
  }

  /**
   * Creates a goalquest promotion domain object
   * 
   * @param suffix
   * @return Promotion
   */
  public static GoalQuestPromotion buildGoalQuestPromotion( String suffix )
  {
    GoalQuestPromotion promotion = new GoalQuestPromotion();

    // Required fields
    promotion.setName( "testPromotion" + suffix );
    promotion.setPromotionType( PromotionType.lookup( PromotionType.GOALQUEST ) );
    promotion.setPromotionStatus( PromotionStatusType.lookup( PromotionStatusType.LIVE ) );
    // promotion.setApprovalConditionalAmountOperator( ApprovalConditionalAmmountOperatorType
    // .lookup( ApprovalConditionalAmmountOperatorType.EQ ) );
    // promotion.setApprovalType( ApprovalType.lookup( ApprovalType.AUTOMATIC_IMMEDIATE ) );
    // promotion.setApproverType( ApproverType.lookup( ApproverType.SUBMITTERS_MANAGER ) );
    promotion.setAwardType( PromotionAwardsType.lookup( PromotionAwardsType.TRAVEL_AWARD ) );
    promotion.setPromoNameAssetCode( "GQ Promo Name" );
    promotion.setPromoNameAssetCode( "testAssetCode" );
    ClaimForm claimForm = ClaimFormDAOImplTest.buildClaimFormDomainObjectWithStepsAndElements();

    claimFormDao.saveClaimForm( claimForm );

    promotion.setClaimForm( claimForm );
    promotion.setSubmissionStartDate( new Date() );
    promotion.setSubmissionEndDate( new Date() );

    promotion.setWebRulesStartDate( new Date() );
    promotion.setWebRulesEndDate( new Date() );
    promotion.setWebRulesActive( true );
    // promotion.setApprovalStartDate( DateUtils.toStartDate( new Date() ) );

    // promotion.setPayoutType( PromotionPayoutType.lookup( PromotionPayoutType.ONE_TO_ONE ) );
    // promotion.setPromotionProcessingMode( PromotionProcessingModeType
    // .lookup( PromotionProcessingModeType.REAL_TIME ) );
    //
    // promotion.setPayoutManagerPeriod( PromoMgrPayoutFreqType
    // .lookup( PromoMgrPayoutFreqType.MONTHLY ) );

    promotion.setWebRulesAudienceType( WebRulesAudienceType.lookup( WebRulesAudienceType.ALL_ACTIVE_PAX_CODE ) );

    promotion.setGoalCollectionStartDate( new Date() );
    promotion.setGoalCollectionEndDate( new Date() );
    promotion.setAchievementRule( AchievementRuleType.lookup( AchievementRuleType.FIXED ) );
    promotion.setPayoutStructure( PayoutStructure.lookup( PayoutStructure.BOTH ) );
    promotion.setRoundingMethod( RoundingMethod.lookup( RoundingMethod.DOWN ) );
    promotion.setAchievementPrecision( AchievementPrecision.lookup( AchievementPrecision.ONE ) );
    promotion.setOverrideStructure( ManagerOverrideStructure.lookup( ManagerOverrideStructure.AWARD_PER_ACHIEVER ) );
    promotion.setFinalProcessDate( new Date() );
    promotion.setObjective( "Promotion Objective" );
    promotion.setObjectiveAssetCode( "goal_quest_cp_promo_objective.testobjv" );

    GoalLevel goalLevel = new GoalLevel();
    goalLevel.setSequenceNumber( 1 );
    goalLevel.setId( new Long( 1 ) );
    /*
     * goalLevel.setGoalLevelName( "Test Name" ); goalLevel.setGoalLevelDescription( "Test Desc" );
     */
    goalLevel.setAward( new BigDecimal( "10.5" ) );
    goalLevel.setMinimumQualifier( new BigDecimal( "10" ) );
    goalLevel.setBonusAward( new Integer( "10" ) );
    promotion.addGoalLevel( goalLevel );
    goalLevel = new GoalLevel();
    goalLevel.setId( new Long( 2 ) );
    goalLevel.setSequenceNumber( 2 );
    /*
     * goalLevel.setGoalLevelName( "Test Name 2" ); goalLevel.setGoalLevelDescription( "Test Desc 2"
     * );
     */
    goalLevel.setAward( new BigDecimal( "10.6" ) );
    goalLevel.setMinimumQualifier( new BigDecimal( "5" ) );
    goalLevel.setBonusAward( new Integer( "10" ) );
    promotion.addGoalLevel( goalLevel );

    ManagerOverrideGoalLevel managerOverrideGoalLevel = new ManagerOverrideGoalLevel();
    managerOverrideGoalLevel.setSequenceNumber( 1 );
    managerOverrideGoalLevel.setTeamAchievementPercent( new BigDecimal( "33.4" ) );
    promotion.addManagerOverrideGoalLevel( managerOverrideGoalLevel );

    return promotion;
  }

  public static GoalQuestPromotion buildGoalQuestPromotionForFileLoad( String suffix )
  {
    GoalQuestPromotion promotion = new GoalQuestPromotion();

    // Required fields
    promotion.setName( "testPromotion" + suffix );
    promotion.setPromotionType( PromotionType.lookup( PromotionType.GOALQUEST ) );
    promotion.setPromotionStatus( PromotionStatusType.lookup( PromotionStatusType.LIVE ) );
    promotion.setAwardType( PromotionAwardsType.lookup( PromotionAwardsType.POINTS ) );
    promotion.setPromoNameAssetCode( "testAssetCode" );
    ClaimForm claimForm = ClaimFormDAOImplTest.buildClaimFormDomainObjectWithStepsAndElements();

    promotion.setClaimForm( claimForm );
    promotion.setSubmissionStartDate( new Date() );
    promotion.setSubmissionEndDate( new Date() );

    promotion.setWebRulesStartDate( new Date() );
    promotion.setWebRulesEndDate( new Date() );
    promotion.setWebRulesActive( true );

    promotion.setWebRulesAudienceType( WebRulesAudienceType.lookup( WebRulesAudienceType.ALL_ACTIVE_PAX_CODE ) );

    promotion.setGoalCollectionStartDate( new Date() );
    promotion.setGoalCollectionEndDate( new Date() );
    promotion.setAchievementRule( AchievementRuleType.lookup( AchievementRuleType.PERCENT_OF_BASE ) );
    promotion.setPayoutStructure( PayoutStructure.lookup( PayoutStructure.BOTH ) );
    promotion.setRoundingMethod( RoundingMethod.lookup( RoundingMethod.DOWN ) );
    promotion.setAchievementPrecision( AchievementPrecision.lookup( AchievementPrecision.ONE ) );
    promotion.setOverrideStructure( ManagerOverrideStructure.lookup( ManagerOverrideStructure.AWARD_PER_ACHIEVER ) );
    promotion.setFinalProcessDate( new Date() );

    GoalLevel goalLevel = new GoalLevel();
    /*
     * goalLevel.setSequenceNumber( 1 ); goalLevel.setGoalLevelName( "Level One" );
     * goalLevel.setGoalLevelDescription( "Level One Desc" ); goalLevel.setAward( new BigDecimal(
     * "10.5" ) ); goalLevel.setMinimumQualifier( new BigDecimal( "10" ) ); goalLevel.setBonusAward(
     * new Integer( "10" ) ); promotion.getGoalLevels().add( goalLevel ); goalLevel = new
     * GoalLevel(); goalLevel.setSequenceNumber( 2 ); goalLevel.setGoalLevelName( "Level Two" );
     * goalLevel.setGoalLevelDescription( "Level Two Desc" ); goalLevel.setAward( new BigDecimal(
     * "10.6" ) ); goalLevel.setMinimumQualifier( new BigDecimal( "5" ) );
     */
    goalLevel.setBonusAward( new Integer( "10" ) );
    promotion.getGoalLevels().add( goalLevel );

    ManagerOverrideGoalLevel managerOverrideGoalLevel = new ManagerOverrideGoalLevel();
    managerOverrideGoalLevel.setSequenceNumber( 1 );
    managerOverrideGoalLevel.setTeamAchievementPercent( new BigDecimal( "33.4" ) );
    promotion.getManagerOverrideGoalLevels().add( managerOverrideGoalLevel );

    return promotion;
  }

  /**
   * Creates a goalquest promotion domain object
   * 
   * @param suffix
   * @return Promotion
   */
  public static ChallengePointPromotion buildChallengePointPromotion( String suffix )
  {
    ChallengePointPromotion promotion = new ChallengePointPromotion();

    // Required fields
    promotion.setName( "testPromotion" + suffix );
    promotion.setPromotionType( PromotionType.lookup( PromotionType.CHALLENGE_POINT ) );
    promotion.setPromotionStatus( PromotionStatusType.lookup( PromotionStatusType.LIVE ) );
    // challengPoint Primary award type is always points
    promotion.setAwardType( PromotionAwardsType.lookup( PromotionAwardsType.POINTS ) );
    promotion.setChallengePointAwardType( ChallengePointAwardType.lookup( ChallengePointAwardType.POINTS ) );
    promotion.setSubmissionStartDate( new Date() );
    promotion.setSubmissionEndDate( new Date() );
    promotion.setPromoNameAssetCode( "ChallengePoint Promo Name" );
    promotion.setPromoNameAssetCode( "testAssetCode" );
    promotion.setWebRulesStartDate( new Date() );
    promotion.setWebRulesEndDate( new Date() );
    promotion.setWebRulesActive( true );
    promotion.setObjective( "Promotion Objective" );
    promotion.setObjectiveAssetCode( "goal_quest_cp_promo_objective.testobjv" );

    promotion.setWebRulesAudienceType( WebRulesAudienceType.lookup( WebRulesAudienceType.ALL_ACTIVE_PAX_CODE ) );
    promotion.setManagerCanSelect( new Boolean( false ) );
    promotion.setGoalCollectionStartDate( new Date() );
    promotion.setGoalCollectionEndDate( new Date() );
    promotion.setAwardThresholdType( ChallengePointPromotion.PRIMARY_AWARD_THRESHOLD_FIXED );
    promotion.setAwardThresholdValue( new Integer( 50 ) );
    promotion.setAwardIncrementType( ChallengePointPromotion.PRIMARY_AWARD_INCREMENT_FIXED );
    promotion.setAwardIncrementValue( new Integer( 40 ) );
    promotion.setAwardPerIncrement( new Integer( 10 ) );
    promotion.setAchievementRule( AchievementRuleType.lookup( AchievementRuleType.FIXED ) );
    promotion.setRoundingMethod( RoundingMethod.lookup( RoundingMethod.DOWN ) );
    promotion.setAchievementPrecision( AchievementPrecision.lookup( AchievementPrecision.ONE ) );
    promotion.setOverrideStructure( ManagerOverrideStructure.lookup( ManagerOverrideStructure.AWARD_PER_ACHIEVER ) );
    promotion.setFinalProcessDate( new Date() );

    return promotion;
  }

  /**
   * creates an expired promotion domain object
   * 
   * @param suffix
   * @return Promotion
   */
  public static Promotion buildExpiredPromotion( String suffix )
  {
    Promotion promotion = buildProductClaimPromotion( suffix );

    promotion.setPromotionStatus( PromotionStatusType.lookup( PromotionStatusType.EXPIRED ) );

    return promotion;
  }

  /**
   * Creates a PromotionApprovalOption domain object
   * 
   * @param code
   * @param buildReason
   * @return PromotionApprovalOption
   */
  public static PromotionApprovalOption buildPromotionApprovalOption( String code, boolean buildReason )
  {
    PromotionApprovalOption promoApprovalOption = new PromotionApprovalOption();

    // Required fields
    promoApprovalOption.setPromotionApprovalOptionType( PromotionApprovalOptionType.lookup( code ) );

    if ( buildReason )
    {
      PromotionApprovalOptionReason promoApprovalOptionReason = buildPromotionApprovalOptionReason();
      promoApprovalOption.addPromotionApprovalOptionReason( promoApprovalOptionReason );
    }
    return promoApprovalOption;
  }

  /**
   * Creates a PromotionApprovalOptionReason domain object
   * 
   * @return PromotionApprovalOptionReason
   */
  public static PromotionApprovalOptionReason buildPromotionApprovalOptionReason()
  {
    PromotionApprovalOptionReason promoApprovalOptionReason = new PromotionApprovalOptionReason();

    // Required fields
    promoApprovalOptionReason.setPromotionApprovalOptionReasonType( PromotionApprovalOptionReasonType.lookup( "REASON_CODE1" ) );

    return promoApprovalOptionReason;
  }

  /**
   * Builds a promotionTeamPositionType with a previously saved Promotion.
   * 
   * @return PromotionTeamPosition
   */
  public static PromotionTeamPosition buildPromotionTeamPosition()
  {

    PromotionTeamPosition promotionTeamPosition = new PromotionTeamPosition();
    promotionTeamPosition.setRequired( true );
    // promotionTeamPosition.setPromotion( promotion );
    promotionTeamPosition.setPromotionJobPositionType( PromotionJobPositionType.lookup( "sales_rep" ) );

    return promotionTeamPosition;
  }

  /**
   * build a PromotionPrimaryAudience.
   * 
   * @param pax
   * @param uniqueString
   * @param promotion
   * 
   * @return PromotionPrimaryAudience
   */
  public static PromotionPrimaryAudience buildAndSavePromotionGiverAudience( Participant pax, String uniqueString, Promotion promotion )
  {
    Audience audience = AudienceDAOImplTest.getPaxAudience( pax, uniqueString );
    getAudienceDAO().save( audience );
    return new PromotionPrimaryAudience( audience, promotion );
  }

  /**
   * build a PromotionSecondaryAudience.
   * @param pax
   * @param uniqueString
   * @param promotion
   * 
   * @return PromotionSecondaryAudience
   */
  public static PromotionSecondaryAudience buildAndSavePromotionReceiverAudience( Participant pax, String uniqueString, Promotion promotion )
  {
    Audience audience = AudienceDAOImplTest.getPaxAudience( pax, uniqueString );
    getAudienceDAO().save( audience );
    return new PromotionSecondaryAudience( audience, promotion );
  }

  /**
   * Creates a product claim promotion domain object
   * 
   * @param suffix
   * @return Promotion
   */
  public static ProductClaimPromotion buildProductClaimPromotionWithStackRank( String suffix )
  {
    NodeType nodeType = NodeTypeDAOImplTest.buildNodeType( suffix );
    getNodeTypeDAO().saveNodeType( nodeType );

    flushAndClearSession();

    ProductClaimPromotion claimPromotion = buildProductClaimPromotion( suffix );

    StackRankPayoutGroup stackRankPayoutGroup = new StackRankPayoutGroup();

    SubmittersToRankType rankType = SubmittersToRankType.lookup( SubmittersToRankType.SELECTED_NODES );
    stackRankPayoutGroup.setSubmittersToRankType( rankType );
    stackRankPayoutGroup.setNodeType( nodeType );

    // create bunch of stackRankPayout obj's
    StackRankPayout stackRankPayout = new StackRankPayout();
    stackRankPayout.setStartRank( 1 );
    stackRankPayout.setEndRank( 10 );
    stackRankPayout.setPayout( 5 );
    stackRankPayoutGroup.addStackRankPayout( stackRankPayout );

    StackRankPayout stackRankPayTwo = new StackRankPayout();
    stackRankPayTwo.setStartRank( 1 );
    stackRankPayTwo.setEndRank( 10 );
    stackRankPayTwo.setPayout( 5 );
    stackRankPayoutGroup.addStackRankPayout( stackRankPayTwo );

    claimPromotion.addStackRankPayoutGroup( stackRankPayoutGroup );
    return claimPromotion;
  }

  /**
   * Tests create, update and selecting the promotion by the Id.
   */
  public void testQuizPromotionSaveAndGetById()
  {
    // create a new quiz promotion
    PromotionDAO promotionDAO = getPromotionDAO();

    String uniqueString = buildUniqueString();

    Promotion expectedPromotion = PromotionDAOImplTest.buildQuizPromotion( "QUIZ" + uniqueString );
    promotionDAO.save( expectedPromotion );

    assertEquals( "Actual promotion doesn't match with expected", expectedPromotion, promotionDAO.getPromotionById( expectedPromotion.getId() ) );

    // do an update on the saved promotion
    expectedPromotion.setName( "QUIZ_UPDATED: " + uniqueString );
    expectedPromotion.setUpperCaseName( expectedPromotion.getPromotionName().toString() );
    promotionDAO.save( expectedPromotion );

    flushAndClearSession();

    // retrieve the promotion
    Promotion actualPromotion = promotionDAO.getPromotionById( expectedPromotion.getId() );
    // assertDomainObjectEquals( "Bean properties were not equal for property name:upperCaseName.
    // expected", expectedPromotion, actualPromotion );

    assertDomainObjectEquals( "Actual promotion ClaimForm doesn't match with expected", expectedPromotion.getClaimForm(), actualPromotion.getClaimForm() );
  }

  /**
   * Tests create, update and selecting the product by the Id.
   */
  public void testSaveAndRetreiveSweepstake()
  {
    String uniqueString = "Test" + ( System.currentTimeMillis() % 3122131 );
    Promotion promotion = PromotionDAOImplTest.buildRecognitionPromotion( uniqueString );

    // create sweepstake and save
    PromotionSweepstake expectedPromotionSweepstake = PromotionSweepstakeDAOImplTest.buildPromotionSweepstake( promotion );

    PromotionDAO promotionDAO = getPromotionDAO();
    promotion = promotionDAO.save( promotion );

    Set sweepstakes = promotion.getPromotionSweepstakes();
    PromotionSweepstake actualPromotionSweepstake = null;
    for ( Iterator iter = sweepstakes.iterator(); iter.hasNext(); )
    {
      actualPromotionSweepstake = (PromotionSweepstake)iter.next();
      expectedPromotionSweepstake.setId( actualPromotionSweepstake.getId() );
      expectedPromotionSweepstake.setGuid( actualPromotionSweepstake.getGuid() );
    }

    flushAndClearSession();

    // Confirm objects are retrieved
    assertEquals( "Actual sweepstake doesn't match with expected.", expectedPromotionSweepstake, actualPromotionSweepstake );

    assertEquals( "Actual promotion doesn't match with expected.", expectedPromotionSweepstake.getPromotion(), actualPromotionSweepstake.getPromotion() );

    assertEquals( "Actual start date doesn't match with expected.",
                  DateUtils.toStartDate( expectedPromotionSweepstake.getStartDate() ),
                  DateUtils.toStartDate( actualPromotionSweepstake.getStartDate() ) );

    // assertEquals( "Actual end date match with expected.",
    // expectedPromotionSweepstake.getEndDate().getTime(),
    // actualPromotionSweepstake.getEndDate().getTime() );

  }

  /**
   * Tests create, update and selecting the promotion by the Id.
   */
  public void testSurveyPromotionSaveAndGetById()
  {
    // create a new survey promotion
    PromotionDAO promotionDAO = getPromotionDAO();

    String uniqueString = buildUniqueString();

    Promotion expectedPromotion = PromotionDAOImplTest.buildSurveyPromotion( "SURVEY" + uniqueString );
    promotionDAO.save( expectedPromotion );

    assertEquals( "Actual promotion doesn't match with expected", expectedPromotion, promotionDAO.getPromotionById( expectedPromotion.getId() ) );

    // do an update on the saved promotion
    expectedPromotion.setName( "SURVEY_UPDATED: " + uniqueString );
    expectedPromotion.setUpperCaseName( expectedPromotion.getPromotionName().toString() );
    promotionDAO.save( expectedPromotion );

    flushAndClearSession();

    // retrieve the promotion
    Promotion actualPromotion = promotionDAO.getPromotionById( expectedPromotion.getId() );
    // assertDomainObjectEquals( "Actual promotion doesn't match with expected", expectedPromotion,
    // actualPromotion );

  }

  public void testGetECards()
  {
    // Need a nom promo for the card settings
    String uniqueString = buildUniqueString();
    NominationPromotion testPromotion = PromotionDAOImplTest.buildNominationPromotion( "NOMINATION" + uniqueString );
    testPromotion.setDrawYourOwnCard( true );

    // Card setup
    ECard card = new ECard();
    card.setActive( true );
    card.setId( 99999L );
    card.setLargeImageName( "largeImage.ext" );
    card.setSmallImageName( "smallImage.ext" );
    card.setName( "submit_card_test" );
    PromotionECard promoCard = new PromotionECard();
    promoCard.seteCard( card );
    promoCard.setPromotion( testPromotion );
    testPromotion.addECard( promoCard );

    getPromotionDAO().save( testPromotion );
    flushAndClearSession();

    // Getting cards for the promotion we just created should return the card we just created (and
    // only that card)
    List<NominationSubmitDataECardValueBean> cardList = getPromotionDAO().getECards( testPromotion.getId(), "en_US" );
    assertTrue( cardList != null && cardList.size() == 1 );
    assertTrue( cardList.get( 0 ).getCardType().equals( "card" ) );
    assertTrue( cardList.get( 0 ).isCanEdit() );
    assertTrue( cardList.get( 0 ).getName().equals( "submit_card_test" ) );
  }

  public void testGetDrawToolSettings()
  {
    // Need a nom promo for the config settings
    String uniqueString = buildUniqueString();
    NominationPromotion testPromotion = PromotionDAOImplTest.buildNominationPromotion( "NOMINATION" + uniqueString );
    testPromotion.setDrawYourOwnCard( true );
    testPromotion.setAllowYourOwnCard( false );

    getPromotionDAO().save( testPromotion );
    flushAndClearSession();

    // Get settings. Make sure the boolean flags came back as expected.
    NominationSubmitDataDrawSettingsValueBean drawSettings = getPromotionDAO().getDrawToolSettings( testPromotion.getId() );
    assertTrue( "Draw Card setting wrong", drawSettings.isCanDraw() );
    assertFalse( "Upload Card setting wrong", drawSettings.isCanUpload() );
    assertNotNull( drawSettings.getSizes() );
    assertNotNull( drawSettings.getColors() );
  }

  public void testGetBehaviorTypes()
  {
    // List of behaviors we're going to add to begin with. These become the expected values.

    List<PromotionBehaviorsValueBean> expectedTypes = new ArrayList<PromotionBehaviorsValueBean>();
    expectedTypes.add( new PromotionBehaviorsValueBean( PromoNominationBehaviorType.LEADERSHIP_CODE, PromoNominationBehaviorType.LEADERSHIP_CODE, "1" ) );
    expectedTypes.add( new PromotionBehaviorsValueBean( PromoNominationBehaviorType.SUPPORTIVE_CODE, PromoNominationBehaviorType.SUPPORTIVE_CODE, "2" ) );

    // Need a nom promo for the behavior settings
    String uniqueString = buildUniqueString();
    NominationPromotion testPromotion = PromotionDAOImplTest.buildNominationPromotion( "NOMINATION" + uniqueString );
    testPromotion.setBehaviorActive( true );

    for ( PromotionBehaviorsValueBean type : expectedTypes )
    {
      testPromotion.addPromotionBehavior( PromoNominationBehaviorType.lookup( type.getPromoNominationBehaviorTypeCode() ), type.getBehaviorOrder() );
    }

    getPromotionDAO().save( testPromotion );
    flushAndClearSession();

    // DAO call should now give list containing behavior codes we just added
    List<String> behaviorTypes = getPromotionDAO().getBehaviorTypes( testPromotion.getId() );

    List<String> expectedTypeCodes = new ArrayList<>();
    expectedTypeCodes.add( PromoNominationBehaviorType.LEADERSHIP_CODE );
    expectedTypeCodes.add( PromoNominationBehaviorType.SUPPORTIVE_CODE );

    assertEquals( behaviorTypes, expectedTypeCodes );
  }

  public static Participant buildParticipant( long id )
  {
    Participant participant = new Participant();
    participant.setId( new Long( id ) );
    participant.setFirstName( String.valueOf( id ) );
    participant.setLastName( String.valueOf( id ) );
    participant.setUserName( String.valueOf( id ) );
    participant.setUserType( UserType.lookup( UserType.PARTICIPANT ) );
    return participant;
  }

  /**
   * Builds a static promotion audience with a given promotionId and returns it
   * @param promotion
   * @param audience
   * @param primary
   * @return PromotionAudience
   */
  public static PromotionAudience buildPromotionAudience( Promotion promotion, Audience audience, boolean primary )
  {
    PromotionAudience promotionAudience;
    if ( primary )
    {
      promotionAudience = new PromotionPrimaryAudience();
      promotionAudience.setPromotion( promotion );
      promotionAudience.setAudience( audience );
    }
    else
    {
      promotionAudience = new PromotionSecondaryAudience();
      promotionAudience.setPromotion( promotion );
      promotionAudience.setAudience( audience );

    }

    return promotionAudience;
  }

  /**
   * Builds a static promotion with a unique string, saves the promotion and returns it.
   * 
   * @param uniqueString
   * @return Promotion
   */
  public static Promotion getSavedPromotionForTesting( String uniqueString )
  {
    return getPromotionDAO().save( PromotionDAOImplTest.buildProductClaimPromotion( uniqueString ) );
  }

  /**
   * Builds a static goalquest promotion with a unique string, saves the promotion and returns it.
   * 
   * @param uniqueString
   * @return Promotion
   */
  public static Promotion getSavedGoalQuestPromotionForTesting( String uniqueString )
  {
    return getPromotionDAO().save( PromotionDAOImplTest.buildGoalQuestPromotion( uniqueString ) );
  }

  /**
   * Creates a survey promotion domain object
   * 
   * @param suffix
   * @return Promotion
   */
  public static SurveyPromotion buildSurveyPromotion( String suffix )
  {
    SurveyPromotion promotion = new SurveyPromotion();

    promotion.setName( "testPromotion" + suffix );
    promotion.setPromotionType( PromotionType.lookup( PromotionType.SURVEY ) );
    promotion.setPromotionStatus( PromotionStatusType.lookup( PromotionStatusType.UNDER_CONSTRUCTION ) );
    promotion.setSubmissionStartDate( new Date() );
    promotion.setSubmissionEndDate( new Date() );
    promotion.setAwardType( PromotionAwardsType.lookup( PromotionAwardsType.POINTS ) );
    promotion.setTaxable( true );
    promotion.setPromoNameAssetCode( "testAssetCode" );
    promotion.setSweepstakesActive( true );
    promotion.setSweepstakesPrimaryWinners( new Integer( 4 ) );
    promotion.setSweepstakesPrimaryBasisType( SweepstakesWinnersType.lookup( "percentage" ) );
    promotion.setSweepstakesPrimaryAwardAmount( new Long( 13 ) );
    PromotionSweepstake sweepstake = new PromotionSweepstake();
    sweepstake.setStartDate( new Date() );
    sweepstake.setEndDate( new Date() );
    sweepstake.setProcessed( false );
    promotion.addPromotionSweepstake( sweepstake );

    return promotion;
  }

  /**
   * Creates a quiz promotion domain object
   * 
   * @param suffix
   * @return Promotion
   */
  public static QuizPromotion buildQuizPromotion( String suffix )
  {
    QuizPromotion promotion = new QuizPromotion();

    // Required fields
    promotion.setName( "testPromotion" + suffix );

    promotion.setPromotionType( PromotionType.lookup( PromotionType.QUIZ ) );
    promotion.setPromotionStatus( PromotionStatusType.lookup( PromotionStatusType.UNDER_CONSTRUCTION ) );
    promotion.setApprovalConditionalAmountOperator( ApprovalConditionalAmmountOperatorType.lookup( ApprovalConditionalAmmountOperatorType.EQ ) );
    promotion.setApprovalType( ApprovalType.lookup( ApprovalType.AUTOMATIC_IMMEDIATE ) );
    promotion.setApproverType( ApproverType.lookup( ApproverType.SUBMITTERS_MANAGER ) );
    promotion.setAwardType( PromotionAwardsType.lookup( PromotionAwardsType.POINTS ) );
    promotion.setPromoNameAssetCode( "testAssetCode" );
    Quiz quiz = QuizDAOImplTest.buildCompletedQuiz( "testQuiz" + suffix );
    promotion.setQuiz( quiz );

    promotion.setSubmissionStartDate( new Date() );
    promotion.setSubmissionEndDate( new Date() );

    promotion.setWebRulesStartDate( new Date() );
    promotion.setWebRulesEndDate( new Date() );
    promotion.setWebRulesActive( true );
    promotion.setApprovalStartDate( DateUtils.toStartDate( new Date() ) );

    // quiz-specific setters for required fields
    promotion.setAllowUnlimitedAttempts( true );
    promotion.setMaximumAttempts( 5 );
    promotion.setIncludePassingQuizCertificate( true );
    promotion.setAwardActive( true );
    promotion.setAwardAmount( new Long( 13 ) );
    promotion.setSweepstakesActive( true );
    promotion.setSweepstakesPrimaryWinners( new Integer( 4 ) );
    promotion.setSweepstakesPrimaryBasisType( SweepstakesWinnersType.lookup( "percentage" ) );
    promotion.setSweepstakesPrimaryAwardAmount( new Long( 13 ) );

    return promotion;
  }

  /**
   * Creates a Throwdown promotion domain object
   * 
   * @param suffix
   * @return Promotion
   */
  public static ThrowdownPromotion buildThrowdownPromotion( String suffix )
  {
    ThrowdownPromotion promotion = new ThrowdownPromotion();

    // Required fields
    promotion.setName( "testPromotion" + suffix );
    promotion.setPromoNameAssetCode( "testAssetCode" + suffix );
    promotion.setPromotionType( PromotionType.lookup( PromotionType.THROWDOWN ) );
    promotion.setPromotionStatus( PromotionStatusType.lookup( PromotionStatusType.LIVE ) );
    promotion.setAwardType( PromotionAwardsType.lookup( PromotionAwardsType.POINTS ) );

    promotion.setSubmissionStartDate( new Date() );
    promotion.setSubmissionEndDate( new Date() );

    promotion.setHeadToHeadStartDate( new Date() );
    promotion.setNumberOfRounds( 3 );
    promotion.setLengthOfRound( 3 );
    promotion.setDaysPriorToRoundStartSchedule( 2 );
    promotion.setThrowdownPromotionType( ThrowdownPromotionType.lookup( ThrowdownPromotionType.HEAD_2_HEAD ) );
    promotion.setTeamUnavailableResolverType( TeamUnavailableResolverType.lookup( TeamUnavailableResolverType.MINIMUM_QUALIFIER ) );
    promotion.setDisplayTeamProgress( true );
    promotion.setSmackTalkAvailable( true );

    promotion.setWebRulesStartDate( new Date() );
    promotion.setWebRulesEndDate( new Date() );
    promotion.setWebRulesActive( true );
    promotion.setApprovalStartDate( DateUtils.toStartDate( new Date() ) );

    promotion.setWebRulesAudienceType( WebRulesAudienceType.lookup( WebRulesAudienceType.ALL_ACTIVE_PAX_CODE ) );
    return promotion;
  }

  // ---------------------------------------------------------------------------
  // DAO Getter Methods
  // ---------------------------------------------------------------------------

  private static ParticipantDAO getParticipantDAO()
  {
    return (ParticipantDAO)getDAO( ParticipantDAO.BEAN_NAME );
  }

  /**
   * Uses the ApplicationContextFactory to look up the PromotionDAO implementation.
   * 
   * @return PromotionDAO
   */
  private static PromotionDAO getPromotionDAO()
  {
    return (PromotionDAO)getDAO( PromotionDAO.BEAN_NAME );
  }

  /**
   * Uses the ApplicationContextFactory to look up the PromotionDAO implementation.
   * 
   * @return PromotionDAO
   */
  private static NodeTypeDAO getNodeTypeDAO()
  {
    return (NodeTypeDAO)getDAO( "nodeTypeDAO" );
  }

  /**
   * Returns a reference to the claim DAO.
   * 
   * @return a reference to the claim DAO.
   */
  private ClaimDAO getClaimDAO()
  {
    return (ClaimDAO)ApplicationContextFactory.getApplicationContext().getBean( ClaimDAO.BEAN_NAME );
  }

  /**
   * Uses the ApplicationContextFactory to look up the ClaimFormDAO implementation.
   * 
   * @return PromotionDAO
   */
  private ClaimFormDAO getClaimFormDAO()
  {
    return (ClaimFormDAO)ApplicationContextFactory.getApplicationContext().getBean( ClaimFormDAO.BEAN_NAME );
  }

  private static AudienceDAO getAudienceDAO()
  {
    return (AudienceDAO)getDAO( "audienceDAO" );
  }

  /**
   * Returns a reference to the claim form DAO.
   * 
   * @return a reference to the claim form DAO.
   */
  private static ClaimFormDAO getClaimFormDao()
  {
    return (ClaimFormDAO)ApplicationContextFactory.getApplicationContext().getBean( ClaimFormDAO.BEAN_NAME );
  }

  public static NominationPromotionTimePeriod buildNomPromoPeriod( Integer maxsubmissionAllowed, String timePeriodName, Date startDate, Date endDate, NominationPromotion promo )
  {
    NominationPromotionTimePeriod timePeriod = new NominationPromotionTimePeriod();
    timePeriod.setMaxSubmissionAllowed( maxsubmissionAllowed );
    timePeriod.setTimePeriodName( timePeriodName );
    timePeriod.setTimePeriodStartDate( startDate );
    timePeriod.setTimePeriodEndDate( endDate );
    timePeriod.setNominationPromotion( promo );
    return timePeriod;
  }

  public static PromoAwards buidNomPromoAwards( List<NominationPromotionTimePeriod> timePeriods )
  {
    PromoAwards awards = new PromoAwards();
    if ( timePeriods != null && timePeriods.size() > 0 )
    {
      awards.setTimePeriodActive( true );
      awards.setTimePeriods( timePeriods );
    }
    return awards;
  }

  public void testGetAllUniqueBillCodes()
  {
    PromotionDAO promotionDAO = getPromotionDAO();

    flushAndClearSession();
    assertNotNull( promotionDAO.getAllUniqueBillCodes( 2419L ) );
  }

  public void testGetExpiredPromotions()
  {
    PromotionDAO promotionDAO = getPromotionDAO();
    assertNotNull( promotionDAO.getExpiredPromotions() );
  }

  public void testgetTotalUnapprovedAwardQuantity()
  {
    PromotionDAO promotionDAO = getPromotionDAO();

    // case 1
    Long promotionId = new Long( 10021 );
    Long userId = null;
    Long nodeId = null;
    Long budgetMasterId = null;
    BigDecimal value = promotionDAO.getTotalUnapprovedAwardQuantity( promotionId, userId, nodeId, budgetMasterId );
    assertNotNull( value );

    // case 2

    Long promotionId1 = new Long( 10021 );
    Long userId1 = new Long( 100 );
    Long nodeId1 = null;
    Long budgetMasterId1 = new Long( 5040 );
    promotionDAO.getTotalUnapprovedAwardQuantity( promotionId1, userId1, nodeId1, budgetMasterId1 );
    assertNotNull( value );
    // case3
    Long promotionId2 = new Long( 10021 );
    Long userId2 = null;
    Long nodeId2 = new Long( 100 );
    Long budgetMasterId2 = new Long( 5040 );

    promotionDAO.getTotalUnapprovedAwardQuantity( promotionId2, userId2, nodeId2, budgetMasterId2 );
    assertNotNull( value );
  }

  public void testgetTotalUnapprovedAwardQuantityPurl()
  {
    PromotionDAO promotionDAO = getPromotionDAO();

    // case 1
    Long promotionId = new Long( 10021 );
    Long userId = null;
    Long nodeId = null;
    Long budgetMasterId = null;
    BigDecimal value = promotionDAO.getTotalUnapprovedAwardQuantityPurl( promotionId, userId, nodeId, budgetMasterId );
    assertNotNull( value );

    // case 2

    Long promotionId1 = new Long( 10021 );
    Long userId1 = new Long( 100 );
    Long nodeId1 = null;
    Long budgetMasterId1 = new Long( 5040 );

    promotionDAO.getTotalUnapprovedAwardQuantityPurl( promotionId1, userId1, nodeId1, budgetMasterId1 );
    assertNotNull( value );
    // case3
    Long promotionId2 = new Long( 10021 );
    Long userId2 = null;
    Long nodeId2 = new Long( 100 );
    Long budgetMasterId2 = new Long( 5040 );

    promotionDAO.getTotalUnapprovedAwardQuantityPurl( promotionId2, userId2, nodeId2, budgetMasterId2 );
    assertNotNull( value );
  }

  public void testGetClaimAwardQuantity()
  {
    PromotionDAO promotionDAO = getPromotionDAO();
    Long claimId = new Long( 10021 );
    Long value = promotionDAO.getClaimAwardQuantity( claimId );
    assertNotNull( value );
  }

  public void testGetTotalImportPaxAwardQuantity()
  {
    PromotionDAO promotionDAO = getPromotionDAO();
    Long importFileId = new Long( 100 );
    long awardAmount = promotionDAO.getTotalImportPaxAwardQuantity( importFileId );
    assertNotNull( awardAmount );
  }
}
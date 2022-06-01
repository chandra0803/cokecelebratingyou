/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javatest/com/biperf/core/dao/claim/hibernate/ClaimDAOImplTest.java,v $
 */

package com.biperf.core.dao.claim.hibernate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.support.MutableSortDefinition;
import org.springframework.beans.support.PropertyComparator;

import com.biperf.core.dao.activity.ActivityDAO;
import com.biperf.core.dao.activity.hibernate.ActivityDAOImplTest;
import com.biperf.core.dao.claim.ClaimDAO;
import com.biperf.core.dao.claim.ClaimFormDAO;
import com.biperf.core.dao.hibernate.BaseDAOTest;
import com.biperf.core.dao.hierarchy.NodeDAO;
import com.biperf.core.dao.hierarchy.hibernate.NodeDAOImplTest;
import com.biperf.core.dao.journal.JournalDAO;
import com.biperf.core.dao.journal.hibernate.JournalDAOImplTest;
import com.biperf.core.dao.oracle.OracleSequenceDAO;
import com.biperf.core.dao.participant.ParticipantDAO;
import com.biperf.core.dao.participant.UserDAO;
import com.biperf.core.dao.participant.hibernate.ParticipantDAOImplTest;
import com.biperf.core.dao.product.ProductCategoryDAO;
import com.biperf.core.dao.product.ProductDAO;
import com.biperf.core.dao.product.hibernate.ProductCategoryDAOImplTest;
import com.biperf.core.dao.product.hibernate.ProductDAOImplTest;
import com.biperf.core.dao.promotion.PromotionDAO;
import com.biperf.core.dao.promotion.PromotionParticipantDAO;
import com.biperf.core.dao.promotion.hibernate.PromotionDAOImplTest;
import com.biperf.core.dao.promotion.hibernate.PromotionParticipantDAOImplTest;
import com.biperf.core.dao.promotion.hibernate.PromotionPayoutDAOImplTest;
import com.biperf.core.dao.quiz.QuizDAO;
import com.biperf.core.dao.quiz.hibernate.QuizDAOImplTest;
import com.biperf.core.domain.activity.SalesActivity;
import com.biperf.core.domain.claim.AbstractRecognitionClaim;
import com.biperf.core.domain.claim.Claim;
import com.biperf.core.domain.claim.ClaimElement;
import com.biperf.core.domain.claim.ClaimForm;
import com.biperf.core.domain.claim.ClaimFormStep;
import com.biperf.core.domain.claim.ClaimFormStepElement;
import com.biperf.core.domain.claim.ClaimProduct;
import com.biperf.core.domain.claim.ClaimProductCharacteristic;
import com.biperf.core.domain.claim.ClaimRecipient;
import com.biperf.core.domain.claim.NominationClaim;
import com.biperf.core.domain.claim.ProductClaim;
import com.biperf.core.domain.claim.ProductClaimParticipant;
import com.biperf.core.domain.claim.QuizClaim;
import com.biperf.core.domain.claim.QuizResponse;
import com.biperf.core.domain.claim.RecognitionClaim;
import com.biperf.core.domain.claim.RecognitionClaimSource;
import com.biperf.core.domain.enums.ApprovalStatusType;
import com.biperf.core.domain.enums.JournalStatusType;
import com.biperf.core.domain.enums.NominationClaimStatusType;
import com.biperf.core.domain.enums.PromoNominationBehaviorType;
import com.biperf.core.domain.enums.PromoRecognitionBehaviorType;
import com.biperf.core.domain.enums.PromotionStatusType;
import com.biperf.core.domain.hierarchy.Node;
import com.biperf.core.domain.journal.ActivityJournal;
import com.biperf.core.domain.journal.Journal;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.product.Product;
import com.biperf.core.domain.product.ProductCategory;
import com.biperf.core.domain.product.ProductCharacteristicType;
import com.biperf.core.domain.promotion.AbstractRecognitionPromotion;
import com.biperf.core.domain.promotion.NominationPromotion;
import com.biperf.core.domain.promotion.NominationPromotionTimePeriod;
import com.biperf.core.domain.promotion.ProductClaimPromotion;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.PromotionPayout;
import com.biperf.core.domain.promotion.PromotionPayoutGroup;
import com.biperf.core.domain.promotion.PromotionTeamPosition;
import com.biperf.core.domain.promotion.QuizPromotion;
import com.biperf.core.domain.promotion.RecognitionPromotion;
import com.biperf.core.domain.quiz.Quiz;
import com.biperf.core.domain.quiz.QuizQuestion;
import com.biperf.core.domain.quiz.QuizQuestionAnswer;
import com.biperf.core.domain.user.UserNode;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.claim.ClaimAssociationRequest;
import com.biperf.core.utils.ApplicationContextFactory;
import com.biperf.core.utils.HibernateSessionManager;

/**
 * ClaimDAOImplTest.
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
 * <td>crosenquest</td>
 * <td>Jun 28, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ClaimDAOImplTest extends BaseDAOTest
{

  public static Long PAX_ID = 5583L;

  // ---------------------------------------------------------------------------
  // Fields
  // ---------------------------------------------------------------------------

  ClaimDAO claimDao = getClaimDao();
  ClaimFormDAO claimFormDao = getClaimFormDao();
  NodeDAO nodeDao = getNodeDao();
  ParticipantDAO participantDao = getParticipantDao();
  ProductDAO productDao = getProductDao();
  PromotionDAO promotionDao = getPromotionDao();
  UserDAO userDao = getUserDao();
  OracleSequenceDAO oracleSequenceDAO = getOracleSequenceDao();

  // ---------------------------------------------------------------------------
  // Test Methods
  // ---------------------------------------------------------------------------

  public void testProxyUser()
  {
    //
  }

  /**
   * Tests deleting a claim.
   */
  public void testDeleteClaim()
  {
    // Build a claim.
    String uniqueString = String.valueOf( System.currentTimeMillis() % 5503032 );
    Claim claim = buildProductClaim( uniqueString );

    // Insert the claim into the database.
    claimDao.saveClaim( claim );
    flushAndClearSession();

    // Delete the claim from the database.
    claimDao.deleteClaim( claim );
    flushAndClearSession();

    Claim deletedClaim = claimDao.getClaimById( claim.getId() );
    assertNull( deletedClaim );
  }

  /**
   * Tests saving and getting a claim by the ID.
   */
  public void testSaveUpdateAndGetClaimById()
  {

    // Build a unique string to be used during testing.
    String uniqueString = String.valueOf( System.currentTimeMillis() % 5503032 );

    // Create a promotion.
    Promotion promotion = PromotionDAOImplTest.buildProductClaimPromotion( uniqueString + "promo" );
    promotionDao.save( promotion );

    // Create a claim submitter.
    Participant submitter = ParticipantDAOImplTest.buildUniqueParticipant( uniqueString );
    userDao.saveUser( submitter );

    Participant proxy = ParticipantDAOImplTest.buildUniqueParticipant( uniqueString + "2" );
    userDao.saveUser( proxy );

    // Create a node.
    Node node = NodeDAOImplTest.buildUniqueNode( uniqueString );
    nodeDao.saveNode( node );

    // Create the first claim product.
    Product product1 = ProductDAOImplTest.buildStaticProductDomainObject( uniqueString, ProductDAOImplTest.getProductCategoryDomainObject( uniqueString ) );
    productDao.save( product1 );

    ClaimProduct claimProduct1 = new ClaimProduct( product1, 2 );
    for ( Iterator prodCharIter = product1.getProductCharacteristicTypes().iterator(); prodCharIter.hasNext(); )
    {
      ProductCharacteristicType pct = (ProductCharacteristicType)prodCharIter.next();

      ClaimProductCharacteristic cpc1 = new ClaimProductCharacteristic();
      cpc1.setProductCharacteristicType( pct );
      cpc1.setValue( ( System.currentTimeMillis() % 321312312 ) + "tp11" );

      claimProduct1.addClaimProductCharacteristics( cpc1 );
    }

    // Create the second claim product
    Product product2 = ProductDAOImplTest.buildStaticProductDomainObject( uniqueString + "dcsd", ProductDAOImplTest.getProductCategoryDomainObject( uniqueString + "dcsd" ) );
    productDao.save( product2 );

    ClaimProduct claimProduct2 = new ClaimProduct( product2, 1 );
    for ( Iterator prodCharIter = product2.getProductCharacteristicTypes().iterator(); prodCharIter.hasNext(); )
    {
      ProductCharacteristicType pct = (ProductCharacteristicType)prodCharIter.next();

      ClaimProductCharacteristic cpc2 = new ClaimProductCharacteristic();
      cpc2.setProductCharacteristicType( pct );
      cpc2.setValue( ( System.currentTimeMillis() % 22123102 ) + "tp22" );

      claimProduct2.addClaimProductCharacteristics( cpc2 );
    }

    // Create the third claim product.
    Product product3 = ProductDAOImplTest.buildStaticProductDomainObject( uniqueString + "e3da", ProductDAOImplTest.getProductCategoryDomainObject( uniqueString + "dcsd" ) );
    productDao.save( product3 );

    ClaimProduct claimProduct3 = new ClaimProduct( product3, 9 );
    for ( Iterator prodCharIter = product3.getProductCharacteristicTypes().iterator(); prodCharIter.hasNext(); )
    {
      ProductCharacteristicType pct = (ProductCharacteristicType)prodCharIter.next();

      ClaimProductCharacteristic cpc3 = new ClaimProductCharacteristic();
      cpc3.setProductCharacteristicType( pct );
      cpc3.setValue( ( System.currentTimeMillis() % 55323423 ) + "tp33s" );

      claimProduct3.addClaimProductCharacteristics( cpc3 );
    }

    // Get a claim participant team position.
    PromotionTeamPosition expectedPromotionTeamPosition = PromotionParticipantDAOImplTest.buildPromotionTeamPosition( promotion );

    getPromotionParticipantDAO().savePromotionTeamPosition( expectedPromotionTeamPosition );

    // Create the first claim participant.
    Participant participant1 = ParticipantDAOImplTest.buildUniqueParticipant( uniqueString + "-participant-1" );
    participantDao.saveParticipant( participant1 );
    ProductClaimParticipant productClaimParticipant1 = new ProductClaimParticipant( participant1, expectedPromotionTeamPosition );

    // Create the second claim participant.
    Participant participant2 = ParticipantDAOImplTest.buildUniqueParticipant( uniqueString + "-participant-2" );
    participantDao.saveParticipant( participant2 );
    ProductClaimParticipant productClaimParticipant2 = new ProductClaimParticipant( participant2, expectedPromotionTeamPosition );

    // Create the third claim participant.
    Participant participant3 = ParticipantDAOImplTest.buildUniqueParticipant( uniqueString + "-participant-3" );
    participantDao.saveParticipant( participant3 );

    flushAndClearSession();

    ProductClaimParticipant productClaimParticipant3 = new ProductClaimParticipant( participant3, expectedPromotionTeamPosition );

    // Create a claim.
    ProductClaim expectedClaim = ClaimDAOImplTest.buildStaticProductClaim( true );

    expectedClaim.setSubmitter( submitter );
    expectedClaim.setProxyUser( proxy );
    expectedClaim.setPromotion( promotion );
    expectedClaim.setNode( node );
    expectedClaim.setSubmissionDate( new Date() );
    expectedClaim.setClaimNumber( new Long( oracleSequenceDAO.getOracleSequenceNextValue( "claim_nbr_sq" ) ).toString() );

    expectedClaim.addClaimProduct( claimProduct1 );
    expectedClaim.addClaimProduct( claimProduct2 );
    expectedClaim.addClaimProduct( claimProduct3 );
    assertEquals( 3, expectedClaim.getClaimProducts().size() );

    expectedClaim.addClaimParticipant( productClaimParticipant1 );
    expectedClaim.addClaimParticipant( productClaimParticipant2 );
    expectedClaim.addClaimParticipant( productClaimParticipant3 );

    // add elements
    ClaimFormStep claimFormStep = (ClaimFormStep)expectedClaim.getPromotion().getClaimForm().getClaimFormSteps().get( 0 );
    ClaimFormStepElement claimFormStepElement = (ClaimFormStepElement)claimFormStep.getClaimFormStepElements().get( 0 );

    ClaimElement element1 = new ClaimElement();
    element1.setClaimFormStepElement( claimFormStepElement );
    element1.setValue( "some value" );
    expectedClaim.addClaimElement( element1 );

    // Test saving a claim.
    claimDao.saveClaim( expectedClaim );

    flushAndClearSession();

    assertNotNull( "Id of expectedClaim is null.", expectedClaim.getId() );

    ProductClaim actualClaim = (ProductClaim)claimDao.getClaimById( expectedClaim.getId() );

    ProductClaimParticipant productClaimParticipant = (ProductClaimParticipant)actualClaim.getClaimParticipants().iterator().next();
    Participant proxyParticipant = productClaimParticipant.getParticipant();
    proxyParticipant.setCentraxId( "foo" );
    participantDao.saveParticipant( proxyParticipant );

    assertEquals( "Actual Claim wasn't equal to what was expected.", expectedClaim, actualClaim );
    assertEquals( "Expected claim products to be equal.", actualClaim.getClaimProducts(), expectedClaim.getClaimProducts() );
    assertEquals( "Expected claim participants to be equal.", actualClaim.getClaimParticipants(), expectedClaim.getClaimParticipants() );
    assertNotNull( "Expected node to be not null.", actualClaim.getNode() );
    assertEquals( "Expected nodes to be equal.", actualClaim.getNode(), expectedClaim.getNode() );

    assertNotNull( actualClaim.getClaimElements() );
    ClaimElement actualClaimElement = (ClaimElement)actualClaim.getClaimElements().iterator().next();
    assertEquals( "some value", actualClaimElement.getValue() );
    assertEquals( proxy.getUserName(), actualClaim.getProxyUser().getUserName() );
    HibernateSessionManager.getSession().flush();
    HibernateSessionManager.getSession().clear();

    // ------------------------
    // Test updating a claim.
    // ------------------------
    ProductClaim expectedUpdatedClaim = (ProductClaim)claimDao.getClaimById( expectedClaim.getId() );
    // flushAndClearSession();

    // expectedUpdatedClaim.setClaimNumber( "Tes-sste2-2232" );
    expectedUpdatedClaim.setClaimNumber( new Long( oracleSequenceDAO.getOracleSequenceNextValue( "claim_nbr_sq" ) ).toString() );

    expectedUpdatedClaim.getClaimProducts().remove( new ClaimProduct( expectedUpdatedClaim, product2 ) );
    expectedUpdatedClaim.getClaimParticipants().remove( new ProductClaimParticipant( expectedUpdatedClaim, participant2, expectedPromotionTeamPosition ) );

    // Create the 4th claim product with chars to add to the claim.
    Product product4 = ProductDAOImplTest.buildStaticProductDomainObject( uniqueString + "e444da", ProductDAOImplTest.getProductCategoryDomainObject( uniqueString + "dcasdftd" ) );
    productDao.save( product4 );
    ClaimProduct claimProduct4 = new ClaimProduct( product4, 9 );
    for ( Iterator prodCharIter = product4.getProductCharacteristicTypes().iterator(); prodCharIter.hasNext(); )
    {
      ProductCharacteristicType pct = (ProductCharacteristicType)prodCharIter.next();
      ClaimProductCharacteristic cpc4 = new ClaimProductCharacteristic();
      cpc4.setProductCharacteristicType( pct );
      cpc4.setValue( ( System.currentTimeMillis() % 55323423 ) + "tp33s4" );
      claimProduct4.addClaimProductCharacteristics( cpc4 );
    }
    claimProduct4.addApprover( proxy, new Date(), null, null, null, null, null );
    expectedUpdatedClaim.addClaimProduct( claimProduct4 );

    // //update existing claim product
    // ClaimProduct firstClaimProduct =
    // (ClaimProduct)expectedUpdatedClaim.getClaimProducts().iterator().next();
    // firstClaimProduct.addClaimItemApprover(proxy, new Date());
    //
    // ClaimProductCharacteristic cpc5 = new ClaimProductCharacteristic();
    // ProductCharacteristicType firstProdCharType =
    // (ProductCharacteristicType)firstClaimProduct.getProduct().getProductCharacteristicTypes().iterator().next();
    // cpc5.setProductCharacteristicType( firstProdCharType );
    // cpc5.setValue( ( System.currentTimeMillis() % 55323423 ) + "tp33s4" );
    // firstClaimProduct.addClaimProductCharacteristics( cpc5 );

    // Update the Claim
    expectedUpdatedClaim = (ProductClaim)claimDao.saveClaim( expectedUpdatedClaim );

    flushAndClearSession();

    ProductClaim actualUpdatedClaim = (ProductClaim)claimDao.getClaimById( expectedUpdatedClaim.getId() );

    assertEquals( "Actual updated Claim wasn't equals to what was expected.", expectedUpdatedClaim, actualUpdatedClaim );
    assertEquals( "Expected claim products to be equal.", actualUpdatedClaim.getClaimProducts(), expectedUpdatedClaim.getClaimProducts() );
    assertEquals( "Expected claim participants to be equal.", actualUpdatedClaim.getClaimParticipants(), expectedUpdatedClaim.getClaimParticipants() );
    assertNotNull( "Expected node to be not null.", actualUpdatedClaim.getNode() );
    assertEquals( "Expected nodes to be equal.", actualUpdatedClaim.getNode(), expectedUpdatedClaim.getNode() );

    // Insure newly created characteristics and claim item approvals has been presisted.
    for ( Iterator iter = actualUpdatedClaim.getClaimProducts().iterator(); iter.hasNext(); )
    {
      ClaimProduct claimProduct = (ClaimProduct)iter.next();
      if ( claimProduct.getProduct().equals( product4 ) )
      {
        assertFalse( claimProduct.getApprovableItemApprovers().isEmpty() );
        assertFalse( claimProduct.getClaimProductCharacteristics().isEmpty() );
      }
    }

  }

  /**
   * Tests saving and getting a nomination claim by the ID.
   */
  public void testNominationSaveUpdateAndGetClaimById()
  {
    // Build a unique string to be used during testing.

    String uniqueString = String.valueOf( System.currentTimeMillis() % 5503032 );

    // Create a promotion.
    AbstractRecognitionPromotion promotion = PromotionDAOImplTest.buildNominationPromotion( uniqueString + "promo" );
    promotionDao.save( promotion );

    // Create a claim.
    AbstractRecognitionClaim claim = buildStaticNominationClaim( true );

    testAbstractRecognitionSaveUpdateAndGetClaimById( promotion, claim );

  }

  @SuppressWarnings( { "unchecked", "rawtypes" } )
  public void testsubmitNominationClaim()
  {
    // tx.begin();

    NominationPromotionTimePeriod timePeriod = new NominationPromotionTimePeriod();
    timePeriod.setTimePeriodName( "monthly" );
    Date currentDateTrimmed = com.biperf.core.utils.DateUtils.getCurrentDateTrimmed();
    timePeriod.setTimePeriodStartDate( currentDateTrimmed );
    timePeriod.setTimePeriodEndDate( com.biperf.core.utils.DateUtils.getDateAfterNumberOfDays( currentDateTrimmed, 7 ) );
    timePeriod.setMaxNominationsAllowed( 1 );

    NominationPromotion nomPromotion = PromotionDAOImplTest.buildNominationPromotion( "suffix" );
    nomPromotion.setPromotionStatus( PromotionStatusType.lookup( PromotionStatusType.LIVE ) );
    nomPromotion.setNominationTimePeriods( new HashSet<NominationPromotionTimePeriod>( Arrays.asList( timePeriod ) ) );
    timePeriod.setNominationPromotion( nomPromotion );

    promotionDao.save( nomPromotion );

    Participant submitter = participantDao.getParticipantById( PAX_ID );
    NominationClaim expectedClaim = buildStaticNominationClaim( true );
    expectedClaim.setNominationStatusType( NominationClaimStatusType.lookup( NominationClaimStatusType.COMPLETE ) );
    expectedClaim.setTimPeriod( timePeriod );
    Node node = null;
    String uniqueString = String.valueOf( System.currentTimeMillis() % 5503032 );

    for ( UserNode n : submitter.getUserNodes() )
    {
      node = n.getNode();
      break;

    }

    Participant recipient1 = ParticipantDAOImplTest.buildAndSaveParticipant( "recipient1-" + uniqueString );

    Set userNodes1 = recipient1.getUserNodes();
    Node recipient1Node1 = null;
    Iterator iter1 = userNodes1.iterator();
    if ( iter1.hasNext() )
    {
      recipient1Node1 = ( (UserNode)iter1.next() ).getNode();
    }

    ClaimRecipient claimRecipient1 = new ClaimRecipient();
    claimRecipient1.setRecipient( recipient1 );
    claimRecipient1.setNode( recipient1Node1 );
    claimRecipient1.setAwardQuantity( new Long( 5 ) );

    flushAndClearSession();

    expectedClaim.setSubmitter( submitter );
    expectedClaim.setPromotion( nomPromotion );
    expectedClaim.setNode( node );
    expectedClaim.setSubmissionDate( new Date() );
    expectedClaim.setClaimNumber( new Long( oracleSequenceDAO.getOracleSequenceNextValue( "claim_nbr_sq" ) ).toString() );

    expectedClaim.addClaimRecipient( claimRecipient1 );

    claimDao.saveClaim( expectedClaim );

    flushAndClearSession();

    assertNotNull( "Id of expectedClaim is null.", expectedClaim.getId() );

    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
    associationRequestCollection.add( new ClaimAssociationRequest( ClaimAssociationRequest.CLAIM_RECIPIENTS ) );
    AbstractRecognitionClaim actualClaim = (AbstractRecognitionClaim)claimDao.getClaimByIdWithAssociations( expectedClaim.getId(), associationRequestCollection );

    assertEquals( "Actual Claim wasn't equal to what was expected.", expectedClaim, actualClaim );
    assertNotNull( "Expected node to be not null.", actualClaim.getNode() );
    assertEquals( "Expected nodes to be equal.", actualClaim.getNode(), expectedClaim.getNode() );
    NominationClaim nomClaim = (NominationClaim)actualClaim;
    assertNotNull( nomClaim.getTimPeriod() );

    // tx.commit();
  }

  /**
   * Tests saving and getting a recognition claim by the ID.
   */
  public void testRecognitionSaveUpdateAndGetClaimById()
  {
    // Build a unique string to be used during testing.
    String uniqueString = String.valueOf( System.currentTimeMillis() % 5503032 );

    // Create a promotion.
    AbstractRecognitionPromotion promotion = PromotionDAOImplTest.buildRecognitionPromotion( uniqueString + "promo" );
    promotionDao.save( promotion );

    // Create a claim.
    AbstractRecognitionClaim claim = buildStaticRecognitionClaim( true );

    testAbstractRecognitionSaveUpdateAndGetClaimById( promotion, claim );
  }

  /**
   * Tests saving and getting a claim by the ID.
   */
  private void testAbstractRecognitionSaveUpdateAndGetClaimById( AbstractRecognitionPromotion promotion, AbstractRecognitionClaim expectedClaim )
  {
    // Build a unique string to be used during testing.
    String uniqueString = String.valueOf( System.currentTimeMillis() % 5503032 );

    // Create a claim submitter.
    Participant submitter = ParticipantDAOImplTest.buildAndSaveParticipant( "submitter-" + uniqueString );

    // Create a node.
    Node node = NodeDAOImplTest.buildUniqueNode( uniqueString );
    nodeDao.saveNode( node );

    // Create the first claim recipient.
    Participant recipient1 = ParticipantDAOImplTest.buildAndSaveParticipant( "recipient1-" + uniqueString );

    Set userNodes1 = recipient1.getUserNodes();
    Node recipient1Node1 = null;
    Iterator iter1 = userNodes1.iterator();
    if ( iter1.hasNext() )
    {
      recipient1Node1 = ( (UserNode)iter1.next() ).getNode();
    }

    ClaimRecipient claimRecipient1 = new ClaimRecipient();
    claimRecipient1.setRecipient( recipient1 );
    claimRecipient1.setNode( recipient1Node1 );
    claimRecipient1.setAwardQuantity( new Long( 5 ) );

    // Create the second claim recip
    Participant recipient2 = ParticipantDAOImplTest.buildAndSaveParticipant( "recipient2-" + uniqueString );

    Set userNodes2 = recipient1.getUserNodes();
    Node recipient1Node2 = null;
    Iterator iter = userNodes2.iterator();
    if ( iter.hasNext() )
    {
      recipient1Node2 = ( (UserNode)iter.next() ).getNode();
    }

    ClaimRecipient claimRecipient2 = new ClaimRecipient();
    claimRecipient2.setRecipient( recipient2 );
    claimRecipient2.setNode( recipient1Node2 );
    claimRecipient2.setAwardQuantity( new Long( 5 ) );

    flushAndClearSession();

    // Create a claim.
    expectedClaim.setSubmitter( submitter );
    expectedClaim.setPromotion( promotion );
    expectedClaim.setNode( node );
    expectedClaim.setSubmissionDate( new Date() );
    expectedClaim.setClaimNumber( new Long( oracleSequenceDAO.getOracleSequenceNextValue( "claim_nbr_sq" ) ).toString() );

    expectedClaim.addClaimRecipient( claimRecipient1 );
    expectedClaim.addClaimRecipient( claimRecipient2 );
    Set expectedClaimRecipients = expectedClaim.getClaimRecipients();
    assertEquals( 2, expectedClaimRecipients.size() );

    // add elements
    ClaimFormStep claimFormStep = (ClaimFormStep)expectedClaim.getPromotion().getClaimForm().getClaimFormSteps().get( 0 );
    ClaimFormStepElement claimFormStepElement = (ClaimFormStepElement)claimFormStep.getClaimFormStepElements().get( 0 );

    ClaimElement element1 = new ClaimElement();
    element1.setClaimFormStepElement( claimFormStepElement );
    element1.setValue( "some value" );
    expectedClaim.addClaimElement( element1 );

    // Test saving a claim.
    claimDao.saveClaim( expectedClaim );

    flushAndClearSession();

    assertNotNull( "Id of expectedClaim is null.", expectedClaim.getId() );

    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
    associationRequestCollection.add( new ClaimAssociationRequest( ClaimAssociationRequest.CLAIM_RECIPIENTS ) );

    AbstractRecognitionClaim actualClaim = (AbstractRecognitionClaim)claimDao.getClaimByIdWithAssociations( expectedClaim.getId(), associationRequestCollection );

    assertEquals( "Actual Claim wasn't equal to what was expected.", expectedClaim, actualClaim );

    assertEquals( "Expected claim recipients to be equal.", actualClaim.getClaimRecipients(), expectedClaimRecipients );
    assertNotNull( "Expected node to be not null.", actualClaim.getNode() );
    assertEquals( "Expected nodes to be equal.", actualClaim.getNode(), expectedClaim.getNode() );

    assertNotNull( actualClaim.getClaimElements() );
    ClaimElement actualClaimElement = (ClaimElement)actualClaim.getClaimElements().iterator().next();
    assertEquals( "some value", actualClaimElement.getValue() );

    flushAndClearSession();

    // ------------------------
    // Test updating a claim.
    // ------------------------
    AbstractRecognitionClaim expectedUpdatedClaim = (AbstractRecognitionClaim)claimDao.getClaimByIdWithAssociations( expectedClaim.getId(), associationRequestCollection );

    ClaimRecipient removeClaimRecipient = new ClaimRecipient();
    removeClaimRecipient.setSerialId( claimRecipient1.getSerialId() );
    expectedUpdatedClaim.getClaimRecipients().remove( removeClaimRecipient );
    expectedUpdatedClaim.setCopySender( true );
    expectedUpdatedClaim.setSubmitterComments( "some comments" );

    if ( expectedUpdatedClaim instanceof RecognitionClaim )
    {
      RecognitionClaim tmpClaim = (RecognitionClaim)expectedUpdatedClaim;
      tmpClaim.setBehavior( PromoRecognitionBehaviorType.getDefaultItem() );
      tmpClaim.setCopyManager( true );
    }
    else if ( expectedUpdatedClaim instanceof NominationClaim )
    {
      NominationClaim tmpClaim = (NominationClaim)expectedUpdatedClaim;
      tmpClaim.setBehavior( PromoNominationBehaviorType.getDefaultItem() );
      tmpClaim.setTeamName( "my team" );
    }

    // Create an additional claim recipient
    Participant recipient3 = ParticipantDAOImplTest.buildUniqueParticipant( "recipient3-" + uniqueString );
    userDao.saveUser( recipient3 );

    ClaimRecipient claimRecipient3 = new ClaimRecipient();
    claimRecipient3.setRecipient( recipient3 );
    claimRecipient3.setAwardQuantity( new Long( 5 ) );
    expectedUpdatedClaim.addClaimRecipient( claimRecipient3 );
    Set expectedUpdatedClaimRecipients = expectedUpdatedClaim.getClaimRecipients();
    // Update the Claim
    expectedUpdatedClaim = (AbstractRecognitionClaim)claimDao.saveClaim( expectedUpdatedClaim );

    flushAndClearSession();

    AbstractRecognitionClaim actualUpdatedClaim = (AbstractRecognitionClaim)claimDao.getClaimByIdWithAssociations( expectedUpdatedClaim.getId(), associationRequestCollection );

    assertEquals( "Actual updated Claim wasn't equals to what was expected.", expectedUpdatedClaim, actualUpdatedClaim );
    assertEquals( "Expected claim recipients to be equal.", actualUpdatedClaim.getClaimRecipients(), expectedUpdatedClaimRecipients );
    assertNotNull( "Expected node to be not null.", actualUpdatedClaim.getNode() );
    assertEquals( "Expected nodes to be equal.", actualUpdatedClaim.getNode(), expectedUpdatedClaim.getNode() );
  }

  /**
   * Tests saving and getting a claim by the ID.
   */
  public void testQuizSaveUpdateAndGetClaimById()
  {

    // Build a unique string to be used during testing.
    String uniqueString = String.valueOf( System.currentTimeMillis() % 5503032 );

    // Create a quiz
    Quiz quiz = QuizDAOImplTest.buildCompletedQuiz( buildUniqueString() );
    getQuizDAO().saveQuiz( quiz );

    // Create a promotion.
    QuizPromotion promotion = PromotionDAOImplTest.buildQuizPromotion( uniqueString + "promo" );
    promotion.setQuiz( quiz );
    promotionDao.save( promotion );

    // Create a claim submitter.
    Participant submitter = ParticipantDAOImplTest.buildUniqueParticipant( "submitter-" + uniqueString );
    userDao.saveUser( submitter );

    // Create a node.
    Node node = NodeDAOImplTest.buildUniqueNode( uniqueString );
    nodeDao.saveNode( node );

    QuizResponse quizResponse1 = new QuizResponse();
    quizResponse1.setQuizQuestion( (QuizQuestion)quiz.getQuizQuestions().get( 0 ) );
    quizResponse1.setSequenceNumber( 0 );
    quizResponse1.setSelectedQuizQuestionAnswer( (QuizQuestionAnswer)quizResponse1.getQuizQuestion().getQuizQuestionAnswers().get( 0 ) );

    QuizResponse quizResponse2 = new QuizResponse();
    quizResponse2.setQuizQuestion( (QuizQuestion)quiz.getQuizQuestions().get( 1 ) );
    quizResponse2.setSequenceNumber( 1 );
    quizResponse2.setSelectedQuizQuestionAnswer( (QuizQuestionAnswer)quizResponse2.getQuizQuestion().getQuizQuestionAnswers().get( 0 ) );

    flushAndClearSession();

    // Create a claim.
    QuizClaim expectedClaim = buildStaticQuizClaim( true );

    expectedClaim.setSubmitter( submitter );
    expectedClaim.setPromotion( promotion );
    expectedClaim.setNode( node );
    expectedClaim.setSubmissionDate( new Date() );
    expectedClaim.setClaimNumber( new Long( oracleSequenceDAO.getOracleSequenceNextValue( "claim_nbr_sq" ) ).toString() );
    expectedClaim.setQuestionCount( 2 );
    expectedClaim.setPassingScore( 1 );

    expectedClaim.addQuizResponse( quizResponse1 );
    expectedClaim.addQuizResponse( quizResponse2 );
    Set expectedQuizResponses = expectedClaim.getQuizResponses();
    assertEquals( 2, expectedQuizResponses.size() );

    expectedClaim.setCurrentQuizQuestion( quizResponse1.getQuizQuestion() );

    // Test saving a claim.
    claimDao.saveClaim( expectedClaim );

    flushAndClearSession();

    assertNotNull( "Id of expectedClaim is null.", expectedClaim.getId() );

    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
    associationRequestCollection.add( new ClaimAssociationRequest( ClaimAssociationRequest.QUIZ_RESPONSES ) );

    QuizClaim actualClaim = (QuizClaim)claimDao.getClaimByIdWithAssociations( expectedClaim.getId(), associationRequestCollection );

    assertEquals( "Actual Claim wasn't equal to what was expected.", expectedClaim, actualClaim );

    assertEquals( "Expected quiz responses to be equal.", expectedQuizResponses, actualClaim.getQuizResponses() );
    assertNotNull( "Expected node to be not null.", actualClaim.getNode() );
    assertEquals( "Expected nodes to be equal.", actualClaim.getNode(), expectedClaim.getNode() );

    flushAndClearSession();

    // ------------------------
    // Test updating a claim.
    // ------------------------
    QuizClaim expectedUpdatedClaim = (QuizClaim)claimDao.getClaimByIdWithAssociations( expectedClaim.getId(), associationRequestCollection );

    expectedUpdatedClaim.setPass( Boolean.TRUE );

    Set expectedUpdatedQuizReponses = expectedUpdatedClaim.getQuizResponses();

    // Update a quiz reponse
    QuizResponse savedQuizResponse1 = (QuizResponse)expectedUpdatedQuizReponses.iterator().next();
    assertEquals( quizResponse1, savedQuizResponse1 );
    savedQuizResponse1.setSelectedQuizQuestionAnswer( (QuizQuestionAnswer)savedQuizResponse1.getQuizQuestion().getQuizQuestionAnswers().get( 0 ) );

    // Remove a response
    expectedUpdatedClaim.getQuizResponses().remove( quizResponse2 );
    //
    // Create an additional quiz repsone
    QuizResponse quizResponse3 = new QuizResponse();
    quizResponse3.setQuizQuestion( (QuizQuestion)quiz.getQuizQuestions().get( 2 ) );
    quizResponse3.setSequenceNumber( 2 );
    quizResponse3.setSelectedQuizQuestionAnswer( (QuizQuestionAnswer)quizResponse3.getQuizQuestion().getQuizQuestionAnswers().get( 0 ) );

    expectedUpdatedClaim.addQuizResponse( quizResponse3 );

    // Update the Claim
    expectedUpdatedClaim = (QuizClaim)claimDao.saveClaim( expectedUpdatedClaim );

    flushAndClearSession();

    QuizClaim actualUpdatedClaim = (QuizClaim)claimDao.getClaimByIdWithAssociations( expectedUpdatedClaim.getId(), associationRequestCollection );

    assertEquals( "Actual updated Claim wasn't equals to what was expected.", expectedUpdatedClaim, actualUpdatedClaim );
    assertEquals( "Expected claim recipients to be equal.", actualUpdatedClaim.getQuizResponses(), expectedUpdatedQuizReponses );
    QuizResponse actualQuizResponse1 = (QuizResponse)actualUpdatedClaim.getQuizResponses().iterator().next();
    assertEquals( actualQuizResponse1.getSelectedQuizQuestionAnswer(), quizResponse1.getSelectedQuizQuestionAnswer() );

    assertNotNull( "Expected node to be not null.", actualUpdatedClaim.getNode() );
    assertEquals( "Expected nodes to be equal.", actualUpdatedClaim.getNode(), expectedUpdatedClaim.getNode() );
  }

  /**
   * Tests saving and getting a recognition claim by criteria.
   */
  public void testGetRecognitionClaimByCriteria()
  {

    List initialAllClaimsList = claimDao.getClaimList( new ClaimQueryConstraint() );
    List intialRecognitionClaimsList = claimDao.getClaimList( new RecognitionClaimQueryConstraint() );
    List initialProductClaimsList = claimDao.getClaimList( new ProductClaimClaimQueryConstraint() );

    int initialAllClaimsListSize = initialAllClaimsList.size();
    int initialRecognitionClaimsListSize = intialRecognitionClaimsList.size();
    int initialProductClaimsListSize = initialProductClaimsList.size();

    // Build a unique string to be used during testing.
    String uniqueString = String.valueOf( System.currentTimeMillis() % 5503032 );

    // Create a promotion.
    Promotion promotion = PromotionDAOImplTest.buildRecognitionPromotion( uniqueString + "promo" );
    promotionDao.save( promotion );

    // Create a claim submitter.
    Participant submitter = ParticipantDAOImplTest.buildUniqueParticipant( "submitter-" + uniqueString );
    userDao.saveUser( submitter );

    // Create a node.
    Node node = NodeDAOImplTest.buildUniqueNode( uniqueString );
    nodeDao.saveNode( node );

    // Create the first claim recipient.
    Participant recipient1 = ParticipantDAOImplTest.buildUniqueParticipant( "recipient1-" + uniqueString );
    userDao.saveUser( recipient1 );

    ClaimRecipient claimRecipient1 = new ClaimRecipient();
    claimRecipient1.setRecipient( recipient1 );
    claimRecipient1.setAwardQuantity( new Long( 5 ) );

    // Create the second claim recip
    Participant recipient2 = ParticipantDAOImplTest.buildUniqueParticipant( "recipient2-" + uniqueString );
    userDao.saveUser( recipient2 );

    ClaimRecipient claimRecipient2 = new ClaimRecipient();
    claimRecipient2.setRecipient( recipient2 );
    claimRecipient2.setAwardQuantity( new Long( 5 ) );

    flushAndClearSession();

    // Create a claim.
    RecognitionClaim expectedClaim = buildStaticRecognitionClaim( true );

    expectedClaim.setSubmitter( submitter );
    expectedClaim.setPromotion( promotion );
    expectedClaim.setNode( node );
    expectedClaim.setSource( RecognitionClaimSource.WEB );
    expectedClaim.setSubmissionDate( new Date() );
    expectedClaim.setClaimNumber( new Long( oracleSequenceDAO.getOracleSequenceNextValue( "claim_nbr_sq" ) ).toString() );

    expectedClaim.addClaimRecipient( claimRecipient1 );
    expectedClaim.addClaimRecipient( claimRecipient2 );
    assertEquals( 2, expectedClaim.getClaimRecipients().size() );

    // add elements
    ClaimFormStep claimFormStep = (ClaimFormStep)expectedClaim.getPromotion().getClaimForm().getClaimFormSteps().get( 0 );
    ClaimFormStepElement claimFormStepElement = (ClaimFormStepElement)claimFormStep.getClaimFormStepElements().get( 0 );

    ClaimElement element1 = new ClaimElement();
    element1.setClaimFormStepElement( claimFormStepElement );
    element1.setValue( "some value" );
    expectedClaim.addClaimElement( element1 );

    // Test saving a claim.
    claimDao.saveClaim( expectedClaim );

    flushAndClearSession();

    assertNotNull( "Id of expectedClaim is null.", expectedClaim.getId() );
    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
    associationRequestCollection.add( new ClaimAssociationRequest( ClaimAssociationRequest.CLAIM_RECIPIENTS ) );

    // refetch inserted claim
    expectedClaim = (RecognitionClaim)claimDao.getClaimByIdWithAssociations( expectedClaim.getId(), associationRequestCollection );

    RecognitionClaim actualClaim = (RecognitionClaim)claimDao.getClaimByIdWithAssociations( expectedClaim.getId(), associationRequestCollection );

    assertEquals( "Actual Claim wasn't equal to what was expected.", expectedClaim, actualClaim );

    assertEquals( "Expected claim recipients to be equal.", actualClaim.getClaimRecipients(), expectedClaim.getClaimRecipients() );
    assertNotNull( "Expected node to be not null.", actualClaim.getNode() );
    assertEquals( "Expected nodes to be equal.", actualClaim.getNode(), expectedClaim.getNode() );

    assertNotNull( actualClaim.getClaimElements() );
    ClaimElement actualClaimElement = (ClaimElement)actualClaim.getClaimElements().iterator().next();
    assertEquals( "some value", actualClaimElement.getValue() );

    flushAndClearSession();

    // ------------------------
    // Test updating a claim.
    // ------------------------
    RecognitionClaim expectedUpdatedClaim = (RecognitionClaim)claimDao.getClaimByIdWithAssociations( expectedClaim.getId(), associationRequestCollection );

    expectedUpdatedClaim.setClaimNumber( new Long( oracleSequenceDAO.getOracleSequenceNextValue( "claim_nbr_sq" ) ).toString() );
    // expectedUpdatedClaim.setClaimNumber( "Tes-sste2-2232" );

    ClaimRecipient removeClaimRecipient = new ClaimRecipient();
    removeClaimRecipient.setSerialId( claimRecipient1.getSerialId() );
    expectedUpdatedClaim.getClaimRecipients().remove( removeClaimRecipient );

    // Create an additional claim recipient
    Participant recipient3 = ParticipantDAOImplTest.buildUniqueParticipant( "recipient3-" + uniqueString );
    userDao.saveUser( recipient3 );

    ClaimRecipient claimRecipient3 = new ClaimRecipient();
    claimRecipient3.setRecipient( recipient3 );
    claimRecipient3.setAwardQuantity( new Long( 5 ) );
    expectedUpdatedClaim.addClaimRecipient( claimRecipient3 );
    // Update the Claim
    expectedUpdatedClaim = (RecognitionClaim)claimDao.saveClaim( expectedUpdatedClaim );

    flushAndClearSession();

    List allClaimsList = claimDao.getClaimList( new ClaimQueryConstraint() );
    List recognitionClaimsList = claimDao.getClaimList( new RecognitionClaimQueryConstraint() );
    List productClaimsList = claimDao.getClaimList( new ProductClaimClaimQueryConstraint() );

    assertEquals( "Actual List wasn't equals to what was expected.", initialAllClaimsListSize + 1, allClaimsList.size() );

    assertEquals( "Actual List wasn't equals to what was expected.", initialRecognitionClaimsListSize + 1, recognitionClaimsList.size() );

    assertEquals( "Actual List wasn't equals to what was expected.", initialProductClaimsListSize, productClaimsList.size() );
  }

  // nomination claim
  /**
   * Tests saving and getting a recognition claim by criteria.
   */
  public void testGetNominationClaimByCriteria()
  {

    List initialAllClaimsList = claimDao.getClaimList( new ClaimQueryConstraint() );
    List intialNominationClaimsList = claimDao.getClaimList( new NominationClaimQueryConstraint() );
    List initialProductClaimsList = claimDao.getClaimList( new ProductClaimClaimQueryConstraint() );

    int initialAllClaimsListSize = initialAllClaimsList.size();
    int initialNominationClaimsListSize = intialNominationClaimsList.size();
    int initialProductClaimsListSize = initialProductClaimsList.size();

    // Build a unique string to be used during testing.
    String uniqueString = String.valueOf( System.currentTimeMillis() % 5503032 );

    // Create a promotion.
    Promotion promotion = PromotionDAOImplTest.buildRecognitionPromotion( uniqueString + "promo" );
    promotionDao.save( promotion );

    // Create a claim submitter.
    Participant submitter = ParticipantDAOImplTest.buildUniqueParticipant( "submitter-" + uniqueString );
    userDao.saveUser( submitter );

    // Create a node.
    Node node = NodeDAOImplTest.buildUniqueNode( uniqueString );
    nodeDao.saveNode( node );

    // Create the first claim recipient.
    Participant recipient1 = ParticipantDAOImplTest.buildUniqueParticipant( "recipient1-" + uniqueString );
    userDao.saveUser( recipient1 );

    ClaimRecipient claimRecipient1 = new ClaimRecipient();
    claimRecipient1.setRecipient( recipient1 );
    claimRecipient1.setAwardQuantity( new Long( 5 ) );

    // Create the second claim recip
    Participant recipient2 = ParticipantDAOImplTest.buildUniqueParticipant( "recipient2-" + uniqueString );
    userDao.saveUser( recipient2 );

    ClaimRecipient claimRecipient2 = new ClaimRecipient();
    claimRecipient2.setRecipient( recipient2 );
    claimRecipient2.setAwardQuantity( new Long( 5 ) );

    flushAndClearSession();

    // Create a claim.
    NominationClaim expectedClaim = buildStaticNominationClaim( true );

    expectedClaim.setSubmitter( submitter );
    expectedClaim.setPromotion( promotion );
    expectedClaim.setNode( node );
    expectedClaim.setSubmissionDate( new Date() );
    expectedClaim.setClaimNumber( new Long( oracleSequenceDAO.getOracleSequenceNextValue( "claim_nbr_sq" ) ).toString() );

    expectedClaim.addClaimRecipient( claimRecipient1 );
    expectedClaim.addClaimRecipient( claimRecipient2 );
    assertEquals( 2, expectedClaim.getClaimRecipients().size() );

    // add elements
    ClaimFormStep claimFormStep = (ClaimFormStep)expectedClaim.getPromotion().getClaimForm().getClaimFormSteps().get( 0 );
    ClaimFormStepElement claimFormStepElement = (ClaimFormStepElement)claimFormStep.getClaimFormStepElements().get( 0 );

    ClaimElement element1 = new ClaimElement();
    element1.setClaimFormStepElement( claimFormStepElement );
    element1.setValue( "some value" );
    expectedClaim.addClaimElement( element1 );

    NominationClaimStatusType nomStatus = NominationClaimStatusType.lookup( NominationClaimStatusType.COMPLETE );
    expectedClaim.setNominationStatusType( nomStatus );

    // Test saving a claim.
    claimDao.saveClaim( expectedClaim );

    flushAndClearSession();

    NominationClaimQueryConstraint nomQryCon = new NominationClaimQueryConstraint();
    nomQryCon.setSubmitterId( expectedClaim.getSubmitter().getId() );
    nomQryCon.setNominationStatusType( nomStatus );

    List claimList = claimDao.getClaimList( nomQryCon );
    assertNotNull( claimList );
    NominationClaim actualNom = (NominationClaim)claimList.get( 0 );
    assertTrue( actualNom.getId().equals( expectedClaim.getId() ) );

    assertNotNull( "Id of expectedClaim is null.", expectedClaim.getId() );
    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
    associationRequestCollection.add( new ClaimAssociationRequest( ClaimAssociationRequest.CLAIM_RECIPIENTS ) );

    // refetch inserted claim
    expectedClaim = (NominationClaim)claimDao.getClaimByIdWithAssociations( expectedClaim.getId(), associationRequestCollection );

    NominationClaim actualClaim = (NominationClaim)claimDao.getClaimByIdWithAssociations( expectedClaim.getId(), associationRequestCollection );

    assertEquals( "Actual Claim wasn't equal to what was expected.", expectedClaim, actualClaim );

    assertEquals( "Expected claim recipients to be equal.", actualClaim.getClaimRecipients(), expectedClaim.getClaimRecipients() );
    assertNotNull( "Expected node to be not null.", actualClaim.getNode() );
    assertEquals( "Expected nodes to be equal.", actualClaim.getNode(), expectedClaim.getNode() );

    assertNotNull( actualClaim.getClaimElements() );
    ClaimElement actualClaimElement = (ClaimElement)actualClaim.getClaimElements().iterator().next();
    assertEquals( "some value", actualClaimElement.getValue() );

    flushAndClearSession();

    // ------------------------
    // Test updating a claim.
    // ------------------------
    NominationClaim expectedUpdatedClaim = (NominationClaim)claimDao.getClaimByIdWithAssociations( expectedClaim.getId(), associationRequestCollection );

    expectedUpdatedClaim.setClaimNumber( new Long( oracleSequenceDAO.getOracleSequenceNextValue( "claim_nbr_sq" ) ).toString() );
    // expectedUpdatedClaim.setClaimNumber( "Tes-sste2-2232" );

    ClaimRecipient removeClaimRecipient = new ClaimRecipient();
    removeClaimRecipient.setSerialId( claimRecipient1.getSerialId() );
    expectedUpdatedClaim.getClaimRecipients().remove( removeClaimRecipient );

    // Create an additional claim recipient
    Participant recipient3 = ParticipantDAOImplTest.buildUniqueParticipant( "recipient3-" + uniqueString );
    userDao.saveUser( recipient3 );

    ClaimRecipient claimRecipient3 = new ClaimRecipient();
    claimRecipient3.setRecipient( recipient3 );
    claimRecipient3.setAwardQuantity( new Long( 5 ) );
    expectedUpdatedClaim.addClaimRecipient( claimRecipient3 );
    // Update the Claim
    expectedUpdatedClaim = (NominationClaim)claimDao.saveClaim( expectedUpdatedClaim );

    flushAndClearSession();

    List allClaimsList = claimDao.getClaimList( new ClaimQueryConstraint() );
    List nominationClaimsList = claimDao.getClaimList( new NominationClaimQueryConstraint() );
    List productClaimsList = claimDao.getClaimList( new ProductClaimClaimQueryConstraint() );

    assertEquals( "Actual List wasn't equals to what was expected.", initialAllClaimsListSize + 1, allClaimsList.size() );

    assertEquals( "Actual List wasn't equals to what was expected.", initialNominationClaimsListSize + 1, nominationClaimsList.size() );

    assertEquals( "Actual List wasn't equals to what was expected.", initialProductClaimsListSize, productClaimsList.size() );
  }
  // nomination claim

  /**
   * Test getting a list of all claims by various constraints.
   */
  public void testGetClaims()
  {

    // Build unique Strings for saving the claim
    String uniqueString = String.valueOf( System.currentTimeMillis() % 5503032 );
    String uniqueString2 = String.valueOf( System.currentTimeMillis() % 212314551 );
    String uniqueString3 = String.valueOf( System.currentTimeMillis() % 412314551 );

    Promotion promotion1 = promotionDao.save( PromotionDAOImplTest.buildProductClaimPromotion( uniqueString + "promo1" ) );
    Promotion promotion2 = promotionDao.save( PromotionDAOImplTest.buildProductClaimPromotion( uniqueString + "promo2" ) );

    // Prepare the list which is expected to be returned from the database
    List expectedClaimList = new ArrayList();
    List expectedOpenList = new ArrayList();
    List expectedClosedList = new ArrayList();
    List expectedPromotion1List = new ArrayList();
    List expectedOldList = new ArrayList();
    List expectedDateRangePromotion1List = new ArrayList();
    List expectedProxyClaimList = new ArrayList();

    // Build unique participants.
    Participant submitter1 = ParticipantDAOImplTest.buildUniqueParticipant( uniqueString );
    Participant submitter2 = ParticipantDAOImplTest.buildUniqueParticipant( uniqueString2 );
    Participant proxy1 = ParticipantDAOImplTest.buildUniqueParticipant( uniqueString3 );

    userDao.saveUser( submitter2 );
    userDao.saveUser( submitter1 );
    userDao.saveUser( proxy1 );

    // Build a unique node.
    Node node = NodeDAOImplTest.buildUniqueNode( uniqueString );
    nodeDao.saveNode( node );

    Date yesterday = new Date( new Date().getTime() - DateUtils.MILLIS_PER_DAY );
    Date twoDaysAgo = new Date( new Date().getTime() - ( 2 * DateUtils.MILLIS_PER_DAY ) );

    // Build a claim, assign a submitter and save.
    Claim claim1 = ClaimDAOImplTest.buildStaticProductClaim( true );
    claim1.setSubmitter( submitter1 );
    claim1.setProxyUser( proxy1 );
    claim1.setPromotion( promotion1 );
    claim1.setNode( node );
    // Update create time to yesterday
    claim1.setSubmissionDate( yesterday );
    claim1.setClaimNumber( new Long( oracleSequenceDAO.getOracleSequenceNextValue( "claim_nbr_sq" ) ).toString() );
    claimDao.saveClaim( claim1 );
    expectedClaimList.add( claim1 );
    expectedOpenList.add( claim1 );
    expectedPromotion1List.add( claim1 );
    expectedOldList.add( claim1 );
    expectedDateRangePromotion1List.add( claim1 );
    expectedProxyClaimList.add( claim1 );

    // Build a claim, assign a submitter and save.
    Claim claim2 = ClaimDAOImplTest.buildStaticProductClaim( false );
    claim2.setSubmitter( submitter1 );
    claim2.setProxyUser( proxy1 );
    claim2.setPromotion( promotion1 );
    claim2.setNode( node );
    claim2.setOpen( false );
    // Update create time to yesterday
    claim2.setSubmissionDate( yesterday );
    claim2.setClaimNumber( new Long( oracleSequenceDAO.getOracleSequenceNextValue( "claim_nbr_sq" ) ).toString() );
    claimDao.saveClaim( claim2 );
    expectedClaimList.add( claim2 );
    expectedClosedList.add( claim2 );
    expectedPromotion1List.add( claim2 );
    expectedOldList.add( claim2 );
    expectedDateRangePromotion1List.add( claim2 );
    expectedProxyClaimList.add( claim2 );

    // Build a claim, assign a submitter and save.
    Claim claim3 = ClaimDAOImplTest.buildStaticProductClaim( true );
    claim3.setSubmitter( submitter2 );
    claim3.setPromotion( promotion1 );
    claim3.setNode( node );
    claim3.setOpen( true );
    claim3.setSubmissionDate( new Date() );
    claim3.setClaimNumber( new Long( oracleSequenceDAO.getOracleSequenceNextValue( "claim_nbr_sq" ) ).toString() );
    claimDao.saveClaim( claim3 );
    expectedPromotion1List.add( claim3 );
    expectedDateRangePromotion1List.add( claim3 );

    // Build a claim, assign a submitter and save.
    Claim claim4 = ClaimDAOImplTest.buildStaticProductClaim( true );
    claim4.setSubmitter( submitter1 );
    claim4.setPromotion( promotion1 );
    claim4.setNode( node );
    claim4.setOpen( true );
    claim4.setSubmissionDate( new Date() );
    claim4.setClaimNumber( new Long( oracleSequenceDAO.getOracleSequenceNextValue( "claim_nbr_sq" ) ).toString() );
    claimDao.saveClaim( claim4 );
    expectedClaimList.add( claim4 );
    expectedOpenList.add( claim4 );
    expectedPromotion1List.add( claim4 );
    expectedDateRangePromotion1List.add( claim4 );

    // Build a claim, assign a submitter and save.
    Claim claim5 = ClaimDAOImplTest.buildStaticProductClaim( true );
    claim5.setSubmitter( submitter1 );
    claim5.setPromotion( promotion2 );
    claim5.setNode( node );
    claim5.setSubmissionDate( new Date() );
    claim5.setClaimNumber( new Long( oracleSequenceDAO.getOracleSequenceNextValue( "claim_nbr_sq" ) ).toString() );
    claimDao.saveClaim( claim5 );
    expectedClaimList.add( claim5 );
    expectedOpenList.add( claim5 );

    // Build a claim, assign a submitter and save.
    Claim claim6 = ClaimDAOImplTest.buildStaticProductClaim( false );
    claim6.setSubmitter( submitter1 );
    claim6.setPromotion( promotion2 );
    claim6.setNode( node );
    claim6.setSubmissionDate( new Date() );
    claim6.setClaimNumber( new Long( oracleSequenceDAO.getOracleSequenceNextValue( "claim_nbr_sq" ) ).toString() );
    claimDao.saveClaim( claim6 );
    expectedClaimList.add( claim6 );
    expectedClosedList.add( claim6 );

    flushAndClearSession();

    ClaimQueryConstraint allByPaxClaimQueryConstraint = new ClaimQueryConstraint();
    allByPaxClaimQueryConstraint.setSubmitterId( submitter1.getId() );
    List actualClaimList = claimDao.getClaimList( allByPaxClaimQueryConstraint );
    PropertyComparator.sort( actualClaimList, new MutableSortDefinition( "id", true, true ) );

    assertTrue( "Actual List wasn't the same size we expected: " + actualClaimList.size(), actualClaimList.size() >= 5 );
    assertEquals( "Actual List wasn't equals to what was expected.", expectedClaimList, actualClaimList );

    ClaimQueryConstraint openClaimQueryConstraint = new ClaimQueryConstraint();
    openClaimQueryConstraint.setSubmitterId( submitter1.getId() );
    openClaimQueryConstraint.setOpen( Boolean.TRUE );
    List actualOpenList = claimDao.getClaimList( openClaimQueryConstraint );
    PropertyComparator.sort( actualOpenList, new MutableSortDefinition( "id", true, true ) );
    assertTrue( "Actual Open List wasn't the same size we expected:" + actualOpenList.size(), actualOpenList.size() >= 3 );
    assertEquals( "Actual Open List wasn't equals to what was expected.", actualOpenList, expectedOpenList );

    ClaimQueryConstraint closedClaimQueryConstraint = new ClaimQueryConstraint();
    closedClaimQueryConstraint.setSubmitterId( submitter1.getId() );
    closedClaimQueryConstraint.setOpen( Boolean.FALSE );
    List actualClosedList = claimDao.getClaimList( closedClaimQueryConstraint );
    PropertyComparator.sort( actualClosedList, new MutableSortDefinition( "id", true, true ) );
    assertTrue( "Actual Closed List wasn't the same size we expected:" + actualClosedList.size(), actualClosedList.size() >= 2 );
    assertEquals( "Actual Closed List wasn't equals to what was expected.", expectedClosedList, actualClosedList );

    ClaimQueryConstraint promotionClaimQueryConstraint = new ClaimQueryConstraint();
    promotionClaimQueryConstraint.setIncludedPromotionIds( new Long[] { promotion1.getId() } );
    List actualPromotion1List = claimDao.getClaimList( promotionClaimQueryConstraint );
    PropertyComparator.sort( actualPromotion1List, new MutableSortDefinition( "id", true, true ) );
    assertTrue( "Actual List wasn't the same size we expected:" + actualPromotion1List.size(), actualPromotion1List.size() >= 4 );
    assertEquals( "ActualList wasn't equals to what was expected.", expectedPromotion1List, actualPromotion1List );

    ClaimQueryConstraint oldClaimQueryConstraint = new ClaimQueryConstraint();
    oldClaimQueryConstraint.setIncludedPromotionIds( new Long[] { promotion1.getId() } );
    oldClaimQueryConstraint.setEndDate( yesterday );
    List actualOldList = claimDao.getClaimList( oldClaimQueryConstraint );
    PropertyComparator.sort( actualOldList, new MutableSortDefinition( "id", true, true ) );
    assertTrue( "Actual List wasn't the same size we expected:" + actualOldList.size(), actualOldList.size() >= 2 );
    assertEquals( "Actual List wasn't equals to what was expected.", expectedOldList, actualOldList );

    ClaimQueryConstraint tooOldClaimQueryConstraint = new ClaimQueryConstraint();
    tooOldClaimQueryConstraint.setIncludedPromotionIds( new Long[] { promotion1.getId() } );
    tooOldClaimQueryConstraint.setEndDate( twoDaysAgo );
    assertTrue( "count should be 0 for claims on promo 1 created on or before two days ago", claimDao.getClaimList( tooOldClaimQueryConstraint ).isEmpty() );

    ClaimQueryConstraint dateRangeClaimQueryConstraint = new ClaimQueryConstraint();
    dateRangeClaimQueryConstraint.setIncludedPromotionIds( new Long[] { promotion1.getId() } );
    dateRangeClaimQueryConstraint.setStartDate( yesterday );
    dateRangeClaimQueryConstraint.setEndDate( new Date() );
    List actualDateRangePromotion1List = claimDao.getClaimList( dateRangeClaimQueryConstraint );
    PropertyComparator.sort( actualDateRangePromotion1List, new MutableSortDefinition( "id", true, true ) );
    assertTrue( "Actual List wasn't the same size we expected:" + actualDateRangePromotion1List.size(), actualDateRangePromotion1List.size() >= 4 );
    assertEquals( "Actual List wasn't equals to what was expected.", expectedDateRangePromotion1List, actualDateRangePromotion1List );

    ClaimQueryConstraint tooOldDateRangeClaimQueryConstraint = new ClaimQueryConstraint();
    tooOldDateRangeClaimQueryConstraint.setIncludedPromotionIds( new Long[] { promotion1.getId() } );
    tooOldDateRangeClaimQueryConstraint.setStartDate( twoDaysAgo );
    tooOldDateRangeClaimQueryConstraint.setEndDate( twoDaysAgo );
    assertTrue( "count should be 0 for claims on promo 1 created on or before two days ago using date range", claimDao.getClaimList( tooOldDateRangeClaimQueryConstraint ).isEmpty() );

    ClaimQueryConstraint proxyClaimQueryConstraint = new ClaimQueryConstraint();
    proxyClaimQueryConstraint.setSubmitterId( submitter1.getId() );
    proxyClaimQueryConstraint.setProxyUserId( proxy1.getId() );
    assertEquals( "Actual Proxy Claim List wasn't equals to what was expected.", expectedProxyClaimList, claimDao.getClaimList( proxyClaimQueryConstraint ) );
  }

  /**
   * Test getting a list of product claims by various constraints.
   */
  public void testGetClaimListProductClaims()
  {
    // Build unique Strings for saving the claim
    String uniqueString = String.valueOf( System.currentTimeMillis() % 5503032 );

    Promotion promotion1 = promotionDao.save( PromotionDAOImplTest.buildProductClaimPromotion( uniqueString + "promo1" ) );

    // Prepare the list which is expected to be returned from the database
    List expectedNoParticipantList = new ArrayList();
    List expectedParticipant1AsSubmitterList = new ArrayList();
    List expectedParticipant1AsClaimParticipantList = new ArrayList();
    List expectedParticipant1AsEitherList = new ArrayList();

    // Get a claim participant team position.
    PromotionTeamPosition expectedPromotionTeamPosition = PromotionParticipantDAOImplTest.buildPromotionTeamPosition( promotion1 );
    getPromotionParticipantDAO().savePromotionTeamPosition( expectedPromotionTeamPosition );

    // Create the first claim participant.
    Participant participant1 = ParticipantDAOImplTest.buildUniqueParticipant( uniqueString + "-participant-1" );
    participantDao.saveParticipant( participant1 );
    ProductClaimParticipant productClaimParticipant1 = new ProductClaimParticipant( participant1, expectedPromotionTeamPosition );

    // Create the second claim participant.
    Participant participant2 = ParticipantDAOImplTest.buildUniqueParticipant( uniqueString + "-participant-2" );
    participantDao.saveParticipant( participant2 );
    ProductClaimParticipant productClaimParticipant2 = new ProductClaimParticipant( participant2, expectedPromotionTeamPosition );

    // Create the third claim participant.
    Participant participant3 = ParticipantDAOImplTest.buildUniqueParticipant( uniqueString + "-participant-3" );
    participantDao.saveParticipant( participant3 );

    ProductClaimParticipant productClaimParticipant3 = new ProductClaimParticipant( participant3, expectedPromotionTeamPosition );

    flushAndClearSession();

    // Build a unique node.
    Node node = NodeDAOImplTest.buildUniqueNode( uniqueString );
    nodeDao.saveNode( node );

    Date yesterday = new Date( new Date().getTime() - DateUtils.MILLIS_PER_DAY );

    // Build a claim, assign a submitter and save.
    ProductClaim claim1 = ClaimDAOImplTest.buildStaticProductClaim( true );
    claim1.setSubmitter( participant2 );
    claim1.addClaimParticipant( productClaimParticipant3 );
    claim1.setPromotion( promotion1 );
    claim1.setNode( node );
    // Update create time to yesterday
    claim1.setSubmissionDate( yesterday );
    claim1.setClaimNumber( new Long( oracleSequenceDAO.getOracleSequenceNextValue( "claim_nbr_sq" ) ).toString() );
    claimDao.saveClaim( claim1 );
    expectedNoParticipantList.add( claim1 );

    // Build a claim, assign a submitter and save.
    ProductClaim claim2 = ClaimDAOImplTest.buildStaticProductClaim( false );
    claim2.setSubmitter( participant1 );
    claim2.addClaimParticipant( productClaimParticipant2 );
    claim2.setPromotion( promotion1 );
    claim2.setNode( node );
    claim2.setOpen( false );
    // Update create time to yesterday
    claim2.setSubmissionDate( yesterday );
    claim2.setClaimNumber( new Long( oracleSequenceDAO.getOracleSequenceNextValue( "claim_nbr_sq" ) ).toString() );
    claimDao.saveClaim( claim2 );
    expectedParticipant1AsSubmitterList.add( claim2 );
    expectedParticipant1AsEitherList.add( claim2 );

    // Build a claim, assign a submitter and save.
    ProductClaim claim3 = ClaimDAOImplTest.buildStaticProductClaim( true );
    claim3.setSubmitter( participant2 );
    claim3.addClaimParticipant( productClaimParticipant1 );
    claim3.setPromotion( promotion1 );
    claim3.setNode( node );
    claim3.setOpen( true );
    claim3.setSubmissionDate( new Date() );
    claim3.setClaimNumber( new Long( oracleSequenceDAO.getOracleSequenceNextValue( "claim_nbr_sq" ) ).toString() );
    claimDao.saveClaim( claim3 );
    expectedParticipant1AsClaimParticipantList.add( claim3 );
    expectedParticipant1AsEitherList.add( claim3 );

    flushAndClearSession();

    ProductClaimClaimQueryConstraint submitterClaimQueryConstraint = new ProductClaimClaimQueryConstraint();
    submitterClaimQueryConstraint.setSubmitter( true );
    submitterClaimQueryConstraint.setSubmitterOrTeamMemberParticipantId( participant1.getId() );
    List actualParticipant1AsSubmitterList = claimDao.getClaimList( submitterClaimQueryConstraint );
    // assertTrue( "Actual Open List wasn't the same size we expected:" + actualOpenList.size(),
    // actualOpenList.size() >= 3 );
    assertEquals( "actualParticipant1AsSubmitterList wasn't equals to what was expected.", actualParticipant1AsSubmitterList, expectedParticipant1AsSubmitterList );

    ProductClaimClaimQueryConstraint teamMemberClaimQueryConstraint = new ProductClaimClaimQueryConstraint();
    teamMemberClaimQueryConstraint.setTeamMember( true );
    teamMemberClaimQueryConstraint.setSubmitterOrTeamMemberParticipantId( participant1.getId() );
    List actualParticipant1AsClaimParticipantList = claimDao.getClaimList( teamMemberClaimQueryConstraint );
    // assertTrue( "Actual Closed List wasn't the same size we expected:" + actualClosedList.size(),
    // actualClosedList.size() >= 2 );
    assertEquals( "actualParticipant1AsClaimParticipantList wasn't equals to what was expected.", expectedParticipant1AsClaimParticipantList, actualParticipant1AsClaimParticipantList );

    ProductClaimClaimQueryConstraint eitherClaimQueryConstraint = new ProductClaimClaimQueryConstraint();
    eitherClaimQueryConstraint.setSubmitter( true );
    eitherClaimQueryConstraint.setTeamMember( true );
    eitherClaimQueryConstraint.setSubmitterOrTeamMemberParticipantId( participant1.getId() );
    List actualParticipant1AsEitherList = claimDao.getClaimList( eitherClaimQueryConstraint );
    // assertTrue( "Actual Closed List wasn't the same size we expected:" + actualClosedList.size(),
    // actualClosedList.size() >= 2 );
    assertEquals( "actualParticipant1AsEitherList wasn't equals to what was expected.", expectedParticipant1AsEitherList, actualParticipant1AsEitherList );
  }

  /**
   * Test getting a list of all claims by various constraints. two test 1. all closed 2. all closed
   * atleast one approved product item
   */
  public void testGetClaimsWithEligibilityProductClaimClaimQueryConstraint()
  {
    // Build unique Strings for saving the claim
    String testString = String.valueOf( System.currentTimeMillis() % 5503032 );
    ProductCategoryDAO productCategoryDAO = getProductCategoryDAO();
    ProductDAO productDAO = getProductDAO();
    PromotionDAO promotionDAO = getPromotionDao();

    // /product start
    // Build a productCategory
    ProductCategory productCategory = ProductCategoryDAOImplTest.buildProductCategory( testString );

    // Save the productCategory
    productCategoryDAO.saveProductCategory( productCategory );

    // Build a list of products
    Product product1 = ProductDAOImplTest.buildStaticProductDomainObject( "1" + testString, productCategory );
    Product product2 = ProductDAOImplTest.buildStaticProductDomainObject( "2" + testString, productCategory );
    Product product3 = ProductDAOImplTest.buildStaticProductDomainObject( "3" + testString, productCategory );

    // Save the list of products
    productDAO.save( product3 );
    productDAO.save( product2 );
    productDAO.save( product1 );

    // Build the expected List of products
    List expectedProductList = new ArrayList();
    expectedProductList.add( product1 );
    expectedProductList.add( product2 );
    expectedProductList.add( product3 );

    // Build a promotion
    ProductClaimPromotion promotion = PromotionDAOImplTest.buildProductClaimPromotion( testString );

    // Build a promotionPayoutGroup
    PromotionPayoutGroup promotionPayoutGroup = PromotionPayoutDAOImplTest.buildPromotionPayoutGroup();

    // Assign the promotion to the promotionPayoutGroup
    promotionPayoutGroup.setPromotion( promotion );

    // Build a list of promotionPayouts with the products
    PromotionPayout promotionPayout1 = PromotionPayoutDAOImplTest.buildPromotionPayout( product1 );
    PromotionPayout promotionPayout2 = PromotionPayoutDAOImplTest.buildPromotionPayout( product2 );
    PromotionPayout promotionPayout3 = PromotionPayoutDAOImplTest.buildPromotionPayout( product3 );

    // Assign the promotionPayouts to the promotionPayoutGroup
    promotionPayoutGroup.addPromotionPayout( promotionPayout1 );
    promotionPayoutGroup.addPromotionPayout( promotionPayout2 );
    promotionPayoutGroup.addPromotionPayout( promotionPayout3 );

    promotion.addPromotionPayoutGroup( promotionPayoutGroup );

    // Save the promotion
    promotionDAO.save( promotion );

    // /product end

    // Create the first claim participant.
    Participant participant1 = ParticipantDAOImplTest.buildUniqueParticipant( testString + "-participant-1" );
    participantDao.saveParticipant( participant1 );

    // Create the second claim participant.
    Participant participant2 = ParticipantDAOImplTest.buildUniqueParticipant( testString + "-participant-2" );
    participantDao.saveParticipant( participant2 );

    // Create the third claim participant.
    Participant participant3 = ParticipantDAOImplTest.buildUniqueParticipant( testString + "-participant-3" );
    participantDao.saveParticipant( participant3 );

    flushAndClearSession();

    // Build a unique node.
    Node node = NodeDAOImplTest.buildUniqueNode( testString );
    nodeDao.saveNode( node );

    Date yesterday = new Date( new Date().getTime() - DateUtils.MILLIS_PER_DAY );

    List expectedAllApprovedItemClaimsList = new ArrayList();
    List expectedClaimsAsSubmitterList = new ArrayList();

    // Build a claim, assign a submitter and save.
    ProductClaim claim1 = ClaimDAOImplTest.buildStaticProductClaim( true );
    claim1.setSubmitter( participant2 );
    claim1.setPromotion( promotion );
    claim1.setNode( node );
    // Update create time to yesterday
    claim1.setSubmissionDate( yesterday );
    claim1.setClaimNumber( new Long( oracleSequenceDAO.getOracleSequenceNextValue( "claim_nbr_sq" ) ).toString() );
    claimDao.saveClaim( claim1 );

    // Build a claim, assign a submitter and save.
    ProductClaim claim2 = ClaimDAOImplTest.buildStaticProductClaim( false );
    claim2.setSubmitter( participant1 );
    claim2.setPromotion( promotion );
    claim2.setNode( node );
    // Update create time to yesterday
    claim2.setSubmissionDate( yesterday );
    claim2.setClaimNumber( new Long( oracleSequenceDAO.getOracleSequenceNextValue( "claim_nbr_sq" ) ).toString() );
    claimDao.saveClaim( claim2 );
    expectedClaimsAsSubmitterList.add( claim2 );
    expectedAllApprovedItemClaimsList.add( claim2 );

    // Build a claim, assign a submitter and save.
    ProductClaim claim3 = ClaimDAOImplTest.buildStaticProductClaim( false );
    claim3.setSubmitter( participant2 );
    claim3.setPromotion( promotion );
    claim3.setNode( node );
    claim3.setSubmissionDate( new Date() );
    claim3.setClaimNumber( new Long( oracleSequenceDAO.getOracleSequenceNextValue( "claim_nbr_sq" ) ).toString() );
    claimDao.saveClaim( claim3 );
    expectedClaimsAsSubmitterList.add( claim3 );
    expectedAllApprovedItemClaimsList.add( claim3 );
    flushAndClearSession();

    ProductClaimClaimQueryConstraint submitterClaimQueryConstraint = new ProductClaimClaimQueryConstraint();
    submitterClaimQueryConstraint.setIncludedPromotionIds( promotion.getId() != null ? new Long[] { promotion.getId() } : null );
    submitterClaimQueryConstraint.setOpen( Boolean.FALSE );// we always need closed claims only
    submitterClaimQueryConstraint.setEligibleClaimsAllApprovedItem( false );
    submitterClaimQueryConstraint.setEligibleClaimsAtleastOneApprovedItem( false );
    List actualClaimsAsSubmitterList = claimDao.getClaimList( submitterClaimQueryConstraint );
    assertEquals( "actualClaimsAsSubmitterList wasn't equals to what was expected.", actualClaimsAsSubmitterList, expectedClaimsAsSubmitterList );

    // test atleast one approved
    // Build a claim, assign a submitter and save.
    List expectedAtleastOneApprovedItemClaimsList = new ArrayList();
    ProductClaim claim4 = ClaimDAOImplTest.buildStaticProductClaim( false );
    claim4.setSubmitter( participant2 );
    claim4.setPromotion( promotion );
    claim4.setNode( node );
    claim4.setSubmissionDate( new Date() );
    claim4.setClaimNumber( new Long( oracleSequenceDAO.getOracleSequenceNextValue( "claim_nbr_sq" ) ).toString() );

    ClaimProduct claimProduct = new ClaimProduct();
    // Product product = new Product();
    claimProduct.setProduct( product1 );
    // all claim products approved, so claim should close and run promo engine
    claimProduct.setApprovalStatusType( ApprovalStatusType.lookup( ApprovalStatusType.APPROVED ) );
    claim4.addClaimProduct( claimProduct );
    claimDao.saveClaim( claim4 );
    expectedAtleastOneApprovedItemClaimsList.add( claim4 );
    expectedAllApprovedItemClaimsList.add( claim4 );

    ProductClaim claim5 = ClaimDAOImplTest.buildStaticProductClaim( false );
    claim5.setSubmitter( participant3 );
    claim5.setPromotion( promotion );
    claim5.setNode( node );
    claim5.setSubmissionDate( new Date() );
    claim5.setClaimNumber( new Long( oracleSequenceDAO.getOracleSequenceNextValue( "claim_nbr_sq" ) ).toString() );

    ClaimProduct claimProduct2 = new ClaimProduct();
    claimProduct2.setProduct( product2 );
    // all claim products approved, so claim should close and run promo engine
    claimProduct2.setApprovalStatusType( ApprovalStatusType.lookup( ApprovalStatusType.APPROVED ) );
    claim5.addClaimProduct( claimProduct2 );
    claimDao.saveClaim( claim5 );
    expectedAtleastOneApprovedItemClaimsList.add( claim5 );
    expectedAllApprovedItemClaimsList.add( claim5 );
    flushAndClearSession();

    ProductClaimClaimQueryConstraint atleastOneItemApprovedClaimQueryConstraint = new ProductClaimClaimQueryConstraint();
    atleastOneItemApprovedClaimQueryConstraint.setIncludedPromotionIds( promotion.getId() != null ? new Long[] { promotion.getId() } : null );
    atleastOneItemApprovedClaimQueryConstraint.setOpen( Boolean.FALSE );// we always need closed
    // claims only
    atleastOneItemApprovedClaimQueryConstraint.setEligibleClaimsAllApprovedItem( false );
    atleastOneItemApprovedClaimQueryConstraint.setEligibleClaimsAtleastOneApprovedItem( true );
    List actualAtleastOneApprovedItemClaimsList = claimDao.getClaimList( atleastOneItemApprovedClaimQueryConstraint );

    assertTrue( " doesn't contains all the elements in the expected list ", actualAtleastOneApprovedItemClaimsList.containsAll( expectedAtleastOneApprovedItemClaimsList ) );

  }

  /**
   * Test getting a list of all claims by various constraints.
   */
  public void testGetAllApprovedClaimsWithEligibilityProductClaimClaimQueryConstraint()
  {
    // Build unique Strings for saving the claim
    String testString = String.valueOf( System.currentTimeMillis() % 5503032 );
    ProductCategoryDAO productCategoryDAO = getProductCategoryDAO();
    ProductDAO productDAO = getProductDAO();
    PromotionDAO promotionDAO = getPromotionDao();
    List expectedAllApprovedItemClaimsList = new ArrayList();

    // Build a productCategory
    ProductCategory productCategory = ProductCategoryDAOImplTest.buildProductCategory( testString );

    // Save the productCategory
    productCategoryDAO.saveProductCategory( productCategory );

    // Build a list of products
    Product product1 = ProductDAOImplTest.buildStaticProductDomainObject( "1" + testString, productCategory );
    Product product2 = ProductDAOImplTest.buildStaticProductDomainObject( "2" + testString, productCategory );
    Product product3 = ProductDAOImplTest.buildStaticProductDomainObject( "3" + testString, productCategory );

    // Save the list of products
    productDAO.save( product3 );
    productDAO.save( product2 );
    productDAO.save( product1 );

    // Build the expected List of products
    List expectedProductList = new ArrayList();
    expectedProductList.add( product1 );
    expectedProductList.add( product2 );
    expectedProductList.add( product3 );

    // Build a promotion
    ProductClaimPromotion promotion = PromotionDAOImplTest.buildProductClaimPromotion( testString );

    // Build a promotionPayoutGroup
    PromotionPayoutGroup promotionPayoutGroup = PromotionPayoutDAOImplTest.buildPromotionPayoutGroup();

    // Assign the promotion to the promotionPayoutGroup
    promotionPayoutGroup.setPromotion( promotion );

    // Build a list of promotionPayouts with the products
    PromotionPayout promotionPayout1 = PromotionPayoutDAOImplTest.buildPromotionPayout( product1 );
    PromotionPayout promotionPayout2 = PromotionPayoutDAOImplTest.buildPromotionPayout( product2 );
    PromotionPayout promotionPayout3 = PromotionPayoutDAOImplTest.buildPromotionPayout( product3 );

    // Assign the promotionPayouts to the promotionPayoutGroup
    promotionPayoutGroup.addPromotionPayout( promotionPayout1 );
    promotionPayoutGroup.addPromotionPayout( promotionPayout2 );
    promotionPayoutGroup.addPromotionPayout( promotionPayout3 );

    promotion.addPromotionPayoutGroup( promotionPayoutGroup );

    // Save the promotion
    promotionDAO.save( promotion );
    // Create the first claim participant.
    Participant participant1 = ParticipantDAOImplTest.buildUniqueParticipant( testString + "-participant-1" );
    participantDao.saveParticipant( participant1 );

    flushAndClearSession();

    // Build a unique node.
    Node node = NodeDAOImplTest.buildUniqueNode( testString );
    nodeDao.saveNode( node );

    // Date yesterday = new Date( new Date().getTime() - DateUtils.MILLIS_PER_DAY );

    // test all approved
    // Build a claim, assign a submitter and save.
    ProductClaim claim6 = ClaimDAOImplTest.buildStaticProductClaim( false );
    claim6.setSubmitter( participant1 );
    claim6.setPromotion( promotion );
    claim6.setNode( node );
    claim6.setSubmissionDate( new Date() );
    claim6.setClaimNumber( new Long( oracleSequenceDAO.getOracleSequenceNextValue( "claim_nbr_sq" ) ).toString() );

    ClaimProduct claimProduct3 = new ClaimProduct();
    claimProduct3.setProduct( product1 );
    // all claim products approved, so claim should close and run promo engine
    claimProduct3.setApprovalStatusType( ApprovalStatusType.lookup( ApprovalStatusType.APPROVED ) );
    claim6.addClaimProduct( claimProduct3 );
    claimDao.saveClaim( claim6 );
    expectedAllApprovedItemClaimsList.add( claim6 );

    ProductClaim claim7 = ClaimDAOImplTest.buildStaticProductClaim( false );
    claim7.setSubmitter( participant1 );
    claim7.setPromotion( promotion );
    claim7.setNode( node );
    claim7.setSubmissionDate( new Date() );
    claim7.setClaimNumber( new Long( oracleSequenceDAO.getOracleSequenceNextValue( "claim_nbr_sq" ) ).toString() );

    ClaimProduct claimProduct4 = new ClaimProduct();
    claimProduct4.setProduct( product2 );
    // all claim not products approved, so claim should close and run promo engine
    claimProduct4.setApprovalStatusType( ApprovalStatusType.lookup( ApprovalStatusType.HOLD ) );
    claim7.addClaimProduct( claimProduct4 );
    claimDao.saveClaim( claim7 );
    flushAndClearSession();

    ProductClaimClaimQueryConstraint allItemsApprovedClaimQueryConstraint = new ProductClaimClaimQueryConstraint();
    allItemsApprovedClaimQueryConstraint.setIncludedPromotionIds( promotion.getId() != null ? new Long[] { promotion.getId() } : null );
    allItemsApprovedClaimQueryConstraint.setOpen( Boolean.FALSE );// we always need closed claims
    // only
    allItemsApprovedClaimQueryConstraint.setEligibleClaimsAllApprovedItem( true );
    allItemsApprovedClaimQueryConstraint.setEligibleClaimsAtleastOneApprovedItem( false );
    List actualAllApprovedItemClaimsList = claimDao.getClaimList( allItemsApprovedClaimQueryConstraint );

    assertEquals( "actualAllApprovedItemClaimsList wasn't equals to what was expected.", actualAllApprovedItemClaimsList, expectedAllApprovedItemClaimsList );
  }

  /**
   * Test getting a list of product claims by various constraints.
   */
  public void testGetClaimListQuizClaims()
  {
    // Build a unique string to be used during testing.
    String uniqueString = String.valueOf( System.currentTimeMillis() % 5503032 );

    // Create a quiz
    Quiz quiz = QuizDAOImplTest.buildCompletedQuiz( buildUniqueString() );
    getQuizDAO().saveQuiz( quiz );

    // Create a promotion.
    QuizPromotion promotion = PromotionDAOImplTest.buildQuizPromotion( uniqueString + "promo" );
    promotion.setQuiz( quiz );

    promotionDao.save( promotion );

    // Create a claim submitter.
    Participant submitter = ParticipantDAOImplTest.buildUniqueParticipant( "submitter-" + uniqueString );
    userDao.saveUser( submitter );

    // Create a node.
    Node node = NodeDAOImplTest.buildUniqueNode( uniqueString );
    nodeDao.saveNode( node );

    flushAndClearSession();

    List expectedAllQuizClaimsForThisPromo = new ArrayList();
    List expectedPassClaimsForThisPromo = new ArrayList();
    List expectedFailClaimsForThisPromo = new ArrayList();
    // Create a failing claim.
    QuizClaim expectedFailedClaim = buildStaticQuizClaim( true );

    expectedFailedClaim.setSubmitter( submitter );
    expectedFailedClaim.setPromotion( promotion );
    expectedFailedClaim.setNode( node );
    expectedFailedClaim.setSubmissionDate( new Date() );
    expectedFailedClaim.setClaimNumber( new Long( oracleSequenceDAO.getOracleSequenceNextValue( "claim_nbr_sq" ) ).toString() );
    expectedFailedClaim.setQuestionCount( 2 );
    expectedFailedClaim.setPassingScore( 1 );
    expectedFailedClaim.setPass( Boolean.FALSE );
    expectedFailedClaim.setCurrentQuizQuestion( (QuizQuestion)quiz.getQuizQuestions().get( 0 ) );
    claimDao.saveClaim( expectedFailedClaim );
    expectedAllQuizClaimsForThisPromo.add( expectedFailedClaim );
    expectedFailClaimsForThisPromo.add( expectedFailedClaim );

    // Create a passing claim.
    QuizClaim expectedPassingClaim = buildStaticQuizClaim( true );

    expectedPassingClaim.setSubmitter( submitter );
    expectedPassingClaim.setPromotion( promotion );
    expectedPassingClaim.setNode( node );
    expectedPassingClaim.setSubmissionDate( new Date() );
    expectedPassingClaim.setClaimNumber( new Long( oracleSequenceDAO.getOracleSequenceNextValue( "claim_nbr_sq" ) ).toString() );
    expectedPassingClaim.setQuestionCount( 2 );
    expectedPassingClaim.setPassingScore( 1 );
    expectedPassingClaim.setCurrentQuizQuestion( (QuizQuestion)quiz.getQuizQuestions().get( 0 ) );
    expectedPassingClaim.setPass( Boolean.TRUE );
    claimDao.saveClaim( expectedPassingClaim );
    expectedAllQuizClaimsForThisPromo.add( expectedPassingClaim );
    expectedPassClaimsForThisPromo.add( expectedPassingClaim );

    // Create a claim with null pass value.
    QuizClaim expectedNullPassClaim = buildStaticQuizClaim( true );

    expectedNullPassClaim.setSubmitter( submitter );
    expectedNullPassClaim.setPromotion( promotion );
    expectedNullPassClaim.setNode( node );
    expectedNullPassClaim.setSubmissionDate( new Date() );
    expectedNullPassClaim.setClaimNumber( new Long( oracleSequenceDAO.getOracleSequenceNextValue( "claim_nbr_sq" ) ).toString() );
    expectedNullPassClaim.setQuestionCount( 2 );
    expectedNullPassClaim.setPassingScore( 1 );
    expectedNullPassClaim.setCurrentQuizQuestion( (QuizQuestion)quiz.getQuizQuestions().get( 0 ) );
    claimDao.saveClaim( expectedNullPassClaim );
    expectedAllQuizClaimsForThisPromo.add( expectedNullPassClaim );

    flushAndClearSession();

    QuizClaimQueryConstraint allClaimQueryForPromotionConstraint = new QuizClaimQueryConstraint();
    allClaimQueryForPromotionConstraint.setIncludedPromotionIds( new Long[] { promotion.getId() } );
    List allForPromotionClaimList = claimDao.getClaimList( allClaimQueryForPromotionConstraint );
    assertEquals( expectedAllQuizClaimsForThisPromo, allForPromotionClaimList );

    QuizClaimQueryConstraint passClaimQueryConstraint = new QuizClaimQueryConstraint();
    passClaimQueryConstraint.setIncludedPromotionIds( new Long[] { promotion.getId() } );
    passClaimQueryConstraint.setPass( Boolean.TRUE );
    List passClaimList = claimDao.getClaimList( passClaimQueryConstraint );
    assertEquals( expectedPassClaimsForThisPromo, passClaimList );

    QuizClaimQueryConstraint failClaimQueryConstraint = new QuizClaimQueryConstraint();
    failClaimQueryConstraint.setIncludedPromotionIds( new Long[] { promotion.getId() } );
    failClaimQueryConstraint.setPass( Boolean.FALSE );
    List failClaimList = claimDao.getClaimList( failClaimQueryConstraint );
    assertEquals( expectedFailClaimsForThisPromo, failClaimList );

  }

  /**
   * test isClaimElementValueUniqueWithinNode
   */
  public void testIsClaimElementValueUniqueWithinNode()
  {
    ClaimFormStepElement claimFormStepElement = new ClaimFormStepElement();
    claimFormStepElement.setId( new Long( 1 ) );
    claimFormStepElement.setClaimFormStep( new ClaimFormStep() );

    ClaimElement claimElement = new ClaimElement();
    claimElement.setClaimFormStepElement( claimFormStepElement );
    claimElement.setValue( "SOMETHING" );

    Node node = new Node();
    node.setId( new Long( 1 ) );

    Promotion promotion = new ProductClaimPromotion();
    promotion.setId( new Long( 1 ) );

    boolean isUnique = claimDao.isClaimElementValueUniqueWithinNode( claimElement, node, promotion );

    assertTrue( isUnique );
  }

  /**
   * test isClaimElementValueUniqueWithinNodeType
   */
  public void testIsClaimElementValueUniqueWithinNodeType()
  {
    ClaimFormStepElement claimFormStepElement = new ClaimFormStepElement();
    claimFormStepElement.setId( new Long( 1 ) );
    claimFormStepElement.setClaimFormStep( new ClaimFormStep() );

    ClaimElement claimElement = new ClaimElement();
    claimElement.setClaimFormStepElement( claimFormStepElement );
    claimElement.setValue( "SOMETHING" );

    Node node = new Node();
    node.setId( new Long( 1 ) );

    Promotion promotion = new ProductClaimPromotion();
    promotion.setId( new Long( 1 ) );

    boolean isUnique = claimDao.isClaimElementValueUniqueWithinNodeType( claimElement, node, promotion );

    assertTrue( isUnique );
  }

  /**
   * test isClaimElementValueUniqueWithinHierarchy
   */
  public void testIsClaimElementValueUniqueWithinHierarchy()
  {
    ClaimFormStepElement claimFormStepElement = new ClaimFormStepElement();
    claimFormStepElement.setId( new Long( 1 ) );
    claimFormStepElement.setClaimFormStep( new ClaimFormStep() );

    ClaimElement claimElement = new ClaimElement();
    claimElement.setClaimFormStepElement( claimFormStepElement );
    claimElement.setValue( "SOMETHING" );

    Node node = new Node();
    node.setId( new Long( 1 ) );

    Promotion promotion = new ProductClaimPromotion();
    promotion.setId( new Long( 1 ) );

    boolean isUnique = claimDao.isClaimElementValueUniqueWithinHierarchy( claimElement, node, promotion );

    assertTrue( isUnique );
  }

  /**
   * test isClaimProductCharacteristicUnique
   */
  public void testIsClaimProductCharacteristicUnique()
  {
    ProductCharacteristicType productCharacteristicType = new ProductCharacteristicType();
    productCharacteristicType.setId( new Long( 1 ) );

    ClaimProductCharacteristic claimProductCharacteristic = new ClaimProductCharacteristic();
    claimProductCharacteristic.setValue( "SOMETHING" );
    claimProductCharacteristic.setProductCharacteristicType( productCharacteristicType );

    Promotion promotion = new ProductClaimPromotion();
    promotion.setId( new Long( 1 ) );

    boolean isUnique = claimDao.isClaimProductCharacteristicUnique( claimProductCharacteristic, promotion );

    assertTrue( isUnique );
  }

  public void testGetClaimSubmittedCount()
  {

    // Build unique Strings for saving the claim
    String uniqueString = String.valueOf( System.currentTimeMillis() % 5503032 );

    Promotion promotion1 = promotionDao.save( PromotionDAOImplTest.buildProductClaimPromotion( uniqueString + "promo1" ) );
    Promotion promotion2 = promotionDao.save( PromotionDAOImplTest.buildProductClaimPromotion( uniqueString + "promo2" ) );

    // Build a unique node.
    Node node = NodeDAOImplTest.buildUniqueNode( uniqueString );
    nodeDao.saveNode( node );

    // Build unique participants.
    Participant submitter1 = ParticipantDAOImplTest.buildUniqueParticipant( uniqueString );

    userDao.saveUser( submitter1 );

    // #1 promo1
    Claim claim1 = ClaimDAOImplTest.buildStaticProductClaim( false );
    claim1.setSubmitter( submitter1 );
    claim1.setPromotion( promotion1 );
    claim1.setClaimNumber( new Long( oracleSequenceDAO.getOracleSequenceNextValue( "claim_nbr_sq" ) ).toString() );
    claim1.setSubmissionDate( new Date() );
    claim1.setNode( node );
    claimDao.saveClaim( claim1 );

    // #2 promo1
    Claim claim2 = ClaimDAOImplTest.buildStaticProductClaim( false );
    claim2.setSubmitter( submitter1 );
    claim2.setPromotion( promotion1 );
    claim2.setClaimNumber( new Long( oracleSequenceDAO.getOracleSequenceNextValue( "claim_nbr_sq" ) ).toString() );
    claim2.setSubmissionDate( new Date() );
    claim2.setNode( node );
    claimDao.saveClaim( claim2 );

    // #1 promo2
    Claim claim6 = ClaimDAOImplTest.buildStaticProductClaim( false );
    claim6.setSubmitter( submitter1 );
    claim6.setPromotion( promotion2 );
    claim6.setClaimNumber( new Long( oracleSequenceDAO.getOracleSequenceNextValue( "claim_nbr_sq" ) ).toString() );
    claim6.setSubmissionDate( new Date() );
    claim6.setNode( node );
    claimDao.saveClaim( claim6 );

    int result = claimDao.getClaimSubmittedCount( promotion1.getId() );
    assertEquals( 2, result );
  }

  public void testGetEarningsForClaim()
  {

    String uniqueString = String.valueOf( System.currentTimeMillis() % 55031 );

    // Build and save an activity.
    SalesActivity activity = ActivityDAOImplTest.buildSalesActivity( uniqueString, false );
    getActivityDAO().saveActivity( activity );
    HibernateSessionManager.getSession().flush();

    // Build and save a journal1 transaction.
    Journal journal1 = JournalDAOImplTest.buildAndSaveJournal( uniqueString );
    journal1.setTransactionAmount( new Long( "100" ) );
    getJournalDAO().saveJournalEntry( journal1 );
    HibernateSessionManager.getSession().flush();

    // Build and save a journal transaction.
    Journal journal = JournalDAOImplTest.buildAndSaveJournal( uniqueString + "fds" );
    journal.setTransactionAmount( new Long( "50" ) );
    journal.setJournalStatusType( JournalStatusType.lookup( JournalStatusType.APPROVE ) );
    journal.setParticipant( activity.getParticipant() );
    getJournalDAO().saveJournalEntry( journal );
    HibernateSessionManager.getSession().flush();

    // Associate the activity and the journal1 transaction.
    ActivityJournal activityJournal1 = new ActivityJournal();
    activityJournal1.setJournal( journal1 );
    activity.addActivityJournal( activityJournal1 );

    ActivityJournal activityJournal = new ActivityJournal();
    activityJournal.setJournal( journal );
    activity.addActivityJournal( activityJournal );

    getActivityDAO().saveActivity( activity );
    flushAndClearSession();

    Long result = claimDao.getEarningsForClaim( activity.getClaim().getId(), journal.getParticipant().getId() );
    assertEquals( 50, result.intValue() );
  }

  public void testGetTeamClaimsByClaimId()
  {
    List<AbstractRecognitionClaim> teamClaims = claimDao.getTeamClaimsByClaimId( 10002L );
    assertNotNull( teamClaims );
  }

  public void testFindMostRecentRecipientsFor()
  {
    List<ClaimRecipient> recipients = claimDao.findMostRecentRecipientsFor( new Long( 1 ) );
    assertNotNull( recipients );
  }

  public void testGetCelebrationClaims()
  {
    // Build a unique string to be used during testing.
    String uniqueString = String.valueOf( System.currentTimeMillis() % 5503032 );

    // Create a promotion.
    RecognitionPromotion promotion = PromotionDAOImplTest.buildRecognitionPromotion( uniqueString + "promo" );
    promotion.setIncludeCelebrations( true );
    promotionDao.save( promotion );

    // Create a claim.
    RecognitionClaim expectedClaim = buildStaticRecognitionClaim( false );

    testAbstractRecognitionSaveUpdateAndGetClaimById( promotion, expectedClaim );

    assertNotNull( "Id of expectedClaim is null.", expectedClaim.getId() );
    assertTrue( "Celebration is active in promotion: ", promotion.isIncludeCelebrations() );

    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
    associationRequestCollection.add( new ClaimAssociationRequest( ClaimAssociationRequest.CLAIM_RECIPIENTS ) );

    AbstractRecognitionClaim actualClaim = (AbstractRecognitionClaim)claimDao.getClaimByIdWithAssociations( expectedClaim.getId(), associationRequestCollection );

    List<ClaimRecipient> claimRecipients = new ArrayList<ClaimRecipient>( actualClaim.getClaimRecipients() );
    ClaimRecipient claimRecipient = claimRecipients.get( 0 );

    List<Long> claimIds = claimDao.getCelebrationClaims( claimRecipient.getRecipient().getId() );
    assertNotNull( claimIds );
    assertEquals( claimIds.size(), 1 );
  }

  // ---------------------------------------------------------------------------
  // Build Methods
  // ---------------------------------------------------------------------------

  /**
   * Builds a {@link ProductClaim} object.
   * 
   * @return a new {@link ProductClaim} object.
   */
  public static ProductClaim buildStaticProductClaim()
  {
    return ClaimDAOImplTest.buildStaticProductClaim( true );
  }

  /**
   * Builds a {@link ProductClaim} object given the "is open" property.
   * 
   * @param isOpen
   * @return a new {@link ProductClaim} object.
   */
  public static ProductClaim buildStaticProductClaim( boolean isOpen )
  {

    ProductClaim claim = new ProductClaim();
    claim.setOpen( isOpen );

    return claim;
  }

  /**
   * Builds a {@link Claim} object.
   * 
   * @param uniqueString a unique string.
   * @return a new {@link Claim} object.
   */
  public static ProductClaim buildProductClaim( String uniqueString )
  {
    Promotion promotion = PromotionDAOImplTest.buildProductClaimPromotion( uniqueString );
    getPromotionDao().save( promotion );

    Participant submitter = ParticipantDAOImplTest.buildUniqueParticipant( uniqueString );
    getUserDao().saveUser( submitter );

    Node node = NodeDAOImplTest.buildUniqueNode( uniqueString );
    getNodeDao().saveNode( node );

    ClaimForm claimForm = promotion.getClaimForm();
    ClaimFormStep claimFormStep = (ClaimFormStep)claimForm.getClaimFormSteps().get( 0 );
    ClaimFormStepElement claimFormStepElement = (ClaimFormStepElement)claimFormStep.getClaimFormStepElements().get( 0 );

    ClaimElement claimElement = new ClaimElement();
    claimElement.setClaimFormStepElement( claimFormStepElement );
    claimElement.setValue( uniqueString );

    ProductClaim claim = new ProductClaim();
    claim.setOpen( true );
    claim.setPromotion( promotion );
    claim.setSubmitter( submitter );
    claim.setNode( node );
    claim.addClaimElement( claimElement );
    // claim.setClaimNumber( uniqueString );
    claim.setClaimNumber( new Long( getOracleSequenceDao().getOracleSequenceNextValue( "claim_nbr_sq" ) ).toString() );

    claim.setSubmissionDate( new Date() );

    return claim;
  }

  /**
   * Builds a {@link NominationClaim} object given the "is open" property.
   *
   * @param isOpen
   * @return a new {@link NominationClaim} object.
   */
  public static NominationClaim buildStaticNominationClaim( boolean isOpen )
  {

    NominationClaim claim = new NominationClaim();
    claim.setOpen( isOpen );
    claim.setSource( RecognitionClaimSource.UNKNOWN );
    claim.setNominationStatusType( NominationClaimStatusType.lookup( NominationClaimStatusType.INCOMPLETE ) );

    return claim;
  }

  /**
   * Builds a {@link RecognitionClaim} object.
   * 
   * @return a new {@link RecognitionClaim} object.
   */
  public static RecognitionClaim buildStaticRecognitionClaim()
  {
    return ClaimDAOImplTest.buildStaticRecognitionClaim( true );
  }

  /**
   * Builds a {@link RecognitionClaim} object given the "is open" property.
   * 
   * @param isOpen
   * @return a new {@link RecognitionClaim} object.
   */
  public static RecognitionClaim buildStaticRecognitionClaim( boolean isOpen )
  {

    RecognitionClaim claim = new RecognitionClaim();
    claim.setOpen( isOpen );
    claim.setSource( RecognitionClaimSource.UNKNOWN );

    return claim;
  }

  /**
   * Builds a {@link QuizClaim} object given the "is open" property.
   * 
   * @param isOpen
   * @return a new {@link QuizClaim} object.
   */
  public static QuizClaim buildStaticQuizClaim( boolean isOpen )
  {

    QuizClaim claim = new QuizClaim();
    claim.setOpen( isOpen );

    return claim;
  }

  /**
   * Builds a {@link Claim} object.
   * 
   * @param uniqueString a unique string.
   * @return a new {@link Claim} object.
   */
  public static RecognitionClaim buildRecognitionClaim( String uniqueString )
  {
    Promotion promotion = PromotionDAOImplTest.buildRecognitionPromotion( uniqueString );
    promotion.setPromoNameAssetCode( "promotion_name.10000832" );
    getPromotionDao().save( promotion );

    Participant submitter = ParticipantDAOImplTest.buildUniqueParticipant( uniqueString );
    getUserDao().saveUser( submitter );

    Node node = NodeDAOImplTest.buildUniqueNode( uniqueString );
    getNodeDao().saveNode( node );

    ClaimForm claimForm = promotion.getClaimForm();
    ClaimFormStep claimFormStep = (ClaimFormStep)claimForm.getClaimFormSteps().get( 0 );
    ClaimFormStepElement claimFormStepElement = (ClaimFormStepElement)claimFormStep.getClaimFormStepElements().get( 0 );

    ClaimElement claimElement = new ClaimElement();
    claimElement.setClaimFormStepElement( claimFormStepElement );
    claimElement.setValue( uniqueString );

    RecognitionClaim claim = new RecognitionClaim();
    claim.setOpen( true );
    claim.setSubmitterComments( "test" );
    claim.setPromotion( promotion );
    claim.setSubmitter( submitter );
    claim.setNode( node );
    claim.setSource( RecognitionClaimSource.WEB );
    claim.addClaimElement( claimElement );
    // claim.setClaimNumber( uniqueString );
    claim.setClaimNumber( new Long( getOracleSequenceDao().getOracleSequenceNextValue( "claim_nbr_sq" ) ).toString() );
    claim.setSubmissionDate( new Date() );

    return claim;
  }

  // ---------------------------------------------------------------------------
  // DAO Getter Methods
  // ---------------------------------------------------------------------------

  private static ActivityDAO getActivityDAO()
  {
    return (ActivityDAO)ApplicationContextFactory.getApplicationContext().getBean( ActivityDAO.BEAN_NAME );
  }

  private static JournalDAO getJournalDAO()
  {
    return (JournalDAO)ApplicationContextFactory.getApplicationContext().getBean( JournalDAO.BEAN_NAME );
  }

  /**
   * Returns a reference to the claim DAO.
   * 
   * @return a reference to the claim DAO.
   */
  private static ClaimDAO getClaimDao()
  {
    return (ClaimDAO)ApplicationContextFactory.getApplicationContext().getBean( ClaimDAO.BEAN_NAME );
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

  /**
   * Returns a reference to the node DAO.
   * 
   * @return a reference to the node DAO.
   */
  private static NodeDAO getNodeDao()
  {
    return (NodeDAO)ApplicationContextFactory.getApplicationContext().getBean( NodeDAO.BEAN_NAME );
  }

  /**
   * Returns a reference to the participant DAO.
   * 
   * @return a reference to the participant DAO.
   */
  private static ParticipantDAO getParticipantDao()
  {
    return (ParticipantDAO)ApplicationContextFactory.getApplicationContext().getBean( ParticipantDAO.BEAN_NAME );
  }

  /**
   * Returns a reference to the product DAO.
   * 
   * @return a reference to the product DAO.
   */
  private static ProductDAO getProductDao()
  {
    return (ProductDAO)ApplicationContextFactory.getApplicationContext().getBean( ProductDAO.BEAN_NAME );
  }

  /**
   * Returns a reference to the promotion DAO.
   * 
   * @return a reference to the promotion DAO.
   */
  private static PromotionDAO getPromotionDao()
  {
    return (PromotionDAO)ApplicationContextFactory.getApplicationContext().getBean( PromotionDAO.BEAN_NAME );
  }

  /**
   * Returns a reference to the user DAO.
   * 
   * @return a reference to the user DAO.
   */
  private static UserDAO getUserDao()
  {
    return (UserDAO)ApplicationContextFactory.getApplicationContext().getBean( UserDAO.BEAN_NAME );
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
   * Returns a reference to the quiz DAO.
   * 
   * @return a reference to the quiz DAO.
   */
  private static QuizDAO getQuizDAO()
  {
    return (QuizDAO)ApplicationContextFactory.getApplicationContext().getBean( QuizDAO.BEAN_NAME );
  }

  private static PromotionParticipantDAO getPromotionParticipantDAO()
  {
    return (PromotionParticipantDAO)ApplicationContextFactory.getApplicationContext().getBean( "promotionParticipantDAO" );
  }

  /**
   * Uses the ApplicationContextFactory to look up the ProductDAO implementation.
   * 
   * @return ProductDAO
   */
  private ProductDAO getProductDAO()
  {
    return (ProductDAO)ApplicationContextFactory.getApplicationContext().getBean( "productDAO" );
  }

  /**
   * Uses the ApplicationContextFactory to look up the ProductCategoryDAO implementation.
   * 
   * @return ProductCategoryDAO
   */
  private ProductCategoryDAO getProductCategoryDAO()
  {
    return (ProductCategoryDAO)ApplicationContextFactory.getApplicationContext().getBean( "productCategoryDAO" );
  }

  /**
   * method used to get the time period and the activity id
   */
  public void testGetActivityTimePeriod()
  {
    String uniqueString = String.valueOf( System.currentTimeMillis() % 5503032 );
    RecognitionPromotion promotion = PromotionDAOImplTest.buildRecognitionPromotion( uniqueString + "promo" );
    promotion.setIncludeCelebrations( true );
    getPromotionDao().save( promotion );
    flushAndClearSession();

    RecognitionClaim expectedClaim = buildStaticRecognitionClaim( false );
    testAbstractRecognitionSaveUpdateAndGetClaimById( promotion, expectedClaim );

    assertNotNull( "Id of expectedClaim is null.", expectedClaim.getId() );
    assertTrue( "Celebration is active in promotion: ", promotion.isIncludeCelebrations() );

    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
    associationRequestCollection.add( new ClaimAssociationRequest( ClaimAssociationRequest.CLAIM_RECIPIENTS ) );
    AbstractRecognitionClaim actualClaim = (AbstractRecognitionClaim)getClaimDao().getClaimByIdWithAssociations( expectedClaim.getId(), associationRequestCollection );
    List<ClaimRecipient> claimRecipients = new ArrayList<ClaimRecipient>( actualClaim.getClaimRecipients() );
    ClaimRecipient claimRecipient = claimRecipients.get( 0 );
    List claimIds = getClaimDao().getActivityTimePeriod( actualClaim.getId() );

    assertNotNull( claimIds );
    assertEquals( claimIds.size(), 0 );
  }

}
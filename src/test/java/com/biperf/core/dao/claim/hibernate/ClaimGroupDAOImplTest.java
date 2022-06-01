/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javatest/com/biperf/core/dao/claim/hibernate/ClaimGroupDAOImplTest.java,v $
 */

package com.biperf.core.dao.claim.hibernate;

import java.util.Date;
import java.util.Iterator;
import java.util.Set;

import com.biperf.core.dao.claim.ClaimDAO;
import com.biperf.core.dao.claim.ClaimGroupDAO;
import com.biperf.core.dao.hibernate.BaseDAOTest;
import com.biperf.core.dao.hierarchy.NodeDAO;
import com.biperf.core.dao.hierarchy.hibernate.NodeDAOImplTest;
import com.biperf.core.dao.oracle.OracleSequenceDAO;
import com.biperf.core.dao.participant.ParticipantDAO;
import com.biperf.core.dao.participant.UserDAO;
import com.biperf.core.dao.participant.hibernate.ParticipantDAOImplTest;
import com.biperf.core.dao.product.ProductDAO;
import com.biperf.core.dao.promotion.PromotionDAO;
import com.biperf.core.dao.promotion.hibernate.PromotionDAOImplTest;
import com.biperf.core.domain.claim.AbstractRecognitionClaim;
import com.biperf.core.domain.claim.Claim;
import com.biperf.core.domain.claim.ClaimElement;
import com.biperf.core.domain.claim.ClaimForm;
import com.biperf.core.domain.claim.ClaimFormStep;
import com.biperf.core.domain.claim.ClaimFormStepElement;
import com.biperf.core.domain.claim.ClaimGroup;
import com.biperf.core.domain.claim.ClaimRecipient;
import com.biperf.core.domain.claim.NominationClaim;
import com.biperf.core.domain.claim.ProductClaim;
import com.biperf.core.domain.claim.QuizClaim;
import com.biperf.core.domain.claim.RecognitionClaim;
import com.biperf.core.domain.claim.RecognitionClaimSource;
import com.biperf.core.domain.enums.ApprovalStatusType;
import com.biperf.core.domain.enums.NominationEvaluationType;
import com.biperf.core.domain.hierarchy.Node;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.NominationPromotion;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.user.UserNode;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.claim.ClaimAssociationRequest;
import com.biperf.core.utils.ApplicationContextFactory;
import com.biperf.core.utils.GuidUtils;

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
public class ClaimGroupDAOImplTest extends BaseDAOTest
{

  // ---------------------------------------------------------------------------
  // Fields
  // ---------------------------------------------------------------------------

  ClaimGroupDAO claimGroupDao = getClaimGroupDao();
  ClaimDAO claimDao = getClaimDao();
  NodeDAO nodeDao = getNodeDao();
  ParticipantDAO participantDao = getParticipantDao();
  ProductDAO productDao = getProductDao();
  PromotionDAO promotionDao = getPromotionDao();
  UserDAO userDao = getUserDao();
  OracleSequenceDAO oracleSequenceDAO = getOracleSequenceDao();

  // ---------------------------------------------------------------------------
  // Test Methods
  // ---------------------------------------------------------------------------

  /**
   * Tests saving and getting a nomination claim by the ID.
   */
  public void testCumNomClaimGroupSaveUpdateAndGetById()
  {
    // Build a unique string to be used during testing.
    String uniqueString = String.valueOf( System.currentTimeMillis() % 5503032 );

    // Create a promotion.
    NominationPromotion promotion = PromotionDAOImplTest.buildNominationPromotion( uniqueString + "promo" );
    promotion.setEvaluationType( NominationEvaluationType.lookup( NominationEvaluationType.CUMULATIVE ) );
    promotionDao.save( promotion );

    // Create a claim.
    AbstractRecognitionClaim expectedClaim = buildStaticNominationClaim( true );

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

    flushAndClearSession();

    // Create a claim.
    expectedClaim.setSubmitter( submitter );
    expectedClaim.setPromotion( promotion );
    expectedClaim.setNode( node );
    expectedClaim.setSubmissionDate( new Date() );
    expectedClaim.setClaimNumber( new Long( oracleSequenceDAO.getOracleSequenceNextValue( "claim_nbr_sq" ) ).toString() );

    expectedClaim.addClaimRecipient( claimRecipient1 );
    Set expectedClaimRecipients = expectedClaim.getClaimRecipients();
    assertEquals( 1, expectedClaimRecipients.size() );

    // Test saving a claim.
    claimDao.saveClaim( expectedClaim );

    assertNotNull( "Id of expectedClaim is null.", expectedClaim.getId() );

    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
    associationRequestCollection.add( new ClaimAssociationRequest( ClaimAssociationRequest.CLAIM_RECIPIENTS ) );

    AbstractRecognitionClaim actualClaim = (AbstractRecognitionClaim)claimDao.getClaimByIdWithAssociations( expectedClaim.getId(), associationRequestCollection );

    assertEquals( "Actual Claim wasn't equal to what was expected.", expectedClaim, actualClaim );

    assertEquals( "Expected claim recipients to be equal.", actualClaim.getClaimRecipients(), expectedClaimRecipients );
    assertNotNull( "Expected node to be not null.", actualClaim.getNode() );
    assertEquals( "Expected nodes to be equal.", actualClaim.getNode(), expectedClaim.getNode() );

    ClaimGroup expectedClaimGroup = new ClaimGroup();

    expectedClaimGroup.setApprovalRound( new Long( 1 ) );
    expectedClaimGroup.setApprovalStatusType( ApprovalStatusType.lookup( ApprovalStatusType.PENDING ) );
    expectedClaimGroup.setSerialId( GuidUtils.generateGuid() );
    expectedClaimGroup.setParticipant( claimRecipient1.getRecipient() );
    expectedClaimGroup.setNode( recipient1Node1 );
    expectedClaimGroup.setPromotion( promotion );
    expectedClaimGroup.setApproverComments( "Yo" );
    expectedClaimGroup.addClaim( expectedClaim );
    expectedClaimGroup = claimGroupDao.saveClaimGroup( expectedClaimGroup );

    // hydrate before clearing session
    expectedClaimGroup.getClaims().iterator();
    expectedClaimGroup.getApprovableItemApprovers().iterator();

    flushAndClearSession();
    assertNotNull( expectedClaimGroup.getId() );

    ClaimGroup savedClaimGroup = claimGroupDao.getClaimGroupById( expectedClaimGroup.getId() );
    assertNotNull( savedClaimGroup );
    assertDomainObjectEquals( expectedClaimGroup, savedClaimGroup );

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
    return ClaimGroupDAOImplTest.buildStaticProductClaim( true );
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
    return claim;
  }

  /**
   * Builds a {@link RecognitionClaim} object.
   * 
   * @return a new {@link RecognitionClaim} object.
   */
  public static RecognitionClaim buildStaticRecognitionClaim()
  {
    return ClaimGroupDAOImplTest.buildStaticRecognitionClaim( true );
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
    claim.setPromotion( promotion );
    claim.setSubmitter( submitter );
    claim.setNode( node );
    claim.addClaimElement( claimElement );
    // claim.setClaimNumber( uniqueString );
    claim.setClaimNumber( new Long( getOracleSequenceDao().getOracleSequenceNextValue( "claim_nbr_sq" ) ).toString() );
    claim.setSubmissionDate( new Date() );
    claim.setSource( RecognitionClaimSource.WEB );

    return claim;
  }

  /**
   * Builds a {@link Claim} object.
   * 
   * @param uniqueString a unique string.
   * @return a new {@link Claim} object.
   */
  public static NominationClaim buildNominationClaim( String uniqueString )
  {
    Promotion promotion = PromotionDAOImplTest.buildNominationPromotion( uniqueString );
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

    NominationClaim claim = new NominationClaim();
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

  // ---------------------------------------------------------------------------
  // DAO Getter Methods
  // ---------------------------------------------------------------------------

  /**
   * Returns a reference to the claim DAO.
   * 
   * @return a reference to the claim DAO.
   */
  private static ClaimGroupDAO getClaimGroupDao()
  {
    return (ClaimGroupDAO)ApplicationContextFactory.getApplicationContext().getBean( ClaimGroupDAO.BEAN_NAME );
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
   * Returns a reference to the claim DAO.
   * 
   * @return a reference to the claim DAO.
   */
  private static ClaimDAO getClaimDao()
  {
    return (ClaimDAO)ApplicationContextFactory.getApplicationContext().getBean( ClaimDAO.BEAN_NAME );
  }

}
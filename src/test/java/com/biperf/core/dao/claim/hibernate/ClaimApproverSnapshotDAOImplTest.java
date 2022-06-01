/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javatest/com/biperf/core/dao/claim/hibernate/ClaimApproverSnapshotDAOImplTest.java,v $
 */

package com.biperf.core.dao.claim.hibernate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.biperf.core.dao.claim.ClaimApproverSnapshotDAO;
import com.biperf.core.dao.claim.ClaimDAO;
import com.biperf.core.dao.hibernate.BaseDAOTest;
import com.biperf.core.dao.hierarchy.NodeDAO;
import com.biperf.core.dao.hierarchy.hibernate.NodeDAOImplTest;
import com.biperf.core.dao.oracle.OracleSequenceDAO;
import com.biperf.core.dao.participant.ParticipantDAO;
import com.biperf.core.dao.participant.UserDAO;
import com.biperf.core.dao.participant.hibernate.ParticipantDAOImplTest;
import com.biperf.core.dao.promotion.PromotionDAO;
import com.biperf.core.dao.promotion.hibernate.PromotionDAOImplTest;
import com.biperf.core.domain.claim.ClaimApproverSnapshot;
import com.biperf.core.domain.claim.ProductClaim;
import com.biperf.core.domain.enums.ApprovableTypeEnum;
import com.biperf.core.domain.enums.PromotionType;
import com.biperf.core.domain.enums.TimeZoneId;
import com.biperf.core.domain.hierarchy.Node;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.utils.ApplicationContextFactory;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.DateFormatterUtil;
import com.biperf.core.utils.DateUtils;

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
public class ClaimApproverSnapshotDAOImplTest extends BaseDAOTest
{

  // ---------------------------------------------------------------------------
  // Fields
  // ---------------------------------------------------------------------------

  ClaimDAO claimDao = getClaimDao();
  NodeDAO nodeDao = getNodeDao();
  ParticipantDAO participantDao = getParticipantDao();
  PromotionDAO promotionDao = getPromotionDao();
  UserDAO userDao = getUserDao();
  OracleSequenceDAO oracleSequenceDAO = getOracleSequenceDao();

  ClaimApproverSnapshotDAO claimApproverSnapshotDAO = (ClaimApproverSnapshotDAO)BeanLocator.getBean( ClaimApproverSnapshotDAO.BEAN_NAME );

  // ---------------------------------------------------------------------------
  // Test Methods
  // ---------------------------------------------------------------------------

  public void testSaveAndGetSnapshots()
  {

    // Build a unique string to be used during testing.
    String uniqueString = String.valueOf( System.currentTimeMillis() % 5503032 );

    // Create a promotion.
    Promotion promotion = PromotionDAOImplTest.buildProductClaimPromotion( uniqueString + "promo" );
    promotionDao.save( promotion );

    // Create a claim submitter.
    Participant submitter = ParticipantDAOImplTest.buildUniqueParticipant( buildUniqueString() );
    userDao.saveUser( submitter );

    // Create approvers.
    Participant approver1 = ParticipantDAOImplTest.buildUniqueParticipant( buildUniqueString() );
    userDao.saveUser( approver1 );
    Participant approver2 = ParticipantDAOImplTest.buildUniqueParticipant( buildUniqueString() );
    userDao.saveUser( approver2 );
    Participant approver3 = ParticipantDAOImplTest.buildUniqueParticipant( buildUniqueString() );
    userDao.saveUser( approver3 );

    // Create a node.
    Node node = NodeDAOImplTest.buildUniqueNode( uniqueString );
    nodeDao.saveNode( node );

    // Create two claims.
    ProductClaim claim1 = ClaimDAOImplTest.buildStaticProductClaim( true );
    claim1.setSubmitter( submitter );
    claim1.setProxyUser( null );
    claim1.setPromotion( promotion );
    claim1.setNode( node );
    claim1.setSubmissionDate( new Date() );
    claim1.setClaimNumber( new Long( oracleSequenceDAO.getOracleSequenceNextValue( "claim_nbr_sq" ) ).toString() );
    claimDao.saveClaim( claim1 );

    ProductClaim claim2 = ClaimDAOImplTest.buildStaticProductClaim( true );
    claim2.setSubmitter( submitter );
    claim2.setProxyUser( null );
    claim2.setPromotion( promotion );
    claim2.setNode( node );
    claim2.setSubmissionDate( new Date() );
    claim2.setClaimNumber( new Long( oracleSequenceDAO.getOracleSequenceNextValue( "claim_nbr_sq" ) ).toString() );
    claimDao.saveClaim( claim2 );

    ApprovableTypeEnum claimApprovableType = ApprovableTypeEnum.CLAIM;

    ClaimApproverSnapshot snapshotClaim1Approver1 = new ClaimApproverSnapshot( claim1.getId(), approver1.getId(), null, claimApprovableType );
    claimApproverSnapshotDAO.saveClaimApproverSnapshot( snapshotClaim1Approver1 );
    ClaimApproverSnapshot snapshotClaim1Approver2 = new ClaimApproverSnapshot( claim1.getId(), approver2.getId(), null, claimApprovableType );
    claimApproverSnapshotDAO.saveClaimApproverSnapshot( snapshotClaim1Approver2 );
    ClaimApproverSnapshot snapshotClaim2Approver2 = new ClaimApproverSnapshot( claim2.getId(), approver2.getId(), null, claimApprovableType );
    claimApproverSnapshotDAO.saveClaimApproverSnapshot( snapshotClaim2Approver2 );
    ClaimApproverSnapshot snapshotClaim2Approver3 = new ClaimApproverSnapshot( claim2.getId(), approver3.getId(), null, claimApprovableType );
    claimApproverSnapshotDAO.saveClaimApproverSnapshot( snapshotClaim2Approver3 );

    flushAndClearSession();

    ClaimApproverSnapshotQueryConstraint claim1SnapshotQueryConstraint = new ClaimApproverSnapshotQueryConstraint();
    List<Long> claimIds = new ArrayList<Long>();
    claimIds.add( claim1.getId() );

    claim1SnapshotQueryConstraint.setApprovableId( claimIds );
    ApprovableTypeEnum approvableType = claimApprovableType;
    claim1SnapshotQueryConstraint.setApprovableType( approvableType );
    List claim1Snapshots = claimApproverSnapshotDAO.getClaimApproverSnapshotList( claim1SnapshotQueryConstraint );
    assertEquals( 2, claim1Snapshots.size() );
    assertTrue( claim1Snapshots.contains( snapshotClaim1Approver1 ) );
    assertTrue( claim1Snapshots.contains( snapshotClaim1Approver2 ) );

    ClaimApproverSnapshotQueryConstraint approver2SnapshotQueryConstraint = new ClaimApproverSnapshotQueryConstraint();
    approver2SnapshotQueryConstraint.setApproverUserId( approver2.getId() );
    List approver2Snapshots = claimApproverSnapshotDAO.getClaimApproverSnapshotList( approver2SnapshotQueryConstraint );
    assertEquals( 2, approver2Snapshots.size() );
    assertTrue( approver2Snapshots.contains( snapshotClaim1Approver2 ) );
    assertTrue( approver2Snapshots.contains( snapshotClaim2Approver2 ) );

    ApproverSeekingClaimQueryConstraint approver2ClaimQueryConstraint = new ApproverSeekingClaimQueryConstraint();
    approver2ClaimQueryConstraint.setOpen( Boolean.TRUE );
    approver2ClaimQueryConstraint.setExpired( Boolean.FALSE );
    approver2ClaimQueryConstraint.setApprovableUserId( approver2.getId() );
    approver2ClaimQueryConstraint.setClaimPromotionType( PromotionType.lookup( PromotionType.PRODUCT_CLAIM ) );

    String datePattern = DateFormatterUtil.getOracleDatePattern( Locale.ENGLISH.toString() );
    Date toDay = DateUtils.applyTimeZone( new Date(), TimeZoneId.CST );
    approver2ClaimQueryConstraint.setToDate( DateUtils.toDisplayString( toDay, Locale.ENGLISH ) );
    approver2ClaimQueryConstraint.setDatePattern( datePattern );

    List approver2Claims = claimDao.getClaimList( approver2ClaimQueryConstraint );
    assertEquals( 2, approver2Claims.size() );

    ClaimApproverSnapshotQueryConstraint bothSnapshotQueryConstraint = new ClaimApproverSnapshotQueryConstraint();
    bothSnapshotQueryConstraint.setApproverUserId( approver2.getId() );

    List<Long> claimIds2 = new ArrayList<Long>();
    claimIds.add( claim2.getId() );

    bothSnapshotQueryConstraint.setApprovableId( claimIds2 );
    bothSnapshotQueryConstraint.setApprovableType( approvableType );
    List bothSnapshots = claimApproverSnapshotDAO.getClaimApproverSnapshotList( bothSnapshotQueryConstraint );
    assertEquals( 1, bothSnapshots.size() );
    assertTrue( bothSnapshots.contains( snapshotClaim2Approver2 ) );

    // test delete
    flushAndClearSession();
    ClaimApproverSnapshotQueryConstraint claim1Approver2SnapshotQueryConstraint = new ClaimApproverSnapshotQueryConstraint();
    claim1Approver2SnapshotQueryConstraint.setApproverUserId( approver2.getId() );
    claim1Approver2SnapshotQueryConstraint.setApprovableId( claimIds );
    claim1Approver2SnapshotQueryConstraint.setApprovableType( approvableType );
    List claim1Approver2Snapshots = claimApproverSnapshotDAO.getClaimApproverSnapshotList( claim1Approver2SnapshotQueryConstraint );
    assertEquals( 1, claim1Approver2Snapshots.size() );
    ClaimApproverSnapshot claim1Approver2Snapshot = (ClaimApproverSnapshot)claim1Approver2Snapshots.get( 0 );

    claimApproverSnapshotDAO.deleteClaimApproverSnapshot( claim1Approver2Snapshot );

    ClaimApproverSnapshotQueryConstraint postDeleteClaim1SnapshotQueryConstraint = new ClaimApproverSnapshotQueryConstraint();
    postDeleteClaim1SnapshotQueryConstraint.setApprovableId( claimIds );
    postDeleteClaim1SnapshotQueryConstraint.setApprovableType( approvableType );
    List postDeleteClaim1Snapshots = claimApproverSnapshotDAO.getClaimApproverSnapshotList( postDeleteClaim1SnapshotQueryConstraint );
    assertEquals( 1, postDeleteClaim1Snapshots.size() );
    assertTrue( postDeleteClaim1Snapshots.contains( snapshotClaim1Approver1 ) );
  }
  // public void testSaveAndGetSnapshotsClaimGroup()
  // {
  //
  // // Build a unique string to be used during testing.
  // String uniqueString = String.valueOf( System.currentTimeMillis() % 5503032 );
  //
  // // Create a promotion.
  // NominationPromotion promotion = PromotionDAOImplTest.buildNominationPromotion( uniqueString +
  // "promo" );
  // promotion.setEvaluationType(
  // NominationEvaluationType.lookup(NominationEvaluationType.CUMULATIVE ));
  // promotionDao.save( promotion );
  //
  // // Create a claim submitter.
  // Participant submitter = ParticipantDAOImplTest.buildUniqueParticipant( buildUniqueString() );
  // userDao.saveUser( submitter );
  //
  // // Create approvers.
  // Participant approver1 = ParticipantDAOImplTest.buildUniqueParticipant( buildUniqueString() );
  // userDao.saveUser( approver1 );
  // Participant approver2 = ParticipantDAOImplTest.buildUniqueParticipant( buildUniqueString() );
  // userDao.saveUser( approver2 );
  // Participant approver3 = ParticipantDAOImplTest.buildUniqueParticipant( buildUniqueString() );
  // userDao.saveUser( approver3 );
  //
  // // Create a node.
  // Node node = NodeDAOImplTest.buildUniqueNode( uniqueString );
  // nodeDao.saveNode( node );
  //
  // // Create two claims.
  // ProductClaim claim1 = ClaimDAOImplTest.buildStaticProductClaim( true );
  // claim1.setSubmitter( submitter );
  // claim1.setProxyUser( null );
  // claim1.setPromotion( promotion );
  // claim1.setNode( node );
  // claim1.setSubmissionDate( new Date() );
  // claim1
  // .setClaimNumber( new Long( oracleSequenceDAO.getOracleSequenceNextValue( "claim_nbr_sq" ) )
  // .toString() );
  // claimDao.saveClaim( claim1 );
  //
  // ProductClaim claim2 = ClaimDAOImplTest.buildStaticProductClaim( true );
  // claim2.setSubmitter( submitter );
  // claim2.setProxyUser( null );
  // claim2.setPromotion( promotion );
  // claim2.setNode( node );
  // claim2.setSubmissionDate( new Date() );
  // claim2
  // .setClaimNumber( new Long( oracleSequenceDAO.getOracleSequenceNextValue( "claim_nbr_sq" ) )
  // .toString() );
  // claimDao.saveClaim( claim2 );
  //
  // ApprovableType claimApprovableType = ApprovableType.lookup( ApprovableType.CLAIM );
  //
  // ClaimApproverSnapshot snapshotClaim1Approver1 = new ClaimApproverSnapshot( claim1.getId(),
  // approver1.getId(),
  // null,
  // claimApprovableType);
  // claimApproverSnapshotDAO.saveClaimApproverSnapshot( snapshotClaim1Approver1 );
  // ClaimApproverSnapshot snapshotClaim1Approver2 = new ClaimApproverSnapshot( claim1.getId(),
  // approver2.getId(),
  // null,
  // claimApprovableType );
  // claimApproverSnapshotDAO.saveClaimApproverSnapshot( snapshotClaim1Approver2 );
  // ClaimApproverSnapshot snapshotClaim2Approver2 = new ClaimApproverSnapshot( claim2.getId(),
  // approver2.getId(),
  // null,
  // claimApprovableType );
  // claimApproverSnapshotDAO.saveClaimApproverSnapshot( snapshotClaim2Approver2 );
  // ClaimApproverSnapshot snapshotClaim2Approver3 = new ClaimApproverSnapshot( claim2.getId(),
  // approver3.getId(),
  // null,
  // claimApprovableType );
  // claimApproverSnapshotDAO.saveClaimApproverSnapshot( snapshotClaim2Approver3 );
  //
  // flushAndClearSession();
  // assertTrue( claimApproverSnapshotDAO.isApprover( approver1.getId(),
  // claim1.getId(),
  // claimApprovableType ) );
  // assertFalse( claimApproverSnapshotDAO
  // .isApprover( approver3.getId(), claim1.getId(), ApprovableType
  // .lookup( ApprovableType.CLAIM ) ) );
  //
  // ClaimApproverSnapshotQueryConstraint claim1SnapshotQueryConstraint = new
  // ClaimApproverSnapshotQueryConstraint();
  // claim1SnapshotQueryConstraint.setApprovableId( claim1.getId() );
  // ApprovableType approvableType = claimApprovableType;
  // claim1SnapshotQueryConstraint.setApprovableType( approvableType );
  // List claim1Snapshots = claimApproverSnapshotDAO
  // .getClaimApproverSnapshotList( claim1SnapshotQueryConstraint );
  // assertEquals( 2, claim1Snapshots.size() );
  // assertTrue( claim1Snapshots.contains( snapshotClaim1Approver1 ) );
  // assertTrue( claim1Snapshots.contains( snapshotClaim1Approver2 ) );
  //
  // ClaimApproverSnapshotQueryConstraint approver2SnapshotQueryConstraint = new
  // ClaimApproverSnapshotQueryConstraint();
  // approver2SnapshotQueryConstraint.setApproverUserId( approver2.getId() );
  // List approver2Snapshots = claimApproverSnapshotDAO
  // .getClaimApproverSnapshotList( approver2SnapshotQueryConstraint );
  // assertEquals( 2, approver2Snapshots.size() );
  // assertTrue( approver2Snapshots.contains( snapshotClaim1Approver2 ) );
  // assertTrue( approver2Snapshots.contains( snapshotClaim2Approver2 ) );
  //
  // ApproverSeekingClaimQueryConstraint approver2ClaimQueryConstraint = new
  // ApproverSeekingClaimQueryConstraint();
  // approver2ClaimQueryConstraint.setOpen( Boolean.TRUE );
  // approver2ClaimQueryConstraint.setApprovableUserId( approver2.getId() );
  // approver2ClaimQueryConstraint.setClaimPromotionType( PromotionType
  // .lookup( PromotionType.PRODUCT_CLAIM ) );
  // List approver2Claims = claimDao.getClaimList( approver2ClaimQueryConstraint );
  // assertEquals( 2, approver2Claims.size() );
  //
  // ClaimApproverSnapshotQueryConstraint bothSnapshotQueryConstraint = new
  // ClaimApproverSnapshotQueryConstraint();
  // bothSnapshotQueryConstraint.setApproverUserId( approver2.getId() );
  // bothSnapshotQueryConstraint.setApprovableId( claim2.getId() );
  // bothSnapshotQueryConstraint.setApprovableType( approvableType );
  // List bothSnapshots = claimApproverSnapshotDAO
  // .getClaimApproverSnapshotList( bothSnapshotQueryConstraint );
  // assertEquals( 1, bothSnapshots.size() );
  // assertTrue( bothSnapshots.contains( snapshotClaim2Approver2 ) );
  //
  // // test delete
  // flushAndClearSession();
  // ClaimApproverSnapshotQueryConstraint claim1Approver2SnapshotQueryConstraint = new
  // ClaimApproverSnapshotQueryConstraint();
  // claim1Approver2SnapshotQueryConstraint.setApproverUserId( approver2.getId() );
  // claim1Approver2SnapshotQueryConstraint.setApprovableId( claim1.getId() );
  // claim1Approver2SnapshotQueryConstraint.setApprovableType( approvableType );
  // List claim1Approver2Snapshots = claimApproverSnapshotDAO
  // .getClaimApproverSnapshotList( claim1Approver2SnapshotQueryConstraint );
  // assertEquals( 1, claim1Approver2Snapshots.size() );
  // ClaimApproverSnapshot claim1Approver2Snapshot = (ClaimApproverSnapshot)claim1Approver2Snapshots
  // .get( 0 );
  //
  // claimApproverSnapshotDAO.deleteClaimApproverSnapshot( claim1Approver2Snapshot );
  //
  // ClaimApproverSnapshotQueryConstraint postDeleteClaim1SnapshotQueryConstraint = new
  // ClaimApproverSnapshotQueryConstraint();
  // postDeleteClaim1SnapshotQueryConstraint.setApprovableId( claim1.getId() );
  // postDeleteClaim1SnapshotQueryConstraint.setApprovableType( approvableType );
  // List postDeleteClaim1Snapshots = claimApproverSnapshotDAO
  // .getClaimApproverSnapshotList( postDeleteClaim1SnapshotQueryConstraint );
  // assertEquals( 1, postDeleteClaim1Snapshots.size() );
  // assertTrue( postDeleteClaim1Snapshots.contains( snapshotClaim1Approver1 ) );
  // }

  // ---------------------------------------------------------------------------
  // Build Methods
  // ---------------------------------------------------------------------------

  // ---------------------------------------------------------------------------
  // DAO Getter Methods
  // ---------------------------------------------------------------------------

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

}
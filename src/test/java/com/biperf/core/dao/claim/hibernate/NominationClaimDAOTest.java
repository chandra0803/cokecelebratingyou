
package com.biperf.core.dao.claim.hibernate;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.junit.Test;

import com.biperf.core.dao.claim.ClaimDAO;
import com.biperf.core.dao.claim.NominationClaimDAO;
import com.biperf.core.dao.hibernate.BaseDAOTest;
import com.biperf.core.dao.oracle.OracleSequenceDAO;
import com.biperf.core.dao.participant.ParticipantDAO;
import com.biperf.core.dao.participant.hibernate.ParticipantDAOImplTest;
import com.biperf.core.dao.promotion.PromotionDAO;
import com.biperf.core.dao.promotion.hibernate.PromotionDAOImplTest;
import com.biperf.core.domain.claim.AbstractRecognitionClaim;
import com.biperf.core.domain.claim.ClaimRecipient;
import com.biperf.core.domain.claim.NominationClaim;
import com.biperf.core.domain.claim.RecognitionClaimSource;
import com.biperf.core.domain.enums.NominationClaimStatusType;
import com.biperf.core.domain.enums.PromotionStatusType;
import com.biperf.core.domain.hierarchy.Node;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.NominationPromotion;
import com.biperf.core.domain.promotion.NominationPromotionTimePeriod;
import com.biperf.core.domain.user.UserNode;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.claim.ClaimAssociationRequest;
import com.biperf.core.utils.ApplicationContextFactory;

public class NominationClaimDAOTest extends BaseDAOTest
{

  public static Long PAX_ID = 5583L;

  private NominationClaimDAO nominationClaimDAO = getNominationClaimDao();

  PromotionDAO promotionDao = getPromotionDao();
  ParticipantDAO participantDao = getParticipantDao();
  OracleSequenceDAO oracleSequenceDAO = getOracleSequenceDao();
  ClaimDAO claimDao = getClaimDao();

  private static NominationClaimDAO getNominationClaimDao()
  {
    return (NominationClaimDAO)ApplicationContextFactory.getApplicationContext().getBean( NominationClaimDAO.BEAN_NAME );
  }

  // need to work on this ..
  /*
   * public void preCondionRequiredToRunThis() { NominationsInProgressPaginationMeta meta = new
   * NominationsInProgressPaginationMeta(); meta.setRowNumStart( 0 ); meta.setRowNumEnd( 25 );
   * meta.setUserId( 5583l ); meta.setSortBy( SortByType.getByCodeIfNotExistReturnDefaultAsASC(
   * "asc" ) ); meta.setSortOn(
   * NominationClaimsInProgressSortColumn.getByIndexIfNotExistReturnDefaultAsDateStarted( 1
   * ).getColumnIndex() ); List<NominationsInProgressValueObject> claimList =
   * nominationClaimDAO.getNominationClaimsInProgress( meta ); assertTrue( claimList.size() > 0 ); }
   */
  @SuppressWarnings( { "unchecked", "rawtypes" } )
  public void testsubmitNominationClaimAndVerifyTimePeriodCount()
  {

    Date currentDateTrimmed = com.biperf.core.utils.DateUtils.getCurrentDateTrimmed();
    long currentTimeMillis = System.currentTimeMillis();
    int stepNumber = 1;

    NominationPromotionTimePeriod timePeriod = getNominationTimePeriod( currentDateTrimmed );
    NominationPromotion nomPromotion = getNominationPromotion( timePeriod );
    promotionDao.save( nomPromotion );

    Participant submitter = participantDao.getParticipantById( PAX_ID );

    NominationClaim expectedClaim = buildStaticNominationClaim( true );
    expectedClaim.setNominationStatusType( NominationClaimStatusType.lookup( NominationClaimStatusType.INCOMPLETE ) );
    expectedClaim.setTimPeriod( timePeriod );

    Node node = null;
    String uniqueString = String.valueOf( currentTimeMillis % 5503032 );

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
    expectedClaim.setStepNumber( stepNumber );

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
    assertTrue( nomClaim.getStepNumber() == stepNumber );

    assertTrue( nominationClaimDAO.getClaimsSubmittedCountByPeriod( timePeriod.getId(), submitter.getId() ) == 1L );

  }

  public void testGetNominationClaimsSubmittedCount()
  {
    getNominationClaimDao().getClaimsSubmittedCountByPeriodAndNominee( 250L, 5583L, 5647L );
  }

  @Test
  public void testGetNominationClaimsSubmittedCountByPeriod()
  {
    Date currentDateTrimmed = com.biperf.core.utils.DateUtils.getCurrentDateTrimmed();

    NominationPromotionTimePeriod timePeriod = getNominationTimePeriod( currentDateTrimmed );
    NominationPromotion nomPromotion = getNominationPromotion( timePeriod );
    promotionDao.save( nomPromotion );

    Participant submitter = participantDao.getParticipantById( PAX_ID );

    NominationClaim expectedClaim = buildStaticNominationClaim( true );
    expectedClaim.setNominationStatusType( NominationClaimStatusType.lookup( NominationClaimStatusType.COMPLETE ) );
    expectedClaim.setTimPeriod( timePeriod );
    expectedClaim.setClaimNumber( new Long( oracleSequenceDAO.getOracleSequenceNextValue( "claim_nbr_sq" ) ).toString() );
    expectedClaim.setSubmitter( submitter );
    expectedClaim.setPromotion( nomPromotion );
    expectedClaim.setSubmissionDate( new Date() );
    claimDao.saveClaim( expectedClaim );

    flushAndClearSession();

    assertTrue( "Wrong number of complete claims for time period", getNominationClaimDao().getClaimsSubmittedCountByPeriod( timePeriod.getId(), submitter.getId() ) == 1 );
  }

  private NominationPromotion getNominationPromotion( NominationPromotionTimePeriod timePeriod )
  {
    NominationPromotion nomPromotion = PromotionDAOImplTest.buildNominationPromotion( "suffix" );
    nomPromotion.setPromotionStatus( PromotionStatusType.lookup( PromotionStatusType.LIVE ) );
    nomPromotion.setNominationTimePeriods( new HashSet<NominationPromotionTimePeriod>( Arrays.asList( timePeriod ) ) );
    timePeriod.setNominationPromotion( nomPromotion );
    return nomPromotion;
  }

  private NominationPromotionTimePeriod getNominationTimePeriod( Date currentDateTrimmed )
  {
    NominationPromotionTimePeriod timePeriod = new NominationPromotionTimePeriod();
    timePeriod.setTimePeriodName( "monthly" );
    timePeriod.setTimePeriodStartDate( currentDateTrimmed );
    timePeriod.setTimePeriodEndDate( com.biperf.core.utils.DateUtils.getDateAfterNumberOfDays( currentDateTrimmed, 7 ) );
    return timePeriod;
  }

  private static PromotionDAO getPromotionDao()
  {
    return (PromotionDAO)ApplicationContextFactory.getApplicationContext().getBean( PromotionDAO.BEAN_NAME );
  }

  private static OracleSequenceDAO getOracleSequenceDao()
  {
    return (OracleSequenceDAO)ApplicationContextFactory.getApplicationContext().getBean( OracleSequenceDAO.BEAN_NAME );
  }

  private static ClaimDAO getClaimDao()
  {
    return (ClaimDAO)ApplicationContextFactory.getApplicationContext().getBean( ClaimDAO.BEAN_NAME );
  }

  private static ParticipantDAO getParticipantDao()
  {
    return (ParticipantDAO)ApplicationContextFactory.getApplicationContext().getBean( ParticipantDAO.BEAN_NAME );
  }

  public static NominationClaim buildStaticNominationClaim( boolean isOpen )
  {

    NominationClaim claim = new NominationClaim();
    claim.setOpen( isOpen );
    claim.setSource( RecognitionClaimSource.UNKNOWN );
    claim.setNominationStatusType( NominationClaimStatusType.lookup( NominationClaimStatusType.INCOMPLETE ) );

    return claim;
  }

}

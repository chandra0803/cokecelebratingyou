
package com.biperf.integration.service.claim.nom;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.biperf.core.dao.claim.hibernate.ClaimDAOImplTest;
import com.biperf.core.domain.claim.ClaimRecipient;
import com.biperf.core.domain.claim.NominationClaim;
import com.biperf.core.domain.claim.RecognitionClaimSource;
import com.biperf.core.domain.enums.ApprovalType;
import com.biperf.core.domain.enums.ClaimFormModuleType;
import com.biperf.core.domain.enums.ClaimFormStatusType;
import com.biperf.core.domain.enums.NominationClaimStatusType;
import com.biperf.core.domain.enums.PromotionStatusType;
import com.biperf.core.domain.hierarchy.Node;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.NominationPromotion;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.user.UserNode;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.claim.ClaimService;
import com.biperf.core.service.claim.NominationClaimService;
import com.biperf.core.service.claim.RecognitionClaimSubmission;
import com.biperf.core.service.claim.RecognitionClaimSubmissionResponse;
import com.biperf.core.service.oracle.OracleSequenceService;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.utils.DateUtils;
import com.biperf.integration.BaseIntegrationTest;

public class NominationClaimServiceIntegTest extends BaseIntegrationTest
{

  @Resource
  NominationClaimService nominationClaimService;
  @Resource
  ParticipantService participantService;
  @Resource
  PromotionService promotionService;
  @Resource
  ClaimService claimService;
  @Resource
  OracleSequenceService seqService;

  @Test
  public void submitSelfNominationClaim() throws Exception
  {
    Participant pax = participantService.getParticipantById( PAX_ID );
    addAuthenticatedUserToResourceManager( pax );
    Date submissionDate = new Date( System.currentTimeMillis() + 5000 );

    long promoSuffix = System.currentTimeMillis();
    ClaimFormModuleType claimFormModuleType = ClaimFormModuleType.lookup( ClaimFormModuleType.MODULE_NOMINATION );
    ClaimFormStatusType claimFormStatusType = ClaimFormStatusType.lookup( ClaimFormStatusType.ASSIGNED );
    PromotionStatusType promoStatus = PromotionStatusType.lookup( PromotionStatusType.LIVE );

    NominationPromotion promotion = buildNominationPromotion( promoSuffix + "", promoStatus, claimFormStatusType, claimFormModuleType );
    promotion.setSelfNomination( true );
    promotion.setAwardActive( false );
    promotion.setApprovalType( ApprovalType.lookup( ApprovalType.MANUAL ) );
    promotionService.savePromotion( promotion );

    submitNominationForNTimesThruClaimService( pax, submissionDate, promotion, 99 );

  }

  @SuppressWarnings( "unused" )
  private void submitNominationThruRecognitionSubmision( Participant pax, Date submissionDate, NominationPromotion promotion ) throws ServiceErrorException
  {
    long userNodeId = 0;
    Set<UserNode> userNodes = pax.getUserNodes();
    for ( UserNode userNode : userNodes )
    {
      userNodeId = userNode.getNode().getId();
      break;
    }
    RecognitionClaimSubmission submission = getSubmission( pax, submissionDate, promotion, userNodeId );
    for ( int i = 0; i < 75; i++ )
    {
      RecognitionClaimSubmissionResponse submitRecognition = claimService.submitRecognition( submission );
    }
  }

  private void submitNominationForNTimesThruClaimService( Participant pax, Date submissionDate, NominationPromotion promotion, int nTimes ) throws ServiceErrorException
  {
    for ( int i = 0; i < nTimes; i++ )
    {
      NominationClaim nomClaim = ClaimDAOImplTest.buildStaticNominationClaim( true );
      nomClaim.setOpen( true );
      nomClaim.setNominationStatusType( NominationClaimStatusType.lookup( NominationClaimStatusType.INCOMPLETE ) );
      nomClaim.setPromotion( promotion );
      nomClaim.setSubmitter( pax );
      nomClaim.setClaimRecipients( createClaimRecipient( Arrays.asList( pax ), 5L ) );
      nomClaim.setSubmissionDate( submissionDate );
      nomClaim.setClaimNumber( new Long( seqService.getOracleSequenceNextValue( "claim_nbr_sq" ) ).toString() );
      nomClaim.setSubmitterComments( "submitterComments----- " );
      claimService.saveClaim( nomClaim, null, null, true, false );
    }

  }

  public static RecognitionClaimSubmission getSubmission( Participant pax, Date submissionDate, Promotion promotion, long userNodeId )
  {
    RecognitionClaimSubmission submission = new RecognitionClaimSubmission( RecognitionClaimSource.UNKNOWN, pax.getId(), userNodeId, promotion.getId() );
    submission.setComments( "testComments" );
    submission.setCopyManager( false );
    submission.setCopySender( false );
    submission.setPromotionId( promotion.getId() );
    submission.setRecipientSendDate( DateUtils.toDisplayString( new Date( submissionDate.getTime() + 50000 ) ) );
    submission.setSubmitterId( PAX_ID );
    submission.addRecognitionClaimRecipient( PAX_ID, userNodeId, 1L, null, null );
    return submission;
  }

  @SuppressWarnings( "rawtypes" )
  private Set<ClaimRecipient> createClaimRecipient( List<Participant> recipient, Long awardQy )
  {
    Set<ClaimRecipient> set = new HashSet<ClaimRecipient>();
    for ( Participant pax : recipient )
    {
      ClaimRecipient claimRecipient1 = new ClaimRecipient();
      claimRecipient1.setRecipient( pax );
      Set userNodes1 = pax.getUserNodes();
      Node recipient1Node1 = null;
      Iterator iter1 = userNodes1.iterator();
      if ( iter1.hasNext() )
      {
        recipient1Node1 = ( (UserNode)iter1.next() ).getNode();
      }
      claimRecipient1.setNode( recipient1Node1 );
      claimRecipient1.setAwardQuantity( awardQy );
    }
    return set;
  }

  @Before
  public void beforeEachTest() throws Exception
  {
    super.beforeEachTest();
  }

  @After
  public void afterEachTest() throws Exception
  {
    super.afterEachTest();
  }

}

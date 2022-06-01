
package com.biperf.core.ui.claim;

import static org.junit.Assert.assertTrue;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.junit.Test;

import com.biperf.core.dao.activity.hibernate.ActivityDAOImplTest;
import com.biperf.core.dao.journal.hibernate.JournalDAOImplTest;
import com.biperf.core.dao.promotion.hibernate.PromotionDAOImplTest;
import com.biperf.core.domain.activity.Activity;
import com.biperf.core.domain.claim.Claim;
import com.biperf.core.domain.claim.ClaimRecipient;
import com.biperf.core.domain.claim.NominationClaim;
import com.biperf.core.domain.claim.RecognitionClaimSource;
import com.biperf.core.domain.enums.ApprovalStatusType;
import com.biperf.core.domain.enums.JournalStatusType;
import com.biperf.core.domain.enums.NominationAwardGroupSizeType;
import com.biperf.core.domain.enums.NominationAwardGroupType;
import com.biperf.core.domain.enums.NominationClaimStatusType;
import com.biperf.core.domain.enums.NominationEvaluationType;
import com.biperf.core.domain.hierarchy.Node;
import com.biperf.core.domain.journal.ActivityJournal;
import com.biperf.core.domain.journal.Journal;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.NominationPromotion;
import com.biperf.core.domain.user.UserNode;
import com.biperf.core.service.activity.ActivityService;
import com.biperf.core.service.claim.ClaimService;
import com.biperf.core.service.journal.JournalService;
import com.biperf.core.service.oracle.OracleSequenceService;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.ui.claim.RecognitionHistoryListController.NominationReceiveBuilder;
import com.biperf.core.utils.GuidUtils;
import com.biperf.core.utils.HibernateSessionManager;
import com.biperf.integration.BaseIntegrationTest;

public class RecognitionHistoryListControllerTest extends BaseIntegrationTest
{

  @Resource
  ClaimService claimService;

  @Resource
  OracleSequenceService seqService;

  @Resource
  ParticipantService participantService;

  @Resource
  PromotionService promotionService;

  @Resource
  JournalService journalService;

  @Resource
  ActivityService activityService;

  /**
   * Test buildValueObjects method of NominationReceiveBuilder inner class. 
   * Adds claims and then calls the build value objects method to make sure the expected claims are returned. 
   */
  @SuppressWarnings( "unchecked" )
  @Test
  public void testNomRecBuilder_buildValueObjects() throws Exception
  {
    assertTrue( true );

    final int NUM_CLAIMS = 10;
    final long SUBMITTER_ID = 5583L;
    final long RECIPIENT_ID = 60024L;

    Calendar calendar = Calendar.getInstance();

    // The list of claims is our key to validating the value objects we get back. Start building it.
    List<Claim> claimList = new ArrayList<>( NUM_CLAIMS );

    // Promotion for the claim.
    NominationPromotion promotion = PromotionDAOImplTest.buildNominationPromotion( "-postedClaimTest" );
    promotion.setAwardGroupSizeType( NominationAwardGroupSizeType.lookup( NominationAwardGroupSizeType.UNLIMITED ) );
    promotion.setAwardGroupType( NominationAwardGroupType.lookup( NominationAwardGroupType.INDIVIDUAL ) );
    promotion.setEvaluationType( NominationEvaluationType.lookup( NominationEvaluationType.INDEPENDENT ) );
    promotionService.savePromotion( promotion );
    HibernateSessionManager.getSession().flush();

    // Claim submitter.
    Participant submitter = participantService.getParticipantById( SUBMITTER_ID );

    // Claim recipient.
    Participant recipient = participantService.getParticipantById( RECIPIENT_ID );

    // Look to the Journal test to create a journal, then tweak it to our needs
    String uniqueString = String.valueOf( System.currentTimeMillis() % 5503032 );
    Journal journal = JournalDAOImplTest.buildAndSaveJournal( uniqueString, promotion, recipient );
    journal.setJournalStatusType( JournalStatusType.lookup( JournalStatusType.POST ) );
    journalService.saveJournalEntry( journal );
    HibernateSessionManager.getSession().flush();

    for ( int i = 0; i < NUM_CLAIMS; ++i )
    {
      Claim claim = buildPostedNominationClaim( promotion, submitter, recipient, new Date() );

      claimService.saveClaim( claim, null, null, true, false );
      HibernateSessionManager.getSession().flush();

      claimList.add( claim );

      // Also need an Activity and ActivityJournal to match... courtesy of ActivityDaoImplTest
      Activity activity = ActivityDAOImplTest.buildNominationActivity( uniqueString, true, submitter, promotion, claim );
      activityService.saveActivity( activity );
      HibernateSessionManager.getSession().flush();

      ActivityJournal activityJournal = new ActivityJournal( activity, journal );
      activity.addActivityJournal( activityJournal );
      activityService.saveActivity( activity );
      HibernateSessionManager.getSession().flush();
    }

    // Done making the claims... now try to get the value objects (Hint: commit the transaction here
    // to give some test data)

    calendar.set( 2015, 0, 1 );
    Date startDate = calendar.getTime();
    calendar.set( 2099, 11, 25 );
    Date endDate = calendar.getTime();

    NominationReceiveBuilder builder = new NominationReceiveBuilder( RECIPIENT_ID, promotion.getId(), startDate, endDate );
    List<RecognitionHistoryValueObject> builtObjects = builder.buildValueObjects();

    // It's not impossible that some other claims matched the query...
    // But if we look in the list, we'll recognize the promotion ID is unique to this test
    // So the count of matching promotions *will* tell us what we want
    int numMatchingPromotions = 0;
    for ( RecognitionHistoryValueObject valueObject : builtObjects )
    {
      if ( valueObject.getPromotion().getId().equals( promotion.getId() ) )
      {
        numMatchingPromotions++;
      }
    }
    assertTrue( numMatchingPromotions == NUM_CLAIMS );
  }

  /**
   * Just creates a NominationClaim for buildValueObjects test. Doesn't save.
   */
  private NominationClaim buildPostedNominationClaim( NominationPromotion promotion, Participant submitter, Participant recipient, Date submitDate )
  {
    NominationClaim claim = new NominationClaim();

    claim.setOpen( true );
    claim.setSource( RecognitionClaimSource.UNKNOWN );
    claim.setNominationStatusType( NominationClaimStatusType.lookup( NominationClaimStatusType.COMPLETE ) );
    claim.setPromotion( promotion );
    claim.setSubmitter( submitter );
    claim.setClaimRecipients( createClaimRecipient( claim, Arrays.asList( recipient ), 5L, submitDate ) );
    claim.setSubmissionDate( new Date() );
    claim.setClaimNumber( new Long( seqService.getOracleSequenceNextValue( "claim_nbr_sq" ) ).toString() );
    claim.setSubmitterComments( "test @ " + new Date().toString() );

    return claim;
  }

  private Set<ClaimRecipient> createClaimRecipient( Claim claim, List<Participant> recipient, Long awardQy, Date submitDate )
  {
    Set<ClaimRecipient> set = new HashSet<ClaimRecipient>();
    for ( Participant pax : recipient )
    {
      ClaimRecipient claimRecipient = new ClaimRecipient();
      claimRecipient.setClaim( claim );
      claimRecipient.setRecipient( pax );
      Set<UserNode> userNodes1 = pax.getUserNodes();
      Node recipient1Node1 = null;
      Iterator<UserNode> iter1 = userNodes1.iterator();
      if ( iter1.hasNext() )
      {
        recipient1Node1 = ( iter1.next() ).getNode();
      }
      claimRecipient.setNode( recipient1Node1 );
      claimRecipient.setAwardQuantity( awardQy );
      claimRecipient.setDateApproved( new Timestamp( submitDate.getTime() ) );
      claimRecipient.setApprovalStatusType( ApprovalStatusType.lookup( ApprovalStatusType.WINNER ) );
      claimRecipient.setSerialId( GuidUtils.generateGuid() );

      set.add( claimRecipient );
    }
    return set;
  }

}

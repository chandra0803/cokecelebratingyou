/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javatest/com/biperf/core/dao/journal/hibernate/JournalDAOImplTest.java,v $
 */

package com.biperf.core.dao.journal.hibernate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import com.biperf.core.dao.activity.ActivityDAO;
import com.biperf.core.dao.activity.hibernate.ActivityDAOImplTest;
import com.biperf.core.dao.claim.ClaimDAO;
import com.biperf.core.dao.claim.hibernate.ClaimDAOImplTest;
import com.biperf.core.dao.hibernate.BaseDAOTest;
import com.biperf.core.dao.journal.JournalDAO;
import com.biperf.core.dao.oracle.OracleSequenceDAO;
import com.biperf.core.dao.participant.ParticipantDAO;
import com.biperf.core.dao.participant.hibernate.ParticipantDAOImplTest;
import com.biperf.core.dao.promotion.PromotionDAO;
import com.biperf.core.dao.promotion.hibernate.PromotionDAOImplTest;
import com.biperf.core.domain.activity.Activity;
import com.biperf.core.domain.activity.ManagerOverrideActivity;
import com.biperf.core.domain.activity.SalesActivity;
import com.biperf.core.domain.claim.Claim;
import com.biperf.core.domain.enums.JournalStatusType;
import com.biperf.core.domain.enums.JournalTransactionType;
import com.biperf.core.domain.enums.PromotionApprovalOptionReasonType;
import com.biperf.core.domain.enums.PromotionAwardsType;
import com.biperf.core.domain.journal.ActivityJournal;
import com.biperf.core.domain.journal.Journal;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.NominationPromotion;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.utils.ApplicationContextFactory;
import com.biperf.core.utils.GuidUtils;
import com.biperf.core.utils.HibernateSessionManager;

/*
 * JournalDAOImplTest <p> <b>Change History:</b><br> <table border="1"> <tr> <th>Author</th>
 * <th>Date</th> <th>Version</th> <th>Comments</th> </tr> <tr> <td>OPI Admin</td> <td>Jul
 * 14, 2005</td> <td>1.0</td> <td>created</td> </tr> </table> </p>
 * 
 *
 */

public class JournalDAOImplTest extends BaseDAOTest
{

  // ---------------------------------------------------------------------------
  // Test Methods
  // ---------------------------------------------------------------------------

  public void testDeleteJournal()
  {
    JournalDAO journalDAO = getJournalDAO();

    Journal journal = buildAndSaveJournal( getUniqueString() );
    journalDAO.saveJournalEntry( journal );
    flushAndClearSession();

    Journal retrievedJournal = journalDAO.getJournalById( journal.getId() );
    assertNotNull( retrievedJournal );

    journalDAO.deleteJournal( retrievedJournal );

    journal = journalDAO.getJournalById( retrievedJournal.getId() );
    if ( journal != null )
    {
      fail( "Expected method JournalDAO.getJournalById to return null." );
    }
  }

  public void testSaveGetJournalById()
  {
    String uniqueString = String.valueOf( System.currentTimeMillis() % 5503032 );

    JournalDAO journalDAO = getJournalDAO();

    Journal expectedJournal = buildAndSaveJournal( uniqueString );
    journalDAO.saveJournalEntry( expectedJournal );
    flushAndClearSession();

    Journal actualJournal = journalDAO.getJournalById( expectedJournal.getId() );
    assertEquals( "Journals are not equal", expectedJournal, actualJournal );
  }

  public void ignoretestGetJournalList()
  {
    JournalQueryConstraint queryConstraint = new JournalQueryConstraint();

    List journals = getJournalDAO().getJournalList( queryConstraint );

    assertFalse( journals.isEmpty() );
  }

  public void testGetJournalListByPromotion()
  {
    // Build and save journals
    Promotion expectedPromotion = PromotionDAOImplTest.buildProductClaimPromotion( "promo-" + buildUniqueString() );
    getPromotionDAO().save( expectedPromotion );

    Journal expectedJournal1 = buildAndSaveJournal( buildUniqueString(), expectedPromotion );
    Journal expectedJournal2 = buildAndSaveJournal( buildUniqueString(), expectedPromotion );
    getJournalDAO().saveJournalEntry( expectedJournal1 );
    getJournalDAO().saveJournalEntry( expectedJournal2 );
    List expectedJournals = new ArrayList();
    expectedJournals.add( expectedJournal1 );
    expectedJournals.add( expectedJournal2 );

    // Build third with different promo to insure lisdt doesn't contain more than expected.
    Journal journalWithOtherPromo = buildAndSaveJournal( buildUniqueString() );
    getJournalDAO().saveJournalEntry( journalWithOtherPromo );

    flushAndClearSession();

    JournalQueryConstraint queryConstraint = new JournalQueryConstraint();
    queryConstraint.setPromotionId( expectedPromotion.getId() );

    List actualJournals = getJournalDAO().getJournalList( queryConstraint );
    assertEquals( expectedJournals, actualJournals );

  }

  public void testGetJournalListByJournalStatusType()
  {
    // Build and save journals
    Promotion expectedPromotion = PromotionDAOImplTest.buildProductClaimPromotion( "promo-" + buildUniqueString() );
    getPromotionDAO().save( expectedPromotion );

    Journal expectedPendingJournal1 = buildAndSaveJournal( buildUniqueString(), expectedPromotion );
    expectedPendingJournal1.setJournalStatusType( JournalStatusType.lookup( JournalStatusType.PENDING_MINIMUM_QUALIFIER ) );
    Journal expectedPendingJournal2 = buildAndSaveJournal( buildUniqueString(), expectedPromotion );
    expectedPendingJournal2.setJournalStatusType( JournalStatusType.lookup( JournalStatusType.PENDING_MINIMUM_QUALIFIER ) );
    getJournalDAO().saveJournalEntry( expectedPendingJournal1 );
    getJournalDAO().saveJournalEntry( expectedPendingJournal2 );
    List expectedPendingJournals = new ArrayList();
    expectedPendingJournals.add( expectedPendingJournal1 );
    expectedPendingJournals.add( expectedPendingJournal2 );

    // Build third with different promo to insure lisdt doesn't contain more than expected.
    List expectedReverseJournals = new ArrayList();
    Journal expectedReverseJournal = buildAndSaveJournal( buildUniqueString(), expectedPromotion );
    expectedReverseJournal.setJournalStatusType( JournalStatusType.lookup( JournalStatusType.REVERSE ) );
    getJournalDAO().saveJournalEntry( expectedReverseJournal );
    expectedReverseJournals.add( expectedReverseJournal );

    flushAndClearSession();

    JournalQueryConstraint queryConstraint1 = new JournalQueryConstraint();
    queryConstraint1.setPromotionId( expectedPromotion.getId() );
    queryConstraint1.setJournalStatusTypesIncluded( new JournalStatusType[] { JournalStatusType.lookup( JournalStatusType.PENDING_MINIMUM_QUALIFIER ) } );

    List actualPendingJournals = getJournalDAO().getJournalList( queryConstraint1 );
    assertEquals( expectedPendingJournals, actualPendingJournals );

    JournalQueryConstraint queryConstraint2 = new JournalQueryConstraint();
    queryConstraint2.setPromotionId( expectedPromotion.getId() );
    queryConstraint2.setJournalStatusTypesIncluded( new JournalStatusType[] { JournalStatusType.lookup( JournalStatusType.REVERSE ) } );

    List actualReverseJournals = getJournalDAO().getJournalList( queryConstraint2 );
    assertEquals( expectedReverseJournals, actualReverseJournals );
  }

  /**
   * Tests adding an activity to a journal transaction.
   */
  public void testActivityJournal()
  {
    String uniqueString = String.valueOf( System.currentTimeMillis() % 5503032 );

    // Build and save an activity.
    SalesActivity activity = ActivityDAOImplTest.buildSalesActivity( uniqueString, false );
    getActivityDAO().saveActivity( activity );
    HibernateSessionManager.getSession().flush();

    // Build and save a journal transaction.
    Journal journal = buildAndSaveJournal( uniqueString );
    getJournalDAO().saveJournalEntry( journal );
    HibernateSessionManager.getSession().flush();

    // Associate the activity and the journal transaction.
    ActivityJournal activityJournal = new ActivityJournal();
    activityJournal.setActivity( activity );
    journal.addActivityJournal( activityJournal );
    getJournalDAO().saveJournalEntry( journal );
    flushAndClearSession();

    // Did the journal DAO save the activity/journal too?
    Journal actualJournal = getJournalDAO().getJournalById( journal.getId() );
    assertEquals( 1, actualJournal.getActivityJournals().size() );
  }

  /**
   * Tests finding a journal by claim.
   */
  public void testGetJournalByClaimIdAndUserId()
  {
    String uniqueString = String.valueOf( System.currentTimeMillis() % 5503032 );

    // Build and save an activity.
    SalesActivity activity = ActivityDAOImplTest.buildSalesActivity( uniqueString, false );
    getActivityDAO().saveActivity( activity );
    HibernateSessionManager.getSession().flush();

    // Build and save a journal transaction.
    Journal journal = buildAndSaveJournal( uniqueString );
    getJournalDAO().saveJournalEntry( journal );
    HibernateSessionManager.getSession().flush();

    // Associate the activity and the journal transaction.
    ActivityJournal activityJournal = new ActivityJournal();
    activityJournal.setActivity( activity );
    journal.addActivityJournal( activityJournal );
    getJournalDAO().saveJournalEntry( journal );
    flushAndClearSession();

    Long claimId = activity.getClaim().getId();
    Long userId = activity.getParticipant().getId();
    List journals = getJournalDAO().getJournalsByClaimIdAndUserId( claimId, userId );
    assertEquals( 1, journals.size() );
  }

  /**
   * Tests finding a journal by claim.
   */
  public void testGetJournalByClaimOfAManagerOverrideActivity()
  {
    String uniqueString = String.valueOf( System.currentTimeMillis() % 5503032 );

    // Build and save an activity.
    ManagerOverrideActivity managerOverrideActivity = ActivityDAOImplTest.buildManagerOverrideActivity( uniqueString, false );
    getActivityDAO().saveActivity( managerOverrideActivity );
    HibernateSessionManager.getSession().flush();

    // Build and save a journal transaction.
    Journal journal = buildAndSaveJournal( uniqueString );
    getJournalDAO().saveJournalEntry( journal );
    HibernateSessionManager.getSession().flush();

    // Associate the activity and the journal transaction.
    ActivityJournal activityJournal = new ActivityJournal();
    activityJournal.setActivity( managerOverrideActivity );
    journal.addActivityJournal( activityJournal );
    getJournalDAO().saveJournalEntry( journal );
    flushAndClearSession();

    Long claimId = managerOverrideActivity.getClaim().getId();
    Long userId = managerOverrideActivity.getParticipant().getId();
    List journals = getJournalDAO().getJournalsByClaimIdAndUserId( claimId, userId );
    assertEquals( 1, journals.size() );
  }

  public void testGetTotalEarningsByMediaTypeAndUserId()
  {
    String uniqueString = String.valueOf( System.currentTimeMillis() % 5503032 );

    // Build and save a journal transaction.
    Journal journal = buildAndSaveJournal( uniqueString );
    journal.setJournalStatusType( JournalStatusType.lookup( JournalStatusType.POST ) );
    getJournalDAO().saveJournalEntry( journal );
    HibernateSessionManager.getSession().flush();

    Long totalEarnings = getJournalDAO().getTotalEarningsByMediaTypeAndUserId( journal.getParticipant().getId(), journal.getPromotion().getAwardType().getCode() );

    assertTrue( "Total Earnings was less than it should have been.", totalEarnings.longValue() > 0 );
  }

  @Test
  public void testGetJournalIdForReversedClaim()
  {
    String uniqueString = String.valueOf( System.currentTimeMillis() % 5503786 );
    final long SUBMITTER_ID = 5583L;

    // All of the setup work to make a coherent database entry
    NominationPromotion promotion = PromotionDAOImplTest.buildNominationPromotion( "-postedClaimTest" );
    getPromotionDAO().save( promotion );
    HibernateSessionManager.getSession().flush();

    Participant submitter = getParticipantDAO().getParticipantById( SUBMITTER_ID );

    Claim claim = ClaimDAOImplTest.buildStaticNominationClaim( true );
    claim.setPromotion( promotion );
    claim.setSubmitter( submitter );
    claim.setSubmissionDate( new Date() );
    claim.setClaimNumber( new Long( getSeqDAO().getOracleSequenceNextValue( "claim_nbr_sq" ) ).toString() );
    claim.setAdminComments( "test for get reverse journal id" );

    getClaimDAO().saveClaim( claim );
    HibernateSessionManager.getSession().flush();

    Journal journal = buildAndSaveJournal( uniqueString );
    journal.setJournalStatusType( JournalStatusType.lookup( JournalStatusType.POST ) );
    journal.setTransactionType( JournalTransactionType.lookup( JournalTransactionType.DEPOSIT ) );
    journal.setTransactionDescription( "deposit" );
    journal.setParticipant( submitter );
    getJournalDAO().saveJournalEntry( journal );
    HibernateSessionManager.getSession().flush();

    Journal reverseJournal = buildAndSaveJournal( uniqueString + "2" );
    reverseJournal.setJournalStatusType( JournalStatusType.lookup( JournalStatusType.POST ) );
    reverseJournal.setTransactionType( JournalTransactionType.lookup( JournalTransactionType.REVERSE ) );
    reverseJournal.setTransactionDescription( "reverse of " + String.valueOf( journal.getId().longValue() ) );
    reverseJournal.setParticipant( submitter );
    getJournalDAO().saveJournalEntry( reverseJournal );
    HibernateSessionManager.getSession().flush();

    Activity activity = ActivityDAOImplTest.buildNominationActivity( uniqueString, true, submitter, promotion, claim );
    activity.setApprovalRound( new Long( 1 ) );
    getActivityDAO().saveActivity( activity );
    HibernateSessionManager.getSession().flush();

    ActivityJournal activityJournal = new ActivityJournal( activity, journal );
    activity.addActivityJournal( activityJournal );
    getActivityDAO().saveActivity( activity );
    HibernateSessionManager.getSession().flush();
    Long resultJournalId = getJournalDAO().getJournalIdForReversedClaim( claim.getId(), submitter.getId(), activity.getApprovalRound() );

    assertTrue( "Did not find a matching journal ID.", resultJournalId.equals( reverseJournal.getId() ) );
  }

  // ---------------------------------------------------------------------------
  // Private Methods
  // ---------------------------------------------------------------------------

  /**
   * Returns an {@link ActivityDAO} object.
   * 
   * @return an {@link ActivityDAO} object.
   */
  private ActivityDAO getActivityDAO()
  {
    return (ActivityDAO)ApplicationContextFactory.getApplicationContext().getBean( ActivityDAO.BEAN_NAME );
  }

  /**
   * Returns a {@link JournalDAO} object.
   * 
   * @return a {@link JournalDAO} object.
   */
  private JournalDAO getJournalDAO()
  {
    return (JournalDAO)ApplicationContextFactory.getApplicationContext().getBean( JournalDAO.BEAN_NAME );
  }

  /**
   * Returns a ParticipantDAO object.
   * 
   * @return a {@link ParticipantDAO} object.
   */
  private static ParticipantDAO getParticipantDAO()
  {
    return (ParticipantDAO)ApplicationContextFactory.getApplicationContext().getBean( ParticipantDAO.BEAN_NAME );
  }

  private static PromotionDAO getPromotionDAO()
  {
    return (PromotionDAO)ApplicationContextFactory.getApplicationContext().getBean( PromotionDAO.BEAN_NAME );
  }

  private static ClaimDAO getClaimDAO()
  {
    return (ClaimDAO)ApplicationContextFactory.getApplicationContext().getBean( ClaimDAO.BEAN_NAME );
  }

  private static OracleSequenceDAO getSeqDAO()
  {
    return (OracleSequenceDAO)ApplicationContextFactory.getApplicationContext().getBean( OracleSequenceDAO.BEAN_NAME );
  }

  // ---------------------------------------------------------------------------
  // Build Methods
  // ---------------------------------------------------------------------------

  /**
   * Builds a {@link Journal} object given a unique string.
   * 
   * @param uniqueString a unique string.
   * @return a new {@link Journal} object.
   */
  public static Journal buildAndSaveJournal( String uniqueString )
  {
    Promotion promotion = PromotionDAOImplTest.buildProductClaimPromotion( "promo-" + uniqueString );
    getPromotionDAO().save( promotion );
    return buildAndSaveJournal( uniqueString, promotion );
  }

  /**
   * Builds a {@link Journal} object given a unique string.
   * 
   * @param uniqueString a unique string.
   * @param promotion
   * @return a new {@link Journal} object.
   */
  public static Journal buildAndSaveJournal( String uniqueString, Promotion promotion )
  {
    Participant newParticipant = ParticipantDAOImplTest.buildUniqueParticipant( "Pax" + uniqueString );

    return buildAndSaveJournal( uniqueString, promotion, newParticipant );
  }

  /**
   * Builds a {@link Journal} object given a unique string, a promotion, and a participant
   */
  public static Journal buildAndSaveJournal( String uniqueString, Promotion promotion, Participant participant )
  {
    Journal journal = new Journal();

    journal.setGuid( GuidUtils.generateGuid() );

    participant = getParticipantDAO().saveParticipant( participant );
    HibernateSessionManager.getSession().flush();
    // flushAndClearSession();

    journal.setParticipant( participant );
    journal.setAccountNumber( buildAccountNumber( uniqueString ) );
    journal.setTransactionAmount( new Long( "10" ) );
    journal.setTransactionDate( new Date() );
    journal.setTransactionDescription( "deposit" );
    journal.setTransactionType( JournalTransactionType.lookup( JournalTransactionType.DEPOSIT ) );
    journal.setReasonType( PromotionApprovalOptionReasonType.lookup( "option" ) );
    journal.setComments( "award from JUnit test" );
    journal.setJournalType( Journal.AWARD );
    journal.setPromotion( promotion );
    journal.setAwardPayoutType( (PromotionAwardsType)PromotionAwardsType.getRecognitionList().get( 0 ) );
    return journal;
  }

  /**
   * Returns an account number.
   * 
   * @param uniqueString used to generate the account number.
   * @return an account number.
   */
  private static String buildAccountNumber( String uniqueString )
  {
    return ( uniqueString.length() > 30 ) ? uniqueString.substring( 0, 29 ) : uniqueString;
  }

}
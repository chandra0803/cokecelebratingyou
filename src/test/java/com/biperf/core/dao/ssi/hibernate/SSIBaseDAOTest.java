
package com.biperf.core.dao.ssi.hibernate;

import org.junit.Test;

import com.biperf.core.dao.hibernate.BaseDAOTest;
import com.biperf.core.dao.participant.ParticipantDAO;
import com.biperf.core.dao.participant.hibernate.ParticipantDAOImplTest;
import com.biperf.core.dao.promotion.PromotionDAO;
import com.biperf.core.domain.AuditCreateInfo;
import com.biperf.core.domain.enums.PromotionAwardsType;
import com.biperf.core.domain.enums.PromotionStatusType;
import com.biperf.core.domain.enums.PromotionType;
import com.biperf.core.domain.enums.SSIApproverType;
import com.biperf.core.domain.enums.SSIContestStatus;
import com.biperf.core.domain.enums.SSIContestType;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.ssi.SSIContest;
import com.biperf.core.domain.ssi.SSIContestActivity;
import com.biperf.core.domain.ssi.SSIContestApprover;
import com.biperf.core.domain.ssi.SSIContestLevel;
import com.biperf.core.domain.ssi.SSIContestManager;
import com.biperf.core.domain.ssi.SSIContestParticipant;
import com.biperf.core.domain.ssi.SSIPromotion;
import com.biperf.core.utils.DateUtils;

/**
 * Base DAO for SSI module
 * SSIBaseDAOTest.
 * 
 * @author kandhi
 * @since Nov 5, 2014
 * @version 1.0
 */
public class SSIBaseDAOTest extends BaseDAOTest
{

  public static final Long CONTEST_CREATOR_ID = 5582L;

  @Test
  public void testFoo()
  {
    // Avoiding warning when there are no tests in a class
    assertTrue( true );
  }
  
  protected static PromotionDAO getPromotionDAO()
  {
    return (PromotionDAO)getDAO( PromotionDAO.BEAN_NAME );
  }

  protected static ParticipantDAO getParticipantDAO()
  {
    return (ParticipantDAO)getDAO( ParticipantDAO.BEAN_NAME );
  }

  /**
   * Creates a recognition promotion domain object
   * 
   * @param suffix
   * @return Promotion
   */
  protected static SSIPromotion buildSSIPromotion()
  {
    String uniqueString = buildUniqueString();
    SSIPromotion promotion = new SSIPromotion();
    promotion.setName( "testPromotion" + uniqueString );
    promotion.setPromotionType( PromotionType.lookup( PromotionType.SELF_SERV_INCENTIVES ) );
    promotion.setPromotionStatus( PromotionStatusType.lookup( PromotionStatusType.LIVE ) );
    promotion.setPromoNameAssetCode( "testAssetCode" );
    promotion.setSubmissionStartDate( DateUtils.getLastDayOfPreviousMonth() );
    promotion.setSubmissionEndDate( DateUtils.getLastDayOfCurrentMonth() );
    promotion.setAwardType( PromotionAwardsType.lookup( PromotionAwardsType.POINTS ) );
    promotion.setUpperCaseName( promotion.getPromotionName().toUpperCase() );
    promotion.setContestGuideUrl( "http://" );

    return promotion;
  }

  protected SSIContest createContest( SSIPromotion promotion, String uniqueString )
  {
    SSIContest contest = new SSIContest();
    contest.setPromotion( promotion );
    contest.setContestOwnerId( CONTEST_CREATOR_ID );
    contest.setStartDate( DateUtils.getLastDayOfPreviousMonth() );
    contest.setEndDate( DateUtils.getLastDayOfCurrentMonth() );
    contest.setDisplayStartDate( DateUtils.getLastDayOfPreviousMonth() );
    contest.setCmAssetCode( uniqueString );
    contest.setStatus( SSIContestStatus.lookup( SSIContestStatus.LIVE ) );
    contest.setContestType( SSIContestType.lookup( SSIContestType.OBJECTIVES ) );
    AuditCreateInfo createInfo = new AuditCreateInfo();
    createInfo.setCreatedBy( CONTEST_CREATOR_ID );
    contest.setAuditCreateInfo( createInfo );

    // Approvers
    createLevel1Approver( contest );
    createLevel2Approver( contest );
    createClaimApprover( contest );

    // Participants
    createContestParticipant( contest );
    createContestParticipant( contest );

    // Managers
    createContestManager( contest );

    // Activities
    createContestActivity( contest, new Integer( 1 ) );

    // Levels
    createContestLevel( contest );

    return contest;
  }

  protected SSIContest createAwardThemNowContest( SSIPromotion promotion, String uniqueString )
  {
    SSIContest contest = new SSIContest();
    contest.setPromotion( promotion );
    contest.setContestOwnerId( CONTEST_CREATOR_ID );
    contest.setStartDate( DateUtils.getLastDayOfPreviousMonth() );
    contest.setEndDate( DateUtils.getLastDayOfCurrentMonth() );
    contest.setDisplayStartDate( DateUtils.getLastDayOfPreviousMonth() );
    contest.setCmAssetCode( uniqueString );
    contest.setStatus( SSIContestStatus.lookup( SSIContestStatus.LIVE ) );
    contest.setContestType( SSIContestType.lookup( SSIContestType.AWARD_THEM_NOW ) );
    AuditCreateInfo createInfo = new AuditCreateInfo();
    createInfo.setCreatedBy( CONTEST_CREATOR_ID );
    contest.setAuditCreateInfo( createInfo );

    // Approvers
    createLevel1Approver( contest );
    createLevel2Approver( contest );
    createClaimApprover( contest );

    // Participants
    createContestParticipant( contest );
    createContestParticipant( contest );

    // Managers
    createContestManager( contest );

    // Activities
    createContestActivity( contest, new Integer( 1 ) );

    // Levels
    createContestLevel( contest );

    return contest;
  }

  protected void createContestManager( SSIContest contest )
  {
    SSIContestManager ssiContestManager = new SSIContestManager();
    String uniqueString = buildUniqueString();
    Participant participant = createParticipant( uniqueString );
    ssiContestManager.setManager( participant );
    contest.addContestManager( ssiContestManager );
  }

  protected void createContestActivity( SSIContest contest, Integer sequenceNumber )
  {
    SSIContestActivity contestActivity = new SSIContestActivity();
    contestActivity.setDescription( "activity 1 desc" );
    contestActivity.setIncrementAmount( 50D );
    contestActivity.setPayoutAmount( 5L );
    contestActivity.setMinQualifier( 10D );
    contestActivity.setPayoutCapAmount( 100L );
    contestActivity.setGoalAmount( 100D );
    contestActivity.setSequenceNumber( sequenceNumber );

    contestActivity.setAuditCreateInfo( contest.getAuditCreateInfo() );
    contestActivity.setAuditUpdateInfo( contest.getAuditUpdateInfo() );

    contest.addContestActivity( contestActivity );
  }

  protected void createContestLevel( SSIContest contest )
  {
    SSIContestLevel contestLevel = new SSIContestLevel();
    contestLevel.setGoalAmount( 100D );
    contestLevel.setPayoutAmount( 5L );
    contestLevel.setPayoutDesc( "payout desc" );
    contestLevel.setBadgeRule( null );
    contestLevel.setSequenceNumber( 123 );

    contestLevel.setAuditCreateInfo( contest.getAuditCreateInfo() );
    contestLevel.setAuditUpdateInfo( contest.getAuditUpdateInfo() );

    contest.addContestLevel( contestLevel );
  }

  protected SSIContestParticipant createContestParticipant( SSIContest contest )
  {
    SSIContestParticipant contestParticipant = new SSIContestParticipant();
    String uniqueString = buildUniqueString();
    Participant participant = createParticipant( uniqueString );
    contestParticipant.setParticipant( participant );
    contestParticipant.setActivityDescription( uniqueString + " description" );
    contestParticipant.setObjectiveAmount( 100.0000 );
    contestParticipant.setObjectivePayout( 100L );
    contestParticipant.setObjectivePayoutDescription( uniqueString + "payout description" );
    contestParticipant.setObjectiveBonusIncrement( 50d );
    contestParticipant.setObjectiveBonusPayout( 50L );
    contestParticipant.setObjectiveBonusCap( 100L );
    contest.addContestParticipant( contestParticipant );
    return contestParticipant;
  }

  protected SSIContestParticipant addContestParticipant( SSIContest contest, Participant participant )
  {
    SSIContestParticipant contestParticipant = new SSIContestParticipant();
    String uniqueString = buildUniqueString();
    contestParticipant.setParticipant( participant );
    contestParticipant.setActivityDescription( uniqueString + " description" );
    contestParticipant.setObjectiveAmount( 100.0000 );
    contestParticipant.setObjectivePayout( 100L );
    contestParticipant.setObjectivePayoutDescription( uniqueString + "payout description" );
    contestParticipant.setObjectiveBonusIncrement( 50d );
    contestParticipant.setObjectiveBonusPayout( 50L );
    contestParticipant.setObjectiveBonusCap( 100L );
    contest.addContestParticipant( contestParticipant );
    return contestParticipant;
  }

  protected SSIContestManager addContestManager( SSIContest contest, Participant manager )
  {
    SSIContestManager contestManager = new SSIContestManager();
    contestManager.setManager( manager );
    contest.addContestManager( contestManager );
    return contestManager;
  }

  protected void createClaimApprover( SSIContest contest )
  {
    String uniqueString;
    SSIContestApprover contestClaimApprover = new SSIContestApprover();
    uniqueString = buildUniqueString();
    Participant claimApprover = createParticipant( uniqueString );
    contestClaimApprover.setApprover( claimApprover );
    contestClaimApprover.setApproverType( SSIApproverType.lookup( SSIApproverType.CLAIM_APPROVER ) );
    contest.addClaimApprover( contestClaimApprover );
  }

  protected void createLevel2Approver( SSIContest contest )
  {
    String uniqueString;
    SSIContestApprover contestLevel2Approver = new SSIContestApprover();
    uniqueString = buildUniqueString();
    Participant level2Approver = createParticipant( uniqueString );
    contestLevel2Approver.setApprover( level2Approver );
    contestLevel2Approver.setApproverType( SSIApproverType.lookup( SSIApproverType.CONTEST_LEVEL2_APPROVER ) );
    contest.addContestLevel2Approver( contestLevel2Approver );
  }

  protected void createLevel1Approver( SSIContest contest )
  {
    String uniqueString;
    SSIContestApprover contestLevel1Approver = new SSIContestApprover();
    uniqueString = buildUniqueString();
    Participant level1Approver = createParticipant( uniqueString );
    contestLevel1Approver.setApprover( level1Approver );
    contestLevel1Approver.setApproverType( SSIApproverType.lookup( SSIApproverType.CONTEST_LEVEL1_APPROVER ) );
    contest.addContestLevel1Approver( contestLevel1Approver );
  }

  protected Participant createParticipant( String uniqueString )
  {
    Participant pax = ParticipantDAOImplTest.buildUniqueParticipant( uniqueString );
    getParticipantDAO().saveParticipant( pax );
    return pax;
  }

  public static SSIContestActivity buildSSIContestActivityInstance( SSIContest contest, Integer sequenceNumber )
  {
    SSIContestActivity ssiContestActivity = new SSIContestActivity();
    ssiContestActivity.setDescription( "activity 1 desc" );
    ssiContestActivity.setIncrementAmount( 50D );
    ssiContestActivity.setPayoutAmount( 5L );
    ssiContestActivity.setMinQualifier( 10D );
    ssiContestActivity.setPayoutCapAmount( 100L );
    ssiContestActivity.setGoalAmount( 100D );
    ssiContestActivity.setSequenceNumber( sequenceNumber );
    ssiContestActivity.setContest( contest );

    ssiContestActivity.setAuditCreateInfo( contest.getAuditCreateInfo() );
    ssiContestActivity.setAuditUpdateInfo( contest.getAuditUpdateInfo() );

    return ssiContestActivity;
  }
}

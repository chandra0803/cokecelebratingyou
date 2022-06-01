
package com.biperf.core.dao.ssi.hibernate;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.biperf.core.dao.participant.hibernate.ParticipantDAOImplTest;
import com.biperf.core.dao.ssi.SSIContestDAO;
import com.biperf.core.dao.ssi.SSIContestPaxClaimDAO;
import com.biperf.core.domain.AuditCreateInfo;
import com.biperf.core.domain.AuditUpdateInfo;
import com.biperf.core.domain.enums.MockPickListFactory;
import com.biperf.core.domain.enums.PromotionAwardsType;
import com.biperf.core.domain.enums.PromotionStatusType;
import com.biperf.core.domain.enums.PromotionType;
import com.biperf.core.domain.enums.SSIApproverType;
import com.biperf.core.domain.enums.SSIClaimStatus;
import com.biperf.core.domain.enums.SSIContestStatus;
import com.biperf.core.domain.enums.SSIContestType;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.ssi.SSIContest;
import com.biperf.core.domain.ssi.SSIContestApprover;
import com.biperf.core.domain.ssi.SSIContestPaxClaim;
import com.biperf.core.domain.ssi.SSIPromotion;
import com.biperf.core.utils.DateUtils;

/**
 * 
 * SSIContestPaxClaimDAOTest.
 * 
 * @author Rameshj
 * @since Aug 23, 2017
 * @version 1.0
 */
public class SSIContestPaxClaimDAOTest extends SSIBaseDAOTest
{
  private static SSIContestPaxClaimDAO getSSIContestPaxClaimDAO()
  {
    return (SSIContestPaxClaimDAO)getDAO( SSIContestPaxClaimDAO.BEAN_NAME );
  }

  private static SSIContestDAO getSSIContestDAO()
  {
    return (SSIContestDAO)getDAO( SSIContestDAO.BEAN_NAME );
  }

  public void testgetPaxClaimsForApprovalByContestId() throws SQLException
  {

    SSIPromotion promotion = buildSSIPromotion();
    getPromotionDAO().save( promotion );
    flushAndClearSession();
    String uniqueString = buildUniqueString();
    SSIContest expectedContest = createContest( promotion, uniqueString );
    SSIContest contest = getSSIContestDAO().saveContest( expectedContest );
    flushAndClearSession();

    SSIContestPaxClaim ssiPaxClaim = getSSIContestPaxClaimDAO().savePaxClaim( buildSSIPaxClaim( contest.getId() ) );
    List<Long> contestId = new ArrayList<Long>();
    contestId.add( ssiPaxClaim.getContestId() );
    List<SSIContestPaxClaim> ssiContestPaxClaims = getSSIContestPaxClaimDAO().getPaxClaimsForApprovalByContestId( contestId );
    assertNotNull( ssiContestPaxClaims );
    assertEquals( ssiContestPaxClaims.size(), 1 );

  }

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

  protected static SSIContestPaxClaim buildSSIPaxClaim( Long contestId )
  {
    SSIContestPaxClaim paxClaim = new SSIContestPaxClaim();
    paxClaim.setSubmissionDate( new Date() );
    paxClaim.setApproveDenyDate( new Date() );
    paxClaim.setVersion( 1L );
    List<String> activities = new ArrayList<String>();
    activities.add( "add" );
    activities.add( "remove" );
    paxClaim.setActivities( activities );
    paxClaim.setContestId( contestId );
    paxClaim.setActivitiesAmountQuantity( "20" );
    paxClaim.setApprovedBy( "5634" );
    paxClaim.setApproveDenyDate( new Date() );
    paxClaim.setApproverId( CONTEST_CREATOR_ID );
    AuditCreateInfo createInfo = new AuditCreateInfo();
    createInfo.setCreatedBy( CONTEST_CREATOR_ID );
    paxClaim.setAuditCreateInfo( createInfo );
    AuditUpdateInfo updateInfo = new AuditUpdateInfo();
    updateInfo.setModifiedBy( CONTEST_CREATOR_ID );
    paxClaim.setAuditUpdateInfo( updateInfo );
    paxClaim.setSubmitterId( CONTEST_CREATOR_ID );
    paxClaim.setClaimAmountQuantity( 300D );
    paxClaim.setClaimNumber( "3282323936098" );
    paxClaim.setStatus( (SSIClaimStatus)MockPickListFactory.getMockPickListItem( SSIClaimStatus.class, SSIClaimStatus.WAITING_FOR_APPROVAL ) );
    return paxClaim;
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

}

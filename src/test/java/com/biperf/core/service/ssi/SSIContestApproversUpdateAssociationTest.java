
package com.biperf.core.service.ssi;

import com.biperf.core.dao.participant.hibernate.ParticipantDAOImplTest;
import com.biperf.core.domain.ssi.SSIContest;
import com.biperf.core.domain.ssi.SSIContestApprover;
import com.biperf.core.service.BaseServiceTest;

/**
 * 
 * @author dudam
 * @since Nov 6, 2014
 * @version 1.0
 */
public class SSIContestApproversUpdateAssociationTest extends BaseServiceTest
{

  public void testAddLevel1ApproverNoAttachedApprovers()
  {
    SSIContest attachedContest = buildSsiContest();
    SSIContest detachedContest = buildSsiContest();
    SSIContestApprover contestApprover = new SSIContestApprover();
    contestApprover.setApprover( ParticipantDAOImplTest.buildStaticParticipant() );
    detachedContest.addContestLevel1Approver( contestApprover );

    SSIContestApproversUpdateAssociation updateAssociation = new SSIContestApproversUpdateAssociation( detachedContest );
    updateAssociation.execute( attachedContest );
    assertEquals( detachedContest.getContestLevel1Approvers(), attachedContest.getContestLevel1Approvers() );
  }

  public void testAddLevel1WithAttachedApprovers()
  {
    SSIContest attachedContest = buildSsiContest();
    SSIContestApprover attachedContestApprover = new SSIContestApprover();
    attachedContestApprover.setApprover( ParticipantDAOImplTest.buildUniqueParticipant( "one" ) );
    attachedContest.addContestLevel1Approver( attachedContestApprover );

    SSIContest detachedContest = buildSsiContest();
    SSIContestApprover detachedContestApprover = new SSIContestApprover();
    detachedContestApprover.setApprover( ParticipantDAOImplTest.buildUniqueParticipant( "two" ) );
    detachedContest.addContestLevel1Approver( detachedContestApprover );
    detachedContest.addContestLevel1Approver( attachedContestApprover );

    SSIContestApproversUpdateAssociation updateAssociation = new SSIContestApproversUpdateAssociation( detachedContest );
    updateAssociation.execute( attachedContest );
    assertEquals( detachedContest.getContestLevel1Approvers(), attachedContest.getContestLevel1Approvers() );
  }

  public void testRemoveLevel1ApproverWithNoAttachedApprovers()
  {
    SSIContest attachedContest = buildSsiContest();
    SSIContestApprover contestApprover = new SSIContestApprover();
    contestApprover.setApprover( ParticipantDAOImplTest.buildStaticParticipant() );
    attachedContest.addContestLevel1Approver( contestApprover );

    SSIContest detachedContest = buildSsiContest();
    SSIContestApproversUpdateAssociation updateAssociation = new SSIContestApproversUpdateAssociation( detachedContest );
    updateAssociation.execute( attachedContest );
    assertEquals( detachedContest.getContestLevel1Approvers(), attachedContest.getContestLevel1Approvers() );
  }

  public void testRemoveLevel1ApproverWithAttachedApprovers()
  {
    SSIContest attachedContest = buildSsiContest();
    SSIContestApprover attachedContestApprover = new SSIContestApprover();
    attachedContestApprover.setApprover( ParticipantDAOImplTest.buildUniqueParticipant( "one" ) );
    attachedContest.addContestLevel1Approver( attachedContestApprover );

    SSIContest detachedContest = buildSsiContest();
    SSIContestApprover detachedContestApprover = new SSIContestApprover();
    detachedContestApprover.setApprover( ParticipantDAOImplTest.buildUniqueParticipant( "two" ) );
    detachedContest.addContestLevel1Approver( detachedContestApprover );
    attachedContest.addContestLevel1Approver( detachedContestApprover );

    SSIContestApproversUpdateAssociation updateAssociation = new SSIContestApproversUpdateAssociation( detachedContest );
    updateAssociation.execute( attachedContest );
    assertEquals( detachedContest.getContestLevel1Approvers(), attachedContest.getContestLevel1Approvers() );
  }

  public void testAddLevel2ApproverNoAttachedApprovers()
  {
    SSIContest attachedContest = buildSsiContest();
    SSIContest detachedContest = buildSsiContest();
    SSIContestApprover contestApprover = new SSIContestApprover();
    contestApprover.setApprover( ParticipantDAOImplTest.buildStaticParticipant() );
    detachedContest.addContestLevel2Approver( contestApprover );

    SSIContestApproversUpdateAssociation updateAssociation = new SSIContestApproversUpdateAssociation( detachedContest );
    updateAssociation.execute( attachedContest );
    assertEquals( detachedContest.getContestLevel2Approvers(), attachedContest.getContestLevel2Approvers() );
  }

  public void testAddLevel2WithAttachedApprovers()
  {
    SSIContest attachedContest = buildSsiContest();
    SSIContestApprover attachedContestApprover = new SSIContestApprover();
    attachedContestApprover.setApprover( ParticipantDAOImplTest.buildUniqueParticipant( "one" ) );
    attachedContest.addContestLevel2Approver( attachedContestApprover );

    SSIContest detachedContest = buildSsiContest();
    SSIContestApprover detachedContestApprover = new SSIContestApprover();
    detachedContestApprover.setApprover( ParticipantDAOImplTest.buildUniqueParticipant( "two" ) );
    detachedContest.addContestLevel2Approver( detachedContestApprover );
    detachedContest.addContestLevel2Approver( attachedContestApprover );

    SSIContestApproversUpdateAssociation updateAssociation = new SSIContestApproversUpdateAssociation( detachedContest );
    updateAssociation.execute( attachedContest );
    assertEquals( attachedContest.getContestLevel2Approvers().size(), 2 );
  }

  public void testRemoveLevel2ApproverWithNoAttachedApprovers()
  {
    SSIContest attachedContest = buildSsiContest();
    SSIContestApprover contestApprover = new SSIContestApprover();
    contestApprover.setApprover( ParticipantDAOImplTest.buildStaticParticipant() );
    attachedContest.addContestLevel2Approver( contestApprover );

    SSIContest detachedContest = buildSsiContest();
    SSIContestApproversUpdateAssociation updateAssociation = new SSIContestApproversUpdateAssociation( detachedContest );
    updateAssociation.execute( attachedContest );
    assertEquals( detachedContest.getContestLevel2Approvers(), attachedContest.getContestLevel2Approvers() );
  }

  public void testRemoveLevel2ApproverWithAttachedApprovers()
  {
    SSIContest attachedContest = buildSsiContest();
    SSIContestApprover attachedContestApprover = new SSIContestApprover();
    attachedContestApprover.setApprover( ParticipantDAOImplTest.buildUniqueParticipant( "one" ) );
    attachedContest.addContestLevel2Approver( attachedContestApprover );

    SSIContest detachedContest = buildSsiContest();
    SSIContestApprover detachedContestApprover = new SSIContestApprover();
    detachedContestApprover.setApprover( ParticipantDAOImplTest.buildUniqueParticipant( "two" ) );
    detachedContest.addContestLevel2Approver( detachedContestApprover );
    attachedContest.addContestLevel2Approver( detachedContestApprover );

    SSIContestApproversUpdateAssociation updateAssociation = new SSIContestApproversUpdateAssociation( detachedContest );
    updateAssociation.execute( attachedContest );
    assertEquals( detachedContest.getContestLevel2Approvers(), attachedContest.getContestLevel2Approvers() );
  }

  public void testAddClaimApproverNoAttachedApprovers()
  {
    SSIContest attachedContest = buildSsiContest();
    SSIContest detachedContest = buildSsiContest();
    SSIContestApprover contestApprover = new SSIContestApprover();
    contestApprover.setApprover( ParticipantDAOImplTest.buildStaticParticipant() );
    detachedContest.addContestLevel1Approver( contestApprover );

    SSIContestApproversUpdateAssociation updateAssociation = new SSIContestApproversUpdateAssociation( detachedContest );
    updateAssociation.execute( attachedContest );
    assertEquals( attachedContest.getContestLevel1Approvers().size(), 1 );
  }

  public void testAddClaimWithAttachedApprovers()
  {
    SSIContest attachedContest = buildSsiContest();
    SSIContestApprover attachedContestApprover = new SSIContestApprover();
    attachedContestApprover.setApprover( ParticipantDAOImplTest.buildUniqueParticipant( "one" ) );
    attachedContest.addContestLevel1Approver( attachedContestApprover );

    SSIContest detachedContest = buildSsiContest();
    SSIContestApprover detachedContestApprover = new SSIContestApprover();
    detachedContestApprover.setApprover( ParticipantDAOImplTest.buildUniqueParticipant( "two" ) );
    detachedContest.addContestLevel1Approver( detachedContestApprover );
    detachedContest.addContestLevel1Approver( attachedContestApprover );

    SSIContestApproversUpdateAssociation updateAssociation = new SSIContestApproversUpdateAssociation( detachedContest );
    updateAssociation.execute( attachedContest );
    assertEquals( 2, attachedContest.getContestLevel1Approvers().size() );
  }

  public void testRemoveClaimApproverWithNoAttachedApprovers()
  {
    SSIContest attachedContest = buildSsiContest();
    SSIContestApprover contestApprover = new SSIContestApprover();
    contestApprover.setApprover( ParticipantDAOImplTest.buildStaticParticipant() );
    attachedContest.addContestLevel1Approver( contestApprover );

    SSIContest detachedContest = buildSsiContest();
    SSIContestApproversUpdateAssociation updateAssociation = new SSIContestApproversUpdateAssociation( detachedContest );
    updateAssociation.execute( attachedContest );
    assertEquals( attachedContest.getContestLevel1Approvers().size(), 0 );
  }

  public void testRemoveClaimApproverWithAttachedApprovers()
  {
    SSIContest attachedContest = buildSsiContest();
    SSIContestApprover attachedContestApprover = new SSIContestApprover();
    attachedContestApprover.setApprover( ParticipantDAOImplTest.buildUniqueParticipant( "one" ) );
    attachedContest.addContestLevel1Approver( attachedContestApprover );

    SSIContest detachedContest = buildSsiContest();
    SSIContestApprover detachedContestApprover = new SSIContestApprover();
    detachedContestApprover.setApprover( ParticipantDAOImplTest.buildUniqueParticipant( "two" ) );
    detachedContest.addContestLevel1Approver( detachedContestApprover );
    attachedContest.addContestLevel1Approver( detachedContestApprover );

    SSIContestApproversUpdateAssociation updateAssociation = new SSIContestApproversUpdateAssociation( detachedContest );
    updateAssociation.execute( attachedContest );
    assertEquals( attachedContest.getContestLevel1Approvers().size(), 1 );
  }

  private SSIContest buildSsiContest()
  {
    SSIContest contest = new SSIContest();
    contest.setCmAssetCode( "Test Contest" + System.currentTimeMillis() );
    return contest;
  }

}

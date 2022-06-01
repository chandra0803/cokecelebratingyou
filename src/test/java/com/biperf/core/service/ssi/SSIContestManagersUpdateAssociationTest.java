
package com.biperf.core.service.ssi;

import com.biperf.core.dao.participant.hibernate.ParticipantDAOImplTest;
import com.biperf.core.domain.ssi.SSIContest;
import com.biperf.core.domain.ssi.SSIContestManager;
import com.biperf.core.service.BaseServiceTest;

/**
 * 
 * @author dudam
 * @since Nov 6, 2014
 * @version 1.0
 */
public class SSIContestManagersUpdateAssociationTest extends BaseServiceTest
{
  public void testAddManagerWithNoAttachedManagers()
  {
    SSIContest attachedContest = buildSsiContest();
    SSIContest detachedContest = buildSsiContest();
    SSIContestManager contestManager = new SSIContestManager();
    contestManager.setManager( ParticipantDAOImplTest.buildStaticParticipant() );
    detachedContest.addContestManager( contestManager );
    SSIContestManagersUpdateAssociation updateAssociation = new SSIContestManagersUpdateAssociation( detachedContest );
    updateAssociation.execute( attachedContest );
    assertEquals( detachedContest.getContestManagers(), attachedContest.getContestManagers() );
  }

  public void testAddManagerWithAttachedManagers()
  {
    SSIContest attachedContest = buildSsiContest();
    SSIContestManager attachedContestManager = new SSIContestManager();
    attachedContestManager.setManager( ParticipantDAOImplTest.buildUniqueParticipant( "one" ) );
    attachedContest.addContestManager( attachedContestManager );

    SSIContest detachedContest = buildSsiContest();
    SSIContestManager detachedContestManager = new SSIContestManager();
    detachedContestManager.setManager( ParticipantDAOImplTest.buildUniqueParticipant( "two" ) );
    detachedContest.addContestManager( detachedContestManager );
    detachedContest.addContestManager( attachedContestManager );

    SSIContestManagersUpdateAssociation updateAssociation = new SSIContestManagersUpdateAssociation( detachedContest );
    updateAssociation.execute( attachedContest );
    assertEquals( detachedContest.getContestManagers(), attachedContest.getContestManagers() );
  }

  public void testRemoveManagerWithNoAttachedManagers()
  {
    SSIContest attachedContest = buildSsiContest();
    SSIContestManager contestManager = new SSIContestManager();
    contestManager.setManager( ParticipantDAOImplTest.buildStaticParticipant() );
    attachedContest.addContestManager( contestManager );
    SSIContest detachedContest = buildSsiContest();
    SSIContestManagersUpdateAssociation updateAssociation = new SSIContestManagersUpdateAssociation( detachedContest );
    updateAssociation.execute( attachedContest );
    assertEquals( detachedContest.getContestManagers(), attachedContest.getContestManagers() );
  }

  public void testRemoveManagerWithAttachedManagers()
  {
    SSIContest attachedContest = buildSsiContest();
    SSIContestManager attachedContestManager = new SSIContestManager();
    attachedContestManager.setManager( ParticipantDAOImplTest.buildUniqueParticipant( "one" ) );
    attachedContest.addContestManager( attachedContestManager );

    SSIContest detachedContest = buildSsiContest();
    SSIContestManager detachedContestManager = new SSIContestManager();
    detachedContestManager.setManager( ParticipantDAOImplTest.buildUniqueParticipant( "two" ) );
    detachedContest.addContestManager( detachedContestManager );
    attachedContest.addContestManager( detachedContestManager );

    SSIContestManagersUpdateAssociation updateAssociation = new SSIContestManagersUpdateAssociation( detachedContest );
    updateAssociation.execute( attachedContest );
    assertEquals( detachedContest.getContestManagers(), attachedContest.getContestManagers() );
  }

  private SSIContest buildSsiContest()
  {
    SSIContest contest = new SSIContest();
    contest.setCmAssetCode( "Test Contest" + System.currentTimeMillis() );
    return contest;
  }

}

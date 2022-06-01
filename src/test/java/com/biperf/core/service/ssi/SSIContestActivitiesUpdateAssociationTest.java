
package com.biperf.core.service.ssi;

import com.biperf.core.dao.ssi.hibernate.SSIContestDAOImplTest;
import com.biperf.core.domain.ssi.SSIContest;
import com.biperf.core.domain.ssi.SSIContestActivity;
import com.biperf.core.service.BaseServiceTest;

public class SSIContestActivitiesUpdateAssociationTest extends BaseServiceTest
{
  public void testAddActivityWithNoAttachedActivities()
  {
    SSIContest attachedContest = buildSsiContest();
    SSIContest detachedContest = buildSsiContest();
    SSIContestActivity contestActivity = new SSIContestActivity();
    contestActivity = SSIContestDAOImplTest.buildStaticContestActivity( new Integer( 1 ) );
    detachedContest.addContestActivity( contestActivity );

    SSIContestActivitiesUpdateAssociation updateAssociation = new SSIContestActivitiesUpdateAssociation( detachedContest );
    updateAssociation.execute( attachedContest );
    assertEquals( detachedContest.getContestActivities(), attachedContest.getContestActivities() );
  }

  public void testAddActivityWithAttachedActivities()
  {
    SSIContest attachedContest = buildSsiContest();
    SSIContestActivity attachedContestActivity = new SSIContestActivity();
    attachedContestActivity = SSIContestDAOImplTest.buildStaticContestActivity( new Integer( 1 ) );
    attachedContest.addContestActivity( attachedContestActivity );

    SSIContest detachedContest = buildSsiContest();
    SSIContestActivity detachedContestActivity = new SSIContestActivity();
    detachedContestActivity = SSIContestDAOImplTest.buildStaticContestActivity( new Integer( 2 ) );
    detachedContest.addContestActivity( detachedContestActivity );
    detachedContest.addContestActivity( attachedContestActivity );

    SSIContestActivitiesUpdateAssociation updateAssociation = new SSIContestActivitiesUpdateAssociation( detachedContest );
    updateAssociation.execute( attachedContest );
    assertEquals( detachedContest.getContestActivities(), attachedContest.getContestActivities() );
  }

  public void testRemoveActivityWithNoAttachedActivities()
  {
    SSIContest attachedContest = buildSsiContest();
    SSIContestActivity contestActivity = new SSIContestActivity();
    contestActivity = SSIContestDAOImplTest.buildStaticContestActivity( new Integer( 1 ) );
    attachedContest.addContestActivity( contestActivity );

    SSIContest detachedContest = buildSsiContest();
    SSIContestActivitiesUpdateAssociation updateAssociation = new SSIContestActivitiesUpdateAssociation( detachedContest );
    updateAssociation.execute( attachedContest );
    assertEquals( detachedContest.getContestActivities(), attachedContest.getContestActivities() );
  }

  public void testRemoveActivityWithAttachedActivities()
  {
    SSIContest attachedContest = buildSsiContest();
    SSIContestActivity contestActivity = new SSIContestActivity();
    contestActivity = SSIContestDAOImplTest.buildStaticContestActivity( new Integer( 1 ) );
    attachedContest.addContestActivity( contestActivity );

    SSIContest detachedContest = buildSsiContest();
    SSIContestActivity detachedContestActivity = new SSIContestActivity();
    detachedContest.addContestActivity( detachedContestActivity );
    attachedContest.addContestActivity( detachedContestActivity );

    SSIContestActivitiesUpdateAssociation updateAssociation = new SSIContestActivitiesUpdateAssociation( detachedContest );
    updateAssociation.execute( attachedContest );
    assertEquals( detachedContest.getContestActivities(), attachedContest.getContestActivities() );
  }

  private SSIContest buildSsiContest()
  {
    SSIContest contest = new SSIContest();
    contest.setCmAssetCode( "Test Contest" + System.currentTimeMillis() );
    return contest;
  }

}

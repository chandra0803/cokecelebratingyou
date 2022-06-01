
package com.biperf.core.service.ssi;

import com.biperf.core.dao.ssi.hibernate.SSIContestDAOImplTest;
import com.biperf.core.domain.ssi.SSIContest;
import com.biperf.core.domain.ssi.SSIContestClaimField;
import com.biperf.core.service.BaseServiceTest;

/**
 * 
 * @author dudam
 * @since May 13, 2014
 * @version 1.0
 */
public class SSIContestClaimFieldsUpdateAssociationTest extends BaseServiceTest
{

  public void testAddClaimFieldNoAttachedClaimFields()
  {
    SSIContest attachedContest = buildSsiContest();
    SSIContest detachedContest = buildSsiContest();
    SSIContestClaimField claimField = SSIContestDAOImplTest.buildStaticClaimField( 1 );
    detachedContest.addClaimField( claimField );

    SSIContestClaimFieldsUpdateAssociation updateAssociation = new SSIContestClaimFieldsUpdateAssociation( detachedContest );
    updateAssociation.execute( attachedContest );
    assertEquals( detachedContest.getClaimFields(), attachedContest.getClaimFields() );
  }

  public void testAddClaimFieldWithAttachedClaimFields()
  {
    SSIContest attachedContest = buildSsiContest();
    SSIContestClaimField attachedClaimField = SSIContestDAOImplTest.buildStaticClaimField( 1 );
    attachedContest.addClaimField( attachedClaimField );

    SSIContest detachedContest = buildSsiContest();
    SSIContestClaimField detachedClaimField = SSIContestDAOImplTest.buildStaticClaimField( 2 );
    detachedContest.addClaimField( detachedClaimField );
    detachedContest.addClaimField( attachedClaimField );

    SSIContestClaimFieldsUpdateAssociation updateAssociation = new SSIContestClaimFieldsUpdateAssociation( detachedContest );
    updateAssociation.execute( attachedContest );
    assertEquals( detachedContest.getClaimFields(), attachedContest.getClaimFields() );
  }

  public void testRemoveClaimFieldWithNoAttachedClaimFields()
  {
    SSIContest attachedContest = buildSsiContest();
    SSIContestClaimField claimField = SSIContestDAOImplTest.buildStaticClaimField( 1 );
    attachedContest.addClaimField( claimField );

    SSIContest detachedContest = buildSsiContest();
    SSIContestClaimFieldsUpdateAssociation updateAssociation = new SSIContestClaimFieldsUpdateAssociation( detachedContest );
    updateAssociation.execute( attachedContest );
    assertEquals( detachedContest.getClaimFields(), attachedContest.getClaimFields() );
  }

  public void testRemoveClaimFieldWithAttachedClaimFields()
  {
    SSIContest attachedContest = buildSsiContest();
    SSIContestClaimField attachedClaimField = SSIContestDAOImplTest.buildStaticClaimField( 1 );
    attachedContest.addClaimField( attachedClaimField );

    SSIContest detachedContest = buildSsiContest();
    SSIContestClaimField detachedClaimField = SSIContestDAOImplTest.buildStaticClaimField( 2 );
    detachedContest.addClaimField( detachedClaimField );
    attachedContest.addClaimField( detachedClaimField );

    SSIContestClaimFieldsUpdateAssociation updateAssociation = new SSIContestClaimFieldsUpdateAssociation( detachedContest );
    updateAssociation.execute( attachedContest );
    assertEquals( detachedContest.getClaimFields(), attachedContest.getClaimFields() );
  }

  private SSIContest buildSsiContest()
  {
    SSIContest contest = new SSIContest();
    contest.setCmAssetCode( "Test Contest" + System.currentTimeMillis() );
    return contest;
  }

}

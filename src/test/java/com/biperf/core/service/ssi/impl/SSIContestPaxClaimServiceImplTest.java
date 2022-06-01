
package com.biperf.core.service.ssi.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jmock.Mock;
import org.jmock.MockObjectTestCase;
import org.junit.Before;
import org.junit.Test;

import com.biperf.core.dao.ssi.SSIContestPaxClaimDAO;
import com.biperf.core.domain.managertoolkit.AlertMessage;
import com.biperf.core.domain.managertoolkit.ParticipantAlert;
import com.biperf.core.domain.ssi.SSIContestPaxClaim;
import com.biperf.core.exception.ServiceErrorException;

/**
 * 
 * SSIContestPaxClaimServiceImplTest.
 * 
 * @author Rameshj
 * @since Aug 23, 2017
 * @version 1.0
 */
public class SSIContestPaxClaimServiceImplTest extends MockObjectTestCase
{
  private Mock mockSsiContestPaxClaimDAO = null;

  List<Long> paxContestIds = new ArrayList<Long>();

  private SSIContestPaxClaimServiceImpl ssicontestService = new SSIContestPaxClaimServiceImpl();

  public SSIContestPaxClaimServiceImplTest( String test )
  {
    super( test );
  }

  @Before
  protected void setUp() throws Exception
  {
    super.setUp();
    mockSsiContestPaxClaimDAO = new Mock( SSIContestPaxClaimDAO.class );
    ssicontestService.setSsiContestPaxClaimDAO( (SSIContestPaxClaimDAO)mockSsiContestPaxClaimDAO.proxy() );
  }

  @Test
  public void testgetPaxClaimsForApprovalByContestId() throws ServiceErrorException, SQLException
  {
    Set<Long> contestApprovalIDs = new HashSet<Long>();

    List<SSIContestPaxClaim> waitingForApprovalClaims = new ArrayList<SSIContestPaxClaim>();
    waitingForApprovalClaims.add( SSIServiceTestUtil.addPaxClaimOne() );
    waitingForApprovalClaims.add( SSIServiceTestUtil.addPaxClaimTwo() );
    mockSsiContestPaxClaimDAO.expects( once() ).method( "getPaxClaimsForApprovalByContestId" ).will( returnValue( waitingForApprovalClaims ) );
    contestApprovalIDs = ssicontestService.getPaxClaimsForApprovalByContestId( getAlertLists() );
    assertNotNull( contestApprovalIDs );
    assertTrue( contestApprovalIDs.size() > 0 );
  }

  public static List<ParticipantAlert> getAlertLists()
  {
    List<ParticipantAlert> alertsList = new ArrayList<ParticipantAlert>();

    AlertMessage alrtMsgOne = new AlertMessage();
    alrtMsgOne.setContestId( new Long( 6356 ) );
    alrtMsgOne.setExpiryDate( new Date() );
    alrtMsgOne.setMessage( "Claim Review Approval" );
    alrtMsgOne.setValid( true );
    alrtMsgOne.setSsiAlertType( "claim_approver" );

    ParticipantAlert paxalertOne = new ParticipantAlert();
    paxalertOne.setAlertMessage( alrtMsgOne );
    alertsList.add( paxalertOne );

    return alertsList;
  }

}

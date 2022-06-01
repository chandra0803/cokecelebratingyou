/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javatest/com/biperf/core/service/audit/impl/AuditServiceImplTest.java,v $
 */

package com.biperf.core.service.audit.impl;

import java.util.ArrayList;
import java.util.List;

import org.easymock.EasyMock;

import com.biperf.core.dao.audit.PayoutCalculationAuditDAO;
import com.biperf.core.domain.audit.ClaimBasedPayoutCalculationAudit;
import com.biperf.core.domain.audit.PayoutCalculationAudit;
import com.biperf.core.domain.audit.SalesPayoutCalculationAudit;
import com.biperf.core.domain.claim.ProductClaim;
import com.biperf.core.domain.participant.Participant;

import junit.framework.TestCase;

/*
 * ActivityServiceImplTest <p> <b>Change History:</b><br> <table border="1"> <tr> <th>Author</th>
 * <th>Date</th> <th>Version</th> <th>Comments</th> </tr> <tr> <td>OPI Admin</td> <td>Jul
 * 14, 2005</td> <td>1.0</td> <td>created</td> </tr> </table> </p>
 * 
 *
 */

public class AuditServiceImplTest extends TestCase
{
  private PayoutCalculationAuditServiceImpl classUnderTest;
  private PayoutCalculationAuditDAO mock;

  public void setUp() throws Exception
  {
    mock = EasyMock.createMock( PayoutCalculationAuditDAO.class );
    classUnderTest = new PayoutCalculationAuditServiceImpl();
    classUnderTest.setPayoutCalculationAuditDAO( mock );
  }

  /**
   * Test the saving of an activity
   */
  public void testSaveAudit()
  {
    ClaimBasedPayoutCalculationAudit claimBasedPayoutCalculationAudit = new ClaimBasedPayoutCalculationAudit();
    claimBasedPayoutCalculationAudit.setId( new Long( 1 ) );
    EasyMock.expect( mock.save( claimBasedPayoutCalculationAudit ) ).andReturn( claimBasedPayoutCalculationAudit );
    EasyMock.replay( mock );
    classUnderTest.save( claimBasedPayoutCalculationAudit );
    EasyMock.verify( mock );
  }

  /**
   * Test getting an activity by id
   */
  public void testGetPayoutCalculationAuditById()
  {
    Long id = Long.valueOf( "2" );
    ClaimBasedPayoutCalculationAudit claimBasedPayoutCalculationAudit = new ClaimBasedPayoutCalculationAudit();
    claimBasedPayoutCalculationAudit.setId( id );
    EasyMock.expect( mock.getPayoutCalculationAuditById( id ) ).andReturn( claimBasedPayoutCalculationAudit );
    EasyMock.replay( mock );
    classUnderTest.getPayoutCalculationAuditById( id );
    EasyMock.verify( mock );
  }

  /**
   * Test getting all activities
   */
  public void testGetAllPayoutCalculationAudits()
  {
    Long id1 = Long.valueOf( "2" );
    ClaimBasedPayoutCalculationAudit claimBasedPayoutCalculationAudit = new ClaimBasedPayoutCalculationAudit();
    claimBasedPayoutCalculationAudit.setId( id1 );
    Long id2 = Long.valueOf( "3" );
    SalesPayoutCalculationAudit salesPayoutCalculationAudit = new SalesPayoutCalculationAudit();
    salesPayoutCalculationAudit.setId( id2 );
    List<PayoutCalculationAudit> audits = new ArrayList<>();
    audits.add( claimBasedPayoutCalculationAudit );
    audits.add( salesPayoutCalculationAudit );
    EasyMock.expect( mock.getAll() ).andReturn( audits );
    EasyMock.replay( mock );
    List result = classUnderTest.getAll();
    EasyMock.verify( mock );
    assertSame( audits, result );
  }

  /**
   * Test getting all activities
   */
  public void testGetPayoutCalculationAuditsByClaimAndParticipant()
  {
    Long id1 = Long.valueOf( "1" );
    Long id2 = Long.valueOf( "2" );
    Long id3 = Long.valueOf( "3" );
    Long id4 = Long.valueOf( "4" );
    ClaimBasedPayoutCalculationAudit claimBasedPayoutCalculationAudit = new ClaimBasedPayoutCalculationAudit();
    claimBasedPayoutCalculationAudit.setId( id1 );
    ProductClaim productClaim = new ProductClaim();
    productClaim.setId( id2 );
    claimBasedPayoutCalculationAudit.setClaim( productClaim );
    Participant participant = new Participant();
    participant.setId( id3 );
    claimBasedPayoutCalculationAudit.setParticipant( participant );
    SalesPayoutCalculationAudit salesPayoutCalculationAudit = new SalesPayoutCalculationAudit();
    salesPayoutCalculationAudit.setId( id4 );
    salesPayoutCalculationAudit.setClaim( productClaim );
    salesPayoutCalculationAudit.setParticipant( participant );
    List<PayoutCalculationAudit> audits = new ArrayList<>();
    audits.add( claimBasedPayoutCalculationAudit );
    audits.add( salesPayoutCalculationAudit );
    EasyMock.expect( mock.getPayoutCalculationAuditsByClaimIdAndParticipantId( id2, id3 ) ).andReturn( audits );
    EasyMock.replay( mock );
    List result = classUnderTest.getPayoutCalculationAuditsByClaimIdAndParticipantId( id2, id3 );
    EasyMock.verify( mock );
    assertSame( audits, result );
  }
}

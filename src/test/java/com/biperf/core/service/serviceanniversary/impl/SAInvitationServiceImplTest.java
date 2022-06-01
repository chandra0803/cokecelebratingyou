
package com.biperf.core.service.serviceanniversary.impl;

import org.jmock.Mock;
import org.jmock.MockObjectTestCase;
import org.springframework.beans.factory.annotation.Autowired;

import com.biperf.core.dao.serviceanniversary.ServiceAnniversaryDAO;
import com.biperf.core.domain.serviceanniversary.SACelebrationInfo;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.exception.UniqueConstraintViolationException;

public class SAInvitationServiceImplTest extends MockObjectTestCase
{

  /**
   * Constructor to take the name of the test.
   * 
   * @param test
   */
  public SAInvitationServiceImplTest( String test )
  {
    super( test );
  }

  /** SAInvitationServiceImpl */
  @Autowired
  private ServiceAnniversaryServiceImpl saInvitationService = new ServiceAnniversaryServiceImpl();

  /** mockSAInvitationDAO */
  private Mock mockSAInvitationDAO = null;

  /**
   * Setup this test case. Overridden from
   * 
   * @see junit.framework.TestCase#setUp()
   * @throws Exception
   */
  protected void setUp() throws Exception
  {
    mockSAInvitationDAO = new Mock( ServiceAnniversaryDAO.class );
  }

  /**
   * Test saving the SAInvitationInfo through the service.
   * 
   * @throws ServiceErrorException
   * @throws UniqueConstraintViolationException 
   */
  public void testsaveSAInvitationInfo()
      // throws UniqueConstraintViolationException,
      throws ServiceErrorException, UniqueConstraintViolationException, Exception
  {

    SACelebrationInfo saInvitationInfo = new SACelebrationInfo();
    saInvitationInfo.setId( new Long( 101 ) );

    // save the user
    mockSAInvitationDAO.expects( once() ).method( "saveSAInvitationInfo" ).with( same( saInvitationInfo ) ).will( returnValue( saInvitationInfo ) );

    // test the saInvitationService.saveSAInvitationInfo
    saInvitationService.saveSACelebrationInfo( saInvitationInfo );

    // Verify the mockDAO returns the same thing the service is attempting to save.
    mockSAInvitationDAO.verify();
  }
}

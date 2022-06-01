
package com.biperf.core.service.engageprogram.impl;

import org.jmock.Mock;
import org.jmock.MockObjectTestCase;
import org.springframework.beans.factory.annotation.Autowired;

import com.biperf.core.dao.engageprogram.EngageProgramDAO;
import com.biperf.core.domain.engageprogram.EngageProgram;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.exception.UniqueConstraintViolationException;

public class EngageProgramServiceImplTest extends MockObjectTestCase
{

  /**
   * Constructor to take the name of the test.
   * 
   * @param test
   */
  public EngageProgramServiceImplTest( String test )
  {
    super( test );
  }

  /** EngageProgramServiceImpl */
  @Autowired
  private EngageProgramServiceImpl EngageProgramService = new EngageProgramServiceImpl();

  /** mockEngageProgramDAO */
  private Mock mockEngageProgramDAO = null;

  /**
   * Setup this test case. Overridden from
   * 
   * @see junit.framework.TestCase#setUp()
   * @throws Exception
   */
  protected void setUp() throws Exception
  {
    mockEngageProgramDAO = new Mock( EngageProgramDAO.class );
  }

  /**
   * Test saving the EngageProgram through the service.
   * 
   * @throws ServiceErrorException
   * @throws UniqueConstraintViolationException 
   */
  public void testsaveProgramDetails()
      // throws UniqueConstraintViolationException,
      throws ServiceErrorException, UniqueConstraintViolationException, Exception
  {

    EngageProgram programInfo = new EngageProgram();
    programInfo.setId( new Long( 101 ) );

    // save the user
    mockEngageProgramDAO.expects( once() ).method( "saveProgramDetails" ).with( same( programInfo ) ).will( returnValue( programInfo ) );

    // test the EngageProgramService.saveProgramDetails
    EngageProgramService.saveEngageProgramDetails( programInfo );

    // Verify the mockDAO returns the same thing the service is attempting to save.
    mockEngageProgramDAO.verify();
  }
}

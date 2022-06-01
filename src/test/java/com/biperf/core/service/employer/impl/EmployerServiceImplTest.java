/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javatest/com/biperf/core/service/employer/impl/EmployerServiceImplTest.java,v $
 */

package com.biperf.core.service.employer.impl;

import java.util.ArrayList;
import java.util.List;

import org.jmock.Mock;
import org.jmock.MockObjectTestCase;

import com.biperf.core.dao.employer.EmployerDAO;
import com.biperf.core.domain.employer.Employer;

/**
 * EmployerServiceImplTest.
 * <p>
 * <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>crosenquest</td>
 * <td>Apr 20, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class EmployerServiceImplTest extends MockObjectTestCase
{

  /**
   * Constructor to take the name of the test.
   * 
   * @param test
   */
  public EmployerServiceImplTest( String test )
  {
    super( test );
  }

  /** EmployerServiceImpl */
  private EmployerServiceImpl employerService = new EmployerServiceImpl();

  /** mockEmployerDAO */
  private Mock mockEmployerDAO = null;

  /**
   * Setup this test case. Overridden from
   * 
   * @see junit.framework.TestCase#setUp()
   * @throws Exception
   */
  protected void setUp() throws Exception
  {
    mockEmployerDAO = new Mock( EmployerDAO.class );
    employerService.setEmployerDAO( (EmployerDAO)mockEmployerDAO.proxy() );
  }

  /**
   * Test saving employer and getting the employer by the id.
   */
  public void testSaveEmployerAndGetEmployerById()
  {

    // Create the test Employer.
    Employer employer = new Employer();
    employer.setId( new Long( 1 ) );
    employer.setName( "ServiceTestNAME" );

    mockEmployerDAO.expects( once() ).method( "getEmployerById" ).with( same( employer.getId() ) ).will( returnValue( employer ) );

    Employer actualEmployer = employerService.getEmployerById( employer.getId() );

    assertEquals( "Actual employer wasn't equal to what was expected", employer, actualEmployer );

    mockEmployerDAO.verify();
  }

  /**
   * Test search for the employer with the given search criteria.
   */
  public void testSearchEmployer()
  {

    // Create the test Employer.
    Employer employer = new Employer();
    employer.setId( new Long( 1 ) );
    employer.setName( "name-ABC" );

    // Create the test Employer.
    Employer employer2 = new Employer();
    employer2.setId( new Long( 1 ) );
    employer2.setName( "name-XAZ" );

    List expectedList = new ArrayList();
    expectedList.add( employer );
    expectedList.add( employer2 );

    mockEmployerDAO.expects( once() ).method( "searchEmployer" ).with( same( "A" ) ).will( returnValue( expectedList ) );

    List actualList = employerService.searchEmployer( "A" );

    assertEquals( "Actual search results wasn't equal to what was expected", expectedList, actualList );

    mockEmployerDAO.verify();

  }

  /**
   * Test saving or updating the employer.
   */
  public void testSaveEmployer()
  {

    // Create the test Employer.
    Employer employer = new Employer();
    employer.setId( new Long( 1 ) );
    employer.setName( "testNAME" );

    mockEmployerDAO.expects( once() ).method( "saveEmployer" ).with( same( employer ) ).will( returnValue( employer ) );

    Employer actualEmployer = this.employerService.saveEmployer( employer );

    assertEquals( "Actual saved employer wasn't equal to what was expected", employer, actualEmployer );

    employer.setName( "testUPDATEDNAME" );

    mockEmployerDAO.expects( once() ).method( "saveEmployer" ).with( same( employer ) ).will( returnValue( employer ) );

    actualEmployer = this.employerService.saveEmployer( employer );

    assertEquals( "Actual updated employer wasn't equal to what was expected", employer, actualEmployer );

    mockEmployerDAO.verify();
  }

  /**
   * Test getting the employer by the id.
   */
  public void testGetEmployerById()
  {

    Employer employer = new Employer();
    employer.setId( new Long( 1 ) );
    employer.setName( "testNAME" );

    mockEmployerDAO.expects( once() ).method( "getEmployerById" ).with( same( employer.getId() ) ).will( returnValue( employer ) );

    Employer actualEmployer = this.employerService.getEmployerById( employer.getId() );

    assertEquals( "Actual employer wasn't equal to what was expected", employer, actualEmployer );

    mockEmployerDAO.verify();

  }

}

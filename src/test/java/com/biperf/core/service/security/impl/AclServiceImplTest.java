/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javatest/com/biperf/core/service/security/impl/AclServiceImplTest.java,v $
 */

package com.biperf.core.service.security.impl;

import java.util.ArrayList;
import java.util.List;

import org.jmock.Mock;
import org.jmock.MockObjectTestCase;

import com.biperf.core.dao.security.AclDAO;
import com.biperf.core.domain.user.Acl;

/**
 * Test to exercise the UserAclService.
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
 * <td>Apr 1, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class AclServiceImplTest extends MockObjectTestCase
{

  /**
   * Constructor to take the name of the test.
   * 
   * @param test
   */
  public AclServiceImplTest( String test )
  {
    super( test );
  }

  /** UserAclServiceImplementation */
  private AclServiceImpl aclService = new AclServiceImpl();

  /** mockUserAclDao */
  private Mock mockAclDAO = null;

  /**
   * Setup this test case. Overridden from
   * 
   * @see junit.framework.TestCase#setUp()
   * @throws Exception
   */
  protected void setUp() throws Exception
  {
    mockAclDAO = new Mock( AclDAO.class );
    aclService.setAclDAO( (AclDAO)mockAclDAO.proxy() );
  }

  /**
   * Test getting the Acl by id.
   */
  public void testGetAclById()
  {

    // Create the test Acl.
    Acl acl = new Acl();
    acl.setId( new Long( 1 ) );
    acl.setCode( "ServiceTestNAME" );
    acl.setHelpText( "ServiceTestHELPTEXT" );
    acl.setClassName( "ServiceTestCLASSNAME" );

    // AclDAO expected to call getAclId once with the AclId which will return the
    // Acl expected
    mockAclDAO.expects( once() ).method( "getAclById" ).with( same( acl.getId() ) ).will( returnValue( acl ) );

    aclService.getAclById( acl.getId() );

    mockAclDAO.verify();
  }

  /**
   * Test saving the Acl through the service.
   */
  public void testSaveAcl()
  {

    // Create the test Acl.
    Acl acl = new Acl();
    acl.setCode( "ServiceTestNAME" );
    acl.setHelpText( "ServiceTestHELPTEXT" );
    acl.setClassName( "ServiceTestCLASSNAME" );

    // Setup the dao with the test UserAcl
    mockAclDAO.expects( once() ).method( "saveAcl" ).with( same( acl ) );

    // Test the UserAclService.saveOrUpdateUserAcl
    aclService.saveAcl( acl );

    // Verify the mockDAO returns the same thing the service is attempting to save.
    mockAclDAO.verify();
  }

  /**
   * Test searching the for a list of Acls through the service.
   */
  public void testSearchAcl()
  {

    Acl acl = new Acl();
    acl.setCode( "ServiceTestNAME" );
    acl.setHelpText( "ServiceTestHELPTEXT" );
    acl.setClassName( "ServiceTestCLASSNAME" );

    Acl acl2 = new Acl();
    acl2.setCode( "ServiceTestNAMEABC" );
    acl2.setHelpText( "ServiceTestHELPTEXTABC" );
    acl2.setClassName( "ServiceTestCLASSNAMEABC" );

    List expectedList = new ArrayList();
    expectedList.add( acl2 );
    expectedList.add( acl );

    mockAclDAO.expects( once() ).method( "searchAcl" ).will( returnValue( expectedList ) );

    aclService.searchAcl( "Service", "test", "code" );

    // Verify the mockDAO returns the same thing the service is attempting to save.
    mockAclDAO.verify();

  }

  /**
   * Test getting all Acls in the database.
   */
  public void testGetAll()
  {

    Acl acl = new Acl();
    acl.setCode( "ServiceTestNAME" );
    acl.setHelpText( "ServiceTestHELPTEXT" );
    acl.setClassName( "ServiceTestCLASSNAME" );

    Acl acl2 = new Acl();
    acl2.setCode( "ServiceTestNAMEABC" );
    acl2.setHelpText( "ServiceTestHELPTEXTABC" );
    acl2.setClassName( "ServiceTestCLASSNAMEABC" );

    List expectedList = new ArrayList();
    expectedList.add( acl2 );
    expectedList.add( acl );

    mockAclDAO.expects( once() ).method( "getAll" ).will( returnValue( expectedList ) );

    List actualList = aclService.getAll();

    assertEquals( "Actual All list wasn't what was expected", expectedList, actualList );

    // Verify the mockDAO returns the same thing the service is attempting to save.
    mockAclDAO.verify();
  }

  /**
   * Test getting a list of Acls which aren't present in the list param, but present in the
   * database.
   */
  public void testGetAvailableAclsNotInList()
  {
    Acl acl = new Acl();
    acl.setCode( "ServiceTestNAME" );
    acl.setHelpText( "ServiceTestHELPTEXT" );
    acl.setClassName( "ServiceTestCLASSNAME" );

    Acl acl2 = new Acl();
    acl2.setCode( "ServiceTestNAMEABC" );
    acl2.setHelpText( "ServiceTestHELPTEXTABC" );
    acl2.setClassName( "ServiceTestCLASSNAMEABC" );

    List allList = new ArrayList();
    allList.add( acl2 );
    allList.add( acl );

    List expectedList = new ArrayList();
    expectedList.add( acl );

    List assignedList = new ArrayList();
    assignedList.add( acl2 );

    mockAclDAO.expects( once() ).method( "getAll" ).will( returnValue( allList ) );

    List actualList = aclService.getAvailableAclsNotInList( assignedList );

    assertEquals( "ActualList wasn't what was expected", expectedList, actualList );

    mockAclDAO.verify();
  }

}

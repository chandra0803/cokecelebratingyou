/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javatest/com/biperf/core/service/security/impl/RoleServiceImplTest.java,v $
 */

package com.biperf.core.service.security.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.jmock.Mock;
import org.jmock.MockObjectTestCase;
import org.junit.Test;

import com.biperf.core.dao.security.RoleDAO;
import com.biperf.core.domain.user.Role;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.security.RoleService;

/**
 * RoleServiceImplTest.
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
 * <td>Apr 6, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class RoleServiceImplTest extends MockObjectTestCase
{

  /**
   * Constructor to take the name of the test.
   * 
   * @param test
   */
  public RoleServiceImplTest( String test )
  {
    super( test );
  }

  /** RoleServiceImpl */
  private RoleService roleService = new RoleServiceImpl();

  /** mockRoleDAO */
  private Mock mockRoleDAO = null;
  private Mock mockSystemVariableService = null;

  /**
   * Setup this test case. Overridden from
   * 
   * @see junit.framework.TestCase#setUp()
   * @throws Exception
   */
  protected void setUp() throws Exception
  {
    mockRoleDAO = new Mock( RoleDAO.class );
    roleService.setRoleDAO( (RoleDAO)mockRoleDAO.proxy() );

  }

  /**
   * Tests getting a set of Roles.
   */
  public void testGetAll()
  {

    Set expectedRoles = new LinkedHashSet();

    Role role = new Role();
    role.setId( new Long( 23 ) );
    role.setActive( Boolean.valueOf( true ) );
    role.setHelpText( "testHELPTEXT" );
    expectedRoles.add( role );

    Role role2 = new Role();
    role2.setId( new Long( 23 ) );
    role2.setActive( Boolean.valueOf( true ) );
    role2.setHelpText( "testHELPTEXT2" );
    expectedRoles.add( role2 );

    Role role3 = new Role();
    role3.setId( new Long( 23 ) );
    role3.setActive( Boolean.valueOf( true ) );
    role3.setHelpText( "testHELPTEXT3" );
    expectedRoles.add( role3 );

    // RoleDAO expected to call getAll once with nothing which will return the set expected
    mockRoleDAO.expects( once() ).method( "getAll" ).will( returnValue( expectedRoles ) );

    roleService.getAll();

    mockRoleDAO.verify();
  }

  /**
   * Tests saving or updating and fetching a role.
   */
  public void testSaveAndGetRoleById()
  {

    Role role = new Role();
    role.setId( new Long( 23 ) );
    role.setActive( Boolean.valueOf( true ) );
    role.setHelpText( "testHELPTEXT" );

    // Setup the dao with the test Role
    mockRoleDAO.expects( once() ).method( "saveRole" ).with( same( role ) );

    // Test the UserAclService.saveOrUpdateUserAcl
    roleService.saveRole( role );

    // Verify the mockDAO returns the same thing the service is attempting to save.
    mockRoleDAO.verify();

  }

  public void testGetRoleByCode()
  {
    Role expectedRole = new Role();
    expectedRole.setActive( Boolean.valueOf( true ) );
    expectedRole.setHelpText( "testHELPTEXT" );
    expectedRole.setName( "testNAME" );
    expectedRole.setCode( "testCode" );
    mockRoleDAO.expects( once() ).method( "saveRole" ).with( same( expectedRole ) );

    // Test the UserAclService.saveOrUpdateUserAcl
    roleService.saveRole( expectedRole );
    mockRoleDAO.expects( once() ).method( "getRoleByCode" ).with( same( expectedRole.getCode() ) ).will( returnValue( expectedRole ) );
    Role actualRole = roleService.getRoleByCode( expectedRole.getCode() );
    assertTrue( "Actual  matches to what was expected", expectedRole == actualRole );

    mockRoleDAO.verify();
  }

  public void testGetRoleById()
  {
    Role expectedRole = new Role();
    expectedRole.setId( 1L );
    expectedRole.setActive( Boolean.valueOf( true ) );
    expectedRole.setHelpText( "testHELPTEXT" );
    expectedRole.setName( "testNAME" );
    expectedRole.setCode( "testCode" );
    mockRoleDAO.expects( once() ).method( "saveRole" ).with( same( expectedRole ) );

    // Test the UserAclService.saveOrUpdateUserAcl
    roleService.saveRole( expectedRole );
    mockRoleDAO.expects( once() ).method( "getRoleById" ).with( same( expectedRole.getId() ) ).will( returnValue( expectedRole ) );
    Role actualRole = roleService.getRoleById( expectedRole.getId() );
    assertTrue( "Actual  matches to what was expected", expectedRole == actualRole );

    mockRoleDAO.verify();
  }

  /**
   * Tests searching for role or list of roles given specific search criteria.
   */

  public void testSearchRole()

  {

    Role searchRole1 = new Role();
    searchRole1.setActive( Boolean.valueOf( true ) );
    searchRole1.setHelpText( "testSearchRoleABC" );

    Role searchRole2 = new Role();
    searchRole2.setActive( Boolean.valueOf( true ) );
    searchRole2.setHelpText( "testSearchRoleXYZ" );

    Role searchRole3 = new Role();
    searchRole3.setActive( Boolean.valueOf( false ) );
    searchRole3.setHelpText( "testSearchRoleAYZ" );

    Role searchRole4 = new Role();
    searchRole4.setActive( Boolean.valueOf( false ) );
    searchRole4.setHelpText( "testSearchRoleKFC" );

    List expectedSearchResults = new ArrayList();
    expectedSearchResults.add( searchRole1 );
    expectedSearchResults.add( searchRole3 );
    expectedSearchResults.add( searchRole4 );
    expectedSearchResults.add( searchRole2 );

    // Setup the dao with the test Role
    mockRoleDAO.expects( once() ).method( "searchRole" ).will( returnValue( expectedSearchResults ) );

    // Test the UserAclService.saveOrUpdateUserAcl
    roleService.searchRole( "testSearchRole", null );

    // Verify the mockDAO returns the same thing the service is attempting to save.
    mockRoleDAO.verify();

  }

  @Test
  public void testUserRoleBypassingUserIdAndRoleCode() throws ServiceErrorException, SQLException
  {
    mockRoleDAO.expects( once() ).method( "isUserHasRole" ).will( returnValue( false ) );
    boolean isloggedinUserRole = roleService.getUserRoleBypassingUserIdAndRoleCode( 1234L );
    assertEquals( false, isloggedinUserRole );

    mockRoleDAO.expects( once() ).method( "isUserHasRole" ).will( returnValue( true ) );
    boolean isloggedinUser = roleService.getUserRoleBypassingUserIdAndRoleCode( 1234L );
    assertEquals( true, isloggedinUser );

  }

  public void testGetBiAdminUsrIdByRollId()
  {

    List<Long> userIdList = new ArrayList<>();
    userIdList.add( 5662L );
    userIdList.add( 70268L );

    mockRoleDAO.expects( once() ).method( "getBiAdminUserIds" ).with( same( 6L ) ).will( returnValue( userIdList ) );
    roleService.getBiAdminUserIdsByRoleId( 6L );

  }

}
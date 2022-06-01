/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javatest/com/biperf/core/dao/security/hibernate/RoleDAOImplTest.java,v $
 */

package com.biperf.core.dao.security.hibernate;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.sql.DataSource;

import org.springframework.jdbc.datasource.DataSourceUtils;

import com.biperf.core.dao.hibernate.BaseDAOTest;
import com.biperf.core.dao.security.RoleDAO;
import com.biperf.core.domain.enums.UserType;
import com.biperf.core.domain.user.Role;
import com.biperf.core.domain.user.User;
import com.biperf.core.utils.ApplicationContextFactory;

/**
 * RoleDAOImplTest.
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
public class RoleDAOImplTest extends BaseDAOTest
{

  /**
   * Tests saving or updating the user role. This needs to fetch the userRole by Id so it is also
   * testing UserRoleDAO.getUserRoleById(Long id).
   */
  public void testSaveAndGetRoleById()
  {

    RoleDAO roleDAO = getRoleDAO();

    Role expectedRole = new Role();
    expectedRole.setActive( Boolean.valueOf( true ) );
    expectedRole.setHelpText( "testHELPTEXT" );
    expectedRole.setName( "testNAME" );
    expectedRole.setCode( "testCode" );
    expectedRole.addUserTypeCode( UserType.BI );

    roleDAO.saveRole( expectedRole );

    Role actualRole = roleDAO.getRoleById( expectedRole.getId() );

    assertEquals( "Roles not equals", expectedRole, actualRole );

    // Update the Role
    expectedRole.setCode( "testUpdatedCode" );
    expectedRole.setHelpText( "testUpdatedHELPTEXT" );
    expectedRole.setName( "testUPDATEDNAME" );
    expectedRole.setActive( Boolean.valueOf( false ) );
    expectedRole.addUserTypeCode( UserType.BI );

    roleDAO.saveRole( expectedRole );

    // Updated Role from the database
    actualRole = roleDAO.getRoleById( expectedRole.getId() );

    assertEquals( "Updated Roles not equals", expectedRole, actualRole );

  }

  /**
   * Tests getting a role by role code.
   */
  public void testGetRoleByCode()
  {
    RoleDAO roleDAO = getRoleDAO();

    Role expectedRole = new Role();
    expectedRole.setActive( Boolean.valueOf( true ) );
    expectedRole.setHelpText( "testHELPTEXT" );
    expectedRole.setName( "testNAME" );
    expectedRole.setCode( "testCode" );

    expectedRole = roleDAO.saveRole( expectedRole );

    Role actualRole = roleDAO.getRoleByCode( expectedRole.getCode() );

    assertEquals( "Roles not equals", expectedRole, actualRole );
  }

  /**
   * Tests saving several roles and searching using different criteria to get the roles created.
   */

  public void testSearchRole()
  {

    RoleDAO roleDAO = getRoleDAO();

    Role searchRole1 = new Role();
    searchRole1.setActive( Boolean.valueOf( true ) );
    searchRole1.setHelpText( "testSearchRoleABC" );
    searchRole1.setName( "testSearchRoleABC" );
    searchRole1.setCode( "testSearchRoleCodeABC" );
    roleDAO.saveRole( searchRole1 );

    Role searchRole2 = new Role();
    searchRole2.setActive( Boolean.valueOf( true ) );
    searchRole2.setHelpText( "testSearchRoleXYZ" );
    searchRole2.setName( "testSearchRoleXYZ" );
    searchRole2.setCode( "testSearchRoleCodeXYZ" );
    roleDAO.saveRole( searchRole2 );

    Role searchRole3 = new Role();
    searchRole3.setActive( Boolean.valueOf( false ) );
    searchRole3.setHelpText( "testSearchRoleAYZ" );
    searchRole3.setName( "testSearchRoleAYZ" );
    searchRole3.setCode( "testSearchRoleCodeAYZ" );
    roleDAO.saveRole( searchRole3 );

    Role searchRole4 = new Role();
    searchRole4.setActive( Boolean.valueOf( false ) );
    searchRole4.setHelpText( "testSearchRoleKFC" );
    searchRole4.setName( "testSearchRoleKFC" );
    searchRole4.setCode( "testSearchRoleCodeKFC" );
    roleDAO.saveRole( searchRole4 );

    // Search for the roles: This should result in a list of at least 4 roles
    // This is ordered by the description as the search in the DAO defines
    List expectedSearchResults = new ArrayList();
    expectedSearchResults.add( searchRole1 );
    expectedSearchResults.add( searchRole3 );
    expectedSearchResults.add( searchRole4 );
    expectedSearchResults.add( searchRole2 );

    List actualSearchResults = roleDAO.searchRole( "testSearchRole", null );

    assertTrue( "Search results 1 is not the expected length", actualSearchResults.size() >= 4 );
    assertTrue( "Search results 2 doesn't contain expected roles", actualSearchResults.containsAll( expectedSearchResults ) );

    // Clear the expected search results list.
    expectedSearchResults.clear();

    // Search for different criteria
    actualSearchResults = roleDAO.searchRole( "", Boolean.valueOf( true ) );
    expectedSearchResults.add( searchRole1 );
    expectedSearchResults.add( searchRole2 );

    assertTrue( "Search results 2 is not the expected length", actualSearchResults.size() >= 2 );
    assertTrue( "Search results 2 doesn't contain expected roles", actualSearchResults.containsAll( expectedSearchResults ) );

    // Clear the expected search results list.
    expectedSearchResults.clear();

    // Search for different criteria
    actualSearchResults = roleDAO.searchRole( "", Boolean.valueOf( false ) );
    expectedSearchResults.add( searchRole3 );
    expectedSearchResults.add( searchRole4 );

    assertTrue( "Search results 3 is not the expected length", actualSearchResults.size() >= 2 );
    assertTrue( "Search results 3 doesn't contain expected roles", actualSearchResults.containsAll( expectedSearchResults ) );

    // Clear the expected search results list.
    expectedSearchResults.clear();

    // Search for different criteria
    actualSearchResults = roleDAO.searchRole( "Y", null );
    expectedSearchResults.add( searchRole3 );
    expectedSearchResults.add( searchRole2 );

    assertTrue( "Search results 4 is not the expected length", actualSearchResults.size() >= 2 );
    assertTrue( "Search results 4 doesn't contain expected roles", actualSearchResults.containsAll( expectedSearchResults ) );

    // Clear the expected search results list.
    expectedSearchResults.clear();

    // Search for different criteria
    actualSearchResults = roleDAO.searchRole( "Doesn'tExist", null );

    assertTrue( "Search results 4 is not the expected length", actualSearchResults.size() == 0 );

  }

  /**
   * Tests saving and getting all the roles saved.
   */
  public void testGetAll()
  {

    RoleDAO roleDAO = getRoleDAO();

    // get the count of existing roles
    Connection conn = null;
    Statement stmt = null;
    ResultSet rs = null;
    DataSource dataSource = (DataSource)ApplicationContextFactory.getApplicationContext().getBean( "dataSource" );
    int count = 0;
    try
    {
      conn = DataSourceUtils.getConnection( dataSource );
      stmt = conn.createStatement();
      rs = stmt.executeQuery( "select count(*) from role" );
      if ( rs.next() )
      {
        count = rs.getInt( 1 );
      }
    }
    catch( SQLException e )
    {
      fail( "SQLException getting role count" );
    }
    finally
    {
      try
      {
        if ( rs != null )
        {
          rs.close();
        }

        if ( stmt != null )
        {
          stmt.close();
        }
      }
      catch( SQLException e )
      {
        // ignore
      }
      DataSourceUtils.releaseConnection( conn, dataSource );
    }

    Set expectedRoles = new LinkedHashSet();

    Role expectedRole1 = new Role();
    expectedRole1.setActive( Boolean.valueOf( true ) );
    expectedRole1.setHelpText( "testHELPTEXT" );
    expectedRole1.setName( "testNAME" );
    expectedRole1.setCode( "testCode" );
    expectedRoles.add( expectedRole1 );
    roleDAO.saveRole( expectedRole1 );

    Role expectedRole2 = new Role();
    expectedRole2.setActive( Boolean.valueOf( true ) );
    expectedRole2.setHelpText( "testHELPTEXT2" );
    expectedRole2.setName( "testNAME2" );
    expectedRole2.setCode( "testCode2" );
    expectedRoles.add( expectedRole2 );
    roleDAO.saveRole( expectedRole2 );

    Role expectedRole3 = new Role();
    expectedRole3.setActive( Boolean.valueOf( true ) );
    expectedRole3.setHelpText( "testHELPTEXT3" );
    expectedRole3.setName( "testNAME3" );
    expectedRole3.setCode( "testCode3" );
    expectedRoles.add( expectedRole3 );
    roleDAO.saveRole( expectedRole3 );

    Set actualRoles = roleDAO.getAll();

    assertEquals( "Set of Roles aren't the same size.", expectedRoles.size() + count, actualRoles.size() );

  }

  /**
   * Get the RoleDAO.
   * 
   * @return RoleDAO
   */
  private RoleDAO getRoleDAO()
  {
    return (RoleDAO)ApplicationContextFactory.getApplicationContext().getBean( "roleDAO" );
  }

  public void testIsUserInrole()
  {

    User user = new User();
    user.setId( 85421L );
    Role role = new Role();
    role.setCode( "PLATEAU_REDEMPTION" );
    boolean hasRole = getRoleDAO().isUserHasRole( user.getId(), role.getCode() );
    assertNotSame( true, hasRole );
  }

  public void testIsRoleBiwOnly()
  {
    Role expectedRole = new Role();
    expectedRole.setActive( Boolean.valueOf( true ) );
    expectedRole.setHelpText( "TESTHELPERTEXT" );
    expectedRole.setName( "TESTNAME" );
    expectedRole.setCode( "TESTCODE" );
    Role role = getRoleDAO().saveRole( expectedRole );

    assertNotNull( role );

    boolean roleExisted = getRoleDAO().isRoleBiwOnly( role.getCode() );
    assertNotNull( roleExisted );
    assertEquals( false, roleExisted );

  }
}

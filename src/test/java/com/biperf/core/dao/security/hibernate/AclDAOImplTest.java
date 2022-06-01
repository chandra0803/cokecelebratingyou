/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javatest/com/biperf/core/dao/security/hibernate/AclDAOImplTest.java,v $
 */

package com.biperf.core.dao.security.hibernate;

import java.util.ArrayList;
import java.util.List;

import com.biperf.core.dao.hibernate.BaseDAOTest;
import com.biperf.core.dao.security.AclDAO;
import com.biperf.core.domain.user.Acl;
import com.biperf.core.utils.ApplicationContextFactory;

/**
 * AclDAOImplTest.
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
 * <td>Mar 31, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class AclDAOImplTest extends BaseDAOTest
{

  /**
   * Uses the ApplicationContextFactory to look up the correct DAO implementation
   * 
   * @return UserAclDAO
   */
  protected AclDAO getAclDAO()
  {
    return (AclDAO)ApplicationContextFactory.getApplicationContext().getBean( "aclDAO" );
  }

  /**
   * Tests getting all acls saved in the database.
   */
  public void testGetAll()
  {
    AclDAO aclDAO = getAclDAO();

    List expectedAclList = new ArrayList();

    Acl searchAcl1 = new Acl();
    searchAcl1.setName( "searchNameABC" );
    searchAcl1.setCode( "searchCodeABC" );
    searchAcl1.setHelpText( "searchHELPTEXTABC" );
    searchAcl1.setClassName( "searchCLASSNAMEABC" );
    aclDAO.saveAcl( searchAcl1 );

    Acl searchAcl2 = new Acl();
    searchAcl2.setName( "searchNameXYZ" );
    searchAcl2.setCode( "searchCodeXYZ" );
    searchAcl2.setHelpText( "searchHELPTEXTXYZ" );
    searchAcl2.setClassName( "searchCLASSNAMEXYZ" );
    aclDAO.saveAcl( searchAcl2 );

    Acl searchAcl3 = new Acl();
    searchAcl3.setName( "searchNameXAK" );
    searchAcl3.setCode( "searchCodeXAK" );
    searchAcl3.setHelpText( "searchHELPTEXTXAK" );
    searchAcl3.setClassName( "searchCLASSNAMEXAK" );
    aclDAO.saveAcl( searchAcl3 );

    expectedAclList.add( searchAcl1 );
    expectedAclList.add( searchAcl3 );
    expectedAclList.add( searchAcl2 );

    assertTrue( "Actual list of all acls didn't contain expected list", aclDAO.getAll().containsAll( expectedAclList ) );

  }

  /**
   * Test fetching the Acl.
   */
  public void testSaveAndGetAclById()
  {

    AclDAO aclDAO = getAclDAO();

    Acl expectedAcl = new Acl();
    expectedAcl.setName( "TestName" );
    expectedAcl.setCode( "TestCODE" );
    expectedAcl.setHelpText( "TestDescription" );
    expectedAcl.setClassName( "TestApp" );

    aclDAO.saveAcl( expectedAcl );

    Acl actualAcl = aclDAO.getAclById( expectedAcl.getId() );

    assertEquals( "Acls not equals", expectedAcl, actualAcl );

    // Update the expectedUserAcl and compare what is in the database.
    expectedAcl.setCode( "UpdatedCode" );
    expectedAcl.setName( "UpdatedName" );
    expectedAcl.setHelpText( "UpdatedCategory" );

    aclDAO.saveAcl( expectedAcl );
    actualAcl = aclDAO.getAclById( expectedAcl.getId() );

    assertEquals( "Expected Updated Acl wasn't equals to actual", expectedAcl, actualAcl );

  }

  /**
   * Tests searching for an Acl with specific criteria.
   */
  public void testSearchAcl()
  {

    AclDAO aclDAO = getAclDAO();

    List expectedAclList = new ArrayList();

    Acl searchAcl1 = new Acl();
    searchAcl1.setName( "searchNameABC" );
    searchAcl1.setCode( "searchCodeABC" );
    searchAcl1.setHelpText( "searchHELPABC" );
    searchAcl1.setClassName( "searchCLASSNAMEABC" );
    aclDAO.saveAcl( searchAcl1 );

    Acl searchAcl2 = new Acl();
    searchAcl2.setName( "searchNameXYZ" );
    searchAcl2.setCode( "searchCodeXYZ" );
    searchAcl2.setHelpText( "searchHELPXYZ" );
    searchAcl2.setClassName( "searchCLASSNAMEXYZ" );
    aclDAO.saveAcl( searchAcl2 );

    Acl searchAcl3 = new Acl();
    searchAcl3.setName( "searchNameXAK" );
    searchAcl3.setCode( "searchCodeXAK" );
    searchAcl3.setHelpText( "searchHELPXAK" );
    searchAcl3.setClassName( "searchCLASSNAMEXAK" );
    aclDAO.saveAcl( searchAcl3 );

    expectedAclList.add( searchAcl1 );
    expectedAclList.add( searchAcl3 );
    expectedAclList.add( searchAcl2 );

    assertEquals( "Actual acl list from search was what was expected: ", expectedAclList, aclDAO.searchAcl( "search", "", "" ) );

    expectedAclList.clear();

    expectedAclList.add( searchAcl3 );
    expectedAclList.add( searchAcl2 );

    List actualAclList = aclDAO.searchAcl( "search", "X", "" );
    assertEquals( "Actual acl list from search was what was expected: ", expectedAclList, actualAclList );

    expectedAclList.clear();

    expectedAclList.add( searchAcl3 );

    assertEquals( "Actual acl list from search was what was expected: ", expectedAclList, aclDAO.searchAcl( "search", "X", "K" ) );

    expectedAclList.clear();

    expectedAclList.add( searchAcl3 );

    assertEquals( "Actual acl list from search was what was expected: ", expectedAclList, aclDAO.searchAcl( null, null, "K" ) );
  }

}
/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javatest/com/biperf/core/service/security/impl/AuthorizationServiceImplTest.java,v $
 */

package com.biperf.core.service.security.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import com.biperf.core.service.BaseServiceTest;
import com.biperf.core.service.security.AuthorizationService;

/**
 * AuthorizationServiceImplTest tests the AuthorizationServiceImpl.
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
 * <td>Brian Repko</td>
 * <td>Sep 29, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class AuthorizationServiceImplTest extends BaseServiceTest
{
  private AuthorizationService aznService = new AuthorizationServiceImpl();

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.BaseServiceTest#setUp()
   * @throws Exception
   */
  protected void setUp() throws Exception
  {
    super.setUp();
    //
    // Setup the SecureContext, Authentication and GrantedAuthorities
    //

    // GrantedAuthority[] roles = new GrantedAuthority[] { new GrantedAuthorityImpl( "ROLE_TEST" )
    // };
    // TODO may be we can use following when we setup acls test code
    // GrantedAuthority[] rolesAndacls = new GrantedAuthority[] { new GrantedAuthorityImpl(
    // "ROLE_TEST" ),new GrantedAuthorityAclEntryImpl( "ACL_TEST", aclEntry ) };

    List<GrantedAuthority> grantedAuthority = new ArrayList<GrantedAuthority>();
    SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority( "ROLE_TEST" );
    grantedAuthority.add( simpleGrantedAuthority );

    Authentication auth = new UsernamePasswordAuthenticationToken( "test", "test", grantedAuthority );

    /*
     * avoid redeployments issues by clearing the context instead of creating a new one
     */
    SecurityContextHolder.clearContext();
    SecurityContextHolder.getContext().setAuthentication( auth );

    // TODO put the ACLs related setup code, need to mock AclEntry and test three conditions
    // Don't have ACLs, Have ACLs but AclEntry retuns false, have ACLs and AclEntry returns True
    // AclEntry aclEntry = null;
    // GrantedAuthority[] acls = new GrantedAuthority[] { new GrantedAuthorityAclEntryImpl(
    // "ACL_TEST", aclEntry ) };
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.BaseServiceTest#tearDown()
   * @throws Exception
   */
  protected void tearDown() throws Exception
  {
    //
    // Nullify the SecureContext
    //

    /*
     * avoid redeployments issues by clearing the context instead of creating a new one
     */
    SecurityContextHolder.clearContext();

    super.tearDown();
  }

  public void testUserInRole() throws Exception
  {
    assertTrue( aznService.isUserInRole( "TEST" ) );
  }

  public void testUserNotInRole() throws Exception
  {
    assertFalse( aznService.isUserInRole( "NOWAY" ) );
  }

  /**
   * @throws Exception
   */
  public void testUserInRoleSatisfyAllSets() throws Exception
  {
    Set setAll = new HashSet();
    setAll.add( "TEST" );
    Set setAny = new HashSet();
    setAny.add( "TEST" );
    setAny.add( "ANY" );
    Set setNone = new HashSet();
    setNone.add( "NONE" );
    assertTrue( aznService.isUserInRole( setAll, setAny, setNone ) );
  }

  /**
   * User will be in role with All set satisfies the condition with Any and None sets empty but
   * satisfy condition
   * 
   * @throws Exception
   */
  public void testUserInRoleAllEmptySatisfyAnyAndNone() throws Exception
  {
    Set setAll = new HashSet();
    setAll.add( "TEST" );
    Set setAny = new HashSet();
    Set setNone = new HashSet();
    assertTrue( aznService.isUserInRole( setAll, setAny, setNone ) );
  }

  /**
   * User will be in role with Any set satisfies the condition with All and None sets empty but
   * satisfy condition
   * 
   * @throws Exception
   */
  public void testUserInRoleAnyEmptySatisfyAllAndNone() throws Exception
  {
    Set setAll = new HashSet();
    Set setAny = new HashSet();
    setAny.add( "TEST" );
    Set setNone = new HashSet();
    assertTrue( aznService.isUserInRole( setAll, setAny, setNone ) );
  }

  /**
   * User will be in role with None set satisfies the condition with All and Any sets empty but
   * satisfy condition
   * 
   * @throws Exception
   */
  public void testUserInRoleNoneEmptySatisfyAllAndAny() throws Exception
  {
    Set setAll = new HashSet();
    Set setAny = new HashSet();
    Set setNone = new HashSet();
    setNone.add( "ROLE_NONE" );
    assertTrue( aznService.isUserInRole( setAll, setAny, setNone ) );
  }

  /**
   * User will not be in role with all sets null
   * 
   * @throws Exception
   */
  public void testUserNotInRoleAllNullSets() throws Exception
  {
    assertFalse( aznService.isUserInRole( null, null, null ) );
  }

  /**
   * User will not be in role with all sets empty
   * 
   * @throws Exception
   */
  public void testUserNotInRoleAllEmptySets() throws Exception
  {
    Set setAll = new HashSet();
    Set setAny = new HashSet();
    Set setNone = new HashSet();

    assertFalse( aznService.isUserInRole( setAll, setAny, setNone ) );
  }

  /*************************************************************************************************
   * User will not be in role with all sets having elements but not satisfy roles
   * 
   * @throws Exception
   */
  public void testUserNotInRoleNotSatisfyAllSets() throws Exception
  {
    Set setAll = new HashSet();
    setAll.add( "ROLE_ALL" );
    Set setAny = new HashSet();
    setAny.add( "ROLE_ANY" );
    Set setNone = new HashSet();
    setNone.add( "ROLE_TEST" );
    assertFalse( aznService.isUserInRole( setAll, setAny, setNone ) );
  }

  /**
   * User will not be in role with all sets having elemnts and Any and None sets only satisfy the
   * conditions.
   * 
   * @throws Exception
   */
  public void testUserNotInRoleNotSatisfyAll() throws Exception
  {
    Set setAll = new HashSet();
    setAll.add( "ROLE_ALL" );
    Set setAny = new HashSet();
    setAny.add( "ROLE_TEST" );
    setAny.add( "ROLE_ANY" );
    Set setNone = new HashSet();
    setNone.add( "ROLE_NONE" );
    assertFalse( aznService.isUserInRole( setAll, setAny, setNone ) );
  }

  /**
   * User will not be in role with all sets having elemnts and All and None sets only satisfy the
   * conditions.
   * 
   * @throws Exception
   */
  public void testUserNotInRoleNotSatisfyAny() throws Exception
  {
    Set setAll = new HashSet();
    setAll.add( "ROLE_TEST" );
    Set setAny = new HashSet();
    setAny.add( "ROLE_ANY1" );
    setAny.add( "ROLE_ANY2" );
    Set setNone = new HashSet();
    setNone.add( "ROLE_NONE" );
    assertFalse( aznService.isUserInRole( setAll, setAny, setNone ) );
  }

  /**
   * User will not be in role with all sets having elemnts and All and Any set only satisfy the
   * conditions.
   * 
   * @throws Exception
   */
  public void testUserNotInRoleNotSatisfyNone() throws Exception
  {
    Set setAll = new HashSet();
    setAll.add( "ROLE_TEST" );
    Set setAny = new HashSet();
    setAny.add( "ROLE_TEST" );
    setAny.add( "ROLE_ANY" );
    Set setNone = new HashSet();
    setNone.add( "ROLE_TEST" );
    assertFalse( aznService.isUserInRole( setAll, setAny, setNone ) );
  }

  // TODO put the ACLs related setup code, need to mock AclEntry and test the following three
  // conditions
  // Don't have ACLs, Have ACLs but AclEntry retuns false, have ACLs and AclEntry returns True
  /*
   * public void testNoACL() throws Exception { assertFalse( aznService.hasPermission("ACL_NOT", 1,
   * "TEST" ) ); } public void testHaveACLsAclEntryFalse() throws Exception { assertFalse(
   * aznService.hasPermission("ACL_NOT", 1, "TEST" ) ); } public void testHaveACLsAclEntryTrue()
   * throws Exception { assertTrue( aznService.hasPermission("ACL_NOT", 1, "TEST" ) ); }
   */
}

/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/security/AuthorizationService.java,v $
 */

package com.biperf.core.service.security;

import java.util.Set;

import com.biperf.core.security.acl.AclEntry;
import com.biperf.core.service.SAO;

/**
 * AuthorizationService is the main service for determining if a user has a role or a permission.
 * This is the single-point where access decisions are determined.
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
 * <td>Sep 28, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public interface AuthorizationService extends SAO
{
  public static final String BEAN_NAME = "authorizationService";

  //
  // Constants for roles
  //
  public final String ROLE_CODE_PROJ_MGR = "PROJ_MGR";
  public final String ROLE_CODE_PROCESS_TEAM = "PROCESS_TEAM";
  public final String ROLE_CODE_BI_ADMIN = "BI_ADMIN";
  public final String ROLE_CODE_CONTENT_ADMINISTRATOR = "CONTENT_ADMINISTRATOR";

  // Functional roles
  public final String ROLE_CODE_VIEW_REPORTS = "VIEW_REPORTS";
  public final String ROLE_CODE_VIEW_PARTICIPANTS = "VIEW_PARTICIPANTS";
  public final String ROLE_CODE_LAUNCH_AS_PAX = "LAUNCH_AS_PAX";
  public final String ROLE_CODE_UNLOCK_LOGIN = "UNLOCK_LOGIN";
  public final String ROLE_CODE_MODIFY_PROXIES = "MODIFY_PROXIES";
  public final String ROLE_CODE_VIEW_PLATEAU_AWARD = "PLATEAU_REDEMPTION";
  public final String ROLE_CODE_MODIFY_RECOVERY_CONTACTS = "MODIFY_RECOVERY_CONTACTS";

  // Hidden roles
  //
  public final String ROLE_CODE_AUTHENTICATED = "AUTHENTICATED";
  public final String ROLE_CODE_USER = "USER";
  public final String ROLE_CODE_PAX = "PARTICIPANT";
  public final String ROLE_CODE_BI_USER = "BI_USER";
  public final String ROLE_CODE_LOGIN_AS = "LOGIN_AS";
  public final String ROLE_MANAGER = "MANAGER";

  /**
   * isUserInRole is a quick determination of user having the specified role.
   * 
   * @param roleCode of the role. This does not include the "ROLE_" used by ACEGI
   * @return boolean value for has it or not
   */
  boolean isUserInRole( String roleCode );

  // TODO add a role method that matches the ACEGI authorize tag (all/any/none)

  /**
   * isUserInRole is a quick determination of user having the specified role.
   * 
   * @param setAllRoles set of All roles
   * @param setAnyRoles set of Any roles
   * @param setNoneRoles set of None roles
   * @return boolean value for has it or not
   */
  public boolean isUserInRole( Set setAllRoles, Set setAnyRoles, Set setNoneRoles );

  /**
   * hasPermission is a determination of user having a specified ACL permission. The permissions are
   * actually a bitmap integer value representing any of the permissions that you want to check for
   * (OR-logic)
   * 
   * @param aclCode
   * @param permissions an int representing the bitmap of any of the permissions to look for
   * @param objectToTest
   * @return boolean value for has it or not
   */
  boolean hasPermission( String aclCode, int permissions, Object objectToTest );

  /**
   * getAclEntry return
   */
  AclEntry getAclEntry( String aclCode );
}

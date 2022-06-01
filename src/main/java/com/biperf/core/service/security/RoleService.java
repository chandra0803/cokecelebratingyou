/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/security/RoleService.java,v $
 */

package com.biperf.core.service.security;

import java.util.List;
import java.util.Set;

import com.biperf.core.dao.security.RoleDAO;
import com.biperf.core.dao.security.hibernate.RoleQueryConstraint;
import com.biperf.core.domain.user.Role;
import com.biperf.core.service.SAO;

/**
 * RoleService.
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
public interface RoleService extends SAO
{
  /** BEAN_NAME */
  public static final String BEAN_NAME = "roleService";

  /**
   * Get all roles in the database.
   * 
   * @return Set
   */
  public Set<Role> getAll();

  /**
   * Saves or updates the role to the database.
   * 
   * @param role
   * @return Role
   */
  public Role saveRole( Role role );

  /**
   * Get the role by id.
   * 
   * @param roleId
   * @return Role
   */
  public Role getRoleById( Long roleId );

  /**
   * Get the role by code.
   * 
   * @param roleCode
   * @return Role
   */
  public Role getRoleByCode( String roleCode );

  /**
   * Returns the specified roles.
   *
   * @param queryConstraint  specifies which roles are returned and in what order.
   * @return the specified roles, as a <code>List</code> of {@link Role} objects.
   */
  public List<Role> getRoleList( RoleQueryConstraint queryConstraint );

  /**
   * Search for the role with the given search params.
   * 
   * @param isActive
   * @param description
   * @return List
   */
  public List searchRole( String description, Boolean isActive );

  // Security Patch 3 - start
  /**
   * isUserHasRole
   * Check if the user is assigned to role
   *   
   * @param userId
   * @param roleCode
   * @return booolen - true if already has the role, false if they do not
   */
  public boolean isUserHasRole( Long userId, String roleCode );

  /**
   * getNbrUsersWithRoleCount
   * Return the number of users assigned to specific role
   * 
   * @return int - count of the number of users with the Reissue Send Password
   */
  // public int getNbrUsersWithRoleCount( String roleCode );

  /**
   * checkCanAssignReissueSendPwdRole
   * Check if it is ok to assign the user to the specific role REISSUE_SEND_PASSWORD
   *   
   * @param userId
   * @return boolean - true if ok to be assigned to the role
   */
  // public boolean checkCanAssignReissueSendPwdRole( Long userId ) throws ServiceErrorException;

  // Security Patch 3 - end

  /**
   * Set the RoleDAO through IoC.
   * 
   * @param roleDAO
   */
  public void setRoleDAO( RoleDAO roleDAO );

  // public List<Role> getRolesById( List<Long> assignedRolesLong );

  public boolean isRoleBiwOnly( Role role );

  public boolean getUserRoleBypassingUserIdAndRoleCode( Long loginUserId );

  /**
   * getBiAdminUsrIdByRollId
   * Get all the user ids which belongs to bi admin role id
   * 
   * @return List - All User ID belongs to BI Admin Role ID
   */

  public List<Long> getBiAdminUserIdsByRoleId( Long biAdminRlId );

}

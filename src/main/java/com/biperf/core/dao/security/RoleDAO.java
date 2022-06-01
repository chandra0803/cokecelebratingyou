/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/dao/security/RoleDAO.java,v $
 */

package com.biperf.core.dao.security;

import java.util.List;
import java.util.Set;

import com.biperf.core.dao.DAO;
import com.biperf.core.dao.security.hibernate.RoleQueryConstraint;
import com.biperf.core.domain.user.Role;

/**
 * RoleDAO.
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
 */
public interface RoleDAO extends DAO
{

  /**
   * Get the role by id.
   * 
   * @param id
   * @return Role
   */
  public abstract Role getRoleById( Long id );

  /**
   * Get the role by code.
   * 
   * @param code
   * @return Role
   */
  public abstract Role getRoleByCode( String code );

  /**
   * Returns the specified roles.
   *
   * @param queryConstraint  specifies which roles are returned and in what order.
   * @return the specified roles, as a <code>List</code> of {@link Role} objects.
   */
  public abstract List<Role> getRoleList( RoleQueryConstraint queryConstraint );

  /**
   * Get All roles.
   * 
   * @return Set
   */
  public abstract Set<Role> getAll();

  /**
   * Save or update the role.
   * 
   * @param role
   * @return Role
   */
  public abstract Role saveRole( Role role );

  /**
   * Search for the role with the given search params.
   * 
   * @param isActive
   * @param description
   * @return List
   */
  public List searchRole( String description, Boolean isActive );

  /**
   * Check the role by roldeCode Param.
   * @param roleCode
   * @return boolean
   */
  public boolean isRoleBiwOnly( String roleCode );

  public boolean isUserHasRole( Long loginUserId, String roleCode );

  // Security Patch 3 - start
  /**
   * getNbrUsersWithRoleCount
   * Check the number of users with a specific role
   * 
   * @param roleCode
   * @return int - number of users with a specific role
   */
  // public int getNbrUsersWithRoleCount( String code );

  /**
   * isUserHasRole
   *  Check if a user has a specific role
   * 
   * @param userId
   * @param roleCode
   * @return boolean - true if the user has the role, false if the user does not have the role
   */

  // Security Patch 3 - end

  // public abstract List<Role> getRolesById( List<Long> list );

  /**
   * getBiAdminUsrId
   *  Get all the user id which belongs to bi admin role id
   * 
   * @param biAdminroleId  
   * @return List - All the users belogns to bi admin role id
   */

  public List<Long> getBiAdminUserIds( Long biAdminroleId );
}

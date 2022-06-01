/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/security/impl/RoleServiceImpl.java,v $
 */

package com.biperf.core.service.security.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import com.biperf.core.dao.security.RoleDAO;
import com.biperf.core.dao.security.hibernate.RoleQueryConstraint;
import com.biperf.core.domain.user.Role;
import com.biperf.core.service.security.AuthorizationService;
import com.biperf.core.service.security.RoleService;
import com.biperf.core.service.system.SystemVariableService;

/**
 * RoleServiceImpl.
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
public class RoleServiceImpl implements RoleService
{

  /** RoleDAO */
  private RoleDAO roleDAO;
  private SystemVariableService systemVariableService;

  /**
   * Set the RoleDAO through IoC.
   * 
   * @param roleDAO
   */
  public void setRoleDAO( RoleDAO roleDAO )
  {
    this.roleDAO = roleDAO;
  }

  /**
   * Gets a list of all roles in the database. Overridden from
   * 
   * @see com.biperf.core.service.security.RoleService#getAll()
   * @return Set
   */
  public Set<Role> getAll()
  {
    return roleDAO.getAll();
  }

  /**
   * Saves or updates the role. Overridden from
   * 
   * @see com.biperf.core.service.security.RoleService#saveRole(Role role)
   * @param role
   * @return Role
   */
  public Role saveRole( Role role )
  {

    this.roleDAO.saveRole( role );

    return role;
  }

  /**
   * Gets the role by the id.
   * 
   * @param roleId
   * @see com.biperf.core.service.security.RoleService#getRoleById(Long roleId)
   * @return Role
   */
  public Role getRoleById( Long roleId )
  {
    return roleDAO.getRoleById( roleId );
  }

  /**
   * Get the role by code.
   * 
   * @param roleCode
   * @return Role
   */
  public Role getRoleByCode( String roleCode )
  {
    return roleDAO.getRoleByCode( roleCode );
  }

  /**
   * Returns the specified roles.
   *
   * @param queryConstraint  specifies which roles are returned and in what order.
   * @return the specified roles, as a <code>List</code> of {@link Role} objects.
   */
  public List<Role> getRoleList( RoleQueryConstraint queryConstraint )
  {
    return roleDAO.getRoleList( queryConstraint );
  }

  /**
   * Search for the role with the given search params.
   * 
   * @param isActive
   * @param description
   * @return Role
   */
  public List searchRole( String description, Boolean isActive )
  {
    return roleDAO.searchRole( description, isActive );
  }

  /**
   * isUserHasRole
   * Check if the user is assigned to role
   *   
   * @param userId
   * @param roleCode
   * @return booolen - true if already has the role, false if they do not
   */
  public boolean isUserHasRole( Long userId, String roleCode )
  {
    return roleDAO.isUserHasRole( userId, roleCode );
  }

  public SystemVariableService getSystemVariableService()
  {
    return systemVariableService;
  }

  public boolean isRoleBiwOnly( Role role )
  {
    String rolesForBiwOnly = systemVariableService.getPropertyByName( SystemVariableService.ROLES_FOR_BIW_ONLY ).getStringVal();
    boolean isRoleBiwOnly = false;
    if ( null != rolesForBiwOnly && !rolesForBiwOnly.isEmpty() )
    {
      List<String> biwUserRoleList = new ArrayList<String>( Arrays.asList( rolesForBiwOnly.split( "," ) ) );
      isRoleBiwOnly = biwUserRoleList.stream().filter( e -> role.getCode().equals( e ) ).findFirst().isPresent();
      if ( isRoleBiwOnly )
      {
        return roleDAO.isRoleBiwOnly( role.getCode() );
      }

    }
    return isRoleBiwOnly;
  }

  public void setSystemVariableService( SystemVariableService systemVariableService )
  {
    this.systemVariableService = systemVariableService;
  }

  public boolean getUserRoleBypassingUserIdAndRoleCode( Long loginUserId )
  {
    return roleDAO.isUserHasRole( loginUserId, AuthorizationService.ROLE_CODE_VIEW_PLATEAU_AWARD );
  }

  /**
   * getBiAdminUsrIdByRollId
   *  Get all the user ids which belongs to bi admin role id
   * 
   * @return List - All User ID belongs to BI Admin Role ID
   */

  public List<Long> getBiAdminUserIdsByRoleId( Long biAdminRlId )
  {
    return roleDAO.getBiAdminUserIds( biAdminRlId );
  }

}

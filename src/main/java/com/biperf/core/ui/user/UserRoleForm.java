/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javaui/com/biperf/core/ui/user/UserRoleForm.java,v $
 */

package com.biperf.core.ui.user;

import com.biperf.core.domain.enums.UserType;
import com.biperf.core.domain.user.User;
import com.biperf.core.ui.BaseActionForm;

/**
 * UserRole ActionForm transfer object.
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
 * <td>sedey</td>
 * <td>Apr 20, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class UserRoleForm extends BaseActionForm
{
  /** User ID */
  private Long userId;
  private String userType;

  private String method;

  /** availableRoles Values */
  private String[] availableRoles;

  /** assignedRoles Values */
  private String[] assignedRoles;

  /**
   * Load User Role
   * 
   * @param user
   */
  public void load( User user )
  {
    this.userId = user.getId();
    this.userType = user.getUserType().getCode();
  }

  /**
   * Sets the propertySetItem in the Domain Object
   * 
   * @return user
   */
  public User toDomainObject()
  {

    User user = new User();
    user.setId( this.userId );
    user.setUserType( UserType.lookup( this.userType ) );
    return user;
  }

  /**
   * Get the userId
   * 
   * @return userId
   */
  public Long getUserId()
  {
    return userId;
  }

  /**
   * Set the userId
   * 
   * @param userId
   */
  public void setUserId( Long userId )
  {
    this.userId = userId;
  }

  /**
   * Get the method
   * 
   * @return method
   */
  public String getMethod()
  {
    return method;
  }

  /**
   * Set the method
   * 
   * @param method
   */
  public void setMethod( String method )
  {
    this.method = method;
  }

  /**
   * @return value of availableRoles property
   */
  public String[] getAvailableRoles()
  {
    return availableRoles;
  }

  /**
   * @param availableRoles value for availableRoles property
   */
  public void setAvailableRoles( String[] availableRoles )
  {
    this.availableRoles = availableRoles;
  }

  /**
   * @return value of assignedRoles property
   */
  public String[] getAssignedRoles()
  {
    return assignedRoles;
  }

  /**
   * @param assignedRoles value for assignedRoles property
   */
  public void setAssignedRoles( String[] assignedRoles )
  {
    this.assignedRoles = assignedRoles;
  }

  public String getUserType()
  {
    return userType;
  }

  public void setUserType( String userType )
  {
    this.userType = userType;
  }

}

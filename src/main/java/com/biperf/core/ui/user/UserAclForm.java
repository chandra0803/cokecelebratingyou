/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javaui/com/biperf/core/ui/user/UserAclForm.java,v $
 */

package com.biperf.core.ui.user;

import com.biperf.core.domain.user.Acl;
import com.biperf.core.domain.user.User;
import com.biperf.core.domain.user.UserAcl;
import com.biperf.core.ui.BaseActionForm;

/**
 * Form to hold information for relating users to Acls.
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
 * <td>Apr 18, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class UserAclForm extends BaseActionForm
{

  /** form name constant */
  public static final String FORM_NAME = "userAclForm";

  /** User ID */
  private Long userId;

  /** method */
  private String method;

  /** aclId */
  private String aclId;

  /** guid */
  private String guid;

  /** aclTarget */
  private String aclTarget;

  /** aclPermission */
  private String aclPermission;

  /** update */
  private boolean update = false;

  /** helpText */
  private String helpText;

  public boolean getUpdate()
  {
    return this.update;
  }

  public void setUpdate( boolean update )
  {
    this.update = update;
  }

  public boolean isUpdate()
  {
    return this.update;
  }

  /**
   * Load User Role
   * 
   * @param userAcl
   */
  public void load( UserAcl userAcl )
  {
    this.userId = userAcl.getUser().getId();
    if ( userAcl.getAcl() != null )
    {
      this.aclId = userAcl.getAcl().getId().toString();
      this.helpText = userAcl.getAcl().getHelpText();
    }

    this.aclTarget = userAcl.getTarget();
    this.aclPermission = userAcl.getPermission();
    this.guid = userAcl.getGuid();
  }

  /**
   * Sets the propertySetItem in the Domain Object
   * 
   * @return user
   */
  public UserAcl toDomainObject()
  {

    User user = new User();
    user.setId( this.userId );

    Acl acl = new Acl();
    acl.setId( new Long( this.aclId ) );

    UserAcl userAcl = new UserAcl();
    userAcl.setGuid( this.guid );
    userAcl.setUser( user );
    userAcl.setAcl( acl );
    userAcl.setTarget( this.aclTarget );
    userAcl.setPermission( this.aclPermission );

    return userAcl;

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
   * @return value of aclId property
   */
  public String getAclId()
  {
    return aclId;
  }

  /**
   * @param aclId
   */
  public void setAclId( String aclId )
  {
    this.aclId = aclId;
  }

  /**
   * @return value of guid property
   */
  public String getGuid()
  {
    return guid;
  }

  /**
   * @param guid
   */
  public void setGuid( String guid )
  {
    this.guid = guid;
  }

  /**
   * @return String
   */
  public String getAclTarget()
  {
    return aclTarget;
  }

  /**
   * @param aclTarget
   */
  public void setAclTarget( String aclTarget )
  {
    this.aclTarget = aclTarget;
  }

  /**
   * @return String
   */
  public String getAclPermission()
  {
    return aclPermission;
  }

  /**
   * @param aclPermission
   */
  public void setAclPermission( String aclPermission )
  {
    this.aclPermission = aclPermission;
  }

  public String getHelpText()
  {
    return helpText;
  }

  public void setHelpText( String helpText )
  {
    this.helpText = helpText;
  }

}

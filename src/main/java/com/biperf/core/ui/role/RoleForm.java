/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javaui/com/biperf/core/ui/role/RoleForm.java,v $
 */

package com.biperf.core.ui.role;

import java.sql.Timestamp;
import java.util.Iterator;
import java.util.Set;

import com.biperf.core.domain.user.Role;
import com.biperf.core.ui.BaseForm;

/**
 * RoleForm to collect information for a Role.
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
 * <td>Apr 12, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class RoleForm extends BaseForm
{

  /** Static access to form name. */
  public static final String FORM_NAME = "roleForm";

  /** helpText */
  private String helpText = "";

  /** Code */
  private String code = "";

  /** Name */
  private String name = "";

  /** method */
  private String method;

  /** active */
  private String active = "";

  /** activityStatus */
  private String activityStatus = "3";

  /** version */
  private String version;

  /** Comment for <code>createdBy</code> */
  private String createdBy;

  /** Comment for <code>dateCreated</code> */
  private String dateCreated;

  /** id */
  private String id;

  /** updated */
  private boolean update;

  private String[] selectedUserTypes = {};

  /**
   * Load the form with the domain object value;
   * 
   * @param role
   */
  public void load( Role role )
  {

    this.helpText = role.getHelpText();
    this.code = role.getCode();
    this.name = role.getName();
    this.active = String.valueOf( role.getActive().booleanValue() );

    Set userTypeCodes = role.getUserTypeCodes();
    if ( userTypeCodes != null )
    {
      selectedUserTypes = new String[userTypeCodes.size()];

      int i = 0;
      for ( Iterator iter = userTypeCodes.iterator(); iter.hasNext(); )
      {
        selectedUserTypes[i++] = (String)iter.next();
      }
    }

    if ( role.getId() != null )
    {
      this.id = String.valueOf( role.getId().longValue() );
      this.version = String.valueOf( role.getVersion().longValue() );

      this.createdBy = role.getAuditCreateInfo().getCreatedBy().toString();
      this.dateCreated = String.valueOf( role.getAuditCreateInfo().getDateCreated().getTime() );
    }
  }

  /**
   * Builds a domain object from the form.
   * 
   * @return Role
   */
  public Role toDomainObject()
  {
    Role role = new Role();

    role.setHelpText( this.helpText );
    role.setCode( this.code );
    role.setName( this.name );
    role.setActive( Boolean.valueOf( this.active ) );

    for ( int i = 0; i < selectedUserTypes.length; i++ )
    {
      role.addUserTypeCode( selectedUserTypes[i] );
    }

    // Check to make sure these fields are valid before adding them to the Role.
    if ( null != this.version && !this.version.equals( "" ) )
    {
      role.setVersion( Long.valueOf( this.version ) );
    }

    // Check to make sure these fields are valid before adding them to the Role.
    if ( null != this.id && !this.id.equals( "" ) )
    {
      role.setId( Long.valueOf( this.id ) );
    }

    if ( null != this.createdBy && !this.createdBy.equals( "" ) )
    {
      role.getAuditCreateInfo().setCreatedBy( Long.valueOf( this.createdBy ) );
    }

    if ( null != this.dateCreated && !this.dateCreated.equals( "" ) )
    {
      role.getAuditCreateInfo().setDateCreated( new Timestamp( Long.valueOf( this.dateCreated ).longValue() ) );
    }

    return role;
  }

  /**
   * Get form activityStatus value;
   * 
   * @return int
   */
  public String getActivityStatus()
  {
    return this.activityStatus;
  }

  /**
   * Set the activityStatus value on the form.
   * 
   * @param activityStatus
   */
  public void setActivityStatus( String activityStatus )
  {
    this.activityStatus = activityStatus;
  }

  /**
   * Get form Active value;
   * 
   * @return boolean
   */
  public String getActive()
  {
    return this.active;
  }

  /**
   * Set the active value on the form.
   * 
   * @param active
   */
  public void setActive( String active )
  {
    this.active = active;
  }

  /**
   * Get form method value;
   * 
   * @return string
   */
  public String getMethod()
  {
    return this.method;
  }

  /**
   * Set the method.
   * 
   * @param method
   */
  public void setMethod( String method )
  {
    this.method = method;
  }

  /**
   * Get form code value;
   * 
   * @return string
   */
  public String getCode()
  {
    return this.code;
  }

  /**
   * Set the code.
   * 
   * @param code
   */
  public void setCode( String code )
  {
    this.code = code;
  }

  /**
   * Get the HelpText from the form.
   * 
   * @return String
   */
  public String getHelpText()
  {
    return this.helpText;
  }

  /**
   * Set the helpText value on the form.
   * 
   * @param helpText
   */
  public void setHelpText( String helpText )
  {
    this.helpText = helpText;
  }

  /**
   * Get the dateCreated.
   * 
   * @return String
   */
  public String getDateCreated()
  {
    return this.dateCreated;
  }

  /**
   * Set the dateCreated on the form.
   * 
   * @param dateCreated
   */
  public void setDateCreated( String dateCreated )
  {
    this.dateCreated = dateCreated;
  }

  /**
   * Get the createdBy.
   * 
   * @return String
   */
  public String getCreatedBy()
  {
    return this.createdBy;
  }

  /**
   * Set the createdBy on the form.
   * 
   * @param createdBy
   */
  public void setCreatedBy( String createdBy )
  {
    this.createdBy = createdBy;
  }

  /**
   * Get the id.
   * 
   * @return String
   */
  public String getId()
  {
    return this.id;
  }

  /**
   * Set the id on the form.
   * 
   * @param id
   */
  public void setId( String id )
  {
    this.id = id;
  }

  /**
   * Get the version.
   * 
   * @return long
   */
  public String getVersion()
  {
    return this.version;
  }

  /**
   * Set the version on the form.
   * 
   * @param version
   */
  public void setVersion( String version )
  {
    this.version = version;
  }

  /**
   * Get the role name
   * 
   * @return String
   */
  public String getName()
  {
    return name;
  }

  /**
   * Set the role name.
   * 
   * @param name
   */
  public void setName( String name )
  {
    this.name = name;
  }

  public void setUpdate( boolean update )
  {
    this.update = update;
  }

  public boolean isUpdate()
  {
    return this.update;
  }

  public boolean getUpdate()
  {
    return this.update;
  }

  public String[] getSelectedUserTypes()
  {
    return selectedUserTypes;
  }

  public void setSelectedUserTypes( String[] selectedUserTypes )
  {
    this.selectedUserTypes = selectedUserTypes;
  }
}

/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javaui/com/biperf/core/ui/acl/AclForm.java,v $
 */

package com.biperf.core.ui.acl;

import java.sql.Timestamp;

import com.biperf.core.domain.user.Acl;
import com.biperf.core.ui.BaseForm;

/**
 * AclForm to collect information for an acl.
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
 * <td>Apr 13, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class AclForm extends BaseForm
{

  /** Static access to form name. */
  public static final String FORM_NAME = "aclForm";

  /** Serial ID for this form. */
  private static final long serialVersionUID = 3762812675561632562L;

  /** code */
  private String code;

  /** name */
  private String name;

  /** helpText */
  private String helpText;

  /** classPath */
  private String className;

  /** active */
  private boolean active;

  /** method */
  private String method;

  /** version */
  private String version;

  /** id */
  private String id;

  /** dateCreated */
  private String dateCreated;

  /** createdBy */
  private String createdBy;

  /** update */
  private boolean update;

  /**
   * Load the form with the domain object value;
   * 
   * @param acl
   */
  public void load( Acl acl )
  {
    this.code = acl.getCode();
    this.name = acl.getName();
    this.helpText = acl.getHelpText();
    this.className = acl.getClassName();
    this.active = acl.isActive().booleanValue();

    if ( acl.getId() != null )
    {
      this.id = String.valueOf( acl.getId().longValue() );
      this.version = String.valueOf( acl.getVersion().longValue() );

      this.createdBy = acl.getAuditCreateInfo().getCreatedBy().toString();
      this.dateCreated = String.valueOf( acl.getAuditCreateInfo().getDateCreated().getTime() );
    }

  }

  /**
   * Builds a domain object from the form.
   * 
   * @return userAcl
   */
  public Acl toDomainObject()
  {

    Acl acl = new Acl();
    acl.setCode( this.code );
    acl.setName( this.name );
    acl.setClassName( this.className );
    acl.setHelpText( this.helpText );
    acl.setActive( new Boolean( this.active ) );

    // Check to make sure these fields are valid before adding them to the acl.
    if ( null != this.version && !this.version.equals( "" ) )
    {
      acl.setVersion( Long.valueOf( this.version ) );
    }

    // Check to make sure these fields are valid before adding them to the acl.
    if ( null != this.id && !this.id.equals( "" ) )
    {
      acl.setId( Long.valueOf( this.id ) );
    }

    if ( null != this.createdBy && !this.createdBy.equals( "" ) )
    {
      acl.getAuditCreateInfo().setCreatedBy( Long.valueOf( this.createdBy ) );
    }

    if ( null != this.dateCreated && !this.dateCreated.equals( "" ) )
    {
      acl.getAuditCreateInfo().setDateCreated( new Timestamp( Long.valueOf( this.dateCreated ).longValue() ) );
    }

    return acl;
  }

  /**
   * Get form Active value;
   * 
   * @return boolean
   */
  public boolean getActive()
  {
    return this.active;
  }

  /**
   * Set the active value on the form.
   * 
   * @param active
   */
  public void setActive( boolean active )
  {
    this.active = active;
  }

  public String getName()
  {
    return name;
  }

  public void setName( String name )
  {
    this.name = name;
  }

  public String getCode()
  {
    return code;
  }

  public void setCode( String code )
  {
    this.code = code;
  }

  public String getClassName()
  {
    return className;
  }

  public void setClassName( String className )
  {
    this.className = className;
  }

  public String getHelpText()
  {
    return helpText;
  }

  public void setHelpText( String helpText )
  {
    this.helpText = helpText;
  }

  public String getCreatedBy()
  {
    return createdBy;
  }

  public void setCreatedBy( String createdBy )
  {
    this.createdBy = createdBy;
  }

  public String getDateCreated()
  {
    return dateCreated;
  }

  public void setDateCreated( String dateCreated )
  {
    this.dateCreated = dateCreated;
  }

  public String getId()
  {
    return id;
  }

  public void setId( String id )
  {
    this.id = id;
  }

  public String getMethod()
  {
    return method;
  }

  public void setMethod( String method )
  {
    this.method = method;
  }

  public String getVersion()
  {
    return version;
  }

  public void setVersion( String version )
  {
    this.version = version;
  }

  public boolean getUpdate()
  {
    return this.update;
  }

  public boolean isUpdate()
  {
    return this.update;
  }

  public void setUpdate( boolean update )
  {
    this.update = update;
  }

}

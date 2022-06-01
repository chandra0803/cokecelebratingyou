/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/domain/user/Acl.java,v $
 */

package com.biperf.core.domain.user;

import com.biperf.core.domain.BaseDomain;
import com.objectpartners.cms.util.ContentReaderManager;

/**
 * User ACL class to hold security access information for individual users.
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
public class Acl extends BaseDomain
{
  private static final String CM_ASSET_CODE = "admin.acl";
  private static final long serialVersionUID = 3907215944307587122L;

  private String name;
  private String code;
  private String helpText;
  private String className;
  private Boolean active = new Boolean( true );

  /**
   * Get the isActive. Hibernate required method.
   * 
   * @return Boolean
   */
  public Boolean getActive()
  {
    return this.active;
  }

  /**
   * JavaBean standard boolean method.
   * 
   * @return Boolean
   */
  public Boolean isActive()
  {
    return this.active;
  }

  /**
   * Set the isActive.
   * 
   * @param active
   */
  public void setActive( Boolean active )
  {
    this.active = active;
  }

  /**
   * Get the helpText.
   * 
   * @return String
   */
  public String getHelpText()
  {
    return this.helpText;
  }

  /**
   * Set the helpText.
   * 
   * @param helpText
   */
  public void setHelpText( String helpText )
  {
    this.helpText = helpText;
  }

  /**
   * Get the code.
   * 
   * @return String
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
   * Get the name.
   * 
   * @return String
   */
  public String getName()
  {
    return this.name;
  }

  /**
   * Set the Name.
   * 
   * @param name
   */
  public void setName( String name )
  {
    this.name = name;
  }

  /**
   * Get the className.
   * 
   * @return className
   */
  public String getClassName()
  {
    return this.className;
  }

  /**
   * Set the ClassName.
   * 
   * @param className
   */
  public void setClassName( String className )
  {
    this.className = className;
  }

  /**
   * Builds a String representation of this class. Overridden from
   * 
   * @see java.lang.Object#toString()
   * @return String
   */
  public String toString()
  {
    final StringBuffer buf = new StringBuffer();
    buf.append( "Acl [" );
    buf.append( "{id=" + super.getId() + "}, " );
    buf.append( "{name=" + this.getName() + "}," );
    buf.append( "{code=" + this.getCode() + "}," );
    buf.append( "{helpText=" + this.getHelpText() + "}, " );
    buf.append( "{classname=" + this.getClassName() + "}, " );
    buf.append( "{isActive=" + this.isActive() + "}" );
    buf.append( "]" );
    return buf.toString();
  }

  /**
   * Checks equality of the object parameter to this.
   * 
   * @param o
   * @return boolean
   */
  public boolean equals( Object o )
  {
    if ( this == o )
    {
      return true;
    }
    if ( ! ( o instanceof Acl ) )
    {
      return false;
    }

    final Acl acl = (Acl)o;

    if ( getName() != null ? !getName().equals( acl.getName() ) : acl.getName() != null )
    {
      return false;
    }

    if ( getCode() != null ? !getCode().equals( acl.getCode() ) : acl.getCode() != null )
    {
      return false;
    }

    if ( getHelpText() != null ? !getHelpText().equals( acl.getHelpText() ) : acl.getHelpText() != null )
    {
      return false;
    }

    if ( getClassName() != null ? !getClassName().equals( acl.getClassName() ) : acl.getClassName() != null )
    {
      return false;
    }

    return true;
  }

  /**
   * Define the hashCode from the id. Overridden from
   * 
   * @see com.biperf.core.domain.BaseDomain#hashCode()
   * @return int
   */
  public int hashCode()
  {
    int hashCode = 0;

    hashCode += getCode() != null ? getCode().hashCode() : 0;
    hashCode += getHelpText() != null ? getHelpText().hashCode() : 0;
    hashCode += getClassName() != null ? getClassName().hashCode() : 0;

    return hashCode;
  }

  /**
   * This mehtod is used in displayColumn for sorting
   * 
   * @return String
   */
  public String getActivityName()
  {
    return ContentReaderManager.getText( CM_ASSET_CODE, this.active.toString() );
  }
}

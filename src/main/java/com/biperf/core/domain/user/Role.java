/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/domain/user/Role.java,v $
 */

package com.biperf.core.domain.user;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.biperf.core.domain.BaseDomain;
import com.biperf.core.domain.enums.UserType;
import com.objectpartners.cms.util.ContentReaderManager;

/**
 * Role.
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
 * <td>Apr 1, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class Role extends BaseDomain
{
  private static final String CM_ASSET_CODE = "admin.role";
  private static final long serialVersionUID = 3978988751965859892L;

  private Boolean active = new Boolean( true );
  private String name;
  private String code;
  private String helpText;

  /**
   * The types of users that can assume this role, as a <code>Set</code>
   * of <code>String</code> objects.
   */
  private Set userTypeCodes = new HashSet();

  /**
   * A local, read-only cache of types of users that can assume this role,
   * as a <code>Set</code> of {@link UserType} objects.
   */
  private Set userTypes;

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
   * Get the HelpText.
   * 
   * @return helpText
   */
  public String getHelpText()
  {
    return this.helpText;
  }

  /**
   * Set the HelpText.
   * 
   * @param helpText
   */
  public void setHelpText( String helpText )
  {
    this.helpText = helpText;
  }

  /**
   * Get the Code.
   * 
   * @return code
   */
  public String getCode()
  {
    return this.code;
  }

  /**
   * Set the Code.
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

  public void addUserTypeCode( String userTypeCode )
  {
    userTypeCodes.add( userTypeCode );

    // Clear the cached user types.
    userTypes = null;
  }

  public Set getUserTypeCodes()
  {
    return userTypeCodes;
  }

  public void setUserTypeCodes( Set userTypeCodes )
  {
    this.userTypeCodes = userTypeCodes;

    // Clear the cached user types.
    userTypes = null;
  }

  /**
   * Returns the types of users that can assume this role.
   *
   * @return the types of users that can assume this role, as a <code>Set</code>
   *         of {@link UserType} objects.
   */
  public Set getUserTypes()
  {
    if ( userTypes == null )
    {
      userTypes = new HashSet();

      for ( Iterator iter = userTypeCodes.iterator(); iter.hasNext(); )
      {
        String userTypeCode = (String)iter.next();
        userTypes.add( UserType.lookup( userTypeCode ) );
      }
    }

    return userTypes;
  }

  /**
   * Builds a String representation of this. Overridden from
   * 
   * @see com.biperf.core.domain.BaseDomain#toString()
   * @return String
   */
  public String toString()
  {
    final StringBuffer buf = new StringBuffer();
    buf.append( "Role [" );
    buf.append( "{id=" ).append( super.getId() ).append( "}, " );
    buf.append( "{helpText=" ).append( this.getHelpText() ).append( "}, " );
    buf.append( "{code=" ).append( this.getCode() ).append( "}, " );
    buf.append( "{name=" ).append( this.getName() ).append( "}, " );
    buf.append( "{active=" ).append( this.getActive() ).append( "}" );
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
    if ( ! ( o instanceof Role ) )
    {
      return false;
    }

    final Role role = (Role)o;

    if ( getHelpText() != null ? !getHelpText().equals( role.getHelpText() ) : role.getHelpText() != null )
    {
      return false;
    }

    if ( getCode() != null ? !getCode().equals( role.getCode() ) : role.getCode() != null )
    {
      return false;
    }

    if ( getName() != null ? !getName().equals( role.getName() ) : role.getName() != null )
    {
      return false;
    }

    if ( getActive() != null ? getActive() != role.getActive() : role.getActive() != null )
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

    hashCode += getHelpText() != null ? getHelpText().hashCode() : 0;
    hashCode += getCode() != null ? getCode().hashCode() : 0;
    hashCode += getName() != null ? getName().hashCode() : 0;

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

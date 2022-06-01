/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/domain/user/UserRole.java,v $
 */

package com.biperf.core.domain.user;

import java.io.Serializable;
import java.sql.Timestamp;

import com.biperf.core.domain.AuditCreateInfo;
import com.biperf.core.domain.AuditCreateInterface;
import com.biperf.core.utils.UserManager;

/**
 * UserRole.
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
 * <td>Apr 4, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class UserRole implements AuditCreateInterface, Serializable
{

  /** serialVersionUID */
  private static final long serialVersionUID = 3905522717791630136L;

  /** role */
  private Role role;

  /** user */
  private User user;

  /** Audit information - cannot be null */
  private AuditCreateInfo auditCreateInfo;

  /**
   * public constructor that sets the createdBy and DateCreated properties. We do this since
   * Hibernate does not treat this as an entity - thus the HibernateAuditInterceptor is not used.
   */
  public UserRole()
  {
    getAuditCreateInfo().setCreatedBy( UserManager.getUserId() );
    getAuditCreateInfo().setDateCreated( new Timestamp( System.currentTimeMillis() ) );
  }

  /**
   * Public constructor to to build this with a user and role.
   * 
   * @param user
   * @param role
   */
  public UserRole( User user, Role role )
  {
    this();
    this.user = user;
    this.role = role;
  }

  /**
   * Get the Role.
   * 
   * @return Role
   */
  public Role getRole()
  {
    return role;
  }

  /**
   * Set the Role.
   * 
   * @param role
   */
  public void setRole( Role role )
  {
    this.role = role;
  }

  /**
   * Get the User.
   * 
   * @return Role
   */
  public User getUser()
  {
    return this.user;
  }

  /**
   * Set the User.
   * 
   * @param user
   */
  public void setUser( User user )
  {
    this.user = user;
  }

  /**
   * Get the Audit Information Overridden from
   * 
   * @see com.biperf.core.domain.AuditCreateInterface#getAuditCreateInfo()
   * @return AuditInfo
   */
  public AuditCreateInfo getAuditCreateInfo()
  {
    if ( this.auditCreateInfo == null )
    {
      this.auditCreateInfo = new AuditCreateInfo();
    }
    return this.auditCreateInfo;
  }

  /**
   * Set the AuditInfo.
   * 
   * @param info
   */
  public void setAuditCreateInfo( AuditCreateInfo info )
  {
    this.auditCreateInfo = info;
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
    buf.append( "UserRole [" );
    buf.append( "{role_id=" + this.getRole().getId() + "}, " );
    buf.append( "{user_id=" + this.getUser().getId() + "}" );
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
    if ( ! ( o instanceof UserRole ) )
    {
      return false;
    }

    final UserRole userRole = (UserRole)o;

    if ( getRole() != null ? !getRole().equals( userRole.getRole() ) : userRole.getRole() != null )
    {
      return false;
    }
    if ( getUser() != null ? !getUser().equals( userRole.getUser() ) : userRole.getUser() != null )
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
    int result;
    result = getRole() != null ? getRole().hashCode() : 0;
    result = 29 * result + ( getUser() != null ? getUser().hashCode() : 0 );

    return result;
  }
}

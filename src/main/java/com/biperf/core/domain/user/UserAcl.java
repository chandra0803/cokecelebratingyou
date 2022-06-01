/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/domain/user/UserAcl.java,v $
 */

package com.biperf.core.domain.user;

import java.io.Serializable;
import java.sql.Timestamp;

import org.springframework.util.ClassUtils;

import com.biperf.core.domain.AuditCreateInfo;
import com.biperf.core.domain.AuditCreateInterface;
import com.biperf.core.security.acl.AclEntry;
import com.biperf.core.utils.UserManager;

/**
 * UserAcl.
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
public class UserAcl implements AuditCreateInterface, Serializable
{

  /** Acl */
  private Acl acl;

  /** user */
  private User user;

  /** target */
  private String target;

  /** permission */
  private String permission;

  /** guid */
  private String guid;

  /** Audit information */
  private AuditCreateInfo auditCreateInfo;

  /**
   * public constructor that sets the createdBy and DateCreated properties. We do this since
   * Hibernate does not treat this as an entity - thus the HibernateAuditInterceptor is not used.
   */
  public UserAcl()
  {
    getAuditCreateInfo().setCreatedBy( UserManager.getUserId() );
    getAuditCreateInfo().setDateCreated( new Timestamp( System.currentTimeMillis() ) );

  }

  /**
   * Construct this with a User and an Acl.
   * 
   * @param user
   * @param acl
   */
  public UserAcl( User user, Acl acl )
  {
    this();
    this.user = user;
    this.acl = acl;
  }

  /**
   * Get the Acl.
   * 
   * @return acl
   */
  public Acl getAcl()
  {
    return acl;
  }

  /**
   * Set the Acl.
   * 
   * @param acl
   */
  public void setAcl( Acl acl )
  {
    this.acl = acl;
  }

  /**
   * Get the GUID.
   * 
   * @return GUID
   */
  public String getGuid()
  {
    return guid;
  }

  /**
   * Set the Guid.
   * 
   * @param guid
   */
  public void setGuid( String guid )
  {
    this.guid = guid;
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
   * Get the Target.
   * 
   * @return target
   */
  public String getTarget()
  {
    return this.target;
  }

  /**
   * Set the target.
   * 
   * @param target
   */
  public void setTarget( String target )
  {
    this.target = target;
  }

  /**
   * Get the permission.
   * 
   * @return permission
   */
  public String getPermission()
  {
    return this.permission;
  }

  /**
   * Set the permission.
   * 
   * @param permission
   */
  public void setPermission( String permission )
  {
    this.permission = permission;
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
   * Returns an object based on the Acl class name. If the object is an AclEntry, then the target
   * and permissions will be set.
   * 
   * @return Object, which may be an AclEntry. Null if there is any exception
   */
  public Object createAclEntry()
  {
    Object result = null;
    String className = acl.getClassName();
    if ( className != null && !"".equals( className ) )
    {
      try
      {
        Class clazz = ClassUtils.forName( className );
        result = clazz.newInstance();
        if ( result instanceof AclEntry )
        {
          AclEntry entry = (AclEntry)result;
          entry.setTarget( target );
          entry.setPermissionMask( permission );
        }
      }
      catch( ClassNotFoundException e )
      {
        result = null;
      }
      catch( InstantiationException e )
      {
        result = null;
      }
      catch( IllegalAccessException e )
      {
        result = null;
      }
    }
    return result;
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
    if ( ! ( o instanceof UserAcl ) )
    {
      return false;
    }

    final UserAcl userAcl = (UserAcl)o;

    if ( guid != null && !guid.equals( userAcl.guid ) )
    {
      return false;
    }

    if ( user != null && !user.equals( userAcl.user ) )
    {
      return false;
    }

    if ( acl != null && !acl.equals( userAcl.acl ) )
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

    hashCode += this.guid.hashCode();
    hashCode += this.user.hashCode();
    hashCode += this.acl.hashCode();

    return hashCode;
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
    buf.append( "USERACL [" );
    buf.append( "{acl =" + this.getAcl().getId() + "}, " );
    buf.append( "{user_id=" + this.getUser().getId() + "}," );
    buf.append( "{target=" + this.getTarget() + "}," );
    buf.append( "{permission=" + this.getPermission() + "}," );
    buf.append( "{guid=" + this.getGuid() + "}" );
    buf.append( "]" );

    return buf.toString();
  }

}

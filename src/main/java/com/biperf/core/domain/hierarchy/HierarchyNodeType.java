/*
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/domain/hierarchy/HierarchyNodeType.java,v $
 * (c) 2005 BI, Inc.  All rights reserved.
 */

package com.biperf.core.domain.hierarchy;

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
 * <td>kumars</td>
 * <td>May 3, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class HierarchyNodeType implements AuditCreateInterface, Serializable
{
  private NodeType nodeType;

  private Hierarchy hierarchy;

  /** Audit information - cannot be null */
  private AuditCreateInfo auditCreateInfo;

  /**
   * public constructor that sets the createdBy and DateCreated properties. We do this since
   * Hibernate does not treat this as an entity - thus the HibernateAuditInterceptor is not used.
   */
  public HierarchyNodeType()
  {
    getAuditCreateInfo().setCreatedBy( UserManager.getUserId() );
    getAuditCreateInfo().setDateCreated( new Timestamp( System.currentTimeMillis() ) );
  }

  /**
   * Get the Role.
   * 
   * @return Role
   */
  public NodeType getNodeType()
  {
    return nodeType;
  }

  /**
   * Set the Role.
   * 
   * @param nodeType
   */
  public void setNodeType( NodeType nodeType )
  {
    this.nodeType = nodeType;
  }

  /**
   * Get the User.
   * 
   * @return Hierarchy
   */
  public Hierarchy getHierarchy()
  {
    return this.hierarchy;
  }

  /**
   * Set the User.
   * 
   * @param hierarchy
   */
  public void setHierarchy( Hierarchy hierarchy )
  {
    this.hierarchy = hierarchy;
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
   * @see Object#toString()
   * @return String
   */
  public String toString()
  {
    final StringBuffer buf = new StringBuffer();
    buf.append( "HierarchyNodeType [" );
    buf.append( "{hierarchy_id=" + this.getNodeType().getId() + "}, " );
    buf.append( "{node_type_id=" + this.getHierarchy().getId() + "}" );
    buf.append( "]" );
    return buf.toString();
  }

  /**
   * Checks equality of the object parameter to this.
   * 
   * @param object
   * @return boolean
   */
  public boolean equals( Object object )
  {
    if ( this == object )
    {
      return true;
    }

    if ( ! ( object instanceof HierarchyNodeType ) )
    {
      return false;
    }

    final HierarchyNodeType hierarchyNodeType = (HierarchyNodeType)object;

    if ( getNodeType() != null ? !getNodeType().equals( hierarchyNodeType.getNodeType() ) : hierarchyNodeType.getNodeType() != null )
    {
      return false;
    }

    if ( getHierarchy() != null ? !getHierarchy().equals( hierarchyNodeType.getHierarchy() ) : hierarchyNodeType.getHierarchy() != null )
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

    result = getNodeType() != null ? getNodeType().hashCode() : 0;
    result = 29 * result + ( getHierarchy() != null ? getHierarchy().hashCode() : 0 );

    return result;
  }
}

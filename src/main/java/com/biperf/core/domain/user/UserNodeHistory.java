/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/domain/user/UserNodeHistory.java,v $
 */

package com.biperf.core.domain.user;

import com.biperf.core.domain.AuditCreateInfo;
import com.biperf.core.domain.AuditCreateInterface;
import com.biperf.core.domain.enums.HierarchyRoleType;
import com.biperf.core.domain.hierarchy.Node;

/**
 * UserNodeHistory.
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
 * <td>zahler</td>
 * <td>Dec 12, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class UserNodeHistory implements AuditCreateInterface
{
  private Long id;
  private User user;
  private Node node;
  private Boolean active = Boolean.TRUE;
  private HierarchyRoleType hierarchyRoleType;
  private AuditCreateInfo auditCreateInfo = new AuditCreateInfo();

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
    if ( ! ( o instanceof UserNodeHistory ) )
    {
      return false;
    }

    final UserNodeHistory userNodeHistory = (UserNodeHistory)o;

    if ( getNode() != null ? !getNode().equals( userNodeHistory.getNode() ) : userNodeHistory.getNode() != null )
    {
      return false;
    }
    if ( getUser() != null ? !getUser().equals( userNodeHistory.getUser() ) : userNodeHistory.getUser() != null )
    {
      return false;
    }
    if ( getAuditCreateInfo() != null
        ? !getAuditCreateInfo().getDateCreated().equals( userNodeHistory.getAuditCreateInfo().getDateCreated() )
        : userNodeHistory.getAuditCreateInfo().getDateCreated() != null )
    {
      return false;
    }

    return true;
  }

  /**
   * Does hashCode on the Business Key Overridden from
   * 
   * @see com.biperf.core.domain.BaseDomain#hashCode()
   * @return int
   */
  public int hashCode()
  {
    int result;

    result = getNode() != null ? getNode().hashCode() : 0;
    result = 31 * result + ( getUser() != null ? getUser().hashCode() : 0 );
    result = 31 * result + ( getAuditCreateInfo() != null ? getAuditCreateInfo().getDateCreated().hashCode() : 0 );

    return result;
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.domain.AuditCreateInterface#getAuditCreateInfo()
   * @return AuditCreateInfo
   */
  public AuditCreateInfo getAuditCreateInfo()
  {
    return auditCreateInfo;
  }

  public void setAuditCreateInfo( AuditCreateInfo auditCreateInfo )
  {
    this.auditCreateInfo = auditCreateInfo;
  }

  public Boolean getActive()
  {
    return active;
  }

  public void setActive( Boolean active )
  {
    this.active = active;
  }

  public HierarchyRoleType getHierarchyRoleType()
  {
    return hierarchyRoleType;
  }

  public void setHierarchyRoleType( HierarchyRoleType hierarchyRoleType )
  {
    this.hierarchyRoleType = hierarchyRoleType;
  }

  public Long getId()
  {
    return id;
  }

  public void setId( Long id )
  {
    this.id = id;
  }

  public Node getNode()
  {
    return node;
  }

  public void setNode( Node node )
  {
    this.node = node;
  }

  public User getUser()
  {
    return user;
  }

  public void setUser( User user )
  {
    this.user = user;
  }

}

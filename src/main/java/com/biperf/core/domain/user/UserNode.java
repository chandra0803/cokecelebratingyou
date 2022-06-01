/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/domain/user/UserNode.java,v $
 */

package com.biperf.core.domain.user;

import com.biperf.core.domain.BaseDomain;
import com.biperf.core.domain.enums.HierarchyRoleType;
import com.biperf.core.domain.hierarchy.Node;

/**
 * UserNode.
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
 * <td>Adam</td>
 * <td>May 4, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class UserNode extends BaseDomain
{
  private User user;
  private Node node;
  private Boolean active = Boolean.TRUE;
  private HierarchyRoleType hierarchyRoleType;
  private Boolean isPrimary;
  private Boolean isMgrPrimary;// Client customization for WIP #41645
  
  /**
   * Default Constructor
   */
  public UserNode()
  {
    super();
  }

  /**
   * Construct this with a user and a node.
   * 
   * @param user
   * @param node
   */
  public UserNode( User user, Node node )
  {
    super();
    this.user = user;
    this.node = node;
  }

  /**
   * @return User
   */
  public User getUser()
  {
    return user;
  }

  /**
   * @param user
   */
  public void setUser( User user )
  {
    this.user = user;
  }

  /**
   * @return Node
   */
  public Node getNode()
  {
    return node;
  }

  /**
   * @param node
   */
  public void setNode( Node node )
  {
    this.node = node;
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
    if ( ! ( o instanceof UserNode ) )
    {
      return false;
    }

    final UserNode userNode = (UserNode)o;

    if ( getNode() != null ? !getNode().equals( userNode.getNode() ) : userNode.getNode() != null )
    {
      return false;
    }
    if ( getUser() != null ? !getUser().equals( userNode.getUser() ) : userNode.getUser() != null )
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
    result = getNode() != null ? getNode().hashCode() : 0;
    result = 31 * result + ( getUser() != null ? getUser().hashCode() : 0 );

    return result;
  }

  /**
   * HierarchyRoleType is the role on the UserNode.
   * 
   * @return HierarchyRoleType
   */
  public HierarchyRoleType getHierarchyRoleType()
  {
    return hierarchyRoleType;
  }

  /**
   * Setting HierarchyRoleType which is the role on a UserNode.
   * 
   * @param hierarchyRoleType
   */
  public void setHierarchyRoleType( HierarchyRoleType hierarchyRoleType )
  {
    this.hierarchyRoleType = hierarchyRoleType;
  }

  /**
   * Is this UserNode active for the user.
   * 
   * @return boolean if node is active will be true
   */
  public Boolean isActive()
  {
    return active;
  }

  /**
   * @return boolean
   */
  public Boolean getActive()
  {
    return active;
  }

  /**
   * @param active
   */
  public void setActive( Boolean active )
  {
    this.active = active;
  }

  public Boolean getIsPrimary()
  {
    return isPrimary;
  }

  public void setIsPrimary( Boolean isPrimary )
  {
    this.isPrimary = isPrimary;
  }
//Client customization for WIP #41645 starts
  public Boolean getIsMgrPrimary() 
  {
	return isMgrPrimary;
  }

  public void setIsMgrPrimary( Boolean isMgrPrimary ) 
  {
	this.isMgrPrimary = isMgrPrimary;
  }
//Client customization for WIP #41645 end

}

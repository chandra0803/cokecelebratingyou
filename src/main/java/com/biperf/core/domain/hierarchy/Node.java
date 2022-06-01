/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/domain/hierarchy/Node.java,v $
 */

package com.biperf.core.domain.hierarchy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import com.biperf.core.domain.BaseDomain;
import com.biperf.core.domain.enums.HierarchyRoleType;
import com.biperf.core.domain.user.ParticipantNameComparator;
import com.biperf.core.domain.user.User;
import com.biperf.core.domain.user.UserNode;

/**
 * Node.
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
 * <td>Apr 22, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class Node extends BaseDomain
{
  // ---------------------------------------------------------------------------
  // Fields
  // ---------------------------------------------------------------------------

  public static final String NODE_PATH_DELIMITER = "/";

  private String name;

  /**
   * string representing the path to the node in the hierarchy <br/> Note: The path is of form
   * "{Root Node Name}/{Node Name}/{Node Name}"<br/> Node names are used instead of node ids (in
   * spite of the node rename cost) because path node name substring searchs are useful such as in:
   * 
   * @see #isMemberOfNodeBranchesStartingWithInputNodeNamePrefix(String)
   */
  private String path;

  private String description;
  private String selfEnrollmentCode;
  private NodeType nodeType;
  private Hierarchy hierarchy;
  private Set userNodes = new LinkedHashSet();
  private Set childNodes = new LinkedHashSet();
  private Set nodeCharacteristics = new LinkedHashSet();
  private boolean deleted = false;

  private Node parentNode;

  private UUID rosterNodeId;

  // ---------------------------------------------------------------------------
  // Constructors
  // ---------------------------------------------------------------------------

  /**
   * Default Constructor
   */
  public Node()
  {
    super();
  }

  /**
   * @param id
   */
  public Node( Long id )
  {
    super( id );
  }

  // ---------------------------------------------------------------------------
  // Getter and Setter Methods
  // ---------------------------------------------------------------------------

  public boolean isDeleted()
  {
    return deleted;
  }

  public void setDeleted( boolean deleted )
  {
    this.deleted = deleted;
  }

  public Set getUserNodes()
  {
    return userNodes;
  }

  public void setUserNodes( Set userNodes )
  {
    this.userNodes = userNodes;
  }

  public String getName()
  {
    return this.name;
  }

  public void setName( String name )
  {
    this.name = name;
  }

  public String getDescription()
  {
    return this.description;
  }

  public void setDescription( String description )
  {
    this.description = description;
  }

  /**
   * @return value of nodeType property
   */
  public NodeType getNodeType()
  {
    return nodeType;
  }

  /**
   * @param nodeType value for nodeType property
   */
  public void setNodeType( NodeType nodeType )
  {
    this.nodeType = nodeType;
  }

  public Hierarchy getHierarchy()
  {
    return hierarchy;
  }

  public void setHierarchy( Hierarchy hierarchy )
  {
    this.hierarchy = hierarchy;
  }

  /**
   * Returns a list of active users who are associated with this node.
   * 
   * @return a list of active users who are associated with this node, as a <code>List</code> of
   *         {@link User} objects.
   */
  public List getActiveUserList()
  {
    List activeUserList = new ArrayList();

    for ( Iterator iter = userNodes.iterator(); iter.hasNext(); )
    {
      UserNode userNode = (UserNode)iter.next();
      User user = userNode.getUser();
      Boolean isActive = user.isActiveClassAware();
      if ( isActive != null && isActive.booleanValue() )
      {
        activeUserList.add( user );
      }
    }

    Collections.sort( activeUserList, new ParticipantNameComparator() );

    return activeUserList;
  }

  public List getNodeUsersList()
  {
    List activeUserList = new ArrayList();

    for ( Iterator iter = userNodes.iterator(); iter.hasNext(); )
    {
      UserNode userNode = (UserNode)iter.next();
      User user = userNode.getUser();
      activeUserList.add( user );
    }

    Collections.sort( activeUserList, new ParticipantNameComparator() );

    return activeUserList;
  }

  /**
   * @return String of the path to the node in the hierarchy.
   */
  public String getPath()
  {
    return path;
  }

  /**
   * Path is the location to the node in the hierarchy.
   * 
   * @param path
   */
  public void setPath( String path )
  {
    this.path = path;
  }

  public Node getParentNode()
  {
    return parentNode;
  }

  public void setParentNode( Node parentNode )
  {
    this.parentNode = parentNode;
  }

  public UUID getRosterNodeId()
  {
    return rosterNodeId;
  }

  public void setRosterNodeId( UUID rosterNodeId )
  {
    this.rosterNodeId = rosterNodeId;
  }

  public Set getChildNodes()
  {
    return childNodes;
  }

  public void setChildNodes( Set childNodes )
  {
    this.childNodes = childNodes;
  }

  public boolean hasUserNodes()
  {
    boolean hasUserNodes = false;
    if ( userNodes != null )
    {
      hasUserNodes = userNodes.isEmpty();
    }
    return hasUserNodes;
  }

  public void addUser( User user )
  {
    this.userNodes.add( new UserNode( user, this ) );
  }

  public void addUserNode( UserNode userNode )
  {
    userNode.setNode( this );
    userNode.getUser().addUserNode( userNode );
    userNodes.add( userNode );
  }

  public void removeUserNode( UserNode userNode )
  {
    userNode.getUser().getUserNodes().remove( userNode );
    userNodes.remove( userNode );
  }

  /**
   * @return boolean telling if a node has an owner who is active
   */
  public boolean hasOwner()
  {
    boolean hasOwner = false;
    if ( null != userNodes )
    {
      Iterator userNodesIterator = userNodes.iterator();
      while ( userNodesIterator.hasNext() )
      {
        UserNode userNode = (UserNode)userNodesIterator.next();
        if ( userNode.isActive().booleanValue() && userNode.getHierarchyRoleType().getCode().equals( HierarchyRoleType.OWNER ) )
        {
          hasOwner = true;
          break;
        }
      }
    }
    return hasOwner;
  }

  public Object getUserNodesNodeList()
  {

    List userNodeNodeList = new ArrayList();

    Iterator userNodeIter = this.userNodes.iterator();

    while ( userNodeIter.hasNext() )
    {
      UserNode userNode = (UserNode)userNodeIter.next();
      userNodeNodeList.add( userNode.getNode() );
    }

    return userNodeNodeList;
  }

  /**
   * Add a node to the set of children nodes.
   * 
   * @param node
   */
  public void addChildNode( Node node )
  {
    if ( childNodes == null )
    {
      childNodes = new LinkedHashSet();
    }

    this.childNodes.add( node );
  }

  /**
   * Add the nodeCharacteristic to the nodeCharacteristic set
   * 
   * @param nodeCharacteristic
   */
  public void addNodeCharacteristic( NodeCharacteristic nodeCharacteristic )
  {
    nodeCharacteristic.setNode( this );
    nodeCharacteristics.add( nodeCharacteristic );
  }

  public Set getActiveNodeCharacteristics()
  {
    Set activeNodeCharacteristics = new HashSet();

    for ( Iterator iter = nodeCharacteristics.iterator(); iter.hasNext(); )
    {
      NodeCharacteristic nodeCharacteristic = (NodeCharacteristic)iter.next();

      if ( nodeCharacteristic.getNodeTypeCharacteristicType().isActive() )
      {
        activeNodeCharacteristics.add( nodeCharacteristic );
      }
    }
    return Collections.unmodifiableSet( activeNodeCharacteristics );
  }

  public Set getNodeCharacteristics()
  {
    return nodeCharacteristics;
  }

  public void setNodeCharacteristics( Set nodeCharacteristics )
  {
    this.nodeCharacteristics = nodeCharacteristics;
  }

  /**
   * Returns the users who have the specified relationship to this node.
   * 
   * @param hierarchyRoleTypeName the name of a hierarchy role type.
   * @return the users who have the specified relationship to this node, as a <code>Set</code> of
   *         {@link User} objects.
   */
  public Set getUsersByRole( String hierarchyRoleTypeName )
  {
    Set userSet = new HashSet();

    Iterator iter = userNodes.iterator();
    while ( iter.hasNext() )
    {
      UserNode userNode = (UserNode)iter.next();
      HierarchyRoleType hierarchyRoleType = userNode.getHierarchyRoleType();
      if ( hierarchyRoleType != null && hierarchyRoleType.getCode().equals( hierarchyRoleTypeName ) && userNode.getUser().isActive().booleanValue() )
      {
        userSet.add( userNode.getUser() );
      }
    }

    return userSet;
  }

  /**
   * Returns the users who is/are the owner of this node.
   * 
   * @return the users who have the specified relationship to this node, as a <code>Set</code> of
   *         {@link User} objects.
   */
  public Set getOwners()
  {
    return getUsersByRole( HierarchyRoleType.OWNER );

  }

  // ---------------------------------------------------------------------------
  // Equals, Hashcode, and To String Methods
  // ---------------------------------------------------------------------------

  /**
   * Ensure equality between this and the object param.
   * 
   * @see com.biperf.core.domain.BaseDomain#equals(java.lang.Object)
   * @param object
   * @return boolean
   */
  public boolean equals( Object object )
  {

    boolean equals = true;

    if ( this == object )
    {
      return true;
    }
    if ( ! ( object instanceof Node ) )
    {
      return false;
    }

    Node node = (Node)object;

    if ( node.getName() != null && !node.getName().equals( this.getName() ) )
    {
      equals = false;
    }
    if ( node.getHierarchy() != null && !node.getHierarchy().equals( this.getHierarchy() ) )
    {
      equals = false;
    }

    return equals;

  }

  /**
   * Generated hashCode required by Hiberate for this. Overridden from
   * 
   * @see com.biperf.core.domain.BaseDomain#hashCode()
   * @return int
   */
  public int hashCode()
  {
    int result;
    result = this.getName() != null ? this.getName().hashCode() : 0;
    result = 29 * result + ( getHierarchy() != null ? getHierarchy().hashCode() : 0 );
    return result;
  }

  /**
   * Returns true if the branch rooted at the input node contains this node.
   * 
   * @param inputNode
   * @return boolean
   */
  public boolean isMemberOfInputNodeBranch( Node inputNode )
  {
    return path.toLowerCase().equals( inputNode.getPath().toLowerCase() ) || path.toLowerCase().startsWith( inputNode.getPath().toLowerCase() + NODE_PATH_DELIMITER );
  }

  /**
   * Returns true if any of the branches rooted at the nodes starting with the input node name
   * contain this node.
   * 
   * @param inputNodeNamePrefix
   * @return boolean
   */
  public boolean isMemberOfNodeBranchesStartingWithInputNodeNamePrefix( String inputNodeNamePrefix )
  {
    return path.toLowerCase().indexOf( NODE_PATH_DELIMITER + inputNodeNamePrefix.toLowerCase() ) >= 0;
  }

  /**
   * Calculate the path string for this node. Parent node must be hydrated.
   * 
   * @return String
   */
  public String calculatePath()
  {
    String calculatedPath = "/";

    if ( parentNode != null )
    {
      calculatedPath = parentNode.getPath() + "/" + name;
    }

    return calculatedPath;
  }

  /**
   * Return the Node Owner for this node.
   * 
   * @return User
   */
  public User getNodeOwner()
  {
    Set owners = getUsersByRole( HierarchyRoleType.OWNER );
    User user = null;
    if ( owners != null && !owners.isEmpty() )
    {
      user = (User)owners.iterator().next();
    }

    return user;
  }

  public String getSelfEnrollmentCode()
  {
    return selfEnrollmentCode;
  }

  public void setSelfEnrollmentCode( String selfEnrollmentCode )
  {
    this.selfEnrollmentCode = selfEnrollmentCode;
  }

  /**
   * Gets the Set of the given User's Managers. Make sure this is hydrated!!
   * 
   * @param user
   * @return Set of all of the Managers for given user's node.
   */
  public Set getNodeManagersForUser( User user )
  {
    Set nodeManagers = new HashSet();

    Node currentNode = this;

    if ( user.isMemberOf( currentNode ) )
    {
      Set managers = currentNode.getUsersByRole( HierarchyRoleType.MANAGER );
      if ( !managers.isEmpty() )
      {
        nodeManagers.addAll( managers );
      }
      else
      {
        while ( currentNode != null && nodeManagers.isEmpty() )
        {
          Set owners = currentNode.getUsersByRole( HierarchyRoleType.OWNER );
          if ( !owners.isEmpty() )
          {
            nodeManagers.addAll( owners );
          }
          else
          {
            currentNode = currentNode.getParentNode();
          }
        }
      }
    }
    else if ( user.isManagerOf( currentNode ) )
    {
      while ( currentNode != null && nodeManagers.isEmpty() )
      {
        Set owners = currentNode.getUsersByRole( HierarchyRoleType.OWNER );
        if ( !owners.isEmpty() )
        {
          nodeManagers.addAll( owners );
        }
        else
        {
          currentNode = currentNode.getParentNode();
        }
      }
    }
    else if ( user.isOwnerOf( currentNode ) )
    {
      currentNode = currentNode.getParentNode();
      while ( currentNode != null && nodeManagers.isEmpty() )
      {
        Set owners = currentNode.getUsersByRole( HierarchyRoleType.OWNER );
        if ( !owners.isEmpty() )
        {
          nodeManagers.addAll( owners );
        }
        else
        {
          currentNode = currentNode.getParentNode();
        }
      }
    }

    return nodeManagers;
  }

  /**
   * Gets the manager's Node Id for a user and manager. Make sure this is hydrated!!
   * 
   * @param claimUser
   * @param managerUser
   * @return Long of the Managers Node Id for given user's manager.
   */
  public Long getManagersNodeIdForManagerAndUser( User claimUser, User managerUser )
  {
    Set nodeManagers = new HashSet();

    Node currentNode = this;

    if ( claimUser.isMemberOf( currentNode ) )
    {
      Set managers = currentNode.getUsersByRole( HierarchyRoleType.MANAGER );
      if ( !managers.isEmpty() )
      {
        for ( Iterator m1 = managers.iterator(); m1.hasNext(); )
        {
          User mCompare = (User)m1.next();
          if ( managerUser.getId().equals( mCompare.getId() ) )
          {
            return currentNode.getId();
          }
        }
        nodeManagers.addAll( managers );
      }
      else
      {
        while ( currentNode != null && nodeManagers.isEmpty() )
        {
          Set owners = currentNode.getUsersByRole( HierarchyRoleType.OWNER );
          if ( !owners.isEmpty() )
          {
            for ( Iterator o1 = owners.iterator(); o1.hasNext(); )
            {
              if ( managerUser.equals( (User)o1.next() ) )
              {
                return currentNode.getId();
              }
            }
            nodeManagers.addAll( owners );
          }
          else
          {
            currentNode = currentNode.getParentNode();
          }
        }
      }
    }
    else if ( claimUser.isManagerOf( currentNode ) )
    {
      while ( currentNode != null && nodeManagers.isEmpty() )
      {
        Set owners = currentNode.getUsersByRole( HierarchyRoleType.OWNER );
        if ( !owners.isEmpty() )
        {
          for ( Iterator o2 = owners.iterator(); o2.hasNext(); )
          {
            if ( managerUser.equals( (User)o2.next() ) )
            {
              return currentNode.getId();
            }
          }
          nodeManagers.addAll( owners );
        }
        else
        {
          currentNode = currentNode.getParentNode();
        }
      }
    }
    else if ( claimUser.isOwnerOf( currentNode ) )
    {
      currentNode = currentNode.getParentNode();
      while ( currentNode != null && nodeManagers.isEmpty() )
      {
        Set owners = currentNode.getUsersByRole( HierarchyRoleType.OWNER );
        if ( !owners.isEmpty() )
        {
          for ( Iterator o3 = owners.iterator(); o3.hasNext(); )
          {
            if ( managerUser.equals( (User)o3.next() ) )
            {
              return currentNode.getId();
            }
          }
          nodeManagers.addAll( owners );
        }
        else
        {
          currentNode = currentNode.getParentNode();
        }
      }
    }

    return null;
  }

  public String toString()
  {
    StringBuffer sb = new StringBuffer();

    sb.append( "[NODE " );
    sb.append( "{name = " ).append( this.name ).append( "}" );
    sb.append( "]" );

    return sb.toString();
  }
}

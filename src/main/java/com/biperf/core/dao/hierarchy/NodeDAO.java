/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/dao/hierarchy/NodeDAO.java,v $
 */

package com.biperf.core.dao.hierarchy;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import com.biperf.core.dao.DAO;
import com.biperf.core.domain.enums.HierarchyRoleType;
import com.biperf.core.domain.hierarchy.Hierarchy;
import com.biperf.core.domain.hierarchy.Node;
import com.biperf.core.domain.hierarchy.NodeCharacteristic;
import com.biperf.core.domain.hierarchy.NodeType;
import com.biperf.core.domain.user.User;
import com.biperf.core.service.hierarchy.NodeSearchCriteria;
import com.biperf.core.value.FormattedValueBean;

/**
 * NodeDAO.
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
 */
public interface NodeDAO extends DAO
{

  public static final String BEAN_NAME = "nodeDAO";

  /**
   * Get a set of all nodes which exclude the node param.
   * 
   * @param node
   * @return List
   */
  public abstract List getAllExcludingNode( Node node );

  /**
   * Get the node from the database by the id.
   * 
   * @param id
   * @return Node
   */
  public abstract Node getNodeById( Long id );

  /**
   * Get the node from the database by location code, which is stored in the node characteristic,
   * expected to be unique per node.
   * 
   * @param locCode
   * @return Node
   */
  public Node getNodeByRegistrationLocCode( String locCode );

  /**
   * Get the node from the database by the nodeName and Hiearchy.
   * 
   * @param nodeName
   * @param hierarchy
   * @return Node
   */
  public Node getNodeByNameAndHierarchy( String nodeName, Hierarchy hierarchy );

  /**
   * Saves the node to the database.
   * 
   * @param node
   * @return Node
   */
  public abstract Node saveNode( Node node );

  /**
   * Searches for the node with the critia param
   * 
   * @param name
   * @param description
   * @param deleted
   * @return Node
   */
  public abstract List searchNode( String name, String description, String deleted );

  /**
   * Search a node by criterion of hierarchyId/partialNodeName/nodeTypeId
   * 
   * @param hierarchyId
   * @param partialNodeName
   * @param nodeTypeId
   * @return List
   */
  public abstract List searchNode( Long hierarchyId, String partialNodeName, Long nodeTypeId );

  /**
   * Retrieves all Nodes from the database.
   * 
   * @return List of nodes
   */
  public abstract List getAll();

  /**
   * Gets all non-deleted Node objects which have an association to the given NodeType;
   * 
   * @param nodeType
   * @return List - list of nodes
   */
  public List getNodesByNodeType( NodeType nodeType );

  public List getNodesByNodeTypeAndHierarchy( NodeType nodeType, Long hierarchyId );

  /**
   * Gets all deleted Node objects.
   * 
   * @return List - list of nodes
   */
  public List getAllDeletedNodes();

  /**
   * Retrieves the root node for the specified hierarchy id
   * 
   * @param hierarchy
   * @return Node
   */
  public Node getRootNode( Hierarchy hierarchy );

  /**
   * Retrieves the list of nodes for the specified hierarchy
   * 
   * @param hierarchy
   * @return Node
   */
  public List getNodesByHierarchy( Hierarchy hierarchy );

  /**
   * Get the immediate child nodes of a parent node
   * 
   * @param parentNodeId
   * @return List
   */
  public List getChildNodesByParent( Long parentNodeId );

  /**
   * Get the hierarchy nodes
   * 
   * @param hierarchyId
   * @return List of Node objects
   */
  public List getNodesAsHierarchy( Long hierarchyId );

  /**
   * returns a list of nodeIds and userIds that will have duplicate node owners after a file is loaded - used
   * during the import file process to verify only one owner per node exists.
   * 
   * @param importFileId
   * @return List of NodeId/UserId Object Arrays
   */
  public List getListOfNodeIdsAndUserIdsByImportFile( Long importFileId );

  /**
   * For live promotions that have an approval Node Type and hierarchy set, return all nodes
   * matching the promotion node type and hierarchy that have no owner for nodes.
   */
  public List getNodeTypeApproverPromotionApprovalNodeswithNoOwner();

  /**
   * Retrieves the list of nodes which have the specified user in the sepecified hierarchyRoleType.
   * 
   * @param user
   * @param hierarchyRoleType
   * @return List of Node
   */
  public List getNodesWithUserHavingRole( User user, HierarchyRoleType hierarchyRoleType );

  /**
   * Update the path of each descendant node of BaseNode, replacing for each descendant node the
   * oldPathSubstring with the newPathSubstring.
   */
  public void updateDescendantsPath( Node baseNode, String oldPathSubstring, String newPathSubstring );

  /**
   * 
   * @param nodeId
   * @return
   */
  public Long getNumberOfUserNodesById( Long nodeId );

  /**
   * Search the database with the given search criteria params. Overridden from
   * 
   * @see com.biperf.core.dao.hierachary.NodeDAO#searchNode(com.biperf.core.service.hierachary.NodeSearchCriteria)
   * @param searchCriteria
   * @return List
   */
  public List<Node> searchNode( NodeSearchCriteria searchCriteria );

  public User getNodeOwnerByLevel( Long nodeId );

  public List<Long> findOwnerNodeIds( Long userId );

  public List<Long> findChildNodeIds( Long nodeId );

  public boolean nodeHasOwner( Long nodeId );

  public List findNodesAsHierarchyByUser( Long userId );

  public abstract boolean isLeafNode( Long nodeId );

  public List allChildNodesUnderParentNodes( Long nodeId );

  public boolean isAnyNodeWithOutEnrollmentCode();

  public Integer getNodesHavingEnrollmentCodesCount();

  public List<Node> getNodesHavingEnrollmentCodes( Integer pageNumber, Integer pageSize );

  public List<Node> getNodesNotHavingEnrollmentCodes( Integer pageNumber, Integer pageSize );

  public Node getNodeByEnrollmentCode( String enrollmentCode );

  public List<FormattedValueBean> getUserNodesAndBelow( Long userId );

  public Set getUsersByRole( Long nodeId, String hierarchyRoleType );

  public abstract void deleteNodeCharacteristics( NodeCharacteristic nodeCharacteristic );

  public Integer getPaxCountBasedOnNodes( Long nodeId, Boolean isIncludeChild );

  Map<Long, String> getNodeDescription( List<Long> nodeIds );

  public int getCountByNodeName( String startsWith );

  public boolean isUserInNodeorBelow( Long searchUserId, Long loggedInUserid );

  public Node getNodeByHierarchyIdAndNodeId( Long hierarchyId, Long nodeId );

  public boolean isOwnerAlreadyInUserNode( Long userId, Long nodeId );

  public Long getNodeIdByRosterNodeId( UUID rosterNodeId );

  public UUID getRosterNodeIdByNodeId( Long nodeId );

  public List<String> getRosterNodeIdsByNodeIds( List<Long> nodeIds );
  
  public Node getNodeByRosterNodeId( UUID rosterNodeId );

}

/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/hierarchy/NodeService.java,v $
 */

package com.biperf.core.service.hierarchy;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import com.biperf.core.domain.enums.HierarchyRoleType;
import com.biperf.core.domain.hierarchy.Hierarchy;
import com.biperf.core.domain.hierarchy.Node;
import com.biperf.core.domain.hierarchy.NodeCharacteristic;
import com.biperf.core.domain.hierarchy.NodeType;
import com.biperf.core.domain.user.User;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.SAO;
import com.biperf.core.value.FormattedValueBean;

/**
 * NodeService.
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
public interface NodeService extends SAO
{
  /** BEAN_NAME for referencing while doing Bean Lookup */
  public static final String BEAN_NAME = "nodeService";

  /**
   * Get a list of children nodes for a parent Id.
   * 
   * @param parentNodeId
   * @return List
   */
  public List getChildrenNodes( Long parentNodeId );

  /**
   * Get all Nodes in the database.
   * 
   * @return List
   */
  public List getAll();

  /**
   * Get all Nodes for the specified association object in the database.
   * 
   * @param associationRequestCollection 
   * @return List
   */
  public List getAll( AssociationRequestCollection associationRequestCollection );

  /**
   * Save the node information to the database.
   * 
   * @param node
   * @param parentId
   * @param updateDescendantsPath
   * @return Node
   */
  public Node saveNode( Node node, Long parentId, boolean updateDescendantsPath );

  /**
   * Update the path of each descendant node of BaseNode, replacing for each descendant node the
   * oldPathSubstring with the newPathSubstring.
   * 
   * @param savedNode
   * @param oldPath
   * @param newPathSubstring
   */
  public void updateDescendantsPath( Node savedNode, String oldPath, String newPathSubstring );

  /**
   * Delete the node from the database.
   * 
   * @param node
   */
  public void deleteNode( Node node );

  /**
   * Get the node from the database by the id param.
   * 
   * @param id
   * @return Node
   */
  public Node getNodeById( Long id );

  /**
   * Get the Node along with its associations
   * 
   * @param id
   * @param associationRequestCollection
   * @return Node
   */
  public Node getNodeWithAssociationsById( Long id, AssociationRequestCollection associationRequestCollection );

  /**
   * Get the node from the database by the name param and hiearachy.
   * 
   * @param nodeName
   * @param hierarchy
   * @return Node
   */
  public Node getNodeByNameAndHierarchy( String nodeName, Hierarchy hierarchy );

  /**
   * Get the node from the database by the name param and Hierarchy.
   * 
   * @param nodeName
   * @param hierarchy
   * @param associationRequestCollection
   * @return Node
   */
  public Node getNodeByNameAndHierarchy( String nodeName, Hierarchy hierarchy, AssociationRequestCollection associationRequestCollection );

  /**
   * Search the database for the search criteria.
   * 
   * @param name
   * @param description
   * @param deleted
   * @return List
   */
  public List searchNode( String name, String description, String deleted );

  /**
   * Search a node by criterion of hierarchyId/partialNodeName/nodeTypeId
   * 
   * @param hierarchyId
   * @param partialNodeName
   * @param nodeTypeId
   * @return List
   */
  public List searchNode( Long hierarchyId, String partialNodeName, Long nodeTypeId );

  /**
   * Get a list of all nodes excluding the node param.
   * 
   * @param node
   * @return List
   */
  public List getAllExcludingNode( Node node );

  /**
   * Get the list of nodes for the specified hierarchy
   * 
   * @param hierarchyId
   * @return List of Node objects
   */
  public List getNodesByHierarchy( Long hierarchyId );

  /**
   * Get the list of nodes as hierarchy
   * 
   * @param hierarchyId
   * @return List of Node objects
   */
  public List getNodesAsHierarchy( Long hierarchyId );

  /**
   * Get the list of immediate nodes below a parent node.
   * 
   * @param parentNodeId
   * @param associationRequestCollection
   * @return List
   */
  public List getChildNodesWithAssociationsByParent( Long parentNodeId, AssociationRequestCollection associationRequestCollection );

  /**
   * Disassociates the list of participants (ids) from the given Node
   * 
   * @param nodeId
   * @param listOfParticipantIds should be a List of Long objects
   * @throws ServiceErrorException
   */
  void removeParticipantsFromNode( Long nodeId, List listOfParticipantIds ) throws ServiceErrorException;

  /**
   * Retrieves the root node for the specified hierarchyId id
   * 
   * @param hierarchyId
   * @param associationCollection
   * @return Node
   */
  public Node getRootNode( Long hierarchyId, AssociationRequestCollection associationCollection );

  /**
   * Update the isDeleted boolean on the node and save it to the database.
   * 
   * @param deletedNodeId
   * @param nodeToHoldChildrenId
   * @param nodeToHoldUsersId
   */
  public void updateNodeAndDelete( Long deletedNodeId, Long nodeToHoldChildrenId, Long nodeToHoldUsersId );

  /**
   * For live promotions that have an approval Node Type and hierarchy set, return all nodes
   * matching the promotion node type and hierarchy that have no owner for nodes.
   * @return List
   */
  public List getNodeTypeApproverPromotionApprovalNodeswithNoOwner();

  /**
   * returns a list of nodeId/userIds that will have duplicate node owners after a file is loaded - used
   * during the import file process to verify only one owner per node exists.
   * 
   * @param importFileId
   * @return List of NodeId/UserId
   */
  public List getListOfNodeIdsAndUserIdsByImportFile( Long importFileId );

  /**
   * returns a list of nodes by nodeType
   * 
   * @param nodeType
   * @return List of Node objects
   */
  public List getNodesByNodeType( NodeType nodeType );

  public List getNodesByNodeTypeAndHierarchy( NodeType nodeType, Long hierarchyId );

  /**
   * Retrieves the list of nodes which have the specified user in the sepecified hierarchyRoleType.
   * 
   * @param user
   * @param hierarchyRoleType
   * @return List of Node
   */
  public List getNodesWithUserHavingRole( User user, HierarchyRoleType hierarchyRoleType );

  /**
   * Creates random location codes for nodes and assigns them as node type characteristics.  The 
   *    location code is a 6 digit number.  
   *
   */
  public void generateNodeRegistrationLocCodes();

  /**
   * Get the node from the database by location code, which is stored in the node characteristic,
   * expected to be unique per node.
   * 
   * @param locCode
   * @return Node
   */
  public Node getNodeByRegistrationLocCode( String locCode );

  /**
   * 
   * @param id
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

  public boolean isLeafNode( Long nodeId );

  public List<Node> allChildNodesUnderParentNodes( Long nodeId );

  public List<Node> allChildNodesUnderParentNodes( Long nodeId, AssociationRequestCollection associationCollection );

  public List<Node> generateEnrollmentCodes();

  public boolean isAnyNodeWithOutEnrollmentCode();

  public Integer getNodesHavingEnrollmentCodesCount();

  public List<Node> getNodesHavingEnrollmentCodes( Integer pageNumber, Integer pageSize );

  public Node getNodeByEnrollmentCode( String enrollmentCode );

  public List<FormattedValueBean> getUserNodesAndBelow( Long userId );

  public List<Node> getNodeAndNodesBelow( Long nodeId );

  public Set getNodeManagersForUser( User user, Node node );

  public Set getNodeOwnerOrManagerForUser( User user, Node node );

  public User getNodeOwnerForUser( User user, Node node );

  public void deleteNodeCharacteristics( NodeCharacteristic nodeCharacteristic );

  public Integer getPaxCountBasedOnNodes( Long nodeId, Boolean isIncludeChild );

  public int getCountByNodeName( String startsWith );

  public boolean isUserInNodeorBelow( Long searchUserId, Long loggedInUserid );

  public Node getNodeByHierarchyIdAndNodeId( Long hierarchyId, Long nodeId );

  public boolean isOwnerAlreadyInUserNode( Long userId, Long nodeId );

  public Long getNodeIdByRosterNodeId( UUID rosterNodeId );

  public UUID getRosterNodeIdByNodeId( Long nodeId );

  public List<String> getRosterNodeIdsByNodeIds( List<Long> nodeIds );
  
  public Node getNodeByRosterNodeId( UUID rosterNodeId );

}

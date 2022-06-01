/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/hierarchy/impl/NodeServiceImpl.java,v $
 */

package com.biperf.core.service.hierarchy.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import com.biperf.core.dao.budget.BudgetMasterDAO;
import com.biperf.core.dao.hierarchy.HierarchyDAO;
import com.biperf.core.dao.hierarchy.NodeDAO;
import com.biperf.core.dao.hierarchy.NodeTypeCharacteristicDAO;
import com.biperf.core.dao.hierarchy.NodeTypeDAO;
import com.biperf.core.domain.budget.Budget;
import com.biperf.core.domain.characteristic.Characteristic;
import com.biperf.core.domain.characteristic.NodeTypeCharacteristicType;
import com.biperf.core.domain.enums.BudgetStatusType;
import com.biperf.core.domain.enums.CharacteristicDataType;
import com.biperf.core.domain.enums.HierarchyRoleType;
import com.biperf.core.domain.hierarchy.Hierarchy;
import com.biperf.core.domain.hierarchy.Node;
import com.biperf.core.domain.hierarchy.NodeCharacteristic;
import com.biperf.core.domain.hierarchy.NodeType;
import com.biperf.core.domain.user.User;
import com.biperf.core.domain.user.UserNode;
import com.biperf.core.exception.BeaconRuntimeException;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.exception.ServiceErrorExceptionWithRollback;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.hierarchy.NodeSearchCriteria;
import com.biperf.core.service.hierarchy.NodeService;
import com.biperf.core.service.hierarchy.NodeToNodeCharacteristicAssociationRequest;
import com.biperf.core.service.hierarchy.NodeTypeCharacteristicService;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.service.util.ServiceError;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.utils.HibernateSessionManager;
import com.biperf.core.utils.StringUtil;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.FormattedValueBean;

/**
 * NodeServiceImpl.
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
public class NodeServiceImpl implements NodeService
{
  private HierarchyDAO hierarchyDAO;

  private NodeDAO nodeDAO;

  private NodeTypeDAO nodeTypeDAO;

  private NodeTypeCharacteristicDAO characteristicDAO;

  private UserService userService;

  private BudgetMasterDAO budgetDAO;

  private NodeTypeCharacteristicService nodeTypeCharacteristicService;

  private static final String ALPHA_NUM = "23456789ABCDEFGHJKMNPQRSTUVWXYZabcdefghjkmnpqrstuvwxyz";

  /**
   * 
   * @param nodeTypeCharacteristicService
   */
  public void setNodeTypeCharacteristicService( NodeTypeCharacteristicService nodeTypeCharacteristicService )
  {
    this.nodeTypeCharacteristicService = nodeTypeCharacteristicService;
  }

  /**
   * Sets the UserDAO through IoC.
   * 
   * @param userService
   */
  public void setUserService( UserService userService )
  {
    this.userService = userService;
  }

  /**
   * Sets the HierarchyDAO through IoC.
   * 
   * @param hierarchyDAO
   */
  public void setHierarchyDAO( HierarchyDAO hierarchyDAO )
  {
    this.hierarchyDAO = hierarchyDAO;
  }

  /**
   * Sets the NodeTypeDAO through IoC.
   * 
   * @param nodeTypeDAO
   */
  public void setNodeTypeDAO( NodeTypeDAO nodeTypeDAO )
  {
    this.nodeTypeDAO = nodeTypeDAO;
  }

  /**
   * Sets the NodeDAO through IoC.
   * 
   * @param nodeDAO
   */
  public void setNodeDAO( NodeDAO nodeDAO )
  {
    this.nodeDAO = nodeDAO;
  }

  public void setBudgetDAO( BudgetMasterDAO budgetDAO )
  {
    this.budgetDAO = budgetDAO;
  }

  /**
   * Saves the Node to the database. Overridden from The path is a list of parent nodes in the
   * hierarchy. If a node is moved that has children the path will need to be adjusted as well as
   * the parentId
   * 
   * @see com.biperf.core.service.hierarchy.NodeService#saveNode(com.biperf.core.domain.hierarchy.Node
   *      node, Long parentId, boolean updateDescendantsPath)
   * @param node
   * @return Node
   */
  public Node saveNode( Node node, Long parentId, boolean updateDescendantsPath )
  {
    boolean isTransient = false;
    if ( node.getId() == null )
    {
      isTransient = true;
    }

    // populated parent node needed for hibernate foreign key relationship + for calculating path
    if ( parentId != null )
    {
      Node parentNode = nodeDAO.getNodeById( parentId );
      node.setParentNode( parentNode );
    }
    else
    {
      node.setParentNode( null );
    }
    String oldPath = node.getPath();

    node.setPath( node.calculatePath() );

    // Set reference items - those with id set but with no version
    node.setHierarchy( hierarchyDAO.getById( node.getHierarchy().getId() ) );
    node.setNodeType( nodeTypeDAO.getNodeTypeById( node.getNodeType().getId() ) );
    for ( Iterator iter = node.getNodeCharacteristics().iterator(); iter.hasNext(); )
    {
      NodeCharacteristic nodeCharacteristic = (NodeCharacteristic)iter.next();
      nodeCharacteristic.setNode( node );
      nodeCharacteristic.setNodeTypeCharacteristicType( (NodeTypeCharacteristicType)characteristicDAO.getCharacteristicById( nodeCharacteristic.getNodeTypeCharacteristicType().getId() ) );

    }

    Node savedNode = nodeDAO.saveNode( node );

    if ( !isTransient && oldPath != null && updateDescendantsPath )
    {
      // Update path of all descendants of the saved node

      String newPathSubstring = savedNode.getPath();
      updateDescendantsPath( savedNode, oldPath, newPathSubstring );
    }

    return savedNode;
  }

  /**
   * Update the path of each descendant node of BaseNode, replacing for each descendant node the
   * oldPathSubstring with the newPathSubstring.
   * @param savedNode
   * @param oldPathSubstring
   * @param newPathSubstring
   */
  public void updateDescendantsPath( Node savedNode, String oldPathSubstring, String newPathSubstring )
  {
    // Paths must begin with a slash, throw error since would be a strange scenerio to happen and
    // just
    // adding the slash would probably not fix the problem.
    if ( !oldPathSubstring.startsWith( Node.NODE_PATH_DELIMITER ) )
    {
      throw new BeaconRuntimeException( "oldPathSubstring should have started with the path delimieter, but did not: " + oldPathSubstring );
    }
    if ( !newPathSubstring.startsWith( Node.NODE_PATH_DELIMITER ) )
    {
      throw new BeaconRuntimeException( "newPathSubstring should have started with the path delimieter, but did not: " + newPathSubstring );
    }

    // Add "/" to ends to insure that we are always comparing against full nodes.
    if ( !oldPathSubstring.endsWith( Node.NODE_PATH_DELIMITER ) )
    {
      oldPathSubstring = oldPathSubstring + Node.NODE_PATH_DELIMITER;
    }
    if ( !newPathSubstring.endsWith( Node.NODE_PATH_DELIMITER ) || isNewPathRootPath( newPathSubstring ) )
    {
      newPathSubstring = newPathSubstring + Node.NODE_PATH_DELIMITER;
    }

    // update node's descendants path if path has changed
    if ( !oldPathSubstring.equals( newPathSubstring ) )
    {
      nodeDAO.updateDescendantsPath( savedNode, oldPathSubstring, newPathSubstring );
    }
  }

  private boolean isNewPathRootPath( String newPathSubstring )
  {
    return newPathSubstring.equals( Node.NODE_PATH_DELIMITER );
  }

  /**
   * Get the node from the database with the matching Id param. Overridden from
   * 
   * @see com.biperf.core.service.hierarchy.NodeService#getNodeById(java.lang.Long)
   * @param id
   * @return Node
   */
  public Node getNodeById( Long id )
  {
    return this.nodeDAO.getNodeById( id );
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.hierarchy.NodeService#getNodeWithAssociationsById(java.lang.Long,
   *      com.biperf.core.service.AssociationRequestCollection)
   * @param id
   * @param associationRequestCollection
   * @return List
   */
  public Node getNodeWithAssociationsById( Long id, AssociationRequestCollection associationRequestCollection )
  {
    Node node = this.nodeDAO.getNodeById( id );
    if ( associationRequestCollection != null )
    {
      associationRequestCollection.process( node );
    }

    return node;
  }

  /**
   * Get the node from the database by the name param and Hierarchy.
   * 
   * @param nodeName
   * @param hierarchy
   * @return Node
   */
  public Node getNodeByNameAndHierarchy( String nodeName, Hierarchy hierarchy )
  {
    return this.nodeDAO.getNodeByNameAndHierarchy( nodeName, hierarchy );
  }

  /**
   * Get the node from the database by the name param and Hierarchy.
   * 
   * @param nodeName
   * @param hierarchy
   * @param associationRequestCollection
   * @return Node
   */
  public Node getNodeByNameAndHierarchy( String nodeName, Hierarchy hierarchy, AssociationRequestCollection associationRequestCollection )
  {
    Node node = this.nodeDAO.getNodeByNameAndHierarchy( nodeName, hierarchy );

    if ( associationRequestCollection != null )
    {
      associationRequestCollection.process( node );
    }

    return node;
  }

  /**
   * Search the database for the Nodes with the matching criteria. Overridden from
   * 
   * @see com.biperf.core.service.hierarchy.NodeService#searchNode(java.lang.String,
   *      java.lang.String, java.lang.String)
   * @param name
   * @param description
   * @return List
   */
  public List searchNode( String name, String description, String deleted )
  {
    return this.nodeDAO.searchNode( name, description, deleted );
  }

  /**
   * Search a node by criterion of hierarchyId/partialNodeName/nodeTypeId Overridden from
   * 
   * @see com.biperf.core.service.hierarchy.NodeService#searchNode(java.lang.Long, java.lang.String,
   *      java.lang.Long)
   * @param hierarchyId
   * @param partialNodeName
   * @param nodeTypeId
   * @return List
   */
  public List searchNode( Long hierarchyId, String partialNodeName, Long nodeTypeId )
  {
    return this.nodeDAO.searchNode( hierarchyId, partialNodeName, nodeTypeId );
  }

  /**
   * Get all Nodes in the database. Overridden from
   * 
   * @see com.biperf.core.service.hierarchy.NodeService#getAll()
   * @return List
   */
  public List getAll()
  {
    return this.nodeDAO.getAll();
  }

  /**
   * Get all Nodes for the specified association object in the database.
   * 
   * @param associationRequestCollection 
   * @return List
   */
  public List getAll( AssociationRequestCollection associationRequestCollection )
  {
    List nodeList = getAll();
    if ( associationRequestCollection != null )
    {
      associationRequestCollection.process( nodeList );
    }
    return nodeList;
  }

  /**
   * Gets a list of all Nodes excluding the Node param. Overridden from
   * 
   * @see com.biperf.core.service.hierarchy.NodeService#getAllExcludingNode(com.biperf.core.domain.hierarchy.Node)
   * @param node
   * @return List
   */
  public List getAllExcludingNode( Node node )
  {
    return this.nodeDAO.getAllExcludingNode( node );
  }

  /**
   * Deletes the node from the database. Overridden from
   * 
   * @see com.biperf.core.service.hierarchy.NodeService#deleteNode(com.biperf.core.domain.hierarchy.Node)
   * @param node
   */
  public void deleteNode( Node node )
  {
    // Change the name of the node so that the name can be reused
    String nodeName = node.getName();
    Timestamp timestamp = new Timestamp( new Date().getTime() );
    node.setName( nodeName + "-" + timestamp );
    node.setDeleted( true );
    this.nodeDAO.saveNode( node );
    List budgetList = this.budgetDAO.getBudgetForNodeId( node.getId() );
    for ( Iterator iter = budgetList.iterator(); iter.hasNext(); )
    {
      Budget budget = (Budget)iter.next();
      if ( !budget.getStatus().getName().equalsIgnoreCase( BudgetStatusType.lookup( BudgetStatusType.CLOSED ).getName() ) )
      {
        budget.setStatus( BudgetStatusType.lookup( BudgetStatusType.CLOSED ) );
        budgetDAO.updateBudget( budget );
      }
    }
  }

  /**
   * Get the list of nodes for the specified hierarchy
   * 
   * @param hierarchyId
   * @return List of Node objects
   */
  public List getNodesByHierarchy( Long hierarchyId )
  {
    Hierarchy hierarchy = this.hierarchyDAO.getById( hierarchyId );

    return this.nodeDAO.getNodesByHierarchy( hierarchy );
  }

  /**
   * Get the list of nodes as hierarchy
   * 
   * @param hierarchyId
   * @return List of Node objects
   */
  public List getNodesAsHierarchy( Long hierarchyId )
  {
    return this.nodeDAO.getNodesAsHierarchy( hierarchyId );
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.hierarchy.NodeService#getChildNodesWithAssociationsByParent(java.lang.Long,
   *      com.biperf.core.service.AssociationRequestCollection)
   * @param parentNodeId
   * @param associationRequestCollection
   * @return List
   */
  public List getChildNodesWithAssociationsByParent( Long parentNodeId, AssociationRequestCollection associationRequestCollection )
  {
    List childNodeList = this.nodeDAO.getChildNodesByParent( parentNodeId );
    if ( associationRequestCollection != null )
    {
      associationRequestCollection.process( childNodeList );
    }
    return childNodeList;
  }

  /**
   * Get a list of children nodes for a parent Id.
   * 
   * @param parentNodeId
   * @return List
   */
  public List getChildrenNodes( Long parentNodeId )
  {
    return this.nodeDAO.getChildNodesByParent( parentNodeId );
  }

  /**
   * Disassociates the list of participants (ids) from the given Node Will throw an exception if a
   * listed user is not associated with another node (i.e., you may not remove the user from a node
   * until it is already associated with another one)
   * 
   * @param nodeId
   * @param listOfParticipantIds should be a List of Long objects
   * @throws ServiceErrorException
   */
  public void removeParticipantsFromNode( Long nodeId, List listOfParticipantIds ) throws ServiceErrorException
  {

    List errors = new ArrayList();

    Node node = getNodeById( nodeId );
    Set userNodes = node.getUserNodes();
    for ( Iterator iterator = userNodes.iterator(); iterator.hasNext(); )
    {
      UserNode userNode = (UserNode)iterator.next();
      if ( listOfParticipantIds.contains( userNode.getUser().getId() ) )
      {
        validateUserHasTwoNodeAssociations( userNode.getUser(), errors );

        userNode.getUser().getUserNodes().remove( userNode );
        iterator.remove();
      }
    }

    if ( !errors.isEmpty() )
    {
      throw new ServiceErrorExceptionWithRollback( errors );
    }

  }

  /**
   * Will add an error message to the list if this user has less than 2 nodes associated with it
   * 
   * @param user
   * @param errors
   */
  protected void validateUserHasTwoNodeAssociations( User user, List errors )
  {
    if ( user.getUserNodes().size() < 2 )
    {
      errors.add( new ServiceError( ServiceErrorMessageKeys.NODE_PARTICIPANT_ORPHAN, user.getLastName() + ", " + user.getFirstName() ) );
    }
  }

  /**
   * Retrieves the root node for the specified hierarchyId id
   * 
   * @param hierarchyId
   * @param associationRequestCollection
   * @return Node
   */
  public Node getRootNode( Long hierarchyId, AssociationRequestCollection associationRequestCollection )
  {
    if ( hierarchyId == null )
    {
      return null;
    }

    Hierarchy hierarchy = hierarchyDAO.getById( hierarchyId );

    Node rootNode = nodeDAO.getRootNode( hierarchy );

    if ( rootNode != null && associationRequestCollection != null )
    {
      associationRequestCollection.process( rootNode );
    }

    return rootNode;
  }

  /**
   * Manages moving the childrenNode and participants to the selected nodes.
   * 
   * @param deletedNodeId
   * @param nodeToHoldChildrenId
   * @param nodeToHoldUsersId
   */
  public void updateNodeAndDelete( Long deletedNodeId, Long nodeToHoldChildrenId, Long nodeToHoldUsersId )
  {

    // Get the old Node
    // AssociationRequestCollection nodeChildrenCollection = new AssociationRequestCollection();
    // nodeChildrenCollection.add( new NodeToUserNodesAssociationRequest() );

    Node deletedNode = this.getNodeById( deletedNodeId );

    String oldPathSubstring = deletedNode.getPath();

    List childrenNodes = this.getChildrenNodes( deletedNode.getId() );

    if ( nodeToHoldChildrenId != null && nodeToHoldChildrenId.longValue() != 0 && childrenNodes.size() > 0 )
    {

      // Get the node elected to become the new parent node
      // Move the children from the old node
      Node nodeToHoldChildrenFromDeletedNode = this.nodeDAO.getNodeById( nodeToHoldChildrenId );

      Iterator childrenNodeIter = childrenNodes.iterator();

      while ( childrenNodeIter.hasNext() )
      {
        Node childNode = (Node)childrenNodeIter.next();

        // Add new parent to childNode
        childNode.setParentNode( nodeToHoldChildrenFromDeletedNode );
        this.nodeDAO.saveNode( childNode );
      }

      // Update path of all descendants of new parent node (somewhat inefficient since existing
      // children won't have any path changes)
      String newPathSubstring = nodeToHoldChildrenFromDeletedNode.getPath();
      updateDescendantsPath( nodeToHoldChildrenFromDeletedNode, oldPathSubstring, newPathSubstring );

    }

    List budgetList = this.budgetDAO.getBudgetForNodeId( deletedNodeId );
    for ( Iterator iter = budgetList.iterator(); iter.hasNext(); )
    {
      Budget budget = (Budget)iter.next();
      if ( !budget.getStatus().getName().equalsIgnoreCase( BudgetStatusType.lookup( BudgetStatusType.CLOSED ).getName() ) )
      {
        budget.setStatus( BudgetStatusType.lookup( BudgetStatusType.CLOSED ) );
        budgetDAO.updateBudget( budget );
      }
    }

    if ( nodeToHoldUsersId != null && nodeToHoldUsersId.longValue() != 0 )
    {

      // Get the node elected to move all participants to
      // Move the participants to the new node
      Node nodeToHoldUsersFromDeletedNode = this.nodeDAO.getNodeById( nodeToHoldUsersId );
      userService.updateUserNodeChangeNode( deletedNode.getId(), nodeToHoldUsersFromDeletedNode.getId() );

    }

    // Logically delete the old node
    this.deleteNode( deletedNode );

  }

  public List getListOfNodeIdsAndUserIdsByImportFile( Long importFileId )
  {
    return nodeDAO.getListOfNodeIdsAndUserIdsByImportFile( importFileId );
  }

  /**
   * For live promotions that have an approval Node Type and hierarchy set, return all nodes
   * matching the promotion node type and hierarchy that have no owner for nodes.
   * @return List
   */
  public List getNodeTypeApproverPromotionApprovalNodeswithNoOwner()
  {
    return nodeDAO.getNodeTypeApproverPromotionApprovalNodeswithNoOwner();
  }

  /**
   * @param characteristicDAO value for characteristicDAO property
   */
  public void setCharacteristicDAO( NodeTypeCharacteristicDAO characteristicDAO )
  {
    this.characteristicDAO = characteristicDAO;
  }

  /**
   * returns a list of nodes by nodeType
   * 
   * @param nodeType
   * @return List of Node objects
   */
  public List getNodesByNodeType( NodeType nodeType )
  {
    return nodeDAO.getNodesByNodeType( nodeType );
  }

  public List getNodesByNodeTypeAndHierarchy( NodeType nodeType, Long hierarchyId )
  {
    return nodeDAO.getNodesByNodeTypeAndHierarchy( nodeType, hierarchyId );
  }

  /**
   * Retrieves the list of nodes which have the specified user in the sepecified hierarchyRoleType.
   * 
   * @param user
   * @param hierarchyRoleType
   * @return List of Node
   */
  public List getNodesWithUserHavingRole( User user, HierarchyRoleType hierarchyRoleType )
  {
    return nodeDAO.getNodesWithUserHavingRole( user, hierarchyRoleType );

  }

  /**
   * Get the node from the database by location code, which is stored in the node characteristic,
   * expected to be unique per node.
   * 
   * @param locCode
   * @return Node
   */
  public Node getNodeByRegistrationLocCode( String locCode )
  {
    return nodeDAO.getNodeByRegistrationLocCode( locCode );
  }

  /**
   * Creates random location codes for nodes and assigns them as node type characteristics.  The 
   *      location code is a 6 digit number.  
   */
  public void generateNodeRegistrationLocCodes()
  {

    // first get all of the node types and add characteristics to each with "NT" as type
    // and node_type_id as domain_id if the node_type_characteristic doesn't exist.
    List nodeTypeList = nodeTypeDAO.getAll();
    addCharacteristicsForNodeTypes( nodeTypeList );
    // FLUSH: The flush was needed after hibernate 3.2.3 upgrade not
    // doing flush before fetch.
    HibernateSessionManager.getSession().flush();

    // Second get all of the nodes and add NodeCharacteristics to each where the characteristic
    // value is the new random location code for the node if the NodeCharacteristic doesn't exist.
    AssociationRequestCollection nodeAssociationRequestCollection = new AssociationRequestCollection();
    nodeAssociationRequestCollection.add( new NodeToNodeCharacteristicAssociationRequest() );
    List nodeList = getAll( nodeAssociationRequestCollection );

    // Loop through the node List
    for ( Iterator nodeIter = nodeList.iterator(); nodeIter.hasNext(); )
    {
      Node node = (Node)nodeIter.next();
      Set nodeCharacteristicList = node.getNodeCharacteristics();

      boolean nodeCharacteristicFound = false;

      // Loop through the node characteristic list
      for ( Iterator nodeCharIter = nodeCharacteristicList.iterator(); nodeCharIter.hasNext(); )
      {
        NodeCharacteristic nodeCharacteristic = (NodeCharacteristic)nodeCharIter.next();
        // if the characteristic is found end the loop as we don't need to add a node characteristic
        // for this node.
        if ( nodeCharacteristic.getNodeTypeCharacteristicType().getCharacteristicName().equals( NodeTypeCharacteristicType.CHARACTERISTIC_REG_CODE_LOC_NAME ) )
        {
          nodeCharacteristicFound = true;
          break;
        }
      }

      if ( !nodeCharacteristicFound )
      {
        addNodeCharacteristic( node );
      }
    }
  }

  /**
   * Add characteristic with "NT" type for each node type
   * 
   *  The domain_id in the characteristic is the node_type_id
   */
  private void addCharacteristicsForNodeTypes( List nodeTypeList )
  {

    // Loop through all node types
    for ( Iterator nodeTypeIter = nodeTypeList.iterator(); nodeTypeIter.hasNext(); )
    {
      NodeType nodeType = (NodeType)nodeTypeIter.next();

      Characteristic characteristicFound = getCharacteristicForNodeTypeLocation( nodeType );
      // If the characteristic does not exist, add it
      if ( characteristicFound == null )
      {
        addCharacteristicForNodeType( nodeType );
      }
    }
  }

  private Characteristic getCharacteristicForNodeTypeLocation( NodeType nodeType )
  {
    if ( nodeType != null )
    {
      List characteristics = nodeTypeCharacteristicService.getAllNodeTypeCharacteristicTypesByNodeTypeId( nodeType.getId() );

      // Loop through the characteristics for this node type to find the REG_CODE_LOCATION
      // characteristic
      for ( Iterator charIter = characteristics.iterator(); charIter.hasNext(); )
      {
        Characteristic characteristic = (Characteristic)charIter.next();
        if ( characteristic.getCharacteristicName().equals( NodeTypeCharacteristicType.CHARACTERISTIC_REG_CODE_LOC_NAME ) )
        {
          // return the characteristic
          return characteristic;
        }
      }
    }
    return null;
  }

  /**
   * Add characteristic with "NT" type for the node type
   * 
   *  The domain_id in the characteristic is the node_type_id
   */
  private void addCharacteristicForNodeType( NodeType nodeType )
  {

    NodeTypeCharacteristicType nodeTypeCharacteristicType = new NodeTypeCharacteristicType();

    nodeTypeCharacteristicType.setDomainId( nodeType.getId() );

    nodeTypeCharacteristicType.setId( null );
    nodeTypeCharacteristicType.setVersion( null );

    nodeTypeCharacteristicType.setDescription( NodeTypeCharacteristicType.CHARACTERISTIC_NODE_TYPE_DESC );
    nodeTypeCharacteristicType.setCharacteristicName( NodeTypeCharacteristicType.CHARACTERISTIC_REG_CODE_LOC_NAME );

    nodeTypeCharacteristicType.setCharacteristicDataType( CharacteristicDataType.lookup( CharacteristicDataType.INTEGER ) );
    nodeTypeCharacteristicType.setMinValue( new BigDecimal( NodeTypeCharacteristicType.CHARACTERISTIC_NODE_TYPE_MIN_VALUE ) );
    nodeTypeCharacteristicType.setMaxValue( new BigDecimal( NodeTypeCharacteristicType.CHARACTERISTIC_NODE_TYPE_MAX_VALUE ) );

    // setting to blank so system generates new cm key and code
    nodeTypeCharacteristicType.setNameCmKey( "" );
    nodeTypeCharacteristicType.setCmAssetCode( "" );

    nodeTypeCharacteristicType.setIsRequired( NodeTypeCharacteristicType.CHARACTERISTIC_NODE_TYPE_REQUIRED );
    nodeTypeCharacteristicType.setActive( true );

    nodeTypeCharacteristicType.getAuditCreateInfo().setCreatedBy( UserManager.getUserId() );
    nodeTypeCharacteristicType.getAuditCreateInfo().setDateCreated( new Timestamp( new Date().getTime() ) );

    try
    {
      nodeTypeCharacteristicService.saveCharacteristic( nodeTypeCharacteristicType );
    }
    catch( ServiceErrorException e )
    {
    }
  }

  /**
   * Add the Node Characteristic
   * 
   * @param node
   */
  private void addNodeCharacteristic( Node node )
  {
    NodeCharacteristic nodeCharacteristic = new NodeCharacteristic();

    nodeCharacteristic.setNode( node );
    nodeCharacteristic.setNodeTypeCharacteristicType( (NodeTypeCharacteristicType)getCharacteristicForNodeTypeLocation( node.getNodeType() ) );
    nodeCharacteristic.setVersion( new Long( NodeTypeCharacteristicType.NODE_CHARACTERISTIC_VERSION ) );
    nodeCharacteristic.setCharacteristicValue( generateRegistrationLocationCode() );
    node.addNodeCharacteristic( nodeCharacteristic );
    saveNode( node, node.getParentNode() == null ? null : node.getParentNode().getId(), false );
  }

  private String generateRegistrationLocationCode()
  {
    long random;
    final long MAX = 999999, MIN = 100000;
    random = (long) ( Math.floor( Math.random() * ( MAX - MIN + 1 ) ) + MIN );
    return String.valueOf( random );
  }

  /**
   * 
   * Overridden from @see com.biperf.core.service.hierarchy.NodeService#getNumberOfUserNodesById(java.lang.Long)
   * @param id
   * @return
   */
  public Long getNumberOfUserNodesById( Long nodeId )
  {
    return nodeDAO.getNumberOfUserNodesById( nodeId );
  }

  /**
   * Search the database with the given search criteria params. Overridden from
   * 
   * @see com.biperf.core.dao.hierachary.NodeDAO#searchNode(com.biperf.core.service.hierachary.NodeSearchCriteria)
   * @param searchCriteria
   * @return List
   */
  public List<Node> searchNode( NodeSearchCriteria searchCriteria )
  {
    return nodeDAO.searchNode( searchCriteria );
  }

  public User getNodeOwnerByLevel( Long nodeId )
  {
    return nodeDAO.getNodeOwnerByLevel( nodeId );
  }

  public List<Long> findOwnerNodeIds( Long userId )
  {
    return nodeDAO.findOwnerNodeIds( userId );
  }

  public List<Long> findChildNodeIds( Long nodeId )
  {
    return nodeDAO.findChildNodeIds( nodeId );
  }

  public boolean nodeHasOwner( Long nodeId )
  {
    return nodeDAO.nodeHasOwner( nodeId );
  }

  public List findNodesAsHierarchyByUser( Long userId )
  {
    return nodeDAO.findNodesAsHierarchyByUser( userId );
  }

  public boolean isLeafNode( Long nodeId )
  {
    return nodeDAO.isLeafNode( nodeId );
  }

  @SuppressWarnings( "unchecked" )
  public List<Node> allChildNodesUnderParentNodes( Long nodeId )
  {
    return nodeDAO.allChildNodesUnderParentNodes( nodeId );
  }

  @SuppressWarnings( "unchecked" )
  public List<Node> allChildNodesUnderParentNodes( Long nodeId, AssociationRequestCollection associationRequestCollection )
  {
    List<Node> childNodeList = nodeDAO.allChildNodesUnderParentNodes( nodeId );
    if ( associationRequestCollection != null )
    {
      associationRequestCollection.process( childNodeList );
    }

    return childNodeList;
  }

  public List<Node> generateEnrollmentCodes()
  {
    @SuppressWarnings( "unchecked" )
    List<Node> nodes = this.nodeDAO.getAll();
    List<String> codes = new ArrayList<String>();
    List<String> newCodes = new ArrayList<String>();

    // copy all existing enrollment codes to list
    for ( Node node : nodes )
    {
      if ( !StringUtil.isEmpty( node.getSelfEnrollmentCode() ) )
      {
        codes.add( node.getSelfEnrollmentCode() );
      }
    }

    // get nodes not having enrollment codes
    List<Node> nodesNotHavingEnrollmentCodes = this.nodeDAO.getNodesNotHavingEnrollmentCodes( null, null );

    // generate new unique enrollment codes for nodes not having enrollment codes
    while ( codes.size() != nodes.size() )
    {
      String code = getAlphaNumeric( 16 );
      if ( !codes.contains( code ) )
      {
        codes.add( code );
        newCodes.add( code );
      }
    }

    // for each node not having enrollment code
    for ( int i = 0; i < nodesNotHavingEnrollmentCodes.size(); i++ )
    {
      // for each new enrollment code. Copy to node
      for ( int j = 0; j < newCodes.size(); j++ )
      {
        if ( i == j )
        {
          nodesNotHavingEnrollmentCodes.get( i ).setSelfEnrollmentCode( newCodes.get( j ) );
          break;
        }
      }
    }
    return nodesNotHavingEnrollmentCodes;
  }

  public boolean isAnyNodeWithOutEnrollmentCode()
  {
    return this.nodeDAO.isAnyNodeWithOutEnrollmentCode();
  }

  public Integer getNodesHavingEnrollmentCodesCount()
  {
    return this.nodeDAO.getNodesHavingEnrollmentCodesCount();
  }

  public List<Node> getNodesHavingEnrollmentCodes( Integer pageNumber, Integer pageSize )
  {
    return this.nodeDAO.getNodesHavingEnrollmentCodes( pageNumber, pageSize );
  }

  public Node getNodeByEnrollmentCode( String enrollmentCode )
  {
    return this.nodeDAO.getNodeByEnrollmentCode( enrollmentCode );
  }

  private String getAlphaNumeric( int len )
  {
    StringBuffer sb = new StringBuffer( len );
    for ( int i = 0; i < len; i++ )
    {
      int ndx = (int) ( Math.random() * ALPHA_NUM.length() );
      sb.append( ALPHA_NUM.charAt( ndx ) );
    }
    return sb.toString();
  }

  /**
   * Get a list of children nodes for a parent Id.
   * 
   * @param parentNodeId
   * @return List
   */
  public List<FormattedValueBean> getUserNodesAndBelow( Long userId )
  {
    return this.nodeDAO.getUserNodesAndBelow( userId );
  }

  public List<Node> getNodeAndNodesBelow( Long nodeId )
  {
    List<Node> nodes = new ArrayList<Node>();
    Node parentNode = this.nodeDAO.getNodeById( nodeId );
    nodes.add( parentNode );

    List<Node> childNodes = this.nodeDAO.getChildNodesByParent( nodeId );
    nodes.addAll( childNodes );
    return nodes;
  }

  /**
   * Gets the Set of the given User's Managers. Make sure this is hydrated!!
   * 
   * @param user
   * @return Set of all of the Managers for given user's node.
   */
  public Set getNodeManagersForUser( User user, Node node )
  {
    Set nodeManagers = new HashSet();

    Node currentNode = node;

    if ( user.isMemberOf( currentNode ) )
    {
      Set managers = this.nodeDAO.getUsersByRole( currentNode.getId(), HierarchyRoleType.MANAGER );
      if ( !managers.isEmpty() )
      {
        nodeManagers.addAll( managers );
      }
      else
      {
        while ( currentNode != null && nodeManagers.isEmpty() )
        {
          Set owners = this.nodeDAO.getUsersByRole( currentNode.getId(), HierarchyRoleType.OWNER );
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
        Set owners = this.nodeDAO.getUsersByRole( currentNode.getId(), HierarchyRoleType.OWNER );
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
        Set owners = this.nodeDAO.getUsersByRole( currentNode.getId(), HierarchyRoleType.OWNER );
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
   * Gets the Set of the given User's Managers. Make sure this is hydrated!!
   * 
   * @param user
   * @return Set of all of the Managers for given user's node.
   */
  public Set<User> getNodeOwnerOrManagerForUser( User user, Node node )
  {
    Set<User> nodeOwnersORManagers = new HashSet<User>();
    Node currentNode = node;
    if ( user.isOwnerOf( currentNode ) )
    {
      currentNode = currentNode.getParentNode();
      nodeOwnersORManagers = getNodeOwnerOrManagerForUser( node );
    }
    nodeOwnersORManagers = getNodeOwnerOrManagerForUser( node );
    return nodeOwnersORManagers;
  }

  private Set<User> getNodeOwnerOrManagerForUser( Node node )
  {
    Set<User> nodeOwnersORManagers = new HashSet<User>();
    Node currentNode = node;

    while ( currentNode != null && nodeOwnersORManagers.isEmpty() )
    {
      Set<User> owners = this.nodeDAO.getUsersByRole( currentNode.getId(), HierarchyRoleType.OWNER );

      if ( !owners.isEmpty() )
      {
        nodeOwnersORManagers.addAll( owners );
        return nodeOwnersORManagers;
      }
      Set<User> managers = this.nodeDAO.getUsersByRole( currentNode.getId(), HierarchyRoleType.MANAGER );
      if ( !managers.isEmpty() )
      {
        nodeOwnersORManagers.addAll( managers );
        return nodeOwnersORManagers;
      }
      else
      {
        currentNode = currentNode.getParentNode();
      }
    }
    return nodeOwnersORManagers;
  }

  /**
   * Gets the Set of the given User's Managers. Make sure this is hydrated!!
   * 
   * @param user
   * @return Set of all of the Managers for given user's node.
   */
  public User getNodeOwnerForUser( User user, Node node )
  {
    User nodeOwner = null;
    Set nodeOwners = new HashSet();
    Set nodeManagers = new HashSet();
    Node currentNode = node;

    if ( user.isMemberOf( currentNode ) || user.isManagerOf( currentNode ) )
    {
      while ( currentNode != null && nodeOwners.isEmpty() && nodeManagers.isEmpty() )
      {
        Set owners = this.nodeDAO.getUsersByRole( currentNode.getId(), HierarchyRoleType.OWNER );
        Set managers = this.nodeDAO.getUsersByRole( currentNode.getId(), HierarchyRoleType.MANAGER );
        if ( !owners.isEmpty() )
        {
          nodeOwners.addAll( owners );
        }
        if ( !managers.isEmpty() )
        {
          nodeManagers.addAll( managers );
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
      while ( currentNode != null && nodeOwners.isEmpty() && nodeManagers.isEmpty() )
      {
        Set owners = this.nodeDAO.getUsersByRole( currentNode.getId(), HierarchyRoleType.OWNER );
        Set managers = this.nodeDAO.getUsersByRole( currentNode.getId(), HierarchyRoleType.MANAGER );
        if ( !owners.isEmpty() )
        {
          nodeOwners.addAll( owners );
        }
        if ( !managers.isEmpty() )
        {
          nodeManagers.addAll( managers );
        }
        else
        {
          currentNode = currentNode.getParentNode();
        }
      }
    }

    // node owner should be just one per node
    for ( Iterator nodeOwnersIter = nodeOwners.iterator(); nodeOwnersIter.hasNext(); )
    {
      nodeOwner = (User)nodeOwnersIter.next();
      break;
    }

    return nodeOwner;
  }

  @Override
  public Node getNodeByHierarchyIdAndNodeId( Long hierarchyId, Long nodeId )
  {
    return nodeDAO.getNodeByHierarchyIdAndNodeId( hierarchyId, nodeId );
  }

  @Override
  public void deleteNodeCharacteristics( NodeCharacteristic nodeCharacteristic )
  {
    nodeDAO.deleteNodeCharacteristics( nodeCharacteristic );
  }

  @Override
  public Integer getPaxCountBasedOnNodes( Long nodeId, Boolean isIncludeChild )
  {
    return nodeDAO.getPaxCountBasedOnNodes( nodeId, isIncludeChild );
  }

  @Override
  public int getCountByNodeName( String startsWith )
  {
    return nodeDAO.getCountByNodeName( startsWith );
  }

  public boolean isUserInNodeorBelow( Long searchUserId, Long loggedInUserid )
  {
    return nodeDAO.isUserInNodeorBelow( searchUserId, loggedInUserid );
  }

  public boolean isOwnerAlreadyInUserNode( Long userId, Long nodeId )
  {
    return nodeDAO.isOwnerAlreadyInUserNode( userId, nodeId );
  }

  @Override
  public Long getNodeIdByRosterNodeId( UUID rosterNodeId )
  {
    return nodeDAO.getNodeIdByRosterNodeId( rosterNodeId );
  }

  @Override
  public UUID getRosterNodeIdByNodeId( Long nodeId )
  {
    return nodeDAO.getRosterNodeIdByNodeId( nodeId );
  }

  @Override
  public List<String> getRosterNodeIdsByNodeIds( List<Long> nodeIds )
  {
    return nodeDAO.getRosterNodeIdsByNodeIds( nodeIds );
  }

  @Override
  public Node getNodeByRosterNodeId( UUID rosterNodeId )
  {
    return this.nodeDAO.getNodeByRosterNodeId( rosterNodeId );
  }

}

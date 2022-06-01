/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javatest/com/biperf/core/service/hierarchy/impl/NodeServiceImplTest.java,v $
 */

package com.biperf.core.service.hierarchy.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.jmock.Mock;

import com.biperf.core.dao.budget.BudgetMasterDAO;
import com.biperf.core.dao.hierarchy.HierarchyDAO;
import com.biperf.core.dao.hierarchy.NodeDAO;
import com.biperf.core.dao.hierarchy.NodeTypeDAO;
import com.biperf.core.domain.budget.Budget;
import com.biperf.core.domain.enums.BudgetStatusType;
import com.biperf.core.domain.enums.MockPickListFactory;
import com.biperf.core.domain.hierarchy.Hierarchy;
import com.biperf.core.domain.hierarchy.Node;
import com.biperf.core.domain.hierarchy.NodeType;
import com.biperf.core.domain.user.User;
import com.biperf.core.domain.user.UserNode;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.AssociationRequest;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.BaseServiceTest;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.service.util.ServiceError;
import com.biperf.core.service.util.ServiceErrorMessageKeys;

/**
 * NodeServiceImplTest.
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
public class NodeServiceImplTest extends BaseServiceTest// MockObjectTestCase
{
  /**
   * Constructor to take the name of the test.
   * 
   * @param test
   */
  public NodeServiceImplTest( String test )
  {
    super( test );
  }

  /** NodeServiceImpl */
  private NodeServiceImpl nodeService = new NodeServiceImpl();

  /** mockNodeDAO */
  private Mock mockNodeDAO = null;

  /** mockNodeTypeDAO */
  private Mock mockNodeTypeDAO = null;

  /** mockHierarchyDAO */
  private Mock mockHierarchyDAO = null;

  /** mockBudgetMasterDAO */
  private Mock mockBudgetMasterDAO = null;

  /** mockUserService */
  private Mock mockUserService = null;

  private AssociationRequest mockAssociationRequest = new AssociationRequest()
  {
    public void execute( Object domainObject )
    {
    }
  };

  /**
   * Setup this test case. Overridden from
   * 
   * @see junit.framework.TestCase#setUp()
   * @throws Exception
   */
  protected void setUp() throws Exception
  {
    mockNodeDAO = new Mock( NodeDAO.class );
    mockNodeTypeDAO = new Mock( NodeTypeDAO.class );
    mockHierarchyDAO = new Mock( HierarchyDAO.class );
    mockBudgetMasterDAO = new Mock( BudgetMasterDAO.class );
    mockUserService = new Mock( UserService.class );

    nodeService.setNodeDAO( (NodeDAO)mockNodeDAO.proxy() );
    nodeService.setNodeTypeDAO( (NodeTypeDAO)mockNodeTypeDAO.proxy() );
    nodeService.setHierarchyDAO( (HierarchyDAO)mockHierarchyDAO.proxy() );
    nodeService.setBudgetDAO( (BudgetMasterDAO)mockBudgetMasterDAO.proxy() );
    nodeService.setUserService( (UserService)mockUserService.proxy() );
  }

  /**
   * Tests saving the node.
   */
  public void testSave()
  {

    Node parentNode = new Node();
    parentNode.setId( new Long( 100 ) );
    parentNode.setPath( "/" );

    Node node = new Node();
    node.setId( new Long( 1 ) );
    node.setName( "testNAME" );
    node.setDescription( "testDESCRIPTION" );
    node.setParentNode( parentNode );

    Hierarchy hierarchy = buildHierarchy();
    node.setHierarchy( hierarchy );

    NodeType nodeType = new NodeType();
    nodeType.setId( new Long( 1 ) );
    node.setNodeType( nodeType );

    Node expectedNode = node;
    expectedNode.setPath( "/" );

    mockNodeDAO.expects( atLeastOnce() ).method( "saveNode" ).with( same( expectedNode ) ).will( returnValue( expectedNode ) );

    mockNodeTypeDAO.expects( once() ).method( "getNodeTypeById" ).with( same( expectedNode.getNodeType().getId() ) ).will( returnValue( expectedNode.getNodeType() ) );
    mockHierarchyDAO.expects( once() ).method( "getById" ).with( same( expectedNode.getHierarchy().getId() ) ).will( returnValue( expectedNode.getHierarchy() ) );

    mockNodeDAO.expects( once() ).method( "getNodeById" ).will( returnValue( parentNode ) );
    mockNodeDAO.expects( once() ).method( "updateDescendantsPath" );

    Node actualNode = nodeService.saveNode( expectedNode, expectedNode.getParentNode().getId(), true );

    assertEquals( "Actual Node wasn't equals to what was expected", expectedNode, actualNode );

  }

  /**
   * Tests getting the node from the service through the DAO.
   */
  public void testGetNodeById()
  {

    Node expectedNode = new Node();
    expectedNode.setId( new Long( 1 ) );
    expectedNode.setName( "testNAME" );
    expectedNode.setDescription( "testDESCRIPTION" );

    mockNodeDAO.expects( once() ).method( "getNodeById" ).with( same( expectedNode.getId() ) ).will( returnValue( expectedNode ) );

    Node actualNode = nodeService.getNodeById( expectedNode.getId() );

    assertEquals( "Actual Node wasn't equals to what was expected", expectedNode, actualNode );

    mockNodeDAO.verify();
  }

  /**
   * Tests deleting the node from the database through the service.
   */
  public void testUpdateAndDeleteNode()
  {

    // Create the hierachy to which these nodes will belong
    Hierarchy hierarchy = buildHierarchy();

    User user1 = createUser();
    User user2 = createUser();
    User user3 = createUser();

    // Create the node which will be deleted
    Node node = new Node();
    node.setId( new Long( 1 ) );
    node.setName( "testNAME" );
    node.setParentNode( null );
    node.setDescription( "testDESCRIPTION" );
    node.setPath( "/" );
    node.addUser( user1 );
    node.addUser( user2 );
    node.addUser( user3 );

    node.setHierarchy( hierarchy );

    // Create the child node to the node which will be deleted
    Node node2 = new Node();
    node2.setId( new Long( 2 ) );
    node2.setName( "testNAME2" );
    node2.setParentNode( null );
    node2.setDescription( "testDESCRIPTION2" );
    node2.setPath( "//foo" );
    node2.addUser( createUser() );
    node2.setHierarchy( hierarchy );
    node.addChildNode( node2 );

    Node node3 = new Node();
    node3.setId( new Long( 3 ) );
    node3.setName( "testNAME3" );
    node3.setParentNode( null );
    node3.setDescription( "testDESCRIPTION3" );
    node3.setPath( "/" );
    node3.addUser( createUser() );
    node3.addUser( createUser() );
    node3.addUser( createUser() );
    node3.setHierarchy( hierarchy );

    List childList = new ArrayList();
    childList.add( node2 );

    // Create a (user) Budget Record
    Budget budget = new Budget();
    budget.setOriginalValue( BigDecimal.valueOf( 500 ) );
    budget.setCurrentValue( BigDecimal.valueOf( 200 ) );

    budget.setStatus( (BudgetStatusType)MockPickListFactory.getMockPickListItem( BudgetStatusType.class, "active" ) );

    mockNodeDAO.expects( once() ).method( "getNodeById" ).with( same( node.getId() ) ).will( returnValue( node ) );
    mockNodeDAO.expects( once() ).method( "getChildNodesByParent" ).with( same( node.getId() ) ).will( returnValue( childList ) );
    mockNodeDAO.expects( once() ).method( "getNodeById" ).with( same( node3.getId() ) ).will( returnValue( node3 ) );
    mockNodeDAO.expects( once() ).method( "saveNode" ).with( same( node2 ) );

    mockBudgetMasterDAO.expects( once() ).method( "getBudgetForNodeId" ).with( same( node.getId() ) ).will( returnValue( new ArrayList() ) );

    mockNodeDAO.expects( once() ).method( "getNodeById" ).with( same( node3.getId() ) ).will( returnValue( node3 ) );
    mockUserService.expects( once() ).method( "updateUserNodeChangeNode" ).with( same( node.getId() ), same( node3.getId() ) );
    mockNodeDAO.expects( once() ).method( "saveNode" ).with( same( node ) ).will( returnValue( node ) );

    mockBudgetMasterDAO.expects( once() ).method( "getBudgetForNodeId" ).with( same( node.getId() ) ).will( returnValue( new ArrayList() ) );

    mockNodeDAO.expects( atLeastOnce() ).method( "updateDescendantsPath" );

    nodeService.updateNodeAndDelete( node.getId(), node3.getId(), node3.getId() );

    mockNodeDAO.verify();
  }

  /**
   * Test getting all nodes from the database through the service.
   */
  public void testGetAll()
  {

    List expectedList = new ArrayList( getNodeList() );

    mockNodeDAO.expects( once() ).method( "getAll" ).will( returnValue( expectedList ) );

    List actualList = nodeService.getAll();

    assertTrue( "Actual set didn't contain expected set for getAll.", actualList.containsAll( expectedList ) );

    mockNodeDAO.verify();
  }

  /**
   * Test getting all nodes from the database excluding the node param.
   */
  public void testGetAllExcludingNode()
  {

    Node node1 = new Node();
    node1.setId( new Long( 1 ) );
    node1.setName( "testNAME-ABC" );
    node1.setDescription( "testDESCRIPTION-ABC" );

    Node node2 = new Node();
    node2.setId( new Long( 2 ) );
    node2.setName( "testNAME-XYZ" );
    node2.setDescription( "testDESCRIPTION-XYZ" );

    Node node3 = new Node();
    node3.setId( new Long( 3 ) );
    node3.setName( "testNAME-XAZ" );
    node3.setDescription( "testDESCRIPTION-XAZ" );

    List expectedList = new ArrayList();
    expectedList.add( node1 );
    expectedList.add( node2 );

    mockNodeDAO.expects( once() ).method( "getAllExcludingNode" ).with( same( node3 ) ).will( returnValue( expectedList ) );

    List actualList = nodeService.getAllExcludingNode( node3 );

    assertTrue( "Actual set didn't contain expected set for getAll.", actualList.containsAll( expectedList ) );

    mockNodeDAO.verify();

  }

  /**
   * Test searching for nodes through the service.
   */
  public void testSearchNode()
  {
    List expectedList = getNodeList();

    mockNodeDAO.expects( once() ).method( "searchNode" ).with( same( "test" ), same( null ), same( null ) ).will( returnValue( expectedList ) );

    List actualList = nodeService.searchNode( "test", null, null );

    assertTrue( "Actual list doesn't contain expectedList", actualList.containsAll( expectedList ) );

    mockNodeDAO.verify();

  }

  /**
   * Test searching for nodes through the service.
   */
  public void testSearchNodeWithHierarchyAndNodeType()
  {
    List expectedList = getNodeList();
    Long hierarchyId = new Long( 1 );
    Long nodeTypeId = new Long( 2 );
    String partialNodeName = "test";

    mockNodeDAO.expects( once() ).method( "searchNode" ).with( same( hierarchyId ), same( partialNodeName ), same( nodeTypeId ) ).will( returnValue( expectedList ) );

    List actualList = nodeService.searchNode( hierarchyId, partialNodeName, nodeTypeId );

    assertTrue( "Actual list doesn't contain expectedList", actualList.containsAll( expectedList ) );

    mockNodeDAO.verify();

  }

  public void testGetNodeWithAssociationsById()
  {
    Node node1 = new Node();
    node1.setId( new Long( 1 ) );
    node1.setName( "testNAME-ABC121" );

    Hierarchy hierarchy = buildHierarchy();
    node1.setHierarchy( hierarchy );

    NodeType nodeType = new NodeType();
    nodeType.setId( new Long( 1 ) );

    node1.setNodeType( nodeType );

    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
    associationRequestCollection.add( mockAssociationRequest );

    mockNodeDAO.expects( once() ).method( "getNodeById" ).with( same( node1.getId() ) ).will( returnValue( node1 ) );

    Node resultNode = nodeService.getNodeWithAssociationsById( node1.getId(), associationRequestCollection );
    assertEquals( "Failed to find Node Type", resultNode.getNodeType().getName(), nodeType.getName() );
    assertEquals( "Failed to find Node hierarchy", resultNode.getHierarchy().getName(), hierarchy.getName() );

  }

  public void testGetChildNodesWithAssociationsByParent()
  {
    Node parentNode = new Node();
    parentNode.setId( new Long( 1 ) );
    parentNode.setName( "testNAME-ABC121" );
    parentNode.setParentNode( null );

    Hierarchy hierarchy = buildHierarchy();

    NodeType nodeType = new NodeType();
    nodeType.setId( new Long( 1 ) );

    Node node2 = new Node();
    node2.setId( new Long( 1 ) );
    node2.setName( "testNAME-ABC122" );
    node2.setParentNode( parentNode );
    node2.setHierarchy( hierarchy );
    node2.setNodeType( nodeType );

    Node node3 = new Node();
    node3.setId( new Long( 1 ) );
    node3.setName( "testNAME-ABC123" );
    node3.setParentNode( parentNode );
    node3.setHierarchy( hierarchy );
    node3.setNodeType( nodeType );

    List childList = new ArrayList( 2 );
    childList.add( node2 );
    childList.add( node3 );

    AssociationRequest[] associationRequests = new AssociationRequest[1];
    associationRequests[0] = mockAssociationRequest;
    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
    associationRequestCollection.add( mockAssociationRequest );
    mockNodeDAO.expects( once() ).method( "getChildNodesByParent" ).with( same( parentNode.getId() ) ).will( returnValue( childList ) );

    List resultList = nodeService.getChildNodesWithAssociationsByParent( parentNode.getId(), associationRequestCollection );
    assertTrue( "Failed to find Node Children", resultList.size() == 2 );
    mockNodeDAO.verify();
  }

  public void testGetRootNodeWithChildren()
  {
    Node parentNode = new Node();
    parentNode.setId( new Long( 1 ) );
    parentNode.setName( "testNAME-ABC121" );
    parentNode.setParentNode( null );

    Hierarchy hierarchy = buildHierarchy();

    NodeType nodeType = new NodeType();
    nodeType.setId( new Long( 1 ) );

    Node node2 = new Node();
    node2.setId( new Long( 1 ) );
    node2.setName( "testNAME-ABC122" );
    node2.setParentNode( parentNode );
    node2.setHierarchy( hierarchy );
    node2.setNodeType( nodeType );

    Node node3 = new Node();
    node3.setId( new Long( 1 ) );
    node3.setName( "testNAME-ABC123" );
    node3.setParentNode( parentNode );
    node3.setHierarchy( hierarchy );
    node3.setNodeType( nodeType );

    List childList = new ArrayList( 2 );
    childList.add( node2 );
    childList.add( node3 );

    AssociationRequest[] associationRequests = new AssociationRequest[1];
    associationRequests[0] = mockAssociationRequest;
    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
    associationRequestCollection.add( mockAssociationRequest );

    mockHierarchyDAO.expects( once() ).method( "getById" ).with( same( hierarchy.getId() ) ).will( returnValue( hierarchy ) );
    mockNodeDAO.expects( once() ).method( "getRootNode" ).with( same( hierarchy ) ).will( returnValue( node2 ) );

    Node rootNode = nodeService.getRootNode( hierarchy.getId(), associationRequestCollection );
    assertEquals( "Actual Node wasn't equals to what was expected", rootNode, node2 );
    mockNodeDAO.verify();
  }

  /**
   * Build and return a set of nodes used for testing.
   * 
   * @return List
   */
  private List getNodeList()
  {

    List nodeList = new ArrayList();

    Node node1 = new Node();
    node1.setId( new Long( 1 ) );
    node1.setName( "testNAME-ABC" );
    node1.setDescription( "testDESCRIPTION-ABC" );

    Node node2 = new Node();
    node2.setId( new Long( 2 ) );
    node2.setName( "testNAME-XYZ" );
    node2.setDescription( "testDESCRIPTION-XYZ" );

    Node node3 = new Node();
    node3.setId( new Long( 3 ) );
    node3.setName( "testNAME-XAZ" );
    node3.setDescription( "testDESCRIPTION-XAZ" );

    nodeList.add( node1 );
    nodeList.add( node3 );
    nodeList.add( node2 );

    return nodeList;

  }

  public void testRemoveParticipantsFromNode() throws Exception
  {

    Long nodeId = new Long( 100 );
    Node node = new Node();
    node.setName( "name1" );
    node.setId( nodeId );

    Long nodeId2 = new Long( 101 );
    Node node2 = new Node();
    node2.setName( "name2" );
    node2.setId( nodeId2 );

    User user = new User();
    user.setId( new Long( 1 ) );

    UserNode userNode1a = new UserNode( user, node );
    UserNode userNode1b = new UserNode( user, node2 );
    UserNode userNode2a = new UserNode( user, node );

    Set userNodeSetForUser = new LinkedHashSet();
    Set userNodeSetForNode = new LinkedHashSet();
    userNodeSetForUser.add( userNode1a );
    userNodeSetForUser.add( userNode1b );
    userNodeSetForNode.add( userNode2a );

    user.setUserNodes( userNodeSetForUser );
    node.setUserNodes( userNodeSetForNode );

    List paxIds = new ArrayList();
    paxIds.add( user.getId() );

    assertEquals( 2, ( (UserNode)node.getUserNodes().iterator().next() ).getUser().getUserNodes().size() );
    mockNodeDAO.expects( once() ).method( "getNodeById" ).with( same( node.getId() ) ).will( returnValue( node ) );

    nodeService.removeParticipantsFromNode( node.getId(), paxIds );

    mockNodeDAO.verify();
    assertEquals( "UserNodes on Node", 0, node.getUserNodes().size() );
    assertEquals( "UserNodes on User", 1, user.getUserNodes().size() );

  }

  public void testRemoveParticipantsFromNodeWithOrphanedNode()
  {

    Long nodeId = new Long( 100 );
    Node node = new Node();
    node.setName( "name1" );
    node.setId( nodeId );

    User user = new User();
    user.setId( new Long( 1 ) );

    UserNode userNode1a = new UserNode( user, node );
    UserNode userNode2a = new UserNode( user, node );

    Set userNodeSetForUser = new LinkedHashSet();
    Set userNodeSetForNode = new LinkedHashSet();
    userNodeSetForUser.add( userNode1a );
    userNodeSetForNode.add( userNode2a );

    user.setUserNodes( userNodeSetForUser );
    node.setUserNodes( userNodeSetForNode );

    List paxIds = new ArrayList();
    paxIds.add( user.getId() );

    assertEquals( 1, ( (UserNode)node.getUserNodes().iterator().next() ).getUser().getUserNodes().size() );
    mockNodeDAO.expects( once() ).method( "getNodeById" ).with( same( node.getId() ) ).will( returnValue( node ) );

    try
    {
      nodeService.removeParticipantsFromNode( node.getId(), paxIds );
    }
    catch( ServiceErrorException e )
    {
      assertEquals( 1, e.getServiceErrors().size() );
      assertEquals( ServiceErrorMessageKeys.NODE_PARTICIPANT_ORPHAN, ( (ServiceError)e.getServiceErrors().get( 0 ) ).getKey() );
      mockNodeDAO.verify();
      return;
    }

    fail( "Should have thrown an exception" );
  }

  public void testGetNodesAsHierarchy()
  {
    Hierarchy hierarchy = buildHierarchy();

    NodeType nodeType = new NodeType();
    nodeType.setId( new Long( 1 ) );

    Node parentNode = new Node();
    parentNode.setId( new Long( 1 ) );
    parentNode.setName( "testNAME-ABC121" );
    parentNode.setParentNode( null );
    parentNode.setHierarchy( hierarchy );
    parentNode.setNodeType( nodeType );

    Node node2 = new Node();
    node2.setId( new Long( 1 ) );
    node2.setName( "testNAME-ABC122" );
    node2.setParentNode( parentNode );
    node2.setHierarchy( hierarchy );
    node2.setNodeType( nodeType );

    Node node3 = new Node();
    node3.setId( new Long( 1 ) );
    node3.setName( "testNAME-ABC123" );
    node3.setParentNode( node2 );
    node3.setHierarchy( hierarchy );
    node3.setNodeType( nodeType );

    List childList = new ArrayList( 2 );
    childList.add( node2 );
    childList.add( node3 );

    AssociationRequest[] associationRequests = new AssociationRequest[1];
    associationRequests[0] = mockAssociationRequest;
    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
    associationRequestCollection.add( mockAssociationRequest );

    mockNodeDAO.expects( once() ).method( "getNodesAsHierarchy" ).with( same( hierarchy.getId() ) ).will( returnValue( childList ) );

    List expectedList = nodeService.getNodesAsHierarchy( hierarchy.getId() );
    assertEquals( "Actual Node wasn't equals to what was expected", expectedList, childList );
    mockNodeDAO.verify();

  }

  public void testGetNodeOwnerByLevel()
  {
    Long nodeId = new Long( 1 );

    User user = new User();
    user.setId( new Long( 1 ) );
    user.setUserName( "testNodeOwner" );

    mockNodeDAO.expects( once() ).method( "getNodeOwnerByLevel" ).with( same( nodeId ) ).will( returnValue( user ) );

    User nodeOwner = nodeService.getNodeOwnerByLevel( nodeId );
    assertEquals( nodeOwner.getUserName(), "TESTNODEOWNER" );

    mockNodeDAO.verify();
  }

  /**
   * Creates and returns a hierarchy for testing.
   * 
   * @return Hierarchy
   */
  public static Hierarchy buildHierarchy()
  {

    Hierarchy hierarchy = new Hierarchy();
    hierarchy.setId( new Long( 1 ) );
    hierarchy.setName( "Test Hierarchy Name" );
    hierarchy.setDescription( "description goes here" );
    return hierarchy;
  }

  /**
   * Create and return a user for testing.
   * 
   * @return User
   */
  private User createUser()
  {

    String unique = String.valueOf( System.currentTimeMillis() % 3994832 );

    User user = new User();

    user.setId( new Long( unique ) );
    user.setUserName( "ServiceTestUserName" + unique );
    user.setFirstName( "ServiceTestTestFirstName" + unique );
    user.setLastName( "ServiceTestLastName" + unique );
    user.setMasterUserId( new Long( 1 ) ); // not clear on its business use
    user.setPassword( "ServiceTestPassword" + unique );
    user.setActive( Boolean.TRUE );

    return user;
  }

  public void testIsUserInNodeorBelow()
  {
    Long loggedInUserId = new Long( 1 );
    Long searchByUserID = new Long( 2 );
    boolean isValidUser = true;

    mockNodeDAO.expects( once() ).method( "isUserInNodeorBelow" ).with( same( searchByUserID ), same( loggedInUserId ) ).will( returnValue( isValidUser ) );

    isValidUser = nodeService.isUserInNodeorBelow( searchByUserID, loggedInUserId );

    assertTrue( "Is a valid user", isValidUser );

    mockNodeDAO.verify();

  }

}

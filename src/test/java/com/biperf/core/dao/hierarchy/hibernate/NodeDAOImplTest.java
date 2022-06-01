/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javatest/com/biperf/core/dao/hierarchy/hibernate/NodeDAOImplTest.java,v $
 */

package com.biperf.core.dao.hierarchy.hibernate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections4.MapUtils;

import com.biperf.core.dao.hibernate.BaseDAOTest;
import com.biperf.core.dao.hierarchy.NodeDAO;
import com.biperf.core.dao.participant.UserDAO;
import com.biperf.core.domain.enums.HierarchyRoleType;
import com.biperf.core.domain.enums.UserType;
import com.biperf.core.domain.hierarchy.Hierarchy;
import com.biperf.core.domain.hierarchy.Node;
import com.biperf.core.domain.hierarchy.NodeType;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.user.User;
import com.biperf.core.domain.user.UserNode;
import com.biperf.core.service.hierarchy.NodeSearchCriteria;
import com.biperf.core.utils.ApplicationContextFactory;

/**
 * NodeDAOImplTest.
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
public class NodeDAOImplTest extends BaseDAOTest
{
  /**
   * Helper method to get the NodeDAO from the beanFactory.
   * 
   * @return NodeDAO
   */
  public static NodeDAO getNodeDAO()
  {
    return (NodeDAO)ApplicationContextFactory.getApplicationContext().getBean( "nodeDAO" );
  }

  public static UserDAO getUserDAO()
  {
    return (UserDAO)ApplicationContextFactory.getApplicationContext().getBean( "userDAO" );
  }

  /**
   * Builds a {@link Node} object given a unique string.
   * 
   * @param uniqueString a unique string.
   * @return a new {@link Node} object.
   */
  public static Node buildUniqueNode( String uniqueString )
  {
    NodeType nodeType = NodeTypeDAOImplTest.buildNodeType( uniqueString );
    Hierarchy hierarchy = HierarchyDAOImplTest.buildHierarchy( uniqueString );

    return buildUniqueNode( uniqueString, nodeType, hierarchy );
  }

  public static Node buildUniqueNodeInPrimaryHierarchy( String uniqueString )
  {
    NodeType nodeType = NodeTypeDAOImplTest.buildNodeType( uniqueString );
    Hierarchy hierarchy = HierarchyDAOImplTest.buildPrimaryHierarchy( uniqueString );

    return buildUniqueNode( uniqueString, nodeType, hierarchy );

  }

  /**
   * Build a node with a unique String.
   * 
   * @param uniqueName
   * @param nodeType
   * @param hierarchy
   * @return Node
   */
  public static Node buildUniqueNode( String uniqueName, NodeType nodeType, Hierarchy hierarchy )
  {
    Node node = new Node();

    node.setName( "testNAME" + uniqueName );
    node.setDescription( "testDESCRIPTION" + uniqueName );
    node.setPath( "testPATH" + uniqueName );
    node.setParentNode( null );

    node.setNodeType( nodeType );
    node.setHierarchy( hierarchy );

    return node;

  }

  public static Node buildUniqueNode( String uniqueName, NodeType nodeType, Hierarchy hierarchy, Node parentNode )
  {
    Node node = buildUniqueNode( uniqueName, nodeType, hierarchy );

    node.setParentNode( parentNode );

    return node;
  }

  /**
   * Test saving the node then getting that node from the database by id.
   */
  public void testSaveNodeAndGetNodeById()
  {

    String uniqueName = String.valueOf( System.currentTimeMillis() % 29930291 );

    NodeDAO nodeDAO = getNodeDAO();

    Hierarchy hierarchy = new Hierarchy();
    hierarchy.setName( "Test NodeHierarchy1" + uniqueName );
    hierarchy.setDescription( "description goes here" );
    hierarchy.setPrimary( false );
    hierarchy.setActive( true );
    hierarchy.setCmAssetCode( "CM name ASSET" );
    hierarchy.setNameCmKey( "CM name KEY" );

    NodeType nodeType = new NodeType();
    nodeType.setName( "testNodeType" + uniqueName );
    nodeType.setCmAssetCode( "test.asset" );
    nodeType.setNameCmKey( "testkey" );

    Node parentNode = new Node();
    parentNode.setName( "testParentNAME" + uniqueName );
    parentNode.setDescription( "testParentDESCRIPTION" );
    parentNode.setParentNode( null );
    parentNode.setPath( "testParentPATH" );
    parentNode.setNodeType( nodeType );
    parentNode.setHierarchy( hierarchy );

    nodeDAO.saveNode( parentNode );

    Node node = new Node();
    node.setName( "testNAME" + uniqueName );
    node.setDescription( "testDESCRIPTION" );
    node.setParentNode( parentNode );
    node.setPath( "testPATH" );
    node.setNodeType( nodeType );
    node.setHierarchy( hierarchy );

    nodeDAO.saveNode( node );

    assertEquals( "Expected node wasn't equal to actual", node, nodeDAO.getNodeById( node.getId() ) );

  }

  /**
   * Tests searching the database for a list of Nodes.
   */
  public void testSearchNode()
  {
    String uniqueName = String.valueOf( System.currentTimeMillis() % 29930291 );

    NodeDAO nodeDAO = getNodeDAO();

    Hierarchy hierarchy = new Hierarchy();
    hierarchy.setName( "Test NodeHierarchy1" + uniqueName );
    hierarchy.setDescription( "description goes here" );
    hierarchy.setPrimary( false );
    hierarchy.setActive( true );
    hierarchy.setCmAssetCode( "CM name ASSET" );
    hierarchy.setNameCmKey( "CM name KEY" );

    NodeType nodeType = new NodeType();
    nodeType.setName( "testNodeType" + uniqueName );
    nodeType.setCmAssetCode( "test.asset" );
    nodeType.setNameCmKey( "testkey" );

    Node parentNode = new Node();
    parentNode.setName( "testParentNAME" + uniqueName );
    parentNode.setDescription( "testParentDESCRIPTION" );
    parentNode.setParentNode( null );
    parentNode.setPath( "testParentPATH" );
    parentNode.setNodeType( nodeType );
    parentNode.setHierarchy( hierarchy );

    nodeDAO.saveNode( parentNode );

    Node node1 = new Node();
    node1.setName( "testNAME-ABC" + uniqueName );
    node1.setDescription( "testDESCRIPTION-ABC" );
    node1.setParentNode( parentNode );
    node1.setNodeType( nodeType );
    node1.setHierarchy( hierarchy );
    node1.setPath( "testPATH" );
    nodeDAO.saveNode( node1 );

    Node node2 = new Node();
    node2.setName( "testNAME-XYZ" + uniqueName );
    node2.setDescription( "testDESCRIPTION-XYZ" );
    node2.setParentNode( parentNode );
    node2.setNodeType( nodeType );
    node2.setHierarchy( hierarchy );
    node2.setPath( node1.getPath() + node2.getName() );
    nodeDAO.saveNode( node2 );

    Node node3 = new Node();
    node3.setName( "testNAME-AZK" + uniqueName );
    node3.setDescription( "testDESCRIPTION-AZK" );
    node3.setParentNode( parentNode );
    node3.setNodeType( nodeType );
    node3.setHierarchy( hierarchy );
    node3.setPath( node2.getPath() + node3.getName() );
    nodeDAO.saveNode( node3 );

    List expectedList = new ArrayList();
    expectedList.add( node1 );
    expectedList.add( node3 );
    expectedList.add( node2 );

    // Search for all records where "test" is in the name
    List searchResults = nodeDAO.searchNode( "test", null, null );
    assertTrue( "Actual search results didn't contain expected list", searchResults.containsAll( expectedList ) );

    // Search all records where "DES" is in the description
    searchResults = nodeDAO.searchNode( "", "DES", null );
    assertTrue( "Actual search results didn't contain expected list", searchResults.containsAll( expectedList ) );

    // Remove the second node and search all records where "Z" is in the name and "A" is in the
    // description
    expectedList.remove( node2 );
    expectedList.remove( node1 );
    searchResults = nodeDAO.searchNode( "Z", "A", null );
    assertTrue( "Actual search results didn't contain expected list", searchResults.containsAll( expectedList ) );

  }

  /**
   * testSearchNodeByHierarchyAndType
   */
  public void testSearchNodeByHierarchyAndType()
  {
    String uniqueName = String.valueOf( System.currentTimeMillis() % 29930291 );

    Hierarchy hierarchy = new Hierarchy();
    hierarchy.setName( "Test NodeHierarchy1" + uniqueName );
    hierarchy.setDescription( "description goes here" );
    hierarchy.setPrimary( false );
    hierarchy.setActive( true );
    hierarchy.setCmAssetCode( "CM name ASSET" );
    hierarchy.setNameCmKey( "CM name KEY" );

    NodeType nodeType = new NodeType();
    nodeType.setName( "testNodeType" + uniqueName );
    nodeType.setCmAssetCode( "test.asset" );
    nodeType.setNameCmKey( "testkey" );

    Node node1 = new Node();
    node1.setName( "HierTestNode110" + uniqueName );
    node1.setDescription( "testDESCRIPTIONNode1" );
    node1.setNodeType( nodeType );
    node1.setHierarchy( hierarchy );
    node1.setPath( "testPATH" );
    hierarchy.getNodes().add( node1 );
    getNodeDAO().saveNode( node1 );

    Node node2 = new Node();
    node2.setName( "HierTestNode12" + uniqueName );
    node2.setDescription( "testDESCRIPTION-XYZ" );
    node2.setNodeType( nodeType );
    node2.setHierarchy( hierarchy );
    node2.setPath( node1.getPath() + node2.getName() );
    hierarchy.getNodes().add( node2 );
    getNodeDAO().saveNode( node2 );

    Node node3 = new Node();
    node3.setName( "HierTestNode13" + uniqueName );
    node3.setDescription( "testDESCRIPTION-AZK" );
    node3.setNodeType( nodeType );
    node3.setHierarchy( hierarchy );
    node3.setPath( node2.getPath() + node3.getName() );
    hierarchy.getNodes().add( node3 );
    getNodeDAO().saveNode( node3 );

    List nodeList = getNodeDAO().searchNode( hierarchy.getId(), "HierTestNode", nodeType.getId() );
    assertTrue( "Failed to search by hierarchyId,nodeName,nodeType", nodeList.size() == 3 );
  }

  /**
   * Test getting all Nodes from the database.
   */
  public void testGetAll()
  {
    String uniqueName = String.valueOf( System.currentTimeMillis() % 29930291 );

    NodeDAO nodeDAO = getNodeDAO();

    Hierarchy hierarchy = new Hierarchy();
    hierarchy.setName( "Test NodeHierarchy1" + uniqueName );
    hierarchy.setDescription( "description goes here" );
    hierarchy.setPrimary( false );
    hierarchy.setActive( true );
    hierarchy.setCmAssetCode( "CM name ASSET" );
    hierarchy.setNameCmKey( "CM name KEY" );

    NodeType nodeType = new NodeType();
    nodeType.setName( "testNodeType" + uniqueName );
    nodeType.setCmAssetCode( "test.asset" );
    nodeType.setNameCmKey( "testkey" );

    Node parentNode = new Node();
    parentNode.setName( "testParentNAME" + uniqueName );
    parentNode.setDescription( "testParentDESCRIPTION" );
    parentNode.setParentNode( null );
    parentNode.setPath( "testParentPATH" );
    parentNode.setNodeType( nodeType );
    parentNode.setHierarchy( hierarchy );

    nodeDAO.saveNode( parentNode );

    Node node1 = new Node();
    node1.setName( "testNAME-ABC" + uniqueName );
    node1.setDescription( "testDESCRIPTION-ABC" );
    // node1.setCmNameAsset( "CM_ASSET_CODE");
    // node1.setCmNameKey( "NAME_CM_KEY");
    node1.setParentNode( parentNode );
    node1.setNodeType( nodeType );
    node1.setHierarchy( hierarchy );
    node1.setPath( "testPATH" );
    nodeDAO.saveNode( node1 );

    Node node2 = new Node();
    node2.setName( "testNAME-XYZ" + uniqueName );
    node2.setDescription( "testDESCRIPTION-XYZ" );
    node2.setParentNode( parentNode );
    node2.setNodeType( nodeType );
    node2.setHierarchy( hierarchy );
    node2.setPath( node1.getPath() + node1.getName() );
    nodeDAO.saveNode( node2 );

    Node node3 = new Node();
    node3.setName( "testNAME-AZK" + uniqueName );
    node3.setDescription( "testDESCRIPTION-AZK" );
    node3.setParentNode( null );
    node3.setNodeType( nodeType );
    node3.setHierarchy( hierarchy );
    node3.setPath( node2.getPath() + node3.getName() );
    nodeDAO.saveNode( node3 );

    List expectedList = new ArrayList();
    expectedList.add( node1 );
    expectedList.add( node3 );
    expectedList.add( node2 );

    List actualList = nodeDAO.getAll();

    assertTrue( "Actual list doesn't contain the expected list of Nodes.", actualList.containsAll( expectedList ) );

  }

  /**
   * Test getting all of the nodes except for the one with the id param.
   */
  public void testGetAllExcludingNode()
  {
    String uniqueName = String.valueOf( System.currentTimeMillis() % 29930291 );

    NodeDAO nodeDAO = getNodeDAO();

    Hierarchy hierarchy = new Hierarchy();
    hierarchy.setName( "Test NodeHierarchy1" + uniqueName );
    hierarchy.setDescription( "description goes here" );
    hierarchy.setPrimary( false );
    hierarchy.setActive( true );
    hierarchy.setCmAssetCode( "CM name ASSET" );
    hierarchy.setNameCmKey( "CM name KEY" );

    NodeType nodeType = new NodeType();
    nodeType.setName( "testNodeType" + uniqueName );
    nodeType.setCmAssetCode( "test.asset" );
    nodeType.setNameCmKey( "testkey" );

    Node parentNode = new Node();
    parentNode.setName( "testParentNAME" + uniqueName );
    parentNode.setDescription( "testParentDESCRIPTION" );
    parentNode.setParentNode( null );
    parentNode.setPath( "testParentPATH" );
    parentNode.setNodeType( nodeType );
    parentNode.setHierarchy( hierarchy );

    nodeDAO.saveNode( parentNode );

    Node node1 = new Node();
    node1.setName( "testNAME-ABC" + uniqueName );
    node1.setDescription( "testDESCRIPTION-ABC" );
    node1.setParentNode( parentNode );
    node1.setNodeType( nodeType );
    node1.setHierarchy( hierarchy );
    node1.setPath( "testPATH" );
    nodeDAO.saveNode( node1 );

    Node node2 = new Node();
    node2.setName( "testNAME-XYZ" + uniqueName );
    node2.setDescription( "testDESCRIPTION-XYZ" );
    node2.setParentNode( parentNode );
    node2.setNodeType( nodeType );
    node2.setHierarchy( hierarchy );
    node2.setPath( node1.getPath() + node2.getName() );
    nodeDAO.saveNode( node2 );

    Node node3 = new Node();
    node3.setName( "testNAME-AZK" + uniqueName );
    node3.setDescription( "testDESCRIPTION-AZK" );
    node3.setParentNode( parentNode );
    node3.setNodeType( nodeType );
    node3.setHierarchy( hierarchy );
    node3.setPath( node2.getPath() + node3.getName() );
    nodeDAO.saveNode( node3 );

    Set expectedSet = new LinkedHashSet();
    expectedSet.add( node3 );
    expectedSet.add( node2 );

    List actualList = nodeDAO.getAllExcludingNode( node1 );

    assertTrue( "ActualSet include a node which is should not.", !actualList.contains( node1 ) );

  }

  /**
   * test to retrieve the root node for a hierarchy
   */
  public void testGetHierarchyRootNode()
  {
    String uniqueName = String.valueOf( System.currentTimeMillis() % 29930291 );

    NodeDAO nodeDAO = getNodeDAO();

    Node node1 = new Node();
    node1.setName( "testNAME-ABC121" + uniqueName );
    node1.setDescription( "testDESCRIPTION-ABC" );
    node1.setParentNode( null );
    node1.setPath( "testPATH" );

    Hierarchy hierarchy = new Hierarchy();
    hierarchy.setName( "Test Hierarchy Name" + uniqueName );
    hierarchy.setDescription( "description goes here" );
    hierarchy.setPrimary( false );
    hierarchy.setActive( true );
    hierarchy.setCmAssetCode( "CM name ASSET" );
    hierarchy.setNameCmKey( "CM name KEY" );

    node1.setHierarchy( hierarchy );

    NodeType nodeType = new NodeType();
    nodeType.setName( "testNodeType" + uniqueName );
    nodeType.setCmAssetCode( "test.asset" );
    nodeType.setNameCmKey( "testkey" );

    node1.setNodeType( nodeType );

    nodeDAO.saveNode( node1 );

    Node expectedNode = nodeDAO.getRootNode( hierarchy );

    assertEquals( "Expected node wasn't equal to actual", node1, expectedNode );
  }

  /**
   * Test getNodeByName
   */
  public void testGetNodeByName()
  {
    String uniqueName = String.valueOf( System.currentTimeMillis() % 29930291 );

    Hierarchy hierarchy = new Hierarchy();
    hierarchy.setName( "Test NodeHierarchy1" + uniqueName );
    hierarchy.setCmAssetCode( "unique.asset.name" + uniqueName );
    hierarchy.setNameCmKey( "HIERARCHY_NAME" );
    hierarchy.setDescription( "description goes here" );
    hierarchy.setPrimary( false );
    hierarchy.setActive( true );

    Hierarchy hierarchy2 = new Hierarchy();
    hierarchy2.setName( "Test NodeHierarchy2" + uniqueName );
    hierarchy2.setCmAssetCode( "unique.asset.name2" + uniqueName );
    hierarchy2.setNameCmKey( "HIERARCHY_NAME" );
    hierarchy2.setDescription( "description goes here" );
    hierarchy2.setPrimary( false );
    hierarchy2.setActive( true );

    NodeType nodeType = new NodeType();
    nodeType.setName( "testNodeType" + uniqueName );
    nodeType.setCmAssetCode( "test.asset" );
    nodeType.setNameCmKey( "testkey" );

    String nodeName1 = "HierTestNode" + uniqueName;

    Node node1 = new Node();
    node1.setName( nodeName1 );
    node1.setDescription( "testDESCRIPTIONNode1" );
    node1.setPath( "testPATH" );
    node1.setNodeType( nodeType );
    node1.setHierarchy( hierarchy );
    hierarchy.getNodes().add( node1 );
    getNodeDAO().saveNode( node1 );

    Node node2 = new Node();
    node2.setName( "HierTestNode2" + uniqueName );
    node2.setDescription( "testDESCRIPTION-XYZ" );
    node2.setPath( "testPATH2" );
    node2.setNodeType( nodeType );
    node2.setHierarchy( hierarchy );
    hierarchy.getNodes().add( node2 );
    getNodeDAO().saveNode( node2 );

    Node node3 = new Node();
    node3.setName( "HierTestNode13" + uniqueName );
    node3.setDescription( "testDESCRIPTION-AZK" );
    node3.setPath( "testPATH3" );
    node3.setNodeType( nodeType );
    node3.setHierarchy( hierarchy );
    hierarchy.getNodes().add( node3 );
    getNodeDAO().saveNode( node3 );

    String sameNodeName = node3.getName();

    // create a node with the same name (but different hierarchy)
    Node node4 = new Node();
    node4.setName( sameNodeName );
    node4.setDescription( "testDESCRIPTION-AZK" );
    node4.setPath( "testPATH4" );
    node4.setNodeType( nodeType );
    node4.setHierarchy( hierarchy2 );
    hierarchy2.getNodes().add( node4 );
    getNodeDAO().saveNode( node4 );

    Node testNode1 = getNodeDAO().getNodeByNameAndHierarchy( nodeName1, hierarchy );
    assertEquals( "Node names didn't match", testNode1.getName(), node1.getName() );

    // assert that two nodes with the same name but different hiearachy are unique
    Node testNode2 = getNodeDAO().getNodeByNameAndHierarchy( sameNodeName, hierarchy );
    Node testNode3 = getNodeDAO().getNodeByNameAndHierarchy( sameNodeName, hierarchy2 );
    assertFalse( "Nodes are equal", testNode2.equals( testNode3 ) );
    assertFalse( "Nodes have the same id", testNode2.equalsId( testNode3 ) );

  }

  /**
   * Test getNodeByNodeType
   */
  public void testGetNodeByNodeType()
  {
    String uniqueName = String.valueOf( System.currentTimeMillis() % 29930291 );

    Hierarchy hierarchy = new Hierarchy();
    hierarchy.setName( "Test NodeHierarchy1" + uniqueName );
    hierarchy.setDescription( "description goes here" );
    hierarchy.setPrimary( false );
    hierarchy.setActive( true );
    hierarchy.setCmAssetCode( "CM name ASSET" );
    hierarchy.setNameCmKey( "CM name KEY" );

    NodeType nodeType = new NodeType();
    nodeType.setName( "testNodeType" + uniqueName );
    nodeType.setCmAssetCode( "test.asset" );
    nodeType.setNameCmKey( "testkey" );

    String nodeName1 = "HierTestNode" + uniqueName;

    Node node1 = new Node();
    node1.setName( nodeName1 );
    node1.setDescription( "testDESCRIPTIONNode1" );
    node1.setPath( "testPATH" );
    node1.setNodeType( nodeType );
    node1.setHierarchy( hierarchy );
    hierarchy.getNodes().add( node1 );
    getNodeDAO().saveNode( node1 );

    Node node2 = new Node();
    node2.setName( "HierTestNode2" + uniqueName );
    node2.setDescription( "testDESCRIPTION-XYZ" );
    node2.setPath( "testPATH2" );
    node2.setNodeType( nodeType );
    node2.setHierarchy( hierarchy );
    hierarchy.getNodes().add( node2 );
    getNodeDAO().saveNode( node2 );

    Node node3 = new Node();
    node3.setName( "HierTestNode13" + uniqueName );
    node3.setDescription( "testDESCRIPTION-AZK" );
    node3.setPath( "testPATH3" );
    node3.setNodeType( nodeType );
    node3.setHierarchy( hierarchy );
    hierarchy.getNodes().add( node3 );
    getNodeDAO().saveNode( node3 );

    List nodeList = getNodeDAO().getNodesByNodeType( nodeType );
    assertTrue( "Node names didn't match", nodeList.size() == 3 );
  }

  /**
   * test to retrieve the list of nodes for a hierarchy
   */
  public void testGetNodesByHierarchy()
  {
    String uniqueName = String.valueOf( System.currentTimeMillis() % 29930291 );

    NodeDAO nodeDAO = getNodeDAO();

    Node node1 = new Node();
    node1.setName( "testNAME-ABC121" + uniqueName );
    node1.setDescription( "testDESCRIPTION-ABC" );
    node1.setParentNode( null );
    node1.setPath( "testPATH" );

    Hierarchy hierarchy = new Hierarchy();
    hierarchy.setName( "Test Hierarchy Name" + uniqueName );
    hierarchy.setDescription( "description goes here" );
    hierarchy.setPrimary( false );
    hierarchy.setActive( true );
    hierarchy.setCmAssetCode( "CM name ASSET" );
    hierarchy.setNameCmKey( "CM name KEY" );

    node1.setHierarchy( hierarchy );

    NodeType nodeType = new NodeType();
    nodeType.setName( "testNodeType" + uniqueName );
    nodeType.setCmAssetCode( "test.asset" );
    nodeType.setNameCmKey( "testkey" );

    node1.setNodeType( nodeType );

    nodeDAO.saveNode( node1 );

    Node node2 = new Node();
    node2.setName( "HierTestNode2" + uniqueName );
    node2.setDescription( "testDESCRIPTION-XYZ" );
    node2.setPath( "testPATH2" );
    node2.setNodeType( nodeType );
    node2.setHierarchy( hierarchy );
    hierarchy.getNodes().add( node2 );
    nodeDAO.saveNode( node2 );

    Node node3 = new Node();
    node3.setName( "HierTestNode13" + uniqueName );
    node3.setDescription( "testDESCRIPTION-AZK" );
    node3.setPath( "testPATH3" );
    node3.setNodeType( nodeType );
    node3.setHierarchy( hierarchy );
    hierarchy.getNodes().add( node3 );
    nodeDAO.saveNode( node3 );

    List actualList = new ArrayList( 3 );
    actualList.add( node1 );
    actualList.add( node2 );
    actualList.add( node3 );

    List expectedList = nodeDAO.getNodesByHierarchy( hierarchy );

    // TODO: update the test to actually iterate thru the list and match the entries.
    assertEquals( "Expected node wasn't equal to actual", actualList.size(), expectedList.size() );
  }

  /**
   * Test getting the node by the parent node.
   */
  public void testGetChildNodesByParent()
  {

    String uniqueName = String.valueOf( System.currentTimeMillis() % 29930291 );

    Node parentNode = new Node();
    parentNode.setName( "testNAME-ABC121" + uniqueName );
    parentNode.setDescription( "testDESCRIPTION-ABC" );
    parentNode.setParentNode( null );
    parentNode.setPath( "testPATH" );

    Hierarchy hierarchy = new Hierarchy();
    hierarchy.setName( "Test Hierarchy Name" + uniqueName );
    hierarchy.setDescription( "description goes here" );
    hierarchy.setPrimary( false );
    hierarchy.setActive( true );
    hierarchy.setCmAssetCode( "CM name ASSET" );
    hierarchy.setNameCmKey( "CM name KEY" );

    parentNode.setHierarchy( hierarchy );

    NodeType nodeType = new NodeType();
    nodeType.setName( "testNodeType" + uniqueName );
    nodeType.setCmAssetCode( "test.asset" );
    nodeType.setNameCmKey( "testkey" );

    parentNode.setNodeType( nodeType );

    getNodeDAO().saveNode( parentNode );

    Node node2 = new Node();
    node2.setName( "testNAME-ABC122" + uniqueName );
    node2.setDescription( "testDESCRIPTION-ABC" );
    node2.setParentNode( parentNode );
    node2.setPath( "testPATH" );
    node2.setHierarchy( hierarchy );
    node2.setNodeType( nodeType );
    getNodeDAO().saveNode( node2 );

    Node node3 = new Node();
    node3.setName( "testNAME-ABC123" + uniqueName );
    node3.setDescription( "testDESCRIPTION-ABC" );
    node3.setParentNode( parentNode );
    node3.setPath( "testPATH" );
    node3.setHierarchy( hierarchy );
    node3.setNodeType( nodeType );
    getNodeDAO().saveNode( node3 );

    List childNodeList = getNodeDAO().getChildNodesByParent( parentNode.getId() );
    assertTrue( "Failed to find child Nodes", childNodeList.size() == 2 );

  }

  /**
   * Test search nodes using criteria search.
   */
  public void testSearchNodesWithCriteria()
  {

    String uniqueName = String.valueOf( System.currentTimeMillis() % 29930291 );

    Node parentNode = new Node();
    parentNode.setName( "MiTest-ABC121" + uniqueName );
    parentNode.setDescription( "testDESCRIPTION-ABC" );
    parentNode.setParentNode( null );
    parentNode.setPath( "testPATH" );

    Hierarchy hierarchy = new Hierarchy();
    hierarchy.setName( "Test Hierarchy Name" + uniqueName );
    hierarchy.setDescription( "description goes here" );
    hierarchy.setPrimary( false );
    hierarchy.setActive( true );
    hierarchy.setCmAssetCode( "CM name ASSET" );
    hierarchy.setNameCmKey( "CM name KEY" );

    parentNode.setHierarchy( hierarchy );

    NodeType nodeType = new NodeType();
    nodeType.setName( "testNodeType" + uniqueName );
    nodeType.setCmAssetCode( "test.asset" );
    nodeType.setNameCmKey( "testkey" );

    parentNode.setNodeType( nodeType );

    getNodeDAO().saveNode( parentNode );

    Node node2 = new Node();
    node2.setName( "Minnesota-ABC122" + uniqueName );
    node2.setDescription( "testDESCRIPTION-ABC" );
    node2.setParentNode( parentNode );
    node2.setPath( "testPATH" );
    node2.setHierarchy( hierarchy );
    node2.setNodeType( nodeType );
    getNodeDAO().saveNode( node2 );

    Node node3 = new Node();
    node3.setName( "Wisconsin-ABC123" + uniqueName );
    node3.setDescription( "testDESCRIPTION-ABC" );
    node3.setParentNode( parentNode );
    node3.setPath( "testPATH" );
    node3.setHierarchy( hierarchy );
    node3.setNodeType( nodeType );
    getNodeDAO().saveNode( node3 );

    Node node4 = new Node();
    node4.setName( "Minneapolis-ABC123" + uniqueName );
    node4.setDescription( "testDESCRIPTION-ABC" );
    node4.setParentNode( node2 );
    node4.setPath( "testPATH" );
    node4.setHierarchy( hierarchy );
    node4.setNodeType( nodeType );
    getNodeDAO().saveNode( node4 );

    Node node5 = new Node();
    node5.setName( "StPaul-ABC123" + uniqueName );
    node5.setDescription( "testDESCRIPTION-ABC" );
    node5.setParentNode( node2 );
    node5.setPath( "testPATH" );
    node5.setHierarchy( hierarchy );
    node5.setNodeType( nodeType );
    getNodeDAO().saveNode( node5 );

    flushAndClearSession();
    NodeSearchCriteria criteria = new NodeSearchCriteria();
    criteria.setNodeId( node2.getId() );
    criteria.setNodeIdAndBelow( true );
    criteria.setNodeName( "Mi" );
    List nodeList = getNodeDAO().searchNode( criteria );
    assertEquals( "Failed to find correct number of nodes", 2, nodeList.size() );
    assertTrue( "Found nodes should contain Minnesota", nodeList.contains( node2 ) );
    assertTrue( "Found nodes should contain Minneapolis", nodeList.contains( node4 ) );

  }

  public void testGetNodesByUserInRole()
  {
    User user = new User();
    user.setId( new Long( 100 ) );
    HierarchyRoleType roleType = HierarchyRoleType.lookup( HierarchyRoleType.OWNER );
    List nodes = getNodeDAO().getNodesWithUserHavingRole( user, roleType );
    assertNotNull( nodes );

  }

  public void testGetNodeOwnerByLevel()
  {
    Long nodeIdWithOwner = new Long( 5001 );
    User nodeOwner = getNodeDAO().getNodeOwnerByLevel( nodeIdWithOwner );
    assertTrue( null != nodeOwner );
  }

  public void testGetNodeDescription()
  {
    List<Long> nodeIds = new ArrayList<Long>();
    nodeIds.add( 5002L );
    nodeIds.add( null );
    assertTrue( MapUtils.isNotEmpty( getNodeDAO().getNodeDescription( nodeIds ) ) );
    assertTrue( MapUtils.isEmpty( getNodeDAO().getNodeDescription( Arrays.asList() ) ) );
  }

  public void testGetCountForNodeByStartsWith()
  {
    int count = getNodeDAO().getCountByNodeName( "a" );
    assertTrue( count > 0 );
  }

  /**
   * Tests searching the database for a list of Nodes.
   */
  public void testIsUserInNodeorBelow()
  {
    String uniqueName = String.valueOf( System.currentTimeMillis() % 29930291 );

    NodeDAO nodeDAO = getNodeDAO();
    UserDAO userDAO = getUserDAO();

    Hierarchy hierarchy = new Hierarchy();
    hierarchy.setName( "Test NodeHierarchy1" + uniqueName );
    hierarchy.setDescription( "description goes here" );
    hierarchy.setPrimary( false );
    hierarchy.setActive( true );
    hierarchy.setCmAssetCode( "CM name ASSET" );
    hierarchy.setNameCmKey( "CM name KEY" );

    Hierarchy hierarchy1 = new Hierarchy();
    hierarchy1.setName( "Test NodeHierarchy2" + uniqueName );
    hierarchy1.setDescription( "description goes here" );
    hierarchy1.setPrimary( false );
    hierarchy1.setActive( true );
    hierarchy1.setCmAssetCode( "CM name ASSET" );
    hierarchy1.setNameCmKey( "CM name KEY" );

    NodeType nodeType = new NodeType();
    nodeType.setName( "testNodeType" + uniqueName );
    nodeType.setCmAssetCode( "test.asset" );
    nodeType.setNameCmKey( "testkey" );

    NodeType nodeType1 = new NodeType();
    nodeType1.setName( "testNodeType" + uniqueName );
    nodeType1.setCmAssetCode( "test.asset" );
    nodeType1.setNameCmKey( "testkey" );

    Node parentNode = new Node();
    parentNode.setName( "testParentNAME" + uniqueName );
    parentNode.setDescription( "testParentDESCRIPTION" );
    parentNode.setParentNode( null );
    parentNode.setPath( "testParentPATH" );
    parentNode.setNodeType( nodeType );
    parentNode.setHierarchy( hierarchy );

    nodeDAO.saveNode( parentNode );

    Node node1 = new Node();
    node1.setName( "Test NodeHierarchy1" + uniqueName );
    node1.setDescription( "description goes here" );
    node1.setParentNode( parentNode );
    node1.setNodeType( nodeType );
    node1.setHierarchy( hierarchy1 );
    node1.setPath( parentNode.getPath() + node1.getName() );
    nodeDAO.saveNode( node1 );

    Node node2 = new Node();
    node2.setName( "Test NodeHierarchy2" + uniqueName );
    node2.setDescription( "description goes here" );
    node2.setParentNode( node1 );
    node2.setNodeType( nodeType1 );
    node2.setPath( node1.getPath() + node2.getName() );
    node2.setHierarchy( hierarchy1 );
    nodeDAO.saveNode( node2 );

    User loggedInUser = buildStaticUser();
    UserNode userNode = new UserNode();
    userNode.setNode( node1 );
    userNode.setIsPrimary( true );
    userNode.setHierarchyRoleType( HierarchyRoleType.lookup( HierarchyRoleType.OWNER ) );
    loggedInUser.addUserNode( userNode );
    userDAO.saveUser( loggedInUser );

    User user1 = buildStaticUser();
    UserNode userNode1 = new UserNode();
    userNode1.setNode( node1 );
    userNode1.setIsPrimary( true );
    userNode1.setHierarchyRoleType( HierarchyRoleType.lookup( HierarchyRoleType.MEMBER ) );
    user1.addUserNode( userNode1 );
    userDAO.saveUser( user1 );

    User user2 = buildStaticUser();
    UserNode userNode2 = new UserNode();
    userNode2.setNode( node2 );
    userNode2.setIsPrimary( true );
    userNode2.setHierarchyRoleType( HierarchyRoleType.lookup( HierarchyRoleType.OWNER ) );
    user2.addUserNode( userNode2 );
    userDAO.saveUser( user2 );

    flushAndClearSession();

    boolean isValidUser = getNodeDAO().isUserInNodeorBelow( user1.getId(), loggedInUser.getId() );
    assertTrue( "Pax details are visible to the owner", isValidUser );

  }

  /**
   * Builds a static user.
   * 
   * @return User
   */
  public static User buildStaticUser()
  {
    return buildStaticUser( "testUsername" + buildUniqueString(), "testFIRSTNAME", "testLASTNAME" );
  }

  /**
   * Builds a static user with the params.
   * 
   * @param userName
   * @param firstName
   * @param lastName
   * @return User
   */
  public static Participant buildStaticUser( String userName, String firstName, String lastName )
  {

    Participant user = new Participant();
    user.setUserType( UserType.lookup( UserType.BI ) );
    user.setUserName( userName );
    user.setFirstName( firstName );
    user.setLastName( lastName );
    user.setPassword( "testPASSWORD" );
    user.setActive( Boolean.TRUE );
    user.setWelcomeEmailSent( Boolean.FALSE );

    return user;
  }

}
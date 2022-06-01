/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javatest/com/biperf/core/dao/participant/hibernate/UserNodeHistoryDAOImplTest.java,v $
 */

package com.biperf.core.dao.participant.hibernate;

import java.util.List;

import com.biperf.core.dao.hibernate.BaseDAOTest;
import com.biperf.core.dao.hierarchy.HierarchyDAO;
import com.biperf.core.dao.hierarchy.NodeDAO;
import com.biperf.core.dao.hierarchy.NodeTypeDAO;
import com.biperf.core.dao.participant.UserDAO;
import com.biperf.core.dao.participant.UserNodeHistoryDAO;
import com.biperf.core.domain.enums.HierarchyRoleType;
import com.biperf.core.domain.hierarchy.Hierarchy;
import com.biperf.core.domain.hierarchy.Node;
import com.biperf.core.domain.hierarchy.NodeType;
import com.biperf.core.domain.user.User;
import com.biperf.core.domain.user.UserNode;
import com.biperf.core.utils.ApplicationContextFactory;
import com.biperf.core.utils.HibernateSessionManager;

/**
 * UserNodeHistoryDAOImplTest.
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
public class UserNodeHistoryDAOImplTest extends BaseDAOTest
{
  /**
   * Test getAllUserNodeHistoryByUser
   */
  public void testGetAllUserNodeHistoryByUser()
  {
    String uniqueName = getUniqueString();

    NodeDAO nodeDAO = getNodeDAO();

    NodeType nodeType = new NodeType();
    nodeType.setCmAssetCode( "test.asset" );
    nodeType.setNameCmKey( "testkey" );
    getNodeTypeDAO().saveNodeType( nodeType );

    Hierarchy hierarchy = new Hierarchy();
    hierarchy.setName( "testNAME" + uniqueName );
    hierarchy.setCmAssetCode( "testASSET" );
    hierarchy.setNameCmKey( "testKEY" );
    getHierarchyDAO().save( hierarchy );

    Node node1 = new Node();
    node1.setName( "testNODENAME1" + uniqueName );
    node1.setDescription( "testNODEDESCRIPTION1" );
    node1.setNodeType( nodeType );
    node1.setHierarchy( hierarchy );
    node1.setPath( "testNodePath" );
    nodeDAO.saveNode( node1 );

    Node node2 = new Node();
    node2.setName( "testNODENAME2" + uniqueName );
    node2.setDescription( "testNODEDESCRIPTION2" );
    node2.setNodeType( nodeType );
    node2.setHierarchy( hierarchy );
    node2.setPath( node1.getPath() + node2.getName() );
    nodeDAO.saveNode( node2 );

    Node node3 = new Node();
    node3.setName( "testNODENAME3" + uniqueName );
    node3.setDescription( "testNODEDESCRIPTION3" );
    node3.setNodeType( nodeType );
    node3.setHierarchy( hierarchy );
    node3.setPath( node1.getPath() + node3.getName() );
    nodeDAO.saveNode( node3 );

    User user = UserDAOImplTest.buildStaticUser();
    getUserDAO().saveUser( user );

    UserNode userNode1 = new UserNode();
    userNode1.setIsPrimary( Boolean.TRUE );
    userNode1.setUser( user );
    userNode1.setNode( node1 );
    userNode1.setActive( Boolean.valueOf( true ) );
    userNode1.setHierarchyRoleType( HierarchyRoleType.lookup( HierarchyRoleType.MEMBER ) );

    UserNode userNode2 = new UserNode();
    userNode2.setIsPrimary( Boolean.TRUE );
    userNode2.setUser( user );
    userNode2.setNode( node2 );
    userNode2.setActive( Boolean.valueOf( true ) );
    userNode2.setHierarchyRoleType( HierarchyRoleType.lookup( HierarchyRoleType.MEMBER ) );

    UserNode userNode3 = new UserNode();
    userNode3.setIsPrimary( Boolean.TRUE );
    userNode3.setUser( user );
    userNode3.setNode( node3 );
    userNode3.setActive( Boolean.TRUE );
    userNode3.setHierarchyRoleType( HierarchyRoleType.lookup( HierarchyRoleType.MEMBER ) );

    user.addUserNode( userNode1 );
    user.addUserNode( userNode2 );

    getUserDAO().saveUser( user );

    flushAndClearSession();

    User savedUser = getUserDAO().getUserById( user.getId() );

    List originalUserNodeHistoryList = getUserNodeHistoryDAO().getAllUserNodeHistoryByUser( savedUser.getId() );
    int originalSize = originalUserNodeHistoryList.size();

    UserNode userNodeToUpdate = (UserNode)savedUser.getUserNodes().iterator().next();
    userNodeToUpdate.setHierarchyRoleType( HierarchyRoleType.lookup( HierarchyRoleType.MANAGER ) );

    getUserDAO().saveUser( savedUser );
    // Flushing initially to flush the update so the updateEvent is caught
    HibernateSessionManager.getSession().flush();
    flushAndClearSession();

    List userNodeHistoryList = getUserNodeHistoryDAO().getAllUserNodeHistoryByUser( savedUser.getId() );

    // One History Record should have been saved.
    assertEquals( "Only one history record should have been saved.", originalSize + 1, userNodeHistoryList.size() );

    // Now test a delete
    User savedUser1 = getUserDAO().getUserById( user.getId() );
    Node newNode = nodeDAO.getNodeById( node3.getId() );

    UserNode userNodeToMove = (UserNode)savedUser1.getUserNodes().iterator().next();
    savedUser1.removeUserNode( userNodeToMove );

    UserNode newUserNode = new UserNode();
    newUserNode.setIsPrimary( Boolean.TRUE );
    newUserNode.setNode( newNode );
    newUserNode.setActive( userNodeToMove.getActive() );
    newUserNode.setHierarchyRoleType( userNodeToMove.getHierarchyRoleType() );

    savedUser1.addUserNode( newUserNode );

    getUserDAO().saveUser( savedUser1 );
    // Flushing initially to flush the update so the deleteEvent is caught
    HibernateSessionManager.getSession().flush();
    flushAndClearSession();

    // One History Record should have been saved.
    assertEquals( "Only one history record should have been saved.", userNodeHistoryList.size() + 1, getUserNodeHistoryDAO().getAllUserNodeHistoryByUser( savedUser.getId() ).size() );
  }

  /**
   * Test getAllUserNodeHistoryByUser
   */
  public void testUserNodeHistoryForNode()
  {
    String uniqueName = getUniqueString();

    NodeDAO nodeDAO = getNodeDAO();

    NodeType nodeType1 = new NodeType();
    nodeType1.setCmAssetCode( "test1.asset" );
    nodeType1.setNameCmKey( "testkey1" );
    getNodeTypeDAO().saveNodeType( nodeType1 );

    NodeType nodeType2 = new NodeType();
    nodeType2.setCmAssetCode( "test2.asset" );
    nodeType2.setNameCmKey( "testkey2" );
    getNodeTypeDAO().saveNodeType( nodeType2 );

    Hierarchy hierarchy1 = new Hierarchy();
    hierarchy1.setName( "testNAME1" + uniqueName );
    hierarchy1.setCmAssetCode( "testASSET1" );
    hierarchy1.setNameCmKey( "testKEY1" );
    getHierarchyDAO().save( hierarchy1 );

    Hierarchy hierarchy2 = new Hierarchy();
    hierarchy2.setName( "testNAME2" + uniqueName );
    hierarchy2.setCmAssetCode( "testASSET2" );
    hierarchy2.setNameCmKey( "testKEY2" );
    getHierarchyDAO().save( hierarchy2 );

    User user1 = UserDAOImplTest.buildStaticUser();
    getUserDAO().saveUser( user1 );

    User user2 = UserDAOImplTest.buildStaticUser();
    getUserDAO().saveUser( user2 );

    User user3 = UserDAOImplTest.buildStaticUser();
    getUserDAO().saveUser( user3 );

    // flushAndClearSession();

    Node node1 = new Node();
    node1.setName( "testNODENAME1" + uniqueName );
    node1.setDescription( "testNODEDESCRIPTION1" );
    node1.setNodeType( nodeType1 );
    node1.setHierarchy( hierarchy1 );
    node1.setPath( "testNodePath" );

    UserNode userNode1 = new UserNode();
    userNode1.setIsPrimary( Boolean.TRUE );
    userNode1.setUser( user1 );
    userNode1.setActive( Boolean.valueOf( true ) );
    userNode1.setHierarchyRoleType( HierarchyRoleType.lookup( HierarchyRoleType.MEMBER ) );

    UserNode userNode2 = new UserNode();
    userNode2.setIsPrimary( Boolean.TRUE );
    userNode2.setUser( user2 );
    userNode2.setActive( Boolean.valueOf( true ) );
    userNode2.setHierarchyRoleType( HierarchyRoleType.lookup( HierarchyRoleType.MEMBER ) );

    node1.addUserNode( userNode1 );
    node1.addUserNode( userNode2 );
    nodeDAO.saveNode( node1 );

    Node node2 = new Node();
    node2.setName( "testNODENAME2" + uniqueName );
    node2.setDescription( "testNODEDESCRIPTION2" );
    node2.setNodeType( nodeType2 );
    node2.setHierarchy( hierarchy2 );
    node2.setPath( node1.getPath() + node2.getName() );

    UserNode userNode3 = new UserNode();
    userNode3.setIsPrimary( Boolean.TRUE );
    userNode3.setUser( user3 );
    userNode3.setActive( Boolean.TRUE );
    userNode3.setHierarchyRoleType( HierarchyRoleType.lookup( HierarchyRoleType.MEMBER ) );

    node2.addUserNode( userNode3 );
    nodeDAO.saveNode( node2 );

    flushAndClearSession();

    Node newNode1 = nodeDAO.getNodeById( node1.getId() );
    Node newNode2 = nodeDAO.getNodeById( node2.getId() );

    UserNode userNodeToMove = (UserNode)newNode1.getUserNodes().iterator().next();

    List originalUserNodeHistory = getUserNodeHistoryDAO().getAllUserNodeHistoryByUser( userNodeToMove.getUser().getId() );

    newNode1.removeUserNode( userNodeToMove );

    UserNode newUserNode = new UserNode();
    newUserNode.setIsPrimary( Boolean.TRUE );
    newUserNode.setUser( userNodeToMove.getUser() );
    newUserNode.setActive( userNodeToMove.getActive() );
    newUserNode.setHierarchyRoleType( userNodeToMove.getHierarchyRoleType() );

    newNode2.addUserNode( newUserNode );

    nodeDAO.saveNode( newNode1 );
    nodeDAO.saveNode( newNode2 );

    HibernateSessionManager.getSession().flush();
    flushAndClearSession();

    List userNodeHistory = getUserNodeHistoryDAO().getAllUserNodeHistoryByUser( userNodeToMove.getUser().getId() );

    assertEquals( "UserNodeHistory was not correct", originalUserNodeHistory.size() + 1, userNodeHistory.size() );
  }

  private UserNodeHistoryDAO getUserNodeHistoryDAO()
  {
    return (UserNodeHistoryDAO)ApplicationContextFactory.getApplicationContext().getBean( "userNodeHistoryDAO" );
  }

  private NodeDAO getNodeDAO()
  {
    return (NodeDAO)ApplicationContextFactory.getApplicationContext().getBean( "nodeDAO" );
  }

  private NodeTypeDAO getNodeTypeDAO()
  {
    return (NodeTypeDAO)ApplicationContextFactory.getApplicationContext().getBean( "nodeTypeDAO" );
  }

  private HierarchyDAO getHierarchyDAO()
  {
    return (HierarchyDAO)ApplicationContextFactory.getApplicationContext().getBean( "hierarchyDAO" );
  }

  private UserDAO getUserDAO()
  {
    return (UserDAO)ApplicationContextFactory.getApplicationContext().getBean( "userDAO" );
  }
}

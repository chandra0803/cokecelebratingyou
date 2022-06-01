/*
 * (c) 2006 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javatest/com/biperf/core/dao/promotion/hibernate/StackRankNodeDAOImplTest.java,v $
 */

package com.biperf.core.dao.promotion.hibernate;

import java.util.List;
import java.util.Set;

import com.biperf.core.dao.hibernate.BaseDAOTest;
import com.biperf.core.dao.hierarchy.NodeDAO;
import com.biperf.core.dao.hierarchy.hibernate.NodeDAOImplTest;
import com.biperf.core.dao.participant.ParticipantDAO;
import com.biperf.core.dao.participant.hibernate.ParticipantDAOImplTest;
import com.biperf.core.dao.promotion.StackRankDAO;
import com.biperf.core.dao.promotion.StackRankNodeDAO;
import com.biperf.core.domain.enums.HierarchyRoleType;
import com.biperf.core.domain.hierarchy.Node;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.StackRank;
import com.biperf.core.domain.promotion.StackRankNode;
import com.biperf.core.domain.promotion.StackRankParticipant;
import com.biperf.core.domain.user.User;
import com.biperf.core.domain.user.UserNode;
import com.biperf.core.utils.ApplicationContextFactory;

/**
 * StackRankNodeDAOImplTest.
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
 * <td>gaddam</td>
 * <td>Mar 8, 2006</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class StackRankNodeDAOImplTest extends BaseDAOTest
{
  // ---------------------------------------------------------------------------
  // Fields
  // ---------------------------------------------------------------------------

  private static final NodeDAO nodeDao = getNodeDao();
  private static final ParticipantDAO participantDao = getParticipantDao();
  private static final StackRankDAO stackRankDao = getStackRankDao();
  private static final StackRankNodeDAO stackRankNodeDao = getStackRankNodeDao();

  // ---------------------------------------------------------------------------
  // Test Methods
  // ---------------------------------------------------------------------------

  /**
   * Tests selecting a StackRankNode object by ID.
   */
  public void testGetStackRankNodeById()
  {
    // Create a stack rank.
    StackRank stackRank = StackRankDAOImplTest.buildStackRank( getUniqueString() );
    StackRank savedStackRank = stackRankDao.saveStackRank( stackRank );
    assertNotNull( savedStackRank.getId() );

    // Get the first stack rank node.
    Set stackRankNodes = savedStackRank.getStackRankNodes();
    StackRankNode stackRankNode = (StackRankNode)stackRankNodes.iterator().next();
    Long stackRankNodeId = stackRankNode.getId();
    assertNotNull( stackRankNodeId );

    flushAndClearSession();

    // Select the stack rank node.
    StackRankNode retrievedStackRankNode = stackRankNodeDao.getStackRankNode( stackRankNodeId );
    assertNotNull( retrievedStackRankNode );
  }

  /**
   * Tests selecting a StackRankNode object.
   */
  public void testGetStackRankNode()
  {
    StackRank stackRank = StackRankDAOImplTest.buildStackRank( getUniqueString() );

    // Insert the stack rank.
    StackRank savedStackRank = stackRankDao.saveStackRank( stackRank );
    assertNotNull( savedStackRank.getId() );

    flushAndClearSession();

    Set stackRankNodes = savedStackRank.getStackRankNodes();
    StackRankNode savedStackRankNode = (StackRankNode)stackRankNodes.iterator().next();

    // Select the stack rank node.
    StackRankNode retrievedStackRankNode = stackRankNodeDao.getStackRankNode( savedStackRank.getId(), savedStackRankNode.getNode().getId() );
    assertEquals( savedStackRankNode, retrievedStackRankNode );
  }

  /**
   * Tests selecting a StackRankNode object by stack rank ID, node ID, and user ID.
   */
  public void testGetStackRankNodeByStackRankIdNodeIdAndUserId()
  {
    // Insert the stack rank.
    StackRank stackRank = StackRankDAOImplTest.buildStackRank( getUniqueString() );
    stackRank = stackRankDao.saveStackRank( stackRank );
    assertNotNull( stackRank.getId() );

    flushAndClearSession();

    // Select the stack rank node.
    Long stackRankId = stackRank.getId();

    Set stackRankNodes = stackRank.getStackRankNodes();
    StackRankNode savedStackRankNode = (StackRankNode)stackRankNodes.iterator().next();
    Node node = savedStackRankNode.getNode();
    Long nodeId = node.getId();

    UserNode userNode = (UserNode)node.getUserNodes().iterator().next();
    Long userId = userNode.getUser().getId();

    StackRankNode retrievedStackRankNode = stackRankNodeDao.getStackRankNode( stackRankId, nodeId, userId );
    assertEquals( savedStackRankNode, retrievedStackRankNode );
  }

  /**
   * Tests selecting a List of StackRankNode objects.
   */
  public void testGetStackRankNodes()
  {
    // Insert the stack rank.
    StackRank stackRank = StackRankDAOImplTest.buildStackRank( getUniqueString() );
    stackRank = stackRankDao.saveStackRank( stackRank );
    assertNotNull( stackRank.getId() );

    flushAndClearSession();

    // Select the stack rank nodes.
    StackRankNode stackRankNode = (StackRankNode)stackRank.getStackRankNodes().iterator().next();
    User user = ( (StackRankParticipant)stackRankNode.getStackRankParticipants().iterator().next() ).getParticipant();

    StackRankNodeQueryConstraint queryConstraint = new StackRankNodeQueryConstraint();
    queryConstraint.setStackRankIdsIncluded( new Long[] { stackRank.getId() } );
    queryConstraint.setUserId( user.getId() );

    List stackRankNodes = stackRankNodeDao.getStackRankNodes( queryConstraint );
    assertNotNull( stackRankNodes );
    assertTrue( stackRankNodes.size() > 0 );
  }

  // ---------------------------------------------------------------------------
  // Build Methods
  // ---------------------------------------------------------------------------

  /**
   * Returns a new {@link StackRankNode} object.
   * 
   * @param uniqueString a unique string.
   * @return a new {@link StackRankNode} object.
   */
  public static StackRankNode buildStackRankNode( String uniqueString )
  {
    StackRankNode stackRankNode = new StackRankNode();

    // Build and save a participant.
    Participant participant = ParticipantDAOImplTest.buildUniqueParticipant( uniqueString );
    participant = participantDao.saveParticipant( participant );

    // Build and save a node.
    Node node = NodeDAOImplTest.buildUniqueNode( uniqueString );

    UserNode userNode = new UserNode( participant, node );
    userNode.setIsPrimary( Boolean.TRUE );
    userNode.setHierarchyRoleType( HierarchyRoleType.lookup( HierarchyRoleType.OWNER ) );
    userNode.setIsPrimary( false );
    node.addUserNode( userNode );

    node = nodeDao.saveNode( node );
    stackRankNode.setNode( node );

    // Add stack rank participants to the stack rank node.
    stackRankNode.addStackRankParticipant( StackRankParticipantDAOImplTest.buildStackRankParticipant( uniqueString + "1", 1 ) );
    stackRankNode.addStackRankParticipant( StackRankParticipantDAOImplTest.buildStackRankParticipant( uniqueString + "2", 2 ) );
    stackRankNode.addStackRankParticipant( StackRankParticipantDAOImplTest.buildStackRankParticipant( uniqueString + "3", 3 ) );

    return stackRankNode;
  }

  // ---------------------------------------------------------------------------
  // DAO Getter Methods
  // ---------------------------------------------------------------------------

  /**
   * Returns a reference to the node DAO.
   * 
   * @return a reference to the node DAO.
   */
  private static NodeDAO getNodeDao()
  {
    return (NodeDAO)ApplicationContextFactory.getApplicationContext().getBean( NodeDAO.BEAN_NAME );
  }

  /**
   * Returns a reference to the participant DAO.
   * 
   * @return a reference to the participant DAO.
   */
  private static ParticipantDAO getParticipantDao()
  {
    return (ParticipantDAO)ApplicationContextFactory.getApplicationContext().getBean( ParticipantDAO.BEAN_NAME );
  }

  /**
   * Returns a reference to the stack rank DAO.
   * 
   * @return a reference to the stack rank DAO.
   */
  private static StackRankDAO getStackRankDao()
  {
    return (StackRankDAO)ApplicationContextFactory.getApplicationContext().getBean( StackRankDAO.BEAN_NAME );
  }

  /**
   * Returns a reference to the stack rank node DAO.
   * 
   * @return a reference to the stack rank node DAO.
   */
  private static StackRankNodeDAO getStackRankNodeDao()
  {
    return (StackRankNodeDAO)ApplicationContextFactory.getApplicationContext().getBean( StackRankNodeDAO.BEAN_NAME );
  }
}

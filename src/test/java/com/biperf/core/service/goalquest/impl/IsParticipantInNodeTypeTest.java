
package com.biperf.core.service.goalquest.impl;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.easymock.EasyMock;
import org.junit.Test;

import com.biperf.core.domain.enums.HierarchyRoleType;
import com.biperf.core.domain.hierarchy.Node;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.GoalQuestPromotion;
import com.biperf.core.domain.user.User;
import com.biperf.core.domain.user.UserNode;
import com.biperf.core.security.AuthenticatedUser;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.utils.UserManager;

public class IsParticipantInNodeTypeTest extends BaseGQTest
{
  Participant participant, currentUserParticipant;
  GoalQuestPromotion promotion;
  Set<UserNode> nodes;
  UserNode node1, node2;
  AuthenticatedUser currentUser, originalUser;

  @Override
  protected void setUp() throws Exception
  {
    super.setUp();
    participant = new Participant();
    currentUserParticipant = new Participant();
    promotion = new GoalQuestPromotion();
    nodes = new LinkedHashSet<UserNode>();
    node1 = new UserNode();
    node2 = new UserNode();
    // Back up old user and replace it with ours (undone in tearDown)
    currentUser = new AuthenticatedUser();
    currentUser.setUserId( 1L );
    originalUser = UserManager.getUser();
    UserManager.setUser( currentUser );

  }

  @Override
  protected void tearDown() throws Exception
  {
    super.tearDown();
    UserManager.setUser( originalUser );
  }

  /**
   * <b>Goal:</b>
   * <br/><br/>
   * <b>Setup:</b>
   * <br/><br/>
   * <b>Expected Behavior:</b>
   */
  @Test
  public void testValid()
  {
    List<User> usersInNode = new ArrayList<User>();
    User user = new User();
    Node innerNode = new Node();

    participant.setId( 1L );
    user.setId( participant.getId() );
    usersInNode.add( user );

    innerNode.setId( 1L );
    node1.setNode( innerNode );
    node1.setHierarchyRoleType( HierarchyRoleType.lookup( HierarchyRoleType.MANAGER ) );
    node2.setNode( new Node() );
    nodes.add( node1 );
    nodes.add( node2 );
    currentUserParticipant.setUserNodes( nodes );

    EasyMock.expect( mockParticipantService.getParticipantByIdWithAssociations( EasyMock.eq( participant.getId() ), (AssociationRequestCollection)EasyMock.anyObject() ) ).andReturn( participant );
    EasyMock.expect( mockParticipantService.getParticipantById( currentUser.getUserId() ) ).andReturn( currentUserParticipant );
    EasyMock.expect( mockUserService.getUserNodes( currentUser.getUserId() ) ).andReturn( nodes );
    EasyMock.expect( mockUserService.getAllParticipantsOnNode( node1.getNode().getId() ) ).andReturn( usersInNode );

    mockControl.replay();
    Boolean result = testInstance.isParticipantInNodeType( participant, promotion );
    mockControl.verify();
    assertTrue( result );
  }
}
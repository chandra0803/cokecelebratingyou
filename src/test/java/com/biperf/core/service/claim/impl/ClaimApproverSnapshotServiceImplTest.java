/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javatest/com/biperf/core/service/claim/impl/ClaimApproverSnapshotServiceImplTest.java,v $
 */

package com.biperf.core.service.claim.impl;

import java.util.LinkedHashSet;
import java.util.Set;

import org.easymock.EasyMock;

import com.biperf.core.dao.claim.hibernate.ClaimDAOImplTest;
import com.biperf.core.dao.hierarchy.NodeDAO;
import com.biperf.core.dao.hierarchy.hibernate.HierarchyDAOImplTest;
import com.biperf.core.dao.hierarchy.hibernate.NodeDAOImplTest;
import com.biperf.core.dao.hierarchy.hibernate.NodeTypeDAOImplTest;
import com.biperf.core.dao.participant.hibernate.ParticipantDAOImplTest;
import com.biperf.core.dao.promotion.hibernate.PromotionDAOImplTest;
import com.biperf.core.domain.claim.ProductClaim;
import com.biperf.core.domain.enums.ApprovalType;
import com.biperf.core.domain.enums.ApproverType;
import com.biperf.core.domain.enums.HierarchyRoleType;
import com.biperf.core.domain.hierarchy.Hierarchy;
import com.biperf.core.domain.hierarchy.Node;
import com.biperf.core.domain.hierarchy.NodeType;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.ProductClaimPromotion;
import com.biperf.core.domain.user.User;
import com.biperf.core.domain.user.UserNode;
import com.biperf.core.service.BaseServiceTest;
import com.biperf.core.service.email.EmailNotificationService;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.value.ClaimApproversValue;

/**
 * ClaimServiceImplTest.
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
 * <td>wadzinsk</td>
 * <td>Feb 20, 2006</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ClaimApproverSnapshotServiceImplTest extends BaseServiceTest
{
  private ClaimApproverSnapshotServiceImpl classUnderTest;
  private NodeDAO nodeDAOMock;

  private EmailNotificationService emailNotificationServiceMock;
  private ParticipantService participantServiceMock;

  public void setUp() throws Exception
  {
    super.setUp();

    nodeDAOMock = EasyMock.createMock( NodeDAO.class );
    emailNotificationServiceMock = EasyMock.createMock( EmailNotificationService.class );
    participantServiceMock = EasyMock.createMock( ParticipantService.class );

    classUnderTest = new ClaimApproverSnapshotServiceImpl();
    classUnderTest.setEmailNotificationService( emailNotificationServiceMock );
    classUnderTest.setParticipantService( participantServiceMock );
    classUnderTest.setNodeDAO( nodeDAOMock );
  }

  public void testGetApproversNodeByType()
  {
    // Build up hierarchy
    ProductClaim claim = ClaimDAOImplTest.buildStaticProductClaim( true );
    Hierarchy hierarchy = HierarchyDAOImplTest.buildHierarchy( "main" );

    NodeType approverNodeType = NodeTypeDAOImplTest.buildNodeType( "approverNodeType", "approverNodeType" );
    Node approverNode1 = NodeDAOImplTest.buildUniqueNode( "approverNode3", approverNodeType, hierarchy );
    Node approverNode2 = NodeDAOImplTest.buildUniqueNode( "approverNode4", approverNodeType, hierarchy, approverNode1 );
    approverNode1.setId( 5261L );
    approverNode2.setId( 5261L );

    NodeType fillerNodeType = NodeTypeDAOImplTest.buildNodeType( "fillerNodeType", "fillerNodeType" );
    Node fillerNode3 = NodeDAOImplTest.buildUniqueNode( "fillerNode2", fillerNodeType, hierarchy, approverNode2 );
    fillerNode3.setId( 5261L );

    NodeType submitterNodeType = NodeTypeDAOImplTest.buildNodeType( "submitternodetype", "submitternodetype" );
    Node submitterNode4 = NodeDAOImplTest.buildUniqueNode( "submitterNode1", submitterNodeType, hierarchy, fillerNode3 );
    submitterNode4.setId( 5261L );

    Participant approver = ParticipantDAOImplTest.buildUniqueParticipant( "approver" );
    addUserToNode( approverNode1, approver, HierarchyRoleType.OWNER );
    addUserToNode( approverNode2, approver, HierarchyRoleType.OWNER );

    Set<Participant> approvers = new LinkedHashSet<>();
    approvers.add( approver );

    ProductClaimPromotion promotion = PromotionDAOImplTest.buildProductClaimPromotion( "foo" );
    promotion.setApprovalNodeLevels( new Integer( 3 ) );
    promotion.setApprovalType( ApprovalType.lookup( ApprovalType.MANUAL ) );
    promotion.setApproverType( ApproverType.lookup( ApproverType.NODE_OWNER_BY_TYPE ) );
    promotion.setApprovalNodeType( approverNodeType );
    promotion.setApprovalHierarchy( hierarchy );
    claim.setPromotion( promotion );

    // test as non-owner member round 1
    Participant member = ParticipantDAOImplTest.buildUniqueParticipant( "member" );
    addUserToNode( submitterNode4, member, HierarchyRoleType.MEMBER );
    claim.setSubmitter( member );

    claim.setNode( submitterNode4 );

    claim.setApprovalRound( new Long( 1 ) );

    EasyMock.expect( nodeDAOMock.getNodeByNameAndHierarchy( submitterNode4.getName(), hierarchy ) ).andReturn( submitterNode4 );
    EasyMock.replay( nodeDAOMock );
    EasyMock.expect( participantServiceMock.getNodeOwner( submitterNode4.getId() ) ).andReturn( (User)approver );
    EasyMock.replay( participantServiceMock );

    ClaimApproversValue claimApproversValue = classUnderTest.getApprovers( claim );
    EasyMock.reset( nodeDAOMock );
    EasyMock.reset( participantServiceMock );

    assertEquals( approvers, claimApproversValue.getApproverUsers() );
    assertEquals( approverNode2.getId(), claimApproversValue.getSourceNode().getId() );
    assertFalse( claimApproversValue.isAutoApprove() );
    assertTrue( claimApproversValue.isAdditionalApprovalRoundRequired() );

    // test round 2
    claim.setApprovalRound( new Long( 2 ) );
    claim.setLastApprovalNode( claimApproversValue.getSourceNode() );

    EasyMock.expect( nodeDAOMock.getNodeByNameAndHierarchy( submitterNode4.getName(), hierarchy ) ).andReturn( submitterNode4 );
    EasyMock.replay( nodeDAOMock );
    EasyMock.expect( participantServiceMock.getNodeOwner( submitterNode4.getId() ) ).andReturn( (User)approver );
    EasyMock.replay( participantServiceMock );
    claimApproversValue = classUnderTest.getApprovers( claim );
    EasyMock.reset( nodeDAOMock );
    EasyMock.reset( participantServiceMock );

    assertEquals( approvers, claimApproversValue.getApproverUsers() );
    assertEquals( approverNode1.getId(), claimApproversValue.getSourceNode().getId() );
    assertFalse( claimApproversValue.isAutoApprove() );
    assertTrue( claimApproversValue.isAdditionalApprovalRoundRequired() );

    // test round 3
    claim.setApprovalRound( new Long( 3 ) );
    claim.setLastApprovalNode( claimApproversValue.getSourceNode() );

    EasyMock.expect( nodeDAOMock.getNodeByNameAndHierarchy( submitterNode4.getName(), hierarchy ) ).andReturn( submitterNode4 );
    EasyMock.replay( nodeDAOMock );
    EasyMock.expect( participantServiceMock.getNodeOwner( submitterNode4.getId() ) ).andReturn( (User)approver );
    EasyMock.replay( participantServiceMock );
    claimApproversValue = classUnderTest.getApprovers( claim );
    EasyMock.reset( nodeDAOMock );
    EasyMock.reset( participantServiceMock );

    assertTrue( claimApproversValue.isAutoApprove() );
    assertNull( claimApproversValue.getAutoApproveUser() );
    assertFalse( claimApproversValue.isAdditionalApprovalRoundRequired() );

    // //******
    // Test with approver as submitter round 1 - should set auto-approve
    claim.setApprovalRound( new Long( 1 ) );
    addUserToNode( submitterNode4, approver, HierarchyRoleType.MEMBER );
    claim.setLastApprovalNode( null );
    claim.setSubmitter( approver );

    EasyMock.expect( nodeDAOMock.getNodeByNameAndHierarchy( submitterNode4.getName(), hierarchy ) ).andReturn( submitterNode4 );
    EasyMock.replay( nodeDAOMock );
    EasyMock.expect( participantServiceMock.getNodeOwner( submitterNode4.getId() ) ).andReturn( (User)approver );
    EasyMock.replay( participantServiceMock );

    claimApproversValue = classUnderTest.getApprovers( claim );
    EasyMock.reset( nodeDAOMock );
    EasyMock.reset( participantServiceMock );

    assertTrue( claimApproversValue.isAutoApprove() );
    assertTrue( claimApproversValue.isAdditionalApprovalRoundRequired() );
    assertEquals( approverNode2.getId(), claimApproversValue.getSourceNode().getId() );
    assertEquals( approver, claimApproversValue.getAutoApproveUser() );

  }

  public void testGetApproversNodeByLevel()
  {
    // Build up hierarchy
    ProductClaim claim = ClaimDAOImplTest.buildStaticProductClaim( true );
    Hierarchy hierarchy = HierarchyDAOImplTest.buildHierarchy( "main" );

    NodeType approverNodeType = NodeTypeDAOImplTest.buildNodeType( "approverNodeType", "approverNodeType" );
    Node approverNode1 = NodeDAOImplTest.buildUniqueNode( "approverNode3", approverNodeType, hierarchy );
    Node approverNode2 = NodeDAOImplTest.buildUniqueNode( "approverNode4", approverNodeType, hierarchy, approverNode1 );
    approverNode1.setId( 5261L );
    approverNode2.setId( 5261L );
    NodeType fillerNodeType = NodeTypeDAOImplTest.buildNodeType( "fillerNodeType", "fillerNodeType" );
    Node fillerNode3 = NodeDAOImplTest.buildUniqueNode( "fillerNode2", fillerNodeType, hierarchy, approverNode2 );
    fillerNode3.setId( 5261L );

    NodeType submitterNodeType = NodeTypeDAOImplTest.buildNodeType( "submitternodetype", "submitternodetype" );
    Node submitterNode4 = NodeDAOImplTest.buildUniqueNode( "submitterNode1", submitterNodeType, hierarchy, fillerNode3 );
    submitterNode4.setId( 5261L );

    Participant approver = ParticipantDAOImplTest.buildUniqueParticipant( "approver" );
    addUserToNode( approverNode1, approver, HierarchyRoleType.OWNER );
    addUserToNode( approverNode2, approver, HierarchyRoleType.OWNER );

    Set<Participant> approvers = new LinkedHashSet<>();
    approvers.add( approver );

    ProductClaimPromotion promotion = PromotionDAOImplTest.buildProductClaimPromotion( "foo" );
    promotion.setApprovalNodeLevels( new Integer( 3 ) );
    promotion.setApprovalType( ApprovalType.lookup( ApprovalType.MANUAL ) );
    promotion.setApproverType( ApproverType.lookup( ApproverType.NODE_OWNER_BY_LEVEL ) );
    promotion.setApprovalNodeType( approverNodeType );
    promotion.setApprovalHierarchy( hierarchy );
    claim.setPromotion( promotion );

    // test as non-owner member round 1
    Participant member = ParticipantDAOImplTest.buildUniqueParticipant( "member" );
    addUserToNode( submitterNode4, member, HierarchyRoleType.MEMBER );
    claim.setSubmitter( member );

    claim.setNode( submitterNode4 );

    claim.setApprovalRound( new Long( 1 ) );

    EasyMock.expect( nodeDAOMock.getNodeByNameAndHierarchy( submitterNode4.getName(), hierarchy ) ).andReturn( submitterNode4 );
    EasyMock.replay( nodeDAOMock );

    EasyMock.expect( participantServiceMock.getNodeOwner( submitterNode4.getId() ) ).andReturn( (User)approver );
    EasyMock.replay( participantServiceMock );
    ClaimApproversValue claimApproversValue = classUnderTest.getApprovers( claim );
    EasyMock.reset( nodeDAOMock );

    EasyMock.reset( participantServiceMock );

    assertEquals( approvers, claimApproversValue.getApproverUsers() );
    assertEquals( approverNode2.getId(), claimApproversValue.getSourceNode().getId() );
    assertFalse( claimApproversValue.isAutoApprove() );
    assertTrue( claimApproversValue.isAdditionalApprovalRoundRequired() );

    // test round 2
    claim.setApprovalRound( new Long( 2 ) );
    claim.setLastApprovalNode( claimApproversValue.getSourceNode() );

    EasyMock.expect( nodeDAOMock.getNodeByNameAndHierarchy( submitterNode4.getName(), hierarchy ) ).andReturn( submitterNode4 );
    EasyMock.replay( nodeDAOMock );
    EasyMock.expect( participantServiceMock.getNodeOwner( submitterNode4.getId() ) ).andReturn( (User)approver );
    EasyMock.replay( participantServiceMock );
    claimApproversValue = classUnderTest.getApprovers( claim );
    EasyMock.reset( nodeDAOMock );
    EasyMock.reset( participantServiceMock );

    assertEquals( approvers, claimApproversValue.getApproverUsers() );
    assertEquals( approverNode1.getId(), claimApproversValue.getSourceNode().getId() );
    assertFalse( claimApproversValue.isAutoApprove() );
    assertTrue( claimApproversValue.isAdditionalApprovalRoundRequired() );

    // test round 3
    claim.setApprovalRound( new Long( 3 ) );
    claim.setLastApprovalNode( claimApproversValue.getSourceNode() );

    EasyMock.expect( nodeDAOMock.getNodeByNameAndHierarchy( submitterNode4.getName(), hierarchy ) ).andReturn( submitterNode4 );
    EasyMock.replay( nodeDAOMock );
    EasyMock.expect( participantServiceMock.getNodeOwner( submitterNode4.getId() ) ).andReturn( (User)approver );
    EasyMock.replay( participantServiceMock );
    claimApproversValue = classUnderTest.getApprovers( claim );
    EasyMock.reset( nodeDAOMock );
    EasyMock.reset( participantServiceMock );

    assertFalse( claimApproversValue.isAutoApprove() );
    assertNull( claimApproversValue.getAutoApproveUser() );
    assertFalse( claimApproversValue.isAdditionalApprovalRoundRequired() );

    // //******
    // Test with approver as submitter round 1 - should set auto-approve
    claim.setApprovalRound( new Long( 1 ) );
    addUserToNode( submitterNode4, approver, HierarchyRoleType.MEMBER );
    claim.setLastApprovalNode( null );
    claim.setSubmitter( approver );

    EasyMock.expect( nodeDAOMock.getNodeByNameAndHierarchy( submitterNode4.getName(), hierarchy ) ).andReturn( submitterNode4 );
    EasyMock.replay( nodeDAOMock );
    EasyMock.expect( participantServiceMock.getNodeOwner( submitterNode4.getId() ) ).andReturn( (User)approver );
    EasyMock.replay( participantServiceMock );
    claimApproversValue = classUnderTest.getApprovers( claim );
    EasyMock.reset( nodeDAOMock );
    EasyMock.reset( participantServiceMock );

    assertTrue( claimApproversValue.isAutoApprove() );
    assertTrue( claimApproversValue.isAdditionalApprovalRoundRequired() );
    assertEquals( approverNode2.getId(), claimApproversValue.getSourceNode().getId() );
    assertEquals( approver, claimApproversValue.getAutoApproveUser() );

  }

  private void addUserToNode( Node node, User user, String roleTypeCode )
  {
    user.addNode( node, Boolean.TRUE, HierarchyRoleType.lookup( roleTypeCode ), false );
    UserNode userNode = new UserNode( user, node );
    userNode.setIsPrimary( Boolean.TRUE );
    userNode.setHierarchyRoleType( HierarchyRoleType.lookup( roleTypeCode ) );
    node.addUserNode( userNode );
  }
}

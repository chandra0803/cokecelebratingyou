/*
 * (c) 2006 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.service.promotion.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.BooleanUtils;

import com.biperf.core.dao.participant.UserDAO;
import com.biperf.core.dao.promotion.StackRankNodeDAO;
import com.biperf.core.dao.promotion.hibernate.StackRankNodeQueryConstraint;
import com.biperf.core.domain.enums.HierarchyRoleType;
import com.biperf.core.domain.enums.PromotionStatusType;
import com.biperf.core.domain.enums.StackRankState;
import com.biperf.core.domain.enums.SubmittersToRankType;
import com.biperf.core.domain.enums.VisibleStackRankNodeQualifierEnum;
import com.biperf.core.domain.hierarchy.Node;
import com.biperf.core.domain.hierarchy.NodeType;
import com.biperf.core.domain.promotion.ProductClaimPromotion;
import com.biperf.core.domain.promotion.StackRank;
import com.biperf.core.domain.promotion.StackRankNode;
import com.biperf.core.domain.promotion.StackRankPayoutGroup;
import com.biperf.core.domain.user.User;
import com.biperf.core.domain.user.UserNode;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.promotion.StackRankNodeService;

/**
 * StackRankNodeServiceImpl.
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
public class StackRankNodeServiceImpl implements StackRankNodeService
{
  // ---------------------------------------------------------------------------
  // Fields
  // ---------------------------------------------------------------------------

  private StackRankNodeDAO stackRankNodeDAO;
  private UserDAO userDAO;

  // ---------------------------------------------------------------------------
  // Service Methods
  // ---------------------------------------------------------------------------

  /**
   * Returns the specified stack rank node.
   * 
   * @param stackRankNodeId the ID of the stack rank node to retrieve.
   * @return the specified stack rank node.
   */
  public StackRankNode getStackRankNode( Long stackRankNodeId )
  {
    return stackRankNodeDAO.getStackRankNode( stackRankNodeId );
  }

  /**
   * Returns the specified stack rank node.
   * 
   * @param stackRankNodeId the ID of the stack rank node to retrieve.
   * @param associationRequests initializes stack rank node associations.
   * @return the specified stack rank node.
   */
  public StackRankNode getStackRankNode( Long stackRankNodeId, AssociationRequestCollection associationRequests )
  {
    StackRankNode stackRankNode = stackRankNodeDAO.getStackRankNode( stackRankNodeId );
    associationRequests.process( stackRankNode );

    return stackRankNode;
  }

  /**
   * Returns the specified stack rank node.
   * 
   * @param stackRankId
   * @param nodeId
   * @return the specified stack rank node.
   */
  public StackRankNode getStackRankNode( Long stackRankId, Long nodeId )
  {
    return stackRankNodeDAO.getStackRankNode( stackRankId, nodeId );
  }

  /**
   * Returns the specified stack rank node.
   * 
   * @param stackRankId
   * @param nodeId
   * @param stackRankNodeAssociation
   * @return the specified stack rank node.
   */
  public StackRankNode getStackRankNode( Long stackRankId, Long nodeId, AssociationRequestCollection stackRankNodeAssociation )
  {
    StackRankNode stackRankNode = stackRankNodeDAO.getStackRankNode( stackRankId, nodeId );
    stackRankNodeAssociation.process( stackRankNode );

    return stackRankNode;
  }

  /**
   * Returns the specified stack rank node.
   * 
   * @param stackRankId the ID of the stack rank whose stack rank node is returned.
   * @param nodeId the ID of the node whose stack rank node is returned.
   * @param userId a stack rank node is returned only if this user is associated with the given
   *          node.
   * @param associationRequests initializes stack rank node associations.
   * @return the specified stack rank node.
   */
  public StackRankNode getStackRankNode( Long stackRankId, Long nodeId, Long userId, AssociationRequestCollection associationRequests )
  {
    StackRankNode stackRankNode = stackRankNodeDAO.getStackRankNode( stackRankId, nodeId, userId );
    associationRequests.process( stackRankNode );

    return stackRankNode;
  }

  /**
   * Returns the stack rank nodes that satisfy the query constraint.
   * 
   * @param queryConstraint specifies the stack rank nodes to return.
   * @return the stack rank nodes that satisfy the query constraint, as a <code>List</code> of
   *         {@link StackRankNode} objects.
   */
  public List getStackRankNodes( StackRankNodeQueryConstraint queryConstraint )
  {
    return stackRankNodeDAO.getStackRankNodes( queryConstraint );
  }

  // ---------------------------------------------------------------------------
  // Getter and Setter Methods
  // ---------------------------------------------------------------------------
  /**
   * Returns the stack rank node dao.
   * 
   * @return the stack rank dao.
   */
  public StackRankNodeDAO getStackRankNodeDAO()
  {
    return stackRankNodeDAO;
  }

  /**
   * Set the stack rank node dao.
   * 
   * @param stackRankNodeDAO
   */
  public void setStackRankNodeDAO( StackRankNodeDAO stackRankNodeDAO )
  {
    this.stackRankNodeDAO = stackRankNodeDAO;
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.promotion.StackRankNodeService#getVisibleStackRankNodes(java.lang.Long, Long[], Long[])
   * @param userId
   */
  public Map getVisibleStackRankNodes( Long userId, Long[] optionalStackRankIdsIncluded, Long[] optionalNodeTypeIdsIncluded )
  {
    // Set to eliminate dups
    Map visibleStackRankNodes = new LinkedHashMap();

    StackRankNodeQueryConstraint allActiveNodesConstraint = new StackRankNodeQueryConstraint();
    allActiveNodesConstraint.setStackRankStatesIncluded( new StackRankState[] { StackRankState.lookup( StackRankState.STACK_RANK_LISTS_APPROVED ) } );
    allActiveNodesConstraint.setPromotionStatusTypesExcluded( new PromotionStatusType[] { PromotionStatusType.lookup( PromotionStatusType.EXPIRED ) } );
    allActiveNodesConstraint.setStackRankIdsIncluded( optionalStackRankIdsIncluded );
    allActiveNodesConstraint.setNodeTypeIdsIncluded( optionalNodeTypeIdsIncluded );

    List allActiveStackRankNodes = stackRankNodeDAO.getStackRankNodes( allActiveNodesConstraint );
    // Note: youngest stack rank node inclusiong would have been overly complicated/intensive to
    // include in query.
    allActiveStackRankNodes = extractStackRankNodesWithYoungestStackRankPerPromotion( allActiveStackRankNodes );

    // Extract from the list those that where the user is considered a manager or owner
    User user = userDAO.getUserById( userId );
    List managerOwnerNodes = new ArrayList();
    for ( Iterator iter = user.getUserNodes().iterator(); iter.hasNext(); )
    {
      UserNode userNode = (UserNode)iter.next();
      if ( BooleanUtils.isTrue( userNode.isActive() )
          && ( userNode.getHierarchyRoleType().getCode().equals( HierarchyRoleType.OWNER ) || userNode.getHierarchyRoleType().getCode().equals( HierarchyRoleType.MANAGER ) ) )
      {
        managerOwnerNodes.add( userNode.getNode() );
      }
    }

    for ( Iterator iterator = allActiveStackRankNodes.iterator(); iterator.hasNext(); )
    {
      StackRankNode stackRankNode = (StackRankNode)iterator.next();
      Node stackRankNodeNode = stackRankNode.getNode();
      NodeType stackRankNodeNodeType = stackRankNodeNode.getNodeType();
      ProductClaimPromotion promotion = stackRankNode.getStackRank().getPromotion();

      StackRankPayoutGroup stackRankPayoutGroup = promotion.getStackRankPayoutGroup( stackRankNodeNodeType );
      if ( stackRankPayoutGroup != null )
      {
        if ( stackRankPayoutGroup.getSubmittersToRankType().getCode().equals( SubmittersToRankType.SELECTED_NODES ) )
        {
          if ( managerOwnerNodes.contains( stackRankNodeNode ) )
          {
            addOrUpdateStackRankNode( visibleStackRankNodes, stackRankNode, VisibleStackRankNodeQualifierEnum.MANAGER_OWNER );
          }
        }
        else
        {
          // selected node and below
          for ( Iterator iter = managerOwnerNodes.iterator(); iter.hasNext(); )
          {
            Node node = (Node)iter.next();
            if ( node.isMemberOfInputNodeBranch( stackRankNodeNode ) )
            {
              addOrUpdateStackRankNode( visibleStackRankNodes, stackRankNode, VisibleStackRankNodeQualifierEnum.MANAGER_OWNER );
              break;
            }
          }
        }
      }
    }

    Long[] includedStackRankIds = extractStackRankIds( allActiveStackRankNodes );

    // Also add in all stackRankParticipants
    StackRankNodeQueryConstraint paxNodesConstraint = new StackRankNodeQueryConstraint();
    paxNodesConstraint.setStackRankStatesIncluded( new StackRankState[] { StackRankState.lookup( StackRankState.STACK_RANK_LISTS_APPROVED ) } );
    paxNodesConstraint.setStackRankIdsIncluded( includedStackRankIds );
    paxNodesConstraint.setPromotionStatusTypesExcluded( new PromotionStatusType[] { PromotionStatusType.lookup( PromotionStatusType.EXPIRED ) } );
    paxNodesConstraint.setStackRankIdsIncluded( optionalStackRankIdsIncluded );
    paxNodesConstraint.setNodeTypeIdsIncluded( optionalNodeTypeIdsIncluded );
    paxNodesConstraint.setUserId( userId );

    List paxStackRankNodes = stackRankNodeDAO.getStackRankNodes( paxNodesConstraint );
    for ( Iterator iter = paxStackRankNodes.iterator(); iter.hasNext(); )
    {
      StackRankNode stackRankNode = (StackRankNode)iter.next();
      addOrUpdateStackRankNode( visibleStackRankNodes, stackRankNode, VisibleStackRankNodeQualifierEnum.STACK_RANK_PARTICIPANT );
    }

    return visibleStackRankNodes;
  }

  private Long[] extractStackRankIds( List allActiveStackRankNodes )
  {
    List stackRankIds = new ArrayList();

    for ( Iterator iter = allActiveStackRankNodes.iterator(); iter.hasNext(); )
    {
      StackRankNode stackRankNode = (StackRankNode)iter.next();
      stackRankIds.add( stackRankNode.getStackRank().getId() );
    }

    return (Long[])stackRankIds.toArray( new Long[0] );
  }

  /**
   * @param allActiveStackRankNodes
   * @return List
   */
  private List extractStackRankNodesWithYoungestStackRankPerPromotion( List allActiveStackRankNodes )
  {
    List allActiveStackRankNodesYoungest = new ArrayList();

    Map stackRankNodesByPromotion = new LinkedHashMap();

    for ( Iterator iter = allActiveStackRankNodes.iterator(); iter.hasNext(); )
    {
      StackRankNode stackRankNode = (StackRankNode)iter.next();
      List stackRankNodes = (List)stackRankNodesByPromotion.get( stackRankNode.getStackRank().getPromotion() );
      if ( stackRankNodes == null )
      {
        stackRankNodes = new ArrayList();
        stackRankNodesByPromotion.put( stackRankNode.getStackRank().getPromotion(), stackRankNodes );
      }
      stackRankNodes.add( stackRankNode );
    }

    for ( Iterator iter = stackRankNodesByPromotion.values().iterator(); iter.hasNext(); )
    {
      List stackRankNodes = (List)iter.next();
      allActiveStackRankNodesYoungest.addAll( getStackRankNodesFromYoungestStackRank( stackRankNodes ) );
    }

    return allActiveStackRankNodesYoungest;
  }

  private Set getStackRankNodesFromYoungestStackRank( List visibleStackRankNodes )
  {
    Set stackRankNodesFromYoungestStackRank = new LinkedHashSet();

    Set stackRanks = new LinkedHashSet();
    for ( Iterator iter = visibleStackRankNodes.iterator(); iter.hasNext(); )
    {
      StackRankNode stackRankNode = (StackRankNode)iter.next();
      stackRanks.add( stackRankNode.getStackRank() );
    }

    Date youngestDate = new Date( 0 );
    StackRank youngestStackRank = null;
    for ( Iterator iter = stackRanks.iterator(); iter.hasNext(); )
    {
      StackRank stackRank = (StackRank)iter.next();
      Timestamp dateCreated = stackRank.getAuditCreateInfo().getDateCreated();
      if ( dateCreated.after( youngestDate ) )
      {
        youngestDate = dateCreated;
        youngestStackRank = stackRank;
      }
    }

    if ( youngestStackRank != null )
    {
      stackRankNodesFromYoungestStackRank = youngestStackRank.getStackRankNodes();
      // retain only those visible
      stackRankNodesFromYoungestStackRank.retainAll( visibleStackRankNodes );
    }

    return stackRankNodesFromYoungestStackRank;
  }

  private void addOrUpdateStackRankNode( Map visibleStackRankNodes, StackRankNode stackRankNode, VisibleStackRankNodeQualifierEnum visibleStackRankNodeQualifierEnum )
  {
    List visibleStackRankNodeQualifierEnums = (List)visibleStackRankNodes.get( stackRankNode );
    if ( visibleStackRankNodeQualifierEnums == null )
    {
      visibleStackRankNodeQualifierEnums = new ArrayList();
      visibleStackRankNodes.put( stackRankNode, visibleStackRankNodeQualifierEnums );
    }

    visibleStackRankNodeQualifierEnums.add( visibleStackRankNodeQualifierEnum );

  }

  public void setUserDAO( UserDAO userDAO )
  {
    this.userDAO = userDAO;
  }

}

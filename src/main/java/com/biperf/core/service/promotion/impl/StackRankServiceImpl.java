/*
 * (c) 2006 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/promotion/impl/StackRankServiceImpl.java,v $
 */

package com.biperf.core.service.promotion.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.biperf.core.dao.promotion.StackRankDAO;
import com.biperf.core.dao.promotion.StackRankNodeDAO;
import com.biperf.core.dao.promotion.hibernate.StackRankNodeQueryConstraint;
import com.biperf.core.dao.promotion.hibernate.StackRankQueryConstraint;
import com.biperf.core.domain.activity.StackRankActivity;
import com.biperf.core.domain.enums.PromotionPayoutType;
import com.biperf.core.domain.enums.StackRankState;
import com.biperf.core.domain.enums.SubmittersToRankType;
import com.biperf.core.domain.hierarchy.Node;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.ProductClaimPromotion;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.StackRank;
import com.biperf.core.domain.promotion.StackRankNode;
import com.biperf.core.domain.promotion.StackRankParticipant;
import com.biperf.core.domain.promotion.StackRankPayoutGroup;
import com.biperf.core.domain.user.UserNode;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.activity.ActivityService;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.promotion.PromotionEngineService;
import com.biperf.core.service.promotion.StackRankAssociationRequest;
import com.biperf.core.service.promotion.StackRankService;
import com.biperf.core.utils.GuidUtils;
import com.biperf.core.utils.HibernateSessionManager;

/*
 * StackRankServiceImpl <p> <b>Change History:</b><br> <table border="1"> <tr> <th>Author</th>
 * <th>Date</th> <th>Version</th> <th>Comments</th> </tr> <tr> <td>Thomas Eaton</td> <td>Mar
 * 7, 2006</td> <td>1.0</td> <td>created</td> </tr> </table> </p>
 * 
 *
 */

public class StackRankServiceImpl implements StackRankService
{
  // ---------------------------------------------------------------------------
  // Fields
  // ---------------------------------------------------------------------------

  private ActivityService activityService;
  private PromotionEngineService promotionEngineService;

  private StackRankDAO stackRankDAO;
  private StackRankNodeDAO stackRankNodeDAO;

  private ParticipantService participantService;

  // ---------------------------------------------------------------------------
  // Service Methods
  // ---------------------------------------------------------------------------

  /**
   * Approves the specified stack rank and, if appropriate, pays qualifying participants.
   * 
   * @param stackRankId the ID of the stack rank that this method will approve.
   */
  public void approveStackRank( Long stackRankId )
  {
    if ( stackRankId != null )
    {
      StackRank stackRank = stackRankDAO.getStackRank( stackRankId );
      if ( stackRank != null && stackRank.getState().isWaitingForStackRankListsToBeApproved() )
      {
        // Mark the stack rank "approved."
        stackRank.setState( StackRankState.lookup( StackRankState.STACK_RANK_LISTS_APPROVED ) );

        // Pay qualifying stack rank participants.
        if ( stackRank.isCalculatePayout() )
        {
          Promotion promotion = stackRank.getPromotion();

          for ( Iterator srnIter = stackRank.getStackRankNodes().iterator(); srnIter.hasNext(); )
          {
            StackRankNode stackRankNode = (StackRankNode)srnIter.next();
            for ( Iterator srpIter = stackRankNode.getStackRankParticipants().iterator(); srpIter.hasNext(); )
            {
              StackRankParticipant stackRankParticipant = (StackRankParticipant)srpIter.next();
              if ( stackRankParticipant.getPayout() != null && stackRankParticipant.getPayout().longValue() > 0 )
              {
                processStackRankActivities( promotion, stackRankParticipant.getParticipant(), createStackRankActivities( stackRankParticipant ) );
              }
            }
          }
        }
      }
    }
  }

  /**
   * Creates the stack rank lists for the specified stack rank.
   * 
   * @param stackRankId the ID of the stack rank whoses stack rank lists this method will create.
   */
  public void createStackRankLists( Long stackRankId )
  {
    StackRank stackRank = getStackRank( stackRankId );
    HibernateSessionManager.getSession().flush();

    stackRankDAO.createStackRankLists( stackRankId );

    // The procedure only populates stack_rank_participant with those that have an eligible event
    // happen. Populate the rest next (Done in Java since stored procedure can't easily
    // determine criteria audiences)
    if ( !stackRank.getStackRankNodes().isEmpty() )
    {
      Map worstRankByStackRankNode = buildWorstRankByStackRankNode( stackRank.getStackRankNodes() );
      Set eligiblePaxForPromotionList = participantService.getAllEligiblePaxForPromotion( stackRank.getPromotion().getId(), true );

      for ( Iterator iter1 = eligiblePaxForPromotionList.iterator(); iter1.hasNext(); )
      {
        Participant participant = (Participant)iter1.next();

        for ( Iterator iter = stackRank.getStackRankNodes().iterator(); iter.hasNext(); )
        {
          StackRankNode stackRankNode = (StackRankNode)iter.next();
          Set stackRankParticipants = stackRankNode.getStackRankParticipants();

          int newWorstRank = ( (Integer)worstRankByStackRankNode.get( stackRankNode ) ).intValue() + 1;
          if ( isParticipantInRank( stackRankNode, participant ) && !isParticipantInExistingStackRankParticipants( participant, stackRankParticipants ) )
          {
            StackRankParticipant stackRankParticipant = new StackRankParticipant();
            stackRankParticipant.setParticipant( participant );
            stackRankParticipant.setPayout( new Long( 0 ) );
            stackRankParticipant.setStackRankFactor( 0 );
            stackRankParticipant.setRank( newWorstRank );
            stackRankParticipant.setTied( true );
            stackRankParticipant.setStackRankNode( stackRankNode );

            stackRankParticipants.add( stackRankParticipant );
          }
        }
      }
    }
  }

  /**
   * @param stackRankNodes
   */
  private Map buildWorstRankByStackRankNode( Set stackRankNodes )
  {
    Map worstRankByStackRankNode = new LinkedHashMap();

    for ( Iterator iter = stackRankNodes.iterator(); iter.hasNext(); )
    {
      StackRankNode stackRankNode = (StackRankNode)iter.next();
      int worstRank = getWorstRank( stackRankNode.getStackRankParticipants() );
      worstRankByStackRankNode.put( stackRankNode, new Integer( worstRank ) );
    }

    return worstRankByStackRankNode;
  }

  private boolean isParticipantInExistingStackRankParticipants( Participant candidateParticipant, Set stackRankParticipants )
  {
    boolean exists = false;

    for ( Iterator iter = stackRankParticipants.iterator(); iter.hasNext(); )
    {
      StackRankParticipant stackRankParticipant = (StackRankParticipant)iter.next();
      Participant existingParticipant = stackRankParticipant.getParticipant();
      if ( existingParticipant.equals( candidateParticipant ) )
      {
        exists = true;
        break;
      }
    }

    return exists;
  }

  /**
   * Retrun the highest/worst
   * @param stackRankParticipants
   */
  private int getWorstRank( Set stackRankParticipants )
  {
    int worstRank = 0;
    for ( Iterator iter = stackRankParticipants.iterator(); iter.hasNext(); )
    {
      StackRankParticipant stackRankParticipant = (StackRankParticipant)iter.next();
      int currentRank = stackRankParticipant.getRank();
      if ( currentRank > worstRank )
      {
        worstRank = currentRank;
      }
    }
    return worstRank;
  }

  public boolean isParticipantInRank( StackRankNode stackRankNode, Participant participant )
  {
    boolean nodeMatchFound = false;

    Node stackRankNodeNode = stackRankNode.getNode();
    boolean childNodesIncluded = areChildNodeIncluded( stackRankNodeNode, stackRankNode.getStackRank().getPromotion() );

    for ( Iterator iterator = participant.getUserNodes().iterator(); iterator.hasNext(); )
    {
      UserNode userNode = (UserNode)iterator.next();
      Node paxNode = userNode.getNode();
      if ( childNodesIncluded )
      {
        if ( paxNode.isMemberOfInputNodeBranch( stackRankNodeNode ) )
        {
          nodeMatchFound = true;
          break;
        }
      }
      else
      {
        if ( paxNode.equals( stackRankNodeNode ) )
        {
          nodeMatchFound = true;
          break;
        }
      }
    }
    return nodeMatchFound;
  }

  /**
   * Returns true if the stack rank payout group matching this node's nodetype includes
   * child nodes.
   * @param stackRankNodeNode
   * @param promotion
   */
  private boolean areChildNodeIncluded( Node stackRankNodeNode, ProductClaimPromotion promotion )
  {
    for ( Iterator iter = promotion.getStackRankPayoutGroups().iterator(); iter.hasNext(); )
    {
      StackRankPayoutGroup stackRankPayoutGroup = (StackRankPayoutGroup)iter.next();
      if ( stackRankPayoutGroup.getNodeType().equals( stackRankNodeNode.getNodeType() ) && stackRankPayoutGroup.getSubmittersToRankType().getCode().equals( SubmittersToRankType.ALL_NODES ) )
      {
        return true;
      }
    }
    return false;
  }

  /**
   * Deletes the specified stack rank.
   * 
   * @param id the ID of the stack rank to delete.
   */
  public void deleteStackRank( Long id )
  {
    StackRank stackRank = stackRankDAO.getStackRank( id );
    if ( stackRank != null )
    {
      stackRankDAO.deleteStackRank( stackRank );
    }
  }

  /**
   * Returns the specified stack rank.
   * 
   * @param id the ID of the stack rank to get.
   * @return the specified stack rank.
   */
  public StackRank getStackRank( Long id )
  {
    return stackRankDAO.getStackRank( id );
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.promotion.StackRankService#getStackRank(java.lang.Long,
   *      com.biperf.core.service.AssociationRequestCollection)
   * @param id
   * @param associationRequest
   * @return StackRank
   */
  public StackRank getStackRank( Long id, AssociationRequestCollection associationRequest )
  {
    return stackRankDAO.getStackRank( id, associationRequest );
  }

  /**
   * Overridden from
   * 
   * @param promotionId
   * @param state
   * @param associationRequest
   * @return StackRank
   */
  public StackRank getLatestStackRankByPromotionId( Long promotionId, String state, AssociationRequestCollection associationRequest )
  {
    return stackRankDAO.getLatestStackRankByPromotionId( promotionId, state, associationRequest );
  }

  /**
   * Saves the given stack rank.
   * 
   * @param stackRank the stack rank to save.
   * @return the saved version of the stack rank.
   */
  public StackRank saveStackRank( StackRank stackRank )
  {
    // Constraint: A promotion can have at most one stack rank whose state is
    // "waiting for stack rank lists to be approved."
    if ( stackRank.getState().isBeforeCreatingStackRankLists() )
    {
      StackRankQueryConstraint queryConstraint = new StackRankQueryConstraint();
      queryConstraint.setPromotionIdsIncluded( new Long[] { stackRank.getPromotion().getId() } );
      queryConstraint.setStackRankStatesIncluded( new StackRankState[] { StackRankState.lookup( StackRankState.WAITING_FOR_STACK_RANK_LISTS_TO_BE_APPROVED ) } );
      List stackRanksToDelete = stackRankDAO.getStackRankList( queryConstraint, null );

      for ( Iterator iter = stackRanksToDelete.iterator(); iter.hasNext(); )
      {
        StackRank stackRankToDelete = (StackRank)iter.next();
        stackRankDAO.deleteStackRank( stackRankToDelete );
      }
    }

    return stackRankDAO.saveStackRank( stackRank );
  }

  /**
   * Returns a list of stack rank objects.
   * 
   * @param queryConstraint
   * @return a list of stack rank objects
   */
  public List getStackRankList( StackRankQueryConstraint queryConstraint )
  {
    return getStackRankList( queryConstraint, null );
  }

  /**
   * Returns a list of stack rank objects.
   * 
   * @param queryConstraint
   * @param associationRequest
   * @return a list of stack rank objects
   */
  public List getStackRankList( StackRankQueryConstraint queryConstraint, AssociationRequestCollection associationRequest )
  {
    return stackRankDAO.getStackRankList( queryConstraint, associationRequest );
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.promotion.StackRankService#getNodesByStackRankIdAndNodeTypeId(java.lang.Long,
   *      java.lang.Long)
   * @param stackRankId
   * @param nodeTypeId
   * @return List of Nodes
   */
  public List getNodesByStackRankIdAndNodeTypeId( Long stackRankId, Long nodeTypeId )
  {
    List nodesForThisUser = new ArrayList();

    StackRankNodeQueryConstraint constraint = new StackRankNodeQueryConstraint();
    constraint.setStackRankIdsIncluded( new Long[] { stackRankId } );
    constraint.setNodeTypeIdsIncluded( new Long[] { nodeTypeId } );

    List stackRankNodeList = stackRankNodeDAO.getStackRankNodes( constraint );

    for ( Iterator iter = stackRankNodeList.iterator(); iter.hasNext(); )
    {
      StackRankNode stackRankNode = (StackRankNode)iter.next();

      nodesForThisUser.add( stackRankNode.getNode() );
    }

    return nodesForThisUser;
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.promotion.StackRankService#getNodeTypesByStackRankId(java.lang.Long)
   * @param stackRankId
   * @return Set of NodeTypes
   */
  public Set getNodeTypesByStackRankId( Long stackRankId )
  {
    Set nodeTypesForThisUser = new LinkedHashSet();

    StackRankNodeQueryConstraint constraint = new StackRankNodeQueryConstraint();
    constraint.setStackRankIdsIncluded( new Long[] { stackRankId } );

    List stackRankNodeList = stackRankNodeDAO.getStackRankNodes( constraint );

    for ( Iterator iter = stackRankNodeList.iterator(); iter.hasNext(); )
    {
      StackRankNode stackRankNode = (StackRankNode)iter.next();

      nodeTypesForThisUser.add( stackRankNode.getNode().getNodeType() );
    }

    return nodeTypesForThisUser;
  }

  /**
   * Returns a count of stack rank product claim promotions that meet the specified criteria. Any
   * parameter can be left null so that the query is not constrained by that parameter.
   * 
   * @param queryConstraint
   * @return int the stack rank promotion list count with a specified state(#StackRankState)
   */
  public int getStackRankListCount( StackRankQueryConstraint queryConstraint )
  {
    return stackRankDAO.getStackRankListCount( queryConstraint );
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.promotion.StackRankService#getStackRankPromotionsForHomepage(java.lang.Long)
   * @param userId
   * @return List of Promotions where stack rank is viewable for the given user.
   */
  public List getStackRankPromotionsForHomepage( Long userId )
  {
    // Initially use a set because we don't want promotions to show up more than once.
    Set promotionSet = new LinkedHashSet();

    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
    associationRequestCollection.add( new StackRankAssociationRequest( StackRankAssociationRequest.PROMOTION ) );

    StackRankQueryConstraint managerStackRankQueryConstraint = new StackRankQueryConstraint();
    managerStackRankQueryConstraint.setStackRankStatesIncluded( new StackRankState[] { StackRankState.lookup( StackRankState.STACK_RANK_LISTS_APPROVED ) } );
    managerStackRankQueryConstraint.setUserIdForNodeAndBelow( userId );

    List managerStackRanks = stackRankDAO.getStackRankList( managerStackRankQueryConstraint, associationRequestCollection );

    StackRankQueryConstraint participantStackRankQueryConstraint = new StackRankQueryConstraint();
    participantStackRankQueryConstraint.setStackRankStatesIncluded( new StackRankState[] { StackRankState.lookup( StackRankState.STACK_RANK_LISTS_APPROVED ) } );
    participantStackRankQueryConstraint.setUserIdsIncluded( new Long[] { userId } );
    List participantStackRanks = stackRankDAO.getStackRankList( participantStackRankQueryConstraint, associationRequestCollection );

    List stackRanks = new ArrayList();
    stackRanks.addAll( managerStackRanks );
    stackRanks.addAll( participantStackRanks );

    for ( Iterator iter = stackRanks.iterator(); iter.hasNext(); )
    {
      StackRank stackRank = (StackRank)iter.next();

      promotionSet.add( stackRank.getPromotion() );
    }

    return new ArrayList( promotionSet );
  }

  // ---------------------------------------------------------------------------
  // Getter and Setter Methods
  // ---------------------------------------------------------------------------

  public ActivityService getActivityService()
  {
    return activityService;
  }

  public PromotionEngineService getPromotionEngineService()
  {
    return promotionEngineService;
  }

  public StackRankDAO getStackRankDAO()
  {
    return stackRankDAO;
  }

  public void setActivityService( ActivityService activityService )
  {
    this.activityService = activityService;
  }

  public void setPromotionEngineService( PromotionEngineService promotionEngineService )
  {
    this.promotionEngineService = promotionEngineService;
  }

  public void setStackRankDAO( StackRankDAO stackRankDAO )
  {
    this.stackRankDAO = stackRankDAO;
  }

  // ---------------------------------------------------------------------------
  // Stack Rank Processing Methods
  // ---------------------------------------------------------------------------

  /**
   * Creates the stack rank activity for the given stack rank participant.
   * 
   * @param stackRankParticipant information about a participant and his or her rank with in a stack
   *          rank list.
   * @return the stack rank activity for the given stack rank participant, as a <code>Set</code>
   *         of {@link StackRankActivity} objects.
   */
  private Set createStackRankActivities( StackRankParticipant stackRankParticipant )
  {
    List activityList = new ArrayList();

    StackRankActivity activity = new StackRankActivity( GuidUtils.generateGuid() );
    activity.setPromotion( stackRankParticipant.getStackRankNode().getStackRank().getPromotion() );
    activity.setParticipant( stackRankParticipant.getParticipant() );
    activity.setSubmissionDate( new Date() );
    activity.setStackRankParticipant( stackRankParticipant );
    activity.setNode( stackRankParticipant.getStackRankNode().getNode() );
    activityList.add( activity );

    activityService.saveActivities( activityList );

    return new LinkedHashSet( activityList );
  }

  /**
   * Processes stack rank activities for the given promotion and participant.
   * 
   * @param promotion the promotion associated with the given stack rank activities.
   * @param participant the participant whose sales activity generated the stack rank activity.
   * @param stackRankActivities the stack rank activities to be processed, as a <code>Set</code>
   *          of {@link StackRankActivity} objects.
   */
  private void processStackRankActivities( Promotion promotion, Participant participant, Set stackRankActivities )
  {
    try
    {
      Set savedPayouts = promotionEngineService.calculatePayoutAndSaveResults( null, promotion, participant, PromotionPayoutType.STACK_RANK, stackRankActivities );
      promotionEngineService.depositApprovedPayouts( savedPayouts );
    }
    catch( ServiceErrorException e )
    {
      // log.error( e.getServiceErrorsCMText() );
    }
  }

  public void setStackRankNodeDAO( StackRankNodeDAO stackRankNodeDAO )
  {
    this.stackRankNodeDAO = stackRankNodeDAO;
  }

  public void setParticipantService( ParticipantService participantService )
  {
    this.participantService = participantService;
  }
}

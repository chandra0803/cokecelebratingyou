
package com.biperf.core.service.throwdown.impl;

import java.math.BigDecimal;
import java.rmi.server.UID;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;

import com.biperf.core.dao.throwdown.StackStandingDAO;
import com.biperf.core.dao.throwdown.TeamDAO;
import com.biperf.core.domain.hierarchy.Node;
import com.biperf.core.domain.hierarchy.NodeType;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.StackStanding;
import com.biperf.core.domain.promotion.StackStandingNode;
import com.biperf.core.domain.promotion.StackStandingParticipant;
import com.biperf.core.domain.promotion.ThrowdownPromotion;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.hierarchy.NodeService;
import com.biperf.core.service.hierarchy.NodeTypeService;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.participant.UserAssociationRequest;
import com.biperf.core.service.promotion.PromotionAssociationRequest;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.service.throwdown.NodeRankingResult;
import com.biperf.core.service.throwdown.StackRankingResult;
import com.biperf.core.service.throwdown.StackStandingNodeService;
import com.biperf.core.service.throwdown.StackStandingParticipantService;
import com.biperf.core.service.throwdown.StackStandingService;
import com.biperf.core.service.throwdown.TeamProgress;
import com.biperf.core.service.throwdown.TeamRankingResult;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.utils.HibernateSessionManager;

public class StackStandingServiceImpl implements StackStandingService
{
  private StackStandingDAO stackStandingDAO;
  private StackStandingNodeService stackStandingNodeService;
  private StackStandingParticipantService stackStandingParticipantService;
  private PromotionService promotionService;
  private NodeService nodeService;
  private NodeTypeService nodeTypeService;
  private TeamDAO teamDAO;
  private ParticipantService participantService;

  @Override
  public StackStanding createRankingForRound( Long promotionId, int roundNumber ) throws ServiceErrorException
  {
    StackStanding ranking = getRankingForPromotionAndRound( promotionId, roundNumber );
    return buildRanking( ranking, promotionId, roundNumber );
  }

  public StackStanding buildRanking( StackStanding ranking, Long promotionId, Integer roundNumber ) throws ServiceErrorException
  {
    // throw error if ranking approved one exists for this promotion and round
    if ( ranking != null && ( ranking.isPayoutsIssued() || stackStandingDAO.isAnyPaxPaidOutForRanking( ranking.getId() ) ) )
    {
      throw new ServiceErrorException( ServiceErrorMessageKeys.ROUND_APPROVED_RANKING_EXISTS );
    }

    // delete if overall/round ranking one exists for this promotion and then recreate
    if ( ranking != null )
    {
      ranking.setActive( false );
    }

    // build promotions
    StackRankingResult stackResult = buildStackRankingResult( promotionId, roundNumber );

    // build nodes
    buildNodeRankingResult( stackResult );

    // build teams by node
    buildTeamRankingResult( stackResult );

    // create ranking
    return createStackRank( stackResult );
  }

  @SuppressWarnings( "unchecked" )
  private StackRankingResult buildStackRankingResult( Long promotionId, Integer roundNumber )
  {
    StackRankingResult stackResult = new StackRankingResult();
    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
    associationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.THROWDOWN_PAYOUTS ) );
    associationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.THROWDOWN_DIVISION_MATCH_OUTCOMES ) );
    ThrowdownPromotion promotion = getThrowdownPromotionByIdWithAssociations( promotionId, associationRequestCollection );
    stackResult.setPromotion( promotion );
    stackResult.setRoundNumber( roundNumber );
    return stackResult;
  }

  private void buildNodeRankingResult( StackRankingResult stackResult )
  {
    ThrowdownPromotion promotion = stackResult.getPromotion();
    List<NodeType> nodeTypes = nodeTypeService.getAll();
    if ( promotion.hasHierarchyStackStandingPayoutGroup() )
    {
      NodeRankingResult nodeResult = new NodeRankingResult();
      stackResult.addNodeRanking( nodeResult );
    }
    for ( NodeType nodeType : nodeTypes )
    {
      if ( promotion.hasStackStandingPayoutGroupForNodeType( nodeType ) )
      {
        List nodes = nodeService.getNodesByNodeType( nodeType );
        for ( Iterator nodeIter = nodes.iterator(); nodeIter.hasNext(); )
        {
          Node node = (Node)nodeIter.next();
          NodeRankingResult nodeResult = new NodeRankingResult();
          nodeResult.setNode( node );
          stackResult.addNodeRanking( nodeResult );
        }
      }
    }
  }

  private void buildTeamRankingResult( StackRankingResult stackResult ) throws ServiceErrorException
  {
    int precision = stackResult.getPromotion().getAchievementPrecision().getPrecision();
    int roundingMode = stackResult.getPromotion().getRoundingMethod().getBigDecimalRoundingMode();
    List<Long> participants = teamDAO.getPaxPlayingPromotionInRound( stackResult.getPromotion().getId(), stackResult.getRoundNumber() );
    List<TeamProgress> teamProgress = teamDAO.getAllPaxsCumulativeProgressUptoRound( stackResult.getPromotion().getId(), stackResult.getRoundNumber() );
    List<TeamRankingResult> teamResults = new ArrayList<TeamRankingResult>();

    for ( TeamProgress progress : teamProgress )
    {
      if ( participants.contains( progress.getUserId() ) )
      {
        AssociationRequestCollection reqs = new AssociationRequestCollection();
        reqs.add( new UserAssociationRequest( UserAssociationRequest.NODE ) );
        Participant participant = participantService.getParticipantByIdWithAssociations( progress.getUserId(), reqs );
        if ( participant.isActive() )
        {
          TeamRankingResult teamResult = new TeamRankingResult();
          teamResult.setParticipant( participant );
          teamResult.setRankingFactor( progress.getProgress() );
          teamResults.add( teamResult );
        }
      }
    }

    for ( NodeRankingResult nodeResult : stackResult.getNodeRankings() )
    {
      if ( nodeResult.isHierarchyRanking() )
      {
        for ( TeamRankingResult teamResult : teamResults )
        {
          try
          {
            TeamRankingResult clonedTeamResult = (TeamRankingResult)teamResult.deepCopy();
            nodeResult.addTeamResult( clonedTeamResult );
          }
          catch( CloneNotSupportedException cnse )
          {
            throw new ServiceErrorException( "Team (id: " + teamResult.getTeam().getId() + ") failed cloning", cnse );
          }
        }
      }
      else
      {
        List<Node> allChildNodes = nodeService.allChildNodesUnderParentNodes( nodeResult.getNode().getId() );
        for ( TeamRankingResult teamResult : teamResults )
        {
          if ( allChildNodes != null && teamResult.getUserNodes() != null && CollectionUtils.containsAny( allChildNodes, teamResult.getUserNodes() ) )
          {
            try
            {
              TeamRankingResult clonedTeamResult = (TeamRankingResult)teamResult.deepCopy();
              nodeResult.addTeamResult( clonedTeamResult );
            }
            catch( CloneNotSupportedException cnse )
            {
              throw new ServiceErrorException( "Team (id: " + teamResult.getTeam().getId() + ") failed cloning", cnse );
            }
          }
        }
      }
      // rank teams within node
      rankTeams( nodeResult, precision, roundingMode );
    }

  }

  private void rankTeams( NodeRankingResult nodeRanking, int precision, int roundingMode )
  {
    // Since we fetch it by progress descending, we dont need comparator and hence below code is
    // commented out.
    // Comparator<TeamRankingResult> userProgressComparator = new ThrowdownUserProgressComparator();
    // Collections.sort( nodeRanking.getTeamResults(), userProgressComparator );
    int rank = 0;
    boolean tied = false;
    TeamRankingResult prevResult = null;
    BigDecimal prevResultProgress = null;
    BigDecimal currentTeamResultProgress = null;
    int numberOfTied = 0;
    for ( TeamRankingResult result : nodeRanking.getTeamResults() )
    {
      currentTeamResultProgress = result.getTeamProgress().setScale( precision, roundingMode );
      if ( prevResultProgress != null && prevResultProgress.equals( currentTeamResultProgress ) )
      {
        tied = true;
        prevResult.setTied( true );
        numberOfTied = numberOfTied + 1;
      }
      else
      {
        tied = false;
        rank = rank + numberOfTied + 1;
        numberOfTied = 0;
      }
      result.setTied( tied );
      result.setRank( rank );
      prevResult = result;
      prevResultProgress = prevResult.getTeamProgress().setScale( precision, roundingMode );
    }
  }

  private StackStanding createStackRank( StackRankingResult stackResult )
  {
    StackStanding ranking = new StackStanding();
    ranking.setActive( true );
    ranking.setPromotion( stackResult.getPromotion() );
    if ( stackResult.getRoundNumber() != null )
    {
      ranking.setRoundNumber( stackResult.getRoundNumber() );
    }
    ranking.setGuid( new UID().toString() );
    ranking.setPayoutsIssued( false );

    ranking = save( ranking );

    // creates ranking nodes
    createStackRankNodes( ranking, stackResult.getNodeRankings() );

    HibernateSessionManager.getSession().flush();

    return ranking;
  }

  private void createStackRankNodes( StackStanding stackRank, List<NodeRankingResult> nodeRankings )
  {
    for ( NodeRankingResult nodeRanking : nodeRankings )
    {
      // create node only if it has participants with rank.
      if ( !nodeRanking.getTeamResults().isEmpty() )
      {
        StackStandingNode rankingNode = createStackRankNode( stackRank, nodeRanking );
        stackRank.addStackStandingNode( rankingNode );

        // creates ranking participants
        createStackRankParticipants( rankingNode, nodeRanking.getTeamResults() );
      }
    }
  }

  private StackStandingNode createStackRankNode( StackStanding stackRank, NodeRankingResult nodeRanking )
  {
    StackStandingNode rankingNode = new StackStandingNode();
    rankingNode.setStackStanding( stackRank );
    rankingNode.setNode( nodeRanking.getNode() );
    return stackStandingNodeService.saveStackStandingNode( rankingNode );
  }

  private void createStackRankParticipants( StackStandingNode stackRankNode, List<TeamRankingResult> teamResults )
  {
    for ( TeamRankingResult teamResult : teamResults )
    {
      StackStandingParticipant rankingPax = createStackRankParticipant( stackRankNode, teamResult );
      stackRankNode.addStackStandingParticipant( rankingPax );
    }
  }

  private StackStandingParticipant createStackRankParticipant( StackStandingNode stackRankNode, TeamRankingResult teamResult )
  {
    ThrowdownPromotion promotion = stackRankNode.getStackStanding().getPromotion();
    int precision = promotion.getAchievementPrecision().getPrecision();
    int roundingMode = promotion.getRoundingMethod().getBigDecimalRoundingMode();
    BigDecimal rankingFactor = teamResult.getTeamProgress().setScale( precision, roundingMode );

    StackStandingParticipant rankingPax = new StackStandingParticipant();
    rankingPax.setStackStandingNode( stackRankNode );
    rankingPax.setParticipant( teamResult.getParticipant() );
    rankingPax.setStackStandingFactor( rankingFactor );
    rankingPax.setStanding( teamResult.getRank() );
    rankingPax.setTied( teamResult.isTied() );
    return saveStackStandingParticipant( rankingPax );
  }

  public boolean isAnyPaxPaidOutForRanking( Long stackStandingId )
  {
    return stackStandingDAO.isAnyPaxPaidOutForRanking( stackStandingId );
  }

  public Integer getNodeRankForUser( Long promotionId, int roundNumber, Long userId, Long nodeId )
  {
    return stackStandingDAO.getNodeRankForUser( promotionId, roundNumber, userId, nodeId );
  }

  public Integer getHierarchyRankForUser( Long promotionId, int roundNumber, Long userId )
  {
    Integer rank = null;
    rank = stackStandingDAO.getHierarchyRankForUser( promotionId, roundNumber, userId );
    if ( rank == null && roundNumber > 1 )
    {
      // handle progress back logs
      while ( rank == null && roundNumber > 0 )
      {
        roundNumber = roundNumber - 1;
        rank = stackStandingDAO.getHierarchyRankForUser( promotionId, roundNumber, userId );
      }
    }
    return rank;
  }

  public StackStandingParticipant getHierarchyRankDetailsForPax( Long promotionId, int roundNumber, Long userId )
  {
    return stackStandingParticipantService.getHierarchyRankDetailsForPax( promotionId, roundNumber, userId );
  }

  public Integer getTotalUsersInHierarchyRanking( Long promotionId, int roundNumber )
  {
    return stackStandingDAO.getTotalUsersInHierarchyRanking( promotionId, roundNumber );
  }

  public StackStandingParticipant getStackStandingParticipant( Long stackStandingNodeId, Long userId )
  {
    return stackStandingParticipantService.getStackStandingParticipant( stackStandingNodeId, userId );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public StackStanding get( Long stackStandingId )
  {
    return stackStandingDAO.get( stackStandingId );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public StackStanding get( Long stackStandingId, AssociationRequestCollection associationRequest )
  {
    return stackStandingDAO.get( stackStandingId, associationRequest );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public StackStanding save( StackStanding stackStanding )
  {
    return stackStandingDAO.save( stackStanding );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<StackStanding> getAll()
  {
    return stackStandingDAO.getAll();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<StackStanding> getRankings()
  {
    return stackStandingDAO.getRankings();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<StackStanding> getRankingsForPromotion( Long promotionId )
  {
    return stackStandingDAO.getRankingsForPromotion( promotionId );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public StackStanding getRankingForPromotionAndRound( Long promotionId, int roundNumber )
  {
    return stackStandingDAO.getRankingForPromotionAndRound( promotionId, roundNumber );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<StackStanding> getApprovedRankings()
  {
    return stackStandingDAO.getApprovedRankings();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<StackStanding> getApprovedRankingsForPromotion( Long promotionId )
  {
    return stackStandingDAO.getApprovedRankingsForPromotion( promotionId );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public StackStanding getApprovedRankingForPromotionAndRound( Long promotionId, int roundNumber )
  {
    return stackStandingDAO.getApprovedRankingForPromotionAndRound( promotionId, roundNumber );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<StackStanding> getUnapprovedRankings()
  {
    return stackStandingDAO.getUnapprovedRankings();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<StackStanding> getUnapprovedRankingsForPromotion( Long promotionId )
  {
    return stackStandingDAO.getUnapprovedRankingsForPromotion( promotionId );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public StackStandingParticipant saveStackStandingParticipant( StackStandingParticipant stackStandingParticipant )
  {
    return stackStandingParticipantService.saveStackStandingParticipant( stackStandingParticipant );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<StackStandingParticipant> getAllRankingParticipants( Long rankingNodeId )
  {
    return stackStandingParticipantService.getAllRankingParticipants( rankingNodeId );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<StackStandingParticipant> getPageRankingParticipants( Long rankingNodeId, int fromIndex, int endIndex )
  {
    return stackStandingParticipantService.getPageRankingParticipants( rankingNodeId, fromIndex, endIndex );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<StackStandingParticipant> getTopRankingParticipants( Long rankingNodeId )
  {
    return stackStandingParticipantService.getTopRankingParticipants( rankingNodeId );
  }

  @Override
  public int getTotalRankingParticipants( Long rankingNodeId )
  {
    return stackStandingParticipantService.getTotalRankingParticipants( rankingNodeId );
  }

  @Override
  public int getPaxPositionInRanking( Long rankingNodeId, Long userId )
  {
    return stackStandingParticipantService.getPaxPositionInRanking( rankingNodeId, userId );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public StackStanding getUnapprovedRankingForPromotionAndRound( Long promotionId, int roundNumber )
  {
    return stackStandingDAO.getUnapprovedRankingForPromotionAndRound( promotionId, roundNumber );
  }

  private ThrowdownPromotion getThrowdownPromotionByIdWithAssociations( Long promotionId, AssociationRequestCollection associationRequestCollection )
  {
    return (ThrowdownPromotion)promotionService.getPromotionByIdWithAssociations( promotionId, associationRequestCollection );
  }

  public void setStackStandingDAO( StackStandingDAO stackStandingDAO )
  {
    this.stackStandingDAO = stackStandingDAO;
  }

  public void setPromotionService( PromotionService promotionService )
  {
    this.promotionService = promotionService;
  }

  public void setNodeService( NodeService nodeService )
  {
    this.nodeService = nodeService;
  }

  public void setStackStandingNodeService( StackStandingNodeService stackStandingNodeService )
  {
    this.stackStandingNodeService = stackStandingNodeService;
  }

  public void setStackStandingParticipantService( StackStandingParticipantService stackStandingParticipantService )
  {
    this.stackStandingParticipantService = stackStandingParticipantService;
  }

  public void setNodeTypeService( NodeTypeService nodeTypeService )
  {
    this.nodeTypeService = nodeTypeService;
  }

  public void setTeamDAO( TeamDAO teamDAO )
  {
    this.teamDAO = teamDAO;
  }

  public void setParticipantService( ParticipantService participantService )
  {
    this.participantService = participantService;
  }

}

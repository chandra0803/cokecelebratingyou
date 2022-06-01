/*
 * (c) 2007 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/promotion/engine/PerAchieverManagerOverrideGoalStrategy.java,v $
 */

package com.biperf.core.service.promotion.engine;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.biperf.core.domain.enums.HierarchyRoleType;
import com.biperf.core.domain.goalquest.GoalQuestAwardSummary;
import com.biperf.core.domain.goalquest.PaxGoal;
import com.biperf.core.domain.hierarchy.Node;
import com.biperf.core.domain.promotion.GoalQuestPromotion;
import com.biperf.core.domain.user.User;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.SAO;
import com.biperf.core.service.hierarchy.NodeService;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.participant.UserAssociationRequest;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.utils.BeanLocator;

/**
 * PercentTeamManagerOverrideGoalStrategy.
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
 * <td>meadows</td>
 * <td>Jan 8, 2007</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class PerAchieverManagerOverrideGoalStrategy extends AbstractManagerOverrideGoalStrategy
{

  /**
   * Overridden from @see com.biperf.core.service.promotion.engine.AbstractManagerOverrideGoalStrategy#processGoalInternal(com.biperf.core.domain.promotion.GoalQuestPromotion, java.util.Set, java.util.Set)
   * @param promotion
   * @param managerOverrideGoal
   * @param goalCalculationResults
   * @return
   */
  protected List<GoalCalculationResult> processGoalInternal( GoalQuestPromotion promotion, PaxGoal managerOverrideGoal, List goalCalculationResults, Long nodeId )
  {
    List<GoalCalculationResult> managerOverrides = new ArrayList<GoalCalculationResult>();
    User nodeOwner = getNodeManager( nodeId );
    if ( promotion != null && promotion.getLevelOneMgrAward() != null )
    {
      GoalCalculationResult goalCalculationResult = new GoalCalculationResult();
      if ( nodeOwner != null )
      {
        double numberOfUserAchieved = 0;
        for ( Iterator goalResultIter = goalCalculationResults.iterator(); goalResultIter.hasNext(); )
        {
          GoalCalculationResult currentGoalCalculationResult = (GoalCalculationResult)goalResultIter.next();
          if ( currentGoalCalculationResult.isAchieved()
              && ! ( managerOverrideGoal != null && managerOverrideGoal.getParticipant().getId().equals( currentGoalCalculationResult.getReciever().getId() ) ) )
          {
            numberOfUserAchieved++;
          }
        }
        BigDecimal totalPerformance = new BigDecimal( numberOfUserAchieved );
        goalCalculationResult.setTotalPerformance( totalPerformance );
        goalCalculationResult.setLeveloneAward( true );
        BigDecimal flatpayout = new BigDecimal( promotion.getLevelOneMgrAward().longValue() );
        if ( totalPerformance.compareTo( new BigDecimal( "0" ) ) > 0 )
        {
          goalCalculationResult.setAchieved( true );
          BigDecimal calculatePayout = totalPerformance.multiply( flatpayout );
          goalCalculationResult.setCalculatedPayout( calculatePayout.setScale( promotion.getAchievementPrecision().getPrecision(), promotion.getRoundingMethod().getBigDecimalRoundingMode() ) );
        }
        if ( goalCalculationResult != null )
        {
          goalCalculationResult.setReciever( nodeOwner );
          goalCalculationResult.setNodeRole( HierarchyRoleType.lookup( HierarchyRoleType.OWNER ) );
          goalCalculationResult.setOwner( true );
          managerOverrides.add( goalCalculationResult );
        }
      }
      else
      {
        goalCalculationResult.setLeveloneAward( true );
        managerOverrides.add( goalCalculationResult );
      }

    }
    Node node = getNodeService().getNodeById( nodeId );
    Node parentNode = node.getParentNode();
    User parentNodeOwner = null;

    if ( parentNode != null && parentNode != null && parentNode.getNodeOwner() != null )
    {
      parentNodeOwner = parentNode.getNodeOwner();
    }

    if ( promotion != null && promotion.getLevelTwoMgrAward() != null && parentNodeOwner != null )
    {
      GoalCalculationResult goalCalculationResult = new GoalCalculationResult();
      double numberOfUserAchieved = 0;
      for ( Iterator goalResultIter = goalCalculationResults.iterator(); goalResultIter.hasNext(); )
      {
        GoalCalculationResult currentGoalCalculationResult = (GoalCalculationResult)goalResultIter.next();
        if ( currentGoalCalculationResult.isAchieved()
            && ! ( managerOverrideGoal != null && managerOverrideGoal.getParticipant().getId().equals( currentGoalCalculationResult.getReciever().getId() ) ) )
        {
          numberOfUserAchieved++;
        }
      }
      BigDecimal totalPerformance = new BigDecimal( numberOfUserAchieved );
      goalCalculationResult.setTotalPerformance( totalPerformance );
      goalCalculationResult.setLeveloneAward( false );
      BigDecimal flatpayout = new BigDecimal( promotion.getLevelTwoMgrAward().longValue() );
      if ( totalPerformance.compareTo( new BigDecimal( "0" ) ) > 0 )
      {
        goalCalculationResult.setAchieved( true );
        BigDecimal calculatePayout = totalPerformance.multiply( flatpayout );
        goalCalculationResult.setCalculatedPayout( calculatePayout.setScale( promotion.getAchievementPrecision().getPrecision(), promotion.getRoundingMethod().getBigDecimalRoundingMode() ) );
      }
      if ( goalCalculationResult != null )
      {
        goalCalculationResult.setReciever( parentNodeOwner );
        goalCalculationResult.setNodeRole( HierarchyRoleType.lookup( HierarchyRoleType.OWNER ) );
        goalCalculationResult.setOwner( true );
        managerOverrides.add( goalCalculationResult );
      }

    }
    return managerOverrides;
  }

  /**
   * @param promotion is a GoalQuestPromotion that goals are being calculated for
   * @param managerOverrideResults is a List of GoalCalculationResult for the manager overrides
   * @param paxGoalList is a List of all paxGoals for the promotion
   * @return a List of GoalQuestAwardSummary objects
   */
  public List summarizeResultsInternal( GoalQuestPromotion promotion, List managerOverrideResults, List paxGoalList )
  {
    GoalQuestAwardSummary goalQuestLevelOneAwardSummary = new GoalQuestAwardSummary();
    GoalQuestAwardSummary goalQuestLevelTwoAwardSummary = new GoalQuestAwardSummary();
    goalQuestLevelOneAwardSummary.setManagerTotals( true );
    goalQuestLevelTwoAwardSummary.setManagerTotals( true );
    List results = new ArrayList();
    BigDecimal totalAward = new BigDecimal( 0 );
    int totalSelected = 0;
    int totalAchieved = 0;
    if ( managerOverrideResults != null )
    {
      for ( Iterator managerOverrideResultIter = managerOverrideResults.iterator(); managerOverrideResultIter.hasNext(); )
      {
        GoalCalculationResult managerOverrideResult = (GoalCalculationResult)managerOverrideResultIter.next();
        if ( managerOverrideResult.isLeveloneAward() )
        {
          if ( managerOverrideResult.isAchieved() )
          {
            totalAchieved++;
            totalSelected += managerOverrideResult.getTotalPerformance().intValue();
            totalAward = totalAward.add( managerOverrideResult.getCalculatedPayout() );
            goalQuestLevelOneAwardSummary.incrementTotalAward( managerOverrideResult.getCalculatedPayout() );
            goalQuestLevelOneAwardSummary.incrementTotalAchieved();
          }
          goalQuestLevelOneAwardSummary.setLeveloneAward( true );
        }
        else if ( managerOverrideResult.isAchieved() && !managerOverrideResult.isLeveloneAward() )
        {
          totalAchieved++;
          totalSelected += managerOverrideResult.getTotalPerformance().intValue();
          totalAward = totalAward.add( managerOverrideResult.getCalculatedPayout() );
          goalQuestLevelTwoAwardSummary.incrementTotalAward( managerOverrideResult.getCalculatedPayout() );
          goalQuestLevelTwoAwardSummary.incrementTotalAchieved();
          goalQuestLevelTwoAwardSummary.setLeveloneAward( false );
        }
      }
    }
    results.add( goalQuestLevelOneAwardSummary );
    results.add( goalQuestLevelTwoAwardSummary );
    GoalQuestAwardSummary finalGoalQuestAwardSummary = new GoalQuestAwardSummary();
    finalGoalQuestAwardSummary.setTotalAchieved( totalAchieved );
    finalGoalQuestAwardSummary.setTotalAward( totalAward );
    finalGoalQuestAwardSummary.setTotalSelected( totalSelected );
    finalGoalQuestAwardSummary.setManagerTotals( false );
    results.add( finalGoalQuestAwardSummary );
    return results;
  }

  @SuppressWarnings( "unchecked" )
  private User getNodeManager( Long nodeId )
  {
    if ( nodeId != null )
    {
      AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
      associationRequestCollection.add( new UserAssociationRequest( UserAssociationRequest.ADDRESS ) );
      associationRequestCollection.add( new UserAssociationRequest( UserAssociationRequest.EMAIL ) );
      associationRequestCollection.add( new UserAssociationRequest( UserAssociationRequest.PHONE ) );
      List users = getUserService().getAllUsersOnNodeHavingRole( nodeId, HierarchyRoleType.lookup( HierarchyRoleType.OWNER ), associationRequestCollection );
      if ( users != null && users.size() > 0 )
      {
        return (User)users.get( 0 );
      }
    }
    return null;
  }

  protected static SAO getService( String beanName )
  {
    return (SAO)BeanLocator.getBean( beanName );
  }

  private static ParticipantService getParticipantService()
  {
    return (ParticipantService)getService( ParticipantService.BEAN_NAME );
  }

  private static UserService getUserService()
  {
    return (UserService)getService( UserService.BEAN_NAME );
  }

  private static NodeService getNodeService()
  {
    return (NodeService)getService( NodeService.BEAN_NAME );
  }
}

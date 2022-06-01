/*
 * (c) 2007 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/promotion/engine/PercentTeamManagerOverrideGoalStrategy.java,v $
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
public class PercentTeamManagerOverrideGoalStrategy extends AbstractManagerOverrideGoalStrategy
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
        Integer overridePercent = promotion.getLevelOneMgrAward().intValue();
        if ( overridePercent != null )
        {
          BigDecimal totalAward = new BigDecimal( "0" );
          for ( Iterator goalResultIter = goalCalculationResults.iterator(); goalResultIter.hasNext(); )
          {
            GoalCalculationResult currentGoalCalculationResult = (GoalCalculationResult)goalResultIter.next();
            if ( currentGoalCalculationResult.getCalculatedPayout() != null )
            {
              if ( currentGoalCalculationResult.isAchieved()
                  && ! ( managerOverrideGoal != null && managerOverrideGoal.getParticipant().getId().equals( currentGoalCalculationResult.getReciever().getId() ) ) )
              {
                totalAward = totalAward.add( currentGoalCalculationResult.getCalculatedPayout() );
              }

            }
          }
          goalCalculationResult.setLeveloneAward( true );
          goalCalculationResult.setTotalPerformance( totalAward );
          long lngtotalAward = totalAward.longValueExact();
          if ( overridePercent > 0 )
          {
            goalCalculationResult.setAchieved( true );
            // override= Total amount * ( amount entered/100)
            Double rawPayout = new Double( lngtotalAward * ( overridePercent.doubleValue() / 100 ) );
            int roundPoint = Integer.parseInt( rawPayout.toString().substring( rawPayout.toString().indexOf( '.' ) + 1, rawPayout.toString().indexOf( '.' ) + 2 ) );
            if ( roundPoint > 0 && roundPoint < 5 )
            {
              // Add .5 to the raw number for the rounding to make sure all things round up.
              rawPayout = new Double( rawPayout.doubleValue() + .5 );
            }
            long payout = Math.round( rawPayout.doubleValue() );
            BigDecimal managersAward = new BigDecimal( payout );
            goalCalculationResult.setCalculatedPayout( managersAward.setScale( 0, promotion.getRoundingMethod().getBigDecimalRoundingMode() ) );
          }
        }
        if ( goalCalculationResult != null && goalCalculationResult.getCalculatedPayout().compareTo( new BigDecimal( "0" ) ) > 0 )
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
    if ( parentNode != null && parentNode.getNodeOwner() != null )
    {
      parentNodeOwner = parentNode.getNodeOwner();
    }

    if ( promotion != null && promotion.getLevelTwoMgrAward() != null && parentNodeOwner != null )
    {
      GoalCalculationResult goalCalculationResult = new GoalCalculationResult();
      Integer overridePercent = promotion.getLevelTwoMgrAward().intValue();
      if ( overridePercent != null )
      {
        BigDecimal totalAward = new BigDecimal( "0" );
        for ( Iterator goalResultIter = goalCalculationResults.iterator(); goalResultIter.hasNext(); )
        {
          GoalCalculationResult currentGoalCalculationResult = (GoalCalculationResult)goalResultIter.next();
          if ( currentGoalCalculationResult.getCalculatedPayout() != null )
          {
            if ( currentGoalCalculationResult.isAchieved()
                && ! ( managerOverrideGoal != null && managerOverrideGoal.getParticipant().getId().equals( currentGoalCalculationResult.getReciever().getId() ) ) )
            {
              totalAward = totalAward.add( currentGoalCalculationResult.getCalculatedPayout() );
            }
          }
        }
        goalCalculationResult.setTotalPerformance( totalAward );
        goalCalculationResult.setLeveloneAward( false );
        long lngtotalAward = totalAward.longValueExact();
        if ( overridePercent > 0 )
        {
          goalCalculationResult.setAchieved( true );
          // override= Total amount * ( amount entered/100)
          Double rawPayout = new Double( lngtotalAward * ( overridePercent.doubleValue() / 100 ) );
          int roundPoint = Integer.parseInt( rawPayout.toString().substring( rawPayout.toString().indexOf( '.' ) + 1, rawPayout.toString().indexOf( '.' ) + 2 ) );
          if ( roundPoint > 0 && roundPoint < 5 )
          {
            // Add .5 to the raw number for the rounding to make sure all things round up.
            rawPayout = new Double( rawPayout.doubleValue() + .5 );
          }
          long payout = Math.round( rawPayout.doubleValue() );
          BigDecimal managersAward = new BigDecimal( payout );
          goalCalculationResult.setCalculatedPayout( managersAward.setScale( 0, promotion.getRoundingMethod().getBigDecimalRoundingMode() ) );
        }
      }
      if ( goalCalculationResult != null && goalCalculationResult.getCalculatedPayout().compareTo( new BigDecimal( "0" ) ) > 0 )
      {
        goalCalculationResult.setReciever( parentNodeOwner );
        goalCalculationResult.setNodeRole( HierarchyRoleType.lookup( HierarchyRoleType.OWNER ) );
        goalCalculationResult.setOwner( true );
        managerOverrides.add( goalCalculationResult );
      }
    }
    return managerOverrides;
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

  protected static SAO getService( String beanName )
  {
    return (SAO)BeanLocator.getBean( beanName );
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

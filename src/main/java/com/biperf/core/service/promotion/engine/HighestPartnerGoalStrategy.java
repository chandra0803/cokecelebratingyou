/*
 * (c) 2007 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/service/promotion/engine/HighestPartnerGoalStrategy.java,v $
 */

package com.biperf.core.service.promotion.engine;

import java.util.Iterator;
import java.util.List;

/**
 * HighestPartnerGoalStrategy.
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
 * <td>gadapa</td>
 * <td>Apr 1, 2008</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 */
public class HighestPartnerGoalStrategy extends AbstractPartnerGoalStrategy
{

  protected GoalCalculationResult processGoalInternal( GoalCalculationResult paxGoalResult, List partnerPaxGoalResults )
  {
    GoalCalculationResult goalCalculationResult = new GoalCalculationResult();

    // Process only if PAX for the partner has a Goal Result, and has achieved the goal
    if ( null != paxGoalResult && paxGoalResult.isAchieved() )
    {
      // Find the highest payout participant
      GoalCalculationResult highestPaxGoalResult = getHighestPaxGoalResult( partnerPaxGoalResults );
      if ( null != highestPaxGoalResult )
      {
        // If the highest payout participant is the same as the participant who picked the partner
        if ( highestPaxGoalResult.getReciever().getId().longValue() == paxGoalResult.getReciever().getId().longValue() )
        {
          // Partner achieved a payout
          goalCalculationResult.setAchieved( true );
        }
        else
        {
          // Partner did not achieve a payout, inspite of participant achieving the goal
          // This will be used to send a diffent kind of email to the partner
          goalCalculationResult.setPartnersParticipantAchieved( true );
        }
      }
    }

    return goalCalculationResult;
  }

  protected GoalCalculationResult getHighestPaxGoalResult( List partnerPaxGoalResults )
  {
    GoalCalculationResult highestPaxGoalResult = null;
    if ( null != partnerPaxGoalResults )
    {
      for ( Iterator iter = partnerPaxGoalResults.iterator(); iter.hasNext(); )
      {
        GoalCalculationResult paxGoalResult = (GoalCalculationResult)iter.next();
        if ( paxGoalResult.isAchieved() )
        {
          highestPaxGoalResult = getHighestPaxGoalResult( highestPaxGoalResult, paxGoalResult );
        }
      }
    }
    return highestPaxGoalResult;
  }

  protected GoalCalculationResult getHighestPaxGoalResult( GoalCalculationResult goalResult1, GoalCalculationResult goalResult2 )
  {
    GoalCalculationResult highestPaxGoalResult = null;

    // If goalResult1 is null, set goalResult2 a highest
    if ( null == goalResult1 )
    {
      highestPaxGoalResult = goalResult2;
    }
    // If goalResult2 is null, set goalResult1 a highest
    else if ( null == goalResult2 )
    {
      highestPaxGoalResult = goalResult1;
    }
    // If either one are not NULL
    else
    {
      // Compare the calculated payout to find the highest
      if ( goalResult1.getCalculatedPayout().doubleValue() > goalResult2.getCalculatedPayout().doubleValue() )
      {
        highestPaxGoalResult = goalResult1;
      }
      else
      {
        highestPaxGoalResult = goalResult2;
      }
    }

    return highestPaxGoalResult;
  }
}

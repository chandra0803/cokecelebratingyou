/*
 * (c) 2007 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/promotion/engine/PartnerGoalStrategy.java,v $
 */

package com.biperf.core.service.promotion.engine;

import java.util.List;
import java.util.Map;

import com.biperf.core.domain.goalquest.GoalQuestAwardSummary;
import com.biperf.core.domain.participant.ParticipantPartner;

/**
 * PartnerGoalStrategy.
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
 * 
 *
 */
public interface PartnerGoalStrategy
{

  public GoalCalculationResult processGoal( ParticipantPartner paxPartner, Map goalResultsByPax, Map paxGoalResultsByPartner, Map partnerGoalLevelsBySequence );

  public List<GoalQuestAwardSummary> summarizeResults( Map<Long, GoalQuestAwardSummary> goalQuestAwardSummaryPartnerMap );

}

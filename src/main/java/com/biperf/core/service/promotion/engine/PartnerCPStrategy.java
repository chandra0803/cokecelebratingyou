/*
 * (c) 2007 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/promotion/engine/PartnerCPStrategy.java,v $
 */

package com.biperf.core.service.promotion.engine;

import java.util.List;
import java.util.Map;

import com.biperf.core.domain.challengepoint.ChallengepointAwardSummary;
import com.biperf.core.domain.participant.ParticipantPartner;

public interface PartnerCPStrategy
{

  public ChallengePointCalculationResult processGoal( ParticipantPartner paxPartner, Map cpResultsByPax, Map paxGoalResultsByPartner, Map partnerCPLevelsBySequence );

  public List<ChallengepointAwardSummary> summarizeResults( Map<Long, ChallengepointAwardSummary> cpAwardSummaryPartnerMap );

}

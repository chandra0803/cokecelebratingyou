/*
 * (c) 2007 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/promotion/engine/PerAchieverPartnerCPStrategy.java,v $
 */

package com.biperf.core.service.promotion.engine;

import java.util.List;

public class PerAchieverPartnerCPStrategy extends AbstractPartnerCPStrategy
{
  protected ChallengePointCalculationResult processCPInternal( ChallengePointCalculationResult paxCPResult, List partnerPaxCPResults )
  {
    ChallengePointCalculationResult cpCalculationResult = new ChallengePointCalculationResult();

    // Process only if PAX for the partner has a CP Result, and has achieved the CP
    if ( null != paxCPResult && paxCPResult.isAchieved() )
    {
      // Partner achieved a payout
      cpCalculationResult.setAchieved( true );
    }

    return cpCalculationResult;
  }
}

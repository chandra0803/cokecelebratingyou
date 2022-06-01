/*
 * (c) 2007 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/promotion/engine/HighestPartnerCPStrategy.java,v $
 */

package com.biperf.core.service.promotion.engine;

import java.util.Iterator;
import java.util.List;

public class HighestPartnerCPStrategy extends AbstractPartnerCPStrategy
{

  protected ChallengePointCalculationResult processCPInternal( ChallengePointCalculationResult paxCPResult, List partnerPaxCPResults )
  {
    ChallengePointCalculationResult cpCalculationResult = new ChallengePointCalculationResult();

    // Process only if PAX for the partner has a CP Result, and has achieved the CP
    if ( null != paxCPResult && paxCPResult.isAchieved() )
    {
      // Find the highest payout participant
      ChallengePointCalculationResult highestPaxCPResult = getHighestPaxCPResult( partnerPaxCPResults );
      if ( null != highestPaxCPResult )
      {
        // If the highest payout participant is the same as the participant who picked the partner
        if ( highestPaxCPResult.getReciever().getId().longValue() == paxCPResult.getReciever().getId().longValue() )
        {
          // Partner achieved a payout
          cpCalculationResult.setAchieved( true );
        }
        else
        {
          // Partner did not achieve a payout, inspite of participant achieving the CP
          // This will be used to send a diffent kind of email to the partner
          cpCalculationResult.setPartnersParticipantAchieved( true );
        }
      }
    }

    return cpCalculationResult;
  }

  protected ChallengePointCalculationResult getHighestPaxCPResult( List partnerPaxCPResults )
  {
    ChallengePointCalculationResult highestPaxCPResult = null;
    if ( null != partnerPaxCPResults )
    {
      for ( Iterator iter = partnerPaxCPResults.iterator(); iter.hasNext(); )
      {
        ChallengePointCalculationResult paxCPResult = (ChallengePointCalculationResult)iter.next();
        if ( paxCPResult.isAchieved() )
        {
          highestPaxCPResult = getHighestPaxCPResult( highestPaxCPResult, paxCPResult );
        }
      }
    }
    return highestPaxCPResult;
  }

  protected ChallengePointCalculationResult getHighestPaxCPResult( ChallengePointCalculationResult CPResult1, ChallengePointCalculationResult CPResult2 )
  {
    ChallengePointCalculationResult highestPaxCPResult = null;

    // If CPResult1 is null, set CPResult2 a highest
    if ( null == CPResult1 )
    {
      highestPaxCPResult = CPResult2;
    }
    // If CPResult2 is null, set CPResult1 a highest
    else if ( null == CPResult2 )
    {
      highestPaxCPResult = CPResult1;
    }
    // If either one are not NULL
    else
    {
      // Compare the calculated payout to find the highest
      if ( CPResult1.getCalculatedAchievement().doubleValue() > CPResult2.getCalculatedAchievement().doubleValue() )
      {
        highestPaxCPResult = CPResult1;
      }
      else
      {
        highestPaxCPResult = CPResult2;
      }
    }

    return highestPaxCPResult;
  }

}

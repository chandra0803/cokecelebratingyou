/*
 * (c) 2013 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.service.throwdown;

import java.util.HashMap;
import java.util.Map;

import com.biperf.core.domain.enums.MatchTeamOutcomeType;
import com.biperf.core.domain.promotion.MatchTeamOutcome;

/**
 * 
 * @author kothanda
 * @since Nov 4, 2013
 * @version 1.0
 */
public class MatchResolution
{
  private Map<Long, MatchTeamOutcomeType> outcomeResolution = new HashMap<Long, MatchTeamOutcomeType>();

  public MatchResolution( MatchTeamOutcome outcome1, MatchTeamOutcomeType outcomeType1, MatchTeamOutcome outcome2, MatchTeamOutcomeType outcomeType2 )
  {
    outcomeResolution.put( outcome1.getId(), outcomeType1 );
    outcomeResolution.put( outcome2.getId(), outcomeType2 );
  }

  public MatchTeamOutcomeType getOutcomeResults( Long matchTeamOutcomeId )
  {
    return outcomeResolution.get( matchTeamOutcomeId );
  }
}

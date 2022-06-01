/*
 * (c) 2013 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/throwdown/StackRankingCalculationResult.java,v $
 */

package com.biperf.core.service.throwdown;

import java.io.Serializable;
import java.util.List;

/**
 * 
 * @author kothanda
 * @since Sep 18, 2013
 * @version 1.0
 */
public class StackRankingCalculationResult implements Serializable
{
  private static final long serialVersionUID = 1L;

  private StackRankingResult roundStackRanking = new StackRankingResult();

  public StackRankingResult getRoundStackRanking()
  {
    return roundStackRanking;
  }

  public void setRoundStackRanking( StackRankingResult roundStackRanking )
  {
    this.roundStackRanking = roundStackRanking;
  }

  public int getTotalPointsIssued()
  {
    int points = 0;
    List<NodeRankingResult> nodeRankings = getRoundStackRanking().getNodeRankings();
    for ( NodeRankingResult nodeRanking : nodeRankings )
    {
      for ( TeamRankingResult teamResult : nodeRanking.getTeamResults() )
      {
        points = points + teamResult.getPayoutAmount();
      }
    }
    return points;
  }

}

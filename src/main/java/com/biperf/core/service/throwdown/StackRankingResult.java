/*
 * (c) 2013 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/throwdown/StackRankingResult.java,v $
 */

package com.biperf.core.service.throwdown;

import java.util.ArrayList;
import java.util.List;

import com.biperf.core.domain.promotion.ThrowdownPromotion;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 
 * @author kothanda
 * @since Sep 18, 2013
 * @version 1.0
 */
public class StackRankingResult
{
  private static final long serialVersionUID = 1L;

  private ThrowdownPromotion promotion = null;
  private Integer roundNumber;
  private List<NodeRankingResult> nodeRankings = new ArrayList<NodeRankingResult>();
  private boolean anyPayoutFailed = false;

  public Integer getRoundNumber()
  {
    return roundNumber;
  }

  public void setRoundNumber( Integer roundNumber )
  {
    this.roundNumber = roundNumber;
  }

  public List<NodeRankingResult> getNodeRankings()
  {
    return nodeRankings;
  }

  public void setNodeRankings( List<NodeRankingResult> nodeRankings )
  {
    this.nodeRankings = nodeRankings;
  }

  public void addNodeRanking( NodeRankingResult result )
  {
    this.nodeRankings.add( result );
  }

  @JsonIgnore
  public ThrowdownPromotion getPromotion()
  {
    return promotion;
  }

  @JsonIgnore
  public void setPromotion( ThrowdownPromotion promotion )
  {
    this.promotion = promotion;
  }

  @JsonIgnore
  public boolean isLastRound()
  {
    return promotion.getNumberOfRounds() == roundNumber.intValue();
  }

  public boolean isAnyPayoutFailed()
  {
    return anyPayoutFailed;
  }

  public void setAnyPayoutFailed( boolean anyPayoutFailed )
  {
    this.anyPayoutFailed = anyPayoutFailed;
  }

}

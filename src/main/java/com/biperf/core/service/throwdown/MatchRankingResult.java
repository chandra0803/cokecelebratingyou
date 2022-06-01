
package com.biperf.core.service.throwdown;

import java.io.Serializable;
import java.math.BigDecimal;

import com.biperf.core.domain.promotion.MatchTeamOutcome;

public class MatchRankingResult implements Serializable
{
  private static final long serialVersionUID = 1L;

  private MatchTeamOutcome outcome = null;
  private BigDecimal totalProgress = null;

  public BigDecimal getTotalProgress()
  {
    return totalProgress;
  }

  public void setTotalProgress( BigDecimal totalProgress )
  {
    this.totalProgress = totalProgress;
  }

  public MatchTeamOutcome getOutcome()
  {
    return outcome;
  }

  public void setOutcome( MatchTeamOutcome outcome )
  {
    this.outcome = outcome;
  }

}


package com.biperf.core.service.throwdown;

import java.io.Serializable;
import java.math.BigDecimal;

import com.biperf.core.domain.enums.MatchTeamOutcomeType;
import com.biperf.core.domain.promotion.Team;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class TeamCalculationResult implements Serializable
{
  private static final long serialVersionUID = 1L;

  private Team team = null;
  private MatchTeamOutcomeType outcomePayoutType = null;
  private BigDecimal totalProgress = null;
  private BigDecimal payoutAmount = null;

  public BigDecimal getTotalProgress()
  {
    return totalProgress;
  }

  public void setTotalProgress( BigDecimal totalProgress )
  {
    this.totalProgress = totalProgress;
  }

  @JsonIgnore
  public Team getTeam()
  {
    return team;
  }

  @JsonIgnore
  public void setTeam( Team team )
  {
    this.team = team;
  }

  public MatchTeamOutcomeType getOutcomePayoutType()
  {
    return outcomePayoutType;
  }

  public void setOutcomePayoutType( MatchTeamOutcomeType outcomePayoutType )
  {
    this.outcomePayoutType = outcomePayoutType;
  }

  public BigDecimal getPayoutAmount()
  {
    return payoutAmount;
  }

  public void setPayoutAmount( BigDecimal payoutAmount )
  {
    this.payoutAmount = payoutAmount;
  }
}

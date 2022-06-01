
package com.biperf.core.value.challengepoint;

import java.math.BigDecimal;

public class ChallengePointSummaryReportValue
{

  // TABULAR RESULTS

  private String goals;
  private Long achieved;
  private BigDecimal percentAchieved;
  private Long totalBaseLineObjective;
  private BigDecimal totalChallengeProduction;
  private Long totalActualProduction;
  private BigDecimal percIncreaseBaseline;
  private Long dolIncBaseline;
  private BigDecimal percentIncreseChallengepoint;
  private BigDecimal unitDolIncCP;
  private Long totalAchieved;
  private Long totalNotAchieved;

  public String getGoals()
  {
    return goals;
  }

  public void setGoals( String goals )
  {
    this.goals = goals;
  }

  public Long getAchieved()
  {
    return achieved;
  }

  public void setAchieved( Long achieved )
  {
    this.achieved = achieved;
  }

  public Long getTotalBaseLineObjective()
  {
    return totalBaseLineObjective;
  }

  public void setTotalBaseLineObjective( Long totalBaseLineObjective )
  {
    this.totalBaseLineObjective = totalBaseLineObjective;
  }

  public BigDecimal getTotalChallengeProduction()
  {
    return totalChallengeProduction;
  }

  public void setTotalChallengeProduction( BigDecimal totalChallengeProduction )
  {
    this.totalChallengeProduction = totalChallengeProduction;
  }

  public Long getTotalActualProduction()
  {
    return totalActualProduction;
  }

  public void setTotalActualProduction( Long totalActualProduction )
  {
    this.totalActualProduction = totalActualProduction;
  }

  public Long getDolIncBaseline()
  {
    return dolIncBaseline;
  }

  public void setDolIncBaseline( Long dolIncBaseline )
  {
    this.dolIncBaseline = dolIncBaseline;
  }

  public BigDecimal getUnitDolIncCP()
  {
    return unitDolIncCP;
  }

  public void setUnitDolIncCP( BigDecimal unitDolIncCP )
  {
    this.unitDolIncCP = unitDolIncCP;
  }

  public Long getTotalAchieved()
  {
    return totalAchieved;
  }

  public void setTotalAchieved( Long totalAchieved )
  {
    this.totalAchieved = totalAchieved;
  }

  public Long getTotalNotAchieved()
  {
    return totalNotAchieved;
  }

  public void setTotalNotAchieved( Long totalNotAchieved )
  {
    this.totalNotAchieved = totalNotAchieved;
  }

  public BigDecimal getPercentAchieved()
  {
    return percentAchieved;
  }

  public void setPercentAchieved( BigDecimal percentAchieved )
  {
    this.percentAchieved = percentAchieved;
  }

  public BigDecimal getPercIncreaseBaseline()
  {
    return percIncreaseBaseline;
  }

  public void setPercIncreaseBaseline( BigDecimal percIncreaseBaseline )
  {
    this.percIncreaseBaseline = percIncreaseBaseline;
  }

  public BigDecimal getPercentIncreseChallengepoint()
  {
    return percentIncreseChallengepoint;
  }

  public void setPercentIncreseChallengepoint( BigDecimal percentIncreseChallengepoint )
  {
    this.percentIncreseChallengepoint = percentIncreseChallengepoint;
  }

}

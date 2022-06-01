
package com.biperf.core.value.goalquest;

import java.math.BigDecimal;

public class GoalQuestSummaryReportValue
{

  // TABULAR RESULTS

  private String goals;

  // Number of Participants
  private Long achieved;
  //// Percent of Participants
  private BigDecimal percentAchieved;
  // Total Baseline Objective
  private Long totalBaseLineObjective;
  // Total Goal Value
  private Long totalGoalValue;
  // Total Actual Production
  private Long totalActualProduction;
  // Percent Increase over baseline
  private Long percIncreaseBaseline;
  //// Unit / Dollar Increase over baseline
  private Long unitDollerIncrBaseline;
  // % increase over goal
  private Long percentIncreseGoal;
  // Unit/Dollar increase over goal
  private Long unitDollerIncrGoal;

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

  public BigDecimal getPercentAchieved()
  {
    return percentAchieved;
  }

  public void setPercentAchieved( BigDecimal percentAchieved )
  {
    this.percentAchieved = percentAchieved;
  }

  public Long getTotalBaseLineObjective()
  {
    return totalBaseLineObjective;
  }

  public void setTotalBaseLineObjective( Long totalBaseLineObjective )
  {
    this.totalBaseLineObjective = totalBaseLineObjective;
  }

  public Long getTotalGoalValue()
  {
    return totalGoalValue;
  }

  public void setTotalGoalValue( Long totalGoalValue )
  {
    this.totalGoalValue = totalGoalValue;
  }

  public Long getTotalActualProduction()
  {
    return totalActualProduction;
  }

  public void setTotalActualProduction( Long totalActualProduction )
  {
    this.totalActualProduction = totalActualProduction;
  }

  public Long getPercIncreaseBaseline()
  {
    return percIncreaseBaseline;
  }

  public void setPercIncreaseBaseline( Long percIncreaseBaseline )
  {
    this.percIncreaseBaseline = percIncreaseBaseline;
  }

  public Long getUnitDollerIncrBaseline()
  {
    return unitDollerIncrBaseline;
  }

  public void setUnitDollerIncrBaseline( Long unitDollerIncrBaseline )
  {
    this.unitDollerIncrBaseline = unitDollerIncrBaseline;
  }

  public Long getPercentIncreseGoal()
  {
    return percentIncreseGoal;
  }

  public void setPercentIncreseGoal( Long percentIncreseGoal )
  {
    this.percentIncreseGoal = percentIncreseGoal;
  }

  public Long getUnitDollerIncrGoal()
  {
    return unitDollerIncrGoal;
  }

  public void setUnitDollerIncrGoal( Long unitDollerIncrGoal )
  {
    this.unitDollerIncrGoal = unitDollerIncrGoal;
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

}

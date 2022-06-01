
package com.biperf.core.domain.goalquest;

import java.math.BigDecimal;

import com.biperf.core.domain.BaseDomain;
import com.biperf.core.domain.enums.GoalROICountType;
import com.biperf.core.domain.promotion.GoalQuestPromotion;

/**
 * A value bean used by RptGoalROI named queries to contain fetched ROI counts for
 * a given participant for a promotion. 
 * 
 * RptGoalROI.
 * 
 * <p>
 * <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>Tammy Cheng</td>
 * <td>Mar 15, 2007</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class RptGoalROI extends BaseDomain
{
  /**
   * The promotion from which the Return On Investment performance numbers are for
   */
  private GoalQuestPromotion goalQuestPromotion;

  /**
   * The type that constitutes a category by which the return of investment count is based on.
   * e.g:
   * Goals Achieved, 
   * Goals Not Achieved Sales Over Baseline,
   * Goals Not Achieved Sales Under Baseline,
   * Did Not Select Goal
   */
  private GoalROICountType goalROICountType;

  /**
   * Total number of participants for each goal category.
   */
  private Integer nbrOfParticipants;

  /**
   * nbrOfParticipants / total number of participants in promotion audience.
   */
  private Double pctOfParticipants;

  /**
   * Total amount of base objective for all participants in that category.
   */
  private BigDecimal totBaselineObjective;

  /**
   * Total progress for all participants in that category. 
   * Aka Total Current Value or Total Actual Production.
   */
  private BigDecimal totActualProduction;

  /**
   * unitDollarIncrease / totActualProduction.
   */
  private Double pctIncrease;

  /**
   * totActualProduction - totBaselineObjective.
   */
  private Double unitDollarIncrease;

  public boolean equals( Object object )
  {
    boolean equals = true;

    if ( ! ( object instanceof RptGoalROI ) )
    {
      return false;
    }

    RptGoalROI otherRptGoalROI = (RptGoalROI)object;

    if ( otherRptGoalROI.getGoalQuestPromotion() != null && !otherRptGoalROI.getGoalQuestPromotion().equals( this.getGoalQuestPromotion() ) )
    {
      equals = false;
    }

    if ( otherRptGoalROI.getGoalROICountType() != null && !otherRptGoalROI.getGoalROICountType().equals( this.getGoalROICountType() ) )
    {
      equals = false;
    }

    return equals;
  }

  public int hashCode()
  {
    int result;
    result = this.getGoalQuestPromotion() != null ? this.getGoalQuestPromotion().hashCode() : 0;
    result = 29 * result + ( this.getGoalROICountType() != null ? this.getGoalROICountType().hashCode() : 0 );

    return result;
  }

  public GoalQuestPromotion getGoalQuestPromotion()
  {
    return goalQuestPromotion;
  }

  public void setGoalQuestPromotion( GoalQuestPromotion goalQuestPromotion )
  {
    this.goalQuestPromotion = goalQuestPromotion;
  }

  public GoalROICountType getGoalROICountType()
  {
    return goalROICountType;
  }

  public void setGoalROICountType( GoalROICountType goalROICountType )
  {
    this.goalROICountType = goalROICountType;
  }

  public Integer getNbrOfParticipants()
  {
    return nbrOfParticipants;
  }

  public void setNbrOfParticipants( Integer nbrOfParticipants )
  {
    this.nbrOfParticipants = nbrOfParticipants;
  }

  public Double getPctIncrease()
  {
    return pctIncrease;
  }

  public void setPctIncrease( Double pctIncrease )
  {
    this.pctIncrease = pctIncrease;
  }

  public Double getPctOfParticipants()
  {
    return pctOfParticipants;
  }

  public void setPctOfParticipants( Double pctOfParticipants )
  {
    this.pctOfParticipants = pctOfParticipants;
  }

  public BigDecimal getTotActualProduction()
  {
    return totActualProduction;
  }

  public void setTotActualProduction( BigDecimal totActualProduction )
  {
    this.totActualProduction = totActualProduction;
  }

  public BigDecimal getTotBaselineObjective()
  {
    return totBaselineObjective;
  }

  public void setTotBaselineObjective( BigDecimal totBaselineObjective )
  {
    this.totBaselineObjective = totBaselineObjective;
  }

  public Double getUnitDollarIncrease()
  {
    return unitDollarIncrease;
  }

  public void setUnitDollarIncrease( Double unitDollarIncrease )
  {
    this.unitDollarIncrease = unitDollarIncrease;
  }

}

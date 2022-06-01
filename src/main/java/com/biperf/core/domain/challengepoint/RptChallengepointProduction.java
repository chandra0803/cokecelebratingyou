
package com.biperf.core.domain.challengepoint;

import java.math.BigDecimal;

import com.biperf.core.domain.BaseDomain;
import com.biperf.core.domain.enums.ChallengepointProductionCountType;
import com.biperf.core.domain.promotion.ChallengePointPromotion;

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
public class RptChallengepointProduction extends BaseDomain
{
  /**
   * The promotion from which the Return On Investment performance numbers are for
   */
  private ChallengePointPromotion challengepointPromotion;

  /**
   * The type that constitutes a category by which the return of investment count is based on.
   * e.g:
   * Goals Achieved, 
   * Goals Not Achieved Sales Over Baseline,
   * Goals Not Achieved Sales Under Baseline,
   * Did Not Select Goal
   */
  private ChallengepointProductionCountType challengepointProductionCountType;

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

    if ( ! ( object instanceof RptChallengepointProduction ) )
    {
      return false;
    }

    RptChallengepointProduction otherRptGoalROI = (RptChallengepointProduction)object;

    if ( otherRptGoalROI.getChallengepointPromotion() != null && !otherRptGoalROI.getChallengepointPromotion().equals( this.getChallengepointPromotion() ) )
    {
      equals = false;
    }

    if ( otherRptGoalROI.getChallengepointProductionCountType() != null && !otherRptGoalROI.getChallengepointProductionCountType().equals( this.getChallengepointProductionCountType() ) )
    {
      equals = false;
    }

    return equals;
  }

  public int hashCode()
  {
    int result;
    result = this.getChallengepointPromotion() != null ? this.getChallengepointPromotion().hashCode() : 0;
    result = 29 * result + ( this.getChallengepointProductionCountType() != null ? this.getChallengepointProductionCountType().hashCode() : 0 );

    return result;
  }

  public ChallengePointPromotion getChallengepointPromotion()
  {
    return challengepointPromotion;
  }

  public void setChallengepointPromotion( ChallengePointPromotion challengepointPromotion )
  {
    this.challengepointPromotion = challengepointPromotion;
  }

  public ChallengepointProductionCountType getChallengepointProductionCountType()
  {
    return challengepointProductionCountType;
  }

  public void setChallengepointProductionCountType( ChallengepointProductionCountType challengepointProductionCountType )
  {
    this.challengepointProductionCountType = challengepointProductionCountType;
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

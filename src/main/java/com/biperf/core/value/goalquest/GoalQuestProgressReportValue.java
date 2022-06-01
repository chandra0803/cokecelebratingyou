/*
 * (c) 2009 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/value/goalquest/GoalQuestProgressReportValue.java,v $
 */

package com.biperf.core.value.goalquest;

import java.math.BigDecimal;

/**
 * GoalQuestProgressReportValue.
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
 * <td>drahn</td>
 * <td>Sep 6, 2012</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 */
public class GoalQuestProgressReportValue
{
  // TABULAR RESULTS
  private Long totalPax;
  private Long totalPaxNoGoal;
  private Long totalPaxGoalSelected;
  private Long level0To25Count;
  private Long level26To50Count;
  private Long level51To75Count;
  private Long level76To99Count;
  private Long level100Count;
  private String orgName;
  private Long orgId;
  private Long parentNodeId;
  private Boolean isLeaf;
  private Long goal;

  // DETAIL RESULTS
  private String paxName;
  private String promoName;
  private String goalName;
  private Long baseQuantity;
  private Long amountToAchieve;
  private BigDecimal goalAchieve;
  private BigDecimal currentValue;
  private BigDecimal percentAchieved;
  private Boolean achieved;

  public Long getTotalPax()
  {
    return totalPax;
  }

  public void setTotalPax( Long totalPax )
  {
    this.totalPax = totalPax;
  }

  public Long getTotalPaxNoGoal()
  {
    return totalPaxNoGoal;
  }

  public void setTotalPaxNoGoal( Long totalPaxNoGoal )
  {
    this.totalPaxNoGoal = totalPaxNoGoal;
  }

  public Long getTotalPaxGoalSelected()
  {
    return totalPaxGoalSelected;
  }

  public void setTotalPaxGoalSelected( Long totalPaxGoalSelected )
  {
    this.totalPaxGoalSelected = totalPaxGoalSelected;
  }

  public Long getLevel0To25Count()
  {
    return level0To25Count;
  }

  public void setLevel0To25Count( Long level0To25Count )
  {
    this.level0To25Count = level0To25Count;
  }

  public Long getLevel26To50Count()
  {
    return level26To50Count;
  }

  public void setLevel26To50Count( Long level26To50Count )
  {
    this.level26To50Count = level26To50Count;
  }

  public Long getLevel51To75Count()
  {
    return level51To75Count;
  }

  public void setLevel51To75Count( Long level51To75Count )
  {
    this.level51To75Count = level51To75Count;
  }

  public Long getLevel76To99Count()
  {
    return level76To99Count;
  }

  public void setLevel76To99Count( Long level76To99Count )
  {
    this.level76To99Count = level76To99Count;
  }

  public Long getLevel100Count()
  {
    return level100Count;
  }

  public void setLevel100Count( Long level100Count )
  {
    this.level100Count = level100Count;
  }

  public String getOrgName()
  {
    return orgName;
  }

  public void setOrgName( String orgName )
  {
    this.orgName = orgName;
  }

  public void setOrgId( Long orgId )
  {
    this.orgId = orgId;
  }

  public Long getOrgId()
  {
    return orgId;
  }

  public Long getParentNodeId()
  {
    return parentNodeId;
  }

  public void setParentNodeId( Long parentNodeId )
  {
    this.parentNodeId = parentNodeId;
  }

  public void setIsLeaf( Boolean isLeaf )
  {
    this.isLeaf = isLeaf;
  }

  public Boolean getIsLeaf()
  {
    return isLeaf;
  }

  public String getPaxName()
  {
    return paxName;
  }

  public void setPaxName( String paxName )
  {
    this.paxName = paxName;
  }

  public String getPromoName()
  {
    return promoName;
  }

  public void setPromoName( String promoName )
  {
    this.promoName = promoName;
  }

  public String getGoalName()
  {
    return goalName;
  }

  public void setGoalName( String goalName )
  {
    this.goalName = goalName;
  }

  public Long getBaseQuantity()
  {
    return baseQuantity;
  }

  public void setBaseQuantity( Long baseQuantity )
  {
    this.baseQuantity = baseQuantity;
  }

  public Long getAmountToAchieve()
  {
    return amountToAchieve;
  }

  public void setAmountToAchieve( Long amountToAchieve )
  {
    this.amountToAchieve = amountToAchieve;
  }

  public BigDecimal getCurrentValue()
  {
    return currentValue;
  }

  public void setCurrentValue( BigDecimal currentValue )
  {
    this.currentValue = currentValue;
  }

  public BigDecimal getPercentAchieved()
  {
    return percentAchieved;
  }

  public void setPercentAchieved( BigDecimal percentAchieved )
  {
    this.percentAchieved = percentAchieved;
  }

  public Boolean getAchieved()
  {
    return achieved;
  }

  public void setAchieved( Boolean achieved )
  {
    this.achieved = achieved;
  }

  public Long getGoal()
  {
    return goal;
  }

  public void setGoal( Long goal )
  {
    this.goal = goal;
  }

  public BigDecimal getGoalAchieve()
  {
    return goalAchieve;
  }

  public void setGoalAchieve( BigDecimal goalAchieve )
  {
    this.goalAchieve = goalAchieve;
  }
}

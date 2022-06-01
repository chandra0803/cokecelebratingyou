
package com.biperf.core.value.challengepoint;

import java.math.BigDecimal;

public class ChallengePointProgressReportValue
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

  // DETAIL RESULTS
  private String paxName;
  private String promoName;
  private String goalName;
  private BigDecimal baseQuantity;
  private BigDecimal amountToAchieve;
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

  public BigDecimal getBaseQuantity()
  {
    return baseQuantity;
  }

  public void setBaseQuantity( BigDecimal baseQuantity )
  {
    this.baseQuantity = baseQuantity;
  }

  public BigDecimal getAmountToAchieve()
  {
    return amountToAchieve;
  }

  public void setAmountToAchieve( BigDecimal amountToAchieve )
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
}

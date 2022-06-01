/*
 * (c) 2009 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/value/goalquest/GoalQuestManagerReportValue.java,v $
 */

package com.biperf.core.value.goalquest;

import java.math.BigDecimal;

public class GoalQuestManagerReportValue
{
  private String orgName;
  private Long totalPax;
  private Long totalPaxGoalSelected;
  private Long totalPaxAchieved;
  private BigDecimal percentOfSelectedPaxAchieving;
  private BigDecimal percentOfTotalPaxAchieving;
  private Long totalPoints;
  private Long totalManagerPoints;
  private Long orgId;
  private Boolean isLeaf;
  private String managerName;
  private Long managerUserId;
  private BigDecimal percentOfManagerOveride;
  private BigDecimal managerPayOutPerAchiever;
  private String promotionName;

  public String getOrgName()
  {
    return orgName;
  }

  public void setOrgName( String orgName )
  {
    this.orgName = orgName;
  }

  public Long getTotalPax()
  {
    return totalPax;
  }

  public void setTotalPax( Long totalPax )
  {
    this.totalPax = totalPax;
  }

  public Long getTotalPaxGoalSelected()
  {
    return totalPaxGoalSelected;
  }

  public void setTotalPaxGoalSelected( Long totalPaxGoalSelected )
  {
    this.totalPaxGoalSelected = totalPaxGoalSelected;
  }

  public Long getTotalPaxAchieved()
  {
    return totalPaxAchieved;
  }

  public void setTotalPaxAchieved( Long totalPaxAchieved )
  {
    this.totalPaxAchieved = totalPaxAchieved;
  }

  public BigDecimal getPercentOfSelectedPaxAchieving()
  {
    return percentOfSelectedPaxAchieving;
  }

  public void setPercentOfSelectedPaxAchieving( BigDecimal percentOfSelectedPaxAchieving )
  {
    this.percentOfSelectedPaxAchieving = percentOfSelectedPaxAchieving;
  }

  public BigDecimal getPercentOfTotalPaxAchieving()
  {
    return percentOfTotalPaxAchieving;
  }

  public void setPercentOfTotalPaxAchieving( BigDecimal percentOfTotalPaxAchieving )
  {
    this.percentOfTotalPaxAchieving = percentOfTotalPaxAchieving;
  }

  public Long getTotalPoints()
  {
    return totalPoints;
  }

  public void setTotalPoints( Long totalPoints )
  {
    this.totalPoints = totalPoints;
  }

  public Long getTotalManagerPoints()
  {
    return totalManagerPoints;
  }

  public void setTotalManagerPoints( Long totalManagerPoints )
  {
    this.totalManagerPoints = totalManagerPoints;
  }

  public Long getOrgId()
  {
    return orgId;
  }

  public void setOrgId( Long orgId )
  {
    this.orgId = orgId;
  }

  public Boolean getIsLeaf()
  {
    return isLeaf;
  }

  public void setIsLeaf( Boolean isLeaf )
  {
    this.isLeaf = isLeaf;
  }

  public Long getManagerUserId()
  {
    return managerUserId;
  }

  public void setManagerUserId( Long managerUserId )
  {
    this.managerUserId = managerUserId;
  }

  public String getManagerName()
  {
    return managerName;
  }

  public void setManagerName( String managerName )
  {
    this.managerName = managerName;
  }

  public BigDecimal getPercentOfManagerOveride()
  {
    return percentOfManagerOveride;
  }

  public void setPercentOfManagerOveride( BigDecimal percentOfManagerOveride )
  {
    this.percentOfManagerOveride = percentOfManagerOveride;
  }

  public BigDecimal getManagerPayOutPerAchiever()
  {
    return managerPayOutPerAchiever;
  }

  public void setManagerPayOutPerAchiever( BigDecimal managerPayOutPerAchiever )
  {
    this.managerPayOutPerAchiever = managerPayOutPerAchiever;
  }

  public String getPromotionName()
  {
    return promotionName;
  }

  public void setPromotionName( String promotionName )
  {
    this.promotionName = promotionName;
  }

}

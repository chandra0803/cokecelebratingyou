/*
 * (c) 2009 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/value/goalquest/GoalQuestAchievementReportValue.java,v $
 */

package com.biperf.core.value.goalquest;

import java.math.BigDecimal;

/**
 * GoalQuestAchievementReportValue.
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
 * <td>Sep 17, 2012</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 */
public class GoalQuestAchievementReportValue
{
  // TABULAR RESULTS
  private String orgName;
  private Long orgId;
  private Long totalPaxCnt;
  private Long level1SelectedCnt;
  private Long level1AchievedCnt;
  private BigDecimal level1AchievedPercent;
  private Long level1AwardCnt;
  private Long level2SelectedCnt;
  private Long level2AchievedCnt;
  private BigDecimal level2AchievedPercent;
  private Long level2AwardCnt;
  private Long level3SelectedCnt;
  private Long level3AchievedCnt;
  private BigDecimal level3AchievedPercent;
  private Long level3AwardCnt;
  private Long level4SelectedCnt;
  private Long level4AchievedCnt;
  private BigDecimal level4AchievedPercent;
  private Long level4AwardCnt;
  private Long level5SelectedCnt;
  private Long level5AchievedCnt;
  private BigDecimal level5AchievedPercent;
  private Long level5AwardCnt;
  private Long level6SelectedCnt;
  private Long level6AchievedCnt;
  private BigDecimal level6AchievedPercent;
  private Long level6AwardCnt;
  private Long baseQuantity;
  private Long amountToAchieve;
  private Long currentValue;
  private BigDecimal percentAchieved;
  private Boolean isLeaf;

  // DETAIL RESULTS
  private String paxName;
  private String promoName;
  private String levelNumber;
  private Boolean isAchieved;
  private Long points;

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

  public Long getTotalPaxCnt()
  {
    return totalPaxCnt;
  }

  public void setTotalPaxCnt( Long totalPaxCnt )
  {
    this.totalPaxCnt = totalPaxCnt;
  }

  public Long getLevel1SelectedCnt()
  {
    return level1SelectedCnt;
  }

  public void setLevel1SelectedCnt( Long level1SelectedCnt )
  {
    this.level1SelectedCnt = level1SelectedCnt;
  }

  public Long getLevel1AchievedCnt()
  {
    return level1AchievedCnt;
  }

  public void setLevel1AchievedCnt( Long level1AchievedCnt )
  {
    this.level1AchievedCnt = level1AchievedCnt;
  }

  public BigDecimal getLevel1AchievedPercent()
  {
    return level1AchievedPercent;
  }

  public void setLevel1AchievedPercent( BigDecimal level1AchievedPercent )
  {
    this.level1AchievedPercent = level1AchievedPercent;
  }

  public Long getLevel1AwardCnt()
  {
    return level1AwardCnt;
  }

  public void setLevel1AwardCnt( Long level1AwardCnt )
  {
    this.level1AwardCnt = level1AwardCnt;
  }

  public Long getLevel2SelectedCnt()
  {
    return level2SelectedCnt;
  }

  public void setLevel2SelectedCnt( Long level2SelectedCnt )
  {
    this.level2SelectedCnt = level2SelectedCnt;
  }

  public Long getLevel2AchievedCnt()
  {
    return level2AchievedCnt;
  }

  public void setLevel2AchievedCnt( Long level2AchievedCnt )
  {
    this.level2AchievedCnt = level2AchievedCnt;
  }

  public BigDecimal getLevel2AchievedPercent()
  {
    return level2AchievedPercent;
  }

  public void setLevel2AchievedPercent( BigDecimal level2AchievedPercent )
  {
    this.level2AchievedPercent = level2AchievedPercent;
  }

  public Long getLevel2AwardCnt()
  {
    return level2AwardCnt;
  }

  public void setLevel2AwardCnt( Long level2AwardCnt )
  {
    this.level2AwardCnt = level2AwardCnt;
  }

  public Long getLevel3SelectedCnt()
  {
    return level3SelectedCnt;
  }

  public void setLevel3SelectedCnt( Long level3SelectedCnt )
  {
    this.level3SelectedCnt = level3SelectedCnt;
  }

  public Long getLevel3AchievedCnt()
  {
    return level3AchievedCnt;
  }

  public void setLevel3AchievedCnt( Long level3AchievedCnt )
  {
    this.level3AchievedCnt = level3AchievedCnt;
  }

  public BigDecimal getLevel3AchievedPercent()
  {
    return level3AchievedPercent;
  }

  public void setLevel3AchievedPercent( BigDecimal level3AchievedPercent )
  {
    this.level3AchievedPercent = level3AchievedPercent;
  }

  public Long getLevel3AwardCnt()
  {
    return level3AwardCnt;
  }

  public void setLevel3AwardCnt( Long level3AwardCnt )
  {
    this.level3AwardCnt = level3AwardCnt;
  }

  public Long getLevel4SelectedCnt()
  {
    return level4SelectedCnt;
  }

  public void setLevel4SelectedCnt( Long level4SelectedCnt )
  {
    this.level4SelectedCnt = level4SelectedCnt;
  }

  public Long getLevel4AchievedCnt()
  {
    return level4AchievedCnt;
  }

  public void setLevel4AchievedCnt( Long level4AchievedCnt )
  {
    this.level4AchievedCnt = level4AchievedCnt;
  }

  public BigDecimal getLevel4AchievedPercent()
  {
    return level4AchievedPercent;
  }

  public void setLevel4AchievedPercent( BigDecimal level4AchievedPercent )
  {
    this.level4AchievedPercent = level4AchievedPercent;
  }

  public Long getLevel4AwardCnt()
  {
    return level4AwardCnt;
  }

  public void setLevel4AwardCnt( Long level4AwardCnt )
  {
    this.level4AwardCnt = level4AwardCnt;
  }

  public Long getLevel5SelectedCnt()
  {
    return level5SelectedCnt;
  }

  public void setLevel5SelectedCnt( Long level5SelectedCnt )
  {
    this.level5SelectedCnt = level5SelectedCnt;
  }

  public Long getLevel5AchievedCnt()
  {
    return level5AchievedCnt;
  }

  public void setLevel5AchievedCnt( Long level5AchievedCnt )
  {
    this.level5AchievedCnt = level5AchievedCnt;
  }

  public BigDecimal getLevel5AchievedPercent()
  {
    return level5AchievedPercent;
  }

  public void setLevel5AchievedPercent( BigDecimal level5AchievedPercent )
  {
    this.level5AchievedPercent = level5AchievedPercent;
  }

  public Long getLevel5AwardCnt()
  {
    return level5AwardCnt;
  }

  public void setLevel5AwardCnt( Long level5AwardCnt )
  {
    this.level5AwardCnt = level5AwardCnt;
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

  public Long getCurrentValue()
  {
    return currentValue;
  }

  public void setCurrentValue( Long currentValue )
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

  public String getLevelNumber()
  {
    return levelNumber;
  }

  public void setLevelNumber( String levelNumber )
  {
    this.levelNumber = levelNumber;
  }

  public Boolean getIsAchieved()
  {
    return isAchieved;
  }

  public void setIsAchieved( Boolean isAchieved )
  {
    this.isAchieved = isAchieved;
  }

  public Long getPoints()
  {
    return points;
  }

  public void setPoints( Long points )
  {
    this.points = points;
  }

  public Long getLevel6SelectedCnt()
  {
    return level6SelectedCnt;
  }

  public void setLevel6SelectedCnt( Long level6SelectedCnt )
  {
    this.level6SelectedCnt = level6SelectedCnt;
  }

  public Long getLevel6AchievedCnt()
  {
    return level6AchievedCnt;
  }

  public void setLevel6AchievedCnt( Long level6AchievedCnt )
  {
    this.level6AchievedCnt = level6AchievedCnt;
  }

  public BigDecimal getLevel6AchievedPercent()
  {
    return level6AchievedPercent;
  }

  public void setLevel6AchievedPercent( BigDecimal level6AchievedPercent )
  {
    this.level6AchievedPercent = level6AchievedPercent;
  }

  public Long getLevel6AwardCnt()
  {
    return level6AwardCnt;
  }

  public void setLevel6AwardCnt( Long level6AwardCnt )
  {
    this.level6AwardCnt = level6AwardCnt;
  }

}

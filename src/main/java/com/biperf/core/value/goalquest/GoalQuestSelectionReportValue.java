/*
 * (c) 2009 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/value/goalquest/GoalQuestSelectionReportValue.java,v $
 */

package com.biperf.core.value.goalquest;

import java.math.BigDecimal;

/**
 * GoalQuestSelectionReportValue.
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
 * <td>Sep 13, 2012</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 */
public class GoalQuestSelectionReportValue
{
  // TABULAR RESULTS
  private String orgName;
  private Long orgId;
  private Long paxCnt;
  private Long noGoalSelected;
  private BigDecimal noGoalSelectedPercent;
  private Long level1Selected;
  private BigDecimal level1SelectedPercent;
  private Long level2Selected;
  private BigDecimal level2SelectedPercent;
  private Long level3Selected;
  private BigDecimal level3SelectedPercent;
  private Long level4Selected;
  private BigDecimal level4SelectedPercent;
  private Long level5Selected;
  private BigDecimal level5SelectedPercent;
  private Long level6Selected;
  private BigDecimal level6SelectedPercent;
  private Boolean isLeaf;

  // DETAIL RESULTS
  private String paxName;
  private String levelName;
  private String promoName;

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

  public Long getPaxCnt()
  {
    return paxCnt;
  }

  public void setPaxCnt( Long paxCnt )
  {
    this.paxCnt = paxCnt;
  }

  public Long getNoGoalSelected()
  {
    return noGoalSelected;
  }

  public void setNoGoalSelected( Long noGoalSelected )
  {
    this.noGoalSelected = noGoalSelected;
  }

  public BigDecimal getNoGoalSelectedPercent()
  {
    return noGoalSelectedPercent;
  }

  public void setNoGoalSelectedPercent( BigDecimal noGoalSelectedPercent )
  {
    this.noGoalSelectedPercent = noGoalSelectedPercent;
  }

  public Long getLevel1Selected()
  {
    return level1Selected;
  }

  public void setLevel1Selected( Long level1Selected )
  {
    this.level1Selected = level1Selected;
  }

  public BigDecimal getLevel1SelectedPercent()
  {
    return level1SelectedPercent;
  }

  public void setLevel1SelectedPercent( BigDecimal level1SelectedPercent )
  {
    this.level1SelectedPercent = level1SelectedPercent;
  }

  public Long getLevel2Selected()
  {
    return level2Selected;
  }

  public void setLevel2Selected( Long level2Selected )
  {
    this.level2Selected = level2Selected;
  }

  public BigDecimal getLevel2SelectedPercent()
  {
    return level2SelectedPercent;
  }

  public void setLevel2SelectedPercent( BigDecimal level2SelectedPercent )
  {
    this.level2SelectedPercent = level2SelectedPercent;
  }

  public Long getLevel3Selected()
  {
    return level3Selected;
  }

  public void setLevel3Selected( Long level3Selected )
  {
    this.level3Selected = level3Selected;
  }

  public BigDecimal getLevel3SelectedPercent()
  {
    return level3SelectedPercent;
  }

  public void setLevel3SelectedPercent( BigDecimal level3SelectedPercent )
  {
    this.level3SelectedPercent = level3SelectedPercent;
  }

  public Long getLevel4Selected()
  {
    return level4Selected;
  }

  public void setLevel4Selected( Long level4Selected )
  {
    this.level4Selected = level4Selected;
  }

  public BigDecimal getLevel4SelectedPercent()
  {
    return level4SelectedPercent;
  }

  public void setLevel4SelectedPercent( BigDecimal level4SelectedPercent )
  {
    this.level4SelectedPercent = level4SelectedPercent;
  }

  public Long getLevel5Selected()
  {
    return level5Selected;
  }

  public void setLevel5Selected( Long level5Selected )
  {
    this.level5Selected = level5Selected;
  }

  public BigDecimal getLevel5SelectedPercent()
  {
    return level5SelectedPercent;
  }

  public void setLevel5SelectedPercent( BigDecimal level5SelectedPercent )
  {
    this.level5SelectedPercent = level5SelectedPercent;
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

  public String getLevelName()
  {
    return levelName;
  }

  public void setLevelName( String levelName )
  {
    this.levelName = levelName;
  }

  public String getPromoName()
  {
    return promoName;
  }

  public void setPromoName( String promoName )
  {
    this.promoName = promoName;
  }

  public Long getLevel6Selected()
  {
    return level6Selected;
  }

  public void setLevel6Selected( Long level6Selected )
  {
    this.level6Selected = level6Selected;
  }

  public BigDecimal getLevel6SelectedPercent()
  {
    return level6SelectedPercent;
  }

  public void setLevel6SelectedPercent( BigDecimal level6SelectedPercent )
  {
    this.level6SelectedPercent = level6SelectedPercent;
  }

}

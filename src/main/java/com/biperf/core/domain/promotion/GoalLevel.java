/*
 * (c) 2006 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/domain/promotion/GoalLevel.java,v $
 */

package com.biperf.core.domain.promotion;

import java.math.BigDecimal;

/**
 * GoalLevel.
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
 * <td>meadows</td>
 * <td>Dec 11, 2006</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class GoalLevel extends AbstractGoalLevel
{

  private BigDecimal achievementAmount;
  private BigDecimal minimumQualifier;
  private BigDecimal incrementalQuantity;
  private Integer maximumPoints;
  private Integer bonusAward;
  private BigDecimal award;

  public BigDecimal getAchievementAmount()
  {
    return achievementAmount;
  }

  public void setAchievementAmount( BigDecimal achievementAmount )
  {
    this.achievementAmount = achievementAmount;
  }

  public Integer getBonusAward()
  {
    return bonusAward;
  }

  public void setBonusAward( Integer bonusAward )
  {
    this.bonusAward = bonusAward;
  }

  public BigDecimal getIncrementalQuantity()
  {
    return incrementalQuantity;
  }

  public void setIncrementalQuantity( BigDecimal incrementalQuantity )
  {
    this.incrementalQuantity = incrementalQuantity;
  }

  public Integer getMaximumPoints()
  {
    return maximumPoints;
  }

  public void setMaximumPoints( Integer maximumPoints )
  {
    this.maximumPoints = maximumPoints;
  }

  public BigDecimal getMinimumQualifier()
  {
    return minimumQualifier;
  }

  public void setMinimumQualifier( BigDecimal minimumQualifier )
  {
    this.minimumQualifier = minimumQualifier;
  }

  public BigDecimal getAward()
  {
    return award;
  }

  public void setAward( BigDecimal award )
  {
    this.award = award;
  }

  public boolean isManagerOverrideGoalLevel()
  {
    return false;
  }

  public boolean isPromotionPartnerPayout()
  {
    return false;
  }

}

/*
 * (c) 2006 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/domain/promotion/ManagerOverrideGoalLevel.java,v $
 */

package com.biperf.core.domain.promotion;

import java.math.BigDecimal;

/**
 * ManagerOverrideGoalLevel.
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
 * <td>Todd</td>
 * <td>Dec 11, 2006</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ManagerOverrideGoalLevel extends AbstractGoalLevel
{
  private static final long serialVersionUID = 1L;
  private BigDecimal teamAchievementPercent;
  private Long moStartRank;
  private Long moEndRank;
  private Long moAwards;

  public BigDecimal getTeamAchievementPercent()
  {
    return teamAchievementPercent;
  }

  public void setTeamAchievementPercent( BigDecimal teamAchievementPercent )
  {
    this.teamAchievementPercent = teamAchievementPercent;
  }

  public Long getMoStartRank()
  {
    return moStartRank;
  }

  public void setMoStartRank( Long moStartRank )
  {
    this.moStartRank = moStartRank;
  }

  public Long getMoEndRank()
  {
    return moEndRank;
  }

  public void setMoEndRank( Long moEndRank )
  {
    this.moEndRank = moEndRank;
  }

  public Long getMoAwards()
  {
    return moAwards;
  }

  public void setMoAwards( Long moAwards )
  {
    this.moAwards = moAwards;
  }

  public boolean isManagerOverrideGoalLevel()
  {
    return true;
  }

  /**
   * Overridden from @see java.lang.Object#equals(java.lang.Object)
   * @param obj
   * @return
  */
  public boolean equals( Object obj )
  {
    if ( this == obj )
    {
      return true;
    }
    if ( obj == null )
    {
      return false;
    }
    if ( getClass() != obj.getClass() )
    {
      return false;
    }
    final ManagerOverrideGoalLevel other = (ManagerOverrideGoalLevel)obj;
    if ( moStartRank == null )
    {
      if ( other.moStartRank != null )
      {
        return false;
      }
    }
    else if ( !moStartRank.equals( other.moStartRank ) )
    {
      return false;
    }

    if ( moEndRank == null )
    {
      if ( other.moEndRank != null )
      {
        return false;
      }
    }
    else if ( !moEndRank.equals( other.moEndRank ) )
    {
      return false;
    }

    if ( moAwards == null )
    {
      if ( other.moAwards != null )
      {
        return false;
      }
    }
    else if ( !moAwards.equals( other.moAwards ) )
    {
      return false;
    }

    /*
     * if ( ! ( obj instanceof ManagerOverrideGoalLevel ) ) { return false; }
     * ManagerOverrideGoalLevel otherManagerOverrideGoalLevel = (ManagerOverrideGoalLevel)obj;
     * return super.equals( otherManagerOverrideGoalLevel );
     */

    return true;
  }

  /**
   * Overridden from @see java.lang.Object#hashCode()
   * @return
   */
  public int hashCode()
  {
    final int PRIME = 31;
    int result = 1;
    result = PRIME * result + ( moStartRank == null ? 0 : moStartRank.hashCode() );
    result = PRIME * result + ( moEndRank == null ? 0 : moEndRank.hashCode() );
    result = PRIME * result + ( moAwards == null ? 0 : moAwards.hashCode() );
    return result;
  }

  public boolean isPromotionPartnerPayout()
  {
    return false;
  }
}

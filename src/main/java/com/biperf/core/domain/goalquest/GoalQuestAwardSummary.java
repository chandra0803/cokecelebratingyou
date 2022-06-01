/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/domain/goalquest/GoalQuestAwardSummary.java,v $
 */

package com.biperf.core.domain.goalquest;

import java.io.Serializable;
import java.math.BigDecimal;

import com.biperf.core.domain.promotion.AbstractGoalLevel;

/*
 * GoalQuestAwardSummary <p> <b>Change History:</b><br> <table border="1"> <tr> <th>Satish</th> <th>Date</th>
 * <th>Version</th> <th>Comments</th> </tr> <tr> <td>Satish</td> <td>Jan 4, 2007</td>
 * <td>1.0</td> <td>created</td> </tr> </table> </p>
 * 
 *
 */

public class GoalQuestAwardSummary implements Serializable
{

  private AbstractGoalLevel goalLevel;
  private int totalSelected = 0;
  private int totalAchieved = 0;
  private BigDecimal totalAward;
  private boolean participantGoalQuestAwardSummary;
  private boolean managerOverrideGoalQuestAwardSummary;
  private boolean partnerGoalQuestAwardSummary;
  private boolean participantTotals;
  private boolean managerTotals;
  private boolean partnerTotals;
  /* A boolean indicating that by default leveloneAward is true and levetwoAward is false */
  private boolean leveloneAward = false;
  private Long startRank;
  private Long endRank;

  public boolean isManagerOverrideGoalQuestAwardSummary()
  {
    return managerOverrideGoalQuestAwardSummary;
  }

  public void setManagerOverrideGoalQuestAwardSummary( boolean isManagerOverrideGoalQuestAwardSummary )
  {
    this.managerOverrideGoalQuestAwardSummary = isManagerOverrideGoalQuestAwardSummary;
  }

  public boolean isManagerTotals()
  {
    return managerTotals;
  }

  public void setManagerTotals( boolean isManagerTotals )
  {
    this.managerTotals = isManagerTotals;
  }

  public boolean isParticipantGoalQuestAwardSummary()
  {
    return participantGoalQuestAwardSummary;
  }

  public void setParticipantGoalQuestAwardSummary( boolean isParticipantGoalQuestAwardSummary )
  {
    this.participantGoalQuestAwardSummary = isParticipantGoalQuestAwardSummary;
  }

  public boolean isParticipantTotals()
  {
    return participantTotals;
  }

  public void setParticipantTotals( boolean isParticipantTotals )
  {
    this.participantTotals = isParticipantTotals;
  }

  public int getTotalAchieved()
  {
    return totalAchieved;
  }

  public void setTotalAchieved( int totalAchieved )
  {
    this.totalAchieved = totalAchieved;
  }

  public BigDecimal getTotalAward()
  {
    return totalAward;
  }

  public void setTotalAward( BigDecimal totalAward )
  {
    this.totalAward = totalAward;
  }

  public int getTotalSelected()
  {
    return totalSelected;
  }

  public void setTotalSelected( int totalSelected )
  {
    this.totalSelected = totalSelected;
  }

  public AbstractGoalLevel getGoalLevel()
  {
    return goalLevel;
  }

  public void setGoalLevel( AbstractGoalLevel goalLevel )
  {
    this.goalLevel = goalLevel;
  }

  public boolean isPartnerGoalQuestAwardSummary()
  {
    return partnerGoalQuestAwardSummary;
  }

  public void setPartnerGoalQuestAwardSummary( boolean partnerGoalQuestAwardSummary )
  {
    this.partnerGoalQuestAwardSummary = partnerGoalQuestAwardSummary;
  }

  public boolean isPartnerTotals()
  {
    return partnerTotals;
  }

  public void setPartnerTotals( boolean partnerTotals )
  {
    this.partnerTotals = partnerTotals;
  }

  public void incrementTotalSelected()
  {
    totalSelected++;
  }

  public void incrementTotalSelected( int value )
  {
    totalSelected += value;
  }

  public void incrementTotalAchieved()
  {
    totalAchieved++;
  }

  public void incrementTotalAchieved( int value )
  {
    totalAchieved += value;
  }

  public void incrementTotalAward( BigDecimal incrementAward )
  {
    if ( this.totalAward == null )
    {
      this.totalAward = incrementAward;
    }
    else
    {
      if ( incrementAward != null )
      {
        this.totalAward = this.totalAward.add( incrementAward );
      }
    }
  }

  public boolean isLeveloneAward()
  {
    return leveloneAward;
  }

  public void setLeveloneAward( boolean leveloneAward )
  {
    this.leveloneAward = leveloneAward;
  }

  public Long getStartRank()
  {
    return startRank;
  }

  public void setStartRank( Long startRank )
  {
    this.startRank = startRank;
  }

  public Long getEndRank()
  {
    return endRank;
  }

  public void setEndRank( Long endRank )
  {
    this.endRank = endRank;
  }

}

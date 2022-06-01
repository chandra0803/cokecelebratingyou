/*
 * (c) 2008 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/domain/challengepoint/ChallengepointAwardSummary.java,v $
 */

package com.biperf.core.domain.challengepoint;

import java.io.Serializable;
import java.math.BigDecimal;

import com.biperf.core.domain.promotion.GoalLevel;

/*
 * ChallengepointAwardSummary <p> <b>Change History:</b><br> <table
 * border="1"> <tr> <th>Babu</th> <th>Date</th> <th>Version</th> <th>Comments</th>
 * </tr> <tr> <td>Babu</td> <td>Jul 28, 2008</td> <td>1.0</td> <td>created</td>
 * </tr> </table> </p>
 * 
 * 
 */

public class ChallengepointAwardSummary implements Serializable
{

  private GoalLevel goalLevel;

  private Long totalSelected = new Long( 0 );

  private BigDecimal basicEarned;

  private BigDecimal basicDeposited;

  private BigDecimal basicPending;

  private int totalAchieved = 0;

  private BigDecimal totalAchievedAward;

  private BigDecimal totalAward;

  private boolean participantTotals;

  /* A boolean indicating that by default leveloneAward is true and levetwoAward is false */
  private boolean leveloneAward = false;

  private boolean partnerGoalQuestAwardSummary;

  private boolean partnerTotals;
  private boolean managerTotals;

  private Long startRank;
  private Long endRank;

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

  public Long getTotalSelected()
  {
    return totalSelected;
  }

  public void setTotalSelected( Long totalSelected )
  {
    this.totalSelected = totalSelected;
  }

  public void incrementTotalSelected()
  {
    totalSelected = new Long( totalSelected.longValue() + 1 );
  }

  public void incrementTotalSelected( int value )
  {
    totalSelected = new Long( totalSelected.longValue() + value );
  }

  public void incrementTotalAchieved()
  {
    totalAchieved++;
  }

  public void incrementTotalAchieved( int value )
  {
    totalAchieved += value;
  }

  public void incrementTotalAchievedAward( BigDecimal incrementAward )
  {
    if ( this.totalAchievedAward == null )
    {
      this.totalAchievedAward = incrementAward;
    }
    else
    {
      if ( incrementAward != null )
      {
        this.totalAchievedAward = this.totalAchievedAward.add( incrementAward );
      }
    }
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

  public void incrementTotalAward( Long incrementAward )
  {
    if ( this.totalAward == null )
    {
      this.totalAward = new BigDecimal( incrementAward != null ? incrementAward.longValue() : 0 );
    }
    else
    {
      if ( incrementAward != null )
      {
        this.totalAward = this.totalAward.add( new BigDecimal( incrementAward.longValue() ) );
      }
    }
  }

  public BigDecimal getBasicDeposited()
  {
    return basicDeposited;
  }

  public void setBasicDeposited( BigDecimal basicDeposited )
  {
    this.basicDeposited = basicDeposited;
  }

  public BigDecimal getBasicEarned()
  {
    return basicEarned;
  }

  public void setBasicEarned( BigDecimal basicEarned )
  {
    this.basicEarned = basicEarned;
  }

  public BigDecimal getBasicPending()
  {
    return basicPending;
  }

  public void setBasicPending( BigDecimal basicPending )
  {
    this.basicPending = basicPending;
  }

  public GoalLevel getGoalLevel()
  {
    return goalLevel;
  }

  public void incrementBasicEarned( Long incrementAward )
  {
    if ( this.basicEarned == null )
    {
      this.basicEarned = new BigDecimal( incrementAward != null ? incrementAward.longValue() : 0 );
    }
    else
    {
      if ( incrementAward != null )
      {
        this.basicEarned = this.basicEarned.add( new BigDecimal( incrementAward.longValue() ) );
      }
    }
  }

  public void incrementBasicDeposited( Long incrementAward )
  {
    if ( this.basicDeposited == null )
    {
      this.basicDeposited = new BigDecimal( incrementAward != null ? incrementAward.longValue() : 0 );
    }
    else
    {
      if ( incrementAward != null )
      {
        this.basicDeposited = this.basicDeposited.add( new BigDecimal( incrementAward.longValue() ) );
      }
    }
  }

  public void incrementBasicPending( Long incrementAward )
  {
    if ( this.basicPending == null )
    {
      this.basicPending = new BigDecimal( incrementAward != null ? incrementAward.longValue() : 0 );
    }
    else
    {
      if ( incrementAward != null )
      {
        this.basicPending = this.basicPending.add( new BigDecimal( incrementAward.longValue() ) );
      }
    }
  }

  public void incrementBasicEarned( BigDecimal incrementAward )
  {
    if ( this.basicEarned == null )
    {
      this.basicEarned = incrementAward;
    }
    else
    {
      if ( incrementAward != null )
      {
        this.basicEarned = this.basicEarned.add( incrementAward );
      }
    }
  }

  public void incrementBasicDeposited( BigDecimal incrementAward )
  {
    if ( this.basicDeposited == null )
    {
      this.basicDeposited = incrementAward;
    }
    else
    {
      if ( incrementAward != null )
      {
        this.basicDeposited = this.basicDeposited.add( incrementAward );
      }
    }
  }

  public void incrementBasicPending( BigDecimal incrementAward )
  {
    if ( this.basicPending == null )
    {
      this.basicPending = incrementAward;
    }
    else
    {
      if ( incrementAward != null )
      {
        this.basicPending = this.basicPending.add( incrementAward );
      }
    }
  }

  public void setGoalLevel( GoalLevel goalLevel )
  {
    this.goalLevel = goalLevel;
  }

  public BigDecimal getTotalAchievedAward()
  {
    return totalAchievedAward;
  }

  public void setTotalAchievedAward( BigDecimal totalAchievedAward )
  {
    this.totalAchievedAward = totalAchievedAward;
  }

  public boolean isParticipantTotals()
  {
    return participantTotals;
  }

  public void setParticipantTotals( boolean participantTotals )
  {
    this.participantTotals = participantTotals;
  }

  public boolean isBasicPendingGTZero()
  {
    if ( basicPending != null && basicPending.longValue() > 0 )
    {
      return true;
    }
    return false;
  }

  public String getGoalLevelName()
  {
    return goalLevel.getGoalLevelName();
  }

  public boolean isLeveloneAward()
  {
    return leveloneAward;
  }

  public void setLeveloneAward( boolean leveloneAward )
  {
    this.leveloneAward = leveloneAward;
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

  public boolean isManagerTotals()
  {
    return managerTotals;
  }

  public void setManagerTotals( boolean managerTotals )
  {
    this.managerTotals = managerTotals;
  }

}

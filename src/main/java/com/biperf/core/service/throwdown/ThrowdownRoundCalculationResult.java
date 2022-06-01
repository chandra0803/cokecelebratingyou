
package com.biperf.core.service.throwdown;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.biperf.core.domain.promotion.ThrowdownPromotion;
import com.biperf.core.utils.DateUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class ThrowdownRoundCalculationResult implements Serializable
{
  private static final long serialVersionUID = 1L;

  private ThrowdownPromotion promotion = null;
  private List<DivisionCalculationResult> divisionResults = new ArrayList<DivisionCalculationResult>();
  private int roundNumber = 0;
  private boolean anyPayoutFailed = false;

  public String getDisplayStartDate()
  {
    return DateUtils.toDisplayString( getRoundStartDate() );
  }

  public String getDisplayEndDate()
  {
    return DateUtils.toDisplayString( getRoundEndDate() );
  }

  public Date getRoundStartDate()
  {
    return divisionResults.get( 0 ).getRound().getStartDate();
  }

  public Date getRoundEndDate()
  {
    return divisionResults.get( 0 ).getRound().getEndDate();
  }

  public int getRoundNumber()
  {
    return roundNumber;
  }

  public void setRoundNumber( int roundNumber )
  {
    this.roundNumber = roundNumber;
  }

  @JsonIgnore
  public ThrowdownPromotion getPromotion()
  {
    return promotion;
  }

  @JsonIgnore
  public void setPromotion( ThrowdownPromotion promotion )
  {
    this.promotion = promotion;
  }

  public int getTotalPointsIssued()
  {
    int i = 0;
    for ( DivisionCalculationResult divResult : divisionResults )
    {
      i = i + divResult.getTotalPointsIssued();
    }
    return i;
  }

  public int getTotalNumberofWins()
  {
    int i = 0;
    for ( DivisionCalculationResult divResult : divisionResults )
    {
      i = i + divResult.getNumberOfWins();
    }
    return i;
  }

  public int getTotalNumberofTies()
  {
    int i = 0;
    for ( DivisionCalculationResult divResult : divisionResults )
    {
      i = i + divResult.getNumberOfTies();
    }
    return i;
  }

  public int getTotalNumberofLosses()
  {
    int i = 0;
    for ( DivisionCalculationResult divResult : divisionResults )
    {
      i = i + divResult.getNumberOfLosses();
    }
    return i;
  }

  public List<DivisionCalculationResult> getDivisionResults()
  {
    return divisionResults;
  }

  public void setDivisionResults( List<DivisionCalculationResult> divisionResults )
  {
    this.divisionResults = divisionResults;
  }

  public boolean isAnyPayoutFailed()
  {
    return anyPayoutFailed;
  }

  public void setAnyPayoutFailed( boolean anyPayoutFailed )
  {
    this.anyPayoutFailed = anyPayoutFailed;
  }

}

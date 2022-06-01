
package com.biperf.core.service.throwdown;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.biperf.core.domain.enums.MatchTeamOutcomeType;
import com.biperf.core.domain.promotion.Division;
import com.biperf.core.domain.promotion.DivisionPayout;
import com.biperf.core.domain.promotion.Round;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class DivisionCalculationResult implements Serializable
{
  private static final long serialVersionUID = 1L;

  private Division division = null;
  private Round round = null;
  private List<TeamCalculationResult> teamResults = new ArrayList<TeamCalculationResult>();

  public int getTotalPointsIssued()
  {
    return getTotalPointsIssuedForWins() + getTotalPointsIssuedForTies() + getTotalPointsIssuedForLosses();
  }

  public Division getDivision()
  {
    return division;
  }

  public void setDivision( Division division )
  {
    this.division = division;
  }

  @JsonIgnore
  public Round getRound()
  {
    return round;
  }

  @JsonIgnore
  public void setRound( Round round )
  {
    this.round = round;
  }

  public List<TeamCalculationResult> getTeamResults()
  {
    return teamResults;
  }

  public void setTeamResults( List<TeamCalculationResult> teamResults )
  {
    this.teamResults = teamResults;
  }

  public int getNumberOfWins()
  {
    return buildCountOfOutcome( MatchTeamOutcomeType.WIN );
  }

  public int getTotalPointsIssuedForWins()
  {
    return buildPointsIssuedForOutcome( MatchTeamOutcomeType.WIN );
  }

  public int getTotalPointsIssuedForLosses()
  {
    return buildPointsIssuedForOutcome( MatchTeamOutcomeType.LOSS );
  }

  public int getTotalPointsIssuedForTies()
  {
    return buildPointsIssuedForOutcome( MatchTeamOutcomeType.TIE );
  }

  /*
   * How many points per outcome....
   */
  public int getPayoutForLosses()
  {
    return getPayoutByType( MatchTeamOutcomeType.LOSS );
  }

  public int getPayoutForTies()
  {
    return getPayoutByType( MatchTeamOutcomeType.TIE );
  }

  public int getPayoutForWins()
  {
    return getPayoutByType( MatchTeamOutcomeType.WIN );
  }

  private int getPayoutByType( String payoutTypeCode )
  {
    DivisionPayout payout = division.getPayoutForOutcome( MatchTeamOutcomeType.lookup( payoutTypeCode ) );
    if ( null == payout )
    {
      return 0;
    }
    else
    {
      return payout.getPoints();
    }
  }

  public int getNumberOfLosses()
  {
    return buildCountOfOutcome( MatchTeamOutcomeType.LOSS );
  }

  public int getPointsForLosses()
  {
    return buildPointsIssuedForOutcome( MatchTeamOutcomeType.LOSS );
  }

  public int getPointsForWins()
  {
    return buildPointsIssuedForOutcome( MatchTeamOutcomeType.WIN );
  }

  public int getNumberOfTies()
  {
    return buildCountOfOutcome( MatchTeamOutcomeType.TIE );
  }

  public int getPointsForTies()
  {
    return buildPointsIssuedForOutcome( MatchTeamOutcomeType.TIE );
  }

  private int buildPointsIssuedForOutcome( String outcome )
  {
    int points = 0;
    for ( TeamCalculationResult teamResult : teamResults )
    {
      if ( teamResult.getOutcomePayoutType().getCode().equals( outcome ) && teamResult.getTotalProgress().compareTo( division.getMinimumQualifierWithPrecisionAndRounding() ) >= 0 )
      {
        points = teamResult.getPayoutAmount() != null ? points + teamResult.getPayoutAmount().intValue() : points;
      }
    }
    return points;
  }

  private int buildCountOfOutcome( String outcome )
  {
    int counter = 0;
    for ( TeamCalculationResult teamResult : teamResults )
    {
      if ( teamResult.getOutcomePayoutType().getCode().equals( outcome ) )
      {
        counter++;
      }
    }
    return counter;
  }
}

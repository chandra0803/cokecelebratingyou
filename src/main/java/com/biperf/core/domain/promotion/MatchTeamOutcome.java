
package com.biperf.core.domain.promotion;

import java.math.BigDecimal;
import java.util.Set;

import com.biperf.core.domain.BaseDomain;
import com.biperf.core.domain.enums.MatchTeamOutcomeType;
import com.biperf.core.domain.enums.ThrowdownMatchProgressType;

@SuppressWarnings( "serial" )
public class MatchTeamOutcome extends BaseDomain implements Cloneable
{
  private Match match;
  private Team team;
  private MatchTeamOutcomeType outcome;
  private ThrowdownPromotion promotion;
  private BigDecimal currentValue;

  private Set<MatchTeamProgress> progress;

  public Match getMatch()
  {
    return match;
  }

  public void setMatch( Match match )
  {
    this.match = match;
  }

  public Team getTeam()
  {
    return team;
  }

  public void setTeam( Team team )
  {
    this.team = team;
  }

  public MatchTeamOutcomeType getOutcome()
  {
    return outcome;
  }

  public void setOutcome( MatchTeamOutcomeType outcome )
  {
    this.outcome = outcome;
  }

  public ThrowdownPromotion getPromotion()
  {
    return promotion;
  }

  public void setPromotion( ThrowdownPromotion promotion )
  {
    this.promotion = promotion;
  }

  public BigDecimal getTotalProgress()
  {
    BigDecimal totals = new BigDecimal( 0.00 );

    for ( MatchTeamProgress teamProgress : progress )
    {
      if ( teamProgress.getProgressType().getCode().equals( ThrowdownMatchProgressType.INCREMENTAL ) )
      {
        // add this progress to existing progress
        totals = totals.add( teamProgress.getProgress() );
      }
      else if ( teamProgress.getProgressType().getCode().equals( ThrowdownMatchProgressType.REPLACE ) )
      {
        // ok, replace/disregard what's there and replace with this number
        totals = teamProgress.getProgress();
      }
    }
    return totals;
  }

  public BigDecimal getTotalProgressWithPrecisionAndRounding()
  {
    BigDecimal totals = new BigDecimal( 0.00 );

    for ( MatchTeamProgress teamProgress : progress )
    {
      if ( teamProgress.getProgressType().getCode().equals( ThrowdownMatchProgressType.INCREMENTAL ) )
      {
        // add this progress to existing progress
        totals = totals.add( teamProgress.getProgress() );
      }
      else if ( teamProgress.getProgressType().getCode().equals( ThrowdownMatchProgressType.REPLACE ) )
      {
        // ok, replace/disregard what's there and replace with this number
        totals = teamProgress.getProgress();
      }
    }

    int precision = promotion.getAchievementPrecision().getPrecision();
    int roundingMode = promotion.getRoundingMethod().getBigDecimalRoundingMode();
    BigDecimal roundedTotalValue = totals.setScale( precision, roundingMode );

    return roundedTotalValue;
  }

  public Set<MatchTeamProgress> getProgress()
  {
    return progress;
  }

  public void setProgress( Set<MatchTeamProgress> progress )
  {
    this.progress = progress;
  }

  public BigDecimal getCurrentValue()
  {
    return currentValue;
  }

  public BigDecimal getCurrentValueWithPrecisionAndRounding()
  {
    BigDecimal roundedCurrentValueValue = new BigDecimal( 0.00 );
    int precision = promotion.getAchievementPrecision() != null ? promotion.getAchievementPrecision().getPrecision() : 0;
    int roundingMode = promotion.getRoundingMethod().getBigDecimalRoundingMode();
    roundedCurrentValueValue = currentValue != null ? currentValue.setScale( precision, roundingMode ) : roundedCurrentValueValue;
    return roundedCurrentValueValue;
  }

  public void setCurrentValue( BigDecimal currentValue )
  {
    this.currentValue = currentValue;
  }

  @Override
  public boolean equals( Object object )
  {
    if ( this == object )
    {
      return true;
    }
    if ( ! ( object instanceof MatchTeamOutcome ) )
    {
      return false;
    }

    final MatchTeamOutcome matchTeamOutcome = (MatchTeamOutcome)object;

    if ( getMatch() != null ? !getMatch().equals( matchTeamOutcome.getMatch() ) : matchTeamOutcome.getMatch() != null )
    {
      return false;
    }

    return false;

    // if ( getTeam() != null ? !getTeam().getId().equals( matchTeamOutcome.getTeam().getId() ) :
    // matchTeamOutcome.getTeam().getId() != null )
    // return false;
    //
    // return true;
  }

  @Override
  public int hashCode()
  {
    int hashCode = 0;

    // hashCode += ( getMatch() != null ? getMatch().hashCode() : 13 );
    hashCode += getTeam() != null ? getTeam().hashCode() : 17;

    return hashCode;
  }
}

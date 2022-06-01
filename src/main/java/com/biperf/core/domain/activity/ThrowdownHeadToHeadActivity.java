
package com.biperf.core.domain.activity;

import com.biperf.core.domain.promotion.MatchTeamOutcome;

@SuppressWarnings( "serial" )
public class ThrowdownHeadToHeadActivity extends AbstractThrowdownActivity
{
  private MatchTeamOutcome matchTeamOutcome;

  public ThrowdownHeadToHeadActivity()
  {
  }

  public ThrowdownHeadToHeadActivity( String guid )
  {
    super( guid );
  }

  public MatchTeamOutcome getMatchTeamOutcome()
  {
    return matchTeamOutcome;
  }

  public void setMatchTeamOutcome( MatchTeamOutcome matchTeamOutcome )
  {
    this.matchTeamOutcome = matchTeamOutcome;
  }
}


package com.biperf.core.domain.activity;

import com.biperf.core.domain.promotion.StackStandingParticipant;

@SuppressWarnings( "serial" )
public class StackStandingActivity extends AbstractThrowdownActivity
{
  private StackStandingParticipant stackStandingParticipant;

  public StackStandingActivity()
  {
  }

  public StackStandingActivity( String guid )
  {
    super( guid );
  }

  public StackStandingParticipant getStackStandingParticipant()
  {
    return stackStandingParticipant;
  }

  public void setStackStandingParticipant( StackStandingParticipant stackStandingParticipant )
  {
    this.stackStandingParticipant = stackStandingParticipant;
  }
}

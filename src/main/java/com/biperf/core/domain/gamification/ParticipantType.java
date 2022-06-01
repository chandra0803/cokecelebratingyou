
package com.biperf.core.domain.gamification;

public enum ParticipantType
{
  NONE( "none" ), PARTNER( "partner" );

  private String type;

  private ParticipantType( String type )
  {
    this.type = type;
  }

  public String getType()
  {
    return type;
  }

}

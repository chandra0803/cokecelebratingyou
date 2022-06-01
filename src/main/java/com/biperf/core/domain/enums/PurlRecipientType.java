
package com.biperf.core.domain.enums;

public enum PurlRecipientType
{

  UPCOMING( "upcoming" ), PAST( "past" );

  private String upComingPast;

  PurlRecipientType( String upComingPast )
  {
    this.upComingPast = upComingPast;
  }

  public String getCode()
  {
    for ( PurlRecipientType t : values() )
    {
      if ( t.equals( this ) )
      {
        return upComingPast;
      }
    }
    return "";
  }
}

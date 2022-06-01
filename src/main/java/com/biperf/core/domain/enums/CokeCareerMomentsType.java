package com.biperf.core.domain.enums;

public enum CokeCareerMomentsType
{
  UPCOMING( "upcoming" ), PAST( "past" );

  private String upComingPast;

  CokeCareerMomentsType( String upComingPast )
  {
    this.upComingPast = upComingPast;
  }

  public String getCode()
  {
    for ( CokeCareerMomentsType t : values() )
    {
      if ( t.equals( this ) )
      {
        return upComingPast;
      }
    }
    return "";
  }

}

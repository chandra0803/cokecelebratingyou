
package com.biperf.core.domain.enums;

public enum ClaimAwardType
{

  NONE( "none" ), POINTS_FIXED( "fixed" ), POINTS_RANGE( "range" ), CALCULATED( "calculated" ), LEVELS( "levels" );

  private String code;

  private ClaimAwardType( String code )
  {
    this.code = code;
  }

  public String getCode()
  {
    return code;
  }

}

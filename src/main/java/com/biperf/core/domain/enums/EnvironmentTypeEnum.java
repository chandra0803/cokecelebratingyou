
package com.biperf.core.domain.enums;

import java.util.HashMap;
import java.util.Map;

public enum EnvironmentTypeEnum
{
  BIUS( "bius" ), AWS( "aws" ), UNKNOWN( "unknown" );

  // Static initializer to map from code field to enum value for efficient lookup
  private static final Map<String, EnvironmentTypeEnum> codeMap = new HashMap<>();
  static
  {
    for ( EnvironmentTypeEnum value : EnvironmentTypeEnum.values() )
    {
      codeMap.put( value.code, value );
    }
  }

  public static final String ENV_TYPE_NAME = "environment.type";

  private final String code;

  private EnvironmentTypeEnum( String code )
  {
    this.code = code;
  }

  public static boolean isBIUS()
  {
    return BIUS.equals( getEnvironmentType() );
  }

  public static boolean isAws()
  {
    return AWS.equals( getEnvironmentType() );
  }

  public static boolean isUnKnown()
  {
    return UNKNOWN.equals( getEnvironmentType() );
  }

  public static EnvironmentTypeEnum getEnvironmentType()
  {
    try
    {
      String envCode = System.getProperty( ENV_TYPE_NAME );
      if ( envCode == null || envCode.trim().equals( "" ) )
      {
        return UNKNOWN;
      }
      envCode = envCode.toLowerCase();
      return codeMap.containsKey( envCode ) ? codeMap.get( envCode ) : UNKNOWN;
    }
    catch( java.security.AccessControlException e )
    {
      return UNKNOWN;
    }
  }

}

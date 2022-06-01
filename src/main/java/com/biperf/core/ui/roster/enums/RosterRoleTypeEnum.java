
package com.biperf.core.ui.roster.enums;

import java.util.Arrays;

public enum RosterRoleTypeEnum
{
  ROSTER_TO_DM_OWNER( "owner", "own" ), ROSTER_TO_DM_MEMBER( "member", "mbr" ), ROSTER_TO_DM_MANAGER( "manager", "mgr" ), DM_TO_ROSTER_OWNER( "own", "owner" ), DM_TO_ROSTER_MEMBER( "mbr",
      "member" ), DM_TO_ROSTER_MANAGER( "mgr", "manager" );

  private final String code;
  private final String value;

  private RosterRoleTypeEnum( String code, String value )
  {
    this.code = code;
    this.value = value;
  }

  public static RosterRoleTypeEnum findTypeByCode( String code )
  {
    return Arrays.stream( values() ).filter( status -> status.getCode().equalsIgnoreCase( code ) ).findFirst().orElse( null );
  }

  public String getValue()
  {
    return value;
  }

  public String getCode()
  {
    return code;
  }
}

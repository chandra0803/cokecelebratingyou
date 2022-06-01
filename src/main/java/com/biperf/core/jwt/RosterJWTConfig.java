
package com.biperf.core.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.biperf.core.service.system.SystemVariableService;

@Component( "RosterJWTConfig" )
public class RosterJWTConfig extends JWTConfig
{
  protected @Autowired SystemVariableService systemVariableService;

  public RosterJWTConfig()
  {
  }

  @Override
  public String secret()
  {
    return systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.JWT_SECRET_ROSTER ).getStringVal();
  }

  @Override
  public Long expiration()
  {
    return 60L;
  }

  @Override
  public String issuer()
  {
    return systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.JWT_ISSUER_ROSTER ).getStringVal();
  }

}

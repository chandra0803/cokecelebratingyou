
package com.biperf.core.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component( "JWTTokenHandlerFactory" )
public class JWTTokenHandlerFactory
{

  @Autowired
  @Qualifier( "RosterJWTConfig" )
  private RosterJWTConfig rosterJWTConfig;

  @Autowired
  @Qualifier( "SecureJWTConfig" )
  private SecureJWTConfig secureJWTConfig;

  /**
   * Return the JWTTokenHandler for Roster
   * 
   * @return
   */
  public JWTTokenHandler getJWTTokenHandler()
  {
    JWTTokenHandler jwtTokenHandler = new JWTTokenHandlerImpl( rosterJWTConfig );
    return jwtTokenHandler;
  }

  public JWTTokenHandler getSecureJWTTokenHandler()
  {
    JWTTokenHandler jwtTokenHandler = new JWTTokenHandlerImpl( secureJWTConfig );
    return jwtTokenHandler;
  }

}

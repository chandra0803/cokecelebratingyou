
package com.biperf.core.strategy.usertoken;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import com.biperf.core.domain.system.PropertySetItem;
import com.biperf.core.domain.user.UserToken;
import com.biperf.core.domain.user.WelcomeEmailUserToken;
import com.biperf.core.service.system.SystemVariableService;

public class WelcomeEmailUserTokenStrategy extends BaseUserTokenStrategy
{
  @Override
  public UserToken generateUserToken( Long userId )
  {
    WelcomeEmailUserToken token = new WelcomeEmailUserToken();
    token = buildToken( userId, token );
    token.setToken( buildEmailToken() );
    token.setUnencryptedTokenValue( token.getToken() );
    return token;
  }

  @Override
  protected int getLength()
  {
    PropertySetItem prop = getSystemVariableService().getPropertyByName( SystemVariableService.USER_TOKEN_LENGTH_WELCOME_EMAIL );
    return prop.getIntVal();
  }

  @Override
  protected Date buildExpirationDate()
  {
    PropertySetItem prop = getSystemVariableService().getPropertyByName( SystemVariableService.USER_TOKEN_EXPIRATION_DAYS_WELCOME_EMAIL );
    return Date.from( Instant.now().plus( prop.getLongVal(), ChronoUnit.DAYS ) );
  }

}

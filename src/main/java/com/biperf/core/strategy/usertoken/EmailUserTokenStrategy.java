
package com.biperf.core.strategy.usertoken;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import com.biperf.core.domain.system.PropertySetItem;
import com.biperf.core.domain.user.EmailUserToken;
import com.biperf.core.domain.user.UserToken;
import com.biperf.core.service.system.SystemVariableService;

public class EmailUserTokenStrategy extends BaseUserTokenStrategy
{

  @Override
  public UserToken generateUserToken( Long userId )
  {
    EmailUserToken token = new EmailUserToken();
    token = buildToken( userId, token );
    // make sure to lower case the results since this may be hand entered...
    token.setToken( buildEmailToken().toLowerCase() );
    token.setUnencryptedTokenValue( token.getToken() );
    return token;
  }

  @Override
  protected int getLength()
  {
    PropertySetItem prop = getSystemVariableService().getPropertyByName( SystemVariableService.USER_TOKEN_LENGTH_EMAIL );
    return prop.getIntVal();
  }

  @Override
  protected Date buildExpirationDate()
  {
    PropertySetItem prop = getSystemVariableService().getPropertyByName( SystemVariableService.USER_TOKEN_EXPIRATION_HOURS_EMAIL );
    return Date.from( Instant.now().plus( prop.getLongVal(), ChronoUnit.HOURS ) );
  }
}

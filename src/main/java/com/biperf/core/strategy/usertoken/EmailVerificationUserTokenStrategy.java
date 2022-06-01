
package com.biperf.core.strategy.usertoken;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import com.biperf.core.domain.system.PropertySetItem;
import com.biperf.core.domain.user.EmailVerificationUserToken;
import com.biperf.core.domain.user.UserToken;
import com.biperf.core.service.system.SystemVariableService;

public class EmailVerificationUserTokenStrategy extends BaseUserTokenStrategy
{

  @Override
  public UserToken generateUserToken( Long userId )
  {
    EmailVerificationUserToken token = new EmailVerificationUserToken();
    token = buildToken( userId, token );
    token.setToken( buildEmailToken().toLowerCase() );
    token.setUnencryptedTokenValue( token.getToken() );
    return token;
  }

  @Override
  protected int getLength()
  {
    PropertySetItem prop = getSystemVariableService().getPropertyByName( SystemVariableService.USER_TOKEN_LENGTH_EMAIL_VERIFICATION );
    return prop.getIntVal();
  }

  @Override
  protected Date buildExpirationDate()
  {
    PropertySetItem prop = getSystemVariableService().getPropertyByName( SystemVariableService.USER_TOKEN_EXPIRATION_HOURS_EMAIL_VERIFICATION );
    return Date.from( Instant.now().plus( prop.getLongVal(), ChronoUnit.HOURS ) );
  }
}


package com.biperf.core.strategy.usertoken;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.apache.commons.lang.RandomStringUtils;

import com.biperf.core.domain.system.PropertySetItem;
import com.biperf.core.domain.user.PhoneUserToken;
import com.biperf.core.domain.user.UserToken;
import com.biperf.core.service.system.SystemVariableService;

public class PhoneUserTokenStrategy extends BaseUserTokenStrategy
{

  @Override
  public UserToken generateUserToken( Long userId )
  {
    PhoneUserToken token = new PhoneUserToken();
    token = buildToken( userId, token );
    token.setToken( buildPhoneToken() );
    token.setUnencryptedTokenValue( token.getToken() );
    return token;
  }

  @Override
  protected int getLength()
  {
    PropertySetItem prop = getSystemVariableService().getPropertyByName( SystemVariableService.USER_TOKEN_LENGTH_PHONE );
    return prop.getIntVal();
  }

  private String buildPhoneToken()
  {
    return RandomStringUtils.randomNumeric( getLength() );
  }

  @Override
  protected Date buildExpirationDate()
  {
    PropertySetItem prop = getSystemVariableService().getPropertyByName( SystemVariableService.USER_TOKEN_EXPIRATION_MINUTES_PHONE );
    return Date.from( Instant.now().plus( prop.getLongVal(), ChronoUnit.MINUTES ) );
  }
}

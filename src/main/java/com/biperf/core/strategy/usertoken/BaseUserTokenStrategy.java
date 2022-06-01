
package com.biperf.core.strategy.usertoken;

import java.util.Date;

import org.apache.commons.lang.RandomStringUtils;

import com.biperf.core.domain.enums.UserTokenStatusType;
import com.biperf.core.domain.user.UserToken;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.strategy.Strategy;

public abstract class BaseUserTokenStrategy implements Strategy, UserTokenStrategy
{
  private SystemVariableService systemVariableService;
  private UserService userService;

  @Override
  public abstract UserToken generateUserToken( Long userId );

  protected abstract int getLength();

  protected abstract Date buildExpirationDate();

  protected <T extends UserToken> T buildToken( Long userId, T token )
  {
    token.setAllowRegenerate( false );
    token.setStatus( UserTokenStatusType.lookup( UserTokenStatusType.ISSUED ) );
    token.setUser( userService.getUserById( userId ) );
    token.setExpirationDate( buildExpirationDate() );
    return token;
  }

  protected String buildEmailToken()
  {
    return RandomStringUtils.randomAlphanumeric( getLength() );
  }

  @Override
  public SystemVariableService getSystemVariableService()
  {
    return systemVariableService;
  }

  public void setSystemVariableService( SystemVariableService systemVariableService )
  {
    this.systemVariableService = systemVariableService;
  }

  public UserService getUserService()
  {
    return userService;
  }

  public void setUserService( UserService userService )
  {
    this.userService = userService;
  }

}

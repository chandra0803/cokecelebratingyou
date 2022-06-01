
package com.biperf.core.domain.user;

import com.biperf.core.strategy.usertoken.UserTokenType;

@SuppressWarnings( "serial" )
public class EmailUserToken extends UserToken
{
  
  @Override
  public UserTokenType getUserTokenType()
  {
    return UserTokenType.EMAIL;
  }
  
}
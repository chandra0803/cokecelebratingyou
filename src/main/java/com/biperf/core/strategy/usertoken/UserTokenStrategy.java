
package com.biperf.core.strategy.usertoken;

import com.biperf.core.domain.user.UserToken;
import com.biperf.core.strategy.Strategy;

public interface UserTokenStrategy extends Strategy
{
  public UserToken generateUserToken( Long userId );
}

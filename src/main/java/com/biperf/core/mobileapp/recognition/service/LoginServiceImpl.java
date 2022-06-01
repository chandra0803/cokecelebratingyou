
package com.biperf.core.mobileapp.recognition.service;

import org.apache.commons.lang3.RandomStringUtils;

import com.biperf.core.domain.user.User;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.utils.Environment;

public class LoginServiceImpl implements LoginService
{
  private UserService userService = null;

  public MobileLoginToken onSuccessfulAuthentication( Long userId )
  {
    // get the user
    User user = userService.getUserById( userId );

    userService.saveLoginInfo( userId );
    
    // if production, always generate a new token to return to the device
    if ( Environment.ENV_PROD.equals( Environment.getEnvironment() ) )
    {
      // generate a login token for the user
      String token = RandomStringUtils.randomAlphanumeric( 30 );

      // set it on the user
      user.setLoginToken( token );

      return new MobileLoginToken( token );
    }
    else
    {
      // non-prod, so return a login token if it already exists since we often
      // use the same usernames on multiple devices

      // only generate a login token if the user doesn't already have one
      if ( user.getLoginToken() == null || user.getLoginToken().trim().length() == 0 )
      {
        // generate a login token for the user
        String token = RandomStringUtils.randomAlphanumeric( 30 );

        // set it on the user
        user.setLoginToken( token );
      }

      return new MobileLoginToken( user.getLoginToken() );
    }

  }

  public void setUserService( UserService userService )
  {
    this.userService = userService;
  }
}

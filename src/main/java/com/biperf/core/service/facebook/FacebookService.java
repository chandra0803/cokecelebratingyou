
package com.biperf.core.service.facebook;

import com.biperf.core.service.SAO;

public interface FacebookService extends SAO
{
  public static final String BEAN_NAME = "facebookService";

  public boolean postMessageToWall( Long userId, String message );

  public String getApiKey();

  public String getAppId();

  public String getFacebookLoginUrl( String redirectUrl );

  public void updateUserFacebookInfo( Long userId, String code, String redirectUrl );
}

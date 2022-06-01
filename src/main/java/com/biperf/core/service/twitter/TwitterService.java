
package com.biperf.core.service.twitter;

import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.SAO;

public interface TwitterService extends SAO
{
  public static final String BEAN_NAME = "twitterService";

  public void tweet( long userId, String tweet ) throws ServiceErrorException;

  public String startTwitterAuthorizationFor( Long userId ) throws ServiceErrorException;

  public String startTwitterAuthorizationFor( Long userId, String callbackUrl ) throws ServiceErrorException;

  public void completeTwitterAuthorizationFor( Long userId, String pin ) throws ServiceErrorException;

  public void deleteTwitterAuthorizationFor( Long userId );
}

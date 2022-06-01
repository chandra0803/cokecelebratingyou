
package com.biperf.core.mobileapp.recognition.service;

import com.biperf.core.service.SAO;

public interface LoginService extends SAO
{
  public static final String BEAN_NAME = "mobileLoginService";

  public MobileLoginToken onSuccessfulAuthentication( Long userId );
}

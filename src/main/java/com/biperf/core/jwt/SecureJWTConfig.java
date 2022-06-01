
package com.biperf.core.jwt;

import org.springframework.stereotype.Component;

@Component( "SecureJWTConfig" )
public class SecureJWTConfig extends JWTConfig
{
  public static final String SECURE_CLIENT_ID = "ptYhTxwWGXZHnY";
  public static final String SECURE_CLIENT_SECRET = "kCvdawjAdcABLb";

  public SecureJWTConfig()
  {
  }

  @Override
  public String secret()
  {
    return SECURE_CLIENT_SECRET;
  }

  @Override
  public Long expiration()
  {
    return 60L;
  }

  @Override
  public String issuer()
  {
    return SECURE_CLIENT_ID;
  }

}

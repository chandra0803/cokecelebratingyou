
package com.biperf.core.jwt;

public abstract class JWTConfig
{
  public abstract String secret();

  public abstract Long expiration();

  public abstract String issuer();

}

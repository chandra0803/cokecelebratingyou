
package com.biperf.core.security.ws.rest;

public interface SecureToken
{
  public String getSecurityToken();

  public String getHashableAttribute();
}

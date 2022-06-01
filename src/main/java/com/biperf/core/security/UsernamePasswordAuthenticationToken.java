
package com.biperf.core.security;

public class UsernamePasswordAuthenticationToken extends org.springframework.security.authentication.UsernamePasswordAuthenticationToken
{

  // ~ Instance fields
  // ================================================================================================

  private String sessionId;

  private boolean invalidSSO;

  public UsernamePasswordAuthenticationToken( Object principal, Object credentials, String sessionId )
  {
    super( principal, credentials );
    this.sessionId = sessionId;
  }

  public UsernamePasswordAuthenticationToken( Object principal, Object credentials, String sessionId, boolean invalidSSO )
  {
    super( principal, credentials );
    this.sessionId = sessionId;
    this.invalidSSO = invalidSSO;
  }

  public String getSessionId()
  {
    return sessionId;
  }

  public void setSessionId( String sessionId )
  {
    this.sessionId = sessionId;
  }

  public boolean isInvalidSSO()
  {
    return invalidSSO;
  }

  public void setInvalidSSO( boolean invalidSSO )
  {
    this.invalidSSO = invalidSSO;
  }

}

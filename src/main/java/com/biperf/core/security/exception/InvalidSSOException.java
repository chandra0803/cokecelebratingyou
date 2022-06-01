
package com.biperf.core.security.exception;

import javax.security.auth.login.LoginException;

/**
 *
 * @author johnch
 */
@SuppressWarnings( "serial" )
public class InvalidSSOException extends LoginException
{
  public InvalidSSOException()
  {
    super( "InvalidSSOException" );
  }

}

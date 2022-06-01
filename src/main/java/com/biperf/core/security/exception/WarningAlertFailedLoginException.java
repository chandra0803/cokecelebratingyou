
package com.biperf.core.security.exception;

import javax.security.auth.login.FailedLoginException;

public class WarningAlertFailedLoginException extends FailedLoginException
{
  private static final long serialVersionUID = 802556922354616284L;
  private int attemptsRemaining = 0;

  public WarningAlertFailedLoginException( int attemptsRemaining )
  {
    super();
    this.attemptsRemaining = attemptsRemaining;
  }

  public WarningAlertFailedLoginException( int attemptsRemaining, String msg )
  {
    super( msg );
    this.attemptsRemaining = attemptsRemaining;
  }

  public int getAttemptsRemaining()
  {
    return this.attemptsRemaining;
  }
}
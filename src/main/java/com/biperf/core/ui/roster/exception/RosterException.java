package com.biperf.core.ui.roster.exception;

public class RosterException extends RuntimeException
{
  private static final long serialVersionUID = 1L;

  public RosterException( String msg )
  {
    super( msg );
  }

  public RosterException( String msg, Throwable t )
  {
    super( msg, t );
  }

}

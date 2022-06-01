
package com.biperf.core.service.groups;

import com.biperf.core.exception.ServiceErrorException;

@SuppressWarnings( "serial" )
public class ParticipantGroupException extends ServiceErrorException
{
  public ParticipantGroupException( String serviceErrorMsg )
  {
    super( serviceErrorMsg );
  }

  @Override
  public Throwable fillInStackTrace()
  {
    return null;
  }
}

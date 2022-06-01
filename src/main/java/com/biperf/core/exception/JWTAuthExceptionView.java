package com.biperf.core.exception;

import java.io.Serializable;

public class JWTAuthExceptionView extends ExceptionView implements Serializable
{
  private static final long serialVersionUID = 1L;
  private boolean success = false;

  public JWTAuthExceptionView( int responseCode, String responseMessage, String developerMessage )
  {
    super( responseCode, responseMessage, developerMessage );
  }
}

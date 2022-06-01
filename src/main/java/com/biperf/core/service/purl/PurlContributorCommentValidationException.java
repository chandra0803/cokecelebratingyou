
package com.biperf.core.service.purl;

import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.util.ServiceError;

@SuppressWarnings( "serial" )
public class PurlContributorCommentValidationException extends ServiceErrorException
{
  private String message;

  public PurlContributorCommentValidationException( ServiceError serviceError )
  {
    super( serviceError );
  }

  public PurlContributorCommentValidationException( String serviceErrors )
  {
    super( serviceErrors );
    this.message = serviceErrors;
  }

  public String getMessage()
  {
    return message;
  }
}

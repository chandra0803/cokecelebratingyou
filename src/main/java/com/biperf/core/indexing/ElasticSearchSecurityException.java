
package com.biperf.core.indexing;

import com.biperf.core.exception.ServiceErrorException;

@SuppressWarnings( "serial" )
public class ElasticSearchSecurityException extends ServiceErrorException
{
  public ElasticSearchSecurityException( String serviceErrorMsg )
  {
    super( serviceErrorMsg );
  }
}

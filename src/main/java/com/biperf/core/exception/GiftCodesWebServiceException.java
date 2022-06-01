package com.biperf.core.exception;


/** Bug 66870
 * GiftCodesWebServiceException - Wrapper for all runtime application exceptions Runtime exceptions are
 * those which can not be handled gracefully within the normal operation of the application.
 */
public class GiftCodesWebServiceException extends BeaconRuntimeException
{

  public GiftCodesWebServiceException()
  {
    super();
  }

  public GiftCodesWebServiceException( String pMessage )
  {
    super( pMessage );
  }

  public GiftCodesWebServiceException( Exception pException )
  {
    super( pException );
  }

  public GiftCodesWebServiceException( String pMessage, Exception pException )
  {
    super( pMessage, pException );
  }

}

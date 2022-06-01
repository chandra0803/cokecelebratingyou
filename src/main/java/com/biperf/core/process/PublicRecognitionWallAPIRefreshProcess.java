
package com.biperf.core.process;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.service.publicrecognitionwall.PublicRecognitionWallService;

public class PublicRecognitionWallAPIRefreshProcess extends BaseProcessImpl
{
  private static final Log log = LogFactory.getLog( PublicRecognitionWallAPIRefreshProcess.class );

  public static final String BEAN_NAME = "publicRecognitionWallAPIRefreshProcess";

  private PublicRecognitionWallService publicRecognitionWallService;

  protected void onExecute()
  {
    try
    {
      if ( publicRecognitionWallService.isPublicRecognitionWallFeedEnabled() )
      {
        publicRecognitionWallService.refresh();
        addComment( "The PublicRecognitionWallAPIRefreshProcess has been completed.(process invocation ID = " + getProcessInvocationId() + ")" );
      }
      else // lets let them know this process is running even though the feed is disabled...
      {
        log.info( "The PublicRecognitionWallAPIRefreshProcess has been scheduled, but the feed itself is disabled in the system properties." );
        addComment( "The PublicRecognitionWallAPIRefreshProcess has been scheduled, but the feed itself is disabled in the system properties." );
      }
    }
    catch( Exception e )
    {
      log.error( e );
      addComment( "An Exception occurred. " + "\n" + e.toString() + "\n" + " See the log file for additional information.  " + "(process invocation ID = " + getProcessInvocationId() + ")" );
    }
  }

  public PublicRecognitionWallService getPublicRecognitionWallService()
  {
    return publicRecognitionWallService;
  }

  public void setPublicRecognitionWallService( PublicRecognitionWallService publicRecognitionWallService )
  {
    this.publicRecognitionWallService = publicRecognitionWallService;
  }

}

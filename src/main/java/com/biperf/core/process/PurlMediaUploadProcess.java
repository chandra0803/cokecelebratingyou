
package com.biperf.core.process;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.utils.Environment;
import com.biperf.core.utils.NewServiceAnniversaryUtil;

public class PurlMediaUploadProcess extends BaseProcessImpl
{
  private static final Log log = LogFactory.getLog( PurlMediaUploadProcess.class );

  public static final String BEAN_NAME = "purlMediaUploadProcess";
  public static final String PROCESS_NAME = "purlMediaUploadProcess";
  public static final String MESSAGE_NAME = "Purl Media Upload";
  public static final String PARAM_NAME = "purlContributorId";

  public static final String PROTOCOL_HTTP = "http";
  public static final String PROTOCOL_HTTPS = "https";

  private static final String MEDIA_UPLOAD_PATH = "/purl/purlContribution.do?method=uploadMediaToWebdav&mediaFilePath=";

  private static int executeCount = 0;

  // properties set from jobDataMap
  private String mediaFilePath;
  private String serverInstance;

  public void onExecute()
  {
    // Disabling The Process As New Service Anniversary & Celebration Module Enabled From Nackle
    // Eco-System.
    if ( NewServiceAnniversaryUtil.isNewServiceAnniversaryEnabled() )
    {
      log.info( " The Process 'PurlMediaUploadProcess' Has Been Restricted !!!!, Since The New Service Anniversary Has Been Enabled IN DM From Nackle Eco-System." );
      addComment( " The Process 'PurlMediaUploadProcess' Has Been Restricted !!!!, Since The New Service Anniversary Has Been Enabled IN DM From Nackle Eco-System." );

    }
    else
    {
      log.debug( "Running PurlMediaUploadProcess(" + ( ++executeCount ) + ") for mediaFile[" + mediaFilePath + "]" );

      String urlLocation = getUrlLocation();
      if ( null == urlLocation )
      {
        return;
      }

      openUrl( urlLocation );

    }

  }

  private String getUrlLocation()
  {
    String siteUrl = systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();
    try
    {
      StringBuffer urlLocation = new StringBuffer( siteUrl );
      urlLocation.append( MEDIA_UPLOAD_PATH );
      urlLocation.append( mediaFilePath );
      return urlLocation.toString();
    }
    catch( Exception e )
    {
      log.error( "Exception : URL " + siteUrl, e );
    }
    return null;
  }

  private void openUrl( String urlLocation )
  {
    HttpURLConnection conn = null;
    try
    {
      URL url = new URL( urlLocation );
      conn = (HttpURLConnection)url.openConnection();
      conn.getInputStream();
      if ( log.isDebugEnabled() )
      {
        log.debug( "Successfully hit URL: " + urlLocation );
      }
    }
    catch( IOException e )
    {
      log.error( "Failed to invoke URL " + urlLocation, e );
    }
    finally
    {
      if ( conn != null )
      {
        conn.disconnect();
      }
    }
  }

  private Proxy getProxyConfig()
  {
    if ( !Environment.isCtech() )
    {
      return Proxy.NO_PROXY;
    }

    return Environment.buildProxy();
  }

  public String getMediaFilePath()
  {
    return mediaFilePath;
  }

  public void setMediaFilePath( String mediaFilePath )
  {
    this.mediaFilePath = mediaFilePath;
  }

  public String getServerInstance()
  {
    return serverInstance;
  }

  public void setServerInstance( String serverInstance )
  {
    this.serverInstance = serverInstance;
  }

}

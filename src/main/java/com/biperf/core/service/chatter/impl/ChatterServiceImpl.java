/**
 * 
 */

package com.biperf.core.service.chatter.impl;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONObject;

import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.chatter.ChatterService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.utils.UrlReader;

/**
 * 
 * ChatterServiceImpl.
 * 
 * @author kandhi
 * @since Nov 21, 2013
 * @version 1.0
 */
public class ChatterServiceImpl implements ChatterService
{
  private static final Log log = LogFactory.getLog( ChatterServiceImpl.class );

  private static final String AUTHORIZATION_CODE = "authorization_code";

  private static final String LOGIN_URL = "https://login.salesforce.com/services/oauth2/authorize";
  private static final String AUTH_URL = "https://login.salesforce.com/services/oauth2/token";
  private static final String PROFILE_POST_URL = "/services/data/v29.0/chatter/feeds/user-profile/me/feed-items";

  private SystemVariableService systemVariableService;
  private UrlReader urlReader;

  /**
   * This call to chatter will respond with a full HTML for chatter login page.
   */
  @Override
  public String getChatterAuthorizationCode( String state ) throws ServiceErrorException
  {
    String response = null;
    Map<String, String> params = new HashMap<String, String>();
    params.put( "response_type", "code" );
    params.put( "client_id", getAppClientKey() );
    params.put( "redirect_uri", getRedirectURL() );
    params.put( "state", state );
    try
    {
      response = urlReader.asString( LOGIN_URL, params );
    }
    catch( Throwable t )
    {
      log.error( "\n********************************************\n* ERROR in method getChatterLoginFullPage \n********************************************\n" + "message: " + t.getMessage()
          + "\n********************************************\n" );
      throw new ServiceErrorException( ServiceErrorMessageKeys.CHATTER_ACCESS_ERROR );
    }
    return response;
  }

  /**
   * This call to chatter will respond with an access_token.
   */
  @Override
  public JSONObject getChatterAccessToken( String code, String state ) throws ServiceErrorException
  {
    String response = null;
    Map<String, String> params = new HashMap<String, String>();
    params.put( "grant_type", AUTHORIZATION_CODE );
    params.put( "client_id", getAppClientKey() );
    params.put( "client_secret", getAppClientSecret() );
    params.put( "redirect_uri", getRedirectURL() );
    params.put( "code", code );
    params.put( "state", state );
    JSONObject jsonObject = null;
    try
    {
      response = urlReader.postAndReturnString( AUTH_URL, params );
      jsonObject = new JSONObject( response );
    }
    catch( Throwable t )
    {
      log.error( "\n********************************************\n* ERROR in method getChatterLoginFullPage \n********************************************\n" + "message: " + t.getMessage()
          + "\n********************************************\n" );
      throw new ServiceErrorException( ServiceErrorMessageKeys.CHATTER_ACCESS_ERROR );
    }
    return jsonObject;
  }

  /**
   * Method to post to the profile of the user with the message. The access token is set in the message header.
   * @throws ServiceErrorException 
   */
  @Override
  public JSONObject postMessageToWall( String message, String accessToken, String instanceUrl ) throws ServiceErrorException
  {
    String response = null;
    JSONObject jsonObject = null;
    if ( message == null || message.trim().length() == 0 )
    {
      log.error( "\n********************************************\n* ERROR in method postMessageToWall \n********************************************\n" + "message: " + "Message content is empty."
          + "\n********************************************\n" );
      throw new ServiceErrorException( ServiceErrorMessageKeys.CHATTER_ACCESS_ERROR );
    }

    try
    {
      // Truncate if the message is greater than 4000 characters
      message = URLDecoder.decode( message, "utf-8" ).trim();
      if ( message.length() > 4000 )
      {
        message = message.substring( 0, 4000 );
      }

      Map<String, String> params = new HashMap<String, String>();

      if ( message.indexOf( "||" ) > 0 )
      {
        String[] array = message.split( Pattern.quote( "||" ) );
        params.put( "text", array[0] );
        params.put( "attachmentType", "Link" );
        params.put( "url", array[1] );
        params.put( "urlName", array[2] );
      }
      else
      {
        params.put( "text", message );
      }

      response = urlReader.postAndReturnString( instanceUrl + PROFILE_POST_URL, params, accessToken );
      jsonObject = new JSONObject( response );
    }
    catch( Throwable t )
    {
      log.error( "\n********************************************\n* ERROR in method postMessageToWall \n********************************************\n" + "message: " + t.getMessage()
          + "\n********************************************\n" );
      throw new ServiceErrorException( ServiceErrorMessageKeys.CHATTER_ACCESS_ERROR );
    }
    return jsonObject;
  }

  private String getAppClientKey()
  {
    return systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.CHATTER_CONSUMER_KEY ).getStringVal();
  }

  private String getAppClientSecret()
  {
    return systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.CHATTER_CONSUMER_SECRET ).getStringVal();
  }

  public String getRedirectURL()
  {
    String systemUrl = systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();
    String callbackPath = systemVariableService.getPropertyByName( SystemVariableService.CHATTER_CALLBACK_URL ).getStringVal();
    return systemUrl + callbackPath;
  }

  public void setSystemVariableService( SystemVariableService systemVariableService )
  {
    this.systemVariableService = systemVariableService;
  }

  public void setUrlReader( UrlReader urlReader )
  {
    this.urlReader = urlReader;
  }

}


package com.biperf.core.service.facebook.impl;

import java.text.MessageFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONObject;

import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.user.UserFacebook;
import com.biperf.core.service.facebook.FacebookService;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.utils.JsonUtils;
import com.biperf.core.utils.UrlReader;

public class FacebookServiceImpl implements FacebookService
{
  private static final Log log = LogFactory.getLog( FacebookServiceImpl.class );

  private MessageFormat facebookApiUrlFormatter = new MessageFormat( "https://graph.facebook.com/{0}/{1}" );

  private ParticipantService participantService;
  private SystemVariableService systemVariableService;
  private UrlReader urlReader;

  public boolean postMessageToWall( Long userId, String message )
  {
    // get the user
    Participant user = participantService.getParticipantById( userId );

    if ( user.getUserFacebook() == null || message == null || message.trim().length() == 0 )
    {
      return false;
    }

    // add the message
    String msg = message.trim();
    if ( msg.length() > 420 )
    {
      msg = msg.substring( 0, 420 );
    }

    // create the URL using
    String url = getFacebookApiUrl( "me", "feed" );

    // create the parameters
    Map<String, String> params = new HashMap<String, String>();
    params.put( "access_token", user.getUserFacebook().getAccessToken() );
    params.put( "message", msg );

    boolean success = false;
    String response = "default";

    // post to the wall
    try
    {
      response = urlReader.postAndReturnString( url, params );

      // a successful response will look like this: {"id":"100000845967082_130053980340642"}
      if ( response.indexOf( "\"id\"" ) > -1 )
      {
        success = true;
      }
    }
    catch( Throwable t )
    {
      // apparently an unsuccessful response returns an HTTP status code of 500, which is caught
      // here.
      success = false;
    }

    return success;
  }

  protected String getFacebookApiUrl( String objectId, String method )
  {
    return facebookApiUrlFormatter.format( new String[] { objectId, method } );
  }

  protected void logFacebookError( Long userId, String method, JSONObject response )
  {
    StringBuilder sb = new StringBuilder();
    sb.append( "\n**************************************************************\nERROR in " ).append( this.getClass().getName() ).append( " on " ).append( new Date().toString() )
        .append( "\n**************************************************************" );

    sb.append( "\nMethod: " ).append( method );
    sb.append( "\nUser ID: " ).append( userId );
    sb.append( "\nFacebook error code: " ).append( JsonUtils.getStringValueFrom( response, "error_code" ) );
    sb.append( "\nFacebook error message: " ).append( JsonUtils.getStringValueFrom( response, "error_msg" ) );
    sb.append( "\nRequest args:\n " ).append( JsonUtils.getStringValueFrom( response, "request_args" ) );
    sb.append( "\n**************************************************************\nEND ERROR " );
    sb.append( "\n**************************************************************\n" );

    // log it
    log.error( sb.toString() );
  }

  public String getApiKey()
  {
    return getFacebookSetupValue( SystemVariableService.FACEBOOK_API_KEY );
  }

  public String getAppId()
  {
    return getFacebookSetupValue( SystemVariableService.FACEBOOK_APP_ID );
  }

  public String getAppSecret()
  {
    return getFacebookSetupValue( SystemVariableService.FACEBOOK_APP_SECRET );
  }

  private String getFacebookSetupValue( String systemVariable )
  {
    String setupValue = systemVariableService.getPropertyByName( systemVariable ).getStringVal();

    if ( setupValue == null || setupValue.trim().length() == 0 )
    {
      log.error( "\n\n***************************\n* WARNING\n***************************\nFacebook setup value for organization ID: " + systemVariable
          + "] is null or zero length string!\n***************************\n* END WARNING \n***************************\n\n" );
    }

    return setupValue;
  }

  public String getFacebookLoginUrl( String redirectUrl )
  {
    final MessageFormat formatter = new MessageFormat( "http://graph.facebook.com/oauth/authorize?client_id={0}&redirect_uri={1}&scope=publish_stream,offline_access&display=touch" );
    return formatter.format( new String[] { getAppId(), redirectUrl } );
  }

  public void updateUserFacebookInfo( Long userId, String code, String redirectUrl )
  {
    try
    {
      // get the user
      Participant user = participantService.getParticipantById( userId );

      // get the access_token using the code
      String accessToken = getFacebookAccessToken( code, redirectUrl );

      // use the access_token to get the user's facebook id, in case we need it later
      String facebookId = getFacebookUserIdFor( accessToken );

      // create the UserFacebook and add it to the user
      UserFacebook userFacebook = new UserFacebook();
      userFacebook.setUserId( facebookId );
      userFacebook.setAccessToken( accessToken );
      user.setUserFacebook( userFacebook );
    }
    catch( Throwable t )
    {
      StringBuilder sb = new StringBuilder( "\n\n******************************************************************" );
      sb.append( "\n* ERROR in FacebookServiceImpl#updateUserFacebookInfo" );
      sb.append( "\n******************************************************************" );
      sb.append( "\nUser ID: " ).append( userId );
      sb.append( "\nCode: " ).append( code );
      sb.append( "\nRedirect URL: " ).append( redirectUrl );
      sb.append( "\n******************************************************************" );
      sb.append( "\n* END ERROR" );
      sb.append( "\n******************************************************************" );
      log.error( sb.toString() );
    }
  }

  protected String getFacebookAccessToken( String code, String redirectUrl )
  {
    final String ACCESS_CODE_URL = "https://graph.facebook.com/oauth/access_token";

    String appId = getAppId();
    String secret = getAppSecret();

    Map<String, String> params = new HashMap<String, String>();
    params.put( "client_id", appId );
    params.put( "redirect_uri", redirectUrl );
    params.put( "client_secret", secret );
    params.put( "code", code );
    try
    {
      // response is in the form access_token=XXXXXXXX
      String response = urlReader.postAndReturnString( ACCESS_CODE_URL, params );

      // extract the actual accesst_token value
      String[] tokens = response.split( "=" );
      return tokens[1];
    }
    catch( Throwable t )
    {
      log.error( "\n********************************************\n* ERROR in method getFacebookAccessTokenFor \n********************************************" + "\nmessage: " + t.getMessage()
          + "\n********************************************\n" );
      t.printStackTrace();
    }
    return null;
  }

  protected String getFacebookUserIdFor( String accessToken )
  {
    final String USER_INFO_URL = "https://graph.facebook.com/me";

    Map<String, String> params = new HashMap<String, String>();
    params.put( "access_token", accessToken );

    try
    {
      String json = urlReader.asString( USER_INFO_URL, params );

      JSONObject jsonObject = new JSONObject( json );
      return JsonUtils.getStringValueFrom( jsonObject, "id" );
    }
    catch( Throwable t )
    {
      log.error( "\n********************************************\n* ERROR in method getFacebookUserIdFor \n********************************************" + "\nmessage: " + t.getMessage()
          + "\n********************************************\n" );
    }
    return null;
  }

  public void setSystemVariableService( SystemVariableService systemVariableService )
  {
    this.systemVariableService = systemVariableService;
  }

  public void setUrlReader( UrlReader urlReader )
  {
    this.urlReader = urlReader;
  }

  public void setParticipantService( ParticipantService participantService )
  {
    this.participantService = participantService;
  }

}

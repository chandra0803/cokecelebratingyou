
package com.biperf.core.service.twitter.impl;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.dao.participant.UserTwitterDAO;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.user.UserTwitter;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.exception.ServiceErrorExceptionNoRollback;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.service.twitter.TwitterService;

import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.http.AccessToken;
import twitter4j.http.RequestToken;

public class TwitterServiceImpl implements TwitterService
{
  private static final Log log = LogFactory.getLog( TwitterServiceImpl.class );

  private TwitterFactory twitterFactory; // TwitterFactory is thread-safe
  private ParticipantService participantService;
  private SystemVariableService systemVariableService;
  private UserTwitterDAO userTwitterDAO;

  public void tweet( long userId, String tweet ) throws ServiceErrorException
  {
    if ( StringUtils.isEmpty( tweet ) )
    {
      return;
    }

    tweet = tweet.trim();

    if ( tweet.length() > 140 )
    {
      tweet = tweet.substring( 0, 140 );// sorry, can only send 140 characters
    }

    Participant user = participantService.getParticipantById( userId );

    // bail if the user has no twitter information saved, or doesn't have the access token
    UserTwitter userTwitter = user.getUserTwitter();
    if ( userTwitter == null || StringUtils.isEmpty( userTwitter.getAccessToken() ) || StringUtils.isEmpty( userTwitter.getAccessTokenSecret() ) )
    {
      return;
    }

    AccessToken accessToken = new AccessToken( userTwitter.getAccessToken(), userTwitter.getAccessTokenSecret() );
    Twitter twitter = getOAuthAuthorizedTwitterClient( accessToken );

    // do the tweet!
    try
    {
      twitter.updateStatus( tweet );
    }
    catch( Throwable t )
    {
      logErrorMessage( userId, "tweet", t.getMessage() );
      throw new ServiceErrorException( "Error TwitterServiceImpl#getAuthorizationUrlFor()" );
    }
  }

  private void deleteTwitterObject( Long twitterId )
  {
    userTwitterDAO.deleteUserTwitterById( twitterId );
  }

  public void deleteTwitterAuthorizationFor( Long userId )
  {
    Participant user = participantService.getParticipantById( userId );
    if ( user != null )
    {
      if ( user.getUserTwitter() != null )
      {
        deleteTwitterObject( user.getUserTwitter().getId() );
      }
      user.setUserTwitter( null );
    }
  }

  public String startTwitterAuthorizationFor( Long userId, String callbackUrl ) throws ServiceErrorException
  {
    Twitter twitter = getTwitterClient();

    RequestToken requestToken = null;
    try
    {
      if ( StringUtils.isEmpty( callbackUrl ) )
      {
        requestToken = twitter.getOAuthRequestToken();
      }
      else
      {
        requestToken = twitter.getOAuthRequestToken( callbackUrl );
      }
    }
    catch( Throwable t )
    {
      logErrorMessage( userId, "startTwitterAuthorizationFor", t.getMessage() );
      throw new ServiceErrorException( "Error TwitterServiceImpl#getAuthorizationUrlFor()" );
    }

    // store the request token info for the user, for later use
    Participant user = participantService.getParticipantById( userId );
    UserTwitter userTwitter = new UserTwitter();
    userTwitter.setRequestToken( requestToken.getToken() );
    userTwitter.setRequestTokenSecret( requestToken.getTokenSecret() );
    user.setUserTwitter( userTwitter );

    return requestToken.getAuthorizationURL();
  }

  public String startTwitterAuthorizationFor( Long userId ) throws ServiceErrorException
  {
    return startTwitterAuthorizationFor( userId, null );
  }

  public void completeTwitterAuthorizationFor( Long userId, String pin ) throws ServiceErrorException
  {
    Twitter twitter = getTwitterClient();
    Participant user = participantService.getParticipantById( userId );

    UserTwitter userTwitter = user.getUserTwitter();
    if ( userTwitter == null )
    {
      final String ERROR_MESSAGE = "Invalid state; I received a request to complete" + " the user's Twitter authorization, but the user's USER_TWITTER record not available.";
      logErrorMessage( userId, "completeTwitterAuthorizationFor", ERROR_MESSAGE );

      throw new ServiceErrorExceptionNoRollback( "Error TwitterServiceImpl#completeTwitterAuthorizationFor()" );
    }

    RequestToken requestToken = new RequestToken( userTwitter.getRequestToken(), userTwitter.getRequestTokenSecret() );
    AccessToken accessToken = null;
    try
    {
      accessToken = twitter.getOAuthAccessToken( requestToken, pin );
    }
    catch( Throwable t )
    {
      logErrorMessage( userId, "completeTwitterAuthorizationFor", t.getMessage() );

      if ( userTwitter != null )
      {
        deleteTwitterObject( userTwitter.getId() );
        user.setUserTwitter( null );
      }

      throw new ServiceErrorExceptionNoRollback( "Error TwitterServiceImpl#completeTwitterAuthorizationFor()" );
    }

    // update the UserTwitter
    userTwitter.setRequestToken( null );
    userTwitter.setRequestTokenSecret( null );
    userTwitter.setAccessToken( accessToken.getToken() );
    userTwitter.setAccessTokenSecret( accessToken.getTokenSecret() );
  }

  protected Twitter getTwitterClient()
  {
    Twitter twitter = twitterFactory.getInstance();
    twitter.setOAuthConsumer( getTwitterConsumerKey(), getTwitterConsumerSecret() );

    return twitter;
  }

  protected Twitter getOAuthAuthorizedTwitterClient( AccessToken accessToken )
  {
    return twitterFactory.getOAuthAuthorizedInstance( getTwitterConsumerKey(), getTwitterConsumerSecret(), accessToken );
  }

  private String getTwitterSetupValue( String systemVariable )
  {
    String setupValue = systemVariableService.getPropertyByName( systemVariable ).getStringVal();

    if ( setupValue == null || setupValue.trim().length() == 0 )
    {
      log.error( "\n\n***************************\n* WARNING\n***************************\nTwitter setup value for organization ID: " + systemVariable
          + "] is null or zero length string!\n***************************\n* END WARNING \n***************************\n\n" );
    }

    return setupValue;
  }

  protected String getTwitterConsumerKey()
  {
    return getTwitterSetupValue( SystemVariableService.TWITTER_CONSUMER_KEY );
  }

  protected String getTwitterConsumerSecret()
  {
    return getTwitterSetupValue( SystemVariableService.TWITTER_CONSUMER_SECRET );
  }

  private void logErrorMessage( Long userId, String methodName, String errorMessage )
  {
    StringBuilder sb = new StringBuilder( "\n\n******************************************************************" );
    sb.append( "\n* ERROR in TwitterServiceImpl#" ).append( methodName );
    sb.append( "\n******************************************************************" );
    sb.append( "\nUser ID: " ).append( userId );
    sb.append( "\nError Message: " ).append( errorMessage );
    sb.append( "\n******************************************************************" );
    sb.append( "\n* END ERROR" );
    sb.append( "\n******************************************************************" );
    log.error( sb.toString() );
  }

  public void setTwitterFactory( TwitterFactory twitterFactory )
  {
    this.twitterFactory = twitterFactory;
  }

  public void setSystemVariableService( SystemVariableService systemVariableService )
  {
    this.systemVariableService = systemVariableService;
  }

  public void setParticipantService( ParticipantService participantService )
  {
    this.participantService = participantService;
  }

  public void setUserTwitterDAO( UserTwitterDAO userTwitterDAO )
  {
    this.userTwitterDAO = userTwitterDAO;
  }

}

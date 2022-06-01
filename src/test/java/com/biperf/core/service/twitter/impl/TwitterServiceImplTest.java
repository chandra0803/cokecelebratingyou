
package com.biperf.core.service.twitter.impl;

import org.jmock.Mock;
import org.jmock.MockObjectTestCase;

import com.biperf.core.dao.participant.UserTwitterDAO;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.system.PropertySetItem;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.system.SystemVariableService;

import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

public class TwitterServiceImplTest extends MockObjectTestCase
{
  private TwitterServiceImpl service;
  private Mock mockUserTwitterDao;
  private Mock mockParticipantService;
  private Mock mockSystemVariableService;

  private static final String REQUEST_TOKEN = "requestToken";
  private static final String REQUEST_TOKEN_SECRET = "requetTokenSecret";

  private static final String ACCESS_TOKEN = "accessToken";
  private static final String ACCESS_TOKEN_SECRET = "requetTokenSecret";

  private static final String PIN = "1234";

  private Participant user;
  private static final Long USER_ID = new Long( 2345345 );

  public void setUp()
  {
    service = new TwitterServiceImpl();

    mockUserTwitterDao = new Mock( UserTwitterDAO.class );
    service.setUserTwitterDAO( (UserTwitterDAO)mockUserTwitterDao.proxy() );

    mockParticipantService = new Mock( ParticipantService.class );
    service.setParticipantService( (ParticipantService)mockParticipantService.proxy() );

    mockSystemVariableService = new Mock( SystemVariableService.class );
    service.setSystemVariableService( (SystemVariableService)mockSystemVariableService.proxy() );

    service.setTwitterFactory( new TwitterFactory() );

    user = new Participant();
    user.setId( USER_ID );
  }

  public void testCompleteTwitterAuthorizationFor_Failure_InvalidState_NullUserTwitter() throws TwitterException, Exception
  {
    mockParticipantService.expects( once() ).method( "getParticipantById" ).with( same( USER_ID ) ).will( returnValue( user ) );

    PropertySetItem systemVariableConsumerKey = new PropertySetItem();
    systemVariableConsumerKey.setStringVal( ACCESS_TOKEN );
    systemVariableConsumerKey.setKey( SystemVariableService.TWITTER_CONSUMER_KEY );
    systemVariableConsumerKey.setEntityName( SystemVariableService.TWITTER_CONSUMER_KEY );

    PropertySetItem systemVariableConsumerSecret = new PropertySetItem();
    systemVariableConsumerSecret.setStringVal( ACCESS_TOKEN_SECRET );
    systemVariableConsumerSecret.setKey( SystemVariableService.TWITTER_CONSUMER_SECRET );
    systemVariableConsumerSecret.setEntityName( SystemVariableService.TWITTER_CONSUMER_SECRET );

    mockSystemVariableService.expects( once() ).method( "getPropertyByName" ).with( same( SystemVariableService.TWITTER_CONSUMER_KEY ) ).will( returnValue( systemVariableConsumerKey ) );

    mockSystemVariableService.expects( once() ).method( "getPropertyByName" ).with( same( SystemVariableService.TWITTER_CONSUMER_SECRET ) ).will( returnValue( systemVariableConsumerSecret ) );

    // set up the UserTwitter
    user.setUserTwitter( null );

    // do the test!
    try
    {
      service.completeTwitterAuthorizationFor( USER_ID, PIN );

      // fail if we got this far; should have thrown an exception
      fail( "should have thrown an exception" );
    }
    catch( Exception e )
    {
      // expected
    }

    // verify
    mockParticipantService.verify();
    mockSystemVariableService.verify();
  }

  public void testTweet_Fail_EmptyTweet() throws TwitterException, Exception
  {
    // set up the status return for the mock tweet
    final String TWEET = "";

    // OK, run the test!!
    service.tweet( USER_ID, TWEET );
  }

}

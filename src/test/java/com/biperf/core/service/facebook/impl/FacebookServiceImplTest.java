
package com.biperf.core.service.facebook.impl;

import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;

import java.util.Map;

import org.easymock.classextension.EasyMock;

import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.system.PropertySetItem;
import com.biperf.core.domain.user.UserFacebook;
import com.biperf.core.service.BaseServiceTest;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.utils.UrlReader;

public class FacebookServiceImplTest extends BaseServiceTest
{
  private FacebookServiceImpl service;
  private ParticipantService mockParticipantService;
  private SystemVariableService mockSystemVariableService;
  private UrlReader mockUrlReader;

  private Participant user;

  private static final Long USER_ID = new Long( 1 );
  private static final String FACEBOOK_APP_ID = "110793782286572";
  private static final String FACEBOOK_SECRET_KEY = "110793782286572";
  private static final String ACCESS_TOKEN = "110793782286572|d0849247dd745d6f7c675d21-100000845967082|scTSIxipXFi_AMoP0W5FNOYtZPg.";
  private static final String CODE = "d0849247dd745d6f7c675d21-100000845967082|YETmFkOa8iSs1MyuxlsG-uB11tM.";

  public void setUp()
  {
    service = new FacebookServiceImpl();

    mockParticipantService = createMock( ParticipantService.class );
    service.setParticipantService( mockParticipantService );

    mockUrlReader = createMock( UrlReader.class );
    service.setUrlReader( mockUrlReader );

    mockSystemVariableService = createMock( SystemVariableService.class );
    service.setSystemVariableService( mockSystemVariableService );

    UserFacebook facebook = new UserFacebook();
    facebook.setAccessToken( ACCESS_TOKEN );

    user = new Participant();
    user.setId( USER_ID );
    user.setUserFacebook( facebook );
  }

  public void testPostMessageToWall_Mock() throws Exception
  {
    // set up the initial call to the mockApplicationUserDao
    expect( mockParticipantService.getParticipantById( EasyMock.eq( USER_ID ) ) ).andReturn( user );
    replay( mockParticipantService );

    String response = "{\"id\":\"100000845967082_130053980340642\"}";
    expect( mockUrlReader.postAndReturnString( anyObject(), anyObject() ) ).andReturn( response );
    replay( mockUrlReader );

    // create a default message
    final String MESSAGE = "A new message from the G4 application.";

    // post it!!
    boolean success = service.postMessageToWall( USER_ID, MESSAGE );
    assertTrue( success );
  }

  public void testGetFacebookLoginUrl()
  {
    final String REDIRECT_URL = "http://beacon2gf.recognitionpurl.com";

    // set up the call to the mockOrganizationVariableService
    PropertySetItem systemVariableAppId = new PropertySetItem();
    systemVariableAppId.setStringVal( FACEBOOK_APP_ID );
    systemVariableAppId.setKey( SystemVariableService.FACEBOOK_APP_ID );
    systemVariableAppId.setEntityName( SystemVariableService.FACEBOOK_APP_ID );

    expect( mockSystemVariableService.getPropertyByName( SystemVariableService.FACEBOOK_APP_ID ) ).andReturn( systemVariableAppId );
    replay( mockSystemVariableService );

    // do the test!!!
    String actualUrl = service.getFacebookLoginUrl( REDIRECT_URL );

    // asserts
    String expectedUrl = "http://graph.facebook.com/oauth/authorize?client_id=" + FACEBOOK_APP_ID + "&redirect_uri=" + REDIRECT_URL + "&scope=publish_stream,offline_access&display=touch";
    assertEquals( expectedUrl, actualUrl );
  }

  public void testGetFacebookAccessTokenFor_Mock() throws Exception
  {
    // set up the calls to the mockOrganizationVariableService to get the facebook app id and secret
    PropertySetItem systemVariableAppId = new PropertySetItem();
    systemVariableAppId.setStringVal( FACEBOOK_APP_ID );
    systemVariableAppId.setKey( SystemVariableService.FACEBOOK_APP_ID );
    systemVariableAppId.setEntityName( SystemVariableService.FACEBOOK_APP_ID );

    PropertySetItem systemVariableSecretKey = new PropertySetItem();
    systemVariableSecretKey.setStringVal( FACEBOOK_SECRET_KEY );
    systemVariableSecretKey.setKey( SystemVariableService.FACEBOOK_APP_SECRET );
    systemVariableSecretKey.setEntityName( SystemVariableService.FACEBOOK_APP_SECRET );

    expect( mockSystemVariableService.getPropertyByName( SystemVariableService.FACEBOOK_APP_ID ) ).andReturn( systemVariableAppId );
    expect( mockSystemVariableService.getPropertyByName( SystemVariableService.FACEBOOK_APP_SECRET ) ).andReturn( systemVariableSecretKey );
    replay( mockSystemVariableService );

    String response = "access_token=" + ACCESS_TOKEN;
    expect( mockUrlReader.postAndReturnString( anyObject(), anyObject() ) ).andReturn( response );
    replay( mockUrlReader );

    // run the test!!
    final String REDIRECT_URL = "http://beacon2gf.recognitionpurl.com";
    String accessToken = service.getFacebookAccessToken( CODE, REDIRECT_URL );

    // verify and assert
    assertNotNull( accessToken );
    assertEquals( ACCESS_TOKEN, accessToken );
  }

  @SuppressWarnings( { "unchecked", "rawtypes" } )
  public void testGetFacebookUserIdFor_Mock() throws Exception
  {
    String responseJson = "{id=100000845967082}";
    expect( mockUrlReader.asString( anyObject(), (Map)anyObject() ) ).andReturn( responseJson );
    replay( mockUrlReader );

    // run the test!
    String facebookUserId = service.getFacebookUserIdFor( ACCESS_TOKEN );

    // verify and assert
    assertNotNull( facebookUserId );
    assertEquals( "100000845967082", facebookUserId );
  }

  @SuppressWarnings( { "unchecked", "rawtypes" } )
  public void testGetFacebookUserIdFor_Live() throws Exception
  {
    String responseJson = "{id=100000845967082}";
    expect( mockUrlReader.asString( anyObject(), (Map)anyObject() ) ).andReturn( responseJson );
    replay( mockUrlReader );

    String userId = service.getFacebookUserIdFor( ACCESS_TOKEN );

    assertNotNull( userId );
    assertEquals( "100000845967082", userId );
  }

  public void testPostMessageToWall_Live() throws Exception
  {
    // set up the initial call to the mockApplicationUserDao
    expect( mockParticipantService.getParticipantById( USER_ID ) ).andReturn( user );
    replay( mockParticipantService );

    String response = "{\"id\":\"100000845967082_130053980340642\"}";
    expect( mockUrlReader.postAndReturnString( anyObject(), anyObject() ) ).andReturn( response );
    replay( mockUrlReader );

    // create a default message
    final String MESSAGE = "A new message from the RAVE application using Graph API by user " + System.getProperty( "user.name" ) + " on " + new java.util.Date() + ".";

    // post it, using a real UrlReader!!
    boolean success = service.postMessageToWall( USER_ID, MESSAGE );

    assertTrue( success );
  }

  public void testPostMessageToWall_LiveButFails() throws Exception
  {
    // set an invalid session key on the user to cause a failure
    user.getUserFacebook().setAccessToken( ACCESS_TOKEN + "blah" );

    // set up the initial call to the mockApplicationUserDao
    expect( mockParticipantService.getParticipantById( USER_ID ) ).andReturn( user );
    replay( mockParticipantService );

    String response = "{\"error\":\"message\"}";
    expect( mockUrlReader.postAndReturnString( anyObject(), anyObject() ) ).andReturn( response );
    replay( mockUrlReader );

    // create a default message
    final String MESSAGE = "A new message from the RAVE application using Graph API by user " + System.getProperty( "user.name" ) + " on " + new java.util.Date() + ".";

    // post it, using a real UrlReader!!
    boolean success = service.postMessageToWall( USER_ID, MESSAGE );

    // verify
    assertFalse( success );
  }

}

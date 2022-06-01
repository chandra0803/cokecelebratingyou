
package com.biperf.core.process;

import static org.easymock.EasyMock.anyLong;
import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.replay;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.biperf.core.builders.BuilderUtil;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.system.PropertySetItem;
import com.biperf.core.service.email.MailingService;
import com.biperf.core.service.message.MessageService;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.participant.PasswordResetService;
import com.biperf.core.service.process.ProcessInvocationService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.strategy.usertoken.UserTokenType;

public class AdminTestWelcomeEmailProcessTest
{
  AdminTestWelcomeEmailProcess process;
  SystemVariableService systemVariableService;
  ParticipantService participantService;
  PasswordResetService passwordResetService;
  MessageService messageService;
  MailingService mailingService;
  ProcessInvocationService processInvocationService;

  String userName;
  String recipientLocale;

  @Before
  public void setup() throws Exception
  {
    userName = "johndoe";
    recipientLocale = "en_US";
    process = new AdminTestWelcomeEmailProcess();
    systemVariableService = createMock( SystemVariableService.class );
    participantService = createMock( ParticipantService.class );
    passwordResetService = createMock( PasswordResetService.class );
    messageService = createMock( MessageService.class );
    mailingService = createMock( MailingService.class );
    processInvocationService = createMock( ProcessInvocationService.class );

    process.setUserName( userName );
    process.setRecipientLocale( recipientLocale );
    process.setSystemVariableService( systemVariableService );
    process.setParticipantService( participantService );
    process.setPasswordResetService( passwordResetService );
    process.setMessageService( messageService );
    process.setMailingService( mailingService );
    process.setProcessInvocationService( processInvocationService );
  }

  @After
  public void tearDown()
  {
    process = null;
    systemVariableService = null;
    participantService = null;
    passwordResetService = null;
    messageService = null;
    mailingService = null;
    processInvocationService = null;
  }

  @Test
  public void testProcessPasswordShouldUseRegex()
  {
    setupSystemVariables( true );

    Participant participant = BuilderUtil.buildParticipant();

    expect( participantService.getParticipantByUserName( userName ) ).andReturn( participant );

    expect( passwordResetService.generateTokenAndSave( participant.getId(), UserTokenType.WELCOME_EMAIL ) ).andReturn( BuilderUtil.buildIssuedUserTokenForEmail( participant ) );

    expect( messageService.getMessageByCMAssetCode( MessageService.WELCOME_EMAIL_MESSAGE_CM_ASSET_CODE ) ).andReturn( BuilderUtil.buildMessage( MessageService.WELCOME_EMAIL_MESSAGE_CM_ASSET_CODE ) );

    expect( mailingService.submitMailing( anyObject(), anyObject(), anyLong() ) ).andReturn( BuilderUtil.buildMailing( MessageService.WELCOME_EMAIL_MESSAGE_CM_ASSET_CODE ) );

    replay( systemVariableService, participantService, passwordResetService, messageService, mailingService );

    process.onExecute();
  }

  @Test
  public void testProcessPasswordShouldNotUseRegex()
  {
    setupSystemVariables( false );

    Participant participant = BuilderUtil.buildParticipant();

    expect( participantService.getParticipantByUserName( userName ) ).andReturn( participant );

    expect( passwordResetService.generateTokenAndSave( participant.getId(), UserTokenType.WELCOME_EMAIL ) ).andReturn( BuilderUtil.buildIssuedUserTokenForEmail( participant ) );

    expect( messageService.getMessageByCMAssetCode( MessageService.WELCOME_EMAIL_MESSAGE_CM_ASSET_CODE ) ).andReturn( BuilderUtil.buildMessage( MessageService.WELCOME_EMAIL_MESSAGE_CM_ASSET_CODE ) );

    expect( mailingService.submitMailing( anyObject(), anyObject(), anyLong() ) ).andReturn( BuilderUtil.buildMailing( MessageService.WELCOME_EMAIL_MESSAGE_CM_ASSET_CODE ) );

    replay( systemVariableService, participantService, passwordResetService, messageService, mailingService );

    process.onExecute();
  }

  @Test( expected = NullPointerException.class )
  public void testProcessPasswordShouldUseRegexNull()
  {
    setupSystemVariables( null );

    Participant participant = BuilderUtil.buildParticipant();

    expect( participantService.getParticipantByUserName( userName ) ).andReturn( participant );

    expect( passwordResetService.generateTokenAndSave( participant.getId(), UserTokenType.WELCOME_EMAIL ) ).andReturn( BuilderUtil.buildIssuedUserTokenForEmail( participant ) );

    expect( messageService.getMessageByCMAssetCode( MessageService.WELCOME_EMAIL_MESSAGE_CM_ASSET_CODE ) ).andReturn( BuilderUtil.buildMessage( MessageService.WELCOME_EMAIL_MESSAGE_CM_ASSET_CODE ) );

    expect( mailingService.submitMailing( anyObject(), anyObject(), anyLong() ) ).andReturn( BuilderUtil.buildMailing( MessageService.WELCOME_EMAIL_MESSAGE_CM_ASSET_CODE ) );

    replay( systemVariableService, participantService, passwordResetService, messageService, mailingService );

    process.onExecute();
  }

  @Test
  public void testProcessWithUserNameNotFound()
  {
    setupSystemVariables( true );

    expect( participantService.getParticipantByUserName( userName ) ).andReturn( null );

    processInvocationService.addComment( anyLong(), anyObject() );
    expectLastCall().once();

    replay( systemVariableService, participantService, processInvocationService );

    process.onExecute();

  }

  private void setupSystemVariables( Boolean shouldUseRegex )
  {
    PropertySetItem item = new PropertySetItem();

    if ( shouldUseRegex != null )
    {
      item.setBooleanVal( shouldUseRegex );
      item.setKey( SystemVariableService.PASSWORD_SHOULD_USE_REGEX );
      item.setEntityName( SystemVariableService.PASSWORD_SHOULD_USE_REGEX );
      expect( systemVariableService.getPropertyByName( SystemVariableService.PASSWORD_SHOULD_USE_REGEX ) ).andReturn( item );
    }
    else
    {
      expect( systemVariableService.getPropertyByName( SystemVariableService.PASSWORD_SHOULD_USE_REGEX ) ).andReturn( null );
    }

    item = new PropertySetItem();
    item.setStringVal( "unittest@biworldwide.com" );
    item.setKey( SystemVariableService.SYSTEM_SENDER_EMAIL_ADDRESS );
    item.setEntityName( SystemVariableService.SYSTEM_SENDER_EMAIL_ADDRESS );
    expect( systemVariableService.getPropertyByName( SystemVariableService.SYSTEM_SENDER_EMAIL_ADDRESS ) ).andReturn( item );

    item = new PropertySetItem();
    item.setStringVal( "https://www.biworldwide.com" );
    item.setKey( SystemVariableService.SITE_URL_PREFIX );
    item.setEntityName( SystemVariableService.SITE_URL_PREFIX );
    expect( systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ) ).andReturn( item );

    item = new PropertySetItem();
    item.setStringVal( "BI Worldwide" );
    item.setKey( SystemVariableService.CLIENT_NAME );
    item.setEntityName( SystemVariableService.CLIENT_NAME );
    expect( systemVariableService.getPropertyByName( SystemVariableService.CLIENT_NAME ) ).andReturn( item );

    item = new PropertySetItem();
    item.setStringVal( "/login.do" );
    item.setKey( SystemVariableService.CLIENT_URL );
    item.setEntityName( SystemVariableService.CLIENT_URL );
    expect( systemVariableService.getPropertyByName( SystemVariableService.CLIENT_URL ) ).andReturn( item );

    item = new PropertySetItem();
    item.setStringVal( "/contactUsPublic.do" );
    item.setKey( SystemVariableService.CLIENT_CONTACT_URL );
    item.setEntityName( SystemVariableService.CLIENT_CONTACT_URL );
    expect( systemVariableService.getPropertyByName( SystemVariableService.CLIENT_CONTACT_URL ) ).andReturn( item );

  }

}


package com.biperf.core.service.email.impl;

import static org.easymock.EasyMock.anyLong;
import static org.easymock.EasyMock.anyObject;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.easymock.classextension.EasyMock;

import com.biperf.core.builders.BuilderUtil;
import com.biperf.core.domain.enums.EmailAddressType;
import com.biperf.core.domain.enums.LanguageType;
import com.biperf.core.domain.enums.MailingType;
import com.biperf.core.domain.enums.MockPickListFactory;
import com.biperf.core.domain.enums.PickListItem;
import com.biperf.core.domain.enums.UserType;
import com.biperf.core.domain.enums.VerificationStatusType;
import com.biperf.core.domain.mailing.Mailing;
import com.biperf.core.domain.mailing.MailingRecipient;
import com.biperf.core.domain.message.Message;
import com.biperf.core.domain.system.PropertySetItem;
import com.biperf.core.domain.user.User;
import com.biperf.core.domain.user.UserEmailAddress;
import com.biperf.core.service.BaseServiceTest;
import com.biperf.core.service.email.MailingService;
import com.biperf.core.service.message.MessageService;
import com.biperf.core.service.system.SystemVariableService;

public class WelComeEmailServiceImplTest extends BaseServiceTest
{
  private WelcomeEmailServiceImpl welcomeEmailServiceTest = new WelcomeEmailServiceImpl();
  private SystemVariableService systemVariableServiceMock;
  private MessageService messageServiceMock = null;

  private static final String SYSTEM_EMAIL_ADDRESS = "notification@biworldwide.com";

  private static final String CLIENT_NAME = "BI worldWide";
  private static final String CLIENT_URL = "/login.do";
  private static final String SITE_URL_PREFIX = "https://www.biworldwide.com";
  private static final String CLIENT_CONTACT_URL = "/contactUsPublic.do";

  MailingService mailingService;

  @Override
  public void setUp() throws Exception
  {
    messageServiceMock = EasyMock.createMock( MessageService.class );
    systemVariableServiceMock = EasyMock.createMock( SystemVariableService.class );
    mailingService = EasyMock.createMock( MailingService.class );
    welcomeEmailServiceTest.setSystemVariableService( systemVariableServiceMock );
    welcomeEmailServiceTest.setMailingService( mailingService );
    welcomeEmailServiceTest.setMessageService( messageServiceMock );
  }

  public void testsendWelcomeEmail()
  {
    User user = buildUser();
    User runByUser = buildUser();
    boolean isUniqueEmail = true;
    String passwordToken = "token-123";

    Message message = new Message();
    message.setId( new Long( 1 ) );
    message.setName( "TestServiceMessage" );
    message.setCmAssetCode( "message.something.something" );

    Mailing mailing = buildMailing( null, (MailingType)MockPickListFactory.getMockPickListItem( MailingType.class, MailingType.WELCOME_LOGIN_EMAIL ), new Date().getTime() );

    PropertySetItem systemVariableSystemEmailAddress = new PropertySetItem();
    systemVariableSystemEmailAddress.setStringVal( SYSTEM_EMAIL_ADDRESS );
    systemVariableSystemEmailAddress.setKey( SystemVariableService.SYSTEM_EMAIL_ADDRESS );
    systemVariableSystemEmailAddress.setEntityName( SystemVariableService.SYSTEM_EMAIL_ADDRESS );

    PropertySetItem SystemVariableSiteUrl = new PropertySetItem();
    SystemVariableSiteUrl.setStringVal( SITE_URL_PREFIX );
    SystemVariableSiteUrl.setKey( SystemVariableService.SITE_URL_PREFIX );
    SystemVariableSiteUrl.setEntityName( SystemVariableService.SITE_URL_PREFIX );

    PropertySetItem systeVariableClientName = new PropertySetItem();
    systeVariableClientName.setStringVal( CLIENT_NAME );
    systeVariableClientName.setKey( SystemVariableService.CLIENT_NAME );
    systeVariableClientName.setEntityName( SystemVariableService.CLIENT_NAME );

    PropertySetItem systemVariableClientUrl = new PropertySetItem();
    systemVariableClientUrl.setStringVal( CLIENT_URL );
    systemVariableClientUrl.setKey( SystemVariableService.CLIENT_URL );
    systemVariableClientUrl.setEntityName( SystemVariableService.CLIENT_URL );

    PropertySetItem systemVariableClientContactUrl = new PropertySetItem();
    systemVariableClientUrl.setStringVal( CLIENT_CONTACT_URL );
    systemVariableClientUrl.setKey( SystemVariableService.CLIENT_CONTACT_URL );
    systemVariableClientUrl.setEntityName( SystemVariableService.CLIENT_CONTACT_URL );

    EasyMock.expect( messageServiceMock.getMessageByCMAssetCode( mailing.getMessage().getCmAssetCode() ) ).andReturn( mailing.getMessage() );
    EasyMock.replay( messageServiceMock );

    EasyMock.expect( systemVariableServiceMock.getPropertyByName( SystemVariableService.SYSTEM_EMAIL_ADDRESS ) ).andReturn( systemVariableSystemEmailAddress );
    EasyMock.expect( systemVariableServiceMock.getPropertyByName( SystemVariableService.CLIENT_NAME ) ).andReturn( systeVariableClientName );
    EasyMock.expect( systemVariableServiceMock.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ) ).andReturn( SystemVariableSiteUrl ).times( 2 );
    EasyMock.expect( systemVariableServiceMock.getPropertyByName( SystemVariableService.CLIENT_CONTACT_URL ) ).andReturn( systemVariableClientContactUrl );
    EasyMock.expect( systemVariableServiceMock.getPropertyByName( SystemVariableService.CLIENT_URL ) ).andReturn( systemVariableClientUrl );
    EasyMock.replay( systemVariableServiceMock );

    EasyMock.expect( mailingService.buildUserTokenURL( "token-123" ) ).andReturn( "token-123" );
    EasyMock.expect( mailingService.submitMailing( anyObject(), anyObject(), anyLong() ) ).andReturn( BuilderUtil.buildMailing( MessageService.WELCOME_EMAIL_MESSAGE_CM_ASSET_CODE ) );
    EasyMock.replay( mailingService );
    welcomeEmailServiceTest.sendNewWelcomeEmail( user, mailing.getMessage(), passwordToken, runByUser, isUniqueEmail );
    EasyMock.replay();

  }

  public MailingRecipient buildMailingRecipient( long id, String firstName, String secondName, String locale, int semiRandomValue )
  {
    MailingRecipient mailingRecipient = new MailingRecipient();

    mailingRecipient.setId( new Long( id ) );
    mailingRecipient.setUser( buildUser( id, firstName, secondName ) );
    mailingRecipient.setGuid( String.valueOf( new Date().getTime() + semiRandomValue ) );
    mailingRecipient.setLocale( locale );

    return mailingRecipient;
  }

  private User buildUser( long id, String firstName, String lastName )
  {
    User user = new User();
    user.setId( new Long( id ) );
    user.setFirstName( firstName );
    user.setLastName( lastName );
    user.setLanguageType( (LanguageType)MockPickListFactory.getMockPickListItem( LanguageType.class, LanguageType.ENGLISH ) );
    UserEmailAddress userEmailAddress = new UserEmailAddress();
    userEmailAddress.setEmailAddr( firstName + "." + lastName + "@test.com" );
    userEmailAddress.setEmailType( EmailAddressType.lookup( EmailAddressType.HOME ) );
    userEmailAddress.setVerificationStatus( VerificationStatusType.lookup( VerificationStatusType.UNVERIFIED ) );
    userEmailAddress.setIsPrimary( new Boolean( true ) );
    user.getUserEmailAddresses().add( userEmailAddress );

    return user;
  }

  public static User buildUser()
  {
    PickListItem.setPickListFactory( new MockPickListFactory() );

    User user = new User();
    user.setId( 1000L );
    user.setFirstName( "John" );
    user.setMiddleName( "Conrad" );
    user.setLastName( "Doe" );
    user.setUserName( "johndoe" );
    user.setUserType( UserType.lookup( UserType.BI ) );
    user.setActive( true );
    user.setLanguageType( LanguageType.lookup( LanguageType.ENGLISH ) );

    Calendar calendar = Calendar.getInstance();
    calendar.set( 1970, 1, 1 );
    user.setBirthDate( calendar.getTime() );

    return user;
  }

  private Mailing buildMailing( Long id, MailingType mailingType, long time )
  {
    Mailing mailing = new Mailing();
    mailing.setMailingType( mailingType );
    mailing.setId( id );
    mailing.setMailingRecipients( buildMailingRecipients() );
    mailing.setMessage( buildMessage( 1 ) );
    mailing.setDeliveryDate( new Timestamp( time ) );
    mailing.setSender( "testmessage@test.com" );

    return mailing;
  }

  private Set<MailingRecipient> buildMailingRecipients()
  {
    Set<MailingRecipient> mailingRecipients = new HashSet<>();

    mailingRecipients.add( buildMailingRecipient( 1, "Matthew", "Shepard", "en", 1 ) );
    mailingRecipients.add( buildMailingRecipient( 2, "Glynn", "Pooke", "en", 2 ) );
    mailingRecipients.add( buildMailingRecipient( 3, "Robert", "Presswood", "en", 3 ) );
    mailingRecipients.add( buildMailingRecipient( 4, "Vanessa", "Martin", "en", 4 ) );

    return mailingRecipients;
  }

  private Message buildMessage( long id )
  {
    Message message = new Message();
    message.setId( new Long( id ) );
    message.setCmAssetCode( "message_data.message.12556" );

    return message;
  }

}

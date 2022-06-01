
package com.biperf.core.service.participant.impl;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.TimeZone;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.biperf.core.builders.BuilderUtil;
import com.biperf.core.dao.participant.UserTokenDAO;
import com.biperf.core.domain.Address;
import com.biperf.core.domain.AuditCreateInfo;
import com.biperf.core.domain.country.Country;
import com.biperf.core.domain.enums.AddressMethodType;
import com.biperf.core.domain.enums.AddressType;
import com.biperf.core.domain.enums.CountryStatusType;
import com.biperf.core.domain.enums.EmailAddressType;
import com.biperf.core.domain.enums.StateType;
import com.biperf.core.domain.enums.TimeZoneId;
import com.biperf.core.domain.enums.UserTokenStatusType;
import com.biperf.core.domain.enums.UserType;
import com.biperf.core.domain.enums.VerificationStatusType;
import com.biperf.core.domain.mailing.Mailing;
import com.biperf.core.domain.system.PropertySetItem;
import com.biperf.core.domain.user.EmailUserToken;
import com.biperf.core.domain.user.PhoneUserToken;
import com.biperf.core.domain.user.User;
import com.biperf.core.domain.user.UserAddress;
import com.biperf.core.domain.user.UserEmailAddress;
import com.biperf.core.domain.user.UserPhone;
import com.biperf.core.domain.user.UserToken;
import com.biperf.core.service.email.MailingService;
import com.biperf.core.service.message.MessageService;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.participant.ProfileService;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.strategy.usertoken.UserTokenFactory;
import com.biperf.core.strategy.usertoken.UserTokenStrategy;
import com.biperf.core.strategy.usertoken.UserTokenType;
import com.biperf.core.ui.UnitTest;
import com.biperf.core.ui.user.ContactType;
import com.biperf.core.ui.user.PaxContactType;
import com.biperf.core.ui.user.PaxPasswordResetView;

@RunWith( MockitoJUnitRunner.class )
public class PasswordResetServiceImplTest extends UnitTest
{

  @Mock
  SystemVariableService systemVariableService;
  @Mock
  UserService userService;
  @Mock
  MailingService mailingService;
  @Mock
  UserTokenDAO userTokenDAO;
  @Mock
  ProfileService profileService;
  @Mock
  ParticipantService participantService;
  @Mock
  UserTokenFactory userTokenFactory;
  @Mock
  UserTokenStrategy userTokenStrategy;

  String USER_TOKEN = "token-123";
  String USER_NAME = "username";
  String EMAIL_CONTACT_TYPE = "EMAIL";
  String PASSWORD = "password";
  String EMAIL_ID = "email@email.com";

  @InjectMocks
  private PasswordResetServiceImpl underTest = new PasswordResetServiceImpl();

  @Test
  public void getContactsAutocomplete()
  {
    List<PaxContactType> contactResults = buildValidPaxContactTypeList();
    when( participantService.getContactsAutocomplete( any( String.class ), any( String.class ) ) ).thenReturn( contactResults );
    PaxPasswordResetView passwordResetView = underTest.getContactsAutocomplete( EMAIL_ID, EMAIL_ID.substring( 0, 6 ) );
    assertTrue( passwordResetView.getContactMethods().size() == contactResults.size() );
  }

  @Test
  public void generateToken_Then_saveToken()
  {
    EmailUserToken userToken = new EmailUserToken();
    userToken.setToken( "abc123" );
    when( userTokenFactory.getStrategy( any() ) ).thenReturn( userTokenStrategy );
    when( userTokenStrategy.generateUserToken( anyLong() ) ).thenReturn( userToken );
    when( userService.getUserById( anyLong() ) ).thenReturn( new User() );
    when( userService.generatePassword() ).thenReturn( USER_TOKEN );
    when( systemVariableService.getPropertyByName( SystemVariableService.PASSWORD_SHOULD_USE_REGEX ) ).thenReturn( BuilderUtil.buildPropertySetItem() );

    UserToken generatedToken = underTest.generateTokenAndSave( TEST_USER_ID, UserTokenType.EMAIL );
    assertNotNull( generatedToken );
  }

  @Test
  public void sendUserNameByEmailID_When_EmailUnique()
  {
    when( participantService.getAdditionalContactMethodsByEmailOrPhone( any( String.class ) ) ).thenReturn( buildValidPaxContactTypeList() );
    when( userService.getUserIdsByEmailOrPhone( any( String.class ) ) ).thenReturn( new HashSet<>( Arrays.asList( 100L ) ) );
    when( userService.getPrimaryUserAddress( anyLong() ) ).thenReturn( buildUserAddress() );
    when( participantService.getValidUserContactMethodsByUserId( any( Long.class ) ) ).thenReturn( buildValidPaxContactTypeList() );
    when( userService.getUserEmailAddressById( anyLong() ) ).thenReturn( buildEmailAddressWithUser() );
    PaxPasswordResetView passwordResetView = underTest.getUserDetailsByContactInformation( EMAIL_ID );
    assertNotNull( passwordResetView );
    assertTrue( passwordResetView.isUnique() );
  }

  @Test
  public void sendUserNameByEmailID_When_EmailNotUnique()
  {
    when( userService.getUserIdsByEmailOrPhone( any( String.class ) ) ).thenReturn( new HashSet<>( Arrays.asList( 100L, 101L ) ) );
    when( userService.getPrimaryUserAddress( anyLong() ) ).thenReturn( buildUserAddress() );
    when( participantService.getValidUserContactMethodsByEmail( any( String.class ) ) ).thenReturn( buildValidPaxContactTypeList(), buildValidPaxContactTypeList() );
    PaxPasswordResetView passwordResetView = underTest.getUserDetailsByContactInformation( EMAIL_ID );
    assertNotNull( passwordResetView );
    assertTrue( !passwordResetView.isUnique() );
  }

  @Test
  public void sendUserNameByEmailID_When_NoEmails()
  {
    when( userService.getUserIdsByEmailOrPhone( any( String.class ) ) ).thenReturn( null );
    when( userService.getPrimaryUserAddress( anyLong() ) ).thenReturn( buildUserAddress() );
    when( participantService.getValidUserContactMethodsByEmail( any( String.class ) ) ).thenReturn( buildEmptyPaxContactTypeList() );
    PaxPasswordResetView passwordResetView = underTest.getUserDetailsByContactInformation( EMAIL_ID );
    assertNotNull( passwordResetView );
    assertTrue( passwordResetView.getContactMethods().isEmpty() );
  }

  @Test
  public void sendUserName_When_ContactTypeIsEmail()
  {
    when( userService.getUserEmailAddressById( anyLong() ) ).thenReturn( buildEmailAddressWithUser() );
    when( mailingService.buildPAXForgotLoginIDNotification( anyLong(), any( PaxContactType.class ), any( String.class ) ) ).thenReturn( new Mailing() );

    PaxPasswordResetView resetView = underTest.sendForgotUserIdNotification( TEST_USER_ID, ContactType.EMAIL, true );

    assertNotNull( resetView );
    verify( mailingService, times( 1 ) ).submitMailing( any(), any(), any() );
  }

  @Test
  public void sendUserName_When_ContactTypeIsNotEmail()
  {

    PropertySetItem item = new PropertySetItem();
    item.setStringVal( "programUrl" );

    when( userService.getUserPhoneById( anyLong() ) ).thenReturn( userPhone() );
    when( systemVariableService.getPropertyByNameAndEnvironment( any() ) ).thenReturn( item );
    when( mailingService.getShortUrl( any() ) ).thenReturn( "shorturl" );

    PaxPasswordResetView resetView = underTest.sendForgotUserIdNotification( TEST_USER_ID, ContactType.PHONE, true );

    assertNotNull( resetView );
    verify( mailingService, times( 1 ) ).sendSmsMessage( any(), any(), any(), any() );

  }

  @Test
  public void passwordResetByEmailID_WhenEmailIsNotUnique()
  {
    when( userService.getUserByUserName( any( String.class ) ) ).thenReturn( buildUser() );
    when( userService.getUserIdsByEmailOrPhone( any( String.class ) ) ).thenReturn( new HashSet<>( Arrays.asList( 100L, 101L ) ) );

    PaxPasswordResetView paxPasswordResetView = underTest.resetPasswordByEmail( EMAIL_ID, USER_NAME );

    assertNotNull( paxPasswordResetView );
    assertFalse( "emailid mapped to more than one user anme", paxPasswordResetView.isUnique() );
  }

  @Test
  public void passwordResetByEmailID_WhenEmailIsUnique()
  {

    when( userService.getUserByUserName( any( String.class ) ) ).thenReturn( buildUserWithEmailAddress() );
    when( userService.getUserIdsByEmailOrPhone( any( String.class ) ) ).thenReturn( new HashSet<>( Arrays.asList( 100L ) ) );
    when( userService.generatePassword() ).thenReturn( PASSWORD );
    when( userService.isUniqueEmail( any( String.class ) ) ).thenReturn( true );

    PaxPasswordResetView paxPasswordResetView = underTest.resetPasswordByEmail( EMAIL_ID, USER_NAME );

    assertNotNull( paxPasswordResetView );
    assertTrue( paxPasswordResetView.isUnique() );

  }

  @Test
  public void validateToken_ForGiven_Username()
  {
    PropertySetItem item = new PropertySetItem();
    item.setIntVal( 1 );

    when( userService.getUserByUserName( any() ) ).thenReturn( buildUser() );
    when( userTokenDAO.validateToken( any(), any() ) ).thenReturn( true );
    when( userTokenDAO.getTokenById( any() ) ).thenReturn( buildUserToken() );

    PaxPasswordResetView validateToken = underTest.validateToken( USER_TOKEN );

    assertNotNull( validateToken );
  }

  @Test
  public void resetPassword_ForGivenUserWithGivenPassword()
  {
    PropertySetItem item = new PropertySetItem();
    item.setIntVal( 1 );
    when( userService.getUserByUserName( USER_NAME ) ).thenReturn( buildUser() );
    when( userTokenDAO.validateToken( any(), any() ) ).thenReturn( true );
    when( userTokenDAO.getTokenById( any() ) ).thenReturn( buildUserToken() );
    try
    {
      underTest.resetPassword( USER_NAME, PASSWORD, USER_TOKEN, MessageService.PASSWORD_CHANGE );
      assertTrue( true );
    }
    catch( Exception e )
    {
      fail();
    }
  }

  @Test
  public void sendUserTokenTo_EmailContactType()
  {
    EmailUserToken userToken = new EmailUserToken();
    userToken.setToken( "abc123" );
    when( userTokenFactory.getStrategy( any() ) ).thenReturn( userTokenStrategy );
    when( userTokenStrategy.generateUserToken( anyLong() ) ).thenReturn( userToken );
    when( userService.getUserEmailAddressById( anyLong() ) ).thenReturn( buildEmailAddressWithUser() );
    when( userService.generatePassword() ).thenReturn( PASSWORD );
    when( mailingService.buildPAXForgotPasswordNotification( any(), any(), any() ) ).thenReturn( new Mailing() );
    when( systemVariableService.getPropertyByName( SystemVariableService.PASSWORD_SHOULD_USE_REGEX ) ).thenReturn( BuilderUtil.buildPropertySetItem() );

    PaxPasswordResetView paxPasswordResetView = underTest.sendUserToken( TEST_USER_ID, ContactType.EMAIL );

    assertNotNull( paxPasswordResetView );
    verify( mailingService, times( 1 ) ).submitMailing( any( Mailing.class ), any(), any() );

  }

  @Test
  public void sendUserToken_ToNonEmailContactType()
  {
    PropertySetItem item = new PropertySetItem();
    item.setStringVal( "programUrl" );

    PhoneUserToken userToken = new PhoneUserToken();
    userToken.setToken( "abc123=" );
    userToken.setUnencryptedTokenValue( "abc123" );
    when( userTokenFactory.getStrategy( any() ) ).thenReturn( userTokenStrategy );
    when( userTokenStrategy.generateUserToken( anyLong() ) ).thenReturn( userToken );
    when( userService.getUserPhoneById( anyLong() ) ).thenReturn( userPhone() );
    when( userService.generatePassword() ).thenReturn( PASSWORD );
    when( systemVariableService.getPropertyByNameAndEnvironment( any() ) ).thenReturn( item );
    when( mailingService.getShortUrl( any() ) ).thenReturn( "shorturl" );
    when( systemVariableService.getPropertyByName( SystemVariableService.PASSWORD_SHOULD_USE_REGEX ) ).thenReturn( BuilderUtil.buildPropertySetItem() );
    when( systemVariableService.getPropertyByName( SystemVariableService.CLIENT_NAME ) ).thenReturn( BuilderUtil.buildPropertySetItem() );

    PaxPasswordResetView paxPasswordResetView = underTest.sendUserToken( TEST_USER_ID, ContactType.PHONE );

    assertNotNull( paxPasswordResetView );
    verify( mailingService, times( 1 ) ).sendSmsMessage( any(), any(), any(), any() );

  }

  private UserEmailAddress buildUserEmailAddress()
  {
    UserEmailAddress emailAddress = new UserEmailAddress();
    // emailAddress.setUser( buildUser() );
    emailAddress.setEmailAddr( EMAIL_ID );
    emailAddress.setEmailType( EmailAddressType.lookup( EmailAddressType.BUSINESS ) );
    emailAddress.setVerificationStatus( VerificationStatusType.lookup( VerificationStatusType.UNVERIFIED ) );
    return emailAddress;
  }

  private UserEmailAddress buildEmailAddressWithUser()
  {
    UserEmailAddress emailAddress = new UserEmailAddress();
    emailAddress.setUser( buildUser() );
    emailAddress.setEmailAddr( EMAIL_ID );
    emailAddress.setVerificationStatus( VerificationStatusType.lookup( VerificationStatusType.UNVERIFIED ) );
    emailAddress.setIsPrimary( true );
    return emailAddress;
  }

  private User buildUser()
  {
    User user = new User();
    user.setId( 100L );
    user.setUserName( "userName" );
    user.setUserType( UserType.lookup( UserType.PARTICIPANT ) );
    return user;
  }

  private User buildUserWithEmailAddress()
  {
    User user = new User();
    user.setId( 100L );
    user.setUserName( "userName" );
    user.setUserEmailAddresses( new HashSet<UserEmailAddress>( Arrays.asList( buildEmailAddressWithUser() ) ) );
    user.setUserType( UserType.lookup( UserType.PARTICIPANT ) );
    return user;
  }

  private List<PaxContactType> buildValidPaxContactTypeList()
  {
    List<PaxContactType> list = buildEmptyPaxContactTypeList();
    PaxContactType type1 = new PaxContactType();
    type1.setContactId( 1L );
    type1.setContactType( ContactType.EMAIL );
    type1.setUserId( TEST_USER_ID );
    type1.setValue( EMAIL_ID );
    type1.setUnique( true );
    type1.setInputContact( true );
    list.add( type1 );

    return list;
  }

  private List<PaxContactType> buildEmptyPaxContactTypeList()
  {
    return new ArrayList<PaxContactType>();
  }

  private UserPhone userPhone()
  {
    UserPhone userPhone = new UserPhone();
    userPhone.setUser( buildUser() );
    userPhone.setPhoneNbr( "555-123-4557" );
    return userPhone;
  }

  private UserAddress buildUserAddress()
  {
    UserAddress userAddress = new UserAddress();
    userAddress.setUser( buildUser() );
    userAddress.setAddressType( AddressType.lookup( AddressType.BUSINESS_TYPE ) );
    userAddress.setAddress( buildAddress() );
    return userAddress;
  }

  private UserToken buildUserToken()
  {
    UserToken userToken = new EmailUserToken();
    AuditCreateInfo createInfo = new AuditCreateInfo();
    createInfo.setDateCreated( Timestamp.from( Instant.now() ) );
    userToken.setAuditCreateInfo( createInfo );
    userToken.setUser( buildUserWithEmailAddress() );
    userToken.setStatus( UserTokenStatusType.lookup( UserTokenStatusType.ISSUED ) );
    userToken.setExpirationDate( Date.from( Instant.now().plusSeconds( 300000 ) ) );
    return userToken;
  }

  private Address buildAddress()
  {
    Address address = new Address();
    address.setAddr1( "123 Main Street" );
    address.setAddr2( "Floor 33" );
    address.setAddr3( "Office 9" );
    address.setCity( "Anytown" );
    address.setStateType( StateType.lookup( "mn" ) );
    address.setPostalCode( "52901" );
    address.setCountry( buildCountry( getUniqueString() ) );
    return address;
  }

  private Country buildCountry( String uniqueString )
  {
    Country country = new Country();

    country.setCountryCode( "ba" + uniqueString );
    country.setCountryName( "Batavia" + uniqueString );
    country.setCampaignNbr( "NUMBER" + uniqueString );
    country.setCampaignPassword( "PASSWORD" + uniqueString );
    country.setAwardbanqAbbrev( "bat" );
    country.setAddressMethod( AddressMethodType.lookup( AddressMethodType.NORTH_AMERICAN ) );
    country.setStatus( CountryStatusType.lookup( CountryStatusType.ACTIVE ) );
    country.setDateStatus( new Date() );
    country.setAllowSms( Boolean.FALSE );
    country.setRequirePostalCode( Boolean.FALSE );
    country.setTimeZoneId( TimeZoneId.lookup( TimeZone.getDefault().getID() ) );
    country.setDisplayTravelAward( false );
    country.setBudgetMediaValue( BigDecimal.valueOf( .35 ) );

    return country;
  }

}

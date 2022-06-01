
package com.biperf.core.builders;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.biperf.core.domain.Address;
import com.biperf.core.domain.country.Country;
import com.biperf.core.domain.enums.EmailAddressType;
import com.biperf.core.domain.enums.LanguageType;
import com.biperf.core.domain.enums.MailingType;
import com.biperf.core.domain.enums.MessageModuleType;
import com.biperf.core.domain.enums.MessageSMSGroupType;
import com.biperf.core.domain.enums.MessageStatusType;
import com.biperf.core.domain.enums.MessageType;
import com.biperf.core.domain.enums.MockPickListFactory;
import com.biperf.core.domain.enums.ParticipantIdentifierType;
import com.biperf.core.domain.enums.PhoneType;
import com.biperf.core.domain.enums.PickListItem;
import com.biperf.core.domain.enums.StateType;
import com.biperf.core.domain.enums.UserTokenStatusType;
import com.biperf.core.domain.enums.UserType;
import com.biperf.core.domain.enums.VerificationStatusType;
import com.biperf.core.domain.mailing.Mailing;
import com.biperf.core.domain.message.Message;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.participant.ParticipantIdentifier;
import com.biperf.core.domain.system.PropertySetItem;
import com.biperf.core.domain.user.EmailUserToken;
import com.biperf.core.domain.user.PhoneUserToken;
import com.biperf.core.domain.user.User;
import com.biperf.core.domain.user.UserAddress;
import com.biperf.core.domain.user.UserEmailAddress;
import com.biperf.core.domain.user.UserPhone;
import com.biperf.core.domain.user.UserToken;
import com.biperf.core.utils.GuidUtils;

public class BuilderUtil
{

  public static ParticipantIdentifier buildParticipantIdentifier()
  {
    ParticipantIdentifier pi = new ParticipantIdentifier();
    pi.setDescription( "description" );
    pi.setLabel( "label" );
    pi.setParticipantIdentifierType( ParticipantIdentifierType.lookup( ParticipantIdentifierType.EMAIL ) );
    pi.setSelected( true );
    return pi;
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
    user.setUserType( UserType.lookup( UserType.PARTICIPANT ) );
    user.setActive( true );
    user.setLanguageType( LanguageType.lookup( LanguageType.ENGLISH ) );

    Calendar calendar = Calendar.getInstance();
    calendar.set( 1970, 1, 1 );
    user.setBirthDate( calendar.getTime() );

    return user;
  }

  @SuppressWarnings( "unchecked" )
  public static Participant buildParticipant()
  {
    PickListItem.setPickListFactory( new MockPickListFactory() );

    Participant participant = new Participant();
    participant.setId( 1000L );
    participant.setFirstName( "John" );
    participant.setMiddleName( "Conrad" );
    participant.setLastName( "Doe" );
    participant.setUserName( "johndoe" );
    participant.setUserType( UserType.lookup( UserType.PARTICIPANT ) );
    participant.setActive( true );
    participant.setLanguageType( LanguageType.lookup( LanguageType.ENGLISH ) );

    UserEmailAddress email = buildUserEmailAddress();
    email.setUser( participant );
    participant.getUserEmailAddresses().add( buildUserEmailAddress() );

    UserAddress address = buildUserAddress();
    address.setUser( participant );
    participant.getUserAddresses().add( address );

    Calendar calendar = Calendar.getInstance();
    calendar.set( 1970, 1, 1 );
    participant.setBirthDate( calendar.getTime() );

    return participant;
  }

  public static UserAddress buildUserAddress()
  {
    UserAddress userAddress = new UserAddress();
    Address address = new Address();
    address.setAddr1( "Street 1" );
    address.setCity( "edina" );
    address.setCountry( buildCountry() );
    address.setPostalCode( "12345" );
    List stateList = StateType.getList( address.getCountry().getCountryCode() );
    if ( stateList.size() > 0 )
    {
      address.setStateType( (StateType)stateList.get( 0 ) );
    }
    userAddress.setAddress( address );
    userAddress.setIsPrimary( true );
    return userAddress;
  }

  public static Country buildCountry()
  {
    Country country = new Country();
    country.setCountryName( "us" );
    return country;
  }

  public static UserEmailAddress email()
  {
    UserEmailAddress email = new UserEmailAddress();
    email.setEmailAddr( "test@biworldwide.com" );
    email.setEmailType( EmailAddressType.lookup( EmailAddressType.HOME ) );
    email.setIsPrimary( true );
    email.setVerificationStatus( VerificationStatusType.lookup( VerificationStatusType.UNVERIFIED ) );
    return email;
  }

  public static UserEmailAddress buildUserEmailAddress()
  {
    UserEmailAddress email = new UserEmailAddress();
    email.setEmailAddr( "test@biworldwide.com" );
    email.setEmailType( EmailAddressType.lookup( EmailAddressType.HOME ) );
    email.setIsPrimary( true );
    email.setVerificationStatus( VerificationStatusType.lookup( VerificationStatusType.UNVERIFIED ) );
    return email;
  }

  public static UserPhone buildUserPhone()
  {
    UserPhone phone = new UserPhone();
    phone.setId( 1L );
    phone.setIsPrimary( true );
    phone.setPhoneNbr( "123-123-1234" );
    phone.setPhoneType( PhoneType.lookup( PhoneType.BUSINESS ) );
    phone.setVerificationStatus( VerificationStatusType.lookup( VerificationStatusType.UNVERIFIED ) );
    return phone;
  }

  public static UserToken buildIssuedUserTokenForEmail( User user )
  {
    UserToken userToken = new EmailUserToken();
    userToken.setId( 1000L );
    userToken.setStatus( UserTokenStatusType.lookup( UserTokenStatusType.ISSUED ) );
    userToken.setToken( "qwertyuiop" );
    userToken.setUnencryptedTokenValue( userToken.getToken() );
    userToken.setUser( user );
    return userToken;
  }

  public static UserToken buildIssuedUserTokenForPhone( User user )
  {
    UserToken userToken = new PhoneUserToken();
    userToken.setId( 1000L );
    userToken.setStatus( UserTokenStatusType.lookup( UserTokenStatusType.ISSUED ) );
    userToken.setToken( "123456" );
    userToken.setUnencryptedTokenValue( userToken.getToken() );
    userToken.setUser( user );
    return userToken;
  }

  public static Message buildMessage( String cmAssetCode )
  {
    Message message = new Message();
    message.setId( 1000L );
    message.setCmAssetCode( cmAssetCode );
    message.setDateLastSent( new Date() );
    message.setMessageSMSGroupType( MessageSMSGroupType.lookup( MessageSMSGroupType.PROMOTIONAL_MESSAGE ) );
    message.setMessageTypeCode( MessageType.lookup( MessageType.GENERAL ) );
    message.setModuleCode( MessageModuleType.lookup( MessageModuleType.GENERAL ) );
    message.setName( "Message" );
    message.setStatusCode( MessageStatusType.lookup( MessageStatusType.ACTIVE ) );

    return message;
  }

  public static Mailing buildMailing( String cmAssetCode )
  {
    Mailing mailing = new Mailing();
    mailing.setGuid( GuidUtils.generateGuid() );
    mailing.setSender( "sender@biworldwide.com" );
    mailing.setMailingType( MailingType.lookup( MailingType.SYSTEM ) );
    mailing.setMessage( buildMessage( cmAssetCode ) );
    mailing.setMessageTypeSMS( true );
    mailing.setDeliveryDate( Timestamp.from( Instant.now() ) );
    return mailing;
  }

  public static PropertySetItem buildPropertySetItem()
  {
    PropertySetItem propertySetItem = new PropertySetItem();
    propertySetItem.setEntityName( "entity.name" );
    propertySetItem.setKey( "entity.key" );
    propertySetItem.setEntityId( 1L );
    propertySetItem.setStringVal( "" );
    propertySetItem.setBooleanVal( false );
    propertySetItem.setIntVal( 20 );
    return propertySetItem;
  }

}

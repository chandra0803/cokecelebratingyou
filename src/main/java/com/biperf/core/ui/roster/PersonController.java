
package com.biperf.core.ui.roster;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.stereotype.Controller;

import com.biperf.core.domain.Address;
import com.biperf.core.domain.characteristic.Characteristic;
import com.biperf.core.domain.characteristic.UserCharacteristicType;
import com.biperf.core.domain.country.Country;
import com.biperf.core.domain.enums.AddressType;
import com.biperf.core.domain.enums.CharacteristicDataType;
import com.biperf.core.domain.enums.CharacteristicVisibility;
import com.biperf.core.domain.enums.EmailAddressType;
import com.biperf.core.domain.enums.HierarchyRoleType;
import com.biperf.core.domain.enums.LanguageType;
import com.biperf.core.domain.enums.PhoneType;
import com.biperf.core.domain.enums.StateType;
import com.biperf.core.domain.enums.SuffixType;
import com.biperf.core.domain.enums.TitleType;
import com.biperf.core.domain.enums.VerificationStatusType;
import com.biperf.core.domain.hierarchy.Node;
import com.biperf.core.domain.participant.Audience;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.participant.ParticipantEmployer;
import com.biperf.core.domain.user.User;
import com.biperf.core.domain.user.UserAddress;
import com.biperf.core.domain.user.UserCharacteristic;
import com.biperf.core.domain.user.UserEmailAddress;
import com.biperf.core.domain.user.UserNode;
import com.biperf.core.domain.user.UserPhone;
import com.biperf.core.exception.BadRequestException;
import com.biperf.core.exception.ResourceNotFoundException;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.exception.UniqueConstraintViolationException;
import com.biperf.core.mobileapp.recognition.domain.DeviceType;
import com.biperf.core.mobileapp.recognition.domain.UserDevice;
import com.biperf.core.security.AuthenticatedUser;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.participant.ParticipantAssociationRequest;
import com.biperf.core.service.participant.UserAssociationRequest;
import com.biperf.core.utils.LocaleUtils;
import com.biperf.core.utils.crypto.SHA256Hash;
import com.biw.digs.rest.enums.DataFormatEnum;
import com.biw.digs.rest.enums.DataTypeEnum;
import com.biw.digs.rest.enums.PersonFieldEnum;
import com.biw.digs.rest.enums.RoleTypeEnum;
import com.biw.digs.rest.request.AttributeRequest;
import com.biw.digs.rest.request.AuthenticationRequest;
import com.biw.digs.rest.request.ChangePasswordRequest;
import com.biw.digs.rest.request.CreateFullPersonRequest;
import com.biw.digs.rest.request.CreatePersonAddressRequest;
import com.biw.digs.rest.request.CreatePersonAttributeDefinitionRequest;
import com.biw.digs.rest.request.CreatePersonAttributeRequest;
import com.biw.digs.rest.request.CreatePersonEmailRequest;
import com.biw.digs.rest.request.CreatePersonPhoneRequest;
import com.biw.digs.rest.request.CreatePersonPushNotificationSubscriptionRequest;
import com.biw.digs.rest.request.CreatePersonRequest;
import com.biw.digs.rest.request.PersonDetailsRequest;
import com.biw.digs.rest.request.ResetPersonAttributesRequest;
import com.biw.digs.rest.request.UpdatePersonAddressRequest;
import com.biw.digs.rest.request.UpdatePersonAttributeRequest;
import com.biw.digs.rest.request.UpdatePersonEmailRequest;
import com.biw.digs.rest.request.UpdatePersonPhoneRequest;
import com.biw.digs.rest.request.UpdatePersonPushNotificationSubscriptionRequest;
import com.biw.digs.rest.request.UpdatePersonRequest;
import com.biw.digs.rest.response.AttributeDescriptionView;
import com.biw.digs.rest.response.AttributeDescriptions;
import com.biw.digs.rest.response.AttributeView;
import com.biw.digs.rest.response.GroupView;
import com.biw.digs.rest.response.PersonAddressView;
import com.biw.digs.rest.response.PersonAddresses;
import com.biw.digs.rest.response.PersonAttributes;
import com.biw.digs.rest.response.PersonEmailAddresses;
import com.biw.digs.rest.response.PersonEmailView;
import com.biw.digs.rest.response.PersonFullView;
import com.biw.digs.rest.response.PersonGroups;
import com.biw.digs.rest.response.PersonPhoneView;
import com.biw.digs.rest.response.PersonPhones;
import com.biw.digs.rest.response.PersonPushNotificationSubscriptionView;
import com.biw.digs.rest.response.PersonPushNotificationSubscriptions;
import com.biw.digs.rest.response.PersonView;
import com.biw.digs.rest.service.PersonService;
import com.objectpartners.cms.util.CmsResourceBundle;

@Controller
public class PersonController extends RosterBaseController implements PersonService
{
  private static final Log logger = LogFactory.getLog( PersonController.class );

  @Override
  public PersonView authenticate( UUID companyId, AuthenticationRequest request )
  {
    String hashedPassword = new SHA256Hash().encrypt( request.getPassword(), false );

    if ( logger.isDebugEnabled() )
    {
      logger.debug( "compayId" + companyId + "User Id :" + request.getUserId() );
    }
    if ( request.getUserId().isEmpty() )
    {
      throw new BadRequestException( "user name should not be blank" );
    }
    if ( request.getPassword().isEmpty() )
    {
      throw new BadRequestException( "password should  not be blank" );
    }

    try
    {
      AuthenticatedUser user = authenticationService.authenticate( request.getUserId(), hashedPassword );

      if ( logger.isDebugEnabled() )
      {
        logger.debug( "User Id :" + user.getUserId() );
      }

      if ( Objects.nonNull( user.getUserId() ) )
      {
        return buildPersonView( userService.getUserById( user.getUserId() ) );
      }
      else
      {
        throw new ResourceNotFoundException( "Person not found with given id : +'" + user.getUserId() + "' " );
      }
    }
    catch( Exception e )
    {
      throw new ResourceNotFoundException( e.getMessage() );
    }

  }

  @Override
  public PersonView getPersonById( UUID companyId, UUID id )
  {
    if ( logger.isDebugEnabled() )
    {
      logger.debug( "companyId : " + companyId + "rosterUserId :" + id );
    }

    User user = userService.getUserByRosterUserId( id );

    if ( Objects.nonNull( user ) && user.isActive() )
    {
      return buildPersonView( user );
    }
    else
    {
      throw new ResourceNotFoundException( "Person not found with given id : '" + user.getRosterUserId() + "'" );
    }
  }

  @Override
  public PersonFullView getHydratedPerson( UUID companyId, UUID id, PersonDetailsRequest request )
  {
    if ( logger.isDebugEnabled() )
    {
      logger.debug( "companyId : " + companyId + "rosterUserId :" + id );
    }

    User user = userService.getUserByRosterUserId( id );

    if ( Objects.nonNull( user ) )
    {
      return buildPersonFullView( user, request, companyId, id );
    }
    else
    {
      return new PersonFullView();
    }

  }

  @Override
  public PersonView createPerson( UUID companyId, CreatePersonRequest request )
  {
    /*
     * DM doesn’t allow participant creation.
     */

    throw new BadRequestException( " create person is not allowed " );
  }

  @SuppressWarnings( "unchecked" )
  @Override
  public PersonView updatePerson( UUID companyId, UUID personId, UpdatePersonRequest request )
  {
    if ( logger.isDebugEnabled() )
    {
      logger.debug( "companyId : " + companyId + "rosterUserId :" + personId );
    }

    User user = userService.getUserByRosterUserId( personId );

    if ( Objects.nonNull( user ) )
    {
      try
      {
        if ( Objects.isNull( request.getGivenName() ) || request.getGivenName().isEmpty() )
        {
          throw new BadRequestException( "givenName cannot be blank or null " );
        }
        if ( Objects.isNull( request.getSurname() ) || request.getSurname().isEmpty() )
        {
          throw new BadRequestException( "lastname cannot be blank" );
        }

        user.setFirstName( request.getGivenName() );
        user.setLastName( request.getSurname() );

        if ( Objects.isNull( request.getTitle() ) )
        {
          user.setTitleType( TitleType.lookup( request.getTitle() ) );
        }

        if ( Objects.isNull( request.getSuffix() ) )
        {
          user.setSuffixType( SuffixType.lookup( request.getSuffix() ) );
        }

        user.setMiddleName( request.getMiddleName() );

        AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
        associationRequestCollection.add( new ParticipantAssociationRequest( ParticipantAssociationRequest.EMPLOYER ) );
        Participant participant = participantService.getParticipantByIdWithAssociations( user.getId(), associationRequestCollection );

        if ( Objects.nonNull( participant ) && Objects.nonNull( participant.getParticipantEmployers() ) && participant.getParticipantEmployers().size() > 0
            && Objects.nonNull( request.getHireDate() ) )
        {
          participant.getParticipantEmployers().get( 0 ).setHireDate( java.sql.Date.valueOf( request.getHireDate() ) );
        }
        if ( Objects.nonNull( request.getTerminationDate() ) )
        {
          participant.setTerminationDate( java.sql.Date.valueOf( request.getTerminationDate() ) );
        }
        // TODO With simple country code we can't do anything in user side, so ignoring now.
        if ( Objects.nonNull( request.getLocale() ) )
        {
          user.setLanguageType( LanguageType.lookup( request.getLocale().toString() ) );
        }

        return buildPersonView( userService.updateUser( user ) );
      }
      catch( UniqueConstraintViolationException e )
      {
        throw new BadRequestException( e.getMessage() );
      }
      catch( ServiceErrorException e )
      {
        throw new BadRequestException( e.getMessage() );
      }
    }
    throw new BadRequestException( " person not exist " );

  }

  // TODO : partial implementation will re-work on this later
  @SuppressWarnings( "unchecked" )
  @Override
  public void hardDeletePerson( UUID companyId, UUID id )
  {
    if ( logger.isDebugEnabled() )
    {
      logger.debug( "companyId : " + companyId + "rosterUserId :" + id );
    }

    Long userId = userService.getUserIdByRosterUserId( id );

    if ( Objects.nonNull( userId ) )
    {
      AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
      associationRequestCollection.add( new UserAssociationRequest( UserAssociationRequest.EMAIL ) );
      associationRequestCollection.add( new UserAssociationRequest( UserAssociationRequest.ADDRESS ) );
      associationRequestCollection.add( new UserAssociationRequest( UserAssociationRequest.PHONE ) );
      associationRequestCollection.add( new UserAssociationRequest( UserAssociationRequest.NODE ) );
      associationRequestCollection.add( new UserAssociationRequest( UserAssociationRequest.CHARACTERISTIC ) );

      User user = userService.getUserByIdWithAssociations( userId, associationRequestCollection );

      user = scramblePerson( user, companyId, id );

      try
      {
        userService.deleteUser( user );
      }
      catch( UniqueConstraintViolationException e )
      {
        e.printStackTrace();
      }
    }
    else
    {
      throw new ResourceNotFoundException( "Person not found with given roster user id : '" + id + "'" );
    }

  }

  @SuppressWarnings( "unchecked" )
  @Override
  public void deletePerson( UUID companyId, UUID id )
  {
    if ( logger.isDebugEnabled() )
    {
      logger.debug( "companyId : " + companyId + "rosterUserId :" + id );
    }

    User user = userService.getUserByRosterUserId( id );

    if ( Objects.nonNull( user ) )
    {
      AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
      associationRequestCollection.add( new ParticipantAssociationRequest( ParticipantAssociationRequest.EMPLOYER ) );
      Participant participant = participantService.getParticipantByIdWithAssociations( user.getId(), associationRequestCollection );
      ParticipantEmployer participantEmplr = participantService.getCurrentParticipantEmployer( participant );
      if ( Objects.nonNull( participantEmplr ) )
      {
        participantEmplr.setTerminationDate( new Date() );
        participantService.saveParticipantEmployer( participant.getId(), participantEmplr );
      }
      List<Long> participantId = Arrays.asList( new Long( user.getId() ) );
      autoCompleteService.indexParticipants( participantId );
    }
    else
    {
      throw new ResourceNotFoundException( "Person not found with given roster id : '" + id + "'" );
    }
  }

  @SuppressWarnings( "unchecked" )
  @Override
  public PersonEmailAddresses getEmailAddressesForPerson( UUID companyId, UUID id )
  {
    if ( logger.isDebugEnabled() )
    {
      logger.debug( "companyId : " + companyId + "rosterUserId :" + id );
    }

    User user = userService.getUserByRosterUserId( id );

    if ( Objects.nonNull( user ) )
    {
      Set<UserEmailAddress> userEmailAddresses = userService.getUserEmailAddresses( user.getId() );
      PersonEmailAddresses response = new PersonEmailAddresses();
      List<UserEmailAddress> useremailAddressList = userEmailAddresses.stream().collect( Collectors.toList() );
      useremailAddressList.stream().forEach( email -> response.getEmails().add( buildPersonEmailView( email ) ) );
      return response;
    }
    else
    {
      throw new ResourceNotFoundException( "Person not found with given id : '" + id + "'" );
    }
  }

  @SuppressWarnings( "unchecked" )
  @Override
  public PersonEmailView createEmailAddressForPerson( UUID companyId, UUID id, CreatePersonEmailRequest request )
  {
    if ( logger.isDebugEnabled() )
    {
      logger.debug( "companyId : " + companyId + "rosterUserId :" + id + "email address :" + request.getAddress() + "Email Lebel" + request.getLabel() );
    }

    Long userId = userService.getUserIdByRosterUserId( id );

    if ( Objects.nonNull( userId ) )
    {
      AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
      associationRequestCollection.add( new UserAssociationRequest( UserAssociationRequest.EMAIL ) );

      if ( Objects.isNull( request.getLabel() ) || request.getLabel().isEmpty() )
      {
        throw new BadRequestException( "Email label cannot be blank or not null." );
      }

      String labelType = getType( request.getLabel() );

      User user = userService.getUserByIdWithAssociations( userId, associationRequestCollection );

      if ( !isValidEmail( request.getAddress() ) )
      {
        throw new BadRequestException( "You must enter a valid email address" );
      }

      Set<UserEmailAddress> userEmailAddressSet = user.getUserEmailAddresses();
      List<UserEmailAddress> useremailAddressList = userEmailAddressSet.stream().collect( Collectors.toList() );

      for ( UserEmailAddress userAdd : useremailAddressList )
      {
        if ( userAdd.getEmailType().getCode().equalsIgnoreCase( labelType ) )
        {
          throw new BadRequestException( "Email with this  Label " + labelType + "  already existed, please use different label" );
        }
      }

      try
      {
        UserEmailAddress userEmailAddress = new UserEmailAddress();
        userEmailAddress.setEmailAddr( request.getAddress() );
        userEmailAddress.setEmailType( EmailAddressType.lookup( labelType ) );
        userEmailAddress.setIsPrimary( Boolean.FALSE );
        userEmailAddress.setVerificationStatus( VerificationStatusType.lookup( VerificationStatusType.UNVERIFIED ) );

        return buildPersonEmailView( userService.addUserEmailAddress( user.getId(), userEmailAddress ) );
      }
      catch( ServiceErrorException e )
      {
        throw new BadRequestException( e.getMessage() );
      }
    }
    else
    {
      throw new BadRequestException( "Person not found with given personId :  '" + id + "'" );
    }

  }

  @Override
  public void deleteEmailAddressForPerson( UUID companyId, UUID id, UUID emailAddressId )
  {
    if ( logger.isDebugEnabled() )
    {
      logger.debug( "companyId : " + companyId + "rosterUserId :" + id + "emailaddressId :" + emailAddressId );
    }

    User user = userService.getUserByRosterUserId( id );

    if ( Objects.nonNull( user ) )
    {
      userService.deleteEmailAddressForUser( user.getId(), userService.getEmailAddressIdByRosterEmailId( emailAddressId ) );

      // Elastic search index
      autoCompleteService.indexParticipants( Arrays.asList( new Long( user.getId() ) ) );
    }
    else
    {
      throw new ResourceNotFoundException( "Person not found with given id : '" + id + "'" );
    }

  }

  @Override
  public PersonEmailView updateEmailAddressForPerson( UUID companyId, UUID id, UUID emailAddressId, UpdatePersonEmailRequest request )
  {
    if ( logger.isDebugEnabled() )
    {
      logger.debug( "companyId : " + companyId + "rosterUserId :" + id + "emailaddressId :" + emailAddressId + "EmailAddress" + request.getAddress() + "Email Lebel" + request.getLabel() );
    }

    UserEmailAddress userEmailAddress = userService.getUserEmailAddressByRosterEmailId( emailAddressId );

    if ( Objects.nonNull( userEmailAddress ) )
    {
      if ( Objects.isNull( request.getLabel() ) || request.getLabel().isEmpty() )
      {
        throw new BadRequestException( "Email label cannot be blank or Null." );
      }

      String labelType = getType( request.getLabel() );

      if ( !isValidEmail( request.getAddress() ) )
      {
        throw new BadRequestException( "You must enter a valid email address" );
      }

      try
      {
        userEmailAddress.setEmailAddr( request.getAddress() );
        userEmailAddress.setEmailType( EmailAddressType.lookup( labelType ) );
        userEmailAddress = userService.updateEmailAddressForUser( userService.getUserIdByRosterUserId( id ), userEmailAddress );

        // Elastic search index
        autoCompleteService.indexParticipants( Arrays.asList( new Long( userEmailAddress.getUser().getId() ) ) );

        return buildPersonEmailView( userEmailAddress );
      }
      catch( ServiceErrorException e )
      {
        e.printStackTrace();
      }
    }
    else
    {
      throw new BadRequestException( "Email Address with Person ID '" + id + "' and emailAddressId '" + emailAddressId + "' not found for company-id '" + companyId + "'" );
    }
    return null;
  }

  @SuppressWarnings( "unchecked" )
  @Override
  public PersonPhones getPhonesForPerson( UUID companyId, UUID id )
  {
    Long userId = userService.getUserIdByRosterUserId( id );

    if ( Objects.nonNull( userId ) )
    {
      Set<UserPhone> userPhones = userService.getUserPhones( userId );
      PersonPhones response = new PersonPhones();
      List<UserPhone> userAddressesList = userPhones.stream().collect( Collectors.toList() );
      userAddressesList.stream().forEach( phone -> response.getPhoneNumbers().add( buildPersonPhoneView( phone ) ) );
      return response;
    }
    else
    {
      throw new ResourceNotFoundException( "Person not found with given id : '" + id + "'" );
    }
  }

  @SuppressWarnings( "unchecked" )
  @Override
  public PersonPhoneView createPhoneNumberForPerson( UUID companyId, UUID id, CreatePersonPhoneRequest request )
  {
    Long userId = userService.getUserIdByRosterUserId( id );

    if ( Objects.nonNull( userId ) )
    {
      UserPhone userPhone = null;
      AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
      associationRequestCollection.add( new UserAssociationRequest( UserAssociationRequest.PHONE ) );

      User user = userService.getUserByIdWithAssociations( userId, associationRequestCollection );

      if ( Objects.isNull( request.getLabel() ) || request.getLabel().isEmpty() )
      {
        throw new BadRequestException( "Phone label cannot be blank or Null." );
      }

      String labelType = getType( request.getLabel() );

      if ( Objects.isNull( request.getPhoneNumber() ) || request.getPhoneNumber().isEmpty() )
      {
        throw new BadRequestException( "Phone number cannot be blank or Null." );
      }

      Set<UserPhone> userPhoneSet = user.getUserPhones();
      List<UserPhone> userPhoneList = userPhoneSet.stream().collect( Collectors.toList() );
      for ( UserPhone userAdd : userPhoneList )
      {
        if ( userAdd.getPhoneType().getCode().equalsIgnoreCase( labelType ) )
        {
          throw new BadRequestException( "Phone with this  Label " + request.getLabel() + "  already existed, please use different label" );
        }
      }
      userPhone = new UserPhone();
      userPhone.setPhoneExt( request.getPhoneExt() );
      userPhone.setPhoneNbr( request.getPhoneNumber() );
      userPhone.setPhoneType( PhoneType.lookup( labelType ) );

      if ( request.getCountryPhoneCode().length() > 3 )
      {
        throw new BadRequestException( "Invalid Country code." );
      }
      String countryCode = "";
      if ( request.getCountryPhoneCode().length() == 3 )
      {
        countryCode = removeLastCharacterFromCountryCode( request.getCountryPhoneCode() );
      }
      else
      {
        countryCode = request.getCountryPhoneCode();
      }

      userPhone.setCountryPhoneCode( countryCode );
      userPhone.setIsPrimary( Boolean.valueOf( false ) );
      userPhone.setVerificationStatus( VerificationStatusType.lookup( VerificationStatusType.UNVERIFIED ) );
      try
      {
        userPhone = userService.addUserPhone( user.getId(), userPhone );
        return buildPersonPhoneView( userPhone );
      }
      catch( ServiceErrorException e )
      {
        throw new ResourceNotFoundException( e.getMessage() );
      }
    }
    else
    {
      throw new ResourceNotFoundException( "Person not found with given id : '" + id + "'" );
    }
  }

  // TODO : In future, need to re-factor the method, to avoid two DB call.
  @Override
  public void deletePhoneNumberForPerson( UUID companyId, UUID id, UUID phoneNumberId )
  {
    Long phoneNumId = userService.getUserPhoneIdByRosterPhoneId( phoneNumberId );
    Long userId = userService.getUserIdByRosterUserId( id );

    if ( Objects.nonNull( phoneNumId ) && Objects.nonNull( userId ) )
    {
      userService.deletePhoneNumberForUser( userId, phoneNumId );

      // Elastic search index
      autoCompleteService.indexParticipants( Arrays.asList( new Long( userId ) ) );
    }
    else
    {
      throw new ResourceNotFoundException( "Person not found with given id : '" + id + "'" );
    }
  }

  // TODO : In future, need to re-factor the method, to avoid two DB call.
  @Override
  public PersonPhoneView updatePhoneNumberForPerson( UUID companyId, UUID id, UUID phoneNumberId, UpdatePersonPhoneRequest request )
  {
    if ( logger.isDebugEnabled() )
    {
      logger.debug( "companyId : " + companyId + "rosterUserId :" + id + "phoneaddressId :" + phoneNumberId + "phoneNbr" + request.getPhoneNumber() + "phone Lebel" + request.getLabel() );
    }

    Long userId = userService.getUserIdByRosterUserId( id );
    UserPhone userPhone = userService.getUserPhoneByrosterPhoneId( phoneNumberId );

    if ( Objects.nonNull( userId ) && Objects.nonNull( userPhone ) )
    {
      if ( Objects.isNull( request.getLabel() ) || request.getLabel().isEmpty() )
      {
        throw new BadRequestException( "Phone label cannot be blank or Null." );
      }

      String labelType = getType( request.getLabel() );

      if ( Objects.isNull( request.getPhoneNumber() ) || request.getPhoneNumber().isEmpty() )
      {
        throw new BadRequestException( "Phone number cannot be blank or Null." );
      }

      if ( Objects.isNull( request.getCountryPhoneCode() ) || request.getCountryPhoneCode().isEmpty() )
      {
        throw new BadRequestException( "Phone country code cannot be blank or Null." );
      }

      try
      {
        String countryCode = "";

        if ( request.getCountryPhoneCode().length() == 3 )
        {
          countryCode = removeLastCharacterFromCountryCode( request.getCountryPhoneCode() );
        }
        else
        {
          countryCode = request.getCountryPhoneCode();
        }

        Country country = countryService.getCountryByCode( countryCode );

        if ( Objects.isNull( country ) )
        {
          throw new ResourceNotFoundException( "Invalid country code." );
        }

        userPhone.setCountryPhoneCode( countryCode );
        userPhone.setPhoneNbr( request.getPhoneNumber() );
        userPhone.setPhoneExt( request.getPhoneExt() );
        userPhone.setPhoneType( PhoneType.lookup( labelType ) );
        userPhone = userService.updatePhoneNumberForUser( userId, userPhone );

        // Elastic search index
        autoCompleteService.indexParticipants( Arrays.asList( new Long( userId ) ) );

        return buildPersonPhoneView( userPhone );
      }
      catch( ServiceErrorException e )
      {
        throw new BadRequestException( e.getMessage() );
      }

    }
    else
    {
      throw new BadRequestException( "Phone Number with Person ID '" + id + "' and phoneNumberId '" + phoneNumberId + "' not found for company-id '" + companyId + "'" );
    }

  }

  @SuppressWarnings( "unchecked" )
  @Override
  public PersonAddresses getAddressesForPerson( UUID companyId, UUID id )
  {

    User user = userService.getUserById( userService.getUserIdByRosterUserId( id ) );
    if ( Objects.nonNull( user ) )
    {
      Set<UserAddress> userAddresses = userService.getUserAddresses( user.getId() );
      PersonAddresses pa = new PersonAddresses();
      List<UserAddress> userAddressesList = userAddresses.stream().collect( Collectors.toList() );
      userAddressesList.stream().forEach( address -> pa.getAddresses().add( buildPersonAddressView( address ) ) );
      return pa;
    }
    else

    {
      throw new ResourceNotFoundException( "Person not found with given id : '" + user.getRosterUserId() + "'" );
    }
  }

  @SuppressWarnings( "unchecked" )
  @Override
  public PersonAddressView createAddressForPerson( UUID companyId, UUID id, CreatePersonAddressRequest request )
  {

    User user = null;

    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
    associationRequestCollection.add( new UserAssociationRequest( UserAssociationRequest.ADDRESS ) );

    Long userId = userService.getUserIdByRosterUserId( id );

    if ( Objects.nonNull( userId ) )
    {
      user = userService.getUserByIdWithAssociations( userId, associationRequestCollection );

      if ( Objects.isNull( request.getLabel() ) || request.getLabel().isEmpty() )
      {
        throw new BadRequestException( "Address Label cannot be blank or Null." );
      }
      if ( request.getLine1().isEmpty() || Objects.isNull( request.getLine1() ) )
      {
        throw new BadRequestException( "Address line1 cannot be blank oor Null." );
      }

      String labelType = getType( request.getLabel() );

      if ( Objects.isNull( request.getPostalCode() ) || request.getPostalCode().isEmpty() )
      {
        throw new BadRequestException( "Address postalCode cannot be blank or Null." );
      }
      if ( Objects.isNull( request.getCountry() ) || request.getCountry().isEmpty() )
      {
        throw new BadRequestException( "Address country cannot be blank" );
      }
      if ( !getAddressLabel().contains( labelType ) )
      {
        throw new BadRequestException( "Address label type should be 'bus' or 'hom' or 'oth' or 'shp' " );
      }
      Set<UserAddress> userAddressSet = user.getUserAddresses();
      List<UserAddress> userAddressList = userAddressSet.stream().collect( Collectors.toList() );
      for ( UserAddress userAdd : userAddressList )
      {
        if ( userAdd.getAddressType().getCode().equalsIgnoreCase( labelType ) )
        {
          throw new BadRequestException( "Address with this  Label " + request.getLabel() + "  already existed, please use different label" );
        }
      }
      try
      {
        UserAddress userAddress = new UserAddress();
        userAddress.setAddressType( AddressType.lookup( labelType ) );
        userAddress.setAddress( assignAddressAttributes( request ) );
        userAddress.setIsPrimary( Boolean.FALSE );
        return buildPersonAddressView( userService.addUserAddress( user.getId(), userAddress ) );
      }
      catch( ServiceErrorException e )
      {
        throw new ResourceNotFoundException( e.getMessage() );
      }
    }
    else
    {
      throw new ResourceNotFoundException( "Person not found with given id : '" + id + "'" );
    }

  }

  // TODO : In future, need to re-factor the method, to avoid two DB call.
  @Override
  public void deleteAddressForPerson( UUID companyId, UUID id, UUID postalAddressId )
  {
    Long userId = userService.getUserIdByRosterUserId( id );
    Long addressId = userService.getUserAddressIdByRosterAddressId( postalAddressId );

    if ( Objects.nonNull( userId ) && Objects.nonNull( addressId ) )
    {
      userService.deleteAddressForUser( userId, addressId );
    }
    else
    {
      throw new ResourceNotFoundException( "Person not found with given id : '" + id + "'" );
    }

  }

  @Override
  public PersonAddressView updateAddressForPerson( UUID companyId, UUID id, UUID postalAddressId, UpdatePersonAddressRequest request )
  {
    UserAddress userAddress = null;
    String countryCode = "";
    UserAddress updateAddressForPerson = null;
    User user = null;

    Long userId = userService.getUserIdByRosterUserId( id );

    if ( Objects.nonNull( userId ) )
    {
      user = userService.getUserById( userId );

      if ( Objects.isNull( request.getLine1() ) || request.getLine1().isEmpty() )
      {
        throw new BadRequestException( "Address line1 cannot be blank" );
      }
      if ( Objects.isNull( request.getLabel() ) || request.getLabel().isEmpty() )
      {
        throw new BadRequestException( "Address Label cannot be blank" );
      }
      String labelType = request.getLabel().toLowerCase();
      if ( Objects.isNull( request.getPostalCode() ) || request.getPostalCode().isEmpty() )
      {
        throw new BadRequestException( "Address postalCode cannot be blank" );
      }
      if ( Objects.isNull( request.getCountry() ) || request.getCountry().isEmpty() )
      {
        throw new BadRequestException( "Address country code cannot be blank or Null. " );
      }

      if ( !getAddressLabel().contains( labelType ) )
      {
        throw new BadRequestException( "Address label type should be 'bus' or 'hom' or 'oth' or 'shp' " );
      }
      userAddress = userService.getUserAddressByRosterAddressId( postalAddressId );
      if ( Objects.nonNull( userAddress ) )
      {
        try
        {
          userAddress.setAddressType( AddressType.lookup( labelType ) );
          userAddress.getAddress().setAddr1( request.getLine1() );
          if ( Objects.nonNull( request.getLine2() ) )
          {
            userAddress.getAddress().setAddr2( request.getLine2() );
          }
          if ( Objects.nonNull( request.getLine3() ) )
          {
            userAddress.getAddress().setAddr3( request.getLine3() );
          }
          userAddress.getAddress().setCity( request.getCity() );
          userAddress.getAddress().setPostalCode( request.getPostalCode() );

          if ( request.getCountry().length() == 3 )
          {
            countryCode = removeLastCharacterFromCountryCode( request.getCountry() );
          }
          else
          {
            countryCode = request.getCountry();
          }

          Country country = countryService.getCountryByCode( countryCode );

          if ( Objects.isNull( country ) )
          {
            throw new ResourceNotFoundException( "Invalid country code." );
          }
          userAddress.getAddress().setCountry( country );
          userAddress.getAddress().setStateType( StateType.lookup( request.getState() ) );
          updateAddressForPerson = userService.updateAddressForUser( user.getId(), userAddress );
          return buildPersonAddressView( updateAddressForPerson );
        }
        catch( ServiceErrorException e )
        {
          throw new BadRequestException( e.getMessage() );
        }
      }
      else
      {
        throw new BadRequestException( "Address with Person ID '" + id + "' and address '" + postalAddressId + "' not found for company-id '" + companyId + "'" );
      }
    }
    else
    {
      throw new ResourceNotFoundException( "Person not found with given id : '" + id + "'" );
    }
  }

  @Override
  public PersonAttributes getAttributesForPerson( UUID companyId, UUID id )
  {
    Long userId = userService.getUserIdByRosterUserId( id );

    if ( Objects.nonNull( userId ) )
    {
      Set<UserCharacteristic> userCharacteristic = userService.getUserCharacteristics( userId );
      PersonAttributes response = new PersonAttributes();
      List<UserCharacteristic> userCharacteristicList = userCharacteristic.stream().collect( Collectors.toList() );
      userCharacteristicList.stream().forEach( attribute -> response.getAttributes().add( buildPersonAttributeView( attribute ) ) );
      return response;
    }
    else
    {
      throw new ResourceNotFoundException( "Person not found with given id : '" + id + "'" );
    }
  }

  @Override
  public PersonAttributes resetAttributesForPerson( UUID companyId, UUID id, ResetPersonAttributesRequest request )
  {
    Long userId = userService.getUserIdByRosterUserId( id );

    if ( Objects.nonNull( userId ) )
    {
      List<AttributeRequest> attributeRequest = request.getAttributes();

      for ( int i = 0; i <= attributeRequest.size(); i++ )
      {
        AttributeRequest attRequest = attributeRequest.get( i );
        if ( Objects.isNull( attRequest.getName() ) || attRequest.getName().isEmpty() )
        {
          throw new BadRequestException( "Attribute name cannot be blank" );
        }
        if ( Objects.isNull( attRequest.getValue() ) || attRequest.getValue().isEmpty() )
        {
          throw new BadRequestException( "Attribute value cannot be blank" );
        }

      }
      List<UserCharacteristic> userCharacteristicsList = userService.getUserCharacteristicsByUserId( userId );
      for ( UserCharacteristic characteristic : userCharacteristicsList )
      {
        Long userCharId = characteristic.getId();
        userService.resetUserChatracteristic( userId, userCharId );
      }
      userCharacteristicsList.clear();

      List<Characteristic> allCharacterstics = userCharacteristicService.getAllCharacteristics();
      List<AttributeRequest> attributes = request.getAttributes();
      for ( Characteristic characteristic : allCharacterstics )
      {
        UserCharacteristicType userCharacteristicType = null;
        UserCharacteristic userCharacteristic = null;
        String attributeValue = null;
        for ( AttributeRequest attribute : attributes )
        {
          if ( attribute.getValue().equalsIgnoreCase( "male" ) || attribute.getValue().equalsIgnoreCase( "female" ) )
          {
            attributeValue = getType( attribute.getValue() );
          }
          else
          {
            attributeValue = attribute.getValue();
          }
          if ( characteristic.getCharacteristicName().equalsIgnoreCase( attribute.getName() ) )
          {
            userCharacteristic = new UserCharacteristic();
            userCharacteristicType = new UserCharacteristicType();
            CharacteristicDataType dataType = characteristic.getCharacteristicDataType();
            userCharacteristicType.setCharacteristicName( attribute.getName() );
            userCharacteristicType.setCharacteristicDataType( dataType );
            userCharacteristicType.setNameCmKey( dataType.getName() );
            userCharacteristicType.setCmAssetCode( dataType.getCode() );
            userCharacteristicType.setId( characteristic.getId() );
            userCharacteristicType.setDescription( attribute.getName() );
            userCharacteristicType.setVersion( new Long( 1 ) );
            userCharacteristicType.setVisibility( CharacteristicVisibility.lookup( CharacteristicVisibility.VISIBLE ) );
            userCharacteristicType.setActive( Boolean.TRUE );

            userCharacteristic.setCharacteristicValue( attributeValue );
            userCharacteristic.setUserCharacteristicType( userCharacteristicType );
            userCharacteristic.setUser( userService.getUserById( userId ) );
            userService.addUserCharacteristic( userId, userCharacteristic );

          }
        }

      }
      return getAttributesForPerson( companyId, id );

    }
    else
    {
      throw new ResourceNotFoundException( "Person not found with given id : '" + id + "'" );
    }
  }

  @Override
  public AttributeView createAttributeForPerson( UUID companyId, UUID id, CreatePersonAttributeRequest request )
  {
    boolean isAttributeExist = true;
    User user = null;
    Long userId = userService.getUserIdByRosterUserId( id );
    String attributeValue = null;

    if ( Objects.nonNull( userId ) )
    {
      user = userService.getUserById( userId );
      if ( Objects.isNull( request.getName() ) || request.getName().isEmpty() )
      {
        throw new BadRequestException( "Attribute name cannot be blank" );
      }
      if ( Objects.isNull( request.getValue() ) || request.getValue().isEmpty() )
      {
        throw new BadRequestException( "Attribute value cannot be blank" );
      }

      if ( request.getValue().equalsIgnoreCase( "male" ) || request.getValue().equalsIgnoreCase( "female" ) )
      {
        attributeValue = getType( request.getValue() );
      }
      else
      {
        attributeValue = request.getValue();
      }

      UserCharacteristicType userCharacteristicType = new UserCharacteristicType();
      UserCharacteristic userCharacteristic = new UserCharacteristic();
      List<Characteristic> allCharacterstics = userCharacteristicService.getAllCharacteristics();

      for ( Characteristic characteristic : allCharacterstics )
      {
        if ( characteristic.getCharacteristicName().equalsIgnoreCase( request.getName() ) )
        {
          isAttributeExist = false;
          UserCharacteristic uc = userService.getUserCharacteristicById( user.getId(), characteristic.getId() );
          if ( Objects.nonNull( uc ) )
          {
            if ( characteristic.getCharacteristicDataType().getCode().equalsIgnoreCase( CharacteristicDataType.MULTI_SELECT ) )
            {
              if ( Objects.nonNull( uc ) && Objects.nonNull( uc.getCharacteristicValue() ) )
              {
                uc.setCharacteristicValue( uc.getCharacteristicValue() + "," + attributeValue );
              }

            }
            else
            {
              uc.setCharacteristicValue( attributeValue );
            }
            return buildPersonAttributeView( userService.updateCharacteristicForUser( user.getId(), uc ) );

          }
          else
          {
            isAttributeExist = false;
            CharacteristicDataType dataType = characteristic.getCharacteristicDataType();
            userCharacteristicType.setCharacteristicName( request.getName() );
            userCharacteristicType.setCharacteristicDataType( dataType );
            userCharacteristicType.setNameCmKey( dataType.getName() );
            userCharacteristicType.setCmAssetCode( dataType.getCode() );
            userCharacteristicType.setId( characteristic.getId() );
            userCharacteristicType.setDescription( request.getName() );
            userCharacteristicType.setVersion( new Long( 1 ) );
            userCharacteristicType.setVisibility( CharacteristicVisibility.lookup( CharacteristicVisibility.VISIBLE ) );
            userCharacteristicType.setActive( Boolean.TRUE );

            userCharacteristic.setCharacteristicValue( attributeValue );
            userCharacteristic.setAuditCreateInfo( characteristic.getAuditCreateInfo() );
            userCharacteristic.setAuditUpdateInfo( characteristic.getAuditUpdateInfo() );

            userCharacteristic.setUserCharacteristicType( userCharacteristicType );
            userCharacteristic.setUser( user );
            return buildPersonAttributeView( userService.addUserCharacteristic( user.getId(), userCharacteristic ) );
          }

        }

      }
      if ( isAttributeExist )
      {
        logger.error( " characterstic is not found with give name:" + request.getName() );
        throw new ResourceNotFoundException( " characterstic is not found with give name:" + request.getName() );
      }
    }
    else
    {
      throw new ResourceNotFoundException( "Person not found with given id : '" + id + "'" );
    }
    return null;
  }

  @Override
  public void deleteAttributeForPerson( UUID companyId, UUID id, String attributeName )
  {
    Long userId = userService.getUserIdByRosterUserId( id );

    if ( Objects.nonNull( userId ) )
    {
      List<Characteristic> allCharacterstics = userCharacteristicService.getAllCharacteristics();
      for ( Characteristic characteristic : allCharacterstics )
      {
        if ( characteristic.getCharacteristicName().equalsIgnoreCase( attributeName ) )
        {
          Long charctersticId = characteristic.getId();
          userService.deleteCharacteristicForUser( userId, charctersticId );
        }
      }

    }
    else
    {
      throw new ResourceNotFoundException( "Person not found with given id : '" + id + "'" );
    }
  }

  @Override
  public AttributeView updateAttributeForPerson( UUID companyId, UUID id, String attributeName, UpdatePersonAttributeRequest request )
  {
    Long userId = userService.getUserIdByRosterUserId( id );
    String attributeValue = null;

    if ( Objects.isNull( userId ) )
    {
      throw new ResourceNotFoundException( "Person not found with given id : '" + id + "'" );
    }

    if ( !request.getName().equalsIgnoreCase( attributeName ) )
    {
      throw new BadRequestException( "Attribute name mismatch between path variable and payLoad. " );
    }

    if ( request.getValue().equalsIgnoreCase( "male" ) || request.getValue().equalsIgnoreCase( "female" ) )
    {
      attributeValue = getType( request.getValue() );
    }
    else
    {
      attributeValue = request.getValue();
    }

    List<UserCharacteristic> userCharacteristics = new ArrayList<UserCharacteristic>();
    List<Characteristic> allCharacterstics = userCharacteristicService.getAllCharacteristics();
    UserCharacteristic uc = null;

    Characteristic characteristic = allCharacterstics.stream().filter( obj -> obj.getCharacteristicName().equalsIgnoreCase( attributeName ) ).findFirst().orElse( null );

    if ( Objects.nonNull( characteristic ) )
    {
      List<UserCharacteristic> existedUserCharacteristics = userService.getUserCharacteristicsByUserId( userId );

      if ( !existedUserCharacteristics.isEmpty() )
      {
        uc = existedUserCharacteristics.stream().filter( inObj -> inObj.getUserCharacteristicType().getId().equals( characteristic.getId() ) ).findFirst().orElse( null );

        if ( Objects.nonNull( uc ) && !characteristic.getCharacteristicDataType().getCode().equalsIgnoreCase( CharacteristicDataType.MULTI_SELECT ) )
        {
          uc.setCharacteristicValue( attributeValue );
          userCharacteristics.add( uc );
        }

        if ( Objects.nonNull( uc ) && characteristic.getCharacteristicDataType().getCode().equalsIgnoreCase( CharacteristicDataType.MULTI_SELECT ) )
        {
          String charsValue = uc.getCharacteristicValue() + "," + attributeValue;
          String[] value = charsValue.split( "," );
          List<String> characteristicList = Arrays.asList( value );
          Set<String> characteristicSet = characteristicList.stream().map( e -> String.valueOf( e ) ).collect( Collectors.toSet() );
          String charValue = String.join( ",", characteristicSet );

          uc.setCharacteristicValue( charValue );
          userCharacteristics.add( uc );
        }

        if ( !userCharacteristics.isEmpty() )
        {
          userService.updateUserCharacteristics( userId, userCharacteristics );
          userService.updateBankCharacteristics( userId, userCharacteristics );

          try
          {
            audienceService.rematchParticipantForAllCriteriaAudiences( userId );
          }
          catch( ServiceErrorException e )
          {
            e.printStackTrace();
          }

          UserCharacteristic attribute = userService.getUserCharacteristicById( userId, characteristic.getId() );

          return buildPersonAttributeView( attribute );
        }
      }
    }

    if ( Objects.isNull( characteristic ) || Objects.isNull( uc ) || userCharacteristics.isEmpty() )
    {
      throw new ResourceNotFoundException( "Attribute not found for the roster person id '" + id + "' with attribute name '" + attributeName );
    }

    return null;

  }

  @SuppressWarnings( "unchecked" )
  @Override
  public PersonGroups getGroupsForPerson( UUID companyId, UUID id )
  {
    User user = null;
    Long userId = userService.getUserIdByRosterUserId( id );

    if ( Objects.nonNull( userId ) )
    {
      user = userService.getUserById( userId );

      if ( user instanceof Participant )
      {
        Participant pax = (Participant)user;
        Set<Audience> userAudiences = audienceService.getAllParticipantAudiences( pax );
        PersonGroups response = new PersonGroups();
        List<Audience> AudienceList = userAudiences.stream().collect( Collectors.toList() );
        AudienceList.stream().forEach( group -> response.getGroups().add( buildPersonGroupView( group ) ) );
        return response;
      }
      else
      {
        throw new ResourceNotFoundException( "Person not found with given id : '" + id + "'" );
      }
    }
    else
    {
      throw new ResourceNotFoundException( "Person not found with given id : '" + id + "'" );
    }
  }

  @Override
  public PersonPushNotificationSubscriptions getPersonPushNotificationSubscriptionsForPerson( UUID companyId, UUID id )
  {
    Long userId = userService.getUserIdByRosterUserId( id );

    if ( Objects.nonNull( userId ) )
    {
      PersonPushNotificationSubscriptions response = new PersonPushNotificationSubscriptions();
      List<UserDevice> existingDevices = userDeviceService.findUserDevicedForUser( userId );
      existingDevices.stream().forEach( subscription -> response.getSubscriptions().add( buildPersonPushNotificationSubscriptionView( subscription ) ) );
      return response;
    }
    else
    {
      throw new ResourceNotFoundException( "Person not found with given id : '" + id + "'" );
    }
  }

  protected PersonPushNotificationSubscriptionView buildPersonPushNotificationSubscriptionView( UserDevice subscription )
  {
    PersonPushNotificationSubscriptionView view = new PersonPushNotificationSubscriptionView();
    view.setToken( subscription.getRegistrationId() );
    if ( Objects.nonNull( subscription.getDeviceType() ) )
    {
      view.setLabel( subscription.getDeviceType().toString() );
    }
    view.setId( subscription.getRosterDeviceId() );
    return view;
  }

  @Override
  public PersonPushNotificationSubscriptionView createPersonPushNotificationSubscriptionForPerson( UUID companyId, UUID id, CreatePersonPushNotificationSubscriptionRequest request )
  {
    User user = null;
    Long userId = userService.getUserIdByRosterUserId( id );

    if ( Objects.nonNull( userId ) )
    {
      userService.getUserById( userId );

      if ( Objects.isNull( request.getLabel() ) || request.getLabel().isEmpty() )
      {
        throw new BadRequestException( "Push Notification Subscription label cannot be blank or Null." );
      }
      if ( Objects.isNull( request.getToken() ) || request.getToken().isEmpty() )
      {
        throw new BadRequestException( "Push Notification Subscription token cannot be blank or Null." );
      }
      UserDevice userDevice = null;
      if ( request.getLabel().equalsIgnoreCase( "IOS" ) )
      {
        userDevice = new UserDevice( user, DeviceType.IOS, request.getToken(), Boolean.FALSE );
      }
      else
      {
        userDevice = new UserDevice( user, DeviceType.ANDROID, request.getToken(), Boolean.FALSE );
      }

      userDevice.setVersion( new Long( 1 ) );
      return buildPersonPushNotificationSubscriptionView( userDeviceService.createUserDevice( userDevice ) );
    }
    else
    {
      throw new ResourceNotFoundException( "Person not found with given id : '" + id + "'" );
    }
  }

  // TODO : In future, need to re-factor the method, to avoid two DB call.
  @Override
  public void deletePersonPushNotificationSubscriptionForPerson( UUID companyId, UUID id, UUID subscriptionId )
  {
    Long userId = userService.getUserIdByRosterUserId( id );
    Long deviceId = userDeviceService.findUserDeviceIdByRosterDeviceId( subscriptionId );
    if ( Objects.nonNull( userId ) && Objects.nonNull( deviceId ) )
    {
      userDeviceService.deleteDeviceByUserIdAndDeviceId( userId, deviceId );
    }
    else
    {
      throw new ResourceNotFoundException( "Person not found with given id : '" + id + "'" );
    }
  }

  @Override
  public PersonPushNotificationSubscriptionView updatePersonPushNotificationSubscriptionForPerson( UUID companyId,
                                                                                                   UUID id,
                                                                                                   UUID subscriptionId,
                                                                                                   UpdatePersonPushNotificationSubscriptionRequest request )
  {

    Long userId = userService.getUserIdByRosterUserId( id );

    if ( Objects.nonNull( userId ) )
    {
      if ( Objects.isNull( request.getLabel() ) || request.getLabel().isEmpty() )
      {
        throw new BadRequestException( "Push Notification Subscription label cannot be blank or Null." );
      }
      if ( Objects.isNull( request.getToken() ) || request.getToken().isEmpty() )
      {
        throw new BadRequestException( "Push Notification Subscription token cannot be blank or Null." );
      }
      UserDevice userDevice = userDeviceService.findUserDeviceByDeviceId( userId );

      if ( Objects.nonNull( userDevice ) )
      {
        userDevice.setRegistrationId( request.getToken() );

        if ( request.getLabel().equalsIgnoreCase( "IOS" ) )
        {
          userDevice.setDeviceType( DeviceType.IOS );
        }
        else
        {
          userDevice.setDeviceType( DeviceType.ANDROID );
        }
        return buildPersonPushNotificationSubscriptionView( userDeviceService.updateUserDevice( userDevice ) );
      }
      else
      {
        throw new BadRequestException( "Push Notification Subscription with Person roster ID '" + id + "' and subscriptionId '" + subscriptionId + "' not found for company-id '" + companyId + "'" );
      }

    }
    else
    {
      throw new ResourceNotFoundException( "Person not found with given id : '" + id + "'" );
    }
  }

  @SuppressWarnings( "unchecked" )
  @Override
  public AttributeDescriptions getAttributeDescriptionsForPersons( UUID companyId )
  {
    List<Characteristic> allCharacterstics = userCharacteristicService.getAllCharacteristics();
    AttributeDescriptions response = new AttributeDescriptions();
    allCharacterstics.stream().forEach( attribute -> response.getAttributes().add( buildAttributeDescriptionView( attribute ) ) );
    return response;
  }

  // Email validation check return true if email id is invalid format
  private static boolean isValidEmail( String email )
  {
    if ( email == null || email.isEmpty() )
    {
      return false;
    }
    return EmailValidator.getInstance().isValid( email );
  }

  @SuppressWarnings( "unchecked" )
  private Address assignAddressAttributes( CreatePersonAddressRequest request )
  {
    Address address = new Address();
    address.setAddr1( request.getLine1() );
    if ( Objects.nonNull( request.getLine2() ) )
    {
      address.setAddr2( request.getLine2() );
    }
    if ( Objects.nonNull( request.getLine3() ) )
    {
      address.setAddr3( request.getLine3() );
    }
    address.setCity( request.getCity() );
    address.setPostalCode( request.getPostalCode() );
    String countryCode = "";
    if ( request.getCountry().length() == 3 )
    {
      countryCode = removeLastCharacterFromCountryCode( request.getCountry() );
    }
    else
    {
      countryCode = request.getCountry();
    }
    Country country = countryService.getCountryByCode( countryCode );
    if ( Objects.isNull( country ) )
    {
      throw new BadRequestException( "Invalid country code." );
    }
    address.setCountry( country );
    List<StateType> stateList = StateType.getList( countryCode );

    for ( StateType state : stateList )
    {
      if ( state.getName().equalsIgnoreCase( request.getState() ) )
      {
        address.setStateType( StateType.lookup( state.getCode() ) );
      }
    }
    return address;
  }

  @Override
  public PersonView getPersonByExternalId( UUID companyId, String externalId )
  {

    User user = userService.getUserByUserName( externalId );
    if ( Objects.nonNull( user ) )
    {
      return buildPersonView( user );
    }
    else
    {
      throw new ResourceNotFoundException( "Person not found with given external id : '" + externalId + "'" );
    }
  }

  private GroupView buildPersonGroupView( Audience group )
  {
    GroupView response = new GroupView();
    response.setCompanyId( getCompanyId() );
    response.setGroupPublic( group.getPublicAudience() );
    response.setId( group.getRosterAudienceId() );
    response.setName( group.getName() );
    response.setType( getType( group.getAudienceType().getCode() ) );
    return response;
  }

  protected AttributeDescriptionView buildAttributeDescriptionView( Characteristic attributeDescription )
  {
    AttributeDescriptionView view = new AttributeDescriptionView();
    view.setName( attributeDescription.getCharacteristicName() );
    view.setDataFormat( attributeDescription.getCharacteristicDataType().getCode().toString() );
    view.setDataType( attributeDescription.getCharacteristicDataType().getName() );
    return view;
  }

  @SuppressWarnings( "unchecked" )
  protected PersonFullView buildPersonFullView( User person, PersonDetailsRequest request, UUID companyId, UUID id )
  {
    PersonFullView fullView = (PersonFullView)assignPersonFullViewAttributes( new PersonFullView(), person );

    if ( Objects.isNull( request.getPersonProperties() ) )
    {
      return fullView;
    }

    return buildPersonPropsView( person.getId(), request.getPersonProperties(), fullView );

  }

  protected PersonView assignPersonFullViewAttributes( PersonView view, User person )
  {
    view.setGivenName( person.getFirstName() );
    view.setId( person.getRosterUserId() );
    view.setMiddleName( person.getMiddleName() );
    view.setNickName( null );
    view.setPronouns( buildPersonPronouns( person ) );
    if ( Objects.nonNull( person.getSuffixType() ) )
    {
      view.setSuffix( person.getSuffixType().getCode() );
    }
    if ( Objects.nonNull( person.getTitleType() ) )
    {
      view.setTitle( person.getTitleType().getCode() );
    }
    view.setSurname( person.getLastName() );
    view.setExternalId( person.getUserName() );
    view.setUsername( person.getUserName() );
    if ( person.getLanguageType() != null )
    {
      String languageCode = person.getLanguageType().getCode();
      languageCode.replaceAll( "_", "-" );
      view.setLocale( LocaleUtils.getLocale( languageCode ) );
    }
    Country country = userService.getPrimaryUserAddressCountry( person.getId() );
    if ( Objects.nonNull( country ) )
    {
      view.setCountry( country.getAwardbanqAbbrev() );
    }
    return view;
  }

  protected User scramblePerson( User person, UUID companyId, UUID id )
  {
    person.setFirstName( RandomStringUtils.randomAlphanumeric( 30 ) );
    person.setMiddleName( RandomStringUtils.randomAlphanumeric( 30 ) );
    person.setLastName( RandomStringUtils.randomAlphanumeric( 30 ) );
    // person.setUserName( RandomStringUtils.randomAlphanumeric( 30 ) );
    person.setSuffixType( null );
    person.setTitleType( null );
    person.setGenderType( null );
    person.setSsn( null );
    person.setActive( Boolean.FALSE );
    person.setBirthDate( null );
    person.setLanguageType( null );
    @SuppressWarnings( "unchecked" )
    Set<UserAddress> userAdd = person.getUserAddresses();

    for ( UserEmailAddress useremaiAdd : person.getUserEmailAddresses() )
    {
      userService.deleteEmailAddressForUser( person.getId(), useremaiAdd.getId() );
    }

    for ( UserAddress userAddress : userAdd )
    {
      userService.deleteAddressForUser( person.getId(), userAddress.getId() );
    }
    for ( UserPhone userPhone : person.getUserPhones() )
    {
      userService.deletePhoneNumberForUser( person.getId(), userPhone.getId() );
    }
    // TODO have to implement the characteristic functionality
    deletePerson( companyId, id );
    return person;
  }

  private static List<String> getAddressLabel()
  {
    List<String> addressLabelList = new ArrayList<String>();
    addressLabelList.add( AddressType.lookup( AddressType.SHIPPING_TYPE ).getCode() );
    addressLabelList.add( AddressType.lookup( AddressType.BUSINESS_TYPE ).getCode() );
    addressLabelList.add( AddressType.lookup( AddressType.HOME_TYPE ).getCode() );
    addressLabelList.add( "home" );
    addressLabelList.add( AddressType.lookup( AddressType.OTHER_TYPE ).getCode() );
    return addressLabelList;
  }

  @SuppressWarnings( "unchecked" )
  @Override
  public PersonFullView createOrUpdatePersonComplete( UUID companyId, CreateFullPersonRequest request )
  {
    /*
     * DM doesn't allow create participant through roster
     */

    if ( logger.isDebugEnabled() )
    {
      logger.debug( "companyId : " + companyId + "UserId :" + request.getExternalId() );
    }

    throw new BadRequestException( " create person is not allowed " );

  }

  @SuppressWarnings( "unchecked" )
  @Override
  public void removePersonFromHierarchyNode( UUID companyId, UUID id, UUID nodeId )
  {
    if ( logger.isDebugEnabled() )
    {
      logger.debug( "companyId : " + companyId + "rosterUserId :" + id );
    }

    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
    associationRequestCollection.add( new UserAssociationRequest( UserAssociationRequest.NODE ) );
    User user = userService.getUserByIdWithAssociations( userService.getUserIdByRosterUserId( id ), associationRequestCollection );
    UserNode userNode = user.getUserNodeByNodeId( nodeService.getNodeIdByRosterNodeId( nodeId ) );

    if ( Objects.isNull( user ) )
    {
      throw new ResourceNotFoundException( "Person not found with given id : " + user.getRosterUserId() );
    }

    if ( Objects.isNull( userNode ) )
    {
      throw new ResourceNotFoundException( "HierarchyNode '" + userNode.getNode().getRosterNodeId() + "' not found or not assigned to user id '" + user.getRosterUserId() + "'" );
    }
    int count = userService.getAssignedNodes( user.getId() ).size();
    if ( count > 1 )
    {
      try
      {

        String[] nodeIds = new String[] { nodeService.getNodeIdByRosterNodeId( nodeId ).toString() };
        userService.removeUserNodes( user.getId(), nodeIds );
        audienceService.rematchParticipantForAllCriteriaAudiences( user.getId() );
      }
      catch( ServiceErrorException e )
      {
        throw new BadRequestException( e.getMessage() );
      }
    }
    else
    {
      throw new BadRequestException( CmsResourceBundle.getCmsBundle().getString( "node.errors.USER_MORE_THAN_ONE_NODE" ) );
    }

  }

  @Override
  @SuppressWarnings( "rawtypes" )
  public PersonFullView assignPersonToHierarchyNode( UUID companyId, UUID id, UUID nodeId, String roleType )
  {
    if ( logger.isDebugEnabled() )
    {
      logger.debug( "companyId : " + companyId + "rosterUserId :" + id );
    }

    User user = userService.getUserById( userService.getUserIdByRosterUserId( id ) );

    if ( Objects.isNull( user ) )
    {
      throw new ResourceNotFoundException( "Person not found with given id : '" + user.getRosterUserId() + "'" );
    }
    Node node = nodeService.getNodeById( nodeService.getNodeIdByRosterNodeId( nodeId ) );
    if ( Objects.isNull( node ) )
    {
      throw new BadRequestException( "Node not found with given id : '" + node.getRosterNodeId() + "'" );
    }
    RoleTypeEnum role = buildRoleTypeEnum( roleType );

    UserNode userNode = new UserNode();
    if ( Objects.nonNull( role ) )
    {
      if ( role.toString().equalsIgnoreCase( RoleTypeEnum.MEMBER.toString() ) )
      {
        userNode.setHierarchyRoleType( HierarchyRoleType.lookup( HierarchyRoleType.MEMBER ) );
      }
      else if ( role.toString().equalsIgnoreCase( RoleTypeEnum.MANAGER.toString() ) )
      {
        userNode.setHierarchyRoleType( HierarchyRoleType.lookup( HierarchyRoleType.MANAGER ) );
      }
      else if ( role.toString().equalsIgnoreCase( RoleTypeEnum.OWNER.toString() ) )
      {
        userNode.setHierarchyRoleType( HierarchyRoleType.lookup( HierarchyRoleType.OWNER ) );
      }
    }

    node.setId( nodeService.getNodeIdByRosterNodeId( nodeId ) );
    userNode.setNode( node );
    Set userNodes = user.getUserNodes();

    // If this is first node added for user then set this node as primary
    if ( userNodes != null && userNodes.size() > 0 )
    {
      userNode.setIsPrimary( false );
    }
    else
    {
      userNode.setIsPrimary( true );
    }
    try
    {
      userService.addUserNode( user.getId(), userNode );
      audienceService.rematchParticipantForAllCriteriaAudiences( user.getId() );
    }
    catch( ServiceErrorException e )
    {
      throw new BadRequestException( e.getMessage() );
    }
    PersonDetailsRequest details = new PersonDetailsRequest();
    List<String> detailFields = new ArrayList<>();
    detailFields.add( PersonFieldEnum.NODES.getName() );
    details.setPersonProperties( detailFields );
    return buildPersonFullView( user, details, companyId, id );
  }

  @SuppressWarnings( "resource" )
  @Override
  public AttributeDescriptionView createPersonAttributeDescription( UUID companyId, CreatePersonAttributeDefinitionRequest request )
  {
    AttributeDescriptionView attributeDescription = new AttributeDescriptionView();
    if ( logger.isDebugEnabled() )
    {

      logger.debug( "companyId : " + companyId );
    }

    if ( request.getName().isEmpty() )
    {
      throw new BadRequestException( "Attribute Definition name cannot be blank" );
    }
    if ( request.getDataType().isEmpty() )
    {
      throw new BadRequestException( "Attribute Definition dataType cannot be blank" );
    }
    if ( request.getDataFormat().isEmpty() )
    {
      throw new BadRequestException( "Attribute Definition dataFormat cannot be blank" );
    }
    Characteristic characteristicType = new UserCharacteristicType();
    @SuppressWarnings( "unchecked" )
    List<Characteristic> allCharacterstics = userCharacteristicService.getAllCharacteristics();
    for ( Characteristic characteristic : allCharacterstics )
    {
      if ( characteristic.getCharacteristicName().equalsIgnoreCase( request.getName() ) )
      {
        throw new BadRequestException( "Attribute Description '" + request.getName() + " already exists" );
      }
    } // validate
    if ( !DataTypeEnum.isValidEnum( request.getDataType() ) )
    {
      throw new BadRequestException( "Provided Attribute Description has an invalid data type of '" + request.getDataType() + "'.  Valid values are "
          + buildEnumStringValues( DataTypeEnum.values() ) );
    }
    if ( !DataFormatEnum.isValidEnum( request.getDataFormat() ) )
    {
      throw new BadRequestException( "Provided Attribute Description has an invalid data format of '" + request.getDataFormat() + "'.  Valid values are "
          + buildEnumStringValues( DataFormatEnum.values() ) );
    }

    Scanner scanner = new Scanner( request.getDataType() );

    if ( scanner.hasNextBoolean() )
    {
      characteristicType.setCharacteristicDataType( CharacteristicDataType.lookup( CharacteristicDataType.BOOLEAN ) );
    }
    else if ( scanner.hasNextInt() )
    {
      characteristicType.setCharacteristicDataType( CharacteristicDataType.lookup( CharacteristicDataType.INTEGER ) );
    }
    else if ( scanner.hasNextFloat() || scanner.hasNextDouble() )
    {
      characteristicType.setCharacteristicDataType( CharacteristicDataType.lookup( CharacteristicDataType.DECIMAL ) );
    }
    else if ( isDate( request.getDataType() ) )
    {
      characteristicType.setCharacteristicDataType( CharacteristicDataType.lookup( CharacteristicDataType.DATE ) );
    }
    else
    {
      characteristicType.setCharacteristicDataType( CharacteristicDataType.lookup( CharacteristicDataType.TEXT ) );
      characteristicType.setMaxSize( new Long( 18 ) );// max value in database
    }
    characteristicType.setCharacteristicName( request.getName() );
    characteristicType.setVisibility( CharacteristicVisibility.lookup( CharacteristicVisibility.VISIBLE ) );
    characteristicType.setIsRequired( Boolean.FALSE );
    characteristicType.setActive( Boolean.TRUE );
    try
    {
      Characteristic char1 = userCharacteristicService.saveCharacteristic( characteristicType );
      attributeDescription.setName( char1.getCharacteristicName() );
      if ( char1.getCharacteristicDataType().getName().equalsIgnoreCase( "Text" ) )
      {
        attributeDescription.setDataType( "string" );
      }
      else
      {
        attributeDescription.setDataType( char1.getCharacteristicDataType().getName() );
      }
      if ( char1.getCharacteristicDataType().getCode().equalsIgnoreCase( CharacteristicDataType.lookup( CharacteristicDataType.TEXT ).getCode().toString() ) )
      {
        attributeDescription.setDataFormat( "string" );
      }
      else
      {
        attributeDescription.setDataFormat( char1.getCharacteristicDataType().getCode() );
      }
    }
    catch( ServiceErrorException e )
    {
      e.printStackTrace();
    }
    return attributeDescription;
  }

  private static String removeLastCharacterFromCountryCode( String s )
  {
    return s.substring( 0, s.length() - 1 );
  }

  @Override
  public PersonView updatePassword( UUID companyId, UUID id, ChangePasswordRequest request )
  {
    if ( logger.isDebugEnabled() )
    {
      logger.debug( "companyId : " + companyId + "rosterUserId :" + id );
    }

    User user = userService.getUserById( userService.getUserIdByRosterUserId( id ) );
    if ( Objects.nonNull( user ) && user.isActive() )
    {
      @SuppressWarnings( "unused" )
      String hashedPassword = new SHA256Hash().encrypt( request.getNewPassword(), false );
      try
      {
        profileService.changePassword( user.getId(), request.getNewPassword(), null, null, true );
      }
      catch( ServiceErrorException e )
      {
        throw new BadRequestException( e.getServiceErrorsCMText().toString() );
      }

      return buildPersonView( user );
    }
    else
    {
      throw new ResourceNotFoundException( "Person not found with given id : '" + user.getRosterUserId() + "'" );
    }
  }

}

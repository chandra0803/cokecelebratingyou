
package com.biperf.core.ui.user;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.biperf.core.domain.enums.EmailAddressType;
import com.biperf.core.domain.enums.PhoneType;
import com.biperf.core.domain.enums.VerificationStatusType;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.user.User;
import com.biperf.core.domain.user.UserEmailAddress;
import com.biperf.core.domain.user.UserPhone;
import com.biperf.core.exception.ExceptionView;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.security.AuthenticatedUser;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.message.MessageService;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.participant.PasswordResetService;
import com.biperf.core.service.participant.UserAssociationRequest;
import com.biperf.core.service.security.AuthenticationService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.utils.PhoneNumberValidator;
import com.biperf.core.utils.StringUtil;
import com.biperf.core.utils.UserManager;

@Controller
@RequestMapping( "/activate/v1" )
public class ParticipantActivationController extends BaseUserController
{

  private @Autowired ParticipantService participantService;
  private @Autowired AuthenticationService authenticationService;
  private @Autowired PasswordResetService passwordResetService;

  /**
   * This end point is used to determine if the Id for the user is Active in the system
   * @throws ServiceErrorException 
   * @throws InterruptedException 
   */
  @SuppressWarnings( "unchecked" )
  @RequestMapping( value = "/{username}/activated.action", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE )
  public @ResponseBody ResponseEntity<ExceptionView> isUserActivated( @PathVariable String username, HttpServletRequest request ) throws ServiceErrorException, InterruptedException
  {
    UserActivationView userView = new UserActivationView();
    AssociationRequestCollection associations = new AssociationRequestCollection();
    associations.add( new UserAssociationRequest( UserAssociationRequest.EMAIL ) );

    User user = userService.getUserByUserNameWithAssociations( username, associations );
    if ( null == user )
    {
      userView.setExists( false );
      return buildResponse( userView, Arrays.asList( buildCMSMessage( CM_KEY_PREFIX + "ACCOUNT_NOT_EXIST" ) ), HttpStatus.BAD_REQUEST );
    }

    userView.setNonPax( !user.isParticipant() );
    userView.setExists( true );

    if ( user.isParticipant() )
    {
      userView.setUserActivated( ( (Participant)user ).isActivationComplete() );
    }
    else
    {
      userView.setUserActivated( user.isActivationComplete() );
    }

    if ( userView.isUserActivated() )
    {
      return buildResponse( userView, Arrays.asList( buildCMSMessage( CM_KEY_PREFIX + "ACCOUNT_ALREADY_ACTIVE" ) ), HttpStatus.BAD_REQUEST );
    }

    // Facility for the termed user to do the account activation, to redeem his/her points.
    Participant pax = participantService.getParticipantById( user.getId() );

    // if the user has a unique email, then just send the message to them and proceed to sending the
    // activation link:
    UserEmailAddress address = userService.getPrimaryUserEmailAddress( user.getId() );
    if ( Objects.nonNull( address ) && userService.isUniqueEmail( address.getEmailAddr() ) && ( !user.isParticipant() || Objects.isNull( pax.getTerminationDate() ) ) )
    {
      ParticipantActivationLinkDestination dest = new ParticipantActivationLinkDestination();
      dest.setContactId( address.getId() );
      dest.setContactType( ContactType.EMAIL.toString() );
      return this.sendActivationLink( dest, request );
    }

    // Facility for the termed user to do the account activation, to redeem his/her points.
    if ( Objects.nonNull( pax ) ? isTermedUserAndInActive( pax ) : false )
    {
      if ( !systemVariableService.getPropertyByName( SystemVariableService.TERMED_USER_ALLOW_REDEEM ).getBooleanVal() )
      {
        return buildResponse( userView, Arrays.asList( buildCMSMessage( CM_KEY_PREFIX + "TERMED_USER_SHOPPING_OFF" ) ), HttpStatus.BAD_REQUEST );
      }
      else
      {
        if ( isTermedUserAllowToRedeem( pax ) )
        {
          userView.setActivationFields( buildActivationFields() );

          List<PaxContactType> paxContactType = participantService.getValidUserContactMethodsByUserId( user.getId() );

          if ( paxContactType != null && !paxContactType.isEmpty() )
          {
            userView.setContactMethods( maskContactMethods( addNoneOptionToPaxContactType( paxContactType ) ) );
          }

          return buildResponse( userView, HttpStatus.OK );
        }
        else
        {
          return buildResponse( userView, Arrays.asList( buildCMSMessage( CM_KEY_PREFIX + "TERMED_USER_SHOPPING_ON_NO_POINTS" ) ), HttpStatus.BAD_REQUEST );
        }
      }

    }

    userView.setActivationFields( buildActivationFields() );
    userView.setContactMethods( maskContactMethods( participantService.getValidUserContactMethodsByUserId( user.getId() ) ) );

    return buildResponse( userView, HttpStatus.OK );
  }

  /**
   * This end point is used to validate the Participant Identifier information for the user
   * @throws ServiceErrorException 
   */
  @RequestMapping( value = "/{username}/validateActivation.action", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE )
  public @ResponseBody ResponseEntity<ExceptionView> validateActivationFields( @PathVariable String username, @RequestBody List<ActivationField> activationFields, HttpServletRequest request )
      throws ServiceErrorException
  {
    UserActivationRecoveryView view = new UserActivationRecoveryView();
    User user = userService.getUserByUserName( username );
    // validate the input
    if ( activationFields != null && !activationFields.isEmpty() )
    {
      if ( !participantActivationService.isValidActivationFields( user.getId(), activationFields ) )
      {
        return buildResponse( view, Arrays.asList( buildCMSMessage( CM_KEY_PREFIX + "INVALID_ACTIVATION_FIELD" ) ), HttpStatus.BAD_REQUEST );
      }
    }
    // apply a token to the response. The front end will send this back to us to "verify" that the
    // user making the request for creating recovery info went through this validation
    // this is to thwart someone tinkering with the endn points (ie, sending in the username of
    // someone else, but passing in their own recovery methods
    view.setTokenValidation( generateSecurityToken( request ) );

    // do we have Recover methods available for this user?
    view.setHasRecoveryMethods( participantService.isParticipantRecoveryContactsAvailable( user.getId() ) );
    // add possible SMS options
    view.setCountryPhones( getSMSCountryList() );

    // Facility for the termed user to do the account activation, to redeem his/her points.
    Participant pax = participantService.getParticipantById( user.getId() );

    if ( ( Objects.nonNull( pax ) ? isTermedUserAndInActive( pax ) : false ) && ( Objects.nonNull( pax ) ? isTermedUserAllowToRedeem( pax ) : false ) )
    {
      List<PaxContactType> paxContactType = participantService.getValidUserContactMethodsByUserId( user.getId() );

      if ( paxContactType != null && !paxContactType.isEmpty() )
      {
        view.setContactMethods( maskContactMethods( addNoneOptionToPaxContactType( paxContactType ) ) );
      }
    }
    else
    {
      view.setContactMethods( maskContactMethods( participantService.getValidUserContactMethodsByUserId( user.getId() ) ) );
    }

    return buildResponse( view, Arrays.asList( buildCMSMessage( CM_KEY_PREFIX + "MULTIPLE_METHODS_OF_CONTACT" ) ), HttpStatus.OK );
  }

  // CHECKSTYLE:OFF
  @RequestMapping( value = "/{username}/contactsActivation.action", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE )
  public @ResponseBody ResponseEntity<? extends ExceptionView> activateContactMethods( @PathVariable String username,
                                                                                       @RequestBody UserContactActivationView contactActivation,
                                                                                       HttpServletRequest request )
      throws ServiceErrorException, InterruptedException
  {
    // CHECKSTYLE:ON
    if ( null == contactActivation.getContactEmail() && null == contactActivation.getContactPhone() )
    {
      return buildResponse( contactActivation, Arrays.asList( buildCMSMessage( CM_KEY_PREFIX + "MISSING_CONTACT" ) ), HttpStatus.BAD_REQUEST );
    }

    List<String> errors = validateContactMethods( contactActivation );

    if ( !errors.isEmpty() )
    {
      return buildResponse( contactActivation, errors, HttpStatus.BAD_REQUEST );
    }
    // NOTE: do we need to require the user to provide UNIQUE email or phone?
    User user = userService.getUserByUserName( username );
    // all is well, persist the contact methods
    saveContactMethods( user, contactActivation );

    ResponseEntity<ExceptionView> view = isUserActivated( username, request );

    if ( view.getBody() instanceof UserActivationView )
    {
      UserActivationView content = (UserActivationView)view.getBody();
      if ( content.getContactMethods().isEmpty() )
      {
        view.getBody().setResponseMessage( buildCMSMessage( CM_KEY_PREFIX + "NO_UNIQUE_CONTACT_METHODS" ) );
        view.getBody().setResponseCode( HttpStatus.BAD_REQUEST.value() );
      }
    }
    return view;
  }

  @RequestMapping( value = "/activationLink.action", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE )
  public @ResponseBody ResponseEntity<ExceptionView> sendActivationLink( @RequestBody ParticipantActivationLinkDestination activationDestination, HttpServletRequest request )
      throws ServiceErrorException, InterruptedException
  {

    // Facility for the termed user to do the account activation, to redeem his/her points.
    // The eMails and text has been send to contact methods.

    if ( activationDestination.getContactType().equalsIgnoreCase( "NONE" ) )
    {
      User user = userService.getUserById( activationDestination.getContactId() );

      UserEmailAddress address = userService.getPrimaryUserEmailAddress( user.getId() );

      if ( Objects.nonNull( address ) && Objects.nonNull( address.getEmailAddr() ) )
      {
        passwordResetService.resetPassword( user.getUserName(), null, null, MessageService.ACCOUNT_ACTIVATED );
      }

      userService.deleteUserEmailAddress( activationDestination.getContactId() );
      userService.deleteUserPhones( activationDestination.getContactId() );

      return buildResponse( getUserActivationRecoveryInfo( request, activationDestination.getContactId(), null ),
                            Arrays.asList( buildCMSMessage( CM_KEY_PREFIX + "MULTIPLE_METHODS_OF_CONTACT" ) ),
                            HttpStatus.OK );
    }

    // Facility for the termed user to do the account activation, to redeem his/her points.
    UserEmailAddress email = null;
    User user = null;
    if ( activationDestination.getContactType().equalsIgnoreCase( "PHONE" ) )
    {
      UserPhone userPhone = userService.getUserPhoneById( activationDestination.getContactId() );
      user = userService.getUserById( userPhone.getUser().getId() );
    }
    else
    {
      email = userService.getUserEmailAddressById( activationDestination.getContactId() );
      user = userService.getUserByUserName( email.getUser().getUserName() );
    }

    Participant pax = participantService.getParticipantById( user.getId() );

    List<String> messages = null;

    PaxContactType contactMethod = participantActivationService.sendActivationLinkToParticipant( activationDestination.getContactId(), buildContactType( activationDestination.getContactType() ) );

    if ( contactMethod.getContactType() == ContactType.EMAIL )
    {
      messages = Arrays.asList( MessageFormat.format( buildCMSMessage( CM_KEY_PREFIX + "MESSAGE_SENT_TO_EMAIL_ADDRESS" ), new Object[] { StringUtil.maskEmailAddress( contactMethod.getValue() ) } ) );

      if ( systemVariableService.getPropertyByName( SystemVariableService.TERMED_USER_ALLOW_REDEEM ).getBooleanVal() && ( Objects.nonNull( pax ) ? isTermedUserAndInActive( pax ) : false ) )
      {
        return buildResponse( getUserActivationRecoveryInfo( request, user.getId(), "EMAIL" ), messages, HttpStatus.OK );
      }
    }
    else if ( contactMethod.getContactType() == ContactType.PHONE )
    {
      messages = Arrays.asList( MessageFormat.format( buildCMSMessage( CM_KEY_PREFIX + "MESSAGE_SENT_TO_PHONE" ), new Object[] { StringUtil.maskPhoneNumber( contactMethod.getValue() ) } ) );

      if ( systemVariableService.getPropertyByName( SystemVariableService.TERMED_USER_ALLOW_REDEEM ).getBooleanVal() && ( Objects.nonNull( pax ) ? isTermedUserAndInActive( pax ) : false ) )
      {
        return buildResponse( getUserActivationRecoveryInfo( request, user.getId(), "PHONE" ), messages, HttpStatus.OK );
      }
    }

    return buildResponse( new ExceptionView(), messages, HttpStatus.OK );
  }

  @RequestMapping( value = "/countryPhones.action", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE )
  public @ResponseBody ResponseEntity<CountryPhoneListView> getCountryPhoneList()
  {
    CountryPhoneListView view = new CountryPhoneListView();
    view.setCountryPhones( getSMSCountryList() );
    return buildResponse( view, HttpStatus.OK );
  }

  @SuppressWarnings( "rawtypes" )
  @RequestMapping( value = "/registerRecoveryMethods.action", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE )
  public @ResponseBody ResponseEntity registerRecoveryMethods( @RequestBody UserContactActivationView contact, HttpServletRequest request ) throws ServiceErrorException
  {
    // verify the security to prevent malicious anonymous access to this end point
    if ( !isSecurityCheckValid( contact, request ) )
    {
      InvalidTokenView invalidTokenView = new InvalidTokenView();
      return buildResponse( invalidTokenView, Arrays.asList( buildCMSMessage( CM_KEY_PREFIX + "INVALID_USER" ) ), HttpStatus.BAD_REQUEST );
    }

    if ( null == contact.getContactEmail() && null == contact.getContactPhone() )
    {
      return buildResponse( contact, Arrays.asList( buildCMSMessage( CM_KEY_PREFIX + "MISSING_CONTACT" ) ), HttpStatus.BAD_REQUEST );
    }

    List<String> errors = validateContactMethods( contact );

    if ( !errors.isEmpty() )
    {
      return buildResponse( contact, errors, HttpStatus.BAD_REQUEST );
    }

    User user = buildUser( contact );

    if ( !StringUtils.isEmpty( contact.getContactEmail() ) )
    {
      UserEmailAddress recoveryEmail = buildUserEmailAddress( user, contact.getContactEmail() );
      recoveryEmail.setEmailType( EmailAddressType.lookup( EmailAddressType.RECOVERY ) );
      userService.addUserEmailAddress( user.getId(), recoveryEmail );
    }

    if ( !StringUtils.isEmpty( contact.getContactPhone() ) )
    {
      UserPhone recoveryPhone = buildUserPhone( user, contact );
      recoveryPhone.setPhoneType( PhoneType.lookup( PhoneType.RECOVERY ) );
      userService.addUserPhone( user.getId(), recoveryPhone );
    }
    // in one scenario, log the user in and forward to the home page, otherwise let the front end
    // decide
    if ( contact.isFinalLogin() )
    {
      authenticationService.loginAs( user.getUserName() );
      AuthenticatedUser authUser = UserManager.getUser();
      if ( null != authUser && !authUser.isLaunched() )
      {
        // check the first time login
        if ( !authUser.isProfileSetup() )
        {
          return buildFirstTimeLoginRedirectEntity( request );
        }
      }

      return buildHomePageRedirectEntity( request );
    }
    else
    {
      UserActivationRecoveryView view = new UserActivationRecoveryView();
      view.setExists( true );
      if ( user.isParticipant() )
      {
        view.setUserActivated( ( (Participant)user ).isActivationComplete() );
      }
      else
      {
        return buildResponse( view, Arrays.asList( buildCMSMessage( CM_KEY_PREFIX + "INVALID_USER" ) ), HttpStatus.BAD_REQUEST );
      }
      view.setHasRecoveryMethods( participantService.isParticipantRecoveryContactsAvailable( user.getId() ) );

      // Facility for the termed user to do the account activation, to redeem his/her points.
      Participant pax = participantService.getParticipantById( user.getId() );

      if ( ( Objects.nonNull( pax ) ? isTermedUserAndInActive( pax ) : false ) && ( Objects.nonNull( pax ) ? isTermedUserAllowToRedeem( pax ) : false ) )
      {

        List<PaxContactType> paxContactType = participantService.getValidUserContactMethodsByUserId( user.getId() );

        if ( paxContactType != null && !paxContactType.isEmpty() )
        {
          view.setContactMethods( maskContactMethods( addNoneOptionToPaxContactType( paxContactType ) ) );
        }

      }
      else
      {
        view.setContactMethods( maskContactMethods( participantService.getValidUserContactMethodsByUserId( user.getId() ) ) );
      }

      view.setCountryPhones( getSMSCountryList() );
      return buildResponse( view, HttpStatus.OK );
    }
  }

  private boolean isSecurityCheckValid( UserContactActivationView contact, HttpServletRequest request )
  {
    if ( UserManager.isUserLoggedIn() )
    {
      return true;
    }
    else
    {
      // check the session to make sure they went through the Participant Identifier Validation Flow
      String token = (String)request.getSession().getAttribute( SECURITY_TOKEN );
      if ( !Objects.isNull( token ) && contact.getTokenValidation().equals( token ) )
      {
        return true;
      }
    }
    return false;
  }

  private User buildUser( UserContactActivationView contact )
  {
    User user = null;
    if ( !Objects.isNull( UserManager.getUserId() ) )
    {
      return userService.getUserById( UserManager.getUserId() );
    }
    if ( !Objects.isNull( contact.getUserName() ) )
    {
      return userService.getUserByUserName( contact.getUserName() );
    }
    return user;
  }

  private ContactType buildContactType( String type )
  {
    if ( type.equalsIgnoreCase( ContactType.EMAIL.toString() ) )
    {
      return ContactType.EMAIL;
    }
    else if ( type.equalsIgnoreCase( ContactType.PHONE.toString() ) )
    {
      return ContactType.PHONE;
    }

    return null;
  }

  private void saveContactMethods( User user, UserContactActivationView contactActivation ) throws ServiceErrorException
  {
    if ( null != contactActivation.getContactEmail() )
    {
      saveUserEmailAddress( user, contactActivation.getContactEmail() );
    }
    if ( null != contactActivation.getContactPhone() )
    {
      saveUserPhone( user, contactActivation );
    }
  }

  private List<String> validateContactMethods( UserContactActivationView contactActivation )
  {
    List<String> errors = new ArrayList<String>();
    // validate email if exists
    if ( !StringUtils.isEmpty( contactActivation.getContactEmail() ) )
    {
      if ( !isValidEmail( contactActivation.getContactEmail() ) )
      {
        errors.add( buildCMSMessage( CM_KEY_PREFIX + "INVALID_EMAIL" ) );
      }
    }
    // validate phone if exists
    if ( !StringUtils.isEmpty( contactActivation.getContactPhone() ) )
    {
      if ( !PhoneNumberValidator.isValidPhoneNumberLength( contactActivation.getContactPhone() ) )
      {
        errors.add( buildCMSMessage( CM_KEY_PREFIX + "INVALID_PHONE" ) );
      }
    }
    return errors;
  }

  private void saveUserPhone( User user, UserContactActivationView contactActivation ) throws ServiceErrorException
  {
    UserPhone userPhone = buildUserPhone( user, contactActivation );
    userService.addUserPhone( user.getId(), userPhone );
  }

  private UserPhone buildUserPhone( User user, UserContactActivationView contactActivation )
  {
    UserPhone userPhone = new UserPhone();
    userPhone.setCountryPhoneCode( countryService.getCountryById( contactActivation.getCountryId() ).getCountryCode() );
    userPhone.setIsPrimary( false );
    userPhone.setPhoneNbr( contactActivation.getContactPhone() );
    userPhone.setUser( user );
    userPhone.setPhoneType( PhoneType.lookup( PhoneType.MOBILE ) );
    userPhone.setVerificationStatus( VerificationStatusType.lookup( VerificationStatusType.UNVERIFIED ) );
    return userPhone;
  }

  private void saveUserEmailAddress( User user, String email ) throws ServiceErrorException
  {
    UserEmailAddress emailAddress = buildUserEmailAddress( user, email );
    userService.addUserEmailAddress( user.getId(), emailAddress );
  }

  private UserEmailAddress buildUserEmailAddress( User user, String email )
  {
    UserEmailAddress emailAddress = new UserEmailAddress();
    emailAddress.setEmailAddr( email );
    emailAddress.setEmailType( EmailAddressType.lookup( EmailAddressType.OTHER ) );
    emailAddress.setIsPrimary( false );
    emailAddress.setVerificationStatus( VerificationStatusType.lookup( VerificationStatusType.UNVERIFIED ) );
    emailAddress.setUser( user );
    return emailAddress;
  }

  private List<PaxContactType> maskContactMethods( List<PaxContactType> contactMethods )
  {
    contactMethods.stream().filter( c -> c.getContactType() == ContactType.EMAIL ).forEach( c -> c.setValue( StringUtil.maskEmailAddress( c.getValue() ) ) );
    contactMethods.stream().filter( c -> c.getContactType() == ContactType.PHONE ).forEach( c -> c.setValue( StringUtil.maskPhoneNumber( c.getValue() ) ) );
    return contactMethods;
  }

  private boolean isValidEmail( String email )
  {
    if ( email == null || email.isEmpty() )
    {
      return false;
    }
    return EmailValidator.getInstance().isValid( email );
  }

  @SuppressWarnings( "serial" )
  private class CountryPhoneListView extends ExceptionView
  {
    private List<CountryPhoneView> countryPhones;

    @SuppressWarnings( "unused" )
    public List<CountryPhoneView> getCountryPhones()
    {
      return countryPhones;
    }

    public void setCountryPhones( List<CountryPhoneView> countryPhones )
    {
      this.countryPhones = countryPhones;
    }
  }

  private String generateSecurityToken( HttpServletRequest request )
  {
    String token = RandomStringUtils.randomAlphanumeric( 50 );
    request.getSession().setAttribute( SECURITY_TOKEN, token );
    return token;
  }

}

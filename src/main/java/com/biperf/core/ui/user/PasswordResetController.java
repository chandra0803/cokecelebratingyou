
package com.biperf.core.ui.user;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.biperf.core.domain.enums.ParticipantTermsAcceptance;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.user.User;
import com.biperf.core.domain.user.UserEmailAddress;
import com.biperf.core.domain.user.UserPhone;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.security.AuthenticatedUser;
import com.biperf.core.service.message.MessageService;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.participant.PasswordResetService;
import com.biperf.core.service.security.AuthenticationService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.strategy.PasswordRequirements;
import com.biperf.core.utils.StringUtil;
import com.biperf.core.utils.UserManager;
import com.objectpartners.cms.util.CmsResourceBundle;
import com.objectpartners.cms.util.ContentReaderManager;

@Controller
@RequestMapping( "/prelogin" )
public class PasswordResetController extends BaseUserController
{
  private static final String PASSWORD_REQUIRES_DISTINCT_TYPES_REQUIREMENT = "login.forgotpwd.DISTINCT_TYPES_REQUIREMENT";
  private static final String PASSWORD_REQUIRES_NUMERIC = "login.forgotpwd.NUMBER";
  private static final String PASSWORD_REQUIRES_UPPERCASE = "login.forgotpwd.UPPER_CASE";
  private static final String PASSWORD_REQUIRES_LOWERCASE = "login.forgotpwd.LOWER_CASE";
  private static final String PASSWORD_REQUIRES_SPECIAL_CHARACTERS = "login.forgotpwd.SPECIAL_SYMBOL";

  private @Autowired PasswordResetService passwordResetService;
  private @Autowired AuthenticationService authenticationService;
  private @Autowired ParticipantService participantService;
  private @Autowired SystemVariableService systemVariableService;

  @RequestMapping( value = "/contactAutocomplete.action", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE )
  public @ResponseBody ResponseEntity<PaxPasswordResetView> contactAutocomplete( @RequestBody PasswordReset model )
  {
    PaxPasswordResetView passwordResetView = passwordResetService.getContactsAutocomplete( model.getInitialQuery(), model.getEmailOrPhone() );
    maskContactMethods( passwordResetView.getContactMethods() );
    return buildResponse( passwordResetView, HttpStatus.OK );
  }

  /**
   * This end point is used to get the PAX details from DB based on email provided
   * @param model
   * @param httpRequest
   * @throws Exception
   */
  @RequestMapping( value = "/userByContactInformation.action", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE )
  public @ResponseBody ResponseEntity<PaxPasswordResetView> userByContactInformation( @RequestBody PasswordReset model )
  {
    PaxPasswordResetView passwordResetView = passwordResetService.getUserDetailsByContactInformation( model.getEmailOrPhone() );
    maskContactMethods( passwordResetView.getContactMethods() );
    // If contact not exists
    if ( !passwordResetView.isContactExists() )
    {
      // Placed HttpStatus code as 400 for this scenario, since FED is required to change color of
      // message as red.
      return buildResponse( passwordResetView, Arrays.asList( buildCMSMessage( "login.password.reset.errors.CONTACT_NOT_FOUND" ) ), HttpStatus.BAD_REQUEST );
    }
    else // Contact exists
    {
      if ( passwordResetView.isSingle() ) // One matching user
      {
        // More than one contact. Service won't set contact methods if there's only one.
        if ( !CollectionUtils.isEmpty( passwordResetView.getContactMethods() ) )
        {
          return buildResponse( passwordResetView, Arrays.asList( buildCMSMessage( "login.password.reset.errors.MULTIPLE_METHODS_OF_CONTACT" ) ), HttpStatus.OK );
        }
        // One user, one contact - automatically sent
        else
        {
          return buildResponse( passwordResetView,
                                Arrays.asList( MessageFormat.format( ContentReaderManager.getText( "login.password.reset.errors", "MESSAGE_SENT_TO_EMAIL_OR_PHONE_NUMBER" ),
                                                                     new Object[] { maskEmailOrPhone( model.getEmailOrPhone() ) } ) ),
                                HttpStatus.OK );
        }
      }
      else // Multiple matching users
      {
        // Still a unique contact method - automatically sent
        if ( passwordResetView.isUnique() )
        {
          return buildResponse( passwordResetView,
                                Arrays.asList( MessageFormat.format( ContentReaderManager.getText( "login.password.reset.errors", "MESSAGE_SENT_TO_EMAIL_OR_PHONE_NUMBER" ),
                                                                     new Object[] { maskEmailOrPhone( model.getEmailOrPhone() ) } ) ),
                                HttpStatus.OK );
        }
        // Not unique and no other ways of figure-out the PAX
        else if ( CollectionUtils.isEmpty( passwordResetView.getContactMethods() ) || passwordResetView.getContactMethods().size() == 1 )
        {
          return buildResponse( passwordResetView, Arrays.asList( buildCMSMessage( "login.password.reset.errors.MULTIPLE_ACCOUNTS_BY_EMAIL_OR_PHONE" ) ), HttpStatus.BAD_REQUEST );
        }
        // Not unique and there are ways to figure-out the actual PAX
        else
        {
          return buildResponse( passwordResetView, Arrays.asList( buildCMSMessage( "login.password.reset.errors.SELECT_ALTERNATE_CONTACT" ) ), HttpStatus.OK );
        }
      }
    }
  }

  /**
   * This end point is used to send the forgot Login ID email
   * @param model
   * @param httpRequest
   * @throws Exception
   */
  @RequestMapping( value = "/sendForgotUserIdNotification.action", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE )
  public @ResponseBody ResponseEntity<PaxPasswordResetView> sendForgotUserIdNotification( @RequestBody PasswordReset model )
  {
    ContactType contactType = buildContactType( model );
    PaxPasswordResetView passwordResetView = passwordResetService.sendForgotUserIdNotification( model.getContactId(), contactType, model.isSendMessage() );

    if ( model.isSendMessage() )
    {
      return buildResponse( passwordResetView,
                            Arrays.asList( MessageFormat.format( ContentReaderManager.getText( "login.password.reset.errors", "MESSAGE_SENT_TO_EMAIL_OR_PHONE_NUMBER" ),
                                                                 new Object[] { passwordResetView.getContactValue() } ) ),
                            HttpStatus.OK );
    }
    else
    {
      if ( !passwordResetView.isContactExists() )
      {
        return buildResponse( passwordResetView, Arrays.asList( buildCMSMessage( "login.password.reset.errors.CONTACT_NOT_FOUND" ) ), HttpStatus.OK );
      }
      else
      {
        if ( passwordResetView.isUnique() )
        {
          return buildResponse( passwordResetView,
                                Arrays.asList( MessageFormat.format( ContentReaderManager.getText( "login.password.reset.errors", "MESSAGE_SENT_TO_EMAIL_OR_PHONE_NUMBER" ),
                                                                     new Object[] { passwordResetView.getContactValue() } ) ),
                                HttpStatus.OK );
        }
        else if ( passwordResetView.isSingle() )
        {
          return buildResponse( passwordResetView, Arrays.asList( buildCMSMessage( "login.password.reset.errors.MULTIPLE_METHODS_OF_CONTACT" ) ), HttpStatus.OK );
        }
        else
        {
          return buildResponse( passwordResetView, Arrays.asList( buildCMSMessage( "login.password.reset.errors.SELECT_ALTERNATE_CONTACT" ) ), HttpStatus.OK );
        }
      }
    }
  }

  /**
   * This end point is used to send the forgot password
   * @param model
   * @param httpRequest
   * @throws Exception
   */
  @SuppressWarnings( "unused" )
  @RequestMapping( value = "/resetPasswordByEmail.action", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE )
  public @ResponseBody ResponseEntity<PaxPasswordResetView> resetPasswordByEmail( @RequestBody PasswordReset model )
  {
    PaxPasswordResetView passwordResetView = passwordResetService.resetPasswordByEmail( model.getEmail(), model.getUserName() );
    if ( Objects.nonNull( passwordResetView.getContactValue() ) )
    {

      if ( passwordResetView.getContactValue().equals( BaseUserController.TERMED_USER_SHOPPING_OFF ) )
      {

        return buildResponse( new PaxPasswordResetView(), Arrays.asList( CmsResourceBundle.getCmsBundle().getString( CM_KEY_PREFIX + "TERMED_USER_SHOPPING_OFF" ) ), HttpStatus.BAD_REQUEST );
      }

      if ( passwordResetView.getContactValue().equals( BaseUserController.TERMED_USER_SHOPPING_ON_NO_POINTS ) )
      {

        return buildResponse( new PaxPasswordResetView(), Arrays.asList( CmsResourceBundle.getCmsBundle().getString( CM_KEY_PREFIX + "TERMED_USER_SHOPPING_ON_NO_POINTS" ) ), HttpStatus.BAD_REQUEST );
      }
    }
    // User Name not found
    if ( passwordResetView == null )
    {
      return buildResponse( new PaxPasswordResetView(), Arrays.asList( buildCMSMessage( "login.password.reset.errors.USERNAME_NOT_FOUND" ) ), HttpStatus.BAD_REQUEST );
    }
    if ( passwordResetView.isAccountLocked() )
    {
      passwordResetView.setContactValue( "accountLock" );
      passwordResetView.setDeveloperMessage( buildRedirectAccountLock() );
      return buildResponse( passwordResetView, HttpStatus.OK );
    }
    if ( !passwordResetView.isUserActivated() )// must activate first
    {
      return buildResponse( passwordResetView, Arrays.asList( buildCMSMessage( "login.password.reset.errors.ACCOUNT_NOT_ACTIVATED" ) ), HttpStatus.OK );
    }
    if ( passwordResetView.getContactMethods().isEmpty() ) // Nothing we can do without contact
                                                           // methods
    {
      return buildResponse( new PaxPasswordResetView(), Arrays.asList( buildCMSMessage( "login.loginpage.HELP_TEXT" ) ), HttpStatus.BAD_REQUEST );
    }
    else
    {
      passwordResetView.setActivationFields( buildActivationFields() );
      return buildResponse( passwordResetView, HttpStatus.OK );
    }
  }

  @RequestMapping( value = "/passwordValidationRules.action", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE )
  public @ResponseBody ResponseEntity<PasswordValidationRules> getPasswordValidationRules()
  {
    PasswordRequirements requirements = passwordResetService.getPasswordValidationRules();
    PasswordValidationRules rules = new PasswordValidationRules( requirements );
    rules.setDistinctCharacterTypes( requirements.getDistinctCharacterTypesRequired() );
    rules.setLowerCase( new PasswordCheck( requirements.isLowerCaseAvailable(), buildCMSMessage( PASSWORD_REQUIRES_LOWERCASE ) ) );
    rules.setUpperCase( new PasswordCheck( requirements.isUpperCaseAvailable(), buildCMSMessage( PASSWORD_REQUIRES_UPPERCASE ) ) );
    rules.setNumeric( new PasswordCheck( requirements.isNumericAvailable(), buildCMSMessage( PASSWORD_REQUIRES_NUMERIC ) ) );
    rules.setSpecialCharacter( new PasswordCheck( requirements.isSpecialCharacterAvailable(), buildCMSMessage( PASSWORD_REQUIRES_SPECIAL_CHARACTERS ) ) );
    rules.setMinimumLengthCheck( new PasswordCheck( true, MessageFormat.format( buildCMSMessage( ServiceErrorMessageKeys.PASSWORD_TOO_SHORT ), new Object[] { rules.getMinLength() } ) ) );
    rules.setDistinctCharacterTypesCheck( new PasswordCheck( true,
                                                             MessageFormat.format( buildCMSMessage( PASSWORD_REQUIRES_DISTINCT_TYPES_REQUIREMENT ),
                                                                                   new Object[] { rules.getDistinctCharacterTypes() } ) ) );
    return buildResponse( rules, null, HttpStatus.OK );
  }

  /**
   * This end point is used to send the forgot Login ID email
   * @param model
   * @param httpRequest
   * @throws ServiceErrorException 
   * @throws Exception
   */
  @RequestMapping( value = "/userToken.action", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE )
  public @ResponseBody ResponseEntity<PaxPasswordResetView> userToken( @RequestBody PasswordReset model, HttpServletRequest request ) throws ServiceErrorException
  {
    // Facility for the termed user to do the account activation, to redeem his/her points.
    if ( model.getContactType().equalsIgnoreCase( "NONE" ) )
    {
      User user = userService.getUserById( model.getContactId() );

      UserEmailAddress address = userService.getPrimaryUserEmailAddress( user.getId() );
      if ( Objects.nonNull( address ) && Objects.nonNull( address.getEmailAddr() ) )
      {
        passwordResetService.resetPassword( user.getUserName(), null, null, MessageService.ACCOUNT_ACTIVATED );
      }

      UserActivationRecoveryView view = getUserActivationRecoveryInfo( request, model.getContactId(), null );

      userService.deleteUserEmailAddress( model.getContactId() );
      userService.deleteUserPhones( model.getContactId() );

      return buildResponse( view, Arrays.asList( buildCMSMessage( CM_KEY_PREFIX + "MULTIPLE_METHODS_OF_CONTACT" ) ), HttpStatus.OK );
    }

    PaxPasswordResetView passwordResetView = passwordResetService.sendUserToken( model.getContactId(), buildContactType( model ) );
    passwordResetView.setUserName( model.getUserName() );

    // Facility for the termed user to do the account activation, to redeem his/her points.
    UserEmailAddress email = null;
    User user = null;
    if ( model.getContactType().equalsIgnoreCase( "PHONE" ) )
    {
      UserPhone userPhone = userService.getUserPhoneById( model.getContactId() );
      user = userService.getUserById( userPhone.getUser().getId() );
    }
    else
    {
      email = userService.getUserEmailAddressById( model.getContactId() );
      user = userService.getUserByUserName( email.getUser().getUserName() );
    }
    Participant pax = participantService.getParticipantById( user.getId() );

    if ( model.getContactType().equals( ContactType.EMAIL.toString() ) )
    {
      if ( systemVariableService.getPropertyByName( SystemVariableService.TERMED_USER_ALLOW_REDEEM ).getBooleanVal() && ( Objects.nonNull( pax ) ? isTermedUserAndInActive( pax ) : false ) )
      {
        return buildResponse( getUserActivationRecoveryInfo( request, user.getId(), "EMAIL" ),
                              Arrays.asList( MessageFormat.format( ContentReaderManager.getText( "login.password.reset.errors", "MESSAGE_SENT_TO_EMAIL_ADDRESS" ),
                                                                   new Object[] { StringUtil.maskEmailAddress( passwordResetView.getContactValue() ) } ) ),
                              HttpStatus.OK );

      }
      return buildResponse( passwordResetView,
                            Arrays.asList( MessageFormat.format( ContentReaderManager.getText( "login.password.reset.errors", "MESSAGE_SENT_TO_EMAIL_ADDRESS" ),
                                                                 new Object[] { StringUtil.maskEmailAddress( passwordResetView.getContactValue() ) } ) ),
                            HttpStatus.OK );
    }
    else
    {
      if ( systemVariableService.getPropertyByName( SystemVariableService.TERMED_USER_ALLOW_REDEEM ).getBooleanVal() && ( Objects.nonNull( pax ) ? isTermedUserAndInActive( pax ) : false ) )
      {
        return buildResponse( getUserActivationRecoveryInfo( request, user.getId(), "PHONE" ),
                              Arrays.asList( MessageFormat.format( ContentReaderManager.getText( "login.password.reset.errors", "MESSAGE_SENT_TO_PHONE_NUMBER" ),
                                                                   new Object[] { StringUtil.maskPhoneNumber( passwordResetView.getContactValue() ) } ) ),
                              HttpStatus.OK );
      }
      return buildResponse( passwordResetView,
                            Arrays.asList( MessageFormat.format( ContentReaderManager.getText( "login.password.reset.errors", "MESSAGE_SENT_TO_PHONE_NUMBER" ),
                                                                 new Object[] { StringUtil.maskPhoneNumber( passwordResetView.getContactValue() ) } ) ),
                            HttpStatus.OK );
    }
  }

  /**
   * This end point is used to send the forgot password
   * @param model
   * @param httpRequest
   * @throws Exception
   */
  @SuppressWarnings( "rawtypes" )
  @RequestMapping( value = "/resetPasswordByToken.action", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE )
  public @ResponseBody ResponseEntity resetPasswordByToken( @RequestBody PasswordReset model, HttpServletRequest request )
  {
    PaxPasswordResetView passwordResetView = passwordResetService.validateToken( model.getToken() );
    if ( passwordResetView.isAccountLocked() )
    {
      return buildResponse( new PaxPasswordResetView(), Arrays.asList( buildCMSMessage( "login.errors.ACCOUNT_HARD_LOCKED_ERROR" ) ), HttpStatus.BAD_REQUEST );
    }
    passwordResetView.setActivationFields( buildActivationFields() );
    // Token Validity
    if ( passwordResetView.isValidToken() ) // check time the time stamp
    {
      return buildResponse( passwordResetView, HttpStatus.OK );
    }
    else
    {
      // if already used, forward user to the login page
      if ( passwordResetView.isTokenAlreadyUsed() )
      {
        ResponseEntity<PageRedirectMessage> response = buildHomePageRedirectEntity( request );
        if ( model.isFromEmail() )
        {
          response.getBody().getMessages().stream().findFirst().get().setText( buildCMSMessage( "login.password.reset.errors.TOKEN_ALREADY_USED_EMAIL" ) );
        }
        else
        {
          response.getBody().getMessages().stream().findFirst().get().setText( buildCMSMessage( "login.password.reset.errors.TOKEN_ALREADY_USED_SMS" ) );
        }
        return response;
      }
      if ( model.isFromEmail() )
      {
        return buildResponse( passwordResetView, Arrays.asList( buildCMSMessage( "login.loginpage.START_THE_PROCESS_OVER" ) ), HttpStatus.BAD_REQUEST );
      }
      else
      {
        return buildResponse( passwordResetView, Arrays.asList( buildCMSMessage( "login.password.reset.errors.TOKEN_NOT_VALID_SMS" ) ), HttpStatus.BAD_REQUEST );
      }
    }
  }

  /**
   * This end point is used to reset the forgot password
   * @param model
   * @param httpRequest
   * @throws IOException
   * @throws Exception
   */
  @SuppressWarnings( "rawtypes" )
  @RequestMapping( value = "/resetPassword.action", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE )
  public @ResponseBody ResponseEntity resetPassword( @RequestBody PasswordReset model, HttpServletRequest request, HttpServletResponse response ) throws IOException
  {

    try
    {
      passwordResetService.resetPassword( model.getUserName(), model.getPassword(), model.getToken(), model.isActivation() ? MessageService.ACCOUNT_ACTIVATED : MessageService.PASSWORD_CHANGE );
      // log the user in programmatically
      authenticationService.loginAs( model.getUserName() );

      AuthenticatedUser authenticatedUser = UserManager.getUser();
      Participant participant = participantService.getParticipantById( UserManager.getUserId() );

      // check the first time login - note that BI/Client admins skip the first time login
      // If either profile setup or TnCs need to be addressed, hit the first time login. Those two
      // are on the same page.
      if ( null != authenticatedUser && !authenticatedUser.isLaunched() && authenticatedUser.isParticipant() && participant.getTerminationDate() == null )
      {
        boolean isTermsAndConditionsUsed = systemVariableService.getPropertyByName( SystemVariableService.TERMS_CONDITIONS_USED ).getBooleanVal();
        boolean tncDenyAccess = false;
        if ( participant.getTermsAcceptance() == null || participant.getTermsAcceptance().getCode().equals( ParticipantTermsAcceptance.NOTACCEPTED )
            || participant.getTermsAcceptance().getCode().equals( ParticipantTermsAcceptance.DECLINED ) )
        {
          tncDenyAccess = true;
        }
        if ( !authenticatedUser.isProfileSetup() || ( isTermsAndConditionsUsed && tncDenyAccess ) )
        {
          return buildFirstTimeLoginRedirectEntity( request );
        }
      }
      // If user is inactive and has point balance, it should be redirected to catalog
      // If user is inactive and has no point balance, it should throw pax locked error
      boolean isPaxInactiveNoBalance = authenticationService.isPaxInactiveWithNoAwardBanQPoints( participant );
      if ( !Objects.isNull( authenticatedUser ) && authenticatedUser.isParticipant() && authenticatedUser.isPaxInactive() && !isPaxInactiveNoBalance )
      {
        return buildResponseForInActivePaxWithBalance( request, participant.getId() );
      }
      else if ( !Objects.isNull( authenticatedUser ) && authenticatedUser.isParticipant() && authenticatedUser.isPaxInactive() && isPaxInactiveNoBalance )
      {
        String paxLockedMessage = CmsResourceBundle.getCmsBundle().getString( "login.errors.PAX_LOCKED_ERROR" );
        return buildResponse( new PaxPasswordResetView(), new ArrayList<String>( Arrays.asList( paxLockedMessage ) ), HttpStatus.BAD_REQUEST );
      }
      return buildHomePageRedirectEntity( request );
    }
    catch( ServiceErrorException e )
    {
      return buildResponse( new PaxPasswordResetView(), e.getServiceErrorsCMText(), HttpStatus.BAD_REQUEST );
    }
  }

  private ContactType buildContactType( PasswordReset model )
  {
    return model.getContactType().equals( ContactType.PHONE.toString() ) ? ContactType.PHONE : ContactType.EMAIL;
  }

  private String maskEmailOrPhone( String contactMethod )
  {
    if ( !Objects.isNull( contactMethod ) )
    {
      // determine the type
      if ( contactMethod.indexOf( "@" ) > 0 )
      {
        return StringUtil.maskEmailAddress( contactMethod );
      }
      else
      {
        return StringUtil.maskPhoneNumber( contactMethod );
      }
    }
    return contactMethod;
  }

  private void maskContactMethods( List<PaxContactType> contactMethods )
  {
    // mask the contact info:
    if ( null != contactMethods )
    {
      contactMethods.stream().filter( c -> c.getContactType() == ContactType.EMAIL ).forEach( c -> c.setValue( StringUtil.maskEmailAddress( c.getValue() ) ) );
      contactMethods.stream().filter( c -> c.getContactType() == ContactType.PHONE ).forEach( c -> c.setValue( StringUtil.maskPhoneNumber( c.getValue() ) ) );
    }
  }

  private String buildRedirectAccountLock()
  {
    StringBuilder sb = new StringBuilder();
    sb.append( systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal() );
    sb.append( "/login.do?accountLock=true&key=true&isEmail=false" );
    return sb.toString();
  }

}

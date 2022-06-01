
package com.biperf.core.service.participant.impl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.biperf.core.dao.participant.UserTokenDAO;
import com.biperf.core.domain.country.Country;
import com.biperf.core.domain.enums.ParticipantStatus;
import com.biperf.core.domain.enums.PhoneType;
import com.biperf.core.domain.enums.UserTokenStatusType;
import com.biperf.core.domain.mailing.Mailing;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.user.User;
import com.biperf.core.domain.user.UserEmailAddress;
import com.biperf.core.domain.user.UserPhone;
import com.biperf.core.domain.user.UserToken;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.exception.ServiceErrorExceptionWithRollback;
import com.biperf.core.service.awardbanq.AwardBanQServiceFactory;
import com.biperf.core.service.country.CountryService;
import com.biperf.core.service.email.MailingService;
import com.biperf.core.service.email.PersonalizationService;
import com.biperf.core.service.message.MessageService;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.participant.PasswordResetService;
import com.biperf.core.service.participant.ProfileService;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.strategy.PasswordPolicyStrategy;
import com.biperf.core.strategy.PasswordRequirements;
import com.biperf.core.strategy.usertoken.UserTokenFactory;
import com.biperf.core.strategy.usertoken.UserTokenType;
import com.biperf.core.ui.user.BaseUserController;
import com.biperf.core.ui.user.ContactType;
import com.biperf.core.ui.user.PaxContactType;
import com.biperf.core.ui.user.PaxPasswordResetView;
import com.biperf.core.utils.Environment;
import com.biperf.core.utils.StringUtil;
import com.biperf.core.utils.crypto.SHA256Hash;
import com.objectpartners.cms.util.CmsResourceBundle;

public class PasswordResetServiceImpl implements PasswordResetService
{
  /** Maximum number of results before user has to filter contact results */
  public static final int SEARCH_AUTOCOMPLETE_LIMIT = 12;

  private static final Log logger = LogFactory.getLog( PasswordResetServiceImpl.class );

  private SystemVariableService systemVariableService;
  private UserService userService;
  private MailingService mailingService;
  private UserTokenDAO userTokenDAO;
  private ProfileService profileService;
  private ParticipantService participantService;
  private CountryService countryService;
  private PasswordPolicyStrategy passwordPolicyStrategy;
  private UserTokenFactory userTokenFactory;
  private PersonalizationService personalizationService;
  private @Autowired AwardBanQServiceFactory awardBanQServiceFactory;

  @Override
  public PasswordRequirements getPasswordValidationRules()
  {
    return passwordPolicyStrategy.getPasswordRequirements();
  }

  @Override
  public UserToken generateTokenAndSave( Long paxID, UserTokenType type )
  {
    UserToken userToken = userTokenFactory.getStrategy( type ).generateUserToken( paxID );
    SHA256Hash.setDefaultUpperCase( !getSystemVariableService().getPropertyByName( SystemVariableService.PASSWORD_SHOULD_USE_REGEX ).getBooleanVal() );
    userToken.setToken( new SHA256Hash().encryptDefault( userToken.getToken() ) );
    userTokenDAO.saveUserToken( userToken );

    if ( logger.isDebugEnabled() )
    {
      logger.debug( "Generated user token with code: " + userToken.getUnencryptedTokenValue() );
    }

    return userToken;
  }

  @Override
  public PaxPasswordResetView getUserDetailsByContactInformation( String emailOrPhone )
  {
    List<PaxContactType> searchContactResults = participantService.getAdditionalContactMethodsByEmailOrPhone( emailOrPhone );
    PaxContactType inputResult = searchContactResults.stream().filter( PaxContactType::isInputContact ).findFirst().orElse( null );

    PaxPasswordResetView passwordResetView = new PaxPasswordResetView();

    if ( CollectionUtils.isEmpty( searchContactResults ) || inputResult == null )
    {
      passwordResetView.setContactExists( false ); // Contact does not exist error
    }
    else if ( inputResult.isUnique() )
    {
      passwordResetView.setContactExists( true );
      passwordResetView.setSingle( true );
      Long userId = inputResult.getUserId();

      List<PaxContactType> validContactMethods = participantService.getValidUserContactMethodsByUserId( userId );
      List<Long> paxIds = new ArrayList<Long>();
      passwordResetView.setTotalResults( validContactMethods.size() );
      // If No Multiple contacts, send out an email or txt
      if ( validContactMethods.size() == 1 )
      {
        paxIds.add( userId );
        passwordResetView.setUnique( true );
        sendForgotUserIdNotification( validContactMethods.get( 0 ).getContactId(), validContactMethods.get( 0 ).getContactType() );
      }
      else // Multiple Contacts available
      {
        passwordResetView.setContactMethods( validContactMethods );
      }
    }
    else
    {
      List<PaxContactType> paxContactMethods = participantService.getAdditionalContactMethodsByEmailOrPhone( emailOrPhone );
      // Email results shouldn't be case sensitive, here. Doing on backend to avoid possible
      // complications with the procedures being used several places
      Map<String, PaxContactType> lowercaseContactMethods = new LinkedHashMap<>( paxContactMethods.size() );
      paxContactMethods.forEach( contactMethod -> lowercaseContactMethods.put( contactMethod.getValue().toLowerCase(), contactMethod ) );
      paxContactMethods = new ArrayList<>( lowercaseContactMethods.values() );
      if ( CollectionUtils.isEmpty( paxContactMethods ) )
      {
        passwordResetView.setContactExists( false );
      }
      // If there's only one specific contact method that matches - then just send the message. No
      // need to have the user select the contact method
      else if ( paxContactMethods.size() == 1 && paxContactMethods.get( 0 ).isUnique() )
      {
        PaxContactType contact = paxContactMethods.get( 0 );
        sendForgotUserIdNotification( contact.getContactId(), contact.getContactType() );
        passwordResetView.setContactExists( true );
        passwordResetView.setUnique( true ); // this is actually a unique value in this case
        passwordResetView.setTotalResults( paxContactMethods.size() );
      }
      else
      {
        Set<Long> paxIDs = paxContactMethods.stream().map( c -> c.getUserId() ).collect( Collectors.toSet() );
        passwordResetView.setPaxIDs( paxIDs );
        passwordResetView.setContactMethods( paxContactMethods );
        passwordResetView.setContactExists( true );
        passwordResetView.setUnique( false );
        passwordResetView.setTotalResults( paxContactMethods.size() );

        // The record limit is hardcoded for now. Can be changed to a system variable if desired.
        if ( paxContactMethods.get( 0 ).getTotalRecords().longValue() > SEARCH_AUTOCOMPLETE_LIMIT )
        {
          passwordResetView.setShowAutocomplete( true );
        }
      }
    }

    return passwordResetView;
  }

  @Override
  public PaxPasswordResetView getContactsAutocomplete( String initialQuery, String searchQuery )
  {
    PaxPasswordResetView passwordResetView = new PaxPasswordResetView();

    List<PaxContactType> paxContactMethods = participantService.getContactsAutocomplete( initialQuery, searchQuery );
    passwordResetView.setContactMethods( paxContactMethods );
    passwordResetView.setShowAutocomplete( true );
    if ( paxContactMethods != null )
    {
      passwordResetView.setTotalResults( paxContactMethods.size() );
    }

    return passwordResetView;
  }

  private void submitForgotLoginIdNotification( Long userId, PaxContactType contactType )
  {
    Mailing forgotLoginMailing = getMailingService().buildPAXForgotLoginIDNotification( userId, contactType, null );
    getMailingService().submitMailing( forgotLoginMailing, null, userId );
  }

  private PaxContactType buildEmailPaxContactType( UserEmailAddress emailAddress )
  {
    PaxContactType paxContactType = new PaxContactType();
    paxContactType.setContactType( ContactType.EMAIL );
    paxContactType.setValue( StringUtil.maskEmailAddress( emailAddress.getEmailAddr() ) );
    paxContactType.setContactId( emailAddress.getId() );
    paxContactType.setUnique( getUserService().isUniqueEmail( emailAddress.getEmailAddr() ) );
    return paxContactType;
  }

  private PaxContactType buildPhonePaxContactType( UserPhone phone )
  {
    PaxContactType paxContactType = new PaxContactType();
    paxContactType.setContactType( ContactType.PHONE );
    paxContactType.setValue( StringUtil.maskPhoneNumber( phone.getPhoneNbr() ) );
    paxContactType.setContactId( phone.getId() );
    paxContactType.setUnique( getUserService().isUniquePhoneNumber( phone.getPhoneNbr() ) );
    return paxContactType;
  }

  public SystemVariableService getSystemVariableService()
  {
    return systemVariableService;
  }

  public void setSystemVariableService( SystemVariableService systemVariableService )
  {
    this.systemVariableService = systemVariableService;
  }

  public UserService getUserService()
  {
    return userService;
  }

  public void setUserService( UserService userService )
  {
    this.userService = userService;
  }

  public UserTokenDAO getuserTokenDAO()
  {
    return userTokenDAO;
  }

  public void setUserTokenDAO( UserTokenDAO userTokenDAO )
  {
    this.userTokenDAO = userTokenDAO;
  }

  public MailingService getMailingService()
  {
    return mailingService;
  }

  public void setMailingService( MailingService mailingService )
  {
    this.mailingService = mailingService;
  }

  public ProfileService getProfileService()
  {
    return profileService;
  }

  public void setProfileService( ProfileService profileService )
  {
    this.profileService = profileService;
  }

  @Override
  public PaxPasswordResetView sendForgotUserIdNotification( Long contactId, ContactType contactType, boolean sendFlag )
  {
    if ( sendFlag )
    {
      return sendForgotUserIdNotification( contactId, contactType );
    }

    PaxPasswordResetView passwordResetView = new PaxPasswordResetView();
    List<PaxContactType> validContactMethods = new ArrayList<>();
    boolean uniqueContact = false;
    if ( contactType == ContactType.EMAIL )
    {
      UserEmailAddress email = userService.getUserEmailAddressById( contactId );
      validContactMethods = participantService.getValidUserContactMethodsByEmail( email.getEmailAddr() );
      uniqueContact = getUserService().isUniqueEmail( email.getEmailAddr() );
    }
    else
    {
      UserPhone userPhone = getUserService().getUserPhoneById( contactId );
      validContactMethods = participantService.getValidUserContactMethodsByPhone( userPhone.getPhoneNbr() );
      uniqueContact = getUserService().isUniquePhoneNumber( userPhone.getPhoneNbr() );
    }

    validContactMethods.parallelStream().forEach( validContactMethod ->
    {
      if ( validContactMethod.getContactType() == ContactType.EMAIL )
      {
        validContactMethod.setValue( StringUtil.maskEmailAddress( validContactMethod.getValue() ) );
      }
      else if ( validContactMethod.getContactType() == ContactType.PHONE )
      {
        validContactMethod.setValue( StringUtil.maskPhoneNumber( validContactMethod.getValue() ) );
      }
    } );

    if ( validContactMethods == null || validContactMethods.isEmpty() )
    {
      passwordResetView.setContactExists( false );
    }
    else if ( validContactMethods.size() == 1 )
    {
      return sendForgotUserIdNotification( validContactMethods.get( 0 ).getContactId(), validContactMethods.get( 0 ).getContactType() );
    }
    else
    {
      // Email results shouldn't be case sensitive, here. Doing on backend to avoid possible
      // complications with the procedures being used several places
      Map<String, PaxContactType> lowercaseContactMethods = new LinkedHashMap<>( validContactMethods.size() );
      validContactMethods.forEach( contactMethod -> lowercaseContactMethods.put( contactMethod.getValue().toLowerCase(), contactMethod ) );
      validContactMethods = new ArrayList<>( lowercaseContactMethods.values() );
      passwordResetView.setContactExists( true );
      passwordResetView.setSingle( uniqueContact ); // Okay, so the naming is a confusing. A single
                                                    // user. It's a unique contact, so it's a single
                                                    // user.
      passwordResetView.setUnique( false ); // If we send unique as true, upstream will think we've
                                            // already sent the message
      passwordResetView.setContactMethods( validContactMethods );
      if ( validContactMethods.get( 0 ).getTotalRecords().longValue() > SEARCH_AUTOCOMPLETE_LIMIT )
      {
        passwordResetView.setShowAutocomplete( true );
      }
    }

    return passwordResetView;
  }

  private PaxPasswordResetView sendForgotUserIdNotification( Long contactId, ContactType contactType )
  {
    PaxPasswordResetView passwordResetView = new PaxPasswordResetView();
    passwordResetView.setContactExists( true );
    passwordResetView.setEmailExists( true );
    passwordResetView.setUnique( true );
    User user = null;
    boolean sharedPrimaryContact = false;
    boolean isEmailContact = contactType == ContactType.EMAIL;
    if ( isEmailContact )
    {
      UserEmailAddress email = userService.getUserEmailAddressById( contactId );
      sharedPrimaryContact = userService.getUserIdsByEmailOrPhone( email.getEmailAddr() ).size() > 1;
      user = email.getUser();
      PaxContactType paxContactType = new PaxContactType();
      paxContactType.setContactId( contactId );
      paxContactType.setContactType( ContactType.EMAIL );
      paxContactType.setUserId( email.getUser().getId() );
      submitForgotLoginIdNotification( email.getUser().getId(), paxContactType );
      passwordResetView.setContactValue( StringUtil.maskEmailAddress( email.getEmailAddr() ) );
    }
    else
    {
      UserPhone userPhone = getUserService().getUserPhoneById( contactId );
      user = userPhone.getUser();
      String message = CmsResourceBundle.getCmsBundle().getString( MessageService.FORGOT_LOGIN_ID, "TEXT_MSG" );
      String loginid = userPhone.getUser().getUserName();
      String programUrl = systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();
      String formatedMesg = message.replace( "${loginid}", loginid );
      formatedMesg = formatedMesg.replace( "${programUrl}", getMailingService().getShortUrl( programUrl ) );
      getMailingService().sendSmsMessage( userPhone.getUser().getId(), userPhone.getCountryPhoneCode(), userPhone.getPhoneNbr(), formatedMesg );
      passwordResetView.setContactValue( StringUtil.maskPhoneNumber( userPhone.getPhoneNbr() ) );
    }
    passwordResetView.setUserExists( true );
    // now alert all other methods that the user id
    for ( UserEmailAddress email : user.getUserEmailAddresses() )
    {
      Mailing mailing = getMailingService().buildPaxForgotLoginIDSentNotification( email, isEmailContact, sharedPrimaryContact );
      getMailingService().submitMailing( mailing, null, user.getId() );
    }
    // alert all mobile phones
    for ( UserPhone phone : user.getUserPhones() )
    {
      if ( phone.getPhoneType().equals( PhoneType.lookup( PhoneType.MOBILE ) ) || phone.getPhoneType().equals( PhoneType.lookup( PhoneType.RECOVERY ) ) )
      {
        String txtMessage = buildPaxForgotLoginIDSentNotificationSMSMessage( phone, isEmailContact );
        getMailingService().sendSmsMessage( phone.getUser().getId(), phone.getCountryPhoneCode(), phone.getPhoneNbr(), txtMessage );
      }
    }

    return passwordResetView;
  }

  private String buildPaxForgotLoginIDSentNotificationSMSMessage( UserPhone userPhone, boolean isEmail )
  {
    Map<String, Object> dataMap = new HashMap<String, Object>();
    dataMap.put( "programName", getSystemVariableService().getPropertyByName( SystemVariableService.CLIENT_NAME ).getStringVal() );
    dataMap.put( "isEmail", isEmail ? "TRUE" : "FALSE" );
    dataMap.put( "programUrl",
                 getMailingService().getShortUrl( systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal() + "/login.do?forgotPass=true" ) );
    return getPersonalizationService().processMessage( dataMap,
                                                       "Forgot Login ID Request Notification",
                                                       CmsResourceBundle.getCmsBundle().getString( MessageService.FORGOT_LOGIN_ID_ALERT, "TEXT_MSG" ) );
  }

  @Override
  public PaxPasswordResetView resetPasswordByEmail( String email, String userName )
  {
    PaxPasswordResetView passwordResetView = new PaxPasswordResetView();

    User user = getUserService().getUserByUserName( userName );
    // User not found
    if ( user == null )
    {
      return null;
    }
    else
    {
      passwordResetView.setUserExists( true );
      passwordResetView.setUserName( user.getUserName() );
    }
    Set<UserEmailAddress> userEmailAddressSet = user.getUserEmailAddresses();

    List<PaxContactType> contactMethods = new ArrayList<PaxContactType>();
    // collect emails
    userEmailAddressSet.stream().forEach( e -> contactMethods.add( buildEmailPaxContactType( e ) ) );
    // collect phones
    contactMethods.addAll( buildPaxUserPhones( user ) );

    passwordResetView.setContactMethods( contactMethods );

    // Unique flag for this call is if the primary email is unique
    UserEmailAddress emailAddress = user.getPrimaryEmailAddress();
    if ( null != emailAddress )
    {
      boolean uniquePrimaryEmail = getUserService().isUniqueEmail( emailAddress.getEmailAddr() );
      passwordResetView.setUnique( uniquePrimaryEmail );
    }
    Participant pax = null;
    if ( user instanceof Participant )
    {
      pax = (Participant)user;
    }

    if ( isTermedUserAndInactive( pax ) )
    {
      if ( !systemVariableService.getPropertyByName( SystemVariableService.TERMED_USER_ALLOW_REDEEM ).getBooleanVal() )
      {
        PaxPasswordResetView passwordResetViewOne = new PaxPasswordResetView();
        passwordResetViewOne.setContactValue( BaseUserController.TERMED_USER_SHOPPING_OFF );
        return passwordResetViewOne;
      }
      else
      {
        if ( isTermedUserAllowToRedeem( pax ) )
        {
          passwordResetView.setNonPax( isNonPaxUser( user ) );
          passwordResetView.setAccountLocked( user.isAccountLocked() );
          passwordResetView.setUserActivated( user.isActivationComplete() );
          passwordResetView.setUnique( Boolean.FALSE );
          return passwordResetView;

        }
        else
        {
          PaxPasswordResetView passwordResetViewOne = new PaxPasswordResetView();
          passwordResetViewOne.setContactValue( BaseUserController.TERMED_USER_SHOPPING_ON_NO_POINTS );
          return passwordResetViewOne;

        }
      }

    }

    // Admin users will skip activation attributes
    passwordResetView.setNonPax( isNonPaxUser( user ) );
    passwordResetView.setAccountLocked( user.isAccountLocked() );
    passwordResetView.setUserActivated( user.isActivationComplete() );
    return passwordResetView;
  }

  /** A little backwards, but helps us obscure a field from users. True if they are an admin type user */
  private boolean isNonPaxUser( User user )
  {
    return !user.isParticipant();
  }

  private List<PaxContactType> buildPaxUserPhones( User user )
  {
    List<PaxContactType> validPhones = new ArrayList<PaxContactType>();
    Set<UserPhone> phones = user.getUserPhones();
    for ( UserPhone phone : phones )
    {
      PhoneType type = phone.getPhoneType();
      if ( type.getCode().equals( PhoneType.MOBILE ) || type.getCode().equals( PhoneType.RECOVERY ) )
      {
        Country country = countryService.getCountryByCode( phone.getCountryPhoneCode() );
        if ( Objects.nonNull( country ) ? country.isSmsCapable() : false )
        {
          validPhones.add( buildPhonePaxContactType( phone ) );
        }
      }
    }
    return validPhones;
  }

  private void useToken( UserToken token )
  {
    // update the Token to used
    token.setStatus( UserTokenStatusType.lookup( UserTokenStatusType.USED ) );
    userTokenDAO.saveUserToken( token );
  }

  @Override
  public PaxPasswordResetView validateToken( String token )
  {
    PaxPasswordResetView passwordResetView = new PaxPasswordResetView();
    boolean validToken = false;

    // Token status must be issued.
    UserToken userToken = userTokenDAO.getTokenById( new SHA256Hash().encryptDefault( token ) );

    if ( userToken == null ) // invalid token
    {
      return passwordResetView;
    }

    User user = userToken.getUser();

    if ( user != null )
    {
      passwordResetView.setAccountLocked( user.isAccountLocked() );
      passwordResetView.setUserExists( true );
      if ( user.getPrimaryEmailAddress() != null )
      {
        passwordResetView.setEmailExists( true );
        if ( getUserService().isUniqueEmail( user.getPrimaryEmailAddress().getEmailAddr() ) )
        {
          passwordResetView.setUnique( true );
        }
      }
      passwordResetView.setUserName( user.getUserName() );
    }

    // Check Token existence
    boolean tokenExists = userTokenDAO.validateToken( user.getId(), new SHA256Hash().encryptDefault( token ) );
    if ( !tokenExists )
    {
      passwordResetView.setValidToken( validToken );
      return passwordResetView;
    }

    if ( !userToken.getStatus().getCode().equals( UserTokenStatusType.ISSUED ) )
    {
      passwordResetView.setValidToken( validToken );
      // has it been used already?
      if ( userToken.getStatus().getCode().equals( UserTokenStatusType.USED ) )
      {
        passwordResetView.setTokenAlreadyUsed( true );
      }
      return passwordResetView;
    }

    if ( Instant.now().isAfter( userToken.getExpirationDate().toInstant() ) )
    {
      // update token as expired.
      userToken.setStatus( UserTokenStatusType.lookup( UserTokenStatusType.EXPIRED ) );
      userTokenDAO.saveUserToken( userToken );
      // set the token as invalid
      passwordResetView.setValidToken( validToken );
      return passwordResetView;
    }

    // Valid token
    validToken = true;
    passwordResetView.setValidToken( validToken );
    return passwordResetView;
  }

  @Override
  public void resetPassword( String userName, String password, String token, String message ) throws ServiceErrorException
  {
    User user = getUserService().getUserByUserName( userName );

    // verify that the username/loginId assigned to this token matches the one passed in
    if ( Objects.isNull( user ) || ( token == null ) ? false : !userTokenDAO.getTokenById( new SHA256Hash().encryptDefault( token ) ).getUser().getUserName().equalsIgnoreCase( userName ) )
    {
      throw new ServiceErrorExceptionWithRollback( "login.password.reset.errors.USERNAME_NOT_FOUND" );
    }

    // Facility for the termed user to do the account activation, to redeem his/her points.
    // The condition placed no need do the below process for termed users.
    // As we're sending password and token is null.

    if ( Objects.nonNull( password ) && Objects.nonNull( token ) )
    {
      profileService.changePassword( user.getId(), password, null, null, true );
      UserToken userToken = userTokenDAO.getTokenById( new SHA256Hash().encryptDefault( token ) );
      useToken( userToken );
      UserEmailAddress primaryEmail = user.getPrimaryEmailAddress();
      boolean sharedPrimaryContact = ( null == primaryEmail ) ? false : userService.getUserIdsByEmailOrPhone( primaryEmail.getEmailAddr() ).size() > 1;
      Mailing passwordChangeMailing = getMailingService().buildAccountOrPasswordChangeNotification( user.getId(), sharedPrimaryContact, message );
      getMailingService().submitMailing( passwordChangeMailing, null, user.getId() );
    }
    else
    {
      try
      {
        Mailing passwordChangeMailing = getMailingService().buildAccountOrPasswordChangeNotification( user.getId(), true, message );
        passwordChangeMailing = mailingService.submitMailingWithoutScheduling( passwordChangeMailing, null );
        getMailingService().processMailing( passwordChangeMailing.getId() );
      }
      catch( Exception e )
      {
        logger.error( "Sending eMails :" + e.getMessage() );
        throw new ServiceErrorExceptionWithRollback( "profile.personal.info.PLEASE_TRY_LATER" );
      }
    }
    Set<UserPhone> userPhoneSet = user.getUserPhones();
    List<UserPhone> userPhoneList = userPhoneSet.stream().collect( Collectors.toList() );
    List<UserPhone> distinctUserPhone = userPhoneList.stream().filter( distinctByKey( e -> e.getPhoneNbr() ) ).collect( Collectors.toList() );
    // alert all mobile phones
    for ( UserPhone phone : distinctUserPhone )
    {
      if ( phone.getPhoneType().equals( PhoneType.lookup( PhoneType.MOBILE ) ) || phone.getPhoneType().equals( PhoneType.lookup( PhoneType.RECOVERY ) ) )
      {
        String txtMessage = mailingService.buildAccountOrPasswordChangeText( phone.getUser(), message );
        getMailingService().sendSmsMessage( phone.getUser().getId(), phone.getCountryPhoneCode(), phone.getPhoneNbr(), txtMessage );
      }
    }
  }

  @Override
  public PaxPasswordResetView sendUserToken( Long contactId, ContactType contactType )
  {
    PaxPasswordResetView passwordResetView = new PaxPasswordResetView();
    passwordResetView.setEmailExists( true );
    passwordResetView.setUnique( true );

    UserToken userToken = null;

    if ( contactType == ContactType.EMAIL )
    {
      UserEmailAddress userEmailAddress = getUserService().getUserEmailAddressById( contactId );
      // generate token and save it.
      userToken = generateTokenAndSave( userEmailAddress.getUser().getId(), UserTokenType.EMAIL );
      Mailing forgotPasswordMailing = getMailingService().buildPAXForgotPasswordNotification( userEmailAddress.getUser().getId(),
                                                                                              buildEmailPaxContactType( userEmailAddress ),
                                                                                              userToken.getUnencryptedTokenValue() );
      getMailingService().submitMailing( forgotPasswordMailing, null, userEmailAddress.getUser().getId() );
      passwordResetView.setContactValue( userEmailAddress.getEmailAddr() );
    }
    else
    {
      UserPhone userPhone = null;
      String environment = Environment.getEnvironment();

      userPhone = getUserService().getUserPhoneById( contactId );
      String message = CmsResourceBundle.getCmsBundle().getString( MessageService.FORGOT_PASSWORD, "TEXT_MSG" );
      // generate token and save it.
      userToken = generateTokenAndSave( userPhone.getUser().getId(), UserTokenType.PHONE );
      String programName = systemVariableService.getPropertyByName( SystemVariableService.CLIENT_NAME ).getStringVal();
      String formatedMesg = message.replace( "${userToken}", userToken.getUnencryptedTokenValue() );
      formatedMesg = formatedMesg.replace( "${programName}", programName );

      if ( Environment.ENV_PROD.equals( environment ) || Environment.ENV_PRE.equals( environment ) )
      {
        getMailingService().sendSmsMessage( userPhone.getUser().getId(), userPhone.getCountryPhoneCode(), userPhone.getPhoneNbr(), formatedMesg );

      }
      passwordResetView.setContactValue( userPhone.getPhoneNbr() );
    }
    return passwordResetView;
  }

  // Facility for the termed user to do the account activation, to redeem his/her points.
  protected boolean isTermedUserAllowToRedeem( Participant pax )
  {
    boolean isTermedUserAllowToRedeemSysVal = systemVariableService.getPropertyByName( SystemVariableService.TERMED_USER_ALLOW_REDEEM ).getBooleanVal();
    // Get awardBanQ Balance for the Pax
    Long balance = awardBanQServiceFactory.getAwardBanQService().getAccountBalanceForParticipantId( pax.getId() );

    if ( isTermedUserAllowToRedeemSysVal && Objects.nonNull( balance ) && balance.longValue() > 0 )
    {
      return true;
    }

    return false;
  }

  @Override
  public void purgeUserTokens()
  {
    userTokenDAO.purgeUserTokens();
  }

  public ParticipantService getParticipantService()
  {
    return participantService;
  }

  public void setParticipantService( ParticipantService participantService )
  {
    this.participantService = participantService;
  }

  public CountryService getCountryService()
  {
    return countryService;
  }

  public void setCountryService( CountryService countryService )
  {
    this.countryService = countryService;
  }

  public PasswordPolicyStrategy getPasswordPolicyStrategy()
  {
    return passwordPolicyStrategy;
  }

  public void setPasswordPolicyStrategy( PasswordPolicyStrategy passwordPolicyStrategy )
  {
    this.passwordPolicyStrategy = passwordPolicyStrategy;
  }

  public UserTokenFactory getUserTokenFactory()
  {
    return userTokenFactory;
  }

  public void setUserTokenFactory( UserTokenFactory userTokenFactory )
  {
    this.userTokenFactory = userTokenFactory;
  }

  public PersonalizationService getPersonalizationService()
  {
    return personalizationService;
  }

  public void setPersonalizationService( PersonalizationService personalizationService )
  {
    this.personalizationService = personalizationService;
  }

  private boolean isTermedUserAndInactive( Participant pax )
  {
    if ( Objects.nonNull( pax ) && Objects.nonNull( pax.getTerminationDate() ) && Objects.nonNull( pax.getStatus() ) && Objects.nonNull( pax.getStatus().getCode() )
        && pax.getStatus().getCode().equals( ParticipantStatus.INACTIVE ) )
    {
      return Boolean.TRUE;
    }
    return Boolean.FALSE;
  }

  private static <T> Predicate<T> distinctByKey( Function<? super T, Object> keyExtractor )
  {
    Map<Object, Boolean> map = new ConcurrentHashMap<>();
    return t -> map.putIfAbsent( keyExtractor.apply( t ), Boolean.TRUE ) == null;
  }
}

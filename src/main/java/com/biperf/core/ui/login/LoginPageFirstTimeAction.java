
package com.biperf.core.ui.login;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.domain.WebErrorMessage;
import com.biperf.core.domain.enums.EmailAddressType;
import com.biperf.core.domain.enums.ParticipantPreferenceCommunicationsType;
import com.biperf.core.domain.enums.ParticipantTermsAcceptance;
import com.biperf.core.domain.enums.PhoneType;
import com.biperf.core.domain.enums.SecretQuestionType;
import com.biperf.core.domain.enums.VerificationStatusType;
import com.biperf.core.domain.participant.Audience;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.participant.ParticipantCommunicationPreference;
import com.biperf.core.domain.user.User;
import com.biperf.core.domain.user.UserEmailAddress;
import com.biperf.core.domain.user.UserPhone;
import com.biperf.core.domain.user.UserTNCHistory;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.security.CmsAuthorizationUtil;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.awardbanq.AwardBanQService;
import com.biperf.core.service.awardbanq.AwardBanQServiceFactory;
import com.biperf.core.service.email.MailingService;
import com.biperf.core.service.participant.AudienceService;
import com.biperf.core.service.participant.ParticipantAssociationRequest;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.participant.ProfileService;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.service.profileavatar.ProfileAvatarService;
import com.biperf.core.service.security.AuthenticationService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.service.util.ServiceError;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.ui.help.ContactUsAction;
import com.biperf.core.ui.profile.ProfileAvatarImageUploadPropertiesView;
import com.biperf.core.ui.profile.ProfileAvatarImageUploadView;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.ui.utils.ValidatorChecks;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.ImageUtils;
import com.biperf.core.utils.PageConstants;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.AboutMeValueBean;
import com.biperf.core.value.PersonalInfoFileUploadValue;
import com.objectpartners.cms.util.CmsResourceBundle;

/**
 * Used for the First time login Action .
 * <p>
 * <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>dudam</td>
 * <td>Oct 29, 2012</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class LoginPageFirstTimeAction extends BaseDispatchAction
{

  private static final Log logger = LogFactory.getLog( LoginPageFirstTimeAction.class );

  public ActionForward execute( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    if ( mapping.getParameter() != null )
    {
      return super.execute( mapping, form, request, response );
    }

    return this.saveUserInfo( mapping, form, request, response );

  }

  public ActionForward display( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    // get form and create if not exists
    LoginPageFirstTimeForm loginPageFirstTimeForm = (LoginPageFirstTimeForm)form;
    if ( loginPageFirstTimeForm == null )
    {
      loginPageFirstTimeForm = new LoginPageFirstTimeForm();
    }

    AssociationRequestCollection requestCollection = new AssociationRequestCollection();
    requestCollection.add( new ParticipantAssociationRequest( ParticipantAssociationRequest.EMAILS ) );
    requestCollection.add( new ParticipantAssociationRequest( ParticipantAssociationRequest.PHONES ) );
    Participant sessionUser = getParticipantService().getParticipantByIdWithAssociations( UserManager.getUserId(), requestCollection );

    // To restrict login when check on logo
    if ( UserManager.getUser().isPaxInactive() && sessionUser.getTerminationDate() != null )
    {
      return mapping.findForward( ActionConstants.LOGIN_FORWARD );
    }
    // load about me information
    List allAboutMeByUserId = getProfileService().getAllAboutMeByUserId( UserManager.getUserId() );
    loginPageFirstTimeForm.load( allAboutMeByUserId );

    // check login page required email address or not
    Boolean emailRequired = getSystemVariableService().getPropertyByName( SystemVariableService.FIRST_TIME_LOGIN_REQUIRED_EMAIL ).getBooleanVal();

    // set login page email data
    if ( emailRequired && sessionUser.getPrimaryEmailAddress() == null )
    {
      loginPageFirstTimeForm.setShowUpdateEmail( true );
    }
    else
    {
      loginPageFirstTimeForm.setShowUpdateEmail( false );
    }

    // password section
    boolean passwordExpired = getUserService().getPasswordPolicyStrategy().isPasswordExpired( sessionUser );
    if ( passwordExpired || sessionUser.isForcePasswordChange() )
    {
      loginPageFirstTimeForm.setForceChangePassword( true );
    }
    else
    {
      loginPageFirstTimeForm.setForceChangePassword( false );
    }
    loginPageFirstTimeForm.setFromSSO( UserManager.getUser().isFromSSO() );

    // terms and condition section
    Boolean termsAndConditionsUsed = getSystemVariableService().getPropertyByName( SystemVariableService.TERMS_CONDITIONS_USED ).getBooleanVal();
    Boolean showTermsAndConditions = isShowTermsAndConditions( sessionUser );
    if ( termsAndConditionsUsed && showTermsAndConditions )
    {
      loginPageFirstTimeForm.setShowTermsAndConditions( Boolean.TRUE );
    }
    else
    {
      loginPageFirstTimeForm.setShowTermsAndConditions( Boolean.FALSE );
    }

    if ( sessionUser.getAvatarSmallFullPath() == null )
    {
      loginPageFirstTimeForm.setAvatarUrl( ImageUtils.getAvatarInitials( sessionUser ) );
    }
    else
    {
      loginPageFirstTimeForm.setAvatarUrl( sessionUser.getAvatarSmall() );
    }

    request.setAttribute( "forceChangePwd", sessionUser.isForcePasswordChange() );

    // contact us sent confirmation modal
    ContactUsAction.moveToRequest( request );

    // set all ParticipantCommunicationPreference
    Set<ParticipantCommunicationPreference> preferences = sessionUser.getParticipantCommunicationPreferences();
    String[] activeSMSGroupTypes = new String[preferences.size()];
    int index = 0;
    for ( ParticipantCommunicationPreference preference : preferences )
    {
      if ( preference.getParticipantPreferenceCommunicationsType().getCode().equals( ParticipantPreferenceCommunicationsType.TEXT_MESSAGES ) )
      {
        activeSMSGroupTypes[index] = preference.getMessageSMSGroupType().getCode();
        index++;
      }
    }
    loginPageFirstTimeForm.setActiveSMSGroupTypes( activeSMSGroupTypes );

    Set<UserPhone> userPhones = sessionUser.getUserPhones();
    for ( UserPhone userPhone : userPhones )
    {
      if ( userPhone.isPrimary() )
      {
        loginPageFirstTimeForm.setCountryPhoneCode( userPhone.getCountryPhoneCode() );
        loginPageFirstTimeForm.setTextPhoneNbr( userPhone.getPhoneNbr() );
      }
    }

    if ( sessionUser.isAcceptTermsOnTextMessages() )
    {
      loginPageFirstTimeForm.setTermsAndConditionsForTxtMsgs( "true" );
    }

    return mapping.findForward( ActionConstants.SUCCESS_FORWARD );
  }

  private boolean isShowTermsAndConditions( Participant sessionUser )
  {
    if ( sessionUser.getTermsAcceptance() != null
        && ( sessionUser.getTermsAcceptance().getCode().equals( ParticipantTermsAcceptance.NOTACCEPTED ) || sessionUser.getTermsAcceptance().getCode().equals( ParticipantTermsAcceptance.DECLINED ) ) )
    {
      return true;
    }
    return false;
  }

  public ActionForward uploadAvatar( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {

    LoginPageFirstTimeForm loginPageFirstTimeForm = (LoginPageFirstTimeForm)form;

    Participant participant = getParticipantService().getParticipantById( UserManager.getUserId() );
    String avatarSmall = participant.getAvatarSmall();

    // Creating json view objects
    ProfileAvatarImageUploadPropertiesView propertiesView = new ProfileAvatarImageUploadPropertiesView();
    ProfileAvatarImageUploadView profileUploadAvatar = new ProfileAvatarImageUploadView();
    WebErrorMessage message = new WebErrorMessage();

    // if user is a pax else return
    if ( UserManager.getUser().isParticipant() )
    {
      // check for image format
      if ( !ImageUtils.isImageFormatValid( loginPageFirstTimeForm.getProfileImage().getContentType() ) )
      {
        message.setText( CmsResourceBundle.getCmsBundle().getString( "profile.personal.info.INVALID_EXT" ) );
        propertiesView.setIsSuccess( false );
        propertiesView.setAvatarUrl( participant.getAvatarSmallFullPath() );
      }
      // check for image size limit
      else if ( !ImageUtils.isImageSizeValid( loginPageFirstTimeForm.getProfileImage().getFileSize() ) )
      {
        message.setText( CmsResourceBundle.getCmsBundle().getString( "profile.personal.info.IMAGE_SIZE" ) + " " + ImageUtils.getImageSize() );
        propertiesView.setIsSuccess( false );
        propertiesView.setAvatarUrl( participant.getAvatarSmallFullPath() );
      }
      else
      {
        String orginalfilename = loginPageFirstTimeForm.getProfileImage().getFileName();
        String extension = "." + getFileExtension( orginalfilename );
        String filename = orginalfilename.substring( 0, orginalfilename.length() - extension.length() );
        if ( filename != null )
        {
          filename = ValidatorChecks.removesSpecialCharactersAndSpaces( filename );

        }
        filename = filename + extension;

        int filesize = loginPageFirstTimeForm.getProfileImage().getFileSize();
        byte[] imageInByte = loginPageFirstTimeForm.getProfileImage().getFileData();

        PersonalInfoFileUploadValue data = new PersonalInfoFileUploadValue();
        data.setId( UserManager.getUserId() );
        data.setData( imageInByte );
        data.setType( PersonalInfoFileUploadValue.TYPE_AVATAR );
        data.setName( filename );
        data.setSize( filesize );
        data.setContentType( loginPageFirstTimeForm.getProfileImage().getContentType() );
        try
        {
          data = getProfileService().uploadAvatar( data, participant.getId() );
          propertiesView.setAvatarUrl( data.getThumb() );
          propertiesView.setIsSuccess( true );
          message.setText( CmsResourceBundle.getCmsBundle().getString( "profile.personal.info.UPLOAD_SUCCESS" ) );
        }
        catch( Exception e )
        {
          logger.error( "<<<<<<<ERROR >>>>>>" + e );
          message.setName( CmsResourceBundle.getCmsBundle().getString( "profile.personal.info.UPLOAD_FAIL" ) );
          propertiesView.setAvatarUrl( avatarSmall );
          propertiesView.setIsSuccess( false );
        }

      }
    }
    profileUploadAvatar.setProperties( propertiesView );
    profileUploadAvatar.getMessages().add( message );
    writeAsJsonToResponse( profileUploadAvatar, response );
    return null;
  }

  public ActionForward saveUserInfo( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    LoginPageFirstTimeForm loginPageFirstTimeForm = (LoginPageFirstTimeForm)form;

    UserTNCHistory userTNCHistory = null;
    if ( loginPageFirstTimeForm.isShowTermsAndConditions() )// check the TNC status and only if
                                                            // declined.
    {
      Participant participant = getParticipantService().getParticipantById( UserManager.getUserId() );
      userTNCHistory = addPaxTNCHistory( participant );
    }

    Participant sessionPax = null;
    boolean sendConfirmationMessage = false;
    if ( loginPageFirstTimeForm.isShowUpdateEmail() )
    {
      AssociationRequestCollection requestCollection = new AssociationRequestCollection();
      requestCollection.add( new ParticipantAssociationRequest( ParticipantAssociationRequest.EMAILS ) );
      requestCollection.add( new ParticipantAssociationRequest( ParticipantAssociationRequest.PHONES ) );
      sessionPax = getParticipantService().getParticipantByIdWithAssociations( UserManager.getUserId(), requestCollection );

      UserEmailAddress userEmailAddress = new UserEmailAddress();
      userEmailAddress.setEmailAddr( loginPageFirstTimeForm.getEmail() );
      userEmailAddress.setEmailType( EmailAddressType.lookup( EmailAddressType.BUSINESS ) );
      userEmailAddress.setIsPrimary( true );
      userEmailAddress.setVerificationStatus( VerificationStatusType.lookup( VerificationStatusType.UNVERIFIED ) );
      sessionPax.addUserEmailAddress( userEmailAddress );
    }
    else
    {
      AssociationRequestCollection requestCollection = new AssociationRequestCollection();
      requestCollection.add( new ParticipantAssociationRequest( ParticipantAssociationRequest.PHONES ) );
      sessionPax = getParticipantService().getParticipantByIdWithAssociations( UserManager.getUserId(), requestCollection );
    }
    sessionPax.setLastResetDate( new Date() );
    sessionPax.setForcePasswordChange( false );
    sessionPax.setSecretQuestionType( SecretQuestionType.lookup( ActionConstants.CODE_FOR_SECRETQUESTION ) );
    sessionPax.setSecretAnswer( ActionConstants.NOT_APPLICABLE );
    List<AboutMeValueBean> aboutMeValueBeans = loginPageFirstTimeForm.getAboutMeQuestions();

    UserPhone userPhone;
    if ( loginPageFirstTimeForm.getActiveSMSGroupTypes() != null )
    {
      userPhone = new UserPhone();
      userPhone.setCountryPhoneCode( loginPageFirstTimeForm.getCountryPhoneCode() );
      userPhone.setIsPrimary( false );
      userPhone.setPhoneNbr( loginPageFirstTimeForm.getTextPhoneNbr() );
      userPhone.setPhoneType( PhoneType.lookup( PhoneType.MOBILE ) );
      userPhone.setVerificationStatus( VerificationStatusType.lookup( VerificationStatusType.UNVERIFIED ) );
      sessionPax.addUserPhone( userPhone );
      sessionPax.setAcceptTermsOnTextMessages( true );
      sendConfirmationMessage = true;
    }
    else
    {
      userPhone = null;
    }
    User user = null;
    try
    {
      getProfileService().saveFirstTimeLoggedInUserInfo( sessionPax,
                                                         UserManager.getUserId(),
                                                         SecretQuestionType.lookup( ActionConstants.CODE_FOR_SECRETQUESTION ),
                                                         ActionConstants.NOT_APPLICABLE,
                                                         aboutMeValueBeans,
                                                         loginPageFirstTimeForm.getActiveSMSGroupTypes(),
                                                         userPhone,
                                                         loginPageFirstTimeForm.getNewPassword(),
                                                         loginPageFirstTimeForm.isShowTermsAndConditions() );
      user = getUserService().getUserById( UserManager.getUserId() );
      request.getSession().setAttribute( "userObj", user );
      if ( userTNCHistory != null )
      {
        getParticipantService().saveTNCHistory( userTNCHistory );
      }

      if ( sendConfirmationMessage )
      {
        String message = CmsResourceBundle.getCmsBundle().getString( "participant.preference.edit", "TEXT_MESSAGE_SETUP" );
        String companyName = getSystemVariableService().getPropertyByName( SystemVariableService.CLIENT_NAME ).getStringVal();
        String formatedMesg = message.replace( "${companyName}", companyName );
        getMailingService().sendSmsMessage( user.getId(), userPhone.getCountryPhoneCode(), userPhone.getPhoneNbr(), formatedMesg );
      }
    }
    catch( ServiceErrorException e )
    {
      List serviceErrors = e.getServiceErrors();
      for ( Object obj : serviceErrors )
      {
        ServiceError error = (ServiceError)obj;
        String errorMessage = CmsResourceBundle.getCmsBundle().getString( error.getKey() );
        if ( StringUtils.isNotEmpty( error.getArg1() ) )
        {
          errorMessage = errorMessage.replace( "{0}", error.getArg1() );
        }
        if ( StringUtils.isNotEmpty( error.getArg2() ) )
        {
          errorMessage = errorMessage.replace( "{1}", error.getArg2() );
        }
        error.setArg4( errorMessage );
      }
      request.setAttribute( "serviceValidationErrors", serviceErrors );
      request.setAttribute( "serverReturnedErrored", true );

      Participant pax = getParticipantService().getParticipantById( UserManager.getUserId() );
      String profilePicPath = pax.getAvatarSmall();
      request.setAttribute( "previousPhotoUrl", profilePicPath );
      return mapping.findForward( ActionConstants.FAIL_FORWARD );
    }
    Set audienceNames = new HashSet();
    if ( user instanceof Participant )
    {
      audienceNames.add( CmsAuthorizationUtil.DEFAULT_AUDIENCE_NAME );
      audienceNames.add( CmsAuthorizationUtil.PARTICIPANT_AUDIENCE_NAME );
      Participant pax = (Participant)user;
      for ( Iterator iterator = getAudienceService().getAllParticipantAudiences( pax ).iterator(); iterator.hasNext(); )
      {
        Audience audience = (Audience)iterator.next();
        audienceNames.add( audience.getName() );
      }
    }

    UserManager.getUser().setAudienceNames( audienceNames );

    response.sendRedirect( RequestUtils.getBaseURI( request ) + PageConstants.HOME_PAGE_G5_REDIRECT_URL );
    return null;
  }

  public ActionForward declineTNC( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    Participant participant = getParticipantService().getParticipantById( UserManager.getUserId() );
    UserTNCHistory userTNCHistory = addPaxTNCHistory( participant );
    participant.setTermsAcceptance( ParticipantTermsAcceptance.lookup( ParticipantTermsAcceptance.DECLINED ) );
    participant.setTermsAcceptedDate( DateUtils.getCurrentDate() ); // Capture when T&Cs was
                                                                    // accepted
    participant.setUserIDAcceptedTerms( UserManager.getUserId().toString() ); // Capture the User
                                                                              // who
    getParticipantService().saveParticipantWithTNCHistory( participant, userTNCHistory );

    // If PAX has non-zero balance then forward the request to catalog.
    String targetUrl = null;
    Long balance = getAwardBanQService().getAccountBalanceForParticipantId( participant.getId() );
    if ( balance != null && balance.longValue() > 0 )
    {
      String shoppingUrlForInactiveUser = getAuthenticationService().getShoppingUrlForInactiveUser( PageConstants.INACTIVE_SHOPPING_URL,
                                                                                                    PageConstants.MULTISUPPLIER_SHOPPING_URL,
                                                                                                    participant.getId() );
      targetUrl = RequestUtils.getBaseURI( request ) + shoppingUrlForInactiveUser;

      response.sendRedirect( response.encodeRedirectURL( targetUrl ) );
      return null;
    }

    return mapping.findForward( ActionConstants.DECLINE );
  }

  public UserTNCHistory addPaxTNCHistory( Participant pax )
  {
    UserTNCHistory userTNCHistory = new UserTNCHistory();
    userTNCHistory.setUser( pax );
    userTNCHistory.setTncAction( pax.getTermsAcceptance().getCode() );

    if ( pax.getUserIDAcceptedTerms() != null )
    {
      userTNCHistory.setHistoryCreatedBy( new Long( pax.getUserIDAcceptedTerms() ) );
    }
    else
    {
      userTNCHistory.setHistoryCreatedBy( pax.getAuditCreateInfo().getCreatedBy() );
    }

    if ( pax.getTermsAcceptedDate() != null )
    {
      userTNCHistory.setHistoryDateCreated( new Timestamp( pax.getTermsAcceptedDate().getTime() ) );
    }
    else
    {
      userTNCHistory.setHistoryDateCreated( pax.getAuditCreateInfo().getDateCreated() );
    }

    return userTNCHistory;
  }

  // Just for avatar upload ajax responses, overriding the method in BaseDispatch since the content
  // type application/json is not working
  public void writeAsJsonToResponse( Object bean, HttpServletResponse response ) throws IOException
  {
    PrintWriter out = response.getWriter();
    out.print( toJson( bean ) );
    out.flush();
    out.close();
  }

  private ParticipantService getParticipantService()
  {
    return (ParticipantService)getService( ParticipantService.BEAN_NAME );
  }

  private ProfileService getProfileService()
  {
    return (ProfileService)getService( ProfileService.BEAN_NAME );
  }

  private UserService getUserService()
  {
    return (UserService)getService( UserService.BEAN_NAME );
  }

  private static SystemVariableService getSystemVariableService()
  {
    return (SystemVariableService)getService( SystemVariableService.BEAN_NAME );
  }

  private String getFileExtension( String filename )
  {
    return FilenameUtils.getExtension( filename );
  }

  private AudienceService getAudienceService()
  {
    return (AudienceService)getService( AudienceService.BEAN_NAME );
  }

  private MailingService getMailingService()
  {
    return (MailingService)BeanLocator.getBean( MailingService.BEAN_NAME );
  }

  private AuthenticationService getAuthenticationService()
  {
    return (AuthenticationService)getService( AuthenticationService.BEAN_NAME );
  }

  public ProfileAvatarService getProfileAvatarService()
  {
    return (ProfileAvatarService)getService( ProfileAvatarService.BEAN_NAME );
  }

  private AwardBanQService getAwardBanQService()
  {
    AwardBanQServiceFactory factory = (AwardBanQServiceFactory)BeanLocator.getBean( AwardBanQServiceFactory.BEAN_NAME );
    return factory.getAwardBanQService();
  }
}
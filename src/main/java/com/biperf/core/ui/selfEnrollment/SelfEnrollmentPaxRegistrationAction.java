
package com.biperf.core.ui.selfEnrollment;

import java.io.IOException;
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
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.biperf.core.domain.WebErrorMessage;
import com.biperf.core.domain.country.Country;
import com.biperf.core.domain.employer.Employer;
import com.biperf.core.domain.enums.PhoneType;
import com.biperf.core.domain.enums.VerificationStatusType;
import com.biperf.core.domain.hierarchy.Node;
import com.biperf.core.domain.participant.Audience;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.user.User;
import com.biperf.core.domain.user.UserPhone;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.security.CmsAuthorizationUtil;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.country.CountryService;
import com.biperf.core.service.email.MailingService;
import com.biperf.core.service.employer.EmployerService;
import com.biperf.core.service.hierarchy.NodeService;
import com.biperf.core.service.participant.AudienceService;
import com.biperf.core.service.participant.ParticipantAssociationRequest;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.participant.ProfileService;
import com.biperf.core.service.participant.RegistrationService;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.service.util.ServiceError;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.ui.profile.ProfileAvatarImageUploadPropertiesView;
import com.biperf.core.ui.profile.ProfileAvatarImageUploadView;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.ui.utils.ServiceErrorStrutsUtils;
import com.biperf.core.ui.utils.ValidatorChecks;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.ImageUtils;
import com.biperf.core.utils.PageConstants;
import com.biperf.core.utils.StringUtil;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.AboutMeValueBean;
import com.biperf.core.value.PersonalInfoFileUploadValue;
import com.objectpartners.cms.util.CmsResourceBundle;

import botdetect.web.Captcha;

/**
 * SelfEnrollmentPaxRegistrationAction.
 * @author kandhi
 * @since Jun 13, 2013
 * @version 1.0
 * 
 * @see SelfEnrollmentPaxRegistrationSpringController
 */
public class SelfEnrollmentPaxRegistrationAction extends BaseDispatchAction
{

  private static final Log logger = LogFactory.getLog( SelfEnrollmentPaxRegistrationAction.class );

  private static final String SELF_ENROLLMENT_NODE_ID = "selfEnrollmentNodeId";

  /**
   * Used to display the self enrollment form. Invoked from loginPage.jsp
   */
  public ActionForward display( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws IOException
  {
    String actionForward = ActionConstants.SUCCESS_FORWARD;

    SelfEnrollmentPaxRegistrationForm selfEnrollmentPaxRegistrationForm = (SelfEnrollmentPaxRegistrationForm)form;

    if ( !loadNode( mapping, request, selfEnrollmentPaxRegistrationForm ) )
    {
      return mapping.findForward( ActionConstants.CANCEL_FORWARD );
    }

    Boolean termsAndConditionsUsed = getSystemVariableService().getPropertyByName( SystemVariableService.TERMS_CONDITIONS_USED ).getBooleanVal();
    selfEnrollmentPaxRegistrationForm.setShowTermsAndConditions( termsAndConditionsUsed );

    return mapping.findForward( actionForward );
  }

  private boolean loadNode( ActionMapping mapping, HttpServletRequest request, SelfEnrollmentPaxRegistrationForm selfEnrollmentPaxRegistrationForm )
  {
    boolean success = true;
    Long nodeId = (Long)request.getSession().getAttribute( SELF_ENROLLMENT_NODE_ID );
    if ( nodeId == null || nodeId == 0 )
    {
      success = false;
    }
    else
    {
      Node node = getNodeService().getNodeById( nodeId );
      selfEnrollmentPaxRegistrationForm.setNodeId( nodeId.toString() );
      selfEnrollmentPaxRegistrationForm.setNodeName( node.getName() );
    }
    return success;
  }

  // /**
  // * Generates the user Id as per the use-case. First letter of first name + complete last name +
  // random 4 digits
  // */
  // public ActionForward generateUserName( ActionMapping mapping, ActionForm form,
  // HttpServletRequest request, HttpServletResponse response ) throws IOException
  // {
  // SelfEnrollmentPaxRegistrationForm enrollmentForm = (SelfEnrollmentPaxRegistrationForm)form;
  // super.writeAsJsonToResponse( getUserService().generateUniqueUserIdBean(
  // enrollmentForm.getFirstName(), enrollmentForm.getLastName() ), response );
  // return null;
  // }

  /**
   * Save the self enrollment form
   */
  @SuppressWarnings( "rawtypes" )
  public ActionForward save( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws IOException
  {
    SelfEnrollmentPaxRegistrationForm selfEnrollmentPaxRegistrationForm = (SelfEnrollmentPaxRegistrationForm)form;
    if ( !loadNode( mapping, request, selfEnrollmentPaxRegistrationForm ) )
    {
      return mapping.findForward( ActionConstants.CANCEL_FORWARD );
    }

    ActionMessages errors = new ActionMessages();

    verifyCaptcha( request, errors );

    if ( !errors.isEmpty() )
    {
      saveErrors( request, errors );
      return mapping.findForward( ActionConstants.FAIL_FORWARD );
    }

    // Password validation
    try
    {
      getRegistrationService().validatePassword( selfEnrollmentPaxRegistrationForm.getInputPassword() );
    }
    catch( ServiceErrorException e )
    {
      ServiceErrorStrutsUtils.convertServiceErrorsToActionErrors( e.getServiceErrors(), errors );
      saveErrors( request, errors );
      return mapping.findForward( ActionConstants.FAIL_FORWARD );
    }

    List<Employer> employers = getEmployerService().getActiveEmployers();
    Employer employer = null;

    if ( employers != null && employers.size() > 0 )
    {
      employer = employers.get( 0 );
    }

    String languageCode;
    if ( !StringUtil.isEmpty( (String)request.getSession().getAttribute( "languageCodeSelfEnrolled" ) ) )
    {
      languageCode = (String)request.getSession().getAttribute( "languageCodeSelfEnrolled" );
      selfEnrollmentPaxRegistrationForm.setSelfEnrolledLanguageCode( languageCode );
    }

    // Create the participant domain object from the form
    Participant participant = selfEnrollmentPaxRegistrationForm.createParticipantDomainObj( employer );

    User user = null;

    try
    {
      getRegistrationService().enroll( participant );
      getAudienceService().rematchParticipantForAllCriteriaAudiences( participant.getId() );

      user = getUserService().getUserById( UserManager.getUserId() );
      request.getSession().setAttribute( "userObj", user );

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

    }
    catch( ServiceErrorException e )
    {
      List serviceErrors = e.getServiceErrors();
      ServiceErrorStrutsUtils.convertServiceErrorsToActionErrors( serviceErrors, errors );
    }

    if ( !errors.isEmpty() )
    {
      saveErrors( request, errors );
      return mapping.findForward( ActionConstants.FAIL_FORWARD );
    }

    // load form data
    selfEnrollmentPaxRegistrationForm.setAvatarUrl( null );
    // bug fix 56289 in bugzilla
    Country country = getCountryService().getCountryByCode( selfEnrollmentPaxRegistrationForm.getAddressFormBean().getCountryCode() );
    selfEnrollmentPaxRegistrationForm.setCountryAllowSms( country.getAllowSms() );
    /* Dexter# 46388 Start */
    List allAboutMeByUserId = getProfileService().getAllAboutMeByUserId( UserManager.getUserId() );
    selfEnrollmentPaxRegistrationForm.load( allAboutMeByUserId );
    /* Dexter# 46388 End */

    return mapping.findForward( ActionConstants.SUCCESS_FORWARD );
  }

  private void verifyCaptcha( HttpServletRequest request, ActionMessages errors )
  {
    boolean isCaptchaOk = false;
    String response = request.getParameter( "captchaResponse" );
    Captcha captcha = Captcha.load( request, "loginFormCaptcha" );
    try
    {
      isCaptchaOk = captcha.validate( request, response );
    }
    catch( Exception e )
    {
      // Unable to verify
    }

    if ( logger.isDebugEnabled() )
    {
      logger.debug( "Verify Captcha " + ( isCaptchaOk ? "SUCCESS" : "FAILURE" ) );
    }

    if ( !isCaptchaOk )
    {
      errors.add( "captchaResponse", new ActionMessage( "purl.tnc.error.params.CAPTCHA_FAILED" ) );
    }
  }

  public ActionForward uploadAvatar( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {

    SelfEnrollmentPaxRegistrationForm selfEnrollmentPaxRegistrationForm = (SelfEnrollmentPaxRegistrationForm)form;

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
      if ( !ImageUtils.isImageFormatValid( selfEnrollmentPaxRegistrationForm.getProfileImage().getContentType() ) )
      {
        message.setText( CmsResourceBundle.getCmsBundle().getString( "profile.personal.info.INVALID_EXT" ) );
        propertiesView.setIsSuccess( false );
        propertiesView.setAvatarUrl( participant.getAvatarSmallFullPath() );
      }
      // check for image size limit
      else if ( !ImageUtils.isImageSizeValid( selfEnrollmentPaxRegistrationForm.getProfileImage().getFileSize() ) )
      {
        message.setText( CmsResourceBundle.getCmsBundle().getString( "profile.personal.info.IMAGE_SIZE" ) + " " + ImageUtils.getImageSize() );
        propertiesView.setIsSuccess( false );
        propertiesView.setAvatarUrl( participant.getAvatarSmallFullPath() );
      }
      else
      {
        String orginalfilename = selfEnrollmentPaxRegistrationForm.getProfileImage().getFileName();
        String extension = "." + getFileExtension( orginalfilename );
        String filename = orginalfilename.substring( 0, orginalfilename.length() - extension.length() );
        if ( filename != null )
        {
          filename = ValidatorChecks.removesSpecialCharactersAndSpaces( filename );

        }
        filename = filename + extension;

        int filesize = selfEnrollmentPaxRegistrationForm.getProfileImage().getFileSize();
        byte[] imageInByte = selfEnrollmentPaxRegistrationForm.getProfileImage().getFileData();

        PersonalInfoFileUploadValue data = new PersonalInfoFileUploadValue();
        data.setId( UserManager.getUserId() );
        data.setData( imageInByte );
        data.setType( PersonalInfoFileUploadValue.TYPE_AVATAR );
        data.setName( filename );
        data.setSize( filesize );
        data.setContentType( selfEnrollmentPaxRegistrationForm.getProfileImage().getContentType() );
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

  @SuppressWarnings( { "unchecked", "rawtypes" } )
  public ActionForward savePreferences( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws IOException
  {
    SelfEnrollmentPaxRegistrationForm selfEnrollmentPaxRegistrationForm = (SelfEnrollmentPaxRegistrationForm)form;
    boolean sendConfirmationMessage = false;
    AssociationRequestCollection requestCollection = new AssociationRequestCollection();
    requestCollection.add( new ParticipantAssociationRequest( ParticipantAssociationRequest.PHONES ) );
    Participant sessionPax = getParticipantService().getParticipantByIdWithAssociations( UserManager.getUserId(), requestCollection );

    sessionPax.setLastResetDate( new Date() );
    sessionPax.setForcePasswordChange( false );
    List<AboutMeValueBean> aboutMeValueBeans = selfEnrollmentPaxRegistrationForm.getAboutMeQuestions();

    UserPhone userPhone;
    if ( selfEnrollmentPaxRegistrationForm.getActiveSMSGroupTypes() != null )
    {
      userPhone = new UserPhone();
      userPhone.setCountryPhoneCode( selfEnrollmentPaxRegistrationForm.getTelephoneCountryCode() );
      userPhone.setIsPrimary( false );
      userPhone.setPhoneNbr( selfEnrollmentPaxRegistrationForm.getSmsPhoneNumber() );
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
    try
    {
      getProfileService().saveFirstTimeLoggedInUserInfo( sessionPax,
                                                         UserManager.getUserId(),
                                                         null,
                                                         null,
                                                         aboutMeValueBeans,
                                                         selfEnrollmentPaxRegistrationForm.getActiveSMSGroupTypes(),
                                                         userPhone,
                                                         selfEnrollmentPaxRegistrationForm.getInputPassword(),
                                                         selfEnrollmentPaxRegistrationForm.isShowTermsAndConditions() );
      User user = getUserService().getUserById( UserManager.getUserId() );
      request.getSession().setAttribute( "userObj", user );

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
        error.setArg4( errorMessage );
      }
      request.setAttribute( "serviceValidationErrors", serviceErrors );
      request.setAttribute( "serverReturnedErrored", true );

      Participant pax = getParticipantService().getParticipantById( UserManager.getUserId() );
      String profilePicPath = pax.getAvatarSmall();
      request.setAttribute( "previousPhotoUrl", profilePicPath );
      return mapping.findForward( ActionConstants.FAIL_FORWARD );
    }
    response.sendRedirect( RequestUtils.getBaseURI( request ) + PageConstants.HOME_PAGE_G5_REDIRECT_URL );
    return null;
  }

  private String getFileExtension( String filename )
  {
    return FilenameUtils.getExtension( filename );
  }

  private ParticipantService getParticipantService()
  {
    return (ParticipantService)getService( ParticipantService.BEAN_NAME );
  }

  private ProfileService getProfileService()
  {
    return (ProfileService)getService( ProfileService.BEAN_NAME );
  }

  private RegistrationService getRegistrationService()
  {
    return (RegistrationService)getService( RegistrationService.BEAN_NAME );
  }

  private NodeService getNodeService()
  {
    return (NodeService)BeanLocator.getBean( NodeService.BEAN_NAME );
  }

  private UserService getUserService()
  {
    return (UserService)BeanLocator.getBean( UserService.BEAN_NAME );
  }

  private AudienceService getAudienceService()
  {
    return (AudienceService)BeanLocator.getBean( AudienceService.BEAN_NAME );
  }

  private EmployerService getEmployerService()
  {
    return (EmployerService)BeanLocator.getBean( EmployerService.BEAN_NAME );
  }

  private static SystemVariableService getSystemVariableService()
  {
    return (SystemVariableService)getService( SystemVariableService.BEAN_NAME );
  }

  private static CountryService getCountryService()
  {
    return (CountryService)getService( CountryService.BEAN_NAME );
  }

  private MailingService getMailingService()
  {
    return (MailingService)BeanLocator.getBean( MailingService.BEAN_NAME );
  }

}

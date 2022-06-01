/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/profile/ParticipantPreferenceAction.java,v $
 */

package com.biperf.core.ui.profile;

import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.WebApplicationContext;

import com.biperf.core.domain.Fields;
import com.biperf.core.domain.WebErrorMessage;
import com.biperf.core.domain.country.Country;
import com.biperf.core.domain.enums.CountryStatusType;
import com.biperf.core.domain.enums.EmailAddressType;
import com.biperf.core.domain.enums.FilterSetupType;
import com.biperf.core.domain.enums.HierarchyRoleType;
import com.biperf.core.domain.enums.LanguageType;
import com.biperf.core.domain.enums.ParticipantPreferenceCommunicationsType;
import com.biperf.core.domain.enums.PhoneType;
import com.biperf.core.domain.enums.PickListItemSortOrderComparator;
import com.biperf.core.domain.enums.VerificationStatusType;
import com.biperf.core.domain.hierarchy.Hierarchy;
import com.biperf.core.domain.hierarchy.Node;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.participant.ParticipantCommunicationPreference;
import com.biperf.core.domain.user.UserAddress;
import com.biperf.core.domain.user.UserEmailAddress;
import com.biperf.core.domain.user.UserNode;
import com.biperf.core.domain.user.UserPhone;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.country.CountryService;
import com.biperf.core.service.email.MailingService;
import com.biperf.core.service.email.WelcomeEmailService;
import com.biperf.core.service.facebook.FacebookService;
import com.biperf.core.service.hierarchy.HierarchyService;
import com.biperf.core.service.hierarchy.NodeService;
import com.biperf.core.service.home.FilterAppSetupService;
import com.biperf.core.service.home.HomePageFilterHolder;
import com.biperf.core.service.participant.AudienceService;
import com.biperf.core.service.participant.ParticipantAssociationRequest;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.service.security.AuthenticationService;
import com.biperf.core.service.security.AuthorizationService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.service.twitter.TwitterService;
import com.biperf.core.service.underarmour.UnderArmourService;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.WebErrorMessageList;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.ui.participant.ListBuilderAction;
import com.biperf.core.ui.user.AddressFormBean;
import com.biperf.core.ui.user.UserForm;
import com.biperf.core.ui.utils.ActionUtils;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.ui.utils.ServiceErrorStrutsUtils;
import com.biperf.core.utils.ApplicationContextFactory;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;
import com.biperf.core.utils.ClientStateUtils;
import com.biperf.core.utils.FormFieldConstants;
import com.biperf.core.utils.InvalidClientStateException;
import com.biperf.core.utils.PageConstants;
import com.biperf.core.utils.UserManager;
import com.biperf.core.utils.WebResponseConstants;
import com.biperf.core.value.FormattedValueBean;
import com.biperf.util.StringUtils;
import com.objectpartners.cms.exception.CmsServiceException;
import com.objectpartners.cms.service.ContentReader;
import com.objectpartners.cms.util.CmsResourceBundle;
import com.objectpartners.cms.util.ContentReaderFactory;
import com.objectpartners.cms.util.ContentReaderManager;

/**
 * ParticipantAction.
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
 * <td>zahler</td>
 * <td>May 3, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ParticipantPreferenceAction extends BaseDispatchAction
{
  /** Log */
  private static final Log LOG = LogFactory.getLog( ParticipantPreferenceAction.class );

  /**
   * @param actionMapping
   * @param actionForm
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward unspecified( ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response )
  {
    return prepareCreate( actionMapping, actionForm, request, response );
  }

  /**
   * cancelled
   * 
   * @param actionMapping
   * @param actionForm
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward cancelled( ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response )
  {
    return actionMapping.findForward( ActionConstants.CANCEL_FORWARD );
  }

  /**
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward prepareCreate( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    UserForm participantForm = (UserForm)form;

    participantForm.setMethod( "create" );

    // default country to us
    Country country = getCountryService().getCountryByCode( Country.UNITED_STATES );

    /* Bug Fix 23605 start */
    if ( CountryStatusType.ACTIVE.equalsIgnoreCase( country.getStatus().getCode() ) )
    {
      participantForm.getAddressFormBean().setCountryCode( country.getCountryCode() );
      participantForm.getAddressFormBean().setCountryName( country.getI18nCountryName() );
    }
    else
    {
      List activeCountryList = getCountryService().getAllActive();
      if ( activeCountryList != null )
      {
        if ( activeCountryList.size() == 1 )
        {
          country = (Country)activeCountryList.get( 0 );
          participantForm.getAddressFormBean().setCountryCode( country.getCountryCode() );
          participantForm.getAddressFormBean().setCountryName( country.getI18nCountryName() );
        }
        else
        {
          request.setAttribute( "multiple", new Boolean( true ) );
        }
      }
    }
    /* Bug Fix 23605 end */

    // default T&Cs
    participantForm.defaultTermsAndConditions();

    try
    {
      // Trying to deactive exist UA user information from session through browser call
      participantForm.setUaLogOutUrl( getSystemVariableService().getPropertyByName( SystemVariableService.UNDERARMOUR_SESSION_LOGOUT_URL_PREFIX ).getStringVal() );
      if ( getUnderArmourService().uaEnabled() )
      {
        participantForm.setUaEnabled( true );
        Map<String, Object> params = new HashMap<String, Object>();
        params.put( "redirectUrl", "/participantProfilePage.do#profile/Preferences/" );
        participantForm.setUaOuthUrl( getSystemVariableService().getPropertyByName( SystemVariableService.UA_WEBSERVICES_AUTHORIZE_URL_PREFIX ).getStringVal() + "response_type=code&client_id="
            + getSystemVariableService().getPropertyByName( SystemVariableService.UNDERARMOUR_CLIENT_ID ).getStringVal() + "&redirect_uri="
            + getSystemVariableService().getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal() + "/ua/underArmourCallback.action&state="
            + ClientStateUtils.generateEncodedParamMap( params ) );

        if ( getUnderArmourService().isParticipantAuthorized( UserManager.getUserId() ) )
        {
          participantForm.setUaAuthorized( true );
        }

      }

    }
    catch( Exception e )
    {
      log.error( e.getMessage(), e );
    }

    // get the actionForward to display the create pages.
    return mapping.findForward( ActionConstants.CREATE_FORWARD );
  }

  private UnderArmourService getUnderArmourService()
  {
    return (UnderArmourService)BeanLocator.getBean( UnderArmourService.BEAN_NAME );
  }

  /**
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward create( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {

    final String METHOD_NAME = "create";

    if ( isCancelled( request ) )
    {
      // EARLY EXIT
      LOG.info( ">>> " + METHOD_NAME + " cancelled." );
      return mapping.findForward( ActionConstants.CANCEL_FORWARD );
    }

    ActionMessages errors = new ActionMessages();
    if ( !isTokenValid( request, true ) )
    {
      errors.add( ActionConstants.ERROR_TOKEN_FAILURE, new ActionMessage( ActionConstants.ERROR_TOKEN_EXCEPTION ) );
      saveErrors( request, errors );
      return mapping.findForward( ActionConstants.FAIL_CREATE ); // EARLY EXIT
    }

    UserForm participantForm = (UserForm)form;

    Participant participant = participantForm.toDomainObjectParticipant();
    participant.setAllowPublicRecognition( participantForm.isAllowPublicRecognition() );
    participant.setAllowPublicInformation( participantForm.isAllowPublicInformation() );
    participant.setAllowPublicBirthDate( participantForm.isAllowPublicBirthDate() );
    participant.setAllowPublicHireDate( participantForm.isAllowPublicHireDate() );
    // Bug Fix 20398 and 18048,Add default Language one as EN.
    if ( participant.getLanguageType() == null )
    {
      // Bug Fix 40855 - set a default language type
      participant.setLanguageType( LanguageType.lookup( getSystemVariableService().getPropertyByName( SystemVariableService.DEFAULT_LANGUAGE ).getStringVal() ) );
    }

    // set the node for the pax
    String nodeId = participantForm.getNodeId();
    String nodeName = participantForm.getNameOfNode();
    if ( nodeName != null && !nodeName.equals( "" ) )
    {
      Node node = null;

      // if the node id exists then go get the node via id
      // otherwise, get the node via name and primary hiearchy
      if ( nodeId != null && !nodeId.equals( "" ) )
      {
        node = getNodeService().getNodeById( new Long( nodeId ) );
      }
      else
      {
        Hierarchy primaryHierarchy = getHierarchyService().getPrimaryHierarchy();
        node = getNodeService().getNodeByNameAndHierarchy( nodeName, primaryHierarchy );
      }

      // if the node is not found then exit
      if ( node == null )
      {
        participant = participantForm.returnUserCharacteristics( participant );
        // TODO: Create a valid error message for not retrieving a node
        errors.add( ServiceErrorMessageKeys.SYSTEM_ERRORS_SYSTEM_EXCEPTION, new ActionMessage( "node.errors.SEARCH_FAILED" ) );
        saveErrors( request, errors );
        return mapping.findForward( ActionConstants.FAIL_CREATE );
      }

      UserNode userNode = new UserNode();
      userNode.setNode( node );
      userNode.setHierarchyRoleType( HierarchyRoleType.lookup( participantForm.getNodeRelationship() ) );
      userNode.setActive( new Boolean( true ) );

      participant.addUserNode( userNode );
    }

    if ( isAllowEstatements() )
    {
      ParticipantCommunicationPreference participantCommunicationPref = new ParticipantCommunicationPreference();
      participantCommunicationPref.setParticipant( participant );
      participantCommunicationPref.setParticipantPreferenceCommunicationsType( ParticipantPreferenceCommunicationsType.lookup( ParticipantPreferenceCommunicationsType.E_STATEMENTS ) );

      participant.addParticipantCommunicationPreference( participantCommunicationPref );
    }

    try
    {
      getParticipantService().createFullParticipant( participant );
      getAudienceService().rematchParticipantForAllCriteriaAudiences( participant.getId() );
    }
    catch( ServiceErrorException e )
    {
      log.debug( e );
      List serviceErrors = e.getServiceErrors();
      ServiceErrorStrutsUtils.convertServiceErrorsToActionErrors( serviceErrors, errors );
      saveErrors( request, errors );
    }

    Map clientStateParameterMap = new HashMap();
    clientStateParameterMap.put( "userId", participant.getId() );
    String queryString = ClientStateUtils.generateEncodedLink( "", "", clientStateParameterMap );
    String method = "method=display";
    ActionForward forward = ActionUtils.forwardWithParameters( mapping, ActionConstants.SUCCESS_FORWARD, new String[] { queryString, method } );

    if ( !errors.isEmpty() )
    {
      forward = mapping.findForward( ActionConstants.FAIL_CREATE );
    }

    return forward;
  }

  /**
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward display( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    UserForm participantForm = (UserForm)form;
    // BugFix 18201 Starts--
    String userIdFromCommLog = "";
    String isUserIdFromCommLog = "";
    String clientState = request.getParameter( "clientState" );
    if ( clientState != null && clientState.trim().length() > 0 )
    {
      String cryptoPass = request.getParameter( "cryptoPass" );
      String password = ClientStatePasswordManager.getPassword();
      if ( cryptoPass != null && cryptoPass.equals( "1" ) )
      {
        password = ClientStatePasswordManager.getGlobalPassword();
      }
      // Deserialize the client state.
      try
      {
        Map clientStateMap = ClientStateSerializer.deserialize( clientState, password );
        isUserIdFromCommLog = (String)clientStateMap.get( "isFromCommlog" );
        if ( isUserIdFromCommLog != null && isUserIdFromCommLog.equalsIgnoreCase( "true" ) )
        {
          userIdFromCommLog = (String)clientStateMap.get( "userIdFromCommLog" );
          participantForm.setUserId( userIdFromCommLog );
        }
      }
      catch( InvalidClientStateException e )
      {
        // Never Enter This Block
      }

    }
    // BugFix 18201 Ends--
    if ( participantForm.getUserId() == null || participantForm.getUserId().equals( "" ) )
    {
      // This method may be called from the participant list builder so the userId wont
      // exist in the form yet, try to get it out of the session object.
      List participants = (List)request.getSession().getAttribute( ListBuilderAction.SESSION_RESULT_PARTICIPANT_ID_LIST );

      if ( participants == null )
      {
        // Indicates a cancel, return to home page
        return mapping.findForward( "listbuildercancel" );
      }

      Iterator participantIter = participants.iterator();
      if ( participantIter.hasNext() )
      {
        FormattedValueBean participantBean = (FormattedValueBean)participantIter.next();
        participantForm.setUserId( String.valueOf( participantBean.getId() ) );
      }
      // clean out the session attribute
      request.getSession().removeAttribute( ListBuilderAction.SESSION_RESULT_PARTICIPANT_ID_LIST );
    }

    Participant participant = getParticipantService().getParticipantOverviewById( new Long( participantForm.getUserId() ) );

    participantForm.loadParticipant( participant );

    // get system variables on how T&Cs are used and set them on the Form
    participantForm.setTermsConditionsRequired();
    participantForm.setUserAllowedToAcceptForPax();

    request.setAttribute( "accountLocked", Boolean.valueOf( getAuthenticationService().isUserLockedOut( participant ) ) );

    // get the actionForward to display the create pages.
    return mapping.findForward( ActionConstants.DETAILS_FORWARD );
  }

  /**
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward prepareUpdatePreferences( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    UserForm participantForm = (UserForm)form;

    Long userId = null;

    if ( participantForm.getUserId() == null || participantForm.getUserId().equals( "" ) )
    {
      // No userId on the form - So, get the current user's id
      userId = UserManager.getUserId();
    }
    else
    {
      userId = new Long( participantForm.getUserId() );
    }

    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
    associationRequestCollection.add( new ParticipantAssociationRequest( ParticipantAssociationRequest.FACEBOOK ) );
    associationRequestCollection.add( new ParticipantAssociationRequest( ParticipantAssociationRequest.TWITTER ) );
    Participant participant = getParticipantService().getParticipantByIdWithAssociations( userId, associationRequestCollection );

    // If viewing a user other than the current user, then load the user name
    if ( !participantForm.isViewCurrentUser() )
    {
      participantForm.loadPersonalInfo( participant );
    }

    UserEmailAddress emailAddress = getUserService().getPrimaryUserEmailAddress( userId );
    if ( emailAddress != null )
    {
      participantForm.setEmailAddress( emailAddress.getEmailAddr() );
      participantForm.setEmailType( emailAddress.getEmailType().getCode() );
    }

    UserAddress userAddress = getUserService().getPrimaryUserAddress( userId );
    if ( userAddress != null )
    {
      AddressFormBean addressFormBean = new AddressFormBean();
      addressFormBean.load( userAddress.getAddress() );
      participantForm.setAddressFormBean( addressFormBean );
      participantForm.setCountryPhoneCode( addressFormBean.getCountryCode() );
    }

    UserEmailAddress textMessageAddress = getUserService().getTextMessageAddress( userId );
    if ( textMessageAddress != null )
    {
      participantForm.setTextMessageAddress( textMessageAddress.getEmailAddr() );
    }

    // If any of the phone numbers are of the type Fax, then the Fax checkbox
    // can be enabled on the JSP.
    Set userPhones = getUserService().getUserPhones( userId );
    for ( Iterator iter = userPhones.iterator(); iter.hasNext(); )
    {
      UserPhone userPhone = (UserPhone)iter.next();
      if ( userPhone.getPhoneType() != null && userPhone.getPhoneType().getCode() != null && userPhone.getPhoneType().getCode().equals( PhoneType.FAX ) )
      {
        participantForm.setHasFax( true );
        break;
      }
    }
    // Bug #24270 Start
    // Allow text messages?
    // get Allow_SMS flag from table, Country. START
    boolean isUserCountryAllowedForTextMessages = false;
    if ( userAddress != null && userAddress.getAddress() != null && userAddress.getAddress().getCountry() != null )
    {
      isUserCountryAllowedForTextMessages = userAddress.getAddress().getCountry().getAllowSms();
    }
    // END
    request.setAttribute( "isUserCountryAllowedForTextMessages", new Boolean( isUserCountryAllowedForTextMessages ) );
    boolean isTwitterEnabled = false;
    if ( getSystemVariableService().getPropertyByName( SystemVariableService.PURL_ALLOW_TWITTER ).getBooleanVal() )
    {
      isTwitterEnabled = true;
    }
    request.setAttribute( "isTwitterEnabled", new Boolean( isTwitterEnabled ) );
    boolean isFaceBookEnabled = false;
    String facebookAppId = null;
    if ( getSystemVariableService().getPropertyByName( SystemVariableService.PURL_ALLOW_FACEBOOK ).getBooleanVal() )
    {
      isFaceBookEnabled = true;
      if ( getFacebookService().getAppId() != null )
      {
        facebookAppId = getFacebookService().getAppId();
      }
    }
    request.setAttribute( "isFaceBookEnabled", new Boolean( isFaceBookEnabled ) );
    request.setAttribute( "facebookAppId", facebookAppId );
    if ( isUserCountryAllowedForTextMessages )
    {
      UserPhone userPhone = getUserService().getUserPhone( userId, PhoneType.MOBILE );
      if ( userPhone != null )
      {
        participantForm.setTextPhoneNbr( userPhone.getPhoneNbr() );
        participantForm.setCountryPhoneCode( userPhone.getCountryPhoneCode() );
      }
      if ( participant.isAcceptTermsOnTextMessages() )
      {
        participantForm.setAcceptTermsOnTextMessages( "yes" );
      }
    }
    participantForm.setAllowPublicRecognition( participant.isAllowPublicRecognition() );
    participantForm.setAllowPublicInformation( participant.isAllowPublicInformation() );
    participantForm.setAllowPublicBirthDate( participant.isAllowPublicBirthDate() );
    participantForm.setAllowPublicHireDate( participant.isAllowPublicHireDate() );

    // Bug #24270 End
    participantForm.loadPreferences( participant );
    participantForm.setMethod( "updatePreferences" );
    Set roles = new HashSet();
    roles.add( AuthorizationService.ROLE_CODE_PROJ_MGR );
    roles.add( AuthorizationService.ROLE_CODE_PROCESS_TEAM );
    roles.add( AuthorizationService.ROLE_CODE_BI_ADMIN );
    roles.add( AuthorizationService.ROLE_CODE_PAX );
    if ( getAuthorizationService().isUserInRole( null, roles, null ) )
    {
      // get the actionForward to display the update page.
      return mapping.findForward( ActionConstants.UPDATE_FORWARD );
    }
    // get the actionForward to display the view-only page.
    return mapping.findForward( ActionConstants.REVIEW_FORWARD );
  }

  public ActionForward disableFacebook( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    getParticipantService().updateFacebookInfo( UserManager.getUserId(), null );

    return mapping.findForward( ActionConstants.SUCCESS_FORWARD );
  }

  public ActionForward disableUA( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    try
    {
      getUnderArmourService().deAuthorizeParticipant( UserManager.getUserId() );
      writeAsJsonToResponse( "success:true", response );
    }
    catch( Exception e )
    {
      log.error( e.getMessage(), e );
    }

    return null;
  }

  /**
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   * @throws Exception 
   */
  public ActionForward updatePreferences( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    final String METHOD_NAME = "updatePreferences";
    ActionForward forward = null;
    UserForm participantForm = (UserForm)form;
    boolean preferenceUpdated = false;
    WebErrorMessageList errorMessages = new WebErrorMessageList();
    WebErrorMessage errorMessage = new WebErrorMessage();
    Long userId = UserManager.getUserId();
    if ( participantForm.isViewCurrentUser() )
    {
      userId = UserManager.getUserId();
    }
    else
    {
      userId = participantForm.getId();
    }

    try
    {
      if ( participantForm.isUaDeAuthorize() )
      {
        getUnderArmourService().deAuthorizeParticipant( UserManager.getUserId() );
      }
    }
    catch( Exception e )
    {
      log.error( e );
    }
    Fields paxActionNameFields = new Fields();

    if ( isCancelled( request ) )
    {
      // EARLY EXIT
      LOG.info( ">>> " + METHOD_NAME + " cancelled." );
      // If viewing the current user, then go the home page.
      // Otherwise, go to the overview page
      if ( participantForm.isViewCurrentUser() )
      {
        // bugFix 17868
        return mapping.findForward( ActionConstants.SUCCESS_TO_HOMEPAGE );
      }
      else
      {
        Map clientStateParameterMap = new HashMap();
        clientStateParameterMap.put( "userId", userId );
        String queryString = ClientStateUtils.generateEncodedLink( "", "", clientStateParameterMap );
        String method = "method=display";
        // bugFix 17868
        return ActionUtils.forwardWithParameters( mapping, ActionConstants.SUCCESS_FORWARD, new String[] { queryString, method } );
      }
    }

    ActionMessages errors = new ActionMessages();
    UserEmailAddress email = null;
    if ( participantForm.getEmailAddress() != null && participantForm.getEmailAddress().length() > 0 && participantForm.getEmailType() != null )
    {
      email = new UserEmailAddress();
      email.setEmailAddr( participantForm.getEmailAddress() );
      email.setEmailType( EmailAddressType.lookup( participantForm.getEmailType() ) );
      email.setIsPrimary( new Boolean( true ) );
      email.setVerificationStatus( VerificationStatusType.lookup( VerificationStatusType.UNVERIFIED ) );
    }

    UserEmailAddress textMessageAddress = null;
    if ( participantForm.getTextMessageAddress() != null && participantForm.getTextMessageAddress().length() > 0 )
    {
      textMessageAddress = new UserEmailAddress();
      textMessageAddress.setEmailAddr( participantForm.getTextMessageAddress() );
      textMessageAddress.setEmailType( EmailAddressType.lookup( EmailAddressType.SMS ) );
      textMessageAddress.setVerificationStatus( VerificationStatusType.lookup( VerificationStatusType.UNVERIFIED ) );
      // The following code is added to fix Bug# 25898 START
      UserEmailAddress userEmailAddress = getUserService().getPrimaryUserEmailAddress( userId );
      if ( userEmailAddress != null )
      {
        textMessageAddress.setIsPrimary( new Boolean( false ) );
      }
      else
      {
        textMessageAddress.setIsPrimary( new Boolean( true ) );
      }
      // END
    }

    // Bug #24270 Start
    UserAddress userAddress = getUserService().getPrimaryUserAddress( userId );
    // get Allow_SMS flag from table Country. START
    boolean isUserCountryAllowedForTextMessages = false;

    if ( userAddress != null )
    {
      if ( userAddress.getAddress() != null && userAddress.getAddress().getCountry() != null )
      {
        isUserCountryAllowedForTextMessages = userAddress.getAddress().getCountry().getAllowSms();
      }
    }
    // END
    request.setAttribute( "isUserCountryAllowedForTextMessages", new Boolean( isUserCountryAllowedForTextMessages ) );
    if ( isUserCountryAllowedForTextMessages )
    {
      try
      {

        if ( participantForm.getTextPhoneNbr() != null && !participantForm.getTextPhoneNbr().trim().equals( "" ) )
        {
          paxActionNameFields = validatePrefForm( participantForm );

          if ( StringUtils.isEmpty( paxActionNameFields.getText() ) )
          {
            // User Mobile Phone
            UserPhone userPhone = getUserService().getUserPhone( userId, PhoneType.MOBILE );
            // insert the Mobile Phone data
            boolean sendConfirmationMessage = false;
            if ( userPhone == null )
            {
              userPhone = new UserPhone();
              userPhone.setPhoneType( PhoneType.lookup( PhoneType.MOBILE ) );
              userPhone.setVerificationStatus( VerificationStatusType.lookup( VerificationStatusType.UNVERIFIED ) );
              userPhone.setIsPrimary( Boolean.valueOf( false ) );
              userPhone.setPhoneNbr( participantForm.getTextPhoneNbr() );
              userPhone.setCountryPhoneCode( participantForm.getCountryPhoneCode() );
              getUserService().addUserPhone( userId, userPhone );
              sendConfirmationMessage = true;

            }
            else
            { // update the Mobile phone data
              if ( userPhone.getPhoneNbr().equals( participantForm.getTextPhoneNbr() ) )
              {
                sendConfirmationMessage = true;
              }
              userPhone.setPhoneNbr( participantForm.getTextPhoneNbr() );
              userPhone.setCountryPhoneCode( participantForm.getCountryPhoneCode() );
              getUserService().updateUserPhone( userId, userPhone );
            }

            if ( sendConfirmationMessage )
            {
              String message = CmsResourceBundle.getCmsBundle().getString( "participant.preference.edit", "TEXT_MESSAGE_UPDATED" );
              String companyName = getSystemVariableService().getPropertyByName( SystemVariableService.CLIENT_NAME ).getStringVal();
              String formatedMesg = message.replace( "${companyName}", companyName );
              getMailingService().sendSmsMessage( userId, participantForm.getCountryPhoneCode(), participantForm.getTextPhoneNbr(), formatedMesg );
            }

            // Save Termas and condition flag
            if ( !StringUtils.isEmpty( participantForm.getAcceptTermsOnTextMessages() ) )
            {
              if ( participantForm.getAcceptTermsOnTextMessages().equals( "yes" ) )
              {
                Participant participant = (Participant)getParticipantService().getParticipantById( userId );
                participant.setAcceptTermsOnTextMessages( true );
                getParticipantService().saveParticipant( participant );
              }
            }
          }
          else
          {

            errorMessage.getFields().add( paxActionNameFields );
            errorMessage.setType( "error" );
            errorMessage.setCode( WebResponseConstants.RESPONSE_CODE_VALIDATION_ERROR );
          }
        }
        else
        {
          // uncheck terms and conditions -- if it already set
          // Remove Phone Number -- if User had already
          Participant participant = (Participant)getParticipantService().getParticipantById( userId );
          UserPhone userPhone = getUserService().getUserPhone( userId, PhoneType.MOBILE );
          if ( participant.isAcceptTermsOnTextMessages() )
          {
            participant.setAcceptTermsOnTextMessages( false );
            getParticipantService().saveParticipant( participant );
          }
          if ( userPhone != null )
          {
            getUserService().deleteUserPhone( userId, PhoneType.MOBILE );
          }

        }
      }
      catch( ServiceErrorException se )
      {
        log.error( se.getMessage(), se );
      }
    }
    // Bug #24270 End
    // BugFix 17765 Starts
    // immediately update the language for this session
    if ( !StringUtils.isEmpty( participantForm.getLanguage() ) && participantForm.isViewCurrentUser() )
    {
      String previousCMSLanguage = (String)request.getSession().getAttribute( "cmsLocaleCode" );
      request.setAttribute( "userLanguage", LanguageType.lookup( participantForm.getLanguage() ) );
      request.getSession().setAttribute( "cmsLocaleCode", participantForm.getLanguage() );
      request.getSession().removeAttribute( "filteredModuleList" );

      String languageCode = participantForm.getLanguage();
      int index = languageCode.indexOf( '_' );
      if ( index == -1 )
      {
        UserManager.getUser().setLocale( new Locale( languageCode ) );
      }
      else
      {
        UserManager.getUser().setLocale( new Locale( languageCode.substring( 0, index ), languageCode.substring( index + 1 ) ) );
      }

      // check if there is any language change
      if ( previousCMSLanguage != null && !previousCMSLanguage.equalsIgnoreCase( (String)request.getSession().getAttribute( "cmsLocaleCode" ) ) )
      {
        buildFilterSetUpList( request, participantForm.getLanguage() );
      }

    }

    if ( StringUtils.isEmpty( paxActionNameFields.getText() ) )
    {
      try
      {
    	// Client customizations for wip #26532 starts
          getParticipantService().updateParticipantPreferences( userId,
                                                                participantForm.getContactMethodTypes(),
                                                                participantForm.getactiveSMSGroupTypes(),
                                                                participantForm.getContactMethods(),
                                                                participantForm.getLanguage(),
                                                                email,
                                                                textMessageAddress,
                                                                participantForm.isAllowPublicRecognition(),
                                                                participantForm.isAllowPublicInformation(),
                                                                participantForm.isAllowPublicBirthDate(),
                                                                participantForm.isAllowPublicHireDate(),
                                                                participantForm.isAllowSharePurlToOutsiders(),
                                                                participantForm.isAllowPurlContributionsToSeeOthers());
          // Client customizations for wip #26532 ends
        preferenceUpdated = true;
        errorMessage.setCode( "" );
        errorMessage.setText( "" );
        getAudienceService().rematchParticipantForAllCriteriaAudiences( userId );
      }
      catch( ServiceErrorException e )
      {
        log.debug( e );
        List serviceErrors = e.getServiceErrors();
        ServiceErrorStrutsUtils.convertServiceErrorsToActionErrors( serviceErrors, errors );
        saveErrors( request, errors );
        log.error( e.getMessage(), e );
        log.error( errors );
      }
    }
    else
    {
      errorMessage = WebErrorMessage.addErrorMessage( errorMessage );
      errorMessages.getMessages().add( errorMessage );
      request.setAttribute( "preferenceSaved", "false" );
      super.writeAsJsonToResponse( errorMessages, response );
      return null;
    }

    if ( preferenceUpdated )
    {
      Date date = new Date();
      long dateInSeconds = date.getTime();
      errorMessage.setUrl( RequestUtils.getBaseURI( request ) + PageConstants.PRIVATE_PROFILE_VIEW + "?" + dateInSeconds + "=success#tab/Preferences" );
      errorMessage = WebErrorMessage.addServerCmd( errorMessage );
      errorMessage.setType( WebResponseConstants.RESPONSE_TYPE_SERVER_CMD );
      errorMessage.setText( CmsResourceBundle.getCmsBundle().getString( "profile.personal.info.PREFERENCE_SAVE" ) );
      errorMessage.setName( CmsResourceBundle.getCmsBundle().getString( "system.general.SUCCESS" ) );
      errorMessages.getMessages().add( errorMessage );
      request.getSession().setAttribute( "preferenceSaved", Boolean.TRUE );
      super.writeAsJsonToResponse( errorMessages, response );
      return null;

    }
    else
    {
      errorMessage.setCode( "error" );
      errorMessage.setText( WebResponseConstants.RESPONSE_TYPE_SERVER_UNEXPECTED_ERROR );
      errorMessage = WebErrorMessage.addErrorMessage( errorMessage );
      errorMessages.getMessages().add( errorMessage );
      super.writeAsJsonToResponse( errorMessages, response );
      return null;
    }
    // This is unreachable code. Right now commenting it out.
    /*
     * // If viewing the preference of the current user, then go the home page. // Otherwise, go to
     * the overview page if ( participantForm.isViewCurrentUser() ) { forward = mapping.findForward(
     * ActionConstants.SUCCESS_TO_HOMEPAGE ); } else { Map clientStateParameterMap = new HashMap();
     * clientStateParameterMap.put( "userId",userId); String queryString =
     * ClientStateUtils.generateEncodedLink( "", "", clientStateParameterMap ); String method =
     * "method=display"; forward = ActionUtils.forwardWithParameters( mapping,
     * ActionConstants.SUCCESS_FORWARD, new String[] { queryString, method } ); } if (
     * !errors.isEmpty() ) { request.setAttribute( "preferenceSaved", "false" );
     * super.writeAsJsonToResponse( errorMessages, response ); return null; } return forward;
     */

  }

  private boolean checkPhoneNumberFormat( String textPhoneNbr )
  {
    String textPhoneNbrTrm = textPhoneNbr.trim();
    // check for Length
    if ( ! ( textPhoneNbrTrm.length() == 12 || textPhoneNbrTrm.length() == 10 ) )
    {
      return false;
    }
    else
    {
      // if length is 10. ex:0123456789.
      if ( textPhoneNbrTrm.length() == 10 )
      {
        return textPhoneNbrTrm.matches( "\\d{10}" );
      }
      else
      // If Length is 12.
      {
        // check for 4th and 7th palces
        if ( ! ( textPhoneNbrTrm.charAt( 3 ) == '-' ) || ! ( textPhoneNbrTrm.charAt( 7 ) == '-' ) )
        {
          return false;
        }
        return getOnlyNumerics( textPhoneNbrTrm );
      }
    }
  }

  private boolean getOnlyNumerics( String str )
  {
    char c;
    for ( int i = 0; i < str.length(); i++ )
    {
      if ( i != 3 && i != 7 )
      {
        c = str.charAt( i );
        if ( !Character.isDigit( c ) )
        {
          return false;
        }
      }
    }
    return true;
  }

  private boolean isAllowEstatements()
  {
    boolean isAllowEstatements = getSystemVariableService().getPropertyByName( SystemVariableService.PARTICIPANT_ALLOW_ESTATEMENTS ).getBooleanVal();
    return isAllowEstatements;
  }

  private SystemVariableService getSystemVariableService()
  {
    return (SystemVariableService)getService( SystemVariableService.BEAN_NAME );
  }

  private HierarchyService getHierarchyService()
  {
    return (HierarchyService)getService( HierarchyService.BEAN_NAME );
  }

  private NodeService getNodeService()
  {
    return (NodeService)getService( NodeService.BEAN_NAME );
  }

  private UserService getUserService()
  {
    return (UserService)getService( UserService.BEAN_NAME );
  }

  private ParticipantService getParticipantService()
  {
    return (ParticipantService)getService( ParticipantService.BEAN_NAME );
  }

  private AudienceService getAudienceService()
  {
    return (AudienceService)getService( AudienceService.BEAN_NAME );
  }

  /**
   * Returns a reference to the Country service.
   * 
   * @return a reference to the Country service.
   */
  private CountryService getCountryService()
  {
    return (CountryService)getService( CountryService.BEAN_NAME );
  }

  private AuthenticationService getAuthenticationService()
  {
    return (AuthenticationService)getService( AuthenticationService.BEAN_NAME );
  }

  private AuthorizationService getAuthorizationService()
  {
    return (AuthorizationService)getService( AuthorizationService.BEAN_NAME );
  }

  protected MailingService getMailingService()
  {
    return (MailingService)BeanLocator.getBean( MailingService.BEAN_NAME );
  }

  protected TwitterService getTwitterService()
  {
    return (TwitterService)BeanLocator.getBean( TwitterService.BEAN_NAME );
  }

  protected FacebookService getFacebookService()
  {
    return (FacebookService)BeanLocator.getBean( FacebookService.BEAN_NAME );
  }

  protected WelcomeEmailService getWelcomeEmailService()
  {
    return (WelcomeEmailService)getService( WelcomeEmailService.BEAN_NAME );
  }

  private FilterAppSetupService getFilterAppSetupService()
  {
    return (FilterAppSetupService)getService( FilterAppSetupService.BEAN_NAME );
  }

  @SuppressWarnings( "unchecked" )
  private void buildFilterSetUpList( HttpServletRequest request, String language ) throws ServletException
  {
    // set the content reader here
    javax.servlet.ServletContext cmsServletContext = request.getSession().getServletContext().getContext( request.getContextPath() );

    try
    {
      ApplicationContext cmsAppContext = (ApplicationContext)cmsServletContext.getAttribute( WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE );
      // get the cmsContentReaderFactory and cmsConfiguration beans from the Beacon
      // applicationContext.
      ContentReaderFactory readerFactory = (ContentReaderFactory)ApplicationContextFactory.getApplicationContext().getBean( "cmsContentReaderFactory" );

      ContentReader reader = readerFactory.createContentReader( request );
      reader.setApplicationContext( cmsAppContext );
      ContentReaderManager.setContentReader( reader );
    }
    catch( CmsServiceException e )
    {
      throw new ServletException( e.getMessage(), e );
    }

    Participant participant = getParticipantService().getParticipantById( UserManager.getUserId() );
    HomePageFilterHolder filterHolder = getFilterAppSetupService().getHomePageModulesForUser( participant, buildSessionMap( request ) );
    List<FilterSetupType> filterSetupList = filterHolder.getActiveFilters();
    filterSetupList.add( FilterSetupType.lookup( FilterSetupType.REPORTS ) );
    Collections.sort( filterSetupList, new PickListItemSortOrderComparator() );
    request.getSession().setAttribute( "filterList", filterSetupList );
  }

  @SuppressWarnings( "rawtypes" )
  private Map<String, Object> buildSessionMap( HttpServletRequest request )
  {
    Map<String, Object> sessionMap = new HashMap<String, Object>();
    HttpSession session = request.getSession();
    Enumeration keys = session.getAttributeNames();
    while ( keys.hasMoreElements() )
    {
      String key = (String)keys.nextElement();
      sessionMap.put( key, session.getAttribute( key ) );
    }
    return sessionMap;
  }

  public Fields validatePrefForm( UserForm participantForm )
  {
    Fields paxActionNameFields = new Fields();
    boolean checkPhoneNumberFormat = checkPhoneNumberFormat( participantForm.getTextPhoneNbr() );

    if ( !checkPhoneNumberFormat )
    {
      paxActionNameFields.setText( CmsResourceBundle.getCmsBundle().getString( "participant.errors.MOBILE_PHONE_VALIDATION" ) );
      paxActionNameFields.setName( FormFieldConstants.PARTICIPANT_FORM_PHONE_NUMBER );
    }
    if ( participantForm.getAcceptTermsOnTextMessages() == null || participantForm.getAcceptTermsOnTextMessages().trim().equals( "" ) )
    {
      // force the User to select Terms check box...
      paxActionNameFields.setText( CmsResourceBundle.getCmsBundle().getString( "participant.errors.TERMS_CONDITIONS_REQUIRED_TEXT_MESSAGES" ) );
      paxActionNameFields.setName( FormFieldConstants.PARTICIPANT_FORM_TERMS_AND_CONDITIONS );
    }

    return paxActionNameFields;

  }

}

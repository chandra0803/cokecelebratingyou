/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/participant/ParticipantAction.java,v $
 */

package com.biperf.core.ui.participant;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.biperf.core.domain.country.Country;
import com.biperf.core.domain.enums.CountryStatusType;
import com.biperf.core.domain.enums.EmailAddressType;
import com.biperf.core.domain.enums.HierarchyRoleType;
import com.biperf.core.domain.enums.LanguageType;
import com.biperf.core.domain.enums.ParticipantPreferenceCommunicationsType;
import com.biperf.core.domain.enums.ParticipantStatus;
import com.biperf.core.domain.enums.ParticipantTermsAcceptance;
import com.biperf.core.domain.enums.PhoneType;
import com.biperf.core.domain.enums.PromotionType;
import com.biperf.core.domain.enums.PurlContributorState;
import com.biperf.core.domain.enums.PurlRecipientState;
import com.biperf.core.domain.enums.SSIContestStatus;
import com.biperf.core.domain.enums.VerificationStatusType;
import com.biperf.core.domain.hierarchy.Hierarchy;
import com.biperf.core.domain.hierarchy.Node;
import com.biperf.core.domain.mailing.Mailing;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.participant.ParticipantCommunicationPreference;
import com.biperf.core.domain.participant.ParticipantEmployer;
import com.biperf.core.domain.purl.PurlContributor;
import com.biperf.core.domain.purl.PurlRecipient;
import com.biperf.core.domain.ssi.SSIContest;
import com.biperf.core.domain.system.PropertySetItem;
import com.biperf.core.domain.user.User;
import com.biperf.core.domain.user.UserAddress;
import com.biperf.core.domain.user.UserEmailAddress;
import com.biperf.core.domain.user.UserNode;
import com.biperf.core.domain.user.UserPhone;
import com.biperf.core.domain.user.UserTNCHistory;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.security.AuthenticatedUser;
import com.biperf.core.security.credentials.LoginAsCredentials;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.chatter.ChatterService;
import com.biperf.core.service.country.CountryService;
import com.biperf.core.service.email.MailingService;
import com.biperf.core.service.email.WelcomeEmailService;
import com.biperf.core.service.facebook.FacebookService;
import com.biperf.core.service.hierarchy.HierarchyService;
import com.biperf.core.service.hierarchy.NodeService;
import com.biperf.core.service.message.MessageService;
import com.biperf.core.service.participant.AudienceService;
import com.biperf.core.service.participant.AutoCompleteService;
import com.biperf.core.service.participant.ParticipantAssociationRequest;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.participant.UserAssociationRequest;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.service.promotion.impl.PromotionServiceImpl;
import com.biperf.core.service.purl.PurlService;
import com.biperf.core.service.purl.impl.PurlContributorAssociationRequest;
import com.biperf.core.service.security.AuthenticationService;
import com.biperf.core.service.security.AuthorizationService;
import com.biperf.core.service.ssi.SSIContestService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.service.twitter.TwitterService;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.ui.node.NodeSearchAction;
import com.biperf.core.ui.user.AddressFormBean;
import com.biperf.core.ui.user.LockoutInfo;
import com.biperf.core.ui.user.UserForm;
import com.biperf.core.ui.utils.ActionUtils;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.ui.utils.ServiceErrorStrutsUtils;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;
import com.biperf.core.utils.ClientStateUtils;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.InvalidClientStateException;
import com.biperf.core.utils.JsonUtils;
import com.biperf.core.utils.PageConstants;
import com.biperf.core.utils.RequestUtil;
import com.biperf.core.utils.SSIContestUtil;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.FormattedValueBean;
import com.biperf.core.value.PromotionPaxValue;
import com.biperf.util.StringUtils;
import com.biw.hc.core.service.HCServices;
import com.objectpartners.cms.util.CmsResourceBundle;
import com.biperf.core.service.client.TccAdminLoginInfoService;

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
public class ParticipantAction extends BaseDispatchAction
{

  /** Log */
  private static final Log LOG = LogFactory.getLog( ParticipantAction.class );

  private static final String CHATTER_INSTANCE_URL = "chatter_instance_url";
  private static final String STATE = "state";
  private static final String CHATTER_AUTH_CODE = "code";
  private static final String CHATTER_ISSUED_AT = "chatter_issued_at";
  private static final String CHATTER_ACCESS_TOKEN = "chatter_access_token";
  private static final String SESSION_PAX_FORM = "sessionPaxForm";
  private static final String CM_KEY_PREFIX = "user.profile.";

  @Autowired
  private AutoCompleteService autoCompleteService;

  /**
   * @param actionMapping
   * @param actionForm
   * @param request
   * @param response
   * @return ActionForward
   */
  @Override
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
  @Override
  public ActionForward cancelled( ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response )
  {
    return actionMapping.findForward( ActionConstants.CANCEL_FORWARD );
  }

  /**
   * Switch logged in user to the currently selected participant.
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward loginAs( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    UserForm participantForm = (UserForm)form;
    request.getSession().setAttribute( "loginUserId", UserManager.getUserId() );
    String userIdString = participantForm.getUserId();
    User user = getUserService().getUserById( new Long( userIdString ) );
    //-------------------------------
    // customization WIP # 20160 start
    //-------------------------------
    Long adminUserId = UserManager.getUserId();
    Long paxUserId = new Long(userIdString);
    
    getTccAdminLoginInfoService().save( adminUserId, paxUserId );
    
    //-------------------------------
    // customization WIP 20160 end
    //-------------------------------

    LOG.info( "Switching User credentials from user " + user.getUserName() + " to user" + UserManager.getUserName() );

    LoginAsCredentials loginAsCredentials = new LoginAsCredentials();
    AuthenticatedUser newAuthenticatedUser = getAuthenticationService().buildAuthenticatedUser( user, loginAsCredentials );
    newAuthenticatedUser.setOriginalAuthenticatedUser( UserManager.getUser() );
    Object principal = newAuthenticatedUser;
    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken( principal, loginAsCredentials, newAuthenticatedUser.getAuthorities() );

    // setting the new flag isLaunched whenever we launch the site as someone
    newAuthenticatedUser.setLaunched( true );

    authentication.setDetails( newAuthenticatedUser );
    // With new ACEGI upgrade, the constructor already sets authenticated to true..
    // authentication.setAuthenticated( true );

    SecurityContext sc = SecurityContextHolder.getContext();
    sc.setAuthentication( authentication );

    request.getSession().removeAttribute( "filteredModuleList" ); // bug 3853
    request.getSession().removeAttribute( "eligiblePromotions" ); // bug 3853

    // clear current point balance that's in session
    refreshPointBalance( request );

    /*WIP 20160 customization start */
    request.getSession().setAttribute( "launchUser",new Boolean(true));
    request.getSession().setAttribute("adminUserId", adminUserId);
    /*WIP 20160 customization end */

    // get the actionForward to display the create pages.
    return mapping.findForward( ActionConstants.SUCCESS_FORWARD );
  }

  /**
   * Switch logged in user to the currently selected participant.
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward loginAsSSIAdmin( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    Long ssiContestId = null;
    try
    {
      String clientState = RequestUtils.getRequiredParamString( request, "clientState" );
      String cryptoPass = RequestUtils.getOptionalParamString( request, "cryptoPass" );
      String password = ClientStatePasswordManager.getPassword();

      if ( cryptoPass != null && cryptoPass.equals( "1" ) )
      {
        password = ClientStatePasswordManager.getGlobalPassword();
      }
      // Deserialize the client state.
      Map clientStateMap = ClientStateSerializer.deserialize( clientState, password );
      try
      {
        String ssiContestIdString = (String)clientStateMap.get( "ssiContestId" );
        ssiContestId = new Long( ssiContestIdString );
      }
      catch( ClassCastException cce )
      {
        ssiContestId = (Long)clientStateMap.get( "ssiContestId" );
      }
    }
    catch( InvalidClientStateException e )
    {

      throw new IllegalArgumentException( "request parameter clientState was missing" );

    }

    SSIContest ssiContest = getSSIContestService().getContestById( ssiContestId );

    UserForm participantForm = (UserForm)form;
    participantForm.setUserId( ssiContest.getContestOwnerId().toString() );
    String userIdString = participantForm.getUserId();
    User user = getUserService().getUserById( new Long( userIdString ) );

    LOG.info( "Switching User credentials from user " + user.getUserName() + " to user" + UserManager.getUserName() );

    LoginAsCredentials loginAsCredentials = new LoginAsCredentials();
    AuthenticatedUser newAuthenticatedUser = getAuthenticationService().buildAuthenticatedUser( user, loginAsCredentials );

    Object principal = newAuthenticatedUser;
    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken( principal, loginAsCredentials, newAuthenticatedUser.getAuthorities() );
    newAuthenticatedUser.setSSIAdmin( true );
    newAuthenticatedUser.setOriginalAuthenticatedUser( UserManager.getUser() );

    // setting the new flag isLaunched whenever we launch the site as someone
    newAuthenticatedUser.setLaunched( true );

    authentication.setDetails( newAuthenticatedUser );

    SecurityContext sc = SecurityContextHolder.getContext();
    sc.setAuthentication( authentication );

    request.getSession().removeAttribute( "filteredModuleList" ); // bug 3853
    request.getSession().removeAttribute( "eligiblePromotions" ); // bug 3853

    // clear current point balance that's in session
    refreshPointBalance( request );

    String ssiAdnminurl = null;
    if ( participantForm.getSsiAdminAction().equals( "editContest" ) )
    {

      if ( ssiContest.getStatus().getCode().equals( SSIContestStatus.DRAFT ) )
      {
        ssiAdnminurl = RequestUtils.getBaseURI( request ) + "/ssi/createGeneralInfo.do?method=prepareEdit&contestId=" + SSIContestUtil.getClientState( ssiContestId, user.getId() );
      }
      else
      {
        ssiAdnminurl = RequestUtils.getBaseURI( request ) + "/ssi/createGeneralInfo.do?method=prepareEdit&currentStep=5&contestId=" + SSIContestUtil.getClientState( ssiContestId, user.getId() );
      }
    }
    else if ( participantForm.getSsiAdminAction().equals( "updateContest" ) )
    {
      String siteUrl = getSysUrl();

      Map<String, String> paramMap = new HashMap<String, String>();
      paramMap.put( SSIContestUtil.CONTEST_ID, ssiContestId.toString() );
      paramMap.put( SSIContestUtil.USER_ID, user.getId().toString() );
      ssiAdnminurl = ClientStateUtils.generateEncodedLink( siteUrl, PageConstants.SSI_CREATOR_DETAIL_URL, paramMap, false, SSIContestUtil.SSI_CLIENTSTATE_PARAM_ID );
    }
    else if ( participantForm.getSsiAdminAction().equals( "viewContest" ) )
    {

      Map<String, Object> clientStateParameterMap = new HashMap<String, Object>();
      clientStateParameterMap.put( "contestId", ssiContestId.toString() );
      clientStateParameterMap.put( "role", SSIContest.CONTEST_ROLE_CREATOR );
      ssiAdnminurl = ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI( request ), PageConstants.SSI_APPROVAL_SUMMARY, clientStateParameterMap );
    }
    request.getSession().setAttribute( "ssiAdnminurl", ssiAdnminurl );
    // get the actionForward to display the create pages.
    return mapping.findForward( ActionConstants.SUCCESS_FORWARD );
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

    // get the actionForward to display the create pages.
    return mapping.findForward( ActionConstants.CREATE_FORWARD );
  }

  /**
   * Country has changed on the form, used just to reload it, and forward back again.
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward changeCountry( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {

    final String METHOD_NAME = "changeCountry";

    log.info( ">>> " + METHOD_NAME );
    UserForm participantForm = (UserForm)form;
    participantForm.setCountryPhoneCode( participantForm.getAddressFormBean().getCountryCode() );
    // get system variables on how T&Cs are used and set them on the Form
    participantForm.setTermsConditionsRequired();
    participantForm.setUserAllowedToAcceptForPax();

    // Country has changed on the Form, used just to reload it, and forward back again
    ActionForward forward = mapping.findForward( ActionConstants.CREATE_FORWARD );

    log.info( "<<< " + METHOD_NAME );

    return forward;
  } // end changeCountry

  /**
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward prepareUpdatePersonalInfo( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    UserForm participantForm = (UserForm)form;

    // default T&Cs
    participantForm.defaultTermsAndConditions();

    Participant participant = getParticipantService().getParticipantById( new Long( participantForm.getUserId() ) );

    participantForm.loadParticipantPersonalInfo( participant );
    participantForm.setMethod( "updatePersonalInfo" );

    // get the actionForward to display the create pages.
    return mapping.findForward( ActionConstants.UPDATE_FORWARD );
  }

  /**
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward prepareEmailAddressesDisplay( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    String fromPaxScreen = "fromPaxScreen=true";
    Map clientStateParameterMap = new HashMap();
    clientStateParameterMap.put( "userId", ( (UserForm)form ).getUserId() );
    String queryString = ClientStateUtils.generateEncodedLink( "", "", clientStateParameterMap );
    String method = "method=displayList";
    ActionForward forward = ActionUtils.forwardWithParameters( mapping, "email", new String[] { fromPaxScreen, queryString, method } );

    // get the actionForward to display the create pages.
    return forward;
  }

  /**
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward preparePhoneListDisplay( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    String fromPaxScreen = "fromPaxScreen=true";
    Map clientStateParameterMap = new HashMap();
    clientStateParameterMap.put( "userId", ( (UserForm)form ).getUserId() );
    String queryString = ClientStateUtils.generateEncodedLink( "", "", clientStateParameterMap );
    String method = "method=displayList";
    ActionForward forward = ActionUtils.forwardWithParameters( mapping, "phone", new String[] { fromPaxScreen, queryString, method } );

    // get the actionForward to display the create pages.
    return forward;
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
    participant.setProfileSetupDone( false );
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
      userNode.setIsPrimary( new Boolean( true ) );

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
    catch( Exception e )
    {
      log.error( e.getMessage() );
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
  public ActionForward updatePersonalInfo( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    final String METHOD_NAME = "updatePersonalInfo";

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
      return mapping.findForward( ActionConstants.FAIL_UPDATE ); // EARLY EXIT
    }

    UserForm participantForm = (UserForm)form;

    // code fix for bug 18302 and 20618
    // Replacing paxOverview call with specific associations
    // Participant participant = getParticipantService().getParticipantOverviewById( new Long(
    // participantForm
    // .getId() ) );

    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
    associationRequestCollection.add( new ParticipantAssociationRequest( ParticipantAssociationRequest.EMPLOYER ) );
    Participant participant = getParticipantService().getParticipantByIdWithAssociations( new Long( participantForm.getId() ), associationRequestCollection );

    if ( participant.getOptOutAwards() != null && ! ( participant.getOptOutAwards().equals( participantForm.isOptOutAwards() ) ) && participantForm.isOptOutAwards() )
    {
      // PAX Notification
      Mailing createMailing = getMailingService().buildPAXOptOutOfAwardsNotification( participant.getId(), false, null );
      getMailingService().submitMailing( createMailing, null );

      // Owner/Managers Notification
      Set<User> managers = getNodeService().getNodeOwnerOrManagerForUser( participant, participant.getPrimaryUserNode().getNode() );
      for ( User manager : managers )
      {
        createMailing = getMailingService().buildPAXOptOutOfAwardsNotification( manager.getId(), true, participant );
        getMailingService().submitMailing( createMailing, null );
      }

    }

    participantForm.setParticipantPersonalInfo( participant );

    // JIRA - G6-2413
    /*
     * List employerinfo = participant.getParticipantEmployers(); if (
     * !participantForm.getPaxStatus().equals( ParticipantStatus.lookup( ParticipantStatus.ACTIVE
     * ).getCode() ) ) { for ( Iterator iter = employerinfo.iterator(); iter.hasNext(); ) {
     * ParticipantEmployer paxEmployer = (ParticipantEmployer)iter.next(); if ( ! (
     * participant.getActive() ).booleanValue() && paxEmployer.getTerminationDate() == null ) {
     * paxEmployer.setTerminationDate( DateUtils.getCurrentDate() ); participant.setTerminationDate(
     * DateUtils.getCurrentDate() ); getParticipantService().saveParticipantEmployer(
     * participant.getId(), paxEmployer, participantForm ); break; } } }
     */
    List employerinfo = participant.getParticipantEmployers();
    for ( Iterator iter = employerinfo.iterator(); iter.hasNext(); )
    {
      ParticipantEmployer paxEmployer = (ParticipantEmployer)iter.next();
      if ( ! ( participant.getActive() ).booleanValue() && paxEmployer.getTerminationDate() == null )
      {
        List<PurlRecipient> purlRecipients = getPurlService().getPurlRecipientByUserId( participant.getId() );
        if ( null != purlRecipients && !purlRecipients.isEmpty() )
        {
          for ( PurlRecipient purlRecipient : purlRecipients )
          {
            AssociationRequestCollection associationRequestCol = new AssociationRequestCollection();
            associationRequestCol.add( new PurlContributorAssociationRequest( PurlContributorAssociationRequest.PURL_RECIPIENT_CONTRIBUTORS ) );
            List<PurlContributor> purlContributors = getPurlService().getAllPurlContributions( purlRecipient.getId(), associationRequestCol );

            for ( PurlContributor purlContributor : purlContributors )
            {
              purlContributor.setState( PurlContributorState.lookup( PurlContributorState.EXPIRED ) );
              getPurlService().savePurlContributor( purlContributor );
            }
            purlRecipient.setState( PurlRecipientState.lookup( PurlRecipientState.EXPIRED ) );
            getPurlService().savePurlRecipient( purlRecipient );
          }
        }
        getParticipantService().saveParticipantEmployer( participant.getId(), paxEmployer );
        break;
      }
    }
    try
    {
      if ( !participantForm.getOriginalPaxTermsAcceptFromDB().equals( participantForm.getPaxTermsAccept() ) )
      {
        UserTNCHistory userTNCHistory = participantForm.addPaxTNCHistory( participant );
        getParticipantService().saveParticipantWithTNCHistory( participant, userTNCHistory );
      }
      else
      {
        getParticipantService().saveParticipant( participant );
      }

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
   * @param actionMapping
   * @param actionForm
   * @param request
   * @param response
   * @return ActionForward
   */
  @SuppressWarnings( "unchecked" )
  public ActionForward unlockAccount( ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response )
  {
    if ( isCancelled( request ) )
    {
      // EARLY EXIT
      return actionMapping.findForward( ActionConstants.CANCEL_FORWARD );
    }

    ActionMessages errors = new ActionMessages();
    UserForm userForm = (UserForm)actionForm;

    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
    associationRequestCollection.add( new UserAssociationRequest( UserAssociationRequest.EMAIL ) );
    associationRequestCollection.add( new UserAssociationRequest( UserAssociationRequest.PHONE ) );

    Participant participant = getParticipantService().getParticipantByIdWithAssociations( new Long( userForm.getUserId() ), associationRequestCollection );

    // TODO: should this be assumed to be the technique or should we call to auth svc.
    participant.setLoginFailuresCount( new Integer( 0 ) );
    participant.setAccountLocked( false );
    participant.setLockTimeoutExpireDate( null );
    try
    {
      getParticipantService().saveParticipant( participant );

      UserEmailAddress primaryEmail = participant.getPrimaryEmailAddress();
      boolean sharedPrimaryContact = ( null == primaryEmail ) ? false : getUserService().getUserIdsByEmailOrPhone( primaryEmail.getEmailAddr() ).size() > 1;
      Mailing passwordChangeMailing = getMailingService().buildUnLockAccountNotification( participant.getId(), sharedPrimaryContact, MessageService.ACCOUNT_UN_LOCKED );
      getMailingService().submitMailing( passwordChangeMailing, null, participant.getId() );
      // alert all mobile phones

      Set<UserPhone> userPhoneSet = participant.getUserPhones();
      List<UserPhone> userPhoneList = userPhoneSet.stream().collect( Collectors.toList() );
      List<UserPhone> distinctUserPhone = userPhoneList.stream().filter( distinctByKey( e -> e.getPhoneNbr() ) ).collect( Collectors.toList() );

      for ( UserPhone phone : distinctUserPhone )
      {
        if ( phone.getPhoneType().equals( PhoneType.lookup( PhoneType.MOBILE ) ) || phone.getPhoneType().equals( PhoneType.lookup( PhoneType.RECOVERY ) ) )
        {
          String txtMessage = getMailingService().buildUnLockAccountMobileNotification( MessageService.ACCOUNT_UN_LOCKED );

          getMailingService().sendSmsMessage( phone.getUser().getId(), phone.getCountryPhoneCode(), phone.getPhoneNbr(), txtMessage );
        }
      }
      request.setAttribute( "accountJustUnlocked", Boolean.TRUE );
    }
    catch( ServiceErrorException e )
    {
      log.debug( e, e );
      List serviceErrors = e.getServiceErrors();
      ServiceErrorStrutsUtils.convertServiceErrorsToActionErrors( serviceErrors, errors );
    }

    if ( !errors.isEmpty() )
    {
      saveErrors( request, errors );
      return actionMapping.findForward( ActionConstants.FAIL_UPDATE );
    }

    return display( actionMapping, actionForm, request, response );
  }

  public static <T> Predicate<T> distinctByKey( Function<? super T, Object> keyExtractor )
  {
    Map<Object, Boolean> map = new ConcurrentHashMap<>();
    return t -> map.putIfAbsent( keyExtractor.apply( t ), Boolean.TRUE ) == null;
  }

  /**
   * (An User) Accepts the Terms & Conditions on behalf of the Participant
   * 
   * @param actionMapping
   * @param actionForm
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward acceptTermsAndConditions( ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response )
  {
    if ( isCancelled( request ) )
    {
      // EARLY EXIT
      return actionMapping.findForward( ActionConstants.CANCEL_FORWARD );
    }

    ActionMessages errors = new ActionMessages();
    UserForm userForm = (UserForm)actionForm;

    Participant participant = getParticipantService().getParticipantById( new Long( userForm.getUserId() ) );
    UserTNCHistory userTNCHistory = userForm.addPaxTNCHistory( participant );
    // set T&Cs acceptance to ACCEPTED and the date and user who accepted on behalf of the pax
    participant.setTermsAcceptance( ParticipantTermsAcceptance.lookup( ParticipantTermsAcceptance.ACCEPTED ) );
    participant.setUserIDAcceptedTerms( UserManager.getUserId().toString() );
    participant.setTermsAcceptedDate( DateUtils.getCurrentDate() );

    // If this pax has been terminated, do not set status to Active
    ParticipantEmployer paxEmployer = getParticipantService().getCurrentParticipantEmployer( new Long( userForm.getUserId() ) );
    if ( paxEmployer == null || paxEmployer.getTerminationDate() == null || paxEmployer.getTerminationDate().getTime() >= new Date().getTime() )
    {
      // also set pax status to ACTIVE
      participant.setStatus( ParticipantStatus.lookup( ParticipantStatus.ACTIVE ) );
      participant.setTerminationDate( null );
    }

    try
    {

      getParticipantService().saveParticipantWithTNCHistory( participant, userTNCHistory );
    }
    catch( ServiceErrorException e )
    {
      log.debug( e, e );
      List serviceErrors = e.getServiceErrors();
      ServiceErrorStrutsUtils.convertServiceErrorsToActionErrors( serviceErrors, errors );
    }

    // refresh criteria audience for this pax
    try
    {
      getAudienceService().rematchParticipantForAllCriteriaAudiences( new Long( userForm.getUserId() ) );
    }
    catch( ServiceErrorException e )
    {
      log.debug( "Error refreshing Criteria Audience for user:" + userForm.getUserId() + "]" + e );
    }

    if ( !errors.isEmpty() )
    {
      saveErrors( request, errors );
      return actionMapping.findForward( ActionConstants.FAIL_UPDATE );
    }

    return display( actionMapping, actionForm, request, response );
  }

  public ActionForward index( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    UserForm paxForm = (UserForm)form;
    List<Long> participantId = Arrays.asList( new Long( paxForm.getUserId() ) );

    // Elastic search index
    getAutoCompleteService().indexParticipants( participantId );

    // Honeycomb account sync
    getHCServices().syncParticipantDetails( participantId );

    return display( mapping, form, request, response );
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
    boolean isAccountLocked = false;
    UserForm participantForm = (UserForm)form;
    // BugFix 18201 Starts--
    String userIdFromCommLog = "";
    String isUserIdFromCommLog = "";
    Long userId = 0L;
    String userIdFromMap = "";
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
        userId = (Long)clientStateMap.get( "quickSearchDomainId" );
        if ( userId == null )
        {
          try
          {
            userIdFromMap = (String)clientStateMap.get( "userId" );
            if ( userIdFromMap != null )
            {
              userId = new Long( userIdFromMap );
            }

          }
          catch( ClassCastException c )
          {
            userId = (Long)clientStateMap.get( "userId" );
          }
        }
        isUserIdFromCommLog = (String)clientStateMap.get( "isFromCommlog" );
        if ( isUserIdFromCommLog != null && isUserIdFromCommLog.equalsIgnoreCase( "true" ) )
        {
          userIdFromCommLog = (String)clientStateMap.get( "userIdFromCommLog" );
          participantForm.setUserId( userIdFromCommLog );
        }
        else
        {
          if ( userId > 0L )
          {
            participantForm.setUserId( userId + "" );
          }
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

    List paxPromotions = getPromotionService().getAllLiveAndExpiredByTypeAndUserId( PromotionType.THROWDOWN, participant.getId() );
    if ( !paxPromotions.isEmpty() )
    {
      for ( Iterator iter = paxPromotions.iterator(); iter.hasNext(); )
      {
        PromotionPaxValue promoPax = (PromotionPaxValue)iter.next();
        if ( promoPax.getRoleKey().equals( PromotionServiceImpl.THROWDOWN_SECONDARY ) )
        {
          participantForm.setShowThrowdownPlayerStats( true );
          break;
        }
      }
    }

    // Enable Opt Out Awards - Start
    PropertySetItem allowOptOutAwards = getSystemVariableService().getPropertyByName( SystemVariableService.ENABLE_OPT_OUT_AWARDS );
    if ( allowOptOutAwards.getBooleanVal() )
    {
      request.setAttribute( "allowOptOutAwards", new Boolean( true ) );
    }
    else
    {
      request.setAttribute( "allowOptOutAwards", new Boolean( false ) );
    }
    // Enable Opt Out Awards - End

    // Enable Opt Out Program - Start
    PropertySetItem allowOptOutOfProgram = getSystemVariableService().getPropertyByName( SystemVariableService.ENABLE_OPT_OUT_PROGRAM );
    if ( allowOptOutOfProgram.getBooleanVal() )
    {
      request.setAttribute( "allowOptOutOfProgram", new Boolean( true ) );
    }
    else
    {
      request.setAttribute( "allowOptOutOfProgram", new Boolean( false ) );
    }
    // Enable Opt Out Program - End

    // Sales Maker
    PropertySetItem isSalesMaker = getSystemVariableService().getPropertyByName( SystemVariableService.SALES_MAKER );
    if ( isSalesMaker.getBooleanVal() )
    {
      request.setAttribute( "allowMgrDiscAward", new Boolean( false ) );
    }
    else
    {
      request.setAttribute( "allowMgrDiscAward", new Boolean( true ) );
    }

    // get system variables on how T&Cs are used and set them on the Form
    participantForm.setTermsConditionsRequired();
    participantForm.setUserAllowedToAcceptForPax();

    LockoutInfo lockoutInfo = getAuthenticationService().getUserLockOutInfo( participant );

    if ( lockoutInfo.isLockedForBoth() )
    {
      request.setAttribute( "accountLockedMessage", buildCMSMessage( CM_KEY_PREFIX + "ACCOUNT_LOCK_BOTH" ) );
      isAccountLocked = true;
    }
    else if ( lockoutInfo.isHardLocked() )
    {
      request.setAttribute( "accountLockedMessage", buildCMSMessage( CM_KEY_PREFIX + "ACCOUNT_CURRENTLY_LOCKED" ) );
      isAccountLocked = true;
    }
    else if ( lockoutInfo.isSoftLocked() )
    {
      request.setAttribute( "accountLockedMessage", buildCMSMessage( CM_KEY_PREFIX + "ACCOUNT_LOCK" ) + buildTimeRemaining( participant ) );
      isAccountLocked = true;
    }

    if ( request.getAttribute( "accountLockedMessage" ) != null )
    {
      request.setAttribute( "accountLockedInfo", lockoutInfo );
    }

    request.setAttribute( "isAccountLocked", !isAccountLocked );

    // get the actionForward to display the create pages.
    return mapping.findForward( ActionConstants.DETAILS_FORWARD );
  }

  private String buildTimeRemaining( User user )
  {
    Instant now = Instant.now();
    Duration duration = Duration.between( now, user.getLockTimeoutExpireDate().toInstant() );
    long millis = duration.toMillis();
    long minutes = ( millis / 1000 ) / 60;
    int seconds = (int) ( ( millis / 1000 ) % 60 );
    String strSeconds = String.valueOf( seconds );
    if ( seconds < 10 )
    {
      strSeconds = "0" + strSeconds;
    }
    return minutes + ":" + strSeconds;
  }

  /**
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   * @throws IOException
   */
  public ActionForward prepareNodeLookup( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws IOException
  {
    UserForm participantForm = (UserForm)form;

    // Put the form in the session to be reloaded when coming back from node lookup.
    request.getSession().setAttribute( SESSION_PAX_FORM, participantForm );

    response.sendRedirect( RequestUtils.getBaseURI( request ) + "/hierarchy/nodeLookup.do?" + "method=displaySearchWithinPrimary&" + NodeSearchAction.RETURN_ACTION_URL_PARAM
        + "=/participant/participantDisplay.do?method=returnNodeLookup" );

    return mapping.findForward( ActionConstants.SUCCESS_FORWARD );
  }

  /**
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward returnNodeLookup( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    UserForm participantForm = (UserForm)form;

    // Get the form back out of the Session to redisplay.
    UserForm sessionUserForm = (UserForm)request.getSession().getAttribute( SESSION_PAX_FORM );

    if ( sessionUserForm != null )
    {
      try
      {
        BeanUtils.copyProperties( participantForm, sessionUserForm );
      }
      catch( Exception e )
      {
        LOG.info( "Copy Properties failed." );
      }
    }

    // default T&Cs
    participantForm.defaultTermsAndConditions();

    try
    {
      String clientState = RequestUtils.getRequiredParamString( request, "clientState" );
      String cryptoPass = RequestUtils.getOptionalParamString( request, "cryptoPass" );
      String password = ClientStatePasswordManager.getPassword();

      if ( cryptoPass != null && cryptoPass.equals( "1" ) )
      {
        password = ClientStatePasswordManager.getGlobalPassword();
      }
      // Deserialize the client state.
      Map clientStateMap = ClientStateSerializer.deserialize( clientState, password );
      String nodeId = null;
      String nodeName = null;
      try
      {
        nodeId = (String)clientStateMap.get( "nodeId" );
      }
      catch( ClassCastException cce )
      {
        nodeId = ( (Long)clientStateMap.get( "nodeId" ) ).toString();
      }
      nodeName = (String)clientStateMap.get( "nodeName" );
      if ( nodeId != null )
      {
        participantForm.setNodeId( nodeId );
        participantForm.setNameOfNode( nodeName );
      }
    }
    catch( InvalidClientStateException e )
    {
      // do nothing as this is an optional parameter
    }

    // clean up the session
    request.getSession().removeAttribute( SESSION_PAX_FORM );

    return mapping.findForward( ActionConstants.CREATE_FORWARD );
  }

  /**
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward generatePasswordCreate( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    UserForm participantForm = (UserForm)form;

    setGeneratedPassword( participantForm );

    // default T&Cs
    participantForm.defaultTermsAndConditions();

    // get the actionForward to display the create pages.
    return mapping.findForward( ActionConstants.CREATE_FORWARD );
  }

  /**
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward generatePasswordUpdate( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    UserForm participantForm = (UserForm)form;

    setGeneratedPassword( participantForm );

    // get the actionForward to display the create pages.
    return mapping.findForward( ActionConstants.UPDATE_FORWARD );
  }

  /**
   * Get the generated password and set it on the form
   * 
   * @param participantForm
   */
  public void setGeneratedPassword( UserForm participantForm )
  {
    String password = getUserService().generatePassword();

    participantForm.setPassword2( password );
    participantForm.setConfirmPassword2( password );
    participantForm.setPasswordSystemGenerated( new Boolean( true ) );
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
      if ( userPhone.getPhoneType() != null && userPhone.getPhoneType().getCode().equals( PhoneType.FAX ) )
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
    if ( !UserManager.getUser().isParticipant() )
    {
      roles.add( AuthorizationService.ROLE_CODE_PAX );
    }
    if ( getAuthorizationService().isUserInRole( null, roles, null ) )
    {
      // get the actionForward to display the update page.
      return mapping.findForward( ActionConstants.UPDATE_FORWARD );
    }
    // get the actionForward to display the view-only page.
    return mapping.findForward( ActionConstants.REVIEW_FORWARD );
  }

  /**
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward startTwitterAuthorization( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    ActionMessages errors = new ActionMessages();
    try
    {
      Long userId = UserManager.getUserId();
      String url = getTwitterService().startTwitterAuthorizationFor( userId, getTwitterAuthorizationCallbackUrl( request ) );
      response.sendRedirect( url );
    }
    catch( Throwable t )
    {
      errors.add( ActionConstants.ERROR_MESSAGE_PROPERTY, new ActionMessage( "Twitter Catch Exception" ) );
      saveErrors( request, errors );
      ActionForward actionForward = mapping.findForward( ActionConstants.SUCCESS_FORWARD );
      return actionForward;
    }
    return null;
  }

  /*
   * protected String getTwitterAuthorizationCallbackUrl( HttpServletRequest request ) { Long userId
   * = UserManager.getUserId(); Map parameterMap = new HashMap(); parameterMap.put( "uid", userId );
   * String link = ClientStateUtils .generateEncodedLink( "",
   * "/participant/twitterAuthorizationCallback.do?uid=", parameterMap ); String url =
   * RequestUtil.getWebappRootUrl(request) + link ; return url; }
   */

  protected String getTwitterAuthorizationCallbackUrl( HttpServletRequest request )
  {
    Long userId = UserManager.getUserId();
    Map paramMap = new HashMap();
    paramMap.put( "userId", userId );

    String clientStatePassword = ClientStatePasswordManager.getGlobalPassword();
    String clientState = ClientStateSerializer.serialize( paramMap, clientStatePassword );

    try
    {
      clientState = URLEncoder.encode( clientState, "UTF-8" );
    }
    catch( UnsupportedEncodingException e )
    {
      LOG.error( e.getMessage(), e );
    }

    String url = RequestUtil.getWebappRootUrl( request ) + "/participant/twitterAuthorizationCallback.do?method=twitterAuthorizationCallback" + "&uid=" + clientState;

    return url;

  }

  public ActionForward twitterAuthorizationCallback( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws IOException
  {
    UserForm participantForm = (UserForm)form;
    Long userId = null;
    String pin = null;
    String uid = null;
    if ( participantForm.getOauth_verifier() != null )
    {
      pin = participantForm.getOauth_verifier();
    }

    try
    {
      if ( participantForm.getUid() != null )
      {
        uid = participantForm.getUid();

        uid = uid.replace( " ", "+" );
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
            userId = (Long)clientStateMap.get( "userId" );
          }
          catch( InvalidClientStateException e )
          {
            // Never Enter This Block
          }

        }
      }
      else
      {
        userId = UserManager.getUserId();
      }
      getTwitterService().completeTwitterAuthorizationFor( userId, pin );
    }
    catch( Throwable t )
    {
      try
      {
        if ( userId != null )
        {
          getTwitterService().deleteTwitterAuthorizationFor( userId );
        }
      }
      catch( Throwable th )
      {
        // give up. do nothing
      }
    }
    String url = RequestUtil.getWebappRootUrl( request ) + "/homePage.do";
    response.sendRedirect( url );
    return null;
  }

  public ActionForward resetTwitter( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {

    Long userId = UserManager.getUserId();
    getTwitterService().deleteTwitterAuthorizationFor( userId );
    return mapping.findForward( ActionConstants.SUCCESS_FORWARD );

  }

  public ActionForward enableFacebook( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    UserForm userForm = (UserForm)form;
    if ( userForm.getUserId() == null )
    {
      Long userId = UserManager.getUserId();
    }

    Participant participant = getParticipantService().getParticipantById( new Long( userForm.getUserId() ) );

    if ( participant.getUserFacebook() != null && participant.getUserFacebook().getUserId() != null && participant.getUserFacebook().getAccessToken() != null )
    {
      getParticipantService().updateFacebookInfo( UserManager.getUserId(), participant.getUserFacebook() );
    }

    return mapping.findForward( ActionConstants.SUCCESS_FORWARD );
  }

  public ActionForward disableFacebook( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    getParticipantService().updateFacebookInfo( UserManager.getUserId(), null );

    return mapping.findForward( ActionConstants.SUCCESS_FORWARD );
  }

  /**
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return ActionForward
     */
  public ActionForward updatePreferences( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    final String METHOD_NAME = "updatePreferences";
    ActionForward forward = null;
    UserForm participantForm = (UserForm)form;

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
        clientStateParameterMap.put( "userId", new Long( participantForm.getId() ) );
        String queryString = ClientStateUtils.generateEncodedLink( "", "", clientStateParameterMap );
        String method = "method=display";
        // bugFix 17868
        return ActionUtils.forwardWithParameters( mapping, ActionConstants.SUCCESS_FORWARD, new String[] { queryString, method } );
      }
    }

    ActionMessages errors = new ActionMessages();
    if ( !isTokenValid( request, true ) )
    {
      errors.add( ActionConstants.ERROR_TOKEN_FAILURE, new ActionMessage( ActionConstants.ERROR_TOKEN_EXCEPTION ) );
      saveErrors( request, errors );
      return mapping.findForward( ActionConstants.FAIL_UPDATE ); // EARLY EXIT
    }

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
      Long userId = UserManager.getUserId();
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

    try
    {
   // Client customizations for wip #26532 starts
    getParticipantService().updateParticipantPreferences( new Long( participantForm.getId() ),
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
      getAudienceService().rematchParticipantForAllCriteriaAudiences( new Long( participantForm.getId() ) );
    }
    catch( ServiceErrorException e )
    {
      log.debug( e );
      List serviceErrors = e.getServiceErrors();
      ServiceErrorStrutsUtils.convertServiceErrorsToActionErrors( serviceErrors, errors );
      saveErrors( request, errors );
      mapping.findForward( ActionConstants.FAIL_UPDATE );
    }

    // Bug #24270 Start
    UserAddress userAddress = getUserService().getPrimaryUserAddress( new Long( participantForm.getId() ) );
    // get Allow_SMS flag from table Country. START
    boolean isUserCountryAllowedForTextMessages = false;
    if ( userAddress.getAddress() != null && userAddress.getAddress().getCountry() != null )
    {
      isUserCountryAllowedForTextMessages = userAddress.getAddress().getCountry().getAllowSms();
    }
    // END
    request.setAttribute( "isUserCountryAllowedForTextMessages", new Boolean( isUserCountryAllowedForTextMessages ) );
    if ( isUserCountryAllowedForTextMessages )
    {
      try
      {
        if ( participantForm.getTextPhoneNbr() != null && !participantForm.getTextPhoneNbr().trim().equals( "" ) )
        {
          boolean checkPhoneNumberFormat = checkPhoneNumberFormat( participantForm.getTextPhoneNbr() );
          if ( !checkPhoneNumberFormat )
          {
            errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "participant.errors.MOBILE_PHONE_VALIDATION" ) );
            saveErrors( request, errors );
            return mapping.findForward( ActionConstants.FAIL_UPDATE );
          }
          if ( participantForm.getAcceptTermsOnTextMessages() == null || participantForm.getAcceptTermsOnTextMessages().trim().equals( "" ) )
          {
            // force the User to select Terms check box...
            errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "participant.errors.TERMS_CONDITIONS_REQUIRED_TEXT_MESSAGES" ) );
            saveErrors( request, errors );
            return mapping.findForward( ActionConstants.FAIL_UPDATE );
          }
          // User Mobile Phone
          UserPhone userPhone = getUserService().getUserPhone( new Long( participantForm.getId() ), PhoneType.MOBILE );
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
            getUserService().addUserPhone( new Long( participantForm.getId() ), userPhone );
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
            getUserService().updateUserPhone( new Long( participantForm.getId() ), userPhone );
          }

          if ( sendConfirmationMessage )
          {
            String message = CmsResourceBundle.getCmsBundle().getString( "participant.preference.edit", "TEXT_MESSAGE_UPDATED" );
            String companyName = getSystemVariableService().getPropertyByName( SystemVariableService.CLIENT_NAME ).getStringVal();
            String formatedMesg = message.replace( "${companyName}", companyName );
            getMailingService().sendSmsMessage( new Long( participantForm.getId() ), participantForm.getCountryPhoneCode(), participantForm.getTextPhoneNbr(), formatedMesg );
          }

          // Save Termas and condition flag
          if ( participantForm.getAcceptTermsOnTextMessages().equals( "yes" ) )
          {
            Participant participant = getParticipantService().getParticipantById( new Long( participantForm.getId() ) );
            participant.setAcceptTermsOnTextMessages( true );
            getParticipantService().saveParticipant( participant );
          }
        }
        else
        {
          // uncheck terms and conditions -- if it already set
          // Remove Phone Number -- if User had already
          Participant participant = getParticipantService().getParticipantById( new Long( participantForm.getId() ) );
          UserPhone userPhone = getUserService().getUserPhone( new Long( participantForm.getId() ), PhoneType.MOBILE );
          if ( participant.isAcceptTermsOnTextMessages() )
          {
            participant.setAcceptTermsOnTextMessages( false );
            getParticipantService().saveParticipant( participant );
          }
          if ( userPhone != null )
          {
            getUserService().deleteUserPhone( new Long( participantForm.getId() ), PhoneType.MOBILE );
          }

        }
      }
      catch( ServiceErrorException se )
      {
      }
    }
    // Bug #24270 End
    // BugFix 17765 Starts
    // immediately update the language for this session
    if ( !StringUtils.isEmpty( participantForm.getLanguage() ) && participantForm.isViewCurrentUser() )
    {
      request.setAttribute( "userLanguage", LanguageType.lookup( participantForm.getLanguage() ) );
      request.getSession().setAttribute( "cmsLocaleCode", participantForm.getLanguage() );

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

    }

    // If viewing the preference of the current user, then go the home page.
    // Otherwise, go to the overview page
    if ( participantForm.isViewCurrentUser() )
    {
      forward = mapping.findForward( ActionConstants.SUCCESS_TO_HOMEPAGE );
    }
    else
    {
      Map clientStateParameterMap = new HashMap();
      clientStateParameterMap.put( "userId", new Long( participantForm.getId() ) );
      String queryString = ClientStateUtils.generateEncodedLink( "", "", clientStateParameterMap );
      String method = "method=display";
      forward = ActionUtils.forwardWithParameters( mapping, ActionConstants.SUCCESS_FORWARD, new String[] { queryString, method } );
    }

    if ( !errors.isEmpty() )
    {
      forward = mapping.findForward( ActionConstants.FAIL_CREATE );
    }

    return forward;
  }

  private boolean checkPhoneNumberFormat( String textPhoneNbr )
  {
    textPhoneNbr = textPhoneNbr.trim();
    // check for Length
    if ( ! ( textPhoneNbr.length() == 12 || textPhoneNbr.length() == 10 ) )
    {
      return false;
    }
    else
    {
      // if length is 10. ex:0123456789.
      if ( textPhoneNbr.length() == 10 )
      {
        return textPhoneNbr.matches( "\\d{10}" );
      }
      else
      // If Length is 12.
      {
        // check for 4th and 7th palces
        if ( ! ( textPhoneNbr.charAt( 3 ) == '-' ) || ! ( textPhoneNbr.charAt( 7 ) == '-' ) )
        {
          return false;
        }
        return getOnlyNumerics( textPhoneNbr );
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

  // private AutoCompleteService getAutoCompleteService()
  // {
  // return (AutoCompleteService)getService( AutoCompleteService.BEAN_NAME );
  // }

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

  protected ChatterService getChatterService()
  {
    return (ChatterService)BeanLocator.getBean( ChatterService.BEAN_NAME );
  }

  protected WelcomeEmailService getWelcomeEmailService()
  {
    return (WelcomeEmailService)getService( WelcomeEmailService.BEAN_NAME );
  }

  public ActionForward resendWelcomeEmailView( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    UserForm participantForm = (UserForm)form;

    User user = getUserService().getUserById( new Long( participantForm.getUserId() ) );
    request.setAttribute( "user", user );

    return mapping.findForward( "resend_email" );
  }

  public ActionForward resendWelcomeEmail( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    UserForm participantForm = (UserForm)form;

    try
    {
      getWelcomeEmailService().resendWelcomeEmail( new Long( participantForm.getUserId() ) );
    }
    catch( Exception e )
    {
      log.error( "An exception occurred while sending Welcome Email to UserId: " + participantForm.getUserId() + ".", e );
    }

    return mapping.findForward( "pax_overview" );
  }

  /**
   * This method is used to get the authorization code from chatter. Chatter response is a full html page for the user to login.
   * If user is already logged in and session is not expired then this will skip the log in.
   * Once the user successfully logs in chatter will call this method and return access token and other parameters including state parameter.
   * state - Anything passed in the state parameter to chatter will be returned back in the response so that we can use this for post process.
   * @param mapping
   * @param form
   * @param httpServletRequest
   * @param httpServletResponse
   * @return
   * @throws IOException
   */
  public ActionForward getChatterAuthorizationCode( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws IOException
  {
    ActionMessages errors = new ActionMessages();
    String message = request.getParameter( STATE );

    // If there is a valid access token in session then skip the login and redirect the user to
    // preview page
    if ( isChatterTokenInSession( request ) && isChatterTokenNotExpired( request ) )
    {
      String accessToken = (String)request.getSession().getAttribute( CHATTER_ACCESS_TOKEN );
      String instanceUrl = (String)request.getSession().getAttribute( CHATTER_INSTANCE_URL );

      String siteUrl = getSystemVariableService().getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();
      String callbackUrl = getSystemVariableService().getPropertyByName( SystemVariableService.CHATTER_CALLBACK_URL ).getStringVal();
      response.sendRedirect( siteUrl + callbackUrl + "&" + CHATTER_ACCESS_TOKEN + "=" + accessToken + "&" + STATE + "=" + message + "&" + CHATTER_INSTANCE_URL + "=" + instanceUrl );
      return null;
    }

    // Load chatter login page when there is no token or token is expired in session
    try
    {
      String chatterLoginFullPage = getChatterService().getChatterAuthorizationCode( message );
      response.getWriter().write( chatterLoginFullPage );
      response.getWriter().flush();
      response.getWriter().close();
      return null;
    }
    catch( ServiceErrorException e )
    {
      List serviceErrors = e.getServiceErrors();
      ServiceErrorStrutsUtils.convertServiceErrorsToActionErrors( serviceErrors, errors );
      saveErrors( request, errors );
      return mapping.findForward( ActionConstants.FAIL_FORWARD );
    }
  }

  /**
   * This will be invoked by chatter. The response has the access_token and other parameters.
   * State parameter holds the message that needs to be posted on the profile page.
   * @param mapping
   * @param form
   * @param httpServletRequest
   * @param httpServletResponse
   * @return
   * @throws IOException
   */
  public ActionForward chatterAuthorizationCallback( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws IOException
  {
    String message = request.getParameter( STATE );
    String accessToken = request.getParameter( CHATTER_ACCESS_TOKEN );
    String instanceUrl = request.getParameter( CHATTER_INSTANCE_URL );
    String code = request.getParameter( CHATTER_AUTH_CODE );

    ActionMessages errors = new ActionMessages();
    try
    {
      if ( !isChatterTokenInSession( request ) || !isChatterTokenNotExpired( request ) )
      {
        if ( StringUtils.isEmpty( code ) )
        {
          // This might happen when session expired between calls. Then clear the session and
          // display
          // chatter login page and fetch new chatter authorization code
          request.getSession().removeAttribute( CHATTER_ACCESS_TOKEN );
          request.getSession().removeAttribute( CHATTER_ISSUED_AT );
          request.getSession().removeAttribute( CHATTER_INSTANCE_URL );
          String siteUrl = getSystemVariableService().getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();
          response.sendRedirect( siteUrl + "/participant/chatterAuthorizationSubmit.do?method=getChatterAuthorizationCode&state=" + message );
          return null;
        }
        else
        {
          // If there is no valid access token in the session then get a new one
          JSONObject jsonObject = getChatterService().getChatterAccessToken( code, message );
          accessToken = JsonUtils.getStringValueFrom( jsonObject, "access_token" );
          instanceUrl = JsonUtils.getStringValueFrom( jsonObject, "instance_url" );
          String issuedAt = JsonUtils.getStringValueFrom( jsonObject, "issued_at" );
          // Store the token in user session
          request.getSession().setAttribute( CHATTER_ACCESS_TOKEN, accessToken );
          request.getSession().setAttribute( CHATTER_ISSUED_AT, issuedAt );
          request.getSession().setAttribute( CHATTER_INSTANCE_URL, instanceUrl );
        }
      }
    }
    catch( ServiceErrorException e )
    {
      List serviceErrors = e.getServiceErrors();
      ServiceErrorStrutsUtils.convertServiceErrorsToActionErrors( serviceErrors, errors );
      saveErrors( request, errors );
      return mapping.findForward( ActionConstants.FAIL_FORWARD );
    }

    // Http client call to post message to the user profile
    try
    {
      getChatterService().postMessageToWall( message, accessToken, instanceUrl );
      request.setAttribute( "chatterInstanceUrl", instanceUrl );
    }
    catch( ServiceErrorException e )
    {
      List serviceErrors = e.getServiceErrors();
      ServiceErrorStrutsUtils.convertServiceErrorsToActionErrors( serviceErrors, errors );
      saveErrors( request, errors );
      return mapping.findForward( ActionConstants.FAIL_FORWARD );
    }
    return mapping.findForward( ActionConstants.SUCCESS_FORWARD );
  }

  /** 
   * Currently the session is set for 2 hours in Chatter settings in the workbench
   * If issued time is less than 2 hour ago use the same access token
   * otherwise get a fresh authorization code. This check is to skip the login and authorization
   * code http client calls if the user is already logged in.
   */
  protected boolean isChatterTokenNotExpired( HttpServletRequest request )
  {
    boolean chatterTokenNotExpired = false;
    String issuedAt = (String)request.getSession().getAttribute( CHATTER_ISSUED_AT );
    long currentTime = DateUtils.getCurrentDateAsLong();
    long issuedAtPlus2hrs = Long.parseLong( issuedAt ) + 3600 * 1000 * 2;
    if ( issuedAtPlus2hrs > currentTime )
    {
      chatterTokenNotExpired = true;
    }
    return chatterTokenNotExpired;
  }

  /**
   * Check if there is a chatter token saved in user session
   * @param request
   * @return
   */
  private boolean isChatterTokenInSession( HttpServletRequest request )
  {
    HttpSession session = request.getSession();
    if ( session.getAttribute( CHATTER_ACCESS_TOKEN ) != null && session.getAttribute( CHATTER_ISSUED_AT ) != null && session.getAttribute( CHATTER_INSTANCE_URL ) != null )
    {
      return true;
    }
    return false;
  }

  public String buildCMSMessage( String key )
  {
    return CmsResourceBundle.getCmsBundle().getString( key );
  }

  /**
   * Gets the Promotion Service
   * 
   * @return Promotion Service
   */
  private PromotionService getPromotionService()
  {
    return (PromotionService)getService( PromotionService.BEAN_NAME );
  }

  private AutoCompleteService getAutoCompleteService()
  {
    return (AutoCompleteService)getService( AutoCompleteService.BEAN_NAME );
  }

  private HCServices getHCServices()
  {
    return (HCServices)getService( HCServices.BEAN_NAME );
  }

  private SSIContestService getSSIContestService()
  {
    return (SSIContestService)getService( SSIContestService.BEAN_NAME );
  }

  protected String getSysUrl()
  {
    return getSystemVariableService().getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();
  }

  private static PurlService getPurlService()
  {
    return (PurlService)getService( PurlService.BEAN_NAME );
  }
  
//-------------------------------
  // customization WIP 20160 start
  //-------------------------------
  private TccAdminLoginInfoService getTccAdminLoginInfoService()
  {
    return (TccAdminLoginInfoService)getService( TccAdminLoginInfoService.BEAN_NAME );
  }
  //-------------------------------
  // customization WIP 20160 end
  //-------------------------------

}

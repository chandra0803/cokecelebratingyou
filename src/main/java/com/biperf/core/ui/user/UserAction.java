/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/user/UserAction.java,v $
 */

package com.biperf.core.ui.user;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.biperf.core.domain.enums.HierarchyRoleType;
import com.biperf.core.domain.enums.LanguageType;
import com.biperf.core.domain.enums.PhoneType;
import com.biperf.core.domain.enums.UserType;
import com.biperf.core.domain.hierarchy.Hierarchy;
import com.biperf.core.domain.hierarchy.Node;
import com.biperf.core.domain.mailing.Mailing;
import com.biperf.core.domain.user.Role;
import com.biperf.core.domain.user.User;
import com.biperf.core.domain.user.UserEmailAddress;
import com.biperf.core.domain.user.UserNode;
import com.biperf.core.domain.user.UserPhone;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.exception.UniqueConstraintViolationException;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.email.MailingService;
import com.biperf.core.service.email.WelcomeEmailService;
import com.biperf.core.service.hierarchy.HierarchyService;
import com.biperf.core.service.hierarchy.NodeService;
import com.biperf.core.service.message.MessageService;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.participant.PasswordResetService;
import com.biperf.core.service.participant.UserAssociationRequest;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.service.security.AuthenticationService;
import com.biperf.core.service.security.RoleService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.strategy.usertoken.UserTokenType;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.ui.node.NodeSearchAction;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.ui.utils.ServiceErrorStrutsUtils;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;
import com.biperf.core.utils.ClientStateUtils;
import com.biperf.core.utils.InvalidClientStateException;
import com.biperf.core.utils.UserManager;

/**
 * UserAction.
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
 * <td>Apr 19, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class UserAction extends BaseDispatchAction
{
  /** Log */
  private static final Log LOG = LogFactory.getLog( UserAction.class );

  private static final String SESSION_USER_FORM = "sessionUserForm";

  /**
   * @param actionMapping
   * @param actionForm
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward unspecified( ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response )
  {
    return display( actionMapping, actionForm, request, response );
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
    UserForm userForm = (UserForm)form;
    boolean isAccountLocked = false;
    Long userId = new Long( userForm.getUserId() );

    AssociationRequestCollection userAssociationRequestCollection = new AssociationRequestCollection();
    userAssociationRequestCollection.add( new UserAssociationRequest( UserAssociationRequest.ALL ) );

    User user = getUserService().getUserByIdWithAssociations( userId, userAssociationRequestCollection );

    userForm.loadUser( user );

    request.setAttribute( "accountLocked", Boolean.valueOf( getAuthenticationService().isUserLockedOut( user ) ) );
    LockoutInfo lockoutInfo = getAuthenticationService().getUserLockOutInfo( user );

    if ( lockoutInfo.isLockedForBoth() || lockoutInfo.isHardLocked() || lockoutInfo.isSoftLocked() )
    {
      isAccountLocked = true;
    }

    request.setAttribute( "isAccountLocked", !isAccountLocked );
    setAllowSecurityUpdateFlag( userForm, request );

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
  public ActionForward prepareCreate( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    UserForm userForm = (UserForm)form;

    userForm.setMethod( "create" );

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
  public ActionForward prepareCreateInternal( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    UserForm userForm = (UserForm)form;

    userForm.setMethod( "createInternal" );

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
  public ActionForward prepareUpdateLoginInfo( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    UserForm userForm = (UserForm)form;

    User user = getUserService().getUserById( new Long( userForm.getUserId() ) );

    userForm.loadLoginInfo( user );
    userForm.setMethod( "updateLoginInfo" );
    // Security Patch 3 - start
    // settingAttributes( request );
    // Security Patch 3 - start

    setAllowSecurityUpdateFlag( userForm, request );

    // get the actionForward to display the create pages.
    return mapping.findForward( ActionConstants.EDIT_FORWARD );
  }

  /**
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward prepareUpdatePersonalInfo( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    UserForm userForm = (UserForm)form;

    User user = getUserService().getUserById( new Long( userForm.getUserId() ) );

    userForm.loadPersonalInfo( user );
    userForm.setMethod( "updatePersonalInfo" );

    request.setAttribute( "user", user );// for name display
    // get the actionForward to display the create pages.
    return mapping.findForward( ActionConstants.UPDATE_FORWARD );
  }

  /**
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   * @throws Exception
   */
  public ActionForward updatePersonalInfo( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    final String METHOD_NAME = "updatePersonalInfo";
    String forwardTo = ActionConstants.SUCCESS_FORWARD;
    UserForm userForm = (UserForm)form;
    // TODO Don't re-get this. Store in session or serialize
    User user = getUserService().getUserById( new Long( userForm.getId() ) );

    if ( !user.isParticipant() )
    {
      if ( user.getUserType().getCode().equals( UserType.BI ) )
      {
        forwardTo = "success_bi_user";
      }
      else
      {
        forwardTo = "success_client_user";
      }
    }
    if ( isCancelled( request ) )
    {
      // EARLY EXIT
      LOG.info( ">>> " + METHOD_NAME + " cancelled." );
      forwardTo = ActionConstants.CANCEL_FORWARD;
      if ( !user.isParticipant() )
      {
        if ( user.getUserType().getCode().equals( UserType.BI ) )
        {
          forwardTo = "cancel_bi_user";
        }
        else
        {
          forwardTo = "cancel_client_user";
        }
      }
    }
    else
    {
      ActionMessages errors = new ActionMessages();

      if ( !isTokenValid( request, true ) )
      {
        errors.add( "tokenFailure", new ActionMessage( ActionConstants.ERROR_TOKEN_EXCEPTION ) );
        saveErrors( request, errors );
        forwardTo = ActionConstants.FAIL_UPDATE;
      }

      userForm.setPersonalInfo( user );

      try
      {
        getUserService().saveUser( user );
      }
      catch( UniqueConstraintViolationException e )
      {
        errors.add( ActionConstants.ERROR_MESSAGE_PROPERTY, new ActionMessage( "user.error.UNIQUE_CONSTRAINT" ) );
      }
      catch( ServiceErrorException e )
      {
        log.debug( e );
        List serviceErrors = e.getServiceErrors();
        ServiceErrorStrutsUtils.convertServiceErrorsToActionErrors( serviceErrors, errors );
      }

      if ( !errors.isEmpty() )
      {
        saveErrors( request, errors );
        forwardTo = ActionConstants.FAIL_UPDATE;
      }
    }
    ActionForward forward = mapping.findForward( forwardTo );
    Map clientStateParameterMap = new HashMap();
    clientStateParameterMap.put( "userId", user.getId() );
    String returnUrl = ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI( request ), forward.getPath(), clientStateParameterMap );
    response.sendRedirect( returnUrl );
    return null;
  }

  /**
   * @param actionMapping
   * @param actionForm
   * @param request
   * @param response
   * @return ActionForward
   * @throws Exception
   */
  public ActionForward updateLoginInfo( ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    ActionMessages errors = new ActionMessages();
    final String METHOD_NAME = "updateLoginInfo";

    // Security Patch 3 - start
    // settingAttributes( request );
    // Security Patch 3 - end

    UserForm form = (UserForm)actionForm;
    User user = getUserService().getUserById( new Long( form.getUserId() ) );

    String forwardTo = ActionConstants.SUCCESS_FORWARD;
    if ( !user.isParticipant() )
    {
      if ( user.getUserType().getCode().equals( UserType.BI ) )
      {
        forwardTo = "success_bi_user";
      }
      else
      {
        forwardTo = "success_client_user";
      }
    }
    if ( isCancelled( request ) )
    {
      // EARLY EXIT
      LOG.info( ">>> " + METHOD_NAME + " cancelled." );

      forwardTo = ActionConstants.CANCEL_FORWARD;
      if ( !user.isParticipant() )
      {
        if ( user.getUserType().getCode().equals( UserType.BI ) )
        {
          forwardTo = "cancel_bi_user";
        }
        else
        {
          forwardTo = "cancel_client_user";
        }
      }
    }
    else
    {
      if ( !isTokenValid( request, true ) )
      {
        errors.add( "tokenFailure", new ActionMessage( ActionConstants.ERROR_TOKEN_EXCEPTION ) );
        saveErrors( request, errors );
        forwardTo = ActionConstants.FAIL_UPDATE;
      }

      user.setVersion( new Long( form.getVersion() ) );
      user.setUserName( form.getUserName() );
      /*
       * user.setSecretQuestionType( SecretQuestionType.lookup( form.getSecretQuestion() ) );
       * user.setSecretAnswer( form.getSecretAnswer() ); // System generated moved to
       * "reissue password" as of security patch 2 if (
       * !form.getPasswordSystemGenerated().booleanValue() ) { // if password was changed on the
       * form, set the new one String password = form.getPassword(); if ( password != null &&
       * !password.equals( "" ) && !password.equals( "~NO_PASSWORD~" ) ) { user.setPassword(
       * password ); } }
       */
      try
      {
        user = getUserService().saveUser( user );
      }
      catch( UniqueConstraintViolationException e )
      {
        errors.add( ActionConstants.ERROR_MESSAGE_PROPERTY, new ActionMessage( "user.error.UNIQUE_CONSTRAINT" ) );

        LOG.info( " CheckedException - " + e.getMessage() );
        LOG.info( " User - " + user.toString() );
      }
      catch( ServiceErrorException e )
      {
        log.debug( e );
        List serviceErrors = e.getServiceErrors();
        ServiceErrorStrutsUtils.convertServiceErrorsToActionErrors( serviceErrors, errors );
      }

      if ( user == null )
      {
        errors.add( ActionConstants.ERROR_MESSAGE_PROPERTY, new ActionMessage( "user.error.GENERAL" ) );

        LOG.info( " user was null after save.  Going to error page." );
      }

      if ( !errors.isEmpty() )
      {
        saveErrors( request, errors );
        forwardTo = ActionConstants.FAIL_UPDATE;
      }
    }
    if ( errors.isEmpty() )
    {
      ActionForward forward = actionMapping.findForward( forwardTo );
      Map clientStateParameterMap = new HashMap();
      clientStateParameterMap.put( "userId", user.getId() );
      String returnUrl = ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI( request ), forward.getPath(), clientStateParameterMap );
      response.sendRedirect( returnUrl );
      return null;
    }
    else
    {
      return actionMapping.findForward( forwardTo );
    }

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

    UserForm form = (UserForm)actionForm;

    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
    associationRequestCollection.add( new UserAssociationRequest( UserAssociationRequest.EMAIL ) );
    associationRequestCollection.add( new UserAssociationRequest( UserAssociationRequest.PHONE ) );
    User user = getUserService().getUserByIdWithAssociations( new Long( form.getUserId() ), associationRequestCollection );
    // TODO: should this be assumed to be the technique or should we call to auth svc.
    user.setLoginFailuresCount( new Integer( 0 ) );
    user.setAccountLocked( false );
    user.setLockTimeoutExpireDate( null );
    try
    {
      getUserService().saveUser( user );
      UserEmailAddress primaryEmail = user.getPrimaryEmailAddress();
      boolean sharedPrimaryContact = ( null == primaryEmail ) ? false : getUserService().getUserIdsByEmailOrPhone( primaryEmail.getEmailAddr() ).size() > 1;
      Mailing passwordChangeMailing = getMailingService().buildUnLockAccountNotification( user.getId(), sharedPrimaryContact, MessageService.ACCOUNT_UN_LOCKED );
      getMailingService().submitMailing( passwordChangeMailing, null, user.getId() );
      // alert all mobile phones

      Set<UserPhone> userPhoneSet = user.getUserPhones();
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
      request.setAttribute( "isAccountLocked", !user.isAccountLocked() );
      request.setAttribute( "accountJustUnlocked", Boolean.TRUE );
    }
    catch( UniqueConstraintViolationException e )
    {
      errors.add( ActionConstants.ERROR_MESSAGE_PROPERTY, new ActionMessage( "user.error.UNIQUE_CONSTRAINT" ) );

      LOG.info( " CheckedException - " + e.getMessage() );
      LOG.info( " User - " + user.toString() );
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
   * @param actionMapping
   * @param actionForm
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward create( ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response )
  {
    final String METHOD_NAME = "create";

    if ( isCancelled( request ) )
    {
      // EARLY EXIT
      LOG.info( ">>> " + METHOD_NAME + " cancelled." );
      return actionMapping.findForward( ActionConstants.CANCEL_FORWARD );
    }

    ActionMessages errors = new ActionMessages();
    if ( !isTokenValid( request, true ) )
    {
      errors.add( "tokenFailure", new ActionMessage( ActionConstants.ERROR_TOKEN_EXCEPTION ) );
      saveErrors( request, errors );
      return actionMapping.findForward( ActionConstants.FAIL_CREATE ); // EARLY EXIT
    }

    UserForm form = (UserForm)actionForm;

    User user = new User();

    form.setUserInfo( user );

    // UserType is client for this method
    user.setUserType( UserType.lookup( UserType.BI ) );

    // Add the role to the user
    Role newRole = getRoleService().getRoleById( new Long( form.getRole() ) );
    if ( newRole != null )
    {
      user.addRole( newRole );
    }

    String nodeId = form.getNodeId();
    String nodeName = form.getNameOfNode();
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
        // TODO: Create a valid error message for not retrieving a node
        errors.add( ServiceErrorMessageKeys.SYSTEM_ERRORS_SYSTEM_EXCEPTION, new ActionMessage( "node.errors.SEARCH_FAILED" ) );
        saveErrors( request, errors );
        return actionMapping.findForward( ActionConstants.FAIL_CREATE );
      }

      UserNode userNode = new UserNode();
      userNode.setNode( node );
      userNode.setHierarchyRoleType( HierarchyRoleType.lookup( form.getNodeRelationship() ) );
      userNode.setActive( new Boolean( true ) );
      userNode.setIsPrimary( new Boolean( true ) );
      user.addUserNode( userNode );
    }

    // set a default language type
    // ------------------------------------------------
    user.setLanguageType( LanguageType.lookup( getSystemVariableService().getPropertyByName( SystemVariableService.DEFAULT_LANGUAGE ).getStringVal() ) );
    // ------------------------------------------------

    try
    {
      user = getUserService().saveUser( user );
    }
    catch( UniqueConstraintViolationException e )
    {
      errors.add( ActionConstants.ERROR_MESSAGE_PROPERTY, new ActionMessage( "user.error.UNIQUE_CONSTRAINT" ) );

      LOG.info( " CheckedException - " + e.getMessage() );
      LOG.info( " User - " + user.toString() );
    }
    catch( ServiceErrorException e )
    {
      log.debug( e );
      List serviceErrors = e.getServiceErrors();
      ServiceErrorStrutsUtils.convertServiceErrorsToActionErrors( serviceErrors, errors );
    }

    if ( user == null )
    {
      errors.add( ActionConstants.ERROR_MESSAGE_PROPERTY, new ActionMessage( "user.error.GENERAL" ) );

      LOG.info( " user was null after save.  Going to error page." );
    }

    ActionForward actionForward = actionMapping.findForward( ActionConstants.SUCCESS_CREATE );
    if ( !errors.isEmpty() )
    {
      saveErrors( request, errors );
      actionForward = actionMapping.findForward( ActionConstants.FAIL_CREATE );
    }

    return actionForward;
  }

  /**
   * @param actionMapping
   * @param actionForm
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward createInternal( ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response )
  {
    final String METHOD_NAME = "createInternal";

    if ( isCancelled( request ) )
    {
      // EARLY EXIT
      LOG.info( ">>> " + METHOD_NAME + " cancelled." );
      return actionMapping.findForward( ActionConstants.CANCEL_FORWARD );
    }

    ActionMessages errors = new ActionMessages();
    if ( !isTokenValid( request, true ) )
    {
      errors.add( ActionConstants.ERROR_TOKEN_FAILURE, new ActionMessage( ActionConstants.ERROR_TOKEN_EXCEPTION ) );
      saveErrors( request, errors );
      return actionMapping.findForward( ActionConstants.FAIL_CREATE ); // EARLY EXIT
    }

    UserForm form = (UserForm)actionForm;

    ActionForward actionForward = actionMapping.findForward( ActionConstants.SUCCESS_CREATE );

    User user = new User();

    form.setUserInfo( user );

    // UserType is client for this method
    user.setUserType( UserType.lookup( UserType.BI ) );

    // Add the role to the user
    Role newRole = getRoleService().getRoleById( new Long( form.getRole() ) );
    if ( newRole != null )
    {
      user.addRole( newRole );
    }

    // set a default language type
    // ------------------------------------------------
    user.setLanguageType( LanguageType.lookup( getSystemVariableService().getPropertyByName( SystemVariableService.DEFAULT_LANGUAGE ).getStringVal() ) );
    // ------------------------------------------------

    try
    {
      user = getUserService().saveUser( user );
    }
    catch( UniqueConstraintViolationException e )
    {
      errors.add( ActionConstants.ERROR_MESSAGE_PROPERTY, new ActionMessage( "user.error.UNIQUE_CONSTRAINT" ) );

      LOG.info( " CheckedException - " + e.getMessage() );
      LOG.info( " User - " + user.toString() );
    }
    catch( ServiceErrorException e )
    {
      log.debug( e );
      List serviceErrors = e.getServiceErrors();
      ServiceErrorStrutsUtils.convertServiceErrorsToActionErrors( serviceErrors, errors );
    }

    if ( user == null )
    {
      errors.add( ActionConstants.ERROR_MESSAGE_PROPERTY, new ActionMessage( "user.error.GENERAL" ) );

      LOG.info( " user was null after save.  Going to error page." );
    }

    if ( !errors.isEmpty() )
    {
      saveErrors( request, errors );
      actionForward = actionMapping.findForward( ActionConstants.FAIL_CREATE );
    }

    return actionForward;
  }

  /**
   * @param actionMapping
   * @param actionForm
   * @param request
   * @param response
   * @return ActionForward
   */
  @SuppressWarnings( "unchecked" )
  public ActionForward SendNewWelcomeEmail( ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response )
  {
    UserForm form = (UserForm)actionForm;
    try
    {
      AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
      associationRequestCollection.add( new UserAssociationRequest( UserAssociationRequest.EMAIL ) );

      User user = getUserService().getUserByIdWithAssociations( new Long( form.getUserId() ), associationRequestCollection );
      boolean uniquePrimaryEmail = false;

      if ( !Objects.isNull( user.getPrimaryEmailAddress().getEmailAddr() ) )
      {
        uniquePrimaryEmail = getUserService().isUniqueEmail( user.getPrimaryEmailAddress().getEmailAddr() );
      }

      User loggedInUser = getUserService().getUserById( UserManager.getUser().getUserId() );
      if ( !Objects.isNull( user ) )
      {
        getWelcomeEmailService().sendNewWelcomeEmail( user,
                                                      getMessageService().getMessageByCMAssetCode( MessageService.WELCOME_EMAIL_MESSAGE_CM_ASSET_CODE ),
                                                      getPasswordResetService().generateTokenAndSave( user.getId(), UserTokenType.WELCOME_EMAIL ).getUnencryptedTokenValue(),
                                                      loggedInUser,
                                                      uniquePrimaryEmail );

      }
    }
    catch( Exception e )
    {
      if ( log.isDebugEnabled() )
      {
        log.debug( " user was null. Going to error page." );
      }
    }
    ActionForward actionForward = actionMapping.findForward( ActionConstants.SENT_WELCOME_EMAIL );

    return actionForward;
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
    UserForm userForm = (UserForm)form;

    // Put the form in the session to be reloaded when coming back from node lookup.
    request.getSession().setAttribute( SESSION_USER_FORM, userForm );

    response.sendRedirect( RequestUtils.getBaseURI( request ) + "/hierarchy/nodeLookup.do?" + NodeSearchAction.RETURN_ACTION_URL_PARAM + "=/userDisplay.do?method=returnNodeLookup" );

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
    UserForm userForm = (UserForm)form;

    // Get the form back out of the Session to redisplay.
    UserForm sessionUserForm = (UserForm)request.getSession().getAttribute( SESSION_USER_FORM );

    if ( sessionUserForm != null )
    {
      try
      {
        BeanUtils.copyProperties( userForm, sessionUserForm );
      }
      catch( Exception e )
      {
        LOG.info( "Copy Properties failed." );
      }
    }

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
      try
      {
        nodeId = (String)clientStateMap.get( "nodeId" );
      }
      catch( ClassCastException cce )
      {
        nodeId = ( (Long)clientStateMap.get( "nodeId" ) ).toString();
      }
      if ( nodeId != null )
      {
        userForm.setNodeId( nodeId );
      }
      String nodeName = (String)clientStateMap.get( "nodeName" );
      if ( nodeName != null )
      {
        userForm.setNameOfNode( nodeName );
      }
    }
    catch( InvalidClientStateException e )
    {
      // do nothing as this an optional parameters
    }
    // clean up the session
    request.getSession().removeAttribute( SESSION_USER_FORM );

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
    // setGeneratedPassword( form );

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
    // setGeneratedPassword( form );

    UserForm userForm = (UserForm)form;

    userForm.setMethod( "updateLoginInfo" );

    // get the actionForward to display the create pages.
    return mapping.findForward( ActionConstants.EDIT_FORWARD );
  }

  /**
   * Get the generated password and set it on the form
   * 
   * @param form
   */
  /*
   * public void setGeneratedPassword( ActionForm form ) { UserForm userForm = (UserForm)form;
   * String password = getUserService().generatePassword(); userForm.setPassword2( password );
   * userForm.setConfirmPassword2( password ); userForm.setPasswordSystemGenerated( new Boolean(
   * true ) ); }
   */
  /**
   * Flag if the user can update password / security qa. (True if updating your own information.)
   */
  private void setAllowSecurityUpdateFlag( UserForm userForm, HttpServletRequest request )
  {
    boolean allowSecurityUpdate = false;
    Long currentUserId = UserManager.getOriginalUserId();
    if ( userForm.getUserId() != null && !"".equals( userForm.getUserId() ) )
    {
      Long userIdValue = new Long( userForm.getUserId() );
      allowSecurityUpdate = userIdValue.equals( currentUserId );
    }

    request.setAttribute( "allowSecurityUpdate", allowSecurityUpdate );
  }

  private UserService getUserService()
  {
    return (UserService)getService( UserService.BEAN_NAME );
  }

  private RoleService getRoleService()
  {
    return (RoleService)getService( RoleService.BEAN_NAME );
  }

  private AuthenticationService getAuthenticationService()
  {
    return (AuthenticationService)getService( AuthenticationService.BEAN_NAME );
  }

  private SystemVariableService getSystemVariableService()
  {
    return (SystemVariableService)BeanLocator.getBean( SystemVariableService.BEAN_NAME );
  }

  private HierarchyService getHierarchyService()
  {
    return (HierarchyService)getService( HierarchyService.BEAN_NAME );
  }

  private NodeService getNodeService()
  {
    return (NodeService)getService( NodeService.BEAN_NAME );
  }

  private MailingService getMailingService()
  {
    return (MailingService)getService( MailingService.BEAN_NAME );
  }

  private WelcomeEmailService getWelcomeEmailService()
  {
    return (WelcomeEmailService)getService( WelcomeEmailService.BEAN_NAME );
  }

  private MessageService getMessageService()
  {
    return (MessageService)getService( MessageService.BEAN_NAME );
  }

  private PasswordResetService getPasswordResetService()
  {
    return (PasswordResetService)getService( PasswordResetService.BEAN_NAME );
  }

  private ParticipantService getparticipantService()
  {
    return (ParticipantService)getService( ParticipantService.BEAN_NAME );
  }
  // participantService
}

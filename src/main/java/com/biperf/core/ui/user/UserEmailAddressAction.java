/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javaui/com/biperf/core/ui/user/UserEmailAddressAction.java,v $
 */

package com.biperf.core.ui.user;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.biperf.core.domain.enums.EmailAddressType;
import com.biperf.core.domain.enums.PhoneType;
import com.biperf.core.domain.user.UserEmailAddress;
import com.biperf.core.domain.user.UserPhone;
import com.biperf.core.exception.BeaconRuntimeException;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.participant.AutoCompleteService;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.service.security.AuthorizationService;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.ui.utils.ActionUtils;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.ui.utils.ServiceErrorStrutsUtils;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;
import com.biperf.core.utils.ClientStateUtils;
import com.biperf.core.utils.InvalidClientStateException;
import com.biperf.core.utils.UserManager;

/**
 * UserEmailAddressAction.
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
 * <td>sharma</td>
 * <td>Apr 27, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class UserEmailAddressAction extends BaseDispatchAction
{
  private static final String PAX_SCREEN = "paxScreen";

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
    return displayList( actionMapping, actionForm, request, response );
  }

  /**
   * @param actionMapping
   * @param actionForm
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward displayList( ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response )
  {
    final String METHOD_NAME = "displayList";

    String userId = "";
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
        userId = (String)clientStateMap.get( "userId" );
      }
      catch( ClassCastException cce )
      {
        Long id = (Long)clientStateMap.get( "userId" );
        userId = id.toString();
      }
      if ( userId == null )
      {
        ActionMessages errors = new ActionMessages();
        errors.add( "errorMessage", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, "userId as part of clientState" ) );
        saveErrors( request, errors );
        return actionMapping.findForward( ActionConstants.FAIL_ERRORS_ONLY );
      }
    }
    catch( InvalidClientStateException e )
    {
      throw new IllegalArgumentException( "request parameter clientState was missing" );
    }
    UserEmailAddressForm userEmailAddressForm = (UserEmailAddressForm)actionForm;

    if ( isCancelled( request ) )
    {
      // EARLY EXIT
      if ( userEmailAddressForm.isFromPaxScreen() )
      {
        Map clientStateParameterMap = new HashMap();
        clientStateParameterMap.put( "userId", userId );
        String queryString = ClientStateUtils.generateEncodedLink( "", "", clientStateParameterMap );
        String method = "method=display";
        return ActionUtils.forwardWithParameters( actionMapping, PAX_SCREEN, new String[] { queryString, method } );
      }

      Map clientStateParameterMap = new HashMap();
      clientStateParameterMap.put( "userId", userId );
      String queryString = ClientStateUtils.generateEncodedLink( "", "", clientStateParameterMap );
      return ActionUtils.forwardWithParameters( actionMapping, ActionConstants.CANCEL_FORWARD, new String[] { queryString } );
    }

    Long longUserId = new Long( userId );
    UserEmailAddress userEmailAddress = getUserService().getPrimaryUserEmailAddress( longUserId );
    if ( userEmailAddress != null )
    {
      userEmailAddressForm.setPrimary( userEmailAddress.getEmailType().getCode() );
    }
    userEmailAddressForm.setUserId( userId );

    ActionForward actionForward = actionMapping.findForward( ActionConstants.SUCCESS_FORWARD );

    userEmailAddressForm.setMethod( METHOD_NAME );

    return actionForward;
  }

  /**
   * Saves the changes to user's email address list
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward changePrimary( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    ActionMessages errors = new ActionMessages();
    UserEmailAddressForm userEmailAddressForm = (UserEmailAddressForm)form;

    Long userId = new Long( userEmailAddressForm.getUserId() );

    String primaryEmaiAddressType = userEmailAddressForm.getPrimary();

    // The recovery email address can't be a primary contact
    if ( EmailAddressType.RECOVERY.equals( primaryEmaiAddressType ) )
    {
      errors.add( "errorMessage", new ActionMessage( "participant.emailaddr.PRIMARY_RECOVERY_ERROR" ) );
    }
    if ( !errors.isEmpty() )
    {
      saveErrors( request, errors );
      return mapping.findForward( ActionConstants.FAIL_FORWARD );
    }

    if ( primaryEmaiAddressType != null )
    {
      try
      {
        getUserService().setPrimaryUserEmailAddress( userId, EmailAddressType.lookup( primaryEmaiAddressType ) );
      }
      catch( ServiceErrorException se )
      {
        List serviceErrors = se.getServiceErrors();
        ServiceErrorStrutsUtils.convertServiceErrorsToActionErrors( serviceErrors, errors );
        saveErrors( request, errors );
        return mapping.findForward( ActionConstants.FAIL_FORWARD );
      }
    }
    Map clientStateParameterMap = new HashMap();
    clientStateParameterMap.put( "userId", userEmailAddressForm.getUserId() );
    String queryString = ClientStateUtils.generateEncodedLink( "", "", clientStateParameterMap );

    if ( userEmailAddressForm.isFromPaxScreen() )
    {
      String method = "method=prepareEmailAddressesDisplay";
      return ActionUtils.forwardWithParameters( mapping, PAX_SCREEN, new String[] { queryString, method } );
    }

    return ActionUtils.forwardWithParameters( mapping, ActionConstants.SUCCESS_UPDATE, new String[] { queryString } );
  }

  /**
   * Removes the email address from user's email address list
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward remove( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    UserEmailAddressForm userEmailAddressForm = (UserEmailAddressForm)form;

    Long userId = new Long( userEmailAddressForm.getUserId() );

    ActionMessages errors = new ActionMessages();

    // For recovery change notification
    String recoveryEmailBeforeSave = getRecoveryEmail( userId ).map( UserEmailAddress::getEmailAddr ).orElse( null );
    Optional<UserPhone> recoveryPhoneBeforeSave = getRecoveryPhone( userId );
    String recoveryPhoneCCBeforeSave = recoveryPhoneBeforeSave.map( UserPhone::getCountryPhoneCode ).orElse( null );
    String recoveryPhoneNbrBeforeSave = recoveryPhoneBeforeSave.map( UserPhone::getPhoneNbr ).orElse( null );

    String[] deleteEmailAddressTypes = userEmailAddressForm.getDelete();
    if ( deleteEmailAddressTypes != null )
    {
      try
      {
        for ( int i = 0; i < deleteEmailAddressTypes.length; i++ )
        {
          // Don't allow removing someone else's recovery email
          if ( canModifyEmail( userId, deleteEmailAddressTypes[i] ) )
          {
            getUserService().deleteUserEmailAddressbyType( userId, EmailAddressType.lookup( deleteEmailAddressTypes[i] ) );
          }
        }
      }
      catch( ServiceErrorException e )
      {
        log.debug( e );
        List serviceErrors = e.getServiceErrors();
        ServiceErrorStrutsUtils.convertServiceErrorsToActionErrors( serviceErrors, errors );
      }
    }

    if ( !errors.isEmpty() )
    {
      saveErrors( request, errors );
      return mapping.findForward( ActionConstants.FAIL_DELETE );
    }

    // Send out a notification if recovery email changed
    String recoveryEmailAfterSave = getRecoveryEmail( userId ).map( UserEmailAddress::getEmailAddr ).orElse( null );
    getParticipantService().sendRecoveryChangeNotification( userId,
                                                            recoveryPhoneCCBeforeSave,
                                                            recoveryPhoneNbrBeforeSave,
                                                            recoveryPhoneNbrBeforeSave,
                                                            recoveryEmailBeforeSave,
                                                            recoveryEmailAfterSave );

    Map clientStateParameterMap = new HashMap();
    clientStateParameterMap.put( "userId", userEmailAddressForm.getUserId() );
    String queryString = ClientStateUtils.generateEncodedLink( "", "", clientStateParameterMap );
    String fromPaxScreen = "fromPaxScreen=" + userEmailAddressForm.isFromPaxScreen();

    // Elastic search index
    getAutoCompleteService().indexParticipants( Arrays.asList( new Long( userId ) ) );

    return ActionUtils.forwardWithParameters( mapping, ActionConstants.SUCCESS_DELETE, new String[] { queryString, fromPaxScreen } );

  }

  protected boolean canModifyEmail( Long userId, String type )
  {
    if ( EmailAddressType.RECOVERY.equals( type ) )
    {
      return currentUserMatchesView( userId ) || UserManager.isUserInRole( AuthorizationService.ROLE_CODE_MODIFY_RECOVERY_CONTACTS );
    }
    else
    {
      return true;
    }
  }

  /**
   * Create a user's email address
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward create( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    ActionMessages errors = new ActionMessages();
    UserEmailAddressForm userEmailAddressForm = (UserEmailAddressForm)form;

    Long userId = new Long( userEmailAddressForm.getUserId() );

    if ( isCancelled( request ) )
    {
      String queryString = "method=displayList";
      Map clientStateParameterMap = new HashMap();
      clientStateParameterMap.put( "userId", userId );
      String userIdString = ClientStateUtils.generateEncodedLink( "", "", clientStateParameterMap );
      String fromPaxScreen = "fromPaxScreen=" + userEmailAddressForm.isFromPaxScreen();
      return ActionUtils.forwardWithParameters( mapping, ActionConstants.CANCEL_FORWARD, new String[] { queryString, userIdString, fromPaxScreen } );
    }

    // For recovery change notification
    String recoveryEmailBeforeSave = getRecoveryEmail( userId ).map( UserEmailAddress::getEmailAddr ).orElse( null );
    Optional<UserPhone> recoveryPhoneBeforeSave = getRecoveryPhone( userId );
    String recoveryPhoneCCBeforeSave = recoveryPhoneBeforeSave.map( UserPhone::getCountryPhoneCode ).orElse( null );
    String recoveryPhoneNbrBeforeSave = recoveryPhoneBeforeSave.map( UserPhone::getPhoneNbr ).orElse( null );

    // create new user email address
    UserEmailAddress userEmailAddress = userEmailAddressForm.toInsertDoaminObject();

    // Don't allow adding someone else's recovery email. This isn't allowed on screen, but let's
    // catch it here, too, for safety.
    if ( !canModifyEmail( userId, userEmailAddress.getEmailType().getCode() ) )
    {
      throw new BeaconRuntimeException( "Cannot add another user's recovery email" );
    }

    try
    {
      getUserService().addUserEmailAddress( userId, userEmailAddress );
    }
    catch( ServiceErrorException se )
    {
      List serviceErrors = se.getServiceErrors();
      ServiceErrorStrutsUtils.convertServiceErrorsToActionErrors( serviceErrors, errors );
      saveErrors( request, errors );
      return mapping.findForward( ActionConstants.FAIL_CREATE );
    }

    // Send out a notification if recovery email changed
    String recoveryEmailAfterSave = getRecoveryEmail( userId ).map( UserEmailAddress::getEmailAddr ).orElse( null );
    getParticipantService().sendRecoveryChangeNotification( userId,
                                                            recoveryPhoneCCBeforeSave,
                                                            recoveryPhoneNbrBeforeSave,
                                                            recoveryPhoneNbrBeforeSave,
                                                            recoveryEmailBeforeSave,
                                                            recoveryEmailAfterSave );

    Map clientStateParameterMap = new HashMap();
    clientStateParameterMap.put( "userId", userEmailAddressForm.getUserId() );
    String queryString = ClientStateUtils.generateEncodedLink( "", "", clientStateParameterMap );
    String fromPaxScreen = "fromPaxScreen=" + userEmailAddressForm.isFromPaxScreen();
    return ActionUtils.forwardWithParameters( mapping, ActionConstants.SUCCESS_CREATE, new String[] { queryString, fromPaxScreen } );
  }

  /**
   * Create a user's email address
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward update( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    ActionMessages errors = new ActionMessages();
    UserEmailAddressForm userEmailAddressForm = (UserEmailAddressForm)form;

    Long userId = new Long( userEmailAddressForm.getUserId() );

    if ( isCancelled( request ) )
    {
      Map clientStateParameterMap = new HashMap();
      clientStateParameterMap.put( "userId", userId );
      String queryString = ClientStateUtils.generateEncodedLink( "", "", clientStateParameterMap );
      queryString += "&method=displayList";
      String fromPaxScreen = "fromPaxScreen=" + userEmailAddressForm.isFromPaxScreen();
      return ActionUtils.forwardWithParameters( mapping, ActionConstants.CANCEL_FORWARD, new String[] { queryString, fromPaxScreen } );
    }

    // For recovery change notification
    String recoveryEmailBeforeSave = getRecoveryEmail( userId ).map( UserEmailAddress::getEmailAddr ).orElse( null );
    Optional<UserPhone> recoveryPhoneBeforeSave = getRecoveryPhone( userId );
    String recoveryPhoneCCBeforeSave = recoveryPhoneBeforeSave.map( UserPhone::getCountryPhoneCode ).orElse( null );
    String recoveryPhoneNbrBeforeSave = recoveryPhoneBeforeSave.map( UserPhone::getPhoneNbr ).orElse( null );

    // update an existing email address
    UserEmailAddress userEmailAddress = userEmailAddressForm.toUpdateDoaminObject();

    // Don't allow changing someone else's recovery email. This isn't allowed on screen, but let's
    // catch it here, too, for safety.
    if ( !canModifyEmail( userId, userEmailAddress.getEmailType().getCode() ) )
    {
      throw new BeaconRuntimeException( "Cannot update another user's recovery email" );
    }

    try
    {
      getUserService().updateUserEmailAddress( userId, userEmailAddress );
    }
    catch( ServiceErrorException se )
    {
      List serviceErrors = se.getServiceErrors();
      ServiceErrorStrutsUtils.convertServiceErrorsToActionErrors( serviceErrors, errors );
      saveErrors( request, errors );
      return mapping.findForward( ActionConstants.FAIL_FORWARD );
    }

    // Send out a notification if recovery email changed
    String recoveryEmailAfterSave = getRecoveryEmail( userId ).map( UserEmailAddress::getEmailAddr ).orElse( null );
    getParticipantService().sendRecoveryChangeNotification( userId,
                                                            recoveryPhoneCCBeforeSave,
                                                            recoveryPhoneNbrBeforeSave,
                                                            recoveryPhoneNbrBeforeSave,
                                                            recoveryEmailBeforeSave,
                                                            recoveryEmailAfterSave );

    Map clientStateParameterMap = new HashMap();
    clientStateParameterMap.put( "userId", userEmailAddressForm.getUserId() );
    String queryString = ClientStateUtils.generateEncodedLink( "", "", clientStateParameterMap );
    String fromPaxScreen = "fromPaxScreen=" + userEmailAddressForm.isFromPaxScreen();

    // Elastic search index
    getAutoCompleteService().indexParticipants( Arrays.asList( new Long( userId ) ) );

    return ActionUtils.forwardWithParameters( mapping, ActionConstants.SUCCESS_UPDATE, new String[] { queryString, fromPaxScreen } );
  }

  /**
   * @param actionMapping
   * @param actionForm
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward prepareUpdate( ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response )
  {
    UserEmailAddressForm userEmailAddressForm = (UserEmailAddressForm)actionForm;

    Long userId = new Long( userEmailAddressForm.getUserId() );
    String emailType = userEmailAddressForm.getEmailAddrType();
    Set userEmailAddresses = getUserService().getUserEmailAddresses( userId );
    for ( Iterator iter = userEmailAddresses.iterator(); iter.hasNext(); )
    {
      UserEmailAddress userEmailAddress = (UserEmailAddress)iter.next();
      if ( userEmailAddress.getEmailType().getCode().equals( emailType ) )
      {
        userEmailAddressForm.load( userEmailAddress );
      }
    }

    userEmailAddressForm.setMethod( "update" );

    return actionMapping.findForward( ActionConstants.EDIT_FORWARD );
  }

  /**
   * @param mapping
   * @param actionForm
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward prepareCreate( ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response )
  {
    UserEmailAddressForm userEmailAddressForm = (UserEmailAddressForm)actionForm;

    userEmailAddressForm.setMethod( "create" );

    // get the actionForward to display the create pages.
    return mapping.findForward( ActionConstants.CREATE_FORWARD );
  }

  /** For preventing changing recovery email. Is the current user the same as the one whose information is being changed */
  private boolean currentUserMatchesView( Long viewUserId )
  {
    return UserManager.getUserId().equals( viewUserId );
  }

  @SuppressWarnings( "unchecked" )
  private Optional<UserEmailAddress> getRecoveryEmail( Long userId )
  {
    Set<UserEmailAddress> emails = getUserService().getUserEmailAddresses( userId );
    return emails.stream().filter( email -> email.getEmailType().getCode().equals( EmailAddressType.RECOVERY ) ).findFirst();
  }

  @SuppressWarnings( "unchecked" )
  private Optional<UserPhone> getRecoveryPhone( Long userId )
  {
    Set<UserPhone> phones = getUserService().getUserPhones( userId );
    return phones.stream().filter( phone -> phone.getPhoneType().getCode().equals( PhoneType.RECOVERY ) ).findFirst();
  }

  /**
   * Get the userService from the BeanFactory.
   * 
   * @return UserService
   */
  private UserService getUserService()
  {
    return (UserService)getService( UserService.BEAN_NAME );
  }

  private ParticipantService getParticipantService()
  {
    return (ParticipantService)getService( ParticipantService.BEAN_NAME );
  }

  private static AutoCompleteService getAutoCompleteService()
  {
    return (AutoCompleteService)getService( AutoCompleteService.BEAN_NAME );
  }
}

/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/user/UserPhoneAction.java,v $
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

import org.apache.struts.action.ActionErrors;
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
 * UserPhoneAction.
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
 * <td>Apr 25, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class UserPhoneAction extends BaseDispatchAction
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
    String userId = null;
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
    UserPhoneListForm userPhoneListForm = null;
    if ( actionForm instanceof UserPhoneListForm )
    {
      userPhoneListForm = (UserPhoneListForm)actionForm;
    }
    else
    {
      userPhoneListForm = new UserPhoneListForm();
      request.setAttribute( "userPhoneListForm", userPhoneListForm );
    }

    if ( isCancelled( request ) )
    {
      // EARLY EXIT
      if ( userPhoneListForm.isFromPaxScreen() )
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
      queryString += "&method=display";
      return ActionUtils.forwardWithParameters( actionMapping, ActionConstants.CANCEL_FORWARD, new String[] { queryString } );
    }

    UserPhone userPhone = getUserService().getPrimaryUserPhone( new Long( userId ) );
    if ( userPhone != null )
    {
      userPhoneListForm.setPrimary( userPhone.getPhoneType().getCode() );
    }
    userPhoneListForm.setUserId( userId.toString() );

    return actionMapping.findForward( ActionConstants.SUCCESS_FORWARD );
  }

  /**
   * @param actionMapping
   * @param actionForm
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward changePrimary( ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response )
  {
    ActionMessages errors = new ActionMessages();
    UserPhoneListForm userPhoneListForm = (UserPhoneListForm)actionForm;

    String primaryType = userPhoneListForm.getPrimary();
    Long userId = new Long( userPhoneListForm.getUserId() );

    // The recovery phone number can't be a primary contact
    if ( PhoneType.RECOVERY.equals( primaryType ) )
    {
      errors.add( "errorMessage", new ActionMessage( "participant.phone.list.PRIMARY_RECOVERY_ERROR" ) );
    }
    if ( !errors.isEmpty() )
    {
      saveErrors( request, errors );
      return actionMapping.findForward( ActionConstants.FAIL_FORWARD );
    }

    try
    {
      getUserService().updatePrimaryPhone( userId, primaryType );
    }
    catch( ServiceErrorException se )
    {
      List serviceErrors = se.getServiceErrors();
      ServiceErrorStrutsUtils.convertServiceErrorsToActionErrors( serviceErrors, errors );
      saveErrors( request, errors );
      return actionMapping.findForward( ActionConstants.FAIL_FORWARD );
    }

    Map clientStateParameterMap = new HashMap();
    clientStateParameterMap.put( "userId", userId );
    String queryString = ClientStateUtils.generateEncodedLink( "", "", clientStateParameterMap );
    if ( userPhoneListForm.isFromPaxScreen() )
    {
      String method = "method=display";
      return ActionUtils.forwardWithParameters( actionMapping, PAX_SCREEN, new String[] { queryString, method } );
    }

    return ActionUtils.forwardWithParameters( actionMapping, ActionConstants.SUCCESS_FORWARD, new String[] { queryString } );
  }

  /**
   * @param actionMapping
   * @param actionForm
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward remove( ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response )
  {
    UserPhoneListForm form = (UserPhoneListForm)actionForm;

    Long userId = new Long( form.getUserId() );
    String[] deleteList = form.getDelete();
    ActionMessages errors = new ActionMessages();
    // BugFix 19077.Do not Delete the primary Phone Details.
    Set userPhones = getUserService().getUserPhones( userId );

    // For recovery change notification
    String recoveryEmailBeforeSave = getRecoveryEmail( userId ).map( UserEmailAddress::getEmailAddr ).orElse( null );
    Optional<UserPhone> recoveryPhoneBeforeSave = getRecoveryPhone( userId );
    String recoveryPhoneCCBeforeSave = recoveryPhoneBeforeSave.map( UserPhone::getCountryPhoneCode ).orElse( null );
    String recoveryPhoneNbrBeforeSave = recoveryPhoneBeforeSave.map( UserPhone::getPhoneNbr ).orElse( null );

    if ( deleteList == null || deleteList.length < 1 )
    {
      errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "participant.phone.list.NOTHING_SELECTED_TO_DELETE_ERROR" ) );
    }
    if ( userPhones != null && userPhones.size() > 0 )
    {
      if ( userPhones.size() == 1 && deleteList != null && deleteList.length > 0 )
      {
        errors.add( "errorMessage", new ActionMessage( "participant.phone.list.PRIMARY_PHONE_DO_NOT_DELETE" ) );

      }
      if ( errors.size() == 0 )
      {
        for ( Iterator iter = userPhones.iterator(); iter.hasNext(); )
        {
          UserPhone savedUserPhone = (UserPhone)iter.next();
          if ( savedUserPhone.isPrimary() )
          {
            for ( int i = 0; deleteList != null && i < deleteList.length; i++ )
            {
              if ( savedUserPhone.getPhoneType().getCode().equals( deleteList[i] ) )
              {

                errors.add( "errorMessage", new ActionMessage( "participant.phone.list.PRIMARY_PHONE_DO_NOT_DELETE" ) );

              }
            }
          }
        }
      }
    }
    for ( String deleteCode : deleteList )
    {
      if ( !canModifyPhone( userId, deleteCode ) )
      {
        errors.add( "errorMessage", new ActionMessage( "participant.phone.list.REMOVE_RECOVERY_PHONE" ) );
      }
    }
    if ( errors.size() > 0 )
    {
      saveErrors( request, errors );
      // return actionMapping.findForward( ActionConstants.FAIL_ERRORS_ONLY );
    }
    else if ( deleteList != null )
    {
      for ( int i = 0; i < deleteList.length; i++ )
      {
        String deleteCode = deleteList[i];
        getUserService().deleteUserPhone( userId, deleteCode );
      }
    }

    // Send out a notification if recovery phone changed
    Optional<UserPhone> recoveryPhoneAfterSave = getRecoveryPhone( userId );
    String recoveryPhoneNbrAfterSave = recoveryPhoneAfterSave.map( UserPhone::getPhoneNbr ).orElse( null );
    getParticipantService().sendRecoveryChangeNotification( userId,
                                                            recoveryPhoneCCBeforeSave,
                                                            recoveryPhoneNbrBeforeSave,
                                                            recoveryPhoneNbrAfterSave,
                                                            recoveryEmailBeforeSave,
                                                            recoveryEmailBeforeSave );

    Map clientStateParameterMap = new HashMap();
    clientStateParameterMap.put( "userId", form.getUserId() );
    String queryString = ClientStateUtils.generateEncodedLink( "", "", clientStateParameterMap );
    String fromPaxScreen = "fromPaxScreen=" + form.isFromPaxScreen();

    // Elastic search index
    getAutoCompleteService().indexParticipants( Arrays.asList( new Long( userId ) ) );

    return ActionUtils.forwardWithParameters( actionMapping, ActionConstants.SUCCESS_FORWARD, new String[] { queryString, fromPaxScreen } );
  }

  protected boolean canModifyPhone( Long userId, String type )
  {
    if ( PhoneType.RECOVERY.equals( type ) )
    {
      return currentUserMatchesView( userId ) || UserManager.isUserInRole( AuthorizationService.ROLE_CODE_MODIFY_RECOVERY_CONTACTS );
    }
    else
    {
      return true;
    }
  }

  /**
   * @param actionMapping
   * @param actionForm
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward update( ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response )
  {
    ActionMessages errors = new ActionMessages();
    if ( !isTokenValid( request, true ) )
    {
      errors.add( ActionConstants.ERROR_TOKEN_FAILURE, new ActionMessage( ActionConstants.ERROR_TOKEN_EXCEPTION ) );
      saveErrors( request, errors );
      return actionMapping.findForward( ActionConstants.FAIL_UPDATE ); // EARLY EXIT
    }

    UserPhoneForm form = (UserPhoneForm)actionForm;

    Long userId = new Long( form.getUserId() );

    if ( isCancelled( request ) )
    {
      Map clientStateParameterMap = new HashMap();
      clientStateParameterMap.put( "userId", userId );
      String queryString = ClientStateUtils.generateEncodedLink( "", "", clientStateParameterMap );
      queryString += "&method=displayList";
      String fromPaxScreen = "fromPaxScreen=" + form.isFromPaxScreen();
      return ActionUtils.forwardWithParameters( actionMapping, ActionConstants.CANCEL_FORWARD, new String[] { queryString, fromPaxScreen } );
    }

    UserPhone userPhone = form.toFullDomainObject();

    // For recovery change notification
    String recoveryEmailBeforeSave = getRecoveryEmail( userId ).map( UserEmailAddress::getEmailAddr ).orElse( null );
    Optional<UserPhone> recoveryPhoneBeforeSave = getRecoveryPhone( userId );
    String recoveryPhoneCCBeforeSave = recoveryPhoneBeforeSave.map( UserPhone::getCountryPhoneCode ).orElse( null );
    String recoveryPhoneNbrBeforeSave = recoveryPhoneBeforeSave.map( UserPhone::getPhoneNbr ).orElse( null );

    // Don't allow adding someone else's recovery phone number. This isn't allowed on screen, but
    // let's catch it here too for saftey.
    if ( !canModifyPhone( userId, userPhone.getPhoneType().getCode() ) )
    {
      throw new BeaconRuntimeException( "Cannot add a recovery phone number for another user" );
    }

    try
    {
      getUserService().updateUserPhone( userId, userPhone );
    }
    catch( ServiceErrorException se )
    {
      List serviceErrors = se.getServiceErrors();
      ServiceErrorStrutsUtils.convertServiceErrorsToActionErrors( serviceErrors, errors );
      saveErrors( request, errors );
      return actionMapping.findForward( ActionConstants.FAIL_UPDATE );
    }

    // Send out a notification if recovery phone changed
    Optional<UserPhone> recoveryPhoneAfterSave = getRecoveryPhone( userId );
    String recoveryPhoneNbrAfterSave = recoveryPhoneAfterSave.map( UserPhone::getPhoneNbr ).orElse( null );
    getParticipantService().sendRecoveryChangeNotification( userId,
                                                            recoveryPhoneCCBeforeSave,
                                                            recoveryPhoneNbrBeforeSave,
                                                            recoveryPhoneNbrAfterSave,
                                                            recoveryEmailBeforeSave,
                                                            recoveryEmailBeforeSave );

    Map clientStateParameterMap = new HashMap();
    clientStateParameterMap.put( "userId", form.getUserId() );
    String queryString = ClientStateUtils.generateEncodedLink( "", "", clientStateParameterMap );
    String fromPaxScreen = "fromPaxScreen=" + form.isFromPaxScreen();

    // Elastic search index
    getAutoCompleteService().indexParticipants( Arrays.asList( new Long( userId ) ) );

    return ActionUtils.forwardWithParameters( actionMapping, ActionConstants.SUCCESS_UPDATE, new String[] { queryString, fromPaxScreen } );
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
    ActionMessages errors = new ActionMessages();
    if ( !isTokenValid( request, true ) )
    {
      errors.add( ActionConstants.ERROR_TOKEN_FAILURE, new ActionMessage( ActionConstants.ERROR_TOKEN_EXCEPTION ) );
      saveErrors( request, errors );
      return actionMapping.findForward( ActionConstants.FAIL_CREATE ); // EARLY EXIT
    }

    UserPhoneForm form = (UserPhoneForm)actionForm;

    Long userId = new Long( form.getUserId() );

    if ( isCancelled( request ) )
    {
      Map clientStateParameterMap = new HashMap();
      clientStateParameterMap.put( "userId", userId );
      String queryString = ClientStateUtils.generateEncodedLink( "", "", clientStateParameterMap );
      queryString += "&method=displayList";
      String fromPaxScreen = "fromPaxScreen=" + form.isFromPaxScreen();
      return ActionUtils.forwardWithParameters( actionMapping, ActionConstants.CANCEL_FORWARD, new String[] { queryString, fromPaxScreen } );
    }

    UserPhone userPhoneToAdd = form.toInsertedDomainObject();

    // For recovery change notification
    String recoveryEmailBeforeSave = getRecoveryEmail( userId ).map( UserEmailAddress::getEmailAddr ).orElse( null );
    Optional<UserPhone> recoveryPhoneBeforeSave = getRecoveryPhone( userId );
    String recoveryPhoneCCBeforeSave = recoveryPhoneBeforeSave.map( UserPhone::getCountryPhoneCode ).orElse( null );
    String recoveryPhoneNbrBeforeSave = recoveryPhoneBeforeSave.map( UserPhone::getPhoneNbr ).orElse( null );

    // Don't allow adding someone else's recovery phone number. This isn't allowed on screen, but
    // let's catch it here too for saftey.
    if ( !canModifyPhone( userId, userPhoneToAdd.getPhoneType().getCode() ) )
    {
      throw new BeaconRuntimeException( "Cannot add a recovery phone number for another user" );
    }

    try
    {
      getUserService().addUserPhone( userId, userPhoneToAdd );
    }
    catch( ServiceErrorException se )
    {
      List serviceErrors = se.getServiceErrors();
      ServiceErrorStrutsUtils.convertServiceErrorsToActionErrors( serviceErrors, errors );
      saveErrors( request, errors );
      return actionMapping.findForward( ActionConstants.FAIL_CREATE );
    }

    // Send out a notification if recovery phone changed
    Optional<UserPhone> recoveryPhoneAfterSave = getRecoveryPhone( userId );
    String recoveryPhoneNbrAfterSave = recoveryPhoneAfterSave.map( UserPhone::getPhoneNbr ).orElse( null );
    getParticipantService().sendRecoveryChangeNotification( userId,
                                                            recoveryPhoneCCBeforeSave,
                                                            recoveryPhoneNbrBeforeSave,
                                                            recoveryPhoneNbrAfterSave,
                                                            recoveryEmailBeforeSave,
                                                            recoveryEmailBeforeSave );

    Map clientStateParameterMap = new HashMap();
    clientStateParameterMap.put( "userId", form.getUserId() );
    String queryString = ClientStateUtils.generateEncodedLink( "", "", clientStateParameterMap );
    String fromPaxScreen = "fromPaxScreen=" + form.isFromPaxScreen();
    return ActionUtils.forwardWithParameters( actionMapping, ActionConstants.SUCCESS_CREATE, new String[] { queryString, fromPaxScreen } );
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
    UserPhoneForm form = (UserPhoneForm)actionForm;

    Long userId = new Long( form.getUserId() );
    String phoneType = form.getPhoneType();

    UserPhone userPhone = getUserService().getUserPhone( userId, phoneType );

    form.load( userPhone );

    form.setMethod( "update" );

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
    UserPhoneForm form = (UserPhoneForm)actionForm;

    form.setMethod( "create" );

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

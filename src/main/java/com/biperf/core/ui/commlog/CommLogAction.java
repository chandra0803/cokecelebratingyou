/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/commlog/CommLogAction.java,v $
 */

package com.biperf.core.ui.commlog;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.biperf.core.domain.commlog.CommLog;
import com.biperf.core.domain.commlog.CommLogComment;
import com.biperf.core.domain.enums.LanguageType;
import com.biperf.core.domain.mailing.Mailing;
import com.biperf.core.domain.user.User;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.commlog.CommLogAssociationRequest;
import com.biperf.core.service.commlog.CommLogService;
import com.biperf.core.service.email.MailingService;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.ui.participant.ParticipantSearchAction;
import com.biperf.core.ui.utils.ActionUtils;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;
import com.biperf.core.utils.ClientStateUtils;
import com.biperf.core.utils.InvalidClientStateException;
import com.biperf.core.utils.UserManager;

/**
 * CommLogAction
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
 * <td>Ashok Attada</td>
 * <td>Nov 18, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class CommLogAction extends BaseDispatchAction
{
  private static final Log logger = LogFactory.getLog( CommLogAction.class );
  public static final String SESSION_COMM_LOG_FORM = "sessionCommLogForm";
  protected SystemVariableService systemVariableService;

  /**
   * @param actionMapping
   * @param actionForm
   * @param request
   * @param response
   * @return ActionForward
   */
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
      userId = (String)clientStateMap.get( "userId" );
      if ( userId == null )
      {
        ActionMessages errors = new ActionMessages();
        errors.add( "errorMessage", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, "userId as part of clientState" ) );
        saveErrors( request, errors );
        return actionMapping.findForward( ActionConstants.SUCCESS_FORWARD );
      }
    }
    catch( InvalidClientStateException e )
    {
      throw new IllegalArgumentException( "request parameter clientState was missing" );
    }

    request.setAttribute( "userId", userId );

    if ( isCancelled( request ) )
    {
      Map clientStateParameterMap = new HashMap();
      clientStateParameterMap.put( "userId", userId );
      String queryString = ClientStateUtils.generateEncodedLink( "", "", clientStateParameterMap );
      queryString = queryString + "method=display";
      return ActionUtils.forwardWithParameters( actionMapping, ActionConstants.CANCEL_FORWARD, new String[] { queryString } );
    }

    return actionMapping.findForward( ActionConstants.SUCCESS_FORWARD );
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
      return actionMapping.findForward( ActionConstants.FAIL_FORWARD ); // EARLY EXIT
    }
    // code Fix to Bug#17851 check the status type of commlogs
    if ( request.getParameter( "urgencyTypeInitial" ) != null && request.getParameter( "urgencyTypeInitial" ).equals( "normal" ) )
    {
      request.setAttribute( "commLogListType", MyCommLogListController.OPEN_LIST );
    }
    else
    {
      request.setAttribute( "commLogListType", MyCommLogListController.ESCALATED_LIST );
    }

    CommLogForm form = (CommLogForm)actionForm;
    // Long userId = new Long( form.getUserId() );
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
      userId = (String)clientStateMap.get( "userId" );
    }
    catch( InvalidClientStateException e )
    {
      throw new IllegalArgumentException( "request parameter clientState was missing" );
    }

    if ( isCancelled( request ) )
    {
      // BugFix 18201 You need to have the userId for the BackToOverview DispatchAction to display
      // the Pax Overview Screen
      request.setAttribute( "userId", userId );
      Map clientStateParameterMap = new HashMap();
      clientStateParameterMap.put( "userId", userId );
      String queryString = ClientStateUtils.generateEncodedLink( "", "", clientStateParameterMap );
      queryString += "&method=displayList";
      // code Fix to Bug#17851 check the where from the list comes,accordingly redirects the page
      if ( request.getParameter( "commLogList" ) != null && request.getParameter( "commLogList" ).equals( "adminCommLogs" ) )
      {
        return ActionUtils.forwardWithParameters( actionMapping, "cancel_admin_comm_log", new String[] { queryString } );
      }
      return ActionUtils.forwardWithParameters( actionMapping, "cancel_participant_comm_log", new String[] { queryString } );

    }

    CommLog commLog = form.toDomainObject();
    commLog.setUser( getUserService().getUserById( new Long( form.getUserId() ) ) );
    commLog.setAssignedByUser( getUserService().getUserById( UserManager.getUserId() ) );
    if ( ! ( commLog.getCommLogStatusType().isClosed() || commLog.getCommLogStatusType().isDeferred() ) )
    {
      // no need to set assigned to usr for closed and deffered
      commLog.setAssignedToUser( getUserService().getUserById( new Long( form.getAssignedToUserId() ) ) );
    }

    // adding comments
    if ( form.getNewComment() != null && !"".equals( form.getNewComment() ) )
    {
      Set commentsHistorySet = getUpdatedCommentsHistory( form.getNewComment(), commLog );
      commLog.setComments( commentsHistorySet );
    }

    getCommLogService().saveCommLog( commLog );

    Map clientStateParameterMap = new HashMap();
    clientStateParameterMap.put( "userId", form.getUserId() );
    String queryString = ClientStateUtils.generateEncodedLink( "", "", clientStateParameterMap );
    // code Fix to Bug#17851 check where from the list comes,accordingly redirects the page
    if ( request.getParameter( "commLogList" ) != null && request.getParameter( "commLogList" ).equals( "adminCommLogs" ) )
    {
      return ActionUtils.forwardWithParameters( actionMapping, "success_admin_comm_log", new String[] { queryString } );
    }
    return ActionUtils.forwardWithParameters( actionMapping, "success_participant_comm_log", new String[] { queryString } );

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
      userId = (String)clientStateMap.get( "userId" );
      if ( userId == null )
      {
        errors.add( "errorMessage", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, "userId as part of clientState" ) );
        saveErrors( request, errors );
      }
    }
    catch( InvalidClientStateException e )
    {
      throw new IllegalArgumentException( "request parameter clientState was missing" );
    }
    CommLogForm form = (CommLogForm)actionForm;

    if ( isCancelled( request ) )
    {
      Map clientStateParameterMap = new HashMap();
      clientStateParameterMap.put( "userId", userId );
      String queryString = ClientStateUtils.generateEncodedLink( "", "", clientStateParameterMap );
      queryString += "&method=displayList";
      return ActionUtils.forwardWithParameters( actionMapping, ActionConstants.CANCEL_FORWARD, new String[] { queryString } );
    }

    CommLog commLog = form.toDomainObject();
    commLog.setUser( getUserService().getUserById( new Long( form.getUserId() ) ) );
    commLog.setAssignedByUser( getUserService().getUserById( UserManager.getUserId() ) );
    if ( ! ( commLog.getCommLogStatusType().isClosed() || commLog.getCommLogStatusType().isDeferred() ) )
    {
      // no need to set assigned to usr for closed and deffered
      commLog.setAssignedToUser( getUserService().getUserById( new Long( form.getAssignedToUserId() ) ) );
    }
    // adding comments
    if ( form.getNewComment() != null && !"".equals( form.getNewComment() ) )
    {
      Set commentsHistorySet = getUpdatedCommentsHistory( form.getNewComment(), commLog );
      commLog.setComments( commentsHistorySet );
    }

    // update the commlog
    getCommLogService().saveCommLog( commLog );

    Map clientStateParameterMap = new HashMap();
    clientStateParameterMap.put( "userId", form.getUserId() );
    String queryString = ClientStateUtils.generateEncodedLink( "", "", clientStateParameterMap );
    return ActionUtils.forwardWithParameters( actionMapping, ActionConstants.SUCCESS_CREATE, new String[] { queryString } );
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
    CommLogForm form = (CommLogForm)actionForm;

    form.setMethod( "create" );

    // get the actionForward to display the create pages.
    return mapping.findForward( ActionConstants.CREATE_FORWARD );
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
    CommLogForm form = (CommLogForm)actionForm;
    String commLogId = "";
    try
    {
      String clientState = RequestUtils.getRequiredParamString( request, "clientState" );
      String cryptoPass = RequestUtils.getOptionalParamString( request, "cryptoPass" );
      String password = ClientStatePasswordManager.getPassword();
      // code Fix to Bug#17851 get the attribute from jsp
      request.setAttribute( "commLogList", request.getParameter( "commLogList" ) );
      if ( cryptoPass != null && cryptoPass.equals( "1" ) )
      {
        password = ClientStatePasswordManager.getGlobalPassword();
      }
      // Deserialize the client state.
      Map clientStateMap = ClientStateSerializer.deserialize( clientState, password );
      try
      {
        commLogId = String.valueOf( clientStateMap.get( "commLogId" ) ); // 17960
      }
      catch( ClassCastException cce )
      {
        Long commLogIdLong = (Long)clientStateMap.get( "commLogId" );
        commLogId = commLogIdLong.toString();
      }
      if ( commLogId == null )
      {
        ActionMessages errors = new ActionMessages();
        errors.add( "errorMessage", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, "commLogId as part of clientState" ) );
        saveErrors( request, errors );
        return actionMapping.findForward( ActionConstants.UPDATE_FORWARD );
      }
    }
    catch( InvalidClientStateException e )
    {
      throw new IllegalArgumentException( "request parameter clientState was missing" );
    }
    AssociationRequestCollection requestCollection = new AssociationRequestCollection();
    requestCollection.add( new CommLogAssociationRequest( CommLogAssociationRequest.ALL ) );
    CommLog commLog = getCommLogService().getCommLogById( new Long( commLogId ), requestCollection );

    form.load( commLog );

    form.setMethod( "update" );

    return actionMapping.findForward( ActionConstants.UPDATE_FORWARD );
  }

  /**
   * @param actionMapping
   * @param actionForm
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward prepareDisplay( ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response )
  {
    CommLogForm form = (CommLogForm)actionForm;
    String commLogId = "";
    String backUrl = null;
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
      backUrl = (String)clientStateMap.get( "backUrl" );
      if ( StringUtils.isNotBlank( backUrl ) )
      {
        request.setAttribute( "backUrl", backUrl );
      }
      try
      {
        commLogId = (String)clientStateMap.get( "commLogId" );
      }
      catch( ClassCastException cce )
      {
        Long commLogIdLong = (Long)clientStateMap.get( "commLogId" );
        commLogId = commLogIdLong.toString();
      }
      if ( commLogId == null )
      {
        ActionMessages errors = new ActionMessages();
        errors.add( "errorMessage", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, "commLogId as part of clientState" ) );
        saveErrors( request, errors );
        return actionMapping.findForward( ActionConstants.DISPLAY_FORWARD );
      }
    }
    catch( InvalidClientStateException e )
    {
      throw new IllegalArgumentException( "request parameter clientState was missing" );
    }
    AssociationRequestCollection requestCollection = new AssociationRequestCollection();
    requestCollection.add( new CommLogAssociationRequest( CommLogAssociationRequest.ALL ) );
    CommLog commLog = getCommLogService().getCommLogById( new Long( commLogId ), requestCollection );

    form.load( commLog );

    form.setMethod( "view" );

    return actionMapping.findForward( ActionConstants.DISPLAY_FORWARD );
  }

  /**
   * @param actionMapping
   * @param actionForm
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward changeCreateUser( ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response )
  {
    CommLogForm form = (CommLogForm)actionForm;

    // Put the form in the session to be reloaded when coming back from node lookup.
    request.getSession().setAttribute( SESSION_COMM_LOG_FORM, form );

    String queryString = ParticipantSearchAction.RETURN_ACTION_URL_PARAM + "=" + "/participant/userCommLogMaintainDisplay.do?method=createApprover";

    return ActionUtils.forwardWithParameters( actionMapping, "select_approver", new String[] { queryString } );
  }

  /**
   * @param actionMapping
   * @param actionForm
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward changeUpdateUser( ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response )
  {
    CommLogForm form = (CommLogForm)actionForm;

    // Put the form in the session to be reloaded when coming back from node lookup.
    request.getSession().setAttribute( SESSION_COMM_LOG_FORM, form );

    String queryString = ParticipantSearchAction.RETURN_ACTION_URL_PARAM + "=" + "/participant/userCommLogMaintainDisplay.do?method=updateApprover";

    return ActionUtils.forwardWithParameters( actionMapping, "select_approver", new String[] { queryString } );
  }

  /**
   * @param actionMapping
   * @param actionForm
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward updateApprover( ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response )
  {
    CommLogForm commLogForm = (CommLogForm)actionForm;

    // Get the form back out of the Session to redisplay.
    CommLogForm sessionCommLogForm = (CommLogForm)request.getSession().getAttribute( SESSION_COMM_LOG_FORM );

    if ( sessionCommLogForm != null )
    {
      String assignedToUserId = "";
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
        assignedToUserId = (String)clientStateMap.get( "assignedToUserId" );
        if ( assignedToUserId == null )
        {
          ActionMessages errors = new ActionMessages();
          errors.add( "errorMessage", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, "assignedToUserId as part of clientState" ) );
          saveErrors( request, errors );
          return actionMapping.findForward( "update_approver" );
        }
      }
      catch( InvalidClientStateException e )
      {
        throw new IllegalArgumentException( "request parameter clientState was missing" );
      }
      sessionCommLogForm.setAssignedToUserId( assignedToUserId );
      if ( sessionCommLogForm.getAssignedToUserId() != null && !"".equals( sessionCommLogForm.getAssignedToUserId() ) )
      {
        User user = getUserService().getUserById( new Long( sessionCommLogForm.getAssignedToUserId() ) );
        sessionCommLogForm.setAssignedToName( user.getNameLFMWithComma() );
      }

      try
      {
        BeanUtils.copyProperties( commLogForm, sessionCommLogForm );
      }
      catch( Exception e )
      {
        logger.info( "Copy Properties failed." );
      }
    }

    request.getSession().removeAttribute( SESSION_COMM_LOG_FORM );

    return actionMapping.findForward( "update_approver" );
  }

  /**
   * @param actionMapping
   * @param actionForm
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward createApprover( ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response )
  {
    CommLogForm commLogForm = (CommLogForm)actionForm;

    // Get the form back out of the Session to redisplay.
    CommLogForm sessionCommLogForm = (CommLogForm)request.getSession().getAttribute( SESSION_COMM_LOG_FORM );

    if ( sessionCommLogForm != null )
    {
      String assignedToUserId = "";
      // BugFix 18202
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
        assignedToUserId = (String)clientStateMap.get( "assignedToUserId" );
        userId = (String)clientStateMap.get( "userId" );
        if ( assignedToUserId == null )
        {
          ActionMessages errors = new ActionMessages();
          errors.add( "errorMessage", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, "assignedToUserId as part of clientState" ) );
          saveErrors( request, errors );
          return actionMapping.findForward( "create_approver" );
        }
        if ( userId != null )
        {
          commLogForm.setUserId( userId );
        }

      }
      catch( InvalidClientStateException e )
      {
        throw new IllegalArgumentException( "request parameter clientState was missing" );
      }
      sessionCommLogForm.setAssignedToUserId( assignedToUserId );
      if ( sessionCommLogForm.getAssignedToUserId() != null && !"".equals( sessionCommLogForm.getAssignedToUserId() ) )
      {
        User user = getUserService().getUserById( new Long( sessionCommLogForm.getAssignedToUserId() ) );
        sessionCommLogForm.setAssignedToName( user.getNameLFMWithComma() );
      }

      try
      {
        BeanUtils.copyProperties( commLogForm, sessionCommLogForm );
      }
      catch( Exception e )
      {
        logger.info( "Copy Properties failed." );
      }
    }

    request.getSession().removeAttribute( SESSION_COMM_LOG_FORM );

    return actionMapping.findForward( "create_approver" );
  }

  private CommLogService getCommLogService()
  {
    return (CommLogService)getService( CommLogService.BEAN_NAME );
  }

  private UserService getUserService()
  {
    return (UserService)getService( UserService.BEAN_NAME );
  }

  /**
   * @param actionMapping
   * @param actionForm
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward displayMyEscalatedCommLogs( ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response )
  {
    request.setAttribute( "commLogListType", MyCommLogListController.ESCALATED_LIST );
    return actionMapping.findForward( ActionConstants.SUCCESS_FORWARD );
  }

  /**
   * @param actionMapping
   * @param actionForm
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward displayMyOpenCommLogs( ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response )
  {
    request.setAttribute( "commLogListType", MyCommLogListController.OPEN_LIST );
    return actionMapping.findForward( ActionConstants.SUCCESS_FORWARD );
  }

  /**
   * @param actionMapping
   * @param actionForm
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward message( ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response )
  {
    if ( isCancelled( request ) )
    {
      String clientState = RequestUtils.getRequiredParamString( request, "clientState" );
      String cryptoPass = RequestUtils.getOptionalParamString( request, "cryptoPass" );
      String queryString = "method=prepareDisplay&clientState=" + clientState + "cryptoPass" + cryptoPass;
      return ActionUtils.forwardWithParameters( actionMapping, ActionConstants.CANCEL_FORWARD, new String[] { queryString } );
    }
    return actionMapping.findForward( ActionConstants.SUCCESS_FORWARD );
  }

  /**
   * @param actionMapping
   * @param actionForm
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward sendMessage( ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response )
  {
    String commLogId = "";
    String localeCode = getSystemVariableService().getPropertyByName( SystemVariableService.DEFAULT_LANGUAGE ).getStringVal();

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
        commLogId = (String)clientStateMap.get( "commLogId" );
      }
      catch( ClassCastException cce )
      {
        Long commLogIdLong = (Long)clientStateMap.get( "commLogId" );
        commLogId = commLogIdLong.toString();
      }
      if ( commLogId == null )
      {
        ActionMessages errors = new ActionMessages();
        errors.add( "errorMessage", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, "commLogId as part of clientState" ) );
        saveErrors( request, errors );
      }
    }
    catch( InvalidClientStateException e )
    {
      throw new IllegalArgumentException( "request parameter clientState was missing" );
    }
    Map clientStateParameterMap = new HashMap();
    clientStateParameterMap.put( "commLogId", commLogId );
    String queryString = ClientStateUtils.generateEncodedLink( "", "", clientStateParameterMap );

    if ( isCancelled( request ) )
    {
      return ActionUtils.forwardWithParameters( actionMapping, ActionConstants.CANCEL_FORWARD, new String[] { queryString, "method=prepareDisplay" } );
    }

    AssociationRequestCollection requestCollection = new AssociationRequestCollection();
    requestCollection.add( new CommLogAssociationRequest( CommLogAssociationRequest.ALL ) );
    requestCollection.add( new CommLogAssociationRequest( CommLogAssociationRequest.MAILING_WITH_RECIPIENTS_AND_LOCALES ) );

    CommLog commLog = getCommLogService().getCommLogById( new Long( commLogId ), requestCollection );

    User user = commLog.getUser();
    boolean emailQueued = true;

    Mailing mailing = null;
    try
    {
      mailing = commLog.getMailing();
      // BugFix 18449 Set the default user language as "en" if it is null
      if ( user.getLanguageType() == null )
      {
        user.setLanguageType( LanguageType.lookup( localeCode ) );
      }
      mailing = getMailingService().reSubmitMailing( mailing, user, null, true ); // BugFix 20310.

    }
    catch( Exception e )
    {
      log.error( "An exception occurred while resubmitting mail from commlog " + commLog.getId() );
      emailQueued = false;
    }

    if ( mailing == null )
    {
      log.error( "Error occurred while re-submitting mailing from commlog " + commLog.getId() );
      emailQueued = false;
    }

    String comment = "";
    if ( emailQueued )
    {
      comment = "Message re-sent by " + getUserService().getUserById( UserManager.getUserId() ).getNameLFMWithComma();
    }
    else
    {
      comment = "Tried to resend message by " + getUserService().getUserById( UserManager.getUserId() ).getNameLFMWithComma();
      comment += ", but there was a problem processing the message.";
    }

    Set commentsHistorySet = getUpdatedCommentsHistory( comment, commLog );
    commLog.setComments( commentsHistorySet );

    getCommLogService().saveCommLog( commLog );

    return ActionUtils.forwardWithParameters( actionMapping, ActionConstants.SUCCESS_UPDATE, new String[] { queryString, "method=prepareDisplay" } );
  }

  public void setSystemVariableService( SystemVariableService systemVariableService )
  {
    this.systemVariableService = systemVariableService;
  }

  public SystemVariableService getSystemVariableService()
  {
    return (SystemVariableService)getService( SystemVariableService.BEAN_NAME );
  }

  /**
   * This is for adding new CommLogComment to Comments history
   * 
   * @param comment
   * @param commLog
   * @return Set
   */

  private Set getUpdatedCommentsHistory( String comment, CommLog commLog )
  {
    // adding comments
    Set commentsHistorySet = new LinkedHashSet();
    if ( comment != null && !"".equals( comment ) )
    {
      CommLogComment commLogComment = new CommLogComment();
      commLogComment.setComments( comment );
      commLogComment.setCommentUser( getUserService().getUserById( UserManager.getUserId() ) );
      commLogComment.setCommLog( commLog );
      commentsHistorySet.add( commLogComment );
    }
    return commentsHistorySet;
  }

  private MailingService getMailingService()
  {
    return (MailingService)getService( MailingService.BEAN_NAME );
  }
}

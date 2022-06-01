
package com.biperf.core.ui.welcomemail;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.biperf.core.domain.message.Message;
import com.biperf.core.domain.participant.Audience;
import com.biperf.core.domain.welcomemail.WelcomeMessage;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.message.MessageService;
import com.biperf.core.service.participant.AudienceService;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.service.welcomemail.WelcomeMessageService;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.ui.message.MessageForm;
import com.biperf.core.ui.promotion.PromotionAudienceFormBean;
import com.biperf.core.ui.utils.ActionUtils;
import com.biperf.core.ui.utils.PresentationUtils;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.ui.utils.ServiceErrorStrutsUtils;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;
import com.biperf.core.utils.ClientStateUtils;
import com.biperf.core.utils.InvalidClientStateException;

/**
 * 
 * WelcomeMessageAction.
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
 * <td>Ramesh Kunasekaran</td>
 * <td>Sep 18, 2009</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 *
 */

public class WelcomeMessageAction extends BaseDispatchAction
{

  public static final String SPECIFIC_AUDIENCE = "specificAudience";

  /**
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return action forward
   */
  public ActionForward prepareCreate( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    WelcomeMessageForm welcomeMessageForm = (WelcomeMessageForm)form;
    welcomeMessageForm.setMethod( "create" );
    return mapping.findForward( ActionConstants.CREATE_FORWARD );
  } // end prepareCreate

  public ActionForward cancel( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {

    return mapping.findForward( ActionConstants.CANCEL_FORWARD );
  }

  /**
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return
   */
  public ActionForward createMessage( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    WelcomeMessageForm welcomeMessageForm = (WelcomeMessageForm)form;
    ActionMessages errors = new ActionMessages();
    WelcomeMessage welcomeMessage = null;
    errors = welcomeMessageForm.validate( mapping, request );
    if ( errors.isEmpty() )
    {
      try
      {
        welcomeMessage = welcomeMessageForm.toInsertedDomainObject();
        List audienceList = welcomeMessageForm.getAudienceList();
        if ( audienceList != null )
        {
          Set audienceSet = new HashSet();
          for ( Iterator audienceIter = audienceList.iterator(); audienceIter.hasNext(); )
          {
            PromotionAudienceFormBean promotionAudienceFormBean = (PromotionAudienceFormBean)audienceIter.next();

            audienceSet.add( promotionAudienceFormBean.getAudienceId() );
          }
          welcomeMessage.setAudienceSet( audienceSet );
        }

        getWelcomeMessageService().saveWelcomeMessage( welcomeMessage );

      }
      catch( ServiceErrorException se )
      {
        List serviceErrors = se.getServiceErrors();
        ServiceErrorStrutsUtils.convertServiceErrorsToActionErrors( serviceErrors, errors );

      }

    }

    if ( !errors.isEmpty() )
    {
      saveErrors( request, errors );
      return mapping.findForward( ActionConstants.FAIL_CREATE );
    }
    Map clientStateParameterMap = new HashMap();
    clientStateParameterMap.put( "welcomeMessageId", new Long( welcomeMessageForm.getWelcomeMessageId() ) );
    String queryString = ClientStateUtils.generateEncodedLink( "", "", clientStateParameterMap );
    String forward = ActionConstants.DISPLAY_FORWARD;
    return ActionUtils.forwardWithParameters( mapping, forward, new String[] { queryString, "method=display" } );
  }

  /**
   * Removes welcome message
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward remove( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    // logger.debug( ">>> " + METHOD_NAME );

    ActionMessages errors = new ActionMessages();

    if ( !isTokenValid( request, true ) )
    {
      errors.add( ActionConstants.ERROR_TOKEN_FAILURE, new ActionMessage( ActionConstants.ERROR_TOKEN_EXCEPTION ) );
      saveErrors( request, errors );
      return mapping.findForward( ActionConstants.FAIL_DELETE ); // EARLY EXIT
    }

    String forward = ActionConstants.DELETE_FORWARD;

    WelcomeMessageForm welcomeMessageForm = (WelcomeMessageForm)form;
    String[] deletedIds = welcomeMessageForm.getDelete();
    // BugFix 18504
    if ( deletedIds != null )
    {
      log.debug( "deletedIds " + deletedIds.length );
    }
    List list = PresentationUtils.convertStringArrayToListOfLongs( deletedIds );
    try
    {
      // getClaimFormDefinitionService().deleteClaimForms( list );
      getWelcomeMessageService().deleteWelcomeMessages( list );
    }
    catch( ServiceErrorException e )
    {
      log.debug( e );
      List serviceErrors = e.getServiceErrors();
      ServiceErrorStrutsUtils.convertServiceErrorsToActionErrors( serviceErrors, errors );
      saveErrors( request, errors );
      forward = ActionConstants.FAIL_DELETE;
    }

    /// logger.info( "<<< " + METHOD_NAME );
    return mapping.findForward( forward );
  }

  /**
   * Makes a request to the Audience builder sending it a redirect URL which will be used to forward
   * back information built in the audience.
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   * @throws Exception
   */
  public ActionForward prepareAudienceLookup( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    WelcomeMessageForm welcomeMessageForm = (WelcomeMessageForm)form;

    ActionForward returnForward = mapping.findForward( "audienceLookup" );

    String returnUrl = ActionUtils.getForwardUriWithParameters( request, returnForward, new String[] { "method=returnAudienceLookup" } );

    String queryString = "saveAudienceReturnUrl=" + returnUrl;

    // Put the form in the session to be reloaded when coming back from node lookup.
    request.getSession().setAttribute( "sessionwelcomeMessageForm", welcomeMessageForm );

    ActionForward forward = ActionUtils.forwardWithParameters( mapping, "listbuilder", new String[] { queryString } );

    return forward;
  }

  /**
   * Prepares anything necessary before showing the Update Message
   * 
   * @param actionMapping
   * @param actionForm
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward prepareUpdate( ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response )
  {
    ActionMessages errors = new ActionMessages();
    MessageForm messageForm = (MessageForm)actionForm;
    Long messageId = null;

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
        messageId = (Long)clientStateMap.get( "messageId" );
      }
      catch( ClassCastException cce )
      {
        String id = (String)clientStateMap.get( "messageId" );
        if ( id != null && id.length() > 0 )
        {
          messageId = new Long( id );
        }
      }
    }
    catch( InvalidClientStateException e )
    {
      throw new IllegalArgumentException( "request parameter clientState was missing" );
    }

    if ( messageId != null )
    {
      if ( messageId.longValue() == 0 )
      {
        errors.add( "errorMessage", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED ) );
        saveErrors( request, errors );
        return actionMapping.findForward( ActionConstants.FAIL_UPDATE );
      }
    }

    Message message = getMessageService().getMessageById( messageId );
    if ( message == null )
    {
      errors.add( "errorMessage", new ActionMessage( ServiceErrorMessageKeys.MESSAGE_NOT_FOUND ) );
      saveErrors( request, errors );
      return actionMapping.findForward( ActionConstants.FAIL_UPDATE );
    }
    // MessageForm messageForm = new MessageForm();
    messageForm.load( message );
    messageForm.setMethod( "update" );
    if ( !errors.isEmpty() )
    {
      saveErrors( request, errors );
      return actionMapping.findForward( ActionConstants.FAIL_UPDATE );
    }
    return actionMapping.findForward( ActionConstants.CREATE_FORWARD );
  } // end prepareUpdate

  /**
   * Updates a message
   * 
   * @param actionMapping
   * @param actionForm
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward update( ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response )
  {

    if ( isCancelled( request ) )
    {
      // EARLY EXIT
      return actionMapping.findForward( ActionConstants.CANCEL_FORWARD );
    }
    ActionMessages errors = new ActionMessages();
    MessageForm messageForm = (MessageForm)actionForm;

    if ( isTokenValid( request, true ) )
    {
      Message message = messageForm.toFullDomainObject();
      try
      {
        getMessageService().saveMessage( message,
                                         messageForm.getSubject(),
                                         messageForm.getHtmlMsg(),
                                         messageForm.getPlainTextMsg(),
                                         messageForm.getTextMsg(),
                                         messageForm.getStrongMailSubject(),
                                         messageForm.getStrongMailMsg() );
      }
      catch( ServiceErrorException se )
      {
        List serviceErrors = se.getServiceErrors();
        ServiceErrorStrutsUtils.convertServiceErrorsToActionErrors( serviceErrors, errors );
      }
    }
    else
    {
      errors.add( ActionConstants.ERROR_TOKEN_FAILURE, new ActionMessage( ActionConstants.ERROR_TOKEN_EXCEPTION ) );
    }

    if ( !errors.isEmpty() )
    {
      saveErrors( request, errors );
      return actionMapping.findForward( ActionConstants.FAIL_UPDATE );
    }
    return actionMapping.findForward( ActionConstants.SUCCESS_UPDATE );

  } // end update

  /**
   * Add an Audience
   * 
   * @param actionMapping
   * @param actionForm
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward addAudience( ActionMapping actionMapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    WelcomeMessageForm welcomeMessageForm = (WelcomeMessageForm)form;

    // If there was no audience selected, then return an error
    if ( welcomeMessageForm.getSelectedAudienceId() == null || welcomeMessageForm.getSelectedAudienceId().length() == 0 )
    {
      ActionMessages errors = new ActionMessages();
      errors.add( ActionConstants.ERROR_MESSAGE_PROPERTY, new ActionMessage( "promotion.errors.NO_AUDIENCE" ) );
      saveErrors( request, errors );
      return actionMapping.findForward( ActionConstants.FAIL_CREATE );
    }

    Long audienceId = new Long( welcomeMessageForm.getSelectedAudienceId() );

    addAudienceToForm( audienceId, welcomeMessageForm );
    welcomeMessageForm.setMethod( "create" );
    return actionMapping.findForward( ActionConstants.CREATE_FORWARD );
  }

  private void addAudienceToForm( Long audienceId, WelcomeMessageForm welcomeMessageForm )
  {
    // get the audience object
    Audience audience = getAudienceService().getAudienceById( audienceId, null );

    PromotionAudienceFormBean audienceFormBean = new PromotionAudienceFormBean();
    audienceFormBean.setAudienceId( audience.getId() );
    audienceFormBean.setName( audience.getName() );
    audienceFormBean.setAudienceType( audience.getAudienceType().getCode() );
    audienceFormBean.setSize( audience.getSize() );

    welcomeMessageForm.getAudienceList().add( audienceFormBean );
  }

  /**
   * Handles the return from the audience builder. This will look for the AudienceId on the request,
   * load the audience and the promotion and build a new PromotionWebRulesAudience which is set onto
   * the form in preparation to saving the webRules.
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   * @throws Exception
   */
  public ActionForward returnAudienceLookup( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    WelcomeMessageForm welcomeMessageForm = (WelcomeMessageForm)form;

    // Get the form back out of the Session to redisplay.
    WelcomeMessageForm sessionSendMessageForm = (WelcomeMessageForm)request.getSession().getAttribute( "sessionwelcomeMessageForm" );

    if ( sessionSendMessageForm != null )
    {
      try
      {
        BeanUtils.copyProperties( welcomeMessageForm, sessionSendMessageForm );

        Long audienceId = null;
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
            audienceId = (Long)clientStateMap.get( "audienceId" );
          }
          catch( ClassCastException cce )
          {
            String id = (String)clientStateMap.get( "audienceId" );
            if ( id != null && id.length() > 0 )
            {
              audienceId = new Long( id );
            }
          }
        }
        catch( InvalidClientStateException e )
        {
          throw new IllegalArgumentException( "request parameter clientState was missing" );
        }

        if ( audienceId != null )
        {
          addAudienceToForm( audienceId, welcomeMessageForm );
        }
        welcomeMessageForm.setMethod( "create" );
      }
      catch( Exception e )
      {
        e.printStackTrace();
      }

    }
    request.getSession().removeAttribute( "sessionwelcomeMessageForm" );

    return mapping.findForward( ActionConstants.CREATE_FORWARD );
  }

  /**
   * removes any audiences selected
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */

  public ActionForward removeAudience( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    ActionForward forward = mapping.findForward( ActionConstants.CREATE_FORWARD );

    WelcomeMessageForm welcomeMessageForm = (WelcomeMessageForm)form;

    for ( Iterator iter = welcomeMessageForm.getAudienceList().iterator(); iter.hasNext(); )
    {
      PromotionAudienceFormBean audienceFormBean = (PromotionAudienceFormBean)iter.next();
      if ( audienceFormBean.isRemoved() )
      {
        iter.remove();
      }
    }

    return forward;
  }

  private AudienceService getAudienceService()
  {
    return (AudienceService)getService( AudienceService.BEAN_NAME );
  }

  /**
   * Retrieves a WelcomeMessage Service 
   * 
   * @return WelcomeMessageService
   */
  private WelcomeMessageService getWelcomeMessageService()
  {
    return (WelcomeMessageService)getService( WelcomeMessageService.BEAN_NAME );
  } // end getWelcomeMessageService

  /**
   * Returns the message service.
   * 
   * @return a reference to the message service.
   */
  private MessageService getMessageService()
  {
    return (MessageService)getService( MessageService.BEAN_NAME );
  }

}

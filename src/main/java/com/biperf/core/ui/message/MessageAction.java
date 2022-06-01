/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/message/MessageAction.java,v $
 */

package com.biperf.core.ui.message;

import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.biperf.core.domain.enums.MessageStatusType;
import com.biperf.core.domain.enums.MessageType;
import com.biperf.core.domain.message.Message;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.message.MessageService;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.ui.utils.ServiceErrorStrutsUtils;
import com.objectpartners.cms.util.CmsResourceBundle;
import com.objectpartners.cms.util.CmsUtil;
import com.objectpartners.cms.util.ContentReaderManager;

/**
 * MessageAction.
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
 * <td>robinsra</td>
 * <td>Sep 2, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class MessageAction extends BaseDispatchAction
{

  /**
   * Prepares anything necessary before displaying the create screen.
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward prepareCreate( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    MessageForm messageForm = (MessageForm)form;

    // default Status to active
    messageForm.setStatusCode( MessageStatusType.lookup( MessageStatusType.ACTIVE ).getCode() );

    // default Message Type to general
    messageForm.setMessageTypeCode( MessageType.lookup( MessageType.GENERAL ).getCode() );

    // get the actionForward to display the create pages.
    messageForm.setMethod( "create" );
    return mapping.findForward( ActionConstants.CREATE_FORWARD );
  } // end prepareCreate

  /**
   * Creates a new message
   * 
   * @param actionMapping
   * @param actionForm
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward create( ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response )
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
      // -----------------------------
      // Create the Message
      // -----------------------------
      try
      {
        Message message = messageForm.toInsertedDomainObject();
        getMessageService().saveMessage( message,
                                         messageForm.getSubject(),
                                         messageForm.getHtmlMsg(),
                                         messageForm.getPlainTextMsg(),
                                         messageForm.getTextMsg(),
                                         messageForm.getStrongMailSubject(),
                                         messageForm.getStrongMailMsg() );
        if ( !messageForm.getTranslatedLocale().equals( ContentReaderManager.getText( "system.general", "SELECT_ONE" ) ) )
        {
          Locale locale = CmsUtil.getLocale( messageForm.getTranslatedLocale() );
          getMessageService().saveMessageCmText( message,
                                                 messageForm.getTranslatedSubject(),
                                                 messageForm.getTranslatedHtmlMsg(),
                                                 messageForm.getTranslatedPlainTextMsg(),
                                                 messageForm.getTranslatedTextMsg(),
                                                 messageForm.getStrongMailSubject(),
                                                 messageForm.getStrongMailMsg(),
                                                 locale );
        }
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
      return actionMapping.findForward( ActionConstants.FAIL_CREATE );
    }
    return actionMapping.findForward( ActionConstants.SUCCESS_CREATE );

  } // end create

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
    Long messageId = new Long( messageForm.getMessageId() );
    if ( messageId.longValue() == 0 )
    {
      errors.add( "errorMessage", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED ) );
      saveErrors( request, errors );
      return actionMapping.findForward( ActionConstants.FAIL_UPDATE );
    }

    Message message = getMessageService().getMessageById( messageId );
    if ( message == null )
    {
      errors.add( "errorMessage", new ActionMessage( ServiceErrorMessageKeys.MESSAGE_NOT_FOUND ) );
      saveErrors( request, errors );
      return actionMapping.findForward( ActionConstants.FAIL_UPDATE );
    }

    messageForm.load( message );
    messageForm.setMethod( "update" );

    if ( !errors.isEmpty() )
    {
      saveErrors( request, errors );
      return actionMapping.findForward( ActionConstants.FAIL_UPDATE );
    }
    request.setAttribute( "disable", "true" );
    request.setAttribute( "editErrorMessageDisplay", CmsResourceBundle.getCmsBundle().getString( "admin.message.details", "EDIT_THROUGH_CONTENT_MANAGER" ) + " : " + message.getCmAssetCode() );
    return actionMapping.findForward( ActionConstants.UPDATE_FORWARD );
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
        if ( !messageForm.getTranslatedLocale().equals( ContentReaderManager.getText( "system.general", "SELECT_ONE" ) ) )
        {
          Locale locale = CmsUtil.getLocale( messageForm.getTranslatedLocale() );
          getMessageService().saveMessageCmText( message,
                                                 messageForm.getTranslatedSubject(),
                                                 messageForm.getTranslatedHtmlMsg(),
                                                 messageForm.getTranslatedPlainTextMsg(),
                                                 messageForm.getTranslatedTextMsg(),
                                                 messageForm.getTranslatedStrongMailSubject(),
                                                 messageForm.getTranslatedStrongMailMsg(),
                                                 locale );
        }
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
   * Retrieves a Message Service
   * 
   * @return MessageService
   */
  private MessageService getMessageService()
  {
    return (MessageService)getService( MessageService.BEAN_NAME );
  } // end getMessageService

}

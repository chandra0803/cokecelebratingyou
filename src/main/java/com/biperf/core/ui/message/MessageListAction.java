/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/message/MessageListAction.java,v $
 */

package com.biperf.core.ui.message;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.domain.enums.MessageModuleType;
import com.biperf.core.domain.enums.MessageStatusType;
import com.biperf.core.domain.message.Message;
import com.biperf.core.service.message.MessageService;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.ui.utils.PresentationUtils;
import com.biperf.util.StringUtils;

/**
 * MessageListAction.
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
 * <td>Sep 7, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class MessageListAction extends BaseDispatchAction
{
  /**
   * unspecified will display list
   * 
   * @param actionMapping
   * @param actionForm
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward unspecified( ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response )
  {
    return display( actionMapping, actionForm, request, response );
  } // end unspecified

  /**
   * Prepares anything necessary before showing the Update Message
   * 
   * @param actionMapping
   * @param actionForm
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward display( ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response )
  {
    MessageListForm messageListForm = (MessageListForm)actionForm;
    String statusCode = messageListForm.getStatusCode();

    // if status code is not defined, display the Active
    if ( statusCode == null || statusCode.equals( "" ) )
    {
      messageListForm.setStatusCode( MessageStatusType.ACTIVE );
    }

    List<Message> messageList = new ArrayList<Message>();
    if ( messageListForm.getStatusCode().equalsIgnoreCase( MessageStatusType.ACTIVE ) && !StringUtils.isEmpty( messageListForm.getModuleCode() ) )
    {
      if ( messageListForm.getModuleCode().equalsIgnoreCase( "all" ) )
      {
        messageList = getMessageService().getAllMessagesByStatus( messageListForm.getStatusCode() );
      }
      else
      {
        messageList = getMessageService().getAllActiveMessagesByModuleType( messageListForm.getModuleCode() );
      }

    }
    else if ( messageListForm.getStatusCode().equalsIgnoreCase( MessageStatusType.INACTIVE ) )
    {
      messageList = getMessageService().getAllMessagesByStatus( messageListForm.getStatusCode() );
    }

    request.setAttribute( "moduleList", MessageModuleType.getList() );
    request.setAttribute( "messageList", messageList );
    request.setAttribute( "pageSize", new Integer( PresentationUtils.getDisplayTablePageSize( messageList.size() ) ) );

    return actionMapping.findForward( ActionConstants.SUCCESS_FORWARD );
  } // end display

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

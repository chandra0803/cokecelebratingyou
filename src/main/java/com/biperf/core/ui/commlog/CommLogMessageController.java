/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/commlog/CommLogMessageController.java,v $
 *
 */

package com.biperf.core.ui.commlog;

import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.domain.commlog.CommLog;
import com.biperf.core.domain.user.User;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.commlog.CommLogAssociationRequest;
import com.biperf.core.service.commlog.CommLogService;
import com.biperf.core.ui.BaseController;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;
import com.biperf.core.utils.InvalidClientStateException;

/**
 * MyCommLogListController <p/> <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>dunne</td>
 * <td>Nov 22, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 * @author dunne
 *
 */
public class CommLogMessageController extends BaseController
{

  public static final String VIEW_MESSAGE = "view";
  public static final String SEND_MESSAGE = "send";

  /**
   * Overridden from
   * 
   * @see com.biperf.core.ui.BaseController#execute(org.apache.struts.tiles.ComponentContext,
   *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
   *      javax.servlet.ServletContext)
   * @param componentContext
   * @param request
   * @param response
   * @param servletContext
   * @throws Exception
   */
  public void onExecute( ComponentContext componentContext, HttpServletRequest request, HttpServletResponse response, ServletContext servletContext ) throws Exception
  {
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
      String commLogId = null;
      String messageActionType = null;
      messageActionType = (String)clientStateMap.get( "actionType" );

      try
      {
        commLogId = (String)clientStateMap.get( "commLogId" );
      }
      catch( ClassCastException cce )
      {
        Long commLogIdLong = (Long)clientStateMap.get( "commLogId" );
        commLogId = commLogIdLong.toString();
      }

      if ( messageActionType == null || commLogId == null )
      {
        throw new IllegalArgumentException( "required parameter clientState commLogId or actionType was missing" );
      }

      if ( commLogId != null )
      {
        AssociationRequestCollection requestCollection = new AssociationRequestCollection();
        requestCollection.add( new CommLogAssociationRequest( CommLogAssociationRequest.ALL ) );
        CommLog commLog = getCommLogService().getCommLogById( new Long( commLogId ), requestCollection );
        request.setAttribute( "message", commLog.getPlainMessage() );
        request.setAttribute( "actionType", messageActionType );
        User user = commLog.getUser();
        if ( user != null )
        // for displaying name
        {
          if ( user.getId() != null )
          {
            request.setAttribute( "displayNameUserId", user.getId().toString() );
          }
        }
      }

    }
    catch( InvalidClientStateException e )
    {
      throw new IllegalArgumentException( "request parameter clientState was missing" );
    }
  }

  private CommLogService getCommLogService() throws Exception
  {
    return (CommLogService)getService( CommLogService.BEAN_NAME );
  }

}

/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javaui/com/biperf/core/ui/commlog/CommLogListController.java,v $
 */

package com.biperf.core.ui.commlog;

import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.commlog.CommLogAssociationRequest;
import com.biperf.core.service.commlog.CommLogService;
import com.biperf.core.ui.BaseController;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;
import com.biperf.core.utils.InvalidClientStateException;

/**
 * UserPhoneListController.
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
 * <td>Ashok</td>
 * <td>Nov 18, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class CommLogListController extends BaseController
{

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
      String userId = (String)clientStateMap.get( "userId" );
      if ( userId != null )
      {
        AssociationRequestCollection requestCollection = new AssociationRequestCollection();
        requestCollection.add( new CommLogAssociationRequest( CommLogAssociationRequest.ALL ) );
        List userCommLogs = getCommLogService().getCommLogsForUser( new Long( userId ), requestCollection );
        request.setAttribute( "commLogList", userCommLogs );

        // for displaying name
        request.setAttribute( "displayNameUserId", userId );
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

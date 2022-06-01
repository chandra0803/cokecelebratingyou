/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javaui/com/biperf/core/ui/commlog/MyCommLogListController.java,v $
 *
 */

package com.biperf.core.ui.commlog;

import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.commlog.CommLogAssociationRequest;
import com.biperf.core.service.commlog.CommLogService;
import com.biperf.core.ui.BaseController;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.UserManager;

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
public class MyCommLogListController extends BaseController
{

  public static final String OPEN_LIST = "open";
  public static final String ESCALATED_LIST = "escalated";

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
    String listType = RequestUtils.getRequiredAttributeString( request, "commLogListType" );
    AssociationRequestCollection requestCollection = new AssociationRequestCollection();
    requestCollection.add( new CommLogAssociationRequest( CommLogAssociationRequest.ALL ) );

    if ( listType.equals( OPEN_LIST ) )
    {
      List commLogList = getCommLogService().getOpenCommLogsAssignedToUser( UserManager.getUserId(), requestCollection );
      request.setAttribute( "commLogList", commLogList );
    }
    else if ( listType.equals( ESCALATED_LIST ) )
    {
      List commLogList = getCommLogService().getEscalatedCommLogsAssignedToUser( UserManager.getUserId(), requestCollection );
      request.setAttribute( "commLogList", commLogList );
    }

  }

  private CommLogService getCommLogService() throws Exception
  {
    return (CommLogService)getService( CommLogService.BEAN_NAME );
  }

}

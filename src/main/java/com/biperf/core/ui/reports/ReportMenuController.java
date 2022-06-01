/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/reports/ReportMenuController.java,v $
 *
 */

package com.biperf.core.ui.reports;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.ui.BaseController;
import com.biperf.core.utils.ClientStateUtils;

/**
 * ReportDashboardController.
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
 * <td>arasi</td>
 * <td>May 19, 2009</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 */
public class ReportMenuController extends BaseController
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
    boolean largeAudience = getSystemVariableService().getPropertyByName( SystemVariableService.REPORT_LARGE_AUDIENCE ).getBooleanVal();
    request.setAttribute( "largeAudience", largeAudience );
    if ( ClientStateUtils.getClientStateMap( request ) != null )
    {
      String isEngagement = ClientStateUtils.getParameterValue( request, ClientStateUtils.getClientStateMap( request ), "isEngagement" );
      if ( "true".equals( isEngagement ) )
      {
        request.setAttribute( "isEngagement", ClientStateUtils.getParameterValue( request, ClientStateUtils.getClientStateMap( request ), "isEngagement" ) );
        request.setAttribute( "fromDate", ClientStateUtils.getParameterValue( request, ClientStateUtils.getClientStateMap( request ), "fromDate" ) );
        request.setAttribute( "toDate", ClientStateUtils.getParameterValue( request, ClientStateUtils.getClientStateMap( request ), "toDate" ) );
        request.setAttribute( "reportId", ClientStateUtils.getParameterValue( request, ClientStateUtils.getClientStateMap( request ), "reportId" ) );
      }
    }

    // set the role in the scope
    request.setAttribute( "isAdmin", isAdmin() );
    request.setAttribute( "isDelegate", isDelegate() );
  }

  private SystemVariableService getSystemVariableService()
  {
    return (SystemVariableService)getService( SystemVariableService.BEAN_NAME );
  }

}

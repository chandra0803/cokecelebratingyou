
package com.biperf.core.ui.maincontent;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.ui.BaseController;

/**
 * GoogleAnalyticsWebtrackingController.
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
 * <td>Jan 18, 2010</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class GoogleAnalyticsWebtrackingController extends BaseController
{
  /**   
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
    String googleAnalyticsAccount = getSystemVariableService().getPropertyByName( SystemVariableService.GOOGLE_ANALYTICS_ACCOUNT ).getStringVal();
    if ( StringUtils.isEmpty( googleAnalyticsAccount ) )
    {
      googleAnalyticsAccount = "none";
    }
    request.setAttribute( "googleAnalyticsAccount", googleAnalyticsAccount );
  }

  private SystemVariableService getSystemVariableService()
  {
    return (SystemVariableService)getService( SystemVariableService.BEAN_NAME );
  }
}

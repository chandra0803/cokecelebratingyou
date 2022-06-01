
package com.biperf.core.ui.welcomemail;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.service.welcomemail.WelcomeMessageService;
import com.biperf.core.ui.BaseController;

/**
 * 
 * WelcomeMessageController.
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
public class WelcomeMessageController extends BaseController
{

  /**
   * 
   * Overridden from @see com.biperf.core.ui.BaseController#onExecute(org.apache.struts.tiles.ComponentContext, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, javax.servlet.ServletContext)
   * @param tileContext
   * @param request
   * @param response
   * @param servletContext
   * @throws Exception
   */
  public void onExecute( ComponentContext tileContext, HttpServletRequest request, HttpServletResponse response, ServletContext servletContext ) throws Exception
  {
    request.setAttribute( "welcomeMessageList", getWelcomeMessageService().getAllWelcomeMessages() );
  }

  /**
   * Returns the Welcome message service.
   * 
   * @return a reference to the Welcome message service.
   */
  private WelcomeMessageService getWelcomeMessageService()
  {
    return (WelcomeMessageService)getService( WelcomeMessageService.BEAN_NAME );
  }

}
